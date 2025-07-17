import React from 'react';
import { useNavigate } from 'react-router-dom';
import AdminNavbar from '../navbar/AdminNavbar';
import { useEffect } from 'react';
import Footer from '../navbar/Footer';


function AdminDashboard() {
  const navigate = useNavigate();
  useEffect(() => {
  document.body.classList.add('admin-body');
  return () => document.body.classList.remove('admin-body');
}, []);

  return (
    <>
      <AdminNavbar />

      {/* Scrollable Content Wrapper */}
      <div className="container-fluid py-4 overflow-auto" style={{ maxHeight: 'calc(100vh - 70px)' }}>
        {/* Intro Section */}
        <div className="text-center mb-5">
          <h1 className="display-5 fw-bold text-primary">HexaAsset Admin Portal</h1>
          <p className="lead text-muted">
            Elevating asset management since <strong>2003</strong>. Powered by precision. Founded by <span className="text-success">Keerthana</span>.
          </p>
          <span className="badge bg-dark fs-6">Your trusted asset companion</span>
        </div>

        {/* Services Section */}
        <div className="row g-4 mb-5">
          {[
            {
              title: 'Asset Allocation',
              desc: 'Smart tools to allocate and track company assets across departments and individuals.',
              icon: 'bi bi-box-seam',
              color: 'success'
            },
            {
              title: 'Employee Management',
              desc: 'Streamlined onboarding, offboarding, and real-time asset visibility for HR.',
              icon: 'bi bi-person-check',
              color: 'primary'
            },
            {
              title: 'Service Requests',
              desc: 'Maintain operational health with automated maintenance and service flows.',
              icon: 'bi bi-wrench-adjustable',
              color: 'info'
            },
            {
              title: 'Audit Reports',
              desc: 'Ensure compliance and transparency with custom reporting tools.',
              icon: 'bi bi-journal-check',
              color: 'warning'
            },
          ].map((item, index) => (
            <div className="col-md-6 col-lg-3" key={index}>
              <div className={`card border-0 shadow-sm h-100 text-${item.color}`}>
                <div className="card-body text-center">
                  <i className={`${item.icon} fs-2 mb-3`}></i>
                  <h5 className="card-title fw-bold">{item.title}</h5>
                  <p className="card-text text-muted">{item.desc}</p>
                </div>
              </div>
            </div>
          ))}
        </div>

        {/* Vision Section */}
        <div className="bg-light p-5 rounded shadow-sm mb-5">
          <h4 className="text-dark fw-bold mb-3">Our Vision</h4>
          <p className="text-muted">
            At <strong>HexaAsset</strong>, our goal is simple: make asset management seamless, transparent, and employee-first. From startups to enterprises, our platform adapts to you.
          </p>
          <ul className="list-group list-group-flush mt-3">
            <li className="list-group-item">🔒 Secure role-based access with Spring Security</li>
            <li className="list-group-item">🛠 Powerful backend powered by MySQL and custom APIs</li>
            <li className="list-group-item">🎨 Clean, professional interface to impress every stakeholder</li>
          </ul>
        </div>

        {/* CTA Section */}
        <div className="text-center">
          <h4 className="fw-bold mb-3">Ready to explore the backend brilliance?</h4>
          <button
            className="btn btn-outline-success btn-lg rounded-pill px-4"
            onClick={() => navigate('/admin/assets')}
          >
            Manage Assets Now
          </button>
        </div>

      </div>
    <Footer/>

    </>
  );
}

export default AdminDashboard;
