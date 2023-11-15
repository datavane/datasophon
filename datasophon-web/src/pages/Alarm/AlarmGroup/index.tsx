import { ProTable, ProColumns } from '@ant-design/pro-components'
import request from '../../../services/request';

type AlarmGroupType = {
    id: number;
    alertQuotaName: string;
    compareMethod: string;
    alertThreshold: number;
    alertGroupName: string;
    noticeGroupId: number;
    quotaStateCode: number;
    quotaState: string;
}

const AlarmGroup = () => {
    const columns: ProColumns<AlarmGroupType>[] = [
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
        dataIndex: 'alertGroupName'
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
    },
    ]
    return (<ProTable
        columns={columns}
        rowKey="id"
        request={async (params) => {
            const { code, data, total } = await request.ajax({
                method: 'POST',
                url: '/cluster/alert/quota/list',
                form: {
                    ...params,
                    // 需要将 current 修改为 page
                    page: params.current,
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

export default AlarmGroup