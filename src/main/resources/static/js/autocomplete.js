// Autocomplétion à partir d'une liste JSON.
//
// <div class="autocomplete" data-source="/data/pays.json" data-cle="nationalite" data-secondaire="pays">
//     <input class="form-control" ...>
//     <ul class="autocomplete-menu"></ul>
// </div>
//
// data-cle       : propriété proposée et recopiée dans le champ
// data-secondaire : propriété affichée en gris à droite (facultative)
// Le champ n'est valide que si sa valeur fait partie de la liste.
(function () {
    const normaliser = s => s.normalize('NFD').replace(/[̀-ͯ]/g, '').toLowerCase();
    const echapper = s => s.replace(/[&<>"']/g, c => ({ '&': '&amp;', '<': '&lt;', '>': '&gt;', '"': '&quot;', "'": '&#39;' }[c]));

    function surligner(texte, requete) {
        if (!requete) return echapper(texte);
        const i = normaliser(texte).indexOf(normaliser(requete));
        if (i < 0) return echapper(texte);
        return echapper(texte.slice(0, i)) + '<mark>' + echapper(texte.slice(i, i + requete.length)) + '</mark>'
            + echapper(texte.slice(i + requete.length));
    }

    document.querySelectorAll('.autocomplete[data-source]').forEach(wrapper => {
        const input = wrapper.querySelector('input');
        const menu = wrapper.querySelector('.autocomplete-menu');
        const cle = wrapper.dataset.cle;
        const secondaire = wrapper.dataset.secondaire;
        let items = [];
        let resultats = [];
        let actif = -1;
        let erreurChargement = false;

        input.setAttribute('role', 'combobox');
        input.setAttribute('aria-autocomplete', 'list');
        input.setAttribute('aria-expanded', 'false');
        input.setAttribute('autocomplete', 'off');
        menu.setAttribute('role', 'listbox');
        menu.id = menu.id || input.id + 'Liste';
        input.setAttribute('aria-controls', menu.id);

        function valider() {
            const ok = !items.length || !input.value.trim()
                || items.some(it => normaliser(it[cle]) === normaliser(input.value.trim()));
            input.setCustomValidity(ok ? '' : 'invalide');
        }

        function ouvrir() { menu.classList.add('show'); input.setAttribute('aria-expanded', 'true'); }
        function fermer() { menu.classList.remove('show'); input.setAttribute('aria-expanded', 'false'); actif = -1; }

        // tout = true : liste complète (à l'ouverture, quand une valeur est déjà choisie)
        function afficher(tout = false) {
            const q = tout ? '' : input.value.trim();
            const nq = normaliser(q);
            const commence = it => normaliser(it[cle]).startsWith(nq);
            resultats = items
                .filter(it => !nq || normaliser(it[cle]).includes(nq) || (secondaire && normaliser(it[secondaire]).includes(nq)))
                .sort((a, b) => commence(a) === commence(b) ? 0 : commence(a) ? -1 : 1)
                .slice(0, 50);

            if (resultats.length) {
                menu.innerHTML = resultats.map((it, i) =>
                    `<li role="option" id="${menu.id}-${i}" data-index="${i}">
                        <span>${it.code ? `<span class="code">${echapper(it.code)}</span>` : ''}${surligner(it[cle], q)}</span>
                        ${secondaire ? `<span class="secondaire">${surligner(it[secondaire], q)}</span>` : ''}
                     </li>`).join('');
            } else if (erreurChargement) {
                menu.innerHTML = '<li class="vide text-danger"><i class="fa-solid fa-triangle-exclamation me-2"></i>Liste indisponible</li>';
            } else if (!items.length) {
                menu.innerHTML = '<li class="vide"><i class="fa-solid fa-spinner fa-spin me-2"></i>Chargement…</li>';
            } else {
                menu.innerHTML = '<li class="vide"><i class="fa-solid fa-magnifying-glass me-2"></i>Aucun résultat</li>';
            }
            actif = -1;
            ouvrir();
        }

        function choisir(i) {
            const it = resultats[i];
            if (!it) return;
            input.value = it[cle];
            valider();
            fermer();
            input.dispatchEvent(new Event('change', { bubbles: true }));
        }

        function deplacer(delta) {
            if (!resultats.length) return;
            actif = (actif + delta + resultats.length) % resultats.length;
            menu.querySelectorAll('li').forEach((li, i) => li.classList.toggle('active', i === actif));
            const li = menu.children[actif];
            li.scrollIntoView({ block: 'nearest' });
            input.setAttribute('aria-activedescendant', li.id);
        }

        input.addEventListener('focus', () => {
            input.select();
            afficher(items.some(it => normaliser(it[cle]) === normaliser(input.value.trim())));
        });
        input.addEventListener('input', () => { afficher(); valider(); });
        input.addEventListener('keydown', e => {
            if (e.key === 'ArrowDown') { e.preventDefault(); menu.classList.contains('show') ? deplacer(1) : afficher(); }
            else if (e.key === 'ArrowUp') { e.preventDefault(); deplacer(-1); }
            else if (e.key === 'Enter' && menu.classList.contains('show')) { e.preventDefault(); choisir(actif >= 0 ? actif : 0); }
            else if (e.key === 'Escape') fermer();
        });
        // mousedown plutôt que click : se déclenche avant la perte de focus du champ
        menu.addEventListener('mousedown', e => {
            const li = e.target.closest('li[data-index]');
            if (li) { e.preventDefault(); choisir(+li.dataset.index); }
        });
        input.addEventListener('blur', () => { fermer(); valider(); });

        fetch(wrapper.dataset.source)
            .then(r => { if (!r.ok) throw new Error(r.status); return r.json(); })
            .then(data => { items = data; valider(); if (document.activeElement === input) afficher(); })
            .catch(() => { erreurChargement = true; console.error('Liste indisponible :', wrapper.dataset.source); });
    });
})();
