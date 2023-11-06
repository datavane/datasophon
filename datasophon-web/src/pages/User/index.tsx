import { useRef, useState } from 'react';
import { App, Button, Form, Popconfirm } from 'antd';
import { PageContainer, ProColumns, ProTable } from '@ant-design/pro-components'
import {  useTranslation } from 'react-i18next'
import { PlusOutlined } from '@ant-design/icons';
import request from '../../services/request'
import UserModal from './UserModal';
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
  const { t } = useTranslation()
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
    title: t('user.username'),
    dataIndex: 'username'
  }, {
    title: t('user.email'),
    search: false,
    dataIndex: 'email'
  }, {
    title: t('user.phone'),
    search: false,
    dataIndex: 'phone'
  }, {
    title: t('user.createTime'),
    search: false,
    dataIndex: 'createTime'
  },{
    title: t('user.operation'),
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
        {t('common.edit')}
      </Button>,
      <Popconfirm
        title={t('user.deleteConfirm')}
        key="confirm"
        onConfirm={() => {
          handleOnConfirmClick(record)
        }}
      >
          <Button
            key="delete"
            type="link"
            disabled={record.userType == 1 }
          >
          {t('common.delete')}
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
      message.success(`${t('common.delete')}${t('common.success')}！`)
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
        message.success(`${t('common.edit')}${t('common.success')}！`)
        setUserModalOpen(false)
        userActionRef.current?.reload()
      } else {
        message.error(msg)
      }
    } else {
      const { code, msg} = await APIS.UserApi.save(values)
      if (code === 200) {
        message.success(`${t('common.newAdd')}${t('common.success')}！`)
        setUserModalOpen(false)
        userActionRef.current?.reload()
      } else {
        message.error(msg)
      }
    }
  }
    return (
        <PageContainer header={{ title: t('user.title')}}>
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
                    {t('common.newAdd')}
                  </Button>,
                ]}
                toolbar={{ 
                  // 隐藏工具栏设置区
                  settings: []
                }}
            ></ProTable>
            <UserModal
              form={form}
              title={userModalType === ModalType.Add ? `${t('common.newAdd')}${t('user.user')}` : `${t('common.edit')}${t('user.user')}`}
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