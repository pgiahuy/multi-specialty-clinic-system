import {
    Badge,
    Button,
    Container,
    Nav,
    Navbar,
    NavDropdown,
} from "react-bootstrap";
import { Bell } from "react-bootstrap-icons";
import { Link } from "react-router-dom";
import NotificationBox from "./NotificationBox";
import { useEffect, useState } from "react";
import { onMessageListener, requestForToken } from "../configs/firebaseConfig";
import { authApis, endpoint } from "../configs/Apis";

import cookies from 'react-cookies'




const Header = () => {


    const [notifications, setNotifications] = useState([]);
    const [fcmToken, setFcmToken] = useState(null);


    // Request FCM token from user
    const handleRequestNotificationPermission = async () => {
        try {
            // Check if browser supports notifications
            if ('Notification' in window) {
                if (Notification.permission === 'granted') {
                    // Already granted, just get token
                    const token = await requestForToken();
                    if (token) {
                        setFcmToken(token);
                        console.log('FCM Token updated:', token);
                        // Optionally send token to backend to save
                        // await saveFcmTokenToBackend(token);
                    }
                } else if (Notification.permission !== 'denied') {
                    // Request permission
                    const permission = await Notification.requestPermission();
                    if (permission === 'granted') {
                        const token = await requestForToken();
                        if (token) {
                            setFcmToken(token);
                            console.log('FCM Token obtained:', token);
                            // Optionally send token to backend to save
                            // await saveFcmTokenToBackend(token);
                        }
                    }
                }
            }
        } catch (err) {
            console.error('Error requesting notification permission:', err);
        }
    };

    const fetchNotifications = async () => {
        try {

            const res = await authApis().get(endpoint['notifications']);
            setNotifications(res.data);
        } catch (err) {
            console.error("Không thể lấy thông báo:", err);
        }
    };

    const addNotificationToState = (payload) => {
        // Lấy ID từ backend (nếu có), hoặc tạo mới
        const notificationId = payload.data?.notificationId || payload.notification.id || Date.now().toString();
        const newNoti = {
            id: notificationId,
            content: payload.notification.body,
            title: payload.notification.title,
            time: "Vừa xong",
            isRead: false
        };

        // Kiểm tra xem notification này đã có trong state chưa (tránh trùng lặp)
        setNotifications(prev => {
            const exists = prev.some(n => n.id === notificationId);
            if (exists) {
                console.log('Notification đã tồn tại, bỏ qua:', notificationId);
                return prev;
            }
            return [newNoti, ...prev];
        });
    };

    useEffect(() => {
        // Request notification permission when component mounts
        handleRequestNotificationPermission();
        fetchNotifications();

        // 1. Lắng nghe khi tab đang mở (Foreground)
        onMessageListener()
            .then((payload) => {
                addNotificationToState(payload);
            })
            .catch((err) => console.log('Lỗi FCM:', err));

        // 2. Lắng nghe từ Service Worker (Background truyền sang)
        const bc = new BroadcastChannel('fcm_notifications');
        bc.onmessage = (event) => {
            console.log("Nhận từ BroadcastChannel:", event.data);
            addNotificationToState(event.data);
        };

        return () => bc.close(); // Dọn dẹp khi component unmount
    }, []);

    return (
        <Navbar expand="lg" className="bg-body-tertiary">
            <Container fluid className="m-0 ps-5 pe-4">
                <Navbar.Brand href="#home">OU-Clinic</Navbar.Brand>
                <Navbar.Toggle aria-controls="basic-navbar-nav" />
                <Navbar.Collapse id="basic-navbar-nav">
                    <Nav className="me-auto">
                        <Nav.Link href="#home">Trang chủ</Nav.Link>
                        <Nav.Link href="#link">Link</Nav.Link>
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
                        <Nav.Link href="#profile">Hồ sơ</Nav.Link>
                        <Nav.Link href="#appointments">Lịch hẹn</Nav.Link>
                    </Nav>

                    <Nav>
                        <NotificationBox notifications={notifications} />
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
