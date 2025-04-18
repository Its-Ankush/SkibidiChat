// import { ClientMessage, ServerMessage } from '/proto.js';
let isAutoScrollEnabled = true;
const toggleButton = document.getElementById('toggle-autoscroll-button');
const messagesContainer = document.getElementById('messages');

function getCookie(name) {
    
    let cookieArr = document.cookie.split(";");

    for(let i = 0; i < cookieArr.length; i++) {
        let cookiePair = cookieArr[i].split("=");


        if(name == cookiePair[0].trim()) {
            
            return decodeURIComponent(cookiePair[1]);
        }
    }

    return null;
}


toggleButton.addEventListener('click', toggleAutoScroll);

function scrollToBottom() {
    setTimeout(() => {
        messagesContainer.scrollTop = messagesContainer.scrollHeight;
    }, 0);
}




function toggleAutoScroll() {
    isAutoScrollEnabled = !isAutoScrollEnabled; // Flip the state
    toggleButton.textContent = isAutoScrollEnabled ? 'Disable Auto-Scroll' : 'Enable Auto-Scroll';

    if (isAutoScrollEnabled) {
        scrollToBottom();
    }
}


const jwt=getCookie("jwt");
console.log(window.location.host);
const socket = new WebSocket(`wss://${window.location.host}/ws?token=${jwt}`);
// socket.binaryType = 'arraybuffer'

let inputElement; 
let buttonElement; 

// When connection opens
socket.onopen = () => {
    document.getElementById('status').textContent = 'Connected. Type below:';

    // Create input elements after connection
    inputElement = document.createElement('input');
    inputElement.type = 'text';
    inputElement.id = 'messageInput';
    inputElement.placeholder = 'Type message here';

    buttonElement = document.createElement('button');
    buttonElement.id ='sendButton'
    buttonElement.textContent = 'Send';
    buttonElement.onclick = () => {
        if (socket.readyState === WebSocket.OPEN) {

            
            socket.send(inputElement.value);
            inputElement.value = '';
        }
    };

    const inputArea = document.createElement('div');
    inputArea.classList.add('input-area');
    inputArea.appendChild(inputElement);
    inputArea.appendChild(buttonElement);

    document.body.appendChild(inputArea);
};

// Message send from jetty - [pulled from redis streams. Sadly need to loop :( ]
socket.onmessage = (event) => {
    const jsonResponse = JSON.parse(event.data);

    const toAppendUsername = jsonResponse.username;
    const message = jsonResponse.message;
    //  Did not manage to make this work. I hope textContent handles it lol
    // const clean = DOMPurify.sanitize(message);
    // console.log(clean);
    const isHistory = jsonResponse.history;

    const msgDiv = document.createElement('div');
    msgDiv.classList.add('message');

    const usernameSpan = document.createElement('span');
    usernameSpan.classList.add('username');
    usernameSpan.textContent = toAppendUsername + ":";

    const messageContentSpan = document.createElement('span');
    messageContentSpan.classList.add('message-content');
    messageContentSpan.textContent = message;

    msgDiv.appendChild(usernameSpan);
    msgDiv.appendChild(messageContentSpan);

    

    if (isHistory==="true") {
        messagesContainer.prepend(msgDiv);
    } else {
        messagesContainer.appendChild(msgDiv);
    }
    if (isAutoScrollEnabled) {
        scrollToBottom();
    }
};


socket.onerror = (error) => {
    document.getElementById('status').textContent = 'Connection failed.';
};


socket.onclose = () => {
    document.getElementById('status').textContent = 'Connection closed. Refresh or Login again';
};

// Send on Enter key
document.addEventListener('keypress', (e) => {
    if (e.key === 'Enter' && document.getElementById('messageInput')) {
        document.getElementById("sendButton").click();
    }
});