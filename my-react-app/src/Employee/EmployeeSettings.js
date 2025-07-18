import React, { useEffect, useState } from 'react';
import EmployeeNavbar from '../navbar/EmployeeNavbar';

function EmployeeSettings() {
  const token = localStorage.getItem('token');

  const [profile, setProfile] = useState({
    username: '',
    name: '',
    email: '',
    contactNumber: '',
    address: '',
    gender: '',
  });

  const [passwords, setPasswords] = useState({
    currentPassword: '',
    newPassword: '',
  });

  const [msg, setMsg] = useState('');
  const [msgType, setMsgType] = useState('');

  useEffect(() => {
    // Fetch current employee info (GET /api/employees/1 or /api/employees/me)
    // Assuming you have an API to get current employee data, else load from token or local storage
    fetch('http://localhost:8081/api/employees/1', { // replace 1 with your user id or dynamic
      headers: { Authorization: `Bearer ${token}` },
    })
      .then(res => res.json())
      .then(data => {
        setProfile(data);
      })
      .catch(() => {
        setMsg('Failed to load profile.');
        setMsgType('error');
      });
  }, [token]);

  const handleProfileChange = e => {
    const { name, value } = e.target;
    setProfile(prev => ({ ...prev, [name]: value }));
  };

  const handlePasswordChange = e => {
    const { name, value } = e.target;
    setPasswords(prev => ({ ...prev, [name]: value }));
  };

  // Update profile API call
  const saveProfile = () => {
    fetch('http://localhost:8081/api/employees/update', {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
        Authorization: `Bearer ${token}`,
      },
      body: JSON.stringify(profile),
    })
      .then(res => {
        if (res.ok) {
          setMsg('Profile updated successfully.');
          setMsgType('success');
        } else {
          setMsg('Failed to update profile.');
          setMsgType('error');
        }
      })
      .catch(() => {
        setMsg('Error updating profile.');
        setMsgType('error');
      });
  };

  // Change password API call (using your forgot-password API)
  const changePassword = () => {
    // Call your forgot-password API with email, username, newPassword
    const url = `http://localhost:8081/auth/forgot-password?email=${encodeURIComponent(profile.email)}&username=${encodeURIComponent(profile.username)}&newPassword=${encodeURIComponent(passwords.newPassword)}`;

    fetch(url, {
      method: 'POST',
    })
      .then(res => res.text())
      .then(text => {
        setMsg(text || 'Password updated successfully.');
        setMsgType('success');
        setPasswords({ currentPassword: '', newPassword: '' });
      })
      .catch(() => {
        setMsg('Failed to update password.');
        setMsgType('error');
      });
  };

  return (
    <>
    <EmployeeNavbar/>
    <div className="container mt-4">
      <h3>Employee Settings</h3>

      {msg && (
        <div className={`alert alert-${msgType === 'success' ? 'success' : 'danger'}`}>
          {msg}
        </div>
      )}

      <div className="mb-3">
        <label>Name</label>
        <input
          className="form-control"
          name="name"
          value={profile.name || ''}
          onChange={handleProfileChange}
        />
      </div>

      <div className="mb-3">
        <label>Contact Number</label>
        <input
          className="form-control"
          name="contactNumber"
          value={profile.contactNumber || ''}
          onChange={handleProfileChange}
        />
      </div>

      <div className="mb-3">
        <label>Address</label>
        <input
          className="form-control"
          name="address"
          value={profile.address || ''}
          onChange={handleProfileChange}
        />
      </div>

      <div className="mb-3">
        <label>Gender</label>
        <select
          className="form-control"
          name="gender"
          value={profile.gender || ''}
          onChange={handleProfileChange}
        >
          <option value="">-- Select --</option>
          <option value="MALE">Male</option>
          <option value="FEMALE">Female</option>
          <option value="OTHER">Other</option>
        </select>
      </div>

      <button className="btn btn-primary mb-4" onClick={saveProfile}>
        Save Profile
      </button>

      <h5>Change Password</h5>
      <div className="mb-3">
        <label>New Password</label>
        <input
          type="password"
          className="form-control"
          name="newPassword"
          value={passwords.newPassword}
          onChange={handlePasswordChange}
        />
      </div>

      <button className="btn btn-warning" onClick={changePassword}>
        Change Password
      </button>
    </div>
        </>

  );
}


export default EmployeeSettings;
