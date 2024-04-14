import { Navigate, Outlet } from "react-router-dom";
import { useAuth } from "../provider/authProvider";
import NavBar from "../pages/NavBar";

export const ProtectedRoute = () => {
    const {token} = useAuth();
    if(!token) {
        return <Navigate to = "/login" />
    }

    return (
        <>
            <NavBar/>
            <Outlet/>
        </>
    )
}