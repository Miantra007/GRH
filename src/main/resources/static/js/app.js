// Comportements communs à toutes les pages
(function () {
    // Ligne de tableau cliquable : ouvre la fiche (sauf clic sur un bouton ou un lien)
    document.querySelectorAll('tr[data-href]').forEach(tr => {
        tr.addEventListener('click', e => {
            if (!e.target.closest('a, button, form')) {
                window.location = tr.dataset.href;
            }
        });
    });

    // Recherche instantanée dans un tableau
    document.querySelectorAll('[data-filtre-tableau]').forEach(input => {
        const tableau = document.querySelector(input.dataset.filtreTableau);
        const compteur = document.querySelector(input.dataset.compteur);
        const vide = document.querySelector(input.dataset.vide);
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
            if (vide) vide.classList.toggle('d-none', visibles > 0);
        });
    });

    // Confirmation de suppression (fenêtre modale unique, alimentée par le bouton cliqué)
    const modale = document.getElementById('modaleSuppression');
    if (modale) {
        modale.addEventListener('show.bs.modal', e => {
            const bouton = e.relatedTarget;
            modale.querySelector('form').action = bouton.dataset.url;
            modale.querySelector('[data-libelle]').textContent = bouton.dataset.libelle;
        });
    }

    // Validation Bootstrap côté navigateur : on amène l'utilisateur au premier champ invalide
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
