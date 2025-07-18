import React from 'react';

function Footer() {
  return (
    <footer
      className="text-dark text-center py-3 mt-auto shadow-sm"
      style={{ backgroundColor: '#fcd5ce' }}
    >
      <div className="container">
        <small>
          &copy; {new Date().getFullYear()} <strong>HexaAsset</strong>. All rights reserved.
        </small>
        <br />
        <small className="text-muted">Built with ❤️ by Keerthana</small>
        <br />
        <small className="text-muted">
          <i className="fas fa-envelope me-1"></i>
          For queries: <a href="mailto:kitcbe.25.21bcs022@gmail.com" className="text-decoration-none text-dark">kitcbe.25.21bcs022@gmail.com</a>
        </small>
      </div>
    </footer>
  );
}

export default Footer;
