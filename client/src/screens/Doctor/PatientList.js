import { authApis, CLINIC_ENDPOINTS } from "../../configs/Apis";
import { useEffect, useRef, useState } from "react";
import { Container, Table } from "react-bootstrap";
import Footer from "../../components/Footer";
import Header from "../../components/Header";
import MySpinner from "../../components/MySpinner";

const PatientList = () => {
    const [patients, setPatients] = useState([]);
    const [loading, setLoading] = useState(true);
    const [filters, setFilters] = useState({ patientName: "", gender: "" });
    const searchTimeout = useRef(null);

    const filterStyle = {
        display: "flex",
        flexWrap: "wrap",
        alignItems: "center",
        gap: 12,
        marginBottom: 16,
        padding: 12,
        border: "1px solid #e5e7eb",
        borderRadius: 12,
        background: "#fff"
    };

    const inputStyle = {
        minWidth: 260,
        maxWidth: 360
    };

    useEffect(() => {
        const loadPatients = async () => {
            try {
                const response = await authApis().get(CLINIC_ENDPOINTS.DOCTOR_GET_PATIENTS);
                setPatients(Array.isArray(response.data) ? response.data : []);
            } catch (error) {
                setPatients([]);
                console.error("Lỗi khi tải danh sách bệnh nhân:", error);
            } finally {
                setLoading(false);
            }
        };

        loadPatients();

        return () => {
            if (searchTimeout.current) clearTimeout(searchTimeout.current);
        };
    }, []);

    const fetchPatients = (nextFilters) => {
        if (searchTimeout.current) clearTimeout(searchTimeout.current);

        searchTimeout.current = setTimeout(async () => {
            setLoading(true);
            try {
                const response = await authApis().get(CLINIC_ENDPOINTS.DOCTOR_GET_PATIENTS, {
                    params: nextFilters
                });
                setPatients(Array.isArray(response.data) ? response.data : []);
            } catch (error) {
                console.error("Lỗi khi tìm kiếm bệnh nhân:", error);
            } finally {
                setLoading(false);
            }
        }, 350);
    };

    const handleNameChange = (e) => {
        const patientName = e.target.value.trim().toLowerCase();
        const nextFilters = { ...filters, patientName };
        setFilters(nextFilters);
        fetchPatients(nextFilters);
    };

    const handleGenderChange = (e) => {
        const gender = e.target.value;
        const nextFilters = { ...filters, gender };
        setFilters(nextFilters);
        fetchPatients(nextFilters);
    };

    return (
        <>
            <Header />
            <Container className="my-4">
                <h2 className="text-center">DANH SÁCH BỆNH NHÂN</h2>

                <div className="mb-3 text-center">
                    <span className="text-muted">Tổng số bệnh nhân: </span>
                    <span className="text-primary fw-bold">{patients.length}</span>
                </div>

                <div style={filterStyle}>
                    <div style={{ fontWeight: 600, minWidth: 140 }}>Tìm kiếm bệnh nhân</div>
                    <input
                        type="text"
                        className="form-control form-control-sm"
                        placeholder="Tìm theo tên hoặc CCCD..."
                        onChange={handleNameChange}
                        value={filters.patientName}
                        style={inputStyle}
                    />
                    <select
                        onChange={handleGenderChange}
                        className="form-select form-select-sm"
                        value={filters.gender}
                        style={{ width: 160 }}
                    >
                        <option value="">Tất cả giới tính</option>
                        <option value="Nam">Nam</option>
                        <option value="Nữ">Nữ</option>
                        <option value="Khác">Khác</option>
                    </select>
                </div>

                <Table className="table-striped align-middle" bordered hover responsive>
                    <thead className="text-center">
                        <tr>
                            <th style={{ width: "25%" }}>Họ và tên</th>
                            <th style={{ width: "15%" }}>CCCD</th>
                            <th style={{ width: "15%" }}>SĐT</th>
                            <th style={{ width: "10%" }}>Năm sinh</th>
                            <th style={{ width: "10%" }}>Giới tính</th>
                            <th style={{ width: "25%" }}>Địa chỉ</th>
                        </tr>
                    </thead>
                    <tbody className="text-center">
                        {loading ? (
                            <tr>
                                <td colSpan="6" className="text-center">
                                    <MySpinner animation="border" size="sm" />
                                </td>
                            </tr>
                        ) : patients.length === 0 ? (
                            <tr>
                                <td colSpan="6" className="text-center">
                                    Không có bệnh nhân nào.
                                </td>
                            </tr>
                        ) : (
                            patients.map((patient) => (
                                <tr key={patient.id}>
                                    <td className="py-3">{patient.fullName}</td>
                                    <td className="py-3">{patient.cccd}</td>
                                    <td className="py-3">{patient.phone}</td>
                                    <td className="py-3">{patient.dob}</td>
                                    <td className="py-3">{patient.gender}</td>
                                    <td className="py-3">{patient.address}</td>
                                </tr>
                            ))
                        )}
                    </tbody>
                </Table>
            </Container>
            <Footer />
        </>
    );
};

export default PatientList;
