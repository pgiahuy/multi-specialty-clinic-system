import { useEffect, useState } from "react";
import { Container, Card, Badge, Row, Col, Spinner, Tabs, Tab, Form, Button } from "react-bootstrap";
import Footer from "../../components/Footer";
import Header from "../../components/Header";
import { authApis, CLINIC_ENDPOINTS, endpoint, USER_ENDPOINTS } from "../../configs/Apis";
import { useNavigate } from "react-router-dom";
import MySpinner from "../../components/MySpinner";

const getStatusVariant = (status) => {
    switch ((status || "").toLowerCase()) {
        case "un_paid":
        case "chưa thanh toán":
        case "chua thanh toan":
            return "warning";
        case "đã hoàn thành":
        case "completed":
            return "success";
        case "đang chờ":
        case "pending":
            return "warning";
        case "đã hủy":
        case "cancelled":
            return "danger";
        default:
            return "secondary";
    }
};




const HistoryBooking = () => {
    const [appointments, setAppointments] = useState([]);
    const [loading, setLoading] = useState(false);
    const [activeStatus, setActiveStatus] = useState('all');
    const [patientProfiles, setPatientProfiles] = useState([]);
    const [selectedProfileId, setSelectedProfileId] = useState('');
    const navigate = useNavigate();

    const [fromDate, setFromDate] = useState(() => {
        const date = new Date();
        date.setMonth(date.getMonth() - 1);
        return date.toISOString().slice(0, 10);
    });
    const [toDate, setToDate] = useState(() => {
        const date = new Date();
        return date.toISOString().slice(0, 10);
    });

    const loadAppointments = async (patientId) => {
        try {
            setLoading(true);
            if (!patientId) {
                setAppointments([]);
                return;
            }

            const url = endpoint['appointment-patient'] ? endpoint['appointment-patient'](patientId) : endpoint['appointments'];
            const res = await authApis().get(url);
            setAppointments(res.data || []);
        } catch (err) {
            console.log(err);
            setAppointments([]);
        } finally {
            setLoading(false);
        }
    };

    const loadPatientProfiles = async () => {
        try {
            const res = await authApis().get(USER_ENDPOINTS.PATIENT_PROFILES);
            const profiles = res.data || [];
            setPatientProfiles(profiles);
            if (profiles.length > 0) {
                const firstId = String(profiles[0].id);
                setSelectedProfileId(firstId);
                loadAppointments(firstId);
            }
        } catch (err) {
            console.log(err);
        }
    };

    useEffect(() => {
        loadPatientProfiles();
    }, []);

    useEffect(() => {
        if (selectedProfileId) loadAppointments(selectedProfileId);
    }, [selectedProfileId]);

    const statusMatches = (status, filter) => {
        if (!filter || filter === 'all') return true;
        if (!status) return false;
        const s = String(status).toLowerCase();
        switch (filter) {
            case 'pending':
                return s.includes('pending') || s.includes('un_paid') || s.includes('đang chờ') || s.includes('chưa thanh toán');
            case 'confirmed':
                return s.includes('confirmed') || s.includes('đã xác nhận');
            case 'completed':
                return s.includes('completed') || s.includes('đã khám');
            case 'cancelled':
                return s.includes('cancel') || s.includes('đã hủy');
            default:
                return true;
        }
    };

    const filtered = appointments.filter(a => statusMatches(a.status, activeStatus));

    const renderStatusText = (status) => {
        if (!status) return <span className="text-muted">Không rõ</span>;

        switch (status.toUpperCase()) {
            case 'PENDING': return <span className="text-warning">Chưa thanh toán</span>;
            case 'CONFIRMED': return <span className="text-primary">Đã thanh toán</span>;
            case 'IN_PROGRESS': return <span className="text-info">Đang khám</span>;
            case 'COMPLETED': return <span className="text-success">Đã hoàn thành</span>;
            case 'CANCELLED': return <span className="text-danger">Đã hủy</span>;

        }
    };

    return (
        <>
            <div className="d-flex flex-column min-vh-100">
                <Header />
                <Container className="py-4">
                    <style>{`.nav-pills .nav-link{transition: transform .12s ease, box-shadow .12s ease; cursor: pointer;}
                                .nav-pills .nav-link:hover{transform: translateY(-4px); box-shadow: 0 10px 30px rgba(13,110,253,0.12);} 
                                .nav-pills .nav-link.active{transform: none; box-shadow: none;}`}</style>

                    <div className="mb-4 pb-3 border-bottom">
                        <div className="d-flex flex-column flex-lg-row justify-content-between align-items-lg-end gap-3">


                            <div>
                                <h2 className="fw-bold mb-0 text-primary">Lịch sử đặt khám</h2>
                            </div>


                            <div className="d-flex flex-column flex-md-row align-items-md-end gap-3">


                                <div>
                                    <Form.Label className="small text-muted mb-1">Bệnh nhân</Form.Label>
                                    {patientProfiles.length > 0 ? (
                                        <Form.Select
                                            value={selectedProfileId}
                                            className="rounded-3 shadow-sm px-3"
                                            onChange={(e) => setSelectedProfileId(e.target.value)}
                                            style={{ minWidth: '250px' }}
                                        >
                                            <option value="">-- Chọn hồ sơ bệnh nhân --</option>
                                            {patientProfiles.map(p => (
                                                <option key={p.id} value={String(p.id)}>
                                                    {p.fullName || p.name || `Hồ sơ ${p.id}`}
                                                </option>
                                            ))}
                                        </Form.Select>
                                    ) : (
                                        <div className="text-muted small py-2 fst-italic">Không có hồ sơ...</div>
                                    )}
                                </div>


                                <div>
                                    <Form.Label className="small text-muted mb-1">Từ ngày</Form.Label>
                                    <Form.Control
                                        type="date"
                                        value={fromDate}
                                        className="rounded-3 shadow-sm px-3"
                                        onChange={(e) => setFromDate(e.target.value)}
                                        style={{ width: '150px' }}
                                    />
                                </div>


                                <div>
                                    <Form.Label className="small text-muted mb-1">Đến ngày</Form.Label>
                                    <Form.Control
                                        type="date"
                                        value={toDate}
                                        className="rounded-3 shadow-sm px-3"
                                        onChange={(e) => setToDate(e.target.value)}
                                        style={{ width: '150px' }}
                                    />
                                </div>

                            </div>
                        </div>
                    </div>


                    <div className="mb-4 d-flex justify-content-start">
                        <Tabs
                            activeKey={activeStatus}
                            onSelect={(k) => setActiveStatus(k)}
                            className="nav-pills px-1 py-1 rounded-4 bg-white d-inline-flex"
                            style={{ boxShadow: '0 12px 30px rgba(13,110,253,0.04)' }}
                        >
                            <Tab eventKey="all" title="Tất cả" tabClassName="rounded-pill px-3 py-2" />
                            <Tab eventKey="pending" title="Chưa thanh toán" tabClassName="rounded-pill px-3 py-2" />
                            <Tab eventKey="confirmed" title="Đã thanh toán" tabClassName="rounded-pill px-3 py-2" />
                            <Tab eventKey="completed" title="Đã khám" tabClassName="rounded-pill px-3 py-2" />
                            <Tab eventKey="cancelled" title="Đã hủy" tabClassName="rounded-pill px-3 py-2" />
                        </Tabs>
                    </div>
                    {loading ? (
                        <div className="d-flex justify-content-center py-5">
                            <MySpinner />
                        </div>
                    ) : filtered.length === 0 ? (
                        <div className="text-center text-muted py-5">
                            Chưa có lịch sử đặt khám phù hợp.
                        </div>
                    ) : (
                        <Row className="g-4">
                            {filtered.map((item) => (
                                <Col key={item.id} xs={12} md={6} lg={4}>
                                    <Card className="h-100 shadow-sm border-0" style={{ borderRadius: 18, overflow: 'hidden' }}>

                                        <div className="bg-light" style={{ padding: '16px 20px', borderBottom: '1px solid #edf2f7' }}>
                                            <div className="d-flex justify-content-between align-items-center">
                                                <div className="small text-muted">
                                                    Ngày đăng ký: {item.createdAt || '-'}
                                                </div>
                                                <div>{renderStatusText(item.status)}</div>
                                            </div>
                                        </div>


                                        <Card.Body className="p-4">
                                            <div className="d-flex justify-content-between align-items-center mb-3">
                                                <div className="small text-muted">Bệnh nhân</div>
                                                <div className="fw-semibold text-dark text-end">{item.patientFullName || '-'}</div>
                                            </div>

                                            <div className="d-flex justify-content-between align-items-center mb-4">
                                                <div className="small text-muted">Ngày khám</div>
                                                <div className="fw-bold text-end">{item.appointmentDate || '-'}</div>
                                            </div>


                                            <Button
                                                variant="primary"
                                                className="w-100 rounded-4 fw-medium"

                                                onClick={() => navigate(`/appointment/${item.id}`)}
                                            >
                                                Xem chi tiết
                                            </Button>
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

export default HistoryBooking;

