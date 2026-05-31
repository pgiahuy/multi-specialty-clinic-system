import { useEffect, useState, useRef } from "react";
import { Container, Row, Col, Card, Button, Spinner, Image, Form } from "react-bootstrap";
import { Link, useNavigate } from "react-router-dom";
import { authApis, CLINIC_ENDPOINTS, endpoint } from "../../configs/Apis";
import Header from "../../components/Header";
import Footer from "../../components/Footer";
import { formCardStyle } from "../User/UserStyle";
import MySpinner from "../../components/MySpinner";

const ListDoctor = () => {
    const [doctors, setDoctors] = useState([]);
    const [loading, setLoading] = useState(false);
    const nav = useNavigate();
    const [searchTerm, setSearchTerm] = useState('');
    const [selectedSpecialty, setSelectedSpecialty] = useState('');

    const loadDoctors = async (name = '', specialty = '') => {
        try {
            setLoading(true);
            const params = {};
            if (name && name.trim()) params.doctorName = name.trim();
            if (specialty) params.specialty = specialty;

            const res = await authApis().get(CLINIC_ENDPOINTS.DOCTORS, { params });
            setDoctors(res.data || []);
        } catch (err) {
            console.log(err);
        } finally {
            setLoading(false);
        }
    };

    const openDoctorDetail = (doctorId) => {
        nav(`/doctor/detail/${doctorId}`);
    };

    useEffect(() => {
        loadDoctors();
    }, []);

    const didMountRef = useRef(true);

    
    useEffect(() => {
        if (didMountRef.current) {
            didMountRef.current = false;
            return;
        }

        const q = (searchTerm || '').trim();
        if (q.length === 1) return; 

        const t = setTimeout(() => {
            loadDoctors(searchTerm, selectedSpecialty);
        }, 350);

        return () => clearTimeout(t);
    }, [searchTerm, selectedSpecialty]);

    return (
        <>
            <div className="d-flex flex-column min-vh-100">
                <Header />
                <Container className="py-4">
                    <div>
                        <Form>
                            <Row>
                                <Col md={8}>
                                    <Form.Group className="mb-4">
                                        <Form.Control
                                            placeholder="Tìm kiếm bác sĩ..."
                                            value={searchTerm}
                                            onChange={(e) => setSearchTerm(e.target.value)}
                                        />

                                    </Form.Group>
                                </Col>
                                <Col md={4}>
                                    <Form.Select className="mb-4" value={selectedSpecialty} onChange={(e) => setSelectedSpecialty(e.target.value)}>
                                        <option value="">---Chuyên khoa---</option>
                                        <option value="cardiology">Tim mạch</option>
                                        <option value="dermatology">Da liễu</option>
                                        <option value="neurology">Thần kinh</option>
                                        <option value="pediatrics">Nhi khoa</option>
                                        <option value="psychiatry">Tâm thần</option>
                                    </Form.Select>
                                </Col>
                            </Row>
                        </Form>
                    </div>

                    <h3 className="mb-4">Danh sách bác sĩ</h3>

                    {loading ? (
                        <div className="text-center py-5"><MySpinner/></div>
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
                                                    <Image
                                                        src={doc.avatar || '/doctor-avatar.png'}
                                                        alt={doc.fullName}
                                                        className="rounded-circle"
                                                        style={{ width: 48, height: 48, objectFit: 'cover' }}
                                                        onError={(e) => { e.target.src = '/doctor-avatar.png'; }}
                                                    />
                                                </div>
                                                <div>
                                                    <div className="fw-bold">{doc.fullName || doc.name}</div>
                                                    <div className="text-muted small">{doc.specialtyName || doc.specialty || 'Chuyên khoa'}</div>
                                                </div>
                                            </div>

                                            <div className="mt-auto d-flex justify-content-between align-items-center">

                                                <div>
                                                    <Button variant="outline-primary" className="me-2 rounded-3" onClick={() => openDoctorDetail(doc.id)}>
                                                        Xem hồ sơ
                                                    </Button>
                                                    <Button
                                                        as={Link}
                                                        to={`/patient/booking?doctorId=${doc.id}`}


                                                        className="rounded-3 border-0"
                                                    >
                                                        Đặt lịch khám
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
                <Footer />
            </div>
        </>




    );
};

export default ListDoctor;