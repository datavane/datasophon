import { ProTable, ProColumns } from '@ant-design/pro-components'
import request from '../../../services/request';
import { useParams } from 'react-router-dom';

type AlarmGroupType = {
    id: number;
    alertGroupName: string;
    alertGroupCategory: string;
    alertQuotaNum: number;
}

const AlarmGroup = () => {
    const { clusterId } = useParams()
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
    return (<ProTable
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
    ></ProTable>)
}

export default AlarmGroup