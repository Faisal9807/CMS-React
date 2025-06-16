import React, { useState } from "react";
import "../CSS/ClaimRequestForm.css";
import axios from "axios";

export function ClaimFormPage() {
  const [claimType, setClaimType] = useState("");
  const [description, setDescription] = useState("");
  const [amount, setAmount] = useState("");
  const [message, setMessage] = useState("");
  const [error, setError] = useState("");

  const handleSubmit = async (e) => {
    e.preventDefault();
  
    const token = localStorage.getItem("token");
  
    if (!token) {
      setError("❌ No authentication token found. Please log in.");
      return;
    }
  
    try {
      const response = await axios.post(
        "http://localhost:8081/api/member/claims/submit",
        {
          claimType,
          description,
          amount: parseFloat(amount),
        },
        {
          headers: {
            Authorization: `Bearer ${token}`
          },
        }
      );
  
      setMessage(`✅ Claim submitted successfully. ID: ${response.data.id}`);
      setClaimType("");
      setDescription("");
      setAmount("");
    } catch (err) {
      console.error("API error:", err);
      setError("❌ Failed to submit claim. Please check your input and try again.");
    }
  };
  

  return (
    <main className="claim-main">
      <section className="claim-header">
        <div className="claim-container">
          <h2 className="claim-title">Submit a New Claim</h2>
          <p className="claim-description">Fill in the details below to raise a claim.</p>
        </div>
      </section>

      <section className="claim-body">
        <div className="claim-container">
          <form className="claim-form" onSubmit={handleSubmit}>
            <input
              type="text"
              placeholder="Claim Type"
              value={claimType}
              onChange={(e) => setClaimType(e.target.value)}
              required
            />
            <textarea
              placeholder="Description"
              value={description}
              onChange={(e) => setDescription(e.target.value)}
              required
              rows="5"
            />
            <input
              type="number"
              placeholder="Amount"
              value={amount}
              onChange={(e) => setAmount(e.target.value)}
              required
            />
            <button type="submit">Submit Claim</button>

            {message && <p className="success-msg">{message}</p>}
            {error && <p className="error-msg">{error}</p>}
          </form>
        </div>
      </section>
    </main>
  );
}

export default ClaimFormPage;
