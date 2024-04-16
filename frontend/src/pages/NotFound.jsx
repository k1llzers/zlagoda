import not_found_picture from '../images/404_picture.jpg';
import '../App.css';
import { Container, Image } from 'react-bootstrap';
import '../styles/NotFound.css'

const NotFound = () => {
    return (
        <Container style={{height: '100vh', display: 'flex', justifyContent: 'center', alignItems: 'center', flexDirection: 'column'}}>
            <h4>Oops... Something went wrong</h4>
            <Image src={not_found_picture} style={{width: '40%', marginTop: '20px'}}></Image>
            <h3 className="link-wrapper"><a className="go-home-link" href="/">Go Home</a></h3>
        </Container>
    );
};

export default NotFound;