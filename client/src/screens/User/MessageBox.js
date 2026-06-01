import React, { useState } from 'react';
import { Form, Button, InputGroup } from 'react-bootstrap';

const MessageBox = () => {
    // Giả lập dữ liệu cuộc trò chuyện công việc thực tế
    const [messages, setMessages] = useState([
        { id: 1, sender: 'patient', text: 'Chào Bác sĩ, kết quả xét nghiệm máu của em có sao không ạ?', time: '10:30' },
        { id: 2, sender: 'doctor', text: 'Chào bạn, các chỉ số đều bình thường. Tuy nhiên chỉ số đường huyết hơi cao một chút, bạn cần hạn chế ăn đồ ngọt nhé.', time: '10:32' },
        { id: 3, sender: 'patient', text: 'Dạ vâng, em cảm ơn bác sĩ. Em có cần uống thuốc gì thêm không ạ?', time: '10:33' },
    ]);
    const [inputText, setInputText] = useState('');

    const handleSendMessage = (e) => {
        e.preventDefault();
        if (!inputText.trim()) return;

        // Thêm tin nhắn mới của bác sĩ vào danh sách
        setMessages([
            ...messages,
            { id: Date.now(), sender: 'doctor', text: inputText, time: '10:35' }
        ]);
        setInputText('');
    };

    return (
        <div className="card border-0 shadow-sm rounded-3 overflow-hidden d-flex flex-column" style={{ height: '600px', backgroundColor: '#fff' }}>

            {/* 1. HEADER: Thông tin bệnh nhân đang tư vấn */}
            <div className="p-3 border-bottom bg-white d-flex justify-content-between align-items-center">
                <div className="d-flex align-items-center">
                    {/* Ảnh đại diện giả lập */}
                    <div className="bg-primary-subtle text-primary rounded-circle d-flex align-items-center justify-content-center fw-bold" style={{ width: '45px', height: '45px' }}>
                        BN
                    </div>
                    <div className="ms-3">
                        <h6 className="mb-0 fw-bold text-dark">Nguyễn Văn A</h6>
                        <span className="text-success small fw-semibold">● Đang trực tuyến</span>
                    </div>
                </div>
                {/* Nút tiện ích mở nhanh bệnh án */}
                <Button variant="outline-primary" size="sm" className="rounded-2 fw-semibold">
                    Xem bệnh án
                </Button>
            </div>

            {/* 2. BODY: Nội dung các tin nhắn (Có thanh cuộn tự động) */}
            <div className="p-3 flex-grow-1 overflow-y-auto bg-light" style={{ backgroundImage: 'linear-gradient(#f8f9fa, #e9ecef)' }}>
                <div className="d-flex flex-column gap-3">
                    {messages.map((msg) => {
                        const isDoctor = msg.sender === 'doctor';
                        return (
                            <div key={msg.id} className={`d-flex ${isDoctor ? 'justify-content-end' : 'justify-content-start'}`}>
                                <div className="d-flex flex-column" style={{ maxWidth: '75%' }}>
                                    {/* Khung bong bóng tin nhắn */}
                                    <div className={`p-3 rounded-3 shadow-sm ${isDoctor
                                        ? 'bg-primary text-white rounded-bottom-end-0'
                                        : 'bg-white text-dark rounded-bottom-start-0'
                                        }`}>
                                        <p className="mb-0 small">{msg.text}</p>
                                    </div>
                                    {/* Thời gian nhắn */}
                                    <span className={`text-muted mx-1 mt-1`} style={{ fontSize: '10px', textAlign: isDoctor ? 'right' : 'left' }}>
                                        {msg.time}
                                    </span>
                                </div>
                            </div>
                        );
                    })}
                </div>
            </div>

            {/* 3. FOOTER: Ô nhập và nút gửi tin nhắn */}
            <div className="p-3 border-top bg-white">
                <Form onSubmit={handleSendMessage}>
                    <InputGroup>
                        {/* Nút đính kèm hình ảnh/file (ví dụ: Đính kèm file đơn thuốc, ảnh chụp triệu chứng) */}
                        <Button variant="outline-secondary" className="border-end-0 bg-white text-muted px-3">
                            📎
                        </Button>
                        <Form.Control
                            placeholder="Nhập nội dung tư vấn cho bệnh nhân..."
                            value={inputText}
                            onChange={(e) => setInputText(e.target.value)}
                            className="border-start-0 border-end-0 px-2 py-2"
                            style={{ boxShadow: 'none' }}
                        />
                        <Button variant="primary" type="submit" className="px-4 fw-bold">
                            Gửi
                        </Button>
                    </InputGroup>
                </Form>
            </div>

        </div>
    );
}

export default MessageBox;