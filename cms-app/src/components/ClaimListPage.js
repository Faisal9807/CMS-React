import React, { useEffect, useState } from "react";
import axios from "axios";
import { useAuth } from "../components/AuthContext";
import "../CSS/ClaimListPage.css";
import { useNavigate } from "react-router-dom";


const ClaimListPage = () => {
  const { user } = useAuth();
  const [claims, setClaims] = useState([]);
  const [error, setError] = useState(null);
  const navigate=useNavigate();

  useEffect(() => {
    const fetchClaims = async () => {
      const token = localStorage.getItem("token");
      if (!token) {
        setError("User not authenticated.");
        return;
      }

      try {
        const response = await axios.post(
          "http://localhost:8081/api/member/claims/all",
          {}, // empty body
          {
            headers: {
              Authorization: `Bearer ${token}`,
              "Content-Type": "application/json",
            },
          }
        );
        setClaims(response.data);
      } catch (err) {
        console.error("Error fetching claims:", err);
        setError("Failed to fetch claims.");
      }
    };

    fetchClaims();
  }, []);

  if (error) return <p className="error">{error}</p>;

  return (
    <div className="claim-list-page">
      <h2>My Claims</h2>
      {claims.length === 0 ? (
        <p>No claims submitted yet.</p>
      ) : (
        <table className="claim-table">
          <thead>
            <tr>
              <th>Claim ID</th>
              <th>Member ID</th>
              <th>Type</th>
              <th>Description</th>
              <th>Amount</th>
              <th>Status</th>
              <th>Remarks</th>
            </tr>
          </thead>
          <tbody>
            {claims.map((claim) => (
              <tr key={claim.claimId}>
                <td>{claim.claimId}</td>
                <td>{claim.memberId}</td>
                <td>{claim.claimType}</td>
                <td>{claim.description}</td>
                <td>{claim.amount}</td>
                <td>{claim.status}</td>
                <td>{claim.remarks}</td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
       {/* Button Container for Alignment */}
        <div style={{ textAlign: 'right', marginTop: '20px' }}>
          <button className="btn login-btn" onClick={() => navigate("/home")}>
            Back
          </button>
        </div>
    </div>
  );
};

export default ClaimListPage;
