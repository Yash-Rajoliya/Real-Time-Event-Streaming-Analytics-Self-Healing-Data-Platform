// scripts/load-generator/producer.js
const { Kafka } = require("kafkajs");
const config = require("./config.json");

const kafka = new Kafka({
  clientId: "load-generator-producer",
  brokers: config.kafkaBrokers,
});

const producer = kafka.producer();

function generateTelemetryPayload() {
  const metric = config.metrics[Math.floor(Math.random() * config.metrics.length)];
  const service = config.services[Math.floor(Math.random() * config.services.length)];
  return {
    metricId: metric,
    service: service,
    value: parseFloat((Math.random() * 100).toFixed(2)),
    timestamp: Date.now(),
  };
}

async function startLoadGeneration() {
  await producer.connect();
  console.log(`[LOAD GENERATOR] Connected to Kafka. Target rate: ${config.ratePerSecond} msg/sec`);

  const intervalMs = 1000 / config.ratePerSecond;

  setInterval(async () => {
    const payload = generateTelemetryPayload();
    try {
      await producer.send({
        topic: config.topic,
        messages: [{ value: JSON.stringify(payload) }],
      });
    } catch (err) {
      console.error("[LOAD GENERATOR] Error producing payload:", err.message);
    }
  }, intervalMs);
}

startLoadGeneration().catch(console.error);