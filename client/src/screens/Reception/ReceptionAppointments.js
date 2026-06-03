import { useEffect, useState, useCallback, useRef } from "react";
import Footer from "../../components/Footer";
import Header from "../../components/Header";
import { APPOINTMENT_ENDPOINTS, authApis, CLINIC_ENDPOINTS, PAYMENT_ENDPOINTS } from "../../configs/Apis";
import { Button, Col, Container, Row, Table, Form } from "react-bootstrap";
import { useNavigate } from "react-router-dom";
import MySpinner from "../../components/MySpinner";

const ReceptionAppointments = () => {
    const [appointments, setAppointments] = useState([]);
    const [loading, setLoading] = useState(false);
    const [filterKw, setFilterKw] = useState("");
    const [filterDate, setFilterDate] = useState("");
    const [filterStatus, setFilterStatus] = useState("");
    const [searchTerm, setSearchTerm] = useState("");
    const timeRef = useRef(null);
    const nav = useNavigate();
    const getStatusLabel = (status) => {
        const statusMap = {
            'UN_PAID': 'Chưa thanh toán',
            'PENDING': 'Đang chờ',
            'CONFIRMED': 'Đã xác nhận',
            'IN_PROGRESS': 'Đang khám',
            'COMPLETED': 'Đã khám',
            'CANCELLED': 'Đã hủy',
        };
        return statusMap[status] || status;
    };

    const loadAppointments = useCallback(async () => {
        try {
            setLoading(true);
            const response = await authApis().get(APPOINTMENT_ENDPOINTS.APPOINTMENTS, {
                params: {
                    kw: searchTerm,
                    date: filterDate,
                    status: filterStatus
                }
            });
            setAppointments(response.data || []);
        } catch (error) {
            console.error("Lỗi khi tải danh sách lịch hẹn:", error);
        } finally {
            setLoading(false);
        }
    }, [searchTerm, filterDate, filterStatus]);

    useEffect(() => {
        if (timeRef.current) clearTimeout(timeRef.current);

        timeRef.current = setTimeout(() => {
            setSearchTerm(filterKw);
        }, 500);

        return () => {
            if (timeRef.current) clearTimeout(timeRef.current);
        };
    }, [filterKw]);

    const handleReceiveCash = async (appointmentId) => {
        try {
            const response = await authApis().get(PAYMENT_ENDPOINTS.BY_APPOINTMENT(appointmentId));
            const paymentData = response.data;
            if (paymentData && paymentData.id) {
                nav(`/reception/payments/${paymentData.id}`);
            } else {
                alert("Không tìm thấy thông tin thanh toán cho lịch hẹn này!");
            }
        } catch (error) {
            console.error("Lỗi khi tải thông tin thanh toán:", error);
            alert("Có lỗi xảy ra khi lấy thông tin thanh toán. Vui lòng thử lại!");
        }
    };

    useEffect(() => {
        loadAppointments();
    }, [loadAppointments]);

    return (
        <div className="d-flex flex-column min-vh-100">
            <Header />
            <Container fluid className="py-4" style={{ width: '97%' }}>
                <div className="d-flex justify-content-between align-items-center mb-4">
                    <h3 className="mb-0 text-center flex-grow-1">Quầy Tiếp Nhận - DANH SÁCH LỊCH HẸN</h3>
                    <div className="d-flex gap-2">
                        <Button variant="outline-primary" size="sm" onClick={() => nav('/reception/prescriptions')}>
                            Xem đơn thuốc
                        </Button>
                        <Button variant="outline-primary" size="sm" onClick={() => nav('/reception/invoices')}>
                            Xem hóa đơn
                        </Button>
                    </div>
                </div>
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
                                <option value="UN_PAID">Chưa thanh toán</option>
                                <option value="PENDING">Đang chờ</option>
                                <option value="CONFIRMED">Đã xác nhận</option>
                                <option value="IN_PROGRESS">Đang khám</option>
                                <option value="COMPLETED">Đã khám</option>
                                <option value="CANCELLED">Đã hủy</option>
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

                <div className="table-responsive w-100 mx-auto">
                    <Table hover className="table">
                        <thead>
                            <tr>
                                <th>STT</th>
                                <th>Bệnh nhân</th>
                                <th>Bác sĩ</th>
                                <th>Ngày khám</th>
                                <th>Ca</th>
                                <th>Phòng</th>
                                <th>Trạng thái</th>
                                <th></th>
                            </tr>
                        </thead>
                        <tbody>
                            {loading ? (
                                <tr>
                                    <td colSpan={8} className="text-center py-4"><MySpinner /></td>
                                </tr>
                            ) : appointments.length === 0 ? (
                                <tr>
                                    <td colSpan={8} className="text-center text-muted py-5">Không có lịch hẹn.</td>
                                </tr>
                            ) : (
                                appointments.map((a, idx) => (
                                    <tr key={a.id} className="align-middle">
                                        <td>{idx + 1}</td>
                                        <td>{a.patientFullName}</td>
                                        <td>{a.doctorFullName || '-'}</td>
                                        <td>{a.appointmentDate || '-'}</td>
                                        <td>{a.timeSlot || '-'}</td>
                                        <td>{a.roomName || '-'}</td>
                                        <td>{getStatusLabel(a.status) || '-'}</td>
                                        <td>
                                            {a.status === 'UN_PAID' ? (
                                                <Button
                                                    variant="success"
                                                    size="sm"
                                                    className="me-2"
                                                    onClick={() => handleReceiveCash(a.id)}
                                                >
                                                    Nhận tiền mặt
                                                </Button>
                                            ) : a.status !== 'PENDING' ? (
                                                <Button
                                                    variant="primary"
                                                    size="sm"
                                                    className="me-2"
                                                    onClick={() => nav(`/appointments/${a.id}/medical-record`)}
                                                >
                                                    Bệnh án
                                                </Button>
                                            ) : null}
                                        </td>
                                    </tr>
                                ))
                            )}
                        </tbody>
                    </Table>
                </div>

            </Container>
        </div>
    );
};

export default ReceptionAppointments;
