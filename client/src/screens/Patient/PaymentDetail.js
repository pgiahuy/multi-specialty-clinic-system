import { useEffect, useState } from "react";
import { Card, Col, Container, Row, Badge, Form, Tabs, Tab, Button, Modal } from "react-bootstrap";
import { useNavigate } from "react-router-dom";
import Footer from "../../components/Footer";
import Header from "../../components/Header";
import { authApis, PAYMENT_ENDPOINTS, USER_ENDPOINTS } from "../../configs/Apis";
import MySpinner from "../../components/MySpinner";
import MyModal from "../../components/MyModal";
import { CheckCircleFill } from "react-bootstrap-icons";

const PAID_STATUSES = new Set(["success", "paid", "đã thanh toán"]);

const getInitialFromDate = () => {
    const date = new Date();
    date.setMonth(date.getMonth() - 1);
    return date.toISOString().slice(0, 10);
};

const getInitialToDate = () => {
    const date = new Date();
    return date.toISOString().slice(0, 10);
};

const normalizeStatus = (status) => String(status || "").toLowerCase();

const PaymentDetail = () => {
    const navigate = useNavigate();
    const [payments, setPayments] = useState([]);
    const [patientProfiles, setPatientProfiles] = useState([]);
    const [selectedPatientId, setSelectedPatientId] = useState(null);
    const [loadingPayments, setLoadingPayments] = useState(false);
    const [activeTab, setActiveTab] = useState('unpaid');
    const [showPaymentModal, setShowPaymentModal] = useState(false);
    const [showSuccessModal, setShowSuccessModal] = useState(false);
    const [successMessage, setSuccessMessage] = useState('Thanh toán đã được ghi nhận thành công.');
    const [selectedPaymentMethod, setSelectedPaymentMethod] = useState('CASH');
    const [currentInvoice, setCurrentInvoice] = useState(null);
    const [fromDate, setFromDate] = useState(getInitialFromDate);
    const [toDate, setToDate] = useState(getInitialToDate);
    const [testNamesMap, setTestNamesMap] = useState({});

    const loadPatientProfiles = async () => {
        try {
            const res = await authApis().get(USER_ENDPOINTS.PATIENT_PROFILES);
            const profiles = res.data || [];
            setPatientProfiles(profiles);

            // if (!patientId && profiles.length > 0) {
            //     const firstPatientId = String(profiles[0].id);
            //     setSelectedPatientId(firstPatientId);
            //     navigate(`/patient/payments`, { replace: true });
            // } else if (patientId) {
            //     setSelectedPatientId(String(patientId));
            // }
        } catch (err) {
            console.log(err);
        }
    };

    const loadPayments = async (patientId, startDate, endDate) => {

        setLoadingPayments(true);
        try {
            const params = {};

            if (patientId) params.patientId = patientId;
            if (startDate) params.startDate = startDate;
            if (endDate) params.endDate = endDate;

            const res = await authApis().get(PAYMENT_ENDPOINTS.HISTORY, { params });
            const paymentList = res.data || [];
            setPayments(paymentList);
            loadTestNames(paymentList);
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

            setTestNamesMap(prev => {
                const newNames = { ...prev };

                responses.forEach(res => {
                    if (res.data && res.data.id) {
                        newNames[res.data.id] = res.data.testName || res.data.name;
                    }
                });

                return newNames;
            });
        } catch (err) {
            console.error("Lỗi khi load tên xét nghiệm:", err);
        }
    };



    useEffect(() => {
        loadPatientProfiles();
    }, []);

    useEffect(() => {
        loadPayments(selectedPatientId, fromDate, toDate);
    }, [selectedPatientId, fromDate, toDate]);

    const handlePatientChange = (event) => {
        const value = event.target.value;
        setSelectedPatientId(value);

    };

    const profileLabel = (profile) => {
        return profile.fullName || profile.name || profile.namePatient || profile.patientName || `Hồ sơ ${profile.id}`;
    };

    const isPaidInvoice = (payment) => {
        return PAID_STATUSES.has(normalizeStatus(payment.status));
    };

    const paymentStatusVariant = (status) => {
        if (PAID_STATUSES.has(normalizeStatus(status))) return 'success';
        return 'warning';
    };

    const getItemLabel = (item) => {
        const type = String(item?.itemType || '').toUpperCase();

        if (type === 'LAB_TEST') {

            if (item.referenceId && testNamesMap[item.referenceId]) {
                return testNamesMap[item.referenceId];
            }
            return item.itemName || 'Phí xét nghiệm (Đang tải...)';
        }

        switch (type) {
            case 'APPOINTMENT': return 'Phí khám bệnh';
            case 'PRESCRIPTION': return 'Đơn thuốc';
            default: return item?.itemName || item?.itemType || 'Dịch vụ y tế';
        }
    };

    const getInvoiceTitle = (payment) => {
        const firstItemType = payment?.paymentItems?.[0]?.itemType;
        const type = String(firstItemType || '').toUpperCase();

        switch (type) {
            case 'APPOINTMENT':
                return 'PHÍ KHÁM BỆNH';
            case 'LAB_TEST':
                return 'PHÍ XÉT NGHIỆM';

            case 'PRESCRIPTION':
                return 'PHÍ MUA THUỐC';
            default:
                return 'HÓA ĐƠN DỊCH VỤ';
        }
    };



    const parseVietnameseDate = (dateString) => {
        if (!dateString) return null;


        const parts = dateString.split(' ');
        const datePart = parts[0];
        const timePart = parts[1] || '00:00:00';


        const [day, month, year] = datePart.split('/');


        if (!day || !month || !year) return new Date(dateString);


        return new Date(`${year}-${month}-${day}T${timePart}`);
    };

    const isPaymentInRange = (payment) => {
        const rawDate = payment.createdDate || payment.createdAt || '';


        const invoiceDate = parseVietnameseDate(rawDate);

        if (!invoiceDate || Number.isNaN(invoiceDate.getTime())) return true;

        if (fromDate) {
            const from = new Date(fromDate);
            from.setHours(0, 0, 0, 0);
            if (invoiceDate < from) return false;
        }

        if (toDate) {
            const to = new Date(toDate);
            to.setHours(23, 59, 59, 999);
            if (invoiceDate > to) return false;
        }

        return true;
    };

    const filteredPayments = payments.filter((payment) => {
        const shouldShowPaid = activeTab === 'paid';
        if (shouldShowPaid ? !isPaidInvoice(payment) : isPaidInvoice(payment)) return false;
        return isPaymentInRange(payment);
    });

    const handlePayClick = (payment) => {
        setCurrentInvoice(payment);
        setSelectedPaymentMethod('CASH');
        setShowPaymentModal(true);
    };

    const handleConfirmPayment = async () => {
        if (!currentInvoice) {
            setShowPaymentModal(false);
            return;
        }

        try {
            const formData = new URLSearchParams();
            formData.append("method", selectedPaymentMethod);
            formData.append("paymentId", currentInvoice.id);
            formData.append("orderInfo", `Thanh toan hoa don ${currentInvoice.id}`);

            const res = await authApis().post(PAYMENT_ENDPOINTS.PAY, formData, {
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                }
            });


            const payUrl = res.data.payUrl || res.data.shortLink;

            if (payUrl) {
                setShowPaymentModal(false);
                window.location.href = payUrl;
            } else {
                setPayments((prev) =>
                    prev.map((payment) =>
                        payment.id === currentInvoice.id ? { ...payment, status: 'SUCCESS' } : payment
                    )
                );
                setShowPaymentModal(false);
                setCurrentInvoice(null);
                setSuccessMessage('Thanh toán thành công!');
                setShowSuccessModal(true);
            }

        } catch (error) {
            console.error("Lỗi thanh toán:", error);


        }
    };

    const closePaymentModal = () => {
        setShowPaymentModal(false);
        setCurrentInvoice(null);
    };

    return (
        <>
            <div className="d-flex flex-column min-vh-100 bg-light">
                <Header />


                <Container className="py-4">
                    <style>{`.nav-pills .nav-link{transition: transform .12s ease, box-shadow .12s ease; cursor: pointer;}
                                .nav-pills .nav-link:hover{transform: translateY(-4px); box-shadow: 0 10px 30px rgba(13,110,253,0.12);} 
                                .nav-pills .nav-link.active{transform: none; box-shadow: none;}`}</style>

                    <div className="mb-4 pb-3 border-bottom">
                        <div className="d-flex flex-column flex-md-row justify-content-between align-items-md-end gap-3">


                            <div>
                                <h2 className="fw-bold mb-2 text-primary">Danh sách hóa đơn</h2>
                            </div>


                            <div className="d-flex flex-column flex-md-row align-items-md-end gap-3">


                                <div>
                                    <Form.Label className="small text-muted mb-1">Bệnh nhân</Form.Label>

                                    <Form.Select
                                        value={selectedPatientId}
                                        className="rounded-3 shadow-sm px-3"
                                        style={{ minWidth: '250px' }}
                                        onChange={(e) => handlePatientChange(e)}
                                        aria-label="Chọn hồ sơ bệnh nhân"
                                    >
                                        <option value="">---Chọn hồ sơ bệnh nhân---</option>
                                        {patientProfiles.map((profile) => (
                                            <option key={profile.id} value={profile.id}>
                                                {profileLabel(profile)}
                                            </option>
                                        ))}
                                    </Form.Select>

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


                    <div className="mb-4 d-flex justify-content-start">
                        <Tabs
                            activeKey={activeTab}
                            onSelect={(key) => setActiveTab(key)}
                            className="nav-pills px-1 py-1 rounded-4 bg-white d-inline-flex"
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
                                    <MySpinner />
                                    <div className="text-muted mt-3">Đang tải hóa đơn...</div>
                                </div>
                            </Col>
                        ) : filteredPayments.length === 0 ? (
                            <Col>
                                <div className="text-center text-muted py-5">
                                    Không có hóa đơn phù hợp.
                                </div>
                            </Col>
                        ) : (
                            filteredPayments.map((p) => (
                                <Col key={p.id} xs={12} md={6} lg={4}>
                                    <Card
                                        className="h-100 shadow-sm border-0"
                                        style={{
                                            borderRadius: '20px',
                                            overflow: 'hidden',
                                            transition: 'transform 0.2s ease, box-shadow 0.2s ease',
                                            cursor: 'pointer'
                                        }}
                                        onMouseEnter={(e) => {
                                            e.currentTarget.style.transform = 'translateY(-5px)';
                                            e.currentTarget.classList.replace('shadow-sm', 'shadow');
                                        }}
                                        onMouseLeave={(e) => {
                                            e.currentTarget.style.transform = 'translateY(0)';
                                            e.currentTarget.classList.replace('shadow', 'shadow-sm');
                                        }}
                                    >
                                        <div className="h-100 d-flex flex-column bg-white">


                                            <Card.Header className="bg-white border-0 p-4 pb-0">
                                                <div className="d-flex justify-content-between align-items-start gap-3">
                                                    <div>
                                                        <h5 className="text-primary fw-bold mb-2" style={{ lineHeight: '1.4' }}>
                                                            {getInvoiceTitle(p)}
                                                        </h5>
                                                        <div className="text-secondary small d-flex align-items-center">
                                                            <i className="bi bi-clock-history me-2"></i>
                                                            {p.createdDate || p.createdAt || 'Chưa cập nhật ngày'}
                                                        </div>
                                                    </div>
                                                    <div>
                                                        <Badge
                                                            bg="transparent"
                                                            className={`rounded-pill px-3 py-2 border ${p.status === 'SUCCESS'
                                                                ? 'border-success text-success bg-success-subtle'
                                                                : 'border-warning text-warning bg-warning-subtle'
                                                                }`}
                                                        >
                                                            {p.status === 'SUCCESS' ? 'ĐÃ THANH TOÁN' : 'CHỜ THANH TOÁN'}
                                                        </Badge>
                                                    </div>
                                                </div>
                                            </Card.Header>


                                            <Card.Body className="d-flex flex-column px-4 py-4">

                                                <div className="mb-auto">
                                                    {p.paymentItems && p.paymentItems.length > 0 && String(p.paymentItems[0].itemType).toUpperCase() === 'APPOINTMENT' && (
                                                        <Button
                                                            variant="outline-primary"
                                                            className="rounded-pill px-3 py-2 w-100 fw-medium"
                                                            onClick={() => navigate(`/appointment/${p.paymentItems[0].referenceId}`)}
                                                        >
                                                            <i className="bi bi-info-circle me-2"></i> Xem chi tiết
                                                        </Button>
                                                    )}
                                                </div>


                                                <div className="mt-4 pt-3 border-top border-light">
                                                    <div className="d-flex justify-content-between align-items-baseline mb-3">
                                                        <span className="text-secondary small fw-medium">Tổng cộng:</span>
                                                        <span className="fs-5 fw-bold text-danger">
                                                            {(p.totalAmount || 0).toLocaleString('vi-VN')} đ
                                                        </span>
                                                    </div>

                                                    {!isPaidInvoice(p) && activeTab === 'unpaid' && (
                                                        <Button
                                                            variant="primary"
                                                            className="w-100 rounded-pill py-2 fw-bold shadow-sm"
                                                            onClick={() => handlePayClick(p)}
                                                        >
                                                            <i className="bi bi-credit-card me-2"></i> Thanh toán ngay
                                                        </Button>
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


                <Footer />

                <Modal show={showPaymentModal} onHide={closePaymentModal} centered>
                    <Modal.Header closeButton className="border-0 pb-0">
                        <Modal.Title className="fw-bold fs-5">Xác nhận thanh toán</Modal.Title>
                    </Modal.Header>
                    <Modal.Body>
                        <div className="mb-4">
                            <div className="small text-muted mb-2">Chọn phương thức thanh toán</div>


                            <div
                                className={`d-flex align-items-center p-3 mb-2 border rounded-3 ${selectedPaymentMethod === 'CASH' ? 'border-primary bg-primary bg-opacity-10' : 'bg-white'}`}
                                style={{ cursor: 'pointer', transition: 'all 0.2s' }}
                                onClick={() => setSelectedPaymentMethod('CASH')}
                            >
                                <div className="flex-grow-1">
                                    <div className="fw-bold text-dark">Tiền mặt tại quầy</div>
                                    <div className="small text-muted">Thanh toán trực tiếp cho thu ngân</div>
                                </div>
                                <div>
                                    <Form.Check type="radio" checked={selectedPaymentMethod === 'CASH'} readOnly />
                                </div>
                            </div>


                            <div
                                className={`d-flex align-items-center p-3 mb-2 border rounded-3 ${selectedPaymentMethod === 'MOMO' ? 'border-danger bg-danger bg-opacity-10' : 'bg-white'}`}
                                style={{ cursor: 'pointer', transition: 'all 0.2s' }}
                                onClick={() => setSelectedPaymentMethod('MOMO')}
                            >
                                <div className="flex-grow-1">
                                    <div className="fw-bold" style={{ color: '#a50064' }}>Ví điện tử MoMo</div>
                                    <div className="small text-muted">Thanh toán quét mã QR</div>
                                </div>
                                <div>
                                    <Form.Check type="radio" checked={selectedPaymentMethod === 'MOMO'} readOnly style={{ accentColor: '#a50064' }} />
                                </div>
                            </div>


                            <div
                                className={`d-flex align-items-center p-3 border rounded-3 ${selectedPaymentMethod === 'VNPAY' ? 'border-info bg-info bg-opacity-10' : 'bg-white'}`}
                                style={{ cursor: 'pointer', transition: 'all 0.2s' }}
                                onClick={() => setSelectedPaymentMethod('VNPAY')}
                            >
                                <div className="flex-grow-1">

                                    <div className="fw-bold" style={{ color: '#005baa' }}>Cổng thanh toán VNPAY</div>
                                    <div className="small text-muted">Thẻ ATM / Thẻ tín dụng / QR Code</div>
                                </div>
                                <div>
                                    <Form.Check type="radio" checked={selectedPaymentMethod === 'VNPAY'} readOnly style={{ accentColor: '#005baa' }} />
                                </div>
                            </div>
                        </div>

                        <div className="border rounded-3 p-3 bg-light">
                            <div className="small text-muted text-center mb-1">Số tiền cần thanh toán</div>
                            <div className="fw-bold fs-3 text-center text-primary">
                                {(currentInvoice?.totalAmount || 0).toLocaleString('vi-VN')} đ
                            </div>
                        </div>
                    </Modal.Body>

                    <Modal.Footer className="justify-content-between border-0 pt-0">
                        <Button variant="outline-secondary" className="px-4 rounded-pill" onClick={closePaymentModal}>
                            Hủy bỏ
                        </Button>
                        <Button
                            className="px-4 rounded-pill fw-bold border-0 text-white transition-all"
                            style={{

                                backgroundColor:
                                    selectedPaymentMethod === 'MOMO' ? '#a50064' :
                                        selectedPaymentMethod === 'VNPAY' ? '#005baa' :
                                            '#0d6efd'
                            }}
                            onClick={handleConfirmPayment}
                        >
                            Thanh toán ngay
                        </Button>
                    </Modal.Footer>
                </Modal>

                <MyModal
                    show={showSuccessModal}
                    onHide={() => setShowSuccessModal(false)}
                    title="Thông báo"
                    cancelText="Đóng"
                >
                    <div className="text-center py-3">
                        <h4 className="fw-semibold mb-2">{successMessage}</h4>
                        <CheckCircleFill color="green" size={50} />
                    </div>
                </MyModal>
            </div>
        </>
    );
};

export default PaymentDetail;