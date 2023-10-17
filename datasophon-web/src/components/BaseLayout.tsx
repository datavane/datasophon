import { Layout, Menu } from 'antd'
import type { MenuProps } from 'antd';
import { useNavigate, Outlet } from 'react-router-dom'
const { Header, Content } = Layout
const items: MenuProps['items'] = [{
    label: '集群',
    key: 'cluster'
}]
const BaseLayout = () => {
    const navigate = useNavigate()
    const handleOnClick: MenuProps['onClick'] = ({key}) => {
        navigate(`/${key}`)
    };

    return (<div>
        {/* header */}
        <Header>
            <Menu mode="horizontal" theme="dark" onClick={handleOnClick} items={items} defaultSelectedKeys={['cluster-manage']}/>
        </Header>
        {/* content */}
        <Content style={{ height: '100vh'}}>
            <Outlet />
        </Content>
    </div>)
}

export default BaseLayout