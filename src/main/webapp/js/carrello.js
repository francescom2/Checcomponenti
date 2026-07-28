

/* Funzione per aggiungere al carrello tramite ajax */
function aggiungiAlCarrello(buttonElement, idProdotto, contextPath) {
    const originalText = buttonElement.innerHTML;

    fetch(contextPath + '/carrello', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: 'action=add&id=' + idProdotto + '&ajax=true'
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Errore di risposta dal server');
        }
        return response.json();
    })
    .then(data => {
		console.log("Risposta dal server:", data);
		
        if (data.status === 'success') {
			const cartBadge = document.getElementById('cart-count');
			if (cartBadge && data.totalCount !== undefined) {
				cartBadge.textContent = data.totalCount;
			}
			
            buttonElement.innerHTML = '✓ Aggiunto!';
            buttonElement.classList.add('btn-added');

            setTimeout(() => {
                buttonElement.innerHTML = originalText;
                buttonElement.classList.remove('btn-added');
            }, 2000);
        }
    })
    .catch(error => {
        console.error('Errore durante l\'aggiunta al carrello:', error);
    });
}