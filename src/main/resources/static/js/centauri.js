const mostraDataRodape = () => {
    const dataRodape = document.getElementById("dataRodape");
    const data = new Date();
    dataRodape.innerHTML = "Data: " + data.toLocaleString();
    setTimeout(mostraDataRodape, 1);
};

window.onload = function () {
    mostraDataRodape();
}

$("#imagem").change(function (evt) {
    const imagemUpload = evt.target.files[0]

    alterarTamanhoImagem(imagemUpload, function (resizedFile) {
        const imagem = new FileReader();

        imagem.onload = function () {
            $("#imgPublicacao").attr("src", imagem.result);
        };
        imagem.readAsDataURL(resizedFile);
    });
});


// função para alterar o tamanho da imagem ao fazer o upload
function alterarTamanhoImagem(file, callback) {
    const maxFileSizeMB = 10; // Tamanho máximo do arquivo em MB
    const img = new Image();
    const reader = new FileReader();

    reader.onload = function (e) {
        img.src = e.target.result;

        img.onload = function () {
            const canvas = document.createElement('canvas');
            const context = canvas.getContext('2d');

            // Define a largura e altura do canvas como as dimensões originais da imagem
            canvas.width = img.width;
            canvas.height = img.height;

            // Desenha a imagem no canvas
            context.drawImage(img, 0, 0);

            // Função para comprimir a imagem
            function compressImage(quality) {
                canvas.toBlob(function (blob) {
                    // Verifica o tamanho do arquivo resultante
                    if (blob.size / (1024 * 1024) > maxFileSizeMB && quality > 0.1) {
                        // Reduz a qualidade se o arquivo ainda for maior que 5 MB
                        console.log(`Compressão em andamento... Qualidade: ${(quality * 100).toFixed(0)}%`);
                        compressImage(quality - 0.1);
                    } else {
                        // Cria o arquivo final com o nome original sem espaços
                        const compressedFile = new File([blob], file.name.replace(/\s+/g, '_'), {type: file.type});
                        console.log('Compressão concluída!');
                        callback(compressedFile);  // Retorna o arquivo comprimido
                    }
                }, file.type, quality);
            }

            // Inicia a compressão com qualidade máxima
            console.log('Iniciando compressão...');
            compressImage(1.0);
        };
    };

    reader.readAsDataURL(file);
}

$(document).ready(function() {
    // Função para buscar destinatários quando a tecla Enter for pressionada
    $("#email").on("keypress", function(e) {
        // Verifica se a tecla pressionada foi Enter (código 13)
        if (e.which == 13) {
            e.preventDefault(); // Impede o comportamento padrão do Enter

            var email = $(this).val(); // Pega o valor digitado

            $.ajax({
                url: "/cadastro/destinatario/busca", // URL corrigida
                type: "POST",
                contentType: "application/x-www-form-urlencoded; charset=UTF-8", // Certifique-se de enviar os dados corretamente
                data: {
                    email: email // Envia o e-mail como parâmetro
                },
                success: function(destinatarios) {
                    // Limpa a lista de destinatários anterior
                    $("#destinatarios-list").empty();

                    // Oculta o botão "Enviar Convite" inicialmente
                    $("#send-invite-btn").hide();

                    // Verifica se nenhum destinatário foi encontrado
                    if (destinatarios.length === 0) {
                        // Exibe a mensagem de destinatário não encontrado e o botão de convite
                        $("#destinatarios-list").append(
                            '<li>Nenhum destinatário encontrado com este e-mail.</li>'
                        );
                        //Esconde Botão Cadastro destinatario
                        $("#cad-destinatario-btn").hide();
                        // Mostra o botão de convite
                        $("#send-invite-btn").show();

                        // Ação para o botão de envio de convite
                        $("#send-invite-btn").off("click").on("click", function() {
                            enviarConvite(email); // Função para enviar convite
                        });
                    } else {
                        // Itera sobre os destinatários retornados e os adiciona na lista
                        $.each(destinatarios, function(index, destinatario) {
                            $("#destinatarios-list").append(
                                '<li>' +
                                '<input type="checkbox" value="' + destinatario.endereco + '" name="destinatariosSelecionados">' +
                                '   '+destinatario.nome + ' \n' + destinatario.endereco +
                                '</li>'
                            );
                        });
                        // desativa botão de cadastro
                        $("#cad-destinatario-btn").show();
                        // Se há destinatários, o botão de convite permanece oculto
                        $("#send-invite-btn").hide();
                    }
                },
                error: function(xhr, status, error) {
                    console.error("Erro ao buscar destinatários. Tente novamente.");
                    console.error("Status: " + status + ", Erro: " + error);
                }
            });
        }
    });

    // Função para enviar convite
    function enviarConvite(email) {
        $.ajax({
            url: "/cadastro/destinatario/enviarConvite", // Endpoint para envio do convite
            type: "POST",
            contentType: "application/x-www-form-urlencoded; charset=UTF-8",
            data: {
                email: email // Envia o e-mail como parâmetro
            },
            success: function(response) {
                alert("Convite enviado com sucesso para " + email);
            },
            error: function(xhr, status, error) {
                console.error("Erro ao enviar o convite. Tente novamente.");
                console.error("Status: " + status + ", Erro: " + error);
            }
        });
    }
});
