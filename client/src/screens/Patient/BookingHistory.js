import { useEffect, useState } from "react";
import { Container, Card, Badge, Row, Col, Spinner, Tabs, Tab } from "react-bootstrap";
import Footer from "../../components/Footer";
import Header from "../../components/Header";
import { authApis, endpoint } from "../../configs/Apis";

const getStatusVariant = (status) => {
    switch ((status || "").toLowerCase()) {
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

    const loadAppointments = async () => {
        try {
            setLoading(true);
            const res = await authApis().get(endpoint["appointments"]);
            setAppointments(res.data);
        } catch (err) {
            console.log(err);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        loadAppointments();
    }, []);

    const statusMatches = (status, filter) => {
        if (!filter || filter === 'all') return true;
        if (!status) return false;
        const s = String(status).toLowerCase();
        switch (filter) {
            case 'pending':
                return s.includes('pending') || s.includes('đang chờ');
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

    return (
        <>
            <div className="d-flex flex-column min-vh-100">
                <Header />
                <Container className="py-4">
                    <h3 className="mb-4">Lịch sử đặt lịch</h3>

                    <div className="mb-3">
                        <Tabs
                            activeKey={activeStatus}
                            onSelect={(k) => setActiveStatus(k)}
                            className="nav-pills px-1 py-1 rounded-4 bg-white"
                            style={{ boxShadow: '0 12px 30px rgba(13,110,253,0.04)' }}
                        >
                           
                            <Tab eventKey="pending" title="Chưa thanh toán" tabClassName="rounded-pill px-3 py-2" />
                            <Tab eventKey="confirmed" title="Đã thanh toán" tabClassName="rounded-pill px-3 py-2" />
                            <Tab eventKey="completed" title="Đã khám" tabClassName="rounded-pill px-3 py-2" />
                            <Tab eventKey="cancelled" title="Đã hủy" tabClassName="rounded-pill px-3 py-2" />
                        </Tabs>
                    </div>

                    {loading ? (
                        <div className="d-flex justify-content-center py-5">
                            <Spinner animation="border" />
                        </div>
                    ) : filtered.length === 0 ? (
                        <div className="text-center text-muted py-5">
                            Không có lịch hẹn phù hợp với bộ lọc.
                        </div>
                    ) : (
                        <Row className="g-4">
                            {filtered.map((item) => (
                                <Col key={item.id} xs={12} md={6} lg={4}>
                                    <Card className="h-100 shadow-sm border-0" style={{ borderRadius: 18, overflow: 'hidden' }}>
                                        <div style={{ background: 'linear-gradient(90deg, rgba(13,110,253,0.06), rgba(13,110,253,0.02))', padding: '18px 20px' }}>
                                            <div className="d-flex justify-content-between align-items-start">
                                                <div>
                                                    <div className="fw-bold text-dark">#{item.id}</div>
                                                    <div className="text-muted small">{item.specialtyName || 'Chuyên khoa'}</div>
                                                </div>
                                                <div className="text-end">
                                                    <div className="small text-muted mb-1">{item.createdAt || '-'}</div>
                                                    <Badge bg={getStatusVariant(item.status)} className="text-capitalize">{item.status || 'Chưa rõ'}</Badge>
                                                </div>
                                            </div>
                                        </div>

                                        <Card.Body className="px-4 py-3">
                                            <div className="mb-3">
                                                <div className="small text-muted">Bệnh nhân</div>
                                                <div className="fw-semibold">{item.patientFullName || '-'}</div>
                                            </div>

                                            <div className="mb-3">
                                                <div className="small text-muted">Bác sĩ</div>
                                                <div className="fw-semibold">{item.doctorFullName || '-'}</div>
                                            </div>

                                            <div className="d-flex justify-content-between align-items-center mt-3">
                                                <div>
                                                    <div className="small text-muted">Ngày khám</div>
                                                    <div className="fw-semibold">{item.appointmentDate || '-'}</div>
                                                </div>
                                                <div className="text-end">
                                                    <div className="small text-muted">Ca</div>
                                                    <div className="fw-semibold">{item.session || '-'}</div>
                                                    <div className="small text-muted mt-1">{item.timeSlot || '-'}</div>
                                                </div>
                                            </div>

                                            <div className="d-flex justify-content-between align-items-center mt-4">
                                                <div>
                                                    <div className="small text-muted">Phòng</div>
                                                    <div className="fw-semibold">{item.roomName || '-'}</div>
                                                </div>
                                                <div className="text-end">
                                                    <div className="small text-muted">Khu vực</div>
                                                    <div className="fw-semibold">{item.areaName || '-'}</div>
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

export default HistoryBooking;

