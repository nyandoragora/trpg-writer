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

        // Event listener for opening the "Edit Info" modal (event delegation)
        document.getElementById('scene-info-list').addEventListener('click', (event) => {
            if (event.target.classList.contains('edit-info-btn')) {
                const button = event.target;
                const infoId = button.dataset.infoId;
                const infoName = button.dataset.infoName;
                const infoContent = button.dataset.infoContent;
                this.openInfoModal({ id: infoId, name: infoName, content: infoContent });
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
                
                const updatedData = await this.apiClient.fetchSceneData(this.scenarioId, this.sceneId);
                this.uiUpdater.refreshLists(updatedData);
                this.uiUpdater.refreshPreview(updatedData);
                this.uiUpdater.showSuccessToast('情報を保存しました。');
                this.infoModal.hide();

            } catch (error) {
                console.error('Failed to save info:', error);
                alert('情報の保存に失敗しました。');
            }
        });

        // Event listener for showing info details (event delegation)
        document.getElementById('all-info-list').addEventListener('click', (event) => {
            if (event.target.classList.contains('detail-info-btn')) {
                const button = event.target;
                const name = button.dataset.infoName;
                const content = button.dataset.infoContent;

                document.getElementById('infoDetailName').textContent = name;
                document.getElementById('infoDetailContent').textContent = content;
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
