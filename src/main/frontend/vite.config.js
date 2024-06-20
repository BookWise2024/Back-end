import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [react()],
  server: {
    proxy: {
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/api/, '')
      // },
      // '/oauth2' : {
      //   target: 'http://localhost:8080',
      //   changeOrigin: true,
      // },
      // '/login' : {
      //   target: 'http://localhost:8080',
      //   changeOrigin: true,
      // },
      // '/logout' : {
      //   target: 'http://localhost:8080',
      //   changeOrigin: true,
      }
    },
  }
})
