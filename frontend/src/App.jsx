import { Routes, Route, Navigate } from 'react-router-dom';
import Login from './pages/Login';
import Dashboard from './pages/Dashboard';
import EmployeeForm from './pages/EmployeeForm';

export default function App() {
  return (
    <Routes>
      <Route path="/" element={<Login />} />
      <Route path="/dashboard" element={<Dashboard />} />
      <Route path="/employee/new" element={<EmployeeForm />} />
      <Route path="/employee/:id" element={<EmployeeForm />} />
      <Route path="*" element={<Navigate to="/" replace />} />
    </Routes>
  );
}
