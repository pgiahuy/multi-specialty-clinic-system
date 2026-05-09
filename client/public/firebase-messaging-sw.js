importScripts("https://www.gstatic.com/firebasejs/9.0.0/firebase-app-compat.js");
importScripts("https://www.gstatic.com/firebasejs/9.0.0/firebase-messaging-compat.js");

firebase.initializeApp({
    apiKey: "AIzaSyDsDX-j9Jom_u_ccVvm4Q6RcTQ6wNicC2M",
    authDomain: "multi-specialty-clinic-system.firebaseapp.com",
    projectId: "multi-specialty-clinic-system",
    storageBucket: "multi-specialty-clinic-system.firebasestorage.app",
    messagingSenderId: "1062156140412",
    appId: "1:1062156140412:web:7aa4ab550f894f423aaef9"
});

const messaging = firebase.messaging();
const channel = new BroadcastChannel('fcm_notifications');

messaging.onBackgroundMessage((payload) => {
    console.log('Nhận thông báo ngầm:', payload);

    channel.postMessage(payload);

    const notificationTitle = payload.notification.title;
    const notificationOptions = {
        body: payload.notification.body,
        icon: '/logo192.png'
    };

    self.registration.showNotification(notificationTitle, notificationOptions);
});