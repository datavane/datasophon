import { ModalForm, ModalFormProps, ProFormSelect } from "@ant-design/pro-components";

interface ModalType extends ModalFormProps {
    data?: any;
}
const AssignModal = (props: ModalType) => {
    return (
        <ModalForm
            { ...props}
        >
            <ProFormSelect
                name="nodeLabelId"
                label="标签"
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

export default AssignModal