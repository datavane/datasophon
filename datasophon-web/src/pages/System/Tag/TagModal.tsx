import { ModalForm, ModalFormProps, ProFormText } from "@ant-design/pro-components";

interface ModalType extends ModalFormProps {
    data?: any;
}
const TagModal = (props: ModalType) => {
    return (
        <ModalForm
            { ...props}
        >
            <ProFormText
                name="rack"
                label="标签名称"
                required
            ></ProFormText>
        </ModalForm>
    )
}

export default TagModal