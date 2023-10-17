import { useParams, useNavigate } from 'react-router-dom'
import { Button, Space, Dropdown } from 'antd'
import { PageContainer, ProCard } from '@ant-design/pro-components'
import { EllipsisOutlined } from '@ant-design/icons'
const ClusterManage = () => {
    const { clusterId } = useParams()
    const navigate = useNavigate()
    const handleOnClick = () => {
        navigate('/cluster/1/host')
    }
    const handleOnServiceClick = () => {
        navigate('/cluster/1/service')
    }
    return (<PageContainer
        title="xx集群"
        header={{
            // 缺一个面包屑导航
            extra: [
                <Button type="primary" key="1" onClick={handleOnClick}>主机管理</Button>,
                <Button type="primary" key="1">告警管理</Button>,
                <Button type="primary" key="1">系统管理</Button>,
                <Dropdown
                    key="dropdown"
                    trigger={['click']}
                    menu={{
                    items: [
                        {
                        label: '下拉菜单',
                        key: '1',
                        },
                        {
                        label: '下拉菜单2',
                        key: '2',
                        },
                        {
                        label: '下拉菜单3',
                        key: '3',
                        },
                    ],
                    }}
                >
                    <Button key="4" style={{ padding: '0 8px' }}>
                    <EllipsisOutlined />
                    </Button>
                </Dropdown>,
            ]
        }}
        >
            <Space>
                <ProCard bordered onClick={handleOnServiceClick}>
                    <div> {clusterId}Card content</div>
                    <div>Card content</div>
                    <div>Card content</div>
                </ProCard>
                <ProCard bordered>
                    <div>Card content</div>
                    <div>Card content</div>
                    <div>Card content</div>
                </ProCard>
                <ProCard bordered>
                    <div>Card content</div>
                    <div>Card content</div>
                    <div>Card content</div>
                </ProCard>
            </Space>
        </PageContainer>)
}

export default ClusterManage