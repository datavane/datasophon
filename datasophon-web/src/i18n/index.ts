import i18next from 'i18next';
import { initReactI18next } from 'react-i18next';
import LanguageDetector  from 'i18next-browser-languagedetector' 

i18next
    .use(initReactI18next)
    .use(LanguageDetector)
    .init({
        fallbackLng: 'en',
        resources: {
            zh: {
                translation: {
                    login: {
                        tab: '账号密码登录'
                    }
                }
            },
            en: {
                translation: {
                    login: {
                        tab: 'Login with account and password'
                    }
                }
            }
        }
    })