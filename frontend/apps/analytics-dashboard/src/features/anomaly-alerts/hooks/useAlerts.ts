import { useQuery } from "@tanstack/react-query";
import { fetchAlerts } from "../alertService";

export const useAlerts = () => {
  return useQuery({
    queryKey: ["alerts"],
    queryFn: fetchAlerts,
    refetchInterval: 5000,
  });
};