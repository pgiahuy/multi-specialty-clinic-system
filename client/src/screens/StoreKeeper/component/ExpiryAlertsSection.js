
import { memo } from "react";
import { Alert, Button } from "react-bootstrap";
const ExpiryAlertsSection = memo(({ loading, expiryAlerts, expiryPage, setExpiryPage, alertPageSize, deletingBatchId, onDeleteBatch }) => {
    console.log("%c[Render] ExpiryAlertsSection chạy lại!", "color: #ff9800; font-weight: bold;");

    return (

        <Alert variant={loading ? 'info' : expiryAlerts.length > 0 ? 'warning' : 'success'}>
            <strong>Cảnh báo hạn dùng</strong>
            {loading ? (
                <div className="mt-2">Đang tải cảnh báo hạn dùng...</div>
            ) : expiryAlerts.length === 0 ? (
                <div className="mt-2">Không có lô thuốc sắp hết hạn hoặc đã hết hạn.</div>
            ) : (
                <>
                    <ul className="mb-0 mt-2 list-unstyled">
                        {expiryAlerts.map((item) => (
                            <li className="d-flex flex-row py-2 border-bottom border-warning gap-3" key={item.id}>
                                <div className={`${item.isExpired ? 'text-danger' : ''}`}>
                                    {item.title}. {item.description}
                                </div>

                                {item.isExpired && (
                                    <Button
                                        size="sm"
                                        variant="danger"
                                        onClick={() => onDeleteBatch(item.batchId)}
                                        disabled={deletingBatchId === item.batchId}
                                    >
                                        {deletingBatchId === item.batchId ? 'Đang tiêu hủy...' : 'Tiêu hủy lô'}
                                    </Button>
                                )}
                            </li>
                        ))}
                    </ul>
                    <div className="d-flex justify-content-between align-items-center mt-3">
                        <Button
                            size="sm"
                            variant="secondary"
                            disabled={loading || expiryPage <= 1}
                            onClick={() => setExpiryPage((prev) => Math.max(prev - 1, 1))}
                        >
                            Trang trước
                        </Button>
                        <span>Trang {expiryPage}</span>
                        <Button
                            size="sm"
                            variant="secondary"
                            disabled={loading || expiryAlerts.length < alertPageSize}
                            onClick={() => setExpiryPage((prev) => prev + 1)}
                        >
                            Trang tiếp
                        </Button>
                    </div>
                </>
            )}
        </Alert>
    )
});

export default ExpiryAlertsSection;