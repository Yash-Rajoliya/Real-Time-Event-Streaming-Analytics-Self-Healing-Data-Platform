// apps/analytics-dashboard/src/features/system-health/healthService.ts

export type HealthStatus = "UP" | "DOWN" | "DEGRADED" | "UNKNOWN";

export interface ComponentHealth {
  status: HealthStatus;
  details?: Record<string, unknown>;
}

export interface ServiceHealthResponse {
  status: HealthStatus;
  components?: {
    db?: ComponentHealth;
    kafka?: ComponentHealth;
    diskSpace?: ComponentHealth;
    ping?: ComponentHealth;
  };
  timestamp: string;
}

export interface SystemServiceStatus {
  id: string;
  name: string;
  url: string;
  status: HealthStatus;
  responseTimeMs: number;
  lastChecked: string;
}

const SERVICES_TO_MONITOR = [
  { id: "gateway", name: "API Gateway", url: "/api/actuator/health" },
  { id: "ingestion", name: "Ingestion Service", url: "/api/ingestion/actuator/health" },
  { id: "analytics", name: "Analytics Service", url: "/api/analytics/actuator/health" },
  { id: "query", name: "Query Service", url: "/api/query/actuator/health" },
  { id: "storage", name: "Storage Connector", url: "/api/storage/actuator/health" },
  { id: "governance", name: "Data Governance", url: "/api/governance/actuator/health" },
];

export const fetchServiceHealth = async (endpoint: string): Promise<ServiceHealthResponse> => {
  const response = await fetch(endpoint, {
    headers: {
      Accept: "application/json",
    },
  });

  if (!response.ok) {
    throw new Error(`Failed to fetch health status from ${endpoint}: ${response.statusText}`);
  }

  return response.json();
};

export const fetchAllSystemServices = async (): Promise<SystemServiceStatus[]> => {
  const results = await Promise.allSettled(
    SERVICES_TO_MONITOR.map(async (service) => {
      const startTime = performance.now();
      try {
        const data = await fetchServiceHealth(service.url);
        const endTime = performance.now();
        return {
          id: service.id,
          name: service.name,
          url: service.url,
          status: data.status || "UP",
          responseTimeMs: Math.round(endTime - startTime),
          lastChecked: new Date().toISOString(),
        };
      } catch {
        const endTime = performance.now();
        return {
          id: service.id,
          name: service.name,
          url: service.url,
          status: "DOWN" as HealthStatus,
          responseTimeMs: Math.round(endTime - startTime),
          lastChecked: new Date().toISOString(),
        };
      }
    })
  );

  return results.map((result, index) => {
    if (result.status === "fulfilled") {
      return result.value;
    }
    const fallback = SERVICES_TO_MONITOR[index];
    return {
      id: fallback.id,
      name: fallback.name,
      url: fallback.url,
      status: "DOWN",
      responseTimeMs: 0,
      lastChecked: new Date().toISOString(),
    };
  });
};
