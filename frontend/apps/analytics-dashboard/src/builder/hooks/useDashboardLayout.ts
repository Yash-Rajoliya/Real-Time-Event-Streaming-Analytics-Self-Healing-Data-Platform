import { useState } from "react";
import { DashboardLayout, WidgetConfig } from "../types/dashboard.types";

export const useDashboardLayout = () => {
  const [layout, setLayout] = useState<DashboardLayout>({
    id: "default",
    name: "My Dashboard",
    widgets: [],
  });

  const addWidget = (widget: WidgetConfig) => {
    setLayout(prev => ({
      ...prev,
      widgets: [...prev.widgets, widget],
    }));
  };

  const updateWidget = (id: string, updated: Partial<WidgetConfig>) => {
    setLayout(prev => ({
      ...prev,
      widgets: prev.widgets.map(w =>
        w.id === id ? { ...w, ...updated } : w
      ),
    }));
  };

  const removeWidget = (id: string) => {
    setLayout(prev => ({
      ...prev,
      widgets: prev.widgets.filter(w => w.id !== id),
    }));
  };

  return {
    layout,
    addWidget,
    updateWidget,
    removeWidget,
  };
};