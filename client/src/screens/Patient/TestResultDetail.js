import { useContext, useEffect, useState } from "react";
import { APPOINTMENT_ENDPOINTS, authApis, CLINIC_ENDPOINTS, endpoint, USER_ENDPOINTS, TEST_ENDPOINTS } from "../../configs/Apis";
import { exp } from "firebase/firestore/pipelines";
import { MyUserContext } from "../../configs/Contexts";
import { Card, Col, Container, Form, Row, Table, Modal, Button } from "react-bootstrap";
import Header from "../../components/Header";
import Footer from "../../components/Footer";
import MySpinner from "../../components/MySpinner";

const TestResultDetail = () => {


    const [user] = useContext(MyUserContext);
    const [testResults, setTestResults] = useState([]);
    const [labResultSheets, setLabResultSheets] = useState([]);
    const [selectedLabResult, setSelectedLabResult] = useState(null);
    const [showResultModal, setShowResultModal] = useState(false);
    const [patientProfiles, setPatientProfiles] = useState([]);
    const [selectedProfileId, setSelectedProfileId] = useState(null);
    const [loading, setLoading] = useState(false);

    const loadPatientProfiles = async () => {
        try {
            setLoading(true);
            const res = await authApis().get(USER_ENDPOINTS.PATIENT_PROFILES);
            setPatientProfiles(res.data);
           
            if (res.data && res.data.length > 0) {
                setSelectedProfileId(String(res.data[0].id));
            }
        } catch (err) {
            console.log(err);
        } finally {
            setLoading(false);
        }
    };


    const loadLabResultSheets = async (patientId) => {
        try {
            setLoading(true);
            const res = await authApis().get(TEST_ENDPOINTS.LAB_RESULTS, { params: { patientId } });
           
            let sheets = [];
            if (Array.isArray(res.data)) {
                sheets = res.data;
            } else if (res.data && typeof res.data === 'object') {
                sheets = [res.data];
            }
            setLabResultSheets(sheets);
            setSelectedLabResult(null);
            setTestResults([]);
        } catch (err) {
            console.log(err);
            setLabResultSheets([]);
        } finally {
            setLoading(false);
        }
    };

    const handleSelectLabResult = (labResult) => {
        setSelectedLabResult(labResult);

        if (labResult && labResult.resultDetails) {
            const transformedResults = labResult.resultDetails.map(result => ({
                id: result.id,
                testName: result.testName,
                value: result.value,
                result: result.value,
                unit: result.unit || '',
                normalRange: result.normalRange || '',
                isNormal: !result.isAbnormal,
                isAbnormal: result.isAbnormal,
                createAt: labResult.createdAt || labResult.testAt
            }));
            setTestResults(transformedResults);
        }
        setShowResultModal(true);
    };

    useEffect(() => {
        loadPatientProfiles();
    }, []);

    useEffect(() => {
        if (selectedProfileId) {
            loadLabResultSheets(selectedProfileId);
        }
    }, [selectedProfileId]);

    return (
        <>
            <div className="d-flex flex-column min-vh-100">
                <Header />
                {loading && (
                    <div className="position-fixed top-0 start-0 vw-100 vh-100 d-flex align-items-center justify-content-center bg-white bg-opacity-75" style={{ zIndex: 1060 }}>
                        <MySpinner />
                    </div>
                )}
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

                            </div>
                        </div>
                    </div>
                    

                    {selectedProfileId && (
                        <div className="mb-4 pb-3 border-bottom">
                           
                            {labResultSheets.length > 0 ? (
                                <div className="row g-3">
                                    {labResultSheets.map(sheet => (
                                        <div key={sheet.id} className="col-md-6 col-lg-4">
                                            <Card className=" shadow-sm rounded-4 h-100" style={{ cursor: 'pointer', transition: 'transform 0.2s' }}>
                                                <Card.Body className="p-4 d-flex flex-column">
                                                    <div className="row p-3 rounded-3 mb-3 mx-0" style={{ backgroundColor: '#e2e3e5' }}>
                                                        <div className="col-6 border-secondary border-opacity-25">
                                                            <small className="text-muted">Ngày xét nghiệm</small>
                                                            <div className="fw-semibold text-dark">{sheet.createdAt || sheet.testAt || '-'}</div>
                                                        </div>
                                                        <div className="col-6 ps-4">
                                                            <small className="text-muted">Bác sĩ chỉ định</small>
                                                            <div className="fw-semibold text-dark">{sheet.doctorName || 'N/A'}</div>
                                                        </div>
                                                    </div>
                                                    <div className="mb-4 d-flex justify-content-end">
                                                        <span className="fw-semibold px-3 py-2 fs-6 text-success rounded-3">
                                                            {sheet.status === 'COMPLETED' ? 'Đã xét nghiệm' : sheet.status === 'PENDING' ? 'Đang chờ' : 'Đã xác nhận'}
                                                        </span>
                                                    </div>
                                                    <div className="mt-auto">
                                                        <Button
                                                            className="btn btn-primary w-100 rounded-4 fw-semibold"
                                                            onClick={() => handleSelectLabResult(sheet)}
                                                        >
                                                            Xem chi tiết
                                                        </Button>
                                                    </div>
                                                </Card.Body>
                                            </Card>
                                        </div>
                                    ))}
                                </div>
                            ) : (
                                <div className="text-center text-muted py-4">
                                    <i className="bi bi-inbox fs-3 d-block mb-2 opacity-50"></i>
                                    Không có phiếu xét nghiệm nào.
                                </div>
                            )}
                        </div>
                    )}



                    <Modal show={showResultModal} onHide={() => setShowResultModal(false)} size="xl" centered>
                        <Modal.Header closeButton className="border-0 pb-0 pt-4 px-4">
                            <div>
                                <h5 className="mb-1 fw-bold">Kết quả xét nghiệm</h5>
                                <div className="text-secondary small">
                                    Bệnh nhân: <span className="fw-semibold text-dark">{selectedLabResult?.patientName || 'Không xác định'}</span>
                                </div>
                            </div>
                        </Modal.Header>

                        <Modal.Body className="p-4" >
                            <div className="d-flex flex-column flex-md-row gap-3 mb-4">
                                <div className="flex-fill rounded-4 p-3 bg-light">
                                    <div className="text-secondary small mb-1">Ngày xét nghiệm</div>
                                    <div className="fw-semibold text-dark">{selectedLabResult?.createdAt || selectedLabResult?.testAt || '-'}</div>
                                </div>
                                <div className="flex-fill rounded-4 p-3 bg-light">
                                    <div className="text-secondary small mb-1">Bác sĩ chỉ định</div>
                                    <div className="fw-semibold text-dark">{selectedLabResult?.doctorName || 'N/A'}</div>
                                </div>
                                <div className="flex-fill rounded-4 p-3 bg-light">
                                    <div className="text-secondary small mb-1">Trạng thái</div>
                                    <div className="text-dark fw-semibold">
                                        {selectedLabResult?.status === 'COMPLETED' ? 'Hoàn thành' : selectedLabResult?.status === 'PENDING' ? 'Đang chờ' : 'Đã xác nhận'}
                                    </div>
                                </div>
                            </div>

                            {testResults.length > 0 ? (
                                <div className="table-responsive rounded-4 overflow-hidden shadow-sm border" >
                                    <Table borderless hover className="align-middle mb-0 bg-white">
                                        <thead className="bg-white text-secondary small">
                                            <tr>
                                                <th className="py-3 px-3">Chỉ số xét nghiệm</th>
                                                <th className="py-3 px-3 text-center">Kết quả</th>
                                                <th className="py-3 px-3">Đơn vị</th>
                                                <th className="py-3 px-3">Tham chiếu</th>
                                                <th className="py-3 px-3 text-end">Đánh giá</th>
                                            </tr>
                                        </thead>
                                        <tbody className="text-dark">
                                            {testResults.map((result) => {
                                                const isAbnormal = result.isAbnormal === true || result.isNormal === false;
                                                return (
                                                    <tr key={result.id} className="border-bottom">
                                                        <td className="py-3 px-3 fw-semibold">{result.testName}</td>
                                                        <td className="py-3 px-3 text-center fs-5" style={{ fontFamily: 'monospace, sans-serif' }}>
                                                            {!result.result ? (
                                                                <span className="text-secondary">-</span>
                                                            ) : isAbnormal ? (
                                                                <span className="text-danger fw-bold">{result.result}</span>
                                                            ) : (
                                                                <span className="text-success fw-semibold">{result.result}</span>
                                                            )}
                                                        </td>
                                                        <td className="py-3 px-3 text-secondary small">{result.unit || '-'}</td>
                                                        <td className="py-3 px-3 text-secondary small">{result.normalRange || '-'}</td>
                                                        <td className="py-3 px-3 text-end">
                                                            {isAbnormal ? (
                                                                <span className="text-danger fw-semibold">Bất thường</span>
                                                            ) : (
                                                                <span className="text-secondary">Bình thường</span>
                                                            )}
                                                        </td>
                                                    </tr>
                                                );
                                            })}
                                        </tbody>
                                    </Table>
                                </div>
                            ) : (
                                <div className="text-center py-5 rounded-4 bg-secondary bg-opacity-10 border border-secondary border-opacity-10">
                                    <i className="bi bi-folder2-open display-5 mb-3 text-secondary"></i>
                                    <div className="fw-semibold mb-1">Không có dữ liệu xét nghiệm chi tiết</div>
                                    <div className="text-secondary">Phiếu này hiện đang trống hoặc chưa được nhập kết quả.</div>
                                </div>
                            )}
                        </Modal.Body>

                        <Modal.Footer className="border-0 pt-0 justify-content-end px-4 pb-4">
                            <Button variant="light" className="rounded-pill px-4" onClick={() => setShowResultModal(false)}>
                                Đóng
                            </Button>
                        </Modal.Footer>
                    </Modal>

                </Container>
                <Footer />
            </div>

        </>
    );
}
export default TestResultDetail;