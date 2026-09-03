import { useAlerts } from "../hooks/useAlerts";
import AlertCard from "./AlertCard";

export default function AlertList() {
  const { data = [] } = useAlerts();

  return (
    <div>
      {data.map((a: any, i: number) => (
        <AlertCard key={i} alert={a} />
      ))}
    </div>
  );
}