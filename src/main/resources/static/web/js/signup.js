
document.addEventListener('DOMContentLoaded', () => {

    const signupForm = document.getElementById('signupForm');
    const usernameInput = document.getElementById('username');
    const passwordInput = document.getElementById('password');


    const usernameHint = document.getElementById('username-hint');
    const passwordHint = document.getElementById('password-hint');

    const setupHintToggling = (inputElement, hintElement) => {
        if (inputElement && hintElement) { 
            inputElement.addEventListener('focus', () => {
                hintElement.style.display = 'block'; 
            });

            inputElement.addEventListener('blur', () => {
                hintElement.style.display = 'none'; 
            });
        }
    };
    setupHintToggling(usernameInput, usernameHint);
    setupHintToggling(passwordInput, passwordHint);


    signupForm.addEventListener('submit', function(event) {
    
    event.preventDefault();
    const username = document.getElementById('username').value;
    const password = document.getElementById('password').value;
    const messageElement = document.getElementById('message');

    // console.log(username);
    fetch('signup', {
        method: 'POST',
        credentials:'include',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({
            username: username,
            password: password,
        }),
    })
    .then(async response => {
        if (response.ok) {
            return response.text();
        } else {


            const errorData = await response.json();
            // console.log(errorData);
            throw new Error(errorData.response || 'Signup Failed');


        }
    })
    .then(data => {
        messageElement.textContent = 'Signup success. Redirecting to login page';

        setTimeout(() => {
            window.location.href = '/login'; // Redirect to login page
        }, 2800);

        

    })
    .catch(error => {
        messageElement.textContent = error.message;
    });
});
});