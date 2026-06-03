package com.hb.dto.response.stats;

public class MedicineInventoryStatsResponse {

    private String medicineName;
    private Long exportedQty;
    private Long importedQty;

    public MedicineInventoryStatsResponse() {
    }

    public MedicineInventoryStatsResponse(String medicineName, Long exportedQty, Long importedQty) {
        this.medicineName = medicineName;
        this.exportedQty = exportedQty;
        this.importedQty = importedQty;
    }

    public String getMedicineName() {
        return medicineName;
    }

    public void setMedicineName(String medicineName) {
        this.medicineName = medicineName;
    }

    public Long getExportedQty() {
        return exportedQty;
    }

    public void setExportedQty(Long exportedQty) {
        this.exportedQty = exportedQty;
    }

    public Long getImportedQty() {
        return importedQty;
    }

    public void setImportedQty(Long importedQty) {
        this.importedQty = importedQty;
    }
}
