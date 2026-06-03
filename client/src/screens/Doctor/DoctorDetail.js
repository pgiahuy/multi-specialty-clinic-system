import { useEffect, useState } from "react";
import { Button, Card, Col, Container, Row, Spinner } from "react-bootstrap";
import { useNavigate, useParams } from "react-router-dom";
import Header from "../../components/Header";
import Footer from "../../components/Footer";
import { authApis, CLINIC_ENDPOINTS } from "../../configs/Apis";

const DoctorDetail = () => {
    const { doctorId } = useParams();
    const navigate = useNavigate();
    const [doctor, setDoctor] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");

    useEffect(() => {
        const loadDoctor = async () => {
            try {
                setLoading(true);
                const res = await authApis().get(`${CLINIC_ENDPOINTS.DOCTORS}/${doctorId}`);
                setDoctor(res.data);
            } catch (err) {
                setError("Không tải được thông tin bác sĩ.");
                console.error(err);
            } finally {
                setLoading(false);
            }
        };

        if (doctorId) {
            loadDoctor();
        }
    }, [doctorId]);

    const name = doctor?.fullName || doctor?.name || "Bác sĩ";
    const specialties = (() => {
        if (Array.isArray(doctor?.specialtiesOfDoctor) && doctor.specialtiesOfDoctor.length > 0) {
            return doctor.specialtiesOfDoctor.map(item => ({
                id: item.id,
                name: item.name || item.specialtyName || "Chuyên khoa",
            }));
        }

        if (doctor?.specialtyName || doctor?.specialty) {
            return [{
                id: null,
                name: doctor.specialtyName || doctor.specialty,
            }];
        }
        return [];
    })();
    const specialtyLabel = specialties.length > 0 ? specialties[0].name : "Chưa có chuyên khoa";

    return (
        <div className="d-flex flex-column min-vh-100">
            <Header />
            <Container className="py-4">
                <Row className="justify-content-center g-4 align-items-stretch">
                    <Col lg={4} md={5}>
                        <Card className="shadow-sm border-0 rounded-4 h-100">
                            <Card.Body className="p-4 text-center d-flex flex-column justify-content-center" style={{ minHeight: "360px" }}>
                                <div className="mx-auto" style={{ width: 120 }}>
                                    <div
                                        className="rounded-circle bg-light border d-flex align-items-center justify-content-center mx-auto mb-3"
                                        style={{ width: 120, height: 120, overflow: "hidden" }}
                                    >
                                        {loading ? (
                                            <Spinner animation="border" />
                                        ) : doctor?.avatar ? (
                                            <img
                                                src={doctor.avatar}
                                                alt="avatar"
                                                style={{ width: "100%", height: "100%", objectFit: "cover" }}
                                                onError={(e) => { e.target.src = "/doctor-avatar.png"; }}
                                            />
                                        ) : (
                                            <img
                                                src="/doctor-avatar.png"
                                                alt="avatar"
                                                style={{ width: "100%", height: "100%", objectFit: "cover" }}
                                            />
                                        )}
                                    </div>
                                </div>
                                {loading ? (
                                    <div className="d-flex flex-column gap-2 align-items-center">
                                        <div className="placeholder-glow w-75">
                                            <span className="placeholder col-8 rounded-3" style={{ height: 22 }}></span>
                                        </div>
                                        <div className="placeholder-glow w-50">
                                            <span className="placeholder col-12 rounded-3" style={{ height: 14 }}></span>
                                        </div>
                                        <div className="placeholder-glow w-50 mt-2">
                                            <span className="placeholder col-12 rounded-pill" style={{ height: 30 }}></span>
                                        </div>
                                        <div className="placeholder-glow w-75 mt-3">
                                            <span className="placeholder col-12 rounded-3" style={{ height: 38 }}></span>
                                        </div>
                                    </div>
                                ) : (
                                    <>
                                        <h4 className="fw-bold mb-3">{name}</h4>
                                        <div className="d-flex flex-wrap justify-content-center gap-2 mb-3">
                                            {specialties.length > 0 ? specialties.map(specialty => (
                                                <span key={specialty.id || specialty.name} className="badge rounded-pill text-bg-light border px-3 py-2" style={{ fontSize: "0.8rem" }}>
                                                    {specialty.name}
                                                </span>
                                            )) : (
                                                <div className="text-muted">Chưa có chuyên khoa</div>
                                            )}
                                        </div>
                                        <div className="mt-4 d-grid gap-2">
                                            <Button
                                                variant="outline-primary"
                                                onClick={() => {
                                                    if (window.history.length > 1) {
                                                        navigate(-1);
                                                        return;
                                                    }
                                                    navigate("/doctors");
                                                }}
                                            >
                                                Quay lại
                                            </Button>
                                        </div>
                                    </>
                                )}
                            </Card.Body>
                        </Card>
                    </Col>

                    <Col lg={8} md={7}>
                        <Card className="shadow-sm border-0 rounded-4 h-100">
                            <Card.Body className="p-4" style={{ minHeight: "360px" }}>
                                {error ? (
                                    <div className="alert alert-danger mb-0">{error}</div>
                                ) : loading ? (
                                    <div className="d-flex flex-column gap-3">
                                        <div className="placeholder-glow">
                                            <span className="placeholder col-4 rounded-3" style={{ height: 22 }}></span>
                                        </div>
                                        <Row className="g-3">
                                            {Array.from({ length: 8 }).map((_, index) => (
                                                <Col md={6} key={index}>
                                                    <div className="placeholder-glow">
                                                        <div className="placeholder col-5 rounded-3 mb-2" style={{ height: 12 }}></div>
                                                        <div className="placeholder col-10 rounded-3" style={{ height: 18 }}></div>
                                                    </div>
                                                </Col>
                                            ))}
                                        </Row>
                                    </div>
                                ) : (
                                    <>
                                        <h5 className="mb-3 fw-bold">Thông tin bác sĩ</h5>
                                        <Row className="g-3">
                                            <Col md={6}>
                                                <div className="text-muted small">Họ và tên</div>
                                                <div className="fw-semibold">{doctor?.fullName || doctor?.name || "-"}</div>
                                            </Col>
                                            <Col md={6}>
                                                <div className="text-muted small">Chuyên khoa</div>
                                                <div className="d-flex flex-wrap gap-2 mt-1">
                                                    {specialties.length > 0 ? specialties.map(specialty => (
                                                        <span key={specialty.id || specialty.name} className="badge rounded-pill text-bg-light border text-dark px-3 py-2" style={{ fontSize: "0.78rem" }}>
                                                            {specialty.name}
                                                        </span>
                                                    )) : (
                                                        <div className="fw-semibold">Chưa có chuyên khoa</div>
                                                    )}
                                                </div>
                                            </Col>
                                            <Col md={6}>
                                                <div className="text-muted small">Số điện thoại</div>
                                                {doctor?.phone ? (
                                                    <div className="fw-semibold">{doctor.phone}</div>
                                                ) : (
                                                    <div style={{ fontSize: "0.75rem" }} className="text-muted fst-italic">{"Chưa cập nhật"}</div>
                                                )}
                                            </Col>
                                            <Col md={6}>
                                                <div className="text-muted small">Giới tính</div>
                                                {doctor?.gender ? (
                                                    <div className="fw-semibold">{doctor.gender}</div>
                                                ) : (
                                                    <div style={{ fontSize: "0.75rem" }} className="text-muted fst-italic">{"Chưa cập nhật"}</div>
                                                )}
                                            </Col>

                                            <Col md={6}>
                                                <div className="text-muted small">Chức danh</div>

                                                <div className="fw-semibold">{"Bác sĩ"}</div>
                                            </Col>

                                            <Col md={12}>
                                                <div className="text-muted small">Mô tả</div>
                                                {doctor?.description ? (
                                                    <div className="fw-semibold">{doctor.description}</div>
                                                ) : (
                                                    <div style={{ fontSize: "0.75rem" }} className="text-muted fst-italic">{"Chưa cập nhật"}</div>
                                                )}
                                            </Col>
                                            <Col md={12}>
                                                <div className="text-muted small">Email</div>
                                                {doctor?.email ? (
                                                    <div className="fw-semibold">{doctor.email}</div>
                                                ) : (
                                                    <div style={{ fontSize: "0.75rem" }} className="text-muted fst-italic">{"Chưa cập nhật"}</div>
                                                )}
                                            </Col>
                                        </Row>
                                    </>
                                )}
                            </Card.Body>
                        </Card>
                    </Col>
                </Row>
            </Container>
            <Footer />
        </div>
    );
};

export default DoctorDetail;
