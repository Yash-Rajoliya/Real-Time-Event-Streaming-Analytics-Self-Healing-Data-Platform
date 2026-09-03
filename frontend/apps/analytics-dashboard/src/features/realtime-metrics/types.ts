export interface Metric {
  name: string;
  value: number;
  timestamp: number;
}

export interface MetricMap {
  [key: string]: Metric[];
}