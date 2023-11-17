import { ModalForm, ModalFormProps, ProFormText } from "@ant-design/pro-components";

interface ModalType extends ModalFormProps {
    data?: any;
}
const FrameModal = (props: ModalType) => {
    return (
        <ModalForm
            { ...props}
        >
            <ProFormText
                name="rack"
                label="机架名称"
                required
            ></ProFormText>
        </ModalForm>
    )
}

export default FrameModal