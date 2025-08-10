// info-list-renderer.js

const infoListRenderer = {
    // Render the list of infos currently in the scene
    renderSceneInfos(listElement, sceneInfos) {
        if (!listElement) return;
        listElement.innerHTML = '';

        if (!sceneInfos || sceneInfos.length === 0) {
            listElement.innerHTML = '<p>このシーンに公開される情報はありません。</p>';
            return;
        }

        sceneInfos.forEach(sceneInfo => {
            const info = sceneInfo.info;
            if (!info) return;

            const card = this._createInfoCard(info, true, sceneInfo.id);
            listElement.appendChild(card);
        });
    },

    // Render the list of all infos for the scenario
    renderAllInfos(listElement, allInfos, sceneInfos) {
        if (!listElement) return;
        listElement.innerHTML = '';

        if (!allInfos || allInfos.length === 0) {
            listElement.innerHTML = '<p>このシナリオにはまだ情報が登録されていません。</p>';
            return;
        }

        allInfos.forEach(info => {
            const card = this._createAllInfoCard(info);
            listElement.appendChild(card);
        });
    },

    // Helper function to create a single info card for the "All Info" list
    _createAllInfoCard(info) {
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

        const detailButton = document.createElement('button');
        detailButton.type = 'button';
        detailButton.className = 'btn btn-sm btn-info detail-info-btn';
        detailButton.dataset.bsToggle = 'modal';
        detailButton.dataset.bsTarget = '#infoDetailModal';
        detailButton.dataset.infoName = info.name;
        detailButton.dataset.infoContent = info.content;
        detailButton.textContent = '詳細';
        
        buttonGroup.appendChild(detailButton);

        cardBody.appendChild(name);
        cardBody.appendChild(content);
        cardBody.appendChild(buttonGroup);
        card.appendChild(cardBody);
        return card;
    },

    // Helper function to create a single info card for the "Scene Info" list
    _createInfoCard(info, isInScene, sceneInfoId = null) {
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
            removeButton.dataset.sceneInfoId = sceneInfoId;
            removeButton.textContent = '削除';

            buttonGroup.appendChild(editButton);
            buttonGroup.appendChild(removeButton);
        } 
        // No "add" button for now to keep it simple. 
        // We can add it back later if needed.
        
        cardBody.appendChild(name);
        cardBody.appendChild(content);
        if (buttonGroup.hasChildNodes()) {
            cardBody.appendChild(buttonGroup);
        }
        card.appendChild(cardBody);
        return card;
    }
};
