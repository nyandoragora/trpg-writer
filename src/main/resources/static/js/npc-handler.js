// npc-handler.js

const npcHandler = {
    init(scenarioId, sceneId, apiClient, uiUpdater) {
        this.scenarioId = scenarioId;
        this.sceneId = sceneId;
        this.apiClient = apiClient;
        this.uiUpdater = uiUpdater;

        this.addEventListeners();
    },

    addEventListeners() {
        const allNpcList = document.getElementById('all-npc-list');
        const sceneNpcList = document.getElementById('scene-npc-list');

        // --- Event listener for "Add to Scene" button ---
        allNpcList.addEventListener('click', async (event) => {
            if (event.target.classList.contains('add-npc-to-scene-btn')) {
                const npcId = event.target.dataset.npcId;
                if (!npcId) return;

                try {
                    await this.apiClient.addNpcToScene(this.scenarioId, this.sceneId, npcId);
                    const updatedData = await this.apiClient.fetchSceneData(this.scenarioId, this.sceneId);
                    
                    this.uiUpdater.refreshLists(updatedData, this.sceneId);
                    this.uiUpdater.refreshPreview(updatedData);
                    this.uiUpdater.showSuccessToast('NPCをシーンに追加しました。');

                } catch (error) {
                    console.error('Failed to add NPC:', error);
                    alert('NPCの追加に失敗しました。');
                }
            }
        });

        // --- Event listener for "Remove from Scene" button ---
        sceneNpcList.addEventListener('click', async (event) => {
            if (event.target.classList.contains('remove-npc-from-scene-btn')) {
                const sceneNpcId = event.target.dataset.sceneNpcId;
                if (!sceneNpcId) return;

                if (confirm('このNPCをシーンから削除しますか？')) {
                    try {
                        await this.apiClient.removeNpcFromScene(this.scenarioId, this.sceneId, sceneNpcId);
                        const updatedData = await this.apiClient.fetchSceneData(this.scenarioId, this.sceneId);
                        
                        this.uiUpdater.refreshLists(updatedData, this.sceneId);
                        this.uiUpdater.refreshPreview(updatedData);
                        this.uiUpdater.showSuccessToast('NPCを削除しました。');

                    } catch (error) {
                        console.error('Failed to remove NPC:', error);
                        alert('NPCの削除に失敗しました。');
                    }
                }
            }
        });

        // --- Event listener for "Detail" button on ALL NPCs list ---
        allNpcList.addEventListener('click', (event) => {
            if (event.target.classList.contains('detail-npc-btn')) {
                const npcId = event.target.dataset.npcId;
                const npc = this.uiUpdater.getNpcById(npcId);
                if (npc) {
                    this.uiUpdater.updateNpcDetailModal(npc);
                }
            }
        });

        // --- Event listener for "Detail" button on SCENE NPCs list ---
        sceneNpcList.addEventListener('click', (event) => {
            if (event.target.classList.contains('detail-npc-btn')) {
                const npcId = event.target.dataset.npcId;
                // Scene NPCs are a subset of all NPCs, so we can still use the same getter
                const npc = this.uiUpdater.getNpcById(npcId);
                if (npc) {
                    this.uiUpdater.updateNpcDetailModal(npc);
                }
            }
        });

        // --- Event listener for the "Delete NPC" button inside the modal ---
        document.getElementById('npcDetailModal').addEventListener('click', async (event) => {
            if (event.target.classList.contains('delete-npc-btn')) {
                const npcId = event.target.dataset.npcId;
                if (!npcId) return;

                if (confirm('このNPCをシナリオから完全に削除します。本当によろしいですか？\n（このNPCが登場する全てのシーンからも削除されます）')) {
                    try {
                        await this.apiClient.deleteNpc(this.scenarioId, npcId);
                        
                        // Hide the modal
                        const modal = bootstrap.Modal.getInstance(document.getElementById('npcDetailModal'));
                        modal.hide();

                        // Fetch the latest data and refresh the entire UI
                        const updatedData = await this.apiClient.fetchSceneData(this.scenarioId, this.sceneId);
                        this.uiUpdater.refreshLists(updatedData);
                        this.uiUpdater.refreshPreview(updatedData);
                        
                        this.uiUpdater.showSuccessToast('NPCを削除しました。');

                    } catch (error) {
                        console.error('Failed to delete NPC:', error);
                        alert('NPCの削除に失敗しました。');
                    }
                }
            }
        });
    }
};
