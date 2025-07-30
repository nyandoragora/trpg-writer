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
});
