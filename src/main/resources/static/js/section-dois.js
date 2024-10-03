document.addEventListener('DOMContentLoaded', function () {
    const cards = document.querySelectorAll('.card');

    if (cards.length === 0) {
        console.error('Nenhum elemento .card foi encontrado!');
    }

    cards.forEach(function (card) {
        const cardInner = card.querySelector('.card-inner');
        const cardFront = card.querySelector('.card-front img');
        const cardBack = card.querySelector('.card-back');

        if (!cardInner || !cardFront || !cardBack) {
            console.error('Elementos .card-inner, .card-front ou .card-back não foram encontrados para a carta.', card);
            return;
        }

        cardFront.onload = function () {
            if (cardFront.clientHeight === 0 || cardFront.clientWidth === 0) {
                console.error('A imagem da frente da carta não tem altura ou largura válidas.', cardFront);
            } else {
                cardBack.style.height = `${cardFront.clientHeight}px`;
                card.style.width = `${cardFront.clientWidth}px`;
                card.style.height = `${cardFront.clientWidth}px`;
                cardBack.style.width = `${cardFront.clientWidth}px`;
            }
        };

        if (cardFront.complete) {
            if (cardFront.clientHeight === 0 || cardFront.clientWidth === 0) {
                console.error('A imagem da frente da carta carregada previamente não tem altura ou largura válidas.', cardFront);
            } else {
                cardBack.style.height = `${cardFront.clientHeight}px`;
                card.style.width = `${cardFront.clientWidth}px`;
                card.style.height = `${cardFront.clientHeight}px`;
                cardBack.style.width = `${cardFront.clientWidth}px`;
            }
        }

        cardInner.addEventListener('click', function () {
            card.classList.toggle('flipped');
        });
    });

    const saveIcons = document.querySelectorAll('.saveIcon');

    if (saveIcons.length === 0) {
        console.warn('Nenhum ícone de salvar (.saveIcon) foi encontrado.');
    }

    saveIcons.forEach(icon => {
        icon.addEventListener('click', () => {
            const estrelaSave = icon.closest('.estrela')?.querySelector('#estrela-save');

            if (!estrelaSave) {
                console.error('Elemento #estrela-save não encontrado dentro do .estrela.', icon);
                return;
            }

            if (icon.classList.contains('fa-regular')) {
                icon.classList.remove('fa-regular');
                icon.classList.add('fa-solid');
                icon.style.color = '#fff';
                estrelaSave.checked = true;
            } else {
                icon.classList.remove('fa-solid');
                icon.classList.add('fa-regular');
                icon.style.color = '#fff';
                estrelaSave.checked = false;
            }
        });
    });

    const images = document.querySelectorAll('.reveal');

    if (images.length === 0) {
        console.warn('Nenhuma imagem com a classe .reveal foi encontrada para o efeito ScrollReveal.');
    }

    images.forEach((img, index) => {
        ScrollReveal().reveal(img, {
            distance: '100px',
            duration: 900,
            easing: 'ease-in-out',
            origin: 'bottom',
            delay: index * 30,
            reset: true
        });
    });

    const allCards = document.querySelectorAll('.card');

    if (allCards.length === 0) {
        console.warn('Nenhuma carta (.card) foi encontrada para a rotação aleatória.');
    }

    allCards.forEach(card => {
        const randomRotation = Math.random() * 20 - 10;
        card.style.transform = `rotate(${randomRotation}deg)`;
    });
});