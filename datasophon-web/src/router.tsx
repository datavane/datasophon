import { createBrowserRouter } from 'react-router-dom'
import App from './App'
import ClusterList from './pages/ClusterList'
import ClusterManage from './pages/ClusterManage'
import HostList from './pages/HostList'
import Service from './pages/Service'
import Login from './pages/Login'

const router = createBrowserRouter([
    {
        path: '/',
        element: <App/>,
        children: [
            {
                path: '/cluster',
                element: <ClusterList />,
            },
            {
                path: '/cluster/:clusterId',
                element: <ClusterManage/>
            },
            {
                path: '/cluster/:clusterId/host',
                element: <HostList/>
            },
            {
                path: '/cluster/:clusterId/service',
                element: <Service/>
            },
        ]
    },
    {
        path: '/login',
        element: <Login />
    }
])

export default router