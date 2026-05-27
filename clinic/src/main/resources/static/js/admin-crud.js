
function toggleCrudForm(formId, buttonId) {
    const form = document.getElementById(formId);
    const btn = document.getElementById(buttonId);
    if (!form || !btn) return;

    if (form.classList.contains('d-none')) {
        form.classList.remove('d-none');
        btn.innerHTML = '<i class="bi bi-dash-circle me-2"></i>Đóng form';
        btn.className = 'btn btn-secondary';
    } else {
        form.reset(); 
        form.querySelector('input[type="hidden"]').value = '';
        form.classList.add('d-none');
        btn.innerHTML = '<i class="bi bi-plus-circle me-2"></i>Thêm mới';
        btn.className = 'btn btn-success';
    }
}

function initFormState(openFlag, formId, buttonId) {
    document.addEventListener('DOMContentLoaded', () => {
        if (openFlag === true) {
            const form = document.getElementById(formId);
            const btn = document.getElementById(buttonId);
            if (form && btn) {
                form.classList.remove('d-none');
                btn.innerHTML = '<i class="bi bi-dash-circle me-2"></i>Đóng form';
                btn.className = 'btn btn-secondary';
            }
        }
    });
}

function deleteCrudItem(id, deleteUrl, rowSelectorId) {
    if (confirm('Bạn có chắc chắn muốn xóa mục này không?')) {
        fetch(deleteUrl + id, { method: 'DELETE' })
            .then(res => {
                if (res.ok) {
                    const targetRow = document.getElementById(rowSelectorId + id);
                    if (targetRow) {
                        targetRow.remove();
                    } else {
                        location.reload();
                    }
                }
            })
            .catch(err => console.error('Error:', err));
    }
}