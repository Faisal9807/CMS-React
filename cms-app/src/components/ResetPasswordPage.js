// src/pages/ResetPasswordPage.js
import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { useNavigate, useLocation } from 'react-router-dom';
import '../CSS/ResetPasswordPage.css'; // You'll create this CSS file

export function ResetPasswordPage() {
  const [password, setPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');
  const [message, setMessage] = useState('');
  const [error, setError] = useState('');
  const [isLoading, setIsLoading] = useState(false);
  const [token, setToken] = useState(null); // State to store the token from URL

  const navigate = useNavigate();
  const location = useLocation(); // Hook to access URL parameters

  // Effect to extract the token from the URL when the component mounts
  useEffect(() => {
    const queryParams = new URLSearchParams(location.search);
    const tokenFromUrl = queryParams.get('token');
    if (tokenFromUrl) {
      setToken(tokenFromUrl);
      setMessage('Please enter and confirm your new password.');
    } else {
      setError('Password reset token is missing from the URL.');
    }
  }, [location.search]); // Re-run if URL query parameters change

  const handleSubmit = async (e) => {
    e.preventDefault();

    // Clear previous messages and errors
    setMessage('');
    setError('');

    if (!token) {
      setError('Invalid or missing password reset token.');
      return;
    }

    if (password !== confirmPassword) {
      setError('Passwords do not match.');
      return;
    }

    if (password.length < 6) { // Basic password length validation
        setError('Password must be at least 6 characters long.');
        return;
    }

    setIsLoading(true);

    try {
      // Send the token and new password to your backend
      const response = await axios.post(
        'http://localhost:8081/api/auth/reset-password',
        { newPassword: password }, // Send newPassword in the body
        {
          params: { token: token } // Send token as a URL query parameter
        }
      );

      setMessage(response.data || 'Your password has been reset successfully!');
      setPassword('');
      setConfirmPassword('');
      setToken(null); // Invalidate token in state after successful use

      // Redirect to login page after a short delay
      setTimeout(() => {
        navigate('/login');
      }, 3000);

    } catch (err) {
      console.error('Password reset failed:', err);
      if (err.response) {
        if (err.response.data && err.response.data.message) {
          setError(`Error: ${err.response.data.message}`);
        } else if (err.response.status === 400) {
          setError('Invalid or expired password reset link. Please try again.');
        } else {
          setError('An error occurred during password reset. Please try again.');
        }
      } else if (err.request) {
        setError('No response from server. Please check your network connection.');
      } else {
        setError('An unexpected error occurred. Please try again.');
      }
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className="reset-password-container">
      <div className="reset-password-card">
        <h2>Reset Password</h2>
        
        {error && <p className="error-message">{error}</p>}
        {message && <p className="success-message">{message}</p>}

        {token && !error && ( // Only show form if token is present and no initial error
          <form onSubmit={handleSubmit}>
            <div className="form-group">
              <label htmlFor="password">New Password:</label>
              <input
                type="password"
                id="password"
                name="password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                placeholder="Enter new password"
                required
                disabled={isLoading}
              />
            </div>
            <div className="form-group">
              <label htmlFor="confirmPassword">Confirm New Password:</label>
              <input
                type="password"
                id="confirmPassword"
                name="confirmPassword"
                value={confirmPassword}
                onChange={(e) => setConfirmPassword(e.target.value)}
                placeholder="Confirm new password"
                required
                disabled={isLoading}
              />
            </div>
            <button type="submit" className="submit-btn" disabled={isLoading}>
              {isLoading ? 'Resetting...' : 'Reset Password'}
            </button>
          </form>
        )}

        <p className="back-to-login">
          Back to <span onClick={() => navigate('/login')}>Login</span>
        </p>
      </div>
    </div>
  );
}

export default ResetPasswordPage;