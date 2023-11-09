import { ModalForm, ModalFormProps, ProFormSelect } from "@ant-design/pro-components";
import { useTranslation } from "react-i18next";

interface ModalType extends ModalFormProps {
    data?: any;
}
const ClusterAuthModal = (props: ModalType) => {
    const { t } = useTranslation()
    return (
        <ModalForm
            { ...props}
        >
            <ProFormSelect
                name="userIds"
                label={t('cluster.clusterAdministrator')}
                options={props.data?.adminOptions || []}
                mode="multiple"
            ></ProFormSelect>
        </ModalForm>
    )
}

export default ClusterAuthModal