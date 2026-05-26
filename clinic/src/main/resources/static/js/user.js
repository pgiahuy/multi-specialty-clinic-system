//function toggleUserForm() {
//    const form = document.getElementById('userForm');
//    if (!form) {
//        return;
//    }
//
//    if (form.classList.contains('d-none')) {
//        openFormForCreate();
//    } else {
//        resetAndCloseForm();
//    }
//}
//
//function applyOpenFormState() {
//    const form = document.getElementById('userForm');
//    const toggleBtn = document.getElementById('toggleUserFormBtn');
//
//    if (!form || !toggleBtn) {
//        return;
//    }
//
//    if (window.__OPEN_USER_FORM__ === true) {
//        form.classList.remove('d-none');
//        toggleBtn.innerHTML = '<i class="bi bi-dash-circle me-2"></i>Đóng form';
//        toggleBtn.className = 'btn btn-secondary';
//    }
//}
//
//function openFormForCreate() {
//    const form = document.getElementById('userForm');
//    const toggleBtn = document.getElementById('toggleUserFormBtn');
//
//
//    document.getElementById('userId').value = '';
//    document.getElementById('username').value = '';
//    document.getElementById('email').value = '';
//    document.getElementById('password').value = '';
//    document.getElementById('password').required = true;
//    document.getElementById('passwordGroup').style.display = 'block';
//
//
//    document.getElementById('formTitle').innerText = 'Tạo tài khoản mới';
//    document.getElementById('submitFormBtn').innerHTML = '<i class="bi bi-person-plus-fill me-2"></i>Tạo tài khoản';
//
//    form.classList.remove('d-none');
//    toggleBtn.innerHTML = '<i class="bi bi-dash-circle me-2"></i>Đóng form';
//    toggleBtn.className = 'btn btn-secondary';
//}
//
//
//function editUser(id) {
//    const form = document.getElementById('userForm');
//    const toggleBtn = document.getElementById('toggleUserFormBtn');
//
//
//    const usernameVal = document.getElementById('name-' + id).innerText;
//    const emailVal = document.getElementById('email-' + id).innerText;
//
//
//    document.getElementById('userId').value = id;
//    document.getElementById('username').value = usernameVal;
//    document.getElementById('email').value = emailVal;
//
//
//    document.getElementById('password').required = false;
//    document.getElementById('passwordGroup').style.display = 'none';
//
//
//    document.getElementById('formTitle').innerText = 'Cập nhật tài khoản (ID: ' + id + ')';
//    document.getElementById('submitFormBtn').innerHTML = '<i class="bi bi-check-circle-fill me-2"></i>Lưu thay đổi';
//
//
//    form.classList.remove('d-none');
//    toggleBtn.innerHTML = '<i class="bi bi-dash-circle me-2"></i>Đóng form';
//    toggleBtn.className = 'btn btn-secondary';
//
//    window.scrollTo({ top: 0, behavior: 'smooth' });
//}
//
//function resetAndCloseForm() {
//    const form = document.getElementById('userForm');
//    const toggleBtn = document.getElementById('toggleUserFormBtn');
//
//    form.classList.add('d-none');
//    toggleBtn.innerHTML = '<i class="bi bi-plus-circle me-2"></i>Thêm tài khoản';
//    toggleBtn.className = 'btn btn-success';
//}
//
//document.addEventListener('DOMContentLoaded', applyOpenFormState);
//
//function deleteUser(id) {
//    if (confirm('Xác nhận xoá?')) {
//        fetch('/clinic/admin/users/' + id, { method: 'DELETE' })
//            .then(res => {
//                console.log('Response status:', res.status);
//                console.log('Response ok:', res.ok);
//                console.log('Response data:', res);
//                if (res.ok) {
//                    const rows = document.querySelectorAll("tr");
//                    rows.forEach(row => {
//                        const firstTd = row.querySelector("td:first-child");
//                        if (firstTd && firstTd.textContent.trim() == id) {
//                            row.remove();
//                        }
//                    });
//                } else {
//                    alert('Xóa thất bại rồi!');
//                }
//            })
//            .catch(err => console.error(err));
//    }
//}
