<script>
    document.addEventListener('DOMContentLoaded', () => {
        const doctorSelect = document.getElementById('doctor');
        const dateInput = document.getElementById('date');
        const timeSelect = document.getElementById('appointment-time');

        // Disable the dropdown initially
        timeSelect.disabled = true;

        async function updateAvailableTimes() {
            const doctorId = doctorSelect.value;
            const date = dateInput.value;

            // Check if both doctor and date are selected
            if (!doctorId || !date) {
                timeSelect.disabled = true;
                timeSelect.innerHTML = '<option value="" disabled selected>Select an appointment time</option>';
                return;
            }

            try {
                const response = await fetch(`/appointment/available-times?doctorId=${doctorId}&date=${date}`);
                const availableTimes = await response.json();

                timeSelect.innerHTML = '<option value="" disabled selected>Select an appointment time</option>';

                if (availableTimes.length > 0) {
                    timeSelect.disabled = false;
                    availableTimes.forEach(time => {
                        const option = document.createElement('option');
                        option.value = time;
                        option.textContent = time;
                        timeSelect.appendChild(option);
                    });
                } else {
                    timeSelect.disabled = true;
                    timeSelect.innerHTML = '<option value="" disabled>No times available</option>';
                }
            } catch (error) {
                console.error('Error fetching available times:', error);
            }
        }

        doctorSelect.addEventListener('change', updateAvailableTimes);
        dateInput.addEventListener('change', updateAvailableTimes);
    });
</script>
