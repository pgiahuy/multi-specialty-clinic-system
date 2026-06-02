import { useRef, useState } from "react";
import { Button, Card, Container, Form, Alert } from "react-bootstrap";
import { Link, useNavigate } from "react-router-dom";
import MySpinner from "../../components/MySpinner";
import API, { AUTH_ENDPOINTS, endpoint, USER_ENDPOINTS } from "../../configs/Apis";
import { formCardStyle } from "./UserStyle";

const Register = () => {

    const userInfo = [{
        field: "name",
        title: "Họ và tên",
        type: "text",
    }, {
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



    const validateUsername = (username) => {
        if (!username || !username.trim()) return "Tên tài khoản không được để trống!";
        const value = username.trim();
        if (value.length < 3 || value.length > 20) return "Tên tài khoản phải có từ 3 đến 20 ký tự!";
        if (!/^[a-z0-9._-]+$/.test(value)) return "Tên tài khoản chỉ được chứa chữ thường, số, dấu chấm, gạch dưới và gạch ngang!";
        if (/\s/.test(value)) return "Tên tài khoản không được chứa khoảng trắng!";
        return null;
    };

    const validatePassword = (password) => {
        if (!password || !password.trim()) return "Mật khẩu không được để trống!";
        if (password.length < 6) return "Mật khẩu phải có ít nhất 6 ký tự!";
        if (!/[A-Z]/.test(password)) return "Mật khẩu phải chứa ít nhất một chữ cái viết hoa!";
        if (!/[a-z]/.test(password)) return "Mật khẩu phải chứa ít nhất một chữ cái viết thường!";
        if (!/\d/.test(password)) return "Mật khẩu phải chứa ít nhất một chữ số!";
        if (!/[!@#$%^&*()]/.test(password)) return "Mật khẩu phải chứa ít nhất một ký tự đặc biệt!";
        if (password.includes(" ")) return "Mật khẩu không được chứa khoảng trắng!";
        return null;
    };

    const validateEmail = (email) => {
        if (!email || !email.trim()) return "Email không được để trống!";
        const value = email.trim();
        if (!/^[\w.-]+@[\w.-]+\.[a-zA-Z]{2,}$/.test(value)) return "Email không hợp lệ!";
        if (value.includes(" ")) return "Email không được chứa khoảng trắng!";
        return null;
    };

    const validate = () => {
        for (let u of userInfo) {
            if (u.field === "confirm") continue;
            if (!(u.field in user) || !user[u.field]?.toString().trim()) {
                setErr(`Vui lòng nhập ${u.title}!`);
                return false;
            }
        }

        const usernameError = validateUsername(user.username);
        if (usernameError) {
            setErr(usernameError);
            return false;
        }

        const passwordError = validatePassword(user.password);
        if (passwordError) {
            setErr(passwordError);
            return false;
        }

        if (user.password !== user.confirm) {
            setErr('Mật khẩu không khớp!');
            return false;
        }

        const emailError = validateEmail(user.email);
        if (emailError) {
            setErr(emailError);
            return false;
        }

        if (!user.name || !user.name.trim()) {
            setErr('Họ và tên không được để trống!');
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
                    const serverMessage = error.response.data?.message || error.response.data || error.message;
                    setErr(serverMessage || "Đã xảy ra lỗi hệ thống. Vui lòng thử lại sau.");
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
                        {userInfo.map(u => {
                            const controlId = `userInfo-${u.field}`;
                            return (
                                <Form.Floating key={u.field} className="mb-3" controlId={controlId}>
                                    <Form.Control
                                        id={controlId}
                                        style={formCardStyle.input}
                                        type={u.type}
                                        placeholder={u.title}
                                        value={user[u.field] || ''}
                                        onChange={e => setUser({ ...user, [u.field]: e.target.value })}
                                    />
                                    <Form.Label htmlFor={controlId}>{u.title}</Form.Label>
                                </Form.Floating>
                            );
                        })}

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