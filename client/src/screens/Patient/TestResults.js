import { Fragment, useContext, useEffect, useState } from "react";
import { authApis, USER_ENDPOINTS, TEST_ENDPOINTS } from "../../configs/Apis";
import { exp } from "firebase/firestore/pipelines";
import { MyUserContext } from "../../configs/Contexts";
import { Card, Col, Container, Form, Row, Button, Tabs, Tab, Pagination, Badge } from "react-bootstrap";
import Header from "../../components/Header";
import Footer from "../../components/Footer";
import MySpinner from "../../components/MySpinner";
import TestResultDetailModal from "./TestResultDetail";
import { useNavigate, useSearchParams } from "react-router-dom";

const getInitialFromDate = () => {
    const date = new Date();
    date.setMonth(date.getMonth() - 1);
    return date.toISOString().slice(0, 10);
};

const getInitialToDate = () => {
    const date = new Date();
    return date.toISOString().slice(0, 10);
};

const TestResults = () => {
    const [user] = useContext(MyUserContext);
    const [searchParams, setSearchParams] = useSearchParams();
    const [labResults, setLabResults] = useState([]);
    const [selectedLabResult, setSelectedLabResult] = useState(null);
    const [patientProfiles, setPatientProfiles] = useState([]);
    const [selectedProfileId, setSelectedProfileId] = useState(null);
    const [loading, setLoading] = useState(false);
    const [activeStatusTab, setActiveStatusTab] = useState(searchParams.get('status') || 'all');
    const [fromDate, setFromDate] = useState(searchParams.get('fromDate') || getInitialFromDate);
    const [toDate, setToDate] = useState(searchParams.get('toDate') || getInitialToDate);
    const [currentPage, setCurrentPage] = useState(parseInt(searchParams.get('page') || '1', 10) || 1);
    const [totalResults, setTotalResults] = useState(0);
    const navigate = useNavigate();
    const pageSize = 9;

    const loadPatientProfiles = async () => {
        try {
            setLoading(true);
            const res = await authApis().get(USER_ENDPOINTS.PATIENT_PROFILES);
            setPatientProfiles(res.data);

            if (res.data && res.data.length > 0) {
                setSelectedProfileId(String(res.data[0].id));
            }
        } catch (err) {
            console.log(err);
        } finally {
            setLoading(false);
        }
    };

    const loadLabResults = async (patientId, startDate, endDate, status, page) => {
        try {
            setLoading(true);
            const params = {};
            if (patientId) params.patientId = patientId;
            if (startDate) params.startDate = startDate;
            if (endDate) params.endDate = endDate;
            if (status && status !== 'all') params.status = status;
            params.pageSize = pageSize;
            params.page = page;

            const res = await authApis().get(TEST_ENDPOINTS.LAB_RESULTS, { params });

            let labResults = [];
            let total = 0;

            if (Array.isArray(res.data)) {
                labResults = res.data;
                total = parseInt(res.headers?.['x-total-count'] || res.headers?.['X-Total-Count'] || '0', 10) || 0;
            } else if (res.data && typeof res.data === 'object') {
                if (Array.isArray(res.data.items)) {
                    labResults = res.data.items;
                    total = res.data.totalItems || res.data.total || res.data.count || 0;
                } else {
                    labResults = [res.data];
                }
            }

            setLabResults(labResults);
            setSelectedLabResult(null);
            if (total > 0) {
                setTotalResults(total);
            } else if (labResults.length < pageSize && page === 1) {
                setTotalResults(labResults.length);
            } else if (labResults.length === pageSize) {
                setTotalResults(page * pageSize + 1);
            } else {
                setTotalResults((page - 1) * pageSize + labResults.length);
            }
        } catch (err) {
            console.log(err);
            setLabResults([]);
            setTotalResults(0);
        } finally {
            setLoading(false);
        }
    };

    const handleSelectLabResult = (labResult) => {
        navigate(`/patient/test-results/${labResult.id}`);
    };

    useEffect(() => {
        loadPatientProfiles();
    }, []);

    useEffect(() => {
        if (selectedProfileId) {
            loadLabResults(selectedProfileId, fromDate, toDate, activeStatusTab, currentPage);
            const params = {
                patientId: selectedProfileId,
                status: activeStatusTab,
                fromDate: fromDate,
                toDate: toDate,
                page: currentPage,
                pageSize: pageSize
            };
            setSearchParams(params);
        }
    }, [selectedProfileId, fromDate, toDate, activeStatusTab, currentPage, pageSize, setSearchParams]);

    const totalPages = Math.max(1, Math.ceil(totalResults / pageSize));

    const renderStatusBadge = (status) => {
        let text = 'Đã xác nhận';
        let colorClass = 'text-primary bg-primary bg-opacity-10';

        if (status === 'COMPLETED') {
            text = 'Đã xét nghiệm';
            colorClass = 'border-success text-success bg-success-subtle';
        } else if (status === 'CONFIRMED') {
            text = 'Đã xác nhận';
            colorClass = 'border-primary text-primary bg-primary-subtle';
        }
        else if (status === 'PENDING') {
            text = 'Chưa thanh toán';
            colorClass = 'border-warning text-warning bg-warning-subtle';
        }

        return (
            <Badge bg="transparent" className={`rounded-pill px-3 py-2 border ${colorClass}`}>
                {text}
            </Badge>
        );
    };

    return (
        <>
            <div className="d-flex flex-column min-vh-100 bg-light">
                <Header />
                {loading && (
                    <div className="position-fixed top-0 start-0 vw-100 vh-100 d-flex align-items-center justify-content-center bg-white bg-opacity-75" style={{ zIndex: 1060 }}>
                        <MySpinner />

                    </div>
                )}
                <Container className="py-4">
                    <div className="mb-4 pb-3 border-bottom">
                        <div className="d-flex flex-column flex-lg-row justify-content-between align-items-lg-end gap-3">
                            <div>
                                <h2 className="fw-bold mb-0 text-primary">Danh sách phiếu xét nghiệm</h2>
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
                                        style={{ width: '160px' }}
                                    />
                                </div>
                                <div>
                                    <Form.Label className="small text-muted mb-1">Đến ngày</Form.Label>
                                    <Form.Control
                                        type="date"
                                        value={toDate}
                                        className="rounded-3 shadow-sm px-3"
                                        onChange={(e) => setToDate(e.target.value)}
                                        style={{ width: '160px' }}
                                    />
                                </div>
                            </div>
                        </div>
                    </div>
                    {selectedProfileId && (
                        <>
                            <div className="mb-4 d-flex justify-content-start">
                                <Tabs
                                    activeKey={activeStatusTab}
                                    onSelect={(key) => setActiveStatusTab(key)}
                                    className="nav-pills px-1 py-1 rounded-4 bg-white d-inline-flex"
                                    style={{ boxShadow: '0 12px 30px rgba(13,110,253,0.04)' }}
                                >
                                    <Tab eventKey="all" title="Tất cả" tabClassName="rounded-pill px-3 py-2" />
                                    <Tab eventKey="PENDING" title="Chưa thanh toán" tabClassName="rounded-pill px-3 py-2" />
                                    <Tab eventKey="CONFIRMED" title="Đã xác nhận" tabClassName="rounded-pill px-3 py-2" />
                                    <Tab eventKey="COMPLETED" title="Đã xét nghiệm" tabClassName="rounded-pill px-3 py-2" />
                                </Tabs>
                            </div>

                            {labResults.length > 0 ? (
                                <div className="row g-3">
                                    {labResults.map(labResult => (
                                        <div key={labResult.id} className="col-md-6 col-lg-4">
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
                                                                    phiếu xét nghiệm
                                                                </h6>
                                                            </div>


                                                            <div>
                                                                {renderStatusBadge(labResult.status)}
                                                            </div>
                                                        </div>
                                                    </Card.Header>
                                                    <Card.Body className="d-flex flex-column px-4">
                                                        <div className="text-start">
                                                            <h5 className="fw-bold text-dark text-uppercase text-center mb-3">
                                                                {labResult.patientName || 'Không xác định'}
                                                            </h5>
                                                        </div>
                                                        <Row className="mb-3 g-2 p-3 rounded-3">

                                                            <Col md={6}>
                                                                <small className="text-muted">Bác sĩ chỉ định</small>
                                                                <div className="fw-semibold text-dark">{labResult.doctorName || 'N/A'}</div>
                                                            </Col>
                                                            <Col md={6} className="px-1">
                                                                <small className="text-muted">Ngày chỉ định</small>
                                                                <div className="fw-semibold text-dark">{labResult.createdAt || '-'}</div>
                                                            </Col>

                                                            <Col md={6}>
                                                                <small className="text-muted">Người thực hiện</small>
                                                                <div className="fw-semibold text-dark">{labResult.doctorTestName || '-'}</div>
                                                            </Col>
                                                            <Col md={6}>
                                                                <small className="text-muted">Ngày xét nghiệm</small>
                                                                <div className="fw-semibold text-dark">{labResult.testAt || '-'}</div>
                                                            </Col>
                                                        </Row>
                                                        <div className="mt-auto">
                                                            <Button
                                                                variant="outline-primary"
                                                                className="rounded-pill px-3 py-2 w-100 fw-medium"
                                                                onClick={() => handleSelectLabResult(labResult)}
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
                            ) : (
                                <div className="text-center text-muted py-4">
                                    <i className="bi bi-inbox fs-3 d-block mb-2 opacity-50"></i>
                                    Không có phiếu xét nghiệm nào.
                                </div>
                            )}


                            <div className="d-flex justify-content-center gap-2 mt-3">
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
                                                    onClick={() => setCurrentPage(page)}
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
                        </>
                    )}
                </Container>
                <Footer />
            </div>
        </>
    );
}
export default TestResults;