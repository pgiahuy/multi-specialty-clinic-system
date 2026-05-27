import { useContext, useEffect, useState } from "react";
import { Card, Col, Container, Row } from "react-bootstrap";
import { useNavigate, useParams } from "react-router-dom";
import Footer from "../../components/Footer";
import Header from "../../components/Header";
import { authApis, endpoint } from "../../configs/Apis";
import { MyUserContext } from "../../configs/Contexts";

const PaymentDetail = () => {
    const { patientId } = useParams();
    const [user] = useContext(MyUserContext);
    const [payments, setPayments] = useState([]);
    const nav = useNavigate();

    const loadPayments = async () => {
        try {
            const res = await authApis().get(endpoint['payments'](patientId));
            setPayments(res.data);
        } catch (err) {
            console.log(err);
        }
    };

    useEffect(() => {
        loadPayments();
    }, []);

    return (

        <>
            <div className="d-flex flex-column min-vh-100 bg-light">
                <Header />

                <main className="flex-grow-1 py-5">
                    <Container>
                        <div className="d-flex justify-content-between align-items-end mb-4 border-bottom pb-3">
                            <h2 className="fw-bold mb-0">Danh sách hóa đơn</h2>
                            <span className="text-muted">Tổng số: {payments.length} hóa đơn</span>
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
                                        <Card className="h-100 shadow-sm border-0" style={{ borderRadius: '24px', overflow: 'hidden' }}>
                                            <div className="h-100 d-flex flex-column bg-white">
                                                <div className="px-4 py-4" >
                                                    <div className="d-flex justify-content-between align-items-start gap-3">
                                                        <div>

                                                            <div className="fw-bold text-dark">Mã hóa đơn: {p.id}</div>
                                                        </div>
                                                        <div className="text-end">

                                                            <div className="fw-semibold">{p.createdDate}</div>
                                                        </div>
                                                    </div>
                                                </div>

                                                <Card.Body className="d-flex flex-column flex-grow-1 px-4 py-4">
                                                    <div className="mb-4">
                                                        
                                                        <div>
                                                            <span className="text-uppercase small text-secondary mb-2">Tổng thanh toán: </span>
                                                            <span className="fs-5 fw-bold text-primary">{p.totalAmount.toLocaleString('vi-VN') } VNĐ</span>
                                                        </div>
                                                    </div>

                                                    <div className="mt-auto text-end">
                                                        <button className="btn btn-primary rounded-4 px-4 py-2 fw-semibold border-0 " onClick={() => nav(`/patient/payment-items/${p.id}`)}>
                                                            Xem chi tiết
                                                        </button>
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