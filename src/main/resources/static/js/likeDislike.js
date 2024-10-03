$(document).ready(function () {
//            console.log('Script carregado e executado.');
    $('.starIcon').on('click', function () {
        const $icone = $(this);
        const $estrelaLike = $icone.closest('.estrela').find('#estrela-like');
        const $submitLike = $estrelaLike;

        if ($icone.hasClass('fa-regular')) {
            $icone.removeClass('fa-regular').addClass('fa-solid').css('color', 'yellow');
            $estrelaLike.prop('checked', true);
            chamarAjax('/like/' + $estrelaLike.data('id'), 'POST');
        } else {
            $icone.removeClass('fa-solid').addClass('fa-regular').css('color', '#fff');
            $estrelaLike.prop('checked', false);
            chamarAjax('/dislike/' + $estrelaLike.data('id'), 'POST');
        }
        $submitLike.prop('disabled', true);
        $icone.addClass('disabled');
    });

    function chamarAjax(url, method) {
        $.ajax({
            url: url,
            type: method,
            headers: {
                'Content-Type': 'application/json',
                'Accept': 'application/json'
            },
            success: function (response) {
//                        console.log('Requisição enviada com sucesso para o URL: ' + url);
            },
            error: function (xhr, status, error) {
                console.error('Erro ao enviar requisição:', error);
                $('.estrela-like').prop('disabled', false);
                $('.starIcon').removeClass('disabled');
            }
        });
    }
});