import { useEffect, useState, useCallback, useRef } from "react";
import { useNavigate } from "react-router-dom";
import { Button, Col, Container, Row, Table, Form, Badge } from "react-bootstrap";
import Header from "../../components/Header";
import Footer from "../../components/Footer";
import { authApis } from "../../configs/Apis";
import MySpinner from "../../components/MySpinner";
import moment from "moment";

const ReceptionPrescriptions = () => {
    const [prescriptions, setPrescriptions] = useState([]);
    const [loading, setLoading] = useState(false);
    const [filterKw, setFilterKw] = useState("");
    const [filterFromDate, setFilterFromDate] = useState("");
    const [filterToDate, setFilterToDate] = useState("");
    const [searchTerm, setSearchTerm] = useState("");

    const timeRef = useRef(null);
    const nav = useNavigate();

    const loadPrescriptions = useCallback(async () => {
        try {
            setLoading(true);
            const response = await authApis().get("secure/prescriptions");
            let allPrescriptions = Array.isArray(response.data) ? response.data : [];

            // Filter by search term (patient name or doctor name)
            if (searchTerm) {
                const term = searchTerm.toLowerCase();
                allPrescriptions = allPrescriptions.filter(
                    (p) =>
                        (p.patientName && p.patientName.toLowerCase().includes(term)) ||
                        (p.doctorName && p.doctorName.toLowerCase().includes(term))
                );
            }

            // Filter by date range
            if (filterFromDate || filterToDate) {
                allPrescriptions = allPrescriptions.filter((p) => {
                    if (!p.publicAt) return false;
                    const prescriptionDate = moment(p.publicAt).format("YYYY-MM-DD");
                    if (filterFromDate && prescriptionDate < filterFromDate) return false;
                    if (filterToDate && prescriptionDate > filterToDate) return false;
                    return true;
                });
            }

            setPrescriptions(allPrescriptions);
        } catch (error) {
            console.error("Lỗi khi tải danh sách đơn thuốc:", error);
        } finally {
            setLoading(false);
        }
    }, [searchTerm, filterFromDate, filterToDate]);

    useEffect(() => {
        if (timeRef.current) clearTimeout(timeRef.current);

        timeRef.current = setTimeout(() => {
            setSearchTerm(filterKw);
        }, 500);

        return () => {
            if (timeRef.current) clearTimeout(timeRef.current);
        };
    }, [filterKw]);

    useEffect(() => {
        loadPrescriptions();
    }, [loadPrescriptions]);

    return (
        <div className="d-flex flex-column min-vh-100">
            <Header />
            <Container fluid className="py-4" style={{ width: "97%" }}>
                <div className="d-flex justify-content-between align-items-center mb-4">
                    <h3 className="mb-0 text-center flex-grow-1">Quầy Tiếp Nhận - DANH SÁCH ĐƠN THUỐC</h3>
                    <div className="d-flex gap-2">
                        <Button variant="outline-primary" size="sm" onClick={() => nav("/reception")}>
                            Xem lịch hẹn
                        </Button>
                        <Button variant="outline-primary" size="sm" onClick={() => nav("/reception/invoices")}>
                            Xem hóa đơn
                        </Button>
                    </div>
                </div>

                <Row className="mb-4 g-3 bg-light p-3 rounded shadow-sm">
                    <Col md={4} sm={12}>
                        <Form.Group controlId="filterKw">
                            <Form.Label className="fw-semibold small text-secondary">
                                Tìm theo tên bệnh nhân hoặc bác sĩ
                            </Form.Label>
                            <Form.Control
                                type="text"
                                placeholder="Nhập tên cần tìm..."
                                value={filterKw}
                                onChange={(e) => setFilterKw(e.target.value)}
                            />
                        </Form.Group>
                    </Col>

                    <Col md={3} sm={6}>
                        <Form.Group controlId="filterFromDate">
                            <Form.Label className="fw-semibold small text-secondary">
                                Từ ngày
                            </Form.Label>
                            <Form.Control
                                type="date"
                                value={filterFromDate}
                                onChange={(e) => setFilterFromDate(e.target.value)}
                            />
                        </Form.Group>
                    </Col>

                    <Col md={3} sm={6}>
                        <Form.Group controlId="filterToDate">
                            <Form.Label className="fw-semibold small text-secondary">
                                Đến ngày
                            </Form.Label>
                            <Form.Control
                                type="date"
                                value={filterToDate}
                                onChange={(e) => setFilterToDate(e.target.value)}
                            />
                        </Form.Group>
                    </Col>

                    <Col md={2} sm={12} className="d-flex align-items-end">
                        <Button
                            variant="outline-secondary"
                            className="w-100"
                            onClick={() => {
                                setFilterKw("");
                                setFilterFromDate("");
                                setFilterToDate("");
                            }}
                            disabled={!filterKw && !filterFromDate && !filterToDate}
                        >
                            Xóa bộ lọc
                        </Button>
                    </Col>
                </Row>

                <div className="table-responsive w-100">
                    <Table hover className="table">
                        <thead>
                            <tr>
                                <th>STT</th>
                                <th>Số đơn</th>
                                <th>Bệnh nhân</th>
                                <th>Bác sĩ</th>
                                <th>Ngày kê đơn</th>
                                <th>Trạng thái</th>
                                <th></th>
                            </tr>
                        </thead>
                        <tbody>
                            {loading ? (
                                <tr>
                                    <td colSpan={7} className="text-center py-4">
                                        <MySpinner />
                                    </td>
                                </tr>
                            ) : prescriptions.length === 0 ? (
                                <tr>
                                    <td colSpan={7} className="text-center text-muted py-5">
                                        Không có đơn thuốc nào.
                                    </td>
                                </tr>
                            ) : (
                                prescriptions.map((prescription, idx) => (
                                    <tr key={prescription.id} className="align-middle">
                                        <td>{idx + 1}</td>
                                        <td className="fw-bold">#{prescription.id}</td>
                                        <td>{prescription.patientName || "---"}</td>
                                        <td>{prescription.doctorName || "---"}</td>
                                        <td>
                                            {prescription.publicAt
                                                ? moment(prescription.publicAt).format("DD/MM/YYYY")
                                                : "---"}
                                        </td>
                                        <td>
                                            <Badge bg={prescription.status === "PUBLIC" ? "success" : "secondary"}>
                                                {prescription.status === "PUBLIC" ? "Đã kê đơn" : "Chưa kê"}
                                            </Badge>
                                        </td>
                                        <td>
                                            <Button
                                                variant="info"
                                                size="sm"
                                                onClick={() => nav(`/reception/prescriptions/${prescription.id}`)}
                                            >
                                                Xem chi tiết
                                            </Button>
                                        </td>
                                    </tr>
                                ))
                            )}
                        </tbody>
                    </Table>
                </div>
            </Container>
            <Footer />
        </div>
    );
};

export default ReceptionPrescriptions;
