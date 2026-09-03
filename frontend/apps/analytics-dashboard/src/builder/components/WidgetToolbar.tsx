// src/builder/components/WidgetToolbar.tsx
import React from "react";
import { Plus, LayoutGrid, Save, RefreshCw, Eye } from "lucide-react";

interface WidgetToolbarProps {
  onAddWidget: () => void;
  onSaveLayout: () => void;
  onResetLayout: () => void;
  isEditMode: boolean;
  onToggleEditMode: () => void;
}

export const WidgetToolbar: React.FC<WidgetToolbarProps> = ({
  onAddWidget,
  onSaveLayout,
  onResetLayout,
  isEditMode,
  onToggleEditMode,
}) => {
  return (
    <div className="flex items-center justify-between px-4 py-3 bg-slate-900/90 border-b border-slate-800 backdrop-blur-md">
      <div className="flex items-center gap-3">
        <div className="p-2 bg-indigo-500/10 text-indigo-400 rounded-lg border border-indigo-500/20">
          <LayoutGrid className="w-5 h-5" />
        </div>
        <div>
          <h2 className="text-sm font-semibold text-slate-100">Dashboard Builder</h2>
          <p className="text-xs text-slate-400">Customize widgets and stream layouts</p>
        </div>
      </div>

      <div className="flex items-center gap-2">
        <button
          onClick={onToggleEditMode}
          className={`px-3 py-1.5 rounded-lg text-xs font-medium flex items-center gap-1.5 border transition-colors ${
            isEditMode
              ? "bg-amber-500/10 text-amber-400 border-amber-500/30 hover:bg-amber-500/20"
              : "bg-slate-800 text-slate-300 border-slate-700 hover:bg-slate-700"
          }`}
        >
          <Eye className="w-3.5 h-3.5" />
          {isEditMode ? "Editing Mode" : "View Mode"}
        </button>

        {isEditMode && (
          <>
            <button
              onClick={onAddWidget}
              className="px-3 py-1.5 bg-indigo-600 hover:bg-indigo-500 text-white rounded-lg text-xs font-medium flex items-center gap-1.5 transition-colors shadow-sm shadow-indigo-600/30"
            >
              <Plus className="w-3.5 h-3.5" />
              Add Widget
            </button>

            <button
              onClick={onResetLayout}
              className="p-2 text-slate-400 hover:text-slate-200 hover:bg-slate-800 rounded-lg transition-colors border border-transparent hover:border-slate-700"
              title="Reset Layout"
            >
              <RefreshCw className="w-4 h-4" />
            </button>

            <button
              onClick={onSaveLayout}
              className="px-3 py-1.5 bg-emerald-600 hover:bg-emerald-500 text-white rounded-lg text-xs font-medium flex items-center gap-1.5 transition-colors shadow-sm shadow-emerald-600/30"
            >
              <Save className="w-3.5 h-3.5" />
              Save
            </button>
          </>
        )}
      </div>
    </div>
  );
};