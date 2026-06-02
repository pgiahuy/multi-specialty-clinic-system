import { Container, Row, Col, Card, Button } from "react-bootstrap";
import { Calendar, FileText, CreditCard, Telephone, Envelope, GeoAlt, Clock } from "react-bootstrap-icons";
import "./HomeStyle.css";
import { useNavigate } from "react-router-dom";


const features = [
  {
    title: "Đặt lịch khám nhanh chóng",
    description: "Chọn bác sĩ, ngày giờ phù hợp và nhận xác nhận ngay lập tức.",
    icon: <Calendar className="text-primary" />,
  },
  {
    title: "Theo dõi hồ sơ y tế",
    description: "Quản lý đơn thuốc, kết quả xét nghiệm và lịch sử khám bệnh dễ dàng.",
    icon: <FileText className="text-primary" />,
  },
  {
    title: "Thanh toán an toàn",
    description: "Hỗ trợ nhiều hình thức thanh toán trực tuyến bảo mật.",
    icon: <CreditCard className="text-primary" />,
  },
];

const Home = () => {
  const navigate = useNavigate();
  return (
    <>
      <section
        className="home-banner text-white"
        style={{ backgroundImage: "url(/banner.jpg)" }}
      >
        <div className="banner-content">
          <Container>
            <Row className="align-items-center">
              <Col lg={7} md={9}>
                <h1 className="display-5 fw-bold">
                  Hệ thống phòng khám đa chuyên khoa
                </h1>
                <p className="lead mt-3 banner-subtitle">
                  Kết nối bệnh nhân với bác sĩ chuyên môn, đặt lịch khám và theo dõi
                  hồ sơ y tế trực tuyến một cách nhanh chóng, an toàn và tiện lợi.
                </p>
                <div className="mt-4">
                  <Button variant="primary rounded-4" className="border-0" size="lg" onClick={() => navigate('/patient/booking')}>
                    Đặt lịch khám ngay
                  </Button>
                </div>
              </Col>
            </Row>
          </Container>
        </div>
      </section>

      <section className="home-features-section py-5">
        <Container>
          <Row className="g-4">
            {features.map((feature, index) => (
              <Col md={4} key={index}>
                <Card className="h-100 border-0 shadow-sm home-feature-card">
                  <Card.Body>
                    <div
                      className="home-feature-icon d-inline-flex align-items-center justify-content-center mb-3"
                    >
                      {feature.icon}
                    </div>
                    <Card.Title className="fw-bold">
                      {feature.title}
                    </Card.Title>
                    <Card.Text className="text-muted">
                      {feature.description}
                    </Card.Text>
                  </Card.Body>
                </Card>
              </Col>
            ))}
          </Row>

          <Row className="align-items-center mt-5">
            <Col lg={6} className="mb-4 mb-lg-0">
              <div className="home-intro-image">
                <img
                  src="/banner2.jpg"
                  alt="Giới thiệu hệ thống"
                  className="img-fluid"
                />
              </div>
            </Col>
            <Col lg={6}>
              <div className="home-intro">
                <h2>Giới thiệu dịch vụ</h2>
                <p>
                  Chúng tôi cung cấp trải nghiệm chăm sóc sức khỏe toàn diện với hệ thống
                  phòng khám đa chuyên khoa, đội ngũ bác sĩ giàu kinh nghiệm và công nghệ
                  hỗ trợ hiện đại. Từ đặt lịch khám đến thanh toán và quản lý hồ sơ,
                  mọi thao tác đều được tối ưu để phục vụ bạn tốt nhất.
                </p>
                <ul className="list-unstyled home-intro-list">
                  <li>• Hỗ trợ đặt lịch khám trực tuyến 24/7.</li>
                  <li>• Quản lý lịch sử khám chữa bệnh và đơn thuốc.</li>
                  <li>• Thanh toán và xác nhận tự động.</li>
                </ul>
                <Button variant="outline-primary rounded-4" onClick={() => navigate('/contact')}>Tìm hiểu thêm</Button>
              </div>
            </Col>
          </Row>
        </Container>
      </section>

      <section className="home-contact-section py-5">
        <Container>
          <Row className="g-4 align-items-center">
            <Col lg={6}>
              <div className="home-contact-content p-4 rounded-4 shadow-sm bg-white">
                <h2>Liên hệ với OU-Clinic</h2>
                <p className="mb-4">
                  Nếu bạn cần hỗ trợ đặt lịch khám, tư vấn bác sĩ, hoặc giải đáp thắc mắc về dịch vụ,
                  hãy liên hệ ngay với đội ngũ chăm sóc khách hàng của chúng tôi.
                </p>
                <ul className="list-unstyled home-contact-list mb-4">
                  <li><strong>Hotline:</strong> 1900 1234</li>
                  <li><strong>Email:</strong> support@ouclinic.com</li>
                  <li><strong>Địa chỉ:</strong> 123 Đường Sức Khỏe, Quận 1, TP. Hồ Chí Minh</li>
                </ul>
                <Button variant="primary rounded-4" size="lg" onClick={() => navigate('/contact')}>
                  Gửi yêu cầu ngay
                </Button>
              </div>
            </Col>
            <Col lg={6}>
              <Row className="g-3">
                <Col md={6}>
                  <Card className="h-100 contact-card">
                    <Card.Body>
                      <div className="contact-icon bg-primary bg-opacity-10 text-primary mb-3">
                        <Telephone size={24} />
                      </div>
                      <h5 className="fw-bold">Tư vấn đặt lịch</h5>
                      <p className="text-muted mb-0">Hỗ trợ chọn bác sĩ và khung giờ phù hợp.</p>
                    </Card.Body>
                  </Card>
                </Col>
                <Col md={6}>
                  <Card className="h-100 contact-card">
                    <Card.Body>
                      <div className="contact-icon bg-success bg-opacity-10 text-success mb-3">
                        <Envelope size={24} />
                      </div>
                      <h5 className="fw-bold">Thông tin dịch vụ</h5>
                      <p className="text-muted mb-0">Gọi hoặc gửi email để được hỗ trợ nhanh chóng.</p>
                    </Card.Body>
                  </Card>
                </Col>
                <Col md={6}>
                  <Card className="h-100 contact-card">
                    <Card.Body>
                      <div className="contact-icon bg-warning bg-opacity-10 text-warning mb-3">
                        <GeoAlt size={24} />
                      </div>
                      <h5 className="fw-bold">Địa chỉ phòng khám</h5>
                      <p className="text-muted mb-0">Tìm kiếm phòng khám gần nhất với bạn.</p>
                    </Card.Body>
                  </Card>
                </Col>
                <Col md={6}>
                  <Card className="h-100 contact-card">
                    <Card.Body>
                      <div className="contact-icon bg-info bg-opacity-10 text-info mb-3">
                        <Clock size={24} />
                      </div>
                      <h5 className="fw-bold">Giờ làm việc</h5>
                      <p className="text-muted mb-0">Thứ 2 - Thứ 7: 07:00 - 17:00.</p>
                    </Card.Body>
                  </Card>
                </Col>
              </Row>
            </Col>
          </Row>
        </Container>
      </section>
    </>
  );
};

export default Home;
