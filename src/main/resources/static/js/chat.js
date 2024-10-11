// Armazena o contato atual selecionado e seu endereço
let currentContact = null;
let currentAdress = null;

// Armazena mensagens por contato, agrupadas por endereço de e-mail
const messagesByContact = {};

// IDs de mensagens já exibidas para evitar duplicação
const displayedMessageIds = new Set();

// ID do intervalo de atualização das mensagens
let messageUpdateIntervalId = null;

// Intervalo de atualização das mensagens (90 segundos)
const MESSAGE_UPDATE_INTERVAL = 90000;

/**
 * Função para iniciar a atualização periódica de mensagens.
 * Inicia o intervalo de 90 segundos para atualizar as mensagens do contato selecionado.
 */
function startMessageUpdate() {
    // Limpa o intervalo anterior, se houver
    if (messageUpdateIntervalId) {
        clearInterval(messageUpdateIntervalId);
    }

    // Inicia o intervalo para atualizar mensagens a cada 90 segundos
    messageUpdateIntervalId = setInterval(() => {
        if (currentContact) {
            fetchMessagesFromBackend(currentAdress); // Atualiza as mensagens do servidor
        }
    }, MESSAGE_UPDATE_INTERVAL);
}

/**
 * Função para selecionar um contato, carregar suas mensagens e atualizar a interface.
 * Atualiza o nome do contato exibido e habilita os campos de mensagem e botão de envio.
 *
 * @param {string} contactName - Nome do contato selecionado.
 * @param {string} contactAdress - Endereço do contato selecionado.
 */
function selectContact(contactName, contactAdress) {
    currentContact = contactName; // Atualiza o nome do contato selecionado
    currentAdress = contactAdress; // Atualiza o endereço do contato

    // Exibe o nome do contato na interface
    document.getElementById('contact-name').textContent = contactName;

    // Habilita o campo de entrada de mensagem e o botão de envio
    document.getElementById("message-input").disabled = false;
    document.querySelector(".message-send-btn").disabled = false;

    // Inicia o processo de atualização periódica de mensagens
    startMessageUpdate();

    // Busca as mensagens imediatamente ao selecionar o contato
    fetchMessagesFromBackend(contactAdress);
}

/**
 * Função para buscar mensagens do servidor por AJAX.
 * As mensagens do remetente e destinatário são exibidas na interface.
 *
 * @param {string} adress - Endereço do contato do qual buscar as mensagens.
 */
function fetchMessagesFromBackend(adress) {
    const destinatario = {
        endereco: adress
    };

    // Faz requisição ao servidor para buscar mensagens
    fetch('/chat/mensagens', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(destinatario), // Envia destinatário no corpo da requisição
    })
        .then(response => response.json()) // Converte a resposta para JSON
        .then(data => {
            const mensagensRemetente = data.mensagensRemetente || [];
            const mensagensDestinatario = data.mensagensDestinatario || [];

            // Armazena as mensagens para o contato atual
            messagesByContact[adress] = {
                mensagensRemetente,
                mensagensDestinatario
            };

            // Exibe as mensagens no chat
            displayMessages(mensagensDestinatario, mensagensRemetente);
        })
        .catch(error => {
            console.error('Erro ao buscar mensagens:', error);
        });
}

/**
 * Função para exibir mensagens na interface do chat.
 * As mensagens são organizadas por data de envio e diferenciadas entre remetente e destinatário.
 *
 * @param {Array} mensagensDestinatario - Lista de mensagens do destinatário.
 * @param {Array} mensagensRemetente - Lista de mensagens do remetente.
 */
