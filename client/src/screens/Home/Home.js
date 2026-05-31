import { Container, Row, Col, Card, Button } from "react-bootstrap";
import { Calendar, FileText, CreditCard } from "react-bootstrap-icons";
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
              <Button variant="outline-primary rounded-4">Tìm hiểu thêm</Button>
            </div>
          </Col>
        </Row>
      </Container>
    </section>
    </>
  );
};

export default Home;
