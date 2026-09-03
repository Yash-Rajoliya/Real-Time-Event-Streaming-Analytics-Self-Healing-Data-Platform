import EventThroughputWidget from "@/widgets/EventThroughputWidget";
import KafkaLagWidget from "@/widgets/KafkaLagWidget";
import ErrorRateWidget from "@/widgets/ErrorRateWidget";
import ActiveUsersWidget from "@/widgets/ActiveUsersWidget";
import SystemLoadWidget from "@/widgets/SystemLoadWidget";

export const widgetRegistry = {
  throughput: EventThroughputWidget,
  lag: KafkaLagWidget,
  errors: ErrorRateWidget,
  users: ActiveUsersWidget,
  systemLoad: SystemLoadWidget,
};