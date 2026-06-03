import { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { Badge, Button, Card, Container, Modal, Table } from "react-bootstrap";
import Header from "../../components/Header";
import Footer from "../../components/Footer";
import MySpinner from "../../components/MySpinner";
import { authApis, PAYMENT_ENDPOINTS } from "../../configs/Apis";
import moment from "moment";

const ReceptionPrescriptionDetail = () => {
    const { prescriptionId } = useParams();
    const nav = useNavigate();
    const [prescription, setPrescription] = useState(null);
    const [payment, setPayment] = useState(null);
    const [loading, setLoading] = useState(false);
    const [dispensing, setDispensing] = useState(false);
    const [showDispenseModal, setShowDispenseModal] = useState(false);
    const [error, setError] = useState(null);

    useEffect(() => {
        const loadPrescription = async () => {
            try {
                setLoading(true);
                const response = await authApis().get(`secure/prescriptions/${prescriptionId}`);
                setPrescription(response.data);
            } catch (err) {
                console.error("Lỗi khi tải đơn thuốc:", err);
                setError("Không thể tải đơn thuốc. Vui lòng thử lại sau.");
            } finally {
                setLoading(false);
            }
        };

        if (prescriptionId) {
            loadPrescription();
        }
    }, [prescriptionId]);


    useEffect(() => {
        const loadPayment = async () => {
            if (!prescription) return;
            try {
                const response = await authApis().get(PAYMENT_ENDPOINTS.HISTORY);
                const allPayments = Array.isArray(response.data) ? response.data : [];
                const prescriptionPayment = allPayments.find((p) => {
                    if (p.paymentItems && Array.isArray(p.paymentItems)) {
                        return p.paymentItems.some(
                            (item) =>
                                item.itemType === "PRESCRIPTION" &&
                                item.referenceId === parseInt(prescriptionId)
                        );
                    }
                    return false;
                });
                setPayment(prescriptionPayment || null);
            } catch (err) {
                console.error("Lỗi khi tải thông tin thanh toán:", err);
            }
        };

        loadPayment();
    }, [prescription, prescriptionId]);

    const handleDispense = async () => {
        try {
            setDispensing(true);
            await authApis().post(`secure/prescriptions/${prescriptionId}/dispense`);
            const response = await authApis().get(`secure/prescriptions/${prescriptionId}`);
            setPrescription(response.data);
            setShowDispenseModal(false);
            alert("Xuất thuốc thành công!");
        } catch (err) {
            console.error("Lỗi khi xuất thuốc:", err);
            alert("Lỗi khi xuất thuốc. Vui lòng thử lại sau.");
        } finally {
            setDispensing(false);
        }
    };

    const isPaid = payment && payment.status === "SUCCESS";
    const isDispensed = prescription && prescription.status === "DISPENSED";

    if (loading) {
        return (
            <div className="d-flex flex-column min-vh-100 bg-light">
                <Header />
                <Container className="d-flex justify-content-center align-items-center" style={{ minHeight: "60vh" }}>
                    <MySpinner />
                </Container>
                <Footer />
            </div>
        );
    }

    if (error) {
        return (
            <div className="d-flex flex-column min-vh-100 bg-light">
                <Header />
                <Container className="py-4">
                    <div className="alert alert-danger">{error}</div>
                    <Button onClick={() => nav("/reception/prescriptions")}>Quay lại</Button>
                </Container>
                <Footer />
            </div>
        );
    }

    if (!prescription) {
        return (
            <div className="d-flex flex-column min-vh-100 bg-light">
                <Header />
                <Container className="py-4">
                    <div className="alert alert-info">Không tìm thấy đơn thuốc.</div>
                    <Button onClick={() => nav("/reception/prescriptions")}>Quay lại</Button>
                </Container>
                <Footer />
            </div>
        );
    }

    return (
        <div className="d-flex flex-column min-vh-100 bg-light">
            <Header />
            <Container className="py-4" style={{ maxWidth: "900px" }}>
                <div className="mb-4">
                    <Button variant="outline-secondary" size="sm" onClick={() => nav("/reception/prescriptions")}>
                        Quay lại
                    </Button>
                </div>

                <Card className="mb-4 shadow-sm">
                    <Card.Header className="bg-white border-bottom">
                        <div className="d-flex justify-content-between align-items-center">
                            <h4 className="mb-0">Đơn thuốc #{prescription.id}</h4>
                            <div className="d-flex gap-2">
                                <Badge bg={prescription.status === "DISPENSED" ? "info" : prescription.status === "PUBLIC" ? "success" : "secondary"}>
                                    {prescription.status === "DISPENSED" ? "Đã xuất" : prescription.status === "PUBLIC" ? "Đã kê đơn" : prescription.status}
                                </Badge>
                            </div>
                        </div>
                    </Card.Header>
                    <Card.Body>
                        {!isPaid && !isDispensed && (
                            <div className="alert alert-warning mb-4">
                                <strong>Chưa thanh toán:</strong> Cần thanh toán để xem đơn thuốc.
                                {payment && (
                                    <>
                                        <Button
                                            onClick={() => nav(`/reception/payments/${payment.id}`)}
                                            className="ms-3"
                                            variant="success"
                                        >
                                            Nhận tiền
                                        </Button>
                                    </>

                                )}
                            </div>
                        )}

                        <div className="row g-3 mb-4">
                            <div className="col-md-4">
                                <div className="border rounded-3 p-3 bg-light h-100">
                                    <div className="text-secondary small mb-1">Bác sĩ kê đơn</div>
                                    <div className="fw-semibold">{prescription.doctorName || "-"}</div>
                                </div>
                            </div>
                            <div className="col-md-4">
                                <div className="border rounded-3 p-3 bg-light h-100">
                                    <div className="text-secondary small mb-1">Bệnh nhân</div>
                                    <div className="fw-semibold">{prescription.patientName || "-"}</div>
                                </div>
                            </div>
                            <div className="col-md-4">
                                <div className="border rounded-3 p-3 bg-white h-100">
                                    <div className="text-secondary small mb-1">Ngày kê đơn</div>
                                    <div className="fw-semibold">
                                        {prescription.publicAt ? moment(prescription.publicAt).format("DD/MM/YYYY") : "-"}
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div className="row g-3" style={{ marginBottom: "40px" }}>
                            <div className="col-md-6">
                                <div className="text-secondary small mb-2">Chuẩn đoán</div>
                                <div className="border rounded-3 p-3 bg-white h-100">{prescription.diagnosis || "Không có"}</div>
                            </div>
                            <div className="col-md-6">
                                <div className="text-secondary small mb-2">Ghi chú</div>
                                <div className="border rounded-3 p-3 bg-white h-100">{prescription.note || "Không có"}</div>
                            </div>
                        </div>

                        {isPaid || isDispensed ? (
                            <>
                                <h5 className="mb-3 ">Danh sách thuốc</h5>
                                <div className="table-responsive mb-6">
                                    <Table hover className="table">
                                        <thead className="table-light">
                                            <tr>
                                                <th>Thuốc</th>
                                                <th className="text-center">SL</th>
                                                <th className="text-center">Đơn vị</th>
                                                <th className="text-center">Ngày dùng</th>
                                                <th>Ghi chú</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            {prescription.items && prescription.items.length > 0 ? (
                                                prescription.items.map((item) => (
                                                    <tr key={item.id || `${item.medicineId}`}>
                                                        <td>{item.medicineName || "N/A"}</td>
                                                        <td className="text-center">{item.quantity ?? "-"}</td>
                                                        <td className="text-center">{item.unit || "-"}</td>
                                                        <td className="text-center">{item.daysToUse ?? "-"}</td>
                                                        <td>{item.note || "-"}</td>
                                                    </tr>
                                                ))
                                            ) : (
                                                <tr>
                                                    <td colSpan="5" className="text-center text-muted py-4">
                                                        Không có thuốc nào trong đơn.
                                                    </td>
                                                </tr>
                                            )}
                                        </tbody>
                                    </Table>
                                </div>

                                {isPaid && !isDispensed && (
                                    <div className="d-flex gap-2">
                                        <Button variant="success" size="lg" onClick={() => setShowDispenseModal(true)}>
                                            Xuất thuốc
                                        </Button>
                                    </div>
                                )}

                                {isDispensed && (
                                    <div className="alert alert-success">
                                        <strong>Trạng thái:</strong> Đã xuất thuốc vào lúc: {moment(prescription.dispensedAt).format("DD/MM/YYYY HH:mm:ss")}
                                    </div>
                                )}
                            </>
                        ) : (
                            <div className="alert alert-info ">
                                Vui lòng chờ bệnh nhân thanh toán hoặc nhận tiền mặt trước khi xem chi tiết thuốc.
                            </div>
                        )}
                    </Card.Body>
                </Card>
            </Container>

            <Modal show={showDispenseModal} onHide={() => setShowDispenseModal(false)}>
                <Modal.Header closeButton>
                    <Modal.Title>Xác nhận xuất thuốc</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <p>Bạn có chắc muốn xuất thuốc cho đơn này?</p>
                    <p className="text-muted small">
                        Hệ thống sẽ tự động trừ kho theo số lượng thuốc trong đơn.
                    </p>
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={() => setShowDispenseModal(false)}>
                        Hủy
                    </Button>
                    <Button
                        variant="success"
                        onClick={handleDispense}
                        disabled={dispensing}
                    >
                        {dispensing ? "Đang xử lý..." : "Xác nhận xuất thuốc"}
                    </Button>
                </Modal.Footer>
            </Modal>

            <Footer />
        </div>
    );
};

export default ReceptionPrescriptionDetail;
