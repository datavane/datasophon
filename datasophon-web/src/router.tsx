import { createBrowserRouter } from 'react-router-dom'
import ClusterList from './pages/ClusterList'
import HostList from './pages/HostList'
import Service from './pages/Service'
import Login from './pages/Login'
import UserList from './pages/User'
import ClusterStorage from './pages/ClusterStorage'
import ClusterFramework from './pages/ClusterFramework'
import BaseLayout from './components/BaseLayout'
import Cluster from './pages/Cluster'

const router = createBrowserRouter([
    {
        path: '/',
        element: <BaseLayout/>,
        children: [
            {
                path: '/cluster',
                element: <ClusterList />,
            },
            {
                path: '/cluster/:clusterId',
                element: <Cluster/>
            },
            {
                path: '/cluster/:clusterId/host',
                element: <HostList/>
            },
            {
                path: '/cluster/:clusterId/service/:serviceId',
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