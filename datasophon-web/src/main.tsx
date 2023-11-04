import 'antd/dist/reset.css'
import * as React from 'react'
import * as ReactDOM from 'react-dom/client'
import { RouterProvider } from 'react-router-dom'
import router from './router'
import './i18n/index'

ReactDOM.createRoot(document.getElementById('root')!).render(
  <React.StrictMode>
      <React.Suspense fallback="loading">
        <RouterProvider router={router} />
      </React.Suspense>
  </React.StrictMode>,
)