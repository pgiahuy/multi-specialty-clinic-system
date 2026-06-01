import { Button, Modal } from "react-bootstrap";

const MyModal = ({
    show,
    onHide,
    title,
    children,
    onConfirm,
    confirmText = "Lưu",
    cancelText = "Hủy",
    size = "md",
    hideFooter = false
}) => {
    return (
        <>
            <Modal
                show={show}
                onHide={onHide}
                size={size}
                centered
                className="rounded-4"
            >
                <Modal.Header closeButton className="border-bottom-0 pb-3">
                    <Modal.Title className="fw-bold">{title}</Modal.Title>
                </Modal.Header>

                <Modal.Body>
                    {children}
                </Modal.Body>

                {!hideFooter && (
                    <Modal.Footer className="border-top-0 pt-0">
                        <Button variant="outline-secondary" className="rounded-pill px-4" onClick={onHide}>
                            {cancelText}
                        </Button>
                        {onConfirm && (
                            <Button variant="primary" className="rounded-pill px-4" onClick={onConfirm}>
                                {confirmText}
                            </Button>
                        )}
                    </Modal.Footer>
                )}
            </Modal>
        </>
    );
};

export default MyModal;