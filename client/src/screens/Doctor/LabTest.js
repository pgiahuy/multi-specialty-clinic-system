import { useEffect, useRef, useState } from "react";
import { Card, Col, Container, Row, Table, Spinner, Form, Button } from "react-bootstrap";
import { authApis, TEST_ENDPOINTS } from "../../configs/Apis";
import Header from "../../components/Header";
import Footer from "../../components/Footer";
import FloatAlert from "../../components/FloatAlert";

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
    

    const loadLabResults = async () => {
        try {
            setLoading(true);
            const res = await authApis().get(TEST_ENDPOINTS.LAB_RESULTS);
            const results = Array.isArray(res.data) ? res.data.map((item) => ({
                ...item,
                testAt: item.testAt || item.testDate || item.test_at || item.testDateTime || getCurrentLocalDateTime(),
                resultDetails: item.resultDetails || item.details || [],
            })) : [];
            setLabResults(results);
            if (results.length > 0) {
                setSelectedLabResult(results[0]);
            }
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
                        ? value === 'true'
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
        loadLabResults();
    }, []);

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
                        <Col xs={12} lg={4}>
                            <Card className="border-0 shadow-sm rounded-4 h-100">
                                <Card.Header className="bg-white border-0 p-4 pb-0">
                                    <h5 className="fw-bold text-primary mb-0">Danh sách phiếu xét nghiệm</h5>
                                </Card.Header>
                                <Card.Body className="p-4">
                                    <div className="d-flex flex-column flex-sm-row align-items-sm-end justify-content-between gap-3 mb-4">
                                        <div className="w-100 w-sm-auto">
                                            
                                            <Form.Select
                                                value={statusFilter}
                                                onChange={(e) => setStatusFilter(e.target.value)}
                                                className="rounded-3 shadow-sm"
                                            >
                                                <option value="all">Tất cả</option>
                                                <option value="confirmed">Đã thanh toán</option>
                                                <option value="pending">Chưa thanh toán</option>
                                                <option value="completed">Đã xét nghiệm</option>
                                            </Form.Select>
                                        </div>
                                       
                                    </div>
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
                                        <div className="d-grid gap-3">
                                            {filteredLabResults.map((result) => (
                                                <Card
                                                    key={result.id}
                                                    className={`border ${selectedLabResult?.id === result.id ? 'border-primary shadow' : 'border-light'} rounded-4 p-3 bg-white`}
                                                    role="button"
                                                    onClick={() => handleSelectResult(result)}
                                                    style={{ cursor: 'pointer' }}
                                                >
                                                    <div className="d-flex justify-content-between align-items-start gap-3">
                                                        <div>
                                                            <div><span className="fw-bold text-dark">Bệnh nhân:</span> {result.patientName || 'Không xác định'}</div>
                                                            <div className="mt-2"><span className="fw-bold text-dark">Bác sĩ chỉ định:</span> {result.doctorName}</div>
                                                            <div className="mt-2"><span className="fw-bold text-dark">Chỉ định vào:</span> {result.createdAt || '-'}</div>
                                                        </div>
                                                        
                                                    </div>
                                                    
                                                </Card>
                                            ))}
                                        </div>
                                    )}
                                </Card.Body>
                            </Card>
                        </Col>

                        <Col xs={12} lg={8}>
                            <Card className="border-0 shadow-sm rounded-4 h-100">
                                <Card.Header className="bg-white border-0 p-4 pb-0">
                                    <h5 className="fw-bold text-primary mb-0">Chi tiết và nhập kết quả</h5>
                                </Card.Header>
                                <Card.Body className="p-4">
                                    {!selectedLabResult ? (
                                        <div className="text-center text-muted py-5">
                                            <i className="bi bi-search fs-1 d-block mb-3 opacity-50"></i>
                                            Chọn một phiếu để xem và nhập kết quả.
                                        </div>
                                    ) : (
                                        <>
                                            <div className="mb-4">
                                                <Row className="g-3">
                                                    <Col xs={12} md={6}>
                                                        <div className="text-muted small">Bệnh nhân</div>
                                                        <div className="fw-semibold">{selectedLabResult.patientName}</div>
                                                    </Col>
                                                    <Col xs={12} md={6}>
                                                        <div className="text-muted small">Ngày tạo</div>
                                                        <div className="fw-semibold">{selectedLabResult.createdAt || '-'}</div>
                                                    </Col>
                                                    <Col xs={12} md={6}>
                                                        <div className="text-muted small">Ngày xét nghiệm</div>
                                                        <div className="fw-semibold">{testDateValue}</div>
                                                    </Col>
                                                    <Col xs={12} md={6}>
                                                        <div className="text-muted small">Trạng thái</div>
                                                        <div className={`fw-semibold ${selectedLabResult.status === 'CONFIRMED' ? 'text-primary' : 'text-warning'}`}>
                                                            {selectedLabResult.status || '-'}
                                                        </div>
                                                    </Col>
                                                </Row>
                                            </div>

                                            {hasDetails ? (
                                                <div className="table-responsive">
                                                    <Table hover className="align-middle">
                                                        <thead className="table-light text-muted small">
                                                            <tr>
                                                                <th className="fw-semibold py-3">Tên chỉ số</th>
                                                                <th className="fw-semibold py-3">Giá trị</th>
                                                                <th className="fw-semibold py-3">Bất thường</th>
                                                            </tr>
                                                        </thead>
                                                        <tbody>
                                                            {selectedLabResult.resultDetails.map((detail) => (
                                                                <tr key={detail.id}>
                                                                    <td>{detail.testName}</td>
                                                                    <td>
                                                                        <Form.Control
                                                                            type="text"
                                                                            value={detail.value ?? ''}
                                                                            onChange={(e) => updateDetail(detail.id, 'value', e.target.value)}
                                                                            placeholder="Nhập giá trị"
                                                                            disabled={selectedLabResult?.status === 'COMPLETED' && !isEditing}
                                                                        />
                                                                    </td>
                                                                    <td>
                                                                        <div className="d-flex align-items-center gap-3 mt-2">
                                                                            
                                                                            <Form.Check
                                                                                type="radio"
                                                                                id={`abnormal-yes-${detail.id}`}
                                                                                name={`abnormal-group-${detail.id}`}
                                                                                label={<span className="text-danger fw-medium">Có</span>}
                                                                                checked={detail.isAbnormal === true}
                                                                                onChange={() => updateDetail(detail.id, 'isAbnormal', 'true')}
                                                                                disabled={selectedLabResult?.status === 'COMPLETED' && !isEditing}
                                                                            />

                                                                            
                                                                            <Form.Check
                                                                                type="radio"
                                                                                id={`abnormal-no-${detail.id}`}
                                                                                name={`abnormal-group-${detail.id}`}
                                                                                label={<span className="text-success fw-medium">Không</span>}
                                                                                checked={detail.isAbnormal === false}
                                                                                onChange={() => updateDetail(detail.id, 'isAbnormal', 'false')}
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
                                                <div className="text-center text-muted py-5">
                                                    Chưa có chỉ số xét nghiệm để nhập. Vui lòng tải phiếu có resultDetails.
                                                </div>
                                            )}

                                            <div className="d-flex justify-content-end mt-4">
                                                {selectedLabResult?.status === 'COMPLETED' && !isEditing ? (
                                                    <Button variant="outline-primary" onClick={() => setIsEditing(true)}>
                                                        Chỉnh sửa
                                                    </Button>
                                                ) : (
                                                    <Button variant="primary" disabled={saving} onClick={handleSave}>
                                                        {saving ? 'Đang lưu...' : 'Lưu kết quả'}
                                                    </Button>
                                                )}
                                            </div>
                                        </>
                                    )}
                                </Card.Body>
                            </Card>
                        </Col>
                    </Row>
                </Container>
                <Footer />
            </div>
        </>
    );
};

export default LabTest;