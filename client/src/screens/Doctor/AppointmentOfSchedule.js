import { useEffect, useState } from "react";
import Footer from "../../components/Footer";
import Header from "../../components/Header";
import { authApis, CLINIC_ENDPOINTS, endpoint } from "../../configs/Apis";
import { useNavigate, useParams } from "react-router-dom";
import { Button, Container, Table } from "react-bootstrap";
import MySpinner from "../../components/MySpinner";
import { tableStyles } from "../Patient/PatientStyle";

const AppointmentOfSchedule = () => {

    const { scheduleId } = useParams();
    const [appointments, setAppointments] = useState([]);
    const [loading, setLoading] = useState(false);
    const [confirmingAppointmentId, setConfirmingAppointmentId] = useState(null);
    const nav = useNavigate();


    const loadAppointments = async (scheduleId) => {
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
        if (scheduleId) {
            loadAppointments(scheduleId);
        }
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

            <span className={`${mapped.textColor} fw-bold`}>
                {mapped.text}
            </span>
        );
    };

    const getAppointmentAction = (appointment) => {
        if (appointment.status === 'PENDING') {
            return {
                label: confirmingAppointmentId === appointment.id ? 'Đang xác nhận...' : 'Xác nhận lịch hẹn',
                variant: 'warning',
                disabled: confirmingAppointmentId === appointment.id,
                onClick: () => handleConfirmAppointment(appointment.id),
            };
        }

        if (appointment.status === 'CONFIRMED') {
            return {
                label: confirmingAppointmentId === appointment.id ? 'Đang bắt đầu...' : 'Bắt đầu khám',
                variant: 'info',
                disabled: confirmingAppointmentId === appointment.id,
                onClick: () => handleStartAppointment(appointment.id),
            };
        }

        if (appointment.status === 'IN_PROGRESS' || appointment.status === 'COMPLETED') {
            return {
                label: 'Xem bệnh án',
                variant: 'primary',
                onClick: () => nav(`/doctor/appointments/${appointment.id}/medical-record`),
            };
        }


        return null;
    };

    return (
        <>
            <div className="d-flex flex-column min-vh-100">
                <Header />
                <Container className="py-4">
                    <h3 className="mb-4 text-center">DANH SÁCH LỊCH HẸN</h3>
                    {loading ? (
                        <div className="text-center">
                            <MySpinner />
                        </div>
                    ) : appointments.length === 0 ? (
                        <div className="text-center text-muted py-5">
                            Không có lịch hẹn nào.
                        </div>
                    ) : (
                        <div className="table-responsive w-100 mx-auto" style={tableStyles.container}>
                            <Table hover className="table" style={tableStyles.table}>
                                <thead>
                                    <tr style={tableStyles.headerRow}>
                                        <th style={tableStyles.headerCell}>Số thứ tự</th>
                                        <th style={tableStyles.headerCell}>Bệnh nhân</th>
                                        <th style={tableStyles.headerCell}>Bác sĩ</th>
                                        <th style={tableStyles.headerCell}>Chuyên khoa</th>
                                        <th style={tableStyles.headerCell}>Ngày khám</th>
                                        <th style={tableStyles.headerCell}>Ca khám</th>
                                        <th style={tableStyles.headerCell}>Phòng</th>
                                        <th style={tableStyles.headerCell}>Khu vực</th>
                                        <th style={tableStyles.headerCell}>Trạng thái</th>
                                        <th style={tableStyles.headerCell}></th>
                                    </tr>
                                </thead>
                                <tbody>
                                    {appointments.map((appointment, index) => {
                                        const action = getAppointmentAction(appointment);

                                        return (
                                            <tr key={appointment.id}
                                                style={tableStyles.bodyRow(index)}
                                                onMouseEnter={(e) => e.currentTarget.style.backgroundColor = '#e7f1ff'}
                                                onMouseLeave={(e) => e.currentTarget.style.backgroundColor = tableStyles.bodyRow(index).backgroundColor}>
                                                <td style={tableStyles.dataCell}>{index + 1}</td>
                                                <td style={tableStyles.dataCell}>{appointment.patientFullName}</td>
                                                <td style={tableStyles.dataCell}>{appointment.doctorFullName || '-'}</td>
                                                <td style={tableStyles.dataCell}>{appointment.specialtyName || '-'}</td>
                                                <td style={tableStyles.dataCell}>{appointment.appointmentDate || '-'}</td>
                                                <td style={tableStyles.dataCell}>
                                                    <div className="fw-semibold">{appointment.session || '-'}</div>
                                                    <div className="small text-muted">{appointment.timeSlot || '-'}</div>
                                                </td>
                                                <td style={tableStyles.dataCell}>{appointment.roomName || '-'}</td>
                                                <td style={tableStyles.dataCell}>{appointment.areaName || '-'}</td>
                                                <td style={tableStyles.dataCell}>{renderStatusText(appointment.status)}</td>
                                                <td style={tableStyles.dataCell}>
                                                    {action && (
                                                        <Button
                                                            variant={action.variant}
                                                            className="rounded-4"
                                                            onClick={action.onClick}
                                                            disabled={action.disabled}
                                                        >
                                                            {action.label}
                                                        </Button>
                                                    )}
                                                </td>
                                            </tr>
                                        );
                                    })}
                                </tbody>
                            </Table>
                        </div>
                    )}
                </Container>
                <Footer />
            </div>
        </>
    );
};

export default AppointmentOfSchedule;