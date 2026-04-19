import { Button, Card, Form } from "react-bootstrap";
import { Link } from "react-router-dom";

const Login = () => {
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
                            <Form>
                                <Form.Floating className="mb-3">
                                    <Form.Control
                                        id="loginEmail"
                                        type="text"
                                        placeholder="Tên tài khoản"
                                    />
                                    <Form.Label htmlFor="loginEmail">Tên tài khoản</Form.Label>
                                </Form.Floating>
                                <Form.Floating className="mb-3">
                                    <Form.Control
                                        id="loginPassword"
                                        type="password"
                                        placeholder="Mật khẩu"
                                    />
                                    <Form.Label htmlFor="loginPassword">Mật khẩu</Form.Label>
                                </Form.Floating>
                                <Button variant="primary" type="submit" className="w-100 login-button">
                                    Đăng nhập
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