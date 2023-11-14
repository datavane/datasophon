import { useBaseModal } from "./useBaseModal"
import { ReactNode } from "react";
import { Modal, ModalProps } from "antd";

interface BaseModalType extends  ModalProps{
    id: string;
    children: ReactNode,
    data?: any
}


export const BaseModal: React.FC<BaseModalType> = ({ id, children, ...rest}) =>{
    const modal = useBaseModal(id)
    return (
        <Modal
            open={!modal.hiding}
            onOk={() => { modal.hide()}}
            onCancel={() => { modal.hide()}}
            afterClose={() => {
                modal.hide(true)
            }}
            {...rest}
        >
            {children}
        </Modal>
    )
}

