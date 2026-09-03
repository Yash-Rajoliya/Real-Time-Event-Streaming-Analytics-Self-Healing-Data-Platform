// src/builder/components/WidgetRenderer.tsx
import React from "react";
import { DashboardWidget } from "../../app/store/dashboardStore";
import { Trash2, GripVertical, Settings } from "lucide-react";

interface WidgetRendererProps {
  widget: DashboardWidget;
  isEditMode?: boolean;
  onRemove?: (id: string) => void;
}

export const WidgetRenderer: React.FC<WidgetRendererProps> = ({
  widget,
  isEditMode = false,
  onRemove,
}) => {
  return (
    <div className="relative h-full w-full bg-slate-900/80 border border-slate-800 rounded-xl p-4 flex flex-col justify-between hover:border-slate-700 transition-all shadow-md group">
      {/* Header Bar */}
      <div className="flex items-center justify-between border-b border-slate-800/80 pb-2 mb-3">
        <div className="flex items-center gap-2">
          {isEditMode && (
            <GripVertical className="w-4 h-4 text-slate-500 cursor-grab active:cursor-grabbing" />
          )}
          <span className="text-xs font-semibold text-slate-300 uppercase tracking-wider">
            {widget.title}
          </span>
        </div>

        {isEditMode && (
          <div className="flex items-center gap-1 opacity-0 group-hover:opacity-100 transition-opacity">
            <button className="p-1 text-slate-400 hover:text-slate-200 rounded">
              <Settings className="w-3.5 h-3.5" />
            </button>
            {onRemove && (
              <button
                onClick={() => onRemove(widget.id)}
                className="p-1 text-rose-400 hover:text-rose-300 rounded"
              >
                <Trash2 className="w-3.5 h-3.5" />
              </button>
            )}
          </div>
        )}
      </div>

      {/* Widget Content Placeholder */}
      <div className="flex-1 flex items-center justify-center border border-dashed border-slate-800 rounded-lg p-4 bg-slate-950/40">
        <span className="text-xs font-mono text-slate-500">
          [{widget.type.toUpperCase()}] Widget View
        </span>
      </div>
    </div>
  );
};