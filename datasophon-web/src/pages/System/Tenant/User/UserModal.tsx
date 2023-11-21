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
                name="username"
                label="用户名称"
                rules={[
                    {
                      required: true,
                    },
                ]}
            ></ProFormText>
            <ProFormSelect
                name="mainGroupId"
                label="主用户组"
                options={props.data.group || []}
                rules={[
                    {
                      required: true,
                    },
                ]}
            ></ProFormSelect>
            <ProFormSelect
                name="otherGroupIds"
                label="附属用户组"
                mode="multiple"
                options={props.data.group || []}
            ></ProFormSelect>
        </ModalForm>
    )
}

export default UserModal