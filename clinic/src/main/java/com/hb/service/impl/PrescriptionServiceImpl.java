package com.hb.service.impl;

import com.hb.dto.request.PrescriptionCreateRequest;
import com.hb.dto.request.PrescriptionItemCreateRequest;
import com.hb.enums.AppointmentStatus;
import com.hb.enums.InventoryLogType;
import com.hb.enums.PrescriptionStatus;
import com.hb.exception.BadRequestException;
import com.hb.exception.DuplicateResourceException;
import com.hb.exception.ResourceNotFoundException;
import com.hb.pojo.InventoryLog;
import com.hb.pojo.MedicalRecord;
import com.hb.pojo.Medicine;
import com.hb.pojo.MedicineBatch;
import com.hb.pojo.Payment;
import com.hb.pojo.Prescription;
import com.hb.pojo.PrescriptionItem;
import com.hb.pojo.User;
import com.hb.repository.MedicalRecordRepository;
import com.hb.repository.MedicineBatchRepository;
import com.hb.repository.MedicineRepository;
import com.hb.repository.PrescriptionItemRepository;
import com.hb.repository.PrescriptionRepository;
import com.hb.service.NotificationService;
import com.hb.service.PaymentItemsService;
import com.hb.service.PaymentService;
import com.hb.service.PrescriptionService;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.hb.repository.InventoryLogRepository;

@Service
public class PrescriptionServiceImpl implements PrescriptionService {
    
    @Autowired
    private PrescriptionRepository prescriptionRepo;
    @Autowired
    private PrescriptionItemRepository prescriptionItemRepo;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private MedicalRecordRepository medicalRecordRepo;
    @Autowired
    private MedicineRepository medicineRepo;
    @Autowired
    private InventoryLogRepository inventoryLogRepo;
    @Autowired
    private MedicineBatchRepository medicineBatchRepo;
    @Autowired
    private PaymentService paymentService;
    @Autowired
    private PaymentItemsService paymentItemsService;
    
    @Override
    @Transactional(readOnly = true)
    public List<Prescription> getPrescriptions(Map<String, String> params) {
        return this.prescriptionRepo.getPrescriptions(params);
    }
    
    private void validateReq(PrescriptionCreateRequest req) {
        if (req == null) {
            throw new BadRequestException("Thiếu thông tin đơn thuốc!");
        }
        if (req.getMedicalRecordId() == null) {
            throw new BadRequestException("Không tìm thấy bệnh án!!");
        }
        if (req.getItems() == null || req.getItems().isEmpty()) {
            throw new BadRequestException("Cần ít nhất một loại thuốc để tạo đơn!");
        }
    }
    
    private Medicine validateReqItem(PrescriptionItemCreateRequest item) {
        if (item == null) {
            throw new BadRequestException("Prescription item is required");
        }
        if (item.getMedicineId() == null) {
            throw new BadRequestException("Thiếu thông tin thuốc!");
        }
        if (item.getQuantity() <= 0) {
            throw new BadRequestException("Số lượng phải lớn hơn 0 !");
        }
        
        Medicine m = medicineRepo.getMedicineById(item.getMedicineId());
        if (m == null) {
            throw new ResourceNotFoundException("Không tìm thấy thuốc này!");
        }
        return m;
    }
    
