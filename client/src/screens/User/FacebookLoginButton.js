import "./SocialLoginButtons.css";
import { useContext, useState } from 'react';
import { Button } from 'react-bootstrap';
import { useNavigate } from 'react-router-dom';
import { jwtDecode } from 'jwt-decode';
import cookies from 'react-cookies';
import API, { AUTH_ENDPOINTS, authApis, USER_ENDPOINTS } from '../../configs/Apis';
import { MyUserContext } from '../../configs/Contexts';

const FacebookLoginButton = () => {
    const nav = useNavigate();
    const [, dispatch] = useContext(MyUserContext);
    const [error, setError] = useState('');

    const responseFacebook = async (response) => {
        if (!response?.accessToken) return;

        try {
            let deviceId = localStorage.getItem('deviceId');
            if (!deviceId) {
                deviceId = crypto?.randomUUID ? crypto.randomUUID() : Math.random().toString(36).slice(2) + Date.now();
                localStorage.setItem('deviceId', deviceId);
            }
            const deviceInfo = navigator.userAgent;

            const res = await API.post(AUTH_ENDPOINTS.FACEBOOK_LOGIN, {
                token: response.accessToken,
                deviceId: deviceId,
                deviceInfo: deviceInfo
            });

            cookies.save('accessToken', res.data.accessToken, { path: '/' });
            if (res.data.refreshToken) cookies.save('refreshToken', res.data.refreshToken, { path: '/' });

            const currentUser = await authApis().get(USER_ENDPOINTS.CURRENT_USER);
            localStorage.setItem("user", JSON.stringify(currentUser.data));
            dispatch({ type: 'LOGIN', payload: currentUser.data });

            const decoded = jwtDecode(res.data.accessToken);
            if (decoded.role === 'ROLE_DOCTOR') nav('/doctor/dashboard');
            else nav('/patient/dashboard');
        } catch (err) {
            setError('Đăng nhập Facebook thất bại!');
            console.error('Lỗi xác thực Facebook:', err);
        }
    };

    const isSecureOrigin =
        window.location.protocol === 'https:' ||
        window.location.hostname === 'localhost' ||
        window.location.hostname === '127.0.0.1';

    return (
        <>
            <Button
                className="social-btn facebook"
                onClick={() => {
                    if (!isSecureOrigin) {
                        setError('HTTPS only');
                        return;
                    }
                    if (!window.FB) {
                        setError('Facebook SDK chưa sẵn sàng hoặc bị chặn.');
                        return;
                    }
                    window.FB.login(function (resp) {
                        if (resp.authResponse && resp.authResponse.accessToken) {
                            responseFacebook({ accessToken: resp.authResponse.accessToken });
                        } else {
                            setError('Đăng nhập Facebook không thành công.');
                        }
                    }, { scope: 'email,public_profile' });
                }}
                disabled={!isSecureOrigin}
                aria-label="Đăng nhập bằng Facebook"
            >
                <svg viewBox="0 0 24 24" fill="white">
                    <path d="M24 12.073c0-6.627-5.373-12-12-12s-12 5.373-12 12c0 5.99 4.388 10.954 10.125 11.854v-8.385H7.078v-3.47h3.047V9.43c0-3.007 1.792-4.669 4.533-4.669 1.312 0 2.686.235 2.686.235v2.953H15.83c-1.491 0-1.956.925-1.956 1.874v2.25h3.328l-.532 3.47h-2.796v8.385C19.612 23.027 24 18.062 24 12.073z"></path>
                </svg>
            </Button>

            <div style={{ position: 'absolute', top: '100%', left: 0, width: '100%', zIndex: 10 }}>
                {!isSecureOrigin && (
                    <div className="alert alert-warning mt-2 small p-2" style={{ fontSize: '12px' }}>
                        Facebook yêu cầu HTTPS/localhost.
                    </div>
                )}
                {error && (
                    <div className="alert alert-danger mt-2 small p-2" style={{ fontSize: '12px' }}>
                        {error}
                    </div>
                )}
            </div>
        </>
    );
};

export default FacebookLoginButton;