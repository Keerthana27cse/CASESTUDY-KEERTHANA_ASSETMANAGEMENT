import React, { useEffect, useState } from 'react';
import AdminNavbar from '../navbar/AdminNavbar';

function AdminSettings() {
  const token = localStorage.getItem('token');
  const [employees, setEmployees] = useState([]);
  const [msg, setMsg] = useState('');
  const [msgType, setMsgType] = useState('');

  useEffect(() => {
    fetchEmployees();
  }, []);

  const fetchEmployees = () => {
    fetch('http://localhost:8081/api/employees', {
      headers: { Authorization: `Bearer ${token}` },
    })
      .then(res => res.json())
      .then(data => setEmployees(data))
      .catch(() => {
        setMsg('Failed to load employees');
        setMsgType('error');
      });
  };

  const handleEmployeeChange = (index, field, value) => {
    const updated = [...employees];
    updated[index][field] = value;
    setEmployees(updated);
  };

  const saveEmployee = employee => {
    fetch('http://localhost:8081/api/employees/update', {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json', Authorization: `Bearer ${token}` },
      body: JSON.stringify(employee),
    })
      .then(res => {
        if (res.ok) {
          setMsg('Employee updated successfully');
          setMsgType('success');
          fetchEmployees();
        } else {
          setMsg('Failed to update employee');
          setMsgType('error');
        }
      })
      .catch(() => {
        setMsg('Error updating employee');
        setMsgType('error');
      });
  };

  return (
    <>
    <AdminNavbar/>
    <div className="container mt-4">
      <h3>Admin Settings - Manage Employees</h3>

      {msg && (
        <div className={`alert alert-${msgType === 'success' ? 'success' : 'danger'}`}>
          {msg}
        </div>
      )}

      <table className="table table-bordered table-striped mt-3">
        <thead>
          <tr>
            <th>Name</th>
            <th>Email</th>
            <th>Contact Number</th>
            <th>Role</th>
            <th>Status</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {employees.map((emp, i) => (
            <tr key={emp.id}>
              <td>
                <input
                  type="text"
                  value={emp.name}
                  onChange={e => handleEmployeeChange(i, 'name', e.target.value)}
                  className="form-control"
                />
              </td>
              <td>{emp.email}</td>
              <td>
                <input
                  type="text"
                  value={emp.contactNumber}
                  onChange={e => handleEmployeeChange(i, 'contactNumber', e.target.value)}
                  className="form-control"
                />
              </td>
              <td>
                <select
                  value={emp.role}
                  onChange={e => handleEmployeeChange(i, 'role', e.target.value)}
                  className="form-control"
                >
                  <option value="EMPLOYEE">Employee</option>
                  <option value="ADMIN">Admin</option>
                </select>
              </td>
              <td>
                <select
                  value={emp.empstatus || 'ACTIVE'}
                  onChange={e => handleEmployeeChange(i, 'empstatus', e.target.value)}
                  className="form-control"
                >
                  <option value="ACTIVE">Active</option>
                  <option value="INACTIVE">Inactive</option>
                </select>
              </td>
              <td>
                <button
                  className="btn btn-sm btn-primary"
                  onClick={() => saveEmployee(emp)}
                >
                  Save
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
    </>
  );
}

export default AdminSettings;
