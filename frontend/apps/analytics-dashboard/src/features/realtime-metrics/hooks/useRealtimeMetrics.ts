import { useEffect } from "react";
import { useMetricStore } from "../../../app/store/metricStore";
import { useWebSocket } from "../../../app/hooks/useWebSocket";

export const useRealtimeMetrics = () => {
  const socket = useWebSocket();
  const { metrics, setMetrics } = useMetricStore();

  useEffect(() => {
    if (!socket) return;

    socket.onmessage = (event) => {
      const data = JSON.parse(event.data);
      setMetrics(data);
    };
  }, [socket]);

  return metrics;
};