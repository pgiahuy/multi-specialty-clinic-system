import { Card, Row, Col } from 'react-bootstrap';
import { CalendarPlus, FileEarmarkMedical, PersonCircle, ClockHistory, Receipt, PersonBadge, Droplet, Clipboard2Pulse, DropletHalf } from 'react-bootstrap-icons';
import { useNavigate } from 'react-router-dom';

const ControlCard = () => {
  const navigate = useNavigate();


  const navItems = [
    { title: 'Đặt lịch hẹn', icon: <CalendarPlus size={30} className="text-primary mb-2" />, path: '/patient/booking' },
    { title: 'Hồ sơ sức khoẻ', icon: <PersonBadge size={30} className="text-success mb-2" />, path: '/patient/profiles' },
    { title: 'Hoá đơn', icon: <Receipt size={30} className="text-success mb-2" />, path: '/patient/payment' },
    { title: 'Chuyên khoa', icon: <FileEarmarkMedical size={30} className="text-info mb-2" />, path: '/chuyen-khoa' },
    { title: 'Lịch hẹn', icon: <ClockHistory size={30} className="text-warning mb-2" />, path: '/lich-hen' },
    { title: 'Kết quả xét nghiệm', icon: <DropletHalf size={30} className="text-danger mb-2" />, path: '/patient/test-results' },
  ];

  const navItemStyle = {
    cursor: 'pointer',
    transition: 'all 0.2s ease-in-out',
    borderRadius: '12px',
    border: '1px solid rgb(102, 109, 192)',
    height: '100%',
    backgroundColor: '#fff'
  };

  return (
    <Card className="shadow-sm border-0 mb-4 m-3" style={{ borderRadius: '15px', backgroundColor: '#e0f6ff' }}>
      <Card.Body>
        <Row className="text-center g-3">

          {navItems.map((item, index) => (
            <Col xs={6} md={3} key={index}>
              <div
                className="p-3 nav-item-hover"
                style={navItemStyle}
                onClick={() => navigate(item.path)}
              >
                {item.icon}
                <div className="fw-bold small">{item.title}</div>
              </div>
            </Col>
          ))}
        </Row>
      </Card.Body>
    </Card>
  );
};

export default ControlCard;