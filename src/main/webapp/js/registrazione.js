/* --- Validazione e Check Email AJAX per Registrazione --- */
let emailDisponibile = true;

// 1. Controllo AJAX disponibilità Email in tempo reale
function verificaEmailAJAX() {
    const emailInput = document.getElementById("email").value.trim();
    const emailError = document.getElementById("emailError");
    const regForm = document.getElementById("regForm");

    if (emailInput === "") return;

    // Recupera la base del percorso dall'action del form
    const formAction = regForm.getAttribute("action");
    const contextPath = formAction.substring(0, formAction.indexOf("/registrazione"));

    fetch(contextPath + "/check-email?email=" + encodeURIComponent(emailInput))
        .then(response => response.json())
        .then(data => {
            if (data.exists) {
                emailError.innerText = "❌ Questa email è già usata da un altro utente.";
                emailDisponibile = false;
            } else {
                emailError.innerText = "";
                emailDisponibile = true;
            }
        })
        .catch(err => console.error("Errore AJAX check email:", err));
}

// 2. Validazione client-side con Espressioni Regolari (Regex)
function validaForm(event) {
    let valido = true;

    const username = document.getElementById("username").value.trim();
    const email = document.getElementById("email").value.trim();
    const password = document.getElementById("password").value;
    const confirmPassword = document.getElementById("confirmPassword").value;

    // Reset messaggi errore
    document.getElementById("usernameError").innerText = "";
    document.getElementById("emailError").innerText = "";
    document.getElementById("passwordError").innerText = "";
    document.getElementById("confirmPasswordError").innerText = "";

    // Regex Username: 3-20 caratteri alfanumerici
    const usernameRegex = /^[a-zA-Z0-9_-]{3,20}$/;
    if (!usernameRegex.test(username)) {
        document.getElementById("usernameError").innerText = "Username non valido (3-20 caratteri, senza spazi).";
        valido = false;
    }

    // Regex Email standard
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailRegex.test(email)) {
        document.getElementById("emailError").innerText = "Inserisci un indirizzo e-mail valido.";
        valido = false;
    } else if (!emailDisponibile) {
        document.getElementById("emailError").innerText = "❌ Email già occupata.";
        valido = false;
    }

    // Password min 6 caratteri
    if (password.length < 6) {
        document.getElementById("passwordError").innerText = "La password deve contenere almeno 6 caratteri.";
        valido = false;
    }

    // Controllo corrispondenza password
    if (password !== confirmPassword) {
        document.getElementById("confirmPasswordError").innerText = "Le password non coincidono.";
        valido = false;
    }

    if (!valido) {
        event.preventDefault(); // Blocca l'invio se ci sono errori
    }
    return valido;
}