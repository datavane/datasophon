import { PageContainer, ProColumns, ProTable } from '@ant-design/pro-components'
import request from '../../services/request'
import { Button } from 'antd';
import { PlusOutlined } from '@ant-design/icons';
import UserModal from './UserModal';
import { useState } from 'react';

type UserType = {
    id: number,
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
  const [userModalOpen, setUserModalOpen] = useState(false);
    return (
        <PageContainer header={{ title: '用户管理'}}>
            <ProTable
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
              open={userModalOpen} 
              onOpenChange={(open: boolean | ((prevState: boolean) => boolean)) => {
                setUserModalOpen(open);
              } }
            />
        </PageContainer>
    )
}

export default UserList