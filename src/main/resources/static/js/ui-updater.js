// ui-updater.js

const uiUpdater = {
    // The main function to orchestrate the initial page rendering
    renderInitialPage(data) {
        // Set static content like titles and text areas
        document.getElementById('scenario-title').textContent = data.scenario.title;
        document.getElementById('scene-title').textContent = data.scene.title;
        tinymce.get('content-editor').setContent(data.scene.content || '');
        document.getElementById('gm-info-textarea').value = data.scene.gmInfo || '';

        // Delegate rendering tasks to specialized renderer objects
        this.refreshLists(data);
        this.refreshPreview(data);
    },

    // A function to refresh all dynamic lists, can be called after any data change
    refreshLists(data) {
        const scenarioId = data.scenario.id;
        
        // NPC Lists
        const sceneNpcListEl = document.getElementById('scene-npc-list');
        const allNpcListEl = document.getElementById('all-npc-list');
        npcListRenderer.renderSceneNpcs(sceneNpcListEl, data.sceneNpcs, scenarioId);
        npcListRenderer.renderAllNpcs(allNpcListEl, data.allNpcs, data.sceneNpcs, scenarioId);

        // Info Lists
        const sceneInfoListEl = document.getElementById('scene-info-list');
        const allInfoListEl = document.getElementById('all-info-list');
        infoListRenderer.renderSceneInfos(sceneInfoListEl, data.sceneInfos);
        infoListRenderer.renderAllInfos(allInfoListEl, data.allInfos, data.sceneInfos);
        
        // Scene List
        const sceneListContainerEl = document.getElementById('scene-list-container');
        sceneListRenderer.render(sceneListContainerEl, data.allScenes, scenarioId);
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
    }
};