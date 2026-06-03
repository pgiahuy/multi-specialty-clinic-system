import { useEffect, useState } from "react";
import Footer from "../../components/Footer";
import Header from "../../components/Header";
import { APPOINTMENT_ENDPOINTS, authApis, CLINIC_ENDPOINTS, endpoint } from "../../configs/Apis";
import { Button, Col, Container, Row, Table, Form } from "react-bootstrap";

import { useNavigate, useParams, useSearchParams } from "react-router-dom";

import MySpinner from "../../components/MySpinner";
import { tableStyles } from "../Patient/PatientStyle";

const AppointmentList = () => {

    const [appointments, setAppointments] = useState([]);
    const [loading, setLoading] = useState(false);
    const [confirmingAppointmentId, setConfirmingAppointmentId] = useState(null);

    const [filterKw, setFilterKw] = useState("");
    const [filterDate, setFilterDate] = useState("");
    const [filterStatus, setFilterStatus] = useState("");

    const nav = useNavigate();


    const loadAppointments = async () => {
        try {
            setLoading(true);
            const response = await authApis().get(APPOINTMENT_ENDPOINTS.APPOINTMENTS, {
                params: {
                    kw: filterKw,
                    date: filterDate,
                    status: filterStatus
                }
            });
            setAppointments(response.data);

        } catch (error) {
            console.error("Lỗi khi tải danh sách lịch hẹn:", error);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        loadAppointments();
    }, [filterKw, filterDate, filterStatus]);

    const handleConfirmAppointment = async (appointmentId) => {
        try {
            setConfirmingAppointmentId(appointmentId);
            await authApis().post(CLINIC_ENDPOINTS.DOCTOR_CONFIRM_APPOINTMENT(appointmentId));
            await loadAppointments();
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
            await loadAppointments();
        } catch (error) {
            console.error("Lỗi khi bắt đầu khám:", error);
        } finally {
            setConfirmingAppointmentId(null);
        }
    };





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
                <Container fluid className="py-4" style={{ width: '97%' }}>
                    <h3 className="mb-4 text-center">DANH SÁCH LỊCH HẸN</h3>

                    <Row className="mb-4 g-3 bg-light p-3 pt-0 mt-1 rounded shadow-sm mx-auto" style={{ width: '70%' }}>

                        <Col md={5} sm={12}>
                            <Form.Group controlId="filterKw">
                                <Form.Label className="fw-semibold small text-secondary">Tìm theo tên bệnh nhân</Form.Label>
                                <Form.Control
                                    type="text"
                                    placeholder="Nhập tên cần tìm..."
                                    value={filterKw}
                                    onChange={(e) => setFilterKw(e.target.value)}
                                />
                            </Form.Group>
                        </Col>

                        <Col md={2} sm={6}>
                            <Form.Group controlId="filterDate">
                                <Form.Label className="fw-semibold small text-secondary">Chọn ngày hẹn</Form.Label>
                                <Form.Control
                                    type="date"
                                    value={filterDate}
                                    onChange={(e) => setFilterDate(e.target.value)}
                                />
                            </Form.Group>
                        </Col>

                        <Col md={3} sm={6}>
                            <Form.Group controlId="filterStatus">
                                <Form.Label className="fw-semibold small text-secondary">Trạng thái</Form.Label>
                                <Form.Select
                                    value={filterStatus}
                                    onChange={(e) => setFilterStatus(e.target.value)}
                                >
                                    <option value="">Tất cả trạng thái</option>

                                    {Object.entries(statusMap).map(([key, value]) => (
                                        <option key={key} value={key}>
                                            {value.text}
                                        </option>
                                    ))}
                                </Form.Select>
                            </Form.Group>
                        </Col>

                        <Col md={2} sm={12} className="d-flex align-items-end">
                            <Button
                                variant="outline-secondary"
                                className="w-100"
                                onClick={() => { setFilterKw(""); setFilterDate(""); setFilterStatus(""); }}
                                disabled={!filterKw && !filterDate && !filterStatus}
                            >
                                Xóa bộ lọc
                            </Button>
                        </Col>
                    </Row>

                    <div className="table-responsive w-100 mx-auto" style={tableStyles.container}>
                        <Table hover className="table" style={tableStyles.table}>
                            <thead>
                                <tr style={tableStyles.headerRow}>
                                    <th style={tableStyles.headerCell}>STT</th>
                                    <th style={tableStyles.headerCell}>Bệnh nhân</th>
                                    <th style={tableStyles.headerCell}>Bác sĩ</th>
                                    <th style={tableStyles.headerCell}>Chuyên khoa</th>
                                    <th style={tableStyles.headerCell}>Ngày khám</th>
                                    <th style={tableStyles.headerCell}>Ca</th>
                                    <th style={tableStyles.headerCell}>Phòng</th>
                                    <th style={tableStyles.headerCell}>Khu vực</th>
                                    <th style={tableStyles.headerCell}>Trạng thái</th>
                                    <th style={tableStyles.headerCell}></th>
                                </tr>
                            </thead>
                            <tbody>
                                {loading ? (

                                    <tr>
                                        <td colSpan={10} className="text-center py-4 border-bottom-0" >
                                            <MySpinner />
                                        </td>
                                    </tr>
                                ) : appointments.length === 0 ? (

                                    <tr>
                                        <td colSpan={10} className="text-center text-muted py-5 border-bottom-0">
                                            Không có lịch hẹn nào phù hợp.
                                        </td>
                                    </tr>
                                ) : (
                                    appointments.map((appointment, index) => {
                                        const action = getAppointmentAction(appointment);

                                        return (
                                            <tr
                                                key={appointment.id}
                                                className="align-middle"
                                                style={tableStyles.bodyRow(index)}
                                                onMouseEnter={(e) => e.currentTarget.style.backgroundColor = '#e7f1ff'}
                                                onMouseLeave={(e) => e.currentTarget.style.backgroundColor = tableStyles.bodyRow(index).backgroundColor}
                                            >
                                                <td style={tableStyles.dataCell}>{index + 1}</td>
                                                <td style={tableStyles.dataCell}>{appointment.patientFullName}</td>
                                                <td style={tableStyles.dataCell}>{appointment.doctorFullName || '-'}</td>
                                                <td style={tableStyles.dataCell}>{appointment.specialtyName || '-'}</td>
                                                <td style={tableStyles.dataCell}>{appointment.appointmentDate || '-'}</td>
                                                <td className="text-primary fw-semibold" style={tableStyles.dataCell}>
                                                    {appointment.timeSlot || '-'}
                                                </td>
                                                <td style={tableStyles.dataCell}>{appointment.roomName || '-'}</td>
                                                <td style={tableStyles.dataCell}>{appointment.areaName || '-'}</td>
                                                <td style={tableStyles.dataCell}>{renderStatusText(appointment.status)}</td>
                                                <td style={tableStyles.dataCell}>
                                                    {action && (
                                                        <Button
                                                            variant={action.variant}
                                                            className="rounded-2 p-2"
                                                            onClick={action.onClick}
                                                            disabled={action.disabled}
                                                        >
                                                            {action.label}
                                                        </Button>
                                                    )}
                                                </td>
                                            </tr>
                                        );
                                    })
                                )}
                            </tbody>
                        </Table>
                    </div>

                </Container>
                <Footer />
            </div>
        </>
    );
};

export default AppointmentList;