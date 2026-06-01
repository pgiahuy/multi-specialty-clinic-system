import { Container, Row, Col, Card, Modal, Button } from "react-bootstrap";
import Header from "../../components/Header";
import LoginRequiredModal from "../../components/LoginRequiredModal";
import { useContext, useEffect, useRef, useState } from "react";
import { authApis, CLINIC_ENDPOINTS, USER_ENDPOINTS } from "../../configs/Apis";
import MySpinner from "../../components/MySpinner";
import { useNavigate } from "react-router-dom";
import Footer from "../../components/Footer";
import { MyUserContext } from "../../configs/Contexts";
import FloatAlert from "../../components/FloatAlert";

const BookingPage = () => {
    const [user] = useContext(MyUserContext);
    const [loading, setLoading] = useState(false);
    const [showModal, setShowModal] = useState(false);
    const [loginPromptVisible, setLoginPromptVisible] = useState(false);
    const [alertData, setAlertData] = useState({
        show: false,
        heading: "Thông báo",
        message: "",
        variant: "danger",
    });

    const [patientProfiles, setPatientProfiles] = useState([]);
    const [doctors, setDoctors] = useState([]);
    const [schedules, setSchedules] = useState([]);
    const [specialties, setSpecialties] = useState([]);

    const [selectedPatient, setSelectedPatient] = useState("");
    const [selectedSpecialty, setSelectedSpecialty] = useState("");
    const [selectedDoctor, setSelectedDoctor] = useState("");
    const [selectedDate, setSelectedDate] = useState("");
    const [selectedSchedule, setSelectedSchedule] = useState(null);

    const [doctorSearch, setDoctorSearch] = useState("");
    const [showAllSpecialties, setShowAllSpecialties] = useState(false);
    const [doctorSearchLoading, setDoctorSearchLoading] = useState(false);
    const [loadingMoreDoctors, setLoadingMoreDoctors] = useState(false);
    const [doctorPage, setDoctorPage] = useState(1);
    const [hasMoreDoctors, setHasMoreDoctors] = useState(true);

    const doctorListContainerRef = useRef(null);
    const doctorLoadMoreRef = useRef(null);
    const alertTimerRef = useRef(null);

    const nav = useNavigate();

    const loadPatientProfiles = async () => {
        try {
            const res = await authApis().get(USER_ENDPOINTS.PATIENT_PROFILES);
            setPatientProfiles(res.data || []);
        } catch (err) {
            console.log(err);
        }
    };

    const loadSpecialties = async () => {
        try {
            const res = await authApis().get(CLINIC_ENDPOINTS.SPECIALTIES);
            setSpecialties(res.data || []);
        } catch (err) {
            console.log(err);
        }
    };

    const loadSchedules = async () => {
        try {
            if (!selectedSpecialty || !selectedDoctor || !selectedDate) {
                setSchedules([]);
                return;
            }

            const res = await authApis().get(CLINIC_ENDPOINTS.SCHEDULES, {
                params: {
                    specialtyId: selectedSpecialty,
                    doctorId: selectedDoctor,
                    date: selectedDate,
                },
            });

            setSchedules(res.data || []);
        } catch (err) {
            console.log(err);
        }
    };

    const loadDoctors = async (doctorName = "", specialtyId = "", page = 1, replace = true) => {
        try {
            if (replace) {
                setDoctorSearchLoading(true);
            } else {
                setLoadingMoreDoctors(true);
            }

            const params = { page };
            if (doctorName && doctorName.trim()) {
                params.doctorName = doctorName.trim();
            }
            if (specialtyId) {
                params.specialtyId = specialtyId;
            }

            const res = await authApis().get(CLINIC_ENDPOINTS.DOCTORS, { params });
            const fetchedDoctors = res.data || [];

            if (replace) {
                setDoctors(fetchedDoctors);
                setHasMoreDoctors(fetchedDoctors.length > 0);
            } else {
                setDoctors(prev => {
                    const existingDoctorIds = new Set(prev.map(doc => String(doc.id)));
                    const uniqueFetchedDoctors = fetchedDoctors.filter(doc => !existingDoctorIds.has(String(doc.id)));

                    if (uniqueFetchedDoctors.length === 0) {
                        setHasMoreDoctors(false);
                        return prev;
                    }

                    setHasMoreDoctors(true);
                    return [...prev, ...uniqueFetchedDoctors];
                });
            }

            setDoctorPage(page);
        } catch (err) {
            console.log(err);
        } finally {
            if (replace) {
                setDoctorSearchLoading(false);
            } else {
                setLoadingMoreDoctors(false);
            }
        }
    };

    const handleShowAlert = (heading, message, variant) => {
        if (alertTimerRef.current) {
            clearTimeout(alertTimerRef.current);
        }

        setAlertData({
            show: true,
            heading,
            message,
            variant,
        });
        window.scrollTo({ top: 0, behavior: "smooth" });

        alertTimerRef.current = setTimeout(() => {
            setAlertData(prev => ({ ...prev, show: false }));
        }, 2000);
    };

    const registerAppointment = async () => {
        if (!user) {
            setLoginPromptVisible(true);
            return;
        }

        if (!selectedPatient || !selectedSpecialty || !selectedDoctor || !selectedDate || !selectedSchedule) {
            handleShowAlert(
                "Thiếu thông tin",
                "Vui lòng chọn đầy đủ hồ sơ, chuyên khoa, bác sĩ, ngày khám và ca khám.",
                "warning"
            );
            return;
        }

        try {
            setLoading(true);
            await authApis().post(CLINIC_ENDPOINTS.PATIENT_BOOKING_APPOINTMENT, {
                patientId: selectedPatient,
                scheduleId: selectedSchedule.id,
            });

            setShowModal(true);
            handleShowAlert(
                "Đăng ký lịch khám thành công",
                "Bạn đã đăng ký lịch khám thành công!",
                "success"
            );
        } catch (error) {
            if (error.response) {
                if (error.response.status === 409) {
                    handleShowAlert(
                        "Lịch khám đã đăng ký",
                        "Bạn đã đăng ký lịch khám này trước đó. Vui lòng kiểm tra lại thông tin hoặc chọn lịch khác.",
                        "warning"
                    );
                } else if (error.response.status === 400) {
                    handleShowAlert(
                        "Lỗi đặt lịch",
                        error.response.data?.message || "Ca khám đã đầy hoặc dữ liệu không hợp lệ.",
                        "danger"
                    );
                } else {
                    handleShowAlert(
                        "Lỗi không xác định",
                        "Đã có lỗi xảy ra khi đặt lịch khám. Vui lòng thử lại sau.",
                        "danger"
                    );
                }
            } else {
                handleShowAlert(
                    "Lỗi kết nối",
                    "Không thể kết nối đến máy chủ. Vui lòng kiểm tra mạng.",
                    "danger"
                );
            }
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        loadPatientProfiles();
        loadSpecialties();
    }, []);

    useEffect(() => {
        const timerId = setTimeout(() => {
            setHasMoreDoctors(true);
            loadDoctors(doctorSearch, selectedSpecialty, 1, true);
        }, 350);

        return () => clearTimeout(timerId);
    }, [doctorSearch, selectedSpecialty]);

    useEffect(() => {
        const container = doctorListContainerRef.current;
        const sentinel = doctorLoadMoreRef.current;

        if (!container || !sentinel) {
            return;
        }

        const observer = new IntersectionObserver(
            (entries) => {
                const firstEntry = entries[0];
                if (!firstEntry?.isIntersecting || doctorSearchLoading || loadingMoreDoctors || !hasMoreDoctors) {
                    return;
                }

                loadDoctors(doctorSearch, selectedSpecialty, doctorPage + 1, false);
            },
            {
                root: container,
                rootMargin: "0px 0px 80px 0px",
                threshold: 0.1,
            }
        );

        observer.observe(sentinel);

        return () => observer.disconnect();
    }, [doctorSearchLoading, loadingMoreDoctors, hasMoreDoctors, doctorPage, doctorSearch, selectedSpecialty]);

    useEffect(() => {
        loadSchedules();
    }, [selectedDoctor, selectedDate, selectedSpecialty]);

    useEffect(() => {
        return () => {
            if (alertTimerRef.current) {
                clearTimeout(alertTimerRef.current);
            }
        };
    }, []);

    const handleFilterChange = (field, value) => {
        switch (field) {
            case "profile":
                setSelectedPatient(value || "");
                break;
            case "specialty":
                setSelectedSpecialty(value || "");
                setSelectedDoctor("");
                setSelectedSchedule(null);
                break;
            case "doctor":
                setSelectedDoctor(value || "");
                setSelectedSchedule(null);
                break;
            case "date":
                setSelectedDate(value || "");
                setSelectedSchedule(null);
                break;
            case "scheduleId": {
                const sched = schedules.find(s => String(s.id) === String(value));
                if (sched) {
                    const timeLabel = `${sched.shiftStartTime || ""}${sched.shiftStartTime && sched.shiftEndTime ? " - " : ""}${sched.shiftEndTime || ""}`;
                    setSelectedSchedule({ ...sched, time: timeLabel });
                } else {
                    setSelectedSchedule(null);
                }
                break;
            }
            default:
                break;
        }
    };

    const getProfileDisplayName = (profileId) => {
        const profile = patientProfiles.find(item => String(item.id) === String(profileId));
        return profile?.fullName || profileId;
    };

    const getDoctorDisplayName = (doctorId) => {
        const doctor = doctors.find(item => String(item.id) === String(doctorId));
        return doctor?.fullName || doctorId;
    };

    const getSpecialtyDisplayName = (specialtyId) => {
        const specialty = specialties.find(item => String(item.id) === String(specialtyId));
        return specialty?.name || specialtyId;
    };

    const hasManySpecialties = specialties.length > 8;
    const specialtiesToShow = showAllSpecialties ? specialties : specialties.slice(0, 6);

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
        nav("/patient/payments");
    };

    const openDoctorDetail = (doctorId) => {
        nav(`/doctor/detail/${doctorId}`);
    };

    return (
        <>
            <div className="d-flex flex-column min-vh-100">
                <Header />

                <Container style={{ width: "90%" }} className="mt-4 mb-5">
                    <Row className="d-flex flex-row">
                        <Col lg={8} className="d-flex flex-column gap-3">
                            <Card className="p-4 shadow-sm mb-4 rounded-4">
                                <Card.Title className="fw-bold mb-3">Thông tin đặt lịch</Card.Title>
                                <div>
                                    <FloatAlert show={alertData.show} heading={alertData.heading} variant={alertData.variant}>
                                        {alertData.message}
                                    </FloatAlert>
                                </div>

                                <div className="mb-3">
                                    <label className="form-label">Chọn hồ sơ</label>
                                    <div
                                        className="border rounded-4 p-2"
                                        style={{
                                            maxHeight: "260px",
                                            overflowY: "auto",
                                            backgroundColor: "#f8f9fa",
                                        }}
                                    >
                                        <div className="row row-cols-1 row-cols-sm-2 row-cols-lg-3 g-2">
                                            {patientProfiles.length === 0 ? (
                                                <div className="text-muted small fst-italic px-2 py-2">Chưa có hồ sơ để hiển thị.</div>
                                            ) : (
                                                patientProfiles.map(profile => {
                                                    const selected = String(selectedPatient) === String(profile.id);
                                                    const profileName = profile.fullName || "Hồ sơ";
                                                    const profileCccc = profile.cccd || "Chưa có CCCD";

                                                    return (
                                                        <div className="col" key={profile.id}>
                                                            <div
                                                                role="button"
                                                                tabIndex={0}
                                                                className={`w-100 h-100 text-start border rounded-4 p-2 bg-white ${selected ? "border-success shadow-sm" : "border-light"}`}
                                                                onClick={() => handleFilterChange("profile", profile.id)}
                                                                style={{
                                                                    transition: "all 0.2s ease",
                                                                    outline: "none",
                                                                    cursor: "pointer",
                                                                }}
                                                            >
                                                                <div className="d-flex align-items-start gap-2">
                                                                    <div
                                                                        className={`d-flex align-items-center justify-content-center rounded-circle flex-shrink-0 ${selected ? "bg-primary text-white" : "bg-light text-secondary"}`}
                                                                        style={{ width: "36px", height: "36px", fontWeight: 700, fontSize: "13px" }}
                                                                    >
                                                                        {(profileName || "H").trim().charAt(0).toUpperCase()}
                                                                    </div>
                                                                    <div className="flex-grow-1 min-w-0">
                                                                        <div className="d-flex align-items-center justify-content-between gap-2">
                                                                            <div className="fw-semibold text-dark text-truncate small">{profileName}</div>
                                                                        </div>

                                                                        <div className="text-muted" style={{ fontSize: "11px", lineHeight: 1.1 }}>
                                                                            {profileCccc}
                                                                        </div>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </div>
                                                    );
                                                })
                                            )}
                                        </div>
                                    </div>
                                </div>

                                <div className="mb-3">
                                    <div className="mb-2 border rounded-4 p-3" style={{ backgroundColor: "#f8fbff" }}>
                                        <div className="small text-muted mb-2 fw-semibold">Chọn chuyên khoa</div>
                                        <div className="d-flex flex-wrap gap-2 align-items-center">
                                            <button
                                                type="button"
                                                className={`btn btn-sm rounded-pill px-3 ${selectedSpecialty ? "btn-outline-secondary" : "btn-primary"}`}
                                                onClick={() => {
                                                    setSelectedSpecialty("");
                                                    setSelectedDoctor("");
                                                    setSelectedSchedule(null);
                                                }}
                                            >
                                                Tất cả
                                            </button>

                                            {specialtiesToShow.map(specialty => {
                                                const selected = String(selectedSpecialty) === String(specialty.id);
                                                return (
                                                    <button
                                                        key={specialty.id}
                                                        type="button"
                                                        className={`btn btn-sm rounded-pill px-3 ${selected ? "btn-primary shadow-sm" : "btn-outline-primary"}`}
                                                        onClick={() => {
                                                            const nextSpecialty = selected ? "" : specialty.id;
                                                            setSelectedSpecialty(nextSpecialty);
                                                            setSelectedDoctor("");
                                                            setSelectedSchedule(null);
                                                        }}
                                                        style={{ transition: "all 0.2s ease" }}
                                                    >
                                                        {specialty.name}
                                                    </button>
                                                );
                                            })}

                                            {hasManySpecialties && (
                                                <button
                                                    type="button"
                                                    className="btn btn-sm btn-light border rounded-pill px-3"
                                                    onClick={() => setShowAllSpecialties(prev => !prev)}
                                                >
                                                    {showAllSpecialties ? "Thu gọn" : `Xem thêm ${specialties.length - 6} khoa`}
                                                </button>
                                            )}
                                        </div>
                                    </div>

                                    <div className="border rounded-4 p-3 bg-light-subtle">
                                        <div className="d-flex align-items-center justify-content-between gap-2 mb-2">
                                            <label className="form-label mb-0 fw-semibold">Chọn bác sĩ</label>
                                        </div>
                                        <input
                                            type="text"
                                            className="form-control form-control-sm mb-2"
                                            placeholder="Tìm bác sĩ..."
                                            value={doctorSearch}
                                            onChange={(e) => setDoctorSearch(e.target.value)}
                                        />
                                        <div
                                            className="border rounded-4 p-2 bg-white"
                                            ref={doctorListContainerRef}
                                            style={{
                                                maxHeight: "260px",
                                                overflowY: "auto",
                                                overscrollBehavior: "contain",
                                            }}
                                        >
                                            <div className="row row-cols-1 row-cols-sm-2 row-cols-lg-3 g-2">
                                                {doctorSearchLoading && doctors.length === 0 ? (
                                                    <div className="text-muted small fst-italic px-2 py-2">Đang tìm bác sĩ...</div>
                                                ) : doctors.length === 0 ? (
                                                    <div className="text-muted small fst-italic px-2 py-2">
                                                        {doctorSearch.trim() ? "Không tìm thấy bác sĩ phù hợp." : "Chưa có bác sĩ để hiển thị."}
                                                    </div>
                                                ) : (
                                                    doctors.map(doc => {
                                                        const selected = String(selectedDoctor) === String(doc.id);
                                                        const doctorName = doc.fullName || doc.name || "Bác sĩ";
                                                        const specialtyName = doc.specialtyName || doc.specialty || "Chuyên khoa";
                                                        const doctorSpecialties = (() => {
                                                            if (doc.specialtiesOfDoctor && Array.isArray(doc.specialtiesOfDoctor) && doc.specialtiesOfDoctor.length) {
                                                                return doc.specialtiesOfDoctor.map(s => ({ id: s.id, name: s.name }));
                                                            }

                                                            if (doc.specialtyCollection && Array.isArray(doc.specialtyCollection) && doc.specialtyCollection.length) {
                                                                return doc.specialtyCollection.map(s => ({ id: s.id, name: s.name }));
                                                            }

                                                            if (doc.specialties && Array.isArray(doc.specialties) && doc.specialties.length) {
                                                                return doc.specialties.map(s => ({ id: s.id || null, name: s.name || s }));
                                                            }

                                                            if (doc.specialtyName || doc.specialty) {
                                                                const name = doc.specialtyName || doc.specialty;
                                                                const matched = specialties.find(s => s.name === name);
                                                                return [{ id: matched ? matched.id : null, name }];
                                                            }

                                                            return [];
                                                        })();

                                                        return (
                                                            <div className="col" key={doc.id}>
                                                                <div
                                                                    role="button"
                                                                    tabIndex={0}
                                                                    className={`w-100 h-100 text-start border rounded-4 p-2 bg-white ${selected ? "border-success shadow-sm" : "border-light"}`}
                                                                    onClick={() => {
                                                                        if (selected) {
                                                                            handleFilterChange("doctor", "");
                                                                        } else {
                                                                            handleFilterChange("doctor", doc.id);
                                                                        }
                                                                    }}
                                                                    style={{
                                                                        transition: "all 0.2s ease",
                                                                        outline: "none",
                                                                        cursor: "pointer",
                                                                    }}
                                                                >
                                                                    <div className="d-flex align-items-start gap-2">
                                                                        <div
                                                                            className={`d-flex align-items-center justify-content-center rounded-circle flex-shrink-0 ${selected ? "bg-success text-white" : "bg-light text-secondary"}`}
                                                                            style={{ width: "36px", height: "36px", fontWeight: 700, fontSize: "13px" }}
                                                                        >
                                                                            {(doctorName || "B").trim().charAt(0).toUpperCase()}
                                                                        </div>
                                                                        <div className="flex-grow-1 min-w-0">
                                                                            <div className="d-flex align-items-center justify-content-between gap-2">
                                                                                <div className="fw-semibold text-dark text-truncate small">{doctorName}</div>
                                                                            </div>
                                                                            <div className="d-flex flex-wrap gap-1" style={{ fontSize: "12px", lineHeight: 1.2 }}>
                                                                                {doctorSpecialties.length === 0 ? (
                                                                                    <div className="text-muted small">{specialtyName}</div>
                                                                                ) : (
                                                                                    doctorSpecialties.map(sp => {
                                                                                        const spSelected = String(selectedSpecialty) === String(sp.id || sp.name);
                                                                                        return (
                                                                                            <button
                                                                                                key={sp.id || sp.name}
                                                                                                type="button"
                                                                                                className={`btn btn-sm ${spSelected ? "btn-primary" : "btn-outline-secondary"} rounded-pill py-0 px-2`}
                                                                                                onClick={(e) => {
                                                                                                    e.stopPropagation();
                                                                                                    if (sp.id) {
                                                                                                        setSelectedSpecialty(sp.id);
                                                                                                    } else {
                                                                                                        const matchedSpecialty = specialties.find(s => s.name === sp.name);
                                                                                                        if (matchedSpecialty) {
                                                                                                            setSelectedSpecialty(matchedSpecialty.id);
                                                                                                        }
                                                                                                    }

                                                                                                    handleFilterChange("doctor", doc.id);
                                                                                                }}
                                                                                                style={{ fontSize: "11px" }}
                                                                                            >
                                                                                                {sp.name}
                                                                                            </button>
                                                                                        );
                                                                                    })
                                                                                )}
                                                                            </div>
                                                                            <div className="d-flex align-items-center justify-content-between gap-2 mt-1">
                                                                                <div className={`${selected ? "text-primary" : "text-muted"}`} style={{ fontSize: "11px", lineHeight: 1.1 }}>
                                                                                    {selected ? "Bấm để bỏ chọn" : "Bấm để chọn"}
                                                                                </div>
                                                                                <Button
                                                                                    type="button"
                                                                                    variant="outline-secondary"
                                                                                    size="sm"
                                                                                    className="px-2 py-0"
                                                                                    style={{ fontSize: "10px" }}
                                                                                    onClick={(e) => {
                                                                                        e.stopPropagation();
                                                                                        openDoctorDetail(doc.id);
                                                                                    }}
                                                                                >
                                                                                    Xem thông tin
                                                                                </Button>
                                                                            </div>
                                                                        </div>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        );
                                                    })
                                                )}

                                                {hasMoreDoctors && doctors.length > 0 && (
                                                    <div className="col-12" ref={doctorLoadMoreRef} style={{ height: "1px" }} />
                                                )}

                                                {loadingMoreDoctors && doctors.length > 0 && (
                                                    <div className="col-12 text-center text-muted small py-1">Đang tải thêm bác sĩ...</div>
                                                )}
                                            </div>
                                        </div>
                                    </div>
                                </div>

                                <div className="mb-3">
                                    <label className="form-label">Chọn ngày khám</label>
                                    <input
                                        type="date"
                                        className="form-control"
                                        value={selectedDate}
                                        onChange={(e) => handleFilterChange("date", e.target.value)}
                                    />
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
                                                        const selected = selectedSchedule && selectedSchedule.time === timeLabel;
                                                        const remainingSlots = (schedule.maxPatients || 0) - (schedule.currentPatients || 0);
                                                        const isFull = remainingSlots <= 0;

                                                        return (
                                                            <div className="col" key={schedule.id}>
                                                                <button
                                                                    type="button"
                                                                    className={`w-100 text-start p-2 rounded-3 border bg-white ${selected ? "border-success shadow-sm" : "border-default"}`}
                                                                    onClick={() => {
                                                                        if (!isFull) {
                                                                            handleFilterChange("scheduleId", schedule.id);
                                                                        }
                                                                    }}
                                                                    disabled={isFull}
                                                                    style={{ minHeight: "58px", transition: "all 0.2s ease", opacity: isFull ? 0.55 : 1 }}
                                                                >
                                                                    <div className="d-flex flex-column align-items-start gap-1">
                                                                        <div className="fw-semibold text-dark small">{timeLabel || "Chưa có thời gian"}</div>
                                                                        <div className={`${isFull ? "text-danger" : ""} ${selected ? "text-success" : ""}`} style={{ fontSize: "12px", lineHeight: 1 }}>
                                                                            {isFull ? "Hết chỗ" : selected ? "Đã chọn" : "Chọn ca"}
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
                                            <div className="text-muted small fst-italic">Chưa có ca khám phù hợp.</div>
                                        )}
                                    </div>
                                </div>
                            </Card>
                        </Col>

                        <Col lg={4}>
                            <Card className="shadow-sm border rounded-4 sticky-top" style={{ top: "20px", backgroundColor: "#ffffff" }}>
                                <Card.Body className="p-3 p-xl-4">
                                    <div className="d-flex align-items-center justify-content-between gap-2 mb-3 pb-2 border-bottom">
                                        <div>
                                            <Card.Title className="fw-semibold mb-0" style={{ fontSize: "1.05rem" }}>Lịch hẹn</Card.Title>
                                        </div>
                                    </div>

                                    <div className="d-flex flex-column gap-2">
                                        <div className="d-flex align-items-start justify-content-between gap-3 py-2 border-bottom">
                                            <div className="text-muted small">Người khám</div>
                                            <div className={`text-end small ${selectedPatient ? "fw-semibold text-dark" : "text-muted fst-italic"}`}>
                                                {selectedPatient ? getProfileDisplayName(selectedPatient) : "Chưa chọn"}
                                            </div>
                                        </div>

                                        <div className="d-flex align-items-start justify-content-between gap-3 py-2 border-bottom">
                                            <div className="text-muted small">Chuyên khoa</div>
                                            <div className={`text-end small ${selectedSpecialty ? "fw-semibold text-dark" : "text-muted fst-italic"}`}>
                                                {selectedSpecialty ? getSpecialtyDisplayName(selectedSpecialty) : "Chưa chọn"}
                                            </div>
                                        </div>

                                        <div className="d-flex align-items-start justify-content-between gap-3 py-2 border-bottom">
                                            <div className="text-muted small">Bác sĩ</div>
                                            <div className={`text-end small ${selectedDoctor ? "fw-semibold text-dark" : "text-muted fst-italic"}`}>
                                                {selectedDoctor ? getDoctorDisplayName(selectedDoctor) : "Chưa chọn"}
                                            </div>
                                        </div>

                                        <div className="d-flex align-items-start justify-content-between gap-3 py-2 border-bottom">
                                            <div className="text-muted small">Ngày khám</div>
                                            <div className={`text-end small ${selectedDate ? "fw-semibold text-dark" : "text-muted fst-italic"}`}>
                                                {selectedDate || "Chưa chọn"}
                                            </div>
                                        </div>

                                        <div className="d-flex align-items-start justify-content-between gap-3 py-2 border-bottom">
                                            <div className="text-muted small">Ca khám</div>
                                            <div className={`text-end small ${selectedSchedule && selectedSchedule.time ? "fw-semibold text-dark" : "text-muted fst-italic"}`}>
                                                {(selectedSchedule && selectedSchedule.time) || "Chưa chọn"}
                                            </div>
                                        </div>

                                        <div className="pt-2">
                                            {loading ? (
                                                <div className="d-flex justify-content-center py-1"><MySpinner /></div>
                                            ) : (
                                                <button className="btn btn-primary w-100 fw-semibold rounded-3 py-2" onClick={registerAppointment}>
                                                    Xác nhận đặt lịch
                                                </button>
                                            )}
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
                                <p className="mb-2"><strong>Người khám:</strong> {selectedPatient ? getProfileDisplayName(selectedPatient) : ""}</p>
                                <p className="mb-2"><strong>Bác sĩ:</strong> {selectedDoctor ? getDoctorDisplayName(selectedDoctor) : ""}</p>
                                <p className="mb-2"><strong>Ngày khám:</strong> {selectedDate || "Chưa chọn"}</p>
                                <p className="mb-0"><strong>Ca khám:</strong> {(selectedSchedule && selectedSchedule.time) || "Chưa chọn"}</p>
                            </div>
                            <p className="text-center text-success fst-italic mt-3">
                                Thông tin lịch hẹn đã được lưu.<br /> Vui lòng tiến hành thanh toán để hoàn tất quá trình đặt lịch!
                            </p>
                        </Modal.Body>
                        <Modal.Footer className="justify-content-center border-top-0">
                            <Button variant="primary" className="px-5 py-2 fw-bold" onClick={handleCloseModal}>
                                Thanh toán ngay
                            </Button>
                        </Modal.Footer>
                    </Modal>

                    <LoginRequiredModal
                        show={loginPromptVisible}
                        onHide={() => setLoginPromptVisible(false)}
                        onLogin={() => nav("/login")}
                    />
                </Container>
                <Footer />
            </div>
        </>
    );
};

export default BookingPage;
