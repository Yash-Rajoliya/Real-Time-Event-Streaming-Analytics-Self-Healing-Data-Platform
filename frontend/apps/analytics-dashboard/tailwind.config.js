import { defineConfig } from "vite";
import react from "@vitejs/plugin-react";
import path from "path";

export default defineConfig({
  plugins: [react()],
  server: {
    port: 5173,
    strictPort: true
  },
  resolve: {
    alias: {
      "@": path.resolve(__dirname, "./src"),
      "@ui": path.resolve(__dirname, "../../packages/ui"),
      "@hooks": path.resolve(__dirname, "../../packages/hooks"),
      "@services": path.resolve(__dirname, "../../packages/services"),
      "@utils": path.resolve(__dirname, "../../packages/utils"),
      "@config": path.resolve(__dirname, "../../packages/config")
    }
  },
  build: {
    outDir: "dist",
    sourcemap: false,
    chunkSizeWarningLimit: 1000
  }
});