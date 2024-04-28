import Home from '../pages/Home'
import Films from '../pages/Films'

export interface RouteProps {
    path: string
    component: React.ComponentType
}

const publicRoutes: Array<RouteProps> = [
    { path: "/", component: Home },
    { path: "/films", component: Films },
]

export { publicRoutes }