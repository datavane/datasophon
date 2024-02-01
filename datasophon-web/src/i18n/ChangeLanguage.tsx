import { Button } from "antd"
import { useState } from "react"
import { useTranslation } from 'react-i18next'

const ChangeLanguage = () => {
    const { i18n } = useTranslation()
    const [language, setLanguage ] = useState('zh')
    const handleOnChangeLanguageClick = () => {
        const currentLanguage = language === 'zh' ? 'en' : 'zh'
        setLanguage(currentLanguage)
        i18n.changeLanguage(currentLanguage)
    }
    return(
        <Button
            type='primary'
            shape="circle"
            style={{ position: 'fixed', right: '50px', top: '20px', zIndex: '1'}}
            onClick={handleOnChangeLanguageClick}
        >
            { language === 'zh' ? 'EN': '中'}
        </Button>
    )
}

export default ChangeLanguage