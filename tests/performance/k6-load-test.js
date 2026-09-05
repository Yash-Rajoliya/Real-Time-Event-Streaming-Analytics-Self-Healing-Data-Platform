// tests/performance/k6-load-test.js
import http from 'k6/http';
import { check, sleep, group } from 'k6';
import { Rate, Trend } from 'k6/metrics';

// Custom performance metrics
const errorRate = new Rate('http_errors');
const alertQueryDuration = new Trend('alert_query_duration');

export const options = {
  stages: [
    { duration: '30s', target: 20 },  // Ramp-up to 20 virtual users
    { duration: '1m', target: 50 },   // Sustained peak load
    { duration: '30s', target: 0 },   // Ramp-down to 0
  ],
  thresholds: {
    http_req_duration: ['p(95)<500'], // 95% of requests must complete within 500ms
    http_errors: ['rate<0.01'],       // Error rate must be under 1%
  },
};

const BASE_URL = __ENV.TARGET_URL || 'http://localhost:8080';

export default function () {
  const params = {
    headers: {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${__ENV.AUTH_TOKEN || 'test-jwt-token'}`,
    },
  };

  group('Metrics API Load Test', function () {
    const res = http.get(`${BASE_URL}/api/v1/metrics`, params);
    const success = check(res, {
      'status is 200': (r) => r.status === 200,
      'response body contains metrics': (r) => r.body && r.body.length > 0,
    });

    errorRate.add(!success);
  });

  group('Alerts Query Load Test', function () {
    const startTime = new Date();
    const res = http.get(`${BASE_URL}/api/v1/alerts?severity=CRITICAL`, params);
    
    alertQueryDuration.add(new Date() - startTime);

    const success = check(res, {
      'status is 200': (r) => r.status === 200,
    });

    errorRate.add(!success);
  });

  sleep(1);
}