// frontend/packages/services/websocketClient.ts
type EventCallback = (data: any) => void;

export class WebSocketClient {
  private socket: WebSocket | null = null;
  private listeners: Map<string, Set<EventCallback>> = new Map();
  private url: string;
  private reconnectInterval: number;
  private isExplicitlyClosed: boolean = false;

  constructor(url: string, reconnectInterval: number = 3000) {
    this.url = url;
    this.reconnectInterval = reconnectInterval;
  }

  public connect(): void {
    this.isExplicitlyClosed = false;
    this.socket = new WebSocket(this.url);

    this.socket.onmessage = (event) => {
      try {
        const parsed = JSON.parse(event.data);
        const { channel, payload } = parsed;
        if (channel && this.listeners.has(channel)) {
          this.listeners.get(channel)?.forEach((cb) => cb(payload));
        }
      } catch (e) {
        console.error("Failed to parse WebSocket frame:", e);
      }
    };

    this.socket.onclose = () => {
      if (!this.isExplicitlyClosed) {
        setTimeout(() => this.connect(), this.reconnectInterval);
      }
    };
  }

  public subscribe(channel: string, callback: EventCallback): () => void {
    if (!this.listeners.has(channel)) {
      this.listeners.set(channel, new Set());
    }
    this.listeners.get(channel)!.add(callback);

    return () => {
      this.listeners.get(channel)?.delete(callback);
    };
  }

  public send(channel: string, payload: any): void {
    if (this.socket && this.socket.readyState === WebSocket.OPEN) {
      this.socket.send(JSON.stringify({ channel, payload }));
    }
  }

  public disconnect(): void {
    this.isExplicitlyClosed = true;
    if (this.socket) {
      this.socket.close();
    }
  }
}