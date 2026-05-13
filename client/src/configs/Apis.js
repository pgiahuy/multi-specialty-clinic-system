import axios from "axios";

import cookies from 'react-cookies'

const API = axios.create({
    baseURL: "http://localhost:8080/clinic/api/"
})


export const getDoctors = () => API.get("/z");
export const getSpecialties = () => API.get("/specialties");
export const getSchedule = () => API.get("/schedules");



export const endpoint = {
    'register': 'auth/register',
    'login': 'auth/login',
    'patientProfiles': '/secure/profiles',
    'doctors': '/doctors',
    'schedules': '/schedules',
    'notifications': '/secure/users/notifications',
    'current-user': '/secure/users/profile',
};

export const authApis = () => {
    return axios.create({
        baseURL: "http://localhost:8080/clinic/api/",
        headers: {
            'Authorization': `Bearer ${cookies.load("token")}`
        }
    });
}

export default API;