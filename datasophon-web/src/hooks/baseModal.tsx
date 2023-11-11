import { ModalForm, ModalFormProps } from "@ant-design/pro-components"
import { useBaseModal } from "./useBaseModal"
import { ReactNode } from "react";
import { Form } from "antd";

interface BaseModalType extends  ModalFormProps{
    id: string;
    children: ReactNode,
    data: any
}


export const BaseModal: React.FC<BaseModalType> = ({ id, children, data, ...rest}) =>{
    const modalForm = useBaseModal(id)
    const [ form ] = Form.useForm();
    return (
        <ModalForm
            form={form}
            open={modalForm.visible}
            onOpenChange={(visible) => {
                if (!visible) {
                    modalForm.hide(!false)
                }
            }}
            onFinish={async () => {
                modalForm.resolve({ ...data, ...form.getFieldsValue() })
                modalForm.hide(!false)
            }}
            {...rest}
        >
            {children}
        </ModalForm>
    )
}

