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

function createPost() {
    var form = document.getElementById('newpost-form');
    var formData = new FormData(form);

    var xhr = new XMLHttpRequest();
    xhr.onreadystatechange = function() {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            if (xhr.status === 200) {
                // Request successful
                var newPostHTML = xhr.responseText;

                // Create a temporary container to hold the new post HTML
                var tempContainer = document.createElement('div');
                tempContainer.innerHTML = newPostHTML;

                // Get the first child (new post) from the temporary container
                var newPost = tempContainer.firstChild;

                // Insert the new post at the top of the posts container
                var postsContainer = document.querySelector('.col-md-9');
                postsContainer.insertBefore(newPost, postsContainer.firstChild);

                // Clear the textarea
                form.querySelector('textarea[name="content"]').value = '';
            } else {
                // Request failed
                console.error('Failed to create a new post.');
            }
        }
    };

    xhr.open(form.method, form.action, true);
    xhr.setRequestHeader('X-Requested-With', 'XMLHttpRequest');
    xhr.send(formData);
}
