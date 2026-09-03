import React from "react";
import ReactDOM from "react-dom/client";
import AppRoutes from "./routes/AppRoutes";
import { ThemeProvider } from "./app/providers/ThemeProvider";
import { SocketProvider } from "./app/providers/SocketProvider";
import { QueryProvider } from "./app/providers/QueryProvider";
import "./styles/theme.css";

ReactDOM.createRoot(document.getElementById("root")!).render(
  <React.StrictMode>
    <ThemeProvider>
      <QueryProvider>
        <SocketProvider>
          <AppRoutes />
        </SocketProvider>
      </QueryProvider>
    </ThemeProvider>
  </React.StrictMode>
);