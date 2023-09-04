$('#search-year').val(new Date().getYear()+1900).prop("selected",true);

const searchYear = document.getElementById('search-year');
const editForm = document.getElementById('edit-form');

/*const mainVacation = function () {
    $.ajax({
        url: contextPath + 'main/schedule.do',
        method: 'get'
    })
        .done(function (data) {
            const schedule = data.schedule;
            let info, status;
            if (schedule === undefined) {
                info = '신청한 휴가가 없습니다.'
                status = '';
            } else {
                // info = schedule.crtDt + '에 신청한 ' + schedule.typeName;
                info = schedule.startDt.replaceAll('-', '.') + ' ' + schedule.typeName;
                status = schedule.statusName;
            }
            document.getElementById('vacation-info').innerText = info;
            document.getElementById('vacation-status').innerText = status;
            document.getElementById('vacation-cnt').innerText = data.cnt + '건';
        })
        .fail(function () {
            alert('신청 휴가 조회 중 오류가 발생했습니다.');
            return false;
        });
};*/

const searching = function () {
	let url = '';
	url += ('&y=' + (searchYear !=null ? searchYear.value : ''));
	 $.ajax({
        url: contextPath + '/main/searching.do?' + url,
        method: 'get'
    })
        .done(function (data) {
            let tr = ``;
            if (data.length > 0) {
                data.forEach(function (e) {

                    tr += `
                    <tr class="tbbody">
                    <td>${koreanType(e.type)}</td>
                    <td>${e.strDt.replaceAll('-','').split("T")[0] + '~' + e.endDt.replaceAll('-','').split("T")[0]}</td>
                    <td>${e.approve.name}</td>
                    <td>${(e.approveDt == null ? '' : e.approveDt)}</td>
                    <td><span>${koreanStatus(e.status)}</span></td>
                    </tr>
                    `;
                });

            } else {
                tr += `<tr><td colspan="5">신청한 휴가가 없습니다.</td></tr>`
            }

            document.getElementById('vacation-tbody').innerHTML = tr;
        })
        .fail(function () {
            alert('신청 휴가 조회 중 오류가 발생했습니다.');
            return false;
        });
	openModal('vacation-modal')
}


document.getElementById('vacation-btn').addEventListener('click', searching, true);
document.getElementById('vacation-close-btn').addEventListener('click', function () {
    closeModal('vacation-modal');
});