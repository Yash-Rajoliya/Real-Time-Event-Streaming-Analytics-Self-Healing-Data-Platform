// src/pages/NotFoundPage.tsx
import React from "react";
import { Link } from "react-router-dom";
import { FileQuestion, Home } from "lucide-react";

export const NotFoundPage: React.FC = () => {
  return (
    <div className="min-h-screen bg-slate-950 flex flex-col items-center justify-center text-center p-6">
      <div className="p-4 bg-slate-900 rounded-full border border-slate-800 mb-4 text-indigo-400">
        <FileQuestion className="w-12 h-12" />
      </div>
      <h1 className="text-4xl font-bold text-slate-100 mb-2">404 - Page Not Found</h1>
      <p className="text-slate-400 max-w-md mb-6">
        The requested analytics view or stream pipeline route does not exist.
      </p>
      <Link
        to="/"
        className="px-4 py-2 bg-indigo-600 hover:bg-indigo-500 text-white rounded-lg text-sm font-medium flex items-center gap-2 transition-colors"
      >
        <Home className="w-4 h-4" />
        Return to Dashboard
      </Link>
    </div>
  );
};