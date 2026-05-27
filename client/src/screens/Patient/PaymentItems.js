import { useEffect, useState } from "react";
import { authApis, endpoint } from "../../configs/Apis";
import { useParams } from "react-router-dom";
import { Button, Card, Col, Container, Modal, Row, Tab, Tabs } from "react-bootstrap";
import Footer from "../../components/Footer";
import Header from "../../components/Header";

const PaymentItems = () => {

    const { paymentId } = useParams();
    const [paymentItems, setPaymentItems] = useState([]);
    const [showPaymentModal, setShowPaymentModal] = useState(false);
    const [selectedInvoice, setSelectedInvoice] = useState(null);
    const [selectedMethod, setSelectedMethod] = useState('MOMO');
    const [activeTab, setActiveTab] = useState('pending');

    const loadPaymentItems = async () => {
        try {
            const res = await authApis().get(endpoint['payment-items'](paymentId));
            setPaymentItems(res.data);
        } catch (err) {
            console.log(err);
        }
    };


    useEffect(() => {
        loadPaymentItems();
    }, []);


    const groupedInvoicesObject = paymentItems.reduce((acc, item) => {
        const type = item.type;

        if (!acc[type]) {
            acc[type] = {
                type: type,
                status: item.status,
                createdAt: item.createdAt,
                paidAt: item.paidAt,
                method: item.method,
                stransId: item.stransId,
                totalAmount: 0,
                count: 0,
                details: [] 
            };
        }

        acc[type].totalAmount += item.amount;
        acc[type].count += 1;

       
        const serviceName = item.testName || (type === 'APPOINTMENT' ? 'Khám chuyên khoa' : 'Dịch vụ y tế');

        
        acc[type].details.push({
            id: item.id,
            name: serviceName,
            price: item.amount
        });

        return acc;
    }, {});

    const invoicesArray = Object.values(groupedInvoicesObject);
    const unpaidInvoices = invoicesArray.filter(invoice => invoice.status === 'PENDING');
    const paidInvoices = invoicesArray.filter(invoice => invoice.status !== 'PENDING');

    const translateType = (type) => {
        switch (type) {
            case 'APPOINTMENT': return 'Phí khám bệnh';
            case 'LAB_TEST': return 'Phí xét nghiệm';
            case 'PRESCRIPTION': return 'Tiền thuốc';
            default: return type;
        }
    };


    const handleConfirmPayment = async () => {
        
        if (!selectedInvoice || !selectedInvoice.details) {
            alert("Dữ liệu hóa đơn không hợp lệ!");
            return;
        }

        try {
            
            const itemIds = selectedInvoice.details.map(detail => detail.id);

           
            const orderInfo = `Thanh toán ${translateType(selectedInvoice.type).toLowerCase()}`;
            
            const formData = new URLSearchParams();
            formData.append("method", selectedMethod); 
            formData.append("orderInfo", orderInfo);

           
            itemIds.forEach(id => formData.append("itemIds", id));

           
            const res = await authApis().post(endpoint['create-payment'], formData, {
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                }
            });

            
            const payUrl = res.data.payUrl; 
            if (payUrl) {
               
                setShowPaymentModal(false);
                window.location.href = payUrl;
            } else {
                console.error("Không có payUrl trong kết quả:", res.data);
                alert("Không thể tạo giao dịch. Vui lòng thử lại!");
            }

        } catch (err) {
            console.error("Lỗi gọi API thanh toán:", err);
            alert("Lỗi kết nối đến máy chủ thanh toán. Hãy kiểm tra lại mạng hoặc thử lại sau.");
        }
    };

    const openPaymentModal = (invoice) => {
        setSelectedInvoice(invoice);
        setSelectedMethod('MOMO'); 
        setShowPaymentModal(true);
    };

    return (
        <>
            <div className="d-flex flex-column min-vh-100 bg-light">
                <Header />
                <main className="flex-grow-1 py-5">
                    <Container>
                        <div className="d-flex flex-column flex-md-row justify-content-between align-items-start align-items-md-end mb-4 gap-3 border-bottom pb-3">
                            <div>
                                <h2 className="fw-bold mb-1">Danh sách hóa đơn</h2>
                                
                            </div>
                            
                        </div>

                        <Card className="shadow-sm rounded-4 border-0 overflow-hidden mb-4">
                            <Card.Body className="p-0">
                                <Tabs
                                    activeKey={activeTab}
                                    onSelect={(k) => setActiveTab(k)}
                                    className="nav-pills px-3 py-2 border border-1 rounded-4 bg-white"
                                    variant="pills"
                                    mountOnEnter
                                    unmountOnExit
                                >
                                    <Tab tabClassName="rounded-pill px-4 py-2 me-2 fw-semibold" eventKey="pending" title={`Chưa thanh toán (${unpaidInvoices.length})`}>
                                        <Row className="g-4 mt-3 px-3 pb-4">
                                            {paymentItems.length === 0 ? (
                                                <Col>
                                                    <div className="text-center p-5 bg-white rounded border shadow-sm">
                                                        <h5 className="text-muted">Hiện chưa có hóa đơn nào.</h5>
                                                    </div>
                                                </Col>
                                            ) : unpaidInvoices.length === 0 ? (
                                                <Col>
                                                    <div className="text-center p-5 bg-white rounded border shadow-sm">
                                                        <h5 className="text-muted">Không có hóa đơn chưa thanh toán.</h5>
                                                    </div>
                                                </Col>
                                            ) : unpaidInvoices.map((invoice, index) => (
                                                <Col key={index} xs={12} md={6} lg={4}>
                                                    <Card className="h-100 shadow border-0 rounded-4" style={{ border: '1px solid rgba(13,110,253,0.12)' }}>
                                                        <Card.Body>
                                                            <div className="d-flex justify-content-between align-items-start mb-3">
                                                                <div>
                                                                    <div className="text-uppercase text-primary small fw-bold mb-2">{translateType(invoice.type)}</div>
                                                                    <div className="fs-4 fw-bold">{invoice.totalAmount.toLocaleString('vi-VN')} VNĐ</div>
                                                                </div>
                                                                <span className="badge bg-warning text-dark py-2 px-3 rounded-pill">Chưa thanh toán</span>
                                                            </div>
                                                            <div className="mb-3 p-3 bg-light rounded-4">
                                                                <div className="text-muted small mb-2">Chi tiết dịch vụ</div>
                                                                {invoice.details?.map((detail, idx) => (
                                                                    <div key={idx} className="d-flex justify-content-between mb-2">
                                                                        <span className="text-truncate pe-2" title={detail.name}>{detail.name}</span>
                                                                        <span className="fw-semibold">{detail.price.toLocaleString('vi-VN')} VNĐ</span>
                                                                    </div>
                                                                ))}
                                                            </div>
                                                            <div className="mb-3 text-muted small">
                                                                <div className="d-flex justify-content-between mb-2"><span>Ngày tạo</span><span>{invoice.createdAt}</span></div>
                                                                {invoice.method && <div className="d-flex justify-content-between"><span>Phương thức</span><span>{invoice.method}</span></div>}
                                                            </div>
                                                        </Card.Body>
                                                        <Card.Footer className="d-flex justify-content-end bg-transparent border-0 mt-auto px-0 pb-0 pt-3">
                                                            <Button variant="primary" className="mb-3 me-3 w-50 rounded-4 fw-bold border-0 header-cta header-cta-primary" style={{ minHeight: '48px' }} onClick={() => openPaymentModal(invoice)}>
                                                                Thanh toán ngay
                                                            </Button>
                                                        </Card.Footer>
                                                    </Card>
                                                </Col>
                                            ))}
                                        </Row>
                                    </Tab>
                                    <Tab tabClassName="rounded-pill px-4 py-2 me-2 fw-semibold" eventKey="paid" title={`Đã thanh toán (${paidInvoices.length})`}>
                                        <Row className="g-4 mt-3 px-3 pb-4">
                                            {paymentItems.length === 0 ? (
                                                <Col>
                                                    <div className="text-center p-5 bg-white rounded border shadow-sm">
                                                        <h5 className="text-muted">Hiện chưa có hóa đơn nào.</h5>
                                                    </div>
                                                </Col>
                                            ) : paidInvoices.length === 0 ? (
                                                <Col>
                                                    <div className="text-center p-5 bg-white rounded border shadow-sm">
                                                        <h5 className="text-muted">Không có hóa đơn đã thanh toán.</h5>
                                                    </div>
                                                </Col>
                                            ) : paidInvoices.map((invoice, index) => (
                                                <Col key={index} xs={12} md={6} lg={4}>
                                                    <Card className="h-100 shadow border-0 rounded-4" style={{ border: '1px solid rgba(33,37,41,0.08)' }}>
                                                        <Card.Body>
                                                            <div className="d-flex justify-content-between align-items-start mb-3">
                                                                <div>
                                                                    <div className="text-uppercase text-secondary small fw-bold mb-2">{translateType(invoice.type)}</div>
                                                                    <div className="fs-4 fw-bold text-success">{invoice.totalAmount.toLocaleString('vi-VN')} VNĐ</div>
                                                                </div>
                                                                <span className="badge bg-success py-2 px-3 rounded-pill">Đã thanh toán</span>
                                                            </div>
                                                            <div className="mb-3 p-3 bg-light rounded-4">
                                                                <div className="text-muted small mb-2">Chi tiết dịch vụ</div>
                                                                {invoice.details?.map((detail, idx) => (
                                                                    <div key={idx} className="d-flex justify-content-between mb-2">
                                                                        <span className="text-truncate pe-2" title={detail.name}>{detail.name}</span>
                                                                        <span className="fw-semibold">{detail.price.toLocaleString('vi-VN')} VNĐ</span>
                                                                    </div>
                                                                ))}
                                                            </div>
                                                            <div className="mb-3 text-muted small">
                                                                <div className="d-flex justify-content-between mb-2"><span className="fw-bold">Ngày tạo</span><span>{invoice.createdAt}</span></div>
                                                                {invoice.paidAt && <div className="d-flex justify-content-between"><span className="fw-bold">Ngày thanh toán</span><span>{invoice.paidAt}</span></div>}
                                                                {invoice.method && <div className="d-flex justify-content-between"><span className="fw-bold">Phương thức</span><span>{invoice.method}</span></div>}
                                                            </div>
                                                        </Card.Body>
                                                       
                                                    </Card>
                                                </Col>
                                            ))}
                                        </Row>
                                    </Tab>
                                </Tabs>
                            </Card.Body>
                        </Card>
                        <Modal
                            show={showPaymentModal}
                            onHide={() => setShowPaymentModal(false)}
                            centered
                            dialogClassName="rounded-5"
                            contentClassName="overflow-hidden"
                        >
                            <Modal.Header closeButton className="border-bottom-0 pb-0">
                                <Modal.Title className="fw-bold">Chọn phương thức thanh toán</Modal.Title>
                            </Modal.Header>
                            <Modal.Body className="pt-2 px-4 pb-4 bg-white">
                               

                                <div className="d-grid gap-3">
                                    <label
                                        className={`border rounded-4 p-3 d-flex align-items-center gap-3 transition-all ${selectedMethod === 'MOMO' ? 'border-primary bg-light' : 'border-secondary-subtle bg-white'}`}
                                        style={{ cursor: 'pointer', boxShadow: selectedMethod === 'MOMO' ? '0 10px 30px rgba(13,110,253,0.08)' : 'none' }}
                                    >
                                        <input
                                            type="radio"
                                            name="paymentMethod"
                                            className="form-check-input mt-0"
                                            checked={selectedMethod === 'MOMO'}
                                            onChange={() => setSelectedMethod('MOMO')}
                                            style={{ transform: 'scale(1.2)' }}
                                        />
                                        <img src="/logo_momo.png" alt="MoMo" width="45" className="rounded-3" />
                                        <div>
                                            <div className="fw-semibold">Ví điện tử MoMo</div>
                                            <div className="text-muted small">Thanh toán nhanh qua MoMo</div>
                                        </div>
                                    </label>

                                    <label
                                        className={`border rounded-4 p-3 d-flex align-items-center gap-3 transition-all ${selectedMethod === 'VNPAY' ? 'border-primary bg-light' : 'border-secondary-subtle bg-white'}`}
                                        style={{ cursor: 'pointer', boxShadow: selectedMethod === 'VNPAY' ? '0 10px 30px rgba(13,110,253,0.08)' : 'none' }}
                                    >
                                        <input
                                            type="radio"
                                            name="paymentMethod"
                                            className="form-check-input mt-0"
                                            checked={selectedMethod === 'VNPAY'}
                                            onChange={() => setSelectedMethod('VNPAY')}
                                            style={{ transform: 'scale(1.2)' }}
                                        />
                                        <img src="/Logo-VNPAY.png" alt="VNPay" width="60" className="rounded-3" />
                                        <div>
                                            <div className="fw-semibold">Cổng thanh toán VNPay</div>
                                            <div className="text-muted small">Thanh toán bằng mã QR hoặc thẻ</div>
                                        </div>
                                    </label>
                                </div>
                            </Modal.Body>
                            <Modal.Footer className="border-top-0 pt-0 bg-white">
                                <Button variant="outline-secondary" className="rounded-4 px-4" onClick={() => setShowPaymentModal(false)}>
                                    Hủy
                                </Button>
                                <Button variant="primary" className="rounded-4 px-4 fw-bold border-0 header-cta header-cta-primary" onClick={handleConfirmPayment}>
                                    Tiến hành thanh toán
                                </Button>
                            </Modal.Footer>
                        </Modal>
                    </Container>
                </main>

                <Footer />
            </div>

        </>
    );
};

export default PaymentItems;