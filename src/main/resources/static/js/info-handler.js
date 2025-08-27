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
        
        this.deleteConfirmed = false; // Flag to track if deletion was confirmed

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
            const infoId = target.dataset.infoId;

            // Handle "Edit Info" button click
            if (target.classList.contains('edit-info-btn')) {
                if (!infoId) return;
                try {
                    const info = await this.apiClient.fetchInfoDetails(this.scenarioId, infoId);
                    this.openInfoModal(info);
                } catch (error) {
                    console.error('Failed to fetch info details for editing:', error);
                    alert('情報の読み込みに失敗しました。');
                }
                return;
            }

            // Handle "Remove from Scene" button click
            if (target.classList.contains('remove-info-from-scene-btn')) {
                const sceneId = target.dataset.sceneId;
                if (!sceneId || !infoId) {
                    return;
                }

                try {
                    await this.apiClient.removeInfoFromScene(this.scenarioId, sceneId, infoId);
                    await this.uiUpdater.refreshEntirePage(this.scenarioId, this.sceneId);
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
                    // Note: The original createInfo was tied to a scene. 
                    // A more general create might be needed if called from "all-infos" context.
                    // For now, assuming creation is always linked to the current scene.
                    await this.apiClient.createInfo(this.scenarioId, { name, content, sceneId: this.sceneId });
                }
                
                await this.uiUpdater.refreshEntirePage(this.scenarioId, this.sceneId);
                this.uiUpdater.showToast('情報を保存しました。');
                this.infoModal.hide();

            } catch (error) {
                console.error('Failed to save info:', error);
                alert('情報の保存に失敗しました。');
            }
        });

        // Combined event listener for the all-info-list (Detail, Add, Edit)
        document.getElementById('all-info-list').addEventListener('click', async (event) => {
            const target = event.target;
            const infoId = target.dataset.infoId;
            if (!infoId) return;

            // Handle "Detail" button click
            if (target.classList.contains('detail-info-btn')) {
                try {
                    const infoDetails = await this.apiClient.fetchInfoDetails(this.scenarioId, infoId);
                    document.getElementById('infoDetailName').textContent = infoDetails.name;
                    document.getElementById('infoDetailContent').textContent = infoDetails.content;
                } catch (error) {
                    console.error('Failed to fetch info details:', error);
                    alert('情報の詳細の取得に失敗しました。');
                }
                return;
            }

            // Handle "Add to Scene" button click
            if (target.classList.contains('add-info-to-scene-btn')) {
                try {
                    await this.apiClient.addInfoToScene(this.scenarioId, this.sceneId, infoId);
                    await this.uiUpdater.refreshEntirePage(this.scenarioId, this.sceneId);
                    this.uiUpdater.showToast('情報をシーンに追加しました。');
                } catch (error) {
                    console.error('Failed to add info to scene:', error);
                    alert('情報の追加に失敗しました。');
                }
                return;
            }

            // Handle "Edit Info" button click (added for all-info-list)
            if (target.classList.contains('edit-info-btn')) {
                try {
                    const info = await this.apiClient.fetchInfoDetails(this.scenarioId, infoId);
                    this.openInfoModal(info);
                } catch (error) {
                    console.error('Failed to fetch info details for editing:', error);
                    alert('情報の読み込みに失敗しました。');
                }
            }
        });

        // Event listener for the final delete confirmation button
        document.getElementById('confirm-delete-info-btn').addEventListener('click', async (event) => {
            const infoId = event.target.dataset.infoId;
            if (!infoId) return;

            try {
                this.deleteConfirmed = true; // Set the flag before hiding
                await this.apiClient.deleteInfo(this.scenarioId, infoId);
                await this.uiUpdater.refreshEntirePage(this.scenarioId, this.sceneId);
                
                this.deleteModal.hide();
                this.uiUpdater.showToast('情報を完全に削除しました。');

            } catch (error) {
                console.error('Failed to delete info:', error);
                alert('情報の削除に失敗しました。');
                this.deleteConfirmed = false; // Reset flag on error
            }
        });

        // Event listener to bridge the edit modal's delete button to the confirmation modal
        document.getElementById('delete-info-btn').addEventListener('click', (event) => {
            const infoId = event.target.dataset.infoId;
            document.getElementById('confirm-delete-info-btn').dataset.infoId = infoId;
            this.infoModal.hide();
            this.deleteModal.show();
        });

        // Event listener for when the delete confirmation modal is hidden
        this.deleteModal._element.addEventListener('hidden.bs.modal', () => {
            if (!this.deleteConfirmed) {
                // If deletion was not confirmed (i.e., cancelled), show the edit modal again
                this.infoModal.show();
            }
            // Reset the flag for the next operation
            this.deleteConfirmed = false;
        });
    },

    openInfoModal(info = null) {
        this.infoForm.reset();
        const modalTitle = this.infoModal._element.querySelector('.modal-title');
        const infoIdInput = this.infoForm.querySelector('#infoId');
        const deleteBtn = this.infoModal._element.querySelector('#delete-info-btn');
        
        if (info) {
            modalTitle.textContent = '情報編集';
            infoIdInput.value = info.id;
            this.infoForm.querySelector('#infoName').value = info.name;
            this.infoForm.querySelector('#infoContent').value = info.content;
            deleteBtn.dataset.infoId = info.id;
            deleteBtn.style.display = 'inline-block'; // Show delete button for existing info
        } else {
            modalTitle.textContent = '情報追加';
            infoIdInput.value = '';
            deleteBtn.style.display = 'none'; // Hide delete button for new info
        }
        this.infoModal.show();
    }
};
