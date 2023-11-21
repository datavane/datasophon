
import { PlusOutlined } from "@ant-design/icons"
import { ProColumns, ProTable } from "@ant-design/pro-components"
import { Button, Popconfirm, App, Form } from "antd"
import { useTranslation } from "react-i18next"
import { useParams } from "react-router-dom"
import { useCallback, useEffect, useRef, useState } from "react"
import UserModal from "./UserGroupModal"
import { APIS } from "../../../../services/cluster"
import request from "../../../../services/request"

type UserGroupType = {
    id: number;
    groupName: string;
    clusterUsers: string;
}

const UserGroup = () => {
    const { clusterId } = useParams()
    const { t } = useTranslation()
    const { message } = App.useApp()
    const actionRef = useRef<any>();
    const [modalOpen, setModalOpen] = useState(false);
    const [ form ] = Form.useForm<UserGroupType>();
    const [ group, setGroup] = useState<[]>([])
    const columns: ProColumns<UserGroupType>[] = [{ 
        dataIndex: 'index',
        valueType: 'indexBorder',
        width: 48
    },{
        title: '用户组',
        dataIndex: 'groupName'
    },{
        title: '用户',
        dataIndex: 'clusterUsers',
        search: false
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
    const handleOnConfirmClick = async (record: UserGroupType) => {
        const { code, msg } = await APIS.ClusterApi.groupDelete({ id: record.id })
        if (code === 200) {
          message.success(`${t('common.delete')}${t('common.success')}！`)
          actionRef.current?.reload()
        } else {
          message.error(msg)
        }
    }

    const handleOnFinishClick = async (values: any) => {
          // 创建
          const { code, msg } = await APIS.ClusterApi.groupSave({...values, clusterId})
          if (code === 200) {
              message.success('创建成功')
              actionRef.current?.reload()
              setModalOpen(false)
          } else {
              message.error(msg)
          }
    }

    return (<>
        <ProTable
            actionRef={actionRef}
            columns={columns}
            request={async (params) => {
                const { code, data, total } = await request.ajax({
                    method: 'POST',
                    url: '/cluster/group/list',
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
        <UserModal
            form={form}
            open={modalOpen}
            title={t('common.newAdd')}
            onOpenChange={setModalOpen}
            data={{
                group
            }}
            modalProps={{
                // 复杂场景慎用，会引起性能问题
                destroyOnClose: true,
                // https://stackoverflow.com/questions/61056421/warning-instance-created-by-useform-is-not-connect-to-any-form-element
                forceRender: true
            }}
            onFinish={handleOnFinishClick}
        ></UserModal>
    </>)
}

export default UserGroup