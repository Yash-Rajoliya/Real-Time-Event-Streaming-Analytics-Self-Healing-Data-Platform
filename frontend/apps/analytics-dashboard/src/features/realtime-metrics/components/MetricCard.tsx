export default function MetricCard({ title, children }: any) {
  return (
    <div className="bg-slate-800 rounded-xl p-4 shadow-lg">
      <h3 className="text-sm text-gray-400 mb-2">{title}</h3>
      {children}
    </div>
  );
}