import { Badge, Button, Container, Nav, Navbar, NavDropdown } from "react-bootstrap";
import { Bell } from "react-bootstrap-icons";
import { Link, useNavigate } from "react-router-dom";
import NotificationBox from "./NotificationBox";
import LoginRequiredModal from "./LoginRequiredModal";
import API, { AUTH_ENDPOINTS, authApis, CLINIC_ENDPOINTS } from "../configs/Apis";
import cookies from 'react-cookies';
import { useContext, useEffect, useState } from "react";
import { MyUserContext } from "../configs/Contexts";


const Header = () => {
    const navigate = useNavigate();
    const [specialties, setSpecialties] = useState([]);
    const [showLoginRequired, setShowLoginRequired] = useState(false);
    const [user, dispatch] = useContext(MyUserContext);

    const handleLogout = async () => {
        try {
            const refreshToken = cookies.load('refreshToken');
            if (refreshToken) {
                await authApis().post(AUTH_ENDPOINTS.LOGOUT, { refreshToken });
            }
        } catch (err) {
            console.error('Logout revoke failed:', err);
        } finally {
            cookies.remove('accessToken', { path: '/' });
            cookies.remove('refreshToken', { path: '/' });
            localStorage.removeItem("user");
            dispatch({ type: "LOGOUT" });
            navigate("/");
        }
    };


    const loadSpecialties = async () => {
        try {
            const response = await API.get(CLINIC_ENDPOINTS.SPECIALTIES);
            setSpecialties(response.data);
        } catch (error) {
            console.error("Lỗi khi lấy danh sách chuyên khoa:", error);
        }
    };

    useEffect(() => {
        loadSpecialties();
    }, []);

    return (
        <Navbar
            expand="lg"
            className="header-navbar"
            sticky="top"
            style={{ zIndex: 1030 }}
        >
            <Container fluid className="m-0 ps-5 pe-4 py-2">
                <Navbar.Brand className="header-brand mb-0" onClick={() => navigate('/')}>
                    OU-Clinic
                </Navbar.Brand>
                <Navbar.Toggle aria-controls="basic-navbar-nav" />
                <Navbar.Collapse id="basic-navbar-nav">
                    <Nav className="w-100 d-flex align-items-center">
                        <Nav.Link
                            className="header-navlink"
                            onClick={() => {
                                if (user === null) {
                                    setShowLoginRequired(true);
                                    return;
                                }
                                navigate(user.role === 'ROLE_DOCTOR' ? '/doctor/dashboard' : '/patient/dashboard');
                            }}
                        >
                            {user?.role === 'ROLE_DOCTOR' ? 'Công việc' : 'Dịch vụ'}
                        </Nav.Link>



                        {user && user.role === 'ROLE_PATIENT' && (
                            <Nav.Link className="header-navlink" onClick={() => navigate('/patient/appointments')}>
                                Bác sĩ
                            </Nav.Link>
                        )}
                        {user && user.role === 'ROLE_DOCTOR' && (
                            <Nav.Link className="header-navlink" onClick={() => navigate('/doctor/appointments')}>
                                Lịch hẹn
                            </Nav.Link>
                        )}

                        <NavDropdown title="Chuyên khoa" id="specialties-nav-dropdown" className="me-2 header-dropdown">
                            {specialties.map(s => (
                                <NavDropdown.Item key={s.id} onClick={() => navigate(`/specialties/${s.id}`)}>
                                    {s.name}
                                </NavDropdown.Item>
                            ))}
                        </NavDropdown>




                        <Nav.Link className="header-navlink" onClick={() => navigate('/')}>
                            Liên hệ
                        </Nav.Link>

                        {user === null ? (
                            <div className="ms-auto d-flex align-items-center">
                                <Button variant="outline-primary" className="rounded-4 m-2" as={Link} to="/register">
                                    Đăng ký
                                </Button>
                                <Button variant="primary" className="rounded-4 border-0 m-2" as={Link} to="/login">
                                    Đăng nhập
                                </Button>
                            </div>
                        ) : (
                            <div className="ms-auto d-flex align-items-center">
                                <Nav className="align-items-center header-notification me-1">
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
                                <NavDropdown
                                    align="end"
                                    id="user-nav-dropdown"
                                    title={
                                        <span className="d-inline-flex align-items-center">
                                            <img
                                                src={user?.avatar}
                                                className="rounded-circle header-avatar"
                                                alt="avatar"
                                                onError={(e) => e.target.src = '/default-avatar.png'}
                                            />
                                        </span>
                                    }>

                                    {user?.role === 'ROLE_DOCTOR' ? (
                                        <NavDropdown.Item onClick={() => navigate('/doctor/profile')}>Thông tin cá nhân & tài khoản</NavDropdown.Item>
                                    ) : (
                                        <>
                                            <NavDropdown.Item onClick={() => navigate('/patient/profiles')}>Hồ sơ sức khỏe</NavDropdown.Item>
                                            <NavDropdown.Item onClick={() => navigate('/patient/account')}>Xem tài khoản</NavDropdown.Item>
                                        </>
                                    )}
                                    <NavDropdown.Item onClick={() => navigate('/user/change-password')}>Đổi mật khẩu</NavDropdown.Item>
                                    <NavDropdown.Divider />
                                    <NavDropdown.Item className="text-danger" onClick={handleLogout}>
                                        Đăng xuất
                                    </NavDropdown.Item>
                                </NavDropdown>
                            </div>
                        )}
                    </Nav>
                    <LoginRequiredModal
                        show={showLoginRequired}
                        onHide={() => setShowLoginRequired(false)}
                        onLogin={() => navigate('/login')}
                    />
                </Navbar.Collapse>
            </Container>
        </Navbar >
    );
};

export default Header;
