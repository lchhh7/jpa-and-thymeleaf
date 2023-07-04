const viewForm = document.getElementById('view-form');

const approve = function (status) {
    if (!confirm ('요청을 처리하시겠습니까?')) return false;

    $.ajax({
        url: contextPath + '/admin/schedule/' + viewForm.id.value + '.do',
        method: 'put',
        data: JSON.stringify({id: viewForm.id.value, status: status}),
        contentType: "application/json; charset=utf-8"
    })
        .done(function (data) {
            location.reload();
        })
        .fail(function (data) {
            alert(data.responseText);
            return false;
        });
};

const displayViewModal = function (d) {
    const div = document.getElementById('approve-btn-box');
    while (div.firstChild) {
        div.removeChild(div.firstChild);
    }

    let closeBtn = document.createElement('a');
    closeBtn.setAttribute('role', 'button');
    closeBtn.classList.add('btn');
    closeBtn.classList.add('jjblue');
    closeBtn.innerText = '닫기'
    closeBtn.addEventListener('click', function () {
        closeModal('detail-info-modal');
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

    div.appendChild(closeBtn);
}

const schedule = function (id) {
    $.ajax({
        url: contextPath + '/admin/schedule/' + id + '.do',
        method: 'get'
    })
        .done(function (data) {
			console.log(data);
            viewForm.id.value = data.id;
            viewForm.memberName.value = data.member.name;
            viewForm.typeName.value = koreanType(data.type);
            viewForm.startDt.value = data.strDt.substr(0,10);
            viewForm.startTm.value = data.strDt.split(" ")[1].substr(0,5);
            viewForm.endDt.value = data.endDt.substr(0,10);
            viewForm.endTm.value = data.endDt.split(" ")[1].substr(0,5);
            viewForm.title.value = data.title;
            viewForm.content.value = data.content;
            viewForm.statusName.value = koreanStatus(data.status);
            if (data.status !== 'R' && data.status !== 'C' )
                viewForm.approveDt.value = formatDate(new Date(data.approveDt));

            displayViewModal(data);
            openModal('detail-info-modal');
        })
        .fail(function () {
            alert('일정신청 상세정보를 조회하는 중 오류가 발생했습니다.');
            return false;
        });
}

const schedules = function (p) {
    const box1 = document.getElementById('search-status1');
    const box2 = document.getElementById('search-status2');
    const box3 = document.getElementById('search-status3');

    let url = "";
    url += "&m=" + document.getElementById('search-member-id').value;
    url += "&r=" + (box1.checked ? box1.value : "");
    url += "&y=" + (box2.checked ? box2.value : "");
    url += "&n=" + (box3.checked ? box3.value : "");

    $.ajax({
        url: contextPath + '/admin/schedule/search.do?page=' + (p-1) + url,
        type: 'get',
        dataType: "json",
        contentType: "application/json; charset=utf-8"
    })
        .done(function (data) {
            const list = data.list.content;
            let tr = "";
			
            if (list.length > 0) {
                list.forEach(function (el) {
                    tr += '<tr class="tbbody" onclick="schedule(' + el.id + ')">';
                    tr += '<td>' + el.member.name + '</td>';
                    tr += '<td>' + koreanType(el.type) + '</td>';
                    tr += '<td>' + el.title + '</td>';
                    if (el.strDt === el.endDt) {
                        tr += '<td>' + el.strDt.replaceAll('-', '').split(" ")[0] + '</td>';
                    } else {
                        tr += '<td>' + el.strDt.replaceAll('-', '').split(" ")[0] + '~' + el.endDt.replaceAll('-', '').split(" ")[0] + '</td>';
                    }

                    tr += '<td id="status">' + koreanStatus(el.status) + '</td>';
                    tr += '</tr>';
                });
            } else {
                tr += '<tr class="tbbody"><td colspan="6">등록된 일정신청내역이 없습니다.</td></tr>';
            }
            document.getElementById('totalSchedule').innerText = '총 게시물' + data.list.totalElements + '건';
            document.getElementById('schedules').innerHTML = tr;
            document.getElementById('page').innerHTML = data.page ;
            
        })
        .fail(function () {
            alert('일정신청 내역을 조회하는 중 오류가 발생했습니다.');
            return false;
        });
}

document.getElementById('search-member-id').addEventListener('change', function () {
    schedules(1);
}, true);
document.getElementById('search-status1').addEventListener('click', function () {
    schedules(1);
}, true);
document.getElementById('search-status2').addEventListener('click', function () {
    schedules(1);
}, true);
document.getElementById('search-status3').addEventListener('click', function () {
    schedules(1);
}, true);
document.addEventListener('DOMContentLoaded', function () {
    schedules(1);
}, true);

const vacationDays = function () {
    $.ajax({
        url: contextPath + '/admin/schedule/vacationDays.do',
        type: 'get'
    })
        .done(function (data) {
            let tr = "";

            Array.prototype.forEach.call(data, function (e) {
                tr += '<tr>';
                tr += '<td>' + e.memberName  + '</td>';
                tr += '<td>' + (e.total) + '(' + e.total +  '-' + e.use +')'  + '</td>';
                tr += '<td>' + (e.total-e.use)  + '</td>';
                tr += '</tr>';
            });

            document.getElementById('vacation-days').innerHTML = tr;
            openModal('vacation-days-modal');
        })
        .fail(function () {
            alert('전체 휴가 일수를 조회하는 중 오류가 발생했습니다.');
            return false;
        });
};

const holidayAPI = function () {
    $.ajax({
        url: contextPath + '/admin/holidayInfoAPI.do',
        method: 'post',
        contentType: "application/json; charset=utf-8"
    })
        .done(function (data) {
            alert(data);
        })
        .fail(function (data) {
            alert(data);
            return false;
        });
};

document.getElementById('vacation-days-btn').addEventListener('click', vacationDays, true)

document.getElementById('holiday-api-btn').addEventListener('click', holidayAPI, true)

document.getElementById('vacation-days-close-btn').addEventListener('click', function () {
    closeModal('vacation-days-modal');
}, true);
