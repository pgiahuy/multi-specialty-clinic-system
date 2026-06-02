import { useState } from "react";
import { Container, Row, Col, Form, Button, Card, Alert } from "react-bootstrap";
import { Envelope, Telephone, GeoAlt, Clock } from "react-bootstrap-icons";

const Contact = () => {
    const [submitted, setSubmitted] = useState(false);
    const [formData, setFormData] = useState({ name: "", email: "", message: "" });

    const handleChange = (field) => (event) => {
        setFormData(prev => ({ ...prev, [field]: event.target.value }));
    };

    const handleSubmit = (event) => {
        event.preventDefault();
        setSubmitted(true);
    };

    return (
        <section className="py-5">
            <Container>
                <Row className="justify-content-center">
                    <Col lg={10}>
                        <div className="text-center mb-5">
                            <h1 className="fw-bold">Liên hệ OU-Clinic</h1>
                            <p className="text-muted mx-auto" style={{ maxWidth: 700 }}>
                                Cần hỗ trợ đặt lịch, tư vấn khám bệnh, hoặc giải đáp thắc mắc về dịch vụ?
                                Hãy gửi tin nhắn cho chúng tôi và đội ngũ chăm sóc khách hàng sẽ phản hồi nhanh chóng.
                            </p>
                        </div>

                        <Row className="g-4">
                            <Col lg={5}>
                                <Card className="h-100 p-4 border-0 shadow-sm">
                                    <div className="d-flex align-items-center mb-3">
                                        <div className="contact-icon bg-primary bg-opacity-10 text-primary me-3">
                                            <Telephone size={24} />
                                        </div>
                                        <div>
                                            <h5 className="fw-bold mb-1">Hotline hỗ trợ</h5>
                                            <p className="mb-0 text-muted">1900 1234</p>
                                        </div>
                                    </div>
                                    <div className="d-flex align-items-center mb-3">
                                        <div className="contact-icon bg-success bg-opacity-10 text-success me-3">
                                            <Envelope size={24} />
                                        </div>
                                        <div>
                                            <h5 className="fw-bold mb-1">Email</h5>
                                            <p className="mb-0 text-muted">support@ouclinic.com</p>
                                        </div>
                                    </div>
                                    <div className="d-flex align-items-center mb-3">
                                        <div className="contact-icon bg-warning bg-opacity-10 text-warning me-3">
                                            <GeoAlt size={24} />
                                        </div>
                                        <div>
                                            <h5 className="fw-bold mb-1">Địa chỉ</h5>
                                            <p className="mb-0 text-muted">123 Đường Sức Khỏe, Quận 1, TP. Hồ Chí Minh</p>
                                        </div>
                                    </div>
                                    <div className="d-flex align-items-center">
                                        <div className="contact-icon bg-info bg-opacity-10 text-info me-3">
                                            <Clock size={24} />
                                        </div>
                                        <div>
                                            <h5 className="fw-bold mb-1">Giờ làm việc</h5>
                                            <p className="mb-0 text-muted">Thứ 2 - Thứ 7, 07:00 - 17:00</p>
                                        </div>
                                    </div>
                                </Card>
                            </Col>

                            <Col lg={7}>
                                <Card className="border-0 shadow-sm p-4">
                                    <Card.Body>
                                        {submitted && (
                                            <Alert variant="success" onClose={() => setSubmitted(false)} dismissible>
                                                Tin nhắn của bạn đã được gửi. Chúng tôi sẽ liên hệ lại sớm nhất.
                                            </Alert>
                                        )}
                                        <Form onSubmit={handleSubmit}>
                                            <Form.Group className="mb-3" controlId="contactName">
                                                <Form.Label>Họ tên</Form.Label>
                                                <Form.Control
                                                    type="text"
                                                    placeholder="Nhập họ tên"
                                                    value={formData.name}
                                                    onChange={handleChange('name')}
                                                    required
                                                />
                                            </Form.Group>
                                            <Form.Group className="mb-3" controlId="contactEmail">
                                                <Form.Label>Email</Form.Label>
                                                <Form.Control
                                                    type="email"
                                                    placeholder="Nhập email"
                                                    value={formData.email}
                                                    onChange={handleChange('email')}
                                                    required
                                                />
                                            </Form.Group>
                                            <Form.Group className="mb-4" controlId="contactMessage">
                                                <Form.Label>Nội dung</Form.Label>
                                                <Form.Control
                                                    as="textarea"
                                                    rows={5}
                                                    placeholder="Mô tả yêu cầu hoặc thắc mắc của bạn"
                                                    value={formData.message}
                                                    onChange={handleChange('message')}
                                                    required
                                                />
                                            </Form.Group>
                                            <Button type="submit" variant="primary" className="rounded-4 px-4">
                                                Gửi yêu cầu
                                            </Button>
                                        </Form>
                                    </Card.Body>
                                </Card>
                            </Col>
                        </Row>
                    </Col>
                </Row>
            </Container>
        </section>
    );
};

export default Contact;
