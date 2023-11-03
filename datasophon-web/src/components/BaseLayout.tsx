import { App, Layout, Menu } from 'antd'
import type { MenuProps } from 'antd';
import { useNavigate, Outlet } from 'react-router-dom'
const { Header, Content } = Layout
const items: MenuProps['items'] = [{
    label: '集群管理',
    key: 'cluster'
}, {
    label: '用户管理',
    key: 'user'
}]
const BaseLayout = () => {
    const navigate = useNavigate()
    const handleOnClick: MenuProps['onClick'] = ({key}) => {
        navigate(`/${key}`)
    };

    return (<App>
        {/* header */}
        <Header>
            <Menu mode="horizontal" theme="dark" onClick={handleOnClick} items={items} defaultSelectedKeys={['cluster-manage']}/>
        </Header>
        {/* content */}
        <Content style={{ height: '100vh'}}>
            <Outlet />
        </Content>
    </App>)
}

export default BaseLayout