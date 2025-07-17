// src/components/Start.js
import React from 'react';
import LoginNavbar from '../navbar/LoginNavbar';
import '../navbar/Start.css';
import useNavigation from '../hooks/navigation';

function Start() {
  const navigate = useNavigation();

  return (
    <>
      <LoginNavbar />

<div
  className="start-hero d-flex align-items-center justify-content-center vh-100"
  style={{ backgroundColor: 'white', backgroundImage: 'none' }}
>

        <div className="text-center px-4">
          <h1 className="display-4 fw-bold mb-3">Welcome to HexaAsset</h1>
          <p className="lead fw-semibold mb-4">
            Securely manage, allocate, and track your organization's assets with ease.
          </p>
          <div className="d-inline-flex gap-3">
            <button className="btn btn-primary btn-lg" onClick={() => navigate('/login')}>
              Login
            </button>
            <button className="btn btn-outline-primary btn-lg" onClick={() => navigate('/register')}>
              Register
            </button>
          </div>
        </div>
      </div>

      <footer className="footer text-center py-3">
        © {new Date().getFullYear()} Hexaware Technologies Limited. All rights reserved.
      </footer>
    </>
  );
}

export default Start;
