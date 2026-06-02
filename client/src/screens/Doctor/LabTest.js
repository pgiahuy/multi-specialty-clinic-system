import { useEffect, useRef, useState } from "react";
import { Card, Col, Container, Row, Table, Spinner, Form, Button } from "react-bootstrap";
import { authApis, TEST_ENDPOINTS } from "../../configs/Apis";
import Header from "../../components/Header";
import Footer from "../../components/Footer";
import FloatAlert from "../../components/FloatAlert";
import { useSearchParams } from "react-router-dom";

const LabTest = () => {
    const [labResults, setLabResults] = useState([]);
    const [selectedLabResult, setSelectedLabResult] = useState(null);
    const [loading, setLoading] = useState(false);
    const [saving, setSaving] = useState(false);
    const [saveError, setSaveError] = useState(null);
    const [successMessage, setSuccessMessage] = useState(null);
    const [statusFilter, setStatusFilter] = useState('all');
    const [alertData, setAlertData] = useState({
        show: false,
        heading: 'Thông báo',
        message: '',
        variant: 'success',
    });

    const alertTimerRef = useRef(null);
    const [isEditing, setIsEditing] = useState(false);
    const [showLabList, setShowLabList] = useState(true);

    const [searchParams, setSearchParams] = useSearchParams();
    const filterKw = searchParams.get("kw") || "";
    const filterDate = searchParams.get("date") || "";
    const filterStatus = searchParams.get("status") || "";

    const updateFilter = (key, value) => {
        const newParams = new URLSearchParams(searchParams);
        if (value) {
            newParams.set(key, value);
        } else {
            newParams.delete(key);
        }
        setSearchParams(newParams);
    };


    const STATUS_LAB = {
        'PENDING': { text: 'Chưa thanh toán', textClass: 'text-danger' },
        'CONFIRMED': { text: 'Đã thanh toán', textClass: 'text-primary' },
        'COMPLETED': { text: 'Đã xét nghiệm', textClass: 'text-success' },
    };

    const renderStatusBadge = (statusEnum) => {

        const config = STATUS_LAB[statusEnum] || { text: 'Không xác định', textClass: 'text-muted' };

        return (
            <span className={`${config.textClass} fw-bold`}>
                {config.text}
            </span>
        );
    };


    const loadLabResults = async () => {
        try {
            setLoading(true);

            const params = {};
            if (filterKw) params.kw = filterKw;
            if (filterDate) params.date = filterDate;
            if (filterStatus) params.status = filterStatus;

            const res = await authApis().get(TEST_ENDPOINTS.LAB_RESULTS, { params });

            const results = Array.isArray(res.data) ? res.data.map((item) => ({
                ...item,
                testAt: item.testAt || item.testDate || item.test_at || item.testDateTime || getCurrentLocalDateTime(),
                resultDetails: item.resultDetails || item.details || [],
            })) : [];
            setLabResults(results);
        } catch (err) {
            console.error("Failed to load lab results", err);
        } finally {
            setLoading(false);
        }
    };

    const handleSelectResult = (result) => {
        setSuccessMessage(null);
        setSaveError(null);
        setIsEditing(false);
        setShowLabList(false);
        setSelectedLabResult({
            ...result,
            testAt: result.testAt || result.testDate || result.test_at || result.testDateTime || getCurrentLocalDateTime(),
            resultDetails: result.resultDetails || result.details || [],
        });
    };

    const updateDetail = (detailId, field, value) => {
        if (!selectedLabResult) return;
        setSelectedLabResult((prev) => {
            if (!prev) return prev;
            const updatedDetails = prev.resultDetails.map((detail) => {
                if (detail.id !== detailId) return detail;
                return {
                    ...detail,
                    [field]: field === 'isAbnormal'
                        ? value === true
                            ? true
                            : value === false
                                ? false
                                : value === 'true'
                                    ? true
                                    : value === 'false'
                                        ? false
                                        : null
                        : value,
                };
            });
            return { ...prev, resultDetails: updatedDetails };
        });
    };

    const parseLocalDateTime = (dateString) => {
        if (!dateString) return null;
        const trimmed = String(dateString).trim();
        const ddmmyyyy = trimmed.match(/^(\d{2})\/(\d{2})\/(\d{4})(?:\s+(\d{2}):(\d{2}):(\d{2}))?$/);
        if (ddmmyyyy) {
            const [, dd, mm, yyyy, hh = '00', min = '00', ss = '00'] = ddmmyyyy;
            return `${yyyy}-${mm}-${dd}T${hh}:${min}:${ss}`;
        }
        const iso = new Date(trimmed);
        return Number.isNaN(iso.getTime()) ? null : trimmed;
    };

    const getCurrentLocalDateTime = () => {
        const now = new Date();
        const pad = (value) => String(value).padStart(2, '0');
        return `${pad(now.getDate())}/${pad(now.getMonth() + 1)}/${now.getFullYear()} ${pad(now.getHours())}:${pad(now.getMinutes())}:${pad(now.getSeconds())}`;
    };

    const handleSave = async () => {
        if (!selectedLabResult) return;
        setSaveError(null);
        setSuccessMessage(null);
        setSaving(true);

        try {
            const { id, patientName, createdAt, testAt, status, resultDetails } = selectedLabResult;
            const payload = {
                id,
                patientName,
                createdAt: parseLocalDateTime(createdAt),
                testAt: parseLocalDateTime(testAt),
                status,
                details: (resultDetails || []).map((detail) => ({
                    id: detail.id,
                    testName: detail.testName,
                    value: detail.value,
                    isAbnormal: detail.isAbnormal,
                })),
            };


            const res = await authApis().put(TEST_ENDPOINTS.UPDATE_LAB_RESULT(selectedLabResult.id), payload);

            const updated = res.data || selectedLabResult;
            const normalizedUpdated = {
                ...updated,
                resultDetails: updated.resultDetails || updated.details || [],
            };
            
            setSelectedLabResult(normalizedUpdated);
            setLabResults((prev) => prev.map((item) => (item.id === normalizedUpdated.id ? normalizedUpdated : item)));
            setSuccessMessage('Lưu kết quả xét nghiệm thành công.');
            handleShowAlert('Thành công', 'Lưu kết quả xét nghiệm thành công.', 'success');
            setIsEditing(false);
        } catch (err) {
            setSaveError('Không thể lưu kết quả. Vui lòng thử lại.');
            handleShowAlert('Lỗi', 'Không thể lưu kết quả. Vui lòng thử lại.', 'danger');
        } finally {
            setSaving(false);
        }
    };

    useEffect(() => {
        const delayDebounceFn = setTimeout(() => {
            loadLabResults();
        }, 500);
        return () => clearTimeout(delayDebounceFn);
    }, [filterKw, filterDate, filterStatus]);

    useEffect(() => {
        return () => {
            if (alertTimerRef.current) {
                clearTimeout(alertTimerRef.current);
            }
        };
    }, []);

    const handleShowAlert = (heading, message, variant) => {
        if (alertTimerRef.current) {
            clearTimeout(alertTimerRef.current);
        }

        setAlertData({
            show: true,
            heading,
            message,
            variant,
        });

        alertTimerRef.current = setTimeout(() => {
            setAlertData(prev => ({ ...prev, show: false }));
        }, 2000);
    };

    const testDateValue = selectedLabResult?.testAt || selectedLabResult?.testDate || selectedLabResult?.test_at || selectedLabResult?.testDateTime || selectedLabResult?.createdDate || selectedLabResult?.createdAt || '-';
    const hasDetails = selectedLabResult?.resultDetails?.length > 0;
    const filteredLabResults = labResults.filter((result) => {
        if (statusFilter === 'all') return true;
        return String(result.status || '').toLowerCase() === statusFilter;
    });

    return (
        <>
            <div className="d-flex flex-column min-vh-100 bg-light">
                <Header />
                <FloatAlert show={alertData.show} heading={alertData.heading} message={alertData.message} variant={alertData.variant} />
                <Container className="py-4">
                    <div className="mb-4 pb-3 border-bottom">
                        <h2 className="fw-bold text-primary">Xét nghiệm</h2>
                    </div>

                    <Row className="g-4">
                        {!showLabList && (
                            <Col xs={12} className="pb-0 mb-0">
                                <Button variant="outline-secondary" className="rounded-3 px-4" onClick={() => setShowLabList(true)}>
                                    Quay lại
                                </Button>
                            </Col>
                        )}
                        {showLabList && (
                            <Col>
                                <Card className="border-0 shadow-sm rounded-4 h-100">
                                    <Card.Header className="bg-white border-0 p-4 pb-0">
                                        <h5 className="fw-bold text-primary mb-0 text-center">Danh sách phiếu xét nghiệm</h5>
                                    </Card.Header>
                                    <Card.Body className="p-4">

                                        <Row className="mb-4 g-3 bg-light p-3 pt-0 mt-1 rounded shadow-sm mx-auto" style={{ width: '70%' }}>

                                            <Col md={4} sm={12}>
                                                <Form.Group controlId="filterKw">
                                                    <Form.Label className="fw-semibold small text-secondary">Tìm theo tên bệnh nhân</Form.Label>
                                                    <Form.Control
                                                        type="text"
                                                        placeholder="Nhập tên cần tìm..."
                                                        value={filterKw}
                                                        onChange={(e) => updateFilter("kw", e.target.value)}
                                                    />
                                                </Form.Group>
                                            </Col>

                                            <Col md={3} sm={6}>
                                                <Form.Group controlId="filterDate">
                                                    <Form.Label className="fw-semibold small text-secondary">Chọn ngày chỉ định</Form.Label>
                                                    <Form.Control
                                                        type="date"
                                                        value={filterDate}
                                                        onChange={(e) => updateFilter("date", e.target.value)}
                                                    />
                                                </Form.Group>
                                            </Col>

                                            <Col md={3} sm={6}>
                                                <Form.Group controlId="filterStatus">
                                                    <Form.Label className="fw-semibold small text-secondary">Trạng thái</Form.Label>
                                                    <Form.Select
                                                        value={filterStatus}
                                                        onChange={(e) => updateFilter("status", e.target.value)}
                                                    >
                                                        <option value="">Tất cả trạng thái</option>

                                                        {Object.entries(STATUS_LAB).map(([key, value]) => (
                                                            <option key={key} value={key}>
                                                                {value.text}
                                                            </option>
                                                        ))}
                                                    </Form.Select>
                                                </Form.Group>
                                            </Col>

                                            <Col md={2} sm={12} className="d-flex align-items-end">
                                                <Button
                                                    variant="outline-secondary"
                                                    className="w-100"
                                                    onClick={() => setSearchParams({})}
                                                    disabled={!filterKw && !filterDate && !filterStatus}
                                                >
                                                    Xóa bộ lọc
                                                </Button>
                                            </Col>
                                        </Row>
                                        {loading ? (
                                            <div className="text-center py-5">
                                                <Spinner animation="border" variant="primary" />
                                                <div className="text-muted mt-3">Đang tải phiếu xét nghiệm...</div>
                                            </div>
                                        ) : filteredLabResults.length === 0 ? (
                                            <div className="text-center text-muted py-5">
                                                <i className="bi bi-inbox fs-1 d-block mb-3 opacity-50"></i>
                                                Không có phiếu xét nghiệm nào.
                                            </div>
                                        ) : (
                                            <div className="table-responsive border rounded-4 overflow-hidden shadow-sm">
                                                <Table hover className="align-middle mb-0 bg-white">
                                                    <thead className="bg-light text-secondary small text-center">
                                                        <tr>
                                                            <th className="py-3 px-4 fw-semibold border-bottom-0">Mã phiếu</th>
                                                            <th className="py-3 px-4 fw-semibold border-bottom-0">Bệnh nhân</th>
                                                            <th className="py-3 px-3 fw-semibold border-bottom-0">Bác sĩ chỉ định</th>
                                                            <th className="py-3 px-3 fw-semibold border-bottom-0">Chỉ định vào</th>
                                                            <th className="py-3 px-3 fw-semibold border-bottom-0">Trạng thái</th>
                                                            <th className="py-3 px-4 fw-semibold border-bottom-0 text-end"></th>
                                                        </tr>
                                                    </thead>
                                                    <tbody className="border-top-0 text-center">
                                                        {labResults.map((result) => (
                                                            <tr
                                                                key={result.id}

                                                            >
                                                                <td className="py-3 px-4">
                                                                    <div className="fw-semibold text-dark">XN{result.id || '-'}</div>
                                                                </td>
                                                                <td className="py-3 px-4">
                                                                    <div className="fw-semibold text-dark">{result.patientName || 'Không xác định'}</div>

                                                                </td>
                                                                <td className="py-3 px-3 text-dark">
                                                                    {result.doctorName || '-'}
                                                                </td>
                                                                <td className="py-3 px-3 text-muted">
                                                                    {result.createdAt || '-'}
                                                                </td>
                                                                <td className="py-3 px-3">
                                                                    {renderStatusBadge(result.status)}
                                                                </td>
                                                                <td className=" px-4 text-end">
                                                                    {result.status !== 'PENDING' && (
                                                                        <Button
                                                                            variant="primary"
                                                                            size="sm"
                                                                            className="rounded-3 px-3 fw-medium p-2"
                                                                            onClick={() => handleSelectResult(result)}
                                                                        >
                                                                            {result.status === 'COMPLETED' ? 'Xem kết quả' : 'Xét nghiệm'}
                                                                        </Button>
                                                                    )}
                                                                </td>
                                                            </tr>
                                                        ))}
                                                    </tbody>
                                                </Table>
                                            </div>
                                        )}
                                    </Card.Body>
                                </Card>
                            </Col>
                        )}

                        {!showLabList && selectedLabResult && (
                            <Col>
                                <Card className="border-0 shadow-sm rounded-4 h-100">
                                    <Card.Header className="bg-white border-0 p-4 pb-0">
                                        <h5 className="fw-bold text-primary mb-0">Chi tiết và nhập kết quả</h5>
                                    </Card.Header>
                                    <Card.Body className="p-4">
                                        <div className="mb-4">
                                            <Row className="g-3">
                                                <Col xs={12} md={6}>
                                                    <div className="text-muted small">Bệnh nhân</div>
                                                    <div className="fw-semibold">{selectedLabResult.patientName}</div>
                                                </Col>
                                                <Col xs={12} md={6}>
                                                    <div className="text-muted small">Thời gian chỉ định</div>
                                                    <div className="fw-semibold">{selectedLabResult.createdAt || '-'}</div>
                                                </Col>
                                                <Col xs={12} md={6}>
                                                    <div className="text-muted small">Thời gian xét nghiệm</div>
                                                    <div className="fw-semibold">{testDateValue}</div>
                                                </Col>
                                                <Col xs={12} md={6}>
                                                    <div className="text-muted small">Bác sĩ chỉ định</div>
                                                    <div className="fw-semibold">{selectedLabResult.doctorName || '-'}</div>
                                                </Col>
                                                <Col xs={12} md={6}>
                                                    <div className="text-muted small">Trạng thái</div>
                                                    <div>{renderStatusBadge(selectedLabResult.status)}</div>
                                                </Col>
                                                <Col xs={12} md={6}>
                                                    <div className="text-muted small">Bác sĩ thực hiện</div>
                                                    <div className="fw-semibold">{selectedLabResult.doctorTestName || '-'}</div>
                                                </Col>
                                            </Row>
                                        </div>

                                        {hasDetails ? (
                                            <div className="table-responsive border rounded-3 overflow-hidden">
                                                <Table hover className="align-middle mb-0">
                                                    <thead className="table-light text-muted small">
                                                        <tr>
                                                            <th className="fw-semibold py-3 px-3">Xét nghiệm</th>
                                                            <th className="fw-semibold py-3">Giá trị</th>
                                                            <th className="fw-semibold py-3">Tham chiếu</th>
                                                            <th className="fw-semibold py-3">Đơn vị</th>
                                                            <th className="fw-semibold py-3 px-3">Kết luận</th>
                                                        </tr>
                                                    </thead>
                                                    <tbody>
                                                        {selectedLabResult.resultDetails.map((detail) => (
                                                            <tr key={detail.id}>
                                                                <td className="px-3 fw-medium">{detail.testName}</td>
                                                                <td>
                                                                    <Form.Control
                                                                        type="text"
                                                                        value={detail.value ?? ''}
                                                                        onChange={(e) => updateDetail(detail.id, 'value', e.target.value)}
                                                                        placeholder="Nhập giá trị..."
                                                                        disabled={selectedLabResult?.status === 'COMPLETED' && !isEditing}
                                                                    />
                                                                </td>
                                                                <td className="text-muted">{detail.normalRange || '-'}</td>
                                                                <td className="text-muted">{detail.unit || '-'}</td>
                                                                <td className="px-3">
                                                                    <div className="d-flex align-items-center gap-3 mt-2">
                                                                        <Form.Check
                                                                            inline
                                                                            type="checkbox"
                                                                            id={`normal-check-${detail.id}`}
                                                                            label={<span className="text-success fw-medium">Bình thường</span>}
                                                                            checked={detail.isAbnormal === false}
                                                                            onChange={(e) => updateDetail(detail.id, 'isAbnormal', e.target.checked ? false : true)}
                                                                            disabled={selectedLabResult?.status === 'COMPLETED' && !isEditing}
                                                                        />
                                                                    </div>
                                                                </td>
                                                            </tr>
                                                        ))}
                                                    </tbody>
                                                </Table>
                                            </div>
                                        ) : (
                                            <div className="text-center text-muted py-5 bg-light rounded-3">
                                                <i className="bi bi-file-earmark-x fs-1 d-block mb-3 opacity-50"></i>
                                                Chưa có chỉ số xét nghiệm để nhập.
                                            </div>
                                        )}

                                        <div className="d-flex justify-content-end mt-4 gap-2">
                                            {selectedLabResult?.status === 'COMPLETED' ? (
                                                /* KỊCH BẢN 1: ĐÃ HOÀN THÀNH */
                                                !isEditing ? (
                                                    <Button variant="warning" className="px-4" onClick={() => setIsEditing(true)}>
                                                        <i className="bi bi-pencil-square me-2"></i> Chỉnh sửa
                                                    </Button>
                                                ) : (
                                                    <>
                                                        <Button variant="light" className="border px-4" onClick={() => setIsEditing(false)} disabled={saving}>
                                                            Hủy
                                                        </Button>
                                                        <Button variant="primary" className="px-4 shadow-sm" disabled={saving} onClick={() => handleSave('COMPLETED')}>
                                                            {saving ? 'Đang lưu...' : 'Lưu kết quả'}
                                                        </Button>
                                                    </>
                                                )
                                            ) : (
                                                
                                                <>
                                                    <Button
                                                        variant="outline-primary"
                                                        className="px-4 shadow-sm bg-white"
                                                        disabled={saving}
                                                        onClick={() => handleSave(selectedLabResult.status)}
                                                    >
                                                        <i className="bi bi-save2 me-2"></i> {saving ? 'Đang lưu...' : 'Lưu nháp'}
                                                    </Button>

                                                    <Button
                                                        variant="primary"
                                                        className="px-4 shadow-sm"
                                                        disabled={saving}
                                                        onClick={() => handleSave('COMPLETED')}
                                                    >
                                                        <i className="bi bi-check2-circle me-2"></i> {saving ? 'Đang lưu...' : 'Hoàn thành xét nghiệm'}
                                                    </Button>
                                                </>
                                            )}
                                        </div>
                                    </Card.Body>
                                </Card>
                            </Col>
                        )}
                    </Row>
                </Container>
                <Footer />
            </div>
        </>
    );
};

export default LabTest;