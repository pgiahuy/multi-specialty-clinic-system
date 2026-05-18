import { Table, Container, Row, Col, Button, Stack } from "react-bootstrap";
import Header from "../../components/Header";
import { useContext, useEffect, useState } from "react";
import { MyUserContext } from "../../configs/Contexts";
import { authApis, endpoint } from "../../configs/Apis";
import { useNavigate } from "react-router-dom";
import PatientProfileCard from "./components/ProfileCard";
import { Eye } from 'react-bootstrap-icons';
import Footer from "../../components/Footer";

const TestResults = () => {

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

    const viewResults = (profile) => {
        console.log("View test results for:", profile);
        // TODO: Filter results or navigate with profile ID
    };

    if (!user) {
        nav('/login');
    }

    return (
        <>
            <Header />
            <h3 className="text-center">Lịch sử xét nghiệm</h3>

            <Container className="mt-4">
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
                                            onClick={() => nav(`/patient/test-results/${profile.id}`)}
                                            style={{ borderRadius: '5px', padding: '4px 8px', border: 'none' }}
                                            title="Xem kết quả"
                                        >
                                            Lịch sử xét nghiệm
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

export default TestResults;

