import React, { useEffect, useState } from 'react';
import { NavDropdown, Badge, ListGroup, Stack } from 'react-bootstrap';
import { Bell, CircleFill, Check2All, Capsule, Trash2, CalendarCheck, CurrencyDollar } from 'react-bootstrap-icons';
import '../User/NotificationBox.css';
import { onMessageListener, requestForToken } from '../../configs/firebaseConfig';
import { authApis, USER_ENDPOINTS } from '../../configs/Apis';
import { useNavigate } from 'react-router-dom';

// Hàm helper đổi ngày tháng thành khoảng thời gian tương đối
const formatRelativeTime = (dateStr) => {
    if (!dateStr) return "Vừa xong";

    let notiDate;
    // Hỗ trợ parse nếu backend trả về định dạng Việt Nam DD/MM/YYYY HH:mm:ss
    if (typeof dateStr === 'string' && dateStr.includes('/')) {
        const [datePart, timePart] = dateStr.split(' ');
        const [day, month, year] = datePart.split('/');
        notiDate = timePart ? new Date(`${year}-${month}-${day}T${timePart}`) : new Date(`${year}-${month}-${day}`);
    } else {
        notiDate = new Date(dateStr);
    }

    if (isNaN(notiDate.getTime())) return "Vừa xong";

    const now = new Date();
    const diffInSeconds = Math.floor((now - notiDate) / 1000);

    if (diffInSeconds < 60) return "Vừa xong";

    const diffInMinutes = Math.floor(diffInSeconds / 60);
    if (diffInMinutes < 60) return `${diffInMinutes} phút trước`;

    const diffInHours = Math.floor(diffInMinutes / 60);
    if (diffInHours < 24) return `${diffInHours} giờ trước`;

    const diffInDays = Math.floor(diffInHours / 24);
    if (diffInDays < 30) return `${diffInDays} ngày trước`;

    const diffInMonths = Math.floor(diffInDays / 30);
    if (diffInMonths < 12) return `${diffInMonths} tháng trước`;

    return notiDate.toLocaleDateString('vi-VN');
};

const NotificationBox = ({ onNavigate }) => {
    const [notifications, setNotifications] = useState([]);
    const [fcmToken, setFcmToken] = useState(null);

    const navigate = useNavigate();
    const unreadCount = notifications?.filter(n => !n.isRead).length || 0;

    const markAllAsRead = () => {
        setNotifications(prev => prev.map(n => ({ ...n, isRead: true })));
        authApis().post(`secure/notifications/read-all`);
    };

    const ICON_MAP = [
        {
            keywords: ['thanh toán', 'tiền'],
            icon: <CurrencyDollar size={16} className="notification-icon-payment" />,
            bgClass: 'bg-success-subtle text-success'
        },
        {
            keywords: ['lịch khám', 'lịch hẹn'],
            icon: <CalendarCheck size={16} className="notification-icon-appointment" />,
            bgClass: 'bg-primary-subtle text-primary'
        },
        {
            keywords: ['đơn thuốc', 'thuốc'],
            icon: <Capsule size={16} className="notification-icon-prescription" />,
            bgClass: 'bg-warning-subtle text-warning'
        }
    ];

    const getNotificationIcon = (noti) => {
        const title = noti.title?.toLowerCase() || '';
        const matched = ICON_MAP.find(item => item.keywords.some(kw => title.includes(kw)));
        const icon = matched ? matched.icon : <Bell size={16} />;
        const bgClass = matched ? matched.bgClass : 'bg-light text-secondary';
        return (
            <div className={`notification-icon-wrapper ${bgClass} d-flex align-items-center justify-content-center rounded-circle flex-shrink-0`} style={{ width: '32px', height: '32px' }}>
                {icon}
            </div>
        );
    };

    const deleteNotification = (id) => {
        try {
            authApis().delete(`secure/notifications/${id}`);
            setNotifications(prev => prev.filter(n => n.id !== id));
        } catch (err) {
            console.error("Không thể xóa thông báo:", err);
        }
    };

    const fetchNotifications = async () => {
        try {
            const res = await authApis().get(USER_ENDPOINTS.NOTIFICATIONS);
            const data = res.data || [];
            const sortedData = data.sort((a, b) => {
                const timeA = a.createdAt || a.id;
                const timeB = b.createdAt || b.id;
                return timeB > timeA ? 1 : -1;
            });
            setNotifications(sortedData);
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
            isRead: false,
            path,
            createdAt: new Date().toISOString() // Lưu thời gian hiện tại khi nhận real-time để tính "phút trước"
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
            await authApis().post(`secure/notifications/${noti.id}/read`);
            const targetPath = noti.path || noti.click_action || noti.data?.click_action;

            if (targetPath) {
                navigate(targetPath);
            }
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
                    <Bell size={24} className="notification-bell" />
                    {unreadCount > 0 && (
                        <Badge pill bg="danger" className="notification-badge">
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
                        <button className="mark-read-btn" onClick={markAllAsRead} title="Đánh dấu tất cả là đã đọc">
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
                                    {getNotificationIcon(noti)}

                                    <div className="notification-text-wrapper">
                                        <div className="notification-text-title">
                                            {noti.title || 'Thông báo'}
                                        </div>
                                        <div className="notification-text-body">
                                            {noti.content || noti.body || 'Bạn có một thông báo mới'}
                                        </div>
                                        <div className="notification-text-time">
                                            {/* SỬ DỤNG HÀM TÍNH THỜI GIAN TƯƠNG ĐỐI TẠI ĐÂY */}
                                            {formatRelativeTime(noti.createdAt)}
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
                        <button className="view-all-btn" onClick={() => onNavigate ? onNavigate('/patient/notifications') : navigate('/patient/notifications')}>
                            Xem tất cả
                        </button>
                    </div>
                )}
            </div>
        </NavDropdown>
    );
};

export default NotificationBox;