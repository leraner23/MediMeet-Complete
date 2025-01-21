document.addEventListener("DOMContentLoaded", () => {
    const form = document.getElementById("registrationForm");

    form.addEventListener("submit", (event) => {
        event.preventDefault();
        document.querySelectorAll(".error-message").forEach(msg => msg.remove());

        let isValid = true;
        const fullName = document.getElementById("fullName");
        const email = document.getElementById("email");
        const password = document.getElementById("password");
        const confirmPassword = document.getElementById("Cpassword");
        const age = document.getElementById("age");
        const genderRadios = document.querySelectorAll('input[name="gender"]');
        const contact = document.getElementById("contact");
        const address = document.getElementById("address");
        const profile = document.getElementById("profile");

        // Validate full name
        const fullNameValue = fullName.value.trim();
        const nameRegex = /^[a-zA-Z ]{5,50}$/;
        if (!nameRegex.test(fullNameValue)) {
            displayError(
                fullName,
                "Full name must contain only letters and spaces, and be between 5-50 characters long."
            );
            isValid = false;
        }

        // Validate email (general email format)
        const emailValue = email.value.trim().toLowerCase();
                const emailRegex = /^[a-z0-9]+[a-z0-9._]*[a-z0-9]+@gmail\.com$/;
                if (!emailRegex.test(emailValue)) {
                    displayError(email, "Please enter a valid Gmail address (e.g., example@gmail.com). Special characters are only allowed in the middle.");
                    isValid = false;
                }

        // Validate password
        const passwordValue = password.value.trim();
        const confirmPasswordValue = confirmPassword.value.trim();
        const passwordRegex = /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d@#$]{8,}$/;

        if (!passwordRegex.test(passwordValue)) {
            displayError(
                password,
                "Password must be at least 8 characters long, contain at least one letter and one number. Only @, #, $ special characters are allowed."
            );
            isValid = false;
        } else if (passwordValue !== confirmPasswordValue) {
            displayError(confirmPassword, "Passwords do not match.");
            isValid = false;
        }

        // Validate gender
        if (![...genderRadios].some(radio => radio.checked)) {
            displayError(
                genderRadios[0].closest(".form-group"),
                "Please select a gender."
            );
            isValid = false;
        }

        // Validate age (for doctors, typically between 25-70)
        const ageValue = parseInt(age.value);
        if (isNaN(ageValue) || ageValue < 18 || ageValue > 70) {
            displayError(age, "Age must be between 18 and 70 years.");
            isValid = false;
        }

        // Validate contact number
         const contactValue = contact.value.trim();
                const contactRegex = /^\+977-?[9][678]\d{8}$/;

                if (!contactRegex.test(contactValue)) {
                    displayError(contact, "Please enter a valid Nepali phone number (+977 followed by a 10-digit number starting with 98, 97, or 96)");
                    isValid = false;
                } else if (/(\d)\1{4}/.test(contactValue.slice(-10))) { // Check repeating digits in the actual number
                    displayError(contact, "Phone number cannot have same digit repeated more than 4 times.");
                    isValid = false;
                }

       const addressValue = address.value.trim();
               const addressRegex = /^[a-zA-Z ]+$/;  // Only letters, numbers, and spaces allowed

               if (addressValue.length < 5 || addressValue.length > 60) {
                   displayError(address, "Address must be between 5 and 60 characters.");
                   isValid = false;
               } else if (!addressRegex.test(addressValue)) {
                   displayError(address, "Address can only contain letters, numbers, and spaces.");
                   isValid = false;
               }

        // Validate profile photo
        const allowedTypes = ['image/jpeg', 'image/png', 'image/jpg'];
        if (!profile.files.length) {
            displayError(profile, "Please select a profile photo.");
            isValid = false;
        } else if (!allowedTypes.includes(profile.files[0].type)) {
            displayError(profile, "Please upload only JPG, JPEG or PNG images.");
            isValid = false;
        }

        if (!isValid) {
            showErrorPopup("Please correct the highlighted errors before proceeding.");
            return;
        }

        // If all validations pass, submit the form
        form.submit();
    });

    function displayError(element, message) {
        const error = document.createElement("div");
        error.className = "error-message";
        error.style.color = "red";
        error.style.fontSize = "0.9em";
        error.textContent = message;

        const parent = element.closest('.form-group');
        const existing = parent.querySelector('.error-message');
        if (existing) {
            existing.remove();
        }
        parent.appendChild(error);
    }

    function showErrorPopup(message) {
        const overlay = document.createElement("div");
        overlay.className = "overlay";
        overlay.style.position = "fixed";
        overlay.style.top = "0";
        overlay.style.left = "0";
        overlay.style.width = "100%";
        overlay.style.height = "100%";
        overlay.style.backgroundColor = "rgba(0, 0, 0, 0.5)";
        overlay.style.zIndex = "999";
        document.body.appendChild(overlay);

        const popup = document.createElement("div");
        popup.className = "error-popup";
        popup.style.position = "fixed";
        popup.style.top = "30%";
        popup.style.left = "50%";
        popup.style.transform = "translate(-50%, -50%)";
        popup.style.padding = "20px";
        popup.style.backgroundColor = "white";
        popup.style.color = "black";
        popup.style.fontSize = "1.2em";
        popup.style.borderRadius = "8px";
        popup.style.boxShadow = "0 4px 15px rgba(0, 0, 0, 0.5)";
        popup.style.zIndex = "1000";

        const title = document.createElement("h3");
        title.textContent = "Error";
        title.style.marginBottom = "10px";
        title.style.color = "red";
        popup.appendChild(title);

        const messageText = document.createElement("p");
        messageText.textContent = message;
        messageText.style.margin = "0";
        popup.appendChild(messageText);

        const okButton = document.createElement("button");
        okButton.textContent = "OK";
        okButton.style.marginTop = "15px";
        okButton.style.padding = "8px 15px";
        okButton.style.border = "none";
        okButton.style.borderRadius = "5px";
        okButton.style.backgroundColor = "red";
        okButton.style.color = "white";
        okButton.style.cursor = "pointer";
        okButton.style.fontSize = "1em";

        okButton.addEventListener("click", () => {
            overlay.remove();
            popup.remove();
        });

        popup.appendChild(okButton);
        document.body.appendChild(popup);
    }
});