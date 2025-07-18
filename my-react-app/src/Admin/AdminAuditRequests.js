
import { useState } from "react";
import { useEffect } from "react";
import AdminNavbar from "../navbar/AdminNavbar";

function AdminAssetAuditRequests() {
  const [audits, setAudits] = useState([]);
  const [msg, setMsg] = useState('');
  const [msgType, setMsgType] = useState('');
  const token = localStorage.getItem('token');

  useEffect(() => {
    fetchAuditRequests();
  }, []);

  // Existing fetch method
  const fetchAuditRequests = async () => {
    try {
      const res = await fetch('http://localhost:8081/api/asset-audits', {
        headers: { Authorization: `Bearer ${token}` },
      });
      if (!res.ok) throw new Error('Failed to fetch audit requests.PLEASE LOGIN');
      const data = await res.json();
      setAudits(data);
      setMsg('');
    } catch (err) {
      setMsg(err.message);
      setMsgType('error');
    }
  };

  // Existing update status method
  const updateStatus = async (auditId, status) => {
    // ... your existing code
  };

  // New method to trigger audit requests sending
  const triggerAuditRequests = async () => {
    try {
      const res = await fetch('http://localhost:8081/api/asset-audits/trigger', {
        method: 'POST',
        headers: { Authorization: `Bearer ${token}` },
      });

      const text = await res.text();

      if (res.ok) {
        setMsg(text);
        setMsgType('success');
        fetchAuditRequests(); // refresh the list after trigger
      } else {
        setMsg(`Failed to send audit requests: ${text}`);
        setMsgType('error');
      }
    } catch (err) {
      setMsg(`Error sending audit requests: ${err.message}`);
      setMsgType('error');
    }
  };

  // Format date helper
  const formatDate = (dateStr) => {
    if (!dateStr) return '-';
    return new Date(dateStr).toLocaleString();
  };

  return (
    <>
      <AdminNavbar />
      <div className="container mt-4">
        <div className="container mt-4">
        <div className="text-center mb-4">
          <h3 className="fw-bold text-dark" style={{ fontFamily: 'Verdana' }}>
            <i className="fa-solid fa-clipboard-list me-2"></i> Audit Requests
          </h3>
        </div>

        {/* Trigger button */}
        <button className="btn btn-primary mb-3" onClick={triggerAuditRequests}>
          Send Audit Requests to All Allocated Employees
        </button>

        {msg && (
          <div className={`alert ${msgType === 'success' ? 'alert-success' : 'alert-danger'}`}>
            {msg}
          </div>
        )}

        {audits.length === 0 ? (
          <div className="alert alert-info">No audit requests found.</div>
        ) : (
          <div className="table-responsive">
          <table className="table table-bordered table-striped mt-3">
            <thead className="table-dark">
              <tr>
                <th>#</th>
                <th>Employee</th>
                <th>Asset</th>
                <th>Remarks</th>
                <th>Status</th>
                <th>Audited On</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              {audits.map((audit, index) => (
                <tr key={audit.id}>
                  <td>{index + 1}</td>
                  <td>{audit.employeeName || '-'}</td>
                  <td>{audit.assetName || '-'}</td>
                  <td>{audit.remarks||'-'}</td>
                  <td>{audit.status}</td>
                  <td>{formatDate(audit.auditDate)}</td>
                  <td>
                    {audit.status === 'PENDING' ? (
                      <>
                        <button
                          className="btn btn-sm btn-success me-2"
                          onClick={() => updateStatus(audit.id, 'APPROVED')}
                        >
                          Approve
                        </button>
                        <button
                          className="btn btn-sm btn-danger"
                          onClick={() => updateStatus(audit.id, 'REJECTED')}
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
      </div>
    </>
  );
}

export default AdminAssetAuditRequests;
