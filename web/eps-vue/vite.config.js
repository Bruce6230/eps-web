const path = require('path')
import vue from '@vitejs/plugin-vue'
import viteSvgIcons from 'vite-plugin-svg-icons';


module.exports = {
  base: '/',
  server: {
    port: 3000,
    open: false,
    cors: true,
    // 允许 WebSocket 跨域
    proxy: {
      '/ws': {
        target: 'ws://localhost:8888',
        changeOrigin: true,
        ws: true,
        secure: false
      }
    }
  },
  plugins: [
    vue(),
    viteSvgIcons({
      iconDirs: [path.resolve(process.cwd(), 'src/icons/svg')],
      symbolId: '[name]',
    })
  ]
}
