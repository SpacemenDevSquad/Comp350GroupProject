import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

export default defineConfig({
  plugins: [react()],
  server: {
    allowedHosts: true,
    host: '0.0.0.0',
    port: 5173,
    strictPort: true,
    proxy: {
      '/api': {
        target: 'http://backend:8096',
        changeOrigin: true
      },
      '/ping': {
        target: 'http://backend:8096',
        changeOrigin: true
      }
    },
    hmr: process.env.VITE_HMR_HOST
      ? {
          host: process.env.VITE_HMR_HOST,
          clientPort: process.env.VITE_HMR_CLIENT_PORT ? Number(process.env.VITE_HMR_CLIENT_PORT) : undefined,
          protocol: process.env.VITE_HMR_PROTOCOL || undefined
        }
      : undefined
  }
})
