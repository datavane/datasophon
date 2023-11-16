import { ProTable, ProColumns } from '@ant-design/pro-components'
import request from '../../../services/request';
import { useParams, useSearchParams } from 'react-router-dom';
import { useCallback, useEffect, useState } from 'react';
import { APIS } from '../../../services/cluster';
import { message } from 'antd';
import useUrlState from '@ahooksjs/use-url-state';

type AlarmMetricsType = {
    id: number;
    alertQuotaName: string;
    compareMethod: string;
    alertThreshold: number;
    alertGroupName: string;
    noticeGroupId: number;
    quotaStateCode: number;
    quotaState: string;
}

const AlarmMetrics = () => {
    const { clusterId } = useParams()
    const [urlState, setUrlState] = useUrlState()
    const [alarmGroup, setAlarmGroup] = useState<[]>([])
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
    },]

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
    useEffect(()=>{
        alarmGroupList()
    }, [alarmGroupList])

    return (<ProTable
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
          toolbar={{ 
            // 隐藏工具栏设置区
            settings: []
          }}
          pagination={{
            pageSize: 10
          }}
    ></ProTable>)
}

export default AlarmMetrics