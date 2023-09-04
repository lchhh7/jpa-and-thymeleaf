const writeForm = document.getElementById('write-form');

const writeNotice = function () {
    if (!confirm('공지사항을 등록하시겠습니까?')) return false;

   // oEditors.getById["ir1"].exec("UPDATE_CONTENTS_FIELD", []);

    const data = confirmNotice(writeForm);
    if (data === false) return false;
	
	
    $.ajax({
        url: contextPath + '/notice/write.do',
        method: 'post',
        data: JSON.stringify(data),
        contentType: 'application/json; charset=utf-8'
    })
        .done(function (data) {
            alert('공지사항을 정상적으로 등록했습니다.');
            location.href = contextPath + "/notice/view.do?id=" + data;
        })
        .fail(function (data) {
            alert(data.responseText);
            return false;
        });
};

document.getElementById('write-btn').addEventListener('click', writeNotice, true);

$("#postYN").change(function() {
	let currentDate = new Date();
    let postStrDt = document.getElementsByName('postStrDt')[0];
    let postEndDt = document.getElementsByName('postEndDt')[0];
		if(this.checked) {
			document.getElementsByName('postStrDt')[0].value = currentDate.toISOString().substring(0,10);
			document.getElementsByName('postEndDt')[0].value = new Date(currentDate.setDate(currentDate.getDate() + 3)).toISOString().substring(0,10);
			postStrDt.setAttribute('type','text');
			postEndDt.setAttribute('type','text');
		}else{
			document.getElementsByName('postStrDt')[0].value = "";
			document.getElementsByName('postEndDt')[0].value = "";
			postStrDt.setAttribute('type','hidden');
			postEndDt.setAttribute('type','hidden');
		}
});
