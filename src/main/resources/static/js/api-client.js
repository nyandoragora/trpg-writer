// api-client.js

const apiClient = {
    // CSRFトークンを取得するヘルパーメソッド
    _getCsrfToken() {
        return document.querySelector('meta[name="_csrf"]').getAttribute('content');
    },
    _getCsrfHeader() {
        return document.querySelector('meta[name="_csrf_header"]').getAttribute('content');
    },

    // API呼び出しの共通処理
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
            // エラーレスポンスがJSON形式の場合、その内容をエラーメッセージとして使用する
            try {
                const errorData = await response.json();
                const errorMessage = errorData.message || JSON.stringify(errorData);
                throw new Error(`API Error: ${errorMessage}`);
            } catch (e) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
        }
        // 成功レスポンスが空の場合もあるので、JSON解析を試みて失敗したらnullを返す
        return response.text().then(text => text ? JSON.parse(text) : null);
    },

    // シーンデータ取得
    fetchSceneData(scenarioId, sceneId) {
        return this._fetch(`/scenarios/${scenarioId}/scenes/${sceneId}/edit-data`, { method: 'GET' });
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
    removeInfoFromScene(scenarioId, sceneId, sceneInfoId) {
        return this._fetch(`/scenarios/${scenarioId}/scenes/${sceneId}/scene-infos/${sceneInfoId}/remove`, {
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

    // NPCの削除
    deleteNpc(scenarioId, npcId) {
        return this._fetch(`/scenarios/${scenarioId}/npcs/${npcId}`, {
            method: 'DELETE',
        });
    },
};
