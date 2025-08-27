// info-list-renderer.js

const infoListRenderer = {
    // Render the list of infos currently in the scene
    renderSceneInfos(listElement, sceneInfos, sceneId) {
        if (!listElement) return;
        listElement.innerHTML = '';

        if (!sceneInfos || sceneInfos.length === 0) {
            listElement.innerHTML = '<p>このシーンに公開される情報はありません。</p>';
            return;
        }

        sceneInfos.forEach(sceneInfo => {
            const info = sceneInfo.info;
            if (!info) return;

            const card = this._createInfoCard(info, true, sceneId, sceneInfo.info.id);
            listElement.appendChild(card);
        });
    },

    // Render the list of all infos for the scenario with scene badges
    renderAllInfos(listElement, allInfosWithScenes, currentSceneTitle) {
        if (!listElement) return;
        listElement.innerHTML = '';

        if (!allInfosWithScenes || allInfosWithScenes.length === 0) {
            listElement.innerHTML = '<p>このシナリオにはまだ情報が登録されていません。</p>';
            return;
        }

        allInfosWithScenes.forEach(infoDto => {
            const card = this._createAllInfoCard(infoDto, currentSceneTitle);
            listElement.appendChild(card);
        });
    },

    // Helper function to create a single info card for the "All Info" list
    _createAllInfoCard(infoDto, currentSceneTitle) {
        const card = document.createElement('div');
        card.className = 'card mb-2';

        const cardBody = document.createElement('div');
        cardBody.className = 'card-body';

        const titleContainer = document.createElement('div');
        titleContainer.className = 'd-flex align-items-center mb-2';

        const name = document.createElement('h5');
        name.className = 'card-title mb-0 me-3';
        name.textContent = infoDto.title;

        titleContainer.appendChild(name);

        // Add scene badges
        if (infoDto.sceneNames && infoDto.sceneNames.length > 0) {
            infoDto.sceneNames.forEach(sceneName => {
                const badge = document.createElement('span');
                badge.className = 'badge me-1';
                if (sceneName === currentSceneTitle) {
                    badge.classList.add('bg-primary'); // Highlight for current scene
                } else {
                    badge.classList.add('bg-secondary');
                }
                badge.textContent = sceneName;
                titleContainer.appendChild(badge);
            });
        }

        const content = document.createElement('p');
        content.className = 'card-text';
        content.textContent = infoDto.summary; // Use summary from DTO

        const buttonGroup = document.createElement('div');
        buttonGroup.className = 'd-flex justify-content-end mt-2';

        const detailButton = document.createElement('button');
        detailButton.type = 'button';
        detailButton.className = 'btn btn-sm btn-info me-2 detail-info-btn';
        detailButton.dataset.bsToggle = 'modal';
        detailButton.dataset.bsTarget = '#infoDetailModal';
        detailButton.dataset.infoId = infoDto.id; // Use id from DTO
        detailButton.textContent = '詳細';

        const editButton = document.createElement('button');
        editButton.type = 'button';
        editButton.className = 'btn btn-sm btn-secondary me-2 edit-info-btn';
        editButton.dataset.bsToggle = 'modal';
        editButton.dataset.bsTarget = '#infoModal';
        editButton.dataset.infoId = infoDto.id;
        editButton.textContent = '編集';

        const addButton = document.createElement('button');
        addButton.type = 'button';
        addButton.className = 'btn btn-sm btn-primary add-info-to-scene-btn';
        addButton.dataset.infoId = infoDto.id;
        addButton.textContent = 'シーンに追加';
        
        buttonGroup.appendChild(detailButton);
        buttonGroup.appendChild(editButton);
        buttonGroup.appendChild(addButton);

        cardBody.appendChild(titleContainer);
        cardBody.appendChild(content); // Add summary content
        cardBody.appendChild(buttonGroup);
        card.appendChild(cardBody);
        return card;
    },

    // Helper function to create a single info card for the "Scene Info" list
    _createInfoCard(info, isInScene, sceneId = null, infoId = null) {
        const card = document.createElement('div');
        card.className = 'card mb-2';

        const cardBody = document.createElement('div');
        cardBody.className = 'card-body';

        const name = document.createElement('h5');
        name.className = 'card-title';
        name.textContent = info.name;

        const content = document.createElement('p');
        content.className = 'card-text';
        content.textContent = info.content ? (info.content.substring(0, 80) + (info.content.length > 80 ? '...' : '')) : '';

        const buttonGroup = document.createElement('div');
        buttonGroup.className = 'd-flex justify-content-end mt-2';

        if (isInScene) {
            const editButton = document.createElement('button');
            editButton.type = 'button';
            editButton.className = 'btn btn-sm btn-info me-2 edit-info-btn';
            editButton.dataset.bsToggle = 'modal';
            editButton.dataset.bsTarget = '#infoModal';
            editButton.dataset.infoId = info.id;
            editButton.dataset.infoName = info.name;
            editButton.dataset.infoContent = info.content;
            editButton.textContent = '編集';

            const removeButton = document.createElement('button');
            removeButton.type = 'button';
            removeButton.className = 'btn btn-sm btn-danger remove-info-from-scene-btn';
            removeButton.dataset.sceneId = sceneId;
            removeButton.dataset.infoId = infoId;
            removeButton.textContent = 'シーンから削除';

            buttonGroup.appendChild(editButton);
            buttonGroup.appendChild(removeButton);
        } 
        
        cardBody.appendChild(name);
        cardBody.appendChild(content);
        if (buttonGroup.hasChildNodes()) {
            cardBody.appendChild(buttonGroup);
        }
        card.appendChild(cardBody);
        return card;
    }
};
