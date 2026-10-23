// Comportements communs à toutes les pages
(function () {
    // Barre latérale sur mobile : <button data-sidebar-toggle> ouvre, [data-sidebar-close] ou Échap ferment
    document.querySelectorAll('[data-sidebar-toggle]').forEach(b =>
        b.addEventListener('click', () => document.body.classList.toggle('sidebar-open')));
    document.querySelectorAll('[data-sidebar-close]').forEach(b =>
        b.addEventListener('click', () => document.body.classList.remove('sidebar-open')));
    document.addEventListener('keydown', e => { if (e.key === 'Escape') document.body.classList.remove('sidebar-open'); });

    // Confirmation avant une action importante : <form data-confirm="Question ?" data-confirm-bouton="Valider">
    const modale = document.getElementById('modaleConfirmation');
    if (modale) {
        let formEnAttente = null;
        const titre = modale.querySelector('[data-titre]');
        const message = modale.querySelector('[data-message]');
        const bouton = modale.querySelector('[data-confirmer]');

        document.addEventListener('submit', e => {
            const form = e.target;
            if (!form.dataset || !form.dataset.confirm) return;
            e.preventDefault();
            formEnAttente = form;
            titre.textContent = form.dataset.confirmTitre || 'Confirmer';
            message.textContent = form.dataset.confirm;
            bouton.querySelector('span').textContent = form.dataset.confirmBouton || 'Confirmer';
            bouton.className = 'btn ' + (form.dataset.confirmStyle || 'btn-primary');
            bootstrap.Modal.getOrCreateInstance(modale).show();
        });
        // form.submit() ne redéclenche pas l'événement submit : pas de boucle
        bouton.addEventListener('click', () => { if (formEnAttente) formEnAttente.submit(); });
    }

    // Recherche instantanée dans un tableau : <input data-filtre-tableau="#id" data-compteur="#id">
    document.querySelectorAll('[data-filtre-tableau]').forEach(input => {
        const tableau = document.querySelector(input.dataset.filtreTableau);
        const compteur = document.querySelector(input.dataset.compteur);
        const normaliser = s => s.normalize('NFD').replace(/[̀-ͯ]/g, '').toLowerCase();
        input.addEventListener('input', () => {
            const q = normaliser(input.value.trim());
            let visibles = 0;
            tableau.querySelectorAll('tbody tr').forEach(tr => {
                const ok = !q || normaliser(tr.textContent).includes(q);
                tr.classList.toggle('d-none', !ok);
                if (ok) visibles++;
            });
            if (compteur) compteur.textContent = visibles;
        });
    });

    // Validation Bootstrap : on amène l'utilisateur au premier champ invalide
    document.querySelectorAll('form.needs-validation').forEach(form => {
        form.addEventListener('submit', e => {
            if (!form.checkValidity()) {
                e.preventDefault();
                e.stopPropagation();
                const premier = form.querySelector(':invalid');
                premier.scrollIntoView({ behavior: 'smooth', block: 'center' });
                premier.focus({ preventScroll: true });
            }
            form.classList.add('was-validated');
        });
    });
})();
