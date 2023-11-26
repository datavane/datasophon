import { ModalForm, ModalFormProps, ProFormSelect } from "@ant-design/pro-components";

interface ModalType extends ModalFormProps {
    data?: any;
}
const AssignFrameModal = (props: ModalType) => {
    return (
        <ModalForm
            { ...props}
        >
            <ProFormSelect
                name="rack"
                label="机架"
                rules={[
                    {
                      required: true,
                    },
                  ]}
                options={props.data?.options || []}
            ></ProFormSelect>
        </ModalForm>
    )
}

export default AssignFrameModal