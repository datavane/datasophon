import { ProTable, ProColumns, PageContainer } from '@ant-design/pro-components'
import { useParams } from 'react-router-dom';
import { useCallback, useEffect, useRef, useState } from 'react';
import { Button, Form, Popover, Progress, Space, message } from 'antd';
import useUrlState from '@ahooksjs/use-url-state';
import { useTranslation } from 'react-i18next';
import CreateModal from './CreateModal';
import { PlusOutlined } from '@ant-design/icons';
import { APIS } from '../../services/cluster';
import request from '../../services/request';
import RoleModal from './RoleModal';

type ColumnType = {
    id: number;
    hostname: string;
    ip: string;
    rack: string;
    totalDisk: number;
    coreNum: number;
    totalMem: number;
    usedMem: number;
    usedDisk: number;
    averageLoad: number;
    hostState: number;
    cpuArchitecture: string;
    nodeLabel: string;
    serviceRoleNum: number;
}

enum ModalType {
    Add = 'add',
    Edit = 'edit' 
}

const Host = () => {
    const { clusterId } = useParams()
    const { t } = useTranslation()
    const [urlState, setUrlState] = useUrlState()
    const [modalOpen, setModalOpen] = useState(false);
    const [roleModalOpen, setRoleModalOpen] = useState(false);
    const [ form ] = Form.useForm<ColumnType>();
    const [alarmGroup, setAlarmGroup] = useState<[]>([])
    const [serviceRoleName, setServiceRoleName] = useState<[]>([])
    const [ alarmModalType, setAlarmModalType] = useState('add')
    const [ currentRow, setCurrentRow ] = useState<any>()
    const alarmActionRef = useRef<any>();
    let currentSelectedRowKeys: (string | number)[] = [];
    const columns: ProColumns<ColumnType>[] = [
        { 
            dataIndex: 'index',
            valueType: 'indexBorder',
            width: 48
        },{
            title: '状态',
            dataIndex: 'hostState',
            valueEnum: {
                '1': {
                    text: '正常',
                    status: 'Success',
                },
                '2': {
                    text: '掉线',
                    status: 'Default',
                },
                '3': {
                    text: '存在告警',
                    status: 'Warning',
                }
            }
        },{
            title: '主机',
            dataIndex: 'hostname',
        },
        {
            title: 'IP地址',
            dataIndex: 'ip',
        },
        {
            title: '核数',
            dataIndex: 'coreNum',
            search: false
        },
        {
            title: '内存使用',
            dataIndex: 'usedMem',
            search: false,
            render(dom, entity, index, action, schema) {
                const percent = parseFloat((entity.usedMem  / entity.totalMem).toFixed(2)) * 100
                const text = `${entity.usedMem}GB/${entity.totalMem}GB`
                return (
                    <>
                        <span>{text}</span>
                        <Progress percent={percent} showInfo={false} strokeColor={status}></Progress>
                    </>
                )
            },
        },
        {
            title: '磁盘使用',
            dataIndex: 'usedDisk',
            search: false,
            render(dom, entity, index, action, schema) {
                const percent = parseFloat((entity.usedDisk  / entity.totalDisk).toFixed(2)) * 100
                const text = `${entity.usedDisk}GB/${entity.totalDisk}GB`
                return (
                    <>
                        <span>{text}</span>
                        <Progress percent={percent} showInfo={false}></Progress>
                    </>
                )
            },
        },
        {
            title: '平均负载',
            dataIndex: 'averageLoad',
            search: false
        },
        {
            title: '标签',
            dataIndex: 'nodeLabel',
            search: false,
        },
        {
            title: '机架',
            dataIndex: 'rack',
            search: false
        },
        {
            title: 'CPU 架构',
            dataIndex: 'cpuArchitecture',
            valueEnum: {
                'x86_64': {
                    text: 'x86_64'
                },
                'aarch64': {
                    text: 'aarch64'
                }
            }
        },
        {
            title: '角色',
            dataIndex: 'serviceRoleNum',
            search: false,
            render(dom, entity, index, action, schema) {
                return (
                  <Button type="link" onClick={() => {
                    setCurrentRow(entity)
                    setRoleModalOpen(true)
                  }}>{entity.serviceRoleNum}</Button>
                )
            },
        }
    ]

    const handleOnModalTriggerClick = async (type: ModalType, record?: ColumnType) => {
        if (type === 'add') {
            setAlarmModalType(ModalType.Add)
            setModalOpen(true)
          } else {
            setAlarmModalType(ModalType.Edit)
            setCurrentRow(record)
            setModalOpen(true)
            const option = await getServiceRoleByServiceName(clusterId, record?.alertGroupId || '')
            setServiceRoleName(option)
            form.setFieldsValue({
              ...record,
            })
          }
    }

    const handleOnConfirmClick = async (record: ColumnType) => {
      const { code, msg } = await APIS.ClusterApi.alertQuotaDelete([record.id])
      if (code === 200) {
        message.success(`${t('common.delete')}${t('common.success')}！`)
        alarmActionRef.current?.reload()
      } else {
        message.error(msg)
      }
    }

    const getServiceRoleByServiceName = async (clusterId: string | undefined, alertGroupId: string)=> {
      const options: any = []
      const { code, data} = await APIS.ClusterApi.getServiceRoleByServiceName({
        alertGroupId,
        clusterId
      })
      
      if (code === 200) {
        data.forEach((element: { id: number; serviceRoleName: string; }) => {
          options.push({
              value: element.id,
              label: element.serviceRoleName
          })
        });
      }
      return options
    }
    const alarmGroupList = useCallback(async () => {
        const { code, data, msg } = await APIS.ClusterApi.alarmGroupList({
            pageSize: 1000,
            page: 1,
            clusterId
        })
        const options: any = []
        if (code === 200) {
            data.forEach((element: { id: number; alertGroupName: string; }) => {
                options.push({
                    value: element.id,
                    label: element.alertGroupName
                })
            });
            setAlarmGroup(options)
        } else {
            message.error(msg)
        }
    }, [clusterId])

    const handleOnFinishClick = async (values: any) => {
      if(alarmModalType === 'edit') {
        const { code, msg} = await APIS.ClusterApi.alertQuotaUpdate({
          ...currentRow,
          ...values
        })
        if (code === 200) {
          message.success(`${t('common.edit')}${t('common.success')}！`)
          setModalOpen(false)
          alarmActionRef.current?.reload()
        } else {
          message.error(msg)
        }
      } else {
        const { code, msg} = await APIS.ClusterApi.alertQuotaSave(values)
        if (code === 200) {
          message.success(`${t('common.newAdd')}${t('common.success')}！`)
          setModalOpen(false)
          alarmActionRef.current?.reload()
        } else {
          message.error(msg)
        }
      }
    }

    useEffect(()=>{
        alarmGroupList()
    }, [alarmGroupList])

    return (
    <PageContainer title='主机管理'>
      <ProTable
          actionRef={alarmActionRef}
          columns={columns}
          rowKey="id"
          request={async (params) => {
              const { code, data, total } = await request.ajax({
                  method: 'POST',
                  url: '/api/cluster/host/list',
                  form: {
                      ...params,
                      // 需要将 current 修改为 page
                      page: params.current,
                      clusterId,
                  }
              });
              return {
                  data,
                  total,
                  success: code === 200
              }
            }}
            toolBarRender={() => [
              <Button
                key="button"
                icon={<PlusOutlined />}
                onClick={() => {
                  handleOnModalTriggerClick(ModalType.Add)
                }}
                type="primary"
              >
                {t('common.newAdd')}
              </Button>,
            ]}
            rowSelection={{
              defaultSelectedRowKeys: []
            }}
            tableAlertRender={({
              selectedRowKeys,
              onCleanSelected,
            }) => {
              currentSelectedRowKeys = selectedRowKeys
              return (
                <Space size={24}>
                  <span>
                    已选 {selectedRowKeys.length} 项
                    <a style={{ marginInlineStart: 8 }} onClick={onCleanSelected}>
                      取消选择
                    </a>
                  </span>
                </Space>
              );
            }}
            tableAlertOptionRender={() => {
              return (
                <Space size={16}>
                  <a key="start" onClick={
                    async () => {
                      const { code } = await APIS.ClusterApi.alertQuotaStart({ alertQuotaIds: currentSelectedRowKeys.join(','), clusterId})
                      if (code === 200) {
                        console.log(alarmActionRef)
                        alarmActionRef.current?.reload()
                      } else {
                        message.error('启用指标失败')
                      }
                  }}>
                    启用指标
                  </a>
                  <a key="stop" onClick={
                    async () => {
                      const { code } = await APIS.ClusterApi.alertQuotaStart({ alertQuotaIds: currentSelectedRowKeys.join(','), clusterId})
                      if (code === 200) {
                        message.success('停用指标成功')
                        alarmActionRef.current?.reload()
                      } else {
                        message.error('停用指标失败')
                      }
                  }}>
                    停用指标
                  </a>
                </Space>
              );
            }}
            toolbar={{ 
              // 隐藏工具栏设置区
              settings: []
            }}
            pagination={{
              pageSize: 10
            }}
      ></ProTable>
      <CreateModal
            layout="horizontal"
            labelCol={
              {
                span: 4,
              }
            }
            form={form}
            open={modalOpen}
            title={alarmModalType === ModalType.Add ? `${t('common.newAdd')}` : `${t('common.edit')}`}
            onOpenChange={setModalOpen}
            data={{
              alarmGroup,
              serviceRoleName,
              onAlertGroupIdChange:  async (value: string) => {
                const option = await getServiceRoleByServiceName(clusterId, value)
                setServiceRoleName(option)
                form.setFieldValue('serviceRoleName', option[0].value)
              }
            }}
            modalProps={{
                // 复杂场景慎用，会引起性能问题
                destroyOnClose: true,
                // https://stackoverflow.com/questions/61056421/warning-instance-created-by-useform-is-not-connect-to-any-form-element
                forceRender: true
            }}
            onFinish={handleOnFinishClick}
        ></CreateModal>
        <RoleModal
          title="角色列表"
          open={roleModalOpen} 
          data={{
            hostname: currentRow?.hostname,
            clusterId
          }}
          onCancel={() => {
            setRoleModalOpen(false)
          }}
          footer={null}
        ></RoleModal>
    </PageContainer>
    )
}

export default Host