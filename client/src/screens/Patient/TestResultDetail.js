import { useContext, useEffect, useState } from "react";
import { authApis, endpoint } from "../../configs/Apis";
import { exp } from "firebase/firestore/pipelines";
import { MyUserContext } from "../../configs/Contexts";
import { Col, Container, Row, Table } from "react-bootstrap";
import Header from "../../components/Header";
import Footer from "../../components/Footer";
import { useParams } from "react-router-dom";
import { patientInfoCard, tableStyles, emptyState, filterBox } from "./PatientStyle";

const TestResultDetail = () => {

    const { patientId } = useParams();
    const [user] = useContext(MyUserContext);
    const [testResults, setTestResults] = useState([]);
    const [patient, setPatient] = useState(null);
    const [appointments, setAppointments] = useState([]);
    const [selectedAppointmentId, setSelectedAppointmentId] = useState(null);

    const patientFields = [
        { label: 'Họ và tên', key: 'fullName' },
        { label: 'Ngày sinh', key: 'dob' },
        { label: 'Giới tính', key: 'gender' },
        { label: 'Số điện thoại', key: 'phone' },
        { label: 'Địa chỉ', key: 'address' },
    ];

    const loadPatient = async () => {
        try {
            const res = await authApis().get(endpoint['patient-profile'](patientId));
            setPatient(res.data);
        } catch (err) {
            console.log(err);
        }
    };


    const loadAppointments = async () => {
        try {
            const res = await authApis().get(endpoint['appointments']);
            setAppointments(res.data);
        }
        catch (err) {
            console.log(err);
        }
    };


    const loadTestResults = async (appointmentId) => {
        try {

            const res = await authApis().get(endpoint['test-results'](patientId), {
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
        loadPatient();
        loadAppointments();
    }, []);

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
                    <Row className="gap-3">

                        <Col md={8} className="mt-5">
                            <h3 className="text-center">Bệnh nhân</h3>

                            {patient && Object.keys(patient).length > 0 && (
                                <div style={patientInfoCard.container}>
                                    <div style={patientInfoCard.grid}>
                                        {patientFields.map((field) => (
                                            <div key={field.key}>
                                                <label style={patientInfoCard.label}>{field.label}</label>
                                                <p style={patientInfoCard.value}>{patient[field.key]}</p>
                                            </div>
                                        ))}
                                    </div>
                                </div>
                            )}


                            <h3 className="text-center mt-5">Kết quả xét nghiệm {selectedAppointmentId && `- Phiếu khám #${selectedAppointmentId}`}</h3>
                            {!selectedAppointmentId ? (
                                <div style={emptyState.container}>
                                    <h5 style={emptyState.title}>Vui lòng chọn một phiếu khám để xem kết quả xét nghiệm</h5>
                                </div>
                            ) : testResults.length > 0 ? (
                                <div style={tableStyles.container}>
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
                                <div style={emptyState.container}>
                                    <h5 style={emptyState.title}>Không có kết quả xét nghiệm nào</h5>
                                </div>
                            )}

                        </Col>
                        <Col md={3} className="ps-0 mb-5 mt-5">
                            <h3 className="text-center mb-4">Chọn phiếu khám</h3>
                            {appointments.length > 0 ? (
                                <div style={{ marginBottom: '24px' }}>
                                    <select
                                        className="form-select form-select-lg"
                                        value={selectedAppointmentId || ''}
                                        onChange={(e) => setSelectedAppointmentId(e.target.value ? parseInt(e.target.value) : null)}
                                        style={{
                                            borderRadius: '8px',
                                            borderColor: '#0d6efd',
                                            borderWidth: '2px',
                                            padding: '12px 16px',
                                            fontSize: '16px',
                                            fontWeight: '500'
                                        }}
                                    >
                                        <option value="">-- Chọn một phiếu khám --</option>
                                        {appointments.map((appointment) => (
                                            <option key={appointment.id} value={appointment.id}>
                                                Phiếu khám #{appointment.id} - {appointment.appointmentDate}
                                            </option>
                                        ))}
                                    </select>
                                </div>
                            ) : (
                                <div style={emptyState.container}>
                                    <h5 style={emptyState.title}>Không có lịch khám nào</h5>
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