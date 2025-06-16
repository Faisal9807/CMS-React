// src/pages/ForgotPasswordPage.js
import React, { useState } from 'react';
import axios from 'axios'; // Ensure axios is installed: npm install axios
import { useNavigate } from 'react-router-dom'; // Assuming you use react-router-dom for navigation
import '../CSS/ForgotPasswordPage.css'; // Import the CSS file

export function ForgotPasswordPage() {
  const [email, setEmail] = useState('');
  const [message, setMessage] = useState(''); // For success messages
  const [error, setError] = useState('');     // For error messages
  const [isLoading, setIsLoading] = useState(false); // To manage button state during API call
  const navigate = useNavigate(); // Hook for navigation

  const handleChange = (e) => {
    setEmail(e.target.value);
  };

  const handleSubmit = async (e) => {
    e.preventDefault(); // Prevent default form submission

    // Clear previous messages and errors
    setMessage('');
    setError('');
    setIsLoading(true); // Set loading state

    try {
      // Replace with your actual backend endpoint for forgot password
      // This endpoint typically just needs the email to send the reset link
      const response = await axios.post('http://localhost:8081/api/auth/forgot-password', { email });

      setMessage(response.data || 'Password reset link sent to your email. Please check your inbox.');
      setEmail(''); // Clear the email input on success
      
      // Optionally, navigate to a confirmation page or login page after a delay
      setTimeout(() => {
        navigate('/login'); // Redirect to login page
      }, 5000); // Redirect after 5 seconds

    } catch (err) {
      console.error('Forgot password request failed:', err);
      if (err.response) {
        // Server responded with a status other than 2xx
        if (err.response.status === 404) {
          setError('Email not found. Please check the address or register.');
        } else if (err.response.data && err.response.data.message) {
          setError(`Error: ${err.response.data.message}`);
        } else {
          setError('An error occurred. Please try again.');
        }
      } else if (err.request) {
        // Request was made but no response received
        setError('No response from server. Please check your network connection.');
      } else {
        // Something else happened
        setError('An unexpected error occurred. Please try again.');
      }
    } finally {
      setIsLoading(false); // Reset loading state
    }
  };

  return (
    <div className="forgot-password-container">
      <div className="forgot-password-card">
        <h2>Forgot Password</h2>
        <p className="forgot-password-intro">
          Enter your email address below and we'll send you a link to reset your password.
        </p>

        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <label htmlFor="email">Email Address:</label>
            <input
              type="email"
              id="email"
              name="email"
              value={email}
              onChange={handleChange}
              placeholder="your.email@example.com"
              required
              disabled={isLoading} // Disable input while loading
            />
          </div>

          <button type="submit" className="submit-btn" disabled={isLoading}>
            {isLoading ? 'Sending...' : 'Send Reset Link'}
          </button>
        </form>

        {message && <p className="success-message">{message}</p>}
        {error && <p className="error-message">{error}</p>}

        <p className="back-to-login">
          Remembered your password? <span onClick={() => navigate('/login')}>Login here</span>
        </p>
      </div>
    </div>
  );
}

export default ForgotPasswordPage;