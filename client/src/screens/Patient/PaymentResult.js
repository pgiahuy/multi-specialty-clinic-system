import { Container } from "react-bootstrap";
import Footer from "../../components/Footer";
import Header from "../../components/Header";
import { useEffect, useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import { authApis, endpoint, PAYMENT_ENDPOINTS } from "../../configs/Apis";

const PaymentResult = () => {
    const [paymentStatus, setPaymentStatus] = useState(null);
    const [paymentMessage, setPaymentMessage] = useState("");
    const [invoiceInfo, setInvoiceInfo] = useState(null);

    const location = useLocation();
    const navigate = useNavigate();

    useEffect(() => {
        const verifyPayment = async () => {
            const searchParams = location.search;
            if (!searchParams) {
                setPaymentStatus('FAILED');
                setPaymentMessage('Không tìm thấy thông tin giao dịch.');
                return;
            }

            try {
                const urlWithParams = `${PAYMENT_ENDPOINTS.MOMO_RETURN}${searchParams}`;
                const res = await authApis().get(urlWithParams);

                const urlParamsObject = new URLSearchParams(searchParams);
                const resultCode = urlParamsObject.get('resultCode');
                const orderId = urlParamsObject.get('orderId') || res.data.orderId || res.data.paymentId || res.data.invoiceId;
                const paymentId = res.data.paymentId || res.data.invoiceId || orderId;
                const amount = res.data.amount || urlParamsObject.get('amount');

                if (paymentId) {

                    setInvoiceInfo({
                        orderId,
                        amount,
                        status: resultCode === '0' ? 'Thành công' : 'Thất bại',
                        message: res.data.message || ''
                    });
                }

                if (resultCode === '0') {
                    setPaymentStatus('SUCCESS');
                    setPaymentMessage(res.data.message || 'Giao dịch của bạn đã được ghi nhận.');
                } else {
                    setPaymentStatus('FAILED');
                    setPaymentMessage('Giao dịch không thành công. Khách hàng đã hủy hoặc có lỗi xảy ra.');
                }

            } catch (error) {
                console.error("Lỗi gọi API Return:", error);
                setPaymentStatus('FAILED');
                setPaymentMessage('Không thể xác minh giao dịch với máy chủ. Vui lòng liên hệ hỗ trợ.');
            }
        };

        verifyPayment();
    }, [location]);
    return (
        <div className="container d-flex justify-content-center align-items-center" style={{ minHeight: '80vh' }}>
            <div className="card shadow border-0" style={{ maxWidth: '500px', width: '100%', borderRadius: '20px' }}>
                <div className="card-body text-center p-5">

                    {paymentStatus === 'SUCCESS' && (
                        <div className="py-3">
                            <i className="bi bi-check-circle-fill text-success mb-3 d-block" style={{ fontSize: '5rem' }}></i>
                            <h3 className="fw-bold text-success mb-3">Thanh toán thành công!</h3>
                            <p className="text-muted mb-4">{paymentMessage}</p>

                            {invoiceInfo && (
                                <div className="mb-4 p-3 bg-light rounded-4 text-start text-dark">
                                    <div className="mb-2">
                                        <span className="text-muted">Mã hóa đơn: </span>
                                        <span className="fw-semibold">{invoiceInfo.orderId}</span>
                                    </div>
                                    {invoiceInfo.amount && (
                                        <div className="mb-2">
                                            <span className="text-muted">Số tiền: </span>
                                            <span className="fw-semibold text-danger">{Number(invoiceInfo.amount).toLocaleString('vi-VN')} VNĐ</span>
                                        </div>
                                    )}
                                    <div>
                                        <span className="text-muted">Trạng thái: </span>
                                        <span className="fw-semibold">{invoiceInfo.status}</span>
                                    </div>
                                </div>
                            )}

                            <button
                                className="btn btn-primary w-100 rounded-pill py-2 fw-bold"
                                onClick={() => navigate('/patient/dashboard')}
                            >
                                Quay về dashboard
                            </button>
                        </div>
                    )}


                    {paymentStatus === 'FAILED' && (
                        <div className="py-3">
                            <i className="bi bi-x-circle-fill text-danger mb-3 d-block" style={{ fontSize: '5rem' }}></i>
                            <h3 className="fw-bold text-danger mb-3">Giao dịch thất bại</h3>
                            <p className="text-muted mb-4">{paymentMessage}</p>

                            <div className="d-flex gap-2">
                                <button
                                    className="btn btn-outline-secondary w-50 rounded-pill py-2"
                                    onClick={() => navigate('/')}
                                >
                                    Về trang chủ
                                </button>
                                <button
                                    className="btn btn-primary w-50 rounded-pill py-2 fw-bold"
                                    onClick={() => navigate(-1)}
                                >
                                    Thử lại
                                </button>
                            </div>
                        </div>
                    )}

                </div>
            </div>
        </div>
    );
}

export default PaymentResult;