import { ProTable } from "@ant-design/pro-components"
import { Button, message } from "antd"
import { useParams } from "react-router-dom"
import { APIS } from "../../services/cluster"
import { useTranslation } from "react-i18next"


type ListType = {
    data: any
}

const CreateCheckList = (props: ListType) => {
    const { t } = useTranslation()
    const { clusterId } = useParams()
    const analysisColumns = [
        { 
            dataIndex: 'index',
            valueType: 'indexBorder',
            width: 48
        },
        {
          title: '主机',
          dataIndex: 'hostname'
        },
        {
          title: '当前受管',
          dataIndex: 'managed',
          render: (text, record, _, action) => {
            return record.managed === true ? '是' : '否'
          }
        },
        {
          title: '检查结果',
          dataIndex: 'checkResult',
          render: (text, record, _, action) => {
            return record.checkResult?.msg
          }
        },{
          title: t('user.operation'),
          valueType: 'option',
          key: 'option',
          render: (text, record, _, action) => [
            <Button
              key="restart"
              type="link"
              onClick={async () => {
                const { code, msg } = await APIS.ClusterApi.rehostCheck({
                  clusterId,
                  hostnames: record.hostname,
                  sshPort: record.sshPort,
                  sshUser: record.sshUser
                })
                if (code === 200) {
                  message.success(msg)
                } else {
                  message.error(msg)
                }
              }}
            >
              重试
            </Button>]
        }
        ]
    return (
        <ProTable
            columns={analysisColumns}
            search={false}
            toolbar={{ 
            // 隐藏工具栏设置区
            settings: []
            }}
            dataSource={props.data}
      ></ProTable>
    )
}

export default CreateCheckList