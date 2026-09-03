import { BarChart, Bar, XAxis, YAxis } from "recharts";
import { useRealtimeMetrics } from "../features/realtime-metrics/hooks/useRealtimeMetrics";

export default function SystemLoadWidget() {
  const metrics = useRealtimeMetrics();
  const data = metrics.systemLoad || [];

  return (
    <div className="bg-slate-900 p-4 rounded-xl">
      <BarChart width={350} height={200} data={data}>
        <XAxis dataKey="timestamp" />
        <YAxis />
        <Bar dataKey="value" />
      </BarChart>
    </div>
  );
}