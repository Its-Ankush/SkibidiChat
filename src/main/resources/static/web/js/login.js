
document.getElementById('loginForm').addEventListener('submit', function(event) {
    
    event.preventDefault();
    const username = document.getElementById('username').value;
    const password = document.getElementById('password').value;
    const messageElement = document.getElementById('message');

    console.log(username);
    fetch('login', {
        method: 'POST',
        credentials:'include',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({
            username: username,
            password: password
        }),
    })
    .then(async response => {
        if (response.ok) {
            return response.text();
        } else {


            const errorData = await response.json();
            // console.log(errorData);
            throw new Error(errorData.response || 'Login Failed');

        }
    })
    .then(data => {

        messageElement.textContent = 'Good, you are in!';
        window.location.href = '/chat';

    })
    .catch(error => {
        messageElement.textContent = error.message;
    });
});