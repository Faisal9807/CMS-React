// src/pages/SupportPage.js
import React from "react";
import "../CSS/SupportPage.css";

export function SupportPage() {
  return (
    <main className="support-main">
      <section className="support-header">
        <div className="support-container">
          <h2 className="support-title">Contact Us</h2>
          <p className="support-description">
            For prompt assistance with claim data issues, please open a support ticket using an example claim.
          </p>
        </div>
      </section>

      <section className="support-body">
        <div className="support-container support-flex">
          <div className="support-info">
            <div className="info-block">
              <h3>Claim Address</h3>
              <address>
                P.O. Box 1177<br />
                Pecos, NM 87552
              </address>
              <small>(Do not mail claims to this address)</small>
            </div>
            <div className="info-block">
              <p><strong>Telephone:</strong> <a href="#">(+91) 757-6060-800</a></p>
              <p><strong>Sales:</strong> <a href="#">(+91) 757-6060-900</a></p>
              <p><strong>Support:</strong> <a href="#">(+91) 757-6060-700</a></p>
            </div>
          </div>

          <div className="support-form">
            <form>
              <input type="text" name="name" placeholder="Your Name" maxLength="50" />
              <input type="tel" name="phone" placeholder="Phone Number" maxLength="25" />
              <input type="email" name="email" placeholder="Email Address" maxLength="50" />
              <div className="radio-group">
                <label>
                  <input type="radio" name="type" value="sales" defaultChecked /> Sales
                </label>
                <label>
                  <input type="radio" name="type" value="support" /> Support
                </label>
              </div>
              <textarea name="message" placeholder="Message" rows="6"></textarea>
              <button type="submit">Send</button>
            </form>
            <p className="disclaimer">*** Claim is not the insurance company and does not provide claim status information.</p>
          </div>
        </div>
      </section>
    </main>
  );
}

export default SupportPage;
