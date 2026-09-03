// src/app/store/store.ts
import { create } from "zustand";
import { createDashboardSlice, DashboardState } from "./dashboardStore";
import { createMetricSlice, MetricState } from "./metricStore";
import { createUISlice, UIState } from "./uiStore";

export interface RootState {
  dashboard: DashboardState;
  metrics: MetricState;
  ui: UIState;
}

export const useStore = create<RootState>((set) => ({
  dashboard: createDashboardSlice(set),
  metrics: createMetricSlice(set),
  ui: createUISlice(set),
}));