// src/components/CategoryComponent.js
import React, { useEffect, useState } from 'react';
import AdminNavbar from '../navbar/AdminNavbar';
function CategoryComponent() {
  const [categories, setCategories] = useState([]);
  const [categoryName, setCategoryName] = useState('');
  const [editingId, setEditingId] = useState(null);
  const [msg, setMsg] = useState('');

  const token = localStorage.getItem('token');

  const fetchCategories = async () => {
    try {
      const res = await fetch('http://localhost:8081/api/categories', {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });

      if (res.ok) {
        const data = await res.json();
        setCategories(data.sort((a,b)=>a.id-b.id));
      } else {
        setMsg('Unauthorized or failed to fetch categories.');
        console.error('Fetch failed with status:', res.status);
      }
    } catch (error) {
      console.error('Error:', error);
      setMsg('Server error while fetching categories.');
    }
  };

  const handleSave = async (e) => {
    e.preventDefault();
    if (!categoryName) {
      setMsg('Category name is required');
      return;
    }

    const method = editingId ? 'PUT' : 'POST';
    const url = editingId
      ? `http://localhost:8081/api/categories/${editingId}`
      : 'http://localhost:8081/api/categories/save';

    try {
      const res = await fetch(url, {
        method,
        headers: {
          'Content-Type': 'application/json',
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify({ categoryName }),
      });

      if (res.ok) {
        setMsg(editingId ? 'Category updated' : 'Category added');
        setCategoryName('');
        setEditingId(null);
        fetchCategories();
      } else {
        setMsg('Operation failed: Unauthorized or server error.');
        console.error('Save failed with status:', res.status);
      }
    } catch (error) {
      console.error('Error:', error);
      setMsg('Error while saving category.');
    }
  };

  const handleEdit = (cat) => {
    setCategoryName(cat.categoryName);
    setEditingId(cat.id);
  };

  const handleDelete = async (id) => {
    if (!window.confirm('Are you sure you want to delete this category?')) return;

    try {
      const res = await fetch(`http://localhost:8081/api/categories/${id}`, {
        method: 'DELETE',
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });

      if (res.ok) {
        setMsg('Category deleted');
        fetchCategories();
      } else {
        setMsg('Delete failed: Unauthorized or server error.');
        console.error('Delete failed with status:', res.status);
      }
    } catch (error) {
      console.error('Error deleting category:', error);
      setMsg('Server error while deleting category.');
    }
  };

  useEffect(() => {
    fetchCategories();
  }, []);

  return (
    <>
    <AdminNavbar/>
    <div className="container mt-4">
<div className="text-center mb-4">
<h3 style={{ fontFamily: 'Verdana, Geneva, sans-serif', fontWeight: '600' }}>
  <i className="fa fa-folder-open me-2"></i> MANAGE CATEGORIES
</h3>     
<h1> 
  <br></br>
  </h1> 


{msg && <div className="alert alert-info">{msg}</div>}

      <form onSubmit={handleSave} className="row g-3 mb-4">
        <div className="col-md-6">
          <input
            type="text"
            className="form-control"
            placeholder="Enter category name"
            value={categoryName}
            onChange={(e) => setCategoryName(e.target.value)}
          />
        </div>
        <div className="col-md-3">
          <button type="submit" className="btn btn-primary">
            {editingId ? 'Update' : 'Add'} Category
          </button>
        </div>
        {editingId && (
          <div className="col-md-3">
            <button
              type="button"
              className="btn btn-secondary"
              onClick={() => {
                setCategoryName('');
                setEditingId(null);
              }}
            >
              Cancel
            </button>
          </div>
        )}
      </form>

      <table className="table table-bordered">
        <thead>
          <tr>
            <th>Category ID</th>
            <th>Category Name</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {categories.map((cat) => (
            <tr key={cat.id}>
              <td>{cat.id}</td>
              <td>{cat.categoryName}</td>
              <td>
                <button
                  className="btn btn-sm btn-warning me-2"
                  onClick={() => handleEdit(cat)}
                >
                  Edit
                </button>
                <button
                  className="btn btn-sm btn-danger"
                  onClick={() => handleDelete(cat.id)}
                >
                  Delete
                </button>
              </td>
            </tr>
          ))}
          
        </tbody>
        
      </table>
    </div>
    </div>
        </>

  );
}

export default CategoryComponent;
