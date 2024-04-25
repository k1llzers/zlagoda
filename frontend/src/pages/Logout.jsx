import {Navigate, useNavigate} from "react-router-dom";
import { useAuth } from "../provider/authProvider";
import {useEffect} from "react";

const Logout = () => {
    const { setToken } = useAuth();
    const { setRole } = useAuth();
    const navigate = useNavigate();

    const handleLogout = () => {
        setToken();
        setRole();
    };

    useEffect(() => {
        handleLogout();
    }, []);

    return <Navigate to="/login"/>;
};

export default Logout;