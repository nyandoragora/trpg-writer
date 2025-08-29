// form-unsaved-changes-protector.js

class UnsavedChangesProtector {
    /**
     * @param {string} formId - The ID of the form to protect.
     * @param {object} [options] - Optional settings.
     * @param {function} [options.isDirtyCheck] - A custom function to check if the form is dirty. Should return true or false.
     * @param {function} [options.saveFunction] - An async function to call when "Save and Navigate" is clicked. It should return true on success, false on failure.
     * @param {string} [options.modalId='unsavedChangesModal'] - The ID of the confirmation modal.
     */
    constructor(formId, options = {}) {
        this.form = document.getElementById(formId);
        if (!this.form) {
            // Silently fail if the form doesn't exist on the page.
            return;
        }

        this.isDirtyCheck = options.isDirtyCheck || this.isFormDirty.bind(this);
        this.saveFunction = options.saveFunction;
        this.modalId = options.modalId || 'unsavedChangesModal';
        this.modalEl = document.getElementById(this.modalId);
        
        if (!this.modalEl) {
            // Silently fail if the modal doesn't exist.
            return;
        }

        this.modal = new bootstrap.Modal(this.modalEl);
        this.initialState = this.getFormState();
        this.isSubmitting = false;
        this.navigationUrl = null;

        this.setupEventListeners();
    }

    getFormState() {
        const elements = this.form.elements;
        let state = {};
        for (let i = 0; i < elements.length; i++) {
            const el = elements[i];
            if (el.name && el.type !== 'hidden') { // Exclude hidden inputs from state
                state[el.name] = el.value;
            }
        }
        return JSON.stringify(state);
    }

    isFormDirty() {
        return this.getFormState() !== this.initialState;
    }

    // Public method to allow external components to reset the state (e.g., after a successful save)
    resetState() {
        this.initialState = this.getFormState();
        this.isSubmitting = false;
    }

    setupEventListeners() {
        // For browser-level navigation (reload, back, close tab)
        window.addEventListener('beforeunload', (event) => {
            if (this.isDirtyCheck() && !this.isSubmitting) {
                event.preventDefault();
                event.returnValue = '';
            }
        });

        // For in-page navigation (clicking links)
        document.body.addEventListener('click', (event) => {
            const link = event.target.closest('a');
            // Check if the link is valid, not a button, and the form is dirty
            if (link && link.href && this.isDirtyCheck() && !this.isSubmitting) {
                // Ignore links that open in a new tab, are javascript calls, part of a modal, or pagination links
                if (link.target === '_blank' || link.href.startsWith('javascript:') || link.closest('.modal') || link.closest('.pagination')) {
                    return;
                }
                event.preventDefault();
                this.navigationUrl = link.href;
                this.modal.show();
            }
        });

        // For form submission (e.g., the main save button)
        this.form.addEventListener('submit', () => {
            this.isSubmitting = true;
        });

        // Modal button listeners
        const discardBtn = this.modalEl.querySelector('[data-action="discard"]');
        const saveAndNavigateBtn = this.modalEl.querySelector('[data-action="save-and-navigate"]');

        if (discardBtn) {
            discardBtn.addEventListener('click', () => {
                this.isSubmitting = true; // Allow navigation without saving
                window.location.href = this.navigationUrl;
            });
        }

        if (saveAndNavigateBtn) {
            if (this.saveFunction) {
                saveAndNavigateBtn.addEventListener('click', async () => {
                    const success = await this.saveFunction();
                    if (success) {
                        this.isSubmitting = true; // Allow navigation
                        window.location.href = this.navigationUrl;
                    } else {
                        // If save fails, just hide the modal and let the user decide what to do next.
                        this.modal.hide();
                    }
                });
            } else {
                // If no save function is provided, hide the button.
                saveAndNavigateBtn.style.display = 'none';
            }
        }
    }
}