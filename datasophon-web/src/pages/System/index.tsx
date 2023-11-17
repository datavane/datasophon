import { PageContainer } from '@ant-design/pro-components'
import { useSearchParams } from 'react-router-dom'
import Tenant from './Tenant'
import Frame from './Frame'
import Tag from './Tag'
type tabType = {
    [key: string]: any
}

const tabMap: tabType = {
    'tenant': {
        component: <Tenant/>
    },
    'frame': {
        component: <Frame />
    },
    'tag': {
        component: <Tag/>
    }
}
const System = () => {
    const [searchParams, SetSearchParams] = useSearchParams()
    const handleOnTabChange = (activeKey: string) => {
        SetSearchParams({ activeKey })
    }
    const activeKey: string = searchParams.get('activeKey') || 'tenant'
    const activeComponent= tabMap[activeKey].component
    return (<PageContainer
        header={{ title: '系统管理'}}
        tabActiveKey={activeKey}
        tabList={[
            {
              tab: '租户管理',
              key: 'tenant',
              closable: false,
            },
            {
              tab: '机架管理',
              key: 'frame',
              closable: false,
            },
            {
              tab: '标签管理',
              key: 'tag',
              closable: false,
            },
        ]}
        onTabChange={handleOnTabChange}
    >
        <div  style={{
            height: '100vh'
        }}>
            {activeComponent}
        </div>
    </PageContainer>)
}

export default System