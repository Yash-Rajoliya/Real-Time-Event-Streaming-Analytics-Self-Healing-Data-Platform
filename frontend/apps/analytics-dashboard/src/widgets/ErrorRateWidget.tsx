import { AreaChart, Area, XAxis, YAxis, Tooltip } from "recharts";
import { useRealtimeMetrics } from "../features/realtime-metrics/hooks/useRealtimeMetrics";

export default function ErrorRateWidget() {
  const metrics = useRealtimeMetrics();
  const data = metrics.errorRate || [];

  return (
    <div className="bg-slate-900 p-4 rounded-xl shadow">
      <h2>Error Rate</h2>
      <AreaChart width={350} height={200} data={data}>
        <XAxis dataKey="timestamp" />
        <YAxis />
        <Tooltip />
        <Area dataKey="value" stroke="#ef4444" fill="#ef4444" />
      </AreaChart>
    </div>
  );
}