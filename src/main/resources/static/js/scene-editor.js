// scene-editor.js

document.addEventListener('DOMContentLoaded', () => {
    const sceneDataContainer = document.getElementById('scene-data');
    const scenarioId = sceneDataContainer.dataset.scenarioId;
    const sceneId = sceneDataContainer.dataset.sceneId;
    const tinymceApiKey = sceneDataContainer.dataset.tinymceApiKey;

    // --- Modal Elements ---
    const unsavedChangesModalEl = document.getElementById('unsavedChangesModal');
    const unsavedChangesModal = new bootstrap.Modal(unsavedChangesModalEl);
    const saveAndNavigateBtn = document.getElementById('save-and-navigate-btn');
    const discardAndNavigateBtn = document.getElementById('discard-and-navigate-btn');

    let isDirty = false;
    let navigationUrl = null; // To store the URL to navigate to

    // --- Save Scene Content Logic ---
    const saveSceneContent = async (editor) => {
        const title = document.getElementById('scene-title').textContent;
        const content = editor.getContent();
        const gmInfo = document.getElementById('gm-info-textarea').value;
        const sceneData = { title, content, gmInfo };

        try {
            await apiClient.saveSceneContent(scenarioId, sceneId, sceneData);
            editor.setDirty(false);
            isDirty = false;
            uiUpdater.showSaveStatus('保存しました！');
            const updatedData = await apiClient.fetchSceneData(scenarioId, sceneId);
            uiUpdater.refreshPreview(updatedData);
            return true; // Indicate success
        } catch (error) {
            console.error('Save failed:', error);
            alert('保存に失敗しました。');
            return false; // Indicate failure
        }
    };

    // --- Delete Scene Logic ---
    const handleDeleteScene = async (idToDelete) => {
        if (!confirm('本当にこのシーンを削除しますか？この操作は元に戻せません。')) {
            return;
        }

        try {
            await apiClient.deleteScene(scenarioId, idToDelete);
            
            isDirty = false; // Avoid unsaved changes warning after deletion

            if (idToDelete.toString() === sceneId.toString()) {
                // The current scene was deleted, so we must redirect.
                isDirty = false; // Avoid unsaved changes warning.
                window.location.href = `/scenarios/${scenarioId}/edit`;
            } else {
                // Another scene was deleted. Refresh the page state.
                uiUpdater.showToast('シーンが削除されました。');
                await uiUpdater.refreshEntirePage(scenarioId, sceneId);
            }
        } catch (error) {
            console.error('Delete failed:', error);
            alert('シーンの削除に失敗しました。');
        }
    };

    // Function to initialize the page after TinyMCE is ready
    const initializePage = async (editor) => {
        try {
            // Initialize the commander first, so it has access to the apiClient
            uiUpdater.init(apiClient);

            const [mainData, infosWithScenes] = await Promise.all([
                apiClient.fetchSceneData(scenarioId, sceneId),
                apiClient.fetchAllInfosWithScenes(scenarioId)
            ]);

            uiUpdater.renderInitialPage(mainData, sceneId);
            uiUpdater.renderAllInfosList(infosWithScenes, mainData.scene.title);
            
            npcHandler.init(scenarioId, sceneId, apiClient, uiUpdater);
            infoHandler.init(scenarioId, sceneId, apiClient, uiUpdater);

        } catch (error) {
            console.error('Initialization failed:', error);
            alert('ページの読み込みに失敗しました。');
        }

        // --- Event Listeners ---

        // Handle unsaved changes for browser-level navigation (reload, back, close tab)
        window.addEventListener('beforeunload', (e) => {
            if (isDirty || editor.isDirty()) {
                e.preventDefault();
                e.returnValue = '';
            }
        });

        // Handle unsaved changes for in-page navigation (clicking links)
        document.body.addEventListener('click', (event) => {
            const link = event.target.closest('a');
            if (link && (isDirty || editor.isDirty())) {
                // Exclude links that open in a new tab or are not for navigation
                if (link.target === '_blank' || link.href.startsWith('javascript:')) {
                    return;
                }
                event.preventDefault();
                navigationUrl = link.href;
                unsavedChangesModal.show();
            }
        });

        // Modal button listeners
        discardAndNavigateBtn.addEventListener('click', () => {
            isDirty = false;
            editor.setDirty(false);
            window.location.href = navigationUrl;
        });

        saveAndNavigateBtn.addEventListener('click', async () => {
            const success = await saveSceneContent(editor);
            if (success) {
                window.location.href = navigationUrl;
            }
        });

        // Save content button
        document.getElementById('save-content-btn').addEventListener('click', () => {
            saveSceneContent(editor);
        });

        // Delete current scene button
        document.getElementById('delete-scene-btn').addEventListener('click', () => {
            handleDeleteScene(sceneId);
        });

        // Event delegation for delete icons in the scene list
        const sceneListContainer = document.getElementById('scene-list-container');
        sceneListContainer.addEventListener('click', (event) => {
            const deleteButton = event.target.closest('.delete-scene-icon-btn');
            if (deleteButton) {
                const idToDelete = deleteButton.dataset.sceneId;
                handleDeleteScene(idToDelete);
            }
        });
    };

    // Initialize TinyMCE
    tinymce.init({
        selector: '#content-editor',
        api_key: tinymceApiKey,
        plugins: 'advlist autolink lists link image charmap preview anchor',
        toolbar_mode: 'floating',
        toolbar: 'undo redo | formatselect | bold italic backcolor | alignleft aligncenter alignright alignjustify | bullist numlist outdent indent | removeformat | help',
        content_css: '/css/style.css',
        init_instance_callback: (editor) => {
            initializePage(editor);
        },
        setup: (editor) => {
            editor.on('dirty', () => {
                isDirty = true;
            });
        }
    });
});