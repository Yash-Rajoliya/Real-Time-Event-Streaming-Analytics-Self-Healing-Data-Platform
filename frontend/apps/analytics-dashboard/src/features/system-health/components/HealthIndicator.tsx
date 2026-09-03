// apps/analytics-dashboard/src/features/system-health/components/HealthIndicator.tsx
import React from "react";
import { HealthStatus } from "../healthService";

interface HealthIndicatorProps {
  status: HealthStatus;
  size?: "sm" | "md" | "lg";
  showLabel?: boolean;
}

export const HealthIndicator: React.FC<HealthIndicatorProps> = ({
  status,
  size = "md",
  showLabel = true,
}) => {
  const getStatusColor = (status: HealthStatus) => {
    switch (status) {
      case "UP":
        return "bg-emerald-500 shadow-emerald-500/50";
      case "DEGRADED":
        return "bg-amber-500 shadow-amber-500/50";
      case "DOWN":
        return "bg-rose-500 shadow-rose-500/50";
      default:
        return "bg-slate-500 shadow-slate-500/50";
    }
  };

  const getSizeClasses = (size: string) => {
    switch (size) {
      case "sm":
        return "w-2 h-2";
      case "lg":
        return "w-4 h-4";
      default:
        return "w-3 h-3";
    }
  };

  return (
    <div className="inline-flex items-center gap-2">
      <span className="relative flex">
        {status === "UP" && (
          <span
            className={`animate-ping absolute inline-flex h-full w-full rounded-full opacity-75 ${getStatusColor(
              status
            )}`}
          />
        )}
        <span
          className={`relative inline-flex rounded-full ${getSizeClasses(
            size
          )} ${getStatusColor(status)}`}
        />
      </span>
      {showLabel && (
        <span className="text-xs font-mono font-medium tracking-wider uppercase text-slate-300">
          {status}
        </span>
      )}
    </div>
  );
};