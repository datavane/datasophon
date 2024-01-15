import { PageContainer } from '@ant-design/pro-components'
import { useSearchParams } from 'react-router-dom'
import { Button } from 'antd'
import ServiceOverview from './ServiceOverview'
import ServiceInstance from './ServiceInstance'

type tabItemType = {
  component: any
}

type tabType = {
  [key: string]: tabItemType
}

const tabMap: tabType = {
  'service-overview': {
    component: <ServiceOverview />
  },
  'service-instance': {
    component: <ServiceInstance />
  }
}

const Service = () => {
  const [searchParams, SetSearchParams] = useSearchParams()

  const handleOnTabChange = (activeKey: string) => {
    SetSearchParams({ activeKey })
  }
  const activeKey: string = searchParams.get('activeKey') || 'service-overview'
  const activeComponent= tabMap[activeKey].component
  
  return (
    <PageContainer 
      header={{ 
        title: 'service',
        extra: [
          <Button type="primary" key="1">重启</Button>,
        ]
      }}
      tabActiveKey={activeKey}
      tabList={[
        {
          tab: '总览',
          key: 'service-overview',
          closable: false,
        },
        {
          tab: '实例',
          key: 'service-instance',
          closable: false,
        },
      ]}
      onTabChange={handleOnTabChange}
    >
      <div>
        {activeComponent}
      </div>
    </PageContainer>
  )
}

export default Service