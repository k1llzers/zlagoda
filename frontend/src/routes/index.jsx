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
                {
                    path: "/",
                    element: <div>Home Page</div>,
                },
                {
                    path: "/profile",
                    element: <div>User Profile</div>,
                },
            ]
        }

    ]

    const routesForNotAuthenticatedOnly = [
        {
            path: "/login",
            element: <Login/>
        }
    ]

    // нада потім врахувати що через пошукову стрічку касир може зайти на сторінку тільки для менеджера

    const router = createBrowserRouter([
        ...(!token ? routesForNotAuthenticatedOnly : []),
        ...routesForAuthenticatedOnly
    ])

    return <RouterProvider router={router} />
}

export default Routes;