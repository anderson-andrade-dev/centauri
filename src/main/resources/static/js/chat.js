/**
 * Desenvolvedor: Anderson Andrade Dev
 * Email: andersonandradedev@outlook.com
 * Data: 27/09/2024
 *
 * Função principal do sistema de chat para selecionar contatos e enviar mensagens.
 */

let currentContact = null; // Contato atualmente selecionado
let currentAdress = null;  // Endereço do contato atualmente selecionado
const messagesByContact = {}; // Armazena mensagens por contato
let messageUpdateIntervalId = null; // ID do intervalo para atualização de mensagens

// Intervalo para atualização das mensagens (90 segundos)
const MESSAGE_UPDATE_INTERVAL = 90000; // 90 segundos em milissegundos

// Armazena IDs únicos de todas as mensagens já exibidas para evitar duplicação
const displayedMessageIds = new Set();

/**
 * Inicia o intervalo de atualização das mensagens quando um contato é selecionado.
 * O intervalo de 90 segundos permite que o sistema atualize as mensagens sem duplicá-las.
 */
function startMessageUpdate() {
    // Limpa o intervalo anterior se já existir
    if (messageUpdateIntervalId) {
        clearInterval(messageUpdateIntervalId);
    }

    // Inicia um novo intervalo
    messageUpdateIntervalId = setInterval(() => {
        if (currentContact) {
            updateMessages(); // Atualiza as mensagens
        }
    }, MESSAGE_UPDATE_INTERVAL);
}

/**
 * Função responsável por selecionar um contato e carregar suas mensagens.
 * Atualiza o nome do contato na interface e carrega as mensagens do servidor via requisição AJAX.
 *
 * @param {string} contactName - Nome do contato selecionado.
 * @param {string} contactAdress - Endereço do contato selecionado.
 */
function selectContact(contactName, contactAdress) {
    currentContact = contactName; // Atualiza contato atual
    currentAdress = contactAdress;  // Atualiza endereço do contato

    // Atualiza o nome do contato na interface
    document.getElementById('contact-name').textContent = contactName;
    document.getElementById('message-input').disabled = false; // Habilita campo de mensagem
    document.querySelector('.message-send-btn').disabled = false; // Habilita botão de envio

    // Inicia o intervalo de atualização de mensagens
    startMessageUpdate();

    updateMessages(); // Carrega as mensagens imediatamente
}

/**
 * Função responsável por buscar e exibir as mensagens do contato atual.
 */
function updateMessages() {
    const destinatario = {
        nome: currentContact,
        endereco: currentAdress
    };

    const chatMessages = document.getElementById("chat-messages");
    chatMessages.innerHTML = ""; // Limpa as mensagens antes de exibir as novas

    // Verifica se já temos mensagens armazenadas para o contato selecionado
    if (messagesByContact[currentAdress]) {
        // Exibe as mensagens do contato atual armazenadas no frontend
        displayMessages(messagesByContact[currentAdress]);
    } else {
        // Se não houver mensagens armazenadas, faz a requisição AJAX para buscar no servidor
        fetch(`/chat/mensagens`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(destinatario), // Envia destinatário como JSON
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Erro ao buscar mensagens'); // Trata erro de resposta
                }
                return response.json(); // Retorna o JSON contendo as mensagens
            })
            .then(data => {
                // Verifica se existem mensagens para o destinatário
                if (data.mensagensDestinatario.length > 0) {
                    // Armazena as mensagens recebidas do destinatário atual
                    messagesByContact[currentAdress] = data.mensagensDestinatario;
                    // Exibe as mensagens recebidas no chat
                    displayMessages(messagesByContact[currentAdress]);
                } else {
                    // Se não houver mensagens, exibe mensagem padrão
                    chatMessages.innerHTML = "<div class='no-messages'>Nenhuma mensagem disponível</div>";
                }
            })
            .catch(error => {
                console.error('Erro ao carregar mensagens:', error); // Log de erro
            });
    }
}

