import { PageContainer, ProColumns, ProTable } from '@ant-design/pro-components'
import request from '../../services/request'

type UserType = {
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
    dataIndex: 'email'
  }, {
    title: '电话',
    dataIndex: 'phone'
  }, {
    title: '创建时间',
    dataIndex: 'createTime'
  }]

const UserList = () => {
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
            ></ProTable>
        </PageContainer>
    )
}

export default UserList