import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../provider/authProvider";
import axios from "axios";
import 'bootstrap/dist/css/bootstrap.min.css';

const Login = () => {
    const { setToken, setRole } = useAuth();
    const navigate = useNavigate();
    const [login, setLogin] = useState("");
    const [password, setPassword] = useState("");

    const handleLogin = async (e) => {
        e.preventDefault();
        try {
            const response = await axios.post("http://localhost:8080/api/auth/login", {
                username: login,
                password: password
            });
            setToken(response.data.token);
            setRole(response.data.role);
            navigate("/", { replace: true });
        } catch (error) {
            console.error("Login failed:", error);
        }
    };

    return (
        <div className="container">
            <div className="row">
                <div className="col-md-6">
                    <img src="../login_picture.png" alt="Image" className="img-fluid"/>
                </div>
                <div className="col-md-6 contents">
                    <div className="row justify-content-center">
                        <div className="col-md-8">
                            <div className="mb-4">
                                <h3>Log In</h3>
                            </div>
                            <form action="#" method="post" onSubmit={handleLogin}>
                                <div className="form-group first">
                                    <label htmlFor="username">Login</label>
                                    <input type="text" className="form-control" id="username"
                                           value={login} onChange={(e) => setLogin(e.target.value)}/>
                                </div>
                                <div className="form-group last mb-4">
                                    <label htmlFor="password">Password</label>
                                    <input type="password" className="form-control" id="password"
                                           value={password} onChange={(e) => setPassword(e.target.value)}/>
                                </div>
                                <input type="submit" value="Log In" className="btn btn-block btn-primary" />
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
};


export default Login;