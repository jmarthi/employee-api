import { useState } from 'react';
import { useNavigate, Navigate } from 'react-router-dom';
import { login, isAuthenticated } from '../auth';

export default function Login() {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const navigate = useNavigate();

  if (isAuthenticated()) {
    return <Navigate to="/dashboard" replace />;
  }

  function handleSubmit(e) {
    e.preventDefault();
    setError('');
    if (login(username, password)) {
      navigate('/dashboard', { replace: true });
    } else {
      setError('Invalid username or password.');
    }
  }

  return (
    <div style={styles.container}>
      <div style={styles.card}>
        <h1 style={styles.title}>Employee Portal</h1>
        <p style={styles.subtitle}>Sign in to continue</p>
        <form onSubmit={handleSubmit} style={styles.form}>
          {error && <div style={styles.error}>{error}</div>}
          <label style={styles.label}>Username</label>
          <input
            type="text"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
            required
            autoFocus
            style={styles.input}
          />
          <label style={styles.label}>Password</label>
          <input
            type="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            required
            style={styles.input}
          />
          <button type="submit" style={styles.button}>
            Sign in
          </button>
        </form>
      </div>
    </div>
  );
}

const styles = {
  container: {
    minHeight: '100vh',
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'center',
    padding: 16,
  },
  card: {
    background: '#fff',
    borderRadius: 8,
    padding: 32,
    width: '100%',
    maxWidth: 360,
    boxShadow: '0 2px 8px rgba(0,0,0,0.08)',
  },
  title: {
    margin: '0 0 4px 0',
    color: '#2563eb',
    fontSize: '1.5rem',
  },
  subtitle: {
    margin: '0 0 24px 0',
    color: '#6b7280',
    fontSize: '0.9rem',
  },
  form: {
    display: 'flex',
    flexDirection: 'column',
  },
  label: {
    marginBottom: 4,
    fontWeight: 500,
    color: '#374151',
  },
  input: {
    padding: '10px 12px',
    marginBottom: 16,
    border: '1px solid #d1d5db',
    borderRadius: 6,
    fontSize: 16,
  },
  button: {
    padding: '10px 16px',
    background: '#2563eb',
    color: '#fff',
    border: 'none',
    borderRadius: 6,
    fontWeight: 500,
    marginTop: 8,
  },
  error: {
    padding: 10,
    marginBottom: 16,
    background: '#fef2f2',
    color: '#991b1b',
    borderRadius: 6,
    fontSize: 14,
  },
};
