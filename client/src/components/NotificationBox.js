import { NavDropdown, Badge, ListGroup, Stack } from 'react-bootstrap';
import { Bell, CircleFill, Check2All, Trash2 } from 'react-bootstrap-icons';
import './NotificationBox.css';

const NotificationBox = ({ notifications }) => {

    const unreadCount = notifications?.filter(n => !n.isRead).length || 0;

    const markAllAsRead = () => {
        console.log("Đánh dấu tất cả là đã đọc");
    };

    const deleteNotification = (id) => {
        console.log("Xóa thông báo:", id);
    };

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
                        <button className="view-all-btn">
                            Xem tất cả thông báo
                        </button>
                    </div>
                )}
            </div>
        </NavDropdown>
    );
};

export default NotificationBox;