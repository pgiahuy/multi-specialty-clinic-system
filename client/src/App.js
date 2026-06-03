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
import PatientAccount from "./screens/Patient/PatientAccount";
import Prescriptions from "./screens/Patient/Prescriptions";
import AllNotifications from "./screens/User/AllNotifications";
import BookingPage from "./screens/Patient/Booking";
import { MyUserContext } from "./configs/Contexts";
import { useReducer } from "react";
import MyUserReducers from "./reducers/MyUserReducers";
import RegisterRecord from "./screens/Patient/RegisterRecord";
import PaymentDetail from "./screens/Patient/PaymentDetail";
import TestResults from "./screens/Patient/TestResults";
import PaymentResult from "./screens/Patient/PaymentResult";
import HistoryBooking from "./screens/Patient/BookingHistory";
import ListDoctor from "./screens/Home/ListDoctor";
import Contact from "./screens/Home/Contact";
import AppointmentList from "./screens/Doctor/AppointmentList";
import DoctorProfile from "./screens/Doctor/DoctorProfile";
import RegisterSchedule from "./screens/Doctor/RegisterSchedule";
import MedicalRecord from "./screens/Doctor/MedicalRecord";
import AssignTest from "./screens/Doctor/AssignTest";
import DoctorDetail from "./screens/Doctor/DoctorDetail";
import CreateMedicalRecord from "./screens/Doctor/CreateMedicalRecord";
import PrescribeMedicine from "./screens/Doctor/PrescribeMedicine";
import PatientList from "./screens/Doctor/PatientList";
import AppointmentDetail from "./screens/Patient/AppointmentDetail";
import ScheduleManagement from "./screens/Doctor/ScheduleManagement";
import ProtectedRoute from "./components/ProtectedRoute";
import AppointmentOfSchedule from "./screens/Doctor/AppointmentOfSchedule";
import LabTest from "./screens/Doctor/LabTest";
import MessageBox from "./screens/User/MessageBox";
import ConsultationPage from "./screens/Doctor/ConsultationPage";
import TestResultDetail from "./screens/Patient/TestResultDetail";
import ReceptionAppointments from "./screens/Reception/ReceptionAppointments";
import ReceptionPayment from "./screens/Reception/ReceptionPayment";
import ReceptionInvoices from "./screens/Reception/ReceptionInvoices";
import ReceptionPrescriptions from "./screens/Reception/ReceptionPrescriptions";
import ReceptionPrescriptionDetail from "./screens/Reception/ReceptionPrescriptionDetail";
import StoreKeeper from "./screens/StoreKeeper/StoreKeeper";
import StoreKeeperHome from "./screens/StoreKeeper/StoreKeeperHome";

const initUserState = () => {
  const savedUser = localStorage.getItem("user");
  return savedUser ? JSON.parse(savedUser) : null;
};

function App() {
  const [user, dispatch] = useReducer(MyUserReducers, null, initUserState);

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
            <Route path="/contact" element={
              <>
                <Header />
                <Container>
                  <Contact />
                </Container>
                <Footer />
              </>
            } />
            <Route path="/login" element={<Login />} />
            <Route path="/register" element={<Register />} />



            <Route element={<ProtectedRoute allowedRoles={["ROLE_PATIENT"]} />}>
              <Route path="/patient/dashboard" element={<PatientDashboard />} />
              <Route path="/patient/notifications" element={<AllNotifications />} />
              <Route path="/patient/prescriptions/:prescriptionId?" element={<Prescriptions />} />
              <Route path="/patient/profiles" element={<PatientProfile />} />
              <Route path="/patient/account" element={<PatientAccount />} />
              <Route path="/patient/register-record" element={<RegisterRecord />} />
              <Route path="/patient/booking" element={<BookingPage />} />
              <Route path="/patient/history-booking" element={<HistoryBooking />} />
              <Route path="/patient/test-results" element={<TestResults />} />
              <Route path="/patient/test-results/:labResultId" element={<TestResultDetail />} />
              <Route path="/patient/payment-result" element={<PaymentResult />} />
              <Route path="/patient/chat" element={<MessageBox />} />
              <Route path="/patient/payments" element={<PaymentDetail />} />
            </Route>



            <Route element={<ProtectedRoute allowedRoles={["ROLE_DOCTOR", "ROLE_STAFF"]} />}>
              <Route path="/create-medical-record" element={<CreateMedicalRecord />} />
            </Route>

            <Route element={<ProtectedRoute allowedRoles={["ROLE_DOCTOR"]} />}>
              <Route path="/doctor/dashboard" element={<DoctorDashboard />} />
              <Route path="/doctor/schedules" element={<ScheduleManagement />} />
              <Route path="/doctor/:scheduleId/appointments" element={<AppointmentOfSchedule />} />
              <Route path="/doctor/appointments" element={<AppointmentList />} />
              <Route path="/doctor/profile" element={<DoctorProfile />} />
              <Route path="/doctor/register-schedule" element={<RegisterSchedule />} />
              <Route path="/doctor/assign-test/:appointmentId" element={<AssignTest />} />
              <Route path="/doctor/prescribe/:medicalRecordId" element={<PrescribeMedicine />} />
              <Route path="/doctor/patients" element={<PatientList />} />
              <Route path="/doctor/lab-tests" element={<LabTest />} />
            </Route>

            <Route element={<ProtectedRoute allowedRoles={["ROLE_DOCTOR", "ROLE_PATIENT", "ROLE_STAFF"]} />}>
              <Route path="/appointments/:appointmentId/medical-record" element={<MedicalRecord />} />
            </Route>

            <Route element={<ProtectedRoute />}>
              <Route path="/doctor/consultations" element={<ConsultationPage />} />
              <Route path="/doctors" element={<ListDoctor />} />
              <Route path="/appointments/:appointmentId" element={<AppointmentDetail />} />
            </Route>

            <Route element={<ProtectedRoute allowedRoles={["ROLE_STAFF"]} />}>
              <Route path="/reception" element={<ReceptionAppointments />} />
              <Route path="/reception/invoices" element={<ReceptionInvoices />} />
              <Route path="/reception/payments/:appointmentId" element={<ReceptionPayment />} />
              <Route path="/reception/prescriptions" element={<ReceptionPrescriptions />} />
              <Route path="/reception/prescriptions/:prescriptionId" element={<ReceptionPrescriptionDetail />} />
            </Route>

            <Route element={<ProtectedRoute allowedRoles={["ROLE_STOREKEEPER", "ROLE_STAFF"]} />}>
              <Route path="/storekeeper" element={<StoreKeeper />} />
              <Route path="/storekeeper/home" element={<StoreKeeperHome />} />
            </Route>

            <Route path="/doctor/detail/:doctorId" element={<DoctorDetail />} />


          </Routes>

        </Container>
      </BrowserRouter>
    </MyUserContext.Provider>

  );
}

export default App;
