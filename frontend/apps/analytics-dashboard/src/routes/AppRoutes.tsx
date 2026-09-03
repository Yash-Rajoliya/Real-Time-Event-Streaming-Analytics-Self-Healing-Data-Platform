// src/routes/AppRoutes.tsx
import React from "react";
import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import { ProtectedRoute } from "./ProtectedRoute";
import { DashboardPage } from "../pages/DashboardPage";
import { MetricsPage } from "../pages/MetricsPage";
import { AlertsPage } from "../pages/AlertsPage";
import { BuilderPage } from "../pages/BuilderPage";
import { NotFoundPage } from "../pages/NotFoundPage";

export const AppRoutes: React.FC = () => {
  return (
    <BrowserRouter>
      <Routes>
        {/* Protected Routes */}
        <Route element={<ProtectedRoute />}>
          <Route path="/" element={<DashboardPage />} />
          <Route path="/metrics" element={<MetricsPage />} />
          <Route path="/alerts" element={<AlertsPage />} />
          <Route path="/builder" element={<BuilderPage />} />
        </Route>

        {/* Fallbacks */}
        <Route path="/404" element={<NotFoundPage />} />
        <Route path="*" element={<Navigate to="/404" replace />} />
      </Routes>
    </BrowserRouter>
  );
};

export default AppRoutes;