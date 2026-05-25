import React, { use, useEffect, useState } from 'react';
import { NavDropdown, Badge, ListGroup, Stack } from 'react-bootstrap';
import { Bell, CircleFill, Check2All, Trash2 } from 'react-bootstrap-icons';
import './NotificationBox.css';
import { onMessageListener, requestForToken } from '../configs/firebaseConfig';
import { authApis, endpoint } from '../configs/Apis';
import cookies from 'react-cookies';
import { useNavigate } from 'react-router-dom';

const NotificationBox = ({ onNavigate }) => {
    const [notifications, setNotifications] = useState([]);
    const [fcmToken, setFcmToken] = useState(null);

    const navigate = useNavigate();

    const unreadCount = notifications?.filter(n => !n.isRead).length || 0;

    const markAllAsRead = () => {
        setNotifications(prev => prev.map(n => ({ ...n, isRead: true })));
        // notifications.filter(n => !n.isRead).forEach(noti => {
        //     authApis.patch(`secure/notifications/${noti.id}/read`);
        // });
        console.log("Đánh dấu tất cả là đã đọc");
    };

    const deleteNotification = (id) => {
        setNotifications(prev => prev.filter(n => n.id !== id));
        console.log("Xóa thông báo:", id);
    };

    const fetchNotifications = async () => {
        try {
            const res = await authApis(cookies.load('token')).get(endpoint['notifications']);
            setNotifications(res.data || []);
        } catch (err) {
            console.error("Không thể lấy thông báo:", err);
        }
    };

    const addNotificationToState = (payload) => {
        const notificationId = payload.data?.id || Date.now().toString();
        const path = payload.data?.path || null;
        const newNoti = {
            id: notificationId,
            content: payload.notification?.body,
            title: payload.notification?.title,
            time: "Vừa xong",
            isRead: false,
            path,
            raw: payload
        };

        setNotifications(prev => {
            const exists = prev.some(n => n.id === notificationId);
            if (exists) return prev;
            return [newNoti, ...prev];
        });
    };

    const handleRequestNotificationPermission = async () => {
        try {
            if ('Notification' in window) {
                if (Notification.permission === 'granted') {
                    const token = await requestForToken();
                    if (token) setFcmToken(token);
                } else if (Notification.permission !== 'denied') {
                    const permission = await Notification.requestPermission();
                    if (permission === 'granted') {
                        const token = await requestForToken();
                        if (token) setFcmToken(token);
                    }
                }
            }
        } catch (err) {
            console.error('Error requesting notification permission:', err);
        }
    };


    const handleNotificationClick = async (noti) => {
        try {

            setNotifications(prev => prev.map(n => n.id === noti.id ? { ...n, isRead: true } : n));


            const token = cookies.load('token');
            await authApis(token).patch(`secure/notifications/${noti.id}/read`);


            const targetPath = noti.path || noti.click_action || noti.data?.click_action;
            // noti.path: "/api/secure/prescriptions/1"
            navigate('/patient/prescriptions')

            // if (targetPath) {
            //     navigate(targetPath);
            // } else {
            //     console.warn("Không tìm thấy đường dẫn cho thông báo này!");
            // }

        } catch (err) {

            console.error('Error handling notification click:', err);
        }
    };


    useEffect(() => {
        handleRequestNotificationPermission();
        fetchNotifications();

        onMessageListener()
            .then(payload => addNotificationToState(payload))
            .catch(err => console.log('Lỗi FCM:', err));

        const bc = new BroadcastChannel('fcm_notifications');
        bc.onmessage = (event) => addNotificationToState(event.data);

        return () => bc.close();
    }, []);

    return (
        <NavDropdown

            title={
                <span className="notification-bell-container">
                    <Bell
                        size={24}
                        className="notification-bell"
                    />
                    {unreadCount > 0 && (
                        <Badge
                            pill
                            bg="danger"
                            className="notification-badge"
                        >
                            {unreadCount > 9 ? '9+' : unreadCount}
                        </Badge>
                    )}
                </span>
            }
            id="notification-dropdown"
            align="end"
            className="notification-dropdown"
            drop="down"
        >
            <div className="notification-box">

                <div className="notification-header">
                    <span className="notification-title">Thông báo</span>
                    {unreadCount > 0 && (
                        <button
                            className="mark-read-btn"
                            onClick={markAllAsRead}
                            title="Đánh dấu tất cả là đã đọc"
                        >
                            <Check2All size={14} /> Đánh dấu đã đọc
                        </button>
                    )}
                </div>


                <ListGroup variant="flush" className="notification-list">
                    {notifications.length > 0 ? (
                        notifications.map((noti) => (
                            <ListGroup.Item
                                key={noti.id}
                                as="div"
                                role="button"
                                tabIndex={0}
                                className={`notification-item ${!noti.isRead ? 'unread' : ''}`}
                                onClick={() => handleNotificationClick(noti)}
                                onKeyDown={(e) => {
                                    if (e.key === 'Enter' || e.key === ' ') {
                                        e.preventDefault();
                                        handleNotificationClick(noti);
                                    }
                                }}
                            >
                                <Stack direction="horizontal" gap={3} className="align-items-start notification-content">

                                    <div className="notification-icon">
                                        {noti.icon || <Bell size={16} />}
                                    </div>


                                    <div className="notification-text-wrapper">
                                        <div className="notification-text-title">
                                            {noti.title || 'Thông báo'}
                                        </div>
                                        <div className="notification-text-body">
                                            {noti.content || noti.body || 'Bạn có một thông báo mới'}
                                        </div>
                                        <div className="notification-text-time">
                                            {noti.createdAt
                                                ? (typeof noti.createdAt === 'string' ? noti.createdAt.slice(0, -3) : noti.createdAt)
                                                : 'Vừa xong'}
                                        </div>
                                    </div>


                                    {!noti.isRead && (
                                        <CircleFill size={8} className="notification-unread-dot" />
                                    )}


                                    <button
                                        className="notification-delete-btn"
                                        onClick={(e) => {
                                            e.stopPropagation();
                                            deleteNotification(noti.id);
                                        }}
                                        title="Xóa thông báo"
                                    >
                                        <Trash2 size={14} />
                                    </button>
                                </Stack>
                            </ListGroup.Item>
                        ))
                    ) : (
                        <div className="notification-empty">
                            <Bell size={32} className="empty-icon" />
                            <p>Không có thông báo mới</p>
                        </div>
                    )}
                </ListGroup>


                {notifications.length > 0 && (
                    <div className="notification-footer">
                        <button className="view-all-btn" onClick={() => onNavigate ? onNavigate('/patient/notifications') : window.location.assign('/patient/notifications')}>
                            Xem tất cả
                        </button>
                    </div>
                )}
            </div>
        </NavDropdown>
    );
};

export default NotificationBox;