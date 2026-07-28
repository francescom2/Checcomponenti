document.addEventListener("DOMContentLoaded", () => {
    const searchInput = document.getElementById("search-input");
    const searchResults = document.getElementById("search-results");

    if (!searchInput || !searchResults) return;

    let debounceTimer = null;

    searchInput.addEventListener("input", function () {
        const query = this.value.trim();
        const contextPath = searchInput.dataset.contextpath || "";

        // Cancelliamo il timer precedente se l'utente sta ancora digitando veloce
        clearTimeout(debounceTimer);

        if (query.length < 2) {
            searchResults.innerHTML = "";
            searchResults.style.display = "none";
            return;
        }

        // Attendiamo 300ms prima di inviare la richiesta (Debouncing)
        debounceTimer = setTimeout(() => {
            fetch(`${contextPath}/RicercaProdotti?q=${encodeURIComponent(query)}`)
                .then(response => response.json())
                .then(prodotti => {
                    searchResults.innerHTML = "";

                    if (prodotti.length === 0) {
                        searchResults.innerHTML = '<div class="search-item-empty">Nessun componente trovato</div>';
                    } else {
                        prodotti.forEach(p => {
                            const a = document.createElement("a");
                            a.href = `${contextPath}/PaginaProdotto?id=${p.id}`;
                            a.className = "search-item";
                            a.innerHTML = `
                                <img src="${contextPath}/${p.imgPath}" alt="${p.nome}" onerror="this.src='${contextPath}/img/imgNonTrovata.png';">
                                <div class="search-item-info">
                                    <span class="search-item-title">${p.nome}</span>
                                    <span class="search-item-price">€ ${p.prezzo.toFixed(2)}</span>
                                </div>
                            `;
                            searchResults.appendChild(a);
                        });
                    }
                    searchResults.style.display = "block";
                })
                .catch(err => console.error("Errore nella ricerca AJAX:", err));
        }, 300);
    });

    // Nascondi i risultati se l'utente clicca in un punto qualsiasi fuori dalla barra
    document.addEventListener("click", (e) => {
        if (!searchInput.contains(e.target) && !searchResults.contains(e.target)) {
            searchResults.style.display = "none";
        }
    });
});