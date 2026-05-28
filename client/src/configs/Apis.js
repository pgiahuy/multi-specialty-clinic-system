import axios from "axios";

import cookies from 'react-cookies'

const getBaseURL = () => {
    if (window.location.hostname === "localhost" || window.location.hostname === "127.0.0.1") {
        return "http://localhost:8080/clinic/api/";
    }
    return "https://admin.triplehstorage.site/clinic/api/";
};

const API = axios.create({
    baseURL: getBaseURL()
});


export const getDoctors = () => API.get("/z");
export const getSpecialties = () => API.get("/specialties");
export const getSchedule = () => API.get("/schedules");



export const endpoint = {
    'register': 'auth/register',
    'login': 'auth/login',
    'facebook-login': '/auth/facebook',
    'patientProfiles': '/secure/profiles',
    'doctors': '/doctors',
    'schedules': '/secure/schedules',
    'notifications': '/secure/users/notifications',
    'current-user': '/secure/users/profile',
    'appointments': '/secure/appointments',
    'appointment': (appointmentId) => `/secure/appointment/${appointmentId}`,
    'create-payment': '/secure/payments/create',
    'momo-return': '/secure/payments/momo/return',
    'payments': (patientId) => `/secure/payments/${patientId}`,
    'payment-items': (paymentId) => `/secure/payment-items/${paymentId}`,
    'test-results': (patientId) => `/secure/tests/${patientId}`,
    'lab-test': '/secure/tests',
    'test-result-appointment': (appointmentId) => `/secure/tests/appointment/${appointmentId}`,
    'patient-profile': (patientId) => `/secure/profile/${patientId}`,
    'specialties': '/specialties',
    'medical-record': (appointmentId) => `/secure/medical-records/appointment/${appointmentId}`,
    'medical-record-update': (medicalRecordId) => `/secure/medical-records/${medicalRecordId}`
};

export const authApis = () => {
    return axios.create({
        baseURL: getBaseURL(),
        headers: {
            'Authorization': `Bearer ${cookies.load("token")}`
        }
    });
}

export default API;