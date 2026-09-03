// src/pages/BuilderPage.tsx
import React from "react";
import { DashboardBuilder } from "../builder/components/DashboardBuilder";

export const BuilderPage: React.FC = () => {
  return (
    <div className="h-full w-full">
      <DashboardBuilder />
    </div>
  );
};