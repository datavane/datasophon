import { ModalForm, ModalFormProps, ProFormSelect, ProFormText } from "@ant-design/pro-components";
import { useTranslation } from "react-i18next";

interface ModalType extends ModalFormProps {
    data?: any;
}
const ClusterModal = (props: ModalType) => {
    const { t } = useTranslation()
    return (
        <ModalForm
            { ...props}
        >
            <ProFormText
                name="clusterName"
                label={t('cluster.clusterName')}
            ></ProFormText>
              <ProFormText
                name="clusterCode"
                label={t('cluster.clusterCode')}
            ></ProFormText>
            <ProFormSelect
                name="clusterFrame"
                label={t('cluster.clusterFramework')}
                options={props.data?.frameworkOptions || []}
            ></ProFormSelect>
        </ModalForm>
    )
}

export default ClusterModal