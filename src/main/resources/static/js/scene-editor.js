// scene-editor.js

document.addEventListener('DOMContentLoaded', () => {
    const sceneDataContainer = document.getElementById('scene-data');
    const scenarioId = sceneDataContainer.dataset.scenarioId;
    const sceneId = sceneDataContainer.dataset.sceneId;
    const tinymceApiKey = sceneDataContainer.dataset.tinymceApiKey;

    let isDirty = false;

    // Function to initialize the page after TinyMCE is ready
    const initializePage = async (editor) => {
        try {
            const data = await apiClient.fetchSceneData(scenarioId, sceneId);
            uiUpdater.renderInitialPage(data);
            
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
