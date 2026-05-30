import { useEffect, useRef, useState, useMemo } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { Button, ButtonGroup, Badge, Card, Col, Container, Form, InputGroup, Row, Table } from "react-bootstrap";
import { ArrowLeft, Plus, PlusSquare, PlusSquareFill, XCircleFill, Save, CheckCircle, XSquareFill, TwitterX, XSquare } from "react-bootstrap-icons";
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
    const scrollableStyle = { paddingRight: "6px" };
    const smallInputStyle = { maxWidth: "76px", margin: "0 auto" };
    const noteInputStyle = { minWidth: "110px", resize: "none", fontSize: "0.9rem" };
    const searchRowStyle = { fontSize: "0.92rem", lineHeight: 1.08 };

    const prescriptionTotals = useMemo(() => {
        const totalQuantity = prescriptionItems.reduce((sum, it) => sum + (Number(it.quantity) || 0), 0);
        return { totalItems: prescriptionItems.length, totalQuantity };
    }, [prescriptionItems]);


    const getAgeFromBirthYear = (birthValue) => {
        if (!birthValue) {
            return "N/A";
        }

        const birthText = String(birthValue).trim();
        const yearMatch = birthText.match(/\b(19|20)\d{2}\b/);
        const birthYear = yearMatch ? Number.parseInt(yearMatch[0], 10) : Number.parseInt(birthText, 10);

        if (!Number.isFinite(birthYear)) {
            return "N/A";
        }

        const currentYear = new Date().getFullYear();
        const age = currentYear - birthYear;

        return age >= 0 ? `${age}` : "N/A";
    };

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
                    daysToUse: 7,
                    note: "",
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

    const updatePrescriptionDays = (medicineId, value) => {
        const parsedValue = Math.max(1, Number.parseInt(value, 10) || 1);

        setPrescriptionItems((currentItems) =>
            currentItems.map((item) => (item.id !== medicineId ? item : { ...item, daysToUse: parsedValue }))
        );
    };

    const updatePrescriptionNote = (medicineId, value) => {
        setPrescriptionItems((currentItems) =>
            currentItems.map((item) => (item.id !== medicineId ? item : { ...item, note: value }))
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
                    daysToUse: item.daysToUse,
                    note: item.note,
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

            <Container className="py-3 flex-grow-1" style={{ maxWidth: "96%" }}>
                <Row className="g-2">
                    <Col xs={12} lg={5} >
                        <div className="d-flex flex-column gap-3" >
                            <Card className="border-0 shadow-sm rounded-3">

                                <Card.Body className="p-2">
                                    <div className="bg-light p-2 border mb-1 rounded-2">
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
                                    {statusMessage ? <div className="alert alert-success py-1 small mb-1">{statusMessage}</div> : null}

                                    <div className="mb-2">

                                        {isBrowsingMedicines ? (<>  </>
                                        ) : searching ? (
                                            <div className="text-muted small">Đang tìm thuốc...</div>
                                        ) : searchResults.length === 0 ? (
                                            <div className="text-muted small">Không tìm thấy thuốc phù hợp.</div>
                                        ) : null}

                                        <div
                                            ref={medicineListContainerRef}
                                            className="border rounded-2 bg-white thin-scrollbar "
                                            style={{ maxHeight: "70vh", overflowY: "auto" }}
                                        >
                                            <Table borderless className="align-middle table-hover table-sm mb-0">
                                                <thead className="table-light border-bottom text-secondary small sticky-top top-0">                                                    <tr>
                                                    <th className="text-center" style={{ width: "50px", position: "sticky", top: 0, zIndex: 3, background: "#f8f9fa" }}>STT</th>
                                                    <th style={{ position: "sticky", top: 0, zIndex: 3, background: "#f8f9fa" }}>Tên thuốc</th>
                                                    <th className="text-center" style={{ width: "4rem", position: "sticky", top: 0, zIndex: 3, background: "#f8f9fa" }}>Kho</th>
                                                    <th className="text-center" style={{ width: "4rem", position: "sticky", top: 0, zIndex: 3, background: "#f8f9fa" }}>Đơn vị</th>
                                                    <th style={{ width: "3rem", position: "sticky", top: 0, zIndex: 3, background: "#f8f9fa" }}></th>
                                                </tr>
                                                </thead>
                                                {searchResults.length > 0 ? (

                                                    <tbody>
                                                        {searchResults.map((medicine, index) => {
                                                            const added = prescriptionItems.some((item) => item.id === medicine.id);

                                                            return (
                                                                <tr key={medicine.id} style={searchRowStyle}>
                                                                    <td className="text-center text-muted small py-1">{index + 1}</td>
                                                                    <td className="py-1">
                                                                        <div className="fw-bold text-dark">{medicine.name}</div>
                                                                        <small className="text-muted">{medicine.code || "Không có mã"}</small>
                                                                    </td>
                                                                    <td className="text-center text-muted small fw-semibold py-1">
                                                                        {medicine.totalStock ?? 0}
                                                                    </td>
                                                                    <td className="text-center small py-1">
                                                                        {medicine.unit || "Viên"}
                                                                    </td>
                                                                    <td className="text-end py-1">
                                                                        <Button
                                                                            size="md"
                                                                            variant="link"
                                                                            onClick={() => addMedicineToPrescription(medicine)}
                                                                            disabled={medicine.totalStock !== undefined && medicine.totalStock <= 0}
                                                                            className={added ? "text-success p-0" : "text-primary p-0"}
                                                                            style={{ padding: 0, minWidth: "3rem" }}
                                                                            title={added ? "Đã thêm" : "Thêm"}
                                                                        >
                                                                            {added ? <CheckCircle size={18} /> : <PlusSquareFill size={18} />}
                                                                        </Button>
                                                                    </td>
                                                                </tr>
                                                            );
                                                        })}
                                                    </tbody>

                                                ) : null}

                                            </Table>
                                            {isBrowsingMedicines ? (
                                                <div ref={medicineLoadMoreRef} className="py-2 text-center text-muted small border-top">
                                                    {loadingMoreMedicines ? "Đang tải thêm thuốc..." : hasMoreMedicines ? "Cuộn xuống để tải thêm" : "Đã hiển thị hết danh sách"}
                                                </div>
                                            ) : null}
                                        </div>
                                    </div>


                                </Card.Body>
                            </Card>
                        </div>
                    </Col>


                    <Col xs={12} lg={7}>
                        <Card className="border-0 shadow-sm rounded-3 h-100" style={{ minHeight: "78vh" }}>
                            <Card.Body className="p-2 p-xl-3 d-flex flex-column gap-3" style={scrollableStyle}>
                                <div className="clinical-meta p-3 bg-light border border-light-subtle rounded-2">
                                    <div className="row g-3">

                                        <Col xs={12} md={5} className="d-flex flex-column justify-content-center border-end-md">
                                            <div className="d-flex align-items-center gap-2 mb-2">
                                                <span className="badge bg-primary-subtle text-primary fw-bold text-uppercase" style={{ fontSize: "0.75rem" }}>
                                                    Thông tin lâm sàng
                                                </span>
                                                <span className="text-muted fw-bold small">BA #{medicalRecord?.id}</span>
                                            </div>

                                            <h5 className="patient-name fw-bold text-dark mb-1">
                                                {medicalRecord?.patientName || "—"}
                                            </h5>

                                            <div className="patient-meta text-secondary small">
                                                <span className="fw-medium">{medicalRecord?.gender || "N/A"}</span>
                                                &nbsp;&nbsp; -&nbsp;&nbsp;&nbsp;
                                                <span>{getAgeFromBirthYear(medicalRecord?.dob)} tuổi</span>
                                            </div>
                                        </Col>


                                        <Col xs={12} md={7}>
                                            <div className="d-flex flex-column gap-2">

                                                <div className="clinical-card p-2 bg-white border border-light-subtle rounded-2 small">
                                                    <div className="fw-bold text-secondary mb-1" style={{ fontSize: "0.8rem" }}>
                                                        Chẩn đoán bệnh lý
                                                    </div>
                                                    <div className="text-secondary ps-1" style={{ whiteSpace: 'pre-line', fontSize: "0.825rem" }}>
                                                        {medicalRecord?.diagnosis || "Chưa có kết luận chẩn đoán"}
                                                    </div>
                                                </div>
                                                <div className="clinical-card p-2 bg-white border border-light-subtle rounded-2 small">
                                                    <div className="fw-bold text-muted mb-1" style={{ fontSize: "0.8rem" }}>
                                                        Ghi chú lâm sàng
                                                    </div>
                                                    <div className="text-muted ps-1" style={{ whiteSpace: 'pre-line', fontSize: "0.825rem" }}>
                                                        {medicalRecord?.note || "Không có ghi chú bổ sung từ hồ sơ bệnh án."}
                                                    </div>
                                                </div>
                                            </div>
                                        </Col>
                                    </div>
                                </div>

                                <div>
                                    {prescriptionItems.length === 0 ? (
                                        <div className="p-3 border rounded-2 bg-white text-muted small text-center">
                                            Chưa có thuốc nào trong toa. Hãy tìm thuốc và thêm vào toa.
                                        </div>
                                    ) : (
                                        <div style={{ overflowX: "auto" }} className="rounded-1 border">
                                            <Table responsive borderless striped className="prescription-table align-middle table-hover table-sm mb-0  rounded-1">
                                                <thead className="table-light border-bottom text-secondary small">
                                                    <tr>
                                                        <th style={{ width: "3rem" }} className="text-center py-2">STT</th>
                                                        <th className="py-2">Tên thuốc</th>
                                                        <th style={{ width: "5rem" }} className="text-center py-2">SL</th>
                                                        <th style={{ width: "5rem" }} className="text-center py-2 px-0">Ngày dùng</th>
                                                        <th style={{ width: "180px" }} className="text-center py-2">Ghi chú</th>
                                                        <th style={{ width: "5rem" }} className="text-center py-2">Đơn vị</th>
                                                        <th style={{ width: "50px" }} className="py-2"></th>
                                                    </tr>
                                                </thead>
                                                <tbody>
                                                    {prescriptionItems.map((medicine, index) => (
                                                        <tr key={medicine.id}>
                                                            <td className="text-center text-muted small py-1">{index + 1}</td>
                                                            <td className="py-1">
                                                                <div className="small fw-semibold text-dark">{medicine.name}</div>
                                                            </td>
                                                            <td className="text-center py-1">
                                                                <Form.Control
                                                                    type="text"
                                                                    inputMode="numeric"
                                                                    pattern="[0-9]*"
                                                                    value={medicine.quantity}
                                                                    onChange={(e) => updatePrescriptionQuantity(medicine.id, e.target.value)}
                                                                    className="text-center qty-input p-1"
                                                                    aria-label={`Số lượng ${medicine.name}`}
                                                                />
                                                            </td>
                                                            <td className="text-center py-1">
                                                                <Form.Control
                                                                    type="text"
                                                                    inputMode="numeric"
                                                                    pattern="[0-9]*"
                                                                    value={medicine.daysToUse}
                                                                    onChange={(e) => updatePrescriptionDays(medicine.id, e.target.value)}
                                                                    className="text-center qty-input p-1"
                                                                    aria-label={`Ngày dùng ${medicine.name}`}
                                                                />
                                                            </td>
                                                            <td className="py-1">
                                                                <Form.Control
                                                                    as="textarea"
                                                                    rows={1}
                                                                    maxLength={200}
                                                                    value={medicine.note}
                                                                    onChange={(e) => updatePrescriptionNote(medicine.id, e.target.value)}
                                                                    placeholder="Ghi chú"
                                                                    className="note-input p-1"
                                                                />
                                                            </td>
                                                            <td className="text-center fw-semibold small py-1">{medicine.unit}</td>
                                                            <td className="py-1">
                                                                <Button variant="link" className="text-danger p-0" onClick={() => removePrescriptionItem(medicine.id)}>
                                                                    <XSquareFill size={17} />
                                                                </Button>
                                                            </td>
                                                        </tr>
                                                    ))}
                                                </tbody>
                                            </Table>
                                        </div>
                                    )}
                                </div>

                                <div className="mt-auto p-2 d-flex flex-wrap justify-content-between align-items-center gap-3" style={{ position: 'sticky', bottom: 0, zIndex: 5, background: '#fff', boxShadow: '0 -6px 18px rgba(16,24,40,0.03)' }}>
                                    <div className="text-muted small">
                                        Tổng mục: <strong className="text-dark">{prescriptionTotals.totalItems}</strong>
                                        &nbsp;·&nbsp;
                                        Tổng SL: <strong className="text-dark">{prescriptionTotals.totalQuantity}</strong>
                                    </div>
                                    <div className="">

                                        <ButtonGroup aria-label="prescription-actions " className="d-flex flex-wrap justify-content-end gap-2">
                                            <Button className="rounded-2 me-3" variant="outline-secondary" onClick={() => navigate(-1)} disabled={saving}>
                                                Hủy bỏ
                                            </Button>
                                            <Button className="rounded-2" variant="primary" onClick={() => handleSavePrescription(true)} disabled={saving}>
                                                {saving ? "Đang lưu..." : "Lưu nháp"}
                                            </Button>
                                            <Button variant="success" className="px-4 rounded-2" onClick={() => handleSavePrescription(false)} disabled={saving}>
                                                {saving ? "Đang lưu..." : "Lưu"}
                                            </Button>
                                        </ButtonGroup>
                                    </div>
                                </div>
                            </Card.Body>
                        </Card>
                    </Col>
                </Row>
            </Container>

            <Footer />
        </div >
    );
};

export default PrescribeMedicine;
