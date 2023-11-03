import { ActionType, PageContainer, ProColumns, ProTable } from '@ant-design/pro-components'
import request from '../../services/request'
import { App, Button } from 'antd';
import { PlusOutlined } from '@ant-design/icons';
import UserModal from './UserModal';
import { MutableRefObject, useRef, useState } from 'react';
import { APIS } from '../../services/user';

type UserType = {
    id?: number,
    username: string;
    password: string;
    email: string;
    phone: string;
}

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
      <a
        key="editable"
        onClick={() => {
        }}
      >
        编辑
      </a>,
      <a
        key="delete"
        onClick={() => {
        }}
      >
        删除
      </a>,
    ],
  },]

const UserList = () => {
  const { message } = App.useApp();
  const [userModalOpen, setUserModalOpen] = useState(false);
  const userActionRef = useRef<any>();
  const handleOnFinishClick = async (values: unknown) => {
    const { code, msg} = await APIS.UserApi.save(values)
    if (code === 200) {
      message.success('新建成功')
      userActionRef.current?.reload()
    } else {
      message.error(msg)
    }
    setUserModalOpen(false)
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
                      setUserModalOpen(true)
                    }}
                    type="primary"
                  >
                    新建
                  </Button>,
                ]}
            ></ProTable>
            <UserModal
              title="新建用户"
              open={userModalOpen}
              onOpenChange={(open: boolean | ((prevState: boolean) => boolean)) => {
                setUserModalOpen(open);
              } }
              modalProps={{
                destroyOnClose: true,
              }}
              onFinish={handleOnFinishClick}
            />
        </PageContainer>
    )
}

export default UserList