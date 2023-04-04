let calendar;
const writeForm = document.getElementById('write-form');
const editForm = document.getElementById('edit-form');
const searchTypeSC = document.getElementById('search-typeSC');
const searchTypeFV = document.getElementById('search-typeFV');
const searchTypeHV = document.getElementById('search-typeHV');
const searchTypeOW = document.getElementById('search-typeOW');
const oneday = 24 * 60 * 60 * 1000;

const schedule = function(info) {
	$.ajax({
		url: contextPath + '/schedule/' + info.event.id + '.do',
		method: 'get',
		dataType: 'json',
		contentType: 'application/json; charset=utf-8'
	})
		.done(function(data) {
			editForm.id.value = data.id;
			editForm.type.value = data.type;
			editForm.memberName.value = data.member.name;
			editForm.typeName.value = koreanType(data.type);
			editForm.startDt.value = data.strDt.substr(0,10);
			editForm.startTm.value = data.strDt.split("T")[1].substr(0,5);
			editForm.endDt.value = data.endDt.substr(0,10);
			editForm.endTm.value = data.endDt.split("T")[1].substr(0,5);
			editForm.title.value = data.title;
			editForm.content.value = data.content;
			editForm.approveName.value = data.approve == null ? null : data.approve.name;
			editForm.statusName.value = koreanStatus(data.status);
			editForm.color.jscolor.fromString(data.color);
			editForm.cancelReason.value = data.cancelReason;
			//레이어모달에 동석자 정보 가져가놓기
			
			displayEditModal(data);
			openModal('edit-modal');
			
		})
		.fail(function(data) {
			alert('일정 정보 조회 중 오류가 발생했습니다.');
			return false;
		});
};

const schedules = function(info, successCallback) {
	const searchMemberId = getRadioValue('calendar-type');
	
	let url = 'm=' + searchMemberId;
	url += ('&sc=' + (searchTypeSC.checked ? searchTypeSC.value : ''));
	url += ('&fv=' + (searchTypeFV.checked ? searchTypeFV.value : ''));
	url += ('&hv=' + (searchTypeHV.checked ? searchTypeHV.value : ''));
	url += ('&ow=' + (searchTypeOW.checked ? searchTypeOW.value : ''));
	url += ('&sd=' + info.startStr.substr(0, 10));
	url += ('&ed=' + info.endStr.substr(0, 10));

	$.ajax({
		url: contextPath + '/schedule/search.do?' + url,
		method: 'get',
		dataType: 'json',
		contentType: 'application/json; charset=utf-8'
	})
		.done(function(data) {
			let events = [];
			const list = data.list;
			Array.prototype.forEach.call(list, function(e) {
				
				if ((e.type === 'FV' || e.type === 'HV') && e.status === 'R') {
					e.title += '(승인대기)';
					events.push({
						id: e.id, title: e.title, start: e.strDt,
						end: e.endDt , color: e.color
					});
				} else {
					events.push({
						id: e.id, title: e.title, start: e.strDt ,
						end: e.endDt , color: e.color
					});
				}
			});

			const holidays = data.holidays;
			Array.prototype.forEach.call(holidays, function(e) {

				let dt = formatDate(new Date(e.holidayDt));

				events.push({
					start: dt, end: formatDate(new Date(new Date(dt).getTime() + oneday)),
					rendering: 'background', backgroundColor: 'red',
					overlap: true
				});

				events.push({
					start: dt, title: e.title, color: 'red'
				});
			});

			successCallback(events);
		})
		.fail(function(data) {
			alert('일정 정보 조회 중 오류가 발생했습니다.');
			return false;
		});
}

document.addEventListener('DOMContentLoaded', function() {
	let calendarEl = document.getElementById('calendar');
	calendar = new FullCalendar.Calendar(calendarEl, {
		plugins: ['interaction', 'dayGrid', 'resourceTimeline'],
		eventTimeFormat: {
			hour: '2-digit',
			minute: '2-digit',
			hour12: false
		},
		businessHours: true,
		locale: 'ko',
		header: {
			left: 'title',
			center: '',
			right: 'prev,next'
		},
		events: function(info, successCallback) {
			schedules(info, successCallback);
		},
		eventClick: function(info) {
			console.log(info);
			schedule(info);
		},
		selectable: true,
		select: function(info) {
			writeModal(info);
		}
	});

	calendar.render();

	searchTypeSC.addEventListener('click', function() {
		calendar.refetchEvents();
	}, true);
	searchTypeFV.addEventListener('click', function() {
		calendar.refetchEvents();
	}, true);
	searchTypeHV.addEventListener('click', function() {
		calendar.refetchEvents();
	}, true);
	searchTypeOW.addEventListener('click', function() {
		calendar.refetchEvents();
	}, true);

	const radios = document.getElementsByName('calendar-type');
	radios.forEach(function(e) {
		e.addEventListener('change', function() {
			calendar.refetchEvents();
		});
	});
});


const confirmSchedule = function(form) {
	const requiredFields = [
		form.type, form.startDt, form.endDt,
		form.startTm, form.endTm,
		form.title, form.content, form.color
	];

	if (confirmRequiredField(requiredFields) === false) return false;

	let data = {
		type: form.type.value,
		strDt: form.startDt.value,
		endDt: form.endDt.value,
		startTm: form.startTm.value,
		endTm: form.endTm.value,
		title: form.title.value,
		content: form.content.value,
		color: form.color.value,
	};

	if (form.getAttribute('id') === 'write-form') {
		if (form.type.value === 'VA') {
			if (form.vacationType.value === '') {
				alert('휴가 종류를 선택해주세요.');
				return false;
			}

			if (form.approveId.value === '') {
				alert('결재자를 선택해주세요.');
				return false;
			}
			data.vacationType = form.vacationType.value;
			data.approveId = form.approveId.value;
		}
	}
	return isEditForm(data, form);
};

const colenWrite = function(_this) {
	_this.value = _this.value.replace(/[^0-9:]/g, "");
	if (_this.value.length == 2) {
		_this.value = _this.value + ":";
	}
};