import { useEffect, useState } from "react";
import Footer from "../../components/Footer";
import Header from "../../components/Header";
import { authApis, CLINIC_ENDPOINTS, endpoint } from "../../configs/Apis";
import { useNavigate, useParams } from "react-router-dom";
import { Badge, Button, Container, Table } from "react-bootstrap";
import MySpinner from "../../components/MySpinner";
import { Check2All, CheckCircle, Clock, PlayCircle, XCircle } from "react-bootstrap-icons";
import { tableStyles } from "../Patient/PatientStyle";

const AppointmentList = () => {

    const { scheduleId } = useParams();
    const [appointments, setAppointments] = useState([]);
    const [loading, setLoading] = useState(false);


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

    useEffect(() => {
        if (scheduleId) {
            loadAppointments(scheduleId);
        }
    }, [scheduleId]);


    const statusMap = {
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
    return (
        <>
            <div className="d-flex flex-column min-vh-100">
                <Header />
                <Container className="py-4">
                    <h3 className="mb-4 text-center">DAN SÁCH LỊCH HẸN</h3>
                    {loading ? (
                        <div className="text-center">
                            <MySpinner />
                        </div>
                    ) : appointments.length === 0 ? (
                        <div className="text-center text-muted py-5">
                            Không có bệnh nhân nào.
                        </div>
                    ) : (
                        <div className="table-responsive w-75 mx-auto" style={tableStyles.container}>
                            <Table hover className="table" style={tableStyles.table}>
                                <thead>
                                    <tr style={tableStyles.headerRow}>
                                        <th style={tableStyles.headerCell}>Số thứ tự</th>
                                        <th style={tableStyles.headerCell}>Bệnh nhân</th>
                                        <th style={tableStyles.headerCell}>Trạng thái</th>
                                        <th style={tableStyles.headerCell}></th>
                                    </tr>
                                </thead>
                                <tbody>
                                    {appointments.map((appointment, index) => (
                                        <tr key={appointment.id}
                                            style={tableStyles.bodyRow(index)}
                                            onMouseEnter={(e) => e.currentTarget.style.backgroundColor = '#e7f1ff'}
                                            onMouseLeave={(e) => e.currentTarget.style.backgroundColor = tableStyles.bodyRow(index).backgroundColor}>
                                            <td style={tableStyles.dataCell}>{index + 1}</td>
                                            <td style={tableStyles.dataCell}>{appointment.patientFullName}</td>
                                            <td style={tableStyles.dataCell}>{renderStatusText(appointment.status)}</td>
                                            {appointment.status === 'COMPLETED' ?
                                                (<td style={tableStyles.dataCell}>
                                                    <Button variant="outline-primary" className=" rounded-4">Cập nhật bệnh án</Button>
                                                </td>) : (
                                                    <td style={tableStyles.dataCell}>
                                                        <Button variant="outline-primary" className="rounded-4">
                                                            Ghi nhận bệnh án
                                                        </Button>
                                                    </td>
                                                )}

                                        </tr>
                                    ))}
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

export default AppointmentList;