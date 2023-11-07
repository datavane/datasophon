import { PageContainer, ProTable, ProColumns } from '@ant-design/pro-components'
import { useEffect, useState } from 'react';
import { useTranslation } from 'react-i18next';
import { APIS } from '../../services/cluster';
import { Button, Popconfirm, message } from 'antd';

type ClusterFrameworkType = {
    id?: number;
    serviceName: string;
    serviceVersion: string;
    serviceDesc: string;
}

type ListToolBarMenuItem = {
    key: React.Key;
    label: React.ReactNode;
    disabled?: boolean;
};

const ClusterFramework = () => {
    const [ activeKey, setActiveKey] = useState<string>('');
    const [ frameMap, setFrameMap] = useState(new Map())
    const [ tabItems, setTabItems] = useState<ListToolBarMenuItem[]>([])
    const [ loading, setLoading] = useState(false)
    const { t } = useTranslation();
    const columns: ProColumns<ClusterFrameworkType>[] = [
        {
            dataIndex: 'index',
            valueType: 'indexBorder',
            width: 48,
        },
        {
            title: t('cluster.framework.serviceName'),
            dataIndex: 'serviceName'
        },
        {
            title: t('cluster.framework.serviceVersion'),
            dataIndex: 'serviceVersion'
        },
        {
            title: t('cluster.framework.serviceDesc'),
            dataIndex: 'serviceDesc'
        },{
            title: t('user.operation'),
            valueType: 'option',
            key: 'option',
            render: (text, record, _, action) => [
              <Popconfirm
                title={t('cluster.framework.deleteConfirm', { serviceName: record.serviceName })}
                key="confirm"
                onConfirm={() => {
                  handleOnConfirmClick(record)
                }}
              >
                  <Button
                    key="delete"
                    type="link"
                  >
                  {t('common.delete')}
                </Button>
              </Popconfirm>
             ,
            ],
          },
    ];

    const initList = async (activeKey) => {
        setLoading(true)
        const { code , data, msg} = await APIS.ClusterApi.frameList()
        if (code === 200) {
            // 数据格式转换为 Map
            // 获取 Tab 数据
            const frameList = new Map()
            data.forEach((element: any) => {
                console.log('element', element)
                frameList.set(element.frameCode, element.frameServiceList)
            });
            setFrameMap(frameList)
            const items: ListToolBarMenuItem[] = []
            Array.from(frameList.keys()).forEach((element: any) => {
                items.push({
                    key: element,
                    label: element
                })
            });
            setTabItems(items)
            setActiveKey(activeKey ? activeKey: items[0].key as string)
            setLoading(false)
        } else {
            message.error(msg)
            setLoading(false)
        }
       
    }

    const handleOnConfirmClick = async (record: ClusterFrameworkType) =>  {
        const { code, msg } = await APIS.ClusterApi.frameDelete(record)
        if (code === 200) {
          message.success(`${t('common.delete')}${t('common.success')}！`)
          initList(activeKey)
        } else {
          message.error(msg)
        }
      }
    useEffect(() => {
        initList()
    }, [])
    return (
        <PageContainer header={{ title: t('cluster.framework.title')}}>
            <ProTable
                columns={columns}
                rowKey="id"
                pagination={false}
                dataSource={frameMap.get(activeKey)}
                search={false}
                loading={loading}
                toolbar={
                    {
                        settings: [],
                        menu: {
                            type: 'tab',
                            activeKey,
                            onChange: (key) => setActiveKey(key as string),
                            items: tabItems
                          },
                    }
                }
            ></ProTable>
        </PageContainer>
    )
}

export default ClusterFramework