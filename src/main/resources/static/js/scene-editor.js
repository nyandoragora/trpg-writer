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

        // --- Background Image Upload Logic ---
        const bgUploadForm = document.querySelector('#background form');
        const bgUploadBtn = bgUploadForm.querySelector('button[type="submit"]');
        const imageInput = bgUploadForm.querySelector('#imageFile');

        bgUploadForm.addEventListener('submit', async (event) => {
            event.preventDefault(); // Stop default form submission

            const file = imageInput.files[0];
            if (!file) {
                alert('画像ファイルを選択してください。');
                return;
            }

            const formData = new FormData();
            formData.append('imageFile', file);

            bgUploadBtn.disabled = true;
            bgUploadBtn.innerHTML = '<span class="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span> アップロード中...';

            try {
                const response = await apiClient.uploadSceneImage(scenarioId, sceneId, formData);
                
                const previewCard = document.getElementById('scene-preview-card');
                // Add a cache-busting query parameter to the URL to ensure the browser fetches the new image
                const newImageUrl = `${response.imageUrl}?t=${new Date().getTime()}`;
                
                // Set the background image and ensure styling is reapplied for consistent display
                previewCard.style.backgroundImage = `url('${newImageUrl}')`;
                previewCard.style.backgroundSize = 'cover';
                previewCard.style.backgroundPosition = 'center';
                previewCard.style.backgroundRepeat = 'no-repeat';

                uiUpdater.showToast('背景画像を更新しました。');
                imageInput.value = ''; // Clear the input

            } catch (error) {
                console.error('Image upload failed:', error);
                alert('画像のアップロードに失敗しました。');
            } finally {
                bgUploadBtn.disabled = false;
                bgUploadBtn.textContent = 'アップロード';
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