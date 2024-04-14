import { useNavigate } from "react-router-dom";
import { useAuth } from "../provider/authProvider";
import {useEffect} from "react";

const Logout = () => {
    const { setToken } = useAuth();
    const { setRole } = useAuth();
    const navigate = useNavigate();

    const handleLogout = () => {
        setToken();
        setRole()
        navigate("/", { replace: true });
    };

    useEffect(() => {
        handleLogout();
    }, []);

    return <>Logout</>;
};

export default Logout;