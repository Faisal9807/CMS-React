import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';

import LoginPage from './components/MemberLoginPage';
import RegisterPage from './components/MemberRegistractionPage';
import WelcomePage from './components/WelcomePage';
import HomePage from './components/HomePage';
import SupportPage from './components/SupportPage';
import Layout from './components/Layout';
import { AuthProvider } from './components/AuthContext';
import ServicePage from './components/ServicePage';
import ClaimRequestForm from './components/ClaimRequestForm';
import ClaimListPage from './components/ClaimListPage';
import ForgotPasswordPage from './components/ForgotPasswordPage';
import ResetPasswordPage from './components/ResetPasswordPage';

 // 

function App() {
  return (
    <AuthProvider> {/* ✅ Wrap your app in AuthProvider */}
      <Router>
        <Routes>
          {/* Route without shared layout */}
          <Route path="/" element={<WelcomePage />} />

          {/* Routes that use the shared Layout */}
          <Route element={<Layout />}>
            <Route path="/home" element={<HomePage />} />
            <Route path="/login" element={<LoginPage />} />
            <Route path="/register" element={<RegisterPage />} />
            <Route path="/support" element={<SupportPage />} />
            <Route path='/service' element={<ServicePage/>}/>
            <Route path='/submitClaim' element={<ClaimRequestForm/>}/>
            <Route path="/my-claims" element={<ClaimListPage/>} />
            <Route path='/forget' element={<ForgotPasswordPage/>}/>
             <Route path="/reset" element={<ResetPasswordPage/>} /> 
          </Route>
        </Routes>
      </Router>
    </AuthProvider>
  );
}

export default App;
