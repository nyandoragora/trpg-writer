document.addEventListener('DOMContentLoaded', () => {
    const formId = 'scenario-form';
    const form = document.getElementById(formId);
    if (!form) return;

    const saveButton = form.querySelector('button[type="submit"]');
    if (saveButton) {
        saveButton.type = 'button';
    }

    const saveScenario = async () => {
        const formData = new FormData(form);
        const data = Object.fromEntries(formData.entries());

        // CSRFトークンを取得
        const csrfToken = document.querySelector('meta[name="_csrf"]').content;
        const csrfHeader = document.querySelector('meta[name="_csrf_header"]').content;

        try {
            const response = await fetch(form.action, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    [csrfHeader]: csrfToken
                },
                body: JSON.stringify(data)
            });

            if (!response.ok) {
                throw new Error('Server responded with an error.');
            }
            
            if (window.unsavedChangesProtector) {
                window.unsavedChangesProtector.resetState();
            }

            console.log('Scenario saved successfully!');
            // ここで成功メッセージを表示する処理を追加できます
            // 例: uiUpdater.showToast('シナリオ情報を更新しました。');
            return true;

        } catch (error) {
            console.error('Failed to save scenario:', error);
            // ここでエラーメッセージを表示する処理を追加できます
            // 例: alert('保存に失敗しました。');
            return false;
        }
    };

    if (saveButton) {
        saveButton.addEventListener('click', saveScenario);
    }

    window.unsavedChangesProtector = new UnsavedChangesProtector(formId, {
        saveFunction: saveScenario
    });
});
