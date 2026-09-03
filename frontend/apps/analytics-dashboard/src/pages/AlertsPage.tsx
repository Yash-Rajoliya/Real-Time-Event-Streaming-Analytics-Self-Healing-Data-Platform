// src/pages/AlertsPage.tsx
import React from "react";
import { AlertTriangle, ShieldAlert, CheckCircle2 } from "lucide-react";

export const AlertsPage: React.FC = () => {
  const alerts = [
    {
      id: "alt-101",
      severity: "CRITICAL",
      title: "Kafka Consumer Lag Breach",
      source: "self-healing-service",
      time: "2 mins ago",
    },
    {
      id: "alt-102",
      severity: "WARNING",
      title: "High Memory Pressure on Ingestion Node",
      source: "ingestion-service",
      time: "14 mins ago",
    },
  ];

  return (
    <div className="p-6 space-y-6">
      <div>
        <h1 className="text-2xl font-bold text-slate-100">System Alerts</h1>
        <p className="text-sm text-slate-400">Active incidents and automated healing log</p>
      </div>

      <div className="space-y-3">
        {alerts.map((alert) => (
          <div
            key={alert.id}
            className="flex items-center justify-between p-4 bg-slate-900/80 border border-slate-800 rounded-xl"
          >
            <div className="flex items-center gap-3">
              {alert.severity === "CRITICAL" ? (
                <ShieldAlert className="w-5 h-5 text-rose-500" />
              ) : (
                <AlertTriangle className="w-5 h-5 text-amber-500" />
              )}
              <div>
                <h3 className="text-sm font-semibold text-slate-200">{alert.title}</h3>
                <p className="text-xs font-mono text-slate-500">{alert.source}</p>
              </div>
            </div>
            <span className="text-xs text-slate-400 font-mono">{alert.time}</span>
          </div>
        ))}
      </div>
    </div>
  );
};