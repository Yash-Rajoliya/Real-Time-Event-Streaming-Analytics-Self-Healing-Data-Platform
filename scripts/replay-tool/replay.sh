#!/usr/bin/env bash
# scripts/replay-tool/replay.sh
# Shell wrapper script to execute log event replays onto Kafka channels.

set -euo pipefail

LOG_FILE="${1:-./data/events.log}"
TOPIC="${2:-telemetry.replay}"

echo "[REPLAY WRAPPER] Initializing replay stream..."
echo "  Source File: ${LOG_FILE}"
echo "  Target Topic: ${TOPIC}"

TARGET_TOPIC="${TOPIC}" node "$(dirname "$0")/replay.js" "${LOG_FILE}"