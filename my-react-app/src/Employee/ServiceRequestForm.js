import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import EmployeeNavbar from '../navbar/EmployeeNavbar';
import useNavigation from '../hooks/navigation';
import useAssetDetails from '../hooks/useAssetDetails';

function ServiceRequestForm() {
  const { assetId, categoryId } = useParams();
  const token = localStorage.getItem('token');
  const navigate = useNavigation();

  const { assetName, categoryName } = useAssetDetails(assetId, token);

  const [form, setForm] = useState({
    assetId: assetId || '',
    categoryId: categoryId || '',
    assetName: '',
    categoryName: '',
    issueType: '',
    description: '',
  });

  const [submitting, setSubmitting] = useState(false);
  const [msg, setMsg] = useState('');
  const [msgType, setMsgType] = useState('');

  useEffect(() => {
    setForm((prev) => ({...prev,assetName,categoryName,
    }));
  }, [assetName, categoryName]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setForm((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (submitting) return;
    setSubmitting(true);
    setMsg('');
    setMsgType('');

    try {
      const res = await fetch('http://localhost:8081/api/service-requests/submit', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify(form),
      });

      let data;
      try {
        data = await res.json();
      } catch {
        data = { message: 'No detailed message from server.' };
      }

      if (res.ok) {
        setMsg('Service request submitted successfully.');
        setMsgType('success');
        setTimeout(() => navigate('/employee/my-service-requests'), 3000);
      } else {
        setMsg(data.message || 'Failed to submit service request.');
        setMsgType('error');
      }
    } catch (err) {
      setMsg('Error while submitting request.');
      setMsgType('error');
      console.error(err);
    }

    setSubmitting(false);
  };

  return (
    <>
      <EmployeeNavbar />
      <div className="container mt-4">
        <h3>Raise Service Request</h3>

        {msg && (
          <div className={`alert alert-${msgType === 'success' ? 'success' : 'danger'}`}>
            {msg}
          </div>
        )}

        <form onSubmit={handleSubmit}>
          <div className="mb-3">
            <label>Asset Name</label>
            <input type="text" className="form-control" value={form.assetName} readOnly />
          </div>

          <div className="mb-3">
            <label>Category Name</label>
            <input type="text" className="form-control" value={form.categoryName} readOnly />
          </div>

          <div className="mb-3">
            <label>Issue Type</label>
            <select
              name="issueType"
              value={form.issueType}
              onChange={handleChange}
              className="form-control"
              required
            >
              <option value="">-- Select --</option>
              <option value="REPAIR">Repair</option>
              <option value="REPLACE">Replace</option>
              <option value="UPGRADE">Upgrade</option>
            </select>
          </div>

          <div className="mb-3">
            <label>Description</label>
            <textarea
              name="description"
              value={form.description}
              onChange={handleChange}
              className="form-control"
              required
            />
          </div>

          <button type="submit" className="btn btn-primary" disabled={submitting}>
            {submitting ? 'Submitting...' : 'Submit Request'}
          </button>
        </form>
      </div>
    </>
  );
}

export default ServiceRequestForm;
