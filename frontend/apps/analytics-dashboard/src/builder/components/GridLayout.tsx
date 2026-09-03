// src/builder/components/GridLayout.tsx
import React from "react";
import { DashboardWidget } from "../../app/store/dashboardStore";
import { WidgetRenderer } from "./WidgetRenderer";

interface GridLayoutProps {
  widgets: DashboardWidget[];
  isEditMode: boolean;
  onRemoveWidget: (id: string) => void;
}

export const GridLayout: React.FC<GridLayoutProps> = ({
  widgets,
  isEditMode,
  onRemoveWidget,
}) => {
  return (
    <div className="grid grid-cols-12 gap-4 p-4 min-h-[500px]">
      {widgets.map((widget) => {
        const colSpan = widget.gridConfig.w || 4;
        return (
          <div
            key={widget.id}
            style={{ gridColumn: `span ${colSpan} / span ${colSpan}` }}
            className="min-h-[160px]"
          >
            <WidgetRenderer
              widget={widget}
              isEditMode={isEditMode}
              onRemove={onRemoveWidget}
            />
          </div>
        );
      })}
    </div>
  );
};