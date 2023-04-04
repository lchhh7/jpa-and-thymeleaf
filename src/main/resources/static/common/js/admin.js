window.onload = function () {
    getDocumentNo(formatDate(new Date()));
	
	document.getElementById('adminTrigger').addEventListener('click', function() {
        document.getElementById('adminModal').classList.add('show-modal');
    });
	
	document.getElementById('adminClose').addEventListener('click', function() {
        document.getElementById('adminModal').classList.remove('show-modal');
    });

    document.getElementById('adminModTrigger').addEventListener('click', function() {
        document.getElementById('adminModModal').classList.add('show-modal');
    });
	
	document.getElementById('adminModClose').addEventListener('click', function() {
        document.getElementById('adminModModal').classList.remove('show-modal');
    });
}