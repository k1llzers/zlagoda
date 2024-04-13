import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../provider/authProvider";
import axios from "axios";
import login_picture from "../images/login_picture.png"
import 'bootstrap/dist/css/bootstrap.min.css';
import '../styles/Login.css'

const Login = () => {
    const { setToken, setRole } = useAuth();
    const navigate = useNavigate();
    const [login, setLogin] = useState("");
    const [password, setPassword] = useState("");
    const [errorMessage, setErrorMessage] = useState("");

    const handleLogin = async (e) => {
        e.preventDefault()
        const response = await axios.post("http://localhost:8080/api/auth/login", {
            username: login,
            password: password
        });
        if (response.data.error) {
            setErrorMessage(response.data.error)
        } else {
            setToken(response.data.token);
            setRole(response.data.role);
            navigate("/", { replace: true });
        }
    };

    return (
        <div className="wrapper">
            <div className="container">
                <div className="row">
                    <div className="col-md-6">
                        <img src={login_picture} alt="Image" className="img-fluid"/>
                    </div>
                    <div className="col-md-6 contents form-data">
                        <div className="row justify-content-center">
                            <div className="col-md-8">
                                <div className="mb-4">
                                    <h3 className="login-title">Log In</h3>
                                </div>
                                <form onSubmit={handleLogin}>
                                    <div className="form-group first">
                                        <input type="text" className="form-control" id="username" placeholder="Login"
                                               value={login} onChange={(e) => setLogin(e.target.value)}/>
                                    </div>
                                    <div className="form-group last mb-4">
                                        <input type="password" className="form-control" id="password" placeholder="Password"
                                               value={password} onChange={(e) => setPassword(e.target.value)}/>
                                    </div>
                                    {errorMessage && <span className="error-message">{errorMessage}</span>}
                                    <input type="submit" value="Log In" className="btn login-button" />
                                </form>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
};


export default Login;