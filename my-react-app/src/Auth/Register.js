import React, { useState, useEffect, useRef } from 'react';
import { useNavigate } from 'react-router-dom';
import LoginNavbar from '../navbar/LoginNavbar';
import '../navbar/index.css'; // For animation (added below)

function Register() {
  const [name, setName] = useState('');
  const [username, setUsername] = useState('');
  const [email, setEmail] = useState('');
  const [contactNumber, setContactNumber] = useState('');
  const [address, setAddress] = useState('');
  const [gender, setGender] = useState('');
  const [password, setPassword] = useState('');
  const [confirmPwd, setConfirmPwd] = useState('');
  const [passwordStrength, setPasswordStrength] = useState('');
  const [msg, setMsg] = useState('');
  const navigate = useNavigate();
  const usernameRef = useRef();


  const getPasswordStrength = (pwd) => {
    if (pwd.length < 6) return 'Weak';
    if (/[A-Z]/.test(pwd) && /\d/.test(pwd) && /[!@#$%^&*]/.test(pwd)) return 'Strong';
    return 'Medium';
  };

  const validate = () => {
    if (!name || !username || !email || !contactNumber || !address || !gender || !password || !confirmPwd) {
      setMsg('❗ Please fill all fields.');
      return false;
    }
    if (password !== confirmPwd) {
      setMsg('❗ Passwords do not match.');
      return false;
    }
    if (password.length < 6) {
      setMsg('❗ Password should be at least 6 characters.');
      return false;
    }
    return true;
  };

    const register = async (e) => {
    e.preventDefault();
    setMsg('');

    if (!validate()) return;
         
    try {
      const res= await fetch('http://localhost:8081/auth/register', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ username, name, email, password, address, gender, contactNumber}),
      });
      const contentType = res.headers.get("content-type");

       const data = contentType && contentType.includes("application/json")
      ? await res.json()
      : { message: await res.text() };

      if (res.ok) {
        setMsg('✅ Registration successful! Redirecting to login...');
        setTimeout(() => navigate('/login',
        {
          state:{email,password}
        }), 2000);
      } else {
        setMsg(data.message || '❌ Registration failed.');
      }
    } catch {
      setMsg('❌ Error connecting to the server.');
    }
  };

  return (
    <>
    <LoginNavbar/>
    
<div
  className="container-fluid vh-100 d-flex justify-content-center align-items-center"
  style={{
    backgroundImage: `url('Gemini_Generated_Image_t9lkent9lkent9lk.png')`,
    backgroundSize: 'cover',
    backgroundPosition: 'center',
    backgroundRepeat: 'no-repeat',
  }}
>  
      <div className="card shadow register-card">
        <div className="text-center mb-3">
          <img src="/logo.png" alt="HexaAsset" width="70" />
             <h3 className="text-primary mt-3">Registration</h3>
          <p className="text-muted">Join HexaAsset to manage your organizational assets</p>
        </div>

        {msg && (
          <div className={`alert mx-3 ${msg.startsWith('✅') ? 'alert-success' : 'alert-danger'}`}>
            {msg}
          </div>
        )}

        <div className="form-scroll px-4">
          <form onSubmit={register}>
            <div className="mb-2">
              <label>Username</label>
              <input type="text" className="form-control" ref={usernameRef} required value={username}
                onChange={(e) => setUsername(e.target.value)} />
            </div>
            <div className="mb-2">
              <label>Full Name</label>
              <input type="text" className="form-control" required value={name}
                onChange={(e) => setName(e.target.value)} />
            </div>
            <div className="mb-2">
              <label>Email</label>
              <input type="email" className="form-control" required value={email}
                onChange={(e) => setEmail(e.target.value)} />
            </div>
            <div className="mb-2">
              <label>Contact Number</label>
              <input type="tel" className="form-control" required value={contactNumber}
                onChange={(e) => setContactNumber(e.target.value)} />
            </div>
            <div className="mb-2">
              <label>Address</label>
              <textarea className="form-control" required value={address}
                onChange={(e) => setAddress(e.target.value)} />
            </div>
            <div className="mb-2">
              <label>Password</label>
              <input type="password" className="form-control" required value={password}
                onChange={(e) => {
                  setPassword(e.target.value);
                  setPasswordStrength(getPasswordStrength(e.target.value));
                }} />
              {password && (
                <small className={`text-${passwordStrength === 'Strong' ? 'success' : passwordStrength === 'Medium' ? 'warning' : 'danger'}`}>
                  Password strength: {passwordStrength}
                </small>
              )}
            </div>
            <div className="mb-2">
              <label>Confirm Password</label>
              <input type="password" className="form-control" required value={confirmPwd}
                onChange={(e) => setConfirmPwd(e.target.value)} />
            </div>
            <div className="mb-3">
              <label>Gender</label>
              <select className="form-select" required value={gender} onChange={(e) => setGender(e.target.value)}>
                <option value="">Select</option>
                <option value="MALE">Male</option>
                <option value="FEMALE">Female</option>
                <option value="OTHER">Other</option>
              </select>
            </div>

            

            <button type="submit" className="btn btn-primary w-100 mb-2">Register</button>
          </form>
        </div>

        <div className="text-center small mb-2">
          Already registered? <a href="#" onClick={() => navigate('/login')}>Login</a>
        </div>
      </div>
    </div>
        </>

  );
}

export default Register;
