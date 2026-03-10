// When UI is served separately, set VITE_API_URL to the API origin (e.g. http://localhost:8080).
// Leave unset when using Vite dev proxy or when UI is served from the same host as the API.
const API_BASE = `${import.meta.env.VITE_API_URL || ''}/api/employees`;

async function handleResponse(res) {
  const text = await res.text();
  const data = text ? JSON.parse(text) : {};
  if (!res.ok) {
    const msg = data.message || data.error || res.statusText;
    throw new Error(typeof msg === 'string' ? msg : JSON.stringify(msg));
  }
  return data;
}

export async function getAllEmployees() {
  const res = await fetch(API_BASE);
  return handleResponse(res);
}

export async function getEmployee(id) {
  const res = await fetch(`${API_BASE}/${id}`);
  return handleResponse(res);
}

export async function createEmployee(body) {
  const res = await fetch(API_BASE, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(body),
  });
  return handleResponse(res);
}

export async function updateEmployee(id, body) {
  const res = await fetch(`${API_BASE}/${id}`, {
    method: 'PUT',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(body),
  });
  return handleResponse(res);
}
