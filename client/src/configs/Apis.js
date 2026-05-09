import axios from "axios";

import cookies from 'react-cookies'

const API = axios.create({
    baseURL: "http://localhost:8080/clinic/api/"
})


export const getProducts = () => API.get("/products");
export const getCategories = () => API.get("/categories");

export const endpoint = {
    'register': 'auth/register',
    'login': 'auth/login',
    'patientProfiles': '/secure/profiles',
    'notifications': '/secure/users/notifications',

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