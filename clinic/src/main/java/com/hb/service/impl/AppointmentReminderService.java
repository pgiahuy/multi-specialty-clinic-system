/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.service.impl;

import com.hb.pojo.Appointment;
import com.hb.pojo.Patient;
import com.hb.pojo.Schedule;
import com.hb.pojo.Shift;
import com.hb.repository.AppointmentRepository;
import com.hb.service.NotificationService;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author HUY
 */
@Service
public class AppointmentReminderService {

    @Autowired
    private AppointmentRepository appointmentRepo;

    @Autowired
    private NotificationService notificationService;

//    @Scheduled(fixedRateString = "1800000")
    @Scheduled(fixedRateString = "60000")
    @Transactional
    public void sendAppointmentReminders() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime reminderStart = now.plusHours(24);
        LocalDateTime reminderEnd = reminderStart.plusMinutes(30);

        List<Appointment> appointments = appointmentRepo.getConfirmedAppointmentsForReminder(reminderStart, reminderEnd);
        if (appointments == null || appointments.isEmpty()) {
            return;
        }

        for (Appointment appointment : appointments) {
            try {
                if (appointment == null) {
                    continue;
                }

                Patient patient = appointment.getPatientId();
                if (patient == null || patient.getUserId() == null || patient.getUserId().getUsername() == null) {
                    continue;
                }

                Schedule schedule = appointment.getScheduleId();
                if (schedule == null) {
                    continue;
                }

                String appointmentDate = schedule.getDate() != null
                        ? schedule.getDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                        : "ngày đã đặt";
                Shift shift = schedule.getShiftId();
                String timeSlot = shift != null && shift.getStartTime() != null && shift.getEndTime() != null
                        ? shift.getStartTime() + " - " + shift.getEndTime()
                        : "thời gian chưa xác định";
                String doctorName = schedule.getDoctorId() != null
                        ? schedule.getDoctorId().getFullName()
                        : "bác sĩ của bạn";

                Map<String, String> notiParams = new HashMap<>();
                notiParams.put("username", patient.getUserId().getUsername());
                notiParams.put("title", "Nhắc lịch khám ngày mai");
                notiParams.put("content", String.format("Bạn có lịch khám với %s vào %s lúc %s. Vui lòng chuẩn bị và đến đúng giờ.", doctorName, appointmentDate, timeSlot));
                notiParams.put("path", "/appointments/" + appointment.getId());

                notificationService.addNotification(notiParams);
                appointment.setReminderSent(true);
                appointmentRepo.addOrUpdateAppointment(appointment);
            } catch (Exception e) {
                System.err.println("Lỗi gửi nhắc lịch hẹn: " + e.getMessage());
            }
        }
    }
}
