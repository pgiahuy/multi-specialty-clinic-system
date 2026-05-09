import { Alert, Button, Container, Nav, Stack, Row,Col } from "react-bootstrap";
import API, { authApis, endpoint } from "../../configs/Apis";
import { useEffect, useState } from "react";
import Header from "../../components/Header";
import ControlCard from "./components/ControlCard";
import ProfileCard from "./components/ProfileCard";
import { PlusLg } from 'react-bootstrap-icons';
const PatientProfile = () => {


    const [patientProfiles, setPatientProfiles] = useState([]);

    const loadPatientProfiles = async () => {
        try {
            const res = await authApis().get(endpoint['patientProfile']);
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
                    onClick={() => {/* Logic thêm mới */}}
                >
                    <PlusLg /> <span>Thêm hồ sơ mới</span>
                </Button>
            </Stack>


            <Row>
                <Col>
                    {patientProfiles.length > 0 ? (
                        patientProfiles.map((profile) => (
                            <ProfileCard key={profile.id} patient={profile} />
                        ))
                    ) : (
                        <div className="text-center py-5 bg-light rounded-3 border-dashed">
                            <p className="text-muted mb-0">Hiện chưa có dữ liệu hồ sơ nào được tạo.</p>
                        </div>
                    )}
                </Col>
            </Row>
        </Container>
    </>
);
};

export default PatientProfile;
