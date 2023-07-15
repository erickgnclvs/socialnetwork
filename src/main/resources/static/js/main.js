function displayWarningMessage(message) {
    // Select the warning message element
    let warningElement = document.querySelector("#warning-message");
    // Set the text content of the warning element to the message
    warningElement.textContent = message;
    // Show the warning element
    warningElement.style.display = "block";
}

window.addEventListener("load", function() {
    if (authFailed) {
        displayWarningMessage("Invalid username or password");
    }
});

window.addEventListener("load", function () {
    if (registrationFailed) {
        displayWarningMessage(error);
    }
});