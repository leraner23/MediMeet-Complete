document.addEventListener("DOMContentLoaded", function () {
    const updateButton = document.getElementById("updateReportBtn");
    const popupOverlay = document.getElementById("popupOverlay");
    const closePopupButton = document.getElementById("closePopupBtn");

    // Show the popup when the update button is clicked
    updateButton.addEventListener("click", function () {
        const reportText = document.getElementById("report").value.trim();

        if (reportText === "") {
            alert("Please enter a report before updating.");
            return;
        }

        // Simulate the update process (replace this with actual AJAX call if needed)
        setTimeout(() => {
            popupOverlay.classList.remove("hidden");
        }, 200);
    });

    // Hide the popup when the "Okay" button is clicked
    closePopupButton.addEventListener("click", function () {
        popupOverlay.classList.add("hidden");
    });
});