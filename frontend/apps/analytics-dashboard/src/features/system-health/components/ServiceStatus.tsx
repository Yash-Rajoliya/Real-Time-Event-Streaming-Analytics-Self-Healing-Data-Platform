import HealthIndicator from "./HealthIndicator";

export default function ServiceStatus({ service }: any) {
  return (
    <div className="flex justify-between bg-slate-800 p-2 rounded">
      <span>{service.name}</span>
      <HealthIndicator status={service.status} />
    </div>
  );
}