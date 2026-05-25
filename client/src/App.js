import { BrowserRouter, Route, Routes } from "react-router-dom"
import './App.css';
import Header from "./components/Header"
import Footer from "./components/Footer"
import 'bootstrap/dist/css/bootstrap.min.css';
import Home from './screens/Home/Home';
import { Container } from "react-bootstrap";
import Login from "./screens/User/Login";
import Register from "./screens/User/Register";
import DoctorDashboard from "./screens/Doctor/DoctorDashboard";
import PatientDashboard from "./screens/Patient/PatientDashboard";
import PatientProfile from "./screens/Patient/PatientProfile";
import Prescriptions from "./screens/Patient/Prescriptions";
import AllNotifications from "./screens/User/AllNotifications";
import BookingPage from "./screens/Patient/Booking";
import { MyUserContext } from "./configs/Contexts";
import { useReducer } from "react";
import MyUserReducers from "./reducers/MyUserReducers";
import RegisterRecord from "./screens/Patient/RegisterRecord";
import TestResults from "./screens/Patient/TestResults";
import TestResultDetail from "./screens/Patient/TestResultDetail";


function App() {
  const [user, dispatch] = useReducer(MyUserReducers, null);
  return (
    <MyUserContext.Provider value={[user, dispatch]}>
      <BrowserRouter>
        <Container fluid className="m-0 p-0">
          <Routes>
            <Route path="/" element={
              <>
                <Header />

                <Container>
                  <Home />
                </Container>

                <Footer />
              </>
            } />
            <Route path="/login" element={<Login />} />
            <Route path="/register" element={<Register />} />
            <Route path="/doctor/dashboard" element={<DoctorDashboard />} />
            <Route path="/patient/dashboard" element={<PatientDashboard />} />
            <Route path="/patient/notifications" element={<AllNotifications />} />
            <Route path="/patient/prescriptions" element={<Prescriptions />} />
            <Route path="/patient/profiles" element={<PatientProfile />} />
            <Route path="/patient/register-record" element={<RegisterRecord />} />
            <Route path="/patient/booking" element={<BookingPage />} />
            <Route path="/patient/test-results" element={<TestResults />} />
            <Route path="/patient/test-results/:patientId" element={<TestResultDetail />} />

          </Routes>

        </Container>
      </BrowserRouter>
    </MyUserContext.Provider>

  );
}

export default App;
