import { Alert, Container } from "react-bootstrap";
import API, { authApis, endpoint } from "../../configs/Apis";
import { useEffect, useState } from "react";
import Header from "../../components/Header";
import ControlCard from "./components/ControlCard";
import Footer from "../../components/Footer";

const PatientDashboard = () => {

    return (
        <>
            <div className="d-flex flex-column min-vh-100 bg-light">
                <Header />
                <Container>
                    <ControlCard />
                </Container>
                <Footer />
            </div>
        </>
    );
};

export default PatientDashboard;
