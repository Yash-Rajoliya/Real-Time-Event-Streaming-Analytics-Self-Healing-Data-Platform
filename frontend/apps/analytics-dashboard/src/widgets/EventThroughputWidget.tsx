import { LineChart, Line, XAxis, YAxis, Tooltip } from "recharts";

const data = [
  { time: "1", value: 100 },
  { time: "2", value: 200 },
];

export default function EventThroughputWidget() {
  return (
    <div className="bg-slate-800 p-4 rounded-xl shadow">
      <h2 className="text-lg mb-2">Event Throughput</h2>
      <LineChart width={300} height={200} data={data}>
        <XAxis dataKey="time" />
        <YAxis />
        <Tooltip />
        <Line type="monotone" dataKey="value" />
      </LineChart>
    </div>
  );
}