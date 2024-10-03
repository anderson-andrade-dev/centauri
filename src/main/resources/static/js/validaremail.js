document.addEventListener('DOMContentLoaded', function () {
    const emailPermitidos = [];
    const emailInput = document.getElementById('email');
    const feedbackEmail = document.getElementById('email-feedback');
    const submitButton = document.querySelector('button[type="submit"]');
    const form = document.getElementById('form-cadastro');
    const filePath = '/js/dominios.txt';

    function carregarDominios() {
        fetch(filePath)
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok ' + response.statusText);
                }
                return response.text();
            })
            .then(data => {
                emailPermitidos.push(...data.split('\n').map(domain => domain.trim()));
                verificarEmail();
            })
            .catch(error => console.error('Erro ao carregar dominios:', error));
    }

    function validarEmail(email) {
        const atSymbolCount = (email.match(/@/g) || []).length;
        if (atSymbolCount !== 1) {
            return false;
        }

        //verificar os dominios
        const dominioEmail = email.substring(email.lastIndexOf('@') + 1).toLowerCase();
        return emailPermitidos.includes('@' + dominioEmail);
    }

    function atualizarFeedbackEmail() {
        const email = emailInput.value.trim();
        if (email) {
            if (validarEmail(email)) {
                feedbackEmail.textContent = 'Email válido.';
                feedbackEmail.style.color = 'green';
                emailInput.setCustomValidity('');
                submitButton.disabled = false;
            } else {
                feedbackEmail.textContent = 'Email inválido.';
                feedbackEmail.style.color = 'red';
                emailInput.setCustomValidity('Email não permitido ou formato inválido.');
                submitButton.disabled = true;
            }
        } else {
            feedbackEmail.textContent = '';
            emailInput.setCustomValidity('');
            submitButton.disabled = true;
        }
    }


    function verificarEmail() {
        atualizarFeedbackEmail();
    }

    emailInput.addEventListener('input', atualizarFeedbackEmail);

    form.addEventListener('submit', function (event) {
        if (emailInput.checkValidity() === false) {
            event.preventDefault(); // O form não é eviado se a verificação do email falhar
        }
    });
    carregarDominios();
});


