import { useCallback, useEffect, useMemo, useState } from "react";
import { Card, Col, Container, Row, Badge, Button } from "react-bootstrap";
import { useNavigate, useParams } from "react-router-dom";
import Header from "../../components/Header";
import MySpinner from "../../components/MySpinner";
import { authApis, PAYMENT_ENDPOINTS } from "../../configs/Apis";
import PrescriptionDetail from "./PrescriptionDetail";
import moment from "moment";


const styles = {
    pageContainer: {
        minHeight: '80vh',
    },
    listPanel: {
        maxHeight: '78vh',
    },
    listScroll: {
        maxHeight: 'calc(78vh - 80px)',
        overflowY: 'auto',
    },
    listItem: {
        width: '100%',
        textAlign: 'left',
        border: 'none',
        borderBottom: '1px solid rgba(0,0,0,.06)',
        borderRadius: 0,
        padding: '1rem 1.2rem',
        backgroundColor: 'transparent',
        transition: 'background-color 0.2s ease, transform 0.15s ease, box-shadow 0.15s ease',
    },
    listItemActive: {
        backgroundColor: 'rgba(13,110,253,0.14)',
        boxShadow: 'inset 0 0 0 1px rgba(13,110,253,0.2)',
    },
    detailPanel: {
        minHeight: '78vh',
    },
};

const Prescriptions = () => {
    const navigate = useNavigate();
    const { prescriptionId } = useParams();
    const [prescriptions, setPrescriptions] = useState([]);
    const [selectedPrescriptionId, setSelectedPrescriptionId] = useState(null);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);
    const [payment, setPayment] = useState(null);

    const selectedPrescription = useMemo(
        () => prescriptions.find((item) => String(item.id) === String(selectedPrescriptionId)),
        [prescriptions, selectedPrescriptionId]
    );

    const loadPrescriptions = async () => {
        setLoading(true);
        setError(null);
        try {
            const response = await authApis().get("secure/prescriptions");
            setPrescriptions(Array.isArray(response.data) ? response.data : []);
        } catch (err) {
            console.error("Lỗi khi tải đơn thuốc:", err);
            setError("Không thể tải danh sách đơn thuốc. Vui lòng thử lại sau.");
            setPrescriptions([]);
        } finally {
            setLoading(false);
        }
    };

    const loadPayment = useCallback(async (id) => {
        if (!id) return;
        try {
            const response = await authApis().get(PAYMENT_ENDPOINTS.HISTORY);
            const allPayments = Array.isArray(response.data) ? response.data : [];
            const prescriptionPayment = allPayments.find((p) => {
                if (p.paymentItems && Array.isArray(p.paymentItems)) {
                    return p.paymentItems.some(
                        (item) =>
                            item.itemType === "PRESCRIPTION" &&
                            String(item.referenceId) === String(id) // So sánh dạng String cho an toàn
                    );
                }
                return false;
            });
            setPayment(prescriptionPayment || null);
        } catch (err) {
            console.error("Lỗi khi tải thông tin thanh toán:", err);
            setPayment(null);
        }
    }, []);

    useEffect(() => {
        loadPrescriptions();
    }, []);

    useEffect(() => {
        if (!prescriptionId) {
            setSelectedPrescriptionId(null);
            setPayment(null);
            return;
        }
        if (prescriptions.length > 0) {
            setSelectedPrescriptionId(prescriptionId);
            loadPayment(prescriptionId);
        }
    }, [prescriptionId, prescriptions, loadPayment]);

    const handleSelectPrescription = (id) => {
        setSelectedPrescriptionId(String(id));
        navigate(`/patient/prescriptions/${id}`);
    };

    return (
        <>
            <Header />
            <Container className="py-4" style={styles.pageContainer}>
                <div className="mb-3">
                    <h2 className="fw-bold text-primary">Đơn thuốc</h2>
                </div>

                <Row className="g-4">
                    <Col lg={3}>
                        <Card className="h-100 shadow-sm" style={styles.listPanel}>
                            <Card.Header className="bg-white py-3">
                                <div className="d-flex flex-row align-items-center gap-2 justify-content-between">
                                    <div className="small text-muted" >Danh sách đơn thuốc</div>

                                </div>
                            </Card.Header>
                            <Card.Body className="p-0">
                                {prescriptions.length === 0 ? (
                                    <div className="p-4 text-center text-muted">
                                        {loading ? "Đang tải đơn thuốc..." : "Không có đơn thuốc nào."}
                                    </div>
                                ) : (
                                    <div style={styles.listScroll}>
                                        {prescriptions.map((prescription) => {
                                            const isActive = String(prescription.id) === String(selectedPrescriptionId);
                                            const itemStyle = isActive
                                                ? { ...styles.listItem, ...styles.listItemActive }
                                                : styles.listItem;
                                            return (
                                                <button
                                                    key={prescription.id}
                                                    type="button"
                                                    className="list-group-item list-group-item-action py-3 px-4"
                                                    style={itemStyle}
                                                    onClick={() => handleSelectPrescription(prescription.id)}
                                                >
                                                    <div className="d-flex justify-content-between align-items-start">
                                                        <div>
                                                            <div className="fw-semibold m-0 p-0">Đơn #{prescription.id}</div>
                                                            <div className="small">
                                                                Bác sĩ: <span className="fw-semibold">{prescription.doctorName || 'Chưa xác định'}</span>
                                                            </div>
                                                        </div>
                                                        <Badge bg={prescription.status === 'PUBLIC' ? 'success' : 'secondary'} className="text-uppercase p-2">
                                                            {prescription.status === 'PUBLIC' ? 'Đã kê đơn' : ''}
                                                        </Badge>
                                                    </div>
                                                    <div className="small text-muted mt-1">
                                                        Ngày kê đơn: {prescription.publicAt ? moment(prescription.publicAt).format('DD/MM/YYYY') : 'Chưa xác định'}
                                                    </div>

                                                </button>
                                            );
                                        })}
                                    </div>
                                )}
                            </Card.Body>
                        </Card>
                    </Col>

                    <Col lg={9}>
                        {payment && payment.status === "SUCCESS" ? (
                            <Card className="h-100 shadow-sm" style={styles.detailPanel}>
                                <Card.Body>
                                    <PrescriptionDetail prescription={selectedPrescription} error={error} />
                                </Card.Body>
                            </Card>
                        ) : (<>
                                <div className="alert alert-info d-flex justify-content-between align-items-center">
                                <div>Đơn thuốc chưa được thanh toán, vui lòng thanh toán để xem chi tiết</div>
                                <Button variant="primary"
                                    className="rounded-pill px-4 py-2"
                                    onClick={() => navigate('/patient/payments/')}
                                >
                                    Thanh toán ngay
                                </Button>
                            </div> 
                        </>
                        )}
                    </Col>
                </Row>
            </Container>
            {loading && (
                <div className="position-fixed top-0 start-0 vw-100 vh-100 d-flex align-items-center justify-content-center bg-white bg-opacity-75" style={{ zIndex: 1060 }}>
                    <MySpinner />
                </div>
            )}
        </>
    );
};

export default Prescriptions;
