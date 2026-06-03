import { useContext } from 'react';
import { Navigate, Outlet, useLocation } from 'react-router-dom';
import { MyUserContext } from '../configs/Contexts';

const ProtectedRoute = ({ allowedRoles = [], redirectTo = '/login' }) => {
  const [user] = useContext(MyUserContext);
  const location = useLocation();

  if (!user) {
    return <Navigate to={redirectTo} replace state={{ from: location }} />;
  }

  if (allowedRoles.length > 0 && !allowedRoles.includes(user.role)) {
    let fallbackPath = '/';
    if (user.role === 'ROLE_DOCTOR') {
      fallbackPath = '/doctor/dashboard';
    } else if (user.role === 'ROLE_PATIENT') {
      fallbackPath = '/patient/dashboard';
    } else if (user.role === 'ROLE_STAFF') {
      fallbackPath = '/reception';
    } else if (user.role === 'ROLE_STOREKEEPER') {
      fallbackPath = '/storekeeper';
    }
    return <Navigate to={fallbackPath} replace />;
  }

  return <Outlet />;
};

export default ProtectedRoute;
