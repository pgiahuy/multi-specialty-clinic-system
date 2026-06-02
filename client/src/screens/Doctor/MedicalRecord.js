import { useEffect, useRef, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { authApis, endpoint, TEST_ENDPOINTS } from "../../configs/Apis";
import Header from "../../components/Header";
import MySpinner from "../../components/MySpinner";
import MyAlert from "../../components/MyAlert";
import Footer from "../../components/Footer";
import { Button, Card, Container, Table, Modal, Row, Col, Form } from "react-bootstrap";
import {
    Clipboard2PulseFill,
    FileEarmarkMedicalFill,
    PersonCardList,
    Activity,
    PencilSquare,
    CheckCircleFill,
    XCircleFill,
    PlusCircleFill,
    EyeFill
} from "react-bootstrap-icons";
import { tableStyles, emptyState } from "./DoctorStyle";
import FloatAlert from "../../components/FloatAlert";

const MedicalRecord = () => {
    const { appointmentId } = useParams();
    const alertTimerRef = useRef(null);
    const [loading, setLoading] = useState(false);
    const [medicalRecord, setMedicalRecord] = useState(null);
    const [labResult, setLabResult] = useState([]);
    const [labResultStatus, setLabResultStatus] = useState(null);
    const [showTestResultsModal, setShowTestResultsModal] = useState(false);
    const [editableDiagnosis, setEditableDiagnosis] = useState('');
    const [editableNotes, setEditableNotes] = useState('');
    const [isEditing, setIsEditing] = useState(false);

    const [alertData, setAlertData] = useState({
        show: false,
        heading: '',
        message: '',
        variant: 'info'
    });
    const [isSaving, setIsSaving] = useState(false);
    const nav = useNavigate();

    const getStatusInVietnamese = (status) => {
        const statusMap = {
            'CONFIRMED': 'Chưa có kết quả',
            'PENDING': 'Chưa thanh toán',
            'COMPLETED': 'Đã xét nghiệm',

        };
        return statusMap[status] || status;
    };

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

            const response = await authApis().get(TEST_ENDPOINTS.LAB_RESULTS_BY_APPOINTMENT(appointmentId));

            // Set status
            if (response.data && response.data.status) {
                setLabResultStatus(response.data.status);
            }

            if (response.data && response.data.resultDetails) {
                const transformedResults = response.data.resultDetails.map(result => ({
                    id: result.id,
                    testName: result.testName,
                    value: result.value,
                    resultValue: result.value,
                    unit: result.unit || '',
                    normalRange: result.normalRange || '',
                    isNormal: result.isAbnormal === false,
                    isAbnormal: result.isAbnormal
                }));
                setLabResult(transformedResults);
            } else {
                setLabResult(response.data);
            }
        } catch (error) {
            console.error("Tải kết quả xét nghiệm thất bại:", error);
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
                handleShowAlert("Thành công", "Cập nhật bệnh án thành công!", "success");
            }
        } catch (error) {
            handleShowAlert("Lỗi", error.response?.data?.message || "Cập nhật thất bại! Bệnh nhân đã khám xong", "danger");
        } finally {
            setIsSaving(false);
        }
    };

    useEffect(() => {
        if (appointmentId) {
            loadMedicalRecord(appointmentId);
            loadTestResults(appointmentId);
        }
    }, [appointmentId]);

    return (
        <div className="d-flex flex-column min-vh-100 bg-light">
            <Header />

            <Container className="py-4 flex-grow-1 d-flex flex-column justify-content-center" style={{ maxWidth: '850px' }}>
                <div>
                    <FloatAlert show={alertData.show} heading={alertData.heading} variant={alertData.variant} message={alertData.message} >
                    </FloatAlert>
                </div>
                {
                    loading ? (
                        <div className="text-center py-5">
                            <MySpinner />
                        </div>
                    ) : !medicalRecord ? (

                        <Card className="border-0 shadow-sm text-center py-5 px-4 rounded-3">
                            <Card.Body>
                                <div className="mb-3 text-secondary opacity-50">
                                    <Clipboard2PulseFill size={64} />
                                </div>
                                <h4 className="fw-bold text-dark mb-3">Hồ Sơ Bệnh Án Trống</h4>
                                <p className="text-muted mb-4">Lịch hẹn này hiện tại vẫn chưa được tiến hành tạo bệnh án</p>
                                <Button
                                    variant="primary"
                                    className="shadow-sm px-4 py-2 fw-semibold rounded-pill"
                                    onClick={() => nav(`/doctor/create-medical-record?appointmentId=${appointmentId}`)}
                                >
                                    <PlusCircleFill className="me-2" /> Tạo bệnh án ngay
                                </Button>
                            </Card.Body>
                        </Card>
                    ) : (

                        <Card className="border-0 shadow-sm rounded-3 overflow-hidden">
                            <Card.Header className="bg-primary text-white p-3 pt-2 pb-2 d-flex justify-content-between align-items-center border-0">
                                <div className="d-flex align-items-center gap-2">
                                    <h5 className="mb-0 fw-bold tracking-wide py-2">CHI TIẾT HỒ SƠ BỆNH ÁN</h5>
                                </div>
                                <span className="badge bg-white text-primary fw-bold px-3 py-2 rounded-pill">
                                    Mã : BA#{medicalRecord.id}
                                </span>
                            </Card.Header>

                            <Card.Body className="p-4">

                                <div className="mb-3">
                                    <div className="d-flex align-items-center gap-2 mb-3 text-secondary border-bottom pb-2">
                                        <h6 className="mb-0 fw-bold text-uppercase text-dark">Thông tin bệnh nhân</h6>
                                    </div>
                                    <div className="bg-light p-3 rounded-3 border">
                                        <Row className="g-3">
                                            <Col xs={12} md={6}>
                                                <div className="text-muted small">Họ và tên</div>
                                                <div className="fw-bold text-primary fs-5">{medicalRecord.patientName || "N/A"}</div>
                                            </Col>
                                            <Col xs={6} md={3}>
                                                <div className="text-muted small">Ngày sinh</div>
                                                <div className="fw-semibold text-dark">{medicalRecord.dob || "N/A"}</div>
                                            </Col>
                                            <Col xs={6} md={3}>
                                                <div className="text-muted small">Giới tính</div>
                                                <div className="fw-semibold text-dark">{medicalRecord.gender || "N/A"}</div>
                                            </Col>
                                            <Col xs={12} md={8}>
                                                <div className="text-muted small">Địa chỉ</div>
                                                <div className="text-dark text-truncate">{medicalRecord.address || "N/A"}</div>
                                            </Col>
                                            <Col xs={12} md={4}>
                                                <div className="text-muted small">Ngày lập hồ sơ</div>
                                                <div className="text-dark fw-medium">{medicalRecord.createdAt || "N/A"}</div>
                                            </Col>
                                        </Row>
                                    </div>
                                </div>


                                <div className="mb-4">
                                    <Form>
                                        <Form.Group className="mb-3">
                                            <Form.Label className="fw-bold text-secondary small">Chuẩn đoán bệnh lý <span className="text-danger">*</span></Form.Label>
                                            {isEditing ? (
                                                <Form.Control
                                                    as="textarea"
                                                    rows={2}
                                                    className="border-primary"
                                                    value={editableDiagnosis}
                                                    onChange={(e) => setEditableDiagnosis(e.target.value)}
                                                    placeholder="Nhập kết luận chẩn đoán..."
                                                />
                                            ) : (
                                                <div className="p-3 bg-white border rounded-3 min-vh-10">
                                                    {medicalRecord.diagnosis || "Chưa có chẩn đoán"}
                                                </div>
                                            )}
                                        </Form.Group>

                                        <Form.Group className="mb-4">
                                            <Form.Label className="fw-bold text-secondary small">Ghi chú</Form.Label>
                                            {isEditing ? (
                                                <Form.Control
                                                    as="textarea"
                                                    rows={4}
                                                    className="border-primary"
                                                    value={editableNotes}
                                                    onChange={(e) => setEditableNotes(e.target.value)}
                                                    placeholder="Lời dặn, đơn thuốc đi kèm hoặc hướng dẫn tái khám..."
                                                />
                                            ) : (
                                                <div className="p-3 bg-white border rounded-3 text-dark min-vh-10" style={{ whiteSpace: 'pre-line' }}>
                                                    {medicalRecord.note || "Không có ghi chú bổ sung"}
                                                </div>
                                            )}
                                        </Form.Group>
                                    </Form>
                                </div>

                                <div className="mb-4 bg-light p-3 rounded-3 border d-flex justify-content-between align-items-center">
                                    <div>
                                        <div className="fw-bold text-dark">Kết quả xét nghiệm cận lâm sàng</div>
                                        <small className="text-muted">
                                            {labResult && labResult.length > 0 ? (
                                                <>
                                                    {labResultStatus && <p className="pt-3 me-2">{getStatusInVietnamese(labResultStatus)}</p>}

                                                </>
                                            ) : (
                                                "Bệnh án này không có dữ liệu xét nghiệm."
                                            )}
                                        </small>
                                    </div>
                                    {labResult && labResult.length > 0 ? (
                                        <Button
                                            variant="primary"
                                            className="fw-semibold px-3 btn-sm rounded-pill"
                                            onClick={() => setShowTestResultsModal(true)}
                                        >
                                            Xem kết quả ({labResult.length})
                                        </Button>
                                    ) : (
                                        <Button
                                            variant="outline-danger"
                                            className="fw-semibold px-3 btn-sm rounded-pill"
                                            onClick={() => nav(`/doctor/assign-test/${appointmentId}`)}
                                        >
                                            <PlusCircleFill className="me-1 mb-1" /> Chỉ định ngay
                                        </Button>
                                    )}
                                </div>


                                <div className="d-flex justify-content-end gap-2 pt-3 border-top">
                                    {!isEditing ? (
                                        <>
                                            <Button
                                                variant="outline-primary"
                                                className="px-4 fw-semibold rounded-pill shadow-sm"
                                                onClick={() => nav(`/doctor/prescribe/${medicalRecord.id}`)}
                                            >
                                                Kê đơn thuốc
                                            </Button>

                                            <Button
                                                variant="warning"
                                                className="px-4 fw-semibold text-white rounded-pill shadow-sm"
                                                onClick={() => setIsEditing(true)}
                                            >
                                                Chỉnh sửa
                                            </Button>
                                        </>
                                    ) : (
                                        <>
                                            <Button
                                                variant="outline-secondary"
                                                className="px-4 fw-semibold rounded-pill"
                                                onClick={() => {
                                                    setIsEditing(false);
                                                    setEditableDiagnosis(medicalRecord.diagnosis || '');
                                                    setEditableNotes(medicalRecord.note || '');
                                                }}
                                                disabled={isSaving}
                                            >
                                                Hủy bỏ
                                            </Button>
                                            <Button
                                                variant="success"
                                                className="px-4 fw-semibold rounded-pill shadow-sm"
                                                onClick={handleUpdateMedicalRecord}
                                                disabled={isSaving}
                                            >
                                                {isSaving ? <>Đang cập nhật...</> : <>Cập nhật hồ sơ</>}
                                            </Button>
                                        </>
                                    )}
                                </div>
                            </Card.Body>
                        </Card>
                    )
                }
            </Container >


            <Modal show={showTestResultsModal} onHide={() => setShowTestResultsModal(false)} size="lg" centered>
                <Modal.Header closeButton className="bg-light">
                    <Modal.Title className="fw-bold text-primary fs-5">
                        Kết quả xét nghiệm của {medicalRecord?.patientName || "bệnh nhân"} - Bệnh án: #BA{medicalRecord?.id || "-"}
                    </Modal.Title>
                </Modal.Header>
                <Modal.Body className="p-0">
                    {labResult ? (
                        <div style={tableStyles.container} className="border-0 m-0 rounded-0">
                            <Table hover responsive style={tableStyles.table} className="mb-0">
                                <thead>
                                    <tr style={tableStyles.headerRow}>
                                        <th style={{ ...tableStyles.headerCell, textAlign: 'center', width: '100px' }}>Mã XN</th>
                                        <th style={tableStyles.headerCell}>Tên xét nghiệm</th>
                                        <th style={{ ...tableStyles.headerCell, width: '90px' }}>Đơn vị</th>
                                        <th style={{ ...tableStyles.headerCell, width: '120px' }}>Kết quả</th>
                                        <th style={tableStyles.headerCell}>Tham chiếu</th>
                                        <th style={{ ...tableStyles.headerCell, width: '180px' }}>Đánh giá kết luận</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    {labResult.map((result, index) => (
                                        <tr
                                            key={result.id}
                                            style={tableStyles.bodyRow(index)}
                                            onMouseEnter={(e) => e.currentTarget.style.backgroundColor = '#e7f1ff'}
                                            onMouseLeave={(e) => e.currentTarget.style.backgroundColor = tableStyles.bodyRow(index).backgroundColor}
                                        >
                                            <td style={{ ...tableStyles.dataCell, textAlign: 'center' }} className="fw-medium text-secondary">XN_{result.id}</td>
                                            <td style={tableStyles.dataCellLeft} className="fw-bold text-dark">{result.testName}</td>
                                            <td style={tableStyles.dataCell}>{result.unit}</td>
                                            <td style={tableStyles.resultCell} className="fw-bold text-primary">{result.resultValue}</td>
                                            <td style={tableStyles.dataCell} className="text-muted">{result.normalRange}</td>
                                            <td style={tableStyles.dataCell}>
                                                {result.isNormal === true ? (
                                                    <span className="badge bg-success-subtle text-success border border-success px-2 py-1 rounded-3">Bình thường</span>
                                                ) : result.isNormal === false ? (
                                                    <span className="badge bg-danger-subtle text-danger border border-danger px-2 py-1 rounded-3 fw-bold">Bất thường (!)</span>
                                                ) : (
                                                    <span className="text-muted">-</span>
                                                )}
                                            </td>
                                        </tr>
                                    ))}
                                </tbody>
                            </Table>
                        </div>
                    ) : (
                        <div style={emptyState.container} className="py-5">
                            <h5 style={emptyState.title} className="text-muted">Không tìm thấy dữ liệu kết quả xét nghiệm</h5>
                        </div>
                    )}
                </Modal.Body>
                <Modal.Footer className="bg-light">
                    <Button variant="secondary" className="px-4 fw-medium" onClick={() => setShowTestResultsModal(false)}>
                        Đóng cửa sổ
                    </Button>
                </Modal.Footer>
            </Modal>

            <Footer />
        </div >
    );
};

export default MedicalRecord;