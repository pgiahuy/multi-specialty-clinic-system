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


const Header = () => {
    const navigate = useNavigate();


    return (
        <Navbar expand="lg" className="bg-body-tertiary">
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
                    <Button variant="outline-success" className="m-2">
                        Đăng xuất
                    </Button>
                    <Button variant="outline-primary" className="m-2">
                        Đăng ký
                    </Button>
                    <Button variant="primary" className="m-2" as={Link} to="/login">
                        Đăng nhập
                    </Button>
                </Navbar.Collapse>
            </Container>
        </Navbar>
    );
};

export default Header;
