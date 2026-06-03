package com.hb.dto.response;

public class DoctorRankingResponse {

    private Long doctorId;
    private String doctorFullName;
    private Long appointmentCount;

    public DoctorRankingResponse() {
    }

    public DoctorRankingResponse(Long doctorId, String doctorFullName, Long appointmentCount) {
        this.doctorId = doctorId;
        this.doctorFullName = doctorFullName;
        this.appointmentCount = appointmentCount;
    }

    public Long getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Long doctorId) {
        this.doctorId = doctorId;
    }

    public String getDoctorFullName() {
        return doctorFullName;
    }

    public void setDoctorFullName(String doctorFullName) {
        this.doctorFullName = doctorFullName;
    }

    public Long getAppointmentCount() {
        return appointmentCount;
    }

    public void setAppointmentCount(Long appointmentCount) {
        this.appointmentCount = appointmentCount;
    }
}
