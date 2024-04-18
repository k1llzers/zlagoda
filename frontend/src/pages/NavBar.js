import { useAuth } from "../provider/authProvider";
import 'bootstrap/dist/css/bootstrap.min.css';
import { Navbar, Nav, Container } from 'react-bootstrap';
const NavBar = () => {
    const {role} = useAuth()
    const {token} = useAuth();

    if (!token)
        return null

    return (
        <Navbar collapseOnSelect expand="lg" className="bg-body-tertiary navbar-dark navbar" style={{ backgroundColor: '#2d434b' }}>
            <Container style={{maxWidth: "100%"}}>
                <Navbar.Brand href="/">Zlagoda</Navbar.Brand>
                <Navbar.Toggle aria-controls="responsive-navbar-nav" />
                <Navbar.Collapse id="responsive-navbar-nav">
                    <Nav className="me-auto">
                        <Nav.Link href="/products">Products</Nav.Link>
                        {role === "MANAGER" && <Nav.Link href="/categories">Categories</Nav.Link>}
                        <Nav.Link href="#storeproducts">Store Products</Nav.Link>
                        <Nav.Link href="/customer-cards">Customer Cards</Nav.Link>
                        {role === "MANAGER" && <Nav.Link href="/employees">Employees</Nav.Link>}
                        {role === "MANAGER" && <Nav.Link href="#checks">Checks</Nav.Link>}
                    </Nav>
                    <Nav>
                        <Nav.Link href="/logout">Log out</Nav.Link>
                    </Nav>
                </Navbar.Collapse>
            </Container>
        </Navbar>
    );
}

export default NavBar