import 'antd/dist/reset.css'
import * as React from 'react'
import * as ReactDOM from 'react-dom/client'
import { RouterProvider } from 'react-router-dom'
import router from './router'
import './i18n/index'

ReactDOM.createRoot(document.getElementById('root')!).render(
  <React.StrictMode>
      {/* TODO: 多语言异步加载是需要优化，最终采取的策略待定 */}
      <React.Suspense fallback="loading">
        <RouterProvider router={router} />
      </React.Suspense>
  </React.StrictMode>,
)