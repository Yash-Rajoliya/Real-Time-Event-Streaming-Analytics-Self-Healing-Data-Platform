import { apiClient } from "./apiClient";

export const login = async (data: any) => {
  return apiClient.post("/auth/login", data);
};