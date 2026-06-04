import http from 'k6/http';
import { check, sleep } from 'k6';
import { Trend, Rate, Counter } from 'k6/metrics';

const overviewTrend = new Trend('overview_duration');
const addSatellitesTrend = new Trend('add_satellites_duration');
const activateTrend = new Trend('activate_duration');
const missionTrend = new Trend('mission_duration');
const deleteTrend = new Trend('delete_duration');
const successRate = new Rate('successful_requests');
const scenarioCounter = new Counter('scenario_iterations');

export const options = {
  stages: [
    { duration: '10s', target: 10 },
    { duration: '20s', target: 20 },
    { duration: '20s', target: 50 },
    { duration: '20s', target: 100 },
    { duration: '10s', target: 0 },
  ],
  thresholds: {
    http_req_duration: ['p(95)<10000'],
    http_req_failed: ['rate<0.2'],
  },
  summaryTrendStats: ['avg', 'min', 'med', 'max', 'p(90)', 'p(95)', 'count'],
};

const BASE_URL = __ENV.BASE_URL || 'http://localhost:8080';
const API = `${BASE_URL}/api`;

function randomInt(min, max) {
  return Math.floor(Math.random() * (max - min + 1)) + min;
}

export default function () {
  const vuId = __VU;
  const iter = __ITER;
  const suffix = `${vuId}-${iter}-${Date.now()}`;
  const constellationName = `load-const-${suffix}`;
  const commSatName = `sat-comm-${suffix}`;
  const imgSatName = `sat-img-${suffix}`;

  // 1. GET overview
  let res = http.get(`${API}/overview`);
  overviewTrend.add(res.timings.duration);
  check(res, { 'overview status 200': (r) => r.status === 200 });

  sleep(1);

  // 2. POST add-satellites
  const addPayload = JSON.stringify({
    constellationName: constellationName,
    satelliteParams: [
      {
        type: 'COMMUNICATION',
        name: commSatName,
        batteryLevel: randomInt(50, 100),
        bandwidth: randomInt(10, 1000),
      },
      {
        type: 'IMAGE',
        name: imgSatName,
        batteryLevel: randomInt(50, 100),
        resolution: randomInt(1, 100),
      },
    ],
  });

  res = http.post(`${API}/add-satellites`, addPayload, {
    headers: { 'Content-Type': 'application/json' },
  });
  addSatellitesTrend.add(res.timings.duration);
  check(res, { 'add-satellites status 200': (r) => r.status === 200 });

  sleep(1);

  // 3. POST activate-satellites
  res = http.post(`${API}/activate-satellites/${constellationName}`);
  activateTrend.add(res.timings.duration);
  check(res, { 'activate status 200': (r) => r.status === 200 });

  sleep(1);

  // 4. POST missions
  const missionPayload = JSON.stringify({
    targetType: 'CONSTELLATION',
    constellationName: constellationName,
    satelliteName: '',
  });

  res = http.post(`${API}/missions`, missionPayload, {
    headers: { 'Content-Type': 'application/json' },
  });
  missionTrend.add(res.timings.duration);
  check(res, { 'mission status 200': (r) => r.status === 200 });

  sleep(1);

  // 5. GET overview final
  res = http.get(`${API}/overview`);
  overviewTrend.add(res.timings.duration);
  check(res, { 'overview final status 200': (r) => r.status === 200 });

  sleep(1);

  // 6. DELETE satellite
  res = http.del(`${API}/delete-satellite/${constellationName}/satellites/${commSatName}`);
  deleteTrend.add(res.timings.duration);
  check(res, { 'delete status 204': (r) => r.status === 204 });

  scenarioCounter.add(1);

  sleep(1);
}

function vals(m) {
  return (m && m.values) || {};
}

function v(m, prop, def) {
  const vv = vals(m)[prop];
  return vv != null && isFinite(vv) ? vv : (def != null ? def : '-');
}

function pct(vv) {
  return vv != null && isFinite(vv) ? (vv * 100).toFixed(2) : '0.00';
}

function fixed(vv, d) {
  return vv != null && isFinite(vv) ? vv.toFixed(d || 2) : '-';
}

function metricTableRow(name, metric, unit) {
  const u = unit || '';
  const va = vals(metric);
  return `<tr>
    <td>${name}</td>
    <td>${fixed(va.avg)}${u}</td>
    <td>${fixed(va.min)}${u}</td>
    <td>${fixed(va.med)}${u}</td>
    <td>${fixed(va.max)}${u}</td>
    <td>${fixed(va['p(90)'])}${u}</td>
    <td>${fixed(va['p(95)'])}${u}</td>
    <td>${va.count || '-'}</td>
  </tr>`;
}

