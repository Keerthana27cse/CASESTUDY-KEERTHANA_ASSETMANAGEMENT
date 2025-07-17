import React, { useEffect, useState, useRef } from 'react';
import AdminNavbar from '../navbar/AdminNavbar';

function AssetComponent() {
  const [assets, setAssets] = useState([]);
  const [categories, setCategories] = useState([]);
  const [msg, setMsg] = useState('');
  const [errors, setErrors] = useState({});
  const [editingId, setEditingId] = useState(null);
  const [selectedCategory, setSelectedCategory] = useState('');
  const [searchTerm, setSearchTerm] = useState('');
  const [loading, setLoading] = useState(false);
  const fileInputRef = useRef(null);

  const [form, setForm] = useState({
    assetNo: '',
    assetName: '',
    assetModel: '',
    description: '',
    manufacturingDate: '',
    expiryDate: '',
    assetValue: '',
    assetStatus: 'AVAILABLE',
    categoryId: '',
    imageFile: null,
    imageUrl: '',
  });

  const fetchCategories = async () => {
    try {
      const res = await fetch('http://localhost:8081/api/categories', {
        headers: { Authorization: 'Bearer ' + localStorage.getItem('token') },
      });
      const data = await res.json();
      setCategories(data.sort((a, b) => a.id - b.id));
    } catch (err) {
      console.error('Error fetching categories:', err);
    }
  };

  const fetchAssets = async () => {
    setLoading(true);
    try {
      let url = 'http://localhost:8081/api/assets';
      if (selectedCategory) {
        url = `http://localhost:8081/api/assets/filter?categoryId=${selectedCategory}`;
      }
      const res = await fetch(url, {
        headers: { Authorization: 'Bearer ' + localStorage.getItem('token') },
      });
      const data = await res.json();
      setAssets(
        data.filter((a) =>
          a.assetName.toLowerCase().includes(searchTerm.toLowerCase())
        )
      );
    } catch (error) {
      console.error('Fetch error:', error);
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

  const handleChange = (e) => {
    const { name, value } = e.target;
    setForm((prev) => ({ ...prev, [name]: value }));
  };

  const handleFileChange = (e) => {
    setForm((prev) => ({ ...prev, imageFile: e.target.files[0] }));
  };

  const validate = () => {
    const errs = {};
    if (!form.assetNo.trim()) errs.assetNo = 'Asset No is required';
    if (!form.assetName.trim()) errs.assetName = 'Asset Name is required';
    if (!form.assetModel.trim()) errs.assetModel = 'Model is required';
    if (!form.manufacturingDate) errs.manufacturingDate = 'Manufacturing date is required';
    if (!form.expiryDate) errs.expiryDate = 'Expiry date is required';
    if (form.manufacturingDate && form.expiryDate) {
      const mDate = new Date(form.manufacturingDate);
      const eDate = new Date(form.expiryDate);
      if (mDate > eDate) errs.expiryDate = 'Expiry must be after manufacturing date';
    }
    if (!form.categoryId) errs.categoryId = 'Category must be selected';
    if (!form.assetValue || parseFloat(form.assetValue) <= 0) errs.assetValue = 'Asset value must be a positive number';
    if(!form.imageFile) errs.imageFile="img required";
    return errs;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    const validationErrors = validate();
    if (Object.keys(validationErrors).length > 0) {
      setErrors(validationErrors);
      return;
    }
    setErrors({});

    const formData = new FormData();
    const assetData = { ...form };
    delete assetData.imageFile;

    formData.append('asset', new Blob([JSON.stringify(assetData)], { type: 'application/json' }));
    if (form.imageFile) {
      formData.append('imageFile', form.imageFile);
    }

    try {
      const res = await fetch('http://localhost:8081/api/assets/save', {
        method: 'POST',
        headers: { Authorization: 'Bearer ' + localStorage.getItem('token') },
        body: formData,
      });


      const contentType = res.headers.get('content-type');
      const data =
        contentType && contentType.includes('application/json')
          ? await res.json()
          : { message: await res.text() };

      if (res.ok) {
        setMsg(editingId ? 'Asset updated' : 'Asset added');
        setEditingId(null);
        setForm({
          assetNo: '',
          assetName: '',
          assetModel: '',
          description: '',
          manufacturingDate: '',
          expiryDate: '',
          assetValue: '',
          assetStatus: 'AVAILABLE',
          categoryId: '',
          imageFile: null,
          imageUrl: '',
        });
        fileInputRef.current.value = ''; //clear inputs
        fetchAssets();
        document.querySelector('.card').scrollIntoView({ behavior: 'smooth' });
      } else {
        setMsg(data.message||'Operation failed');
      }
    } catch (err) {
      console.error('Submit error:', err);
      setMsg('Operation failed');
    }
  };

  const handleEdit = (asset) => {
    setEditingId(asset.id);
    setForm({
      id: asset.id,
      assetNo: asset.assetNo,
      assetName: asset.assetName,
      assetModel: asset.assetModel,
      description: asset.description,
      manufacturingDate: asset.manufacturingDate,
      expiryDate: asset.expiryDate,
      assetValue: asset.assetValue,
      assetStatus: asset.assetStatus,
      categoryId: asset.category?.id || '',
      imageFile: null,
      imageUrl: asset.imageUrl || '',
    });

    window.scrollTo({top:0,behavior: 'smooth'});
  };

  const handleDelete = async (id) => {
    if (window.confirm('Delete this asset?')) {
      try {
        const res = await fetch(`http://localhost:8081/api/assets/${id}`, {
          method: 'DELETE',
          headers: {
            Authorization: 'Bearer ' + localStorage.getItem('token'),
          },
        });
        if (res.ok) {
          setMsg('Asset deleted successfully.');
          fetchAssets();
        } else {
          const errText = await res.text();
          setMsg('Deletion failed: ' + errText);
        }
      } catch (error) {
        console.error('Delete error:', error);
        setMsg('Deletion failed.');
      }
    }
  };

  const inputClass = (field) => `form-control ${errors[field] ? 'is-invalid' : ''}`;

  return (
    <>

    <AdminNavbar/>
<div className="container mt-4">
<div className="text-center mb-4">
<h3 style={{ fontFamily: 'Verdana, Geneva, sans-serif', fontWeight: '600' }}>
<i className="fa-solid fa-box-open me-2"></i> Asset Catalogue
</h3>     
<h1> 
  <br></br>
  </h1> 

      {msg && <div className="alert alert-info">{msg}</div>}

      {/* Form */}
      <div className="card mb-4">
        <div className="card-header bg-success text-white">
          {editingId ? 'Edit Asset' : 'Add Asset'}
        </div>
        <div className="card-body">
        <form onSubmit={handleSubmit} className="row g-3" encType="multipart/form-data" style={{ fontFamily: 'Poppins, sans-serif' }}>
  {[
    { label: 'Asset No', name: 'assetNo' },
    { label: 'Asset Name', name: 'assetName' },
    { label: 'Model', name: 'assetModel' },
  ].map((field, i) => (
    <div className="col-md-6" key={i}>
      <label className="form-label fw-semibold">{field.label}</label>
      <input
        name={field.name}
        className={inputClass(field.name)}
        value={form[field.name]}
        onChange={handleChange}
        required
      />
      {errors[field.name] && <div className="invalid-feedback">{errors[field.name]}</div>}
    </div>
  ))}

  <div className="col-md-6">
    <label className="form-label fw-semibold">Description</label>
    <textarea
      name="description"
      className={inputClass('description')}
      value={form.description}
      onChange={handleChange}
      rows="2"
      required
    />
    {errors.description && <div className="invalid-feedback">{errors.description}</div>}
  </div>

  <div className="col-md-4">
    <label className="form-label fw-semibold">Manufacturing Date</label>
    <input
      type="date"
      name="manufacturingDate"
      className={inputClass('manufacturingDate')}
      value={form.manufacturingDate}
      onChange={handleChange}
      required
    />
    {errors.manufacturingDate && <div className="invalid-feedback">{errors.manufacturingDate}</div>}
  </div>

  <div className="col-md-4">
    <label className="form-label fw-semibold">Expiry Date</label>
    <input
      type="date"
      name="expiryDate"
      className={inputClass('expiryDate')}
      value={form.expiryDate}
      onChange={handleChange}
      required
    />
    {errors.expiryDate && <div className="invalid-feedback">{errors.expiryDate}</div>}
  </div>

  <div className="col-md-4">
    <label className="form-label fw-semibold">Value (₹)</label>
    <input
      type="number"
      name="assetValue"
      className={inputClass('assetValue')}
      value={form.assetValue}
      onChange={handleChange}
      required
      min="1"
    />
    {errors.assetValue && <div className="invalid-feedback">{errors.assetValue}</div>}
  </div>

  <div className="col-md-6">
    <label className="form-label fw-semibold">Status</label>
    <select name="assetStatus" className="form-select" value={form.assetStatus} onChange={handleChange} required>
      {['AVAILABLE', 'ALLOCATED', 'IN_USE', 'UNDER_MAINTENANCE', 'DAMAGED', 'RETURNED', 'DISCARDED', 'WORKING'].map((status) => (
        <option key={status} value={status}>{status}</option>
      ))}
    </select>
  </div>

  <div className="col-md-6">
    <label className="form-label fw-semibold">Category</label>
    <select name="categoryId" className={inputClass('categoryId')} value={form.categoryId} onChange={handleChange} required>
      <option value="">-- Select Category --</option>
      {categories.map((cat) => (
        <option key={cat.id} value={cat.id}>{cat.categoryName}</option>
      ))}
    </select>
    {errors.categoryId && <div className="invalid-feedback">{errors.categoryId}</div>}
  </div>

  <div className="col-md-6">
    <label className="form-label fw-semibold">Asset Image</label>
    <input
      ref={fileInputRef}
      type="file"
      name="imageFile"
      accept="image/*"
      className={inputClass('imageFile')}
      onChange={handleFileChange}
      required
    />
    {errors.imageFile && <div className="invalid-feedback">{errors.imageFile}</div>}
    {form.imageUrl && (
      <div className="mt-2">
        <img
          src={`${form.imageUrl}`}
          alt="Current Asset"
          style={{ maxHeight: '60px' }}
          className="img-thumbnail"
        />
      </div>
    )}
  </div>

  <div className="text-end mt-3">
    <button type="submit" className="btn btn-success px-4 py-2 fw-semibold shadow-sm">
      {editingId ? 'Update Asset' : 'Save Asset'}
    </button>
  </div>
</form>

        </div>
      </div>

      {/* Filters */}
      <div className="row mb-3">
        <div className="col-md-4">
          <select className="form-select" value={selectedCategory} onChange={(e) => setSelectedCategory(e.target.value)}>
            <option value="">-- All Categories --</option>
            {categories.map((cat) => (
              <option key={cat.id} value={cat.id}>{cat.categoryName}</option>
            ))}
          </select>
        </div>
        <div className="col-md-4">
          <input type="text" className="form-control" placeholder="Search by asset name..." value={searchTerm} onChange={(e) => setSearchTerm(e.target.value)} />
        </div>
      </div>

      {/* Asset Display */}
      {loading ? (
        <div className="text-center">
          <div className="spinner-border text-primary" />
        </div>
      ) : (
        <div className="row row-cols-1 row-cols-md-2 row-cols-lg-3 g-4">
          {assets.length > 0 ? (
            assets.map((asset) => (
              <div className="col" key={asset.id}>
                <div className="card h-100 shadow-sm">
                  {asset.imageUrl ? (
                  <img
                    src={asset.imageUrl}                    
                    className="card-img-top"
                    alt="Asset"
                    style={{ maxHeight: '500px', objectFit: 'cover' }}
                  />
    
                  ) : (
                    <div className="d-flex justify-content-center align-items-center bg-light" style={{ height: '200px' }}>
                      <span className="text-muted">No Image Available</span>
                    </div>
                  )}
                  <div className="card-body d-flex flex-column">
                    <h5 className="card-title text-primary">{asset.assetName}</h5>
                    <h6 className="card-subtitle mb-2 text-muted">{asset.assetModel}</h6>
                    <ul className="list-group list-group-flush flex-grow-1">
                      <li className="list-group-item"><strong>Asset No:</strong> {asset.assetNo}</li>
                      <li className="list-group-item"><strong>Category:</strong> {asset.categoryName || 'N/A'}</li>
                      <li className="list-group-item"><strong>Status:</strong> <span className={`badge ${asset.assetStatus === 'AVAILABLE' ? 'bg-success' : 'bg-warning'}`}>{asset.assetStatus}</span></li>
                      <li className="list-group-item"><strong>Value:</strong> ₹{asset.assetValue}</li>
                      {asset.description && <li className="list-group-item"><strong>Description:</strong> {asset.description}</li>}
                      <li className="list-group-item"><strong>Manufactured:</strong> {asset.manufacturingDate}</li>
                      <li className="list-group-item"><strong>Expiry:</strong> {asset.expiryDate}</li>
                      <li className="list-group-item"><strong>Created:</strong> {asset.createdAt ? new Date(asset.createdAt).toLocaleString() : 'N/A'}</li>
                      <li className="list-group-item"><strong>Updated:</strong> {asset.updatedAt ? new Date(asset.updatedAt).toLocaleString() : 'N/A'}</li>
                    </ul>
                  </div>
                  <div className="card-footer d-flex justify-content-end">
                    <button className="btn btn-sm btn-warning me-2" onClick={() => handleEdit(asset)}>Edit</button>
                    <button className="btn btn-sm btn-danger" onClick={() => handleDelete(asset.id)}>Delete</button>
                  </div>
                </div>
              </div>
            ))
          ) : (
            <div className="col-12">
              <div className="alert alert-info text-center">No assets found.</div>
            </div>
          )}
        </div>
      )}
    </div>
    </div>
        </>

  );
}

export default AssetComponent;
