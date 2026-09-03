// apps/analytics-dashboard/src/features/system-health/components/ServiceStatus.tsx
import React from "react";
import { SystemServiceStatus } from "../healthService";
import { HealthIndicator } from "./HealthIndicator";

interface ServiceStatusProps {
  services: SystemServiceStatus[];
  isLoading?: boolean;
}

export const ServiceStatus: React.FC<ServiceStatusProps> = ({ services, isLoading }) => {
  if (isLoading) {
    return (
      <div className="p-4 bg-slate-900/60 rounded-xl border border-slate-800 animate-pulse">
        <div className="h-4 w-32 bg-slate-800 rounded mb-4"></div>
        <div className="space-y-3">
          {[1, 2, 3].map((i) => (
            <div key={i} className="h-10 bg-slate-800/50 rounded-lg"></div>
          ))}
        </div>
      </div>
    );
  }

  return (
    <div className="p-5 bg-slate-900/80 backdrop-blur-sm rounded-xl border border-slate-800 shadow-lg">
      <div className="flex items-center justify-between mb-4">
        <h3 className="text-sm font-semibold tracking-wide text-slate-200">
          System Service Status
        </h3>
        <span className="text-xs font-mono text-slate-400">
          {services.filter((s) => s.status === "UP").length}/{services.length} Healthy
        </span>
      </div>

      <div className="space-y-2">
        {services.map((service) => (
          <div
            key={service.id}
            className="flex items-center justify-between p-3 bg-slate-950/50 hover:bg-slate-800/40 rounded-lg border border-slate-800/60 transition-colors"
          >
            <div className="flex items-center gap-3">
              <HealthIndicator status={service.status} showLabel={false} />
              <div>
                <p className="text-sm font-medium text-slate-100">{service.name}</p>
                <p className="text-xs font-mono text-slate-500">{service.url}</p>
              </div>
            </div>

            <div className="text-right">
              <span className="text-xs font-mono text-slate-400">
                {service.responseTimeMs} ms
              </span>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
};