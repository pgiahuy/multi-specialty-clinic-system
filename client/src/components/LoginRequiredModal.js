import { Modal, Button } from "react-bootstrap";
import { useNavigate } from "react-router-dom";
import "./LoginRequiredModal.css";

const LoginRequiredModal = ({ show, onHide, onLogin }) => {
  const navigate = useNavigate();

  const handleLogin = () => {
    if (onLogin) {
      onLogin();
    } else {
      navigate("/login");
    }
  };

  return (
    <Modal
      className="login-required-modal"
      show={show}
      onHide={onHide}
      centered
      size="sm"
      backdrop="static"
    >
      <Modal.Header closeButton>
        <Modal.Title className="fw-bold">Đăng nhập để tiếp tục</Modal.Title>
      </Modal.Header>
      <Modal.Body>
        <p className="login-required-text">
          Vui lòng đăng nhập để sử dụng tính năng này!
        </p>
      </Modal.Body>
      <Modal.Footer className="justify-content-end border-top-0">
        <Button variant="outline-secondary rounded-4" onClick={onHide}>
          Hủy
        </Button>
        <Button variant="primary rounded-4 border-0 header-cta-primary" onClick={handleLogin}>
          Đăng nhập
        </Button>
      </Modal.Footer>
    </Modal>
  );
};

export default LoginRequiredModal;
