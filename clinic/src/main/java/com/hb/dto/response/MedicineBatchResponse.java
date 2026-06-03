package com.hb.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;

public class MedicineBatchResponse {

    private Long id;
    private String batchCode;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate importDate;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate expiryDate;
    private int quantity;
    private Long medicineId;
    private String medicineName;
    private String medicineCode;

    public MedicineBatchResponse() {
    }

    public MedicineBatchResponse(Long id, String batchCode, LocalDate importDate, LocalDate expiryDate, int quantity, Long medicineId, String medicineName, String medicineCode) {
        this.id = id;
        this.batchCode = batchCode;
        this.importDate = importDate;
        this.expiryDate = expiryDate;
        this.quantity = quantity;
        this.medicineId = medicineId;
        this.medicineName = medicineName;
        this.medicineCode = medicineCode;
    }

    public static MedicineBatchResponse fromEntity(com.hb.pojo.MedicineBatch batch) {
        Long medId = null;
        String medName = null;
        String medCode = null;
        if (batch.getMedicineId() != null) {
            medId = batch.getMedicineId().getId();
            medName = batch.getMedicineId().getName();
            medCode = batch.getMedicineId().getCode();
        }
        return new MedicineBatchResponse(
                batch.getId(),
                batch.getBatchCode(),
                batch.getImportDate(),
                batch.getExpiryDate(),
                batch.getQuantity(),
                medId,
                medName,
                medCode
        );
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBatchCode() {
        return batchCode;
    }

    public void setBatchCode(String batchCode) {
        this.batchCode = batchCode;
    }

    public LocalDate getImportDate() {
        return importDate;
    }

    public void setImportDate(LocalDate importDate) {
        this.importDate = importDate;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Long getMedicineId() {
        return medicineId;
    }

    public void setMedicineId(Long medicineId) {
        this.medicineId = medicineId;
    }

    public String getMedicineName() {
        return medicineName;
    }

    public void setMedicineName(String medicineName) {
        this.medicineName = medicineName;
    }

    public String getMedicineCode() {
        return medicineCode;
    }

    public void setMedicineCode(String medicineCode) {
        this.medicineCode = medicineCode;
    }
}
