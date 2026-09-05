// tests/integration/kafka-test.js
const { Kafka } = require("kafkajs");
const assert = require("assert");

const KAFKA_BROKERS = process.env.KAFKA_BROKERS ? process.env.KAFKA_BROKERS.split(",") : ["localhost:9092"];
const TEST_TOPIC = "telemetry.integration.test";

describe("Kafka Integration Tests", function () {
  this.timeout(15000);

  let kafka;
  let producer;
  let consumer;

  before(async () => {
    kafka = new Kafka({
      clientId: "test-kafka-integration",
      brokers: KAFKA_BROKERS,
      retry: { retries: 5 },
    });

    producer = kafka.producer();
    consumer = kafka.consumer({ groupId: "integration-test-group" });

    await producer.connect();
    await consumer.connect();
    await consumer.subscribe({ topic: TEST_TOPIC, fromBeginning: true });
  });

  after(async () => {
    if (producer) await producer.disconnect();
    if (consumer) await consumer.disconnect();
  });

  it("should produce and consume a telemetry metric payload successfully", async () => {
    const testPayload = {
      metricId: "test-cpu-usage",
      value: 88.5,
      timestamp: Date.now(),
      service: "analytics-worker",
    };

    let receivedPayload = null;

    const consumePromise = new Promise((resolve) => {
      consumer.run({
        eachMessage: async ({ message }) => {
          receivedPayload = JSON.parse(message.value.toString());
          resolve();
        },
      });
    });

    await producer.send({
      topic: TEST_TOPIC,
      messages: [{ value: JSON.stringify(testPayload) }],
    });

    await consumePromise;

    assert.notStrictEqual(receivedPayload, null);
    assert.strictEqual(receivedPayload.metricId, testPayload.metricId);
    assert.strictEqual(receivedPayload.value, testPayload.value);
  });
});