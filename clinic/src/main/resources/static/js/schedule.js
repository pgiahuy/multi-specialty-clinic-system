function deleteSchedule(id) {
	if (!confirm("Bạn có chắc chắn muốn xóa lịch làm việc này?")) return;

	fetch(`/admin/schedules/${id}`, {
		method: 'DELETE',
		headers: {
			'Content-Type': 'application/json'
		}
	})
		.then(response => {
			if (response.ok) {
				// reload current list
				fetchSchedules();
			} else {
				alert("Xóa thất bại");
			}
		})
		.catch(error => {
			console.error('Error:', error);
			alert("Có lỗi xảy ra");
		});
}

function debounce(fn, delay) {
	let t;
	return function (...args) {
		clearTimeout(t);
		t = setTimeout(() => fn.apply(this, args), delay);
	};
}

function attachPaginationHandlers() {
	const pag = document.getElementById('schedulesPagination');
	if (!pag) return;
	const links = pag.querySelectorAll('a');
	links.forEach(a => {
		a.addEventListener('click', function (ev) {
			ev.preventDefault();
			const href = a.getAttribute('href');
			if (!href) return;
			const u = new URL(href, window.location.origin);
			const page = u.searchParams.get('page') || '1';
			fetchSchedules(page);
		});
	});
}

function fetchSchedules(page) {
	const form = document.getElementById('scheduleFilterForm');
	const params = new URLSearchParams(new FormData(form || new FormData()));
	if (page) params.set('page', page);
	const url = '/admin/schedules' + (params.toString() ? ('?' + params.toString()) : '');

	fetch(url, { headers: { 'X-Requested-With': 'XMLHttpRequest' } })
		.then(r => r.text())
		.then(html => {
			try {
				const parser = new DOMParser();
				const doc = parser.parseFromString(html, 'text/html');

				const newTbody = doc.getElementById('schedulesTbody');
				if (newTbody) {
					const localTbody = document.getElementById('schedulesTbody');
					if (localTbody) localTbody.innerHTML = newTbody.innerHTML;
				}

				const newPag = doc.getElementById('schedulesPagination');
				if (newPag) {
					const localPag = document.getElementById('schedulesPagination');
					if (localPag) localPag.innerHTML = newPag.innerHTML;
					attachPaginationHandlers();
				}

				// update browser URL without reloading
				history.replaceState(null, '', url);
			} catch (e) {
				console.error('Failed to parse schedules response', e);
			}
		})
		.catch(err => console.error('Failed to load schedules', err));
}

document.addEventListener('DOMContentLoaded', function () {
	const form = document.getElementById('scheduleFilterForm');
	if (!form) return;

	const inputs = form.querySelectorAll('input');
	const debouncedFetch = debounce(() => fetchSchedules(1), 300);

	inputs.forEach(inp => {
		inp.addEventListener('input', debouncedFetch);
	});

	form.addEventListener('submit', function (e) {
		e.preventDefault();
		fetchSchedules(1);
	});

	// wire initial pagination links
	attachPaginationHandlers();
});

