/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.service.impl;

import com.hb.dto.request.AppointmentCreateRequest;
import com.hb.dto.response.AppointmentResponse;
import com.hb.exception.DuplicateResourceException;
import com.hb.exception.FullSlotException;
import com.hb.exception.ResourceNotFoundException;
import com.hb.enums.AppointmentStatus;
import com.hb.exception.BadRequestException;
import com.hb.exception.ForbiddenException;
import com.hb.mapper.AppointmentMapper;
import com.hb.pojo.Appointment;
import com.hb.pojo.Patient;
import com.hb.pojo.Payment;
import com.hb.pojo.Schedule;
import com.hb.pojo.User;
import com.hb.repository.AppointmentRepository;
import com.hb.repository.PatientRepository;
import com.hb.repository.ScheduleRepository;
import com.hb.service.AppointmentService;
import com.hb.dto.response.DoctorRankingResponse;
import com.hb.service.NotificationService;
import com.hb.service.PaymentItemsService;
import com.hb.service.PaymentService;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author HUY
 */
@Service
public class AppointmentServiceImpl implements AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepo;

    @Autowired
    private ScheduleRepository scheduleRepo;

    @Autowired
    private PatientRepository patientRepo;

    @Autowired
    private AppointmentMapper appointmentMapper;

    @Autowired
    private PaymentItemsService itemService;

    @Autowired
    private PaymentService paymentService;
    
    @Autowired
    private com.hb.repository.ConversationRepository conversationRepo;
    
    @Autowired
    private NotificationService notificationService;

    @Override
    public List<Appointment> getAppointments(Map<String, String> params) {
        return this.appointmentRepo.getAppointments(params);
    }

    @Override
    public Appointment getAppointmentById(Long id) {
        Appointment a = appointmentRepo.getAppointmentById(id);
        if (a == null) {
            throw new ResourceNotFoundException("Appointment not found!");
        }
        return a;
    }

    @Override
    @Transactional
    public AppointmentResponse registerAppointment(AppointmentCreateRequest req) {

        Patient patient = patientRepo.getPatientById(req.getPatientId());
        if (patient == null) {
            throw new ResourceNotFoundException("Không tìm thấy bệnh nhân!");
        }

        Schedule schedule = scheduleRepo.getScheduleById(req.getScheduleId());
        if (schedule == null) {
            throw new ResourceNotFoundException("Không tìm thấy lịch khám!");
        }

        boolean isAlreadyBooked = appointmentRepo.isPatientAlreadyBookedInSchedule(req.getPatientId(), req.getScheduleId());
        if (isAlreadyBooked) {
            throw new DuplicateResourceException("Bạn đã đăng ký khám ca này rồi!");
        }

        if (isDuplicateTimeAppointment(patient.getId(), req.getScheduleId())) {
            throw new DuplicateResourceException("Bạn đã có lịch hẹn vào khung giờ này rồi!");
        }

        int rowsUpdated = scheduleRepo.incrementCurrentPatients(req.getScheduleId());

        if (rowsUpdated == 0) {
            throw new FullSlotException("Rất tiếc, ca khám này đã đủ số lượng người đăng ký!");
        }

        Appointment appointment = appointmentMapper.toEntity(req, patient, schedule);
        appointmentRepo.addOrUpdateAppointment(appointment);


        try {
            if (req.getConversationId() != null) {
                var conv = conversationRepo.getConversationById(req.getConversationId());
                if (conv != null) {
                    appointment.setConversationId(conv);
                    appointmentRepo.addOrUpdateAppointment(appointment);
                }
            }
        } catch (Exception e) {
            System.err.println("Failed linking conversation to appointment: " + e.getMessage());
        }

        Payment p = paymentService.createPayment(appointment.getId());
        itemService.addAppointmentItem(p, appointment.getId());

        return appointmentMapper.toResponse(appointment);

    }

    @Override
    public long countAppointments(Map<String, String> params) {
        return appointmentRepo.countAppointments(params);
    }

    private java.time.LocalDate[] parseMonth(String month) {
        if (month == null || month.isEmpty()) {
            return null;
        }
        try {
            YearMonth ym = YearMonth.parse(month);
            return new java.time.LocalDate[]{ym.atDay(1), ym.atEndOfMonth()};
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<DoctorRankingResponse> getTopDoctorsByAppointmentCount(int limit, String month) {
        java.time.LocalDate[] range = parseMonth(month);
        if (range == null) {
            return appointmentRepo.getTopDoctorsByAppointmentCount(limit, null, null);
        }
        return appointmentRepo.getTopDoctorsByAppointmentCount(limit, range[0], range[1]);
    }

    @Override
    public List<DoctorRankingResponse> getTopDoctorsByConvertedAppointmentCount(int limit, String month) {
        java.time.LocalDate[] range = parseMonth(month);
        if (range == null) {
            return appointmentRepo.getTopDoctorsByConvertedAppointmentCount(limit, null, null);
        }
        return appointmentRepo.getTopDoctorsByConvertedAppointmentCount(limit, range[0], range[1]);
    }

    @Override
    public boolean doctorConfirmAppointment(Long appointmentId) {
        Appointment appointment = appointmentRepo.getAppointmentById(appointmentId);
        if (appointment == null) {
            throw new ResourceNotFoundException("Appointment not found!");
        }

        if (appointment.getStatus() != AppointmentStatus.PENDING) {
            return false;
        }

        appointment.setStatus(AppointmentStatus.CONFIRMED);
        Appointment saved = appointmentRepo.addOrUpdateAppointment(appointment);
        try {
            if (saved != null && saved.getPatientId().getUserId() != null) {

                User patientUser = saved.getPatientId().getUserId();
                if (patientUser != null) {
                    Map<String, String> notiParams = new HashMap<>();
                    notiParams.put("username", patientUser.getUsername());
                    notiParams.put("title", "Lịch khám đã được xác nhận!");
                    notiParams.put("content", "Lịch hẹn khám của bạn đã được bác sĩ xác nhận. Vui lòng kiểm tra!");
                    notiParams.put("path", "/appointments/" + saved.getId().toString());
                    this.notificationService.addNotification(notiParams);
                }
            }
        } catch (Exception e) {
            System.err.println("Lỗi gửi thông báo: " + e.getMessage());
        }
        return true;
    }

    @Override
    public boolean doctorStartAppointment(Long appointmentId) {
        Appointment appointment = appointmentRepo.getAppointmentById(appointmentId);
        if (appointment == null) {
            throw new ResourceNotFoundException("Appointment not found!");
        }

        if (appointment.getStatus() != AppointmentStatus.CONFIRMED) {
            return false;
        }

        appointment.setStatus(AppointmentStatus.IN_PROGRESS);
        appointmentRepo.addOrUpdateAppointment(appointment);
        return true;
    }

    @Override
    public List<Appointment> getAppointmentsByPatientId(Long patientId, Map<String, String> params) {
        return appointmentRepo.getAppointmentByPatientId(patientId, params);
    }

    @Override
    public void cancelAppointment(Long appointmentId, User u) {
        Appointment appointment = appointmentRepo.getAppointmentById(appointmentId);
        if (appointment == null) {
            throw new ResourceNotFoundException("Lịch hẹn không tồn tại");
        }
        Long ownerUserId = appointment.getPatientId().getUserId().getId();
        if (!ownerUserId.equals(u.getId())) {
            throw new ForbiddenException("Bạn không có quyền hủy lịch hẹn của người khác.");
        }
        AppointmentStatus status = appointment.getStatus();

        if (status == AppointmentStatus.COMPLETED || status == AppointmentStatus.CANCELLED) {
            throw new BadRequestException("Không thể thao tác. Lịch hẹn này đã hoàn thành hoặc đã bị hủy từ trước.");
        }

        LocalTime appointmentTime = appointment.getScheduleId().getShiftId().getStartTime();
        LocalDate appointmentDate = appointment.getScheduleId().getDate();
        LocalDateTime appointmentDateTime = LocalDateTime.of(appointmentDate, appointmentTime);
        LocalDateTime currentTime = LocalDateTime.now();

        boolean isTooLateToCancel = currentTime.plusHours(2).isAfter(appointmentDateTime)
                || currentTime.plusHours(2).isEqual(appointmentDateTime);

        if (status != AppointmentStatus.UN_PAID && isTooLateToCancel) {
            throw new BadRequestException("Lịch hẹn đã thanh toán chỉ được phép hủy trước giờ khám ít nhất 2 tiếng.");
        }

        if (status == AppointmentStatus.UN_PAID) {
            Payment payment = paymentService.getPaymentByAppoint(appointment.getId());
            paymentService.deletePayment(payment.getId());
        }

        appointment.setStatus(AppointmentStatus.CANCELLED);
        this.scheduleRepo.decrementCurrentPatients(appointment.getScheduleId().getId());
        appointmentRepo.addOrUpdateAppointment(appointment);

    }

    @Override
    public boolean isDuplicateTimeAppointment(Long patientId, Long scheduleId) {
        Map<String, String> params = new HashMap<>();
        List<Appointment> appointments = this.getAppointmentsByPatientId(patientId, params);

        LocalDate newDate = scheduleRepo.getScheduleById(scheduleId).getDate();
        Long newShiftId = scheduleRepo.getScheduleById(scheduleId).getShiftId().getId();

        for (Appointment oldApp : appointments) {

            if (oldApp.getStatus() == AppointmentStatus.CANCELLED) {
                continue;
            }

            LocalDate oldDate = oldApp.getScheduleId().getDate();
            Long oldShiftId = oldApp.getScheduleId().getShiftId().getId();

            if (newDate.equals(oldDate) && newShiftId.equals(oldShiftId)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void updateStatusAppointment(Long appointmentId, AppointmentStatus status) {
        Appointment appointment = appointmentRepo.getAppointmentById(appointmentId);
        if (appointment == null) {
            throw new ResourceNotFoundException("Không tìm thấy lịch hẹn");
        }
        
        appointment.setStatus(status);
        appointmentRepo.addOrUpdateAppointment(appointment);
    }

}
