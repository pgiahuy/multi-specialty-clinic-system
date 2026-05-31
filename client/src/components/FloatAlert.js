import './FloatAlert.css';

const FloatAlert = ({ show, heading, message, variant }) => {
    if (!show) return null;

    return (
        <div className="alert-container">
            <div className={`custom-alert ${variant || 'warning'}`}>
                <div style={{ fontWeight: 'bold', marginBottom: '5px' }}>
                    {heading}
                </div>
                <small>{message}</small>
            </div>
        </div>
    );
};

export default FloatAlert;