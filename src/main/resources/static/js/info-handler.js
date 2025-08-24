// info-handler.js

const infoHandler = {
    init(scenarioId, sceneId, apiClient, uiUpdater) {
        this.scenarioId = scenarioId;
        this.sceneId = sceneId;
        this.apiClient = apiClient;
        this.uiUpdater = uiUpdater;

        this.infoModal = new bootstrap.Modal(document.getElementById('infoModal'));
        this.deleteModal = new bootstrap.Modal(document.getElementById('deleteInfoConfirmModal'));
        this.infoForm = document.getElementById('infoForm');

        this.addEventListeners();
    },

    addEventListeners() {
        // Event listener for opening the "Add Info" modal
        document.getElementById('add-info-btn').addEventListener('click', () => {
            this.openInfoModal();
        });

        // Combined event listener for the scene-info-list (Edit and Remove)
        document.getElementById('scene-info-list').addEventListener('click', async (event) => {
            const target = event.target;

            // Handle "Edit Info" button click
            if (target.classList.contains('edit-info-btn')) {
                const infoId = target.dataset.infoId;
                const infoName = target.dataset.infoName;
                const infoContent = target.dataset.infoContent;
                this.openInfoModal({ id: infoId, name: infoName, content: infoContent });
                return; // Stop further processing
            }

            // Handle "Remove from Scene" button click
            if (target.classList.contains('remove-info-from-scene-btn')) {
                const sceneId = target.dataset.sceneId;
                const infoId = target.dataset.infoId;
                if (!sceneId || !infoId) return;

                try {
                    await this.apiClient.removeInfoFromScene(this.scenarioId, sceneId, infoId);
                    
                    // Refetch BOTH data sources in parallel to ensure consistency
                    const [updatedMainData, updatedInfosWithScenes] = await Promise.all([
                        this.apiClient.fetchSceneData(this.scenarioId, this.sceneId),
                        this.apiClient.fetchAllInfosWithScenes(this.scenarioId)
                    ]);

                    // Refresh all relevant parts of the UI
                    this.uiUpdater.refreshLists(updatedMainData, this.sceneId);
                    this.uiUpdater.renderAllInfosList(updatedInfosWithScenes, updatedMainData.scene.title);
                    this.uiUpdater.refreshPreview(updatedMainData);
                    
                    this.uiUpdater.showToast('情報をシーンから削除しました。');

                } catch (error) {
                    console.error('Failed to remove info from scene:', error);
                    alert('情報の削除に失敗しました。');
                }
            }
        });
        
        // Event listener for saving info (add or update)
        document.getElementById('save-info-btn').addEventListener('click', async () => {
            const infoId = this.infoForm.querySelector('#infoId').value;
            const name = this.infoForm.querySelector('#infoName').value;
            const content = this.infoForm.querySelector('#infoContent').value;

            try {
                if (infoId) { // Update
                    await this.apiClient.updateInfo(this.scenarioId, infoId, { name, content });
                } else { // Create
                    await this.apiClient.createInfo(this.scenarioId, { name, content });
                }
                
                // Refetch BOTH data sources in parallel
                const [updatedMainData, updatedInfosWithScenes] = await Promise.all([
                    this.apiClient.fetchSceneData(this.scenarioId, this.sceneId),
                    this.apiClient.fetchAllInfosWithScenes(this.scenarioId)
                ]);

                // Refresh all relevant parts of the UI with the correct data
                this.uiUpdater.refreshLists(updatedMainData, this.sceneId); // This handles scene-info-list
                this.uiUpdater.renderAllInfosList(updatedInfosWithScenes, updatedMainData.scene.title); // This handles all-info-list
                this.uiUpdater.refreshPreview(updatedMainData);

                this.uiUpdater.showToast('情報を保存しました。');
                this.infoModal.hide();

            } catch (error) {
                console.error('Failed to save info:', error);
                alert('情報の保存に失敗しました。');
            }
        });

        // Combined event listener for the all-info-list (Detail and Add)
        document.getElementById('all-info-list').addEventListener('click', async (event) => {
            const target = event.target;

            // Handle "Detail" button click
            if (target.classList.contains('detail-info-btn')) {
                const infoId = target.dataset.infoId;
                try {
                    const infoDetails = await this.apiClient.fetchInfoDetails(this.scenarioId, infoId);
                    document.getElementById('infoDetailName').textContent = infoDetails.name;
                    document.getElementById('infoDetailContent').textContent = infoDetails.content;
                } catch (error) {
                    console.error('Failed to fetch info details:', error);
                    alert('情報の詳細の取得に失敗しました。');
                    document.getElementById('infoDetailName').textContent = 'エラー';
                    document.getElementById('infoDetailContent').textContent = '詳細の取得に失敗しました。';
                }
                return;
            }

            // Handle "Add to Scene" button click
            if (target.classList.contains('add-info-to-scene-btn')) {
                const infoId = target.dataset.infoId;
                if (!infoId) return;

                try {
                    // The API doesn't require a body for this simple add operation
                    await this.apiClient.addInfoToScene(this.scenarioId, this.sceneId, infoId);

                    // Refetch BOTH data sources in parallel to ensure consistency
                    const [updatedMainData, updatedInfosWithScenes] = await Promise.all([
                        this.apiClient.fetchSceneData(this.scenarioId, this.sceneId),
                        this.apiClient.fetchAllInfosWithScenes(this.scenarioId)
                    ]);

                    // Refresh all relevant parts of the UI
                    this.uiUpdater.refreshLists(updatedMainData, this.sceneId);
                    this.uiUpdater.renderAllInfosList(updatedInfosWithScenes, updatedMainData.scene.title);
                    this.uiUpdater.refreshPreview(updatedMainData);

                    this.uiUpdater.showToast('情報をシーンに追加しました。');

                } catch (error) {
                    console.error('Failed to add info to scene:', error);
                    alert('情報の追加に失敗しました。');
                }
            }
        });

        // ... other event listeners for delete modal
    },

    openInfoModal(info = null) {
        this.infoForm.reset();
        const modalTitle = this.infoModal._element.querySelector('.modal-title');
        const infoIdInput = this.infoForm.querySelector('#infoId');
        
        if (info) {
            modalTitle.textContent = '情報編集';
            infoIdInput.value = info.id;
            this.infoForm.querySelector('#infoName').value = info.name;
            this.infoForm.querySelector('#infoContent').value = info.content;
        } else {
            modalTitle.textContent = '情報追加';
            infoIdInput.value = '';
        }
        this.infoModal.show();
    }
};
