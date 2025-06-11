import React from 'react';
import { Outlet } from 'react-router-dom';
import Header from './Header';
import Footer from './Footer';
import { motion, useScroll, useTransform } from 'framer-motion';
import '../CSS/Layout.css';

function Layout() {
  const { scrollYProgress } = useScroll();
  const backgroundY = useTransform(scrollYProgress, [0, 1], ["0%", "-20%"]);
  return (
    <div className="layout">
      <motion.div
        className="background-gradient"
        style={{ y: backgroundY }}
      />
      <Header />
      <Outlet />
      <Footer />
    </div>
  );
}

export default Layout;
