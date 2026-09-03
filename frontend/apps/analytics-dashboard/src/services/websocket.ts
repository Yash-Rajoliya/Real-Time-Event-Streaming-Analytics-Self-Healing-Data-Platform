export const createSocket = () => {
  return new WebSocket("ws://localhost:8080/ws");
};