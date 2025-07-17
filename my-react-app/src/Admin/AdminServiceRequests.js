import React, { useEffect, useState } from 'react';
import AdminNavbar from '../navbar/AdminNavbar';

function AdminServiceRequests() {
  const [requests, setRequests] = useState([]);
  const [msg, setMsg] = useState('');
  const [msgType, setMsgType] = useState('');
  const token = localStorage.getItem('token');

  useEffect(() => {
    fetchServiceRequests();
  }, []);

  const fetchServiceRequests = async () => {
    try {
      const res = await fetch('http://localhost:8081/api/service-requests/admin', {
        headers: { Authorization: `Bearer ${token}` },
      });
      if (res.ok) {
        const data = await res.json();
        setRequests(data);
      } else {
        setMsg('Failed to fetch service requests.');
        setMsgType('error');
      }
    } catch (err) {
      console.error(err);
      setMsg('Error fetching service requests.');
      setMsgType('error');
    }
  };

  const updateStatus = async (id, action) => {
    try {
      const res = await fetch(`http://localhost:8081/api/service-requests/${id}/${action}`, {
        method: 'PUT',
        headers: { Authorization: `Bearer ${token}` },
      });

      let data;
      try {
        data = await res.json();
      } catch (e) {
        data = { message: `Service request ${action}d.` };
      }

      if (res.ok) {
        setMsg(`Request ${action}d successfully.`);
        setMsgType('success');
        fetchServiceRequests();
      } else {
        setMsg(data.message || `Failed to ${action} request.`);
        setMsgType('error');
      }
    } catch (err) {
      console.error(err);
      setMsg(`Error while trying to ${action} request.`);
      setMsgType('error');
    }
  };

  return (
    <>
      <AdminNavbar />
       <div className="container mt-4">
        <div className="text-center mb-4">
          <h3 className="fw-bold text-dark" style={{ fontFamily: 'Verdana' }}>
            <i className="fa-solid fa-clipboard-list me-2"></i> Service Requests
          </h3>
        </div>

        {msg && (
          <div
            className={`alert ${msgType === 'success' ? 'alert-success' : 'alert-danger'}`}
          >
            {msg}
          </div>
        )}

        {requests.length === 0 ? (
          <div className="alert alert-info">No service requests found.</div>
        ) : (
          <div className="table-responsive">

          <table className="table table-bordered table-striped mt-3">
            <thead className="table-dark">
              <tr>
                <th>#</th>
                <th>Employee</th>
                <th>Asset</th>
                <th>Issue Type</th>
                <th>Description</th>
                <th>Status</th>
                <th>Requested On</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              {requests.map((req, index) => (
                <tr key={req.id}>
                  <td>{index + 1}</td>
                  <td>{req.employeeName}</td>
                  <td>{req.assetName}</td>
                  <td>{req.issueType}</td>
                  <td>{req.description}</td>
                  <td>
                    <span className={`badge ${getStatusClass(req.status)}`}>
                      {req.status}
                    </span>
                  </td>
                  <td>{formatDate(req.requestDate)}</td>
                  <td>
                    {req.status === 'PENDING' ? (
                      <>
                        <button
                          className="btn btn-sm btn-success me-2"
                          onClick={() => updateStatus(req.id, 'approve')}
                        >
                          Approve
                        </button>
                        <button
                          className="btn btn-sm btn-danger"
                          onClick={() => updateStatus(req.id, 'reject')}
                        >
                          Reject
                        </button>
                      </>
                    ) : (
                      <em>COMPLETED</em>
                    )}
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
          </div>
        )}
      </div>
    </>
  );
}

function formatDate(dateString) {
  if (!dateString) return '-';
  return new Date(dateString).toLocaleString();
}

function getStatusClass(status) {
  switch (status) {
    case 'PENDING':
      return 'bg-warning text-dark';
    case 'APPROVED':
      return 'bg-success';
    case 'REJECTED':
      return 'bg-danger';
    default:
      return 'bg-secondary';
  }
}

export default AdminServiceRequests;
