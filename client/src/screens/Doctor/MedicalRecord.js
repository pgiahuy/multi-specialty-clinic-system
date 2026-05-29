import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { authApis, endpoint } from "../../configs/Apis";
import Header from "../../components/Header";

import MySpinner from "../../components/MySpinner";
import { Calendar2Check, Clipboard2Pulse, Clipboard2PulseFill, FileEarmarkMedical, FileEarmarkMedicalFill, HeartPulse, PersonVcard, PlusCircle, Eye } from "react-bootstrap-icons";
import Footer from "../../components/Footer";
import { Button, Card, Container, Table, Modal } from "react-bootstrap";
import { tableStyles, emptyState } from "./DoctorStyle";

const MedicalRecord = () => {
    const { appointmentId } = useParams();
    const [loading, setLoading] = useState(false);
    const [medicalRecord, setMedicalRecord] = useState(null);
    const [testResults, setTestResults] = useState([]);
    const [showTestResultsModal, setShowTestResultsModal] = useState(false);
    const [editableDiagnosis, setEditableDiagnosis] = useState('');
    const [editableNotes, setEditableNotes] = useState('');
    const [isEditing, setIsEditing] = useState(false);
    const [isSaving, setIsSaving] = useState(false);
    const nav = useNavigate();


    const medicalRecordInfo = [{
        label: "Bệnh nhân",
        value: "patientName",
    }, {
        label: "Ngày sinh",
        value: "dob",
    }, {
        label: "Giới tính",
        value: "gender",
    }, {
        label: "Địa chỉ",
        value: "address",
    }, {
        label: "Ngày tạo",
        value: "createdAt",
    }, {
        label: "Chuẩn đoán",
        value: "diagnosis",
    }, {
        label: "Ghi chú",
        value: "note",
    },];

    const loadMedicalRecord = async () => {
        try {
            setLoading(true);
            const response = await authApis().get(endpoint['medical-record'](appointmentId));
            setMedicalRecord(response.data);
            setEditableDiagnosis(response.data.diagnosis || '');
            setEditableNotes(response.data.note || '');
        } catch (error) {
            console.error("Tải bệnh án thất bại:", error);
        } finally {
            setLoading(false);
        }
    };

  

    const loadTestResults = async () => {
        try {
            const response = await authApis().get(endpoint['test-result-appointment'](appointmentId));
            setTestResults(response.data);
        } catch (error) {
            console.error("Tải kết quả xét nghiệm thất bại:", error);
        }
    };


    const handleUpdateMedicalRecord = async () => {
        try {
            
            setIsSaving(true); 
            const requestData = {
                id: medicalRecord.id,
                diagnosis: editableDiagnosis, 
                note: editableNotes,          
                appointmentId: appointmentId
            };

            const response = await authApis().put(
                endpoint['medical-record-update'](medicalRecord.id),
                requestData
            );

            if (response.status === 200) {
                
                setIsEditing(false); 

                setMedicalRecord(prevState => ({
                    ...prevState,
                    diagnosis: editableDiagnosis,
                    note: editableNotes
                }));
            }

        } catch (error) {
            console.error("Cập nhật thất bại:", error);
            
        } finally {
            setIsSaving(false);
        }
    };


    // const handleAddLabTest = () => {
    //     // Logic để chỉ định xét nghiệm mới cho bệnh nhân
    //     try {

    //     }

    // };

    useEffect(() => {
        if (appointmentId) {
            loadMedicalRecord(appointmentId);
            loadTestResults(appointmentId);
        }
    }, [appointmentId]);




    return (
        <div className="d-flex flex-column min-vh-100 bg-light">
            <Header />

            <Container className="py-2 flex-grow-1 d-flex flex-column justify-content-center" style={{ maxWidth: '600px' }}>
                <div className="text-center mb-3">
                    <h4 className="fw-bold text-primary mb-0">

                        Hồ sơ bệnh án
                    </h4>
                </div>


                {loading ? (
                    <div className="text-center py-5">
                        <MySpinner />

                    </div>
                ) :

                    !medicalRecord ? (
                        <Card className="border-0 shadow-sm rounded-3 py-4">
                            <Card.Body className="text-center py-3">
                                <div className="mb-3">
                                    <Clipboard2PulseFill size={48} className="text-secondary opacity-50" />
                                </div>
                                <h5 className="fw-bold text-dark mb-2">Chưa có bệnh án</h5>
                                <p className="text-muted small mb-3">Tạo bệnh án mới để ghi nhận tình trạng sức khỏe bệnh nhân.</p>
                                <Button
                                    variant="primary"
                                    className="rounded-pill px-3 py-1 fw-semibold"
                                    size="sm"
                                >
                                    <PlusCircle className="me-2 mb-1" />
                                    Thêm bệnh án
                                </Button>
                            </Card.Body>
                        </Card>
                    ) :


                        (
                            <Card className="border-0 shadow-sm rounded-3 overflow-hidden">
                                <Card.Body className="p-2">
                                    {medicalRecordInfo.map(info => {
                                        const isEditableField = info.value === 'diagnosis' || info.value === 'note';
                                        return (
                                            <div key={info.value} className="mb-2 p-2">
                                                <span className="fw-bold me-2">{info.label}:</span>
                                                {isEditableField && isEditing ? (
                                                    <textarea
                                                        className="form-control"
                                                        value={info.value === 'diagnosis' ? editableDiagnosis : editableNotes}
                                                        onChange={(e) => {
                                                            if (info.value === 'diagnosis') {
                                                                setEditableDiagnosis(e.target.value);
                                                            } else {
                                                                setEditableNotes(e.target.value);
                                                            }
                                                        }}
                                                        rows="3"
                                                        placeholder={`Nhập ${info.label.toLowerCase()}`}
                                                        style={{ fontSize: '14px', borderRadius: '4px' }}
                                                    />
                                                ) : (
                                                    <span className="text-muted">{medicalRecord[info.value] || "N/A"}</span>
                                                )}
                                            </div>
                                        );
                                    })}
                                    <div className="p-2">
                                        <div className="fw-bold me-2">Kết quả xét nghiệm (nếu có):</div>
                                        {testResults.length === 0 ? (
                                            <Button variant="outline-danger" className="border-0 rounded-4 px-3 py-2" 
                                            onClick={() => nav(`/doctor/assign-test/${appointmentId}`)}>
                                                Chỉ định xét nghiệm
                                            </Button>
                                        ) : (
                                            <Button
                                                variant="outline-info"
                                                className="rounded-3 border-0 px-3"
                                                onClick={() => setShowTestResultsModal(true)}
                                            >
                                                Xem kết quả
                                            </Button>
                                        )}

                                    </div>

                                    <div className="text-center p-2">
                                        {!isEditing ? (
                                            <Button
                                                variant="primary"
                                                className="rounded-4 px-3 p-2 me-2"
                                                onClick={() => setIsEditing(true)}
                                            >
                                                Chỉnh sửa
                                            </Button>
                                        ) : (
                                            <>
                                             <Button
                                                    variant="outline-danger"
                                                    className="rounded-4 px-3 py-2 me-2"
                                                    onClick={() => {
                                                        setIsEditing(false);
                                                        setEditableDiagnosis(medicalRecord.diagnosis || '');
                                                        setEditableNotes(medicalRecord.notes || '');
                                                    }}
                                                >
                                                    Hủy
                                                </Button>
                                                <Button
                                                    variant="primary"
                                                    className="rounded-4 px-3 py-2"
                                                    onClick={handleUpdateMedicalRecord}
                                                    disabled={loading}
                                                    disabled={isSaving}
                                                >
                                                    {isSaving ? 'Đang lưu...' : 'Lưu'}
                                                </Button>
                                               
                                            </>
                                        )}
                                    </div>

                                </Card.Body>
                            </Card>
                        )}
            </Container>

            <Modal show={showTestResultsModal} onHide={() => setShowTestResultsModal(false)} size="lg" centered>
                <Modal.Header closeButton>
                    <Modal.Title className="fw-bold">Kết quả xét nghiệm</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    {testResults.length > 0 ? (
                        <div style={tableStyles.container}>
                            <Table hover responsive style={tableStyles.table}>
                                <thead>
                                    <tr style={tableStyles.headerRow}>
                                        <th style={{ ...tableStyles.headerCell, textAlign: 'center' }}>Mã</th>
                                        <th style={tableStyles.headerCell}>Xét nghiệm</th>
                                        <th style={tableStyles.headerCell}>Đơn vị</th>
                                        <th style={tableStyles.headerCell}>Kết quả</th>
                                        <th style={tableStyles.headerCell}>Khoảng tham chiếu</th>
                                        <th style={tableStyles.headerCell}>Kết luận</th>
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
                                            <td style={tableStyles.dataCell}>{result.unit}</td>
                                            <td style={tableStyles.resultCell}>{result.resultValue}</td>
                                            <td style={tableStyles.dataCell}>{result.normalRange}</td>
                                            <td style={tableStyles.dataCell}>{result.isNormal === true
                                                ? 'Bình thường'
                                                : result.isNormal === false
                                                    ? 'Không bình thường'
                                                    : ''}</td>
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
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={() => setShowTestResultsModal(false)}>
                        Đóng
                    </Button>
                </Modal.Footer>
            </Modal>

            <Footer />
        </div>
    );


};

export default MedicalRecord;