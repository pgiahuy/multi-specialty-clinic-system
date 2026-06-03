import { useCallback, useEffect, useMemo, useState, memo } from "react";
import { Alert, Button, Container, Form, Row, Col } from "react-bootstrap";
import { useNavigate } from "react-router-dom";
import { clinicApis } from "../../configs/Apis";
import Header from "../../components/Header";
import Footer from "../../components/Footer";
import ExpiryAlertsSection from "./component/ExpiryAlertsSection";
import LowStockAlertsSection from "./component/LowStockAlertsSection";

const StoreKeeperHome = () => {
    const navigate = useNavigate();
    const [lowStockItems, setLowStockItems] = useState([]);
    const [expiringBatches, setExpiringBatches] = useState([]);
    const [loading, setLoading] = useState(true);


    const [inputLowStockThreshold, setInputLowStockThreshold] = useState(100);
    const [inputExpiryDays, setInputExpiryDays] = useState(30);


    const [activeThreshold, setActiveThreshold] = useState(100);
    const [activeExpiryDays, setActiveExpiryDays] = useState(30);

    const [lowStockPage, setLowStockPage] = useState(1);
    const [expiryPage, setExpiryPage] = useState(1);
    const alertPageSize = 10;

    const [deletingBatchId, setDeletingBatchId] = useState(null);

    const loadAlerts = useCallback(async (threshold, days, lowPage, expiryPageNum) => {
        setLoading(true);
        try {
            const response = await clinicApis.getStorekeeperAlerts({
                lowStockThreshold: threshold,
                expiryDays: days,
                lowStockPage: lowPage,
                lowStockPageSize: alertPageSize,
                expiryPage: expiryPageNum,
                expiryPageSize: alertPageSize,
            });
            const data = response.data || {};
            setLowStockItems(Array.isArray(data.lowStockMedicines) ? data.lowStockMedicines : []);
            setExpiringBatches(Array.isArray(data.expiringBatches) ? data.expiringBatches : []);
        } catch (error) {
            console.error("Lỗi khi tải dữ liệu cảnh báo kho thuốc:", error);
            setLowStockItems([]);
            setExpiringBatches([]);
        } finally {
            setLoading(false);
        }
    }, [alertPageSize]);


    useEffect(() => {
        loadAlerts(activeThreshold, activeExpiryDays, lowStockPage, expiryPage);
    }, [activeThreshold, activeExpiryDays, lowStockPage, expiryPage, loadAlerts]);


    const handleApplyFilters = (e) => {
        e.preventDefault();
        setLowStockPage(1);
        setExpiryPage(1);
        setActiveThreshold(inputLowStockThreshold);
        setActiveExpiryDays(inputExpiryDays);
    };

    const handleDeleteBatch = useCallback(async (batchId) => {
        if (!batchId) return;
        setDeletingBatchId(batchId);
        try {
            await clinicApis.deleteMedicineBatch(batchId);

            await loadAlerts(activeThreshold, activeExpiryDays, lowStockPage, expiryPage);
        } catch (error) {
            console.error("Lỗi khi xoá lô thuốc hết hạn:", error);
        } finally {
            setDeletingBatchId(null);
        }
    }, [loadAlerts, activeThreshold, activeExpiryDays, lowStockPage, expiryPage]);

    const parseExpiryDate = (value) => {
        if (!value && value !== 0) return null;
        const raw = String(value).trim();
        if (/^[0-9]{8}$/.test(raw)) {
            return new Date(`${raw.slice(0, 4)}-${raw.slice(4, 6)}-${raw.slice(6, 8)}`);
        }
        if (/^[0-9]{7}$/.test(raw)) {
            const year = raw.slice(0, 4);
            const month = raw.slice(4, 5);
            const day = raw.slice(5);
            return new Date(`${year}-${month.padStart(2, '0')}-${day.padStart(2, '0')}`);
        }
        if (/^[0-9]{2}\/[0-9]{2}\/[0-9]{4}$/.test(raw)) {
            const [day, month, year] = raw.split('/');
            return new Date(`${year}-${month}-${day}`);
        }
        const parsed = new Date(raw);
        return isNaN(parsed.getTime()) ? null : parsed;
    };

    const lowStockAlerts = useMemo(() => {
        return lowStockItems.map((m) => ({
            type: 'low-stock',
            id: `low-${m.id}`,
            title: `Thuốc ${m.name || m.code || 'Không xác định'} còn ${m.totalStock} đơn vị`,
            description: "",
        }));
    }, [lowStockItems]);

    const expiryAlerts = useMemo(() => {
        const now = new Date();
        return expiringBatches
            .map((batch) => {
                const expiryDate = parseExpiryDate(batch.expiryDate);
                if (!expiryDate) return null;
                const diffDays = Math.ceil((expiryDate - now) / (1000 * 60 * 60 * 24));

                if (diffDays <= activeExpiryDays) {
                    const isExpired = diffDays < 0;
                    const title = isExpired
                        ? `Lô ${batch.batchCode || 'Không xác định'} - Thuốc ${batch.medicineCode || 'Không xác định'} đã hết hạn`
                        : `Lô ${batch.batchCode || 'Không xác định'} - Thuốc ${batch.medicineCode || 'Không xác định'} sắp hết hạn`;
                    const description = isExpired
                        ? `Hết hạn ${Math.abs(diffDays)} ngày trước`
                        : `Hạn dùng còn ${diffDays} ngày`;
                    return {
                        type: 'expiry',
                        id: `expiry-${batch.id || batch.batchCode}`,
                        batchId: batch.id,
                        isExpired,
                        title,
                        description,
                    };
                }
                return null;
            })
            .filter(Boolean);
    }, [expiringBatches, activeExpiryDays]);

    return (
        <div className="d-flex flex-column min-vh-100">
            <Header />
            <div className="mb-3 mt-4" style={{ width: '90%', margin: '0 auto' }}>

                <Form onSubmit={handleApplyFilters} className="mb-4 p-3 border rounded bg-light h-100" style={{ maxWidth: '850px', margin: '0 auto' }}>
                    <Row className="g-3 align-items-end">
                        <Col sm={6} md={3}>
                            <Form.Group controlId="expiryDays">
                                <Form.Label>Mức cảnh báo hạn dùng</Form.Label>
                                <Form.Control
                                    type="number"
                                    min={1}
                                    value={inputExpiryDays}
                                    onChange={(e) => setInputExpiryDays(Number(e.target.value))}
                                />
                            </Form.Group>
                        </Col>
                        <Col sm={6} md={3}>
                            <Form.Group controlId="lowStockThreshold">
                                <Form.Label>Mức cảnh báo tồn kho</Form.Label>
                                <Form.Control
                                    type="number"
                                    min={0}
                                    value={inputLowStockThreshold}
                                    onChange={(e) => setInputLowStockThreshold(Number(e.target.value))}
                                />
                            </Form.Group>
                        </Col>
                        <Col sm={12} md={5} className="d-flex justify-content-center gap-4">
                            <Button
                                type="submit"
                                variant="primary"
                                disabled={loading}
                            >
                                Cập nhật cảnh báo
                            </Button>
                            <Button variant="outline-primary" onClick={() => navigate('/storekeeper')}>
                                Quản lý kho
                            </Button>
                        </Col>
                    </Row>
                </Form>

                <Row className="g-3">
                    <Col md={6}>
                        <ExpiryAlertsSection
                            loading={loading}
                            expiryAlerts={expiryAlerts}
                            expiryPage={expiryPage}
                            setExpiryPage={setExpiryPage}
                            alertPageSize={alertPageSize}
                            deletingBatchId={deletingBatchId}
                            onDeleteBatch={handleDeleteBatch}
                        />
                    </Col>
                    <Col md={6}>
                        <LowStockAlertsSection
                            loading={loading}
                            lowStockAlerts={lowStockAlerts}
                            lowStockPage={lowStockPage}
                            setLowStockPage={setLowStockPage}
                            alertPageSize={alertPageSize}
                        />
                    </Col>
                </Row>
            </div>
        </div>
    );
};





export default StoreKeeperHome;