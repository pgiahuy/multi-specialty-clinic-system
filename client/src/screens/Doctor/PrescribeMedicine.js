import { useEffect, useRef, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { Button, Card, Col, Container, Form, InputGroup, Row, Table } from "react-bootstrap";
import { ArrowLeft, XCircleFill } from "react-bootstrap-icons";
import Footer from "../../components/Footer";
import Header from "../../components/Header";
import { authApis, CLINIC_ENDPOINTS, clinicApis } from "../../configs/Apis";

const MEDICINE_PAGE_SIZE = 10;

const PrescribeMedicine = () => {
    const navigate = useNavigate();
    const { medicalRecordId } = useParams();

    const [loading, setLoading] = useState(false);
    const [searching, setSearching] = useState(false);
    const [loadingMoreMedicines, setLoadingMoreMedicines] = useState(false);
    const [saving, setSaving] = useState(false);
    const [medicalRecord, setMedicalRecord] = useState(null);
    const [appointment, setAppointment] = useState(null);
    const [searchTerm, setSearchTerm] = useState("");
    const [searchResults, setSearchResults] = useState([]);
    const [medicinePage, setMedicinePage] = useState(1);
    const [hasMoreMedicines, setHasMoreMedicines] = useState(true);
    const [prescriptionItems, setPrescriptionItems] = useState([]);
    const [statusMessage, setStatusMessage] = useState("");

    const medicineListContainerRef = useRef(null);
    const medicineLoadMoreRef = useRef(null);

    const stickyStyle = { top: "75px" };
    const scrollableStyle = { maxHeight: "62vh", overflowY: "auto", paddingRight: "6px" };
    const actionBtnClass = "btn-sm rounded-pill fw-semibold px-3";

    const loadMedicines = async ({ kw = "", page = 1, replace = true } = {}) => {
        try {
            if (replace) {
                setSearching(true);
            } else {
                setLoadingMoreMedicines(true);
            }

            const params = { page, pageSize: MEDICINE_PAGE_SIZE };
            if (kw.trim()) {
                params.kw = kw.trim();
            }

            const response = await clinicApis.getMedicines(params);
            const fetchedMedicines = Array.isArray(response?.data) ? response.data : [];

            setSearchResults((currentMedicines) => {
                if (replace) {
                    return fetchedMedicines;
                }

                const currentIds = new Set(currentMedicines.map((medicine) => String(medicine.id)));
                const uniqueMedicines = fetchedMedicines.filter((medicine) => !currentIds.has(String(medicine.id)));
                return [...currentMedicines, ...uniqueMedicines];
            });

            setMedicinePage(page);
            setHasMoreMedicines(fetchedMedicines.length === MEDICINE_PAGE_SIZE);
        } catch (error) {
            console.error("Tải danh sách thuốc thất bại:", error);
            if (replace) {
                setSearchResults([]);
            }
            setHasMoreMedicines(false);
        } finally {
            if (replace) {
                setSearching(false);
            } else {
                setLoadingMoreMedicines(false);
            }
        }
    };

    useEffect(() => {
        const loadMedicalRecord = async () => {
            if (!medicalRecordId) {
                return;
            }

            setLoading(true);
            try {
                const response = await authApis().get(CLINIC_ENDPOINTS.MEDICAL_RECORD_BY_ID(medicalRecordId));
                setMedicalRecord(response.data);
            } catch (error) {
                console.error("Lỗi khi tải thông tin bệnh án!:", error);
            } finally {
                setLoading(false);
            }
        };

        loadMedicalRecord();
    }, [medicalRecordId]);

    useEffect(() => {
        const loadAppointment = async () => {
            if (!medicalRecord?.appointmentId) {
                return;
            }

            setLoading(true);
            try {
                const response = await authApis().get(CLINIC_ENDPOINTS.APPOINTMENT_BY_ID(medicalRecord.appointmentId));
                setAppointment(response.data);
            } catch (error) {
                console.error("Lỗi khi tải thông tin phiếu hẹn!:", error);
            } finally {
                setLoading(false);
            }
        };

        loadAppointment();
    }, [medicalRecord]);

    useEffect(() => {
        const trimmedSearchTerm = searchTerm.trim();

        if (!trimmedSearchTerm) {
            setStatusMessage("");
            setHasMoreMedicines(true);
            setMedicinePage(1);
            loadMedicines({ page: 1, replace: true });
            return;
        }

        const debounceTimer = window.setTimeout(() => {
            setHasMoreMedicines(true);
            setMedicinePage(1);
            loadMedicines({ kw: trimmedSearchTerm, page: 1, replace: true });
        }, 400);

        return () => window.clearTimeout(debounceTimer);
    }, [searchTerm]);

    useEffect(() => {
        if (searchTerm.trim()) {
            return;
        }

        const container = medicineListContainerRef.current;
        const sentinel = medicineLoadMoreRef.current;

        if (!container || !sentinel) {
            return;
        }

        const observer = new IntersectionObserver(
            (entries) => {
                const firstEntry = entries[0];
                if (!firstEntry?.isIntersecting || searching || loadingMoreMedicines || !hasMoreMedicines) {
                    return;
                }

                loadMedicines({ page: medicinePage + 1, replace: false });
            },
            {
                root: container,
                rootMargin: "0px 0px 80px 0px",
                threshold: 0.1,
            }
        );

        observer.observe(sentinel);

        return () => observer.disconnect();
    }, [searchTerm, searching, loadingMoreMedicines, hasMoreMedicines, medicinePage]);

    const handleClearSearch = () => {
        setSearchTerm("");
        setSearchResults([]);
        setStatusMessage("");
    };

    const addMedicineToPrescription = (medicine) => {
        if (!medicine?.id) {
            return;
        }

        if (medicine.totalStock !== undefined && medicine.totalStock <= 0) {
            alert("Thuốc này hiện không còn tồn kho.");
            return;
        }

        setPrescriptionItems((currentItems) => {
            const existingItem = currentItems.find((item) => item.id === medicine.id);

            if (existingItem) {
                return currentItems.map((item) => {
                    if (item.id !== medicine.id) {
                        return item;
                    }

                    const maxQuantity = medicine.totalStock > 0 ? medicine.totalStock : item.quantity + 1;
                    return {
                        ...item,
                        quantity: Math.min(item.quantity + 1, maxQuantity),
                    };
                });
            }

            return [
                ...currentItems,
                {
                    id: medicine.id,
                    name: medicine.name,
                    code: medicine.code,
                    unit: medicine.unit || "Viên",
                    totalStock: medicine.totalStock ?? 0,
                    quantity: 1,
                },
            ];
        });

        setStatusMessage(`Đã thêm ${medicine.name} vào toa thuốc.`);
    };

    const updatePrescriptionQuantity = (medicineId, value) => {
        const parsedValue = Math.max(1, Number.parseInt(value, 10) || 1);

        setPrescriptionItems((currentItems) =>
            currentItems.map((item) => {
                if (item.id !== medicineId) {
                    return item;
                }

                const maxQuantity = item.totalStock > 0 ? item.totalStock : parsedValue;
                return {
                    ...item,
                    quantity: Math.min(parsedValue, maxQuantity),
                };
            })
        );
    };

    const removePrescriptionItem = (medicineId) => {
        setPrescriptionItems((currentItems) => currentItems.filter((item) => item.id !== medicineId));
    };

    const handleSavePrescription = async (isDraft = false) => {
        if (!medicalRecordId) {
            alert("Không tìm thấy hồ sơ bệnh án.");
            return;
        }

        if (prescriptionItems.length === 0) {
            alert("Hãy thêm ít nhất một thuốc vào toa trước khi lưu.");
            return;
        }

        try {
            setSaving(true);
            setStatusMessage("");

            const payload = {
                medicalRecordId: Number(medicalRecordId),
                items: prescriptionItems.map((item) => ({
                    medicineId: item.id,
                    quantity: item.quantity,
                })),
            };

            await clinicApis.createPrescription(payload);

            setStatusMessage(isDraft ? "Đã lưu nháp toa thuốc." : "Đã lưu toa thuốc thành công.");

            if (!isDraft) {
                setPrescriptionItems([]);
            }
        } catch (error) {
            console.error("Lưu toa thuốc thất bại:", error);
            alert(error.response?.data?.message || "Lưu toa thuốc thất bại. Vui lòng thử lại!");
        } finally {
            setSaving(false);
        }
    };

    const handleSearchMedicine = (value) => {
        setSearchTerm(value);
    };

    const isBrowsingMedicines = !searchTerm.trim();

    return (
        <div className="d-flex flex-column min-vh-100 bg-light">
            <Header />

            <Container className="py-4 flex-grow-1" style={{ maxWidth: "95%" }}>
                <Row className="g-4">
                    <Col xs={12} lg={6}>
                        <div className="d-flex flex-column gap-4">


                            <Card className="border-0 shadow-sm rounded-3">
                                <Card.Body className="p-3">
                                    <div className="bg-light p-2 border mb-3 rounded-2">
                                        <Row className="g-2 align-items-center">
                                            <InputGroup size="md">
                                                <Form.Control
                                                    placeholder="Nhập tên, mã thuốc..."
                                                    value={searchTerm}
                                                    onChange={(e) => handleSearchMedicine(e.target.value)}
                                                />
                                                <Button variant="primary" type="button" onClick={handleClearSearch}>
                                                    x
                                                </Button>
                                            </InputGroup>
                                        </Row>
                                    </div>

                                    <div className="mb-3">

                                        {isBrowsingMedicines ? (
                                            <div className="text-muted small mb-2">Để trống ô tìm kiếm để xem danh sách thuốc theo từng trang.</div>
                                        ) : searching ? (
                                            <div className="text-muted small">Đang tìm thuốc...</div>
                                        ) : searchResults.length === 0 ? (
                                            <div className="text-muted small">Không tìm thấy thuốc phù hợp.</div>
                                        ) : null}

                                        <div
                                            ref={medicineListContainerRef}
                                            className="border rounded-2 bg-white"
                                            style={{ maxHeight: "65vh", overflowY: "auto" }}
                                        >
                                            {searchResults.length > 0 ? (
                                                <Table responsive borderless className="align-middle table-hover table-sm mb-0">
                                                    <thead className="table-light border-bottom text-secondary small">
                                                        <tr>
                                                            <th className="text-center" style={{ width: "50px", position: "sticky", top: 0, zIndex: 3, background: "#f8f9fa" }}>STT</th>
                                                            <th style={{ position: "sticky", top: 0, zIndex: 3, background: "#f8f9fa" }}>Tên thuốc</th>
                                                            <th className="text-center" style={{ width: "100px", position: "sticky", top: 0, zIndex: 3, background: "#f8f9fa" }}>Tồn kho</th>
                                                            <th className="text-center" style={{ width: "120px", position: "sticky", top: 0, zIndex: 3, background: "#f8f9fa" }}>Đơn vị</th>
                                                            <th style={{ width: "110px", position: "sticky", top: 0, zIndex: 3, background: "#f8f9fa" }}></th>
                                                        </tr>
                                                    </thead>
                                                    <tbody>
                                                        {searchResults.map((medicine, index) => {
                                                            const added = prescriptionItems.some((item) => item.id === medicine.id);

                                                            return (
                                                                <tr key={medicine.id}>
                                                                    <td className="text-center text-muted small">{index + 1}</td>
                                                                    <td>
                                                                        <div className="fw-bold text-dark">{medicine.name}</div>
                                                                        <small className="text-muted">{medicine.code || "Không có mã"}</small>
                                                                    </td>
                                                                    <td className="text-center fw-semibold">{medicine.totalStock ?? 0}</td>
                                                                    <td className="text-center small text-secondary">{medicine.unit || "Viên"}</td>
                                                                    <td className="text-end">
                                                                        <Button
                                                                            size="sm"
                                                                            variant={added ? "outline-secondary" : "outline-primary"}
                                                                            onClick={() => addMedicineToPrescription(medicine)}
                                                                            disabled={medicine.totalStock !== undefined && medicine.totalStock <= 0}
                                                                        >
                                                                            {added ? "Đã thêm" : "Thêm"}
                                                                        </Button>
                                                                    </td>
                                                                </tr>
                                                            );
                                                        })}
                                                    </tbody>
                                                </Table>
                                            ) : null}

                                            {isBrowsingMedicines ? (
                                                <div ref={medicineLoadMoreRef} className="py-2 text-center text-muted small border-top">
                                                    {loadingMoreMedicines ? "Đang tải thêm thuốc..." : hasMoreMedicines ? "Cuộn xuống để tải thêm" : "Đã hiển thị hết danh sách"}
                                                </div>
                                            ) : null}
                                        </div>
                                    </div>

                                    {statusMessage ? <div className="alert alert-success py-2 small mb-0">{statusMessage}</div> : null}
                                </Card.Body>
                            </Card>
                        </div>
                    </Col>

                    <Col xs={12} lg={6}>
                        <Card className="border-0 shadow-sm rounded-3 bg-white">
                            <Card.Body className="bg-dark text-white fw-bold py-3 border-0 d-flex justify-content-between align-items-center">
                                <span className="small tracking-wide">THÔNG TIN LÂM SÀNG</span>
                                <span className="badge bg-secondary fw-semibold rounded-pill">BA#{medicalRecord?.id}</span>
                            </Card.Body>

                            <Card.Body className="p-3 d-flex flex-row" style={scrollableStyle}>
                                <Col lg={4}>
                                    <div className="text-muted small mb-1">Bệnh nhân tiếp nhận</div>
                                    <div className="fw-bold text-primary fs-5">{medicalRecord?.patientName}</div>
                                    <div className="small mt-1 fw-medium">Giới tính: {medicalRecord?.gender}</div>
                                    <div className="small mt-1 fw-medium">Ngày sinh: {medicalRecord?.dob}</div>
                                </Col>

                                <Col lg={8}>
                                    <div>
                                        <div className="small mb-1">Chẩn đoán bệnh lý</div>
                                        <div className="p-2 bg-light border border-secondary-subtle text-dark rounded-0 fw-semibold small">
                                            {medicalRecord?.diagnosis || "Chưa có kết luận chẩn đoán"}
                                        </div>
                                    </div>

                                    <div className="mb-2 mt-3">
                                        <div className="small mb-1">Ghi chú</div>
                                        <div className="p-2 bg-light border border-light-subtle text-muted rounded-0 small" style={{ whiteSpace: "pre-line" }}>
                                            {medicalRecord?.note || "Không có ghi chú bổ sung từ hồ sơ bệnh án."}
                                        </div>
                                    </div>
                                </Col>
                            </Card.Body>
                        </Card>

                        <Card className="border-0 shadow-sm rounded-3 sticky-top" style={stickyStyle}>
                            <Card.Header className="bg-white border-bottom py-3 px-3">
                                <h5 className="mb-0 fw-bold text-dark small tracking-wide text-uppercase">Toa thuốc hiện tại</h5>
                            </Card.Header>

                            <Card.Body className="p-3" style={scrollableStyle}>
                                <div className="mb-3">
                                    {prescriptionItems.length === 0 ? (
                                        <div className="p-4 border rounded-2 bg-white text-muted small text-center">
                                            Chưa có thuốc nào trong toa. Hãy tìm thuốc ở cột bên trái và thêm vào danh sách.
                                        </div>
                                    ) : (
                                        <Table responsive borderless className="align-middle border table-hover table-sm mb-0">
                                            <thead className="table-light border-bottom text-secondary small">
                                                <tr>
                                                    <th style={{ width: "50px" }} className="text-center">STT</th>
                                                    <th>Tên thuốc</th>
                                                    <th style={{ width: "130px" }} className="text-center">Số lượng</th>
                                                    <th style={{ width: "120px" }} className="text-center">Đơn vị</th>
                                                    <th style={{ width: "50px" }}></th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                {prescriptionItems.map((medicine, index) => (
                                                    <tr key={medicine.id}>
                                                        <td className="text-center text-muted small">{index + 1}</td>
                                                        <td>
                                                            <div className="fw-bold text-dark">{medicine.name}</div>
                                                            <small className="text-muted">Tồn kho: {medicine.totalStock}</small>
                                                        </td>
                                                        <td>
                                                            <Form.Control
                                                                type="number"
                                                                min={1}
                                                                max={medicine.totalStock > 0 ? medicine.totalStock : undefined}
                                                                value={medicine.quantity}
                                                                onChange={(e) => updatePrescriptionQuantity(medicine.id, e.target.value)}
                                                                className="text-center"
                                                            />
                                                        </td>
                                                        <td className="text-center fw-semibold">{medicine.unit}</td>
                                                        <td>
                                                            <Button variant="link" className="text-danger p-0" onClick={() => removePrescriptionItem(medicine.id)}>
                                                                <XCircleFill size={18} />
                                                            </Button>
                                                        </td>
                                                    </tr>
                                                ))}
                                            </tbody>
                                        </Table>
                                    )}
                                </div>

                                <div className="d-flex justify-content-end gap-2 pt-3 border-top mt-3">
                                    <Button variant="outline-secondary" className={actionBtnClass} onClick={() => navigate(-1)} disabled={saving}>
                                        Hủy bỏ
                                    </Button>
                                    <Button variant="primary" className={actionBtnClass} onClick={() => handleSavePrescription(true)} disabled={saving}>
                                        {saving ? "Đang lưu..." : "Lưu nháp"}
                                    </Button>
                                    <Button variant="success" className={`px-4 ${actionBtnClass}`} onClick={() => handleSavePrescription(false)} disabled={saving}>
                                        {saving ? "Đang lưu..." : "Lưu"}
                                    </Button>
                                </div>
                            </Card.Body>
                        </Card>
                    </Col>
                </Row>
            </Container>

            <Footer />
        </div>
    );
};

export default PrescribeMedicine;
