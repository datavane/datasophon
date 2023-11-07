import { PageContainer, ProCard } from '@ant-design/pro-components'
import { Space} from 'antd'
import { useNavigate } from 'react-router-dom'
import { Button } from 'antd'
import { useTranslation } from 'react-i18next'
const ClusterList = () => {
    const navigate = useNavigate()
    const { t } = useTranslation()
    const handleOnClick = () => {
        navigate('/cluster/1')
    }

    const handleOnNavigateClick = (path: string) => {
        navigate(path)
    }
    return (
        <PageContainer header={{
            title: t('cluster.title'),
            extra: [
                <Button type="primary" key="1">新建集群</Button>,
                <Button type="primary" key="2" onClick={() => {
                    handleOnNavigateClick('/cluster-storage')
                }}>存储库管理</Button>,
                <Button type="primary" key="3" onClick={() => {
                    handleOnNavigateClick('/cluster-framework')
                }}>{t('cluster.framework.title')}</Button>,
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