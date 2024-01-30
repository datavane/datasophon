import { PageContainer, ProColumns, ProTable } from '@ant-design/pro-components'
import { AlertOutlined, CheckCircleOutlined, StopOutlined } from '@ant-design/icons'
import { Modal, Button } from 'antd'
import CodeMirror from '../../../components/CodeMirror'
import { useTranslation } from 'react-i18next'
import { useParams } from 'react-router-dom';
import { APIS } from '../../../services/service';
import request from '../../../services/request'
import { App } from 'antd';
import { useEffect, useRef, useState } from 'react';
interface InstanceType {
  id: number,
  clusterId: number,
  createTime: string,
  hostname: string,
  needRestart: boolean,
  roleGroupId: number,
  roleGroupName: string,
  roleType: string,
  serviceId: number,
  serviceName: string,
  serviceRoleName: string,
  serviceRoleState: string,
  serviceRoleStateCode: number,
  updateTime: string,
}

const ServiceInstance = () => {
  const { t } = useTranslation()
  const { serviceId } = useParams()
  const { message } = App.useApp();

  const [roleTypeOptions, setRoleTypeOptions] = useState([])
  const [roleGroupOptions, setRoleGroupOptions] = useState([])
  const [logModalOpen, setLogModalOpen] = useState(false)
  const [logContent, setLogContent] = useState('')
  const [loading, setLoading] = useState(false)
  const codeMirrorRef = useRef<any>()
  const currentRecord = useRef<InstanceType>()
  const timer = useRef<any>(null)

  const columns: ProColumns<InstanceType>[] = [
    {
      dataIndex: 'index',
      valueType: 'indexBorder',
      width: 48,
    },
    {
      title: t('service.roleType'),
      valueType: 'select',
      fieldProps: {
        placeholder: t('service.roleTypePlaceholder'),
        options: roleTypeOptions,
        fieldNames: {
          label: 'serviceRoleName',
          value: 'serviceId'
        }
      },
      dataIndex: 'serviceRoleName'
    },
    {
      title: t('service.hostname'),
      fieldProps: {
        placeholder: t('service.hostnamePlaceholder'),
      },
      dataIndex: 'hostname'
    },
    {
      title: t('service.roleGroupName'),
      valueType: 'select',
      fieldProps: {
        placeholder: t('service.roleGroupNamePlaceholder'),
        options: roleGroupOptions,
        fieldNames: {
          label: 'roleGroupName',
          value: 'serviceInstanceId'
        }
      },
      dataIndex: 'roleGroupName'
    },
    {
      title: t('service.state'),
      valueType: 'select',
      fieldProps: {
        placeholder: t('service.statePlaceholder'),
        options: [
          { value: "1", label: "正在运行" },
          { value: "2", label: "停止" },
          { value: "3", label: "告警" },
          { value: "4", label: "退役中" },
          { value: "5", label: "已退役" },
        ],
      },
      dataIndex: 'serviceRoleState',
      render: (text, record) => [
        getServiceRoleStateIcon(record.serviceRoleStateCode),
        <span style={{marginLeft: '10px'}}>{text}</span>
      ]
    },
    {
      title: t('service.operation'),
      valueType: 'option',
      key: 'operation',
      render: (text, record) => [
        <a key='viewLog' onClick={() => viewLog(record)}>{t('service.viewLog')}</a>
      ]
    }
  ]

  const getServiceRoleStateIcon = (serviceRoleStateCode: number) => {
    return serviceRoleStateCode === 1
      ? <CheckCircleOutlined style={{color: '#52c41a'}} />
        : serviceRoleStateCode === 2
          ? <StopOutlined style={{color: '#f5222f'}} />
            : <AlertOutlined style={{color: '#FF8833'}} />
  }

  const getServiceRoleType = async () => {
    const params = {
      serviceInstanceId: serviceId || ''
    }

    const { code, data, msg } = await APIS.InstanceApi.getServiceRoleType(params)

    if (code === 200) {
      setRoleTypeOptions(data || [])
    } else {
      message.error(msg)
    }
  }

  const getServiceRoleGroupList = async () => {
    const params = {
      serviceInstanceId: serviceId || ''
    }

    const { code, data, msg } = await APIS.InstanceApi.getServiceRoleGroupList(params)

    if (code === 200) {
      setRoleGroupOptions(data || [])
    } else {
      message.error(msg)
    }
  }

  const viewLog = async (record?: InstanceType) => {
    currentRecord.current = record
    setLogContent('')
    setLogModalOpen(true)

    getLog()
  }

  const getLog = async () => {
    const params = {
      serviceRoleInstanceId: currentRecord.current!.id
    }

    const { code, data, msg } = await APIS.InstanceApi.getLog(params)

    if (code === 200) {
      setLogContent(data || '')
    } else {
      message.error(msg)
    }
  }

  useEffect(() => {
    getServiceRoleType()
    getServiceRoleGroupList()
  }, [])

  useEffect(() => {
    if (logModalOpen) {
      timer.current = setInterval(() => {
        getLog()
      }, 10000)
    } else {
      if (timer.current) {
        clearInterval(timer.current)
        timer.current = null
      }
    }
  }, [logModalOpen])

  useEffect(() => {
    if (logContent) {
      const scroller = codeMirrorRef.current?.editor.querySelector('.cm-scroller')
      scroller?.scrollTo(0, scroller.clientHeight + scroller.scrollHeight)
    }
  },  [logContent])

  return (
    <PageContainer header={{ title: t('service.title')}}>
      <ProTable
        columns={columns}
        rowKey="id"
        loading={loading}
        request={async (params) => {
          setLoading(true)
          const { code, data } = await request.ajax({
            method: 'POST',
            url: '/cluster/service/role/instance/list',
            form: {
              ...params,
              // 需要将 current 修改为 page
              page: params.current,
              serviceInstanceId: serviceId
            }
            });
            setLoading(false)
            return {
              data,
              success: code === 200
            }
          }
        }
        toolbar={{ 
          // 隐藏工具栏设置区
          settings: []
        }}
      />
      <Modal open={logModalOpen} width='100%' title='查看日志' footer={[
        <div key='btns' style={{textAlign: 'center'}}><Button onClick={() => {getLog()}}>刷新</Button></div>
      ]} onCancel={() => {setLogModalOpen(false)}}>
        <CodeMirror ref={codeMirrorRef} value={logContent} editable={false} />
      </Modal>
    </PageContainer>
  )
}

export default ServiceInstance