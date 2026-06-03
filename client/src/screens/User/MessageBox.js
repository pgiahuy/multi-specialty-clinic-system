import React, { useState, useEffect, useRef } from 'react';
import { Card, ListGroup, Form, InputGroup, Button, Spinner } from 'react-bootstrap';
import { Search, Send, ChatDots, ChevronDown } from 'react-bootstrap-icons';
import { authApis, CHAT_ENDPOINTS, CLINIC_ENDPOINTS, USER_ENDPOINTS } from '../../configs/Apis';
import { db } from '../../configs/firebaseConfig';
import { ref, query, orderByChild, onValue, off } from 'firebase/database';
import Header from '../../components/Header';
import { useNavigate } from 'react-router-dom';

const MessageBox = () => {
    const [patientProfiles, setPatientProfiles] = useState([]);
    const [selectedProfileId, setSelectedProfileId] = useState(null);
    const [conversations, setConversations] = useState([]);
    const [activeChat, setActiveChat] = useState(null);
    const [messages, setMessages] = useState([]);
    const [inputText, setInputText] = useState('');
    const [loadingProfiles, setLoadingProfiles] = useState(true);
    const [loadingConversations, setLoadingConversations] = useState(false);
    const [loadingMessages, setLoadingMessages] = useState(false);

    const [searchTerm, setSearchTerm] = useState('');
    const [doctorSearchResults, setDoctorSearchResults] = useState([]);

    const unsubscribeRef = useRef(null);
    const chatBodyRef = useRef(null);
    const shouldAutoScrollRef = useRef(false);
    const [showScrollBtn, setShowScrollBtn] = useState(false);
    const navigate = useNavigate();

    const loadConversations = async (profileId) => {
        setLoadingConversations(true);
        setConversations([]);
        setActiveChat(null);
        setMessages([]);
        try {
            if (!profileId) {
                setConversations([]);
                return;
            }
            const res = await authApis().get(CHAT_ENDPOINTS.CONVERSATIONS, {
                params: { patientProfileId: profileId }
            });
            const mapped = (res.data || []).map(item => ({
                id: item.id != null ? String(item.id) : null,
                doctorId: item.doctor_id != null ? String(item.doctor_id)
                    : item.doctorId != null ? String(item.doctorId)
                        : item.receiverId?.id != null ? String(item.receiverId.id)
                            : item.receiverId != null ? String(item.receiverId)
                                : null,
                doctor_name: item.doctor_name || item.receiverName || 'Bác sĩ',
                appointment_id: item.appointment_id != null ? String(item.appointment_id) : item.appointmentId ? String(item.appointmentId.id) : null,
                is_active: item.is_active != null ? item.is_active : item.isActive,
            }));
            setConversations(mapped);
            if (mapped.length > 0) {
                setActiveChat(mapped[0]);
            }
        } catch (err) {
            console.error('Lỗi khi tải danh sách cuộc trò chuyện:', err);
        } finally {
            setLoadingConversations(false);
        }
    };



    const handleSearchDoctors = async (query) => {
        const trimmed = query?.trim();
        if (!trimmed) {
            setDoctorSearchResults([]);
            return;
        }

        try {
            const res = await authApis().get(CLINIC_ENDPOINTS.DOCTORS, {
                params: {
                    doctorName: trimmed,
                },
            });
            setDoctorSearchResults(res.data || []);
        } catch (err) {
            console.error('Lỗi khi tìm kiếm bác sĩ:', err);
            setDoctorSearchResults([]);
        }
    };

    const handleStartConversation = async (doctor) => {
        if (!doctor || !selectedProfileId) return;
        const doctorId = doctor.id || doctor.uid || doctor.userId || doctor.doctor_id;
        if (!doctorId) return;

        try {
            const res = await authApis().post(CHAT_ENDPOINTS.START_CONVERSATION, {
                doctorId,
                patientId: Number(selectedProfileId)
            });

            const responseData = res.data || {};
            const conversationId = responseData.id ? String(responseData.id) : null;
            if (!conversationId) {
                return;
            }

            const createdConvo = {
                id: conversationId,
                doctorId,
                doctor_name: doctor.fullName || doctor.name || doctor.doctor_name || responseData.receiverUsername || 'Bác sĩ',
                appointment_id: responseData.appointmentId != null ? String(responseData.appointmentId) : null,
                is_active: responseData.isActive,
            };

            const existing = conversations.find(c => String(c.id) === conversationId);
            if (existing) {
                setActiveChat(existing);
            } else {
                setConversations(prev => [createdConvo, ...prev]);
                setActiveChat(createdConvo);
            }

            setSearchTerm('');
            setDoctorSearchResults([]);
        } catch (err) {
            console.error('Lỗi khi khởi tạo cuộc trò chuyện mới:', err);
        }
    };

    useEffect(() => {
        const delayDebounce = setTimeout(() => {
            handleSearchDoctors(searchTerm);
        }, 300);

        return () => clearTimeout(delayDebounce);
    }, [searchTerm]);


    const loadPatientProfiles = async () => {
        setLoadingProfiles(true);
        try {
            const res = await authApis().get(USER_ENDPOINTS.PATIENT_PROFILES);
            const profiles = Array.isArray(res.data) ? res.data : [];
            setPatientProfiles(profiles);
            if (profiles.length > 0) {
                const defaultProfileId = String(profiles[0].id);
                setSelectedProfileId(defaultProfileId);
            }
        } catch (err) {
            console.error('Lỗi khi tải danh sách hồ sơ bệnh nhân:', err);
        } finally {
            setLoadingProfiles(false);
        }
    };

    useEffect(() => {
        loadPatientProfiles();
    }, []);

    useEffect(() => {
        if (selectedProfileId) {
            loadConversations(selectedProfileId);
        }
    }, [selectedProfileId]);

    useEffect(() => {
        if (unsubscribeRef.current) {
            unsubscribeRef.current();
            unsubscribeRef.current = null;
        }

        if (!activeChat || !activeChat.id) {
            setMessages([]);
            return;
        }

        setLoadingMessages(true);
        const messagesRef = query(ref(db, `chat_messages/${activeChat.id}`), orderByChild('createdAt'));

        const onDataChange = (snapshot) => {
            const loadedMessages = [];
            snapshot.forEach(child => {
                const data = child.val();
                loadedMessages.push({
                    id: String(child.key),
                    sender_id: data.senderId,
                    sender_type: data.senderType,
                    content: data.content,
                    created_at: data.createdAt ? new Date(data.createdAt).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' }) : ''
                });
            });
            setMessages(loadedMessages);
            setLoadingMessages(false);
        };

        const onError = (err) => {
            console.error('Lỗi realtime Realtime Database:', err);
            setLoadingMessages(false);
        };

        onValue(messagesRef, onDataChange, onError);
        unsubscribeRef.current = () => off(messagesRef, 'value', onDataChange);
        unsubscribeRef.current = () => off(messagesRef, 'value', onDataChange);

        return () => {
            if (unsubscribeRef.current) {
                unsubscribeRef.current();
                unsubscribeRef.current = null;
            }
        };
    }, [activeChat]);

    useEffect(() => {

        if (!chatBodyRef.current) return;
        const el = chatBodyRef.current;
        const nearBottom = el.scrollHeight - (el.scrollTop + el.clientHeight) < 5;

        if (shouldAutoScrollRef.current || nearBottom) {

            requestAnimationFrame(() => {
                setTimeout(() => {
                    if (!chatBodyRef.current) return;
                    chatBodyRef.current.scrollTo({ top: chatBodyRef.current.scrollHeight, behavior: 'smooth' });
                    shouldAutoScrollRef.current = false;
                    setShowScrollBtn(false);
                }, 50);
            });
        }
    }, [messages]);

    useEffect(() => {

        shouldAutoScrollRef.current = true;
        setShowScrollBtn(false);
    }, [activeChat]);

    const handleScroll = (e) => {
        const el = e.target;
        const isNearBottom = el.scrollHeight - (el.scrollTop + el.clientHeight) < 5;
        setShowScrollBtn(!isNearBottom);
    };

    const scrollToBottom = () => {
        if (!chatBodyRef.current) return;
        chatBodyRef.current.scrollTo({ top: chatBodyRef.current.scrollHeight, behavior: 'smooth' });
        setShowScrollBtn(false);
    };

    const handleSelectChat = (chat) => {
        setActiveChat(chat);
    };

    const filteredConversations = conversations.filter(conv =>
        !searchTerm.trim() || conv.doctor_name?.toLowerCase().includes(searchTerm.toLowerCase())
    );

    const handleSendMessage = async (e) => {
        e.preventDefault();
        if (!inputText.trim() || !activeChat || !activeChat.id) return;

        const text = inputText.trim();
        shouldAutoScrollRef.current = true;
        setInputText('');

        try {
            await authApis().post(CHAT_ENDPOINTS.SEND_MESSAGE, {
                conversation_id: activeChat.id,
                content: text,
                message_type: 'TEXT'
            });
        } catch (err) {
            console.error('Lỗi gửi tin nhắn:', err);
        }
    };

    return (
        <div className="d-flex flex-column bg-light" style={{ height: "100vh", overflow: "hidden" }}>
            <Header />
            <div className="d-flex flex-column bg-light mx-auto h-100" style={{ overflow: "hidden", padding: '1rem', width: '80%' }}>

                <div className="d-flex gap-3 h-100" style={{ minHeight: '100%' }}>
                    <Card style={{ width: '350px', minWidth: '250px', backgroundColor: '#d5dbe1' }}
                        className=" border-0 shadow-sm rounded-4 d-flex flex-column h-100">

                        <div className="mb-3 p-3 w-100 bg-white border">
                            <Form.Label className="small text-secondary mb-1">Chọn hồ sơ bệnh nhân</Form.Label>
                            <Form.Select
                                value={selectedProfileId || ''}
                                onChange={(e) => setSelectedProfileId(e.target.value)}
                                disabled={loadingProfiles || patientProfiles.length === 0}
                            >
                                {patientProfiles.length === 0 ? (
                                    <option value="">Không có hồ sơ</option>
                                ) : (
                                    patientProfiles.map(profile => (
                                        <option key={profile.id} value={profile.id}>
                                            {profile.fullName}
                                        </option>
                                    ))
                                )}
                            </Form.Select>
                        </div>

                        <div className="p-2 border bg-white d-flex flex-column gap-2">
                            <InputGroup size="sm">
                                <InputGroup.Text className="bg-transparent border-0 text-muted">
                                    <Search size={14} />
                                </InputGroup.Text>
                                <Form.Control
                                    placeholder="Tìm bác sĩ..."
                                    className="border-0 shadow-none"
                                    value={searchTerm}
                                    onChange={(e) => setSearchTerm(e.target.value)}
                                />
                            </InputGroup>

                            {searchTerm.trim() && (
                                <div className="border rounded-3 bg-white shadow-sm overflow-hidden" style={{ maxHeight: 240, overflowY: 'auto' }}>
                                    {doctorSearchResults.length > 0 ? (
                                        doctorSearchResults.map((doctor) => (
                                            <div
                                                key={doctor.id || doctor.uid || doctor.userId || doctor.name}
                                                className="px-3 py-2 border-bottom"
                                                style={{ cursor: 'pointer' }}
                                                onClick={() => handleStartConversation(doctor)}
                                            >
                                                <div className="fw-bold small text-truncate">{doctor.fullName || doctor.name || doctor.doctor_name || doctor.username}</div>
                                                {doctor.specialty && <div className="text-muted xsmall">{doctor.specialty}</div>}
                                            </div>
                                        ))
                                    ) : (
                                        <div className="px-3 py-2 text-muted small">Không tìm thấy bác sĩ.</div>
                                    )}
                                </div>
                            )}
                        </div>
                        <ListGroup variant="flush" className="overflow-y-auto h-100">
                            {loadingConversations ? (
                                <div className="p-4 text-center text-secondary">
                                    <Spinner animation="border" size="sm" /> Đang tải...
                                </div>
                            ) : (!selectedProfileId || patientProfiles.length === 0) ? (
                                <div className="p-4 text-center text-muted small">
                                    Vui lòng chọn hồ sơ bệnh nhân để xem cuộc trò chuyện.
                                </div>
                            ) : filteredConversations.length > 0 ? (
                                filteredConversations.map((conv, index) => {
                                    const selected = activeChat?.id === conv.id;
                                    return (
                                        <ListGroup.Item
                                            key={conv.id || `conv-${index}`}
                                            action
                                            active={selected}
                                            onClick={() => handleSelectChat(conv)}
                                            className={`d-flex align-items-center gap-3 border-top-0  border-bottom ${selected ? "bg-primary-subtle text-primary  border-primary" : ""}`}
                                        >
                                            <div className={`rounded-circle d-flex align-items-center justify-content-center text-white fw-bold ${selected ? 'bg-primary' : 'bg-secondary'}`} style={{ width: '42px', height: '42px' }}>
                                                {conv.doctor_name ? conv.doctor_name.split(' ').pop().substring(0, 2).toUpperCase() : 'BS'}
                                            </div>
                                            <div className="text-start overflow-hidden ">
                                                <div className="fw-bold small text-truncate">{conv.doctor_name || 'Bác sĩ'}</div>
                                                <div className="text-muted small text-truncate">{conv.appointment_id ? 'Tư vấn theo lịch hiẹn' : 'Tư vấn tổng quát'}</div>
                                            </div>
                                        </ListGroup.Item>
                                    );
                                })
                            ) : (
                                <div className="p-4 text-center text-muted small">
                                    <ChatDots size={24} className="mb-2" />
                                    <div>Không có cuộc trò chuyện.</div>
                                </div>
                            )}
                        </ListGroup>
                    </Card>

                    <Card className="shadow-sm border-0 flex-grow-1 d-flex flex-column bg-white" style={{ minHeight: '72vh' }}>
                        <div className="p-3 border-bottom d-flex align-items-center justify-content-between gap-3 flex-wrap">
                            <div>
                                <div className="text-secondary small">Bác sĩ</div>
                                <div className="fw-bold">{activeChat?.doctor_name || 'Chưa chọn bác sĩ'}</div>
                            </div>
                            <div className="d-flex align-items-center gap-2 flex-wrap justify-content-end">
                                <Button
                                    size="sm"
                                    variant="outline-primary"
                                    onClick={() => {
                                        const params = [];
                                        if (selectedProfileId) params.push(`patientId=${selectedProfileId}`);
                                        if (activeChat?.doctorId) params.push(`doctorId=${activeChat.doctorId}`);
                                        if (activeChat?.id) params.push(`conversationId=${activeChat.id}`);
                                        const q = params.length ? `?${params.join('&')}` : '';
                                        navigate(`/patient/booking${q}`);
                                    }}
                                >
                                    Đặt lịch ngay
                                </Button>
                            </div>
                        </div>

                        <div className="flex-grow-1 position-relative d-flex flex-column" style={{ minHeight: 0 }}>
                            <div ref={chatBodyRef} onScroll={handleScroll} className="flex-grow-1 overflow-y-auto p-3" style={{ backgroundColor: '#f8f9fa', minHeight: 0 }}>
                                {loadingMessages ? (
                                    <div className="text-center text-secondary py-5">
                                        <Spinner animation="border" size="sm" /> Đang tải tin nhắn...
                                    </div>
                                ) : activeChat ? (
                                    messages.length > 0 ? (
                                        messages.map(msg => {
                                            const isDoctor = msg.sender_type === 'ROLE_DOCTOR' || msg.sender_type === 'DOCTOR';
                                            return (
                                                <div key={msg.id} className={`d-flex ${isDoctor ? 'justify-content-start' : 'justify-content-end'}`}>
                                                    <div className={`mb-3 p-3 rounded-3 ${isDoctor ? 'bg-white text-dark border' : 'bg-primary text-white'}`} style={{ maxWidth: '72%' }}>
                                                        {msg.content}
                                                        <div className="text-end text-muted" style={{ fontSize: '10px' }}>{msg.created_at}</div>
                                                    </div>
                                                </div>
                                            );
                                        })
                                    ) : (
                                        <div className="text-center text-muted py-5 small">
                                            Chưa có tin nhắn cũ. Hãy gửi tin nhắn đầu tiên tới bác sĩ.
                                        </div>
                                    )
                                ) : (
                                    <div className="text-center text-muted py-5 small">
                                        Vui lòng chọn cuộc trò chuyện ở bên trái để xem chi tiết.
                                    </div>
                                )}
                            </div>
                            {showScrollBtn && (
                                <Button
                                    variant="primary"
                                    size="sm"
                                    className="position-absolute bottom-0 start-50 translate-middle-x mb-3 rounded-circle shadow"
                                    style={{
                                        zIndex: 10, width: "36px",
                                        height: "36px"
                                    }}
                                    onClick={scrollToBottom}
                                >
                                    <ChevronDown size={16} />
                                </Button>
                            )}
                        </div>

                        <div className="p-3 border-top bg-white">
                            <Form onSubmit={handleSendMessage}>
                                <InputGroup>
                                    <Form.Control
                                        placeholder={activeChat ? 'Nhập tin nhắn...' : 'Chọn cuộc trò chuyện để bắt đầu'}
                                        value={inputText}
                                        onChange={(e) => setInputText(e.target.value)}
                                        disabled={!activeChat}
                                    />
                                    <Button type="submit" variant="primary" disabled={!activeChat || !inputText.trim()}>
                                        <Send size={14} className="me-1" /> Gửi
                                    </Button>
                                </InputGroup>
                            </Form>
                        </div>
                    </Card>
                </div>
            </div>

        </div>
    );
};

export default MessageBox;