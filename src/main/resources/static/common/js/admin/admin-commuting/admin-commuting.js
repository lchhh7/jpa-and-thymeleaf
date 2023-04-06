const modifyForm = document.getElementById('modify-form');

const approve = function (status) {
	if (!confirm ('요청을 처리하시겠습니까?')) return false;

	$.ajax({
		url: contextPath + '/admin/commuting/' + modifyForm.id.value + '.do',
		method: 'put',
		data: JSON.stringify({id: modifyForm.id.value, status: status ,requestDt : modifyForm.requestDt.value , memberId : modifyForm.memberId.value}),
		contentType: "application/json; charset=utf-8"
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

const displayViewModal = function (d) {
	const div = document.getElementById('modify-btn-box');
	while (div.firstChild) {
		div.removeChild(div.firstChild);
	}

	let closeBtn = document.createElement('a');
	closeBtn.setAttribute('role', 'button');
	closeBtn.classList.add('btn');
	closeBtn.classList.add('jjblue');
	closeBtn.innerText = '닫기'
	closeBtn.addEventListener('click', function () {
		closeModal('modify-modal');
	});

	if (d.status === 'R' || d.status === 'C') {
		let yBtn = document.createElement('a');
		yBtn.setAttribute('role', 'button');
		yBtn.classList.add('btn');
		yBtn.classList.add('jjgreen');
		yBtn.classList.add('mr5');
		yBtn.innerText = '승인';
		let nBtn = document.createElement('a');
		nBtn.setAttribute('role', 'button');
		nBtn.classList.add('btn');
		nBtn.classList.add('jjred');
		nBtn.classList.add('mr5');
		nBtn.innerText = '비승인';
		if (d.status === 'C') {
			yBtn.addEventListener('click', function () {
				approve('D');
			}, true);
			nBtn.addEventListener('click', function () {
				approve('Y');
			}, true);
		} else {
			yBtn.addEventListener('click', function () {
				approve('Y');
			}, true);
			nBtn.addEventListener('click', function () {
				approve('N');
			}, true);
		}

		div.appendChild(yBtn);
		div.appendChild(nBtn);
	}

	if ((d.status === 'Y' || d.status === 'N') && d.type === 'O') {
		let initBtn = document.createElement('a');
		initBtn.setAttribute('role', 'button');
		initBtn.classList.add('btn');
		initBtn.classList.add('jjgreen');
		initBtn.classList.add('mr5');
		initBtn.innerText = '승인초기화';

		initBtn.addEventListener('click', function () {
			approve('R');
		}, true);

		div.appendChild(initBtn);
	}

	div.appendChild(closeBtn);
}

const commuting = function (id) {
	$.ajax({
		url: contextPath + '/admin/commuting/' + id + '.do',
		method: 'get',
		dataType: 'json',
		contentType: 'application/json; charset=utf-8'
	})
		.done(function (data) {
			if (data.type === 'A') {
				data.content = '추가휴가 ' + data.content + '개'
			}
			console.log(data);
			modifyForm.id.value = data.id;
			modifyForm.memberId.value = data.member.id;
			modifyForm.memberName.value = data.member.name;
			modifyForm.requestDt.value = data.requestDt;
			modifyForm.requestTm.value = data.requestTm;
			modifyForm.content.value = data.content;
			modifyForm.statusName.value = koreanStatus(data.status);
			if (data.status !== 'R' && data.status !== 'C' ) {
				modifyForm.approveDt.value = formatDate(new Date(data.approveDt));
			}

			displayViewModal(data);
			openModal('modify-modal');
		})
		.fail(function (data) {
			alert('일정 정보 조회 중 오류가 발생했습니다.');
			return false;
		});

}

const commutings = function (p) {
	const box1 = document.getElementById('search-status1');
	const box2 = document.getElementById('search-status2');
	const box3 = document.getElementById('search-status3');

	let url = "";
	url += "&m=" + document.getElementById('search-member-id').value;
	url += "&r=" + (box1.checked ? box1.value : "");
	url += "&y=" + (box2.checked ? box2.value : "");
	url += "&n=" + (box3.checked ? box3.value : "");

	$.ajax({
		url: contextPath + '/admin/commuting/search.do?page=' + (p-1) + url,
		type: 'get',
		dataType: "json",
		contentType: "application/json; charset=utf-8"
	})
		.done(function (data) {
			const list = data.list.content;
			let tr = "";
			if (list.length > 0) {
				list.forEach(function (el) {
					if (el.type === 'A' && el.content.length < 3) {
						el.content = '추가휴가 ' + el.content + '개'
						el.requestDt = el.requestDt.substr(0, 7);
					} else if (el.type === 'O') {
						el.requestTm = ''
					}

					tr += '<tr class="tbbody" onclick="commuting(' + el.id + ')">';
					tr += '<td>' + el.member.name + '</td>';
					tr += '<td>' + koreanType(el.type) + '</td>';
					tr += '<td>' + el.requestDt.replaceAll('-', '.') + ' ' + nullStr(el.requestTm) + '</td>';
					tr += '<td>' + el.content + '</td>';
					tr += '<td id="status">' + koreanStatus(el.status) + '</td>';
					tr += '</tr>';
				});
			} else {
				tr += '<tr class="tbbody"><td colspan="5">등록된 신청내역이 없습니다.</td></tr>';
			}

			document.getElementById('totalCommuting').innerText = '총 게시물' + data.list.totalElements + '건';
			document.getElementById('commutings').innerHTML = tr;
			document.getElementById('page').innerHTML = data.page;
		})
		.fail(function () {
			alert('일정신청 내역을 조회하는 중 오류가 발생했습니다.');
			return false;
		});


};

document.getElementById('search-member-id').addEventListener('change', function () {
	commutings(1);
}, true);
document.getElementById('search-status1').addEventListener('click', function () {
	commutings(1);
}, true);
document.getElementById('search-status2').addEventListener('click', function () {
	commutings(1);
}, true);
document.getElementById('search-status3').addEventListener('click', function () {
	commutings(1);
}, true);
document.addEventListener('DOMContentLoaded', function () {
	commutings(1);
}, true);