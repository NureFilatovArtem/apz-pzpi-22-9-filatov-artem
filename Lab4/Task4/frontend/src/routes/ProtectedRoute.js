import React from 'react';
import { Navigate, Outlet, useLocation } from 'react-router-dom';
import { useAuth } from '../contexts/AuthContext';

// allowedRoles: array of roles that can access this route (e.g., ['admin'])
// If allowedRoles is not provided, only authentication is checked.
const ProtectedRoute = ({ allowedRoles }) => {
  const { isAuthenticated, user, isLoading } = useAuth();
  const location = useLocation(); // To redirect back after login

  if (isLoading) {
    // You can return a loader here if you prefer
    return <div>Loading authentication status...</div>;
  }

  if (!isAuthenticated) {
    // Redirect to login, saving the current location to redirect back
    return <Navigate to="/login" state={{ from: location }} replace />;
  }

  // If roles are specified, check if the user's role is allowed
  if (allowedRoles && !allowedRoles.includes(user?.role)) {
    // User is authenticated but not authorized for this route
    // Redirect to a general page or an "Unauthorized" page
    // For now, redirecting to user dashboard or admin if role is admin
    return <Navigate to={user?.role === 'admin' ? '/admin' : '/user'} replace />;
  }

  return <Outlet />; // User is authenticated and authorized, render the child route component
};

export default ProtectedRoute;