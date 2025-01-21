  document.addEventListener("DOMContentLoaded", () => {
        const form = document.getElementById("LoginForm");

        form.addEventListener("submit", async (event) => {
            event.preventDefault(); // Prevent default form submission

            // Get form data
            const email = document.getElementById("email").value.trim();
            const password = document.getElementById("password").value.trim();

            // Validate that terms checkbox is checked
            const terms = document.getElementById("terms");
            if (!terms.checked) {
                showErrorPopup("Please agree to the Terms and Conditions.");
                return;
            }

            try {
                // Send credentials to the server for validation
                const response = await fetch("/user/login", {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json",
                    },
                    body: JSON.stringify({ email, password }),
                });

                if (response.ok) {
                    const data = await response.json();
                    if (data.success) {
                        showSuccessPopup("Login successful!", () => {
                            // Redirect to dashboard or desired page
                            window.location.href = "/dashboard";
                        });
                    } else {
                        showErrorPopup(data.message || "Invalid email or password.");
                    }
                } else {
                    showErrorPopup("An error occurred while processing your request.");
                }
            } catch (error) {
                showErrorPopup("Network error. Please try again later.");
            }
        });

        function showErrorPopup(message) {
            const popup = createPopup(message, "Error", "red");
            document.body.appendChild(popup);
        }

        function showSuccessPopup(message, callback) {
            const popup = createPopup(message, "Success", "green");
            document.body.appendChild(popup);
            const okButton = popup.querySelector(".popup-button");
            okButton.addEventListener("click", () => {
                popup.remove();
                if (callback) callback();
            });
        }

        function createPopup(message, title, color) {
            const overlay = document.createElement("div");
            overlay.style.position = "fixed";
            overlay.style.top = "0";
            overlay.style.left = "0";
            overlay.style.width = "100%";
            overlay.style.height = "100%";
            overlay.style.backgroundColor = "rgba(0, 0, 0, 0.5)";
            overlay.style.zIndex = "999";

            const popup = document.createElement("div");
            popup.style.position = "fixed";
            popup.style.top = "30%";
            popup.style.left = "50%";
            popup.style.transform = "translate(-50%, -50%)";
            popup.style.backgroundColor = "white";
            popup.style.padding = "20px";
            popup.style.borderRadius = "8px";
            popup.style.boxShadow = "0 4px 15px rgba(0, 0, 0, 0.5)";
            popup.style.zIndex = "1000";
            popup.style.textAlign = "center";

            const titleEl = document.createElement("h3");
            titleEl.textContent = title;
            titleEl.style.color = color;
            popup.appendChild(titleEl);

            const messageEl = document.createElement("p");
            messageEl.textContent = message;
            popup.appendChild(messageEl);

            const okButton = document.createElement("button");
            okButton.textContent = "OK";
            okButton.className = "popup-button";
            okButton.style.marginTop = "15px";
            okButton.style.padding = "10px 20px";
            okButton.style.border = "none";
            okButton.style.borderRadius = "5px";
            okButton.style.backgroundColor = color;
            okButton.style.color = "white";
            okButton.style.cursor = "pointer";
            okButton.style.fontSize = "1em";
            popup.appendChild(okButton);

            overlay.appendChild(popup);
            okButton.addEventListener("click", () => overlay.remove());
            return overlay;
        }
    });