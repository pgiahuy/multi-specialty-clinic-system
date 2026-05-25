import { Button, Col, Container, Row, Stack } from "react-bootstrap";
import Header from "../../components/Header";
import PatientProfileCard from "./components/ProfileCard";
import Footer from "../../components/Footer";
import { useContext, useEffect, useState } from "react";
import { MyUserContext } from "../../configs/Contexts";
import { useNavigate } from "react-router-dom";
import { authApis, endpoint } from "../../configs/Apis";

const Payment = () => {
    const [user] = useContext(MyUserContext);
    const [patientProfiles, setPatientProfiles] = useState([]);
    const nav = useNavigate();

    const loadPatientProfiles = async () => {
        try {
            const res = await authApis().get(endpoint['patientProfiles']);
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
            <Header />


            <Container className="mt-4">
                <div>
                    <h4 className="fw-bold mb-0 text-dark">
                        Danh sách hồ sơ bệnh nhân
                    </h4>

                </div>
                <Row>
                    <Col>
                        {patientProfiles.length > 0 ? (
                            patientProfiles.map((profile) => (
                                <div key={profile.id} className="mb-3">
                                    <PatientProfileCard patient={profile} />
                                    <Stack direction="horizontal" gap={2} style={{ justifyContent: 'flex-end', paddingRight: '15px' }}>
                                        <Button
                                            variant="outline-primary"
                                            size="sm"
                                            onClick={() => nav(`/patient/payment/${profile.id}`)}
                                            style={{ borderRadius: '5px', padding: '4px 8px', border: 'none' }}
                                            title="Xem kết quả"
                                        >
                                            Xem hóa đơn
                                        </Button>
                                    </Stack>
                                </div>
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
        </>
    );

};

export default Payment;