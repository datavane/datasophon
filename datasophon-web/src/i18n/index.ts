import i18next from 'i18next';
import { initReactI18next } from 'react-i18next';
import LanguageDetector  from 'i18next-browser-languagedetector' 

i18next
    .use(initReactI18next)
    .use(LanguageDetector)
    .init({
        fallbackLng: 'zh',
        resources: {
            zh: {
                translation: {
                    login: {
                        tab: '账号密码登录'
                    }
                }
            }
        }
    })