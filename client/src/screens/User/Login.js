import { Button, Card, Form, Alert } from "react-bootstrap";
import { Link, useNavigate } from "react-router-dom";
import { useState } from 'react';
import { jwtDecode } from "jwt-decode";
import cookies from 'react-cookies'

import Apis, { authApis, endpoint } from "../../configs/Apis";

import MySpinner from '../../components/MySpinner';
import { requestForToken } from "../../configs/firebaseConfig";

const Login = () => {
    const [user, setUser] = useState({});
    const [err, setErr] = useState("");
    const [loading, setLoading] = useState(false);
    const nav = useNavigate();

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
        <div className="login-page">
            <div className="login-card-container">
                <div className="login-grid">
                    <div className="login-illustration">
                        <div className="login-illustration-content">
                            <div className="login-hero-dot" />
                            <div className="login-hero-dot small" />
                        </div>
                    </div>
                    <Card className="login-card shadow-lg">
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
                                    />
                                    <Form.Label htmlFor="loginPassword">Mật khẩu</Form.Label>
                                </Form.Floating>
                                <Button
                                    variant="primary"
                                    type="submit"
                                    className="w-100 login-button"
                                    disabled={loading}
                                >
                                    {loading ? <MySpinner /> : 'Đăng nhập'}
                                </Button>
                            </Form>
                            <div className="login-footer text-center mt-4">
                                Chưa có tài khoản? <Link to="/register">Đăng ký ngay</Link>
                            </div>
                        </Card.Body>
                    </Card>
                </div>
            </div>
        </div>
    );
}

export default Login;