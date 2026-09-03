// src/config/env.ts
export const env = {
  API_BASE_URL: import.meta.env.VITE_API_GATEWAY_URL || "http://localhost:8080",
  WEBSOCKET_URL: import.meta.env.VITE_WEBSOCKET_URL || "http://localhost:8080",
  IS_DEV: import.meta.env.DEV,
  IS_PROD: import.meta.env.PROD,
} as const;