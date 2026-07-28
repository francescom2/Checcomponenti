// Popola il form in alto per la Modifica di un prodotto
function caricaInForm(id, nome, idCategoria, prezzo, iva, quantita, imgPath, descrizione) {
    document.getElementById('form-title').textContent = "Modifica Prodotto #" + id;
    document.getElementById('form-action').value = "update";
    document.getElementById('prodotto-id').value = id;
    
    document.getElementById('nome').value = nome;
    document.getElementById('idCategoria').value = idCategoria;
    document.getElementById('prezzo').value = prezzo;
    document.getElementById('iva').value = iva;
    document.getElementById('quantita').value = quantita;
    document.getElementById('descrizione').value = descrizione;
    
    // Mantiene l'immagine corrente nel campo nascosto
    document.getElementById('existingImgPath').value = imgPath;
    
    // Cambia testo al bottone e mostra il tasto Annulla
    document.getElementById('btn-submit').textContent = "Aggiorna Prodotto";
    document.getElementById('btn-reset').style.display = 'inline-block';
    
    // Scorrimento fluido verso il form
	document.getElementById('crud-form').scrollIntoView({ 
	        behavior: 'smooth', 
	        block: 'start' 
	    });}

// Resetta il form allo stato "Aggiungi Nuovo Prodotto"
function resetForm() {
    document.getElementById('form-title').textContent = 'Aggiungi Nuovo Prodotto';
    document.getElementById('form-action').value = 'add';
    document.getElementById('prodotto-id').value = '';
    document.getElementById('existingImgPath').value = '';
    
    document.getElementById('crud-form').reset();
    document.getElementById('btn-submit').textContent = "Salva Prodotto";
    document.getElementById('btn-reset').style.display = 'none';
}

