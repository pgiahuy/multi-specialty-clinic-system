import { memo } from "react";
import { Alert, Button } from "react-bootstrap";

const LowStockAlertsSection = memo(({ loading, lowStockAlerts, lowStockPage, setLowStockPage, alertPageSize }) => {
    console.log("%c[Render] LowStockAlertsSection chạy lại!", "color: #2196f3; font-weight: bold;");
    return (
        <Alert variant={loading ? 'info' : lowStockAlerts.length > 0 ? 'warning' : 'success'}>
            <strong>Cảnh báo số lượng thấp</strong>
            {loading ? (
                <div className="mt-2">Đang tải cảnh báo tồn kho...</div>
            ) : lowStockAlerts.length === 0 ? (
                <div className="mt-2">Không có thuốc còn số lượng thấp.</div>
            ) : (
                <>
                    <ul className="mb-0 mt-2">
                        {lowStockAlerts.map((item) => (
                            <li className="py-1 border-bottom border-warning" key={item.id}>{item.title}. {item.description}</li>
                        ))}
                    </ul>
                    <div className="d-flex justify-content-between align-items-center mt-3">
                        <Button
                            size="sm"
                            variant="secondary"
                            disabled={loading || lowStockPage <= 1}
                            onClick={() => setLowStockPage((prev) => Math.max(prev - 1, 1))}
                        >
                            Trang trước
                        </Button>
                        <span>Trang {lowStockPage}</span>
                        <Button
                            size="sm"
                            variant="secondary"
                            disabled={loading || lowStockAlerts.length < alertPageSize}
                            onClick={() => setLowStockPage((prev) => prev + 1)}
                        >
                            Trang tiếp
                        </Button>
                    </div>
                </>
            )}
        </Alert>
    )
});

export default LowStockAlertsSection;