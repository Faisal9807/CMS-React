// src/pages/SupportPage.js
import React, { useState } from "react";
import "../CSS/SupportPage.css";
import axios from "axios";

export function SupportPage() {
  const [formData, setFormData] = useState({
    name: "",
    phone: "",
    email: "",
    message: ""
  });

  const [message, setMessage] = useState(""); // For success messages
  const [errors, setErrors] = useState("");   // For error messages

  // Handler for input changes to update formData state
  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prevFormData => ({
      ...prevFormData,
      [name]: value
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault(); // Prevent default form submission behavior

    // Clear previous messages/errors
    setMessage("");
    setErrors("");

    const token = localStorage.getItem("token");

    if (!token) {
      setErrors("No authentication token found. Please log in.");
      return;
    }

    try {
      const response = await axios.post(
        "http://localhost:8081/api/members/contactUs",
        // Send the actual formData object directly.
        // The keys (name, phone, email, message) will match your ContactUsRequest DTO.
        formData,
        {
          // Fix: 'header' should be 'headers' (plural)
          headers: {
            'Authorization': `Bearer ${token}`, // Use the retrieved token
            'Content-Type': 'application/json' // Crucial for sending JSON body
          }
        }
      );

      // Handle successful response
      console.log("Contact Us form submitted successfully:", response.data);
      // Assuming Spring Boot returns a string message, or a simple success indicator
      setMessage(response.data || "Your details are submitted successfully!");
      setFormData({ // Optionally clear the form after successful submission
        name: "",
        phone: "",
        email: "",
        message: ""
      });

    } catch (error) {
      // Handle errors
      console.error("Error submitting Contact Us form:", error);

      if (error.response) {
        // The request was made and the server responded with a status code
        // that falls out of the range of 2xx
        console.error("Server responded with:", error.response.status, error.response.data);
        if (error.response.status === 401 || error.response.status === 403) {
          setErrors("Authentication failed or you are not authorized. Please log in again.");
        } else if (error.response.data && error.response.data.message) {
          // Assuming your Spring Boot backend returns an error message in a 'message' field
          setErrors(`Submission failed: ${error.response.data.message}`);
        } else {
          setErrors(`Submission failed with status: ${error.response.status}. Please try again.`);
        }
      } else if (error.request) {
        // The request was made but no response was received
        console.error("No response received:", error.request);
        setErrors("No response from server. Please check your network or try again later.");
      } else {
        // Something happened in setting up the request that triggered an Error
        console.error("Error setting up request:", error.message);
        setErrors("An unexpected error occurred. Please try again.");
      }
    }
  };

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
              <p><strong>Telephone:</strong> <a href="tel:+917576060800">(+91) 757-6060-800</a></p>
              <p><strong>Sales:</strong> <a href="tel:+917576060900">(+91) 757-6060-900</a></p>
              <p><strong>Support:</strong> <a href="tel:+917576060700">(+91) 757-6060-700</a></p>
            </div>
          </div>

          <div className="support-form">
            <form onSubmit={handleSubmit}> {/* Attach handleSubmit to the form */}
              <input
                type="text"
                name="name"
                placeholder="Your Name"
                maxLength="50"
                value={formData.name} // Bind value to state
                onChange={handleChange} // Update state on change
                required // Make field required
              />
              <input
                type="tel"
                name="phone"
                placeholder="Phone Number"
                maxLength="25"
                value={formData.phone} // Bind value to state
                onChange={handleChange} // Update state on change
                required // Make field required
              />
              <input
                type="email"
                name="email"
                placeholder="Email Address"
                maxLength="50"
                value={formData.email} // Bind value to state
                onChange={handleChange} // Update state on change
                required // Make field required
              />
              <textarea
                name="message"
                placeholder="Message"
                rows="6"
                value={formData.message} // Bind value to state
                onChange={handleChange} // Update state on change
                required // Make field required
              ></textarea>
              <button type="submit">Send</button>
            </form>
            <p className="disclaimer">*** Claim is not the insurance company and does not provide claim status information.</p>

            {/* Display messages and errors */}
            {message && <p className="success-message">{message}</p>}
            {errors && <p className="error-message">{errors}</p>}
          </div>
        </div>
      </section>
    </main>
  );
}

export default SupportPage; // Ensure default export matches function name