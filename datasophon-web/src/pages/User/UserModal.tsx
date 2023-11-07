import { ModalForm, ModalFormProps, ProFormText } from '@ant-design/pro-components'
import { useTranslation } from 'react-i18next';
interface ModalType extends ModalFormProps {
    data?: any;
}

const UserModal = (props: ModalType) => {
    const { t } = useTranslation()
    return (<ModalForm
    {...props}
    >
        <ProFormText
            name="username"
            label={t('user.username')}
            rules={[
                {
                  required: true,
                },
              ]}
        />
        <ProFormText
            name="email"
            label={t('user.email')}
            rules={[
                {
                  required: true,
                  pattern: new RegExp(/\w{3,}(\.\w+)*@[A-z0-9]+(\.[A-z]{2,5}){1,2}/),
                  message: t('user.emailMessage')
                },
              ]}
        />
        <ProFormText
            name="phone"
            label={t('user.phone')}
            rules={[
                {
                  required: true,
                },
              ]}
        />
        <ProFormText.Password
            name="password"
            label={t('user.password')}
            rules={[
                {
                  required: true,
                },
              ]}
        />
    </ModalForm>)
}

export default UserModal