function displayMessages(mensagensDestinatario, mensagensRemetente) {
    const chatMessages = document.getElementById("chat-messages");

    // Limpa o chat antes de adicionar novas mensagens
    chatMessages.innerHTML = "";

    // Combina e ordena as mensagens por data de envio
    const todasAsMensagens = [
        ...mensagensDestinatario.map(mensagem => ({ ...mensagem, tipo: 'destinatario' })),
        ...mensagensRemetente.map(mensagem => ({ ...mensagem, tipo: 'remetente' }))
    ];

    todasAsMensagens.sort((a, b) => new Date(a.dataEnvio) - new Date(b.dataEnvio));

    // Exibe as mensagens no chat
    todasAsMensagens.forEach(mensagem => {
        const uniqueId = `${mensagem.conteudo}-${mensagem.dataEnvio}-${currentAdress}-${mensagem.tipo}`;
        if (!displayedMessageIds.has(uniqueId)) {
            const messageElement = document.createElement("div");

            // Aplica classes diferentes para remetente e destinatário
            if (mensagem.tipo === 'destinatario') {
                messageElement.classList.add("message-bubble-receiver");
            } else {
                messageElement.classList.add("message-bubble-sender");
            }

            // Formata o conteúdo e a hora da mensagem
            messageElement.innerHTML = `
                <div class="message-content">${mensagem.conteudo}</div>
                <div class="message-time">${dataFormatada.format(new Date(mensagem.dataEnvio))}</div>
            `;

            // Adiciona a mensagem ao chat e evita duplicação
            chatMessages.appendChild(messageElement);
            displayedMessageIds.add(uniqueId);
        }
    });

    // Rola para a última mensagem exibida
    chatMessages.scrollTop = chatMessages.scrollHeight;
}

/**
 * Função para enviar uma nova mensagem via AJAX.
 * A mensagem é adicionada ao chat e enviada ao servidor.
 */
function sendMessage() {
    const messageInput = document.getElementById("message-input");
    const messageText = messageInput.value.trim(); // Remove espaços em branco

    if (messageText) { // Verifica se a mensagem não está vazia
        const payload = {
            destinatario: {
                endereco: currentAdress
            },
            mensagem: messageText // Texto da mensagem a ser enviado
        };

        // Envia a mensagem para o servidor
        fetch('/chat/enviar', {
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
                messageInput.value = ""; // Limpa o campo de mensagem após o envio
            })
            .catch(error => {
                console.error('Erro ao enviar a mensagem:', error);
            });

        // Exibe a mensagem na interface
        const chatMessages = document.getElementById("chat-messages");
        const messageElement = document.createElement("div");
        messageElement.classList.add("message-bubble-sender");
        messageElement.textContent = messageText;
        chatMessages.appendChild(messageElement);
        chatMessages.scrollTop = chatMessages.scrollHeight;
    } else {
        alert("A mensagem não pode estar vazia!");
    }
}

/**
 * Adiciona o evento de envio de mensagem ao pressionar a tecla Enter.
 * Se o campo de mensagem não estiver vazio, chama a função `sendMessage`.
 */
document.getElementById("message-input").addEventListener("keydown", function(event) {
    if (event.key === "Enter") {
        event.preventDefault(); // Previne o comportamento padrão do Enter
        sendMessage(); // Chama a função de envio de mensagem
    }
});

/**
 * Adiciona o evento ao botão de envio de mensagem.
 * Chama a função `sendMessage` quando o botão é clicado.
 */
document.querySelector('.message-send-btn').addEventListener('click', function() {
    sendMessage(); // Chama a função de envio de mensagem
});

/**
 * Função para inicializar o chat. Pode incluir outras configurações iniciais conforme necessário.
 * Aqui, estamos apenas iniciando o processo de seleção de contatos e exibição de mensagens.
 */
function initializeChat() {
    if (currentContact) {
        fetchMessagesFromBackend(currentAdress); // Atualiza as mensagens ao inicializar
        startMessageUpdate(); // Inicia o intervalo de atualização de mensagens
    }
}

// Chama a função de inicialização do chat assim que a página for carregada
window.onload = initializeChat;

/**
 * Função para formatar a data no padrão brasileiro (dd/mm/aaaa hh:mm:ss).
 */
const dataFormatada = new Intl.DateTimeFormat('pt-BR', {
    day: '2-digit',
    month: '2-digit',
    year: 'numeric',
    hour: '2-digit',
    minute: '2-digit',
    second: '2-digit',
    hour12: false
});
