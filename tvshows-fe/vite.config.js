import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'
import tailwindcss from "@tailwindcss/vite";

export default defineConfig({
    plugins: [react(), tailwindcss()],
    server: {
        proxy: {
            // <-- update this block
            '/api': {
                target: 'http://backend:5665',
                changeOrigin: true,
            },
        },
        port: 3000,
        strictPort: true,
    },
})
