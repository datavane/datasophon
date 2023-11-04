import { LoginFormPage, ProConfigProvider, ProFormText } from '@ant-design/pro-components'
import { APIS } from '../services/user'
import { Tabs ,theme, App, Button } from 'antd'
import { LockOutlined, UserOutlined } from '@ant-design/icons'
import { useNavigate } from 'react-router-dom'
import { useCookieState } from 'ahooks'
import { useTranslation } from 'react-i18next'
import ChangeLanguage from '../i18n/ChangeLanguage'
const LoginForm = () => {
    const { message } = App.useApp();
    const navigate = useNavigate();
    const { t } = useTranslation()
    const [, setSessionId] = useCookieState('sessionId')
    const handleOnLoginClick = async (formData: any) => {
        const { code, msg, data } = await APIS.LoginApi.login(formData)
        if (code === 200) {
            // 登录成功跳转到主页
            navigate('/cluster')
            // cookie 存储用户信息, expires 逻辑复用原有逻辑，不做变更，24 小时过期
            setSessionId(data.sessionId, { expires: (() => new Date(+new Date() + 1000 * 60 * 60 * 24))(), })
        } else {
            message.error(msg)
        }
    }
    const { token } = theme.useToken();
    return ( 
        <ProConfigProvider dark={true}>
            <div
                style={{
                backgroundColor: 'white',
                height: '100vh',
                }}
            >
                <ChangeLanguage></ChangeLanguage>
                <LoginFormPage
                    backgroundImageUrl=""
                    logo=""
                    backgroundVideoUrl="https://gw.alipayobjects.com/v/huamei_gcee1x/afts/video/jXRBRK_VAwoAAAAAAAAAAAAAK4eUAQBr"
                    title="DataSophon"
                    containerStyle={{
                        backgroundColor: 'rgba(0, 0, 0,0.65)',
                        backdropFilter: 'blur(4px)',
                    }}
                    
                    onFinish={handleOnLoginClick}
                >
                    <Tabs
                        centered
                        activeKey="account"
                    >
                        <Tabs.TabPane key={'account'} tab={t('login.tab')} />
                    </Tabs>
                    <ProFormText
                        name="username"
                        fieldProps={{
                            size: 'large',
                            prefix: (
                            <UserOutlined
                                style={{
                                color: token.colorText,
                                }}
                                className={'prefixIcon'}
                            />
                            ),
                        }}
                        placeholder={'请输入用户名'}
                        rules={[
                            {
                            required: true,
                            message: '请输入用户名!',
                            },
                        ]}
                    />
                    <ProFormText.Password
                        name="password"
                        fieldProps={{
                            size: 'large',
                            prefix: (
                            <LockOutlined
                                style={{
                                color: token.colorText,
                                }}
                                className={'prefixIcon'}
                            />
                            ),
                        }}
                        placeholder={'请输入密码'}
                        rules={[
                            {
                            required: true,
                            message: '请输入密码！',
                            },
                        ]}
                    />
                </LoginFormPage>
            </div>
        </ProConfigProvider>
      )
}

const Login = () => {
    return (
        <App>
            <LoginForm></LoginForm>
        </App>
    )
}

export default Login