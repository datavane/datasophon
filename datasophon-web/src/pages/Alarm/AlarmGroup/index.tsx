import { ProTable, ProColumns, ActionType } from '@ant-design/pro-components'
import request from '../../../services/request';
import { useParams } from 'react-router-dom';
import { App, Button, Form } from 'antd';
import { PlusOutlined } from '@ant-design/icons';
import { useTranslation } from 'react-i18next';
import AlarmGroupModal from './AlarmGroupModal';
import { useCallback, useEffect, useRef, useState } from 'react';
import { APIS } from '../../../services/cluster';

type AlarmGroupType = {
    id: number;
    alertGroupName: string;
    alertGroupCategory: string;
    alertQuotaNum: number;
}

const AlarmGroup = () => {
    const { clusterId } = useParams()
    const [modalOpen, setModalOpen] = useState(false);
    const [groupOptions, setGroupOptions] = useState<any[]>()
    const alarmActionRef = useRef<ActionType>();
    const [ form ] = Form.useForm<AlarmGroupType>();
    const { t } = useTranslation()
    const { message } = App.useApp();
    const columns: ProColumns<AlarmGroupType>[] = [
    { 
        dataIndex: 'index',
        valueType: 'indexBorder',
        width: 48
    },{
        title: '名称',
        dataIndex: 'alertGroupName'
    },
    {
        title: '模板类别',
        dataIndex: 'alertGroupCategory',
        search: false
    },
    {
        title: '告警指标数',
        dataIndex: 'alertQuotaNum',
        search: false
    },]
    const handleOnModalTriggerClick = async () => {
        const options = await frameServiceList()
        setGroupOptions(options)
        setModalOpen(true)
    }
    const handleOnFinishClick = async (values: any) => {
        const { code, msg} = await APIS.ClusterApi.alarmGroupSave({...values, clusterId})
      if (code === 200) {
        message.success(`${t('common.newAdd')}${t('common.success')}！`)
        setModalOpen(false)
        alarmActionRef.current?.reload()
      } else {
        message.error(msg)
      }
    }

    // TODO: 次接口待优化，获取的无用的数据，数据量导致接口请求速度很慢
    const frameServiceList = async () => {
        const options: { value: string; label: string }[] = []
        const { code, data } = await APIS.ClusterApi.frameServiceList({clusterId})
        if (code === 200) {
            data.forEach((element: { serviceName: string }) => {
                options.push({
                    value: element.serviceName,
                    label: element.serviceName
                })
            });
        }
        return options
    }
    return (
    <>
        <ProTable<AlarmGroupType>
            actionRef={alarmActionRef}
            columns={columns}
            rowKey="id"
            request={async (params) => {
                const { code, data, total } = await request.ajax({
                    method: 'POST',
                    url: '/alert/group/list',
                    form: {
                        ...params,
                        // 需要将 current 修改为 page
                        page: params.current,
                        clusterId
                    }
                });
                return {
                    data,
                    total,
                    success: code === 200
                }
            }}
            toolbar={{ 
                // 隐藏工具栏设置区
                settings: []
            }}
            pagination={{
                pageSize: 10
            }}
            toolBarRender={() => [
                <Button
                key="button"
                icon={<PlusOutlined />}
                onClick={() => {
                    handleOnModalTriggerClick()
                }}
                type="primary"
                >
                {t('common.newAdd')}
                </Button>,
            ]}
        ></ProTable>
        <AlarmGroupModal
            form={form}
            open={modalOpen}
            title={t('common.newAdd')}
            onOpenChange={setModalOpen}
            data={{
                groupOptions
            }}
            modalProps={{
                // 复杂场景慎用，会引起性能问题
                destroyOnClose: true,
                // https://stackoverflow.com/questions/61056421/warning-instance-created-by-useform-is-not-connect-to-any-form-element
                forceRender: true
            }}
            onFinish={handleOnFinishClick}
        ></AlarmGroupModal>
    </>
    )
}

export default AlarmGroup