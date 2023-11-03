import { PageContainer, ProColumns, ProTable } from '@ant-design/pro-components'
import request from '../../services/request'
import { App, Button, Form, Popconfirm } from 'antd';
import { PlusOutlined } from '@ant-design/icons';
import UserModal from './UserModal';
import { useRef, useState } from 'react';
import { APIS } from '../../services/user';

type UserType = {
    id?: number;
    userType?: number;
    username: string;
    password: string;
    email: string;
    phone: string;
}

enum ModalType {
    Add = 'add',
    Edit = 'edit' 
}

const UserList = () => {
  const { message } = App.useApp();
  const [userModalOpen, setUserModalOpen] = useState(false);
  const [ userModalType, setUserModalType] = useState('add')
  const [ currentRow, setCurrentRow ] = useState<any>()
  const userActionRef = useRef<any>();
  const [ form ] = Form.useForm<UserType>();
  const columns: ProColumns<UserType>[] = [{
    dataIndex: 'index',
    valueType: 'indexBorder',
    width: 48,
  }, {
    title: '用户名',
    dataIndex: 'username'
  }, {
    title: '邮箱',
    search: false,
    dataIndex: 'email'
  }, {
    title: '电话',
    search: false,
    dataIndex: 'phone'
  }, {
    title: '创建时间',
    search: false,
    dataIndex: 'createTime'
  },{
    title: '操作',
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
        编辑
      </Button>,
      <Popconfirm
        title="确认删除当前用户？"
        onConfirm={() => {
          handleOnConfirmClick(record)
        }}
      >
          <Button
            key="delete"
            type="link"
            disabled={record.userType == 1 }
          >
          删除
        </Button>
      </Popconfirm>
     ,
    ],
  },]



  const handleOnModalTriggerClick = (type: ModalType, record?: UserType) => {
    if (type === 'add') {
      setUserModalType(ModalType.Add)
      setUserModalOpen(true)
    } else {
      setUserModalType(ModalType.Edit)
      setCurrentRow(record)
      setUserModalOpen(true)
      form.setFieldsValue({
        ...record,
        password: ''
      })
    }
  }

  const handleOnConfirmClick = async (record: UserType) =>  {
    const { code, msg } = await APIS.UserApi.delete([record.id])
    if (code === 200) {
      message.success('删除成功！')
      userActionRef.current?.reload()
    } else {
      message.error(msg)
    }
  }

  const handleOnFinishClick = async (values: any) => {
    if(userModalType === 'edit') {
      const { code, msg} = await APIS.UserApi.update({
        ...currentRow,
        ...values
      })
      if (code === 200) {
        message.success('编辑成功')
        setUserModalOpen(false)
        userActionRef.current?.reload()
      } else {
        message.error(msg)
      }
    } else {
      const { code, msg} = await APIS.UserApi.save(values)
      if (code === 200) {
        message.success('新建成功')
        setUserModalOpen(false)
        userActionRef.current?.reload()
      } else {
        message.error(msg)
      }
    }
  }
    return (
        <PageContainer header={{ title: '用户管理'}}>
            <ProTable
                actionRef={userActionRef}
                columns={columns}
                rowKey="id"
                request={async (params) => {
                    const { code, data } = await request.ajax({
                        method: 'POST',
                        url: '/api/user/list',
                        form: {
                            ...params,
                            // 需要将 current 修改为 page
                            page: params.current,
                        }
                    });
                    return {
                        data,
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
                    新建
                  </Button>,
                ]}
            ></ProTable>
            <UserModal
              form={form}
              title={userModalType === ModalType.Add ? '新建用户' : '编辑用户'}
              open={userModalOpen}
              onOpenChange={setUserModalOpen}
              modalProps={{
                // 复杂场景慎用，会引起性能问题
                destroyOnClose: true,
                // https://stackoverflow.com/questions/61056421/warning-instance-created-by-useform-is-not-connect-to-any-form-element
                forceRender: true
              }}
              onFinish={handleOnFinishClick}
            />
        </PageContainer>
    )
}

export default UserList