import { useNavigate, useSearchParams } from "react-router-dom";
import Header from "../../components/Header";
import Footer from "../../components/Footer";
import { authApis, CLINIC_ENDPOINTS } from "../../configs/Apis";
import { useEffect, useState } from "react";

const CreateMedicalRecord = () => {
    const navigate = useNavigate();
    const [searchParams] = useSearchParams();
    const [loading, setLoading] = useState(false);
    const [appointment, setAppointment] = useState(null);
    const appointmentId = searchParams.get('appointmentId');
    const [diagnosis, setDiagnosis] = useState('');
    const [note, setNote] = useState('');

    const loadAppointment = async (id) => {
        try {
            setLoading(true);
            const response = await authApis().get(CLINIC_ENDPOINTS.APPOINTMENT_BY_ID(id));
            setAppointment(response.data);
        } catch (error) {
            alert("Không thể tải thông tin lịch hẹn.");
        } finally {
            setLoading(false);
        }
    }

    useEffect(() => {
        if (appointmentId) {
            loadAppointment(appointmentId);
        }
    }, [appointmentId]);


    const handleCreateMedicalRecord = async () => {
        if (!diagnosis.trim()) {
            alert("Vui lòng nhập chẩn đoán trước khi lưu!");
            return;
        }

        try {
            setLoading(true);
            const requestData = {
                appointmentId: appointmentId,
                diagnosis: diagnosis,
                note: note,
            };

            await authApis().post(CLINIC_ENDPOINTS.CREATE_MEDICAL_RECORD, requestData);

            alert("Tạo bệnh án thành công!");
            navigate(`/doctor/appointments/${appointmentId}/medical-record`);
        } catch (error) {
            console.error("Tạo bệnh án thất bại:", error);
            alert("Tạo bệnh án thất bại. Vui lòng thử lại!");
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="d-flex flex-column min-vh-100 bg-light">
            <Header />

            <div className="container mt-4 mb-4 flex-grow-1 d-flex justify-content-center align-items-center">
                <div className="card shadow border-0 w-100" style={{ maxWidth: '700px', borderRadius: '12px' }}>

                    <div className="card-header bg-primary text-white text-center py-3" style={{ borderTopLeftRadius: '12px', borderTopRightRadius: '12px' }}>
                        <h4 className="mb-0 fw-bold text-uppercase">Tạo Bệnh Án Mới</h4>
                    </div>

                    <div className="card-body p-4">

                        <div className="row g-3 mb-3 pt-0 text-dark bg-light p-1 rounded border">
                            <div className="col-md-6">
                                <p className="mb-2"><strong>Bệnh nhân:</strong> <span className="text-primary fw-medium">{appointment?.patientFullName || '-'}</span></p>
                                <p className="mb-2"><strong>Ngày khám:</strong> {appointment?.appointmentDate || '-'}</p>
                                <p className="mb-2"><strong>Chuyên khoa:</strong> {appointment?.specialtyName || '-'}</p>
                            </div>
                            <div className="col-md-6">
                                <p className="mb-2"><strong>Ca khám:</strong> {appointment ? `${appointment.session} (${appointment.timeSlot})` : '-'}</p>
                                <p className="mb-2"><strong>Phòng:</strong> {appointment?.roomName || '-'}</p>
                                <p className="mb-2"><strong>Khu vực:</strong> {appointment?.areaName || '-'}</p>
                            </div>
                        </div>



                        <div className="mb-3">
                            <label htmlFor="diagnosis" className="form-label fw-bold">Chẩn đoán <span className="text-danger">*</span></label>
                            <input
                                type="text"
                                className="form-control form-control-lg fs-6"
                                id="diagnosis"
                                placeholder="Nhập tình trạng bệnh lý..."
                                value={diagnosis}
                                onChange={(e) => setDiagnosis(e.target.value)}
                                disabled={loading}
                            />
                        </div>

                        <div className="mb-4">
                            <label htmlFor="note" className="form-label fw-bold">Ghi chú:</label>
                            <textarea
                                className="form-control"
                                id="note"
                                rows="4"
                                placeholder="Hướng dẫn điều trị, ăn uống, tái khám..."
                                value={note}
                                onChange={(e) => setNote(e.target.value)}
                                disabled={loading}
                            ></textarea>
                        </div>


                        <div className="d-flex justify-content-between align-items-center pt-2 border-top">
                            <button
                                className="btn btn-outline-secondary px-4 py-2 fw-medium"
                                onClick={() => navigate(-1)}
                                disabled={loading}
                            >
                                <i className="bi bi-arrow-left me-2"></i>Quay lại
                            </button>
                            <button
                                className="btn btn-primary px-4 py-2 fw-medium shadow-sm"
                                onClick={handleCreateMedicalRecord}
                                disabled={loading}
                            >
                                {loading ? (
                                    <>
                                        <span className="spinner-border spinner-border-sm me-2" role="status" aria-hidden="true"></span>
                                        Đang lưu...
                                    </>
                                ) : (
                                    <>
                                        <i className="bi bi-check-circle me-2"></i>Lưu bệnh án
                                    </>
                                )}
                            </button>
                        </div>
                    </div>

                </div>
            </div>

            <Footer />
        </div>
    );
};

export default CreateMedicalRecord;