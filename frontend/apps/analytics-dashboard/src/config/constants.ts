// src/config/constants.ts
export const APP_CONFIG = {
  NAME: "Analytics Dashboard",
  VERSION: "1.0.0",
  DEFAULT_REFRESH_INTERVAL_MS: 5000,
  MAX_METRIC_HISTORICAL_POINTS: 100,
} as const;

export const API_ENDPOINTS = {
  HEALTH: "/api/actuator/health",
  METRICS: "/api/v1/metrics",
  ALERTS: "/api/v1/alerts",
  USER_PROFILE: "/api/v1/auth/me",
} as const;

export const SEVERITY_LEVELS = {
  INFO: "info",
  WARNING: "warning",
  CRITICAL: "critical",
} as const;