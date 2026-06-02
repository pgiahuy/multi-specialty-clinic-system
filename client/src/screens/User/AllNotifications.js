import { Container, ListGroup, Badge, Spinner, Button } from "react-bootstrap";
import { useEffect, useMemo, useState } from "react";
import { authApis, USER_ENDPOINTS } from "../../configs/Apis";
import { Bell, CircleFill, CurrencyDollar, CalendarCheck, Capsule, Trash2 } from "react-bootstrap-icons";
import Header from "../../components/Header";
import { useNavigate } from "react-router-dom";

const ICON_MAP = [
    {
        keywords: ['thanh toán', 'tiền'],
        icon: <CurrencyDollar size={18} />,
        bgClass: 'bg-success bg-opacity-10 text-success'
    },
    {
        keywords: ['lịch khám', 'lịch hẹn'],
        icon: <CalendarCheck size={18} />,
        bgClass: 'bg-primary bg-opacity-10 text-primary'
    },
    {
        keywords: ['đơn thuốc', 'thuốc'],
        icon: <Capsule size={18} />,
        bgClass: 'bg-warning bg-opacity-10 text-warning'
    }
];

const AllNotifications = () => {
    const [notifications, setNotifications] = useState([]);
    const [loading, setLoading] = useState(true);
    const [page, setPage] = useState(1);
    const [hasMore, setHasMore] = useState(true);
    const navigate = useNavigate();

    const renderNotificationIcon = (titleText) => {
        const title = titleText?.toLowerCase() || '';
        const matched = ICON_MAP.find(item => item.keywords.some(kw => title.includes(kw)));
        const icon = matched ? matched.icon : <Bell size={18} />;
        const bgClass = matched ? matched.bgClass : 'bg-secondary bg-opacity-10 text-secondary';

        return (
            <div className={`${bgClass} rounded-circle me-3 d-flex align-items-center justify-content-center flex-shrink-0`} style={{ width: '44px', height: '44px' }}>
                {icon}
            </div>
        );
    };

    const fetchAllNotifications = async () => {
        try {
            setLoading(true);
            const PAGE_SIZE = 8;
            const res = await authApis().get(USER_ENDPOINTS.NOTIFICATIONS, {
                params: { page: page, page_size: PAGE_SIZE }
            });

            const newData = res.data || [];

            if (page === 1) {
                setNotifications(newData);
            } else {
                setNotifications(prev => [...prev, ...newData]);
            }

            if (newData.length < PAGE_SIZE) {
                setHasMore(false);
            } else {
                setHasMore(true);
            }

            setLoading(false);
        } catch (err) {
            console.error("Lỗi tải thông báo:", err);
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchAllNotifications();
    }, [page]);

    const handleNotificationClick = async (noti) => {
        try {
            setNotifications(prev => prev.map(n => n.id === noti.id ? { ...n, isRead: true } : n));
            await authApis().post(`secure/notifications/${noti.id}/read`);
            const targetPath = noti.path || noti.click_action || noti.data?.click_action;

            if (targetPath) navigate(targetPath);
        } catch (err) {
            console.error('Error handling notification click:', err);
        }
    };

    const deleteNotification = async (id) => {
        try {
            await authApis().delete(`secure/notifications/${id}`);
            setNotifications(prev => prev.filter(n => n.id !== id));
        } catch (err) {
            console.error("Không thể xóa thông báo:", err);
        }
    };

    const sortedNotifications = useMemo(() => {
        const parseTime = (item) => {
            const time = new Date(item.createdAt).getTime();
            return Number.isFinite(time) ? time : 0;
        };

        return [...notifications].sort((a, b) => {
            const aUnread = !a.isRead;
            const bUnread = !b.isRead;

            if (aUnread !== bUnread) {
                return aUnread ? -1 : 1;
            }

            return parseTime(b) - parseTime(a);
        });
    }, [notifications]);

    return (
        <div className="bg-light min-vh-100" style={{ paddingBottom: '50px' }}>
            <Header />
            <Container className="mt-5" style={{ maxWidth: '650px' }}>
                <div className="d-flex justify-content-between align-items-center mb-4">
                    <h4 className="fw-bold m-0 text-dark">Thông báo của tôi</h4>
                    <Badge bg="primary" pill className="px-2 py-1.5 fs-7">
                        {notifications.filter(n => !n.isRead).length} Mới
                    </Badge>
                </div>

                <ListGroup className="border-0">
                    {sortedNotifications.map(noti => {
                        const isUnread = !noti.isRead;
                        return (
                            <ListGroup.Item
                                key={noti.id}
                                onClick={() => handleNotificationClick(noti)}
                                className="d-flex align-items-center p-3 mb-2 rounded-3 border shadow-sm"
                                style={{
                                    cursor: 'pointer',
                                    backgroundColor: isUnread ? '#f4f8ff' : '#ffffff',
                                    borderColor: isUnread ? '#e1ecfd' : '#f0f0f0',
                                    borderLeft: isUnread ? '4px solid #0d6efd' : '1px solid #f0f0f0',
                                    transition: 'all 0.2s ease'
                                }}
                                onMouseEnter={(e) => {
                                    e.currentTarget.style.transform = 'translateY(-1px)';
                                    e.currentTarget.style.boxShadow = '0 .5rem 1rem rgba(0,0,0,.08)';
                                }}
                                onMouseLeave={(e) => {
                                    e.currentTarget.style.transform = 'none';
                                    e.currentTarget.style.boxShadow = '0 .125rem .25rem rgba(0,0,0,.075)';
                                }}
                            >
                                {renderNotificationIcon(noti.title)}

                                <div className="flex-grow-1 pe-2">
                                    <div className="d-flex justify-content-between align-items-baseline mb-1">
                                        <h6 className={`m-0 text-dark ${isUnread ? 'fw-bold' : 'fw-semibold text-opacity-75'}`} style={{ fontSize: '0.95rem' }}>
                                            {noti.title}
                                        </h6>
                                        <small className="text-muted flex-shrink-0 ms-2" style={{ fontSize: '0.75rem' }}>
                                            {noti.createdAt || 'Vừa xong'}
                                        </small>
                                    </div>
                                    <p className="mb-0 text-secondary" style={{ fontSize: '0.85rem', lineHeight: '1.4' }}>
                                        {noti.content}
                                    </p>
                                </div>

                                <div className="d-flex align-items-center flex-shrink-0 ms-2">
                                    {isUnread && (
                                        <CircleFill size={8} className="text-primary me-2" />
                                    )}

                                    <button
                                        className="btn-delete-all-noti p-1 text-muted border-0 bg-transparent"
                                        style={{ transition: 'color 0.2s' }}
                                        onClick={(e) => {
                                            e.stopPropagation();
                                            deleteNotification(noti.id);
                                        }}
                                        onMouseEnter={(e) => e.currentTarget.style.color = '#dc3545'}
                                        onMouseLeave={(e) => e.currentTarget.style.color = '#6c757d'}
                                        title="Xóa thông báo"
                                    >
                                        <Trash2 size={16} />
                                    </button>
                                </div>
                            </ListGroup.Item>
                        );
                    })}
                </ListGroup>

                {loading && (
                    <div className="text-center mt-4">
                        <Spinner animation="border" variant="primary" size="sm" />
                    </div>
                )}

                {!loading && hasMore && notifications.length > 0 && (
                    <div className="text-center mt-4">
                        <Button
                            variant="white"
                            className="border shadow-sm text-primary fw-semibold px-4 btn-sm"
                            onClick={() => setPage(prev => prev + 1)}
                        >
                            Xem thêm thông báo
                        </Button>
                    </div>
                )}

                {!hasMore && notifications.length > 0 && (
                    <div className="text-center text-muted mt-4" style={{ fontSize: '0.8rem' }}>
                        Bạn đã xem hết tất cả thông báo.
                    </div>
                )}
            </Container>
        </div>
    );
};

export default AllNotifications;