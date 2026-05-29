import { useContext, useEffect, useState } from "react";
import { authApis, CLINIC_ENDPOINTS, endpoint, USER_ENDPOINTS } from "../../configs/Apis";
import { exp } from "firebase/firestore/pipelines";
import { MyUserContext } from "../../configs/Contexts";
import { Card, Col, Container, Row, Table } from "react-bootstrap";
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

    const patientFields = [
        { label: 'Họ và tên', key: 'fullName' },
        { label: 'Ngày sinh', key: 'dob' },
        { label: 'Giới tính', key: 'gender' },
        { label: 'Số điện thoại', key: 'phone' },
        { label: 'Địa chỉ', key: 'address' },
    ];


    const loadPatientProfiles = async () => {
        try {
            const res = await authApis().get(USER_ENDPOINTS.PATIENT_PROFILE_DETAIL(patientId));
            setPatientProfiles(res.data);
        } catch (err) {
            console.log(err);
        }
    };


    const loadAppointments = async (patientId) => {
        try {
            const res = await authApis().get(USER_ENDPOINTS.APPOINTMENTS);
            setAppointments(res.data);
        }
        catch (err) {
            console.log(err);
        }
    };


    const loadTestResults = async (appointmentId) => {
        try {

            const res = await authApis().get(CLINIC_ENDPOINTS.TEST_RESULTS(patientId), {
                params: {
                    appointmentId: appointmentId
                }
            });
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
                <Container className="mb-5">
                    <Row className="gx-3">

                        <Col md={4} className="mt-5">
                            <h3 className="text-center mb-4">Thông tin bệnh nhân</h3>
                            {patientProfiles.length > 0 ? (
                                <div className="d-flex justify-content-start mb-3">
                                    <select
                                        className="form-select"
                                        value={selectedProfileId || ''}
                                        onChange={(e) => setSelectedProfileId(e.target.value ? parseInt(e.target.value) : null)}
                                        style={{
                                            maxWidth: '260px',
                                            borderRadius: '8px',
                                            borderColor: '#0d6efd',
                                            borderWidth: '1.5px',
                                            padding: '8px 12px',
                                            fontSize: '14px',
                                            fontWeight: '500',
                                            cursor: 'pointer'
                                        }}
                                    >
                                        <option value="">-- Chọn một hồ sơ bệnh nhân --</option>
                                        {patientProfiles.map((profile) => (
                                            <option key={profile.id} value={profile.id}>
                                                {profile.fullName}
                                            </option>
                                        ))}
                                    </select>
                                </div>
                            ) : (
                                <div style={emptyState.container} className="mt-4">
                                    <h5 style={emptyState.title}>Không có lịch khám nào</h5>
                                </div>
                            )}
                            <Card className="mb-4 mt-4">
                                <Card.Body>
                                    {selectedProfileId && patientProfiles.find(p => p.id === selectedProfileId) ? (
                                        <div >
                                            <div>
                                                <Row className="gy-3">
                                                    {patientFields.map((field) => {

                                                        const selectedProfile = patientProfiles.find(p => p.id === selectedProfileId);


                                                        if (!selectedProfile) return null;

                                                        return (

                                                            <Col xs={12} md={6} key={field.key}>
                                                                <div>
                                                                    <label className="fw-bold text-muted small mb-1">{field.label}</label>
                                                                    <p className="mb-0 text-dark fw-semibold" style={{ fontSize: '15px' }}>
                                                                        {selectedProfile[field.key] || 'N/A'}
                                                                    </p>
                                                                </div>
                                                            </Col>
                                                        );
                                                    })}
                                                </Row>
                                            </div>
                                        </div>
                                    ) : (
                                        <div style={emptyState.container}>
                                            <h5 style={emptyState.title}>Chọn hồ sơ bệnh nhân để xem thông tin</h5>
                                        </div>
                                    )}
                                </Card.Body>
                            </Card>
                        </Col>

                        <Col md={8} className="ps-0 mb-5 mt-5">
                            <h3 className="text-center mb-4">Kết quả xét nghiệm</h3>
                            {appointments.length > 0 ? (
                                <div className="d-flex justify-content-end">
                                    <select
                                        className="form-select"
                                        value={selectedAppointmentId || ''}
                                        onChange={(e) => setSelectedAppointmentId(e.target.value ? parseInt(e.target.value) : null)}
                                        style={{
                                            maxWidth: '260px',
                                            borderRadius: '8px',
                                            borderColor: '#0d6efd',
                                            borderWidth: '1.5px',
                                            padding: '8px 12px',
                                            fontSize: '14px',
                                            fontWeight: '500',
                                            cursor: 'pointer'
                                        }}
                                    >
                                        <option value="">-- Chọn một phiếu khám --</option>
                                        {appointments.map((appointment) => (
                                            <option key={appointment.id} value={appointment.id}>
                                                Phiếu khám {appointment.appointmentDate}
                                            </option>
                                        ))}
                                    </select>
                                </div>
                            ) : (
                                <div style={emptyState.container} className="mt-4">
                                    <h5 style={emptyState.title}>Không có lịch khám nào</h5>
                                </div>
                            )}


                            {!selectedAppointmentId ? (
                                <div style={emptyState.container} className="mt-4">
                                    <h5 style={emptyState.title}>Vui lòng chọn một phiếu khám để xem kết quả xét nghiệm</h5>
                                </div>
                            ) : testResults.length > 0 ? (
                                <div style={tableStyles.container} className="mt-4">
                                    <Table hover responsive style={tableStyles.table}>
                                        <thead>
                                            <tr style={tableStyles.headerRow}>
                                                <th style={{ ...tableStyles.headerCell, textAlign: 'center' }}>Mã</th>
                                                <th style={tableStyles.headerCell}>Xét nghiệm</th>
                                                <th style={tableStyles.headerCell}>Kết quả</th>
                                                <th style={tableStyles.headerCell}>Đơn vị</th>
                                                <th style={tableStyles.headerCell}>Khoảng tham chiếu</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            {testResults.map((result, index) => (
                                                <tr
                                                    key={result.id}
                                                    style={tableStyles.bodyRow(index)}
                                                    onMouseEnter={(e) => e.currentTarget.style.backgroundColor = '#e7f1ff'}
                                                    onMouseLeave={(e) => e.currentTarget.style.backgroundColor = tableStyles.bodyRow(index).backgroundColor}
                                                >
                                                    <td style={tableStyles.dataCell}>{result.id}</td>
                                                    <td style={tableStyles.dataCellLeft}>{result.testName}</td>
                                                    <td style={tableStyles.resultCell}>{result.result}</td>
                                                    <td style={tableStyles.dataCell}>{result.unit}</td>
                                                    <td style={tableStyles.dataCell}>{result.normalRange}</td>
                                                </tr>
                                            ))}
                                        </tbody>
                                    </Table>
                                </div>
                            ) : (
                                <div style={emptyState.container} className="mt-4">
                                    <h5 style={emptyState.title}>Không có kết quả xét nghiệm nào</h5>
                                </div>
                            )}
                        </Col>

                    </Row>
                </Container>
                <Footer />
            </div>

        </>
    );
}
export default TestResultDetail;