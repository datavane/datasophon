import { PageContainer, ProList } from '@ant-design/pro-components'
import { useNavigate } from 'react-router-dom'
import { Button, Tag } from 'antd'
import { useTranslation } from 'react-i18next'
import request from '../services/request'

type ClusterListType = {
    id: number;
    clusterName: string;
    clusterState: string;
    clusterStateCode: number;
    createTime: string;
    clusterManagerList: Array<T>;
}


const ClusterList = () => {
    const navigate = useNavigate()
    const { t } = useTranslation()

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
            <ProList<ClusterListType>
                grid={{ gutter: 16, column: 3 }}
                rowKey="id"
                request={async (params) => {
                    const { code, data } = await request.ajax({
                        method: 'POST',
                        url: '/api/cluster/list',
                        data: {
                            ...params,
                            // 需要将 current 修改为 page
                            page: params.current,
                        }
                    });
                    return {
                        data,
                        success: code === 200
                    }
                  }}
                  metas={
                    {
                        title: {
                            dataIndex: 'clusterName'
                        },
                        subTitle: {
                            dataIndex: 'clusterState',
                            render: (text, row) => {
                                const StatusMap: any = {
                                    1: 'processing',
                                    2: 'warning',
                                    3: 'error'
                                }
                                return <Tag color={StatusMap[row.clusterStateCode]}>{row.clusterState}</Tag>
                            }
                        },
                        actions: {
                            render: () => {
                                return (
                                    <div style={{ display: 'flex', flex: 1, alignItems: 'center', justifyContent: 'space-around'}}>
                                        <Button type="link" key={1}>授权</Button>
                                        <Button type="link" key={2}>编辑</Button>
                                        <Button type="link" key={3}>配置</Button>
                                        <Button type="link" key={4}>删除</Button>
                                    </div>
                                )
                                },
                            cardActionProps: 'actions'
                        },
                        content: {
                            render: (text, row) => {
                                const user: Array<string> = []
                                row.clusterManagerList.forEach(item => {
                                    user.push(item.username)
                                })
                                return (
                                    <div>
                                        {/* 这里为多个管理员 */}
                                        <div>集群管理员： {user.join(',')}</div>
                                        <div>创建时间：{row.createTime}</div>
                                    </div>
                                )
                            }
                        }
                    }
                  }
                  onItem={(record: ClusterListType) => {
                    return {
                      onClick: () => {
                        navigate(`/cluster/${record.id}`)
                      },
                    };
                  }}
            ></ProList>
        </PageContainer>
    )
}

export default ClusterList