import React, { useState } from 'react';
import { jwtDecode } from 'jwt-decode';
import { useNavigate } from 'react-router-dom';
import { useAuth } from './AuthContext';// ✅ import useAuth
import '../CSS/MemberLoginPage.css';

function LoginPage() {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [message, setMessage] = useState('');
  const navigate = useNavigate();
  const { setUser } = useAuth(); // ✅ get setUser from context

  const handleSubmit = async (e) => {
    e.preventDefault();
    setMessage('');

    try {
      const response = await fetch('http://localhost:8081/api/members/login', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ memberId: username, password }),
      });

      const clonedResponse = response.clone();
      const rawResponseBody = await clonedResponse.text();

      if (response.ok) {
        const data = JSON.parse(rawResponseBody);
        const token = data.token;

        if (token) {
          localStorage.setItem("token", token);
          const decoded = jwtDecode(token);
          console.log("Decoded Token:", decoded);

          // ✅ Update AuthContext
          setUser({
            isLoggedIn: true,
            name: decoded.name || decoded.sub,
            email: decoded.email || 'example@example.com',
            role: decoded.role,
            Id: decoded.Id,
          });

          setMessage('Login successful! Welcome, ' + decoded.name);
          navigate('/home'); // ✅ route change without reload
        } else {
          setMessage("Login failed: Token not received.");
        }
      } else {
        setMessage(`Login failed: ${rawResponseBody || 'Unknown error'}.`);
        console.error('Login failed:', rawResponseBody);
      }
    } catch (error) {
      setMessage('A network error occurred. Please try again later.');
      console.error('Network error:', error);
    }
  };

  return (
    <div className="login-container">
      <h2>Login</h2>
      <form onSubmit={handleSubmit} className="login-form">
        <div className="form-group">
          <label htmlFor="username">Username:</label>
          <input
            type="text"
            id="username"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
            required
          />
        </div>
        <div className="form-group">
          <label htmlFor="password">Password:</label>
          <input
            type="password"
            id="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            required
          />
        </div>
        <button type="submit" className="login-button">Login</button>
        <div className="login-link-container">
          <p>New to CMS? <a href="/register">Register here</a></p>
          <a href="/forget">forget password ?</a>
        </div>
      </form>
      {message && <p className={`message ${message.includes('successful') ? 'success' : 'error'}`}>{message}</p>}
    </div>
  );
}

export default LoginPage;
