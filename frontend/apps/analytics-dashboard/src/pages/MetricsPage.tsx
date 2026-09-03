// src/pages/MetricsPage.tsx
import React from "react";
import { Activity, Cpu, HardDrive, Database } from "lucide-react";

export const MetricsPage: React.FC = () => {
  const metricCards = [
    { title: "Kafka Consumer Lag", value: "1,240 msgs", icon: Activity, status: "Normal" },
    { title: "Flink CPU Utilization", value: "42.8%", icon: Cpu, status: "Healthy" },
    { title: "Disk I/O Write", value: "128 MB/s", icon: HardDrive, status: "Optimal" },
    { title: "Database Pool", value: "18 / 50 Active", icon: Database, status: "Normal" },
  ];

  return (
    <div className="p-6 space-y-6">
      <div>
        <h1 className="text-2xl font-bold text-slate-100">System Metrics</h1>
        <p className="text-sm text-slate-400">Detailed metric telemetry and system performance</p>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4">
        {metricCards.map((card) => {
          const Icon = card.icon;
          return (
            <div
              key={card.title}
              className="p-5 bg-slate-900/80 border border-slate-800 rounded-xl space-y-3"
            >
              <div className="flex items-center justify-between text-slate-400">
                <span className="text-xs font-semibold uppercase tracking-wider">{card.title}</span>
                <Icon className="w-5 h-5 text-indigo-400" />
              </div>
              <div className="text-2xl font-mono font-bold text-slate-100">{card.value}</div>
              <div className="text-xs text-emerald-400 font-medium">{card.status}</div>
            </div>
          );
        })}
      </div>
    </div>
  );
};