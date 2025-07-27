document.addEventListener('DOMContentLoaded', function() {
    // CSRFトークンをmetaタグから取得
    const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');
    const csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');

    // CSRFトークンをヘッダーに自動で付与するfetchのラッパー関数
    async function fetchWithCsrf(url, options = {}) {
        const headers = {
            ...options.headers,
            [csrfHeader]: csrfToken
        };
        // fetch APIはGETリクエストではbodyを許可しないため、メソッドに応じてbodyを設定
        const body = options.body ? options.body : undefined;
        const method = options.method ? options.method.toUpperCase() : 'GET';

        const finalOptions = { ...options, method, headers };
        if (method !== 'GET' && method !== 'HEAD') {
            finalOptions.body = body;
        }

        return fetch(url, finalOptions);
    }

    const body = document.body;
    const tinymceApiKey = body.getAttribute('data-tinymce-api-key');
    const updateUrlTemplate = body.getAttribute('data-update-url-template');
    const storeUrl = body.getAttribute('data-store-url');

    let isFormDirty = false;

    tinymce.init({
        selector: '#sceneContent',
        api_key: tinymceApiKey,
        plugins: 'advlist autolink lists link image charmap print preview anchor',
        toolbar_mode: 'floating',
        toolbar: 'undo redo | formatselect | bold italic backcolor | alignleft aligncenter alignright alignjustify | bullist numlist outdent indent | removeformat | help',
        content_css: '/css/style.css',
        setup: function (editor) {
            editor.on('dirty', function () {
                isFormDirty = true;
            });
        }
    });

    const sceneEditForm = document.getElementById('sceneEditForm');
    if (sceneEditForm) {
        sceneEditForm.querySelectorAll('input, textarea').forEach(input => {
            input.addEventListener('input', () => isFormDirty = true);
        });
        sceneEditForm.addEventListener('submit', () => isFormDirty = false);
    }

    window.addEventListener('beforeunload', function (e) {
        if (isFormDirty) {
            e.preventDefault();
            e.returnValue = '';
        }
    });

    // --- Info Modal (Add/Edit) --- 
    const infoModal = document.getElementById('infoModal');
    if (infoModal) {
        infoModal.addEventListener('show.bs.modal', function (event) {
            const button = event.relatedTarget;
            const infoId = button.getAttribute('data-info-id');
            const infoForm = infoModal.querySelector('#infoForm');

            // (中略：モーダルの表示内容を設定するロジックは変更なし)
            const modalTitle = infoModal.querySelector('.modal-title');
            const infoNameInput = infoModal.querySelector('#infoName');
            const infoContentTextarea = infoModal.querySelector('#infoContent');
            if (infoId) {
                modalTitle.textContent = '情報編集';
                infoForm.action = updateUrlTemplate.replace('INFO_ID_PLACEHOLDER', infoId);
                infoModal.querySelector('#infoId').value = infoId;
                infoNameInput.value = button.getAttribute('data-info-name');
                infoContentTextarea.value = button.getAttribute('data-info-content');
            } else {
                modalTitle.textContent = '情報追加';
                infoForm.action = storeUrl;
                infoForm.reset();
            }

            // 本文の変更状態に応じてボタンの表示を切り替え
            document.getElementById('unsaved-warning').style.display = isFormDirty ? 'block' : 'none';
            document.getElementById('save-info-btn').style.display = isFormDirty ? 'none' : 'inline-block';
            document.getElementById('save-all-btn').style.display = isFormDirty ? 'inline-block' : 'none';
            document.getElementById('save-info-only-btn').style.display = isFormDirty ? 'inline-block' : 'none';
        });

        // 「本文と情報を保存」ボタン
        document.getElementById('save-all-btn').addEventListener('click', async function() {
            tinymce.get('sceneContent').save();
            const formData = new FormData(sceneEditForm);
            try {
                const response = await fetchWithCsrf(sceneEditForm.action, { method: 'POST', body: formData });
                if (response.ok) {
                    isFormDirty = false;
                    document.getElementById('infoForm').submit();
                } else {
                    alert('本文の保存に失敗しました。');
                }
            } catch (error) {
                alert('エラーが発生しました。');
            }
        });

        // 「情報のみ保存」ボタン（本文の変更は破棄）
        document.getElementById('save-info-only-btn').addEventListener('click', function() {
            isFormDirty = false; // 警告フラグを解除
            document.getElementById('infoForm').submit();
        });
    }

    // --- Delete Info Modal ---
    const deleteInfoConfirmModal = document.getElementById('deleteInfoConfirmModal');
    if (deleteInfoConfirmModal) {
        let deleteUrl = '';
        deleteInfoConfirmModal.addEventListener('show.bs.modal', function(event) {
            deleteUrl = event.relatedTarget.getAttribute('data-delete-url');
            // 本文の変更状態に応じてボタンの表示を切り替え
            const warning = document.getElementById('delete-unsaved-warning');
            const confirmDeleteBtn = document.getElementById('confirm-delete-btn');
            const saveAndDeleteBtn = document.getElementById('save-and-delete-btn');
            const discardAndDeleteBtn = document.getElementById('discard-and-delete-btn');

            if (isFormDirty) {
                warning.style.display = 'block';
                confirmDeleteBtn.style.display = 'none';
                saveAndDeleteBtn.style.display = 'inline-block';
                discardAndDeleteBtn.style.display = 'inline-block';
            } else {
                warning.style.display = 'none';
                confirmDeleteBtn.style.setProperty('display', 'inline-block', 'important');
                confirmDeleteBtn.style.setProperty('visibility', 'visible', 'important');
                confirmDeleteBtn.style.setProperty('opacity', '1', 'important');
                saveAndDeleteBtn.style.display = 'none';
                discardAndDeleteBtn.style.display = 'none';
            }
        });

        // 削除処理をfetchで実行する関数
        async function performDelete() {
            try {
                const response = await fetchWithCsrf(deleteUrl, { method: 'POST' });
                if (response.ok) {
                    window.location.reload(); // 成功したらページをリロードして反映
                } else {
                    alert(`削除に失敗しました (HTTP ${response.status})`);
                }
            } catch (error) {
                alert('エラーが発生しました。');
            }
        }

        // 「削除」ボタン（通常時）
        document.getElementById('confirm-delete-btn').addEventListener('click', performDelete);

        // 「本文を破棄して削除」ボタン
        document.getElementById('discard-and-delete-btn').addEventListener('click', () => {
            isFormDirty = false;
            performDelete();
        });

        // 「本文を保存して削除」ボタン
        document.getElementById('save-and-delete-btn').addEventListener('click', async function() {
            tinymce.get('sceneContent').save();
            const formData = new FormData(sceneEditForm);
            try {
                const response = await fetchWithCsrf(sceneEditForm.action, { method: 'POST', body: formData });
                if (response.ok) {
                    isFormDirty = false;
                    performDelete();
                } else {
                    alert(`本文の保存に失敗しました (HTTP ${response.status})`);
                }
            } catch (error) {
                alert('エラーが発生しました。');
            }
        });
    }
    
    // (シナリオ情報モーダルのロジックは変更なし)
    const scenarioInfoModal = document.getElementById('scenarioInfoModal');
    if(scenarioInfoModal) {
        scenarioInfoModal.addEventListener('show.bs.modal', function (event) {
            var button = event.relatedTarget;
            var infoName = button.getAttribute('data-info-name');
            var infoContent = button.getAttribute('data-info-content');
            var sceneEditUrl = button.getAttribute('data-scene-edit-url');

            var modalTitle = scenarioInfoModal.querySelector('.modal-title');
            var modalContent = scenarioInfoModal.querySelector('#scenarioInfoModalContent');
            var modalEditButton = scenarioInfoModal.querySelector('#scenarioInfoModalEditButton');

            modalTitle.textContent = infoName;
            modalContent.textContent = infoContent;
            modalEditButton.href = sceneEditUrl;
        });
    }

    // NPCモーダルのバリデーションエラー表示
    const npcFormHasErrors = body.getAttribute('data-npc-form-has-errors');
    if (npcFormHasErrors === 'true') {
        const npcModal = new bootstrap.Modal(document.getElementById('npcModal'), { keyboard: false });
        npcModal.show();
    }
});