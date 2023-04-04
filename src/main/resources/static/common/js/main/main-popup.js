/*var noticeForm = document.getElementById('post-popup-form');
//var noticePopup = null;
function getPopup() {
	$.ajax({
		url: contextPath + 'main/noticePopupInfo.do',
		method: 'get',
		dataType: 'json',
		contentType: 'application/json; charset=utf-8'
	})
		.done(function(data) {
			if(data.length == 0) return false;
			for (var i = 0; i < data.length; i++) {
				noticeForm.memberName.value = data[i].memberName;
				noticeForm.crtDt.value = data[i].crtDt.substr(0, 10);
				noticeForm.title.value = data[i].title;
				noticeForm.content.value = data[i].content.replaceAll(/(<([^>]+)>)/gi, "");
				var onOff = getCookie('post-popup-modal');
				if (onOff != 'false') {
					//openModal('post-popup-modal');
					window.open(contextPath+'popupContent.do','noticePopup','width = 500, height = 600');
					document.getElementById('memberName').value = data[i].memberName;
					console.log(document.getElementById('memberName').value);
					noticeForm.crtDt.value = data[i].crtDt.substr(0, 10);
					noticeForm.title.value = data[i].title;
					noticeForm.content.value = data[i].content.replaceAll(/(<([^>]+)>)/gi, "");
				}
			}
		})
		.fail(function() {
			alert('일정 정보 조회 중 오류가 발생했습니다.');
			return false;
		});
};


document.addEventListener("DOMContentLoaded", function() {
	getPopup();
}, true);


document.getElementById('notice-close-btn').addEventListener('click', function() {
	var check = document.getElementById('popupCookie');
	if (check.checked) {
		setCookie('post-popup-modal', "false", 1);
		self.close();
	}
	closeModal('post-popup-modal');
}, true);
*/
//쿠키설정    
function setCookie(key, value, expiredays) {
	var todayDate = new Date();
	todayDate.setDate(todayDate.getDate() + expiredays);
	document.cookie = key + '=' + escape(value) + '; path=/; expires=' + todayDate.toGMTString() + ';'
}

//쿠키 불러오기
function getCookie(key) {
	var result = null;
	var cookie = document.cookie.split(';');
	cookie.some(function(item) {
		item = item.replace(' ', '');
		var dic = item.split('=');
		if (key === dic[0]) {
			result = dic[1];
			return true;
		}
	});
	return result;
}
/*
 $(document).on('ready', function () {
       $(".popup").draggable();
    });
*/
