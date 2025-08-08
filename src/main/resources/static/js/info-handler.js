document.addEventListener('DOMContentLoaded', function() {
    if (!window.trpgWriter) {
        return;
    }

    const body = document.body;
    const storeUrl = body.getAttribute('data-info-store-url');
    const updateUrlTemplate = body.getAttribute('data-info-update-url-template');
    const deleteUrlTemplate = body.getAttribute('data-info-delete-url-template');

    const infoModalEl = document.getElementById('infoModal');
    const infoModal = infoModalEl ? new bootstrap.Modal(infoModalEl) : null;
    
    const deleteInfoConfirmModalEl = document.getElementById('deleteInfoConfirmModal');
    const deleteInfoConfirmModal = deleteInfoConfirmModalEl ? new bootstrap.Modal(deleteInfoConfirmModalEl) : null;

    // --- Info Modal (Add/Edit) ---
    if (infoModal) {
        const infoForm = infoModalEl.querySelector('#infoForm');
        const modalTitle = infoModalEl.querySelector('.modal-title');
        const infoIdInput = infoModalEl.querySelector('#infoId');
        const infoNameInput = infoModalEl.querySelector('#infoName');
        const infoContentTextarea = infoModalEl.querySelector('#infoContent');
        const saveBtn = infoModalEl.querySelector('#save-info-btn');
        let currentInfoId = null;
        
        const openInfoModal = (button) => {
            currentInfoId = button.getAttribute('data-info-id');
            
            infoForm.reset();
            infoIdInput.value = '';

            if (currentInfoId) {
                modalTitle.textContent = '情報編集';
                infoIdInput.value = currentInfoId;
                infoNameInput.value = button.getAttribute('data-info-name');
                infoContentTextarea.value = button.getAttribute('data-info-content');
            } else {
                modalTitle.textContent = '情報追加';
            }
            infoModal.show();
        };

        document.querySelectorAll('[data-bs-target="#infoModal"]').forEach(button => {
            button.addEventListener('click', (event) => {
                event.preventDefault(); 
                if (window.trpgWriter.isFormDirty) {
                    window.trpgWriter.showUnsavedChangesModal(() => openInfoModal(button));
                } else {
                    openInfoModal(button);
                }
            });
        });

        saveBtn.addEventListener('click', async () => {
            const formData = new FormData(infoForm);
            const data = Object.fromEntries(formData.entries());
            
            const isUpdate = !!data.id;
            const url = isUpdate ? updateUrlTemplate.replace('INFO_ID_PLACEHOLDER', data.id) : storeUrl;
            const method = isUpdate ? 'PUT' : 'POST';

            try {
                const response = await window.trpgWriter.fetchWithCsrf(url, {
                    method: method,
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify(data)
                });

                if (response.ok) {
                    window.location.reload();
                } else {
                    const errorData = await response.json();
                    alert('保存に失敗しました: ' + JSON.stringify(errorData));
                }
            } catch (error) {
                console.error('Error saving info:', error);
                alert('エラーが発生しました。');
            }
        });
    }

    // --- Delete Info Modal ---
    if (deleteInfoConfirmModal) {
        const confirmDeleteBtn = deleteInfoConfirmModalEl.querySelector('#confirm-delete-btn');
        let infoIdToDelete = null;

        const openDeleteModal = (button) => {
            infoIdToDelete = button.getAttribute('data-info-id');
            deleteInfoConfirmModal.show();
        };
        
        document.querySelectorAll('[data-bs-target="#deleteInfoConfirmModal"]').forEach(button => {
            button.addEventListener('click', (event) => {
                event.preventDefault();
                if (window.trpgWriter.isFormDirty) {
                    window.trpgWriter.showUnsavedChangesModal(() => openDeleteModal(button));
                } else {
                    openDeleteModal(button);
                }
            });
        });

        confirmDeleteBtn.addEventListener('click', async () => {
            if (!infoIdToDelete) return;
            const url = deleteUrlTemplate.replace('INFO_ID_PLACEHOLDER', infoIdToDelete);
            try {
                const response = await window.trpgWriter.fetchWithCsrf(url, { method: 'DELETE' });
                if (response.ok) {
                    window.location.reload();
                } else {
                    alert(`削除に失敗しました (HTTP ${response.status})`);
                }
            } catch (error) {
                console.error('Error deleting info:', error);
                alert('エラーが発生しました。');
            }
        });
    }
    
    // --- Scenario Info Modal (No changes needed) ---
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