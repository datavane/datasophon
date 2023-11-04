import React from 'react'
import './App.css'
import { RouterProvider } from 'react-router-dom'
import router from './router'
import { useTranslation } from 'react-i18next'
import { ConfigProvider } from 'antd';
import en from 'antd/locale/en_US';
import zh from 'antd/locale/zh_CN';
const App = () => {
  const { i18n } = useTranslation()
  const language = i18n.language
  return (
    <React.StrictMode>
    {/* TODO: 多语言异步加载是需要优化，最终采取的策略待定 */}
    <React.Suspense fallback="loading">
      <ConfigProvider locale={language === 'zh' ? zh : en}>
        <RouterProvider router={router} />
      </ConfigProvider>
    </React.Suspense>
  </React.StrictMode>
  )
}

export default App
