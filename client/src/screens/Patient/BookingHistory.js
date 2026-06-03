import { Fragment, useEffect, useState } from "react";
import { Container, Card, Badge, Row, Col, Spinner, Tabs, Tab, Form, Button, Pagination } from "react-bootstrap";
import Footer from "../../components/Footer";
import Header from "../../components/Header";
import { APPOINTMENT_ENDPOINTS, authApis, CLINIC_ENDPOINTS, endpoint, USER_ENDPOINTS } from "../../configs/Apis";
import { useNavigate, useSearchParams } from "react-router-dom";
import MySpinner from "../../components/MySpinner";

const getStatusVariant = (status) => {
    switch ((status || "").toLowerCase()) {
        case "un_paid":
        case "chưa thanh toán":
            return "warning";
        case "đã hoàn thành":
        case "completed":
            return "success";
        case "đã hủy":
        case "cancelled":
            return "danger";
        default:
            return "secondary";
    }
};


const HistoryBooking = () => {
    const [searchParams, setSearchParams] = useSearchParams();
    const [appointments, setAppointments] = useState([]);
    const [loading, setLoading] = useState(false);
    const [activeStatus, setActiveStatus] = useState('all');
    const [patientProfiles, setPatientProfiles] = useState([]);
    const [selectedProfileId, setSelectedProfileId] = useState('');
    const [currentPage, setCurrentPage] = useState(parseInt(searchParams.get('page') || '1', 10) || 1);
    const [totalResults, setTotalResults] = useState(0);
    const navigate = useNavigate();
    const pageSize = 9;

    const [fromDate, setFromDate] = useState(() => {
        const date = new Date();
        date.setMonth(date.getMonth() - 1);
        return date.toISOString().slice(0, 10);
    });
    const [toDate, setToDate] = useState(() => {
        const date = new Date();
        return date.toISOString().slice(0, 10);
    });

    const updateSearchParams = (updates) => {
        const newParams = new URLSearchParams(searchParams);
        Object.entries(updates).forEach(([key, value]) => {
            if (value === undefined || value === '') {
                newParams.delete(key);
            } else {
                newParams.set(key, value);
            }
        });
        setSearchParams(newParams);
    };

    const loadAppointments = async () => {
        try {
            setLoading(true);
            const patientId = searchParams.get('patientId');
            if (!patientId) {
                setAppointments([]);
                return;
            }
            const res = await authApis().get(APPOINTMENT_ENDPOINTS.APPOINTMENTS, {
                params: {
                    patientId: patientId,
                    startDate: fromDate,
                    endDate: toDate,
                    status: activeStatus !== 'all' ? activeStatus : undefined,
                    pageSize: pageSize,
                    page: currentPage
                }
            });

            let appointments = [];
            let total = 0;

            if (Array.isArray(res.data)) {
                appointments = res.data;
                total = parseInt(res.headers?.['x-total-count'] || res.headers?.['X-Total-Count'] || '0', 10) || 0;
            } else if (res.data && typeof res.data === 'object') {
                if (Array.isArray(res.data.items)) {
                    appointments = res.data.items;
                    total = res.data.totalItems || res.data.total || res.data.count || 0;
                } else {
                    appointments = [res.data];
                }
            }

            setAppointments(appointments);
            if (total > 0) {
                setTotalResults(total);
            } else if (appointments.length < pageSize && currentPage === 1) {
                setTotalResults(appointments.length);
            } else if (appointments.length === pageSize) {
                setTotalResults(currentPage * pageSize + 1);
            } else {
                setTotalResults((currentPage - 1) * pageSize + appointments.length);
            }
        } catch (err) {
            console.log(err);
            setAppointments([]);
            setTotalResults(0);
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
                setCurrentPage(1);
                updateSearchParams({
                    patientId: firstId,
                    fromDate: fromDate,
                    toDate: toDate,
                    status: activeStatus !== 'all' ? activeStatus : undefined,
                    page: 1
                });
            }
        } catch (err) {
            console.log(err);
        }
    };

    useEffect(() => {
        loadPatientProfiles();
    }, []);


    useEffect(() => {
        loadAppointments();
    }, [searchParams, activeStatus, fromDate, toDate, currentPage]);

    const statusMatches = (status, filter) => {
        if (!filter || filter === 'all') return true;
        if (!status) return false;
        const s = String(status).toLowerCase();
        switch (filter) {
            case 'un_paid':
                return s.includes('un_paid') || s.includes('chưa thanh toán');
            case 'confirmed':
                return s.includes('confirmed') || s.includes('đã xác nhận');
            case 'in_progress':
                return s.includes('in_progress') || s.includes('đang khám');
            case 'completed':
                return s.includes('completed') || s.includes('đã khám');
            case 'cancelled':
                return s.includes('cancel') || s.includes('đã hủy');
            default:
                return true;
        }
    };

    const totalPages = Math.max(1, Math.ceil(totalResults / pageSize));

    const renderStatusText = (status) => {
        if (!status) return <span className="text-muted">Không rõ</span>;

        switch (status.toUpperCase()) {
            case 'UN_PAID': return <Badge bg="transparent"
                className="rounded-pill px-3 py-2 border border-warning text-warning bg-warning-subtle">
                Chưa thanh toán</Badge>;
            case 'PENDING': return <Badge bg="transparent"
                className="rounded-pill px-3 py-2 border border-secondary text-secondary bg-secondary-subtle">
                Chờ xác nhận</Badge>;
            case 'CONFIRMED': return <Badge bg="transparent"
                className="rounded-pill px-3 py-2 border border-primary text-primary bg-primary-subtle">
                Đã xác nhận</Badge>;
            case 'IN_PROGRESS': return <Badge bg="transparent"
                className="rounded-pill px-3 py-2 border border-info text-info bg-info-subtle">
                Đang khám</Badge>;
            case 'COMPLETED': return <Badge bg="transparent"
                className="rounded-pill px-3 py-2 border border-success text-success bg-success-subtle">
                Đã hoàn thành</Badge>;
            case 'CANCELLED': return <Badge bg="transparent"
                className="rounded-pill px-3 py-2 border border-danger text-danger bg-danger-subtle">
                Đã hủy</Badge>;
        }
    };

    return (
        <>
            <div className="d-flex flex-column min-vh-100 bg-light">
                <Header />
                <Container className="py-4">
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
                                            onChange={(e) => {
                                                setSelectedProfileId(e.target.value);
                                                setCurrentPage(1);
                                                updateSearchParams({
                                                    patientId: e.target.value,
                                                    fromDate: fromDate,
                                                    toDate: toDate,
                                                    status: activeStatus !== 'all' ? activeStatus : undefined,
                                                    page: 1
                                                });
                                            }}
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
                                        onChange={(e) => {
                                            setFromDate(e.target.value);
                                            setCurrentPage(1);
                                            updateSearchParams({
                                                patientId: selectedProfileId,
                                                fromDate: e.target.value,
                                                toDate: toDate,
                                                status: activeStatus !== 'all' ? activeStatus : undefined,
                                                page: 1
                                            });
                                        }}
                                        style={{ width: '150px' }}
                                    />
                                </div>
                                <div>
                                    <Form.Label className="small text-muted mb-1">Đến ngày</Form.Label>
                                    <Form.Control
                                        type="date"
                                        value={toDate}
                                        className="rounded-3 shadow-sm px-3"
                                        onChange={(e) => {
                                            setToDate(e.target.value);
                                            setCurrentPage(1);
                                            updateSearchParams({
                                                patientId: selectedProfileId,
                                                fromDate: fromDate,
                                                toDate: e.target.value,
                                                status: activeStatus !== 'all' ? activeStatus : undefined,
                                                page: 1
                                            });
                                        }}
                                        style={{ width: '150px' }}
                                    />
                                </div>
                            </div>
                        </div>
                    </div>
                    <div className="mb-4 d-flex justify-content-center">
                        <Tabs
                            activeKey={activeStatus}
                            onSelect={(k) => {
                                setActiveStatus(k);
                                setCurrentPage(1);
                                updateSearchParams({
                                    patientId: selectedProfileId,
                                    fromDate: fromDate,
                                    toDate: toDate,
                                    status: k !== 'all' ? k : undefined,
                                    page: 1
                                });
                            }}
                            className="nav-pills px-1 py-1 rounded-4 bg-white d-inline-flex"
                            style={{ boxShadow: '0 12px 30px rgba(13,110,253,0.04)' }}
                        >
                            <Tab eventKey="all" title="Tất cả" tabClassName="rounded-pill px-3 py-2" />
                            <Tab eventKey="un_paid" title="Chưa thanh toán" tabClassName="rounded-pill px-3 py-2" />
                            <Tab eventKey="pending" title="Chờ xác nhận" tabClassName="rounded-pill px-3 py-2" />
                            <Tab eventKey="confirmed" title="Đã xác nhận" tabClassName="rounded-pill px-3 py-2" />
                            <Tab eventKey="in_progress" title="Đang khám" tabClassName="rounded-pill px-3 py-2" />
                            <Tab eventKey="completed" title="Đã khám" tabClassName="rounded-pill px-3 py-2" />
                            <Tab eventKey="cancelled" title="Đã hủy" tabClassName="rounded-pill px-3 py-2" />
                        </Tabs>
                    </div>

                    {loading && (
                        <div className="position-fixed top-0 start-0 vw-100 vh-100 d-flex align-items-center justify-content-center bg-white bg-opacity-75" style={{ zIndex: 1060 }}>
                            <MySpinner />
                        </div>
                    )}

                    {selectedProfileId && (
                        <>
                            <div className="row g-3">
                                {appointments.map((item) => (
                                    <div key={item.id} className="col-md-6 col-lg-4">
                                        <Card className="h-100 shadow-sm border-0"
                                            style={{
                                                borderRadius: '20px',
                                                overflow: 'hidden'
                                            }}>
                                            <div className="h-100 d-flex flex-column bg-white">
                                                <Card.Header className="bg-white border-0 p-4">
                                                    <div className="d-flex justify-content-between align-items-center">
                                                        <div className="text-start">
                                                            <h6 className="fw-bold text-primary text-uppercase mb-0">
                                                                Lịch hẹn
                                                            </h6>
                                                        </div>
                                                        <div>
                                                            {renderStatusText(item.status)}
                                                        </div>
                                                    </div>
                                                </Card.Header>
                                                <Card.Body className="d-flex flex-column px-4">
                                                    <div className="text-start">
                                                        <h5 className="fw-bold text-dark text-uppercase text-center mb-3">
                                                            {item.patientFullName || 'Không xác định'}
                                                        </h5>
                                                    </div>
                                                    <Row className="mb-3 g-2 p-3 rounded-3">
                                                        <Col md={6}>
                                                            <small className="text-muted">Ngày đăng ký</small>
                                                            <div className="fw-semibold text-dark">{item.createdAt || 'N/A'}</div>
                                                        </Col>
                                                        <Col md={6}>
                                                            <small className="text-muted">Ngày khám</small>
                                                            <div className="fw-semibold text-dark">{item.appointmentDate || '-'}</div>
                                                        </Col>
                                                    </Row>
                                                    <div className="mt-auto">
                                                        <Button
                                                            variant="outline-primary"
                                                            className="rounded-pill px-3 py-2 w-100 fw-medium"
                                                            onClick={() => navigate(`/appointments/${item.id}`)}
                                                        >
                                                            Xem chi tiết
                                                        </Button>
                                                    </div>
                                                </Card.Body>
                                            </div>
                                        </Card>
                                    </div>
                                ))}
                            </div>
                            {appointments.length === 0 && !loading && (
                                <div className="text-center text-muted py-5">
                                    <i className="bi bi-inbox fs-3 d-block mb-2 opacity-50"></i>
                                    Không có lịch hẹn nào.
                                </div>
                            )}
                            {appointments.length > 0 && totalPages > 1 && (
                                <div className="d-flex justify-content-center gap-2 mt-4">
                                    <Pagination>
                                        <Pagination.First
                                            onClick={() => setCurrentPage(1)}
                                            disabled={currentPage === 1 || loading}
                                        />
                                        <Pagination.Prev
                                            onClick={() => setCurrentPage(Math.max(1, currentPage - 1))}
                                            disabled={currentPage === 1 || loading}
                                        />

                                        {Array.from({ length: totalPages }, (_, i) => i + 1)
                                            .filter(page => page === 1 || page === totalPages || (page >= currentPage - 1 && page <= currentPage + 1))
                                            .map((page, index, arr) => (
                                                <Fragment key={page}>
                                                    {index > 0 && arr[index - 1] !== page - 1 && (
                                                        <Pagination.Ellipsis key={`ellipsis-${page}`} disabled />
                                                    )}
                                                    <Pagination.Item
                                                        key={`page-${page}`}
                                                        active={currentPage === page}
                                                        onClick={() => {
                                                            setCurrentPage(page);
                                                            updateSearchParams({
                                                                patientId: selectedProfileId,
                                                                fromDate: fromDate,
                                                                toDate: toDate,
                                                                status: activeStatus !== 'all' ? activeStatus : undefined,
                                                                page: page
                                                            });
                                                        }}
                                                        disabled={loading}
                                                    >
                                                        {page}
                                                    </Pagination.Item>
                                                </Fragment>
                                            ))}

                                        <Pagination.Next
                                            onClick={() => setCurrentPage(Math.min(totalPages, currentPage + 1))}
                                            disabled={currentPage >= totalPages || loading}
                                        />
                                        <Pagination.Last
                                            onClick={() => setCurrentPage(totalPages)}
                                            disabled={currentPage >= totalPages || loading}
                                        />
                                    </Pagination>
                                </div>
                            )}
                        </>
                    )}

                </Container>
                <Footer />
            </div>
        </>
    );
};

export default HistoryBooking;

