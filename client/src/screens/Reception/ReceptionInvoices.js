import { useEffect, useState, useCallback, useRef } from "react";
import { useNavigate } from "react-router-dom";
import { Button, Col, Container, Row, Table, Form, Badge } from "react-bootstrap";
import Header from "../../components/Header";
import Footer from "../../components/Footer";
import { authApis, PAYMENT_ENDPOINTS } from "../../configs/Apis";
import MySpinner from "../../components/MySpinner";
import ReceptionNavBar from "./StaffNavBar";

const ReceptionInvoices = () => {
    const [invoices, setInvoices] = useState([]);
    const [loading, setLoading] = useState(false);
    const [filterKw, setFilterKw] = useState("");
    const [filterFromDate, setFilterFromDate] = useState("");
    const [filterToDate, setFilterToDate] = useState("");
    const [filterStatus, setFilterStatus] = useState("PENDING");
    const [searchTerm, setSearchTerm] = useState("");
    const [activeTab, setActiveTab] = useState("unpaid");

    const timeRef = useRef(null);
    const nav = useNavigate();

    const getStatusLabel = (status) => {
        const statusMap = {
            PENDING: "Chưa thanh toán",
            SUCCESS: "Đã thanh toán",
            FAILURE: "Thanh toán thất bại",
        };
        return statusMap[status] || status;
    };

    const getStatusBadge = (status) => {
        switch (status) {
            case "SUCCESS":
                return "success";
            case "PENDING":
                return "warning";
            case "FAILURE":
                return "danger";
            default:
                return "secondary";
        }
    };



    const loadInvoices = useCallback(async () => {
        try {
            setLoading(true);
            const params = {};
            if (searchTerm) params.kw = searchTerm;
            if (filterFromDate) params.startDate = filterFromDate;
            if (filterToDate) params.endDate = filterToDate;

            const response = await authApis().get(PAYMENT_ENDPOINTS.HISTORY, { params });
            const allInvoices = response.data || [];

            const filtered = allInvoices.filter((inv) => {
                if (activeTab === "paid") {
                    return inv.status === "SUCCESS";
                } else {
                    return inv.status === "PENDING";
                }
            });

            setInvoices(filtered);
        } catch (error) {
            console.error("Lỗi khi tải danh sách hóa đơn:", error);
        } finally {
            setLoading(false);
        }
    }, [searchTerm, filterFromDate, filterToDate, activeTab]);

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
        loadInvoices();
    }, [loadInvoices]);

    return (
        <div className="d-flex flex-column min-vh-100">
            <Header />
            <Container fluid className="py-4" style={{ width: "97%" }}>
                <ReceptionNavBar />

                <Row className="mb-4 g-3 bg-light p-3 rounded shadow-sm">
                    <Col md={4} sm={12}>
                        <Form.Group controlId="filterKw">
                            <Form.Label className="fw-semibold small text-secondary">
                                Tìm theo tên bệnh nhân
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

                <div className="mb-4 d-flex justify-content-start">
                    <div className="btn-group" role="group">
                        <Button
                            variant={activeTab === "unpaid" ? "primary" : "outline-primary"}
                            onClick={() => setActiveTab("unpaid")}
                            className="rounded-start"
                        >
                            Chưa thanh toán ({invoices.filter(inv => inv.status === "PENDING").length})
                        </Button>
                        <Button
                            variant={activeTab === "paid" ? "primary" : "outline-primary"}
                            onClick={() => setActiveTab("paid")}
                            className="rounded-end"
                        >
                            Đã thanh toán
                        </Button>
                    </div>
                </div>

                <div className="table-responsive w-100">
                    <Table hover className="table">
                        <thead>
                            <tr>
                                <th>STT</th>
                                <th>Bệnh nhân</th>
                                <th>Số tiền</th>
                                <th>Ngày tạo</th>
                                <th>Trạng thái</th>
                                <th>Phương thức</th>
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
                            ) : invoices.length === 0 ? (
                                <tr>
                                    <td colSpan={7} className="text-center text-muted py-5">
                                        Không có hóa đơn nào.
                                    </td>
                                </tr>
                            ) : (
                                invoices.map((inv, idx) => (
                                    <tr key={inv.id} className="align-middle">
                                        <td>{idx + 1}</td>
                                        <td>{inv.patientName || "---"}</td>
                                        <td className="fw-bold text-danger">
                                            {(inv.totalAmount || 0).toLocaleString("vi-VN")} đ
                                        </td>
                                        <td>{inv.createdAt || "---"}</td>
                                        <td>
                                            <Badge bg={getStatusBadge(inv.status)}>
                                                {getStatusLabel(inv.status)}
                                            </Badge>
                                        </td>
                                        <td>{inv.method || "---"}</td>
                                        <td>
                                            {inv.status === "PENDING" ? (
                                                <Button
                                                    variant="success"
                                                    size="sm"
                                                    onClick={() => nav(`/reception/payments/${inv.id}`)}
                                                >
                                                    Nhận tiền
                                                </Button>
                                            ) : (
                                                <span className="text-muted small">-</span>
                                            )}
                                        </td>
                                    </tr>
                                ))
                            )}
                        </tbody>
                    </Table>
                </div>
            </Container>
        </div>
    );
};

export default ReceptionInvoices;
