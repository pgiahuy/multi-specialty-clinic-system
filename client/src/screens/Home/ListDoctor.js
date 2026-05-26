import { useEffect, useState } from "react";
import { Container, Row, Col, Card, Button, Spinner, Image } from "react-bootstrap";
import { Link } from "react-router-dom";
import { authApis, endpoint } from "../../configs/Apis";
import Header from "../../components/Header";
import Footer from "../../components/Footer";

const ListDoctor = () => {
    const [doctors, setDoctors] = useState([]);
    const [loading, setLoading] = useState(false);

    const loadDoctors = async () => {
        try {
            setLoading(true);
            const res = await authApis().get(endpoint["doctors"]);
            setDoctors(res.data || []);
        } catch (err) {
            console.log(err);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        loadDoctors();
    }, []);

    return (
        <>
            <div className="d-flex flex-column min-vh-100">
                <Header />
                <Container className="py-4">
                    <h3 className="mb-4">Bác sĩ</h3>

                    {loading ? (
                        <div className="text-center py-5"><Spinner animation="border" /></div>
                    ) : doctors.length === 0 ? (
                        <div className="text-center text-muted py-5">Không có bác sĩ để hiển thị.</div>
                    ) : (
                        <Row className="g-4">
                            {doctors.map((doc) => (
                                <Col key={doc.id} xs={12} md={6} lg={4}>
                                    <Card className="h-100 shadow-sm border-0 rounded-4">
                                        <Card.Body className="d-flex flex-column">
                                            <div className="d-flex align-items-center gap-3 mb-3">
                                                <div className="bg-primary bg-opacity-10 text-primary rounded-circle d-flex align-items-center justify-content-center" style={{ width: 56, height: 56 }}>
                                                    <Image src={doc.avatar || '/doctor-default.png'} alt={doc.fullName} className="rounded-circle" style={{ width: 48, height: 48, objectFit: 'cover' }} />
                                                </div>
                                                <div>
                                                    <div className="fw-bold">{doc.fullName || doc.name}</div>
                                                    <div className="text-muted small">{doc.specialtyName || doc.specialty || 'Chuyên khoa'}</div>
                                                </div>
                                            </div>

                                            <div className="mt-auto d-flex justify-content-between align-items-center">
                                                
                                                <div>
                                                    <Button variant="outline-primary" size="sm" className="me-2 rounded-3">Hồ sơ</Button>
                                                    <Button
                                                        as={Link}
                                                        to={`/patient/booking?doctorId=${doc.id}`}
                                                        variant="primary"
                                                        size="sm"
                                                        className="rounded-3"
                                                    >
                                                        Đặt lịch
                                                    </Button>
                                                </div>
                                            </div>
                                        </Card.Body>
                                    </Card>
                                </Col>
                            ))}
                        </Row>
                    )}
                </Container>
                <Footer/>
            </div>
        </>




    );
};

export default ListDoctor;