import { useContext, useEffect, useState } from "react";
import { APPOINTMENT_ENDPOINTS, authApis, CLINIC_ENDPOINTS, endpoint, USER_ENDPOINTS } from "../../configs/Apis";
import { exp } from "firebase/firestore/pipelines";
import { MyUserContext } from "../../configs/Contexts";
import { Card, Col, Container, Form, Row, Table } from "react-bootstrap";
import Header from "../../components/Header";
import Footer from "../../components/Footer";
import { useParams } from "react-router-dom";
import { patientInfoCard, tableStyles, emptyState, filterBox } from "./PatientStyle";

const TestResultDetail = () => {

    const { patientId } = useParams();
    const [user] = useContext(MyUserContext);
    const [testResults, setTestResults] = useState([]);
    const [appointments, setAppointments] = useState([]);
    const [selectedAppointmentId, setSelectedAppointmentId] = useState(null);
    const [patientProfiles, setPatientProfiles] = useState([]);
    const [selectedProfileId, setSelectedProfileId] = useState(null);


    const [fromDate, setFromDate] = useState(() => {
        const date = new Date();
        date.setMonth(date.getMonth() - 1);
        return date.toISOString().slice(0, 10);
    });
    const [toDate, setToDate] = useState(() => {
        const date = new Date();
        return date.toISOString().slice(0, 10);
    });

    const patientFields = [
        { label: 'Họ và tên', key: 'fullName' },
        { label: 'Ngày sinh', key: 'dob' },
        { label: 'Giới tính', key: 'gender' },
        { label: 'Số điện thoại', key: 'phone' },
        { label: 'Địa chỉ', key: 'address' },
    ];


    const loadPatientProfiles = async () => {
        try {
            const res = await authApis().get(USER_ENDPOINTS.PATIENT_PROFILES);
            setPatientProfiles(res.data);
        } catch (err) {
            console.log(err);
        }
    };


    const loadAppointments = async (patientId) => {
        try {
            const res = await authApis().get(APPOINTMENT_ENDPOINTS.APPOINTMENTS_BY_PATIENT(patientId), {
                params: {
                    fromDate,
                    toDate
                }
            });
            setAppointments(res.data);
        }
        catch (err) {
            console.log(err);
        }
    };


    const loadTestResults = async (appointmentId) => {
        try {

            const res = await authApis().get(CLINIC_ENDPOINTS.TEST_RESULTS_APPOINTMENT(appointmentId));
            setTestResults(res.data);
        } catch (err) {
            console.log(err);
        }
    };

    useEffect(() => {
        loadPatientProfiles();
    }, []);

    useEffect(() => {
        if (selectedProfileId) {
            loadAppointments(selectedProfileId);
        }
    }, [selectedProfileId]);

    useEffect(() => {
        if (selectedAppointmentId) {
            loadTestResults(selectedAppointmentId);
        } else {
            setTestResults([]);
        }
    }, [selectedAppointmentId]);

    return (
        <>
            <div className="d-flex flex-column min-vh-100">
                <Header />
                <Container className="py-4">
                    <div className="mb-4 pb-3 border-bottom">
                        <div className="d-flex flex-column flex-lg-row justify-content-between align-items-lg-end gap-3">


                            <div>
                                <h2 className="fw-bold mb-0 text-primary">Lịch sử xét nghiệm</h2>
                            </div>


                            <div className="d-flex flex-column flex-md-row align-items-md-end gap-3">


                                <div>
                                    <Form.Label className="small text-muted mb-1">Bệnh nhân</Form.Label>
                                    {patientProfiles.length > 0 ? (
                                        <Form.Select
                                            value={selectedProfileId}
                                            className="rounded-3 shadow-sm px-3"
                                            onChange={(e) => setSelectedProfileId(e.target.value)}
                                            style={{ minWidth: '250px' }}
                                        >
                                            <option value="">-- Chọn hồ sơ bệnh nhân --</option>
                                            {patientProfiles.map(p => (
                                                <option key={p.id} value={String(p.id)}>
                                                    {p.fullName || p.name || `Hồ sơ ${p.id}`}
                                                </option>
                                            ))}
                                        </Form.Select>
                                    ) : (
                                        <div className="text-muted small py-2 fst-italic">Không có hồ sơ...</div>
                                    )}
                                </div>


                                <div>
                                    <Form.Label className="small text-muted mb-1">Từ ngày</Form.Label>
                                    <Form.Control
                                        type="date"
                                        value={fromDate}
                                        className="rounded-3 shadow-sm px-3"
                                        onChange={(e) => setFromDate(e.target.value)}
                                        style={{ width: '150px' }}
                                    />
                                </div>


                                <div>
                                    <Form.Label className="small text-muted mb-1">Đến ngày</Form.Label>
                                    <Form.Control
                                        type="date"
                                        value={toDate}
                                        className="rounded-3 shadow-sm px-3"
                                        onChange={(e) => setToDate(e.target.value)}
                                        style={{ width: '150px' }}
                                    />
                                </div>

                            </div>
                        </div>
                    </div>
                    <Row className="g-4 mb-5">

                        <Col xs={12} lg={4}>

                            <Card className="border-0 shadow-sm rounded-4 mb-4">
                                <Card.Body className="p-4">
                                    <h5 className="fw-bold text-primary mb-4">
                                        <i className="bi bi-person-vcard me-2"></i>Thông tin bệnh nhân
                                    </h5>

                                    {selectedProfileId && patientProfiles.find(p => p.id === parseInt(selectedProfileId)) ? (
                                        <div className="d-flex flex-column gap-3">
                                            {patientFields.map((field) => {
                                                const selectedProfile = patientProfiles.find(p => p.id === parseInt(selectedProfileId));
                                                if (!selectedProfile) return null;
                                                return (
                                                    <div key={field.key} className="d-flex justify-content-between border-bottom pb-2">
                                                        <span className="text-muted small">{field.label}</span>
                                                        <span className="fw-semibold text-dark text-end">
                                                            {selectedProfile[field.key] || 'N/A'}
                                                        </span>
                                                    </div>
                                                );
                                            })}
                                        </div>
                                    ) : (
                                        <div className="text-center text-muted py-4 fst-italic">
                                            Vui lòng chọn một hồ sơ bệnh nhân.
                                        </div>
                                    )}
                                </Card.Body>
                            </Card>


                            <Card className="border-0 shadow-sm rounded-4 border border-primary border-opacity-25">
                                <Card.Body className="p-4">
                                    <h5 className="fw-bold text-primary mb-3">
                                        <i className="bi bi-file-medical me-2"></i>Chọn phiếu khám
                                    </h5>

                                    {appointments.length > 0 ? (
                                        <Form.Select
                                            className="rounded-3 shadow-sm px-3"
                                            value={selectedAppointmentId || ''}
                                            onChange={(e) => setSelectedAppointmentId(e.target.value ? parseInt(e.target.value) : null)}
                                        >
                                            <option value="">--Chọn phiếu khám --</option>
                                            {appointments.map((appointment) => (
                                                <option key={appointment.id} value={appointment.id}>
                                                    Phiếu khám ngày {appointment.appointmentDate}
                                                </option>
                                            ))}
                                        </Form.Select>
                                    ) : (
                                        <div className="text-center text-muted py-4 fst-italic">
                                            Bệnh chưa có phiếu khám nào.
                                        </div>
                                    )}
                                </Card.Body>
                            </Card>
                        </Col>


                        <Col xs={12} lg={8}>
                            <Card className="border-0 shadow-sm rounded-4 h-100 overflow-hidden">
                                <Card.Header className="bg-white border-0 p-4 pb-0">
                                    <h5 className="fw-bold text-primary mb-0">
                                        <i className="bi bi-clipboard2-pulse me-2"></i>Kết quả xét nghiệm
                                    </h5>
                                </Card.Header>

                                <Card.Body className="p-4">
                                    {!selectedAppointmentId ? (
                                        <div className="d-flex flex-column align-items-center justify-content-center h-100 text-muted py-5">

                                        </div>
                                    ) : testResults.length > 0 ? (
                                        <div className="table-responsive">
                                            <Table hover className="align-middle">
                                                <thead className="table-light text-muted small">
                                                    <tr>
                                                        <th className="fw-semibold py-3 px-3">Tên xét nghiệm</th>
                                                        <th className="fw-semibold py-3">Kết quả</th>
                                                        <th className="fw-semibold py-3">Đơn vị</th>
                                                        <th className="fw-semibold py-3">Tham chiếu</th>
                                                        <th className="fw-semibold py-3 text-end px-3">Ngày cập nhật</th>
                                                    </tr>
                                                </thead>
                                                <tbody>
                                                    {testResults.map((result) => (
                                                        <tr key={result.id}>
                                                            <td className=" px-3 py-3">
                                                                {result.testName}
                                                            </td>


                                                            <td className="fw-bold fs-5 text-center">
                                                                {!result.result ? (
                                                                    <span className="text-muted fw-normal">-</span>
                                                                ) : result.isNormal === false ? (
                                                                    <span className="text-danger">
                                                                        {result.result} <i className="bi bi-exclamation-triangle-fill ms-1 fs-6"></i>
                                                                    </span>
                                                                ) : (
                                                                    <span className="text-success">{result.result}</span>
                                                                )}
                                                            </td>

                                                            <td className="text-muted">{result.unit}</td>
                                                            <td className="text-muted">{result.normalRange}</td>
                                                            <td className="text-muted text-end px-3 small">
                                                                {result.createAt || '-'}
                                                            </td>
                                                        </tr>
                                                    ))}
                                                </tbody>
                                            </Table>
                                        </div>
                                    ) : (
                                        <div className="text-center text-muted py-5">
                                            <i className="bi bi-inbox fs-1 d-block mb-3 opacity-50"></i>
                                            Chưa có kết quả xét nghiệm cho phiếu khám này.
                                        </div>
                                    )}
                                </Card.Body>
                            </Card>
                        </Col>
                    </Row>
                </Container>
                <Footer />
            </div>

        </>
    );
}
export default TestResultDetail;