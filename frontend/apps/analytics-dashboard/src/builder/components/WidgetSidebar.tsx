// src/builder/components/WidgetSidebar.tsx
import React from "react";
import { BarChart3, Activity, Table, X } from "lucide-react";

interface WidgetSidebarProps {
  isOpen: boolean;
  onClose: () => void;
  onAdd: (type: "metric" | "chart" | "table", title: string) => void;
}

export const WidgetSidebar: React.FC<WidgetSidebarProps> = ({
  isOpen,
  onClose,
  onAdd,
}) => {
  if (!isOpen) return null;

  const presets = [
    { type: "metric" as const, title: "Custom Metric", icon: Activity, desc: "Single value live card" },
    { type: "chart" as const, title: "Timeseries Chart", icon: BarChart3, desc: "Real-time stream plot" },
    { type: "table" as const, title: "Data Stream Table", icon: Table, desc: "Tabular log output" },
  ];

  return (
    <div className="fixed inset-y-0 right-0 w-80 bg-slate-900 border-l border-slate-800 p-5 shadow-2xl z-50 flex flex-col justify-between animate-in slide-in-from-right duration-200">
      <div>
        <div className="flex items-center justify-between pb-4 mb-4 border-b border-slate-800">
          <h3 className="text-sm font-semibold text-slate-100">Add Component</h3>
          <button onClick={onClose} className="p-1 text-slate-400 hover:text-slate-200 rounded-lg">
            <X className="w-4 h-4" />
          </button>
        </div>

        <div className="space-y-3">
          {presets.map((preset) => {
            const Icon = preset.icon;
            return (
              <button
                key={preset.type}
                onClick={() => {
                  onAdd(preset.type, preset.title);
                  onClose();
                }}
                className="w-full text-left p-3 bg-slate-950/60 hover:bg-slate-800/60 rounded-xl border border-slate-800 hover:border-indigo-500/50 transition-all group flex items-start gap-3"
              >
                <div className="p-2 rounded-lg bg-indigo-500/10 text-indigo-400 group-hover:bg-indigo-500/20">
                  <Icon className="w-4 h-4" />
                </div>
                <div>
                  <h4 className="text-xs font-semibold text-slate-200">{preset.title}</h4>
                  <p className="text-[11px] text-slate-400">{preset.desc}</p>
                </div>
              </button>
            );
          })}
        </div>
      </div>
    </div>
  );
};