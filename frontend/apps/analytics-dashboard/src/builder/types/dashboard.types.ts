export type WidgetType =
  | "throughput"
  | "lag"
  | "errors"
  | "users"
  | "systemLoad";

export interface WidgetConfig {
  id: string;
  type: WidgetType;
  title: string;
  w: number;
  h: number;
  x: number;
  y: number;
}

export interface DashboardLayout {
  id: string;
  name: string;
  widgets: WidgetConfig[];
}