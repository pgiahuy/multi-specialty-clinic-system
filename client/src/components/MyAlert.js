import { Alert } from "react-bootstrap";

const MyAlert = ({
  show = true,
  heading = "Thông báo",
  message = "",
  variant = "success",
  onClose,
}) => {
  if (!show) return null;
  return (
    <Alert variant={variant} dismissible onClose={onClose}>
      <Alert.Heading className="fs-5">{heading}</Alert.Heading>
      <p>{message}</p>
    </Alert>
  );
};

export default MyAlert;