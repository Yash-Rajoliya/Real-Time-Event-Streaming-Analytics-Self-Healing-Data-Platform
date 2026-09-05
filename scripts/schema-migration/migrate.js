// scripts/schema-migration/migrate.js
const { Client } = require("pg");

const DATABASE_URL = process.env.DATABASE_URL || "postgres://postgres:postgres@localhost:5432/analytics";

const MIGRATIONS = [
  {
    id: 1,
    name: "create_metrics_table",
    query: `
      CREATE TABLE IF NOT EXISTS metrics (
        id VARCHAR(64) PRIMARY KEY,
        service_name VARCHAR(128) NOT NULL,
        metric_name VARCHAR(128) NOT NULL,
        metric_value DOUBLE PRECISION NOT NULL,
        recorded_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
      );
    `,
  },
  {
    id: 2,
    name: "add_metrics_service_index",
    query: `
      CREATE INDEX IF NOT EXISTS idx_metrics_service_recorded 
      ON metrics(service_name, recorded_at DESC);
    `,
  },
];

async function runMigrations() {
  const client = new Client({ connectionString: DATABASE_URL });
  await client.connect();

  console.log("[MIGRATION] Connected to PostgreSQL. Verifying schema version table...");

  await client.query(`
    CREATE TABLE IF NOT EXISTS schema_migrations (
      id INT PRIMARY KEY,
      name VARCHAR(255) NOT NULL,
      executed_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
    );
  `);

  const { rows } = await client.query("SELECT id FROM schema_migrations;");
  const executedIds = new Set(rows.map((r) => r.id));

  for (const migration of MIGRATIONS) {
    if (!executedIds.has(migration.id)) {
      console.log(`[MIGRATION] Applying migration ${migration.id}: ${migration.name}...`);
      await client.query("BEGIN;");
      try {
        await client.query(migration.query);
        await client.query(
          "INSERT INTO schema_migrations (id, name) VALUES ($1, $2);",
          [migration.id, migration.name]
        );
        await client.query("COMMIT;");
        console.log(`[MIGRATION] Migration ${migration.id} applied successfully.`);
      } catch (err) {
        await client.query("ROLLBACK;");
        console.error(`[MIGRATION] Migration ${migration.id} failed:`, err.message);
        process.exit(1);
      }
    } else {
      console.log(`[MIGRATION] Migration ${migration.id} (${migration.name}) already applied. Skipping.`);
    }
  }

  await client.end();
  console.log("[MIGRATION] All database schema migrations completed successfully.");
}

runMigrations().catch((err) => {
  console.error("[MIGRATION] Unhandled migration error:", err);
  process.exit(1);
});