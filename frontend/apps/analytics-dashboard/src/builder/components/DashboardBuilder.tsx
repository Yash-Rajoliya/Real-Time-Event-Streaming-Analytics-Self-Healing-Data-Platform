// src/builder/components/DashboardBuilder.tsx
import React, { useState } from "react";
import { useStore } from "../../app/store/store";
import { WidgetToolbar } from "./WidgetToolbar";
import { GridLayout } from "./GridLayout";
import { WidgetSidebar } from "./WidgetSidebar";

export const DashboardBuilder: React.FC = () => {
  const [isEditMode, setIsEditMode] = useState(true);
  const [isSidebarOpen, setIsSidebarOpen] = useState(false);

  const widgets = useStore((state) => state.dashboard.widgets);
  const addWidget = useStore((state) => state.dashboard.addWidget);
  const removeWidget = useStore((state) => state.dashboard.removeWidget);

  const handleAddWidget = (type: "metric" | "chart" | "table", title: string) => {
    addWidget({
      id: `w-${Date.now()}`,
      type,
      title,
      gridConfig: { x: 0, y: 0, w: type === "chart" ? 8 : 4, h: 2 },
    });
  };

  return (
    <div className="relative min-h-screen bg-slate-950 text-slate-100">
      <WidgetToolbar
        onAddWidget={() => setIsSidebarOpen(true)}
        onSaveLayout={() => setIsEditMode(false)}
        onResetLayout={() => {}}
        isEditMode={isEditMode}
        onToggleEditMode={() => setIsEditMode(!isEditMode)}
      />

      <GridLayout
        widgets={widgets}
        isEditMode={isEditMode}
        onRemoveWidget={removeWidget}
      />

      <WidgetSidebar
        isOpen={isSidebarOpen}
        onClose={() => setIsSidebarOpen(false)}
        onAdd={handleAddWidget}
      />
    </div>
  );
};