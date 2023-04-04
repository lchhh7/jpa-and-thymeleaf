const editCommute = function () {
	
    $.ajax({
        url: contextPath + '/commuting/editCommute.do',
        method: 'post',
		data: JSON.stringify({
		type : document.getElementById("type").value,
		requestDt : document.getElementById("requestDt").value,
		content :  document.getElementById("content").value,
		approveId :  document.getElementById("approveId").value,
		requestTm : document.getElementById("requestTm").value
		}),
		contentType: 'application/json; charset=utf-8'
    })
        .done(function (data) {
			alert(data);
	 		location.reload();
        })
        .fail(function (data) {
            alert(data.responseText);
        });
};

document.getElementById('edit-btn').addEventListener('click', editCommute, true);

document.getElementById('write-close-btn').addEventListener('click', function () {
    closeModal('write-modal');
}, true);