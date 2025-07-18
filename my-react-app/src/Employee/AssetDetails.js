// src/components/AssetDetails.js
import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';

function AssetDetails() {
  const { id } = useParams();
  const navigate = useNavigate();
  const [asset, setAsset] = useState(null);
  const [loading, setLoading] = useState(true);
  const [msg, setMsg] = useState('');
  const token = localStorage.getItem('token');
  
  const handleRequest = () => {
  navigate(`/employee/asset-request/${asset.id}/${asset.categoryId}`);
};

const handleServiceRequest = () => {
  navigate(`/employee/service-request/${asset.id}/${asset.categoryId}`);
};


  useEffect(() => {
    const fetchAsset = async () => {
      try {
        const res = await fetch(`http://localhost:8081/api/assets/${id}`, {
          headers: { Authorization: `Bearer ${token}` },
        });
      const contentType = res.headers.get("content-type");

    // Try to parse response body (even if error)
       const data = contentType && contentType.includes("application/json")
      ? await res.json()
      : { message: await res.text() };

      if (res.ok) {
          console.log("Fetched asset:", data);
          setAsset(data);
        } else {
          const errMsg = await res.text();
          console.error("Error response:", errMsg);
          setAsset(null);
        }
      } catch (err) {
        console.error('Error fetching asset details:', err);
        setAsset(null);
      } finally {
        setLoading(false);
      }
    };

    fetchAsset();
  }, [id, token]);

  if (!token) return <div className="alert alert-warning mt-4">Please login to view asset details.</div>;
  if (loading) return <div className="text-center mt-4">Loading...</div>;
  if (!asset) return <div className="alert alert-danger mt-4">Asset not found.</div>;

  return (
    <div className="container mt-4">
      <button className="btn btn-secondary mb-3" onClick={() => navigate('/employee/assets')}>
        &larr; Back to Catalogue
      </button>

      <h2>{asset.assetName}</h2>
      {msg && <div className="alert alert-info">{msg}</div>}

      <div className="row">
        <div className="col-md-6">
          <img
            src={asset.imageUrl}
            alt="Asset"
            className="img-fluid rounded shadow"
            style={{ maxHeight: '400px', objectFit: 'contain' }}
          />
        </div>
        <div className="col-md-6">
          <ul className="list-group">
            <li className="list-group-item"><strong>Model:</strong> {asset.assetModel}</li>
            <li className="list-group-item"><strong>Value:</strong> ₹{asset.assetValue}</li>
            <li className="list-group-item"><strong>Status:</strong> {asset.assetStatus}</li>
            <li className="list-group-item"><strong>Category:</strong> {asset.categoryName || 'N/A'}</li>
            <li className="list-group-item"><strong>Manufactured On:</strong> {asset.manufacturingDate}</li>
            <li className="list-group-item"><strong>Expires On:</strong> {asset.expiryDate}</li>
          </ul>
          <div className="mt-3 d-flex gap-3">
          <button className="btn btn-primary" onClick={handleRequest}>
            Request Asset
          </button>

          <button className="btn btn-warning" onClick={handleServiceRequest}>
            Request Service
          </button>
          </div>
        </div>
      </div>
    </div>
  );
}

export default AssetDetails;
