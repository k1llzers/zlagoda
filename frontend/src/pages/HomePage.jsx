import home_page_picture from '../images/home_page_picture.png'
import '../App.css';
import { Container, Image } from 'react-bootstrap'
import '../styles/NotFound.css'

const HomePage = () => {
    return (
        <Container style={{height: '93.5vh', display: 'flex', justifyContent: 'center', alignItems: 'center', flexDirection: 'column'}}>
            <Image src={home_page_picture} style={{width: '50%'}}></Image>
        </Container>
    );
};

export default HomePage;