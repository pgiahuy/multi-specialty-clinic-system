import { Alert, Button, Card, Container, Form } from "react-bootstrap";
import { Link } from "react-bootstrap-icons";
import MySpinner from "../../components/MySpinner";
import Header from "../../components/Header";
import { formCardStyle } from "../User/UserStyle";
import { useState } from "react";
import { authApis, endpoint } from "../../configs/Apis";
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
    }];

    const [patient, setPatient] = useState({});
    const [err, setErr] = useState();
    const [loading, setLoading] = useState(false);
    const nav = useNavigate();


    const validate = () => {
        for (let u of patientInfo)
            if (!patient[u.field] || patient[u.field].toString().trim() === ""){ 
                setErr(`Vui lòng nhập ${u.title}!`);
                return false;
            }
        const cccdRegex = /^\d{12}$/;
        if (!cccdRegex.test(patient.cccd)) {
            setErr("CCCD phải có 12 số!");
            return false;
        }
        const phoneRegex = /(84|0[3|5|7|8|9])+([0-9]{8})\b/;
        if (!phoneRegex.test(patient.phone)) {
            setErr("Số điện thoại không hợp lệ!");
            return false;

            const selectedDate = new Date(patient.dob);
            const today = new Date();
            today.setHours(0, 0, 0, 0);

            if (selectedDate > today) {
                setErr("Ngày sinh không thể lớn hơn ngày hiện tại!");
                return false;
            }
        }
        setErr(null);
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
                setLoading(true);

                let res = await authApis().post(endpoint['patientProfiles'], form);

                if (res.status === 201) {
                    nav('/patient/profiles');
                }

            } catch (error) {
                setErr('Có lỗi xảy ra khi thêm hồ sơ!');
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
                                {patientInfo.map(u => <Form.Floating key={u.field} className="mb-3">
                                    {u.type === "select" ? (
                                        <Form.Select
                                            style={formCardStyle.input}
                                            value={patient[u.field] || ""}
                                            onChange={(e) => setPatient({ ...patient, [u.field]: e.target.value })}
                                        >
                                            <option value="">{u.title}</option>
                                            {u.options.map(opt => (
                                                <option key={opt} value={opt}>{opt}</option>
                                            ))}
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


                                <Form.Group className="mb-3 text-center " controlId="button" >
                                    {loading === true ? <MySpinner /> : <Button variant="primary" type="submit" className="w-100 border-0 header-cta header-cta-primary" style={formCardStyle.button}>
                                        Thêm hồ sơ
                                    </Button>}
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