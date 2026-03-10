// Static credentials for now (replace with real auth later)
const STATIC_USER = 'admin';
const STATIC_PASS = 'admin';

const AUTH_KEY = 'employee_portal_logged_in';

export function login(username, password) {
  if (username === STATIC_USER && password === STATIC_PASS) {
    sessionStorage.setItem(AUTH_KEY, 'true');
    return true;
  }
  return false;
}

export function logout() {
  sessionStorage.removeItem(AUTH_KEY);
}

export function isAuthenticated() {
  return sessionStorage.getItem(AUTH_KEY) === 'true';
}
