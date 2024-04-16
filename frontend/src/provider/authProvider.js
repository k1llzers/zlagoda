import axios from "axios";
import {
    createContext,
    useContext,
    useEffect,
    useMemo,
    useState,
} from "react";
import {redirect} from "react-router-dom";

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
            localStorage.setItem("token", token)
            localStorage.setItem("role", role)
        } else {
            localStorage.removeItem("token")
            localStorage.removeItem("role")
        }
    }, [token])

    axios.interceptors.request.use(function (config) {
        const token = localStorage.getItem("token")
        if (token && !config.skipAuth)
            config.headers.Authorization = 'Bearer ' + token;
        return config;
    });

    axios.interceptors.response.use(function (response) {
        if (response.data.error) {
            if (response.data.code == 401)
                redirect("/logout")
        }
        return response;
    });

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
