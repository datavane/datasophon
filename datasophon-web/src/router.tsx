import { createBrowserRouter } from 'react-router-dom'
import App from './App'
import ClusterList from './pages/ClusterList'
import ClusterManage from './pages/ClusterManage'
import HostList from './pages/HostList'
import Service from './pages/Service'
import Login from './pages/Login'
import UserList from './pages/User'
import ClusterStorage from './pages/ClusterStorage'
import ClusterFramework from './pages/ClusterFramework'

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
            {
                path: '/cluster-storage',
                element: <ClusterStorage />,
            },
            {
                path: '/cluster-framework',
                element: <ClusterFramework />,
            },
            {
                path: '/user',
                element: <UserList />,
            },
        ]
    },
    {
        path: '/login',
        element: <Login />
    }
])

export default router