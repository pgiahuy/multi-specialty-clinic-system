import { Alert } from "react-bootstrap";
import API, { authApis, endpoint } from "../../configs/Apis";
import { useEffect, useState } from "react";


const PatientDashboard = () => {


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
        {patientProfiles.map((profile) => (
            <Alert variant="info" key={profile.id}>
                {profile.cccd} - {profile.fullName} - 
                {profile.email} - {profile.phone} - {profile.dob} - {profile.address} 
            </Alert>
        ))}
    </>
);
};

export default PatientDashboard;
