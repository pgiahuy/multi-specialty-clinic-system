function deleteRooms(id) {
    if (confirm('Xác nhận xoá?')) {
        fetch('/clinic/admin/rooms/' + id, {method: 'DELETE'})
                .then(res => {
                    if (res.ok) {
                        const rows = document.querySelectorAll("tr");
                        rows.forEach(row => {
                            const firstTd = row.querySelector("td:first-child");
                            if (firstTd && firstTd.textContent.trim() == id) {
                                row.remove();
                            }
                        });
                    } else {
                        alert('Xóa thất bại');
                    }
                })
                .catch(err => console.error(err));
    }
}
