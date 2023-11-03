import { ModalForm, ModalFormProps, ProFormText } from '@ant-design/pro-components'
interface ModalType extends ModalFormProps {
    data?: any;
}

const UserModal = (props: ModalType) => {
    return (<ModalForm
    {...props}
    >
        <ProFormText
            name="username"
            label="用户名"
            rules={[
                {
                  required: true,
                },
              ]}
        />
        <ProFormText
            name="email"
            label="邮箱"
            rules={[
                {
                  required: true,
                  pattern: new RegExp(/\w{3,}(\.\w+)*@[A-z0-9]+(\.[A-z]{2,5}){1,2}/),
                  message: '请输入正确的邮箱地址'
                },
              ]}
        />
        <ProFormText
            name="phone"
            label="电话"
            rules={[
                {
                  required: true,
                },
              ]}
        />
        <ProFormText.Password
            name="password"
            label="用户密码"
            rules={[
                {
                  required: true,
                },
              ]}
        />
    </ModalForm>)
}

export default UserModal