// src/app/store/metricStore.ts
export interface MetricDataPoint {
  timestamp: string;
  value: number;
  metricName: string;
}

export interface MetricState {
  metrics: Record<string, MetricDataPoint[]>;
  pushMetric: (metricName: string, point: MetricDataPoint) => void;
  clearMetrics: (metricName?: string) => void;
}

export const createMetricSlice = (set: any): MetricState => ({
  metrics: {},
  pushMetric: (metricName, point) =>
    set((state: any) => {
      const existing = state.metrics.metrics[metricName] || [];
      const updated = [...existing.slice(-99), point]; // Keep last 100 data points
      return {
        metrics: {
          ...state.metrics,
          metrics: { ...state.metrics.metrics, [metricName]: updated },
        },
      };
    }),
  clearMetrics: (metricName) =>
    set((state: any) => {
      if (!metricName) {
        return { metrics: { ...state.metrics, metrics: {} } };
      }
      const newMetrics = { ...state.metrics.metrics };
      delete newMetrics[metricName];
      return { metrics: { ...state.metrics, metrics: newMetrics } };
    }),
});