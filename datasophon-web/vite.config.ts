import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react-swc'
import alias from '@rollup/plugin-alias';

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [
    react(),
    alias({
      entries: [
        {
          find: '@ajax', replacement: './src/services/request.ts'
        }
      ]
    })
  ],
  server: {
    proxy: {
      '/ddh': {
        target: 'http://124.221.211.72:9120',
        changeOrigin: true,
      }
    }
  }
})
