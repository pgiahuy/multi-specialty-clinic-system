import { useEffect, useState } from "react";
import { authApis, PAYMENT_ENDPOINTS } from "../../configs/Apis";
import { useParams } from "react-router-dom";
import { Button, Card, Col, Container, Modal, Row } from "react-bootstrap";
import Footer from "../../components/Footer";
import Header from "../../components/Header";

const PaymentItems = () => {
    const { paymentId } = useParams();
    const [payment, setPayment] = useState(null); // Đổi thành lưu 1 object Payment duy nhất
    const [showPaymentModal, setShowPaymentModal] = useState(false);
    const [selectedMethod, setSelectedMethod] = useState('MOMO');

    const loadPayment = async () => {
        try {
            // Thay đổi URL cho phù hợp với API lấy 1 hóa đơn của bạn
            // Ví dụ: GET /api/secure/payments/23
            const res = await authApis().get(`secure/payments/${paymentId}`);
            setPayment(res.data);
        } catch (err) {
            console.log(err);
        }
    };

    useEffect(() => {
        loadPayment();
    }, [paymentId]);

    const translateType = (type) => {
        switch (type) {
            case 'APPOINTMENT': return 'Phí khám bệnh';
            case 'LAB_TEST': return 'Phí xét nghiệm';
            case 'PRESCRIPTION': return 'Tiền thuốc';
            default: return 'Dịch vụ y tế';
        }
    };

    const handleConfirmPayment = async () => {
        if (!payment) return;

        try {
            const orderInfo = `Thanh toán hóa đơn #${payment.id}`;

            const formData = new URLSearchParams();
            formData.append("method", selectedMethod);
            formData.append("orderInfo", orderInfo);
            
            // Ở Backend mới, bạn chỉ cần gửi paymentId xuống là đủ, 
            // không cần gửi 1 nùi itemIds như lúc trước nữa!
            formData.append("paymentId", payment.id);

            const res = await authApis().post(PAYMENT_ENDPOINTS.CREATE, formData, {
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                }
            });

            const payUrl = res.data.payUrl;
            if (payUrl) {
                setShowPaymentModal(false);
                window.location.href = payUrl; // Chuyển hướng sang MoMo/VNPay
            } else {
                alert("Không thể tạo giao dịch. Vui lòng thử lại!");
            }
        } catch (err) {
            console.error("Lỗi gọi API thanh toán:", err);
            alert("Lỗi kết nối đến máy chủ thanh toán.");
        }
    };

    // Nếu dữ liệu chưa load xong thì hiện Loading
    if (!payment) {
        return <div className="text-center mt-5">Đang tải dữ liệu...</div>;
    }

    return (
        <>
            <div className="d-flex flex-column min-vh-100 bg-light">
                <Header />
                <main className="flex-grow-1 py-5">
                    <Container>
                        <div className="mb-4 border-bottom pb-3">
                            <h2 className="fw-bold mb-1">Chi tiết hóa đơn #{payment.id}</h2>
                            <p className="text-muted mb-0">Ngày tạo: {payment.createdAt || payment.createdDate}</p>
                        </div>

                        <Row className="justify-content-center">
                            <Col xs={12} md={8} lg={6}>
                                <Card className="shadow border-0 rounded-4" style={{ border: '1px solid rgba(13,110,253,0.12)' }}>
                                    <Card.Body className="p-4">
                                        <div className="d-flex justify-content-between align-items-center mb-4 border-bottom pb-3">
                                            <div>
                                                <div className="text-uppercase text-secondary small fw-bold mb-1">Tổng thanh toán</div>
                                                <div className={`fs-3 fw-bold ${payment.status === 'SUCCESS' ? 'text-success' : 'text-primary'}`}>
                                                    {(payment.totalAmount || 0).toLocaleString('vi-VN')} VNĐ
                                                </div>
                                            </div>
                                            <span className={`badge py-2 px-3 rounded-pill ${payment.status === 'SUCCESS' ? 'bg-success' : payment.status === 'PENDING' ? 'bg-warning text-dark' : 'bg-danger'}`}>
                                                {payment.status === 'SUCCESS' ? 'ĐÃ THANH TOÁN' : payment.status === 'PENDING' ? 'CHỜ THANH TOÁN' : 'THẤT BẠI'}
                                            </span>
                                        </div>

                                        <div className="mb-4">
                                            <div className="text-muted small fw-bold mb-3 text-uppercase">Danh sách dịch vụ</div>
                                            
                                            {/* Render trực tiếp payment.paymentItems do Backend đã gộp sẵn */}
                                            {payment.paymentItems && payment.paymentItems.map((item, idx) => (
                                                <div key={item.id || idx} className="d-flex justify-content-between mb-3 align-items-center bg-light p-2 rounded-3">
                                                    <div>
                                                        <div className="fw-semibold">{item.itemName || item.testName}</div>
                                                        <div className="text-muted small">{translateType(item.type)}</div>
                                                    </div>
                                                    <div className="fw-bold">
                                                        {(item.amount || item.price || 0).toLocaleString('vi-VN')} VNĐ
                                                    </div>
                                                </div>
                                            ))}
                                        </div>

                                        {payment.paidAt && (
                                            <div className="text-muted small border-top pt-3">
                                                <div className="d-flex justify-content-between mb-2">
                                                    <span className="fw-bold">Ngày thanh toán:</span>
                                                    <span>{payment.paidAt}</span>
                                                </div>
                                                <div className="d-flex justify-content-between">
                                                    <span className="fw-bold">Phương thức:</span>
                                                    <span>{payment.method || 'Tiền mặt'}</span>
                                                </div>
                                            </div>
                                        )}
                                    </Card.Body>

                                    {/* Chỉ hiện nút Thanh toán nếu trạng thái là PENDING */}
                                    {payment.status === 'PENDING' && (
                                        <Card.Footer className="bg-transparent border-0 px-4 pb-4 pt-0">
                                            <Button 
                                                variant="primary" 
                                                className="w-100 rounded-4 fw-bold border-0 shadow-sm" 
                                                style={{ minHeight: '50px', fontSize: '1.1rem' }} 
                                                onClick={() => setShowPaymentModal(true)}
                                            >
                                                Thanh toán ngay
                                            </Button>
                                        </Card.Footer>
                                    )}
                                </Card>
                            </Col>
                        </Row>

                        {/* Modal Chọn Phương Thức Thanh Toán (Giữ nguyên như của bạn) */}
                        <Modal
                            show={showPaymentModal}
                            onHide={() => setShowPaymentModal(false)}
                            centered
                            dialogClassName="rounded-5"
                            contentClassName="overflow-hidden"
                        >
                            <Modal.Header closeButton className="border-bottom-0 pb-0">
                                <Modal.Title className="fw-bold">Chọn phương thức thanh toán</Modal.Title>
                            </Modal.Header>
                            <Modal.Body className="pt-3 px-4 pb-4 bg-white">
                                <div className="d-grid gap-3">
                                    <label
                                        className={`border rounded-4 p-3 d-flex align-items-center gap-3 transition-all ${selectedMethod === 'MOMO' ? 'border-primary bg-light' : 'border-secondary-subtle bg-white'}`}
                                        style={{ cursor: 'pointer' }}
                                    >
                                        <input
                                            type="radio"
                                            name="paymentMethod"
                                            className="form-check-input mt-0"
                                            checked={selectedMethod === 'MOMO'}
                                            onChange={() => setSelectedMethod('MOMO')}
                                            style={{ transform: 'scale(1.2)' }}
                                        />
                                        <img src="/logo_momo.png" alt="MoMo" width="45" className="rounded-3" />
                                        <div>
                                            <div className="fw-semibold">Ví điện tử MoMo</div>
                                            <div className="text-muted small">Thanh toán nhanh qua MoMo</div>
                                        </div>
                                    </label>

                                    <label
                                        className={`border rounded-4 p-3 d-flex align-items-center gap-3 transition-all ${selectedMethod === 'VNPAY' ? 'border-primary bg-light' : 'border-secondary-subtle bg-white'}`}
                                        style={{ cursor: 'pointer' }}
                                    >
                                        <input
                                            type="radio"
                                            name="paymentMethod"
                                            className="form-check-input mt-0"
                                            checked={selectedMethod === 'VNPAY'}
                                            onChange={() => setSelectedMethod('VNPAY')}
                                            style={{ transform: 'scale(1.2)' }}
                                        />
                                        <img src="/Logo-VNPAY.png" alt="VNPay" width="60" className="rounded-3" />
                                        <div>
                                            <div className="fw-semibold">Cổng thanh toán VNPay</div>
                                            <div className="text-muted small">Thanh toán bằng mã QR hoặc thẻ</div>
                                        </div>
                                    </label>
                                </div>
                            </Modal.Body>
                            <Modal.Footer className="border-top-0 pt-0 bg-white">
                                <Button variant="outline-secondary" className="rounded-4 px-4" onClick={() => setShowPaymentModal(false)}>
                                    Hủy
                                </Button>
                                <Button variant="primary" className="rounded-4 px-4 fw-bold border-0" onClick={handleConfirmPayment}>
                                    Tiến hành thanh toán
                                </Button>
                            </Modal.Footer>
                        </Modal>
                    </Container>
                </main>
                <Footer />
            </div>
        </>
    );
};

export default PaymentItems;