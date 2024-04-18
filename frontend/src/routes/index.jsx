import {RouterProvider, createBrowserRouter} from "react-router-dom";
import { useAuth } from "../provider/authProvider";
import { ProtectedRoute } from "./ProtectedRoute";
import Login from "../pages/Login";
import Logout from "../pages/Logout";
import NotFound from "../pages/NotFound";
import Products from "../pages/Products"
import Categories from "../pages/Categories"
import CustomerCards from "../pages/CustomerCards"
import Employees from "../pages/Employees";

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
                    path: "/products",
                    element: <Products/>,
                },
                {
                    path: "/categories",
                    element: <Categories/>,
                },
                {
                    path: "/employees",
                    element: <Employees/>,
                },
                {
                    path: "/customer-cards",
                    element: <CustomerCards/>,
                },
                {
                    path: "/logout",
                    element: <Logout/>,
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

    // нада потім врахувати що через пошукову стрічку касир може зайти на сторінку тільки для менеджера

    const router = createBrowserRouter([
        ...(!token ? routesForNotAuthenticatedOnly : []),
        ...routesForAuthenticatedOnly,
        ...routes404
    ])

    return <RouterProvider router={router} />
}

export default Routes;