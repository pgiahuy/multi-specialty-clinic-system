import { initializeApp } from "firebase/app";
import { getMessaging, getToken, onMessage } from "firebase/messaging";
import { getDatabase } from "firebase/database";

const firebaseConfig = {
    apiKey: "AIzaSyDsDX-j9Jom_u_ccVvm4Q6RcTQ6wNicC2M",
    authDomain: "multi-specialty-clinic-system.firebaseapp.com",
    databaseURL: "https://multi-specialty-clinic-system-default-rtdb.asia-southeast1.firebasedatabase.app",
    projectId: "multi-specialty-clinic-system",
    storageBucket: "multi-specialty-clinic-system.firebasestorage.app",
    messagingSenderId: "1062156140412",
    appId: "1:1062156140412:web:7aa4ab550f894f423aaef9",
    measurementId: "G-MSZ3NFSKZ9"
};

const app = initializeApp(firebaseConfig);
const messaging = getMessaging(app);
const db = getDatabase(app);

export const requestForToken = () => {
    return getToken(messaging, { vapidKey: "BHzjcKnvfNLBpVhFYgY64BBnVK3ElRPl_PirgJvVExgr6pvoDd3B6oObKwYgGOcab0dWrRWLGGDYHOZUzHslsPY" })
        .then((currentToken) => {
            if (currentToken) {
                return currentToken;
            }
        })
        .catch((err) => console.log('Lỗi lấy token:', err));
};

export const onMessageListener = () =>
    new Promise((resolve) => {
        onMessage(messaging, (payload) => {
            resolve(payload);
        });
    });

export { db };