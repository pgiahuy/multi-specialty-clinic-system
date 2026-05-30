import { useEffect, useState } from "react";
import { Badge, Card, Col, Container, Row, Stack } from "react-bootstrap";
import { Envelope, PersonCircle, ShieldLock } from "react-bootstrap-icons";
import Header from "../../components/Header";
import Footer from "../../components/Footer";
import { authApis, USER_ENDPOINTS } from "../../configs/Apis";

const PatientAccount = () => {
    const [profile, setProfile] = useState(null);
    const [err, setErr] = useState("");
    const displayName = profile?.name || profile?.username || "Bệnh nhân";
    const patientInitial = displayName.trim().charAt(0).toUpperCase();

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
                                            {profile?.avatar ? (
                                                <img
                                                    src={profile.avatar}
                                                    alt="avatar"
                                                    className="rounded-circle mb-3"
                                                    style={{ width: 120, height: 120, objectFit: "cover" }}
                                                    onError={(e) => {
                                                        e.target.style.display = "none";
                                                        const fallback = e.target.nextElementSibling;
                                                        if (fallback) fallback.style.display = "flex";
                                                    }}
                                                />
                                            ) :
                                                <div
                                                    className="rounded-circle mb-3 mx-auto d-flex align-items-center justify-content-center bg-light text-primary fw-bold border"
                                                    style={{
                                                        width: 120,
                                                        height: 120,
                                                        fontSize: "44px",
                                                        lineHeight: 1,
                                                        display: profile?.avatar ? "none" : "flex"
                                                    }}
                                                >
                                                    {patientInitial}
                                                </div>}
                                            <div>
                                                <Badge bg="primary" className="rounded-pill px-3 py-2">{profile?.role == "ROLE_PATIENT" ? "Bệnh nhân" : "Chưa xác định"}</Badge>
                                            </div>
                                        </Col>
                                        <Col md={8}>
                                            <Row className="g-3">
                                                <Col md={6}>
                                                    <div className="text-muted small">Tên đăng nhập</div>
                                                    {profile?.username ? (
                                                        <div className="fw-semibold">{profile.username}</div>
                                                    ) : (
                                                        <div style={{ fontSize: "0.75rem" }} className="text-muted ">{"Chưa cập nhật"}</div>
                                                    )}
                                                </Col>
                                                <Col md={6}>
                                                    <div className="text-muted small">Tên người dùng</div>
                                                    {profile?.name ? (
                                                        <div className="fw-semibold">{profile.name}</div>
                                                    ) : (
                                                        <div style={{ fontSize: "0.75rem" }} className="text-muted ">{"Chưa cập nhật"}</div>
                                                    )}
                                                </Col>
                                                <Col md={6}>
                                                    <div className="text-muted small">Email</div>
                                                    {profile?.email ? (
                                                        <div className="fw-semibold d-flex align-items-center gap-2"><Envelope size={14} />{profile.email}</div>
                                                    ) : (
                                                        <div style={{ fontSize: "0.75rem" }} className="text-muted">{"Chưa cập nhật"}</div>
                                                    )}
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