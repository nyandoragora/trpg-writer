// api-client.js

const apiClient = {
    // CSRFトークンを取得するヘルパーメソッド
    _getCsrfToken() {
        return document.querySelector('meta[name="_csrf"]').getAttribute('content');
    },
    _getCsrfHeader() {
        return document.querySelector('meta[name="_csrf_header"]').getAttribute('content');
    },

    // API呼び出しの共通処理 (JSON用)
    async _fetch(url, options = {}) {
        const headers = {
            'Content-Type': 'application/json',
            [this._getCsrfHeader()]: this._getCsrfToken(),
            ...options.headers,
        };

        const response = await fetch(url, {
            credentials: 'include',
            ...options,
            headers,
        });

        if (!response.ok) {
            try {
                const errorData = await response.json();
                const errorMessage = errorData.message || JSON.stringify(errorData);
                throw new Error(`API Error: ${errorMessage}`);
            } catch (e) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
        }
        return response.text().then(text => text ? JSON.parse(text) : null);
    },

    // API呼び出しの共通処理 (ファイルアップロード用)
    async _fetchMultipart(url, options = {}) {
        const headers = {
            [this._getCsrfHeader()]: this._getCsrfToken(),
            // 'Content-Type' is NOT set here. The browser will set it automatically for FormData.
            ...options.headers,
        };

        const response = await fetch(url, {
            credentials: 'include',
            ...options,
            headers,
        });

        if (!response.ok) {
            try {
                const errorData = await response.json();
                const errorMessage = errorData.message || JSON.stringify(errorData);
                throw new Error(`API Error: ${errorMessage}`);
            } catch (e) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
        }
        return response.text().then(text => text ? JSON.parse(text) : null);
    },

    // シーンデータ取得
    fetchSceneData(scenarioId, sceneId) {
        return this._fetch(`/scenarios/${scenarioId}/scenes/${sceneId}/edit-data`, { method: 'GET' });
    },

    // 全情報リスト（シーン名付き）取得
    fetchAllInfosWithScenes(scenarioId) {
        return this._fetch(`/scenarios/${scenarioId}/scenes/all-infos`, { method: 'GET' });
    },

    // シーン本文更新 (URLと送信データを修正)
    saveSceneContent(scenarioId, sceneId, sceneData) {
        return this._fetch(`/scenarios/${scenarioId}/scenes/${sceneId}/update`, {
            method: 'POST',
            body: JSON.stringify(sceneData),
        });
    },

    // シーンにNPCを追加
    addNpcToScene(scenarioId, sceneId, npcId) {
        return this._fetch(`/scenarios/${scenarioId}/scenes/${sceneId}/npcs/${npcId}/add`, {
            method: 'POST',
        });
    },

    // シーンからNPCを削除
    removeNpcFromScene(scenarioId, sceneId, sceneNpcId) {
        return this._fetch(`/scenarios/${scenarioId}/scenes/${sceneId}/scene-npcs/${sceneNpcId}/remove`, {
            method: 'POST', // HTMLのformがPOSTなので合わせる
        });
    },

    // シーンに情報を追加
    addInfoToScene(scenarioId, sceneId, infoId, displayCondition) {
        return this._fetch(`/scenarios/${scenarioId}/scenes/${sceneId}/infos/${infoId}/add`, {
            method: 'POST',
            body: JSON.stringify({ displayCondition }),
        });
    },

    // シーンから情報を削除
    removeInfoFromScene(scenarioId, sceneId, infoId) {
        return this._fetch(`/scenarios/${scenarioId}/scenes/${sceneId}/infos/${infoId}/remove`, {
            method: 'POST',
        });
    },
    
    // --- 新規作成用のAPI ---
    // 情報の新規作成
    createInfo(scenarioId, infoData) {
        return this._fetch(`/api/scenarios/${scenarioId}/infos`, {
            method: 'POST',
            body: JSON.stringify(infoData),
        });
    },

    // 情報の更新
    updateInfo(scenarioId, infoId, infoData) {
        return this._fetch(`/api/scenarios/${scenarioId}/infos/${infoId}`, {
            method: 'PUT',
            body: JSON.stringify(infoData),
        });
    },

    // 情報の削除
    deleteInfo(scenarioId, infoId) {
        return this._fetch(`/api/scenarios/${scenarioId}/infos/${infoId}`, {
            method: 'DELETE',
        });
    },

    // 情報詳細の取得
    fetchInfoDetails(scenarioId, infoId) {
        return this._fetch(`/api/scenarios/${scenarioId}/infos/${infoId}`, {
            method: 'GET'
        });
    },

    // NPCの削除
    deleteNpc(scenarioId, npcId) {
        return this._fetch(`/scenarios/${scenarioId}/npcs/${npcId}`, {
            method: 'DELETE',
        });
    },

    // シーンの削除
    deleteScene(scenarioId, sceneId) {
        return this._fetch(`/scenarios/${scenarioId}/scenes/${sceneId}`, {
            method: 'DELETE',
        });
    },

    // NPC詳細データの取得
    fetchNpcDetails(scenarioId, npcId) {
        return this._fetch(`/scenarios/${scenarioId}/npcs/${npcId}/details`, {
            method: 'GET'
        });
    },

    // NPCの新規作成
    createNpc(scenarioId, npcData) {
        return this._fetch(`/scenarios/${scenarioId}/api/npcs`, {
            method: 'POST',
            body: JSON.stringify(npcData),
        });
    },

    // NPCの更新
    updateNpc(scenarioId, npcId, npcData) {
        return this._fetch(`/scenarios/${scenarioId}/api/npcs/${npcId}`, {
            method: 'PUT',
            body: JSON.stringify(npcData),
        });
    },

    // シーン背景画像のアップロード
    uploadSceneImage(scenarioId, sceneId, formData) {
        return this._fetchMultipart(`/scenarios/${scenarioId}/scenes/${sceneId}/uploadImage`, {
            method: 'POST',
            body: formData,
        });
    },
};
