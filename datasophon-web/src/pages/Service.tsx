import { PageContainer } from '@ant-design/pro-components'
import { Button } from 'antd'
const Service = () => {
    return (<PageContainer 
        header={{ 
            title: 'service',
            extra: [
                <Button type="primary" key="1">重启</Button>,
            ]
    }}></PageContainer>)
}

export default Service