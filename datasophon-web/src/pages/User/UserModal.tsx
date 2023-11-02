import { ModalForm, ModalFormProps } from '@ant-design/pro-components'
interface ModalType extends ModalFormProps {
    data?: any;
}

const UserModal = (props: ModalType) => {
    return (<ModalForm
    {...props}
    >
        test
    </ModalForm>)
}

export default UserModal