import { PageContainer } from '@ant-design/pro-components'
import { useSearchParams } from 'react-router-dom'
import AlarmGroup from './AlarmGroup'
import AlarmMetrics from './AlarmMetrics'
type tabType = {
    [key: string]: any
}

const tabMap: tabType = {
    'alarm-group': {
        component: <AlarmGroup/>
    },
    'alarm-metrics': {
        component: <AlarmMetrics/>
    }
}
const Alarm = () => {
    const [searchParams, SetSearchParams] = useSearchParams()
    const handleOnTabChange = (activeKey: string) => {
        SetSearchParams({ activeKey })
    }
    const activeKey: string = searchParams.get('activeKey') || 'alarm-group'
    const activeComponent= tabMap[activeKey].component
    return (<PageContainer
        header={{ title: '告警管理'}}
        tabActiveKey={activeKey}
        tabList={[
            {
              tab: '告警组管理',
              key: 'alarm-group',
              closable: false,
            },
            {
              tab: '告警指标管理',
              key: 'alarm-metrics',
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

export default Alarm