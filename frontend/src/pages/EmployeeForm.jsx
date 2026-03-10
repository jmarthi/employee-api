import { useState, useEffect } from 'react';
import { useNavigate, useParams, Navigate } from 'react-router-dom';
import { isAuthenticated } from '../auth';
import { getEmployee, createEmployee, updateEmployee } from '../api/employees';

export default function EmployeeForm() {
  const { id } = useParams();
  const isEdit = id && id !== 'new';
  const navigate = useNavigate();

  const [name, setName] = useState('');
  const [email, setEmail] = useState('');
  const [department, setDepartment] = useState('');
  const [loading, setLoading] = useState(isEdit);
  const [saving, setSaving] = useState(false);
  const [error, setError] = useState('');

  if (!isAuthenticated()) {
    return <Navigate to="/" replace />;
  }

  useEffect(() => {
    if (!isEdit) return;
    let cancelled = false;
    getEmployee(id)
      .then((emp) => {
        if (!cancelled) {
          setName(emp.name || '');
          setEmail(emp.email || '');
          setDepartment(emp.department || '');
        }
      })
      .catch((e) => {
        if (!cancelled) setError(e.message);
      })
      .finally(() => {
        if (!cancelled) setLoading(false);
      });
    return () => { cancelled = true; };
  }, [id, isEdit]);

  async function handleSubmit(e) {
    e.preventDefault();
    setError('');
    setSaving(true);
    const body = {
      name: name.trim(),
      email: email.trim(),
      department: department.trim() || null,
    };
    try {
      if (isEdit) {
        await updateEmployee(id, body);
      } else {
        await createEmployee(body);
      }
      navigate('/dashboard', { replace: true });
    } catch (e) {
      setError(e.message);
    } finally {
      setSaving(false);
    }
  }

  if (loading) {
    return (
      <div style={styles.page}>
        <p style={styles.muted}>Loading…</p>
      </div>
    );
  }

  return (
    <div style={styles.page}>
      <header style={styles.header}>
        <h1 style={styles.title}>{isEdit ? 'Edit employee' : 'New employee'}</h1>
        <button type="button" onClick={() => navigate('/dashboard')} style={styles.backBtn}>
          Back to dashboard
        </button>
      </header>

      <main style={styles.main}>
        <div style={styles.card}>
          {error && <div style={styles.error}>{error}</div>}
          <form onSubmit={handleSubmit} style={styles.form}>
            <label style={styles.label}>Name *</label>
            <input
              type="text"
              value={name}
              onChange={(e) => setName(e.target.value)}
              required
              placeholder="e.g. John Doe"
              style={styles.input}
            />
            <label style={styles.label}>Email *</label>
            <input
              type="email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              required
              placeholder="e.g. john@example.com"
              style={styles.input}
            />
            <label style={styles.label}>Department</label>
            <input
              type="text"
              value={department}
              onChange={(e) => setDepartment(e.target.value)}
              placeholder="e.g. Engineering"
              style={styles.input}
            />
            <div style={styles.actions}>
              <button type="submit" disabled={saving} style={styles.saveBtn}>
                {saving ? 'Saving…' : 'Save'}
              </button>
              <button
                type="button"
                onClick={() => navigate('/dashboard')}
                style={styles.cancelBtn}
              >
                Cancel
              </button>
            </div>
          </form>
        </div>
      </main>
    </div>
  );
}

const styles = {
  page: {
    minHeight: '100vh',
    padding: 24,
  },
  header: {
    display: 'flex',
    justifyContent: 'space-between',
    alignItems: 'center',
    marginBottom: 24,
    flexWrap: 'wrap',
    gap: 12,
  },
  title: {
    margin: 0,
    color: '#2563eb',
    fontSize: '1.5rem',
  },
  backBtn: {
    padding: '8px 16px',
    background: '#e5e7eb',
    color: '#374151',
    border: 'none',
    borderRadius: 6,
    fontWeight: 500,
  },
  main: {
    maxWidth: 560,
    margin: '0 auto',
  },
  card: {
    background: '#fff',
    borderRadius: 8,
    padding: 24,
    boxShadow: '0 1px 3px rgba(0,0,0,0.08)',
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
  actions: {
    display: 'flex',
    gap: 12,
    marginTop: 8,
  },
  saveBtn: {
    padding: '10px 20px',
    background: '#2563eb',
    color: '#fff',
    border: 'none',
    borderRadius: 6,
    fontWeight: 500,
  },
  cancelBtn: {
    padding: '10px 20px',
    background: '#e5e7eb',
    color: '#374151',
    border: 'none',
    borderRadius: 6,
    fontWeight: 500,
  },
  error: {
    padding: 12,
    marginBottom: 16,
    background: '#fef2f2',
    color: '#991b1b',
    borderRadius: 6,
  },
  muted: {
    color: '#6b7280',
    margin: 0,
  },
};
