import React, { useEffect, useState } from 'react';
import EmployeeNavbar from '../navbar/EmployeeNavbar';

function EmployeeAssetAuditRequests() {
  const [audits, setAudits] = useState([]);
  const [msg, setMsg] = useState('');
  const [msgType, setMsgType] = useState('');
  const token = localStorage.getItem('token');

  useEffect(() => {
    fetchMyAudits();
  }, []);

  const fetchMyAudits = async () => {
    try {
      const res = await fetch('http://localhost:8081/api/asset-audits/my-requests', {
        headers: { Authorization: `Bearer ${token}` },
      });
      if (!res.ok) throw new Error('Failed to fetch your audit requests.PLEASE LOGIN');
      const data = await res.json();
      setAudits(data);
      setMsg('');
    } catch (err) {
      setMsg(err.message);
      setMsgType('error');
    }
  };

  const submitRemarks = async (auditId, remarks) => {
    if (!remarks.trim()) {
      setMsg('Remarks cannot be empty.');
      setMsgType('error');
      return;
    }

    try {
      const res = await fetch(
        `http://localhost:8081/api/asset-audits/${auditId}/remarks?remarks=${encodeURIComponent(remarks)}`,
        {
          method: 'PUT',
          headers: { Authorization: `Bearer ${token}` },
        }
      );
      const text = await res.text();

      if (res.ok) {
        setMsg('Remarks submitted successfully.');
        setMsgType('success');
        fetchMyAudits();
      } else {
        setMsg(`Failed to submit remarks: ${text}`);
        setMsgType('error');
      }
    } catch (err) {
      setMsg(`Error submitting remarks: ${err.message}`);
      setMsgType('error');
    }
  };

  const [remarksInput, setRemarksInput] = useState({});

  const handleRemarksChange = (auditId, value) => {
    setRemarksInput((prev) => ({ ...prev, [auditId]: value }));
  };

  const formatDate = (dateStr) => {
    if (!dateStr) return '-';
    return new Date(dateStr).toLocaleString();
  };

  return (
    <>
      <EmployeeNavbar />
      <div className="container mt-4">
        <div className="container mt-4">
        <div className="text-center mb-4">
          <h3 className="fw-bold text-dark" style={{ fontFamily: 'Verdana' }}>
            <i className="fa-solid fa-clipboard-list me-2"></i> My Audits
          </h3>
        </div>

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
                <th>Asset</th>
                <th>Status</th>
                <th>Requested On</th>
                <th>Remarks</th>
                <th>Submit Remarks</th>
              </tr>
            </thead>
            <tbody>
              {audits.map((audit, idx) => (
                <tr key={audit.id}>
                  <td>{idx + 1}</td>
                  <td>{audit.assetName || '-'}</td>
                  <td>{audit.status}</td>
                  <td>{formatDate(audit.auditDate)}</td>
                  <td>{audit.remarks || '-'}</td>
                  <td>
                    {/* Only show input if status allows remarks submission */}
                    {audit.status === 'PENDING' || audit.status === 'APPROVED' ? (
                      <>
                        <textarea
                          rows="2"
                          className="form-control mb-1"
                          value={remarksInput[audit.id] || ''}
                          onChange={(e) => handleRemarksChange(audit.id, e.target.value)}
                          placeholder="Enter remarks..."
                        />
                        <button
                          className="btn btn-sm btn-primary"
                          onClick={() => submitRemarks(audit.id, remarksInput[audit.id] || '')}
                        >
                          Submit
                        </button>
                      </>
                    ) : (
                      <em>Remarks locked</em>
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

export default EmployeeAssetAuditRequests;
