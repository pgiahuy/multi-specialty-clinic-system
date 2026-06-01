import { useEffect, useRef, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { authApis, endpoint, TEST_ENDPOINTS } from "../../configs/Apis";
import Header from "../../components/Header";
import Footer from "../../components/Footer";
import MySpinner from "../../components/MySpinner";
import FloatAlert from "../../components/FloatAlert";
import { Button, Card, Container, Table, Form, Pagination } from "react-bootstrap";
import { CheckCircle, XCircle } from "react-bootstrap-icons";
import { tableStyles, emptyState } from "./DoctorStyle";

const AssignTest = () => {
    const { appointmentId } = useParams();
    const [loading, setLoading] = useState(false);
    const [allLabTests, setAllLabTests] = useState([]);
    const [labTests, setLabTests] = useState([]);
    const [selectedTests, setSelectedTests] = useState(new Set());
    const [isSaving, setIsSaving] = useState(false);
    const [currentPage, setCurrentPage] = useState(1);
    const [pageSize] = useState(10);
    const [totalTests, setTotalTests] = useState(0);
    const [searchTerm, setSearchTerm] = useState('');
    const [searchInput, setSearchInput] = useState('');
    const [appointment, setAppointment] = useState(null);
    const [alertData, setAlertData] = useState({ show: false, heading: '', message: '', variant: '' });
    const alertTimerRef = useRef(null);
    const navTimerRef = useRef(null);
    const navigate = useNavigate();

    const loadAppointment = async () => {
        try {
            const response = await authApis().get(endpoint['appointment'](appointmentId));
            setAppointment(response.data);
        } catch (error) {
            console.error("Tải thông tin cuộc hẹn thất bại:", error);
        }
    };

    const loadLabTests = async () => {
        try {
            setLoading(true);
            const params = {
                page: 1,
                pageSize: 10000
            };

            const response = await authApis().get(endpoint['lab-test'], { params });
            const tests = response.data.tests || response.data;
            setAllLabTests(tests);
            setSearchTerm('');
            setCurrentPage(1);
            setTotalTests(tests.length);
        } catch (error) {
            console.error("Tải danh sách xét nghiệm thất bại:", error);
        } finally {
            setLoading(false);
        }
    };

    const handleSearch = () => {
        setSearchTerm(searchInput);
        setCurrentPage(1);
    };

    const applySearchAndPagination = () => {
        const normalizedSearch = searchTerm.trim().toLowerCase();
        const filtered = allLabTests.filter((test) =>
            !normalizedSearch || test.testName?.toLowerCase().includes(normalizedSearch)
        );

        setTotalTests(filtered.length);

        const start = (currentPage - 1) * pageSize;
        setLabTests(filtered.slice(start, start + pageSize));
    };

    const handleSearchInputChange = (e) => {
        setSearchInput(e.target.value);
    };

    const handleSearchKeyPress = (e) => {
        if (e.key === 'Enter') {
            handleSearch();
        }
    };

    useEffect(() => {
        loadLabTests();
    }, []);

    useEffect(() => {
        if (appointmentId) {
            loadAppointment();
        }
    }, [appointmentId]);

    useEffect(() => {
        applySearchAndPagination();
    }, [allLabTests, currentPage, searchTerm]);

    useEffect(() => {
        return () => {
            if (alertTimerRef.current) {
                clearTimeout(alertTimerRef.current);
            }
            if (navTimerRef.current) {
                clearTimeout(navTimerRef.current);
            }
        };
    }, []);

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

        alertTimerRef.current = setTimeout(() => {
            setAlertData(prev => ({ ...prev, show: false }));
        }, 2000);
    };

    const handleCheckboxChange = (testId) => {
        const newSelected = new Set(selectedTests);
        if (newSelected.has(testId)) {
            newSelected.delete(testId);
        } else {
            newSelected.add(testId);
        }
        setSelectedTests(newSelected);
    };

    const handleAssignTests = async () => {
        if (selectedTests.size === 0) {
            handleShowAlert('Lỗi', 'Vui lòng chọn ít nhất một xét nghiệm.', 'danger');
            return;
        }

        try {
            setIsSaving(true);

            const payload = {
                appointmentId: parseInt(appointmentId),
                details: Array.from(selectedTests).map(testId => ({
                    testId: testId
                }))
            };

            const response = await authApis().post(TEST_ENDPOINTS.LAB_RESULTS, payload);
            if (response.status === 200 || response.status === 201) {
                handleShowAlert('Thành công', 'Chỉ định xét nghiệm thành công!', 'success');
                navTimerRef.current = setTimeout(() => {
                    navigate(-1);
                }, 1800);
            } else {
                handleShowAlert('Lỗi', 'Chỉ định xét nghiệm thất bại. Vui lòng thử lại!', 'danger');
            }

        } catch (error) {
            handleShowAlert('Lỗi', 'Chỉ định xét nghiệm thất bại. Vui lòng thử lại!', 'danger');
        } finally {
            setIsSaving(false);
        }
    };

    const formatCurrency = (value) => {
        return new Intl.NumberFormat('vi-VN', {
            style: 'currency',
            currency: 'VND'
        }).format(value);
    };

    const totalPages = Math.max(1, Math.ceil(totalTests / pageSize));

    return (
        <div className="d-flex flex-column min-vh-100 bg-light">
            <Header />
            <FloatAlert {...alertData} />

            <Container className="py-4 flex-grow-1" style={{ width: '70%' }}>
                <div className="text-center mb-4">
                    <h4 className="fw-bold text-primary mb-0">
                        Chỉ định xét nghiệm
                    </h4>
                    <p className="text-muted small mt-2">
                        Bệnh nhân {appointment ? `- ${appointment.patientFullName}` : ''}
                    </p>
                </div>

                <div className="mb-4 d-flex gap-2">
                    <div className="flex-grow-1">
                        <input
                            type="text"
                            className="form-control rounded-3"
                            placeholder="Tìm kiếm xét nghiệm..."
                            value={searchInput}
                            onChange={handleSearchInputChange}
                            onKeyPress={handleSearchKeyPress}
                        />
                    </div>
                    <Button
                        variant="primary"
                        className="rounded-3 px-4"
                        onClick={handleSearch}
                        disabled={loading}
                    >
                        Tìm kiếm
                    </Button>
                </div>

                {loading ? (
                    <div className="text-center py-5">
                        <MySpinner />
                    </div>
                ) : labTests.length === 0 ? (
                    <div style={emptyState.container}>
                        <h5 style={emptyState.title}>Không có xét nghiệm nào</h5>
                    </div>
                ) : (
                    <Card className="border-0 shadow-sm rounded-3 overflow-hidden">
                        <Card.Body className="p-0">
                            <div style={tableStyles.container}>
                                <Table hover responsive style={tableStyles.table}>
                                    <thead>
                                        <tr style={tableStyles.headerRow}>
                                            <th style={{ ...tableStyles.headerCell, textAlign: 'center', width: '8%' }}>Mã</th>
                                            <th style={tableStyles.headerCell}>Tên xét nghiệm</th>
                                            <th style={{ ...tableStyles.headerCell, textAlign: 'center', width: '15%' }}>Đơn vị</th>
                                            <th style={{ ...tableStyles.headerCell, textAlign: 'center', width: '15%' }}>Tham chiếu</th>
                                            <th style={{ ...tableStyles.headerCell, textAlign: 'center', width: '15%' }}>Giá</th>
                                            <th style={{ ...tableStyles.headerCell, textAlign: 'center', width: '10%' }}>Chọn</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        {labTests.map((test, index) => (
                                            <tr
                                                key={test.id}
                                                style={tableStyles.bodyRow(index)}
                                                onMouseEnter={(e) => e.currentTarget.style.backgroundColor = '#e7f1ff'}
                                                onMouseLeave={(e) => e.currentTarget.style.backgroundColor = tableStyles.bodyRow(index).backgroundColor}
                                            >
                                                <td style={tableStyles.dataCell}>{test.id}</td>
                                                <td style={tableStyles.dataCellLeft}>{test.testName}</td>
                                                <td style={tableStyles.dataCell}>{test.unit}</td>
                                                <td style={tableStyles.dataCell}>{test.normalRange}</td>
                                                <td style={tableStyles.dataCell}>
                                                    {formatCurrency(test.price)}
                                                </td>
                                                <td style={{ ...tableStyles.dataCell, textAlign: 'center' }}>
                                                    <Form.Check
                                                        type="checkbox"
                                                        id={`test-${test.id}`}
                                                        checked={selectedTests.has(test.id)}
                                                        onChange={() => handleCheckboxChange(test.id)}
                                                        style={{ cursor: 'pointer' }}
                                                    />
                                                </td>
                                            </tr>
                                        ))}
                                    </tbody>
                                </Table>
                            </div>
                        </Card.Body>
                    </Card>
                )}
                <div className="d-flex justify-content-center gap-2 mt-3">
                    <Pagination>
                        <Pagination.First
                            onClick={() => setCurrentPage(1)}
                            disabled={currentPage === 1 || loading}
                        />
                        <Pagination.Prev
                            onClick={() => setCurrentPage(Math.max(1, currentPage - 1))}
                            disabled={currentPage === 1 || loading}
                        />

                        {Array.from({ length: totalPages }, (_, i) => i + 1)
                            .filter(page => {
                                return (
                                    page === 1 ||
                                    page === totalPages ||
                                    (page >= currentPage - 1 && page <= currentPage + 1)
                                );
                            })
                            .map((page, index, arr) => (
                                <>
                                    {index > 0 && arr[index - 1] !== page - 1 && (
                                        <Pagination.Ellipsis disabled />
                                    )}
                                    <Pagination.Item
                                        active={currentPage === page}
                                        onClick={() => setCurrentPage(page)}
                                        disabled={loading}
                                    >
                                        {page}
                                    </Pagination.Item>
                                </>
                            ))}

                        <Pagination.Next
                            onClick={() => setCurrentPage(Math.min(totalPages, currentPage + 1))}
                            disabled={currentPage >= totalPages || loading}
                        />
                        <Pagination.Last
                            onClick={() => setCurrentPage(totalPages)}
                            disabled={currentPage >= totalPages || loading}
                        />
                    </Pagination>
                </div>

                <div className="mt-4">


                    <div className="text-center mb-4">

                        <Button
                            variant="outline-danger"

                            className="rounded-4 px-3 me-3"
                            onClick={() => setSelectedTests(new Set())}
                            disabled={selectedTests.size === 0}
                        >
                            Xóa chọn
                        </Button>
                        <Button
                            variant="primary"

                            className="rounded-4 px-3"
                            onClick={handleAssignTests}
                            disabled={isSaving || selectedTests.size === 0}
                        >
                            {isSaving ? 'Đang chỉ định...' : 'Xác nhận'}
                        </Button>
                    </div>


                </div>
            </Container>

            <Footer />
        </div>
    );
};

export default AssignTest;