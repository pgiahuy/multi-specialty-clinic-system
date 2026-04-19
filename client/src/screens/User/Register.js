import { Button, Card, Form } from "react-bootstrap";
import { Link } from "react-router-dom";

const Register = () => {
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
                                <h3>Đăng ký</h3>
                                <p>Tạo tài khoản mới để quản lý lịch khám và hồ sơ cá nhân.</p>
                            </div>

                            <Form>
                                <Form.Floating className="mb-3">
                                    <Form.Control
                                        id="registerUsername"
                                        type="text"
                                        placeholder="Tên tài khoản"
                                    />
                                    <Form.Label htmlFor="registerUsername">Tên tài khoản</Form.Label>
                                </Form.Floating>

                                <Form.Floating className="mb-3">
                                    <Form.Control
                                        id="registerPassword"
                                        type="password"
                                        placeholder="Mật khẩu"
                                    />
                                    <Form.Label htmlFor="registerPassword">Mật khẩu</Form.Label>
                                </Form.Floating>

                                <Form.Floating className="mb-3">
                                    <Form.Control
                                        id="registerFullname"
                                        type="text"
                                        placeholder="Họ tên"
                                    />
                                    <Form.Label htmlFor="registerFullname">Họ tên</Form.Label>
                                </Form.Floating>

                                <Form.Floating className="mb-3">
                                    <Form.Control
                                        id="registerEmail"
                                        type="email"
                                        placeholder="email@gmail.com"
                                    />
                                    <Form.Label htmlFor="registerEmail">Email</Form.Label>
                                </Form.Floating>

                                <Form.Floating className="mb-3">
                                    <Form.Select id="registerGender" defaultValue="Nam">
                                        <option value="Nam">Nam</option>
                                        <option value="Nữ">Nữ</option>
                                    </Form.Select>
                                    <Form.Label htmlFor="registerGender">Giới tính</Form.Label>
                                </Form.Floating>

                                <Button variant="primary" type="submit" className="w-100 login-button">
                                    Đăng ký
                                </Button>
                            </Form>

                            <div className="login-footer text-center mt-4">
                                Đã có tài khoản? <Link to="/login">Đăng nhập</Link>
                            </div>
                        </Card.Body>
                    </Card>
                </div>
            </div>
        </div>

    );
}

export default Register;