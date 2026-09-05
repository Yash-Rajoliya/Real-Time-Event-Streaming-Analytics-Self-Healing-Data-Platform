#!/usr/bin/env bash
# tests/chaos/kill-kafka.sh
# Simulates a sudden Kafka broker crash or network partition to verify cluster failover and recovery.

set -euo pipefail

CONTAINER_NAME="${1:-kafka}"
RECOVERY_DELAY="${2:-15}"

echo "[CHAOS TEST] Target Kafka Broker: ${CONTAINER_NAME}"
echo "[CHAOS TEST] Simulating broker failure..."

# Check if the target container exists and is running
if ! docker ps --format '{{.Names}}' | grep -q "^${CONTAINER_NAME}$"; then
  echo "[ERROR] Container '${CONTAINER_NAME}' is not currently running."
  exit 1
fi

# Abruptly stop the Kafka broker container
docker kill "${CONTAINER_NAME}"
echo "[CHAOS TEST] Broker ${CONTAINER_NAME} terminated."

echo "[CHAOS TEST] Waiting for ${RECOVERY_DELAY} seconds to test consumer reconnect behavior..."
sleep "${RECOVERY_DELAY}"

echo "[CHAOS TEST] Restoring Kafka broker..."
docker start "${CONTAINER_NAME}"

echo "[CHAOS TEST] Kafka broker ${CONTAINER_NAME} restored successfully."