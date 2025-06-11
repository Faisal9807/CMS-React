import React from 'react';
import '../CSS/ServicePage.css'; // Import the CSS file

const ServicePage = () => {
  return (
    <div className="service-container">
      <header className="service-header">
        <h1>Claim Management System</h1>
        <p>Our Services</p>
      </header>

      <main className="service-main">
        <section className="service-section">
          <h2>What We Offer</h2>
          <p>Our Claim Management System provides a complete suite of services to manage insurance claims efficiently and securely.</p>
          <ul>
            <li>🔍 Claim Tracking & Status Updates</li>
            <li>📝 Online Claim Submission</li>
            <li>🔐 Secure Document Upload</li>
            <li>📊 Real-time Dashboard & Reports</li>
            <li>📞 Support for Customer Queries</li>
            <li>🔄 Auto Notification & Reminders</li>
          </ul>
        </section>

        <section className="service-section">
          <h2>Who Uses Our Services?</h2>
          <ul>
            <li>✅ Insurance Companies</li>
            <li>✅ Policy Holders</li>
            <li>✅ Claim Adjusters</li>
            <li>✅ Admin Teams</li>
          </ul>
        </section>

        <section className="service-section">
          <h2>Why Choose Us?</h2>
          <p>We make claims simple, fast, and paperless. Our system helps reduce errors, saves time, and improves customer satisfaction.</p>
        </section>
      </main>
    </div>
  );
};

export default ServicePage;
