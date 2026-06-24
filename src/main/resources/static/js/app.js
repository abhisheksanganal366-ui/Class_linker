function openModal(id) {
    document.getElementById(id).classList.add('active');
}

function closeModal(id) {
    document.getElementById(id).classList.remove('active');
}

// Close modal on outside click
window.addEventListener('click', function(e) {
    document.querySelectorAll('.modal.active').forEach(function(modal) {
        if (e.target === modal) modal.classList.remove('active');
    });
});

// Close modal on Escape key
document.addEventListener('keydown', function(e) {
    if (e.key === 'Escape') {
        document.querySelectorAll('.modal.active').forEach(function(modal) {
            modal.classList.remove('active');
        });
    }
});

function openEditModal(id, title, content) {
    document.getElementById('editId').value = id;
    document.getElementById('editTitle').value = title;
    document.getElementById('editContent').value = content;
    openModal('editModal');
}

function openEditStudent(id, name, email, phone, roll) {
    document.getElementById('editStudentId').value = id;
    document.getElementById('editStudentName').value = name;
    document.getElementById('editStudentEmail').value = email;
    document.getElementById('editStudentPhone').value = phone;
    document.getElementById('editStudentRoll').value = roll;
    openModal('editModal');
}
