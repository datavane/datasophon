import { PageContainer, ProCard } from '@ant-design/pro-components'
import { Space} from 'antd'
import { useNavigate } from 'react-router-dom'
import { Button } from 'antd'
const ClusterList = () => {
    const navigate = useNavigate()
    const handleOnClick = () => {
        navigate('/cluster/1')
    }
    return (
        <PageContainer header={{
            title: '集群',
            extra: [
                <Button type="primary" key="1">新建集群</Button>,
            ]
        }}>
            <Space>
                <ProCard title="集群1" bordered onClick={handleOnClick}>
                    <div>Card content</div>
                    <div>Card content</div>
                    <div>Card content</div>
                </ProCard>
                <ProCard title="集群2" bordered>
                    <div>Card content</div>
                    <div>Card content</div>
                    <div>Card content</div>
                </ProCard>
                <ProCard title="集群3" bordered>
                    <div>Card content</div>
                    <div>Card content</div>
                    <div>Card content</div>
                </ProCard>
            </Space>
        </PageContainer>
    )
}

export default ClusterList