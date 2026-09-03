import { LineChart, Line, XAxis, YAxis, Tooltip } from "recharts";
import { useRealtimeMetrics } from "../features/realtime-metrics/hooks/useRealtimeMetrics";

export default function KafkaLagWidget() {
  const metrics = useRealtimeMetrics();
  const data = metrics.kafkaLag || [];

  return (
    <div className="bg-slate-900 p-4 rounded-xl shadow">
      <h2 className="mb-2">Kafka Lag</h2>
      <LineChart width={350} height={200} data={data}>
        <XAxis dataKey="timestamp" />
        <YAxis />
        <Tooltip />
        <Line dataKey="value" stroke="#f87171" />
      </LineChart>
    </div>
  );
}