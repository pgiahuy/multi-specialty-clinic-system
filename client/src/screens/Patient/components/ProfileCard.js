import { Card, Row, Col, Stack, Container, Button } from 'react-bootstrap';
import { PersonVcard, Phone, Calendar3, GeoAlt, Fingerprint, GenderMale, GenderFemale } from 'react-bootstrap-icons';

const PatientProfileCard = ({ patient, onEdit, onDelete }) => {
  const getGenderIcon = () => {
    const gender = patient.gender?.toLowerCase() || '';
    if (gender.includes('nam') || gender.includes('male')) {
      return <GenderMale size={12} />;
    }
    if (gender.includes('nữ') || gender.includes('female')) {
      return <GenderFemale size={12} />;
    }
    return <GenderMale size={12} />;
  };

  const formatRelationship = (relEnum) => {
    if (!relEnum) return 'Chưa cập nhật';

    switch (String(relEnum).toUpperCase()) {
      case 'SELF': return 'Tôi';
      case 'PARENT': return 'Ba/Mẹ';
      case 'CHILD': return 'Con';
      case 'SPOUSE': return 'Vợ/Chồng';
      case 'SIBLING': return 'Anh/Chị/Em';
      case 'GRANDPARENT': return 'Ông/Bà';
      case 'OTHER': return 'Khác';
      default: return relEnum;
    }
  };

  return (
    <Container className="d-flex justify-content-center mt-3 mb-4">
      <Card className="shadow-sm w-100 rounded-4 overflow-hidden" style={{ maxWidth: 760 }}>
        <Card.Body className="p-4">

          <div className="d-flex gap-3 align-items-start mb-4 pb-3 border-bottom border-light">
            <div className="bg-primary bg-opacity-10 text-primary rounded-4 d-flex align-items-center justify-content-center flex-shrink-0" style={{ width: 60, height: 60 }}>
              <PersonVcard size={28} />
            </div>
            <div className="flex-grow-1">
              <div className="d-flex flex-column flex-sm-row align-items-sm-center justify-content-between gap-2">
                <div>
                  <h5 className="mb-1 fw-semibold text-dark">{patient.fullName || 'Chưa cập nhật'}</h5>
                  <div className="small text-muted">{patient.cccd || 'Chưa có CMND/CCCD'}</div>
                </div>
                <div>
                  <span className="badge bg-primary bg-opacity-10 text-primary border border-primary border-opacity-25 py-2 px-4 fw-semibold rounded-pill shadow-sm">
                    {formatRelationship(patient.relationship)}
                  </span>
                </div>
              </div>
            </div>
          </div>


          <Row className="gy-4">
            <Col xs={12} md={6}>
              <div className="text-muted small mb-1 d-flex align-items-center gap-2">
                <Calendar3 size={15} className="text-primary opacity-75" /> Ngày sinh
              </div>
              <div className="fw-semibold text-dark">{patient.dob || '-'}</div>
            </Col>

            <Col xs={12} md={6}>
              <div className="text-muted small mb-1 d-flex align-items-center gap-2">
                {getGenderIcon()} Giới tính
              </div>
              <div className="fw-semibold text-dark">{patient.gender || '-'}</div>
            </Col>

            <Col xs={12} md={6}>
              <div className="text-muted small mb-1 d-flex align-items-center gap-2">
                <Phone size={15} className="text-primary opacity-75" /> Điện thoại
              </div>
              <div className="fw-semibold text-dark">{patient.phone || '-'}</div>
            </Col>

            <Col xs={12} md={6}>
              <div className="text-muted small mb-1 d-flex align-items-center gap-2">
                <GeoAlt size={15} className="text-primary opacity-75" /> Địa chỉ
              </div>
              <div className="fw-semibold text-dark">{patient.address || '-'}</div>
            </Col>
          </Row>


          <div className="mt-4 pt-4 border-top border-light d-flex justify-content-end gap-3">
            <Button
              variant="primary"
              className="rounded-4 px-4 py-2 fw-medium shadow-sm transition-all"
              onClick={() => {
                onEdit();
                
              }}
            >
              Cập nhật thông tin
            </Button>

            <Button
              variant="danger"
              className="rounded-4 px-4 py-2 fw-medium shadow-sm transition-all"
              onClick={() => {
                onDelete();
                
              }}
            >
              Xóa
            </Button>
          </div>

        </Card.Body>
      </Card>
    </Container>
  );
};

export default PatientProfileCard;