import { Badge, Card, Table } from "react-bootstrap";



const PrescriptionDetail = ({ prescription, error }) => {
    if (error) {
        return <div className="text-danger">{error}</div>;
    }

    if (!prescription) {
        return (
            <div className="text-center text-muted py-5">
                Chọn một đơn thuốc để xem chi tiết.
            </div>
        );
    }

    return (
        <div>

            <div className="d-flex justify-content-between align-items-center gap-3 mb-2">
                <div>
                    <div className="fw-semibold">Đơn #{prescription.id}</div>
                </div>
                <Badge bg={prescription.status === 'PUBLIC' ? 'success' : 'secondary'} className="text-uppercase p-2">
                    {prescription.status === 'PUBLIC' ? 'Đã kê đơn' : ''}
                </Badge>
            </div>

            <div className="row g-3 mb-2">
                <div className="col-md-4">
                    <div className="border rounded-3 p-3 bg-light h-100">
                        <div className="text-secondary small mb-1">Bác sĩ kê đơn</div>
                        <div className="fw-semibold">{prescription.doctorName || '-'}</div>
                    </div>
                </div>
                <div className="col-md-4">
                    <div className="border rounded-3 p-3 bg-light h-100">
                        <div className="text-secondary small mb-1">Bệnh nhân</div>
                        <div className="fw-semibold">{prescription.patientName || '-'}</div>
                    </div>
                </div>
                <div className="col-md-4">
                    <div className="border rounded-3 p-3 bg-white h-100">
                        <div className="text-secondary small mb-1">Ngày kê đơn</div>
                        <div className="fw-semibold">{prescription.publicAt || '-'}</div>
                    </div>
                </div>
            </div>

            <div className="row g-3 mb-5">
                <div className="col-md-6">
                    <div className="text-secondary small mb-2">Chuẩn đoán</div>
                    <div className="border rounded-3 p-3 bg-white h-100">{prescription.diagnosis || 'Không có thông tin chẩn đoán'}</div>
                </div>
                <div className="col-md-6">
                    <div className="text-secondary small mb-2">Ghi chú</div>
                    <div className="border rounded-3 p-3 bg-white h-100">{prescription.note || 'Không có ghi chú'}</div>
                </div>
            </div>

            <div className="rounded-4 shadow-sm border overflow-hidden">
                <Table hover responsive className="mb-0 align-middle">
                    <thead className="table-light text-secondary small">
                        <tr>
                            <th>Thuốc</th>
                            <th className="text-center">SL</th>
                            <th className="text-center">Đơn vị</th>
                            <th className="text-center">Ngày dùng</th>
                            <th>Ghi chú</th>
                        </tr>
                    </thead>
                    <tbody>
                        {prescription.items && prescription.items.length > 0 ? (
                            prescription.items.map((item) => (
                                <tr key={item.id || `${item.medicineId}-${item.medicineName}`}>
                                    <td>{item.medicineName || 'N/A'}</td>
                                    <td className="text-center">{item.quantity ?? '-'}</td>
                                    <td className="text-center">{item.unit || '-'}</td>
                                    <td className="text-center">{item.daysToUse ?? '-'}</td>
                                    <td>{item.note || '-'}</td>
                                </tr>
                            ))
                        ) : (
                            <tr>
                                <td colSpan="5" className="text-center text-muted py-4">
                                    Không có thuốc nào trong đơn.
                                </td>
                            </tr>
                        )}
                    </tbody>
                </Table>
            </div>
        </div>
    );
};

export default PrescriptionDetail;