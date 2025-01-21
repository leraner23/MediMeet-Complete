document.addEventListener("DOMContentLoaded", () => {
    const form = document.getElementById("UpdateDoctorAdmin");

    form.addEventListener("submit", (event) => {
        event.preventDefault(); // Prevent default form submission behavior

        // Simulate submission success or error (replace this logic with actual server response handling)
        const isSubmissionSuccessful = simulateServerResponse();

        if (isSubmissionSuccessful) {
            showSuccessPopup("Updated successful!");
        } else {
            showErrorPopup("An error occurred while updating the form. Please try again.");
        }
    });

    function simulateServerResponse() {
        // Simulate a success or failure response (use actual server response logic here)
        return Math.random() > 0.5; // 50% chance of success or failure
    }

    function showErrorPopup(message) {
        const overlay = createOverlay();
        const popup = createPopup("Error", message, "red");

        const okButton = createButton("OK", "red");
        okButton.addEventListener("click", () => {
            overlay.remove();
            popup.remove();
        });

        popup.appendChild(okButton);
        document.body.appendChild(popup);
    }

    function showSuccessPopup(message) {
        const overlay = createOverlay();
        const popup = createPopup("Success", message, "green");

        const okButton = createButton("OK", "green");
        okButton.addEventListener("click", () => {
            overlay.remove();
            popup.remove();
            form.submit(); // Submit the form after the popup is closed
        });

        popup.appendChild(okButton);
        document.body.appendChild(popup);
    }

    function createOverlay() {
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
        return overlay;
    }

    function createPopup(titleText, messageText, color) {
        const popup = document.createElement("div");
        popup.className = "popup";
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
        title.textContent = titleText;
        title.style.marginBottom = "10px";
        title.style.color = color;
        popup.appendChild(title);

        const message = document.createElement("p");
        message.textContent = messageText;
        message.style.margin = "0";
        popup.appendChild(message);

        return popup;
    }

    function createButton(text, color) {
        const button = document.createElement("button");
        button.textContent = text;
        button.style.marginTop = "15px";
        button.style.padding = "8px 15px";
        button.style.border = "none";
        button.style.borderRadius = "5px";
        button.style.backgroundColor = color;
        button.style.color = "white";
        button.style.cursor = "pointer";
        button.style.fontSize = "1em";
        return button;
    }
});