/**
 * Função responsável por exibir as mensagens no chat.
 * Para garantir que nenhuma mensagem seja duplicada, um identificador único é criado
 * usando a data de envio, o conteúdo da mensagem e um timestamp.
 *
 * @param {Array} mensagens - Lista de mensagens a serem exibidas.
 */
function displayMessages(mensagens) {
    const chatMessages = document.getElementById("chat-messages");

    mensagens.forEach(mensagem => {
        // Cria um ID único com a data de envio, o conteúdo e um timestamp.
        const uniqueId = `${mensagem.dataEnvio}-${mensagem.conteudo}-${Date.now()}`;

        // Verifica se a mensagem já foi exibida
        if (!displayedMessageIds.has(uniqueId)) {
            const messageElement = document.createElement("div");
            messageElement.classList.add("message-bubble-sender"); // Classe para destinatário
            messageElement.innerHTML = mensagem.conteudo + "<br>" + dataFormatada.format(new Date(mensagem.dataEnvio)); // Exibe conteúdo da mensagem
            chatMessages.appendChild(messageElement); // Adiciona mensagem ao chat

            // Adiciona a mensagem ao conjunto de IDs exibidos
            displayedMessageIds.add(uniqueId);
        }
    });

    // Rola a tela para a última mensagem
    chatMessages.scrollTop = chatMessages.scrollHeight;
}

/**
 * Função responsável por enviar uma mensagem.
 * Adiciona a mensagem à interface e a envia para o servidor via requisição AJAX.
 */
function sendMessage() {
    const messageInput = document.getElementById("message-input");
    const messageText = messageInput.value; // Captura o texto da mensagem

    if (messageText.trim() !== "") { // Verifica se a mensagem não está vazia
        const chatMessages = document.getElementById("chat-messages");

        // Cria o elemento da mensagem enviada pelo destinatário
        const messageElement = document.createElement("div");
        messageElement.classList.add("message-bubble-sender"); // Classe para remetente
        messageElement.textContent = messageText; // Exibe conteúdo da mensagem

        // Adiciona a mensagem na interface e rola para o fim
        chatMessages.appendChild(messageElement);
        chatMessages.scrollTop = chatMessages.scrollHeight;

        // Armazena a mensagem enviada para o contato atual
        if (!messagesByContact[currentAdress]) {
            messagesByContact[currentAdress] = []; // Inicializa array para novo contato
        }
        messagesByContact[currentAdress].push({
            conteudo: messageText,
            dataEnvio: new Date() // Usando a data atual para o envio
        }); // Adiciona mensagem ao contato

        // Dados da mensagem para enviar ao servidor
        const payload = {
            destinatario: {
                nome: currentContact,
                endereco: currentAdress // Endereço do destinatário
            },
            mensagem: messageText // Texto da mensagem
        };

        // Requisição AJAX para enviar a mensagem
        fetch('/chat/enviar', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(payload), // Envia dados como JSON
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Erro ao enviar a mensagem'); // Trata erro de resposta
                }
                return response.json();
            })
            .then(data => {
                console.log('Mensagem enviada com sucesso:', data); // Log de sucesso
                messageInput.value = ""; // Limpa o campo de entrada após o envio
            })
            .catch((error) => {
                console.error('Erro:', error); // Log de erro
            });
    } else {
        console.error('Mensagem não pode estar vazia'); // Log se a mensagem estiver vazia
    }
}

// Ouvinte de evento para enviar mensagem ao pressionar Enter
document.getElementById("message-input").addEventListener("keypress", function (event) {
    if (event.key === "Enter") {
        sendMessage(); // Chama a função para enviar a mensagem
        event.preventDefault(); // Evita a nova linha no campo de entrada
    }
});

/**
 * Função para formatar a data no padrão brasileiro (dd/mm/aaaa hh:mm:ss).
 */
const dataFormatada = new Intl.DateTimeFormat('pt-BR', {
    day: '2-digit',
    month: '2-digit',
    year: 'numeric',
    hour: '2-digit',
    minute: '2-digit',
    second: '2-digit', // Adiciona a opção de segundos
    hour12: false // Define o formato 24 horas
});
