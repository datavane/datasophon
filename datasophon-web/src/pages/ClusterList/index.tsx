import { ActionType, PageContainer, ProList } from '@ant-design/pro-components'
import { useNavigate } from 'react-router-dom'
import { App, Button, Form, Tag, Modal } from 'antd'
import { useTranslation } from 'react-i18next'
import request from '../../services/request'
import { useLocalStorageState } from 'ahooks'
import ClusterAuthModal from './ClusterAuthModal'
import { useRef, useState } from 'react'
import { APIS } from '../../services/user'
import { APIS as APISCLUSTER } from '../../services/cluster'
import ClusterModal from './ClusterModal'
import { ExclamationCircleFilled } from '@ant-design/icons'

type ClusterListType = {
    id: number;
    clusterName: string;
    clusterState: string;
    clusterStateCode: number;
    createTime: string;
    clusterManagerList: Array<T>;
}

type ClusterAuthType = {
    id: number;
    userIds: Array<number>;
}

type ClusterType = {
    id: number;
    frameCode: string;
    frameName: string;
    clusterFrame: string;
}

type UserOptionType = {
    value: number;
    label: string;
}

const ClusterList = () => {
    const [authModalOpen, setAuthModalOpen] = useState(false);
    const [modalOpen, setModalOpen] = useState(false);
    const clusterActionRef = useRef<ActionType>();
    const navigate = useNavigate()
    const { t } = useTranslation()
    const [user,] = useLocalStorageState<any>('user')
    const [ formAuth ] = Form.useForm<ClusterAuthType>();
    const [ form ] = Form.useForm<ClusterType>();
    const [adminOptions, setAdminOptions] = useState<UserOptionType[]>()
    const [frameworkOptions, setFrameworkOptions] = useState<UserOptionType[]>()
    const [clusterId, setClusterId] = useState<number>()
    const { message } = App.useApp();
    const { confirm } = Modal

    const handleOnNavigateClick = (path: string) => {
        navigate(path)
    }

    const handleOnAuthClick = async (row: ClusterListType) => {
        setAuthModalOpen(true)
        // 初始化集群管理员默认数据
        const userIds: Array<number> = []
        row.clusterManagerList.forEach(item => {
            userIds.push(item.id)
        })
        setClusterId(row.id)
        // 初始化集群管理员下拉列表数据
        const users = await userApiAll()
        setAdminOptions(users)
        formAuth.setFieldsValue({
            userIds,
            id: row.id
        })
    }

    const handleOnClusterModalClick = async (row?: ClusterListType) => {
        setModalOpen(true)
        setClusterId(row?.id)
        // 初始化进群框架列表
        const list = await frameList()
        setFrameworkOptions(list)
        if(row?.id) {
            form.setFieldsValue({
                ...row
            })
        }
    }
    
    const userApiAll = async () => {
        const options: UserOptionType[] = []
        const { code, data } = await APIS.UserApi.all()
        if (code === 200) {
            data.forEach((element: { id: number; username: string }) => {
                options.push({
                    value: element.id,
                    label: element.username
                })
            });
        }
        return options
    }

    const frameList = async () => {
        const options: { value: string; label: string }[] = []
        const { code, data } = await APISCLUSTER.ClusterApi.frameList()
        if (code === 200) {
            data.forEach((element: { frameCode: string }) => {
                options.push({
                    value: element.frameCode,
                    label: element.frameCode
                })
            });
        }
        return options
    }

    const handleOnAuthFinishClick = async (values: any) => {
        const { code, msg } = await APISCLUSTER.ClusterApi.saveClusterManager({ userIds: values.userIds.join(','), clusterId})
        if (code === 200) {
            message.success('授权成功')
            clusterActionRef.current?.reload()
            setAuthModalOpen(false)
        } else {
            message.error(msg)
        }
    }
    const handleOnFinishClick = async (values: any) => {
        if (clusterId) {
            // 编辑
            const { code, msg } = await APISCLUSTER.ClusterApi.clusterUpdate({ ...values, id: clusterId})
            if (code === 200) {
                message.success(t('cluster.editSuccessFul'))
                clusterActionRef.current?.reload()
                setModalOpen(false)
            } else {
                message.error(msg)
            }
        } else {
            // 创建
            const { code, msg } = await APISCLUSTER.ClusterApi.clusterSave(values)
            if (code === 200) {
                message.success(t('cluster.createSuccessFul'))
                clusterActionRef.current?.reload()
                setModalOpen(false)
            } else {
                message.error(msg)
            }
        }
    }

    const handleOnClusterDeleteClick = async (row?: ClusterListType) => {
        confirm({
            title: '提示',
            icon: <ExclamationCircleFilled />,
            content: '确定要删除当前集群？',
            okType: 'danger',
            onOk() {
               return APISCLUSTER.ClusterApi.clusterDelete([row?.id]).then((res: { code: number })=> {
                if (res.code === 200) {
                    message.success('删除成功')
                    clusterActionRef.current?.reload()
                } else {
                    message.success('删除失败')
                }
               })
            },
            onCancel() {
              return 
            },
          });
    }
    return (
        <PageContainer header={{
            title: t('cluster.title'),
            extra: [
                <Button type="primary" key="1" onClick={() => {
                    handleOnClusterModalClick()
                }}>新建集群</Button>,
                <Button type="primary" key="2" onClick={() => {
                    handleOnNavigateClick('/cluster-storage')
                }}>存储库管理</Button>,
                <Button type="primary" key="3" onClick={() => {
                    handleOnNavigateClick('/cluster-framework')
                }}>{t('cluster.framework.title')}</Button>,
            ]
        }}>
            <ProList<ClusterListType>
                actionRef={clusterActionRef}
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
                            render: (text, row) => {
                                const { userType } = user
                                // 仅 admin 才可以授权
                                const authDisabled = userType === 1
                                // 集群状态：正在运行
                                const clusterDisabled = row.clusterStateCode === 2
                                return (
                                    <div style={{ display: 'flex', flex: 1, alignItems: 'center', justifyContent: 'space-around'}}>
                                        {/* only admin */}
                                        <Button type="link" key={1} disabled={!authDisabled} onClick={() => {
                                            handleOnAuthClick(row)
                                        }}>{t('cluster.auth')}</Button>
                                        <Button type="link" key={2} disabled={clusterDisabled} onClick={() => {
                                            handleOnClusterModalClick(row)
                                        }}>{t('common.edit')}</Button>
                                        <Button type="link" key={3} disabled={clusterDisabled}>{t('cluster.config')}</Button>
                                        <Button type="link" key={4} disabled={clusterDisabled} onClick={() => {
                                            handleOnClusterDeleteClick(row)
                                        }}>{t('common.delete')}</Button>
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
                                        <div>{t('cluster.clusterAdministrator')}： {user.join(',')}</div>
                                        <div>{t('common.createTime')}：{row.createTime}</div>
                                    </div>
                                )
                            }
                        }
                    }
                  }
                  onItem={(record: ClusterListType) => {
                    return {
                      onClick: () => {
                        // 待配置状态集群群无法进入
                        if (record.clusterStateCode !== 1) {
                            navigate(`/cluster/${record.id}`)
                        }
                      },
                    };
                  }}
            ></ProList>
            {/* 新建 */}
            {/* 授权 */}
            <ClusterAuthModal
                form={formAuth}
                open={authModalOpen}
                onOpenChange={setAuthModalOpen}
                data={{
                    adminOptions
                }}
                modalProps={{
                  // 复杂场景慎用，会引起性能问题
                  destroyOnClose: true,
                  // https://stackoverflow.com/questions/61056421/warning-instance-created-by-useform-is-not-connect-to-any-form-element
                  forceRender: true
                }}
                onFinish={handleOnAuthFinishClick}
            ></ClusterAuthModal>
            {/* 编辑 and 创建 */}
            <ClusterModal
                form={form}
                open={modalOpen}
                title={clusterId ? t('cluster.editCluster') : t('cluster.createCluster')}
                onOpenChange={setModalOpen}
                data={{
                    frameworkOptions
                }}
                modalProps={{
                    // 复杂场景慎用，会引起性能问题
                    destroyOnClose: true,
                    // https://stackoverflow.com/questions/61056421/warning-instance-created-by-useform-is-not-connect-to-any-form-element
                    forceRender: true
                }}
                onFinish={handleOnFinishClick}
            ></ClusterModal>
            {/* 配置 */}
            {/* 删除 */}
        </PageContainer>
    )
}

export default ClusterList