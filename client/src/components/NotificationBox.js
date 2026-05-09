import React, { useEffect, useState } from 'react';
import { NavDropdown, Badge, ListGroup, Stack } from 'react-bootstrap';
import { Bell, CircleFill, Check2All, Trash2 } from 'react-bootstrap-icons';
import './NotificationBox.css';
import { onMessageListener, requestForToken } from '../configs/firebaseConfig';
import { authApis, endpoint } from '../configs/Apis';
import cookies from 'react-cookies';

const NotificationBox = ({ onNavigate }) => {
    const [notifications, setNotifications] = useState([]);
    const [fcmToken, setFcmToken] = useState(null);

    const unreadCount = notifications?.filter(n => !n.isRead).length || 0;

    const markAllAsRead = () => {
        setNotifications(prev => prev.map(n => ({ ...n, isRead: true })));
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
        const notificationId = payload.data?.notificationId || payload.notification?.id || Date.now().toString();
        const path = payload.data?.path || payload.data?.url || payload.data?.link || null;
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

    const handleNotificationClick = (noti) => {
        try {
            setNotifications(prev => prev.map(n => n.id === noti.id ? { ...n, isRead: true } : n));
            if (noti.path) {
                if (onNavigate) { onNavigate(noti.path); return; }
                if (noti.path.startsWith('http://') || noti.path.startsWith('https://')) { window.open(noti.path, '_blank'); return; }
                try { window.location.href = noti.path; } catch (e) { window.open(noti.path, '_blank'); }
                return;
            }
            const url = noti.url || noti.data?.url || null;
            if (url) window.open(url, '_blank');
        } catch (err) {
            console.error('Error handling notification click:', err);
        }
    };


    useEffect(() => {
        handleRequestNotificationPermission();
        fetchNotifications();

        onMessageListener()
            .then(payload => addNotificationToState(payload))
            .catch(err => console.log('Lỗi FCM (foreground):', err));

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
                                className={`notification-item ${!noti.isRead ? 'unread' : ''}`}
                                action
                                onClick={() => handleNotificationClick(noti)}
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
                                            {noti.createdAt ? new Date(noti.createdAt).toLocaleString('vi-VN') : noti.time || 'Vừa xong'}
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