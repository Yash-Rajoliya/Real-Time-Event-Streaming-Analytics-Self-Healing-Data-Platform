// scripts/replay-tool/replay.js
const fs = require("fs");
const readline = require("readline");
const { Kafka } = require("kafkajs");

const KAFKA_BROKERS = process.env.KAFKA_BROKERS ? process.env.KAFKA_BROKERS.split(",") : ["localhost:9092"];
const TARGET_TOPIC = process.env.TARGET_TOPIC || "telemetry.replay";
const LOG_FILE = process.argv[2] || "./sample-replay.log";

const kafka = new Kafka({ clientId: "log-replay-tool", brokers: KAFKA_BROKERS });
const producer = kafka.producer();

async function replayLogs() {
  if (!fs.existsSync(LOG_FILE)) {
    console.error(`[REPLAY TOOL] Specified log file not found: ${LOG_FILE}`);
    process.exit(1);
  }

  await producer.connect();
  console.log(`[REPLAY TOOL] Replaying events from '${LOG_FILE}' to topic '${TARGET_TOPIC}'...`);

  const fileStream = fs.createReadStream(LOG_FILE);
  const rl = readline.createInterface({ input: fileStream, crlfDelay: Infinity });

  let count = 0;
  for await (const line of rl) {
    if (line.trim()) {
      await producer.send({
        topic: TARGET_TOPIC,
        messages: [{ value: line }],
      });
      count++;
    }
  }

  console.log(`[REPLAY TOOL] Replay complete. Replayed ${count} messages.`);
  await producer.disconnect();
}

replayLogs().catch(console.error);