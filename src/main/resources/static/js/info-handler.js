document.addEventListener('DOMContentLoaded', function() {
    // グローバルオブジェクトがなければ何もしない
    if (!window.trpgWriter) {
        return;
    }

    const body = document.body;
    const updateUrlTemplate = body.getAttribute('data-update-url-template');
    const storeUrl = body.getAttribute('data-store-url');

    // --- Info Modal (Add/Edit) --- 
    const infoModal = document.getElementById('infoModal');
    if (infoModal) {
        infoModal.addEventListener('show.bs.modal', function (event) {
            const button = event.relatedTarget;
            const infoId = button.getAttribute('data-info-id');
            const infoForm = infoModal.querySelector('#infoForm');

            const modalTitle = infoModal.querySelector('.modal-title');
            const infoNameInput = infoModal.querySelector('#infoName');
            const infoContentTextarea = infoModal.querySelector('#infoContent');
            
            if (infoId) {
                // 編集モード
                modalTitle.textContent = '情報編集';
                infoForm.action = updateUrlTemplate.replace('INFO_ID_PLACEHOLDER', infoId);
                infoModal.querySelector('#infoId').value = infoId;
                
                infoNameInput.value = button.getAttribute('data-info-name');
                infoContentTextarea.value = button.getAttribute('data-info-content');
            } else {
                // 追加モード
                modalTitle.textContent = '情報追加';
                infoForm.action = storeUrl;
                infoForm.reset(); 
                infoModal.querySelector('#infoId').value = '';
            }

            const isDirty = window.trpgWriter.isFormDirty;
            document.getElementById('unsaved-warning').style.display = isDirty ? 'block' : 'none';
            document.getElementById('save-info-btn').style.display = isDirty ? 'none' : 'inline-block';
            document.getElementById('save-all-btn').style.display = isDirty ? 'inline-block' : 'none';
            document.getElementById('save-info-only-btn').style.display = isDirty ? 'inline-block' : 'none';
        });

        document.getElementById('save-all-btn').addEventListener('click', async function() {
            tinymce.get('sceneContent').save();
            const formData = new FormData(window.trpgWriter.sceneEditForm);
            try {
                const response = await window.trpgWriter.fetchWithCsrf(window.trpgWriter.sceneEditForm.action, { method: 'POST', body: formData });
                if (response.ok) {
                    window.trpgWriter.isFormDirty = false;
                    document.getElementById('infoForm').submit();
                } else {
                    alert('本文の保存に失敗しました。');
                }
            } catch (error) {
                alert('エラーが発生しました。');
            }
        });

        document.getElementById('save-info-only-btn').addEventListener('click', function() {
            window.trpgWriter.isFormDirty = false;
            document.getElementById('infoForm').submit();
        });
    }

    // --- Delete Info Modal ---
    const deleteInfoConfirmModal = document.getElementById('deleteInfoConfirmModal');
    if (deleteInfoConfirmModal) {
        let deleteUrl = '';
        deleteInfoConfirmModal.addEventListener('show.bs.modal', function(event) {
            deleteUrl = event.relatedTarget.getAttribute('data-delete-url');
            const warning = document.getElementById('delete-unsaved-warning');
            const confirmDeleteBtn = document.getElementById('confirm-delete-btn');
            const saveAndDeleteBtn = document.getElementById('save-and-delete-btn');
            const discardAndDeleteBtn = document.getElementById('discard-and-delete-btn');

            const isDirty = window.trpgWriter.isFormDirty;
            if (isDirty) {
                warning.style.display = 'block';
                confirmDeleteBtn.style.display = 'none';
                saveAndDeleteBtn.style.display = 'inline-block';
                discardAndDeleteBtn.style.display = 'inline-block';
            } else {
                warning.style.display = 'none';
                confirmDeleteBtn.style.setProperty('display', 'inline-block', 'important');
                saveAndDeleteBtn.style.display = 'none';
                discardAndDeleteBtn.style.display = 'none';
            }
        });

        async function performDelete() {
            try {
                const response = await window.trpgWriter.fetchWithCsrf(deleteUrl, { method: 'POST' });
                if (response.ok) {
                    window.location.reload();
                } else {
                    alert(`削除に失敗しました (HTTP ${response.status})`);
                }
            } catch (error) {
                alert('エラーが発生しました。');
            }
        }

        document.getElementById('confirm-delete-btn').addEventListener('click', performDelete);

        document.getElementById('discard-and-delete-btn').addEventListener('click', () => {
            window.trpgWriter.isFormDirty = false;
            performDelete();
        });

        document.getElementById('save-and-delete-btn').addEventListener('click', async function() {
            tinymce.get('sceneContent').save();
            const formData = new FormData(window.trpgWriter.sceneEditForm);
            try {
                const response = await window.trpgWriter.fetchWithCsrf(window.trpgWriter.sceneEditForm.action, { method: 'POST', body: formData });
                if (response.ok) {
                    window.trpgWriter.isFormDirty = false;
                    performDelete();
                } else {
                    alert(`本文の保存に失敗しました (HTTP ${response.status})`);
                }
            } catch (error) {
                alert('エラーが発生しました。');
            }
        });
    }
    
    // --- Scenario Info Modal ---
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
});

