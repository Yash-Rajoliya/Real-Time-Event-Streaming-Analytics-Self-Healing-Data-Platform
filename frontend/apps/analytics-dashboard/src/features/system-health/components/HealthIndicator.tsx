export default function HealthIndicator({ status }: any) {
  return (
    <span className={status === "UP" ? "text-green-400" : "text-red-400"}>
      {status}
    </span>
  );
}