import { Alert, Button, Container, Nav, Stack, Row, Col } from "react-bootstrap";
import API, { authApis, endpoint } from "../../configs/Apis";
import { useEffect, useState, useContext } from "react";
import Header from "../../components/Header";
import ControlCard from "./components/ControlCard";
import ProfileCard from "./components/ProfileCard";
import { Link, PlusLg, PencilSquare, Trash } from 'react-bootstrap-icons';
import { Navigate, useNavigate } from "react-router-dom";
import { MyUserContext } from "../../configs/Contexts";
import Footer from "../../components/Footer";

const PatientProfile = () => {
    const [user] = useContext(MyUserContext);
    const [patientProfiles, setPatientProfiles] = useState([]);
    const navigate = useNavigate();

    const loadPatientProfiles = async () => {
        try {
            const res = await authApis().get(endpoint['patientProfiles']);
            setPatientProfiles(res.data);
        } catch (err) {
            console.log(err);
        }
    };

    const handleEdit = (profile) => {
        // TODO: Implement edit functionality
        console.log("Edit profile:", profile);
        navigate(`/patient/register-record/${profile.id}`);
    };

    const handleDelete = (profile) => {
        if (window.confirm(`Bạn có chắc chắn muốn xóa hồ sơ của ${profile.fullName} không?`)) {
            // TODO: Implement delete functionality via API
            console.log("Delete profile:", profile);
        }
    };

    useEffect(() => {
        loadPatientProfiles();
    }, []);

    if (!user) {
        return <Navigate to="/login" />;
    }

    return (
        <>
            <div className="d-flex flex-column min-vh-100">
                <Header />

                <Container style={{ width: '80%' }} className="mt-3">

                    <Stack direction="horizontal" gap={3} className="mb-4 align-items-end border-bottom pb-3">
                        <div>
                            <h4 className="fw-bold mb-0 text-dark">
                                Danh sách hồ sơ sức khỏe
                            </h4>

                        </div>

                        <Button
                            variant="primary"
                            className="ms-auto d-flex align-items-center gap-2 shadow-sm py-2 px-3"
                            style={{ borderRadius: '10px' }}
                            onClick={() => navigate('/patient/register-record')}
                        >
                            <PlusLg /> <span>Thêm hồ sơ mới</span>
                        </Button>
                    </Stack>


                    <Row>
                        <Col>
                            {patientProfiles.length > 0 ? (
                                patientProfiles.map((profile) => (
                                    
                                    <Row key={profile.id} className="mb-3 align-items-center">

                                        
                                        <Col md={11}>
                                            <ProfileCard patient={profile} />
                                        </Col>

                                       
                                        <Col md={1} className="text-md-end text-center mt-2 mt-md-0">
                                            <Button variant="outline-info" className="mb-5" onClick={() => handleEdit(profile)}>
                                                <PencilSquare />
                                                
                                            </Button>
                                            <Button variant="outline-danger" className="ms-2 mt-5 mt-md-0" onClick={() => handleDelete(profile)}>
                                                <Trash/>
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

export default PatientProfile;
