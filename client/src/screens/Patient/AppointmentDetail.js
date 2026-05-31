import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { APPOINTMENT_ENDPOINTS, authApis } from "../../configs/Apis";
import Header from "../../components/Header";
import Footer from "../../components/Footer";
import { Button, Card, Col, Row, Badge, Spinner } from "react-bootstrap";
import MyModal from "../../components/MyModal";
import MySpinner from "../../components/MySpinner";

const AppointmentDetail = () => {
    const { appointmentId } = useParams();
    const [appointment, setAppointment] = useState(null);
    const [loading, setLoading] = useState(false);
    const navigate = useNavigate();

    const loadAppointmentDetail = async () => {
        setLoading(true);
        try {
            const response = await authApis().get(APPOINTMENT_ENDPOINTS.APPOINTMENT_DETAIL(appointmentId));
            setAppointment(response.data);

        } catch (error) {
            console.error("Failed to load appointment detail", error);
        } finally {
            setLoading(false);
        }
    };

    const [showCancelModal, setShowCancelModal] = useState(false);
    const [showCancelErrorModal, setShowCancelErrorModal] = useState(false);
    const [cancelErrorMessage, setCancelErrorMessage] = useState("");
    const [cancelling, setCancelling] = useState(false);

    useEffect(() => {
        loadAppointmentDetail();
    }, [appointmentId]);

    const confirmCancel = async () => {
        if (!appointment) return;
        setCancelling(true);
        try {
            const response = await authApis().put(APPOINTMENT_ENDPOINTS.CANCEL_APPOINTMENT(appointmentId));

            if (response.status === 200) {
                setAppointment(prev => prev ? { ...prev, status: 'CANCELLED' } : prev);
                setShowCancelModal(false);
            }

        } catch (err) {
            const errorMessage = err.response?.data?.message || 'Đã có lỗi xảy ra khi hủy lịch. Vui lòng thử lại sau.';
            setCancelErrorMessage(errorMessage);
            setShowCancelModal(false);
            setShowCancelErrorModal(true);
            console.error('Lỗi khi hủy lịch:', err);

        } finally {
            setCancelling(false);
        }
    };

    const renderStatusText = (status) => {
        if (!status) return <span className="text-muted">Không rõ</span>;

        switch (status.toUpperCase()) {
            case 'PENDING': return <span className="text-warning">Chờ xác nhận</span>;
            case 'CONFIRMED': return <span className="text-primary">Đã xác nhận</span>;
            case 'IN_PROGRESS': return <span className="text-info">Đang khám</span>;
            case 'COMPLETED': return <span className="text-success">Đã hoàn thành</span>;
            case 'CANCELLED': return <span className="text-danger">Đã hủy</span>;

        }
    };


    return (
        <div className="d-flex flex-column min-vh-100">
            <Header />
            <div className="container py-4">
                <div>
                    <h2 className="fw-bold mb-2 text-primary text-center mb-5">Thông tin lịch hẹn</h2>
                </div>

                {loading ? (
                    <div className="d-flex justify-content-center py-5">
                        <MySpinner />
                    </div>
                ) : !appointment ? (
                    <div className="text-center text-muted p-5 bg-white rounded border">Không có thông tin cuộc hẹn.</div>
                ) : (
                    <Row className="justify-content-center">
                        <Col xs={12} md={8} lg={6}>
                            <Card className="shadow-sm rounded-4 overflow-hidden">
                                <Card.Header className="d-flex justify-content-between align-items-center bg-light py-3 border-bottom-0">
                                    <div> Khoa {appointment.specialtyName} </div>
                                    <div> {appointment.roomName} - {appointment.areaName} </div>


                                </Card.Header>

                                <Card.Body className="p-4">
                                    {/* Dòng 1: Bệnh nhân */}
                                    <div className="d-flex justify-content-between align-items-center mb-3">
                                        <div className="small text-muted">Bệnh nhân</div>
                                        <div className="fw-semibold text-end">{appointment?.patientFullName || '-'}</div>
                                    </div>


                                    <div className="d-flex justify-content-between align-items-center mb-3">
                                        <div className="small text-muted">Bác sĩ phụ trách</div>
                                        <div className="fw-semibold text-end">{appointment?.doctorFullName || '-'}</div>
                                    </div>


                                    <div className="d-flex justify-content-between align-items-center mb-3">
                                        <div className="small text-muted">Ngày khám</div>
                                        <div className="fw-bold text-primary text-end">{appointment?.appointmentDate || '-'} ({appointment.session || '-'})</div>
                                    </div>


                                    <div className="d-flex justify-content-between align-items-center mb-3">
                                        <div className="small text-muted">Giờ khám</div>
                                        <div className="fw-semibold text-end">
                                            {appointment?.timeSlot || '-'}
                                        </div>
                                    </div>

                                    <div className="d-flex justify-content-between align-items-center mb-3">
                                        <div className="small text-muted">Giá khám</div>
                                        <div className="fw-bold text-end text-success">
                                            {appointment.price ? `${appointment.price.toLocaleString()} VND` : '-'}
                                        </div>
                                    </div>

                                    <div className="d-flex justify-content-between align-items-center mb-3">
                                        <div className="small text-muted">Trạng thái</div>
                                        <div className="fw-semibold text-end">
                                            {renderStatusText(appointment?.status)}
                                        </div>
                                    </div>





                                </Card.Body>

                                <Card.Footer className="bg-white border-0 p-4 pt-0">

                                    {appointment?.status === 'PENDING' && (
                                        <div className="d-flex align-items-center p-3 mb-4 bg-warning bg-opacity-10 border border-warning border-opacity-50 rounded-3">
                                            <i className="bi bi-exclamation-circle-fill text-warning fs-5 me-3"></i>
                                            <div className="small fw-semibold text-dark">
                                                Vui lòng thanh toán phí khám để lịch hẹn được xác nhận!
                                            </div>
                                        </div>
                                    )}

                                    {appointment?.status === 'CONFIRMED' && (
                                        <div className="d-flex align-items-center p-3 mb-4 bg-primary bg-opacity-10 border border-primary border-opacity-50 rounded-3">
                                            <i className="bi bi-check-circle-fill text-primary fs-5 me-3"></i>
                                            <div className="small fw-semibold text-dark">
                                                Vui lòng đến phòng khám trước thời gian hẹn ít nhất 15 phút để khám.
                                            </div>
                                        </div>
                                    )}

                                    <div className="d-flex justify-content-end align-items-center gap-3">
                                        <Button variant="outline-secondary" className="rounded-pill px-4 py-2" onClick={() => navigate(-1)}>
                                            Trở về
                                        </Button>
                                        {(appointment?.status === 'PENDING' || appointment?.status === 'CONFIRMED') && (
                                            <Button variant="danger" className="rounded-pill px-4 py-2" onClick={() => setShowCancelModal(true)}>
                                                Hủy lịch hẹn
                                            </Button>
                                        )}

                                        {appointment?.status === 'PENDING' && (
                                            <Button variant="primary" className="rounded-pill px-4 py-2" onClick={() => navigate(`/patient/payment/${appointment.patientId}`)}>
                                                Thanh toán
                                            </Button>
                                        )}


                                    </div>
                                </Card.Footer>
                            </Card>
                        </Col>
                    </Row>
                )}
            </div>
            <MyModal
                show={showCancelModal}
                onHide={() => setShowCancelModal(false)}
                title="Xác nhận hủy lịch"
                onConfirm={confirmCancel}
                confirmText={cancelling ? 'Đang hủy...' : 'Xác nhận hủy'}
                cancelText="Hủy"
            >
                <div>Bạn có chắc chắn muốn hủy lịch hẹn này không? Hành động này có thể không hoàn tác được.</div>
            </MyModal>

            <MyModal
                show={showCancelErrorModal}
                onHide={() => setShowCancelErrorModal(false)}
                title="Lỗi hủy lịch"
                cancelText="Đóng"
                hideFooter={false}
            >
                <div className="text-danger small">{cancelErrorMessage}</div>
            </MyModal>

            <Footer />
        </div>
    );
};

export default AppointmentDetail;