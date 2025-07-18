import React from 'react';
import useNavigation from '../hooks/navigation';
import { useEffect } from 'react';

function AdminNavbar() {
  const handleNavigate = useNavigation();

 // ✅ Set background color when admin navbar is mounted
  useEffect(() => {
    document.body.style.backgroundColor = '#a2cdcfff'; // light teal
    return () => {
      document.body.style.backgroundColor = ''; // reset when unmounted
    };
  }, []);



  const logout = () => {
    localStorage.removeItem('token');
    localStorage.removeItem('role');
    handleNavigate('/login');
  };

  const navItems = [
    { path: '/admin-dashboard', label: 'Home' },
    { path: '/admin/categories', label: 'Asset Categories' },
    { path: '/admin/assets', label: 'Manage Assets' },
    { path: '/admin/EmployeeList', label: 'Manage Employees' },
    { path: '/admin/asset-requests', label: 'Asset Requests' },
    { path: '/admin/service-requests', label: 'Service Requests' },
    { path: '/admin/asset-summary', label: 'Asset Allocation' },
    { path: '/admin/asset-audits', label: 'Asset Audits' },
    { path: '/admin/settings', label: 'Settings' },
  ];

  return (
<nav className="navbar navbar-expand-lg navbar-dark shadow-sm"
     style={{ backgroundColor: '#307e75ff' }}>      <div className="container-fluid">
        {/* Back button */}
        <button className="btn btn-outline-light me-3 rounded-pill px-3 py-1" onClick={() => handleNavigate(-1)}>
          <i className="bi bi-arrow-left me-1"></i>
        </button>

        {/* Logo */}
        <button className="navbar-brand btn btn-link text-white fs-4 fw-bold" onClick={() => handleNavigate('/admin-dashboard')}>
          <i className="bi bi-box-seam me-2"></i>HexaAsset
        </button>

        {/* Toggler */}
        <button
          className="navbar-toggler"
          type="button"
          data-bs-toggle="collapse"
          data-bs-target="#adminNavbar"
          aria-controls="adminNavbar"
          aria-expanded="false"
          aria-label="Toggle navigation"
        >
          <span className="navbar-toggler-icon"></span>
        </button>

        {/* Navigation links */}
        <div className="collapse navbar-collapse" id="adminNavbar">
          <ul className="navbar-nav me-auto mb-2 mb-lg-0">
            {navItems.map((item, index) => (
              <li className="nav-item" key={index}>
                <button
                  className="nav-link btn btn-link text-white fw-medium"
                  onClick={() => handleNavigate(item.path)}
                >
                  {item.label}
                </button>
              </li>
            ))}
          </ul>

          {/* Logout button */}
          <ul className="navbar-nav ms-auto">
            <li className="nav-item">
              <button className="btn btn-light text-primary fw-bold px-3" onClick={logout}>
                <i className="bi bi-box-arrow-right me-2"></i>Logout
              </button>
            </li>
          </ul>
        </div>
      </div>
    </nav>
  );
}

export default AdminNavbar;
