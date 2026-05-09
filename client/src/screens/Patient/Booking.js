import { Container, Stack } from "react-bootstrap";
import Header from "../../components/Header";


const BookingPage = () => {






    return (
        <>
            <Header />

            <Container style={{ width: '80%' }} className="mt-3">

                <Stack direction="horizontal" gap={3} className="mb-4 align-items-end border-bottom pb-3">
                    <div>
                        <h4 className="fw-bold mb-0 text-dark text-center">
                            Đặt lịch khám bệnh
                        </h4>

                    </div>


                </Stack>



            </Container>
        </>
    );
};

export default BookingPage;
