import { useCookieState, useLocalStorageState } from 'ahooks';
import { App, Avatar, Dropdown, Flex, Layout, Menu } from 'antd'
import type { MenuProps } from 'antd';
import { useTranslation } from 'react-i18next';
import { useNavigate, Outlet } from 'react-router-dom'
const { Header, Content } = Layout

const BaseLayout = () => {
    const navigate = useNavigate()
    const { t } = useTranslation()
    const [user,] = useLocalStorageState<any>('user')
    const [, setSessionId] = useCookieState('sessionId')
    const items: MenuProps['items'] = [{
        label: t('cluster.title'),
        key: 'cluster'
    }, {
        label: t('user.title'),
        key: 'user'
    }]

    const dropMenuItems: MenuProps['items'] = [
        {
          key: 'loginOut',
          label: '退出登录'
        },
      ];
    const handleOnClick: MenuProps['onClick'] = ({key}) => {
        navigate(`/${key}`)
    };

    const handleOnDropMenuClick: MenuProps['onClick'] = () => {
        setSessionId('')
        navigate(`/login`)
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
            <Flex justify='space-between'>
                <div>
                    <Menu mode="horizontal" theme="dark" onClick={handleOnClick} items={items} defaultSelectedKeys={['cluster']}/>
                </div>
                <div>
                    <Dropdown menu={{ items: dropMenuItems, onClick: handleOnDropMenuClick }} placement="bottomRight">
                        <Avatar
                            style={{ backgroundColor: '#1677ff'}}
                            size="large"
                        >{user.username}</Avatar>
                    </Dropdown>
                </div>
            </Flex>
        </Header>
        {/* content */}
        <Content>
            <Outlet />
        </Content>
    </App>)
}

export default BaseLayout