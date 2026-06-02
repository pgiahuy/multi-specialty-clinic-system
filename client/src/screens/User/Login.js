import { Button, Card, Form, Alert, Container } from "react-bootstrap";
import { Link, useNavigate } from "react-router-dom";
import { useContext, useState } from 'react';
import { jwtDecode } from "jwt-decode";
import cookies from "react-cookies";
import API, { AUTH_ENDPOINTS, authApis, USER_ENDPOINTS } from "../../configs/Apis";

import MySpinner from '../../components/MySpinner';

import { formCardStyle } from "./UserStyle";
import { MyUserContext } from "../../configs/Contexts";
import GoogleLoginButton from "./GoogleLoginButton";
import FacebookLoginButton from "./FacebookLoginButton";
import { requestForToken } from "../../configs/firebaseConfig";

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
                const fcmToken = await Promise.race([
                    requestForToken(),
                    new Promise((resolve) => setTimeout(() => resolve(null), 1500))
                ]);

                let deviceId = localStorage.getItem('deviceId');
                if (!deviceId) {
                    deviceId = crypto?.randomUUID ? crypto.randomUUID() : Math.random().toString(36).slice(2) + Date.now();
                    localStorage.setItem('deviceId', deviceId);
                }
                const deviceInfo = navigator.userAgent;

                const res = await API.post(AUTH_ENDPOINTS.LOGIN, {
                    ...user,
                    fcmToken: fcmToken,
                    deviceId: deviceId,
                    deviceInfo: deviceInfo
                });



                const decoded = jwtDecode(res.data.accessToken);
                const role = decoded.role;
                cookies.save("accessToken", res.data.accessToken, { path: '/' });
                cookies.save("refreshToken", res.data.refreshToken, { path: '/' });



                let u = await authApis().get(USER_ENDPOINTS.CURRENT_USER);
                localStorage.setItem("user", JSON.stringify(u.data));

                dispatch({ "type": "LOGIN", "payload": u.data });
                console.log("Login successful, user data:", u.data);

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
                                disabled={loading}
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
                                disabled={loading}
                            />
                            <Form.Label htmlFor="loginPassword">Mật khẩu</Form.Label>
                        </Form.Floating>
                        <Button
                            variant="primary"
                            type="submit"
                            className="w-100 rounded-4 border-0" style={formCardStyle.button}
                            disabled={loading}
                        >
                            {loading ? <MySpinner /> : 'Đăng nhập'}
                        </Button>
                        <div className="d-flex justify-content-center gap-2 mt-3 position-relative">
                            <FacebookLoginButton loading={loading} />
                            <GoogleLoginButton loading={loading} setLoading={setLoading} setErr={setErr} />
                        </div>
                    </Form>
                    <div className="login-footer text-center mt-5">
                        Chưa có tài khoản? <Link to="/register">Đăng ký ngay</Link>
                    </div>
                </Card.Body>
            </Card>
        </Container>


    );
}

export default Login;