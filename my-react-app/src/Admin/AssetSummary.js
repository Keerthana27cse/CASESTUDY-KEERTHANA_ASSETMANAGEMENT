import React, { useEffect, useState } from 'react';
import AdminNavbar from '../navbar/AdminNavbar';

const API_BASE = 'http://localhost:8081/api/allocations';

function AssetAssignmentSummary() {
  const [allocations, setAllocations] = useState([]);
  const [counts, setCounts] = useState({});
  const [statusFilter, setStatusFilter] = useState('ALL');
  const [msg, setMsg] = useState('');
  const [msgType, setMsgType] = useState('');
  const token = localStorage.getItem('token');

  useEffect(() => {
    fetchAllocations();
    fetchCounts();
  }, []);

  useEffect(() => {
    if (statusFilter === 'ALL') {
      fetchAllocations();
    } else {
      fetchAllocationsByStatus(statusFilter);
    }
  }, [statusFilter]);

  const fetchAllocations = async () => {
    setMsg('');
    try {
      const res = await fetch(API_BASE, {
        headers: { Authorization: `Bearer ${token}` },
      });
      if (!res.ok) throw new Error('Failed to fetch allocations');
      const data = await res.json();
      setAllocations(data);
    } catch (error) {
      setMsg('Error fetching allocations');
      setMsgType('error');
    }
  };

  const fetchAllocationsByStatus = async (status) => {
    setMsg('');
    try {
      const res = await fetch(`${API_BASE}/status/${status}`, {
        headers: { Authorization: `Bearer ${token}` },
      });
      if (!res.ok) throw new Error('Failed to fetch filtered allocations');
      const data = await res.json();
      setAllocations(data);
    } catch (error) {
      setMsg('Error fetching allocations by status');
      setMsgType('error');
    }
  };

  const fetchCounts = async () => {
    try {
      const res = await fetch(`${API_BASE}/counts`, {
        headers: { Authorization: `Bearer ${token}` },
      });
      if (!res.ok) throw new Error('Failed to fetch counts');
      const data = await res.json();
      setCounts(data);
    } catch (error) {
      // ignore count errors silently or set message
    }
  };

  const statusOptions = ['ALL', 'ALLOCATED', 'IN_USE', 'RETURNED', 'RESOLVED'];

  return (
    <>
      <AdminNavbar />
      <div className="container mt-4">
        <div className="container mt-4">
        <div className="text-center mb-4">
          <h3 className="fw-bold text-dark" style={{ fontFamily: 'Verdana' }}>
            <i className="fa-solid fa-clipboard-list me-2"></i> Allocation Summary
          </h3>
        </div>
        {msg && (
          <div className={`alert alert-${msgType === 'error' ? 'danger' : 'success'}`}>
            {msg}
          </div>
        )}

        <div className="mb-3">
          <label htmlFor="statusFilter" className="form-label">
            Filter by Status:
          </label>
          <select
            id="statusFilter"
            className="form-select"
            value={statusFilter}
            onChange={(e) => setStatusFilter(e.target.value)}
          >
            {statusOptions.map((status) => (
              <option key={status} value={status}>
                {status}
              </option>
            ))}
          </select>
        </div>

        <h5>Allocation Counts Per Employee</h5>
        <ul className="list-group mb-4">
          {Object.entries(counts).map(([employee, count]) => (
            <li key={employee} className="list-group-item d-flex justify-content-between align-items-center">
              {employee}
              <span className="badge bg-primary rounded-pill">{count}</span>
            </li>
          ))}
          {Object.keys(counts).length === 0 && <li className="list-group-item">No data available.</li>}
        </ul>

        <h5>Allocations {statusFilter !== 'ALL' && `- ${statusFilter}`}</h5>
        {allocations.length === 0 ? (
          <div className="alert alert-info">No allocations found.</div>
        ) : (
          <div className="table-responsive">

          <table className="table table-bordered table-striped">
            <thead className="table-dark">
              <tr>
                <th>#</th>
                <th>Employee</th>
                <th>Asset</th>
                <th>Category</th>
                <th>Status</th>
                <th>Allocation Date</th>
                <th>Return Date</th>
              </tr>
            </thead>
            <tbody>
              {allocations.map((alloc, idx) => (
                <tr key={alloc.id}>
                  <td>{idx + 1}</td>
                  <td>{alloc.employeeName || '-'}</td>
                  <td>{alloc.assetName || '-'}</td>
                  <td>{alloc.categoryName || '-'}</td>
                  <td>
                    <span className={`badge ${getStatusClass(alloc.allocationStatus)}`}>
                      {alloc.allocationStatus}
                    </span>
                  </td>
                  <td>{formatDate(alloc.allocationDate)}</td>
                  <td>{formatDate(alloc.returnDate)}</td>
                </tr>
              ))}
            </tbody>
          </table>
          </div>
        )}
      </div>
      </div>
    </>
  );
}

function getStatusClass(status) {
  switch (status) {
    case 'ALLOCATED':
      return 'bg-primary text-white';
    case 'IN_USE':
      return 'bg-success';
    case 'RETURNED':
      return 'bg-warning text-dark';
    case 'RESOLVED':
      return 'bg-info text-white';
    default:
      return 'bg-secondary';
  }
}

function formatDate(dateStr) {
  if (!dateStr) return '-';
  return new Date(dateStr).toLocaleDateString();
}

export default AssetAssignmentSummary;
