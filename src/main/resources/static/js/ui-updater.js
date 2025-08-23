// ui-updater.js

const uiUpdater = {
    allNpcsData: [], // Store all NPC data here

    // The main function to orchestrate the initial page rendering
    renderInitialPage(data, sceneId) {
        this.allNpcsData = data.allNpcs || []; // Store NPC data

        // Set static content like titles and text areas
        document.getElementById('scenario-title').textContent = data.scenario.title;
        document.getElementById('scene-title').textContent = data.scene.title;
        tinymce.get('content-editor').setContent(data.scene.content || '');
        document.getElementById('gm-info-textarea').value = data.scene.gmInfo || '';

        // Delegate rendering tasks to specialized renderer objects
        this.refreshLists(data, sceneId);
        this.refreshPreview(data);
    },

    // A function to refresh all dynamic lists, can be called after any data change
    refreshLists(data, sceneId) {
        this.allNpcsData = data.allNpcs || []; // Update NPC data
        const scenarioId = data.scenario.id;
        
        // NPC Lists
        const sceneNpcListEl = document.getElementById('scene-npc-list');
        const allNpcListEl = document.getElementById('all-npc-list');
        console.log('[ui-updater.js] About to call npcListRenderer. Is it defined?', npcListRenderer); // ★ 新しい偵察ログ
        npcListRenderer.renderSceneNpcs(sceneNpcListEl, data.sceneNpcs, scenarioId, sceneId);
        npcListRenderer.renderAllNpcs(allNpcListEl, data.allNpcs, data.sceneNpcs, scenarioId, sceneId);

        // Info Lists
        const sceneInfoListEl = document.getElementById('scene-info-list');
        const allInfoListEl = document.getElementById('all-info-list');
        infoListRenderer.renderSceneInfos(sceneInfoListEl, data.sceneInfos);
        infoListRenderer.renderAllInfos(allInfoListEl, data.allInfos, data.sceneInfos);
        
        // Scene List
        const sceneListContainerEl = document.getElementById('scene-list-container');
        sceneListRenderer.render(sceneListContainerEl, data.allScenes, scenarioId);
    },

    // A function to specifically render the "All Infos" list
    renderAllInfosList(infosWithScenes, currentSceneTitle) {
        const allInfoListEl = document.getElementById('all-info-list');
        infoListRenderer.renderAllInfos(allInfoListEl, infosWithScenes, currentSceneTitle);
    },

    // Helper function to get a single NPC by its ID
    getNpcById(npcId) {
        return this.allNpcsData.find(npc => npc.id === parseInt(npcId, 10));
    },

    // A function to specifically refresh the preview panel
    refreshPreview(data) {
        const previewCardEl = document.getElementById('scene-preview-card');
        scenePreviewRenderer.render(previewCardEl, data);
    },

    // --- Utility functions for showing feedback to the user ---

    showSaveStatus(message) {
        const statusElement = document.getElementById('save-status-message');
        if (statusElement) {
            statusElement.textContent = message;
            setTimeout(() => {
                statusElement.textContent = '';
            }, 3000); // Message disappears after 3 seconds
        }
    },

    showSuccessToast(message) {
        // This can be implemented with a library like Toastr.js or a simple custom element
        // For now, a simple alert will suffice.
        alert(message);
    },

    displayValidationErrors(errors) {
        // This can be implemented to show errors in a modal
        const errorString = errors.map(e => e.defaultMessage).join('\n');
        alert(`Validation errors:\n${errorString}`);
    },

    updateNpcDetailModal(npc) { // npc is now NpcDetailDto
        const modalBody = document.getElementById('npcDetailModalBody');
        if (!modalBody) return;

        let content = `
            <h4>${npc.name} <small class="text-muted">Lv: ${npc.level}</small></h4>
            <p>${npc.description}</p>
            <hr>
            <h5>基本情報</h5>
            <table class="table table-sm table-bordered">
                <tbody>
                    <tr>
                        <th>知能</th><td>${npc.intelligence || ''}</td>
                        <th>知覚</th><td>${npc.perception || ''}</td>
                        <th>反応</th><td>${npc.position || ''}</td>
                    </tr>
                    <tr>
                        <th>穢れ</th><td>${npc.impurity || ''}</td>
                        <th>言語</th><td>${npc.language || ''}</td>
                        <th>生息地</th><td>${npc.habitat || ''}</td>
                    </tr>
                    <tr>
                        <th>知名度/弱点値</th><td>${npc.popularity || ''}</td>
                        <th>弱点</th><td>${npc.weakness || ''}</td>
                        <th>先制値</th><td>${npc.preemptive || ''}</td>
                    </tr>
                    <tr>
                        <th>移動速度</th><td>${npc.movement || ''}</td>
                        <th>生命抵抗力</th><td>${npc.lifeResist || ''}</td>
                        <th>精神抵抗力</th><td>${npc.mindResist || ''}</td>
                    </tr>
                </tbody>
            </table>
        `;

        if (npc.parts && npc.parts.length > 0) {
            content += '<h5>部位詳細</h5>';
            npc.parts.forEach(part => {
                content += `
                    <div class="card mb-2">
                        <div class="card-body">
                            <h6 class="card-title">${part.name}</h6>
                            <table class="table table-sm table-bordered mb-0">
                                <tbody>
                                    <tr>
                                        <th>命中力</th><td>${part.hit}</td>
                                        <th>打点</th><td>${part.damage}</td>
                                        <th>回避力</th><td>${part.evasion}</td>
                                    </tr>
                                    <tr>
                                        <th>防護点</th><td>${part.defense}</td>
                                        <th>HP</th><td>${part.hitPoint}</td>
                                        <th>MP</th><td>${part.magicPoint}</td>
                                    </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>
                `;
            });
        }

        if (npc.skills && npc.skills.length > 0) {
            content += '<h5 class="mt-3">特殊能力</h5>';
            npc.skills.forEach(skill => { // Simplified loop
                content += `
                    <div class="card mb-2">
                        <div class="card-body">
                            <h6 class="card-title">${skill.name}</h6>
                            <p class="card-text">${skill.content}</p>
                        </div>
                    </div>
                `;
            });
        }

        if (npc.bootys && npc.bootys.length > 0) {
            content += '<h5 class="mt-3">戦利品</h5>';
            npc.bootys.forEach(booty => { // Simplified loop and fixed property name
                content += `
                    <div class="card mb-2">
                        <div class="card-body">
                            <h6 class="card-title">${booty.diceNum}</h6>
                            <p class="card-text">${booty.content}</p>
                        </div>
                    </div>
                `;
            });
        }

        modalBody.innerHTML = content;

        // Add delete button to the modal footer
        const modalFooter = document.querySelector('#npcDetailModal .modal-footer');
        // Remove existing delete button to prevent duplicates
        const existingDeleteBtn = modalFooter.querySelector('.delete-npc-btn');
        if (existingDeleteBtn) {
            existingDeleteBtn.remove();
        }
        
        const deleteButton = document.createElement('button');
        deleteButton.type = 'button';
        deleteButton.className = 'btn btn-danger me-auto delete-npc-btn';
        deleteButton.dataset.npcId = npc.id;
        deleteButton.textContent = 'このNPCを削除';
        
        // Prepend to keep "Close" button on the right
        modalFooter.prepend(deleteButton);
    },

    /**
     * Displays a toast notification with a given message.
     * @param {string} message - The message to display in the toast.
     */
    showToast(message) {
        const toastTemplate = document.getElementById('toast-template');
        const toastContainer = document.querySelector('.toast-container');

        if (!toastTemplate || !toastContainer) {
            console.error('Toast template or container not found.');
            // Fallback to alert if the toast elements are not on the page
            alert(message);
            return;
        }

        // Clone the template
        const newToastEl = toastTemplate.cloneNode(true);
        
        // It's a good practice to give a unique ID to the cloned element
        newToastEl.id = `toast-${new Date().getTime()}`;
        
        // Set the message
        const toastBody = newToastEl.querySelector('.toast-body');
        if (toastBody) {
            toastBody.textContent = message;
        }

        // The template is hidden, so we need to make the clone visible
        newToastEl.style.display = '';

        // Add the new toast to the container
        toastContainer.appendChild(newToastEl);

        // Create a new Bootstrap Toast instance and show it
        const toast = new bootstrap.Toast(newToastEl);
        toast.show();

        // Clean up the DOM after the toast is hidden
        newToastEl.addEventListener('hidden.bs.toast', () => {
            newToastEl.remove();
        });
    }
};