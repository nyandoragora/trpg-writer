// npc-list-renderer.js

const npcListRenderer = {
    // Render the list of NPCs currently in the scene
    renderSceneNpcs(listElement, sceneNpcs, scenarioId) {
        if (!listElement) return;
        listElement.innerHTML = '';

        if (!sceneNpcs || sceneNpcs.length === 0) {
            listElement.innerHTML = '<p>このシーンに登場するNPCはいません。</p>';
            return;
        }

        sceneNpcs.forEach(sceneNpc => {
            const npc = sceneNpc.npc;
            if (!npc) return;

            const listItem = this._createNpcListItem(npc, scenarioId, true, sceneNpc.id);
            listElement.appendChild(listItem);
        });
    },

    // Render the list of all NPCs for the scenario
    renderAllNpcs(listElement, allNpcs, sceneNpcs, scenarioId) {
        if (!listElement) return;
        listElement.innerHTML = '';

        if (!allNpcs || allNpcs.length === 0) {
            listElement.innerHTML = '<p>このシナリオにはまだNPCが登録されていません。</p>';
            return;
        }
        
        const sceneNpcIds = new Set((sceneNpcs || []).map(sn => sn.npc.id));

        allNpcs.forEach(npc => {
            const isInScene = sceneNpcIds.has(npc.id);
            const listItem = this._createNpcListItem(npc, scenarioId, isInScene);
            listElement.appendChild(listItem);
        });
    },

    // Helper function to create a single NPC list item
    _createNpcListItem(npc, scenarioId, isInScene, sceneNpcId = null) {
        const listItem = document.createElement('div');
        listItem.className = 'list-group-item list-group-item-action flex-column align-items-start';

        const header = document.createElement('div');
        header.className = 'd-flex w-100 justify-content-between';
        
        const name = document.createElement('h5');
        name.className = 'mb-1';
        name.textContent = npc.name;

        const level = document.createElement('small');
        level.textContent = `Lv: ${npc.level}`;

        header.appendChild(name);
        header.appendChild(level);

        const description = document.createElement('p');
        description.className = 'mb-1';
        description.textContent = npc.description;

        const detailButton = document.createElement('button');
        detailButton.type = 'button';
        detailButton.className = 'btn btn-sm btn-info mt-2 me-2 detail-npc-btn';
        detailButton.dataset.bsToggle = 'modal';
        detailButton.dataset.bsTarget = '#npcDetailModal';
        detailButton.dataset.npcId = npc.id;
        detailButton.textContent = '詳細';

        const editButton = document.createElement('a');
        editButton.href = `/scenarios/${scenarioId}/npcs/${npc.id}/edit`;
        editButton.className = 'btn btn-sm btn-warning mt-2 me-2';
        editButton.textContent = '編集';

        listItem.appendChild(header);
        listItem.appendChild(description);
        listItem.appendChild(detailButton);
        listItem.appendChild(editButton);
        
        if (isInScene) {
            const removeButton = document.createElement('button');
            removeButton.type = 'button';
            removeButton.className = 'btn btn-sm btn-danger mt-2 remove-npc-from-scene-btn';
            removeButton.dataset.sceneNpcId = sceneNpcId;
            removeButton.textContent = 'シーンから削除';
            listItem.appendChild(removeButton);
        } else {
            const addButton = document.createElement('button');
            addButton.type = 'button';
            addButton.className = 'btn btn-sm btn-success mt-2 add-npc-to-scene-btn';
            addButton.dataset.npcId = npc.id;
            addButton.textContent = 'シーンに追加';
            listItem.appendChild(addButton);
        }
        
        return listItem;
    }
};
