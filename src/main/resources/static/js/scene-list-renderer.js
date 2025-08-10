// scene-list-renderer.js

const sceneListRenderer = {
    render(container, allScenes, scenarioId) {
        if (!container) return;
        container.innerHTML = '';

        if (!allScenes || allScenes.length === 0) {
            container.innerHTML = '<div class="text-center text-muted"><p>シーンがありません</p></div>';
            return;
        }

        allScenes.forEach(scene => {
            const card = document.createElement('div');
            card.className = 'card mb-2';

            const cardBody = document.createElement('div');
            cardBody.className = 'card-body p-2 d-flex';

            const imageBox = document.createElement('div');
            imageBox.className = 'me-2';
            imageBox.style.cssText = 'width: 60px; height: 60px; flex-shrink: 0; background-color: #f0f0f0; display: flex; align-items: center; justify-content: center; overflow: hidden;';

            if (scene.imagePath) {
                const img = document.createElement('img');
                img.src = scene.imagePath;
                img.alt = 'シーン画像';
                img.style.cssText = 'max-width: 100%; max-height: 100%; object-fit: contain;';
                imageBox.appendChild(img);
            } else {
                const noImageSpan = document.createElement('span');
                noImageSpan.className = 'text-muted small';
                noImageSpan.textContent = '画像なし';
                imageBox.appendChild(noImageSpan);
            }

            const contentArea = document.createElement('div');
            contentArea.className = 'flex-grow-1';

            const title = document.createElement('h6');
            title.className = 'card-title mb-0';
            title.textContent = scene.title;

            const gmInfo = document.createElement('p');
            gmInfo.className = 'card-text text-muted small mb-1';
            gmInfo.textContent = scene.gmInfo ? (scene.gmInfo.substring(0, 25) + (scene.gmInfo.length > 25 ? '...' : '')) : '';

            const editLink = document.createElement('a');
            editLink.href = `/scenarios/${scenarioId}/scenes/${scene.id}/edit`;
            editLink.className = 'btn btn-sm btn-outline-primary w-100';
            editLink.textContent = '編集';

            contentArea.appendChild(title);
            contentArea.appendChild(gmInfo);
            contentArea.appendChild(editLink);
            cardBody.appendChild(imageBox);
            cardBody.appendChild(contentArea);
            card.appendChild(cardBody);
            container.appendChild(card);
        });
    }
};
