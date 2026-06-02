import { useEffect, useRef, useState, useMemo } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { Button, ButtonGroup, Badge, Card, Col, Container, Form, InputGroup, Row, Table, Alert } from "react-bootstrap";
import { ArrowLeft, Plus, PlusSquare, PlusSquareFill, XCircleFill, Save, CheckCircle, XSquareFill, TwitterX, XSquare, Receipt } from "react-bootstrap-icons";
import Footer from "../../components/Footer";
import Header from "../../components/Header";
import { authApis, CLINIC_ENDPOINTS, clinicApis } from "../../configs/Apis";
import MySpinner from "../../components/MySpinner";
import MyAlert from "../../components/MyAlert";

const MEDICINE_PAGE_SIZE = 15;
const DRAFT_STORAGE_KEY_PREFIX = "prescriptionDraft_";

const PrescribeMedicine = () => {
    const navigate = useNavigate();
    const { medicalRecordId } = useParams();

    const [loading, setLoading] = useState(false);
    const [searching, setSearching] = useState(false);
    const [loadingMoreMedicines, setLoadingMoreMedicines] = useState(false);
    const [saving, setSaving] = useState(false);
    const [savingDraft, setSavingDraft] = useState(false);
    const [medicalRecord, setMedicalRecord] = useState(null);
    const [appointment, setAppointment] = useState(null);
    const [searchTerm, setSearchTerm] = useState("");
    const [searchResults, setSearchResults] = useState([]);
    const [medicinePage, setMedicinePage] = useState(1);
    const [hasMoreMedicines, setHasMoreMedicines] = useState(true);
    const [prescriptionItems, setPrescriptionItems] = useState([]);
    const [prescriptionId, setPrescriptionId] = useState(null);
    const [prescriptionStatus, setPrescriptionStatus] = useState(null);
    const [isMedicalRecordLoaded, setIsMedicalRecordLoaded] = useState(false);
    const [isPrescriptionLoaded, setIsPrescriptionLoaded] = useState(false);
    const [isAppointmentLoaded, setIsAppointmentLoaded] = useState(false);

    const [statusMessage, setStatusMessage] = useState(null);
    const [hasDraftLoaded, setHasDraftLoaded] = useState(false);

    const medicineListContainerRef = useRef(null);
    const medicineLoadMoreRef = useRef(null);
    const savingDraftLockRef = useRef(false);
    const savingPublishLockRef = useRef(false);

    const getDraftKey = () => `${DRAFT_STORAGE_KEY_PREFIX}${medicalRecordId}`;

    const saveDraftToLocalStorage = (items) => {
        if (!medicalRecordId || items.length === 0) return;
        try {
            const draft = { items, savedAt: new Date().toISOString() };
            localStorage.setItem(getDraftKey(), JSON.stringify(draft));
        } catch (error) {
            console.error("Lỗi khi lưu draft vào localStorage:", error);
        }
    };

    const loadDraftFromLocalStorage = () => {
        if (!medicalRecordId) return null;
        try {
            const draft = localStorage.getItem(getDraftKey());
            return draft ? JSON.parse(draft) : null;
        } catch (error) {
            console.error("Lỗi khi tải draft từ localStorage:", error);
            return null;
        }
    };

    const clearDraftFromLocalStorage = () => {
        if (!medicalRecordId) return;
        try {
            localStorage.removeItem(getDraftKey());
        } catch (error) {
            console.error("Lỗi khi xoá draft từ localStorage:", error);
        }
    };

    const stickyStyle = { top: "75px" };
    const scrollableStyle = { paddingRight: "6px" };
    const smallInputStyle = { maxWidth: "76px", margin: "0 auto" };
    const noteInputStyle = { minWidth: "110px", resize: "none", fontSize: "0.9rem" };
    const searchRowStyle = { fontSize: "0.92rem", lineHeight: 1.08 };

    const prescriptionTotals = useMemo(() => {
        const totalQuantity = prescriptionItems.reduce((sum, it) => sum + (Number(it.quantity) || 0), 0);
        return { totalItems: prescriptionItems.length, totalQuantity };
    }, [prescriptionItems]);


    const renderStockStatus = (stock) => {
        if (!stock || stock <= 0) return <span className="text-danger ">Hết hàng</span>;
        if (stock > 99) return <span className="text-success ">99+</span>;
        if (stock <= 10) return <span className="text-danger ">{stock}</span>;
        return <span className="text-warning ">{stock}</span>;
    };

    const formatVnd = (value) => {
        const numericValue = Number(value);
        if (!Number.isFinite(numericValue)) {
            return "-";
        }

        return `${numericValue.toLocaleString("vi-VN")}đ`;
    };


    const getAgeFromBirthYear = (birthValue) => {
        if (!birthValue) {
            return "-";
        }

        const birthText = String(birthValue).trim();
        const yearMatch = birthText.match(/\b(19|20)\d{2}\b/);
        const birthYear = yearMatch ? Number.parseInt(yearMatch[0], 10) : Number.parseInt(birthText, 10);

        if (!Number.isFinite(birthYear)) {
            return "-";
        }

        const currentYear = new Date().getFullYear();
        const age = currentYear - birthYear;

        return age >= 0 ? `${age}` : "-";
    };

    const totalPrice = useMemo(() => {
        return prescriptionItems.reduce((sum, item) => {
            const price = Number(item.price) || 0;
            const qty = Number(item.quantity) || 0;
            return sum + price * qty;
        }, 0);
    }, [prescriptionItems]);

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
            setIsMedicalRecordLoaded(false);
            setIsPrescriptionLoaded(false);
            setIsAppointmentLoaded(false);

            if (!medicalRecordId) {
                setIsMedicalRecordLoaded(true);
                setIsPrescriptionLoaded(true);
                setIsAppointmentLoaded(true);
                return;
            }

            setLoading(true);
            try {
                const response = await authApis().get(CLINIC_ENDPOINTS.MEDICAL_RECORD_BY_ID(medicalRecordId));
                setMedicalRecord(response.data);
            } catch (error) {
                console.error("Lỗi khi tải thông tin bệnh án!:", error);
                setMedicalRecord(null);
                setIsPrescriptionLoaded(true);
                setIsAppointmentLoaded(true);
            } finally {
                setIsMedicalRecordLoaded(true);
                setLoading(false);
            }
        };

        loadMedicalRecord();
    }, [medicalRecordId]);



    useEffect(() => {
        if (!medicalRecord) {
            return;
        }

        const loadAppointment = async () => {
            if (!medicalRecord?.appointmentId) {
                setIsAppointmentLoaded(true);
                return;
            }

            setLoading(true);
            try {
                const response = await authApis().get(CLINIC_ENDPOINTS.APPOINTMENT_BY_ID(medicalRecord.appointmentId));
                setAppointment(response.data);
            } catch (error) {
                console.error("Lỗi khi tải thông tin phiếu hẹn!:", error);
            } finally {
                setIsAppointmentLoaded(true);
                setLoading(false);
            }
        };
        const loadPrescription = async () => {
            if (!medicalRecord?.id) {
                setPrescriptionItems([]);
                setPrescriptionId(null);
                setPrescriptionStatus(null);
                setIsPrescriptionLoaded(true);
                return;
            }

            setLoading(true);
            try {
                const response = await authApis().get('secure/prescriptions/medical-record/' + medicalRecord.id);
                setPrescriptionId(response.data?.id ?? null);
                setPrescriptionStatus(response.data?.status ?? null);
                const loadedItems = Array.isArray(response.data?.items) ? response.data.items : [];

                if (loadedItems.length > 0) {
                    setPrescriptionItems(
                        loadedItems.map((item) => ({
                            id: item.medicineId ?? item.id,
                            prescriptionItemId: item.id,
                            name: item.medicineName,
                            code: item.medicineCode,
                            image: item.medicineImage,
                            price: item.medicinePrice,
                            unit: item.unit || "Viên",
                            quantity: item.quantity ?? 1,
                            daysToUse: item.daysToUse ?? 7,
                            note: item.note ?? "",
                            source: "prescription",
                        }))
                    );
                    clearDraftFromLocalStorage();
                } else {

                    const draft = loadDraftFromLocalStorage();
                    if (draft?.items && draft.items.length > 0) {
                        setPrescriptionItems(draft.items);
                        setHasDraftLoaded(true);
                    } else {
                        setPrescriptionItems([]);
                    }
                }
            } catch (error) {
                console.error("Lỗi khi tải thông tin đơn thuốc!:", error);

                const draft = loadDraftFromLocalStorage();
                if (draft?.items && draft.items.length > 0) {
                    setPrescriptionItems(draft.items);
                    setHasDraftLoaded(true);
                } else {
                    setPrescriptionItems([]);
                }
                setPrescriptionId(null);
                setPrescriptionStatus(null);
            } finally {
                setIsPrescriptionLoaded(true);
                setLoading(false);
            }
        };

        setIsAppointmentLoaded(false);
        setIsPrescriptionLoaded(false);

        loadPrescription();
        loadAppointment();

    }, [medicalRecord]);


    useEffect(() => {
        const normalizedStatus = String(prescriptionStatus || "").toUpperCase();
        const isPublishedStatus = normalizedStatus === "PUBLIC";
        if (!isPublishedStatus && prescriptionItems.length > 0) {
            saveDraftToLocalStorage(prescriptionItems);
        }
    }, [prescriptionItems, prescriptionStatus]);

    useEffect(() => {
        const trimmedSearchTerm = searchTerm.trim();

        if (!trimmedSearchTerm) {
            setStatusMessage(null);
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

                loadMedicines({ kw: searchTerm, page: medicinePage + 1, replace: false });
            },
            {
                root: container,
                rootMargin: "0px 0px 100px 0px",
                threshold: 0.1,
            }
        );

        observer.observe(sentinel);

        return () => observer.disconnect();
    }, [searchTerm, searching, loadingMoreMedicines, hasMoreMedicines, medicinePage, searchResults.length]);

    useEffect(() => {
        const container = medicineListContainerRef.current;

        if (!container || searching || loadingMoreMedicines || !hasMoreMedicines) {
            return;
        }

        const isScrollable = container.scrollHeight > container.clientHeight + 24;

        if (isScrollable) {
            return;
        }

        const autoFillTimer = window.setTimeout(() => {
            if (!medicineListContainerRef.current || searching || loadingMoreMedicines || !hasMoreMedicines) {
                return;
            }

            loadMedicines({ kw: searchTerm, page: medicinePage + 1, replace: false });
        }, 0);

        return () => window.clearTimeout(autoFillTimer);
    }, [searchTerm, searchResults.length, searching, loadingMoreMedicines, hasMoreMedicines, medicinePage]);

    const handleClearSearch = (e) => {
        e.preventDefault();
        setSearchTerm("");
        setSearchResults([]);
        setStatusMessage(null);
    };

    const handleMedicineListScroll = () => {
        const container = medicineListContainerRef.current;

        if (!container || searching || loadingMoreMedicines || !hasMoreMedicines) {
            return;
        }

        const isNearBottom = container.scrollTop + container.clientHeight >= container.scrollHeight - 80;

        if (isNearBottom) {
            loadMedicines({ kw: searchTerm, page: medicinePage + 1, replace: false });
        }
    };

    const addMedicineToPrescription = (medicine) => {
        if (!medicine?.id) {
            return;
        }

        if (medicine.totalStock !== undefined && medicine.totalStock <= 0) {
            setStatusMessage({ header: "Thông báo", message: "Thuốc này hiện không còn tồn kho.", variant: "warning" });
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
                        price: item.price ?? medicine.price ?? 0,
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
                    price: medicine.price ?? 0,
                    totalStock: medicine.totalStock ?? 0,
                    quantity: 1,
                    daysToUse: 7,
                    note: "",
                },
            ];
        });

        setStatusMessage({ header: "Thành công", message: `Đã thêm ${medicine.name} vào toa thuốc.`, variant: "success" });
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

    const getAvailableStock = (medicineId, totalStock) => {
        const prescriptionItem = prescriptionItems.find(item => item.id === medicineId);
        if (!prescriptionItem) return totalStock;
        return Math.max(0, (totalStock || 0) - (prescriptionItem.quantity || 0));
    };

    const normalizedPrescriptionStatus = String(prescriptionStatus || "").toUpperCase();
    const isPublished = normalizedPrescriptionStatus === "PUBLIC";
    const isInitialDataLoading = !isMedicalRecordLoaded || !isPrescriptionLoaded || !isAppointmentLoaded;
    const prescriptionStatusLabel = normalizedPrescriptionStatus === "PUBLIC"
        ? "BẢN CHÍNH"
        : normalizedPrescriptionStatus === "DRAFT"
            ? "BẢN NHÁP"
            : normalizedPrescriptionStatus || "-";

    const handleSavePrescription = async () => {
        if (savingPublishLockRef.current) {
            return;
        }

        if (!medicalRecordId) {
            console.log("Không tìm thấy hồ sơ bệnh án.");
            return;
        }

        if (prescriptionItems.length === 0) {
            setStatusMessage({ header: "Cảnh báo", message: "Hãy thêm ít nhất một thuốc vào toa trước khi lưu.", variant: "warning" });
            return;
        }

        try {
            savingPublishLockRef.current = true;
            setSaving(true);
            setStatusMessage(null);

            const payload = {
                id: prescriptionId,
                medicalRecordId: Number(medicalRecordId),
                items: prescriptionItems.map((item) => ({
                    medicineId: item.id,
                    quantity: item.quantity,
                    daysToUse: item.daysToUse,
                    note: item.note,
                })),
            };

            const response = await authApis().post(CLINIC_ENDPOINTS.CREATE_PRESCRIPTIONS, payload);
            if (response?.data?.id) {
                setPrescriptionId(response.data.id);
                setPrescriptionStatus(response.data.status);
                clearDraftFromLocalStorage();
            }

            setStatusMessage({ header: "Thành công", message: "Đã lưu toa thuốc thành công.", variant: "success" });

        } catch (error) {
            console.error("Lưu toa thuốc thất bại:", error);
            const errorMessage = error.response?.data?.message || error.message || "Lưu toa thuốc thất bại. Vui lòng thử lại!";
            setStatusMessage({ header: "Lỗi", message: errorMessage, variant: "danger" });
        } finally {
            savingPublishLockRef.current = false;
            setSaving(false);
        }
    };

    const handleSaveDraftPrescription = async () => {
        if (savingDraftLockRef.current) {
            return;
        }

        if (!medicalRecordId) {
            setStatusMessage({ header: "Lỗi", message: "Không tìm thấy hồ sơ bệnh án.", variant: "danger" });
            return;
        }

        if (prescriptionItems.length === 0) {
            setStatusMessage({ header: "Cảnh báo", message: "Hãy thêm ít nhất một thuốc vào toa trước khi lưu.", variant: "warning" });
            return;
        }

        try {
            savingDraftLockRef.current = true;
            setSavingDraft(true);
            setStatusMessage(null);

            const payload = {
                id: prescriptionId,
                medicalRecordId: Number(medicalRecordId),
                items: prescriptionItems.map((item) => ({
                    medicineId: item.id,
                    quantity: item.quantity,
                    daysToUse: item.daysToUse,
                    note: item.note,
                })),
            };

            const response = await authApis().post(CLINIC_ENDPOINTS.SAVE_DRAFT_PRESCRIPTIONS, payload);
            if (response?.data) {
                setPrescriptionId(response.data.id);
                setPrescriptionStatus(response.data.status);
            }

            setStatusMessage({ header: "Thành công", message: "Đã lưu nháp toa thuốc thành công.", variant: "success" });


        } catch (error) {
            console.error("Lưu nháp toa thuốc thất bại:", error);
        } finally {
            savingDraftLockRef.current = false;
            setSavingDraft(false);
        }
    };

    const handleSearchMedicine = (value) => {
        setSearchTerm(value);
    };

    const handleShowAlert = (header, message, variant) => {
        setStatusMessage({ header, message, variant });
        window.scrollTo({ top: 0, behavior: "smooth" });
    };

    const isBrowsingMedicines = !searchTerm.trim();

    return (
        <div className="d-flex flex-column min-vh-100 bg-light">
            <Header />

            <Container className="py-3 flex-grow-1" style={{ maxWidth: isPublished ? "80%" : "96%" }}>
                {isInitialDataLoading ? (
                    <div className="d-flex align-items-center justify-content-center" style={{ minHeight: "72vh" }}>
                        <MySpinner />
                    </div>
                ) : (
                    <Row className="g-2">
                        {!isPublished ? (
                            <Col xs={12} lg={5} className="d-flex">
                                <div className="d-flex flex-column gap-3 w-100 h-100" >
                                    <Card className="border-0 shadow-sm rounded-3 h-100">

                                        <Card.Body className="p-2 d-flex flex-column" style={{ minHeight: 0 }}>
                                            <div className="bg-light p-2 border mb-1 rounded-2">
                                                <Row className="g-2 align-items-center">
                                                    <InputGroup size="md">
                                                        <Form.Control
                                                            placeholder="Nhập tên, mã thuốc..."
                                                            value={searchTerm}
                                                            onChange={(e) => handleSearchMedicine(e.target.value)}
                                                        />
                                                        <Button variant="primary" type="button" onClick={(e) => handleClearSearch(e)}>
                                                            x
                                                        </Button>
                                                    </InputGroup>
                                                </Row>
                                            </div>


                                            <div className="mb-2 d-flex flex-column" style={{ minHeight: 0, flex: 1 }}>

                                                <div
                                                    ref={medicineListContainerRef}
                                                    className="border rounded-2 bg-white thin-scrollbar "
                                                    style={{ maxHeight: "75vh", minHeight: "75vh", overflowY: "auto", flex: 1 }}
                                                    onScroll={handleMedicineListScroll}
                                                >
                                                    <Table borderless className="align-middle table-hover table-sm mb-0">
                                                        <thead className="table-light border-bottom text-secondary small sticky-top top-0">
                                                            <tr>
                                                                <th className="text-center" style={{ width: "50px", position: "sticky", top: 0, zIndex: 3, background: "#f8f9fa" }}>TT</th>
                                                                <th style={{ position: "sticky", top: 0, zIndex: 3, background: "#f8f9fa" }}>Tên thuốc</th>
                                                                <th className="text-center" style={{ width: "7rem", position: "sticky", top: 0, zIndex: 3, background: "#f8f9fa" }}>Giá</th>
                                                                <th className="text-center" style={{ width: "4.5rem", position: "sticky", top: 0, zIndex: 3, background: "#f8f9fa" }}>Kho</th>
                                                                <th className="text-center" style={{ width: "5.5rem", position: "sticky", top: 0, zIndex: 3, background: "#f8f9fa" }}>Đơn vị</th>
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
                                                                                <small className="text-muted">{medicine.code || "Không có mã"} </small>
                                                                            </td>
                                                                            <td className="text-center">
                                                                                <small className="text-success fw-semibold">{formatVnd(medicine.price)}</small>
                                                                            </td>

                                                                            <td className="text-center small py-1">
                                                                                {renderStockStatus(getAvailableStock(medicine.id, medicine.totalStock))}
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
                                                    <div className="mb-2 text-secondary small mt-2 text-center">
                                                        {isBrowsingMedicines ? (
                                                            <span>Đã hiển thị tất cả kết quả phù hợp</span>
                                                        ) : searching ? (
                                                            <span className="text-muted">Đang tìm kiếm kết quả phù hợp...</span>
                                                        ) : searchResults.length === 0 ? (
                                                            <span className="text-danger">Không tìm thấy thuốc theo từ khóa "{searchTerm}"</span>
                                                        ) : (
                                                            <span className="text-success">Tìm thấy {searchResults.length} thuốc phù hợp</span>
                                                        )}
                                                    </div>

                                                    {hasMoreMedicines ? (
                                                        <div ref={medicineLoadMoreRef} className="py-2 text-center text-muted small border-top">
                                                            {loadingMoreMedicines ? "Đang tải thêm thuốc..." : "Cuộn xuống để tải thêm"}
                                                        </div>
                                                    ) : null}

                                                </div>

                                            </div>


                                        </Card.Body>
                                    </Card>
                                </div>
                            </Col>
                        ) : null}


                        <Col xs={12} lg={isPublished ? 10 : 7} className={isPublished ? "mx-auto" : ""}>
                            {statusMessage && <Alert dismissible onClose={() => setStatusMessage(null)} className="mb-2 p-3" variant={statusMessage.variant}>
                                {statusMessage.message}
                            </Alert>}
                            <Card className="border-0 shadow-sm rounded-3 ">
                                <Card.Body className="p-2 p-xl-3 d-flex flex-column gap-3" style={scrollableStyle}>
                                    <div className="clinical-meta p-3 bg-light border border-light-subtle rounded-2">
                                        <div className="row g-3">

                                            <Col xs={12} md={5} className="d-flex flex-column justify-content-center border-end-md">


                                                <h5 className="patient-name fw-bold text-dark mb-1">
                                                    {medicalRecord?.patientName || "—"}
                                                </h5>

                                                <div className="patient-meta text-secondary small">
                                                    <span className="fw-medium">{medicalRecord?.gender || "-"}</span>
                                                    &nbsp;&nbsp; -&nbsp;&nbsp;&nbsp;
                                                    <span>{getAgeFromBirthYear(medicalRecord?.dob)} tuổi</span>

                                                </div>
                                                <span className=" ">Hồ sơ bệnh án: {medicalRecord?.id}</span>
                                                <span className=" ">Mã đơn thuốc: {medicalRecord?.id}</span>

                                                <Badge bg={isPublished ? "success" : "secondary"} className="mt-2 p-3 w-50" style={{ fontSize: "0.9rem" }}>
                                                    {prescriptionStatusLabel}
                                                </Badge>
                                            </Col>


                                            <Col xs={12} md={7}>
                                                <div className="d-flex flex-column gap-2">
                                                    <h5 className="fw-bold text-dark mb-1">
                                                        Bác sĩ: {appointment?.doctorFullName || "—"}
                                                    </h5>

                                                    <div className="clinical-card p-2 bg-white border border-light-subtle rounded-2 small">
                                                        <div className="fw-bold text-secondary mb-1" style={{ fontSize: "0.8rem" }}>
                                                            Chuẩn đoán bệnh lý
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
                                        {loading ? (
                                            <div className="text-center py-5">
                                                <MySpinner />
                                            </div>
                                        ) : prescriptionItems.length === 0 ? (
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
                                                            {!isPublished ? <th style={{ width: "50px" }} className="py-2"></th> : null}
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
                                                                    {isPublished ? (
                                                                        <span className="fw-semibold">{medicine.quantity}</span>
                                                                    ) : (
                                                                        <Form.Control
                                                                            type="text"
                                                                            inputMode="numeric"
                                                                            pattern="[0-9]*"
                                                                            value={medicine.quantity}
                                                                            onChange={(e) => updatePrescriptionQuantity(medicine.id, e.target.value)}
                                                                            className="text-center qty-input p-1"
                                                                            aria-label={`Số lượng ${medicine.name}`}
                                                                        />
                                                                    )}
                                                                </td>
                                                                <td className="text-center py-1">
                                                                    {isPublished ? (
                                                                        <span className="fw-semibold">{medicine.daysToUse}</span>
                                                                    ) : (
                                                                        <Form.Control
                                                                            type="text"
                                                                            inputMode="numeric"
                                                                            pattern="[0-9]*"
                                                                            value={medicine.daysToUse}
                                                                            onChange={(e) => updatePrescriptionDays(medicine.id, e.target.value)}
                                                                            className="text-center qty-input p-1"
                                                                            aria-label={`Ngày dùng ${medicine.name}`}
                                                                        />
                                                                    )}
                                                                </td>
                                                                <td className="py-1   text-center">
                                                                    {isPublished ? (
                                                                        <span className="text-muted">{medicine.note || "-"}</span>
                                                                    ) : (
                                                                        <Form.Control
                                                                            as="textarea"
                                                                            rows={1}
                                                                            maxLength={200}
                                                                            value={medicine.note}
                                                                            onChange={(e) => updatePrescriptionNote(medicine.id, e.target.value)}
                                                                            placeholder="Ghi chú"
                                                                            className=" note-input p-1"
                                                                        />
                                                                    )}
                                                                </td>
                                                                <td className="text-center fw-semibold small py-1">{medicine.unit}</td>
                                                                {!isPublished ? (
                                                                    <td className="py-1">
                                                                        <Button variant="link" className="text-danger p-0" onClick={() => removePrescriptionItem(medicine.id)}>
                                                                            <XSquareFill size={17} />
                                                                        </Button>
                                                                    </td>
                                                                ) : null}
                                                            </tr>
                                                        ))}
                                                    </tbody>
                                                </Table>
                                            </div>
                                        )}
                                    </div>

                                    <div className="mt-auto p-2 d-flex flex-wrap justify-content-between align-items-center gap-3" style={{ position: 'sticky', bottom: 0, zIndex: 5, background: '#fff', boxShadow: '0 -6px 18px rgba(16,24,40,0.03)' }}>
                                        <div className="text-muted small fw-semibold">
                                            Tổng mục: <span className="text-dark">{prescriptionTotals.totalItems}</span>
                                            &nbsp;&nbsp;
                                            Tổng SL: <span className="text-dark">{prescriptionTotals.totalQuantity}</span>

                                        </div>
                                        <div className="text-success fw-bold ">
                                            Tạm tính: {Number(totalPrice).toLocaleString('vi-VN')}đ
                                        </div>
                                        <div className="">

                                            <ButtonGroup aria-label="prescription-actions " className="d-flex flex-wrap justify-content-end gap-2">
                                                <Button className="rounded-2 me-3" variant="outline-secondary" onClick={() => navigate(-1)} disabled={savingDraft || saving}>
                                                    Quay lại
                                                </Button>
                                                {!isPublished && hasDraftLoaded ? (
                                                    <Button className="rounded-2" variant="outline-danger" onClick={() => { clearDraftFromLocalStorage(); setHasDraftLoaded(false); setStatusMessage({ header: "Thông báo", message: "Đã xoá bản nháp tạm.", variant: "info" }); }} disabled={savingDraft || saving}>
                                                        Xoá nháp
                                                    </Button>
                                                ) : null}
                                                {!isPublished ? (
                                                    <Button className="rounded-2" variant="primary" onClick={() => handleSaveDraftPrescription()} disabled={savingDraft}>
                                                        {savingDraft ? <MySpinner size="1" /> : "Lưu nháp"}
                                                    </Button>
                                                ) : null}
                                                {!isPublished ? (
                                                    <Button variant="success" className="px-4 rounded-2" onClick={() => handleSavePrescription()} disabled={saving}>
                                                        {saving ? <MySpinner size="1" /> : "Lưu"}
                                                    </Button>
                                                ) : null}
                                            </ButtonGroup>
                                        </div>
                                    </div>
                                </Card.Body>
                            </Card>
                        </Col>
                    </Row>
                )}
            </Container>

            <Footer />
        </div >
    );
};

export default PrescribeMedicine;
