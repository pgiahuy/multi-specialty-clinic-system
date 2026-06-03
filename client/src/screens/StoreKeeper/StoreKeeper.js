import { useEffect, useMemo, useState } from "react";
import Footer from "../../components/Footer";
import Header from "../../components/Header";
import { clinicApis } from "../../configs/Apis";
import { Button, Container, Form, InputGroup, Table, ButtonGroup, Row, Col } from "react-bootstrap";

const StoreKeeper = () => {
    const [activeTab, setActiveTab] = useState("medicines");

    const [medicines, setMedicines] = useState([]);
    const [medicineLoading, setMedicineLoading] = useState(false);
    const [medicineKw, setMedicineKw] = useState("");
    const [medicinePage, setMedicinePage] = useState(1);
    const [medicinePageSize, setMedicinePageSize] = useState(10);
    const [medicineTotal, setMedicineTotal] = useState(0);

    const [batches, setBatches] = useState([]);
    const [batchLoading, setBatchLoading] = useState(false);
    const [batchKw, setBatchKw] = useState("");
    const [batchFromImport, setBatchFromImport] = useState("");
    const [batchToImport, setBatchToImport] = useState("");
    const [batchFromExpiry, setBatchFromExpiry] = useState("");
    const [batchToExpiry, setBatchToExpiry] = useState("");
    const [batchPage, setBatchPage] = useState(1);
    const [batchPageSize, setBatchPageSize] = useState(10);
    const [batchTotal, setBatchTotal] = useState(0);
    const [batchSearchKey, setBatchSearchKey] = useState(0);

    const alertStockThreshold = 10;
    const alertExpiryDays = 30;

    const medicineTotalPages = useMemo(() => Math.max(1, Math.ceil(medicineTotal / medicinePageSize)), [medicineTotal, medicinePageSize]);
    const batchTotalPages = useMemo(() => Math.max(1, Math.ceil(batchTotal / batchPageSize)), [batchTotal, batchPageSize]);

    const loadMedicines = async () => {
        try {
            setMedicineLoading(true);
            const params = {
                kw: medicineKw,
                page: medicinePage,
                pageSize: medicinePageSize,
            };
            const [listRes, countRes] = await Promise.all([
                clinicApis.getMedicines(params),
                clinicApis.getMedicinesCount(params),
            ]);
            setMedicines(Array.isArray(listRes.data) ? listRes.data : []);
            setMedicineTotal(Number(countRes.data || 0));
        } catch (error) {
            console.error("Lỗi khi tải danh sách thuốc:", error);
            setMedicines([]);
            setMedicineTotal(0);
        } finally {
            setMedicineLoading(false);
        }
    };

    const loadBatches = async () => {
        try {
            setBatchLoading(true);
            const params = {
                kw: batchKw,
                fromImport: batchFromImport,
                toImport: batchToImport,
                fromExpiry: batchFromExpiry,
                toExpiry: batchToExpiry,
                page: batchPage,
                pageSize: batchPageSize,
            };
            const [listRes, countRes] = await Promise.all([
                clinicApis.getMedicineBatchs(params),
                clinicApis.getMedicineBatchsCount(params),
            ]);
            setBatches(Array.isArray(listRes.data) ? listRes.data : []);
            setBatchTotal(Number(countRes.data || 0));
        } catch (error) {
            console.error("Lỗi khi tải danh sách lô thuốc:", error);
            setBatches([]);
            setBatchTotal(0);
        } finally {
            setBatchLoading(false);
        }
    };

    const clearMedicineFilters = () => {
        setMedicineKw("");
        setMedicinePage(1);
    };

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
        const parsed = new Date(raw);
        return isNaN(parsed.getTime()) ? null : parsed;
    };

    const clearBatchFilters = () => {
        setBatchKw("");
        setBatchFromImport("");
        setBatchToImport("");
        setBatchFromExpiry("");
        setBatchToExpiry("");
        setBatchPage(1);
        setBatchSearchKey(prev => prev + 1);
    };

    const searchBatches = () => {
        setBatchPage(1);
        setBatchSearchKey(prev => prev + 1);
    };

    useEffect(() => {
        if (activeTab !== "medicines") {
            return;
        }
        const timer = setTimeout(() => {
            loadMedicines();
        }, 500);
        return () => clearTimeout(timer);
    }, [activeTab, medicineKw, medicinePage, medicinePageSize]);

    useEffect(() => {
        loadBatches();
    }, []);

    useEffect(() => {
        if (activeTab !== "batches") {
            return;
        }
        const timer = setTimeout(() => {
            loadBatches();
        }, 500);
        return () => clearTimeout(timer);
    }, [activeTab, batchSearchKey, batchPage, batchPageSize]);

    const lowStockAlerts = useMemo(() => {
        return medicines
            .filter((m) => typeof m.totalStock === 'number' && m.totalStock <= alertStockThreshold)
            .map((m) => ({
                type: 'low-stock',
                id: `low-${m.id}`,
                title: `Thuốc ${m.name || m.code || 'Không xác định'} còn ${m.totalStock} đơn vị`,
                description: `Tồn kho hiện tại ${m.totalStock} <= ngưỡng ${alertStockThreshold}`,
            }));
    }, [medicines]);

    const expiryAlerts = useMemo(() => {
        const now = new Date();
        return batches
            .map((batch) => {
                const expiryDate = parseExpiryDate(batch.expiryDate);
                if (!expiryDate) return null;
                const diffDays = Math.ceil((expiryDate - now) / (1000 * 60 * 60 * 24));
                if (diffDays >= 0 && diffDays <= alertExpiryDays) {
                    return {
                        type: 'expiry',
                        id: `expiry-${batch.id || batch.batchCode}`,
                        title: `Lô ${batch.batchCode || 'Không xác định'} sắp hết hạn`,
                        description: `Hạn dùng còn ${diffDays} ngày` + (batch.medicineName ? ` - ${batch.medicineName}` : ''),
                    };
                }
                return null;
            })
            .filter(Boolean);
    }, [batches]);

    const alertItems = useMemo(() => [...lowStockAlerts, ...expiryAlerts], [lowStockAlerts, expiryAlerts]);

    return (
        <div className="d-flex flex-column min-vh-100">
            <Header />
            <Container className="py-4" style={{ width: '95%' }}>

                <div className="d-flex justify-content-end align-items-center mb-4">
                    <ButtonGroup>
                        <Button variant={activeTab === 'medicines' ? 'primary' : 'outline-primary'} onClick={() => setActiveTab('medicines')}>
                            Thuốc
                        </Button>
                        <Button variant={activeTab === 'batches' ? 'primary' : 'outline-primary'} onClick={() => setActiveTab('batches')}>
                            Lô thuốc
                        </Button>
                    </ButtonGroup>
                </div>

                {activeTab === 'medicines' ? (
                    <>
                        <Row className="align-items-end mb-3">
                            <Col md={5} sm={12} className="mb-2">
                                <Form.Label>Tìm thuốc</Form.Label>
                                <InputGroup>
                                    <Form.Control placeholder="Tên hoặc mã thuốc" value={medicineKw} onChange={(e) => setMedicineKw(e.target.value)} />
                                    <Button variant="outline-secondary" onClick={clearMedicineFilters}>Xóa bộ lọc</Button>
                                </InputGroup>
                            </Col>
                            <Col md={3} sm={6} className="mb-2">
                                <Form.Group>
                                    <Form.Label>Số dòng/trang</Form.Label>
                                    <Form.Select value={medicinePageSize} onChange={(e) => { setMedicinePageSize(Number(e.target.value)); setMedicinePage(1); }}>
                                        {[5, 10, 20, 50].map(n => <option key={n} value={n}>{n}</option>)}
                                    </Form.Select>
                                </Form.Group>
                            </Col>
                            <Col md={4} sm={6} className="text-end mb-2">
                                <div className="text-muted">Tổng: {medicineTotal} thuốc</div>
                            </Col>
                        </Row>

                        <div className="table-responsive">
                            <Table hover>
                                <thead>
                                    <tr>
                                        <th>STT</th>
                                        <th>Mã</th>
                                        <th>Tên thuốc</th>
                                        <th>Đơn vị</th>
                                        <th>Giá</th>
                                        <th>Tồn kho</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    {medicineLoading ? (
                                        <tr><td colSpan={6} className="text-center py-4">Đang tải...</td></tr>
                                    ) : medicines.length === 0 ? (
                                        <tr><td colSpan={6} className="text-center py-4 text-muted">Không tìm thấy thuốc.</td></tr>
                                    ) : medicines.map((m, index) => (
                                        <tr key={m.id || index}>
                                            <td>{(medicinePage - 1) * medicinePageSize + index + 1}</td>
                                            <td>{m.code || '-'}</td>
                                            <td>{m.name || '-'}</td>
                                            <td>{m.unit || '-'}</td>
                                            <td>{m.price != null ? m.price : '-'}</td>
                                            <td>{m.totalStock != null ? m.totalStock : '-'}</td>
                                        </tr>
                                    ))}
                                </tbody>
                            </Table>
                        </div>
                        <div className="d-flex justify-content-between align-items-center mt-3">
                            <div>Trang {medicinePage} / {medicineTotalPages}</div>
                            <div>
                                <Button variant="outline-secondary" size="sm" className="me-2" disabled={medicinePage <= 1} onClick={() => setMedicinePage(prev => Math.max(1, prev - 1))}>Trước</Button>
                                <Button variant="outline-secondary" size="sm" disabled={medicinePage >= medicineTotalPages} onClick={() => setMedicinePage(prev => Math.min(medicineTotalPages, prev + 1))}>Sau</Button>
                            </div>
                        </div>
                    </>
                ) : (
                    <>
                        <Row className="g-3 mb-2">
                            <Col md={4} sm={12}>
                                <Form.Label>Tìm lô / thuốc</Form.Label>
                                <Form.Control placeholder="Mã lô / tên thuốc" value={batchKw} onChange={(e) => setBatchKw(e.target.value)} />
                            </Col>
                            <Col md={4} sm={12}>
                                <Row className="g-2">
                                    <Col>
                                        <Form.Label>Nhập từ</Form.Label>
                                        <Form.Control type="date" value={batchFromImport} onChange={(e) => setBatchFromImport(e.target.value)} />
                                    </Col>
                                    <Col>
                                        <Form.Label>đến</Form.Label>
                                        <Form.Control type="date" value={batchToImport} onChange={(e) => setBatchToImport(e.target.value)} />
                                    </Col>
                                </Row>
                            </Col>
                            <Col md={4} sm={12}>
                                <Row className="g-2">
                                    <Col>
                                        <Form.Label>Hạn dùng từ</Form.Label>
                                        <Form.Control type="date" value={batchFromExpiry} onChange={(e) => setBatchFromExpiry(e.target.value)} />
                                    </Col>
                                    <Col>
                                        <Form.Label>đến</Form.Label>
                                        <Form.Control type="date" value={batchToExpiry} onChange={(e) => setBatchToExpiry(e.target.value)} />
                                    </Col>
                                </Row>
                            </Col>
                        </Row>
                        <Row className="mb-3 justify-content-end">
                            <Col xs="auto" className="d-flex gap-2 justify-content-end">
                                <Button variant="primary" onClick={searchBatches} style={{ minWidth: '120px' }}>Tìm</Button>
                                <Button variant="outline-secondary" onClick={clearBatchFilters} style={{ minWidth: '120px' }}>Xóa bộ lọc</Button>
                            </Col>
                        </Row>
                        <Row className="align-items-end mb-3">
                            <Col md={3} sm={6}>
                                <Form.Group>
                                    <Form.Label>Số dòng/trang</Form.Label>
                                    <Form.Select value={batchPageSize} onChange={(e) => { setBatchPageSize(Number(e.target.value)); setBatchPage(1); }}>
                                        {[5, 10, 20, 50].map(n => <option key={n} value={n}>{n}</option>)}
                                    </Form.Select>
                                </Form.Group>
                            </Col>
                            <Col md={3} sm={6} className="text-end">
                                <div className="text-muted">Tổng: {batchTotal} lô</div>
                            </Col>
                        </Row>
                        <div className="table-responsive">
                            <Table hover>
                                <thead>
                                    <tr>
                                        <th>STT</th>
                                        <th>Mã lô</th>
                                        <th>Thuốc</th>
                                        <th>Số lượng</th>
                                        <th>Ngày nhập</th>
                                        <th>Hạn dùng</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    {batchLoading ? (
                                        <tr><td colSpan={6} className="text-center py-4">Đang tải...</td></tr>
                                    ) : batches.length === 0 ? (
                                        <tr><td colSpan={6} className="text-center py-4 text-muted">Không tìm thấy lô thuốc.</td></tr>
                                    ) : batches.map((batch, index) => (
                                        <tr key={batch.id || index}>
                                            <td>{(batchPage - 1) * batchPageSize + index + 1}</td>
                                            <td>{batch.batchCode || '-'}</td>
                                            <td>{batch.medicineName ? `${batch.medicineName} (${batch.medicineCode || '-'})` : '-'}</td>
                                            <td>{batch.quantity != null ? batch.quantity : '-'}</td>
                                            <td>{batch.importDate || '-'}</td>
                                            <td>{batch.expiryDate || '-'}</td>
                                        </tr>
                                    ))}
                                </tbody>
                            </Table>
                        </div>
                        <div className="d-flex justify-content-between align-items-center mt-3">
                            <div>Trang {batchPage} / {batchTotalPages}</div>
                            <div>
                                <Button variant="outline-secondary" size="sm" className="me-2" disabled={batchPage <= 1} onClick={() => setBatchPage(prev => Math.max(1, prev - 1))}>Trước</Button>
                                <Button variant="outline-secondary" size="sm" disabled={batchPage >= batchTotalPages} onClick={() => setBatchPage(prev => Math.min(batchTotalPages, prev + 1))}>Sau</Button>
                            </div>
                        </div>
                    </>
                )}
            </Container>
            <Footer />
        </div>
    );
};

export default StoreKeeper;
