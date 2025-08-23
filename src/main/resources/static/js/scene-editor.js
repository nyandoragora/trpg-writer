// scene-editor.js

document.addEventListener('DOMContentLoaded', () => {
    const sceneDataContainer = document.getElementById('scene-data');
    const scenarioId = sceneDataContainer.dataset.scenarioId;
    const sceneId = sceneDataContainer.dataset.sceneId;
    const tinymceApiKey = sceneDataContainer.dataset.tinymceApiKey;

    let isDirty = false;

    // --- Delete Scene Logic ---
    const handleDeleteScene = async (idToDelete) => {
        if (!confirm('本当にこのシーンを削除しますか？この操作は元に戻せません。')) {
            return;
        }

        try {
            await apiClient.deleteScene(scenarioId, idToDelete);
            
            // If the currently edited scene is deleted, redirect.
            if (idToDelete.toString() === sceneId.toString()) {
                alert('シーンが削除されました。シナリオ編集画面に戻ります。');
                window.location.href = `/scenarios/${scenarioId}/edit`;
            } else {
                // If a scene from the list is deleted, just refresh the data and UI
                alert('シーンが削除されました。');
                const updatedData = await apiClient.fetchSceneData(scenarioId, sceneId);
                uiUpdater.renderInitialPage(updatedData, sceneId);
            }
        } catch (error) {
            console.error('Delete failed:', error);
            alert('シーンの削除に失敗しました。');
        }
    };


    // Function to initialize the page after TinyMCE is ready
    const initializePage = async (editor) => {
        try {
            // Fetch main data and info list data in parallel
            const [mainData, infosWithScenes] = await Promise.all([
                apiClient.fetchSceneData(scenarioId, sceneId),
                apiClient.fetchAllInfosWithScenes(scenarioId) // New API call
            ]);

            uiUpdater.renderInitialPage(mainData, sceneId);
            uiUpdater.renderAllInfosList(infosWithScenes, mainData.scene.title); // New UI update call
            
            // Now that the page is rendered, initialize handlers
            npcHandler.init(scenarioId, sceneId, apiClient, uiUpdater);
            infoHandler.init(scenarioId, sceneId, apiClient, uiUpdater);

        } catch (error) {
            console.error('Initialization failed:', error);
            alert('ページの読み込みに失敗しました。');
        }

        // Handle unsaved changes
        window.addEventListener('beforeunload', (e) => {
            if (isDirty || editor.isDirty()) {
                e.preventDefault();
                e.returnValue = '';
            }
        });

        // Save content button
        document.getElementById('save-content-btn').addEventListener('click', async () => {
            const title = document.getElementById('scene-title').textContent;
            const content = editor.getContent();
            const gmInfo = document.getElementById('gm-info-textarea').value;
            const sceneData = { title, content, gmInfo };

            try {
                await apiClient.saveSceneContent(scenarioId, sceneId, sceneData);
                editor.setDirty(false);
                isDirty = false; 
                uiUpdater.showSaveStatus('保存しました！'); // Use the new status message

                // Refetch data to update preview as title might have changed
                const updatedData = await apiClient.fetchSceneData(scenarioId, sceneId);
                uiUpdater.refreshPreview(updatedData);

            } catch (error) {
                console.error('Save failed:', error);
                alert('保存に失敗しました。');
            }
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
            initializePage(editor); // This function is called when the editor is ready
        },
        setup: (editor) => {
            editor.on('dirty', () => {
                isDirty = true;
            });
        }
    });
});