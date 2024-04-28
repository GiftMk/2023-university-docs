import { BrowserRouter as Router, Routes, Route } from "react-router-dom"
import { RouteProps } from "./routesData"

interface Props {
    routes: RouteProps[]
}

const PublicRoutes = ({ routes }: Props) => {
    return (
        <Router>
            <Routes>
                {routes.map((route: RouteProps, i: number) => (
                    <Route 
                        path={route.path} 
                        element={<route.component/>} 
                        key={i}
                    />
                ))}
            </Routes>
        </Router>
    )
}

export default PublicRoutes