export function handleSummary(data) {
  const m = data.metrics;
  const failedV = vals(m.http_req_failed);
  const durationV = vals(m.http_req_duration);
  const httpReqsV = vals(m.http_reqs);

  const totalReqs = v(m.http_reqs, 'count', 0);
  const failedRate = v(m.http_req_failed, 'rate', 0);
  const failedCount = v(m.http_req_failed, 'fails', 0);
  const avgDur = v(m.http_req_duration, 'avg', 0);
  const p95Dur = v(m.http_req_duration, 'p(95)', 0);
  const iterCount = v(m.iterations, 'count', 0);
  const scenCount = v(m.scenario_iterations, 'count', '-');

  const cardClass = (rate, limit) => rate < limit ? 'green' : 'red';

  const html = `<!DOCTYPE html>
<html lang="ru">
<head>
  <meta charset="UTF-8">
  <title>Load Test Report — ConstellationSim</title>
  <style>
    * { margin: 0; padding: 0; box-sizing: border-box; }
    body { font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif; background: #f5f5f5; color: #333; padding: 20px; }
    .container { max-width: 1200px; margin: 0 auto; }
    h1 { font-size: 24px; margin-bottom: 8px; }
    .subtitle { color: #666; margin-bottom: 24px; font-size: 14px; }
    .summary-cards { display: grid; grid-template-columns: repeat(auto-fit, minmax(200px, 1fr)); gap: 16px; margin-bottom: 24px; }
    .card { background: white; border-radius: 8px; padding: 16px; box-shadow: 0 1px 3px rgba(0,0,0,0.1); }
    .card .label { font-size: 12px; color: #666; text-transform: uppercase; letter-spacing: 0.5px; }
    .card .value { font-size: 28px; font-weight: 700; margin-top: 4px; }
    .card .value.green { color: #22c55e; }
    .card .value.red { color: #ef4444; }
    .card .value.blue { color: #3b82f6; }
    h2 { font-size: 18px; margin: 24px 0 12px; }
    table { width: 100%; border-collapse: collapse; background: white; border-radius: 8px; overflow: hidden; box-shadow: 0 1px 3px rgba(0,0,0,0.1); margin-bottom: 24px; }
    th, td { padding: 10px 14px; text-align: left; border-bottom: 1px solid #eee; font-size: 13px; }
    th { background: #f8f9fa; font-weight: 600; color: #555; text-transform: uppercase; font-size: 11px; letter-spacing: 0.5px; }
    tr:hover { background: #f8f9fa; }
    .threshold-ok { color: #22c55e; font-weight: 700; }
    .threshold-fail { color: #ef4444; font-weight: 700; }
    .footer { text-align: center; color: #999; font-size: 12px; margin-top: 32px; }
  </style>
</head>
<body>
  <div class="container">
    <h1>Load Test Report — ConstellationSim</h1>
    <p class="subtitle">Load profile: 10 &rarr; 100 VU &middot; Duration: ~80s &middot; k6</p>

    <div class="summary-cards">
      <div class="card">
        <div class="label">Total Requests</div>
        <div class="value blue">${totalReqs}</div>
      </div>
      <div class="card">
        <div class="label">Failed Requests</div>
        <div class="value ${cardClass(failedRate, 0.1)}">${pct(failedRate)}%</div>
      </div>
      <div class="card">
        <div class="label">Avg Response Time</div>
        <div class="value blue">${fixed(avgDur, 0)}ms</div>
      </div>
      <div class="card">
        <div class="label">p95 Response Time</div>
        <div class="value blue">${fixed(p95Dur, 0)}ms</div>
      </div>
    </div>

    <h2>Thresholds</h2>
    <table>
      <tr><th>Threshold</th><th>Status</th></tr>
      <tr>
        <td>http_req_duration — p(95) &lt; 10s</td>
        <td class="${p95Dur < 10000 ? 'threshold-ok' : 'threshold-fail'}">${p95Dur < 10000 ? 'PASS' : 'FAIL'}</td>
      </tr>
      <tr>
        <td>http_req_failed — rate &lt; 20%</td>
        <td class="${failedRate < 0.2 ? 'threshold-ok' : 'threshold-fail'}">${failedRate < 0.2 ? 'PASS' : 'FAIL'}</td>
      </tr>
    </table>

    <h2>Request Duration by Endpoint</h2>
    <table>
      <tr>
        <th>Endpoint</th>
        <th>Avg</th>
        <th>Min</th>
        <th>Med</th>
        <th>Max</th>
        <th>p(90)</th>
        <th>p(95)</th>
        <th>Count</th>
      </tr>
      ${m.overview_duration ? metricTableRow('GET /api/overview', m.overview_duration, 'ms') : ''}
      ${m.add_satellites_duration ? metricTableRow('POST /api/add-satellites', m.add_satellites_duration, 'ms') : ''}
      ${m.activate_duration ? metricTableRow('POST /api/activate-satellites', m.activate_duration, 'ms') : ''}
      ${m.mission_duration ? metricTableRow('POST /api/missions', m.mission_duration, 'ms') : ''}
      ${m.delete_duration ? metricTableRow('DELETE /api/delete-satellite', m.delete_duration, 'ms') : ''}
    </table>

    <h2>HTTP Metrics</h2>
    <table>
      <tr><th>Metric</th><th>Value</th></tr>
      <tr><td>http_req_duration — avg</td><td>${fixed(avgDur)} ms</td></tr>
      <tr><td>http_req_duration — p(95)</td><td>${fixed(p95Dur)} ms</td></tr>
      <tr><td>http_reqs — total</td><td>${totalReqs}</td></tr>
      <tr><td>http_req_failed — rate</td><td>${pct(failedRate)}%</td></tr>
      <tr><td>http_req_failed — fails</td><td>${failedCount}</td></tr>
      <tr><td>iterations</td><td>${iterCount}</td></tr>
      <tr><td>scenario_iterations</td><td>${scenCount}</td></tr>
    </table>

    <p class="footer">Generated by k6 handleSummary()</p>
  </div>
</body>
</html>`;

  return {
    'load-test-report.html': html,
  };
}
