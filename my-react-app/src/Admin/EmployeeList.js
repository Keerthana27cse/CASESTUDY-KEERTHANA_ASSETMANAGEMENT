import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import AdminNavbar from '../navbar/AdminNavbar';

function EmployeeList() {
  const [employees, setEmployees] = useState([]);
  const [msg, setMsg] = useState('');
  const [editRowId, setEditRowId] = useState(null);
  const [editData, setEditData] = useState({});
  const [searchTerm, setSearchTerm] = useState('');
  const navigate = useNavigate();
  const token = localStorage.getItem('token');

  useEffect(() => {
    fetchEmployees();
  }, []);

  const fetchEmployees = async () => {
    try {
      const res = await fetch('http://localhost:8081/api/employees', {
        headers: {
          Authorization: 'Bearer ' + token,
        },
      });

      if (!res.ok) throw new Error('Failed to fetch employees.');

      const data = await res.json();
      setEmployees(data);
    } catch (err) {
      console.error('Error fetching employees:', err);
    }
  };

  const filteredEmployees = employees.filter(emp =>
    emp.name?.toLowerCase().includes(searchTerm.toLowerCase())
  );

  const handleDelete = async (id) => {
    if (!window.confirm('Are you sure you want to delete this employee?')) return;
    try {
      const res = await fetch(`http://localhost:8081/api/employees/${id}`, {
        method: 'DELETE',
        headers: { Authorization: 'Bearer ' + token },
      });

      if (res.ok) {
        setMsg('✅ Employee deleted successfully.');
        fetchEmployees();
      } else {
        setMsg('❌ Failed to delete employee.');
      }
    } catch (err) {
      console.error('Error deleting employee:', err);
    }
  };

  const handleEdit = (employee) => {
    setEditRowId(employee.id);
    setEditData({ ...employee });
  };

  const handleEditChange = (e) => {
    const { name, value } = e.target;
    setEditData((prev) => ({ ...prev, [name]: value }));
  };

  const handleSave = async () => {
    try {
      const res = await fetch('http://localhost:8081/api/employees/update', {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json',
          Authorization: 'Bearer ' + token,
        },
        body: JSON.stringify(editData),
      });

      if (res.ok) {
        alert('✅ Employee updated successfully.');
        setEditRowId(null);
        fetchEmployees();
      } else {
        setMsg('❌ Update failed.');
      }
    } catch (err) {
      console.error('Error updating employee:', err);
    }
  };

  return (
    <>
      <AdminNavbar />
      <div className="container mt-4">
        <div className="text-center mb-4">
          <h3 style={{ fontFamily: 'Verdana, Geneva, sans-serif', fontWeight: '600' }}>
            <i className="fa-solid fa-users me-2"></i> Employee Records
          </h3>
        </div>

        {msg && <div className="alert alert-info text-center">{msg}</div>}


<div className="mb-3 d-flex justify-content-end">
  <div className="input-group" style={{ maxWidth: '250px' }}>
    <span className="input-group-text bg-light border-secondary">
      <i className="fa fa-search text-muted"></i>
    </span>
    <input
      type="text"
      className="form-control form-control-sm border-secondary"
      placeholder="Search..."
      value={searchTerm}
      onChange={(e) => setSearchTerm(e.target.value)}
    />
  </div>
</div>


        <div className="card shadow">
          <div className="card-body table-responsive">
            <table className="table table-bordered table-hover text-center align-middle">
              <thead className="table-dark">
                <tr>
                  <th>ID</th>
                  <th>Username</th>
                  <th>Full Name</th>
                  <th>Email</th>
                  <th>Contact</th>
                  <th>Address</th>
                  <th>Gender</th>
                  <th>Status</th>
                  <th>Created</th>
                  <th>Actions</th>
                </tr>
              </thead>
              <tbody>
                {filteredEmployees.length > 0 ? (
                  filteredEmployees.map((emp) => (
                    <tr key={emp.id}>
                      {editRowId === emp.id ? (
                        <>
                          <td>{emp.id}</td>
                          <td><input className="form-control" name="username" value={editData.username} onChange={handleEditChange} /></td>
                          <td><input className="form-control" name="name" value={editData.name} onChange={handleEditChange} /></td>
                          <td><input className="form-control" name="email" value={editData.email} onChange={handleEditChange} /></td>
                          <td><input className="form-control" name="contactNumber" value={editData.contactNumber} onChange={handleEditChange} /></td>
                          <td><input className="form-control" name="address" value={editData.address} onChange={handleEditChange} /></td>
                          <td>
                            <select className="form-select" name="gender" value={editData.gender} onChange={handleEditChange}>
                              <option value="MALE">Male</option>
                              <option value="FEMALE">Female</option>
                            </select>
                          </td>
                          <td>
                            <select className="form-select" name="status" value={editData.status} onChange={handleEditChange}>
                              <option value="ACTIVE">Active</option>
                              <option value="INACTIVE">Inactive</option>
                            </select>
                          </td>
                          <td>{new Date(emp.createdAt).toLocaleString()}</td>
                          <td>
                            <button className="btn btn-success btn-sm me-1" onClick={handleSave}>Save</button>
                            <button className="btn btn-secondary btn-sm" onClick={() => setEditRowId(null)}>Cancel</button>
                          </td>
                        </>
                      ) : (
                        <>
                          <td>{emp.id}</td>
                          <td>{emp.username}</td>
                          <td>{emp.name}</td>
                          <td>{emp.email}</td>
                          <td>{emp.contactNumber}</td>
                          <td>{emp.address}</td>
                          <td>{emp.gender}</td>
                          <td>
                            <span className={`badge ${emp.status === 'ACTIVE' ? 'bg-success' : 'bg-secondary'}`}>
                              {emp.status}
                            </span>
                          </td>
                          <td>{new Date(emp.createdAt).toLocaleString()}</td>
                          <td>
                            <button className="btn btn-warning btn-sm me-2" onClick={() => handleEdit(emp)}>Edit</button>
                            <button className="btn btn-danger btn-sm" onClick={() => handleDelete(emp.id)}>Delete</button>
                          </td>
                        </>
                      )}
                    </tr>
                  ))
                ) : (
                  <tr>
                    <td colSpan="10">No employee records found.</td>
                  </tr>
                )}
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </>
  );
}

export default EmployeeList;
