document.addEventListener('DOMContentLoaded', function() {
    // グローバルオブジェクトをここで定義し、HTMLから渡されたデータをマージする
    window.trpgWriter = {
        data: window.trpgWriterData || {},
        isFormDirty: false,
        sceneEditForm: null,
        csrfToken: null,
        csrfHeader: null,
        fetchWithCsrf: null
    };

    // CSRFトークンをmetaタグから取得
    const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');
    const csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');
    window.trpgWriter.csrfToken = csrfToken;
    window.trpgWriter.csrfHeader = csrfHeader;

    // CSRFトークンをヘッダーに自動で付与するfetchのラッパー関数をグローバルに公開
    window.trpgWriter.fetchWithCsrf = async function(url, options = {}) {
        const headers = {
            ...options.headers,
            [window.trpgWriter.csrfHeader]: window.trpgWriter.csrfToken
        };
        const body = options.body ? options.body : undefined;
        const method = options.method ? options.method.toUpperCase() : 'GET';

        const finalOptions = { ...options, method, headers };
        if (method !== 'GET' && method !== 'HEAD') {
            finalOptions.body = body;
        }

        return fetch(url, finalOptions);
    };

    const body = document.body;
    const tinymceApiKey = body.getAttribute('data-tinymce-api-key');

    tinymce.init({
        selector: '#sceneContent',
        api_key: tinymceApiKey,
        plugins: 'advlist autolink lists link image charmap preview anchor',
        toolbar_mode: 'floating',
        toolbar: 'undo redo | formatselect | bold italic backcolor | alignleft aligncenter alignright alignjustify | bullist numlist outdent indent | removeformat | help',
        content_css: '/css/style.css',
        setup: function (editor) {
            editor.on('dirty', function () {
                window.trpgWriter.isFormDirty = true;
            });
        }
    });

    // sceneEditFormをグローバルに公開
    window.trpgWriter.sceneEditForm = document.getElementById('sceneEditForm');
    if (window.trpgWriter.sceneEditForm) {
        window.trpgWriter.sceneEditForm.querySelectorAll('input, textarea').forEach(input => {
            input.addEventListener('input', () => window.trpgWriter.isFormDirty = true);
        });
        window.trpgWriter.sceneEditForm.addEventListener('submit', () => window.trpgWriter.isFormDirty = false);
    }

    window.addEventListener('beforeunload', function (e) {
        if (window.trpgWriter.isFormDirty) {
            e.preventDefault();
            e.returnValue = '';
        }
    });

    // 未保存時のナビゲーション警告モーダル処理
    const unsavedChangesModalElement = document.getElementById('unsavedChangesModal');
    if (unsavedChangesModalElement) {
        const unsavedChangesModal = new bootstrap.Modal(unsavedChangesModalElement);
        let targetUrl = null; // 移動先のURLを保持する変数

        document.querySelectorAll('a').forEach(link => {
            link.addEventListener('click', function (e) {
                // 外部リンクや特殊なリンクは対象外
                if (link.target === '_blank' || link.href.startsWith('javascript:') || link.href.includes('#')) {
                    return;
                }
                if (window.trpgWriter.isFormDirty) {
                    e.preventDefault();
                    targetUrl = link.href;
                    unsavedChangesModal.show();
                }
            });
        });

        document.getElementById('saveAndNavigateBtn').addEventListener('click', async () => {
            const form = window.trpgWriter.sceneEditForm;
            if (form) {
                // TinyMCEのコンテンツをtextareaに反映
                tinymce.get('sceneContent').save();
                
                const formData = new FormData(form);
                
                try {
                    const response = await window.trpgWriter.fetchWithCsrf(form.action, {
                        method: 'POST',
                        body: formData
                    });

                    if (response.ok) {
                        window.trpgWriter.isFormDirty = false; // 保存成功したのでフラグを下ろす
                        if (targetUrl) {
                            window.location.href = targetUrl;
                        }
                    } else {
                        alert('保存に失敗しました。');
                        unsavedChangesModal.hide();
                    }
                } catch (error) {
                    console.error('Error:', error);
                    alert('エラーが発生しました。');
                    unsavedChangesModal.hide();
                }
            }
        });

        document.getElementById('discardAndNavigateBtn').addEventListener('click', () => {
            window.trpgWriter.isFormDirty = false; // 変更を破棄するのでフラグを下ろす
            if (targetUrl) {
                window.location.href = targetUrl;
            }
        });
    }
});
