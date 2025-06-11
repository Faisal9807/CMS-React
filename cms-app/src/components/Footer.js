// src/components/Footer.js
import React from "react";
import { motion } from "framer-motion";
import "../CSS/Footer.css"; // Add your new CSS file here

const sectionVariants = {
  hidden: { opacity: 0, y: 30 },
  visible: {
    opacity: 1,
    y: 0,
    transition: { delay: 0.2, duration: 0.6 }
  }
};

function Footer() {
  return (
    <motion.footer
      initial="hidden"
      whileInView="visible"
      viewport={{ once: true }}
      variants={sectionVariants}
      className="footer"
    >
      <div className="footer-container">
        <nav className="footer-nav">
          {[
            { href: "/about", label: "About" },
            { href: "/faq", label: "FAQ" },
            { href: "/news", label: "News" },
            { href: "/services", label: "Services" },
            { href: "/pricing", label: "Pricing" },
            { href: "/payer-list", label: "Payer List" },
            { href: "/integrated-vendors", label: "Integrated Vendors" },
            { href: "/contact", label: "Contact" }
          ].map(({ href, label }, idx) => (
            <a key={idx} href={href} className="footer-link">
              {label}
            </a>
          ))}
        </nav>

        <nav className="footer-nav">
          {[
            { href: "/forgot-username", label: "Forgot Username" },
            { href: "/forgot-password", label: "Forgot Password" },
            { href: "/terms-of-service", label: "Terms of Service" },
            { href: "/privacy-policy", label: "Privacy Policy" }
          ].map(({ href, label }, idx) => (
            <a key={idx} href={href} className="footer-link">
              {label}
            </a>
          ))}
        </nav>

        <div className="footer-social">
          <a href="#" target="_blank" rel="noopener noreferrer">
            <img src="/images/social_facebook.svg" alt="Facebook" className="icon icon-facebook" />
          </a>
          <a href="#" target="_blank" rel="noopener noreferrer">
            <img src="/images/social_x.svg" alt="X" className="icon icon-x" />
          </a>
          <a href="#" target="_blank" rel="noopener noreferrer">
            <img src="/images/social_linkedin.svg" alt="LinkedIn" className="icon icon-linkedin" />
          </a>
        </div>

        <div className="footer-copy">
          © {new Date().getFullYear()} Claim.MD, Inc. All rights reserved.
        </div>

        <div className="footer-contact">
          <span className="contact-label">Have a question?</span>
          <a id="phone_conversion_number" href="tel:+18557576060" className="contact-phone">
            <span id="phone_conversion_text" title="Call us @ (855) 757-6060">+91 (800) 123-4567</span>
          </a>
        </div>
      </div>
    </motion.footer>
  );
}

export default Footer;
