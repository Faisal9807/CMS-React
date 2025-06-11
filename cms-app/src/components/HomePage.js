import React from "react";
import { motion, useScroll, useTransform } from "framer-motion";
import { useNavigate } from "react-router-dom";
import "../CSS/LandingPage.css";

const pageVariants = {
  hidden: { opacity: 0 },
  visible: { opacity: 1, transition: { duration: 0.6 } },
  exit: { opacity: 0, transition: { duration: 0.3 } }
};

const sectionVariants = {
  hidden: { opacity: 0, y: 30 },
  visible: (i = 1) => ({
    opacity: 1,
    y: 0,
    transition: {
      delay: i * 0.2,
      duration: 0.6
    }
  })
};

function HomePage() {
  const navigate = useNavigate();

  const { scrollYProgress } = useScroll();
  const backgroundY = useTransform(scrollYProgress, [0, 1], ["0%", "-20%"]);

  const keyFeatures = [
    {
      title: "Streamlined Claim Corrections",
      desc: "Quickly receive and manage rejected claims for fast corrections, reducing delays and confusion.",
    },
    {
      title: "Integrated Support Tools",
      desc: "Access personalized assistance directly within the system. Open support tickets from any claim or inquiry with ease, backed by our dedicated team.",
    },
    {
      title: "Flexible Claims Submission",
      desc: "Submit claims individually or in bulk with support for multiple file formats, simplifying your workflow.",
    },
    {
      title: "Comprehensive Payment Details",
      desc: "View, download, or print detailed electronic payment reports for clear and accurate remittance information.",
    },
    {
      title: "Real-Time Eligibility Checks",
      desc: "Instantly verify insurance eligibility with our seamless online portal and API access.",
    },
    {
      title: "No Additional Software Needed",
      desc: "Create and manage claims entirely online with intuitive forms and real-time validation—no installations required.",
    },
  ];

  return (
    <motion.div
      className="page-wrapper light" // always light mode
      initial="hidden"
      animate="visible"
      exit="exit"
      variants={pageVariants}
    >
      <motion.div
        className="background-gradient"
        style={{ y: backgroundY }}
      />
      <main className="main">
        <motion.div
          className="hero"
          variants={sectionVariants}
          initial="hidden"
          whileInView="visible"
          viewport={{ once: true }}
          custom={1}
        >
          <motion.h2 className="hero-title" whileHover={{ scale: 1.05, rotate: 2 }}>
            Welcome to Claim Management System
          </motion.h2>
          <motion.p className="hero-subtitle" whileHover={{ scale: 1.03 }}>
            Efficiently manage your insurance claims with ease and transparency.
          </motion.p>
        </motion.div>

        <motion.section
          className="features"
          initial="hidden"
          whileInView="visible"
          viewport={{ once: true }}
          variants={{ visible: { transition: { staggerChildren: 0.2 } } }}
        >
          {["Submit a Claim", "Track Status", "Support"].map((title, index) => (
            <motion.div
              key={index}
              className="feature-card"
              variants={sectionVariants}
              custom={index + 2}
              whileHover={{ scale: 1.07, rotate: 2 }}
              onClick={() => {
                if (title === "Support") navigate("/support");
                else if (title === "Submit a Claim") navigate("/submitClaim");
                else if(title==="Track Status") navigate("/my-claims")
              }}
              style={{
                cursor:
                  title === "Support" || title === "Submit a Claim" || title === "Track Status"
                    ? "pointer"
                    : "default"
              }}
            >
              <h3 className="feature-title">{title}</h3>
              <p>
                {title === "Submit a Claim" && "Start a new claim quickly with our guided process."}
                {title === "Track Status" && "Check the progress of your existing claims in real-time."}
                {title === "Support" && "Reach out to our support team for any help or guidance."}
              </p>
            </motion.div>
          ))}
        </motion.section>

        {/* Key Features Section */}
        <motion.section
          className="key-features"
          initial="hidden"
          whileInView="visible"
          viewport={{ once: true }}
          variants={{ visible: { transition: { staggerChildren: 0.2 } } }}
          style={{ marginTop: "3rem" }}
        >
          <motion.h2
            className="section-title"
            variants={sectionVariants}
            custom={5}
            style={{ marginBottom: "1.5rem", textAlign: "center" }}
          >
            Key Features at a Glance
          </motion.h2>

          {keyFeatures.map((feature, i) => (
            <motion.div
              key={i}
              className="feature-card"
              variants={sectionVariants}
              custom={i + 6}
              whileHover={{ scale: 1.07, rotate: 2 }}
              style={{ marginBottom: "1rem" }}
            >
              <h3 className="feature-title">{feature.title}</h3>
              <p>{feature.desc}</p>
            </motion.div>
          ))}
        </motion.section>
      </main>
    </motion.div>
  );
}

export default HomePage;
