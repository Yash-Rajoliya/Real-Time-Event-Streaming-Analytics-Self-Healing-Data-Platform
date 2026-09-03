import api from "@/services/apiClient";

export const saveDashboard = (layout) =>
  api.post("/dashboard/save", layout);

export const loadDashboard = () =>
  api.get("/dashboard");