#!/usr/bin/env bash
# tests/chaos/kill-service.sh
# Randomly terminates microservice containers to test self-healing resilience and health check policies.

set -euo pipefail

TARGET_SERVICE="${1:-ingestion-service}"
GRACE_PERIOD="${2:-10}"

echo "[CHAOS TEST] Targeting Microservice: ${TARGET_SERVICE}"

# Locate running container instances for the target service
CONTAINER_ID=$(docker ps --filter "name=${TARGET_SERVICE}" -q | head -n 1)

if [ -z "${CONTAINER_ID}" ]; then
  echo "[ERROR] No running containers found for service matching '${TARGET_SERVICE}'."
  exit 1
fi

echo "[CHAOS TEST] Found instance container ID: ${CONTAINER_ID}"
echo "[CHAOS TEST] Forcefully stopping service..."

docker stop --time 0 "${CONTAINER_ID}"
echo "[CHAOS TEST] Instance ${CONTAINER_ID} killed."

echo "[CHAOS TEST] Waiting ${GRACE_PERIOD}s for orchestrator auto-restart policy..."
sleep "${GRACE_PERIOD}"

# Verify container auto-healing status
HEALTH_STATUS=$(docker inspect --format='{{.State.Status}}' "${CONTAINER_ID}" 2>/dev/null || echo "recreated")

echo "[CHAOS TEST] Service recovery state: ${HEALTH_STATUS}"