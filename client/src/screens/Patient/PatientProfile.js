import { Button, Container, Stack, Row, Col, Modal, Form } from "react-bootstrap";
import { authApis, endpoint, USER_ENDPOINTS } from "../../configs/Apis";
import { useEffect, useState } from "react";
import Header from "../../components/Header";
import ProfileCard from "./components/ProfileCard";
import { PlusLg, PencilSquare, Trash } from 'react-bootstrap-icons';
import { useNavigate } from "react-router-dom";
import Footer from "../../components/Footer";
import { setLogLevel } from "firebase/app";
import MySpinner from "../../components/MySpinner";
import MyModal from "../../components/MyModal";

const PatientProfile = () => {
    const [patientProfiles, setPatientProfiles] = useState([]);
    const navigate = useNavigate();

    const [showEditModal, setShowEditModal] = useState(false);
    const [editData, setEditData] = useState({});
    const [loading, setLoading] = useState(false);
    const [showDeleteModal, setShowDeleteModal] = useState(false);
    const [profileToDelete, setProfileToDelete] = useState(null);
    const [showSuccessModal, setShowSuccessModal] = useState(false);
    const [showConfirmEditModal, setShowConfirmEditModal] = useState(false);
    const [showEditSuccessModal, setShowEditSuccessModal] = useState(false);
    const [showAddSuccessModal, setShowAddSuccessModal] = useState(false);

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

            const res = await authApis().put(USER_ENDPOINTS.PATIENT_PROFILE_DETAIL(editData.id), editData);

            setShowEditModal(false);
            loadPatientProfiles();
        } catch (error) {
            console.error(error);

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
        setProfileToDelete(profile);
        setShowDeleteModal(true);
    };



    const confirmDelete = async () => {
        if (!profileToDelete) return;
        try {
            setShowDeleteModal(false);
            setLoading(true);
            const res = await authApis().delete(USER_ENDPOINTS.PATIENT_PROFILE_DETAIL(profileToDelete.id));
            loadPatientProfiles();
        } catch (err) {
            console.error(err);

        } finally {
            setLoading(false);
            setProfileToDelete(null);
            setShowSuccessModal(true);
        }
    };

    useEffect(() => {
        loadPatientProfiles();
    }, []);


    return (
        <>
            <div className="d-flex flex-column min-vh-100">
                <Header />

                <Container style={{ width: '80%' }} className="py-4">
                    <div className="mb-4 pb-3 border-bottom">
                        <div className="d-flex flex-column flex-lg-row justify-content-between align-items-lg-end gap-3">
                            <div>
                                <h2 className="fw-bold mb-0 text-primary">Danh sách hồ sơ</h2>
                            </div>
                            <Button
                                variant="primary"
                                className="ms-auto d-flex align-items-center gap-2 shadow-sm py-2 px-3 rounded-4 border-0 "
                                style={{ borderRadius: '10px' }}
                                onClick={() => navigate('/patient/register-record')}
                            >
                                <PlusLg /> <span>Thêm hồ sơ mới</span>
                            </Button>
                        </div>
                    </div>


                    <Row className="gy-4">
                        {loading ? (
                            <Col xs={12}>
                                <div className="text-center py-5">
                                    <MySpinner />
                                    <div className="text-muted mt-3 small">Đang tải danh sách hồ sơ...</div>
                                </div>
                            </Col>
                        ) : patientProfiles.length > 0 ? (
                            patientProfiles.map((profile) => (
                                <Col key={profile.id} xs={12} md={6}>
                                    <ProfileCard
                                        patient={profile}
                                        onEdit={() => handleEdit(profile)}
                                        onDelete={() => handleDelete(profile)}
                                    />
                                </Col>
                            ))
                        ) : (
                            <Col xs={12}>
                                <div className="text-center py-5 bg-light rounded-3 border-dashed">
                                    <p className="text-muted mb-0">Hiện chưa có hồ sơ nào.</p>
                                </div>
                            </Col>
                        )}
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
                    <div className="p-4 rounded-4">
                        <Modal.Header closeButton>
                            <Modal.Title className="fw-bold">Chỉnh sửa hồ sơ</Modal.Title>
                        </Modal.Header>
                        <Modal.Body >
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
                    </div >
                </Modal >
                <MyModal
                    show={showDeleteModal}
                    onHide={() => {
                        setShowDeleteModal(false);
                        setProfileToDelete(null);
                    }}
                    title="Xác nhận xóa hồ sơ"
                    onConfirm={confirmDelete}
                    confirmText="Xóa hồ sơ"
                    cancelText="Hủy bỏ"
                >
                    {profileToDelete && (
                        <div className="text-center py-3">

                            <h5 className="mt-3 text-dark">Bạn có chắc chắn muốn xóa hồ sơ của {profileToDelete.fullName}?</h5>
                        </div>
                    )}
                </MyModal>

                <MyModal
                    show={showSuccessModal}
                    onHide={() => setShowSuccessModal(false)}
                    title="Thông báo"
                    cancelText="Đóng"

                >
                    <div className="text-center py-4">
                        <h4 className="mt-3 text-success">Hồ sơ đã được xóa thành công!</h4>
                    </div>
                </MyModal>

            </div >

        </>
    );
};

export default PatientProfile;
