import { useEffect, useState } from "react";
import { Badge, Card, Col, Container, Row, Stack } from "react-bootstrap";
import { Envelope, PersonCircle, ShieldLock } from "react-bootstrap-icons";
import Header from "../../components/Header";
import Footer from "../../components/Footer";
import { authApis, USER_ENDPOINTS } from "../../configs/Apis";

const PatientAccount = () => {
    const [profile, setProfile] = useState(null);
    const [err, setErr] = useState("");

    const loadProfile = async () => {
        try {
            const res = await authApis().get(USER_ENDPOINTS.CURRENT_USER);
            setProfile(res.data);
        } catch (error) {
            setErr("Không tải được thông tin tài khoản.");
            console.error(error);
        }
    };

    useEffect(() => {
        loadProfile();
    }, []);

    return (
        <div className="d-flex flex-column min-vh-100">
            <Header />
            <Container className="py-4">
                <Row className="justify-content-center">
                    <Col lg={8} xl={7}>
                        <Card className="shadow-sm border-0 rounded-4">
                            <Card.Body className="p-4 p-md-5">
                                <Stack direction="horizontal" gap={2} className="mb-4 align-items-center">
                                    <PersonCircle size={24} />
                                    <h4 className="mb-0 fw-bold">Xem tài khoản</h4>
                                </Stack>

                                {err ? (
                                    <div className="alert alert-danger mb-0">{err}</div>
                                ) : (
                                    <Row className="g-4 align-items-center">
                                        <Col md={4} className="text-center">
                                            <img
                                                src={profile?.avatar || "/default-avatar.png"}
                                                alt="avatar"
                                                className="rounded-circle mb-3"
                                                style={{ width: 120, height: 120, objectFit: "cover" }}
                                                onError={(e) => { e.target.src = "/default-avatar.png"; }}
                                            />
                                            <div>
                                                <Badge bg="primary" className="rounded-pill px-3 py-2">{profile?.role || "ROLE_PATIENT"}</Badge>
                                            </div>
                                        </Col>
                                        <Col md={8}>
                                            <Row className="g-3">
                                                <Col md={6}>
                                                    <div className="text-muted small">Tên đăng nhập</div>
                                                    <div className="fw-semibold">{profile?.username || "-"}</div>
                                                </Col>
                                                <Col md={6}>
                                                    <div className="text-muted small">Email</div>
                                                    <div className="fw-semibold d-flex align-items-center gap-2"><Envelope size={14} />{profile?.email || "-"}</div>
                                                </Col>
                                                <Col md={6}>
                                                    <div className="text-muted small">Vai trò</div>
                                                    <div className="fw-semibold"><ShieldLock size={14} className="me-1" />{profile?.role || "-"}</div>
                                                </Col>
                                                <Col md={12}>
                                                    <div className="text-muted small">Ảnh đại diện</div>
                                                    <div className="fw-semibold text-truncate">{profile?.avatar || "-"}</div>
                                                </Col>
                                            </Row>
                                        </Col>
                                    </Row>
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

export default PatientAccount;