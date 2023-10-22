import { PageContainer } from '@ant-design/pro-components'
import { APIS } from '../services/user'
import { Button } from 'antd'
const Login = () => {
    const login =  async () => {
        const res = await APIS.LoginApi.login({
            username: 'admin',
            password: 'admin123'
        })
        console.log(res)
    }

    const handleOnLoginClick = () => {
        login()
    }
    return (<PageContainer header={{ title: 'login'}}>
        <Button onClick={handleOnLoginClick}>login</Button>
    </PageContainer>)
}

export default Login