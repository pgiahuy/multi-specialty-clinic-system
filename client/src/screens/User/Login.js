import { Button, Card, Form, Alert, Container, Image } from "react-bootstrap";
import { Link, useNavigate } from "react-router-dom";
import { useContext, useState } from 'react';
import { jwtDecode } from "jwt-decode";
import cookies from 'react-cookies'

import Apis, { authApis, endpoint } from "../../configs/Apis";

import MySpinner from '../../components/MySpinner';
import { requestForToken } from "../../configs/firebaseConfig";
import { formCardStyle } from "./UserStyle";
import { MyUserContext } from "../../configs/Contexts";

const Login = () => {
    const [user, setUser] = useState({});
    const [err, setErr] = useState("");
    const [loading, setLoading] = useState(false);
    const nav = useNavigate();
    const [, dispatch] = useContext(MyUserContext);

    const validate = () => {
        if (!user.username || !user.password) {
            setErr("Vui lòng điền đầy đủ tên đăng nhập và mật khẩu!");
            return false;
        }
        setErr("");
        return true;
    };

    const handleInputChange = (e) => {
        const { name, value } = e.target;
        setUser({ ...user, [name]: value });
    };

    const login = async (e) => {
        e.preventDefault();

        if (validate()) {
            try {
                setLoading(true);


                const fcmToken = await requestForToken();

                const res = await Apis.post(endpoint['login'], {
                    ...user,
                    fcmToken: fcmToken
                });

                const decoded = jwtDecode(res.data.token);
                const role = decoded.role;
                cookies.save("token", res.data.token);
                


                let u = await authApis().get(endpoint['current-user']);
                localStorage.setItem("user", JSON.stringify(u.data));
                cookies.save("user", u.data);

                dispatch({ "type": "LOGIN", "payload": u.data });


                setTimeout(async () => {
                    if (role === 'ROLE_DOCTOR') {
                        nav('/doctor/dashboard');
                    } else if (role === 'ROLE_PATIENT') {
                        nav('/patient/dashboard');
                    }
                }, 500);


            } catch (ex) {

                if (ex.response && ex.response.status === 401) {
                    setErr("Tên đăng nhập hoặc mật khẩu không đúng!");
                } else {
                    setErr("Đã có lỗi xảy ra. Vui lòng thử lại sau!");
                }
                console.error("Server error:", ex);
            } finally {
                setLoading(false);
            }
        }
    };

    return (
        <Container fluid style={formCardStyle.container}>
            <Card style={formCardStyle.card}>
                <Card.Body>
                    <div className="login-header text-center mb-4">
                        <h3>Đăng nhập</h3>
                    </div>
                    {err && <Alert variant="danger">{err}</Alert>}
                    <Form onSubmit={login}>
                        <Form.Floating className="mb-3">
                            <Form.Control
                                id="loginEmail"
                                type="text"
                                placeholder="Tên tài khoản"
                                name="username"
                                value={user.username || ''}
                                onChange={handleInputChange}
                                style={formCardStyle.input}
                            />
                            <Form.Label htmlFor="loginEmail">Tên tài khoản</Form.Label>
                        </Form.Floating>
                        <Form.Floating className="mb-3">
                            <Form.Control
                                id="loginPassword"
                                type="password"
                                placeholder="Mật khẩu"
                                name="password"
                                value={user.password || ''}
                                onChange={handleInputChange}
                                style={formCardStyle.input}
                            />
                            <Form.Label htmlFor="loginPassword">Mật khẩu</Form.Label>
                        </Form.Floating>
                        <Button
                            variant="primary"
                            type="submit"
                            className="w-100" style={formCardStyle.button}
                            disabled={loading}
                        >
                            {loading ? <MySpinner /> : 'Đăng nhập'}
                        </Button>
                        <Button
                            variant="outline-primary"
                            type="submit"
                            className="w-100 mt-3" style={formCardStyle.button}
                            disabled={loading}
                        ><Image src="/gg.png" alt="Google" style={{ width: 20, height: 20, marginRight: 8 }} />    
                            {loading ? <MySpinner /> : 'Đăng nhập bằng tài khoản Google'}
                        </Button>
                    </Form>
                    <div className="login-footer text-center mt-4">
                        Chưa có tài khoản? <Link to="/register">Đăng ký ngay</Link>
                    </div>
                </Card.Body>
            </Card>
        </Container>


    );
}

export default Login;