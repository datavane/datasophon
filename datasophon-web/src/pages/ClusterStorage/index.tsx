import { PageContainer } from '@ant-design/pro-components'
import CreateBaseModal from '../../hooks/createBaseModal'
import { BaseModal } from '../../hooks/baseModal'
import { Button, Modal } from 'antd'
import { useBaseModal } from '../../hooks/useBaseModal'
import { useState } from 'react'

const ClusterStorage = () => {
    const ClusterModal = CreateBaseModal('cluster-modal', ({ data }) => {
        return (
            <BaseModal id='cluster-modal' data={data}>
                dddd
            </BaseModal>
        )
    })
    const modal = useBaseModal('cluster-modal')
    const [testModal, setTestModal] = useState(false)
    return (<PageContainer header={{ title: '存储库管理'}}>
        {/* 测试 Modal 组件，使用 context 管理, 动态 createModal 组件 */}
        <Button onClick={() => {
            modal.show().then((res) => {
                // 根据回调回来的参数进行数据保存
                console.log('dddd', res)
            })
        }}>open</Button>
        <ClusterModal />
        {/* 测试Modal 组件，使用 useState 管理 */}
            <Button onClick={() => {
                setTestModal(true)
            }}>打开 Test Modal</Button>
            <Modal open={testModal} 
            onCancel={() => {
                setTestModal(false)
            }}
            afterClose={() => {
                console.log('testModal')
            }}
            ></Modal>
    </PageContainer>)
}

export default ClusterStorage