import { useEffect, useState } from "react";
import { Badge, Card, Col, Container, Row } from "react-bootstrap";
import Header from "../../components/Header";
import Footer from "../../components/Footer";
import { authApis, USER_ENDPOINTS } from "../../configs/Apis";

const DoctorProfile = () => {
    const [profile, setProfile] = useState(null);
    const [err, setErr] = useState("");

    const loadProfile = async () => {
        try {
            const res = await authApis().get(USER_ENDPOINTS.CURRENT_USER);
            setProfile(res.data);
        } catch (error) {
            setErr("Không tải được thông tin bác sĩ.");
            console.error(error);
        }
    };

    useEffect(() => {
        loadProfile();
    }, []);

    const doctor = profile?.doctorProfile;
    const roleLabel = profile?.role === "ROLE_DOCTOR"
        ? "Bác sĩ"
        : profile?.role === "ROLE_PATIENT"
            ? "Bệnh nhân"
            : profile?.role || "Chưa xác định";

    return (
        <div className="d-flex flex-column min-vh-100">
            <Header />
            <Container className="py-4">
                <Row className="g-4">
                    <Col lg={4}>
                        <Card className="shadow-sm border-0 rounded-4 h-100">
                            <Card.Body className="p-4 text-center">
                                <img
                                    src={profile?.avatar || "/default-avatar.png"}
                                    alt="avatar"
                                    className="rounded-circle mb-3"
                                    style={{ width: 120, height: 120, objectFit: "cover" }}
                                    onError={(e) => { e.target.src = "/default-avatar.png"; }}
                                />
                                <h4 className="fw-bold mb-1">{doctor?.fullName || profile?.username || "Bác sĩ"}</h4>
                                <div className="text-muted mb-3">{doctor?.specialty || "Chưa có chuyên khoa"}</div>
                                <Badge bg="primary" className="rounded-pill px-3 py-2">{roleLabel}</Badge>
                            </Card.Body>
                        </Card>
                    </Col>

                    <Col lg={8}>
                        <Card className="shadow-sm border-0 rounded-4 mb-4">
                            <Card.Body className="p-4">
                                <h5 className="mb-3 fw-bold">Thông tin tài khoản</h5>
                                {err ? (
                                    <div className="alert alert-danger mb-0">{err}</div>
                                ) : (
                                    <Row className="g-3">
                                        <Col md={6}>
                                            <div className="text-muted small">Tên đăng nhập</div>
                                            <div className="fw-semibold">{profile?.username || "-"}</div>
                                        </Col>
                                        <Col md={6}>
                                            <div className="text-muted small">Email</div>
                                            <div className="fw-semibold">{profile?.email || doctor?.email || "-"}</div>
                                        </Col>
                                        <Col md={6}>
                                            <div className="text-muted small">Vai trò</div>
                                            <div className="fw-semibold">{roleLabel}</div>
                                        </Col>
                                    </Row>
                                )}
                            </Card.Body>
                        </Card>

                        <Card className="shadow-sm border-0 rounded-4">
                            <Card.Body className="p-4">
                                <h5 className="mb-3 fw-bold">Thông tin cá nhân</h5>
                                <Row className="g-3">
                                    <Col md={6}>
                                        <div className="text-muted small">Họ và tên</div>
                                        <div className="fw-semibold">{doctor?.fullName || "-"}</div>
                                    </Col>
                                    <Col md={6}>
                                        <div className="text-muted small">Giới tính</div>
                                        <div className="fw-semibold">{doctor?.gender || "-"}</div>
                                    </Col>
                                    <Col md={12}>
                                        <div className="text-muted small">Mô tả</div>
                                        <div className="fw-semibold">{doctor?.description || "-"}</div>
                                    </Col>
                                </Row>
                            </Card.Body>
                        </Card>
                    </Col>
                </Row>
            </Container>
            <Footer />
        </div>
    );
};

export default DoctorProfile;
