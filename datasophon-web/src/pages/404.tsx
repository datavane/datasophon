import { Button, Result } from "antd"
import {  useNavigate } from 'react-router-dom'

const NotFound: React.FC = () => {
    const navigate = useNavigate();
    return (
        <Result 
            status={404}
            title="404"
            subTitle="抱歉，访问的页面不存在！"
            extra={
                <Button type="primary" onClick={() => navigate('/')}>
                    返回首页
                </Button>
              }
            ></Result>
    )
}

export default NotFound