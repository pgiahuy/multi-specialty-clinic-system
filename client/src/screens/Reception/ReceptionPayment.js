import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { Button, Card, Col, Container, Row, Modal } from "react-bootstrap";
import Header from "../../components/Header";
import Footer from "../../components/Footer";
import MySpinner from "../../components/MySpinner";
import { authApis, PAYMENT_ENDPOINTS } from "../../configs/Apis";
import { CheckCircleFill } from "react-bootstrap-icons";

const ReceptionPayment = () => {
    const { paymentId } = useParams();
    const navigate = useNavigate();
    const [payment, setPayment] = useState(null);
    const [loading, setLoading] = useState(true);
    const [submitting, setSubmitting] = useState(false);
    const [showSuccess, setShowSuccess] = useState(false);
    const [message, setMessage] = useState("");
    const [error, setError] = useState("");

    useEffect(() => {
        const loadPayment = async () => {
            if (!paymentId) {
                setError("Mã hóa đơn không hợp lệ.");
                setLoading(false);
                return;
            }

            try {
                setLoading(true);
                const response = await authApis().get(PAYMENT_ENDPOINTS.BY_PAYMENT_ID(paymentId));
                setPayment(response.data || null);
            } catch (err) {
                console.error(err);
                setError("Không tìm thấy hóa đơn hoặc có lỗi kết nối.");
            } finally {
                setLoading(false);
            }
        };

        loadPayment();
    }, [paymentId]);

    const handleCashPayment = async () => {
        if (!payment || !payment.id) return;

        setSubmitting(true);
        try {
            const formData = new URLSearchParams();
            formData.append("method", "CASH");
            formData.append("paymentId", payment.id);
            formData.append("orderInfo", `Thanh toán tiền mặt cho hóa đơn ${payment.id}`);

            await authApis().post(PAYMENT_ENDPOINTS.PAY, formData, {
                headers: {
                    "Content-Type": "application/x-www-form-urlencoded"
                }
            });

            setPayment((prev) => prev ? { ...prev, status: "SUCCESS", method: "CASH" } : prev);
            setMessage("Đã thanh toán tiền mặt thành công.");
            setShowSuccess(true);
        } catch (err) {
            console.error(err);
            setError("Thanh toán thất bại. Vui lòng thử lại.");
        } finally {
            setSubmitting(false);
        }
    };

    const getPaymentType = (type) => {
        switch (type) {
            case "APPOINTMENT":
                return "Phí khám bệnh";
            case "LAB_TEST":
                return "Phí xét nghiệm";
            case "PRESCRIPTION":
                return "Thanh toán đơn thuốc";
        }
    }

    return (
        <div className="d-flex flex-column min-vh-100">
            <Header />
            <Container className="py-4">
                <div className="mb-4 d-flex justify-content-between align-items-center">
                    <div>
                        <h2 className="fw-bold mb-1 text-primary">Thanh toán quầy tiếp nhận</h2>
                        <p className="text-muted mb-0">Hóa đơn #{paymentId}</p>
                    </div>
                    <Button variant="outline-secondary" onClick={() => navigate('/reception')}>
                        Quay lại danh sách tiếp nhận
                    </Button>
                </div>

                {loading ? (
                    <div className="text-center py-5">
                        <MySpinner />
                    </div>
                ) : error ? (
                    <div className="alert alert-danger">{error}</div>
                ) : payment ? (
                    <Row className="justify-content-center">
                        <Col xs={12} md={8} lg={6}>
                            <Card className="shadow-sm border-0 rounded-4">
                                <Card.Body>
                                    <div className="mb-4">
                                        <div className="text-secondary small mb-2">Bệnh nhân</div>
                                        <h4 className="fw-semibold">{payment.patientName || '---'}</h4>
                                        <div className="text-secondary small">Hóa đơn #{payment.id}</div>
                                        <h4 className="text-center text-uppercase text-success fw-bold">{getPaymentType(payment.paymentItems[0]?.itemType)}</h4>
                                    </div>

                                    <div className="mb-4">
                                        <div className="d-flex justify-content-between align-items-center mb-2">
                                            <span className="text-secondary">Trạng thái</span>
                                            <span className={`badge ${payment.status === 'SUCCESS' ? 'bg-success' : 'bg-warning text-dark'}`}>
                                                {payment.status === 'SUCCESS' ? 'ĐÃ THANH TOÁN' : 'CHƯA THANH TOÁN'}
                                            </span>
                                        </div>
                                        <div className="d-flex justify-content-between align-items-center">
                                            <span className="text-secondary">Phương thức</span>
                                            <span>{payment.method || 'Tiền mặt'}</span>
                                        </div>
                                    </div>

                                    <div className="mb-4 p-4 bg-light rounded-4 text-center">
                                        <div className="text-secondary small mb-2">Tổng phải thu</div>
                                        <div className="fs-2 fw-bold text-danger">{(payment.totalAmount || 0).toLocaleString('vi-VN')} đ</div>
                                    </div>

                                    <div className="d-grid gap-3">
                                        {payment.status !== 'SUCCESS' ? (
                                            <Button
                                                variant="success"
                                               
                                                onClick={handleCashPayment}
                                                disabled={submitting}
                                            >
                                                {submitting ? 'Đang xử lý...' : 'Thanh toán tiền mặt'}
                                            </Button>
                                        ) : (
                                            <Button variant="outline-success" disabled>
                                                Đã thanh toán
                                            </Button>
                                        )}
                                        <Button variant="secondary" onClick={() => navigate(-1)}>
                                            Quay lại
                                        </Button>
                                    </div>
                                </Card.Body>
                            </Card>
                        </Col>
                    </Row>
                ) : (
                    <div className="alert alert-warning">Không có thông tin hóa đơn.</div>
                )}
            </Container>
            <Footer />

            <Modal show={showSuccess} onHide={() => setShowSuccess(false)} centered>
                <Modal.Header closeButton>
                    <Modal.Title>Thanh toán thành công</Modal.Title>
                </Modal.Header>
                <Modal.Body className="text-center">
                    <CheckCircleFill color="green" size={48} />
                    <p className="mt-3 mb-0">{message}</p>
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="primary" onClick={() => setShowSuccess(false)}>
                        Đóng
                    </Button>
                </Modal.Footer>
            </Modal>
        </div>
    );
};

export default ReceptionPayment;
