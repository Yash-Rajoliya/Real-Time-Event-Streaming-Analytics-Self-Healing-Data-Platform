// src/app/store/uiStore.ts
export interface UIState {
  sidebarOpen: boolean;
  activeModal: string | null;
  toggleSidebar: () => void;
  openModal: (modalId: string) => void;
  closeModal: () => void;
}

export const createUISlice = (set: any): UIState => ({
  sidebarOpen: true,
  activeModal: null,
  toggleSidebar: () =>
    set((state: any) => ({
      ui: { ...state.ui, sidebarOpen: !state.ui.sidebarOpen },
    })),
  openModal: (modalId) =>
    set((state: any) => ({
      ui: { ...state.ui, activeModal: modalId },
    })),
  closeModal: () =>
    set((state: any) => ({
      ui: { ...state.ui, activeModal: null },
    })),
});