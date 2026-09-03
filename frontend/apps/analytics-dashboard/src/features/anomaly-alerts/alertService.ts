import { apiClient } from "../../services/apiClient";

export const fetchAlerts = async () => {
  const res = await apiClient.get("/alerts");
  return res.data;
};