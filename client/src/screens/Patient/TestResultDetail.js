import { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { Card, Col, Container, Row, Table, Button, Spinner } from "react-bootstrap";
import { authApis, TEST_ENDPOINTS } from "../../configs/Apis";
import Header from "../../components/Header";
import Footer from "../../components/Footer";
import MySpinner from "../../components/MySpinner";

const TestResultDetail = () => {
    const { labResultId } = useParams();
    const navigate = useNavigate();
    const [labResult, setLabResult] = useState(null);
    const [loading, setLoading] = useState(false);

    const loadLabResultDetail = async (labResultId) => {
        try {
            setLoading(true);
            const res = await authApis().get(TEST_ENDPOINTS.LAB_RESULT_DETAIL(labResultId));
            setLabResult(res.data);
        } catch (err) {
            console.error("Lỗi khi tải chi tiết xét nghiệm:", err);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        if (labResultId) {
            loadLabResultDetail(labResultId);
        }
    }, [labResultId]);

    const renderResultStatus = (status) => {
        if (status === 'COMPLETED') return 'Đã hoàn thành';
        if (status === 'PENDING') return 'Đang chờ thanh toán';
        return 'Đang xử lý';
    };

    const details = labResult?.resultDetails || labResult?.details || [];

    return (
        <>
            <div className="d-flex flex-column min-vh-100 bg-light">
                <Header />
                <Container className="py-5 flex-grow-1">
                    {loading ? (
                        <div className="text-center py-5">
                            <MySpinner />
                            <div className="text-muted mt-3 fw-medium">Đang tải kết quả...</div>
                        </div>
                    ) : !labResult ? (
                        <div className="text-center py-5 bg-white rounded-4 shadow-sm border">
                            <h5 className="text-secondary fw-semibold">Không tìm thấy thông tin xét nghiệm</h5>
                        </div>
                    ) : (
                        <Card className="border-0 shadow-sm rounded-4 mb-4 printable-card">
                            <Card.Body className="p-5">
                                <div className="text-center border-bottom border-2 border-dark pb-4 mb-4">
                                    <h3 className="fw-bold mb-0 text-primary text-uppercase">Kết Quả Xét Nghiệm</h3>
                                    <div className="small text-muted mt-1">Mã phiếu: {labResult.id}</div>
                                </div>
                                <Row className="mb-4 g-3">
                                    <Col md={6}>
                                        <Table borderless size="sm" className="mb-0">
                                            <tbody>
                                                <tr>
                                                    <td className="text-muted w-25">Bệnh nhân:</td>
                                                    <td className="fw-bold fs-6">{labResult.patientName || 'Không xác định'}</td>
                                                </tr>
                                                <tr>
                                                    <td className="text-muted">Ngày chỉ định:</td>
                                                    <td className="fw-semibold">{labResult.createdAt || '-'}</td>
                                                </tr>
                                                <tr>
                                                    <td className="text-muted">Trạng thái:</td>
                                                    <td className="fw-bold text-success">{renderResultStatus(labResult.status)}</td>
                                                </tr>
                                            </tbody>
                                        </Table>
                                    </Col>
                                    <Col md={6}>
                                        <Table borderless size="sm" className="mb-0">
                                            <tbody>
                                                <tr>
                                                    <td className="text-muted w-25">Bác sĩ chỉ định:</td>
                                                    <td className="fw-semibold">{labResult.doctorName || '-'}</td>
                                                </tr>
                                                <tr>
                                                    <td className="text-muted">Người thực hiện:</td>
                                                    <td className="fw-semibold">{labResult.doctorTestName || '-'}</td>
                                                </tr>
                                                <tr>
                                                    <td className="text-muted">Ngày thực hiện:</td>
                                                    <td className="fw-semibold">{labResult.testAt || '-'}</td>
                                                </tr>
                                            </tbody>
                                        </Table>
                                    </Col>
                                </Row>


                                {details.length > 0 ? (
                                    <div className="rounded-3 overflow-hidden mb-4">
                                        <Table hover responsive className="align-middle mb-0 bg-white">
                                            <thead className="bg-gray text-dark text-center">
                                                <tr>
                                                    <th className="py-3 px-3 w-25">Xét nghiệm</th>
                                                    <th className="py-3 px-3">Kết quả</th>
                                                    <th className="py-3 px-3">Đơn vị</th>
                                                    <th className="py-3 px-3">Tham chiếu</th>
                                                    <th className="py-3 px-3">Kết luận</th>
                                                </tr>
                                            </thead>
                                            <tbody className="text-dark border-top-0">
                                                {details.map((result) => {
                                                    const isAbnormal = result.isAbnormal === true || result.isAbnormal === 'true';
                                                    const hasValue = result.value !== null && result.value !== undefined && String(result.value).trim() !== '';

                                                    return (
                                                        <tr key={result.id} className={`${hasValue && isAbnormal ? 'table-danger' : ''}`}>
                                                            <td className="py-3 px-3 fw-semibold">{result.testName}</td>
                                                            <td className="py-3 px-3 text-center fs-5" style={{ fontFamily: 'monospace, sans-serif' }}>
                                                                {!hasValue ? (
                                                                    <span className="text-muted opacity-50">-</span>
                                                                ) : (
                                                                    <span className={isAbnormal ? "text-danger fw-bold" : "text-dark fw-bold"}>
                                                                        {result.value}
                                                                    </span>
                                                                )}
                                                            </td>
                                                            <td className="py-3 px-3 text-center text-secondary small">{result.unit || '-'}</td>
                                                            <td className="py-3 px-3 text-center text-secondary small">{result.normalRange || '-'}</td>
                                                            <td className="py-3 px-3 text-center fw-semibold">
                                                                {!hasValue ? (
                                                                    <span className="text-muted fst-italic small">Chưa có kết quả</span>
                                                                ) : isAbnormal ? (
                                                                    <span className="text-danger d-print-none">Bất thường</span>
                                                                ) : (
                                                                    <span className="text-success d-print-none">Bình thường</span>
                                                                )}

                                                            </td>
                                                        </tr>
                                                    );
                                                })}
                                            </tbody>
                                        </Table>
                                    </div>
                                ) : (
                                    <div className="text-center text-muted py-5 bg-light rounded-3 mb-4 border">
                                        <i className="bi bi-file-earmark-x fs-1 d-block mb-3 opacity-50"></i>
                                        Chưa có chỉ số xét nghiệm để hiển thị.
                                    </div>
                                )}
                                <Button variant="outline-secondary" className="rounded-pill px-4" onClick={() => navigate(-1)}>
                                    Quay lại
                                </Button>
                            </Card.Body>
                        </Card>
                    )}
                </Container>
                <Footer />
            </div>
        </>
    );
};

export default TestResultDetail;