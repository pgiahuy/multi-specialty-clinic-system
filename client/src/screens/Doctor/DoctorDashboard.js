import { Alert, Container } from "react-bootstrap";
import API, { authApis, endpoint } from "../../configs/Apis";
import { useEffect, useState } from "react";
import Header from "../../components/Header";
import ControlCard from "./component/ControlCard";

const DoctorDashboard = () => {


	return (
		<>
			<Header />
			<Container>
				<ControlCard />
			</Container>

		</>
	);
};

export default DoctorDashboard;
