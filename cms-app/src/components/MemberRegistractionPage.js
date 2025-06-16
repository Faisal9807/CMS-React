import React, { useState } from 'react';
import '../CSS/RegisterPage.css'; // Ensure this path is correct

function RegisterPage() {
    const [formData, setFormData] = useState({
        firstName: "",
        lastName: "",
        age: "",
        email:"", // Changed to empty string to easily handle required validation
        gender: "",
        contactNumber: "",
        memberId: "",
        password: ""
    });

    const [message, setMessage] = useState(''); // To display success or error messages from API
    const [errors, setErrors] = useState({}); // To store client-side validation errors

    // Client-side validation function
    const validateForm = () => {
        let newErrors = {};
        let isValid = true;

        if (!formData.firstName.trim()) {
            newErrors.firstName = 'First Name is required.';
            isValid = false;
        }
        if (!formData.lastName.trim()) {
            newErrors.lastName = 'Last Name is required.';
            isValid = false;
        }
        // Basic age validation: must be a number, greater than 0, and not empty
        const ageNum = parseInt(formData.age);
        if (!formData.age.trim() || isNaN(ageNum) || ageNum <=17){
            newErrors.age = 'Your age must be 18';
            isValid = false;
        }
        if (!formData.gender) {
            newErrors.gender = 'Please select your gender.';
            isValid = false;
        }
        if(!formData.email.trim()){
            newErrors.email='Please Enter Email'
            isValid=false;
        }
        // Basic contact number validation: must be 10 digits
        if (!formData.contactNumber.trim() || !/^\d{10}$/.test(formData.contactNumber)) {
            newErrors.contactNumber = 'Please enter a valid 10-digit contact number.';
            isValid = false;
        }
        if (!formData.memberId.trim()) {
            newErrors.memberId = 'Member ID is required.'; // Assuming Member ID is required
            isValid = false;
        }
        // Password length validation
        if (!formData.password.trim() || formData.password.length < 6) {
            newErrors.password = 'Password must be at least 6 characters long.';
            isValid = false;
        }

        setErrors(newErrors); // Update the errors state
        return isValid;
    };

    const handleSubmit = async (e) => {
        e.preventDefault(); // Prevent default form submission

        setMessage(''); // Clear previous API messages
        setErrors({}); // Clear previous client-side errors

        // Run client-side validation
        if (!validateForm()) {
            setMessage('Please correct the errors in the form.'); // Generic message for client-side validation failure
            return; // Stop submission if validation fails
        }

        try {
            const response = await fetch('http://localhost:8081/api/members/register', { // Your Spring Boot API endpoint
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(formData), // formData is already correctly structured
            });

            if (response.ok) {
                const textData = await response.text(); // Assuming backend sends plain text success message
                console.log("Registration successful, received text:", textData);
                setMessage(`Registration successful! ${textData}`); // Display the success message

                // Optionally clear form data after successful registration
                setFormData({
                    firstName: "",
                    lastName: "",
                    age: "",
                    email:"",
                    gender: "",
                    contactNumber: "",
                    memberId: "",
                    password: ""
                });
            } else {
                // Try to parse error as JSON, fallback to text if JSON parsing fails
                let errorData;
                try {
                    errorData = await response.json();
                } catch (jsonError) {
                    errorData = await response.text(); // Fallback to plain text if response isn't JSON
                }

                console.error('Registration failed:', errorData);

                if (typeof errorData === 'object' && errorData !== null && errorData.message) {
                    setMessage(`Registration failed: ${errorData.message}`);
                } else if (typeof errorData === 'string' && errorData.length > 0) {
                     setMessage(`Registration failed: ${errorData}`);
                } else {
                    setMessage('Registration failed. Please try again with different details.');
                }
            }
        } catch (error) {
            setMessage('A network error occurred. Please check your connection and try again.');
            console.error('Network error or unexpected response:', error);
        }
    };

    return (
        <div className="registration-page-container">
            <div className="registration-card">
                <h2 className="registration-title">Create Your Account</h2>
                <p className="registration-subtitle">Join us to unlock exclusive offers and Secure Your Life</p>
                <form onSubmit={handleSubmit} className="registration-form">
                    {/* First Name */}
                    <div className="form-row">
                        <div className="form-group">
                        <label htmlFor="first-name">First Name</label>
                        <input
                            type="text"
                            id="first-name"
                            value={formData.firstName}
                            onChange={(e) => setFormData({ ...formData, firstName: e.target.value })}
                            aria-invalid={errors.firstName ? "true" : "false"}
                            aria-describedby="first-name-error"
                        />
                        {errors.firstName && <p id="first-name-error" className="error-text">{errors.firstName}</p>}
                        </div>

                        <div className="form-group">
                        <label htmlFor="last-name">Last Name</label>
                        <input
                            type="text"
                            id="last-name"
                            value={formData.lastName}
                            onChange={(e) => setFormData({ ...formData, lastName: e.target.value })}
                            aria-invalid={errors.lastName ? "true" : "false"}
                            aria-describedby="last-name-error"
                        />
                        {errors.lastName && <p id="last-name-error" className="error-text">{errors.lastName}</p>}
                        </div>
                    </div>

                    {/* Email (unchanged) */}
                    <div className="form-group">
                        <label htmlFor="email">Email</label>
                        <input
                        type="email"
                        id="email"
                        value={formData.email}
                        onChange={(e) => setFormData({ ...formData, email: e.target.value })}
                        aria-invalid={errors.email ? "true" : "false"}
                        aria-describedby="email-error"
                        />
                        {errors.email && <p id="email-error" className="error-text">{errors.email}</p>}
                    </div>

                    {/* Age + Gender in one row */}
                    <div className="form-row">
                        <div className="form-group">
                        <label htmlFor="age">Age</label>
                        <input
                            type="number"
                            id="age"
                            value={formData.age}
                            onChange={(e) => setFormData({ ...formData, age: e.target.value })}
                            aria-invalid={errors.age ? "true" : "false"}
                            aria-describedby="age-error"
                        />
                        {errors.age && <p id="age-error" className="error-text">{errors.age}</p>}
                        </div>

                        <div className="form-group">
                        <label htmlFor="gender">Gender</label>
                        <select
                            id="gender"
                            value={formData.gender}
                            onChange={(e) => setFormData({ ...formData, gender: e.target.value })}
                            aria-invalid={errors.gender ? "true" : "false"}
                            aria-describedby="gender-error"
                        >
                            <option value="">Select Gender</option>
                            <option value="M">Male</option>
                            <option value="F">Female</option>
                            <option value="Other">Prefer not to say</option>
                        </select>
                        {errors.gender && <p id="gender-error" className="error-text">{errors.gender}</p>}
                        </div>
                    </div>

                    {/* Member ID (If required, otherwise consider removing or making optional) */}
                     <div className="form-group">
                        <label htmlFor="contactNumber">contactNumber</label>
                        <input
                            type="text"
                            id="contactNumber"
                            value={formData.contactNumber}
                            onChange={(e) => setFormData({ ...formData, contactNumber: e.target.value })}
                            aria-invalid={errors.age ? "true" : "false"}
                            aria-describedby="contactNumber-error"
                        />
                        {errors.age && <p id="contactNumber-error" className="error-text">{errors.age}</p>}
                        </div>
                    <div className="form-group">
                        <label htmlFor="memberId">Member ID (if applicable)</label>
                        <input
                            type="text"
                            id="memberId"
                            value={formData.memberId}
                            onChange={(e) => setFormData({ ...formData, memberId: e.target.value })}
                            aria-invalid={errors.memberId ? "true" : "false"}
                            aria-describedby="memberId-error"
                        />
                        {errors.memberId && <p id="memberId-error" className="error-text">{errors.memberId}</p>}
                    </div>

                    {/* Password */}
                    <div className="form-group">
                        <label htmlFor="password">Password</label>
                        <input
                            type="password"
                            id="password"
                            value={formData.password}
                            onChange={(e) => setFormData({ ...formData, password: e.target.value })}
                            aria-invalid={errors.password ? "true" : "false"}
                            aria-describedby="password-error"
                        />
                        {errors.password && <p id="password-error" className="error-text">{errors.password}</p>}
                    </div>

                    <button type="submit" className="register-button">Register Account</button>
                </form>
                {message && (
                    <p className={`form-message ${message.includes('successful') ? 'success' : 'error'}`}>
                        {message}
                    </p>
                )}
                <div className="login-link-container">
                    <p>Already have an account? <a href="/login">Login here</a></p>
                </div>
            </div>
        </div>
    );
}

export default RegisterPage;