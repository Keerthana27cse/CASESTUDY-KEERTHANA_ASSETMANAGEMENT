import { useEffect, useState } from 'react';
import EmployeeNavbar from '../navbar/EmployeeNavbar';
import useNavigation from '../hooks/navigation';
import useAssetDetails from '../hooks/useAssetDetails';
import { useParams } from 'react-router-dom';
import { jsPDF } from 'jspdf';
import axios from 'axios';
import PDF from './PDF';



function AssetRequestForm() {
  const { assetId } = useParams();
  const token = localStorage.getItem('token');
  const navigate = useNavigation();
  const { assetName, categoryName, categoryId: loadedCategoryId } = useAssetDetails(assetId, token);

  const [form, setForm] = useState({
    assetId: assetId || '',
    categoryId: '',
    assetName: '',
    categoryName: '',
    description: '',
    requestReason: '',
    fullAddress: '',
    zipCode: '',
    phone: '',
  });

  const [submitting, setSubmitting] = useState(false);
  const [msg, setMsg] = useState('');
  const [msgType, setMsgType] = useState('');

  useEffect(() => {
    if (assetName && categoryName && loadedCategoryId) {
      setForm((prev) => ({
        ...prev,
        assetName,
        categoryName,
        categoryId: loadedCategoryId,
      }));
    }
  }, [assetName, categoryName, loadedCategoryId]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setForm((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (e) => {
  e.preventDefault();
  setSubmitting(true);
  setMsg('');
  setMsgType('');

  try {
    const res = await axios.post(
      'http://localhost:8081/api/asset-requests/submit',
      form,
      {
        headers: {
          'Content-Type': 'application/json',
          Authorization: `Bearer ${token}`,
        },
      }
    );

    // After successful submission, generate and send PDF
    const doc = PDF(form);
    const pdfBlob = doc.output('blob');
    const emailToSend = 'keerthii2728@gmail.com';

    const formData = new FormData();
    formData.append('file', pdfBlob, 'Asset_Request.pdf');
    formData.append('email', emailToSend);

    const emailRes = await fetch('http://localhost:8081/api/send-pdf-email', {
      method: 'POST',
      headers: {
        Authorization: 'Bearer ' + token,
      },
      body: formData,
    });

    if (!emailRes.ok) {
      console.warn('PDF submitted but failed to send email.');
    }

    setMsg('Asset request submitted and PDF sent successfully!');
    setMsgType('success');

    setTimeout(() => {
      navigate('/employee/my-asset-requests');
    }, 5000);
  } catch (err) {
    const errorMsg = err.response?.data?.message || 'Submission failed. Try again.';
    setMsg(errorMsg);
    setMsgType('error');
    console.error('Submission error:', err);
  } finally {
    setSubmitting(false);
  }
};

  return (
    <>
      <EmployeeNavbar />
      <div className="container mt-5">
        <h2 className="text-center fw-bold mb-4 text-primary">Asset Request Form</h2>

        {msg && (
          <div
            className={`alert ${
              msgType === 'success' ? 'alert-success' : 'alert-danger'
            } text-center`}
            role="alert"
          >
            {msg}
          </div>
        )}

        <div className="card shadow-lg p-4 mb-5 bg-white rounded">
          <div className="card-body">
            <form onSubmit={handleSubmit}>
              <div className="row mb-3">
                <div className="col-md-6">
                  <label htmlFor="assetName" className="form-label fw-semibold">
                    Asset Name
                  </label>
                  <input
                    type="text"
                    id="assetName"
                    className="form-control form-control-lg"
                    value={form.assetName}
                    readOnly
                  />
                </div>
                <div className="col-md-6">
                  <label htmlFor="categoryName" className="form-label fw-semibold">
                    Category Name
                  </label>
                  <input
                    type="text"
                    id="categoryName"
                    className="form-control form-control-lg"
                    value={form.categoryName}
                    readOnly
                  />
                </div>
              </div>

              <div className="mb-3">
                <label htmlFor="description" className="form-label fw-semibold">
                  Description
                </label>
                <textarea
                  id="description"
                  name="description"
                  value={form.description}
                  onChange={handleChange}
                  className="form-control"
                  rows="3"
                  required
                  placeholder="Provide a detailed description of the asset you are requesting."
                />
              </div>

              <div className="mb-3">
                <label htmlFor="requestReason" className="form-label fw-semibold">
                  Reason for Request
                </label>
                <input
                  type="text"
                  id="requestReason"
                  name="requestReason"
                  value={form.requestReason}
                  onChange={handleChange}
                  className="form-control"
                  required
                  placeholder="Explain why you need this asset."
                />
              </div>

              <div className="mb-3">
                <label htmlFor="fullAddress" className="form-label fw-semibold">
                  Shipping Address
                </label>
                <textarea
                  id="fullAddress"
                  name="fullAddress"
                  value={form.fullAddress}
                  onChange={handleChange}
                  className="form-control"
                  rows="3"
                  required
                  placeholder="Enter your full address, including street, city, state, and country."
                />
              </div>

              <div className="row mb-4">
                <div className="col-md-6">
                  <label htmlFor="zipCode" className="form-label fw-semibold">
                    Zip Code
                  </label>
                  <input
                    type="text"
                    id="zipCode"
                    name="zipCode"
                    value={form.zipCode}
                    onChange={handleChange}
                    className="form-control"
                    required
                    pattern="[0-9]{5,10}"
                    title="Enter a valid zip code (5-10 digits)."
                  />
                </div>
                <div className="col-md-6">
                  <label htmlFor="phone" className="form-label fw-semibold">
                    Phone Number
                  </label>
                  <input
                    type="tel"
                    id="phone"
                    name="phone"
                    value={form.phone}
                    onChange={handleChange}
                    className="form-control"
                    required
                    pattern="[0-9]{10}"
                    title="Enter a 10-digit phone number."
                  />
                </div>
              </div>

              <div className="d-grid gap-2">
                <button type="submit" className="btn btn-primary btn-lg" disabled={submitting}>
                  {submitting ? (
                    <>
                      <span className="spinner-border spinner-border-sm me-2" role="status" aria-hidden="true"></span>
                      Submitting...
                    </>
                  ) : (
                    'Submit Request'
                  )}
                </button>

                

                
              </div>
            </form>
          </div>
        </div>
      </div>
    </>
  );
}

export default AssetRequestForm;
