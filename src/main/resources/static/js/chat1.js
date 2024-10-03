let currentContact = null;
let currentAdress = null;
const messagesByContact = {};

function selectContact(contactName, contactAdress) {
    currentContact = contactName;
    currentAdress = contactAdress;
    document.getElementById('contact-name').textContent = contactName;
    document.getElementById('message-input').disabled = false;
    document.querySelector('.message-send-btn').disabled = false;
    const destinatario = {
        nome: contactName,
        endereco: contactAdress
    }
    const chatMessages = document.getElementById("chat-messages");
    chatMessages.innerHTML = "";

    // Fazer requisição AJAX para buscar as mensagens do contato
    fetch(`/chat/mensagens`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(destinatario),
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Erro ao buscar mensagens');
            }
            return response.json(); // Retorna o JSON contendo as mensagens
        })
        .then(data => {
            // Inserir as mensagens no chat
            data.forEach(mensagem => {
                const messageElement = document.createElement("div");
                messageElement.classList.add("chat-message", "message-sender");
                messageElement.textContent = mensagem.conteudo; // Exibe o conteúdo da mensagem
                chatMessages.appendChild(messageElement);
            });

            chatMessages.scrollTop = chatMessages.scrollHeight; // Rola a tela para a última mensagem
        })
        .catch(error => {
            console.error('Erro ao carregar mensagens:', error);
        });
}

function sendMessage() {
    const messageInput = document.getElementById("message-input");
    const messageText = messageInput.value;

    if (messageText.trim() !== "") {
        const chatMessages = document.getElementById("chat-messages");

        const messageElement = document.createElement("div");
        messageElement.classList.add("chat-message", "message-sender");
        messageElement.textContent = messageText;

        chatMessages.appendChild(messageElement);
        chatMessages.scrollTop = chatMessages.scrollHeight;

        if (!messagesByContact[currentContact]) {
            messagesByContact[currentContact] = [];
        }
        messagesByContact[currentContact].push(messageText);

        // Envio da mensagem via AJAX
        const remetente = {
            nome: currentContact, // Substitua pelo nome do remetente
            endereco: currentAdress // Substitua pelo endereço do remetente
        };
        const destinatario = {
            nome: currentContact,
            endereco: currentAdress // Substitua pelo endereço do destinatário
        };

        // Dados a serem enviados
        const payload = {
            remetente: remetente,
            destinatario: destinatario,
            mensagem: messageText
        };

        // Envio da requisição AJAX
        fetch('/chat/enviar', { // Certifique-se de que a URL está correta
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(payload),
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Erro ao enviar a mensagem');
                }
                return response.json();
            })
            .then(data => {
                console.log('Mensagem enviada com sucesso:', data);
                messageInput.value = ""; // Limpa o campo de entrada após o envio
            })
            .catch((error) => {
                console.error('Erro:', error);
            });
    }
}
