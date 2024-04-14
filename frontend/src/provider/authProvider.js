import axios from "axios";
import {
    createContext,
    useContext,
    useEffect,
    useMemo,
    useState,
} from "react";
import NavBar from "../pages/NavBar";

const AuthContext = createContext();

const AuthProvider =  ({children}) => {
    const [token, setToken_] = useState(localStorage.getItem("token"))
    const [role, setRole_] = useState(localStorage.getItem("role"))

    const setToken = (newToken) => {
        setToken_(newToken)
    }

    const setRole = (role) => {
        setRole_(role)
    }

    useEffect(() => {
        if(token) {
            axios.defaults.headers.common["Authorization"] = "Bearer " + token
            localStorage.setItem("token", token)
            localStorage.setItem("role", role)
        } else {
            delete axios.defaults.headers.common["Authorization"];
            localStorage.removeItem("token")
            localStorage.removeItem("role")
        }
    }, [token])

    const contextValue = useMemo(
        () => ({
            token,
            role,
            setToken,
            setRole,
        }),
        [token, role]
    )

    return (
        <AuthContext.Provider value={contextValue}>
            {children}
        </AuthContext.Provider>
    )
}

export const useAuth = () => {
    return useContext(AuthContext);
}

export default AuthProvider;
