import { useEffect, useState } from "react";
import { Card, Col, Container, Row, Badge, Form, Spinner, Tabs, Tab, Button, Modal } from "react-bootstrap";
import { useNavigate, useParams } from "react-router-dom";
import Footer from "../../components/Footer";
import Header from "../../components/Header";
import { authApis, PAYMENT_ENDPOINTS, USER_ENDPOINTS } from "../../configs/Apis";

const PaymentDetail = () => {
    const { patientId } = useParams();
    const navigate = useNavigate();
    const [payments, setPayments] = useState([]);
    const [patientProfiles, setPatientProfiles] = useState([]);
    const [selectedPatientId, setSelectedPatientId] = useState(patientId || "");
    const [loadingProfiles, setLoadingProfiles] = useState(true);
    const [loadingPayments, setLoadingPayments] = useState(false);
    const [activeTab, setActiveTab] = useState('unpaid');
    const [showPaymentModal, setShowPaymentModal] = useState(false);
    const [selectedPaymentMethod, setSelectedPaymentMethod] = useState('CASH');
    const [currentInvoice, setCurrentInvoice] = useState(null);
    const [testDetails, setTestDetails] = useState([]);
    const [testNamesMap, setTestNamesMap] = useState({});

    const loadPatientProfiles = async () => {
        try {
            setLoadingProfiles(true);
            const res = await authApis().get(USER_ENDPOINTS.PATIENT_PROFILES);
            const profiles = res.data || [];
            setPatientProfiles(profiles);

            if (!patientId && profiles.length > 0) {
                const firstPatientId = String(profiles[0].id);
                setSelectedPatientId(firstPatientId);
                navigate(`/patient/payment/${firstPatientId}`, { replace: true });
            } else if (patientId) {
                setSelectedPatientId(String(patientId));
            }
        } catch (err) {
            console.log(err);
        } finally {
            setLoadingProfiles(false);
        }
    };

    const loadPayments = async (id) => {
        if (!id) {
            setPayments([]);
            return;
        }
        setLoadingPayments(true);
        try {
            const res = await authApis().get(PAYMENT_ENDPOINTS.HISTORY(id));
            setPayments(res.data || []);
            loadTestNames(res.data);
        } catch (err) {
            console.log(err);
            setPayments([]);
        } finally {
            setLoadingPayments(false);
        }
    };


    const loadTestNames = async (paymentsList) => {
        const idsToFetch = new Set();


        paymentsList.forEach(p => {
            p.paymentItems?.forEach(item => {
                if (String(item.itemType) === 'LAB_TEST' && item.referenceId && !testNamesMap[item.referenceId]) {
                    idsToFetch.add(item.referenceId);
                }
            });
        });

        if (idsToFetch.size === 0) return;

        try {

            const promises = Array.from(idsToFetch).map(id => authApis().get(`secure/test/${id}`));
            const responses = await Promise.all(promises);

            const newNames = { ...testNamesMap };
            responses.forEach(res => {
                if (res.data && res.data.id) {

                    newNames[res.data.id] = res.data.testName || res.data.name;
                }
            });


            setTestNamesMap(newNames);
        } catch (err) {
            console.error("Lỗi khi load tên xét nghiệm:", err);
        }
    };



    useEffect(() => {
        loadPatientProfiles();
    }, []);

    useEffect(() => {
        if (patientId) {
            setSelectedPatientId(String(patientId));
            loadPayments(patientId);
        }
    }, [patientId]);

    const handlePatientChange = (event) => {
        const value = event.target.value;
        setSelectedPatientId(value);
        if (value) {
            navigate(`/patient/payment/${value}`);
        }
    };

    const profileLabel = (profile) => {
        return profile.fullName || profile.name || profile.namePatient || profile.patientName || `Hồ sơ ${profile.id}`;
    };

    const isPaidInvoice = (payment) => {
        const status = String(payment.status || '').toLowerCase();
        return status === 'success' || status === 'paid' || status === 'đã thanh toán';
    };

    const paymentStatusVariant = (status) => {
        const lower = String(status || '').toLowerCase();
        if (lower === 'success' || lower === 'paid' || lower === 'đã thanh toán') return 'success';
        return 'warning';
    };

    const getItemLabel = (item) => {
        const type = String(item?.itemType || '').toUpperCase();

        if (type === 'LAB_TEST') {
            // Lấy tên từ State, nếu chưa load xong thì hiện "Phí xét nghiệm", load xong sẽ tự đổi tên
            if (item.referenceId && testNamesMap[item.referenceId]) {
                return testNamesMap[item.referenceId];
            }
            return item.itemName || 'Phí xét nghiệm (Đang tải...)';
        }

        switch (type) {
            case 'APPOINTMENT': return 'Phí khám bệnh';
            case 'PRESCRIPTON':
            case 'PRESCRIPTION': return 'Đơn thuốc';
            default: return item?.itemName || item?.itemType || 'Dịch vụ y tế';
        }
    };

    const getInvoiceTitle = (payment) => {



        const type = String(payment.paymentItems[0].itemType || '').toUpperCase();

        switch (type) {
            case 'APPOINTMENT':
                return 'PHÍ KHÁM BỆNH';
            case 'LAB_TEST':
                return 'PHÍ XÉT NGHIỆM';
            case 'PRESCRIPTON':
            case 'PRESCRIPTION':
                return 'PHÍ MUA THUỐC';
            default:
                return 'HÓA ĐƠN DỊCH VỤ';
        }
    };

    const filteredPayments = payments.filter((payment) => {
        if (activeTab === 'paid') return isPaidInvoice(payment);
        return !isPaidInvoice(payment);
    });

    const handlePayClick = (payment) => {
        setCurrentInvoice(payment);
        setSelectedPaymentMethod('CASH');
        setShowPaymentModal(true);
    };

    const handleConfirmPayment = () => {
        if (!currentInvoice) {
            setShowPaymentModal(false);
            return;
        }
        setPayments((prev) => prev.map((payment) => payment.id === currentInvoice.id ? { ...payment, status: 'SUCCESS' } : payment));
        setShowPaymentModal(false);
        setCurrentInvoice(null);
    };

    const closePaymentModal = () => {
        setShowPaymentModal(false);
        setCurrentInvoice(null);
    };

    return (
        <>
            <div className="d-flex flex-column min-vh-100 bg-light">
                <Header />

                <main className="flex-grow-1 py-5">
                    <Container>
                        <div className="mb-4 pb-3 border-bottom">
                            <div className="d-flex flex-column flex-md-row justify-content-between align-items-start align-items-md-end gap-3">
                                <div>
                                    <h2 className="fw-bold mb-2 text-primary">Danh sách hóa đơn</h2>
                                </div>
                            </div>
                            <div className="w-100 w-md-auto">
                                {loadingProfiles ? (
                                    <div className="d-flex align-items-center gap-2 py-2 px-3 bg-white rounded shadow-sm">
                                        <Spinner animation="border" size="sm" />
                                        <span className="text-muted">Đang tải hồ sơ...</span>
                                    </div>
                                ) : (
                                    <Form.Select value={selectedPatientId} style={{ maxWidth: '250px' }} onChange={handlePatientChange} aria-label="Chọn hồ sơ bệnh nhân">
                                        <option value="">---Chọn hồ sơ bệnh nhân---</option>
                                        {patientProfiles.map((profile) => (
                                            <option key={profile.id} value={profile.id}>
                                                {profileLabel(profile)}
                                            </option>
                                        ))}
                                    </Form.Select>
                                )}
                            </div>
                        </div>

                        <div className="mb-3">
                            <Tabs
                                activeKey={activeTab}
                                onSelect={(key) => setActiveTab(key)}
                                className="nav-pills px-1 py-1 rounded-4 bg-white"
                                style={{ boxShadow: '0 12px 30px rgba(13,110,253,0.04)' }}
                            >
                                <Tab eventKey="unpaid" title="Chưa thanh toán" tabClassName="rounded-pill px-3 py-2" />
                                <Tab eventKey="paid" title="Đã thanh toán" tabClassName="rounded-pill px-3 py-2" />
                            </Tabs>
                        </div>

                        <Row className="g-4">
                            {loadingPayments ? (
                                <Col>
                                    <div className="text-center p-5 bg-white rounded border shadow-sm">
                                        <Spinner animation="border" />
                                        <div className="text-muted mt-3">Đang tải hóa đơn...</div>
                                    </div>
                                </Col>
                            ) : filteredPayments.length === 0 ? (
                                <Col>
                                    <div className="text-center p-5 bg-white rounded border shadow-sm">
                                        <h5 className="text-muted">Không có hóa đơn phù hợp.</h5>
                                    </div>
                                </Col>
                            ) : (
                                filteredPayments.map((p) => (
                                    <Col key={p.id} xs={12} md={6} lg={4}>
                                        <Card className="h-100 shadow-sm border-0" style={{ borderRadius: '20px', overflow: 'hidden' }}>
                                            <div className="h-100 d-flex flex-column bg-white">
                                                <Card.Header className="bg-white border-0 pt-4 px-4 pb-2">
                                                    <div className="d-flex justify-content-between align-items-start">

                                                        {/* Cột trái: Gồm Tiêu đề và Ngày tháng nằm dưới */}
                                                        <div>
                                                            <div className="text-success fw-bold fs-5 mb-1">
                                                                {getInvoiceTitle(p)}
                                                            </div>
                                                            <div className="text-muted small">
                                                                <i className="bi bi-calendar-event me-2"></i>
                                                                {p.createdDate || p.createdAt || 'Không có ngày'}
                                                            </div>
                                                        </div>

                                                        {/* Cột phải: Badge Trạng thái */}
                                                        <div>
                                                            <Badge bg={p.status === 'SUCCESS' ? 'success' : 'warning'} className="rounded-pill px-3 py-2 text-dark">
                                                                {p.status === 'SUCCESS' ? 'ĐÃ THANH TOÁN' : 'CHỜ THANH TOÁN'}
                                                            </Badge>
                                                        </div>

                                                    </div>
                                                </Card.Header>


                                                <Card.Body className="d-flex flex-column px-4 py-3">
                                                    <div className="mb-4 flex-grow-1">
                                                        <div className="fw-semibold text-secondary mb-3 small text-uppercase">Chi tiết dịch vụ</div>
                                                        {p.paymentItems && p.paymentItems.length > 0 ? (
                                                            <ul className="list-unstyled mb-0">
                                                                {p.paymentItems.map((item, idx) => (
                                                                    <li key={item.id || idx} className="mb-2 pb-2 border-bottom border-light">
                                                                        <div className="d-flex justify-content-between align-items-start">
                                                                            <div className="text-dark small fw-semibold pe-2">
                                                                                • {getItemLabel(item)}
                                                                            </div>
                                                                            <div className="text-muted small text-end fw-bold">
                                                                                {(item.amount || 0).toLocaleString('vi-VN')} đ
                                                                            </div>
                                                                        </div>
                                                                    </li>
                                                                ))}
                                                            </ul>
                                                        ) : (
                                                            <div className="text-muted small fst-italic">Không có chi tiết.</div>
                                                        )}
                                                    </div>

                                                    <div className="mt-auto pt-3 border-top">
                                                        <div className="d-flex justify-content-between align-items-center mb-3">
                                                            <span className="text-uppercase small fw-bold text-secondary">Tổng cộng:</span>
                                                            <span className="fs-5 fw-bold text-primary">
                                                                {(p.totalAmount || 0).toLocaleString('vi-VN')} đ
                                                            </span>
                                                        </div>

                                                        {!isPaidInvoice(p) && activeTab === 'unpaid' ? (
                                                            <Button
                                                                variant="primary"
                                                                className="w-100 rounded-pill py-2"
                                                                onClick={() => handlePayClick(p)}
                                                            >
                                                                Thanh toán
                                                            </Button>
                                                        ) : (
                                                            <div className="text-end text-success fw-semibold small">Đã thanh toán</div>
                                                        )}
                                                    </div>
                                                </Card.Body>
                                            </div>
                                        </Card>
                                    </Col>
                                ))
                            )}
                        </Row>
                    </Container>
                </main>

                <Footer />

                <Modal show={showPaymentModal} onHide={closePaymentModal} centered>
                    <Modal.Header closeButton>
                        <Modal.Title>Chọn phương thức thanh toán</Modal.Title>
                    </Modal.Header>
                    <Modal.Body>
                        <div className="mb-3">
                            <div className="small text-muted">Hóa đơn</div>
                            <div className="fw-semibold">#{currentInvoice?.id || '-'}</div>
                        </div>
                        <Form.Group className="mb-3">
                            <Form.Label>Phương thức</Form.Label>
                            <Form.Select
                                value={selectedPaymentMethod}
                                onChange={(event) => setSelectedPaymentMethod(event.target.value)}
                            >
                                <option value="CASH">Tiền mặt</option>
                                <option value="CARD">Thẻ ngân hàng</option>
                                <option value="MOMO">Momo</option>
                                <option value="ZALOPAY">ZaloPay</option>
                            </Form.Select>
                        </Form.Group>
                        <div className="border rounded-3 p-3 bg-light">
                            <div className="small text-muted">Số tiền cần thanh toán</div>
                            <div className="fw-bold fs-5">{(currentInvoice?.totalAmount || 0).toLocaleString('vi-VN')} đ</div>
                        </div>
                    </Modal.Body>
                    <Modal.Footer className="justify-content-between">
                        <Button variant="outline-secondary" onClick={closePaymentModal}>
                            Hủy
                        </Button>
                        <Button variant="primary" onClick={handleConfirmPayment}>
                            Xác nhận thanh toán
                        </Button>
                    </Modal.Footer>
                </Modal>
            </div>
        </>
    );
};

export default PaymentDetail;