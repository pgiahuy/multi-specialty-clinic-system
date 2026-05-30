import { Button, Container, Stack, Row, Col, Modal, Form } from "react-bootstrap";
import { authApis, endpoint, USER_ENDPOINTS } from "../../configs/Apis";
import { useEffect, useState } from "react";
import Header from "../../components/Header";
import ProfileCard from "./components/ProfileCard";
import { PlusLg, PencilSquare, Trash } from 'react-bootstrap-icons';
import { useNavigate } from "react-router-dom";
import Footer from "../../components/Footer";

const PatientProfile = () => {
    const [patientProfiles, setPatientProfiles] = useState([]);
    const navigate = useNavigate();

    const [showEditModal, setShowEditModal] = useState(false);
    const [editData, setEditData] = useState({});

    const handleEdit = (profile) => {
        let dataToEdit = { ...profile };


        if (dataToEdit.dob && dataToEdit.dob.includes("/")) {
            const parts = dataToEdit.dob.split("/");
            if (parts.length === 3) {

                dataToEdit.dob = `${parts[2]}-${parts[1]}-${parts[0]}`;
            }
        }


        setEditData(dataToEdit);

        setShowEditModal(true);
    };

    const handleCloseModal = () => {
        setShowEditModal(false);
        setEditData({});
    };

    const handleInputChange = (e) => {
        setEditData({
            ...editData,
            [e.target.name]: e.target.value
        });
    };


    const handleSaveChanges = async () => {
        try {
            console.log("ID đang sửa là: ", editData.id);

            alert("Cập nhật thành công!");
            setShowEditModal(false);
            loadPatientProfiles();
        } catch (error) {
            console.error(error);
            alert("Có lỗi xảy ra khi cập nhật!");
        }
    };

    const loadPatientProfiles = async () => {
        try {
            const res = await authApis().get(USER_ENDPOINTS.PATIENT_PROFILES);
            setPatientProfiles(res.data);
        } catch (err) {
            console.log(err);
        }
    };



    const handleDelete = (profile) => {
        if (window.confirm(`Bạn có chắc chắn muốn xóa hồ sơ của ${profile.fullName} không?`)) {

            console.log("Delete profile:", profile);
        }
    };

    useEffect(() => {
        loadPatientProfiles();
    }, []);


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
                            className="ms-auto d-flex align-items-center gap-2 shadow-sm py-2 px-3 rounded-4 border-0 "
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
                                            <Button variant="outline-info" className="mb-5 w-100 py-2" onClick={() => handleEdit(profile)}>
                                                <PencilSquare />

                                            </Button>
                                            <Button variant="outline-danger" className="mt-5 mt-md-0 w-100 py-2" onClick={() => handleDelete(profile)}>
                                                <Trash />
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
                <Modal
                    show={showEditModal}
                    onHide={handleCloseModal}
                    backdrop="static"
                    centered
                    size="lg"
                    className="rounded-4"
                    contentClassName="rounded-4 border-0 shadow-lg"
                >
                    <Modal.Header closeButton>
                        <Modal.Title className="fw-bold">Chỉnh sửa hồ sơ</Modal.Title>
                    </Modal.Header>
                    <Modal.Body>
                        <Form>
                            <Form.Group className="mb-3">
                                <Form.Label>Họ và tên</Form.Label>
                                <Form.Control
                                    type="text"
                                    name="fullName"
                                    value={editData.fullName || ""}
                                    onChange={handleInputChange}
                                    className="rounded-4"
                                />
                            </Form.Group>

                            <Form.Group className="mb-3">
                                <Form.Label>CCCD/CMND</Form.Label>
                                <Form.Control
                                    type="text"
                                    name="cccd"
                                    value={editData.cccd || ""}
                                    onChange={handleInputChange}
                                    className="rounded-4"
                                />
                            </Form.Group>

                            <Row>
                                <Col md={6}>
                                    <Form.Group className="mb-3">
                                        <Form.Label>Số điện thoại</Form.Label>
                                        <Form.Control
                                            type="tel"
                                            name="phone"
                                            value={editData.phone || ""}
                                            onChange={handleInputChange}
                                            className="rounded-4"
                                        />
                                    </Form.Group>
                                </Col>
                                <Col md={6}>
                                    <Form.Group className="mb-3">
                                        <Form.Label>Ngày sinh</Form.Label>
                                        <Form.Control
                                            type="date"
                                            name="dob"
                                            value={editData.dob || ""}
                                            onChange={handleInputChange}
                                            className="rounded-4"
                                        />
                                    </Form.Group>
                                </Col>
                            </Row>

                            <Form.Group className="mb-3">
                                <Form.Label>Giới tính</Form.Label>
                                <Form.Select
                                    name="gender"
                                    value={editData.gender || ""}
                                    onChange={handleInputChange}
                                    className="rounded-4"
                                >
                                    <option value="Nam">Nam</option>
                                    <option value="Nữ">Nữ</option>
                                    <option value="Khác">Khác</option>
                                </Form.Select>
                            </Form.Group>

                            <Form.Group className="mb-3">
                                <Form.Label>Địa chỉ</Form.Label>
                                <Form.Control

                                    type="text"
                                    name="address"
                                    value={editData.address || ""}
                                    onChange={handleInputChange}
                                    className="rounded-4"
                                />
                            </Form.Group>
                        </Form>
                    </Modal.Body>
                    <Modal.Footer >
                        <Button variant="outline-danger" className="rounded-4 px-4" onClick={handleCloseModal}>
                            Hủy bỏ
                        </Button>
                        <Button variant="primary" className="rounded-4 px-4 border-0" onClick={handleSaveChanges}>
                            Lưu thay đổi
                        </Button>
                    </Modal.Footer>
                </Modal>

            </div>

        </>
    );
};

export default PatientProfile;
