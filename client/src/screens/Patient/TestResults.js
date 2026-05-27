import { Table, Container, Row, Col, Button, Stack } from "react-bootstrap";
import Header from "../../components/Header";
import { useContext, useEffect, useState } from "react";
import { MyUserContext } from "../../configs/Contexts";
import { authApis, endpoint } from "../../configs/Apis";
import { useNavigate } from "react-router-dom";
import ProfileCard from "./components/ProfileCard";
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
            <div className="d-flex flex-column min-vh-100">
                <Header />
            <h3 className="text-center">Lịch sử xét nghiệm</h3>

            <Container className="mt-4">
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
                                            onClick={() => nav(`/patient/test-results/${profile.id}`)}
                                            
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

export default TestResults;

