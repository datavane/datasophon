import { useSearchParams } from 'react-router-dom'
import User from './User'
import UserGroup from './UserGroup'
import { Tabs } from 'antd'

const Tenant = () => {
    const [searchParams, SetSearchParams] = useSearchParams()
    const handleOnTabChange = (activeKey: string) => {
        SetSearchParams({  tenantActiveKey: activeKey })
    }
    const activeKey: string = searchParams.get('tenantActiveKey') || 'user'
    return (<Tabs
        activeKey={activeKey}
        items={[
            {
              label: '用户',
              key: 'user',
              closable: false,
              children: <User />
            },
            {
              label: '用户组',
              key: 'user-group',
              closable: false,
              children: <UserGroup />
            },
        ]}
        onChange={handleOnTabChange}
    >
    </Tabs>)
}

export default Tenant