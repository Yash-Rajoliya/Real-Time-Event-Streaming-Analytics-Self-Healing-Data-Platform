import { useRealtimeMetrics } from "../features/realtime-metrics/hooks/useRealtimeMetrics";

export default function ActiveUsersWidget() {
  const metrics = useRealtimeMetrics();
  const latest = metrics.activeUsers?.slice(-1)[0]?.value || 0;

  return (
    <div className="bg-slate-900 p-6 rounded-xl">
      <h2>Active Users</h2>
      <div className="text-3xl">{latest}</div>
    </div>
  );
}