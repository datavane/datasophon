import { PageContainer } from '@ant-design/pro-components'
import CreateBaseModal from '../../hooks/createBaseModal'
import { BaseModal } from '../../hooks/baseModal'
import { Button } from 'antd'
import { useBaseModal } from '../../hooks/useBaseModal'

const ClusterStorage = () => {

    const ClusterModal = CreateBaseModal('cluster-modal', () => {
        return (
            <BaseModal id='cluster-modal'>
                dd
            </BaseModal>
        )
    })
    const modal = useBaseModal('cluster-modal')
    return (<PageContainer header={{ title: '存储库管理'}}>
        <Button onClick={() => {
            modal.show({})
        }}>open</Button>
        <ClusterModal />
    </PageContainer>)
}

export default ClusterStorage