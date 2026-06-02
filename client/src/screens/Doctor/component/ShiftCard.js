import { Badge, Button, Card } from "react-bootstrap";
import { Clock } from "react-bootstrap-icons";
import { useNavigate } from "react-router-dom";


const ShiftCard = ({ schedule }) => {
    const nav = useNavigate();
    return (
        <Card className={`p-3 border-1 ${schedule.session.toLowerCase() === "sáng" ? "border-warning" : "border-primary"}`} style={{ borderRadius: 20 }}>
            <div className="d-flex justify-content-between align-items-center mb-2 gap-2">
                <h5 className={`fw-bold ${schedule.session.toLowerCase() === "sáng" ? "text-warning" : "text-primary"} mb-0`}>
                    Phòng {schedule.room || "N/A"}
                </h5>
                <Badge bg={schedule.session.toLowerCase() === "sáng" ? "warning" : "primary"} className="p-2 rounded-2">
                    {schedule.shiftStartTime} - {schedule.shiftEndTime}
                </Badge>
            </div>
            <p className="text-muted mb-1">
                <strong>{schedule.specialtyName}</strong>
            </p>
            <p className="text-muted mb-1 small">
                {schedule.area || "N/A"}
            </p>

            <p className="text-muted mb-3">
                Bệnh nhân: <strong>{schedule.currentPatients}</strong>/<strong>{schedule.maxPatients}</strong>
            </p>
            <Button
                variant={schedule.session.toLowerCase() === "sáng" ? "warning" : "primary"}
                className="w-100 rounded-3 fw-bold text-white"
                size="sm"
                onClick={() => nav(`/doctor/${schedule.id}/appointments`)}
            >
                Xem lịch hẹn
            </Button>
        </Card>
    );

};
export default ShiftCard;
