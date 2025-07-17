// src/components/LoginNavbar.js
import React from 'react';
import useNavigation from '../hooks/navigation';

function LoginNavbar() {
  const navigate = useNavigation();

  return (
    <nav className="navbar navbar-expand-lg navbar-dark bg-dark sticky-top shadow-sm">
      <div className="container-fluid px-4">

        {/* Back Button - Increased visibility and spacing */}
        <button className="btn btn-outline-light me-md-4 me-2 rounded-pill px-3 py-2 d-flex align-items-center" onClick={()=>navigate(-1)}>
          <i className="bi bi-arrow-left me-1"></i> Back
        </button>

        {/* Brand Logo or Name - Enhanced styling */}
        <button className="navbar-brand btn btn-link text-white fw-bold fs-4 me-auto" onClick={() => navigate('/')}>
          <i className="bi bi-box-seam me-2"></i> HexaAsset
        </button>

        {/* Toggle Button for Mobile View */}
        <button
          className="navbar-toggler"
          type="button"
          data-bs-toggle="collapse"
          data-bs-target="#loginNavbar"
          aria-controls="loginNavbar"
          aria-expanded="false"
          aria-label="Toggle navigation"
        >
          <span className="navbar-toggler-icon"></span>
        </button>

        {/* Right Side Links */}
        <div className="collapse navbar-collapse" id="loginNavbar">
          <ul className="navbar-nav ms-auto mb-2 mb-lg-0">
            <li className="nav-item me-2">
              <button
                className="nav-link btn btn-primary text-white px-4 rounded-pill"
                onClick={() => navigate('/login')}
              >
                Login
              </button>
            </li>
            <li className="nav-item">
              <button
                className="nav-link btn btn-primary text-white px-4 rounded-pill"
                onClick={() => navigate('/register')}
              >
                Register
              </button>
            </li>
          </ul>
        </div>
      </div>
    </nav>
  );
}

export default LoginNavbar;