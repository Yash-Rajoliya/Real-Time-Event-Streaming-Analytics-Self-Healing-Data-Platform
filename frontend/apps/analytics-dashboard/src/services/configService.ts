import { apiClient } from "./apiClient";

export const getConfig = async () => {
  return apiClient.get("/config");
};