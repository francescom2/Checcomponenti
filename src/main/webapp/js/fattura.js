function stampaFatturaSingola(btn) {
    // 1. Rimuove la classe 'in-stampa' da tutti gli ordini
    document.querySelectorAll('.ordine-box').forEach(box => {
        box.classList.remove('in-stampa');
    });

    // 2. Aggiunge la classe 'in-stampa' SOLO all'ordine contenente il pulsante cliccato
    const ordineSelezionato = btn.closest('.ordine-box');
    ordineSelezionato.classList.add('in-stampa');

    // 3. Avvia la stampa
    window.print();
}
