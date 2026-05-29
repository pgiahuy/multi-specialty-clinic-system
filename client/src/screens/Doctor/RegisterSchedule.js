import { useEffect, useMemo, useState } from "react";
import { Alert, Badge, Button, Card, Col, Container, Form, Row, Table } from "react-bootstrap";
import Footer from "../../components/Footer";
import Header from "../../components/Header";
import MySpinner from "../../components/MySpinner";
import API, { authApis, CLINIC_ENDPOINTS } from "../../configs/Apis";
import { useNavigate } from "react-router-dom";

const RegisterSchedule = () => {
    const nav = useNavigate();
    const [shifts, setShifts] = useState([]);
    const [rooms, setRooms] = useState([]);
    const [schedules, setSchedules] = useState([]);
    const [loadingShifts, setLoadingShifts] = useState(false);
    const [loadingRooms, setLoadingRooms] = useState(false);
    const [loadingSchedules, setLoadingSchedules] = useState(false);
    const [submitting, setSubmitting] = useState(false);
    const [errorMessage, setErrorMessage] = useState("");
    const [successMessage, setSuccessMessage] = useState("");
    const [formData, setFormData] = useState({
        date: "",
        shiftId: "",
        roomId: "",
        maxPatients: "",
    });

    const tomorrow = new Date();
    tomorrow.setDate(tomorrow.getDate() + 1);
    const minDate = tomorrow.toISOString().split("T")[0];

    const loadShifts = async () => {
        try {
            setLoadingShifts(true);
            const response = await API.get(CLINIC_ENDPOINTS.SHIFTS);
            setShifts(response.data);
        } catch (error) {
            console.error("Lỗi khi lấy danh sách ca làm việc:", error);
            setErrorMessage("Không tải được danh sách ca làm việc. Vui lòng thử lại.");
        } finally {
            setLoadingShifts(false);
        }
    };

    const loadSchedules = async () => {
        try {
            setLoadingSchedules(true);
            const response = await authApis().get(CLINIC_ENDPOINTS.SCHEDULES);
            setSchedules(response.data || []);
        } catch (error) {
            console.error("Lỗi khi lấy lịch đã đăng ký:", error);
            setErrorMessage("Không tải được lịch làm việc đã đăng ký.");
        } finally {
            setLoadingSchedules(false);
        }
    };

    const loadRooms = async () => {
        try {
            setLoadingRooms(true);
            const response = await API.get(CLINIC_ENDPOINTS.ROOMS);
            setRooms(response.data || []);
        } catch (error) {
            console.error("Lỗi khi lấy danh sách phòng:", error);
            setErrorMessage("Không tải được danh sách phòng khám.");
        } finally {
            setLoadingRooms(false);
        }
    };

    useEffect(() => {
        loadShifts();
        loadRooms();
        loadSchedules();
    }, []);

    const selectedShift = useMemo(() => {
        if (!formData.shiftId) return null;
        return shifts.find(s => String(s.id) === String(formData.shiftId)) || null;
    }, [formData.shiftId, shifts]);

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData(prev => ({ ...prev, [name]: value }));
    };

    const validateForm = () => {
        if (!formData.date || !formData.shiftId || !formData.roomId || !formData.maxPatients) {
            setErrorMessage("Vui lòng nhập đầy đủ thông tin đăng ký.");
            return false;
        }

        if (formData.date < minDate) {
            setErrorMessage("Ngày làm việc phải từ ngày mai trở đi.");
            return false;
        }

        if (Number(formData.roomId) <= 0 || Number(formData.maxPatients) <= 0) {
            setErrorMessage("Phòng khám và số bệnh nhân tối đa phải lớn hơn 0.");
            return false;
        }

        return true;
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setErrorMessage("");
        setSuccessMessage("");

        if (!validateForm()) return;

        try {
            setSubmitting(true);
            await authApis().post(CLINIC_ENDPOINTS.SCHEDULES, {
                date: formData.date,
                shiftId: Number(formData.shiftId),
                roomId: Number(formData.roomId),
                maxPatients: Number(formData.maxPatients),
            });

            setSuccessMessage("Đăng ký lịch làm việc thành công.");
            setFormData({
                date: "",
                shiftId: "",
                roomId: "",
                maxPatients: "",
            });
            loadSchedules();
        } catch (error) {
            console.error("Lỗi khi đăng ký lịch làm việc:", error);
            const serverMessage = error?.response?.data?.message;
            setErrorMessage(serverMessage || "Đăng ký lịch thất bại. Vui lòng kiểm tra dữ liệu và thử lại.");
        } finally {
            setSubmitting(false);
        }
    };

    return (
        <div className="d-flex flex-column min-vh-100 bg-light">
            <Header />
            <Container className="py-4 flex-grow-1">
                <div className="d-flex justify-content-between align-items-center mb-3">
                    <div>
                        <h3 className="fw-bold mb-1">Đăng ký lịch làm việc</h3>
                        <div className="text-muted">Chọn ca và ngày làm việc để mở lịch khám.</div>
                    </div>
                    <Button variant="outline-primary" onClick={() => nav('/doctor/schedules')}>
                        Xem lịch của tôi
                    </Button>
                </div>

                {errorMessage && (
                    <Alert variant="danger" onClose={() => setErrorMessage("")} dismissible>
                        {errorMessage}
                    </Alert>
                )}
                {successMessage && (
                    <Alert variant="success" onClose={() => setSuccessMessage("")} dismissible>
                        {successMessage}
                    </Alert>
                )}

                <Row className="g-4">
                    <Col lg={4}>
                        <Card className="border-0 shadow-sm rounded-4">
                            <Card.Body>
                                <h5 className="fw-bold mb-3">Form đăng ký</h5>
                                <Form onSubmit={handleSubmit}>
                                    <Form.Group className="mb-3">
                                        <Form.Label>Ngày làm việc</Form.Label>
                                        <Form.Control
                                            type="date"
                                            name="date"
                                            value={formData.date}
                                            min={minDate}
                                            onChange={handleChange}
                                            required
                                        />
                                    </Form.Group>

                                    <Form.Group className="mb-3">
                                        <Form.Label>Ca làm việc</Form.Label>
                                        <Form.Select
                                            name="shiftId"
                                            value={formData.shiftId}
                                            onChange={handleChange}
                                            required
                                            disabled={loadingShifts}
                                        >
                                            <option value="">-- Chọn ca --</option>
                                            {shifts.map(shift => (
                                                <option key={shift.id} value={shift.id}>
                                                    {shift.session} ({shift.startTime} - {shift.endTime})
                                                </option>
                                            ))}
                                        </Form.Select>
                                        {selectedShift && (
                                            <Form.Text className="text-muted">
                                                Gợi ý số bệnh nhân: {selectedShift.minPatients} - {selectedShift.maxPatients}
                                            </Form.Text>
                                        )}
                                    </Form.Group>

                                    <Form.Group className="mb-3">
                                        <Form.Label>Phòng khám</Form.Label>
                                        <Form.Select
                                            name="roomId"
                                            value={formData.roomId}
                                            onChange={handleChange}
                                            required
                                            disabled={loadingRooms}
                                        >
                                            <option value="">-- Chọn phòng --</option>
                                            {rooms.map(room => (
                                                <option key={room.id} value={room.id}>
                                                    {room.roomNumber} - {room.areaName || 'Chưa có khu'}
                                                    {room.locationFloor ? ` (Tầng ${room.locationFloor})` : ''}
                                                </option>
                                            ))}
                                        </Form.Select>
                                    </Form.Group>

                                    <Form.Group className="mb-3">
                                        <Form.Label>Số bệnh nhân tối đa</Form.Label>
                                        <Form.Control
                                            type="number"
                                            name="maxPatients"
                                            min={selectedShift?.minPatients || 1}
                                            max={selectedShift?.maxPatients || undefined}
                                            placeholder="Ví dụ: 15"
                                            value={formData.maxPatients}
                                            onChange={handleChange}
                                            required
                                        />
                                    </Form.Group>

                                    <Button className="w-100" type="submit" disabled={submitting}>
                                        {submitting ? (
                                            <span className="d-inline-flex align-items-center"><MySpinner />Đang đăng ký...</span>
                                        ) : (
                                            "Đăng ký lịch"
                                        )}
                                    </Button>
                                </Form>
                            </Card.Body>
                        </Card>
                    </Col>

                    <Col lg={8}>
                        <Card className="border-0 shadow-sm rounded-4 h-100">
                            <Card.Body>
                                <div className="d-flex justify-content-between align-items-center mb-3">
                                    <h5 className="fw-bold mb-0">Lịch đã đăng ký</h5>
                                    <Button variant="outline-secondary" size="sm" onClick={loadSchedules} disabled={loadingSchedules}>
                                        Tải lại
                                    </Button>
                                </div>

                                {loadingSchedules ? (
                                    <div className="text-center py-4"><MySpinner /></div>
                                ) : schedules.length === 0 ? (
                                    <Alert variant="light" className="mb-0">
                                        Bạn chưa có lịch làm việc nào.
                                    </Alert>
                                ) : (
                                    <div className="table-responsive">
                                        <Table hover className="align-middle mb-0">
                                            <thead>
                                                <tr>
                                                    <th>Ngày</th>
                                                    <th>Ca</th>
                                                    <th>Giờ</th>
                                                    <th>Phòng</th>
                                                    <th>BN hiện tại</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                {schedules.map(schedule => (
                                                    <tr key={schedule.id}>
                                                        <td>{schedule.date}</td>
                                                        <td><Badge bg="primary">{schedule.session}</Badge></td>
                                                        <td>{schedule.shiftStartTime} - {schedule.shiftEndTime}</td>
                                                        <td>{schedule.room} ({schedule.area})</td>
                                                        <td>{schedule.currentPatients}/{schedule.maxPatients}</td>
                                                    </tr>
                                                ))}
                                            </tbody>
                                        </Table>
                                    </div>
                                )}
                            </Card.Body>
                        </Card>
                    </Col>
                </Row>
            </Container>
            <Footer />
        </div>
    );
}

export default RegisterSchedule;