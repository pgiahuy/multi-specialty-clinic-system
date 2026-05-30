import { Container, ListGroup, Badge, Spinner, Button } from "react-bootstrap";
import { useEffect, useState } from "react";
import { authApis, endpoint, USER_ENDPOINTS } from "../../configs/Apis";
import cookies from 'react-cookies';
import { Bell, CircleFill } from "react-bootstrap-icons";
import Header from "../../components/Header";

const AllNotifications = () => {
    const [notifications, setNotifications] = useState([]);
    const [loading, setLoading] = useState(true);
    const [page, setPage] = useState(1);

    const fetchAllNotifications = async () => {
        try {
            const res = await authApis().get(USER_ENDPOINTS.NOTIFICATIONS, {
                params: { page: page, page_size: 20 }
            });

            if (page === 1) setNotifications(res.data);
            else setNotifications(prev => [...prev, ...res.data]);

            setLoading(false);
        } catch (err) {
            console.error("Lỗi tải thông báo:", err);
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchAllNotifications();
    }, [page]);

    return (
        <>
            <Header />
            <Container className="mt-4" style={{ width: '60%' }}>
                <h3 className="mb-4 text-center" >Thông báo của tôi</h3>
                <ListGroup>
                    {notifications.map(noti => (
                        <ListGroup.Item
                            key={noti.id}
                            className={`d-flex align-items-center p-3 ${!noti.isRead ? 'bg-light' : ''}`}
                        >
                            <div className="bg-primary bg-opacity-10 p-3 rounded-circle text-primary me-3">
                                <Bell size={20} />
                            </div>
                            <div className="flex-grow-1">
                                <div className="d-flex justify-content-between">
                                    <h5 className={!noti.isRead ? 'fw-bold' : ''}>{noti.title}</h5>
                                    <small className="text-muted">
                                        {noti.createdAt ? new Date(noti.createdAt).toLocaleString('vi-VN') : 'Vừa xong'}
                                    </small>
                                </div>
                                <p className="mb-0 text-secondary">{noti.content}</p>
                            </div>
                            {!noti.isRead && (
                                <CircleFill size={10} className="text-primary ms-3" />
                            )}
                        </ListGroup.Item>
                    ))}
                </ListGroup>

                {loading && <div className="text-center mt-3"><Spinner animation="border" /></div>}

                {!loading && notifications.length >= 20 && (
                    <div className="text-center mt-4 mb-5">
                        <Button variant="outline-primary" onClick={() => setPage(page + 1)}>
                            Xem thêm
                        </Button>
                    </div>
                )}
            </Container>
        </>
    );
};

export default AllNotifications;