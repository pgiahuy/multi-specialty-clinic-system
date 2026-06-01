import { useRef, useState } from "react";
import { Button, Card, Container, Form, Alert } from "react-bootstrap";
import { Link, useNavigate } from "react-router-dom";
import MySpinner from "../../components/MySpinner";
import API, { AUTH_ENDPOINTS, endpoint, USER_ENDPOINTS } from "../../configs/Apis";
import { formCardStyle } from "./UserStyle";

const Register = () => {

    const userInfo = [{
        field: "username",
        title: "Tên tài khoản",
        type: "text",
    }, {
        field: "password",
        title: "Mật khẩu",
        type: "password",
    }, {
        field: "confirm",
        title: "Xác nhận mật khẩu",
        type: "password",
    }, {
        field: "email",
        title: "Email",
        type: "email",
    }];

    const [user, setUser] = useState({});
    const avatar = useRef();
    const [err, setErr] = useState();
    const nav = useNavigate();
    const [loading, setLoading] = useState(false);



    const validate = () => {
        for (let u of userInfo)
            if (!(u.field in user) || !user[u.field]) {
                setErr(`Vui lòng nhập ${u.title}!`);
                return false;
            }

        if (user.password !== user.confirm) {
            setErr('Mật khẩu không khớp!');
            return false;
        }

        return true;
    };

    const register = async (e) => {
        e.preventDefault();

        if (validate()) {
            let form = new FormData();
            for (let key of Object.keys(user)) {
                if (key !== "confirm")
                    form.append(key, user[key]);
            }

            if (avatar.current.files.length > 0) {
                form.append("avatar", avatar.current.files[0]);
            }

            try {
                setLoading(true);
                let res = await API.post(AUTH_ENDPOINTS.REGISTER, form, {
                    headers: {
                        'Content-Type': 'multipart/form-data'
                    }
                });

                if (res.status === 201) {
                    nav("/login");
                }


                else
                    alert("Hệ thống bị lỗi!");

            } catch (error) {
                if (error.response) {
                    if (error.response.status === 409) {
                        setErr(error.response.data);
                    } else {
                        setErr("Đã xảy ra lỗi hệ thống. Vui lòng thử lại sau.");
                    }
                } else {
                    setErr("Không thể kết nối đến máy chủ.");
                }
            } finally {
                setLoading(false);
            }
        }
    };

    return (
        <Container fluid className="d-flex justify-content-center align-items-center" style={formCardStyle.container}>
            <Card style={formCardStyle.card}>
                <Card.Body>
                    <h3 className="text-center mt-1">Đăng ký</h3>

                    {err && <Alert variant="danger">{err}</Alert>}

                    <Form onSubmit={register}>
                        {userInfo.map(u => <Form.Floating key={u.field} className="mb-3" controlId={u.field}>

                            <Form.Control id="userInfo"
                                style={formCardStyle.input}
                                type={u.type}
                                placeholder={u.title}
                                value={user[u.field]}
                                onChange={e => setUser({ ...user, [u.field]: e.target.value })
                                } />
                            <Form.Label htmlFor="userInfo">{u.title}</Form.Label>
                        </Form.Floating>)}

                        <Form.Floating className="mb-3" controlId="avatar">

                            <Form.Control id="avatar" type="file" ref={avatar} style={formCardStyle.input} />
                            <Form.Label htmlFor="avatar">
                                Ảnh đại diện
                            </Form.Label>
                        </Form.Floating>

                        <Form.Group className="mb-3 text-center" controlId="button">
                            {loading === true ? <MySpinner /> : <Button variant="primary" type="submit" className="w-100 rounded-4 border-0 " style={formCardStyle.button}>
                                Đăng ký
                            </Button>}
                            <div className="login-footer text-center mt-4">
                                Đã có tài khoản? <Link to="/login">Đăng nhập ngay</Link>
                            </div>
                        </Form.Group>
                    </Form>
                </Card.Body>
            </Card>
        </Container>
    );
}

export default Register;