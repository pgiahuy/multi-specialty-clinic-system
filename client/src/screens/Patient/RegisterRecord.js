import { Alert, Button, Card, Container, Form } from "react-bootstrap";
import MySpinner from "../../components/MySpinner";
import Header from "../../components/Header";
import { formCardStyle } from "../User/UserStyle";
import { useEffect, useState } from "react";
import { authApis, endpoint, USER_ENDPOINTS } from "../../configs/Apis";
import { useNavigate } from "react-router-dom";
import Footer from "../../components/Footer";



const RegisterRecord = () => {
    const patientInfo = [{
        field: "cccd",
        title: "CCCD/CMND",
        type: "text",
    }, {
        field: "fullName",
        title: "Họ và tên",
        type: "text",
    }, {
        field: "gender",
        title: "Giới tính",
        type: "select",
        options: ["Nam", "Nữ", "Khác"],
    }, {
        field: "dob",
        title: "Ngày sinh",
        type: "date",
    }, {
        field: "phone",
        title: "Số điện thoại",
        type: "tel",
    }, {
        field: "address",
        title: "Địa chỉ",
        type: "text",
    }, {
        field: "relationship",
        title: "Mối quan hệ với chủ tài khoản",
        type: "select",
        options: [
            { value: "SELF", label: "Tôi" },
            { value: "PARENT", label: "Ba/Mẹ" },
            { value: "GRANDPARENT", label: "Ông/Bà" },
            { value: "SIBLING", label: "Anh/Chị/Em" },
            { value: "CHILD", label: "Con" },
            { value: "SPOUSE", label: "Vợ/Chồng" },
            { value: "OTHER", label: "Khác" }
        ]
    }];

    const [patient, setPatient] = useState({});
    const [err, setErr] = useState();
    const [loading, setLoading] = useState(false);
    const nav = useNavigate();



    const handleShowErr = (message) => {
        setErr(message);
        window.scrollTo({ top: 0, behavior: 'smooth' });

    }


    const validate = () => {
        for (let u of patientInfo)
            if (!patient[u.field] || patient[u.field].toString().trim() === "") {
                handleShowErr(`Vui lòng nhập ${u.title}!`);
                return false;
            }
        const cccdRegex = /^\d{12}$/;
        if (!cccdRegex.test(patient.cccd)) {
            handleShowErr("CCCD phải có 12 số!");
            return false;
        }
        const phoneRegex = /(84|0[3|5|7|8|9])+([0-9]{8})\b/;
        if (!phoneRegex.test(patient.phone)) {
            handleShowErr("Số điện thoại không hợp lệ!");
            return false;

            const selectedDate = new Date(patient.dob);
            const today = new Date();
            today.setHours(0, 0, 0, 0);

            if (selectedDate > today) {
                handleShowErr("Ngày sinh không thể lớn hơn ngày hiện tại!");
                return false;
            }
        }
        handleShowErr(null);
        return true;
    };

    const addRecord = async (e) => {
        e.preventDefault();

        if (validate()) {
            let form = new FormData();
            for (let key of Object.keys(patient)) {
                if (key !== 'dob') {
                    form.append(key, patient[key]);
                }
                if (patient.dob) {
                    const date = new Date(patient.dob);
                    const formattedDate = `${String(date.getDate()).padStart(2, '0')}/${String(date.getMonth() + 1).padStart(2, '0')}/${date.getFullYear()}`;
                    form.append('dob', formattedDate);
                }
            }

            try {
                setErr(null);
                setLoading(true);

                let res = await authApis().post(USER_ENDPOINTS.PATIENT_PROFILES, form);

                if (res.status === 201) {
                    nav('/patient/profiles');
                }

            } catch (error) {
                if (error.response && error.response.data) {
                    handleShowErr(error.response.data.message);
                }
                else if (error.request) {
                    handleShowErr("Không thể kết nối đến máy chủ. Vui lòng kiểm tra mạng!");
                }
            } finally {
                setLoading(false);
            }
        }
    }

    return (
        <>
            <div className="d-flex flex-column min-vh-100">
                <Header />
                <Container className="mt-5 mb-5" style={{ maxWidth: '600px' }}>
                    <Card>
                        <Card.Body>
                            <h3 className="mb-4 text-center text-primary">Thêm hồ sơ bệnh nhân</h3>
                            {err && <Alert variant="danger">{err}</Alert>}
                            <Form onSubmit={addRecord}>
                                {patientInfo.map(u =>
                                    <Form.Floating key={u.field} className="mb-3">
                                        {u.type === "select" ? (
                                            <Form.Select
                                                style={formCardStyle.input}
                                                value={patient[u.field] || ""}
                                                onChange={(e) => setPatient({ ...patient, [u.field]: e.target.value })}
                                            >
                                                <option value="">{u.title}</option>


                                                {u.options.map((opt, index) => {

                                                    const isObject = typeof opt === 'object' && opt !== null;


                                                    const optValue = isObject ? opt.value : opt;
                                                    const optLabel = isObject ? opt.label : opt;

                                                    return (
                                                        <option key={index} value={optValue}>
                                                            {optLabel}
                                                        </option>
                                                    );
                                                })}
                                            </Form.Select>
                                        ) : (
                                            <Form.Control
                                                style={formCardStyle.input}
                                                type={u.type}
                                                placeholder={u.title}
                                                value={patient[u.field] || ""}
                                                onChange={(e) => setPatient({ ...patient, [u.field]: e.target.value })}
                                            />
                                        )}
                                        <Form.Label>{u.title}</Form.Label>
                                    </Form.Floating>)}
                                <Form.Group className="mb-3" controlId="button">
                                    <div className="d-flex gap-3">
                                        <Button
                                            variant="outline-danger"
                                            type="button"
                                            className="w-100 rounded-4"
                                            onClick={() => nav('/patient/profiles')}
                                        >
                                            Quay lại
                                        </Button>
                                        {loading === true ? (
                                            <div className="w-100 d-flex justify-content-center align-items-center">
                                                <MySpinner />
                                            </div>
                                        ) : (
                                            <Button
                                                variant="primary"
                                                type="submit"
                                                className="w-100 border-0"
                                                style={formCardStyle.button}
                                            >
                                                Thêm hồ sơ
                                            </Button>
                                        )}
                                    </div>
                                </Form.Group>
                            </Form>
                        </Card.Body>
                    </Card>
                </Container>
                <Footer />
            </div>
        </>
    );
};

export default RegisterRecord;