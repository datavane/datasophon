import { PlusOutlined } from "@ant-design/icons"
import { ProColumns, ProTable } from "@ant-design/pro-components"
import { Button, Popconfirm, App, Form } from "antd"
import { useTranslation } from "react-i18next"
import { useParams } from "react-router-dom"
import request from "../../../services/request"
import { APIS } from "../../../services/cluster"
import { useRef, useState } from "react"
import FrameModal from "./FrameModal"

type FrameType = {
    id: number;
    rack: string;
}

const Frame = () => {
    const { clusterId } = useParams()
    const { t } = useTranslation()
    const { message } = App.useApp()
    const frameActionRef = useRef<any>();
    const [modalOpen, setModalOpen] = useState(false);
    const [ form ] = Form.useForm<FrameType>();
    const columns: ProColumns<FrameType>[] = [{ 
        dataIndex: 'index',
        valueType: 'indexBorder',
        width: 48
    },{
        title: '名称',
        dataIndex: 'rack'
    },{
        title: t('user.operation'),
        valueType: 'option',
        key: 'option',
        render: (text, record, _, action) => [
          <Popconfirm
            title="确认删除吗？"
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

    const handleOnModalTriggerClick = () => {
      setModalOpen(true)
    }
    const handleOnConfirmClick = async (record: FrameType) => {
        const { code, msg } = await APIS.ClusterApi.rackDelete({ rackId: record.id })
        if (code === 200) {
          message.success(`${t('common.delete')}${t('common.success')}！`)
          frameActionRef.current?.reload()
        } else {
          message.error(msg)
        }
    }

    const handleOnFinishClick = async (values: any) => {
          // 创建
          const { code, msg } = await APIS.ClusterApi.rackSave({...values, clusterId})
          if (code === 200) {
              message.success('创建成功')
              frameActionRef.current?.reload()
              setModalOpen(false)
          } else {
              message.error(msg)
          }
  }

    return (<>
        <ProTable
            actionRef={frameActionRef}
            columns={columns}
            request={async (params) => {
                const { code, data, total } = await request.ajax({
                    method: 'POST',
                    url: '/cluster/rack/list',
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
            rowKey="id"
            search={false}
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
        <FrameModal
            form={form}
            open={modalOpen}
            title={clusterId ? t('cluster.editCluster') : t('cluster.createCluster')}
            onOpenChange={setModalOpen}
            modalProps={{
                // 复杂场景慎用，会引起性能问题
                destroyOnClose: true,
                // https://stackoverflow.com/questions/61056421/warning-instance-created-by-useform-is-not-connect-to-any-form-element
                forceRender: true
            }}
            onFinish={handleOnFinishClick}
        ></FrameModal>
    </>)
}

export default Frame