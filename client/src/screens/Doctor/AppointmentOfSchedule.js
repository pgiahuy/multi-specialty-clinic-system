import { useEffect, useState } from "react";
import Footer from "../../components/Footer";
import Header from "../../components/Header";
import { authApis, CLINIC_ENDPOINTS, endpoint } from "../../configs/Apis";
import { useNavigate, useParams } from "react-router-dom";
import { Button, Card, Col, Container, Row, Table } from "react-bootstrap";
import MySpinner from "../../components/MySpinner";
import { tableStyles } from "../Patient/PatientStyle";

const AppointmentOfSchedule = () => {
    const { scheduleId } = useParams();
    const [scheduleDetails, setScheduleDetails] = useState(null);
    const [appointments, setAppointments] = useState([]);
    const [loading, setLoading] = useState(false);
    const [confirmingAppointmentId, setConfirmingAppointmentId] = useState(null);
    const nav = useNavigate();

    const loadScheduleDetails = async () => {
        try {
            setLoading(true);
            console.log("Loading schedule details for scheduleId:", scheduleId);
            const response = await authApis().get(`${CLINIC_ENDPOINTS.DOCTOR_GET_SCHEDULES_BY_ID(scheduleId)}`);
            setScheduleDetails(response.data);
        } catch (error) {
            console.error("Lỗi khi tải chi tiết lịch khám:", error);
        } finally {
            setLoading(false);
        }
    };

    const loadAppointments = async () => {
        try {
            setLoading(true);
            const response = await authApis().get(`${CLINIC_ENDPOINTS.DOCTOR_APPOINTMENTS(scheduleId)}`);
            setAppointments(response.data);
        } catch (error) {
            console.error("Lỗi khi tải danh sách lịch hẹn:", error);
        } finally {
            setLoading(false);
        }
    };

    const handleConfirmAppointment = async (appointmentId) => {
        try {
            setConfirmingAppointmentId(appointmentId);
            await authApis().post(CLINIC_ENDPOINTS.DOCTOR_CONFIRM_APPOINTMENT(appointmentId));
            await loadAppointments(scheduleId);
        } catch (error) {
            console.error("Lỗi khi xác nhận lịch hẹn:", error);
        } finally {
            setConfirmingAppointmentId(null);
        }
    };

    const handleStartAppointment = async (appointmentId) => {
        try {
            setConfirmingAppointmentId(appointmentId);
            await authApis().post(CLINIC_ENDPOINTS.DOCTOR_START_APPOINTMENT(appointmentId));
            await loadAppointments(scheduleId);
        } catch (error) {
            console.error("Lỗi khi bắt đầu khám:", error);
        } finally {
            setConfirmingAppointmentId(null);
        }
    };

    useEffect(() => {
        loadScheduleDetails();
        loadAppointments();
    }, [scheduleId]);

    const statusMap = {
        'UN_PAID': { text: 'Chưa thanh toán', textColor: 'text-warning' },
        'PENDING': { text: 'Đang chờ', textColor: 'text-warning' },
        'CONFIRMED': { text: 'Đã xác nhận', textColor: 'text-primary' },
        'IN_PROGRESS': { text: 'Đang khám', textColor: 'text-info' },
        'COMPLETED': { text: 'Đã khám', textColor: 'text-success' },
        'CANCELLED': { text: 'Đã hủy', textColor: 'text-danger' }
    };

    const renderStatusText = (statusEn) => {
        const mapped = statusMap[statusEn] || { text: 'Không xác định', textColor: 'text-secondary' };
        return (

            <span className={`${mapped.textColor} `}>
                {mapped.text}
            </span>
        );
    };

    const getAppointmentAction = (appointment) => {
        if (appointment.status === 'PENDING') {
            return {
                label: confirmingAppointmentId === appointment.id ? 'Đang xác nhận...' : 'Xác nhận',
                variant: 'warning',
                disabled: confirmingAppointmentId === appointment.id,
                onClick: () => handleConfirmAppointment(appointment.id),
            };
        }

        if (appointment.status === 'CONFIRMED') {
            return {
                label: confirmingAppointmentId === appointment.id ? 'Đang bắt đầu...' : 'Vào khám',
                variant: 'info',
                disabled: confirmingAppointmentId === appointment.id,
                onClick: () => handleStartAppointment(appointment.id),
            };
        }

        if (appointment.status === 'IN_PROGRESS' || appointment.status === 'COMPLETED') {
            return {
                label: 'Bệnh án',
                variant: 'primary',
                onClick: () => nav(`/appointments/${appointment.id}/medical-record`),
            };
        }
        return null;
    };

    return (
        <>
            <div className="d-flex flex-column min-vh-100">
                <Header />
                <Container className="py-4 px-xl-5">
                    <h3 className="mb-4 text-center text-primary fw-bold">
                        DANH SÁCH LỊCH HẸN
                    </h3>
                    <Row className="g-4">
                        <Col lg={4} md={12}>
                            <div className="sticky-top" style={{ top: '20px', zIndex: 10 }}>
                                <Card className="mb-3 border-0 shadow-sm bg-light rounded-3">
                                    <Card.Body className="p-4">
                                        <div className="d-flex flex-column gap-2 text-secondary small p-2">
                                            <div>
                                                <div className="text-muted fw-bold mb-1" style={{ fontSize: '11px' }}>BÁC SĨ PHỤ TRÁCH</div>
                                                <h5 className="mb-0 fw-bold text-dark">{scheduleDetails?.doctorName || 'Chưa phân công'}</h5>
                                                <span className="badge bg-primary-subtle text-primary mt-2 px-2 py-2 rounded-pill">
                                                    {scheduleDetails?.specialtyName || 'Chuyên khoa'}
                                                </span>
                                            </div>
                                            <hr className="my-2 text-muted" />
                                            <div>
                                                <div className="text-muted fw-bold mb-1" style={{ fontSize: '11px' }}>THỜI GIAN LÀM VIỆC</div>
                                                <h6 className="mb-0 fw-bold text-dark">{scheduleDetails?.date || '-'}</h6>
                                                <div className="text-primary fw-bold mt-1">
                                                    Buổi: {scheduleDetails?.session || '-'}
                                                </div>
                                                <div className="text-success fw-bold mt-1">
                                                    Ca khám: {scheduleDetails?.shiftStartTime || '-'} - {scheduleDetails?.shiftEndTime || '-'}
                                                </div>
                                            </div>
                                            <hr className="my-2 text-muted" />
                                            <div>
                                                <div className="text-muted fw-bold mb-1" style={{ fontSize: '11px' }}>ĐỊA ĐIỂM KHÁM</div>
                                                <h6 className="mb-0 fw-bold text-dark">Phòng {scheduleDetails?.room || '-'}</h6>
                                                <div className="text-muted mt-1">
                                                    <span className="fw-semibold text-dark">{scheduleDetails?.area || '-'}</span>
                                                </div>
                                            </div>
                                            <hr className="my-2 text-muted" />
                                            <div className="d-flex flex-row gap-1 align-items-center  justify-content-between">
                                                <div className="text-muted fw-bold" style={{ fontSize: '14px' }}>SỐ BỆNH NHÂN HIỆN CÓ</div>
                                                <h2 className="mb-0 fw-bold text-dark">{scheduleDetails?.currentPatients || 0} / {scheduleDetails?.maxPatients || 0}</h2>
                                            </div>
                                        </div>
                                    </Card.Body>
                                </Card>
                            </div>
                        </Col>
                        <Col lg={8} md={12}>
                            {loading ? (
                                <div className="text-center py-5">
                                    <MySpinner />
                                </div>
                            ) : appointments.length === 0 ? (
                                <div className="text-center text-muted py-5 bg-white rounded-3 shadow-sm">
                                    Không có lịch hẹn nào trong ca khám này.
                                </div>
                            ) : (
                                <Card className="border-0 shadow-sm rounded-3 overflow-hidden">
                                    <div className="table-responsive w-100" style={tableStyles.container}>
                                        <Table hover className="table mb-0" style={tableStyles.table}>
                                            <thead>
                                                <tr style={tableStyles.headerRow}>
                                                    <th style={tableStyles.headerCell}>STT</th>
                                                    <th style={tableStyles.headerCell}>Bệnh nhân</th>
                                                    <th style={tableStyles.headerCell}>Trạng thái</th>
                                                    <th style={tableStyles.headerCell}>Hành động</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                {appointments.map((appointment, index) => {
                                                    const action = getAppointmentAction(appointment);

                                                    return (
                                                        <tr key={appointment.id}
                                                            className="align-middle"
                                                            style={tableStyles.bodyRow(index)}
                                                            onMouseEnter={(e) => e.currentTarget.style.backgroundColor = '#e7f1ff'}
                                                            onMouseLeave={(e) => e.currentTarget.style.backgroundColor = tableStyles.bodyRow(index).backgroundColor}>
                                                            <td style={tableStyles.dataCell}>{index + 1}</td>
                                                            <td style={tableStyles.dataCell}>{appointment.patientFullName}</td>
                                                            <td style={tableStyles.dataCell}>{renderStatusText(appointment.status)}</td>
                                                            <td style={tableStyles.dataCell}>
                                                                {action ? (
                                                                    <Button
                                                                        variant={action.variant}
                                                                        className="rounded-2 p-2 my-0 btn-sm"
                                                                        onClick={action.onClick}
                                                                        disabled={action.disabled}
                                                                    >
                                                                        {action.label}
                                                                    </Button>
                                                                ) : "-"}
                                                            </td>
                                                        </tr>
                                                    );
                                                })}
                                            </tbody>
                                        </Table>
                                    </div>
                                </Card>
                            )}
                        </Col>
                    </Row>

                </Container>
                <Footer />
            </div>
        </>
    );
};

export default AppointmentOfSchedule;