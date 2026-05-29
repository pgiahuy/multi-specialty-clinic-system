import { useEffect, useState } from "react";
import { Card, Col, Container, Row, Badge } from "react-bootstrap";
import { useParams } from "react-router-dom"; // Đã bỏ useNavigate vì không cần chuyển trang nữa
import Footer from "../../components/Footer";
import Header from "../../components/Header";
import { authApis, PAYMENT_ENDPOINTS } from "../../configs/Apis";

const PaymentDetail = () => {
    const { patientId } = useParams();
    const [payments, setPayments] = useState([]);

    const loadPayments = async () => {
        try {
            const res = await authApis().get(PAYMENT_ENDPOINTS.HISTORY(patientId));
            setPayments(res.data);
        } catch (err) {
            console.log(err);
        }
    };

    useEffect(() => {
        loadPayments();
    }, [patientId]); 

    return (
        <>
            <div className="d-flex flex-column min-vh-100 bg-light">
                <Header />

                <main className="flex-grow-1 py-5">
                    <Container>
                        <div className="d-flex justify-content-between align-items-end mb-4 border-bottom pb-3">
                            <h2 className="fw-bold mb-0">Lịch sử thanh toán</h2>
                            <span className="text-muted text-end">
                                Tổng số: <strong>{payments.length}</strong> hóa đơn
                            </span>
                        </div>

                        <Row className="g-4">
                            {payments.length === 0 ? (
                                <Col>
                                    <div className="text-center p-5 bg-white rounded border shadow-sm">
                                        <h5 className="text-muted">Hiện chưa có hóa đơn nào.</h5>
                                    </div>
                                </Col>
                            ) : (
                                payments.map((p) => (
                                    <Col key={p.id} xs={12} md={6} lg={4}>
                                        <Card className="h-100 shadow-sm border-0" style={{ borderRadius: '20px', overflow: 'hidden' }}>
                                            <div className="h-100 d-flex flex-column bg-white">

                                                {/* Header của Hóa đơn */}
                                                <div className="px-4 pt-4 pb-2 border-bottom">
                                                    <div className="d-flex justify-content-between align-items-center mb-2">
                                                        <div className="fw-bold text-dark fs-5">Mã HĐ: #{p.id}</div>
                                                        <Badge bg={p.status === 'SUCCESS' ? 'success' : 'warning'} className="rounded-pill px-3 py-2">
                                                            {p.status || 'ĐÃ THANH TOÁN'}
                                                        </Badge>
                                                    </div>
                                                    <div className="text-muted small">
                                                        <i className="bi bi-calendar-event me-2"></i>
                                                        {/* Có thể là p.createdAt hoặc p.createdDate tùy theo DTO của bạn */}
                                                        {p.createdDate || p.createdAt}
                                                    </div>
                                                </div>

                                                <Card.Body className="d-flex flex-column px-4 py-3">

                                                    {/* Danh sách chi tiết các món (Lấy thẳng từ DTO kiểu mới) */}
                                                    <div className="mb-4 flex-grow-1">
                                                        <div className="fw-semibold text-secondary mb-3 small text-uppercase">Chi tiết dịch vụ</div>

                                                        {p.paymentItems && p.paymentItems.length > 0 ? (
                                                            <ul className="list-unstyled mb-0">
                                                                {p.paymentItems.map((item, index) => (
                                                                    <li key={item.id || index} className="d-flex justify-content-between align-items-center mb-2 pb-2 border-bottom border-light">
                                                                        <span className="text-dark small text-truncate pe-2" style={{ maxWidth: '70%' }}>
                                                                            • {item.itemName}
                                                                        </span>
                                                                        <span className="text-muted small">
                                                                            {(item.amount || 0).toLocaleString('vi-VN')} đ
                                                                        </span>
                                                                    </li>
                                                                ))}
                                                            </ul>
                                                        ) : (
                                                            <div className="text-muted small fst-italic">Không có chi tiết.</div>
                                                        )}
                                                    </div>

                                                    {/* Tổng tiền */}
                                                    <div className="mt-auto pt-3 border-top d-flex justify-content-between align-items-center">
                                                        <span className="text-uppercase small fw-bold text-secondary">Tổng cộng:</span>
                                                        <span className="fs-5 fw-bold text-primary">
                                                            {(p.totalAmount || 0).toLocaleString('vi-VN')} đ
                                                        </span>
                                                    </div>

                                                </Card.Body>
                                            </div>
                                        </Card>
                                    </Col>
                                ))
                            )}
                        </Row>
                    </Container>
                </main>

                <Footer />
            </div>
        </>
    );
};

export default PaymentDetail;