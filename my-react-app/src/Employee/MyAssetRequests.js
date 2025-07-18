import React, { useEffect, useState } from 'react';
import EmployeeNavbar from '../navbar/EmployeeNavbar';
import PDF from './PDF';

function MyAssetRequests() {
  const [requests, setRequests] = useState([]);
  const [loading, setLoading] = useState(true);
  const [msg, setMsg] = useState('');
  const [msgType, setMsgType] = useState('');
  const token = localStorage.getItem('token');

  useEffect(() => {
    fetchRequests();
  }, []);


  const fetchRequests = async () => {
    setLoading(true);
    try {
      const res = await fetch('http://localhost:8081/api/asset-requests/my-requests', {
        headers: { Authorization: `Bearer ${token}` },
      });
      if (!res.ok) throw new Error('Unauthorized');
      const data = await res.json();
      setRequests(data);
    } catch (err) {
      setMsg('Failed to fetch your asset requests. Please login.');
      setMsgType('error');
    } finally {
      setLoading(false);
    }
  };

  const handleStatusUpdate = async (id, action, isDamaged = false) => {
    let url = `http://localhost:8081/api/asset-requests/${id}/${action}`;
    if (action === 'return') url += `?damaged=${isDamaged}`;

    try {
      const res = await fetch(url, {
        method: 'PUT',
        headers: { Authorization: `Bearer ${token}` },
      });

      let data = { message: '' };
      try {
        data = await res.json();
      } catch {
        data.message = await res.text();
      }

      if (res.ok) {
        setMsg(data.message || `${action} success.`);
        setMsgType('success');
        fetchRequests();
      } else {
        setMsg(data.message || `Failed to ${action} request.`);
        setMsgType('error');
      }
    } catch (err) {
      setMsg(`Error during ${action}.`);
      setMsgType('error');
    }
  };

  
  
const handleDownloadPDF = (request) => {
  const doc = PDF(request);
  doc.save(`Asset_Request_${request.id}.pdf`);
};

  return (
    <>
      <EmployeeNavbar />
      <div className="container mt-4">
      <div className="text-center mb-4">
<h3 style={{ fontFamily: 'Verdana, Geneva, sans-serif', fontWeight: '600' }}>
  <i className="fa fa-folder-open me-2"></i> My Asset Requests
</h3>
</div>


        {msg && <div className={`alert alert-${msgType}`}>{msg}</div>}
        
        {loading && <p>Loading...</p>}

        {!loading && requests.length === 0 && (
          <div className="alert alert-info">No asset requests found.</div>
        )}

        {!loading && requests.length > 0 && (
            <
          div className="card shadow">
          <div className="card-body table-responsive">
            <table className="table table-bordered table-hover text-center align-middle">
            <thead className="table-dark">
              <tr>
                <th>S.No</th>
                <th>Asset</th>
                <th>Category</th>
                <th>Status</th>
                <th>Requested On</th>
                <th>Address</th>
                <th>Zip</th>
                <th>Phone</th>
                <th>Actions</th>
                <th></th>
              </tr>
            </thead>
            <tbody>
              {requests.map((req, index) => (
                <tr key={req.id}>
                  <td>{index + 1}</td>
                  <td>{req.assetName}</td>
                  <td>{req.categoryName}</td>
                  <td>
                    <span className={`badge ${getStatusClass(req.status)}`}>
                      {req.status}
                    </span>
                  </td>
                  <td>{formatDate(req.requestDate)}</td>
                  <td>{req.fullAddress}</td>
                  <td>{req.zipCode}</td>
                  <td>{req.phone}</td>
                  <td>
                    {req.status === 'SHIPPED' && (
                      <button
                        className="btn btn-sm btn-success me-2"
                        onClick={() => handleStatusUpdate(req.id, 'in-use')}
                      >
                        Mark IN USE
                      </button>
                    )}
                    {req.status === 'IN_USE' && (
                      <>
                        <button
                          className="btn btn-sm btn-warning me-2"
                          onClick={() => handleStatusUpdate(req.id, 'return', false)}
                        >
                          Return (Good)
                        </button>
                        <button
                          className="btn btn-sm btn-danger"
                          onClick={() => handleStatusUpdate(req.id, 'return', true)}
                        >
                          Return (Damaged)
                        </button>
                      </>
                    )}
                    {!['SHIPPED', 'IN_USE'].includes(req.status) && (
                      <em>No actions</em>
                    )}
                  </td>
                  <td>
  <button
    className="btn btn-sm btn-outline-success"
    title="Download PDF"
    onClick={() => handleDownloadPDF(req)}
  >
    <i className="fa fa-download me-1"></i>
  </button>
</td>

                
                </tr>
              ))}
            </tbody>
          </table>
          </div>
          </div>
        )}
        </div>
    </>
  );
}

function getStatusClass(status) {
  switch ((status || '').toUpperCase()) {
    case 'PENDING':
      return 'bg-warning text-dark';
    case 'APPROVED':
      return 'bg-success';
    case 'REJECTED':
      return 'bg-danger';
    case 'SHIPPED':
      return 'bg-info text-dark';
    case 'IN_USE':
      return 'bg-primary text-white';
    case 'RETURN_REQUEST':
    case 'DAMAGED':
      return 'bg-warning';
    case 'RETURNED':
      return 'bg-dark text-white';
    case 'RESOLVED':
    case 'COMPLETED':
      return 'bg-success';
    default:
      return 'bg-secondary';
  }
}


function formatDate(dateStr) {
  return dateStr ? new Date(dateStr).toLocaleDateString() : '-';
}

export default MyAssetRequests;
