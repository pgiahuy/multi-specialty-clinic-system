import { Spinner } from 'react-bootstrap';

const MySpinner = () => {
    return (
        <Spinner 
            as="span" 
            animation="border" 
            size="lg" 
            role="status" 
            aria-hidden="true"
            className="me-2"
            variant="info"
            
        />
    );
};

export default MySpinner;
