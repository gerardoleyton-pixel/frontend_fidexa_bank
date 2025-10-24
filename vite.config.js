import { defineConfig } from 'vite'

// Dynamic import for ESM-only plugin to avoid require/ESM loader issues in some
// environments (works with Node versions that don't load plugin via require).
export default defineConfig(async () => {
  const reactPlugin = (await import('@vitejs/plugin-react')).default
  return {
    plugins: [reactPlugin()],
    server: {
      port: 5173,
      host: true
    }
  }
})
