import { Card, Row, Col, Stack, Container } from 'react-bootstrap';
import { PersonVcard, Envelope, Phone, Calendar3, GeoAlt, Fingerprint } from 'react-bootstrap-icons';

const PatientProfileCard = ({ patient }) => {
  return (

    <Container className="d-flex justify-content-center mt-2">
      <Card 
        className="shadow-sm border-0 mb-3" 
        style={{ 
            border: '2px solid #0080ff',
            borderRadius: '12px', 
        //  borderLeft: '5px solid #0d6efd',
            width: '100%' 
        }}
      >
        <Card.Body className="p-3">
          <Row className="align-items-center">

            <Col md={4} lg={3} className="border-end">
              <Stack direction="horizontal" gap={3}>
                <div className="bg-light p-2 rounded-circle text-primary">
                  <PersonVcard size={24} />
                </div>
                <div>
                  <h6 className="mb-0 fw-bold text-primary">{patient.fullName}</h6>
                  <small className="text-muted d-flex align-items-center gap-1">
                    <Fingerprint size={12} /> {patient.cccd}
                  </small>
                </div>
              </Stack>
            </Col>


            <Col md={8} lg={9}>
              <Row className="g-2 ms-md-2">
                <Col md={4}>
                  <div className="text-muted small d-flex align-items-center gap-1 text-nowrap">
                    <Calendar3 size={12} /> Ngày sinh
                  </div>
                  <div className="small fw-semibold">{patient.dob}</div>
                </Col>
                
                <Col md={4}>
                  <div className="text-muted small d-flex align-items-center gap-1 text-nowrap">
                    <Envelope size={12} /> Email
                  </div>
                  <div className="small text-truncate" title={patient.email}>{patient.email}</div>
                </Col>

                <Col md={4}>
                  <div className="text-muted small d-flex align-items-center gap-1 text-nowrap">
                    <Phone size={12} /> Điện thoại
                  </div>
                  <div className="small fw-semibold">{patient.phone}</div>
                </Col>

                <Col md={12} className="mt-2 border-top pt-2">
                  <div className="text-muted small d-flex align-items-center gap-1">
                    <GeoAlt size={12} /> Địa chỉ
                  </div>
                  <div className="small text-muted text-truncate">{patient.address}</div>
                </Col>
              </Row>
            </Col>
          </Row>
        </Card.Body>
      </Card>
    </Container>
  );
};

export default PatientProfileCard;