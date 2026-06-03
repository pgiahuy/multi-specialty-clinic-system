import { useCallback, useEffect, useMemo, useRef, useState } from "react";
import Footer from "../../components/Footer";
import Header from "../../components/Header";
import { authApis, CLINIC_ENDPOINTS } from "../../configs/Apis";
import { Button, Container, Row, Col } from "react-bootstrap";
import MySpinner from "../../components/MySpinner";
import ShiftCard from "./component/ShiftCard";
import moment from 'moment';
import { Calendar } from 'primereact';
import { Plus, PlusCircle } from "react-bootstrap-icons";
import { useNavigate } from "react-router-dom";
const Schedules = () => {
    const [schedules, setSchedules] = useState([]);
    const [loading, setLoading] = useState(false);
    const [selectedDate, setSelectedDate] = useState("");
    const dateInputRef = useRef(null);
    const nav = useNavigate();
    const pageStyle = {
        minHeight: "100vh",
        background: "linear-gradient(180deg, #f4f7fb 0%, #eef3f8 100%)"
    };

    const shellStyle = {
        borderRadius: 24,
        background: "rgba(255,255,255,0.72)",
        border: "1px solid rgba(255,255,255,0.7)",
        boxShadow: "0 20px 60px rgba(15, 23, 42, 0.08)",
        backdropFilter: "blur(10px)"
    };

    const sidebarCardStyle = {
        borderRadius: 20,
        border: "1px solid #e5e7eb",
        boxShadow: "0 10px 30px rgba(15, 23, 42, 0.06)"
    };

    const sectionCardStyle = {
        borderRadius: 20,
        border: "1px solid #e5e7eb",
        boxShadow: "0 10px 30px rgba(15, 23, 42, 0.06)"
    };


    const uniqueDates = useMemo(() => {
        return [
            moment().format('DD-MM-YYYY'),
            moment().add(1, 'days').format('DD-MM-YYYY'),
            moment().add(2, 'days').format('DD-MM-YYYY')
        ];
    }, []);

    const loadSchedules = useCallback(async () => {
        if (!selectedDate) return;

        try {
            setLoading(true);

            const formattedDate = moment(selectedDate, ['YYYY-MM-DD', 'DD-MM-YYYY']).format('YYYY-MM-DD');

            const response = await authApis().get(CLINIC_ENDPOINTS.SCHEDULES, {
                params: { date: formattedDate }
            });
            setSchedules(response.data);
        } catch (error) {
            console.error("Lỗi khi lấy danh sách lịch làm việc:", error);
        } finally {
            setLoading(false);
        }
    }, [selectedDate]);

    useEffect(() => {
        loadSchedules();
    }, [loadSchedules]);


    useEffect(() => {

        setSelectedDate(moment().format('YYYY-MM-DD'));
    }, []);


    const openDatePicker = () => {
        if (dateInputRef.current?.showPicker) {
            dateInputRef.current.showPicker();
            return;
        }
        dateInputRef.current?.click();
    };

    const morningShifts = schedules.filter((schedule) => schedule.session === "Sáng");
    const afternoonShifts = schedules.filter((schedule) => schedule.session === "Chiều");

    return (
        <div className="d-flex flex-column min-vh-100" style={pageStyle}>
            <Header />
            <Container className="py-4 py-md-5 flex-grow-1">
                <Row className="g-4">
                    <Col md={3}>
                        <div className="bg-white p-3" style={sidebarCardStyle}>
                            <div className="d-flex justify-content-between align-items-center mb-3 gap-2">
                                <h6 className="fw-bold text-uppercase text-muted mb-0" style={{ letterSpacing: 0.8 }}>
                                    Chọn ngày làm việc
                                </h6>
                            </div>
                            <input
                                ref={dateInputRef}
                                type="date"
                                className="form-control form-control-sm mb-3"
                                onChange={(e) => setSelectedDate(moment(e.target.value).format('DD-MM-YYYY'))}

                                onClick={openDatePicker}
                                style={{ borderRadius: 12, minHeight: 42 }}
                            />
                            <div className="d-flex flex-column gap-2">
                                {uniqueDates.map((date) => (
                                    <Button
                                        key={date}
                                        variant={selectedDate === date ? "primary" : "outline-primary"}
                                        className={`d-flex justify-content-between align-items-center px-3 py-2 fw-bold ${selectedDate === date ? "shadow-sm" : ""}`}
                                        onClick={() => setSelectedDate(date)}
                                        style={{ borderRadius: 12, width: '100%' }}
                                    >
                                        <span>{date}</span>
                                        {moment().format('DD-MM-YYYY') === date && (
                                            <small >Hôm nay</small>
                                        )}
                                    </Button>
                                ))}
                            </div>
                        </div>
                        <div className="bg-white p-3 mt-3 d-flex align-items-center text-center" style={sidebarCardStyle}>
                            <Button variant="outline-primary" className="w-100 d-flex align-items-center gap-1 px-3 py-2 fw-bold justify-content-center"
                                onClick={() => nav('/doctor/register-schedule')} style={{ borderRadius: 12 }}>
                                <PlusCircle size={18} />
                                Đăng ký ca
                            </Button>
                        </div>
                    </Col>
                    <Col md={9} >
                        <div className="bg-white p-4 rounded-5 border" style={{ minHeight: "80vh" }}>
                            <div className="d-flex flex-column flex-md-row justify-content-between align-items-md-center gap-2 mb-4">
                                <h4 className="fw-bold mb-0 text-primary">Lịch trình ngày: {selectedDate || "--"}</h4>
                                <div className="  badge bg-primary text-white p-2 fs-6 rounded-3">
                                    {morningShifts.length + afternoonShifts.length} ca làm việc
                                </div>
                            </div>
                            {loading ? (
                                <div className="d-flex align-items-center justify-content-center" style={{ minHeight: "60vh" }}>
                                    <MySpinner animation="border" size="lg" />
                                </div>
                            ) : schedules.length === 0 ? (
                                <div className="text-center text-muted py-5 bg-white" style={{ minHeight: "60vh" }}>
                                    <div className="fw-semibold mb-1">Chưa có lịch làm việc nào.</div>
                                    <div className="small">Vui lòng kiểm tra lại ngày làm việc hoặc quay lại sau.</div>
                                </div>
                            ) : (
                                <Col className="d-flex flex-column gap-3">
                                    <Row>
                                        <div className="p-3 bg-light h-100 w-100" style={{ borderRadius: 18 }}>
                                            <h5 className="fw-bold text-warning mb-3">Buổi Sáng</h5>
                                            {morningShifts.length > 0 ? (
                                                <Row className="row-cols-1 row-cols-md-3 g-2">
                                                    {morningShifts.map((schedule) => (
                                                        <Col key={schedule.id}>
                                                            <ShiftCard schedule={schedule} />
                                                        </Col>
                                                    ))}
                                                </Row>
                                            ) : (
                                                <p className="text-muted fst-italic">Không có ca sáng</p>
                                            )}
                                        </div>
                                    </Row>
                                    <Row>
                                        <div className="p-3 bg-light h-100 w-100" style={{ borderRadius: 18 }}>
                                            <h5 className="fw-bold text-primary mb-3">Buổi Chiều</h5>
                                            {afternoonShifts.length > 0 ? (
                                                <Row className="row-cols-1 row-cols-md-3 g-2">
                                                    {afternoonShifts.map((schedule) => (
                                                        <Col key={schedule.id}>
                                                            <ShiftCard schedule={schedule} />
                                                        </Col>
                                                    ))}
                                                </Row>
                                            ) : (
                                                <p className="text-muted fst-italic">Không có ca chiều</p>
                                            )}
                                        </div>
                                    </Row>
                                </Col>
                            )}
                        </div>
                    </Col >
                </Row >
            </Container >
            <Footer />
        </div >
    );
};

export default Schedules;
