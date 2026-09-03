import KafkaLagWidget from "../widgets/KafkaLagWidget";
import ErrorRateWidget from "../widgets/ErrorRateWidget";
import ActiveUsersWidget from "../widgets/ActiveUsersWidget";
import SystemLoadWidget from "../widgets/SystemLoadWidget";

export default function DashboardPage() {
  return (
    <div className="p-6 grid grid-cols-4 gap-4">
      <KafkaLagWidget />
      <ErrorRateWidget />
      <ActiveUsersWidget />
      <SystemLoadWidget />
    </div>
  );
}