import { Button } from "react-bootstrap";
import { useNavigate } from "react-router-dom";

const ReceptionNavBar = () => {

    const nav = useNavigate();

    return (
        <div className="d-flex justify-content-center gap-4 mb-4">
            <Button variant="outline-primary" size="sm" onClick={() => nav('/reception/prescriptions')}>
                Xem đơn thuốc
            </Button>
            <Button variant="outline-primary" size="sm" onClick={() => nav('/reception')}>
                Xem lịch hẹn
            </Button>
            <Button variant="outline-primary" size="sm" onClick={() => nav('/reception/invoices')}>
                Xem hóa đơn
            </Button>
        </div>
    )
};

export default ReceptionNavBar;