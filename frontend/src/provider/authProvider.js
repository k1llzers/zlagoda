import axios from "axios";
import {
    createContext,
    useContext,
    useEffect,
    useMemo,
    useState,
} from "react";

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
