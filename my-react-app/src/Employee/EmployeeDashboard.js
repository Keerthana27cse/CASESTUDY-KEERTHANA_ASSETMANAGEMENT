import React, { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import EmployeeNavbar from '../navbar/EmployeeNavbar';
import Footer from '../navbar/Footer';
import './EmployeeDashboard.css';

function EmployeeDashboard() {
  const navigate = useNavigate();

  useEffect(() => {
    document.body.classList.add('employee-body');
    return () => document.body.classList.remove('employee-body');
  }, []);

  const features = [
    {
      title: 'View Assets',
      desc: 'Explore your assigned and available company assets.',
      icon: '💻',
      link: '/employee/assets',
    },
    {
      title: 'My Requests',
      desc: 'Check status and history of asset allocation requests.',
      icon: '📋',
      link: '/employee/my-asset-requests',
    },
    {
      title: 'Service Requests',
      desc: 'Report issues or schedule maintenance for your assets.',
      icon: '🛠️',
      link: '/employee/my-service-requests',
    },
    {
      title: 'Audit Tracker',
      desc: 'View past audit records related to your asset usage.',
      icon: '✅',
      link: '/employee/my-audit-requests',
    },
  ];

  return (
    <>
      <EmployeeNavbar />
      <div className="dashboard-container">
        <section className="welcome-card">
          <h1>Hello 👋</h1>
          <p>
            Welcome to <span className="brand">HexaAsset</span>, your trusted platform for efficient asset access and requests.
          </p>
        </section>

        <section className="features-grid">
          {features.map((item, index) => (
            <div key={index} className="feature-card" onClick={() => navigate(item.link)}>
              <div className="feature-icon">{item.icon}</div>
              <h3>{item.title}</h3>
              <p>{item.desc}</p>
              <button>Go to {item.title}</button>
            </div>
          ))}
        </section>

        <section className="community-card">
          <h2>Why HexaAsset?</h2>
          <p>
            Founded in <strong>2003</strong> by <span className="founder">Keerthana</span>, HexaAsset strives to make work smoother and more transparent for every employee.
          </p>
          <ul>
            <li>💼 Easy asset request tracking</li>
            <li>🔧 Service scheduling in clicks</li>
            <li>📑 Clear audit history access</li>
            <li>🌐 A responsive UI that just makes sense</li>
          </ul>
        </section>

        <section className="cta-card">
          <h3>Need something sorted?</h3>
          <button onClick={() => navigate('/employee/settings')}>Go to Settings</button>
        </section>
      </div>
      <Footer />
    </>
  );
}

export default EmployeeDashboard;
