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


