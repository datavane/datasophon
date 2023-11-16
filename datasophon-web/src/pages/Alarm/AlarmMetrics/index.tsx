import { ProTable, ProColumns } from '@ant-design/pro-components'
import request from '../../../services/request';
import { useParams } from 'react-router-dom';
import { useCallback, useEffect, useRef, useState } from 'react';
import { APIS } from '../../../services/cluster';
import { Button, Form, Popconfirm, message } from 'antd';
import useUrlState from '@ahooksjs/use-url-state';
import { useTranslation } from 'react-i18next';
import AlarmMetricsModal from './AlarmMetricsModal';
import { PlusOutlined } from '@ant-design/icons';

type AlarmMetricsType = {
    id: number;
    alertQuotaName: string;
    compareMethod: string;
    alertThreshold: number;
    alertGroupName: string;
    noticeGroupId: number;
    quotaStateCode: number;
    quotaState: string;
    alertGroupId: string
}

enum ModalType {
    Add = 'add',
    Edit = 'edit' 
}

const AlarmMetrics = () => {
    const { clusterId } = useParams()
    const { t } = useTranslation()
    const [urlState, setUrlState] = useUrlState()
    const [modalOpen, setModalOpen] = useState(false);
    const [ form ] = Form.useForm<AlarmMetricsType>();
    const [alarmGroup, setAlarmGroup] = useState<[]>([])
    const [serviceRoleName, setServiceRoleName] = useState<[]>([])
    const [ alarmModalType, setAlarmModalType] = useState('add')
    const [ currentRow, setCurrentRow ] = useState<any>()
    const alarmActionRef = useRef<any>();
    const columns: ProColumns<AlarmMetricsType>[] = [
    { 
        dataIndex: 'index',
        valueType: 'indexBorder',
        width: 48
    },{
        title: '指标名称',
        dataIndex: 'alertQuotaName'
    },
    {
        title: '比较方式',
        dataIndex: 'compareMethod',
        search: false
    },
    {
        title: '告警阈值',
        dataIndex: 'alertThreshold',
        search: false
    },
    {
        title: '告警组',
        dataIndex: 'alertGroupName',
        valueType: 'select',
        fieldProps: {
            options: alarmGroup,
        }
    },
    {
        title: '通知组',
        dataIndex: 'noticeGroupId',
        search: false
    },
    {
        title: '状态',
        dataIndex: 'quotaState',
        search: false
    },{
        title: t('user.operation'),
        valueType: 'option',
        key: 'option',
        render: (text, record, _, action) => [
          <Button
            key="editable"
            type="link"
            onClick={() => {
              handleOnModalTriggerClick(ModalType.Edit, record)
            }}
          >
            {t('common.edit')}
          </Button>,
          <Popconfirm
            title={t('user.deleteConfirm')}
            key="confirm"
            onConfirm={() => {
              handleOnConfirmClick(record)
            }}
          >
              <Button
                key="delete"
                type="link"
              >
              {t('common.delete')}
            </Button>
          </Popconfirm>
         ,
        ],
      }]
    
    const handleOnModalTriggerClick = async (type: ModalType, record?: AlarmMetricsType) => {
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

    const handleOnConfirmClick = (record: AlarmMetricsType) => {}

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
    <>
      <ProTable
          actionRef={alarmActionRef}
          columns={columns}
          rowKey="id"
          request={async (params) => {
              if (params.alertGroupName) {
                  setUrlState({alertGroupId: params.alertGroupName})
              }
              const { code, data, total } = await request.ajax({
                  method: 'POST',
                  url: '/cluster/alert/quota/list',
                  form: {
                      ...params,
                      // 需要将 current 修改为 page
                      page: params.current,
                      clusterId,
                      alertGroupId: params.alertGroupName || ''
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
            toolbar={{ 
              // 隐藏工具栏设置区
              settings: []
            }}
            pagination={{
              pageSize: 10
            }}
      ></ProTable>
      <AlarmMetricsModal
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
              serviceRoleName
            }}
            modalProps={{
                // 复杂场景慎用，会引起性能问题
                destroyOnClose: true,
                // https://stackoverflow.com/questions/61056421/warning-instance-created-by-useform-is-not-connect-to-any-form-element
                forceRender: true
            }}
            onFinish={handleOnFinishClick}
            onAlertGroupIdChange={ async (value) => {
              const option = await getServiceRoleByServiceName(clusterId, value)
              setServiceRoleName(option)
              form.setFieldValue('serviceRoleName', option[0].value)

            }}
        ></AlarmMetricsModal>
    </>
    )
}

export default AlarmMetrics