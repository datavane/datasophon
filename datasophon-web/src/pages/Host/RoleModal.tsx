import { Badge, Flex, Modal, ModalProps, Space } from "antd"
import { useCallback, useEffect, useState } from "react"
import { APIS } from "../../services/cluster"

interface ModalType extends ModalProps {
    data?: any
}

const RoleModal = (props: ModalType) => {
    const [state, setState] = useState<[]>([])

    const { hostname, clusterId } = props.data

    const getRoleListByHostname = useCallback(async () => {
        const { code, data } = await APIS.ClusterApi.getRoleListByHostname({
            hostname,
            clusterId
        })
        if (code === 200) {
            setState(data)
        }
    },[clusterId, hostname])
    useEffect(()=> {
        getRoleListByHostname()
    }, [getRoleListByHostname])
    return (
        <Modal
        {...props}
        >
            <Space wrap size="middle">
                {state.map((item: any)=> {
                    const status =  item.serviceRoleStateCode === 1 ? 'processing' : item.serviceRoleStateCode === 2 ? 'error': 'warning'
                    return <Badge key={item.id} text={item.serviceRoleName} status={status}></Badge>
                })}
            </Space>
        </Modal>
    )
}

export default RoleModal