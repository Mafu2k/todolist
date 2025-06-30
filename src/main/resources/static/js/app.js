document.addEventListener('DOMContentLoaded', function() {
    const tableRows = document.querySelectorAll('tbody tr');
    tableRows.forEach((row, index) => {
        row.style.opacity = '0';
        row.style.transform = 'translateY(20px)';
        
        setTimeout(() => {
            row.style.transition = 'all 0.5s ease';
            row.style.opacity = '1';
            row.style.transform = 'translateY(0)';
        }, index * 100);
    });
    
    const deleteButtons = document.querySelectorAll('form[action*="/delete"] button');
    deleteButtons.forEach(button => {
        button.addEventListener('click', function(e) {
            if (!confirm('Czy na pewno chcesz usunąć to zadanie?')) {
                e.preventDefault();
            }
        });
    });
    
    const alerts = document.querySelectorAll('.alert');
    alerts.forEach(alert => {
        setTimeout(() => {
            if (alert.classList.contains('show')) {
                alert.classList.remove('show');
                setTimeout(() => alert.remove(), 150);
            }
        }, 5000);
    });
});