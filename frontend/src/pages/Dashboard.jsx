import { useState, useEffect } from 'react';
import { useNavigate, Navigate } from 'react-router-dom';
import { isAuthenticated, logout } from '../auth';
import { getAllEmployees, deleteEmployee } from '../api/employees';

export default function Dashboard() {
  const [employees, setEmployees] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const navigate = useNavigate();

  if (!isAuthenticated()) {
    return <Navigate to="/" replace />;
  }

  useEffect(() => {
    let cancelled = false;
    getAllEmployees()
      .then((data) => {
        if (!cancelled) setEmployees(data);
      })
      .catch((e) => {
        if (!cancelled) setError(e.message);
      })
      .finally(() => {
        if (!cancelled) setLoading(false);
      });
    return () => { cancelled = true; };
  }, []);

  function handleLogout() {
    logout();
    navigate('/', { replace: true });
  }

  async function handleDelete(emp) {
    if (!window.confirm(`Delete ${emp.firstName} ${emp.lastName}?`)) return;
    try {
      await deleteEmployee(emp.id);
      setEmployees((prev) => prev.filter((e) => e.id !== emp.id));
    } catch (e) {
      setError(e.message);
    }
  }

  return (
    <div style={styles.page}>
      <header style={styles.header}>
        <h1 style={styles.title}>Dashboard</h1>
        <div style={styles.headerRight}>
          <button onClick={() => navigate('/employee/new')} style={styles.addBtn}>
            Add employee
          </button>
          <button onClick={handleLogout} style={styles.logoutBtn}>
            Logout
          </button>
        </div>
      </header>

      <main style={styles.main}>
        {error && <div style={styles.error}>{error}</div>}
        {loading ? (
          <p style={styles.muted}>Loading…</p>
        ) : (
          <div style={styles.card}>
            <h2 style={styles.cardTitle}>All employees</h2>
            {employees.length === 0 ? (
              <p style={styles.muted}>No employees yet. Add one using the button above.</p>
            ) : (
              <table style={styles.table}>
                <thead>
                  <tr>
                    <th style={styles.th}>ID</th>
                    <th style={styles.th}>First name</th>
                    <th style={styles.th}>Last name</th>
                    <th style={styles.th}>Email</th>
                    <th style={styles.th}>Department</th>
                    <th style={styles.th}>Actions</th>
                  </tr>
                </thead>
                <tbody>
                  {employees.map((emp) => (
                    <tr key={emp.id} style={styles.tr}>
                      <td style={styles.td}>{emp.id}</td>
                      <td style={styles.td}>{emp.firstName}</td>
                      <td style={styles.td}>{emp.lastName}</td>
                      <td style={styles.td}>{emp.email}</td>
                      <td style={styles.td}>{emp.department || '—'}</td>
                      <td style={styles.td}>
                        <button
                          type="button"
                          onClick={() => navigate(`/employee/${emp.id}`)}
                          style={styles.editBtn}
                        >
                          Edit
                        </button>
                        <button
                          type="button"
                          onClick={() => handleDelete(emp)}
                          style={styles.deleteBtn}
                        >
                          Delete
                        </button>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            )}
          </div>
        )}
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
  headerRight: {
    display: 'flex',
    gap: 12,
    alignItems: 'center',
  },
  addBtn: {
    padding: '8px 16px',
    background: '#2563eb',
    color: '#fff',
    border: 'none',
    borderRadius: 6,
    fontWeight: 500,
  },
  logoutBtn: {
    padding: '8px 16px',
    background: '#e5e7eb',
    color: '#374151',
    border: 'none',
    borderRadius: 6,
    fontWeight: 500,
  },
  main: {
    maxWidth: 900,
    margin: '0 auto',
  },
  card: {
    background: '#fff',
    borderRadius: 8,
    padding: 24,
    boxShadow: '0 1px 3px rgba(0,0,0,0.08)',
  },
  cardTitle: {
    margin: '0 0 16px 0',
    fontSize: '1.1rem',
    color: '#374151',
  },
  table: {
    width: '100%',
    borderCollapse: 'collapse',
  },
  th: {
    textAlign: 'left',
    padding: '10px 12px',
    background: '#f9fafb',
    fontWeight: 600,
    color: '#374151',
  },
  tr: {
    borderBottom: '1px solid #e5e7eb',
  },
  td: {
    padding: '10px 12px',
  },
  editBtn: {
    padding: '6px 12px',
    background: '#e5e7eb',
    color: '#374151',
    border: 'none',
    borderRadius: 6,
    fontSize: 14,
    fontWeight: 500,
    marginRight: 8,
  },
  deleteBtn: {
    padding: '6px 12px',
    background: '#fef2f2',
    color: '#991b1b',
    border: 'none',
    borderRadius: 6,
    fontSize: 14,
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
