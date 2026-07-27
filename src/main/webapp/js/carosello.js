/**
 * Gestisce lo scorrimento orizzontale dei caroselli del catalogo
 * @param {HTMLElement} btnElement - Il pulsante cliccato 
 * @param {number} direction - 1 per scorrere a destra, -1 per scorrere a sinistra
 */
function scrollCarousel(btnElement, direction) {

    const wrapper = btnElement.closest('.carousel-wrapper');
    if (!wrapper) return;

    const track = wrapper.querySelector('.carousel-track');
    if (!track) return;

    const scrollAmount = track.clientWidth * 0.75;

    track.scrollBy({
        left: direction * scrollAmount,
        behavior: 'smooth'
    });
}