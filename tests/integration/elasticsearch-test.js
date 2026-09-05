// tests/integration/elasticsearch-test.js
const { Client } = require("@elastic/elasticsearch");
const assert = require("assert");

const ES_NODE = process.env.ELASTICSEARCH_URL || "http://localhost:9200";
const TEST_INDEX = "telemetry-logs-test";

describe("Elasticsearch Query Integration Tests", function () {
  this.timeout(15000);

  let client;

  before(async () => {
    client = new Client({ node: ES_NODE });

    const exists = await client.indices.exists({ index: TEST_INDEX });
    if (!exists) {
      await client.indices.create({
        index: TEST_INDEX,
        mappings: {
          properties: {
            timestamp: { type: "date" },
            service: { type: "keyword" },
            level: { type: "keyword" },
            message: { type: "text" },
          },
        },
      });
    }
  });

  after(async () => {
    if (client) {
      await client.indices.delete({ index: TEST_INDEX }).catch(() => {});
      await client.close();
    }
  });

  it("should index a log document and retrieve it using term search", async () => {
    const documentId = `log-${Date.now()}`;
    const testDocument = {
      timestamp: new Date().toISOString(),
      service: "ingestion-service",
      level: "ERROR",
      message: "Integration test pipeline database connection dropped",
    };

    await client.index({
      index: TEST_INDEX,
      id: documentId,
      document: testDocument,
      refresh: true,
    });

    const searchResult = await client.search({
      index: TEST_INDEX,
      query: {
        term: {
          service: "ingestion-service",
        },
      },
    });

    const hits = searchResult.hits.hits;
    assert.strictEqual(hits.length > 0, true);
    assert.strictEqual(hits[0]._source.level, "ERROR");
    assert.strictEqual(hits[0]._source.message, testDocument.message);
  });
});