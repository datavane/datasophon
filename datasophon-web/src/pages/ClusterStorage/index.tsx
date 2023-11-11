import { PageContainer, ProFormText } from '@ant-design/pro-components'
import CreateBaseModal from '../../hooks/createBaseModal'
import { BaseModal } from '../../hooks/baseModal'
import { Button } from 'antd'
import { useBaseModal } from '../../hooks/useBaseModal'

const ClusterStorage = () => {

    const ClusterModal = CreateBaseModal('cluster-modal', ({ data }) => {
        return (
            <BaseModal id='cluster-modal' data={data}>
                <ProFormText name="username" label="用户名"></ProFormText>
            </BaseModal>
        )
    })
    const modal = useBaseModal('cluster-modal')
    return (<PageContainer header={{ title: '存储库管理'}}>
        <Button onClick={() => {
            modal.show({}).then((res) => {
                // 根据回调回来的参数进行数据保存
                console.log('dddd', res)
            })
        }}>open</Button>
        <ClusterModal />
    </PageContainer>)
}

export default ClusterStorage