function deleteSchedule(id) {
    if (confirm("Bạn có chắc chắn muốn xóa lịch làm việc này?")) {
        fetch(`/admin/schedules/${id}`, {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json'
            }
        })
        .then(response => {
            if (response.ok) {
                location.reload();
            } else {
                alert("Xóa thất bại");
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert("Có lỗi xảy ra");
        });
    }
}