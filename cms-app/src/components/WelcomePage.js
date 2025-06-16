// src/components/WelcomePage.js
import React from 'react';
import '../CSS/WelcomePage.css'; // We'll create this CSS file next

function WelcomePage() {
  return (
    <div className="welcome-container">
      <div className="welcome-content">
        <h1>Welcome to Our Claim Management System</h1>
        <p>Your simplified solution for managing claims.</p>
        {/* You could add a button here to navigate to login/dashboard */}
        <button className="start-button"><a href='/home'>Get Started</a></button>
      </div>
    </div>
  );
}

export default WelcomePage;