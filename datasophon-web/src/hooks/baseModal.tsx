import { ModalForm } from "@ant-design/pro-components"
import { useBaseModal } from "./useBaseModal"
import { ReactNode } from "react";

type BaseModalType = {
    id: string;
    children: ReactNode
}

export const BaseModal: React.FC<BaseModalType> = ({ id, children, ...rest }) =>{
    const modalForm = useBaseModal(id) 
    return (
        <ModalForm 
            open={modalForm.visible}
            onOpenChange={(visible) => {
                if (!visible) {
                    modalForm.hide(!false)
                }
            }}
            onFinish={async () => {
                modalForm.hide(!false)
            }}
            {...rest}
        >
            {children}
        </ModalForm>
    )
}

