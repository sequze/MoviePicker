document.addEventListener('DOMContentLoaded', () => {
    const randomBtn = document.getElementById('randomBtn');
    if (randomBtn) {
        randomBtn.addEventListener('click', async () => {
            const form = document.getElementById('filterForm');
            const params = new URLSearchParams(new FormData(form));
            try {
                const res = await fetch(`/movies/random?` + params.toString(), {
                    headers: { 'Accept': 'application/json' }
                });
                const data = await res.json();
                const box = document.getElementById('randomResult');
                if (!data.found) {
                    box.style.display = 'block';
                    box.querySelector('#randomTitle').innerText = 'Ничего не найдено';
                    box.querySelector('#randomMeta').innerText = '';
                    box.querySelector('#randomPoster').src = '';
                    box.querySelector('#randomDetails').style.display = 'none';
                    return;
                }
                box.style.display = 'block';
                document.getElementById('randomTitle').innerText = data.title || '';
                document.getElementById('randomMeta').innerText = `${data.genre || ''} • ${data.director || ''} • ${data.rating ?? ''}`;
                document.getElementById('randomPoster').src = data.posterUrl || '';
                const detailsLink = document.getElementById('randomDetails');
                detailsLink.href = `/movies/view?id=${data.id}`;
                detailsLink.style.display = 'inline-block';
            } catch (e) {
                console.error(e);
                alert('Ошибка при получении случайного фильма');
            }
        });
    }

    const registerForm = document.getElementById('registerForm');
    if (registerForm) {
        registerForm.addEventListener('submit', (e) => {
            const pass = registerForm.querySelector('input[name="password"]').value;
            if (pass.length < 6) {
                e.preventDefault();
                alert('Пароль должен быть не менее 6 символов');
            }
        });
    }
});