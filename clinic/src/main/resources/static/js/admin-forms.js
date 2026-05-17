//(function (window) {
//    const forms = {};
//
//    function $(id) { return document.getElementById(id); }
//
//    function applyOpenState(formId, opts) {
//        const form = $(formId);
//        const toggleBtn = $(opts.toggleBtnId);
//        if (!form || !toggleBtn) return;
//        if (opts.open) {
//            form.classList.remove('d-none');
//            toggleBtn.innerHTML = '<i class="bi bi-dash-circle me-2"></i>Đóng form';
//            toggleBtn.className = 'btn btn-secondary';
//        }
//    }
//
//    function openForCreate(formId, opts) {
//        const form = $(formId);
//        if (!form) return;
//        // clear fields
//        (opts.fields || []).forEach(f => {
//            const el = $(f);
//            if (el) el.value = '';
//        });
//        // password required
//        if (opts.passwordGroupId) {
//            const pwd = $(opts.passwordGroupId).querySelector('input');
//            if (pwd) { pwd.required = true; }
//            $(opts.passwordGroupId).style.display = 'block';
//        }
//        if ($(opts.toggleBtnId)) {
//            $(opts.toggleBtnId).innerHTML = '<i class="bi bi-dash-circle me-2"></i>Đóng form';
//            $(opts.toggleBtnId).className = 'btn btn-secondary';
//        }
//        form.classList.remove('d-none');
//    }
//
//    function resetAndClose(formId, opts) {
//        const form = $(formId);
//        if (!form) return;
//        form.classList.add('d-none');
//        const tb = $(opts.toggleBtnId);
//        if (tb) { tb.innerHTML = '<i class="bi bi-plus-circle me-2"></i>Thêm'; tb.className = 'btn btn-success'; }
//    }
//
//    function editEntity(formId, id, opts) {
//        const form = $(formId);
//        if (!form) return;
//        const toggleBtn = $(opts.toggleBtnId);
//        if (opts.tableFieldMap) {
//            Object.keys(opts.tableFieldMap).forEach(field => {
//                const prefix = opts.tableFieldMap[field];
//                const cell = document.getElementById(prefix + id);
//                if (cell) {
//                    const input = $(field);
//                    if (input) input.value = cell.innerText.trim();
//                }
//            });
//        }
//
//        if (opts.passwordGroupId) {
//            const pwd = $(opts.passwordGroupId).querySelector('input');
//            if (pwd) pwd.required = false;
//            $(opts.passwordGroupId).style.display = 'none';
//        }
//
//        if ($(opts.idField)) $(opts.idField).value = id;
//
//        if ($(opts.submitBtnId)) $(opts.submitBtnId).innerHTML = '<i class="bi bi-check-circle-fill me-2"></i>Lưu thay đổi';
//
//        form.classList.remove('d-none');
//        if (toggleBtn) { toggleBtn.innerHTML = '<i class="bi bi-dash-circle me-2"></i>Đóng form'; toggleBtn.className = 'btn btn-secondary'; }
//        window.scrollTo({ top: 0, behavior: 'smooth' });
//    }
//
//    function initAdminForm(formId, opts) {
//        opts = Object.assign({ toggleBtnId: 'toggleUserFormBtn', submitBtnId: 'submitFormBtn', idField: 'userId' }, opts || {});
//        forms[formId] = opts;
//
//        document.addEventListener('DOMContentLoaded', function () {
//            applyOpenState(formId, opts);
//        });
//
//        window['openForCreate_' + formId] = function () { openForCreate(formId, opts); };
//        window['resetAndClose_' + formId] = function () { resetAndClose(formId, opts); };
//        window['editEntity_' + formId] = function (id) { editEntity(formId, id, opts); };
//        window['initAdminForm'] = initAdminForm;
//    }
//
//    window.AdminForms = { initAdminForm: initAdminForm };
//})(window);
