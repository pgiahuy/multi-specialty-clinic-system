import React, { useState, useEffect, useRef } from "react";
import { useSearchParams } from "react-router-dom";
import { Card, ListGroup, Form, InputGroup, Button, Spinner } from "react-bootstrap";
import { Search, Send, FileText, Person, ChatDots, CircleFill, ChevronDown } from "react-bootstrap-icons";
import { authApis, CHAT_ENDPOINTS, CLINIC_ENDPOINTS } from "../../configs/Apis";
import Header from "../../components/Header";
import MySpinner from "../../components/MySpinner";
import { db } from '../../configs/firebaseConfig';
import { ref, query as dbQuery, orderByChild, onValue, off } from 'firebase/database';


const ConsultationPage = () => {

    const [searchParams, setSearchParams] = useSearchParams();

    const [conversations, setConversations] = useState([]);
    const [allPatients, setAllPatients] = useState([]);
    const [activeChat, setActiveChat] = useState(null);
    const [messages, setMessages] = useState([]);
    const [loadingMessages, setLoadingMessages] = useState(false);

    const [inputText, setInputText] = useState("");
    const [searchTerm, setSearchTerm] = useState("");
    const [showRightCol, setShowRightCol] = useState(true);
    const [debouncedSearchTerm, setDebouncedSearchTerm] = useState("");
    const [latestMedicalRecord, setLatestMedicalRecord] = useState(null);
    const [medicalRecordLoading, setMedicalRecordLoading] = useState(false);

    const chatBodyRef = useRef(null);
    const unsubscribeRef = useRef(null);
    const debounceTimerRef = useRef(null);
    const shouldAutoScrollRef = useRef(false);
    const [showScrollBtn, setShowScrollBtn] = useState(false);

    useEffect(() => {
        const loadConversations = async () => {
            try {
                const resConvo = await authApis().get(CHAT_ENDPOINTS.CONVERSATIONS);

                const mappedConversations = (resConvo.data || []).map(item => {

                    if (Array.isArray(item)) {
                        return {
                            id: String(item[0]),
                            patient_id: String(item[1]),
                            receiver_id: String(item[2]),
                            appointment_id: item[3] ? String(item[3]) : null,
                            is_active: item[4],
                            patient_name: item[5]
                        };
                    }

                    return {
                        id: item.id != null ? String(item.id) : null,
                        patient_id: item.patient_id != null ? String(item.patient_id) : (item.patientId ? String(item.patientId.id) : null),
                        receiver_id: item.receiver_id != null ? String(item.receiver_id) : (item.receiverId ? String(item.receiverId.id) : null),
                        appointment_id: item.appointment_id != null ? String(item.appointment_id) : (item.appointmentId ? String(item.appointmentId.id) : null),
                        is_active: item.is_active != null ? item.is_active : item.isActive,
                        patient_name: item.patient_name || (item.patientId ? item.patientId.fullName : null)
                    };
                });
                setConversations(mappedConversations);



            } catch (err) {
                console.error("Lỗi lấy dữ liệu khởi tạo từ Server:", err);
            }
        };
        loadConversations();
    }, []);



    useEffect(() => {
        const loadAllPatients = async () => {
            try {
                const res = await authApis().get(CLINIC_ENDPOINTS.DOCTOR_GET_PATIENTS);
                const mappedPatients = (res.data || []).map(p => ({
                    ...p,
                    name: p.fullName || p.name,
                    patient_id: p.id,
                    patient_name: p.fullName || p.name
                }));
                setAllPatients(mappedPatients);
            } catch (err) {
                console.error("Lỗi lấy danh sách bệnh nhân:", err);
            }
        };
        loadAllPatients();
    }, []);

    const getFilteredResults = () => {
        if (!debouncedSearchTerm.trim()) {
            return [];
        }
        const matchedPatients = allPatients.filter(p =>
            (p.name || "").toLowerCase().includes(debouncedSearchTerm.toLowerCase())
        );

        return matchedPatients.map(patient => {
            const existingConvo = conversations.find(c => String(c.patient_id) === String(patient.patient_id || patient.id));
            if (existingConvo) {
                return existingConvo;
            }
            return {
                id: null,
                patient_id: patient.patient_id || patient.id,
                patient_name: patient.patient_name || patient.name,
                appointment_id: null,
                is_active: true
            };
        });
    };

    useEffect(() => {
        if (debounceTimerRef.current) {
            clearTimeout(debounceTimerRef.current);
        }

        debounceTimerRef.current = setTimeout(() => {
            setDebouncedSearchTerm(searchTerm);
        }, 300);

        return () => {
            if (debounceTimerRef.current) {
                clearTimeout(debounceTimerRef.current);
            }
        };
    }, [searchTerm]);


    const loadRecentMedicalRecordByPatientId = async (patientId) => {
        if (!patientId) return null;

        try {
            const res = await authApis().get(CLINIC_ENDPOINTS.MEDICAL_RECORD_BY_PATIENT_ID(patientId));
            const records = Array.isArray(res.data) ? res.data : [];
            if (records.length === 0) return null;

            return records.slice().sort((a, b) => {
                const aTime = a.createdAt ? new Date(a.createdAt).getTime() : 0;
                const bTime = b.createdAt ? new Date(b.createdAt).getTime() : 0;
                if (bTime !== aTime) return bTime - aTime;
                return (b.id || 0) - (a.id || 0);
            })[0] || null;
        } catch (err) {
            console.error("Lỗi lấy bệnh án gần nhất của bệnh nhân:", err);
            return null;
        }
    };


    const handleSelectChat = async (chatItem) => {
        setSearchTerm("");
        setLatestMedicalRecord(null);
        setMedicalRecordLoading(true);
        setShowRightCol(true);

        try {
            const recentRecord = await loadRecentMedicalRecordByPatientId(chatItem.patient_id || chatItem.patientId || chatItem.id);
            setLatestMedicalRecord(recentRecord);
        } finally {
            setMedicalRecordLoading(false);
        }

        const existingConvo = conversations.find(c => String(c.patient_id) === String(chatItem.patient_id));
        if (existingConvo && existingConvo.id) {
            setSearchParams({ conversationId: existingConvo.id });
            return;
        }

        if (chatItem.id) {
            setSearchParams({ conversationId: chatItem.id });
            return;
        }

        try {
            const res = await authApis().post(CHAT_ENDPOINTS.START_CONVERSATION, {
                patientId: chatItem.patient_id
            });

            const createdConvo = {
                id: String(res.data.id),
                patient_id: String(res.data.patientId.id),
                receiver_id: String(res.data.receiverId.id),
                appointment_id: null,
                is_active: res.data.isActive,
                patient_name: chatItem.patient_name
            };

            setConversations(prev => [createdConvo, ...prev]);
            setSearchParams({ conversationId: createdConvo.id });

        } catch (err) {
            console.error("Lỗi không thể khởi tạo cuộc trò chuyện mới ngầm:", err);
        }
    };

    useEffect(() => {
        const convoId = searchParams.get("conversationId");

        if (convoId) {
            const chat = conversations.find(c => String(c.id) === String(convoId));
            if (chat) {
                setActiveChat(chat);
            }
        } else if (conversations.length > 0) {
            setSearchParams({ conversationId: conversations[0].id });
        }
    }, [searchParams, conversations]);

    const formatChatTimestamp = (timestamp) => {
        if (!timestamp) return "";
        const parsed = typeof timestamp === 'number' || /^[0-9]+$/.test(String(timestamp))
            ? new Date(Number(timestamp))
            : new Date(String(timestamp));

        if (!isNaN(parsed.getTime())) {
            return parsed.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });
        }
        return String(timestamp);
    };

    useEffect(() => {
        if (unsubscribeRef.current) {
            unsubscribeRef.current();
            unsubscribeRef.current = null;
        }

        if (!activeChat?.id) {
            setMessages([]);
            return;
        }

        setLoadingMessages(true);
        const messagesRef = dbQuery(ref(db, `chat_messages/${activeChat.id}`), orderByChild('createdAt'));

        const onDataChange = (snapshot) => {
            const loadedMessages = [];
            snapshot.forEach(child => {
                const data = child.val();
                loadedMessages.push({
                    id: String(child.key),
                    sender_id: data.senderId,
                    sender_type: data.senderType,
                    content: data.content,
                    created_at: formatChatTimestamp(data.createdAt),
                    raw_timestamp: data.createdAt
                });
            });

            loadedMessages.sort((a, b) => {
                const aTime = a.raw_timestamp ? new Date(a.raw_timestamp).getTime() : 0;
                const bTime = b.raw_timestamp ? new Date(b.raw_timestamp).getTime() : 0;
                return aTime - bTime;
            });

            setMessages(loadedMessages);
            setLoadingMessages(false);
        };

        const onError = (err) => {
            console.error('Lỗi realtime Realtime Database trên doctor:', err);
            setLoadingMessages(false);
        };

        onValue(messagesRef, onDataChange, onError);
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

    const handleSendMessage = async (e) => {
        e.preventDefault();
        if (!inputText.trim() || !activeChat) return;

        const currentText = inputText;
        setInputText("");
        shouldAutoScrollRef.current = true;

        try {
            const res = await authApis().post(CHAT_ENDPOINTS.SEND_MESSAGE, {
                conversation_id: String(activeChat.id),
                content: currentText,
                message_type: "TEXT",
                sender_type: "DOCTOR"
            });

        } catch (error) {
            console.error("Lỗi không thể gửi tin nhắn:", error);
        }
    };

    const activePatient = activeChat ? allPatients.find(p => String(p.patient_id || p.id) === String(activeChat.patient_id)) : null;
    const patientDob = activePatient?.dob || activePatient?.dateOfBirth || latestMedicalRecord?.dob || "";
    const patientGender = activePatient?.gender || latestMedicalRecord?.gender || "";
    const patientAddress = activePatient?.address || latestMedicalRecord?.address || "";
    const patientPhone = activePatient?.phone || activePatient?.phoneNumber || "";

    const formatDate = (value) => {
        if (!value) return "";
        const parsed = new Date(value);
        return !isNaN(parsed.getTime()) ? parsed.toLocaleDateString() : String(value);
    };

    return (
        <div className="d-flex flex-column bg-light" style={{ height: "100vh", overflow: "hidden" }}>
            <Header />

            <div className="flex-grow-1 d-flex p-3 gap-3" style={{ overflow: "hidden" }}>

                <div style={{ width: "320px", minWidth: "320px" }} className="d-flex flex-column h-100">
                    <Card className="h-100 border-0 shadow-sm rounded-3 overflow-hidden d-flex flex-column bg-white">

                        <div className="p-3 border-bottom bg-white position-relative">
                            <InputGroup size="sm" className="bg-light rounded-pill border-0 overflow-hidden px-2">
                                <InputGroup.Text className="bg-transparent border-0 text-muted">
                                    <Search size={14} />
                                </InputGroup.Text>
                                <Form.Control
                                    type="text"
                                    placeholder="Tìm bệnh nhân..."
                                    className="bg-transparent border-0 shadow-none px-1 py-2 small"
                                    style={{ fontSize: "13px" }}
                                    value={searchTerm}
                                    onChange={(e) => setSearchTerm(e.target.value)}
                                />
                            </InputGroup>

                            {debouncedSearchTerm.trim() && getFilteredResults().length > 0 && (
                                <div className="position-absolute start-0 end-0 top-100 mt-2 bg-white border rounded-2 shadow-lg" style={{ zIndex: 1000, maxHeight: "300px", overflow: "auto", left: "12px", right: "12px" }}>
                                    <ListGroup variant="flush">
                                        {getFilteredResults().map((patient, index) => (
                                            <ListGroup.Item
                                                key={patient.id || `search-patient-${index}`}
                                                action
                                                as="button"
                                                className="p-3 border-0 d-flex align-items-center"
                                                onClick={() => handleSelectChat(patient)}
                                            >
                                                <div className="rounded-circle d-flex align-items-center justify-content-center text-white fw-bold small bg-primary" style={{ width: "32px", height: "32px", minWidth: "32px" }}>
                                                    {patient.patient_name ? patient.patient_name.split(" ").pop().substring(0, 2).toUpperCase() : "BN"}
                                                </div>
                                                <div className="ms-3 text-start overflow-hidden flex-grow-1">
                                                    <div className="fw-bold text-dark small text-truncate">{patient.patient_name}</div>

                                                </div>
                                            </ListGroup.Item>
                                        ))}
                                    </ListGroup>
                                </div>
                            )}
                        </div>

                        <ListGroup variant="flush" className="overflow-y-auto flex-grow-1">
                            {conversations.length > 0 ? (
                                conversations.map((c, index) => {
                                    const isSelected = activeChat?.id === c.id && c.id !== null;
                                    return (
                                        <ListGroup.Item
                                            key={c.id || `conversation-${index}`}
                                            action
                                            as="button"
                                            className={`p-3 border-bottom d-flex align-items-center transition-all ${isSelected ? "bg-primary-subtle text-primary  border-primary" : ""}`}
                                            onClick={() => handleSelectChat(c)}
                                        >
                                            <div className={`rounded-circle d-flex align-items-center justify-content-center text-white fw-bold small ${isSelected ? "bg-primary" : "bg-secondary"}`} style={{ width: "40px", height: "40px", minWidth: "40px" }}>
                                                {c.patient_name ? c.patient_name.split(" ").pop().substring(0, 2).toUpperCase() : "BN"}
                                            </div>
                                            <div className="ms-3 text-start overflow-hidden w-100">
                                                <div className="fw-bold text-dark small text-truncate">{c.patient_name}</div>
                                                <div className="text-muted text-truncate mt-1" style={{ fontSize: "11px" }}>
                                                    Tư vấn trực tuyến
                                                </div>
                                            </div>
                                        </ListGroup.Item>
                                    );
                                })
                            ) : (
                                <div className="text-center py-5 text-muted small">
                                    <ChatDots size={24} className="mb-2 text-black-50" />
                                    <div>Không có cuộc trò chuyện</div>
                                </div>
                            )}
                        </ListGroup>
                    </Card>
                </div>

                <div className="flex-grow-1 d-flex flex-column h-100">
                    {activeChat ? (

                        <Card className="h-100 border-0 shadow-sm rounded-3 overflow-hidden d-flex flex-column bg-white position-relative">

                            <div className="p-3 border-bottom bg-white d-flex align-items-center justify-content-between" style={{ height: "60px" }}>
                                <div className="d-flex align-items-center overflow-hidden">
                                    <div className="position-relative">
                                        <div className="bg-primary-subtle text-primary rounded-circle d-flex align-items-center justify-content-center fw-bold small" style={{ width: "38px", height: "38px" }}>BN</div>
                                        <CircleFill size={9} className="position-absolute bottom-0 end-0 text-success border border-white rounded-circle" />
                                    </div>
                                    <div className="ms-3 overflow-hidden">
                                        <h6 className="mb-0 fw-bold text-dark small text-truncate">{activeChat.patient_name}</h6>
                                    </div>
                                </div>
                                {!showRightCol && (
                                    <Button size="sm" variant="outline-primary" className="fw-bold px-3 border-2 rounded-2 d-flex align-items-center gap-1" style={{ fontSize: "12px" }} onClick={() => setShowRightCol(true)}>
                                        Xem Bệnh Án
                                    </Button>
                                )}
                            </div>

                            <div ref={chatBodyRef} onScroll={handleScroll} className="p-3 flex-grow-1 overflow-y-auto" style={{ backgroundColor: "#f8f9fa" }}>
                                <div className="d-flex flex-column gap-3 pb-4">
                                    {loadingMessages ? (
                                        <div className="text-center text-secondary small py-5 my-auto">
                                            <Spinner animation="border" size="sm" className="me-2" /> Đang tải tin nhắn...
                                        </div>
                                    ) : messages.length > 0 ? (
                                        messages.map((msg) => {
                                            const isDoc = msg.sender_type === "ROLE_DOCTOR" || msg.sender_type === "DOCTOR";
                                            return (
                                                <div key={msg.id} className={`d-flex ${isDoc ? "justify-content-end" : "justify-content-start"}`}>
                                                    <div className="d-flex flex-column" style={{ maxWidth: "75%", alignItems: isDoc ? 'flex-end' : 'flex-start' }}>
                                                        <div className={`p-3 rounded-3 shadow-xs small ${isDoc ? "bg-primary text-white rounded-bottom-end-0" : "bg-white text-dark border rounded-bottom-start-0"}`} style={{ lineHeight: "1.5" }}>
                                                            {msg.content}
                                                        </div>
                                                        <span className="text-muted mt-1" style={{ fontSize: "10px", textAlign: isDoc ? "right" : "left", width: '100%' }}>
                                                            {msg.created_at}
                                                        </span>
                                                    </div>
                                                </div>
                                            );
                                        })
                                    ) : (
                                        <div className="text-center text-muted small py-5 my-auto">
                                            <ChatDots size={32} className="mb-2 text-muted" />
                                            <p className="mb-0">Chưa có nội dung trò chuyện cũ.</p>
                                        </div>
                                    )}
                                </div>
                            </div>

                            {showScrollBtn && (
                                <Button
                                    variant="primary"
                                    size="sm"
                                    className="position-absolute start-50 translate-middle-x rounded-circle shadow d-flex align-items-center justify-content-center"
                                    style={{
                                        bottom: "80px",
                                        zIndex: 1050,
                                        width: "36px",
                                        height: "36px"
                                    }}
                                    onClick={scrollToBottom}
                                >
                                    <ChevronDown size={16} />
                                </Button>
                            )}

                            <div className="p-3 border-top bg-white" style={{ height: "70px" }}>
                                <Form onSubmit={handleSendMessage}>
                                    <InputGroup>
                                        <Form.Control
                                            type="text"
                                            placeholder={`Nhập nội dung tư vấn cho ${activeChat.patient_name}...`}
                                            className="px-3 border-end-0 bg-light shadow-none"
                                            style={{ fontSize: "13px" }}
                                            value={inputText}
                                            onChange={(e) => setInputText(e.target.value)}
                                        />
                                        <Button type="submit" variant="primary" className="px-4 d-flex align-items-center justify-content-center">
                                            <Send size={14} className="me-1" /> <strong className="small">GỬI</strong>
                                        </Button>
                                    </InputGroup>
                                </Form>
                            </div>
                        </Card>
                    ) : (
                        <Card className="h-100 border-0 shadow-sm rounded-3 d-flex flex-column align-items-center justify-content-center bg-white text-muted small">
                            <Person size={48} className="mb-2 text-black-50" />
                            Vui lòng chọn một cuộc trò chuyện.
                        </Card>
                    )}
                </div>

                {showRightCol && activeChat && (
                    <div style={{ width: "400px", minWidth: "300px" }} className="d-flex flex-column h-100">
                        <Card className="h-100 border-0 shadow-sm rounded-3 overflow-hidden d-flex flex-column bg-white animate-fade-in">
                            <div className="p-3 border-bottom bg-white d-flex align-items-center justify-content-between" style={{ height: "60px" }}>
                                <span className="fw-bold text-secondary small d-flex align-items-center gap-1">
                                    <FileText size={16} /> THÔNG TIN LÂM SÀNG
                                </span>
                                <Button size="sm" variant="link" className="text-muted text-decoration-none p-0 fw-bold small" onClick={() => setShowRightCol(false)}>
                                    Đóng
                                </Button>
                            </div>

                            <div className="p-3 overflow-y-auto flex-grow-1 small">
                                <div className="mb-3 p-4 bg-light rounded-3 border-primary">
                                    <div className="text-muted" style={{ fontSize: "11px" }}>Họ và tên</div>
                                    <div className="fw-bold text-dark">{activePatient?.patient_name || activeChat.patient_name}</div>

                                    <div className="text-muted mt-2" style={{ fontSize: "11px" }}>Mã định danh hệ thống</div>
                                    <div className="font-monospace text-secondary" style={{ fontSize: "12px" }}>{activeChat.patient_id}</div>

                                    <div className="d-flex gap-4flex-wrap align-items-center justify-content-start">
                                        <div>
                                            <div className="text-muted mt-2" style={{ fontSize: "11px" }}>Ngày sinh</div>
                                            <div className="text-dark">{patientDob ? formatDate(patientDob) : "Không có"}</div>
                                        </div>

                                        <div>
                                            <div className="text-muted mt-2" style={{ fontSize: "11px" }}>Giới tính</div>
                                            <div className="text-dark">{patientGender || "Không có"}</div>
                                        </div>
                                    </div>

                                    <div className="text-muted mt-2" style={{ fontSize: "11px" }}>Địa chỉ</div>
                                    <div className="text-dark">{patientAddress || "Không có"}</div>

                                    {patientPhone && (
                                        <>
                                            <div className="text-muted mt-2" style={{ fontSize: "11px" }}>Số điện thoại</div>
                                            <div className="text-dark">{patientPhone}</div>
                                        </>
                                    )}
                                </div>

                                <div className="mb-4">
                                    <h6 className="fw-bold text-dark border-bottom pb-2" style={{ fontSize: "12px" }}>Hồ sơ bệnh án gần nhất</h6>
                                    {medicalRecordLoading ? (
                                        <div className="d-flex align-items-center text-secondary small py-2">
                                            <MySpinner />
                                            <span>Đang tải hồ sơ bệnh án...</span>
                                        </div>
                                    ) : latestMedicalRecord ? (
                                        <div className="text-secondary" style={{ lineHeight: "1.6" }}>
                                            <div className="mb-2">
                                                <strong className="text-dark">Mã bệnh án:</strong> BA#{latestMedicalRecord.id}
                                            </div>
                                            <div className="mb-2">
                                                <strong className="text-dark">Ngày tạo:</strong> {latestMedicalRecord.createdAt || "Chưa cập nhật"}
                                            </div>
                                            <div className="mb-2">
                                                <strong className="text-dark">Chẩn đoán:</strong> {latestMedicalRecord.diagnosis || "Không có"}
                                            </div>
                                            <div>
                                                <strong className="text-dark">Ghi chú:</strong> {latestMedicalRecord.note || "Không có"}
                                            </div>
                                        </div>
                                    ) : (
                                        <div className="text-secondary small">Không có hồ sơ bệnh án gần nhất.</div>
                                    )}
                                </div>

                            </div>
                        </Card>
                    </div>
                )}
            </div>
        </div >
    );
};

export default ConsultationPage;