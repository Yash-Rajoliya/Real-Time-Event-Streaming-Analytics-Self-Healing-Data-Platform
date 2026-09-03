// frontend/packages/hooks/useDebounce.ts
import { useState, useEffect } from "react";

/**
 * Custom hook that delays updating a value until after a specified delay time has elapsed.
 * Useful for optimizing search inputs, API query triggers, and fast UI interactions.
 * 
 * @param value The value to debounce
 * @param delay Delay time in milliseconds (default: 300ms)
 * @returns The debounced value
 */
export function useDebounce<T>(value: T, delay: number = 300): T {
  const [debouncedValue, setDebouncedValue] = useState<T>(value);

  useEffect(() => {
    const handler = setTimeout(() => {
      setDebouncedValue(value);
    }, delay);

    return () => {
      clearTimeout(handler);
    };
  }, [value, delay]);

  return debouncedValue;
}

export default useDebounce;