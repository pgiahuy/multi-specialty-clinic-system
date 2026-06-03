package com.hb.dto.response;

import java.util.List;

public class StorekeeperAlertResponse {

    private List<MedicineResponse> lowStockMedicines;
    private List<MedicineBatchResponse> expiringBatches;

    public StorekeeperAlertResponse() {
    }

    public StorekeeperAlertResponse(List<MedicineResponse> lowStockMedicines, List<MedicineBatchResponse> expiringBatches) {
        this.lowStockMedicines = lowStockMedicines;
        this.expiringBatches = expiringBatches;
    }

    public List<MedicineResponse> getLowStockMedicines() {
        return lowStockMedicines;
    }

    public void setLowStockMedicines(List<MedicineResponse> lowStockMedicines) {
        this.lowStockMedicines = lowStockMedicines;
    }

    public List<MedicineBatchResponse> getExpiringBatches() {
        return expiringBatches;
    }

    public void setExpiringBatches(List<MedicineBatchResponse> expiringBatches) {
        this.expiringBatches = expiringBatches;
    }
}
