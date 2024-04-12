import { RouterProvider, createBrowserRouter } from "react-router-dom";
import { useAuth } from "../provider/authProvider";
import { ProtectedRoute } from "./ProtectedRoute";
import Login from "../pages/Login";

const Routes = () => {
    const {token} = useAuth()

    const routesForAuthenticatedOnly = [
        {
            path: "/",
            element: <ProtectedRoute/>,
            children : [

            ]
        }

    ]

    const routesForNotAuthenticatedOnly = [
        {
            path: "/login",
            element: <Login/>
        }
    ]

    const router = createBrowserRouter([
        ...(!token ? routesForNotAuthenticatedOnly : []),
        ...routesForAuthenticatedOnly
    ])

    return <RouterProvider router={router} />
}

export default Routes;