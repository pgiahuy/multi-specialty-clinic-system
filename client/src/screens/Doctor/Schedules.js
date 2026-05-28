import { useEffect, useState } from "react";
import Footer from "../../components/Footer";
import Header from "../../components/Header";
import { authApis, endpoint } from "../../configs/Apis";
import { Button, Container, Row, Col, Card, Badge } from "react-bootstrap";
import MySpinner from "../../components/MySpinner";
import { useNavigate } from "react-router-dom";
import { CalendarDate, Clock } from "react-bootstrap-icons"; 

const Schedules = () => {
    const [schedules, setSchedules] = useState([]);
    const [loading, setLoading] = useState(false);
    const [selectedDate, setSelectedDate] = useState(null); 
    const nav = useNavigate();

    const loadSchedules = async () => {
        try {
            setLoading(true);
            const response = await authApis().get(endpoint['schedules']);
            const data = response.data;
            setSchedules(data);
            
            
            if (data && data.length > 0) {
                const dates = [...new Set(data.map(s => s.date))].sort();
                setSelectedDate(dates[0]);
            }
        } catch (error) {
            console.error("Failed to load schedules:", error);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        loadSchedules();
    }, []);

   
    const uniqueDates = [...new Set(schedules.map(s => s.date))].sort();

    
    const currentDaySchedules = schedules.filter(s => s.date === selectedDate);
    
   
    const morningShifts = currentDaySchedules.filter(s => s.session === 'Sáng');
    const afternoonShifts = currentDaySchedules.filter(s => s.session === 'Chiều');

    
    const ShiftCard = ({ schedule }) => (
        <Card className="mb-3 border-0 shadow-sm rounded-4">
            <Card.Body>
                <div className="d-flex justify-content-between align-items-center mb-2">
                    <h5 className="fw-bold text-primary mb-0">
                        {schedule.room} - {schedule.area}
                    </h5>
                    <Badge className="p-2 rounded-3">
                        <Clock className="me-1" />
                        {schedule.shiftStartTime} - {schedule.shiftEndTime}
                    </Badge>
                </div>
                <p className="text-muted mb-3">Số lượng bệnh nhân: <strong>{schedule.currentPatients}</strong></p>
                <Button 
                    variant="outline-primary" 
                    className="w-100 rounded-3 fw-bold"
                    size="sm" 
                    onClick={() => nav(`/doctor/${schedule.id}/appointments`)}
                >
                    Xem lịch hẹn
                </Button>
            </Card.Body>
        </Card>
    );

    return (
        <div className="d-flex flex-column min-vh-100 bg-light">
            <Header />
            <Container className="py-5 flex-grow-1">
                <h3 className="fw-bold mb-4">Quản lý Lịch làm việc</h3>

                {loading ? (
                    <div className="text-center py-5"><MySpinner /></div>
                ) : schedules.length === 0 ? (
                    <div className="text-center text-muted py-5 bg-white rounded-4 shadow-sm">
                        Chưa có lịch làm việc nào.
                    </div>
                ) : (
                    <Row>
                        
                        <Col md={3}>
                            <div className="bg-white p-3 rounded-4 shadow-sm mb-4">
                                <h6 className="fw-bold text-uppercase text-muted mb-3">Chọn ngày làm việc</h6>
                                <div className="d-flex flex-column gap-2">
                                    {uniqueDates.map(date => (
                                        <Button
                                            key={date}
                                           
                                            variant={selectedDate === date ? "primary" : "outline-primary"}
                                            className={`text-start px-3 py-2 fw-bold ${selectedDate === date ? 'shadow' : ''}`}
                                            onClick={() => setSelectedDate(date)}
                                        >
                                            <CalendarDate className="me-2 mb-1" />
                                            {date}
                                        </Button>
                                    ))}
                                </div>
                            </div>
                        </Col>

                       
                        <Col md={9}>
                            <div className="bg-white p-4 rounded-4 shadow-sm">
                                <h4 className="fw-bold mb-4 text-primary">Lịch trình ngày: {selectedDate}</h4>
                                
                                <Row>
                                    
                                    <Col md={6}>
                                        <div className="p-3 bg-light rounded-4 mb-4 mb-md-0">
                                            <h5 className="fw-bold text-warning mb-3">Buổi Sáng</h5>
                                            {morningShifts.length > 0 ? (
                                                morningShifts.map(schedule => (
                                                    <ShiftCard key={schedule.id} schedule={schedule} />
                                                ))
                                            ) : (
                                                <p className="text-muted fst-italic">Không có ca sáng</p>
                                            )}
                                        </div>
                                    </Col>

                                    
                                    <Col md={6}>
                                        <div className="p-3 bg-light rounded-4">
                                            <h5 className="fw-bold text-primary mb-3">Buổi Chiều</h5>
                                            {afternoonShifts.length > 0 ? (
                                                afternoonShifts.map(schedule => (
                                                    <ShiftCard key={schedule.id} schedule={schedule} />
                                                ))
                                            ) : (
                                                <p className="text-muted fst-italic">Không có ca chiều</p>
                                            )}
                                        </div>
                                    </Col>
                                </Row>

                            </div>
                        </Col>
                    </Row>
                )}
            </Container>
            <Footer />
        </div>
    );
};

export default Schedules;