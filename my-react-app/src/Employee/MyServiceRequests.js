//MyServiceRequets.js
import React, { useEffect, useState } from 'react';
import EmployeeNavbar from '../navbar/EmployeeNavbar';

function MyServiceRequests() {
  const [requests, setRequests] = useState([]);
  const [loading, setLoading] = useState(true);
  const [msg, setMsg] = useState('');
  const token = localStorage.getItem('token');

  useEffect(() => {
    const fetchRequests = async () => {
      try {
        const res = await fetch('http://localhost:8081/api/service-requests/my-requests', {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });

        if (res.ok) {
          const data = await res.json();
          setRequests(data);
        } else {
          setMsg('Failed to fetch service requests.');
        }
      } catch (err) {
        setMsg('Error fetching service requests.');
        console.error(err);
      } finally {
        setLoading(false);
      }
    };

    fetchRequests();
  }, []);

  return (
    <>
      <EmployeeNavbar />
       <div className="container mt-4">
        <div className="text-center mb-4">
          <h3 className="fw-bold text-dark" style={{ fontFamily: 'Verdana' }}>
            <i className="fa-solid fa-clipboard-list me-2"></i> Service Requests
          </h3>
        </div>

        {msg && <div className="alert alert-danger">{msg}</div>}
        {loading && <p>Loading...</p>}

        {!loading && requests.length === 0 && (
          <div className="alert alert-info">No service requests found.</div>
        )}

        {!loading && requests.length > 0 && (
        <div className="table-responsive">

          <table className="table table-bordered table-striped mt-3">
            <thead className="table-dark">
              <tr>
                <th>#</th>
                <th>Asset Name</th>
                <th>category Name</th>
                <th>Issue Type</th>
                <th>Description</th>
                <th>Status</th>
                <th>Requested On</th>
              </tr>
            </thead>
            <tbody>
              {requests.map((req, idx) => (
                <tr key={req.id}>
                  <td>{idx + 1}</td>
                  <td>{req.assetName || '-'}</td>
                  <td>{req.categoryName || '-'}</td>
                  <td>{req.issueType}</td>
                  <td>{req.description}</td>
                  <td>
                    <span className={`badge ${getStatusClass(req.status)}`}>
                      {req.status}
                    </span>
                  </td>
                  <td>{formatDate(req.requestDate)}</td>
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

function formatDate(dateString) {
  return new Date(dateString).toLocaleDateString();
}

export default MyServiceRequests;
