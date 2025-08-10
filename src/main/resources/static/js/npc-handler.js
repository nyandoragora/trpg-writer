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
        // Event delegation for adding an NPC to the scene from the "All NPCs" list
        document.getElementById('all-npc-list').addEventListener('click', async (event) => {
            if (event.target.classList.contains('add-npc-to-scene-btn')) {
                const npcId = event.target.dataset.npcId;
                if (!npcId) return;

                try {
                    await this.apiClient.addNpcToScene(this.scenarioId, this.sceneId, npcId);
                    const updatedData = await this.apiClient.fetchSceneData(this.scenarioId, this.sceneId);
                    
                    // Refresh relevant UI components
                    this.uiUpdater.updateSceneNpcList(updatedData.sceneNpcs);
                    this.uiUpdater.updateAllNpcList(updatedData.allNpcs, updatedData.sceneNpcs);
                    this.uiUpdater.updateScenePreview(updatedData);
                    this.uiUpdater.showSuccessToast('NPCをシーンに追加しました。');

                } catch (error) {
                    console.error('Failed to add NPC:', error);
                    alert('NPCの追加に失敗しました。');
                }
            }
        });

        // Event listener for removing an NPC from the scene (event delegation)
        document.getElementById('scene-npc-list').addEventListener('click', async (event) => {
            if (event.target.classList.contains('remove-npc-from-scene-btn')) {
                const sceneNpcId = event.target.dataset.sceneNpcId;
                if (!sceneNpcId) return;

                if (confirm('このNPCをシーンから削除しますか？')) {
                    try {
                        await this.apiClient.removeNpcFromScene(this.scenarioId, this.sceneId, sceneNpcId);
                        const updatedData = await this.apiClient.fetchSceneData(this.scenarioId, this.sceneId);
                        
                        // Refresh relevant UI components
                        this.uiUpdater.updateSceneNpcList(updatedData.sceneNpcs);
                        this.uiUpdater.updateAllNpcList(updatedData.allNpcs, updatedData.sceneNpcs);
                        this.uiUpdater.updateScenePreview(updatedData);
                        this.uiUpdater.showSuccessToast('NPCを削除しました。');

                    } catch (error) {
                        console.error('Failed to remove NPC:', error);
                        alert('NPCの削除に失敗しました。');
                    }
                }
            }
        });

        // ... other event listeners for NPC detail modal, etc.
    }
};
