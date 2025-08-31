// scenario-editor.js

// These instances are now expected to be created in the global scope or imported as modules.
// For this project's structure, we'll assume they are globally available.
// const apiClient = new ApiClient(); // This is defined in api-client.js
// const uiUpdater = new UiUpdater(); // This is defined in ui-updater.js

document.addEventListener('DOMContentLoaded', () => {
    const formId = 'scenario-form';
    const form = document.getElementById(formId);
    if (!form) return;

    // Initialize the UI Updater with the API client
    uiUpdater.init(apiClient);

    const saveButton = form.querySelector('button[type="submit"]');

    const saveScenario = async () => {
        const formData = new FormData(form);
        const data = Object.fromEntries(formData.entries());

        const csrfToken = document.querySelector('meta[name="_csrf"]').content;
        const csrfHeader = document.querySelector('meta[name="_csrf_header"]').content;

        try {
            // We need to use a method on the global apiClient object.
            // Let's assume a method `saveScenarioData` exists for this purpose.
            // Since it doesn't, we'll add it to the apiClient object or use fetch directly.
            // For consistency, let's use fetch as we did before, but without re-declaring apiClient.
            const response = await fetch(form.action, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    [csrfHeader]: csrfToken
                },
                body: JSON.stringify(data)
            });

            if (!response.ok) {
                const errorData = await response.json();
                throw new Error(JSON.stringify(errorData));
            }
            
            if (window.unsavedChangesProtector) {
                window.unsavedChangesProtector.resetState();
            }

            uiUpdater.showToast('シナリオ情報を更新しました。');
            return true;

        } catch (error) {
            console.error('Failed to save scenario:', error);
            uiUpdater.showToast('保存に失敗しました。', false);
            return false;
        }
    };

    if (saveButton) {
        saveButton.type = 'button';
        saveButton.addEventListener('click', saveScenario);
    }

    window.unsavedChangesProtector = new UnsavedChangesProtector(formId, {
        saveFunction: saveScenario
    });
});
