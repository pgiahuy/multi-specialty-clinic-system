import { useContext, useEffect, useState } from "react";
import { Card, Col, Container, Row } from "react-bootstrap";
import { useParams } from "react-router-dom";
import Footer from "../../components/Footer";
import Header from "../../components/Header";
import { authApis, endpoint } from "../../configs/Apis";
import { MyUserContext } from "../../configs/Contexts";

const PaymentDetail = () => {
    const { patientId } = useParams();
    const [user] = useContext(MyUserContext);
    const [payments, setPayments] = useState([]);

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
                        <h2 className="fw-bold text-primary mb-0">Lịch sử thanh toán</h2>
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
                                    <Card className="h-100 shadow-sm border-0" style={{ borderRadius: '12px' }}>
                                        
                                        
                                        <Card.Header className="bg-white border-bottom-0 pt-3 pb-0 d-flex justify-content-between align-items-center">
                                            <span className="text-muted fw-bold">Mã HĐ: #{p.id}</span>
                                           
                                        </Card.Header>

                                        <Card.Body>
                                            
                                            <h3 className="text-primary mb-4 text-center fw-bold">
                                                {p.totalAmount.toLocaleString('vi-VN', { style: 'currency', currency: 'VND' })}
                                                
                                            </h3>
                                            
                                            <div className="d-flex justify-content-between mb-2">
                                                <span className="text-muted">Ngày lập:</span>
                                                <span> {p.createdDate} </span>
                                                
                                            </div>

                                           
                                        </Card.Body>

                                        
                                        <Card.Footer className="bg-white border-top-0 pb-3 pt-0">
                                            <button className="btn btn-outline-primary w-100 rounded-pill">
                                                Xem chi tiết
                                            </button>
                                        </Card.Footer>
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