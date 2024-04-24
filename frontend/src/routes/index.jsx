import {RouterProvider, createBrowserRouter} from "react-router-dom";
import { useAuth } from "../provider/authProvider";
import { ProtectedRoute } from "./ProtectedRoute";
import Login from "../pages/Login";
import Logout from "../pages/Logout";
import NotFound from "../pages/NotFound";
import Products from "../pages/Products"
import Categories from "../pages/Categories"
import CustomerCards from "../pages/CustomerCards"
import HomePage from "../pages/HomePage";
import StoreProducts from "../pages/StoreProducts";
import Employees from "../pages/Employees";
import Checks from "../pages/Checks"
import Profile from "../pages/Profile"

const Routes = () => {
    const {token, role} = useAuth()

    const routesForAuthenticatedOnly = [
        {
            path: "/",
            element: <ProtectedRoute/>,
            children : [
                {
                    path: "/",
                    element: <HomePage/>,
                },
                {
                    path: "/products",
                    element: <Products/>,
                },
                {
                    path: "/customer-cards",
                    element: <CustomerCards/>,
                },
                {
                    path: "/store-products",
                    element: <StoreProducts/>,
                },
                {
                    path: "/checks",
                    element: <Checks/>,
                },
                {
                    path: "/logout",
                    element: <Logout/>,
                },
                {
                    path: "/profile",
                    element: <Profile/>,
                }
            ]
        }
    ]

    const routesForManagerOnly = [
        {
            path: "/",
            element: <ProtectedRoute/>,
            children : [
                {
                    path: "/categories",
                    element: <Categories/>,
                },
                {
                    path: "/employees",
                    element: <Employees/>,
                }
            ]
        }
    ]

    const routesForNotAuthenticatedOnly = [
        {
            path: "/login",
            element: <Login/>
        }
    ]

    const routes404 = [
        {
            path: "*",
            element: <NotFound/>
        }
    ]

    const router = createBrowserRouter([
        ...(!token ? routesForNotAuthenticatedOnly : []),
        ...routesForAuthenticatedOnly,
        ...(role === "MANAGER" ? routesForManagerOnly : []),
        ...routes404
    ])

    return <RouterProvider router={router} />
}

export default Routes;