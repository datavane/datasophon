import React from 'react'
import './App.css'
import { RouterProvider } from 'react-router-dom'
import router from './router'
import { useTranslation } from 'react-i18next'
import { ConfigProvider } from 'antd';
import en from 'antd/locale/en_US';
import zh from 'antd/locale/zh_CN';
import { BaseModalContextProvider } from './hooks/baseModalContextProvider'
const App = () => {
  const { i18n } = useTranslation()
  const language = i18n.language
  return (
    <React.StrictMode>
    {/* TODO: 多语言异步加载是需要优化，最终采取的策略待定 */}
    <BaseModalContextProvider>
      <React.Suspense fallback="loading">
        <ConfigProvider locale={language === 'zh' ? zh : en}>
          <RouterProvider router={router} />
        </ConfigProvider>
      </React.Suspense>
    </BaseModalContextProvider>
  </React.StrictMode>
  )
}

export default App
