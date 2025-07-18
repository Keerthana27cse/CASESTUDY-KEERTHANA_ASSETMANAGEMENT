import React, { useState } from 'react';
import useNavigation from '../hooks/navigation';
import LoginNavbar from '../navbar/LoginNavbar';
import { useLocation } from 'react-router-dom';
import '../navbar/index.css'; // For animation (added below)


function Login() {
  const location=useLocation();
  const [email, setEmail] = useState(location.state?.email||'');
  const [password, setPassword] = useState(location.state?.password||'');
  const [msg, setMsg] = useState('');
  const [showPassword, setShowPassword] = useState(false);
  const navigate = useNavigation();

  const login = async (e) => {
    e.preventDefault();
    setMsg('');

    try {
      const res = await fetch('http://localhost:8081/auth/login', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ email, password }),
      });

      const contentType = res.headers.get('content-type');
      const data =
        contentType && contentType.includes('application/json')
          ? await res.json()
          : { message: await res.text() };

      if (res.ok) {
        localStorage.setItem('token', data.token);
        localStorage.setItem('role', data.role);
        navigate(data.role === 'ADMIN' ? '/admin-dashboard' : '/employee-dashboard');
      } else {
        setMsg(data.message || 'Login failed. INACTIVE USER');
      }
    } catch (err) {
      setMsg('Error connecting to server.');
    }
  };

  return (
    <>   
      <LoginNavbar />

<div
  className="container-fluid vh-100 d-flex justify-content-center align-items-center"
  style={{
    backgroundImage: `url('Gemini_Generated_Image_t9lkent9lkent9lk.png')`,
    backgroundSize: 'cover',
    backgroundPosition: 'center',
    backgroundRepeat: 'no-repeat',
  }}
>        <div className="card p-4 shadow-lg rounded" style={{ maxWidth: '400px', width: '100%' }}>
          <div className="text-center mb-4">
            <img src="/logo.png" alt="HexaAsset" className="img-fluid" style={{ maxWidth: '100px' }} />
            <h3 className="text-primary mt-2">Login</h3>
          </div>

          {msg && <div className="alert alert-danger">{msg}</div>}

          <form onSubmit={login}>
            <div className="mb-3">
              <label htmlFor="email" className="form-label">
                Email
              </label>
              <input
                id="email"
                type="email"
                className="form-control"
                required
                value={email}
                onChange={(e) => setEmail(e.target.value)}
              />
            </div>

            <div className="mb-3">
              <label htmlFor="password" className="form-label">
                Password
              </label>
              <div className="input-group">
                <input
                  id="password"
                  type={showPassword ? 'text' : 'password'}
                  className="form-control"
                  required
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                />
                <button
                  type="button"
                  className="btn btn-outline-secondary"
                  onClick={() => setShowPassword((prev) => !prev)}
                  tabIndex={-1}
                >
                  <i className={`bi ${showPassword ? 'bi-eye-slash' : 'bi-eye'}`}></i>
                </button>
              </div>
            </div>

            <button type="submit" className="btn btn-primary w-100 mb-2">
              Login
            </button>
          </form>

          <div className="text-center mt-2">
            <a href="#" onClick={() => navigate('/forgot-password')}>
              Forgot Password?
            </a>
          </div>
        </div>
      </div>
    </>
  );
}

export default Login;


