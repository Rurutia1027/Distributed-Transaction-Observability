#!/bin/sh
#!/usr/bin/env bash
# Generate sample traces for Jaeger (service: eda-order-service).
# Prereq: docker compose up -d  (or app on :8080 + collector + jaeger)
#
# Usage:
#   chmod +x scripts/build-traces.sh
#   ./scripts/build-traces.sh
#   BASE_URL=http://localhost:8080 ./scripts/build-traces.sh


BASE_URL="${BASE_URL:-http://localhost:8080}"
JAEGER_UI="${JAEGER_UI:-http://localhost:16686}"

if ! command -v jq >/dev/null 2>&1; then
  echo "jq is required (brew install jq)" >&2
  exit 1
fi

hr() { echo ""; echo "── $1 ──"; }

hr "1. Sampling info (GET — inspect head sampling for this request)"
curl -sf "$BASE_URL/api/v1/tracing/sampling-info" | jq .

hr "2. Full order lifecycle — forced trace (root + child spans: create → update → commit)"
CREATE=$(curl -sf -X POST "$BASE_URL/api/v1/orders" \
  -H "Content-Type: application/json" \
  -H "X-Force-Trace: true" \
  -H "X-Project-Id: proj-alpha" \
  -H "X-User-Id: eng-42" \
  -H "baggage: force.trace=true,project.tier=premium" \
  -d '{"name":"trace-demo-order","projectId":"proj-alpha","items":"widget x1"}')
echo "$CREATE" | jq .
ORDER_ID=$(echo "$CREATE" | jq -r .id)

curl -sf -X PUT "$BASE_URL/api/v1/orders/$ORDER_ID" \
  -H "Content-Type: application/json" \
  -H "X-Project-Id: proj-alpha" \
  -H "X-User-Id: eng-42" \
  -d '{"items":"widget x2, cable x1","changeSummary":"Added cable","expectedVersion":1}' \
  | jq .

COMMIT=$(curl -sf -X POST "$BASE_URL/api/v1/orders/$ORDER_ID/commit" \
  -H "X-Project-Id: proj-alpha" \
  -H "X-User-Id: eng-42")
echo "$COMMIT" | jq .
echo "order_id=$ORDER_ID (look for order.create_transaction / update / commit spans)"

hr "3. Commit-only sampling rule (create may drop at ratio=0.2; commit is always sampled)"
EPHEMERAL=$(curl -sf -X POST "$BASE_URL/api/v1/orders" \
  -H "Content-Type: application/json" \
  -d '{"name":"maybe-not-sampled","projectId":"proj-beta","items":"item a"}')
EPHEMERAL_ID=$(echo "$EPHEMERAL" | jq -r .id)
echo "created id=$EPHEMERAL_ID (create trace may be missing in Jaeger)"

curl -sf -X POST "$BASE_URL/api/v1/orders/$EPHEMERAL_ID/commit" | jq .
echo "commit trace should appear (sampler.rule=operation)"

hr "4. ERROR span — simulate-error (always sampled + Collector tail_sampling keeps ERROR)"
curl -s -X POST "$BASE_URL/api/v1/orders/$ORDER_ID/simulate-error" | jq . || true
echo "(expected 500 — check Jaeger for order.simulate_error with ERROR status)"

hr "5. Baggage premium path (no X-Force-Trace; baggage project.tier=premium forces sample)"
curl -sf -X POST "$BASE_URL/api/v1/orders" \
  -H "Content-Type: application/json" \
  -H "X-Project-Id: proj-premium" \
  -H "X-User-Id: pm-1" \
  -H "baggage: project.tier=premium" \
  -d '{"name":"premium-order","projectId":"proj-premium","items":"license x10"}' \
  | jq .

hr "6. Burst creates (ratio sampling — run a few times; only ~20% appear without force-trace)"
for i in 1 2 3 4 5; do
  curl -sf -X POST "$BASE_URL/api/v1/orders" \
    -H "Content-Type: application/json" \
    -d "{\"name\":\"ratio-test-$i\",\"projectId\":\"proj-gamma\",\"items\":\"part $i\"}" \
    | jq -c '{id, status}' || true
done
echo "Tip: compare trace count in Jaeger with EDA_SAMPLING_RATIO (default 0.2)"

hr "Done"
echo "Jaeger UI: $JAEGER_UI"
echo "Service:   eda-order-service"
echo "Search:    Operation = order.commit_transaction | order.simulate_error | POST /api/v1/orders"
