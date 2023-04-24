document.addEventListener("DOMContentLoaded", function () {
	scheduleBadge();
	commutingBadge();
    getToday();

    /**
     * 햄버거 메뉴 토글
     */
    

    document.querySelector(".menu_btn").addEventListener("click", function () {
        var lnb = document.querySelector(".lnb");
        var container = document.querySelector(".container");

        lnb.classList.toggle('active');
        container.classList.toggle('active');
    });

    /**
     * 메인 메뉴, 프로젝트 탭 클릭 시 이벤트
     */
    var m1 = document.querySelectorAll(".tapmenu li");
    var m2 = document.querySelectorAll(".menulist li");
    var m3 = document.querySelectorAll(".pagination .pnum");
    addEventActive(m1);
    addEventActive(m2);
    addEventActive(m3);
});

function getToday() {
    const week = ['일요일', '월요일', '화요일', '수요일', '목요일', '금요일', '토요일'];

    const today = new Date();
    const year = today.getFullYear();
    const month = ("0" + (1 + today.getMonth())).slice(-2);
    const date = ("0" + today.getDate()).slice(-2);
    const day = week[today.getDay()];


    var text = year + "년 " + month + "월 " + date + "일 " + day;

    document.getElementById("today").innerText = text;
}

function activeTap(menu, el) {
    menu.forEach(function (li) {
        li.classList.remove('active');
    });
    el.classList.add('active');
}

function addEventActive(menu) {
    menu.forEach(function (li) {
        li.addEventListener('click', function (e) {
            activeTap(menu, this);
        });
    });
}

function formatDate(date) {
    let year = date.getFullYear();
    let month = date.getMonth() + 1;
    month = month >= 10 ? month : '0' + month;
    let day = date.getDate();
    day = day >= 10 ? day : '0' + day;

    return year + "-" + month + "-" + day;
}

function formatTime(date) {
    let hour = date.getHours();
    let minute = date.getMinutes();

    hour = hour >= 10 ? hour : '0' + hour;
    minute = minute >= 10 ? minute : '0' + minute;

    return hour + ':' + minute;
}

function formatUnixDate(date) {
    let year = date.getFullYear();
    let month = date.getMonth() + 1;
    let day = date.getDate();
	let hour = date.getHours();
    let minute = date.getMinutes();

	month = month >= 10 ? month : '0' + month;
    day = day >= 10 ? day : '0' + day;
	hour = hour >= 10 ? hour : '0' + hour;
    minute = minute >= 10 ? minute : '0' + minute;

    return year + "-" + month + "-" + day +" "+hour + ':' + minute;
}

function getDayOfWeek(date) {
    const week = ['일', '월', '화', '수', '목', '금', '토'];
    return week[new Date(date).getDay()];
}

const nullStr = function (e) {
    return e === null ? '' : e;
};

function openModal(modalId) {
    document.getElementById(modalId).classList.add('show-modal');
}

function closeModal(modalId) {
    document.getElementById(modalId).classList.remove('show-modal');
}

const confirmRequiredField = function (requiredFields) {
    for (let i = 0; i < requiredFields.length; i++) {
        let el = requiredFields[i];
        if (el.value.trim().length === 0) {
            alert("* 표기가 되어있는 항목은 필수 입력 항목입니다.");
            el.focus();
            return false;
        }
    }
}

const getRadioValue = function (name) {
    let value = "";

    const radio = document.getElementsByName(name);
    radio.forEach(function (el) {
        if (el.checked) value = el.value;
    });

    return value;
}

const isEditForm = function (data, form) {
    if (form.getAttribute('id') === 'edit-form') {
        data.id = form.id.value
    }

    return data;
}

const downloadAttach = function (id) {
    const form = document.getElementById('download-form');
    form.id.value = id;
    form.submit();
}

const _timeFormat = function (e) {
   console.log(e);
};

const scheduleBadge  = function () {
	$.ajax({
        url: contextPath + '/admin/schedule/search.do?r=R',
        type: 'get',
        dataType: "json",
        contentType: "application/json; charset=utf-8"
    }).done(function (data) {
		if(data.list.content.length > 0) {
			m7_content = document.getElementById('m_icon7').innerHTML;
			document.getElementById('m_icon7').innerHTML = m7_content + '&nbsp;<span class="badge">'+data.list.content.length+'건 </span>';
		}
});
}

const commutingBadge  = function () {
	$.ajax({
        url: contextPath + '/admin/commuting/search.do?r=R',
        type: 'get',
        dataType: "json",
        contentType: "application/json; charset=utf-8"
    }).done(function (data) {
		if(data.list.content.length > 0) {
			m8_content = document.getElementById('m_icon8').innerHTML;
			document.getElementById('m_icon8').innerHTML = m8_content + '&nbsp;<span class="badge">'+data.list.content.length+'건 </span>';
		}
});
}

document.addEventListener('DOMContentLoaded', function () {
    const _timeInputs = document.getElementsByClassName('time');
    Array.prototype.forEach.call(_timeInputs, function (e, i) {
        e.addEventListener('keypress', function () {
            _timeFormat(this);
        });
    });
});

const koreanType = function(str) {
	var koreanType = '';
	if(str == 'FV') koreanType = '휴가';
	if(str == 'HV') koreanType = '반차';
	if(str == 'SC') koreanType = '일정';
	if(str == 'OW') koreanType = '외근';
	if(str =='Y') koreanType = '출근수정';
	if(str =='N' || str == 'V') koreanType = '퇴근수정';
	if(str =='O') koreanType = '야근신청';
	return koreanType;
};

const koreanStatus = function(str) {
	var koreanStatus = '';
	if(str == 'R') koreanStatus = '대기';
	if(str == 'Y') koreanStatus = '승인';
	if(str == 'N') koreanStatus = '미승인';
	if(str == 'C') koreanStatus = '취소요청';
	return koreanStatus;
}
