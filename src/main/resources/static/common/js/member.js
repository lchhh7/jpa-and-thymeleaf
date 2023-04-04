const editForm = document.getElementById('member-edit');

const editMember = function () {
	if (!confirm('정보를 수정하시겠습니까?')) return false;

	let requiredFields = [
		editForm.name, editForm.position, editForm.mobileNo, editForm.color
	];

	for (let i = 0; i < requiredFields.length; i++) {
		let el = requiredFields[i];
		if (el.value.trim().length === 0) {
			alert("* 표기가 되어있는 항목은 필수 입력 항목입니다.");
			el.focus();
			return false;
		}
	}

	$.ajax({
		url: contextPath + '/member/edit.do',
		method: 'put',
		data: JSON.stringify({
			name: editForm.name.value,
			position: editForm.position.value,
			department: editForm.department.value,
			phoneNo: editForm.phoneNo.value,
			mobileNo: editForm.mobileNo.value,
			useColor: editForm.color.value
		}),
		contentType: 'application/json; charset:UTF-8'
	})
		.done(function (data) {
			alert(data);
			location.reload();
		})
		.fail(function (data) {
			alert(data.responseText);
			return false;
		});
};

document.getElementById('m-edit-btn').addEventListener('click', editMember, true);
