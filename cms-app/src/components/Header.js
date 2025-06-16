import React, { useState } from "react";
import { LogIn, LogOut, UserCircle2 } from "lucide-react";
import { motion, AnimatePresence } from "framer-motion";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../components/AuthContext"; // <-- Use the context
// import "../CSS/LandingPage.css";

const Header = () => {
  const { user, setUser } = useAuth(); // <-- Get user state from context
  const [showProfile, setShowProfile] = useState(false);
  const navigate = useNavigate();

  const handleLogout = () => {
    localStorage.removeItem("token");
    setShowProfile(false); 
    setUser(null); // Update context state
    navigate("/login");
  };

  return (
    <header className="header">
      <div className="container header-content">
        <motion.h1 className="logo" whileHover={{ scale: 1.05, rotate: -2 }}>
          ClaimManager
        </motion.h1>
        <nav className="nav">
          {["Home", "Services", "Support"].map((item, i) => (
            <motion.a
              key={i}
              href={
                item === "Home" ? "/home" :
                item === "Support" ? "/support" :
                item === "Services" ? "/service" : "#"
              }
              className="nav-link"
              whileHover={{ scale: 1.1 }}
            >
              {item}
            </motion.a>
          ))}

          {!user ? (
            <button className="btn login-btn" onClick={() => navigate("/login")}>
              <LogIn size={18} /> Login
            </button>
          ) : (
            <>
              <motion.button
                whileHover={{ scale: 1.1, rotate: 10 }}
                onClick={() => setShowProfile(!showProfile)}
                className="profile-icon"
              >
                <UserCircle2 size={28} />
              </motion.button>

              <AnimatePresence>
                {showProfile && (
                  <motion.div
                    className="profile-popup"
                    initial={{ opacity: 0, y: -10 }}
                    animate={{ opacity: 1, y: 0 }}
                    exit={{ opacity: 0, y: -10 }}
                  >
                    <h4 className="profile-title">{user.Id} Profile</h4>
                    <p><strong>Name:</strong> {user.name}</p>
                    <p><strong>Email:</strong> {user.email}</p>
                    <p><strong>Role:</strong> {user.role}</p>
                    <motion.div whileHover={{ scale: 1.1, rotate: -5 }}>
                      <button
                        className="btn logout-btn"
                        onClick={handleLogout}
                      >
                        <LogOut size={18} className="icon" /> Logout
                      </button>
                    </motion.div>
                  </motion.div>
                )}
              </AnimatePresence>
            </>
          )}
        </nav>
      </div>
    </header>
  );
};

export default Header;
