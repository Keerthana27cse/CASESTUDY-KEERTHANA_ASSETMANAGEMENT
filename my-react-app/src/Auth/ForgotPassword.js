import React, { useState } from 'react';
import LoginNavbar from '../navbar/LoginNavbar';
import { Navigate, useNavigate } from 'react-router-dom';
import '../navbar/index.css'; // For animation (added below)

function ForgotPassword() {
  const [email, setEmail] = useState('');
  const [newPassword, setNewPassword] = useState('');
  const [msg, setMsg] = useState('');


  const navigate=useNavigate()
  const handleReset = async (e) => {
    e.preventDefault();
    try {
      const res = await fetch(
        `http://localhost:8081/auth/forgot-password?email=${encodeURIComponent(email)}&newPassword=${encodeURIComponent(newPassword)}`,
        {
        method: 'POST',
        headers:
        {
          'Content-Type' :'application/json',
        },
        body:JSON.stringify({email,password:newPassword}),
    });
      if (res.ok) {
        const text = await res.text();
        setTimeout(() => navigate('/login'), 2000);

        setMsg(text || 'Password updated successfully.');

      } else {
        const errorText = await res.text();
        setMsg(errorText || 'Failed to update password.');
      }
    } catch {
      setMsg('Error connecting to server.');
    }
  };

  return (
    <>
    <LoginNavbar/>
     <div className="container-fluid bg-light vh-100 d-flex align-items-center justify-content-center fade-in">
      <div className="card shadow p-4 w-100" style={{ maxWidth: '600px' }}>
        <div className="text-center mb-4">
          <img src="/logo.png" alt="HexaAsset" width="80" />
          <h3 className="text-primary mt-3">Reset Password</h3>
        </div>
        {msg && <div className="alert alert-info">{msg}</div>}
        <form onSubmit={handleReset}>
          <div className="mb-3">
            <label>Email</label>
            <input
              type="email"
              className="form-control"
              required
              value={email}
              onChange={(e) => setEmail(e.target.value)}
            />
          </div>
          <div className="mb-3">
            <label>New Password</label>
            <input
              type="password"
              className="form-control"
              required
              value={newPassword}
              onChange={(e) => setNewPassword(e.target.value)}
            />
          </div>
          <button className="btn btn-primary w-100" type="submit">
            Reset Password
          </button>
        </form>
      </div>
    </div>
        </>

  );
}

export default ForgotPassword;
