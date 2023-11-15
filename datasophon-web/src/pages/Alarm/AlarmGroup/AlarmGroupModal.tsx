import { ModalForm, ModalFormProps, ProFormSelect, ProFormText } from "@ant-design/pro-components";
import { useTranslation } from "react-i18next";

interface ModalType extends ModalFormProps {
    data?: any;
}
const AlarmGroupModal = (props: ModalType) => {
    const { t } = useTranslation()
    return (
        <ModalForm
            { ...props}
        >
            <ProFormText
                name="alertGroupName"
                label="告警组名称"
                required
            ></ProFormText>
            <ProFormSelect
                name="alertGroupCategory"
                label="告警组类别"
                required
                options={props.data?.groupOptions || []}
            ></ProFormSelect>
        </ModalForm>
    )
}

export default AlarmGroupModal