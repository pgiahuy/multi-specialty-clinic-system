import {
    Badge,
    Button,
    Container,
    Nav,
    Navbar,
    NavDropdown,
} from "react-bootstrap";
import { Bell } from "react-bootstrap-icons";
import { Link, useNavigate } from "react-router-dom";
import NotificationBox from "./NotificationBox";
import { endpoint } from "../configs/Apis";
import { useContext } from "react";
import { MyUserContext } from "../configs/Contexts";


const Header = () => {
    const navigate = useNavigate();
    const [user, dispatch] = useContext(MyUserContext);

    const handleLogout = () => {
        localStorage.removeItem("user");
        dispatch({ type: 'LOGOUT' });
        navigate("/");
    };

    return (
        <Navbar
            expand="lg"
            className="bg-body-tertiary"
            style={{
                backgroundColor: '#e6f2ff',
                backdropFilter: 'blur(6px)',
                boxShadow: '0 2px 6px rgba(0,0,0,0.04)',
                position: 'sticky',
                top: 0,
                zIndex: 1030,
            }}
        >
            <Container fluid className="m-0 ps-5 pe-4">
                <Navbar.Brand onClick={() => navigate('/')}>
                    OU-Clinic
                </Navbar.Brand>
                <Navbar.Toggle aria-controls="basic-navbar-nav" />
                <Navbar.Collapse id="basic-navbar-nav">
                    <Nav className="me-auto">
                        <Nav.Link onClick={() => navigate('/')}>
                            Trang chủ
                        </Nav.Link>
                        <Nav.Link onClick={() => navigate('/doctors')}>
                            Bác sĩ
                        </Nav.Link>

                        <NavDropdown title="Chuyên khoa" id="basic-nav-dropdown">
                            <NavDropdown.Item href="#action/3.1">Action</NavDropdown.Item>
                            <NavDropdown.Item href="#action/3.2">
                                Another action
                            </NavDropdown.Item>
                            <NavDropdown.Item href="#action/3.3">Something</NavDropdown.Item>
                            <NavDropdown.Divider />
                            <NavDropdown.Item href="#action/3.4">
                                Separated link
                            </NavDropdown.Item>
                        </NavDropdown>
                        <Nav.Link onClick={() => navigate('/patient/dashboard')}>
                           Dịch vụ
                        </Nav.Link>
                        <Nav.Link onClick={() => navigate('/')}>
                           Liên hệ
                        </Nav.Link>
                    </Nav>
                    <Nav>

                    </Nav>

                    <Nav>
                        <NotificationBox onNavigate={(path) => {
                            if (!path) return;
                            if (path.startsWith('/api/secure/prescriptions') || path.includes('/prescriptions')) {
                                navigate('/patient/prescriptions');
                                return;
                            }
                            if (path.startsWith('http://') || path.startsWith('https://')) {
                                window.open(path, '_blank');
                                return;
                            }
                            try { navigate(path); } catch (e) { window.open(path, '_blank'); }
                        }} />
                    </Nav>
                    {user === null ? <>
                        <Button variant="outline-primary" className="m-2" as={Link} to="/login">
                            Đăng ký
                        </Button>
                        <Button variant="primary" className="m-2" as={Link} to="/login">
                            Đăng nhập
                        </Button>
                    </> : <>
                        <NavDropdown
                            align="end"
                            id="user-nav-dropdown"
                            title={
                                <span className="d-inline-flex align-items-center">
                                    <img
                                        src={user.avatar}
                                        className="rounded-circle"
                                        style={{ width: 36, height: 36, objectFit: 'cover', border: '2px solid rgba(13,110,253,0.12)' }}
                                    />
                                    
                                </span>
                            }
                        >
                            <NavDropdown.Item onClick={() => navigate('/patient/profile')}>Cập nhật thông tin</NavDropdown.Item>
                            <NavDropdown.Item onClick={() => navigate('/user/change-password')}>Đổi mật khẩu</NavDropdown.Item>
                            <NavDropdown.Divider />
                            <NavDropdown.Item className="text-danger" onClick={handleLogout}>
                                Đăng xuất
                            </NavDropdown.Item>
                        </NavDropdown>
                    </>}
                </Navbar.Collapse>
            </Container>
        </Navbar>
    );
};

export default Header;
