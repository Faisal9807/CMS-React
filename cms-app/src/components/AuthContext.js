import { createContext, useContext, useState, useEffect } from "react";
import { jwtDecode } from "jwt-decode";

const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const [showProfile, setShowProfile] = useState(false);

  useEffect(() => {
    const token = localStorage.getItem("token");
    const sessionStarted = sessionStorage.getItem("session-started");

    // ✅ First load after `npm start`
    if (!sessionStarted) {
      sessionStorage.setItem("session-started", "true");
      localStorage.removeItem("token");
      setUser(null);
      return;
    }
    if (token) {
      try {
        const decoded = jwtDecode(token);
        setUser({
          isLoggedIn: true,
          name: decoded.name || decoded.sub,
          role: decoded.role,
          email: decoded.email,
          Id: decoded.Id,
        });
        setShowProfile(false); 
      } catch (err) {
        console.error("Invalid token:", err);
        localStorage.removeItem("token");
        setUser(null);
      }
    }
  }, []);

  return (
    <AuthContext.Provider value={{ user, setUser }}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => useContext(AuthContext);
