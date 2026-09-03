// src/app/store/dashboardStore.ts
export interface DashboardWidget {
  id: string;
  type: "metric" | "chart" | "table";
  title: string;
  gridConfig: { x: number; y: number; w: number; h: number };
}

export interface DashboardState {
  widgets: DashboardWidget[];
  addWidget: (widget: DashboardWidget) => void;
  removeWidget: (id: string) => void;
  updateLayout: (widgets: DashboardWidget[]) => void;
}

export const createDashboardSlice = (set: any): DashboardState => ({
  widgets: [
    { id: "w-1", type: "metric", title: "Ingestion Rate", gridConfig: { x: 0, y: 0, w: 4, h: 2 } },
    { id: "w-2", type: "chart", title: "Throughput (RPS)", gridConfig: { x: 4, y: 0, w: 8, h: 4 } },
  ],
  addWidget: (widget) =>
    set((state: any) => ({
      dashboard: { ...state.dashboard, widgets: [...state.dashboard.widgets, widget] },
    })),
  removeWidget: (id) =>
    set((state: any) => ({
      dashboard: {
        ...state.dashboard,
        widgets: state.dashboard.widgets.filter((w: DashboardWidget) => w.id !== id),
      },
    })),
  updateLayout: (widgets) =>
    set((state: any) => ({
      dashboard: { ...state.dashboard, widgets },
    })),
});