import { PageContainer } from '@ant-design/pro-components'
import { useSearchParams } from 'react-router-dom'
import Tenant from './Tenant'
import Frame from './Frame'
import Tag from './Tag'

const System = () => {
    const [searchParams, SetSearchParams] = useSearchParams()
    const handleOnTabChange = (activeKey: string) => {
        SetSearchParams({ activeKey })
    }
    const activeKey: string = searchParams.get('activeKey') || 'tenant'
    return (<PageContainer
        header={{ title: '系统管理'}}
        tabActiveKey={activeKey}
        tabProps={{
            tabPosition: 'left',
        }}
        tabList={[
            {
              tab: '租户管理',
              key: 'tenant',
              closable: false,
              children: <Tenant />
            },
            {
              tab: '机架管理',
              key: 'frame',
              closable: false,
              children: <Frame />
            },
            {
              tab: '标签管理',
              key: 'tag',
              closable: false,
              children: <Tag />
            },
        ]}
        onTabChange={handleOnTabChange}
    >
    </PageContainer>)
}

export default System