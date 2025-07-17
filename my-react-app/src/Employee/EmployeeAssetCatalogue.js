// src/components/EmployeeAssetCatalogue.js
import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import EmployeeNavbar from '../navbar/EmployeeNavbar';

function EmployeeAssetCatalogue() {
  const [assets, setAssets] = useState([]);
  const [categories, setCategories] = useState([]);
  const [selectedCategory, setSelectedCategory] = useState('');
  const [searchTerm, setSearchTerm] = useState('');
  const [loading, setLoading] = useState(false);

  const token = localStorage.getItem('token');
  const navigate = useNavigate();

  const fetchCategories = async () => {
    try {
      const res = await fetch('http://localhost:8081/api/categories', {
        headers: { Authorization: 'Bearer ' + token },
      });
      const data = await res.json();
      setCategories(data);
    } catch (err) {
      console.error('Failed to load categories:', err);
    }
  };

  const fetchAssets = async () => {
    setLoading(true);
    let url = 'http://localhost:8081/api/assets';
    if (selectedCategory) {
      url = `http://localhost:8081/api/assets/filter?categoryId=${selectedCategory}`;
    }
    try {
      const res = await fetch(url, {
        headers: { Authorization: 'Bearer ' + token },
      });
      const data = await res.json();
      setAssets(
        data.filter((a) =>
          a.assetName.toLowerCase().includes(searchTerm.toLowerCase())
        )
      );
    } catch (err) {
      console.error('Failed to load assets:', err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchCategories();
  }, []);

  useEffect(() => {
    fetchAssets();
  }, [selectedCategory, searchTerm]);

  return (
    <>
      <EmployeeNavbar />
      <div className="container mt-4">
        <h2>Browse Assets</h2>

        <div className="row mb-3">
          <div className="col-md-4">
            <select
              className="form-select"
              value={selectedCategory}
              onChange={(e) => setSelectedCategory(e.target.value)}
            >
              <option value="">All Categories</option>
              {categories.map((cat) => (
                <option key={cat.id} value={cat.id}>
                  {cat.categoryName}
                </option>
              ))}
            </select>
          </div>
          <div className="col-md-4">
            <input
              type="text"
              className="form-control"
              placeholder="Search assets..."
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
            />
          </div>
        </div>

        {loading ? (
          <div className="text-center"><div className="spinner-border" /></div>
        ) : (
          <div className="row">
            {assets.length > 0 ? (
              assets.map((asset) => (
                <div className="col-md-4 mb-4" key={asset.id}>
                  <div className="card h-100 shadow-sm">
                    {asset.imageUrl ? (
                      <img
                        src={asset.imageUrl}
                        className="card-img-top"
                        alt="Asset"
                        style={{ maxHeight: '500px', objectFit: 'cover' }}
                      />
                    ) : (
                      <div className="d-flex justify-content-center align-items-center bg-light" style={{ height: '250px' }}>
                        <span className="text-muted">No Image</span>
                      </div>
                    )}
                    <div className="card-body">
                      <h5 className="card-title">{asset.assetName}</h5>
                      <p className="card-text">Model: {asset.assetModel}</p>
                      <p className="card-text">Status: {asset.assetStatus}</p>
                      <p className="fw-bold">₹{asset.assetValue}</p>
                      <div className="d-grid gap-2">
                        <button
                          className="btn btn-outline-primary"
                          onClick={() => navigate(`/employee/assets/${asset.id}`)}
                        >
                          View Details
                        </button>
                      </div>
                    </div>
                  </div>
                </div>
              ))
            ) : (
              <div className="alert alert-info">No assets found.</div>
            )}
          </div>
        )}
      </div>
    </>
  );
}

export default EmployeeAssetCatalogue;
