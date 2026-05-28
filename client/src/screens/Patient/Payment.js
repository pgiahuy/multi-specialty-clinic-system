import { Button, Col, Container, Row, Stack } from "react-bootstrap";
import Header from "../../components/Header";
import ProfileCard from "./components/ProfileCard";
import Footer from "../../components/Footer";
import { useContext, useEffect, useState } from "react";
import { MyUserContext } from "../../configs/Contexts";
import { useNavigate } from "react-router-dom";
import { authApis, endpoint, USER_ENDPOINTS } from "../../configs/Apis";
import { Bullseye, Eye } from "react-bootstrap-icons";

const Payment = () => {
    const [user] = useContext(MyUserContext);
    const [patientProfiles, setPatientProfiles] = useState([]);
    const nav = useNavigate();

    const loadPatientProfiles = async () => {
        try {
            const res = await authApis().get(USER_ENDPOINTS.PATIENT_PROFILES);
            setPatientProfiles(res.data);
        } catch (err) {
            console.log(err);
        }
    };

    useEffect(() => {

        loadPatientProfiles();
    }, []);



    return (
        <>
            <div className="d-flex flex-column min-vh-100 bg-light">
                <Header />
                <Container className="mt-4 mb-5">
                    <div>
                        <h4 className="fw-bold mb-0 text-dark">
                            Danh sách hồ sơ bệnh nhân
                        </h4>

                    </div>
                    <Row>
                        <Col>
                            {patientProfiles.length > 0 ? (
                                patientProfiles.map((profile) => (

                                    <Row key={profile.id} className="mb-3 align-items-center">


                                        <Col md={11}>
                                            <ProfileCard patient={profile} />
                                        </Col>


                                        <Col md={1} className="text-md-end text-center mt-2 mt-md-0">
                                            <Button
                                                variant="outline-info"
                                                size="sm"
                                                onClick={() => nav(`/patient/payment/${profile.id}`)}

                                                title="Xem kết quả"
                                                className="mb-5 w-100 py-2 rounded-4"
                                            >
                                                <Eye />
                                            </Button>
                                        </Col>

                                    </Row>
                                ))
                            ) : (
                                <div className="text-center py-5 bg-light rounded-3 border-dashed">
                                    <p className="text-muted mb-0">Hiện chưa có hồ sơ nào.</p>
                                </div>
                            )}
                        </Col>
                    </Row>

                </Container>
                <Footer />
            </div>

        </>
    );

};

export default Payment;