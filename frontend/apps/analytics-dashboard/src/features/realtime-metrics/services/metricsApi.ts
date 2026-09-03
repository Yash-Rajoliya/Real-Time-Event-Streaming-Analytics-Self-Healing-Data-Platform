import { apiClient } from "../../../services/apiClient";

export const fetchMetrics = async () => {
  const res = await apiClient.get("/metrics");
  return res.data;
};