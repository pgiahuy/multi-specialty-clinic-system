import React from "react";
import { Container, Row, Col } from "react-bootstrap";
import { Link } from "react-router-dom";

const Footer = () => {
    return (
        <footer
            className="mt-auto pt-5 pb-3"
            style={{
                backgroundColor: '#e6f2ff',
                color: '#495057',
                borderTop: '1px solid rgba(0,0,0,0.04)'
            }}
        >
            <Container>
                <Row className="gy-4">


                    <Col lg={5} md={12} className="mb-4 mb-md-0">
                        <h4 className="fw-bold text-primary mb-3">
                            <i className="bi bi-hospital me-2"></i> OU-Clinic
                        </h4>
                        <p className="text-muted pe-lg-4">
                            Hệ thống quản lý phòng khám hiện đại, mang đến trải nghiệm y tế thông minh, nhanh chóng và tận tâm cho bạn cùng gia đình.
                        </p>
                    </Col>


                    <Col lg={3} md={6} className="mb-4 mb-md-0">
                        <h6 className="fw-bold text-uppercase mb-3">Dịch vụ</h6>
                        <ul className="list-unstyled mb-0">
                            <li className="mb-2">

                                <Link to="/" className="text-muted text-decoration-none">Trang chủ</Link>
                            </li>
                            <li className="mb-2">
                                <Link to="/patient/booking" className="text-muted text-decoration-none">Đăng ký lịch hẹn</Link>
                            </li>
                            <li className="mb-2">
                                <Link to="/patient/test-results" className="text-muted text-decoration-none">Tra cứu kết quả</Link>
                            </li>
                            <li>
                                <Link to="/patient/payment" className="text-muted text-decoration-none">Thanh toán viện phí</Link>
                            </li>
                        </ul>
                    </Col>


                    <Col lg={4} md={6} className="mb-4 mb-md-0">
                        <h6 className="fw-bold text-uppercase mb-3">Hỗ trợ khách hàng</h6>
                        <ul className="list-unstyled text-muted">
                            <li className="mb-2">
                                <strong>Hotline:</strong> 1900 1234
                            </li>
                            <li className="mb-2">
                                <strong>Email:</strong> support@ouclinic.com
                            </li>
                            <li className="mb-2">
                                <strong>Địa chỉ:</strong> 123 Đường Sức Khỏe, Quận 1, TP.HCM
                            </li>
                            <li>
                                <Link to="/contact" className="text-muted text-decoration-none">Trang liên hệ</Link>
                            </li>
                        </ul>
                    </Col>
                </Row>
            </Container>


            <Container className="mt-4">
                <div className="pt-3 border-top border-secondary-subtle text-center text-muted">
                    <small>© 2026 OU-Clinic. All rights reserved.</small>
                </div>
            </Container>
        </footer>
    );
}

export default Footer;