import { App, Layout, Menu } from 'antd'
import type { MenuProps } from 'antd';
import { useTranslation } from 'react-i18next';
import { useNavigate, Outlet } from 'react-router-dom'
const { Header, Content } = Layout

const BaseLayout = () => {
    const navigate = useNavigate()
    const { t } = useTranslation()
 

    const items: MenuProps['items'] = [{
        label: t('cluster.title'),
        key: 'cluster'
    }, {
        label: t('user.title'),
        key: 'user'
    }]
    const handleOnClick: MenuProps['onClick'] = ({key}) => {
        navigate(`/${key}`)
    };
    return (<App>
        {/* header */}
        <Header 
            style={{
                position: 'sticky',
                top: 0,
                zIndex: 1,
                width: '100%',
            }}
        >
            <Menu mode="horizontal" theme="dark" onClick={handleOnClick} items={items} defaultSelectedKeys={['cluster']}/>
        </Header>
        {/* content */}
        <Content>
            <Outlet />
        </Content>
    </App>)
}

export default BaseLayout