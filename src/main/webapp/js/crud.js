function confermaEliminazione(id, nome) {
    const confermato = confirm(`Sei sicuro di voler eliminare il prodotto "${nome}" (ID #${id}) dal catalogo?`);
    if (confermato) {
        window.location.href = `${pageContext.request.contextPath}/GestioneProdottiAdmin?action=delete&id=${id}`;
    }
}

// Popola il form in alto per la Modifica
function caricaInForm(id, nome, idCat, prezzo, iva, quantita, imgPath, descrizione) {
    document.getElementById('form-title').innerText = `Modifica Prodotto #${id}`;
    document.getElementById('form-action').value = 'update';
    document.getElementById('prodotto-id').value = id;
    
    document.getElementById('nome').value = nome;
    document.getElementById('idCategoria').value = idCat;
    document.getElementById('prezzo').value = prezzo;
    document.getElementById('iva').value = iva;
    document.getElementById('quantita').value = quantita;
    document.getElementById('imgPath').value = imgPath;
    document.getElementById('descrizione').value = descrizione;

    document.getElementById('btn-reset').style.display = 'inline-block';
    window.scrollTo({ top: 0, behavior: 'smooth' });
}

// Resetta il form allo stato "Aggiungi"
function resetForm() {
    document.getElementById('form-title').innerText = 'Aggiungi Nuovo Prodotto';
    document.getElementById('form-action').value = 'add';
    document.getElementById('prodotto-id').value = '';
    document.getElementById('crud-form').reset();
    document.getElementById('btn-reset').style.display = 'none';
}