    @Override
    @Transactional
    public Prescription saveOrUpdateDraftPrescription(PrescriptionCreateRequest req) {
        this.validateReq(req);
        Prescription prescription;
        
        if (req.getId() == null) {
            prescription = new Prescription();
            prescription.setCreatedAt(LocalDateTime.now());
            
            MedicalRecord mr = medicalRecordRepo.getMedicalRecordById(req.getMedicalRecordId());
            if (mr == null) {
                throw new ResourceNotFoundException("Không tìm thấy bệnh án!!");
            }
            if (mr.getPrescription() != null) {
                throw new BadRequestException("Đã có đơn thuốc cho hồ sơ này!");
            }
            
            prescription.setMedicalRecordId(mr);
            prescription.setPrescriptionItemCollection(new ArrayList<>());
            prescription = prescriptionRepo.saveOrUpdate(prescription);
        } else {
            prescription = this.prescriptionRepo.getPrescriptionById(req.getId());
            if (prescription == null) {
                throw new ResourceNotFoundException("Không tìm thấy đơn thuốc nháp cần cập nhật!");
            }
            
            if (prescription.getPrescriptionItemCollection() != null) {
                for (PrescriptionItem oldItem : prescription.getPrescriptionItemCollection()) {
                    prescriptionItemRepo.delete(oldItem);
                }
                prescription.getPrescriptionItemCollection().clear();
            }
        }
        
        List<PrescriptionItem> items = new ArrayList<>();
        for (var i : req.getItems()) {
            Medicine medicine = validateReqItem(i);
            
            PrescriptionItem item = new PrescriptionItem();
            item.setMedicineId(medicine);
            item.setQuantity(i.getQuantity());
            item.setDaysToUse(i.getDaysToUse());
            item.setNote(i.getNote());
            item.setPrescriptionId(prescription);
            
            prescriptionItemRepo.save(item);
            items.add(item);
        }
        
        prescription.setPrescriptionItemCollection(items);
        prescription.setStatus(PrescriptionStatus.DRAFT);
        return prescriptionRepo.saveOrUpdate(prescription);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Prescription createPrescription(PrescriptionCreateRequest req, String username) {
        this.validateReq(req);
        Prescription p;
        
        if (req.getId() != null) {
            
            p = this.prescriptionRepo.getPrescriptionById(req.getId());
            if (p == null) {
                throw new ResourceNotFoundException("Không tìm thấy đơn thuốc nháp cần phát hành!");
            }
            
            if (p.getStatus() == PrescriptionStatus.PUBLIC) {
                throw new DuplicateResourceException("Bệnh nhân này đã khám xong, không thể chỉnh sửa đơn thuốc!");
            }
            
            if (p.getPrescriptionItemCollection() != null) {
                for (PrescriptionItem oldItem : p.getPrescriptionItemCollection()) {
                    prescriptionItemRepo.delete(oldItem);
                }
                p.getPrescriptionItemCollection().clear();
            }
        } else {
            p = new Prescription();
            p.setCreatedAt(LocalDateTime.now());
            
            MedicalRecord mr = medicalRecordRepo.getMedicalRecordById(req.getMedicalRecordId());
            if (mr == null) {
                throw new ResourceNotFoundException("Không tìm thấy bệnh án!");
            }
            if (mr.getPrescription() != null) {
                throw new BadRequestException("Đã có đơn thuốc cho hồ sơ này!");
            }
            p.setMedicalRecordId(mr);
            
            p = prescriptionRepo.saveOrUpdate(p);
        }
        
        List<PrescriptionItem> items = new ArrayList<>();
        int bufferDays = 3;
        
        for (var i : req.getItems()) {
            Medicine m = validateReqItem(i);
            int requiredQty = i.getQuantity();
            
            int daysToUse = (i.getDaysToUse() > 0) ? i.getDaysToUse() : 7;
            
            LocalDate minExpiryDate = LocalDate.now().plusDays(daysToUse + bufferDays);
            List<MedicineBatch> availableBatches = medicineRepo.getAvailableBatches(m.getId(), minExpiryDate);
            int totalAvailable = availableBatches.stream().mapToInt(MedicineBatch::getQuantity).sum();
            if (totalAvailable < requiredQty) {
                throw new BadRequestException("Thuốc [" + m.getName() + "] không đủ số lượng đạt chuẩn trong kho cho đợt điều trị "
                        + daysToUse + " ngày! (Yêu cầu: " + requiredQty + ", Khả dụng thực tế: " + totalAvailable + ")");
            }
            
            PrescriptionItem item = new PrescriptionItem();
            item.setMedicineId(m);
            item.setQuantity(requiredQty);
            item.setPrescriptionId(p);
            item.setDaysToUse(daysToUse);
            item.setNote(i.getNote());            
            
            prescriptionItemRepo.save(item);
            items.add(item);
        }
        
        p.setStatus(PrescriptionStatus.PUBLIC);
        p.setPublicAt(LocalDateTime.now());
        p.setPrescriptionItemCollection(items);
        
        p.getMedicalRecordId().getAppointmentId().setStatus(AppointmentStatus.COMPLETED);
        
        Prescription saved = prescriptionRepo.saveOrUpdate(p);
        
        if (saved.getMedicalRecordId() != null && saved.getMedicalRecordId().getAppointmentId() != null) {
            Long appointmentId = saved.getMedicalRecordId().getAppointmentId().getId();
            if (appointmentId != null) {
                try {
                    Payment payment = paymentService.createPayment(appointmentId);
                    paymentItemsService.addPrescriptionItem(payment, saved.getId());
                } catch (Exception e) {

                    System.err.println("Lỗi tạo thanh toán cho đơn thuốc: " + e.getMessage());
                }
            }
        }
        this.pushPrescriptionNotify(saved);
        
        return saved;
 
    }
    
    @Override
    @Transactional(readOnly = true)
    public Prescription getPrescriptionById(Long id) {
        
        Prescription prescription = this.prescriptionRepo.getPrescriptionById(id);
        if (prescription == null) {
            throw new ResourceNotFoundException("Không tìm thấy đơn thuốc!");
        }
        return prescription;
    }
    
    @Override
    @Transactional
    public void deletePrescription(Long id) {
        this.prescriptionRepo.deletePrescription(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public long countPrescription(Map<String, String> params) {
        return this.prescriptionRepo.count(params, Prescription.class);
    }
    
    private void pushPrescriptionNotify(Prescription saved) {
        try {
            MedicalRecord currentMr = saved.getMedicalRecordId();
            if (currentMr != null && currentMr.getAppointmentId() != null
                    && currentMr.getAppointmentId().getPatientId() != null) {
                
                User patientUser = currentMr.getAppointmentId().getPatientId().getUserId();
                if (patientUser != null) {
                    Map<String, String> notiParams = new HashMap<>();
                    notiParams.put("username", patientUser.getUsername());
                    notiParams.put("title", "Đơn thuốc mới");
                    notiParams.put("content", "Bác sĩ vừa kê đơn thuốc mới cho bạn. Vui lòng kiểm tra!");
                    notiParams.put("path", "/patient/prescriptions/" + saved.getId());
                    
                    this.notificationService.addNotification(notiParams);
                }
            }
        } catch (Exception e) {
            System.err.println("Lỗi gửi thông báo: " + e.getMessage());
        }
    }
    
    @Override
    public Prescription getPrescriptionByMedicalRecordId(Long recordId) {
        return this.prescriptionRepo.getPrescriptionByMedicalRecordId(recordId);
    }

    @Override
    @Transactional
    public Prescription dispensePrescription(Long id, String username) {
        Prescription prescription = this.prescriptionRepo.getPrescriptionById(id);
        if (prescription == null) {
            throw new ResourceNotFoundException("Không tìm thấy đơn thuốc!");
        }
        int bufferDays = 3;
        

        if (prescription.getPrescriptionItemCollection() != null) {
            for (PrescriptionItem item : prescription.getPrescriptionItemCollection()) {
                Medicine m = item.getMedicineId();
                if (m == null) continue;
                int requiredQty = item.getQuantity();
                int daysToUse = (item.getDaysToUse() > 0) ? item.getDaysToUse() : 7;                  
                LocalDate minExpiryDate = LocalDate.now().plusDays(daysToUse + bufferDays);
                List<MedicineBatch> availableBatches = medicineRepo.getAvailableBatches(m.getId(), minExpiryDate);
                int remainingQtyToDeduct = requiredQty;
                for (MedicineBatch batch : availableBatches) {
                    if (remainingQtyToDeduct <= 0) {
                        break;
                    }

                    int batchQty = batch.getQuantity();
                    int qtyDeducted = 0;

                    if (batchQty >= remainingQtyToDeduct) {
                        qtyDeducted = remainingQtyToDeduct;
                        batch.setQuantity(batchQty - remainingQtyToDeduct);
                        remainingQtyToDeduct = 0;
                    } else {
                        qtyDeducted = batchQty;
                        remainingQtyToDeduct -= batchQty;
                        batch.setQuantity(0);
                    }

                    medicineBatchRepo.saveOrUpdate(batch);

                    InventoryLog log = new InventoryLog();
                    log.setMedicineId(m);
                    log.setBatchId(batch);
                    log.setChangeAmount(-qtyDeducted);
                    log.setReason(InventoryLogType.PRESCRIPTION_EXPORT);
                    log.setReferenceId(prescription.getId());
                    log.setCreatedAt(LocalDateTime.now());
                    log.setCreatedBy(username);

                    inventoryLogRepo.createInventoryLog(log);
                }
            }
        }

        prescription.setDispensedAt(LocalDateTime.now());
        this.prescriptionRepo.saveOrUpdate(prescription);

        return prescription;
    }
}
