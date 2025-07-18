import React, { useEffect, useState } from 'react';
import AdminNavbar from '../navbar/AdminNavbar';
import jsPDF from 'jspdf';

function AdminAssetRequests() {
  const [requests, setRequests] = useState([]);
  const [loading, setLoading] = useState(true);
  const [msg, setMsg] = useState('');
  const [msgType, setMsgType] = useState('');
  const [searchTerm, setSearchTerm] = useState('');
  const [currentPage, setCurrentPage] = useState(1);
  const token = localStorage.getItem('token');
  const itemsPerPage = 5;

  useEffect(() => {
    fetchAllRequests();
  }, []);

  const fetchAllRequests = async () => {
    setLoading(true);
    try {
      const res = await fetch('http://localhost:8081/api/asset-requests/all', {
        headers: { Authorization: `Bearer ${token}` },
      });
      if (!res.ok) throw new Error('Unauthorized');
      const data = await res.json();
      setRequests(data);
    } catch (err) {
      setMsg('Failed to fetch asset requests. Please login.');
      setMsgType('error');
    } finally {
      setLoading(false);
    }
  };

  const updateStatus = async (id, action) => {
    try {
      const res = await fetch(`http://localhost:8081/api/asset-requests/${action}/${id}`, {
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
        setMsg(`Request ${action}d successfully.`);
        setMsgType('success');
        fetchAllRequests();
      } else {
        setMsg(data.message || `Failed to ${action} request.`);
        setMsgType('error');
      }
    } catch (err) {
      setMsg(`Error while trying to ${action} request.`);
      setMsgType('error');
      console.error(err);
    }
  };

  const filteredRequests = requests.filter(
    req =>
      req.employeeName?.toLowerCase().includes(searchTerm.toLowerCase()) ||
      req.assetName?.toLowerCase().includes(searchTerm.toLowerCase())
  );

  const indexOfLast = currentPage * itemsPerPage;
  const indexOfFirst = indexOfLast - itemsPerPage;
  const currentItems = filteredRequests.slice(indexOfFirst, indexOfLast);

  const handleExportPDF = () => {
    const doc = new jsPDF();
    doc.text('Asset Requests', 14, 15);
    doc.autoTable({
      head: [['#', 'Employee', 'Asset', 'Category', 'Status']],
      body: filteredRequests.map((r, i) => [
        i + 1,
        r.employeeName,
        r.assetName,
        r.categoryName,
        r.status,
      ]),
    });
    doc.save('Asset_Requests.pdf');
  };

  return (
    <>
      <AdminNavbar />
      <div className="container mt-4">
        <div className="text-center mb-4">
          <h3 className="fw-bold text-dark" style={{ fontFamily: 'Verdana' }}>
            <i className="fa-solid fa-clipboard-list me-2"></i> Asset Requests
          </h3>
        </div>

        <div className="mb-3 d-flex justify-content-between align-items-center">
          <input
            type="text"
            className="form-control w-50"
            placeholder="Search by employee or asset..."
            onChange={(e) => setSearchTerm(e.target.value)}
          />
          <button className="btn btn-outline-danger ms-3" onClick={handleExportPDF}>
            <i className="fa-solid fa-file-pdf me-1"></i> Export PDF
          </button>
        </div>

        {msg && <div className={`alert alert-${msgType}`}>{msg}</div>}
        {loading && <p>Loading...</p>}

        {!loading && currentItems.length === 0 && (
          <div className="alert alert-info">No asset requests found.</div>
        )}

        {!loading && currentItems.length > 0 && (
          <>
            <div className="table-responsive">
              <table className="table table-bordered table-striped text-center">
                <thead className="table-dark">
                  <tr>
                    <th>#</th>
                    <th>Employee</th>
                    <th>Asset</th>
                    <th>Category</th>
                    <th>Description</th>
                    <th>Reason</th>
                    <th>Status</th>
                    <th>Requested On</th>
                    <th>Address</th>
                    <th>Zip</th>
                    <th>Phone</th>
                    <th>Actions</th>
                  </tr>
                </thead>
                <tbody>
                  {currentItems.map((req, index) => (
                    <tr key={req.id}>
                      <td>{indexOfFirst + index + 1}</td>
                      <td>{req.employeeName}</td>
                      <td>{req.assetName}</td>
                      <td>{req.categoryName}</td>
                      <td>{req.description}</td>
                      <td>{req.requestReason}</td>
                      <td>
                        <span className={`badge ${getStatusClass(req.status)}`}>
                          {getStatusIcon(req.status)} {req.status}
                        </span>
                      </td>
                      <td>{formatDate(req.requestDate)}</td>
                      <td>{req.fullAddress}</td>
                      <td>{req.zipCode}</td>
                      <td>{req.phone}</td>
                      <td>
                        {req.status === 'PENDING' && (
                          <>
                            <button className="btn btn-sm btn-success me-2" onClick={() => updateStatus(req.id, 'approve')}>
                              Approve
                            </button>
                            <button className="btn btn-sm btn-danger" onClick={() => updateStatus(req.id, 'reject')}>
                              Reject
                            </button>
                          </>
                        )}
                        {req.status === 'APPROVED' && (
                          <button className="btn btn-sm btn-primary" onClick={() => updateStatus(req.id, 'ship')}>
                            Ship
                          </button>
                        )}
                        {(req.status === 'RETURN_REQUEST' || req.status === 'DAMAGED') && (
                          <button className="btn btn-sm btn-dark" onClick={() => updateStatus(req.id, 'approve-return')}>
                            Approve Return
                          </button>
                        )}
                        {(req.status === 'RETURNED' || req.status === 'RESOLVED') && (
                          <button className="btn btn-sm btn-secondary" onClick={() => updateStatus(req.id, 'complete')}>
                            Completed
                          </button>
                        )}
                        {!['PENDING', 'APPROVED', 'RETURN_REQUEST', 'DAMAGED', 'RETURNED', 'RESOLVED'].includes(req.status) && (
                          <em>Completed</em>
                        )}
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>

            {/* Pagination */}
            <nav className="mt-3">
              <ul className="pagination justify-content-center">
                {Array.from({ length: Math.ceil(filteredRequests.length / itemsPerPage) }, (_, i) => (
                  <li className={`page-item ${currentPage === i + 1 ? 'active' : ''}`} key={i}>
                    <button className="page-link" onClick={() => setCurrentPage(i + 1)}>
                      {i + 1}
                    </button>
                  </li>
                ))}
              </ul>
            </nav>
          </>
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

function getStatusIcon(status) {
  switch ((status || '').toUpperCase()) {
    case 'PENDING':
      return <i className="fa fa-hourglass-half me-1"></i>;
    case 'APPROVED':
      return <i className="fa fa-check-circle me-1"></i>;
    case 'REJECTED':
      return <i className="fa fa-times-circle me-1"></i>;
    case 'SHIPPED':
      return <i className="fa fa-truck me-1"></i>;
    case 'IN_USE':
      return <i className="fa fa-laptop me-1"></i>;
    case 'RETURN_REQUEST':
      return <i className="fa fa-undo me-1"></i>;
    case 'DAMAGED':
      return <i className="fa fa-exclamation-triangle me-1"></i>;
    case 'RETURNED':
      return <i className="fa fa-box me-1"></i>;
    case 'COMPLETED':
    case 'RESOLVED':
      return <i className="fa fa-check-double me-1"></i>;
    default:
      return <i className="fa fa-question-circle me-1"></i>;
  }
}

function formatDate(dateStr) {
  return dateStr ? new Date(dateStr).toLocaleDateString() : '-';
}

export default AdminAssetRequests;
