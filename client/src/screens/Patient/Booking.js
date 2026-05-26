import { Container, Row, Col, Card, Modal, Button } from "react-bootstrap";
import Header from "../../components/Header";
import { useEffect, useState } from "react";
import { authApis, endpoint } from "../../configs/Apis";
import MySpinner from "../../components/MySpinner";
import MyAlert from "../../components/MyAlert";
import { useNavigate, useSearchParams } from "react-router-dom";
import Footer from "../../components/Footer";


const BookingPage = () => {
    const [loading, setLoading] = useState(false);
    const [bookingData, setBookingData] = useState({
        profile: null,
        doctor: null,
        date: null,
        time: null,
        scheduleId: null,
        reason: ""
    });

    const [showModal, setShowModal] = useState(false);
    const [alertData, setAlertData] = useState({
        show: false,
        heading: "Thông báo",
        message: "",
        variant: "danger",
    });
    const [patientProfiles, setPatientProfiles] = useState([]);
    const [doctors, setDoctors] = useState([]);
    const [schedules, setSchedules] = useState([]);
    const nav = useNavigate();
    const [searchParams] = useSearchParams();
    const [history, setHistory] = useState([]);

    const loadBookingHistory = async () => {
        try {
            const res = await authApis().get(endpoint['appointments']);
            setHistory(res.data);
        } catch (err) {
            console.log(err);
        }
    };

    const loadPatientProfiles = async () => {
        try {
            const res = await authApis().get(endpoint['patientProfiles']);
            setPatientProfiles(res.data);
        } catch (err) {
            console.log(err);
        }
    };

    const loadDoctors = async () => {
        try {
            const res = await authApis().get(endpoint['doctors']);
            setDoctors(res.data);
        } catch (err) {
            console.log(err);
        }
    };

    const loadSchedules = async () => {
        try {
            const res = await authApis().get(endpoint['schedules']);
            setSchedules(res.data);
        } catch (err) {
            console.log(err);
        }
    };

    const registerAppointment = async () => {
        if (!bookingData.profile || !bookingData.doctor || !bookingData.date || !bookingData.time || !bookingData.scheduleId) {
            setAlertData({
                show: true,
                heading: "Lỗi đặt lịch",
                message: "Vui lòng chọn đầy đủ thông tin hồ sơ, bác sĩ, ngày và ca khám!",
                variant: "warning",
            });
            return;
        }

        try {
            setLoading(true);
            const res = await authApis().post(endpoint['appointments'], {
                patientId: bookingData.profile,
                scheduleId: bookingData.scheduleId,
            });

            setShowModal(true);
            setAlertData({
                show: false,
                heading: "Thông báo",
                message: "",
                variant: "success",
            });
            console.log("Appointment registered:", res.data);
        } catch (error) {
            if (error.response) {
                if (error.response.status === 409) {
                    setAlertData({
                        show: true,
                        heading: "Lỗi đặt lịch",
                        message: "Bạn đã đăng ký khám ca này rồi! Vui lòng chọn ca khác.",
                        variant: "warning",
                    });
                } else if (error.response.status === 400) {
                    setAlertData({
                        show: true,
                        heading: "Lỗi đặt lịch",
                        message: error.response.data.message || "Ca khám đã đầy hoặc dữ liệu không hợp lệ.",
                        variant: "danger",
                    });
                } else {
                    setAlertData({
                        show: true,
                        heading: "Lỗi máy chủ",
                        message: "Có lỗi xảy ra từ máy chủ! Vui lòng thử lại sau.",
                        variant: "danger",
                    });
                }
            } else {
                setAlertData({
                    show: true,
                    heading: "Lỗi kết nối",
                    message: "Không thể kết nối đến máy chủ. Vui lòng kiểm tra mạng.",
                    variant: "danger",
                });
            }
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        loadPatientProfiles();
        loadDoctors();
        loadBookingHistory();
    }, []);

    useEffect(() => {
        const doctorId = searchParams.get('doctorId');
        if (doctorId) {
            setBookingData(prev => ({ ...prev, doctor: doctorId }));
        }
    }, [searchParams]);

    useEffect(() => {
        const loadFilteredSchedules = async () => {
            try {
                if (!bookingData.doctor || !bookingData.date) {
                    setSchedules([]);
                    return;
                }

                const res = await authApis().get(endpoint['schedules'], {
                    params: {
                        doctorId: bookingData.doctor,
                        date: bookingData.date,
                    },
                });
                console.log("Loaded schedules:", res.data);
                setSchedules(res.data);
            } catch (err) {
                console.log(err);
            }
        };

        loadFilteredSchedules();
    }, [bookingData.doctor, bookingData.date]);



    const handleFilterChange = (field, value) => {
        console.log(`Filter changed: ${field} = ${value}`);
        setBookingData(prev => ({
            ...prev,
            ...(field === 'doctor' || field === 'date' ? { time: null, scheduleId: null } : {}),
            [field]: value
        }));
    };

    const getProfileDisplayName = (profileId) => {
        const profile = patientProfiles.find(item => String(item.id) === String(profileId));
        return profile?.fullName || profileId;
    };

    const getDoctorDisplayName = (doctorId) => {
        const doctor = doctors.find(item => String(item.id) === String(doctorId));
        return doctor?.fullName || doctorId;
    };

    const schedulesBySession = schedules.reduce((grouped, schedule) => {
        if (!schedule.session) {
            return grouped;
        }

        if (!grouped[schedule.session]) {
            grouped[schedule.session] = [];
        }

        grouped[schedule.session].push(schedule);
        return grouped;
    }, {});

    const handleCloseModal = () => {
        setShowModal(false);
        nav(`/patient/payment/${bookingData.profile}`);
    };

    const closeAlert = () => {
        setAlertData(prev => ({ ...prev, show: false }));
    };

    return (
        <>
            <div className="d-flex flex-column min-vh-100">
                <Header />

                <Container style={{ width: '90%' }} className="mt-4 mb-5">
                    <Row className="gap-3" className="d-flex flex-row">

                        <Col lg={8} className="d-flex flex-column gap-3">
                            <Card className="p-4 shadow-sm mb-4">
                                <Card.Title className="fw-bold mb-3">Thông tin đặt lịch</Card.Title>
                                <div><MyAlert
                                                show={alertData.show}
                                                heading={alertData.heading}
                                                message={alertData.message}
                                                variant={alertData.variant}
                                                onClose={closeAlert}
                                            /></div>
                                <div className="mb-3">
                                    <label className="form-label">Chọn hồ sơ</label>
                                    <select className="form-select" onChange={(e) => handleFilterChange('profile', e.target.value)}>
                                        <option value="">-- Chọn hồ sơ --</option>
                                        {patientProfiles.map(profile => (
                                            <option key={profile.id} value={profile.id}>{profile.fullName}</option>
                                        ))}
                                    </select>
                                </div>

                                <div className="mb-3">
                                    <label className="form-label">Chọn bác sĩ</label>
                                    <select
                                        className="form-select"
                                        value={bookingData.doctor || ''}
                                        onChange={(e) => handleFilterChange('doctor', e.target.value)}
                                    >
                                        <option value="">-- Chọn bác sĩ --</option>
                                        {doctors.map(doc => (
                                            <option key={doc.id} value={doc.id}>{doc.fullName}</option>
                                        ))}
                                    </select>
                                </div>

                                <div className="mb-3">
                                    <label className="form-label">Chọn ngày khám</label>
                                    <input type="date" className="form-control" onChange={(e) => handleFilterChange('date', e.target.value)} />
                                </div>

                                <div className="mb-3">
                                    <label className="form-label">Chọn ca khám</label>

                                    <div className="d-flex flex-column gap-3">
                                        {Object.entries(schedulesBySession).map(([sessionName, sessionSchedules]) => (
                                            <div key={sessionName} className="mb-2">
                                                <div className="fw-semibold text-dark mb-2">{sessionName}</div>

                                                <div className="row row-cols-1 row-cols-sm-2 row-cols-lg-5 g-2 border rounded-3 p-2">
                                                    {sessionSchedules.map(schedule => {
                                                        const timeLabel = `${schedule.shiftStartTime || ""}${schedule.shiftStartTime && schedule.shiftEndTime ? " - " : ""}${schedule.shiftEndTime || ""}`;
                                                        const selected = bookingData.time === timeLabel;
                                                        const remainingSlots = (schedule.maxPatients || 0) - (schedule.currentPatients || 0);
                                                        const isFull = remainingSlots <= 0;

                                                        return (
                                                            <div className="col" key={schedule.id}>
                                                                <button
                                                                    type="button"
                                                                    className={`w-100 text-start p-2 rounded-3 border bg-white ${selected ? 'border-success shadow-sm' : 'border-default'} `}
                                                                    onClick={() => {
                                                                        if (!isFull) {
                                                                            handleFilterChange('time', timeLabel);
                                                                            handleFilterChange('scheduleId', schedule.id); 
                                                                        }
                                                                    }}
                                                                    disabled={isFull}
                                                                    style={{ minHeight: '58px', transition: 'all 0.2s ease', opacity: isFull ? 0.55 : 1 }}
                                                                >
                                                                    <div className="d-flex flex-column align-items-start gap-1">
                                                                        <div className="fw-semibold text-dark small">{timeLabel || "Chưa có thời gian"}</div>
                                                                        <div className={` ${isFull ? 'text-danger' : ''} ${selected ? 'text-success' : ''}`} style={{ fontSize: '12px', lineHeight: 1 }}>
                                                                            {isFull ? 'Hết chỗ' : (selected ? 'Đã chọn' : 'Chọn ca')}
                                                                        </div>
                                                                    </div>
                                                                </button>
                                                            </div>
                                                        );
                                                    })}
                                                </div>
                                            </div>
                                        ))}

                                        {Object.keys(schedulesBySession).length === 0 && (
                                            <div className="text-muted small fst-italic">
                                                Chưa có ca khám phù hợp.
                                            </div>
                                        )}
                                    </div>
                                </div>


                                <div className="placeholder-glow">
                                    <p className="placeholder-lg w-100"></p>
                                    <p className="placeholder-lg w-100"></p>
                                </div>
                            </Card>
                        </Col>


                        <Col lg={4}>
                            <Card className="shadow-sm border rounded-3 sticky-top" style={{ top: '20px' }}>
                                <Card.Body className="p-4">
                                    <Card.Title className="fw-bold mb-4">Lịch hẹn</Card.Title>

                                    <div className="d-flex flex-column gap-3">
                                        <div className="pb-2 border-bottom">
                                            <p className="text-muted small mb-1">Người khám</p>
                                            <p className="fw-semibold mb-0">{bookingData.profile ? getProfileDisplayName(bookingData.profile) : "Chưa chọn"}</p>
                                        </div>

                                        <div className="pb-2 border-bottom">
                                            <p className="text-muted small mb-1">Bác sĩ</p>
                                            <p className="fw-semibold mb-0">{bookingData.doctor ? getDoctorDisplayName(bookingData.doctor) : "Chưa chọn"}</p>
                                        </div>

                                        <div className="pb-2 border-bottom">
                                            <p className="text-muted small mb-1">Ngày khám</p>
                                            <p className="fw-semibold mb-0">{bookingData.date || "Chưa chọn"}</p>
                                        </div>

                                        <div className="pb-2 border-bottom">
                                            <p className="text-muted small mb-1">Ca khám</p>
                                            <p className="fw-semibold mb-0">{bookingData.time || "Chưa chọn"}</p>
                                        </div>

                                        <div className="pb-2 border-bottom">
                                            <p className="text-muted small mb-1">Lý do khám</p>
                                            <p className="fw-semibold mb-0">{bookingData.reason || "Chưa nhập"}</p>
                                        </div>
                                        <div className="text-center mt-4">
                                            
                                            {loading === true ? <MySpinner /> : <button className="btn btn-primary w-100 fw-semibold rounded-4" onClick={registerAppointment}>
                                                Xác nhận đặt lịch
                                            </button>}
                                        </div>
                                    </div>
                                </Card.Body>
                            </Card>
                        </Col>
                    </Row>
                    <Modal show={showModal} onHide={handleCloseModal} centered backdrop="static">
                        <Modal.Header closeButton>
                            <Modal.Title className="fw-bold">
                                <i className="bi bi-check-circle-fill me-2"></i> Đặt lịch thành công!
                            </Modal.Title>
                        </Modal.Header>
                        <Modal.Body className="p-4">
                            <div className="bg-light p-3 rounded border">
                                <p className="mb-2"><strong>Người khám:</strong> {bookingData.profile ? getProfileDisplayName(bookingData.profile) : ""}</p>
                                <p className="mb-2"><strong>Bác sĩ:</strong> {bookingData.doctor ? getDoctorDisplayName(bookingData.doctor) : ""}</p>
                                <p className="mb-2"><strong>Ngày khám:</strong> {bookingData.date}</p>
                                <p className="mb-0"><strong>Ca khám:</strong> {bookingData.time}</p>
                            </div>
                            <p className="text-center text-danger fst-italic mt-3">
                                Thông tin lịch hẹn đã được lưu.<br /> Vui lòng tiến hành thanh toán để hoàn tất quá trình đặt lịch!
                            </p>
                        </Modal.Body>
                        <Modal.Footer className="justify-content-center border-top-0">
                            <Button variant="primary" className="px-5 py-2 fw-bold" onClick={handleCloseModal}>
                                Thanh toán ngay
                            </Button>
                        </Modal.Footer>
                    </Modal>
                </Container >
                <Footer />
            </div>

        </>
    );
};

export default BookingPage;
