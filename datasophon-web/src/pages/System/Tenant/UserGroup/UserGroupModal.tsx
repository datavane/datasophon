import { ModalForm, ModalFormProps, ProFormSelect, ProFormText } from "@ant-design/pro-components";

interface ModalType extends ModalFormProps {
    data?: any;
}
const UserModal = (props: ModalType) => {
    return (
        <ModalForm
            { ...props}
        >
            <ProFormText
                name="groupName"
                label="用户组"
                rules={[
                    {
                      required: true,
                    },
                ]}
            ></ProFormText>
        </ModalForm>
    )
}

export default UserModal