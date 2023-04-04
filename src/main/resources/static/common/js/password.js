const passwordForm = document.getElementById('password-edit');

const confirmPassword = function () {
	const inputs = passwordForm.getElementsByTagName('input');
	Array.prototype.forEach.call(inputs, function (input) {
		if (input.value.trim().length === 0) {
			alert('모든 항목을 입력해주세요.');
			input.focus();
			return false;
		}
	});

	return {
		password: passwordForm.password.value,
		newPassword: passwordForm.newPassword.value,
		newPassword2: passwordForm.newPassword2.value
	};
}

const editPassword = function () {
	if (!confirm('비밀번호를 변경하시겠습니까?')) return false;

	const data = confirmPassword();
	if (data === false) return false;

	$.ajax({
		url: contextPath + '/member/p/edit.do',
		method: 'put',
		data: JSON.stringify(data),
		contentType: 'application/json; charset:UTF-8'
	})
		.done(function (data) {
			alert(data);
			location.href =contextPath +"/main.do";
		})
		.fail(function (data) {
			alert(data.responseText);
			return false;
		});

};

document.getElementById('p-edit-btn').addEventListener('click', editPassword, true);

const validPassword = function(_this){
	var elValidMessage = document.querySelector('.valid-message');
	var regex = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[@$!%*#?&])[A-Za-z\d@$!%*#?&]{6,}$/;
	if( !regex.test(_this.value)) {
		elValidMessage.classList.remove('hide');
	}else {
		elValidMessage.classList.add('hide');
	}
};

const checkPassword = function(_this){
	var pw = document.querySelector('#newPassword').value;
	var elMisMatchMessage = document.querySelector('.mismatch-message');
	
	if(pw === _this.value) {
		elMisMatchMessage.classList.add('hide');
	}else {
		elMisMatchMessage.classList.remove('hide');
	}
};
