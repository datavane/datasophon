import { useBaseModal } from "./useBaseModal"

const CreateBaseModal = (modalId: string, Comp: React.ComponentType<any>) => {
    const { visible, args } = useBaseModal(modalId)
    return (props: any) => {
        if (!visible) return null
        return <Comp {...args} {...props} />
    }
}

export default CreateBaseModal