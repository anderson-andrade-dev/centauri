$(document).ready(function () {
    $('#form-cadastro').on('submit', function (event) {
        event.preventDefault();

        let nomeUsuario = $('#nomeUsuario').val();
        let feedbackElement = $('#nomeUsuario-feedback');

        if (nomeUsuario.length > 0) {
            $.ajax({
                url: '/cadastro/verificarNomeUsuario',
                type: 'GET',
                headers: {
                    "Content-Type": "application/json",
                    "Accept": "application/json"
                },
                data: {nomeUsuario: nomeUsuario},
                success: function (resultado) {
                    //console.log('Resultado recebido do servidor:', resultado);

                    if (resultado == 1) {
                        feedbackElement.text('Nome de usuário já está em uso!');
                    } else if (resultado == 0) {
                        feedbackElement.text('');

                        $('#form-cadastro').off('submit').submit();
                    } else {
                        feedbackElement.text('Resposta inesperada do servidor.');
                    }
                },
                error: function () {
                    feedbackElement.text('Erro ao verificar o nome de usuário.');
                }
            });
        } else {
            feedbackElement.text('');
        }
    });
});