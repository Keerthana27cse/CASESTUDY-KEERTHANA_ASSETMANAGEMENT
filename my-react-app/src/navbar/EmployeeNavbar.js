import useNavigation from '../hooks/navigation';
import { useEffect } from 'react';

function EmployeeNavbar() {
  const handleNavigate = useNavigation();


   useEffect(() => {
    document.body.style.backgroundColor = '#dca9d9ff'; // light teal
    return () => {
      document.body.style.backgroundColor = ''; // reset when unmounted
    };
  }, []);

  const logout = () => {
    localStorage.removeItem('token');
    localStorage.removeItem('role');
    handleNavigate('/login');
  };

  return (
    <>
<nav className="navbar navbar-expand-lg navbar-dark shadow-sm"
     style={{ backgroundColor: '#a268aeff' }}>
        <div className="container-fluid">
          {/* Branding */}
        <button className="btn btn-outline-light me-md-4 me-2 rounded-pill px-3 py-2 d-flex align-items-center" onClick={()=>handleNavigate(-1)}>
          <i className="bi bi-arrow-left me-1"></i>
        </button>

        {/* Brand Logo or Name - Enhanced styling */}
        <button className="navbar-brand btn btn-link text-white fw-bold fs-4 me-auto" onClick={() => handleNavigate('/admin-dashboard')}>
          <i className="bi bi-box-seam me-2"></i> HexaAsset
        </button>

          <button
            className="navbar-toggler"
            type="button"
            data-bs-toggle="collapse"
            data-bs-target="#employeeNavbar"
            aria-controls="employeeNavbar"
            aria-expanded="false"
            aria-label="Toggle navigation"
          >
            <span className="navbar-toggler-icon"></span>
          </button>

          <div className="collapse navbar-collapse" id="employeeNavbar">
            {/* Left Links */}
            <ul className="navbar-nav me-auto mb-2 mb-lg-0 gap-2">
              {[
                { name: 'Home', path: '/employee-dashboard' },
                { name: 'View Assets', path: '/employee/assets' },
                { name: 'My Requests', path: '/employee/my-asset-requests' },
                { name: 'My Service Request', path: '/employee/my-service-requests' },
                { name: 'Audit Request', path: '/employee/my-audit-requests' },
                { name: 'Settings', path: '/employee/settings' },
              ].map((item, index) => (
                <li className="nav-item" key={index}>
<button className="nav-link btn btn-sm btn-light text-dark rounded px-3 shadow-sm fw-medium"
                    onClick={() => handleNavigate(item.path)}
                  >
                    {item.name}
                  </button>
                </li>
              ))}
            </ul>

            {/* Right Side Logout */}
            <ul className="navbar-nav ms-auto">
              <li className="nav-item">
                <button className="btn btn-light text-success fw-semibold" onClick={logout}>
                  <i className="bi bi-box-arrow-right me-2"></i>Logout
                </button>
              </li>
            </ul>
          </div>
        </div>
      </nav>
    </>
  );
}

export default EmployeeNavbar;
