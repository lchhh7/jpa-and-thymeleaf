const writeCommute = function () {
		const data ={
			type : writeForm.type.value,
			requestDt : writeForm.requestDt.value,
			content :  writeForm.content.value,
			approveId :  writeForm.approveId.value,
			requestTm : writeForm.requestTm.value
		};
		
	    $.ajax({
	        url: contextPath + '/commuting/writeCommute.do',
	        method: 'post',
			data: JSON.stringify(data),
			contentType: 'application/json; charset=utf-8'
	    })
	        .done(function (data) {
				alert(data);
		 		window.location.reload();
	        })
	        .fail(function (data) {
	            alert(data.responseText);
	        });
};

document.getElementById('write-btn').addEventListener('click', writeCommute, true);

document.getElementById('write-close-btn').addEventListener('click', function () {
    closeModal('write-modal');
}, true);


