import { Spinner } from 'react-bootstrap';

const MyLoading = ({ message = "Đang tải..." }) => {
    return (
        <div className="d-flex align-items-center text-secondary small py-2">
            <Spinner animation="border" size="sm" role="status" className="me-2" />
            <span>{message}</span>
        </div>
    );
};

export default MyLoading;
