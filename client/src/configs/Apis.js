import axios from "axios";
import { Shift } from "react-bootstrap-icons";

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

let refreshPromise = null;


export const getDoctors = () => API.get("/z");
export const getSpecialties = (params = {}) => API.get("/specialties", { params });
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
    'medical-record-update': (medicalRecordId) => `/secure/medical-records/${medicalRecordId}`,
    'test': (testId) => `/secure/test/${testId}`,
};
export const AUTH_ENDPOINTS = {
    REGISTER: 'auth/register',
    LOGIN: 'auth/login',
    FACEBOOK_LOGIN: 'auth/facebook',
    GOOGLE_LOGIN: 'auth/google',
    REFRESH_TOKEN: 'auth/refresh',
    LOGOUT: 'auth/logout'
};

export const CLINIC_ENDPOINTS = {
    DOCTORS: 'doctors',
    PATIENTS: 'patients',
    SPECIALTIES: 'specialties',
    ROOMS: 'rooms',
    SHIFTS: 'shifts',
    SCHEDULES: 'secure/schedules',
    DOCTOR_APPOINTMENTS: (scheduleId) => `secure/appointments?scheduleId=${scheduleId}`,
    DOCTOR_CONFIRM_APPOINTMENT: (appointmentId) => `secure/appointments/${appointmentId}/confirm`,
    PATIENT_BOOKING_APPOINTMENT: 'secure/appointments',
    TEST_RESULTS: (patientId) => `secure/test/${patientId}`,
};



export const USER_ENDPOINTS = {
    CURRENT_USER: 'secure/users/profile',
    NOTIFICATIONS: 'secure/users/notifications',
    PATIENT_PROFILES: 'secure/profiles',
    PATIENT_PROFILE_DETAIL: (patientId) => `secure/profile/${patientId}`,
};

export const PAYMENT_ENDPOINTS = {
    CREATE: 'secure/payments/create',
    MOMO_RETURN: 'secure/payments/momo/return',
    HISTORY: (patientId) => `secure/payments/${patientId}`,
    ITEMS: (paymentId) => `secure/payment-items/${paymentId}`,
};


export const clinicApis = {
    getDoctors: () => API.get(CLINIC_ENDPOINTS.DOCTORS),
    getSpecialties: (params = {}) => API.get(CLINIC_ENDPOINTS.SPECIALTIES, { params }),
    getSchedule: () => authApis.get(CLINIC_ENDPOINTS.SCHEDULES),
    getDoctorAppointments: (scheduleId) => authApis().get(CLINIC_ENDPOINTS.APPOINTMENTS(scheduleId)),
    getShifts: () => API.get(CLINIC_ENDPOINTS.SHIFTS),
};



const setupResponseInterceptor = (instance) => {

    instance.interceptors.response.use(
        response => response,
        async error => {
            const originalRequest = error.config;
            if (!originalRequest || originalRequest.url?.includes("auth/refresh") || originalRequest.url?.includes("auth/login")) {
                return Promise.reject(error);
            }

            if (error.response?.status === 401 && !originalRequest._retry) {
                originalRequest._retry = true;

                console.debug("Auth interceptor: 401 detected for", originalRequest.method, originalRequest.url);
                const currentRefresh = cookies.load("refreshToken");
                console.debug("Auth interceptor: cookies present?", { access: !!cookies.load("accessToken"), refresh: !!currentRefresh });

                if (!refreshPromise) {
                    console.debug("Auth interceptor: sending refresh request to", `${getBaseURL()}auth/refresh`);
                    refreshPromise = axios.post(`${getBaseURL()}auth/refresh`, {
                        refreshToken: currentRefresh
                    }).then((res) => {
                        const { accessToken, refreshToken } = res.data;
                        if (accessToken) cookies.save("accessToken", accessToken, { path: '/' });
                        if (refreshToken) cookies.save("refreshToken", refreshToken, { path: '/' });

                        const short = t => t ? `${t.slice(0, 8)}...` : null;
                        console.debug("Auth interceptor: refresh successful", { accessToken: short(accessToken), refreshToken: short(refreshToken) });


                        const header = `Bearer ${accessToken}`;
                        instance.defaults.headers = instance.defaults.headers || {};
                        instance.defaults.headers.common = instance.defaults.headers.common || {};
                        instance.defaults.headers.common['Authorization'] = header;
                        API.defaults.headers = API.defaults.headers || {};
                        API.defaults.headers.common = API.defaults.headers.common || {};
                        API.defaults.headers.common['Authorization'] = header;

                        return accessToken;
                    }).catch((refreshError) => {
                        console.debug("Auth interceptor: refresh failed", refreshError);
                        cookies.remove("accessToken");
                        cookies.remove("refreshToken");
                        window.location.href = "/login";
                        throw refreshError;
                    }).finally(() => {
                        refreshPromise = null;
                    });
                }

                try {
                    const accessToken = await refreshPromise;
                    if (accessToken) {
                        const tokenHeader = `Bearer ${accessToken}`;
                        originalRequest.headers = originalRequest.headers || {};

                        originalRequest.headers['Authorization'] = tokenHeader;
                        originalRequest.headers['authorization'] = tokenHeader;


                        instance.defaults.headers = instance.defaults.headers || {};
                        instance.defaults.headers.common = instance.defaults.headers.common || {};
                        instance.defaults.headers.common['Authorization'] = tokenHeader;
                        API.defaults.headers = API.defaults.headers || {};
                        API.defaults.headers.common = API.defaults.headers.common || {};
                        API.defaults.headers.common['Authorization'] = tokenHeader;

                        console.debug("Auth interceptor: retrying original request", originalRequest.method, originalRequest.url);
                        return instance(originalRequest);
                    }
                } catch (refreshError) {
                    return Promise.reject(refreshError);
                }
            }
            return Promise.reject(error);
        }
    );
};

setupResponseInterceptor(API);

export const authApis = () => {
    const instance = axios.create({
        baseURL: getBaseURL()
    });

    instance.interceptors.request.use(config => {
        const token = cookies.load("accessToken");
        if (token) {
            config.headers.Authorization = `Bearer ${token}`;
        }
        return config;
    });

    setupResponseInterceptor(instance);

    return instance;
}

export default API;