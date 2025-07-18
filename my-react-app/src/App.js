import React from 'react';
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';

import Start from './Employee/Start';
import Login from './Auth/Login';
import Register from './Auth/Register';
import AdminDashboard from './Admin/AdminDashboard';
import EmployeeDashboard from './Employee/EmployeeDashboard';
import CategoryComponent from './Admin/CategoryComponent';
import EmployeeAssetCatalogue from './Employee/EmployeeAssetCatalogue';
import AssetDetails from './Employee/AssetDetails';
import AssetComponent from './Admin/AdminAssetComponent';
import ForgotPassword from './Auth/ForgotPassword';
import EmployeeList from './Admin/EmployeeList';
import AssetRequestForm from './Employee/AssetRequestForm';
import ServiceRequestForm from './Employee/ServiceRequestForm';
import MyAssetRequests from './Employee/MyAssetRequests';
import MyServiceRequests from './Employee/MyServiceRequests';
import AdminAssetRequests from './Admin/AdminAssetRequests';
import AdminServiceRequests from './Admin/AdminServiceRequests';
import AssetAssignmentSummary from './Admin/AssetSummary';
import AdminAssetAuditRequests from './Admin/AdminAuditRequests';
import AdminSettings from './Admin/AdminSettings';
import EmployeeSettings from './Employee/EmployeeSettings';
import EmployeeAssetAuditRequests from './Employee/EmployeeAudit';




const PrivateRoute = ({ children, role }) => {
  const token = localStorage.getItem('token');
  const userRole = localStorage.getItem('role');
  return token && userRole === role ? children : <Navigate to="/login" />;
};

function App() {
  return (
    <BrowserRouter>
      <Routes>

        {/* Public routes */}
        <Route path="/" element={<Start />} />
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />
        <Route path="/forgot-password"element={<ForgotPassword/>}/>
      

      
        <Route path="/admin/assets" element={<AssetComponent />} />
        <Route path="/admin/categories" element={<CategoryComponent/>}/>
        <Route path="/admin/EmployeeList" element={<EmployeeList/>}/>
        <Route path="/admin/asset-requests" element={<AdminAssetRequests/>}/>
        <Route path="/admin/service-requests" element={<AdminServiceRequests/>}/>
        <Route path="/admin/asset-summary" element={<AssetAssignmentSummary/>}/>
        <Route path="/admin/asset-audits" element={<AdminAssetAuditRequests/>}/>
        <Route path="/admin/settings" element={<AdminSettings/>}/>






        <Route path="/employee/assets" element={<EmployeeAssetCatalogue/>}/>
        <Route path="/employee/assets/:id"element={<AssetDetails/>}/>
        <Route path="/employee/asset-request/:assetId/:requestedCategoryId" element={<AssetRequestForm />} />  
        <Route path="/employee/my-asset-requests" element={<MyAssetRequests/>}/>
        <Route path="/employee/service-request/:assetId/:requestedCategoryId"element={<ServiceRequestForm />}/>
        <Route path="/employee/my-service-requests" element={<MyServiceRequests/>}/>
        <Route path="/employee/my-audit-requests" element={<EmployeeAssetAuditRequests/>}/>
        <Route path="/employee/settings" element={<EmployeeSettings/>}/>






        {/* Protected routes */}
        <Route
          path="/admin-dashboard"
          element={
            <PrivateRoute role="ADMIN">
              <AdminDashboard />
            </PrivateRoute>
          }
        />
        <Route
          path="/employee-dashboard"
          element={
            <PrivateRoute role="EMPLOYEE">
              <EmployeeDashboard />
            </PrivateRoute>
          }
        />
      </Routes>
    </BrowserRouter>
  );
}

export default App;
