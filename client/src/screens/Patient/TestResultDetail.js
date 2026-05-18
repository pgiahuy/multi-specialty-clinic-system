import { useContext, useEffect, useState } from "react";
import { authApis, endpoint } from "../../configs/Apis";
import { exp } from "firebase/firestore/pipelines";
import { MyUserContext } from "../../configs/Contexts";
import { Col, Container, Row, Table } from "react-bootstrap";
import Header from "../../components/Header";
import Footer from "../../components/Footer";
import { useParams } from "react-router-dom";

const TestResultDetail = () => {

    const {patientId} = useParams();
    const [user] = useContext(MyUserContext);
    const [testResults, setTestResults] = useState([]);
    
   

    const loadTestResults = async () => {
        try {
            const res = await authApis().get(endpoint['test-results'](patientId));
            setTestResults(res.data);
        } catch (err) {
            console.log(err);
        }
    };

    useEffect(() => {
        loadTestResults();
    }, []);

    return (
        <>
            <Header />
            <Container>
                <Row className="gap-3">

                    <Col md={8}>
                        <Table striped="columns">
                            <thead>
                                <tr>
                                    <th></th>
                                    <th>Xét nghiệm</th>
                                    <th>Kết quả</th>
                                    <th>Đơn vị</th>
                                    <th>Khoảng tham chiếu</th>
                                </tr>
                            </thead>
                            <tbody>
                                {testResults.map((result) => (
                                    <tr key={result.id}>
                                        <td>{result.id}</td>
                                        <td>{result.testName}</td>
                                        <td>{result.result}</td>
                                        <td>{result.unit}</td>
                                        <td>{result.normalRange}</td>
                                    </tr>
                                ))}
                            </tbody>
                        </Table>
                    </Col>





                    <Col md={3} className="ps-0">
                        <div className="p-3 border rounded bg-light" style={{ position: 'sticky', top: '20px' }}>
                            <div className="mb-3">
                                <label className="form-label fw-bold text-secondary">Từ ngày:</label>
                                <input type="date" className="form-control" />
                            </div>

                            <div className="mb-3">
                                <label className="form-label fw-bold text-secondary">Đến ngày:</label>
                                <input type="date" className="form-control" />
                            </div>

                            <button className="btn btn-primary w-100 d-flex align-items-center justify-content-center gap-2 mb-2">
                                Xem kết quả
                            </button>


                        </div>
                    </Col>
                </Row>
            </Container>
            <Footer />
        </>
    );
}
export default TestResultDetail;