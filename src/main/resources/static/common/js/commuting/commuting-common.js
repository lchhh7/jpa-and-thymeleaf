let calendar;
const writeForm = document.getElementById('write-form');
const editForm = document.getElementById('edit-form');
const oneday = 24 * 60 * 60 * 1000;

const commuting = function (info) {
    const id = info.event.id;
    if (id === "") return false;
    $.ajax({
        url: contextPath + '/commuting/' + id + '.do',
        method: 'get',
        dataType: 'json',
        contentType: 'application/json; charset=utf-8'
    })
        .done(function (data) {
            const dt = new Date(data.commutingTm);
            const hour = dt.getHours();
            let min = dt.getMinutes();
            min = min < 10 ? '0' + min : min;

            editForm.id.value = data.id;
            editForm.attendYn.value = data.attendYn;
			editForm.requestDt.value = formatDate(info.event.start);
			editForm.type.value = data.attendYn === 'Y' ? '출근시간' :
                                        data.attendYn === 'N'  ? '퇴근시간' : 
                                         data.attendYn === 'V' ? '퇴근시간' : '';
                                        
            editForm.requestTm.value = hour + ':' + min;
			editForm.approveId.value = '';
			
            openModal('edit-modal');
        })
        .fail(function (data) {
            alert('일정 정보 조회 중 오류가 발생했습니다.');
            return false;
        });
};

const selectCommuting = function (info) {
	requestTm.classList.remove('none');
	
	const inputs = writeForm.getElementsByTagName('input');
	
	Array.prototype.forEach.call(inputs, function(e) {
		e.value = '';
	});
	
	const selects = writeForm.getElementsByTagName('select');
	Array.prototype.forEach.call(selects, function(e) {
		e.value = '';
	});
	
	writeForm.requestDt.value = info.startStr;
    writeForm.requestTm.removeAttribute('readonly');
	
	$('#requestTm').siblings()[0].classList.remove('none');
	openModal('write-modal');
};

const setCommutings = function (list) {
    let result = [];
    if (list === undefined) return result;

    Array.prototype.forEach.call(list, function (e) {
        let title;
        let attend = e.attendYn;
        if (attend === 'Y') {
            title = '출근: ' + e.commutingTm.split("T")[1].substr(0,5);
        } else if (attend === 'N') {
            title = '퇴근: ' + e.commutingTm.split("T")[1].substr(0,5);
        } else if (attend === 'V') {
            title = '퇴근: ' + e.commutingTm.split("T")[1].substr(0,5)+ ' (철야)';
        }  else if (attend === 'O') {
            title = '잔업';
        } else {
            alert('올바르지 않은 데이터가 있습니다. 관리자에게 문의하세요.');
            return false;
        }
		new Date(new Date(e.commutingTm.substr(0,10)).setDate(new Date(e.commutingTm.substr(0,10)).getDate() -1))
        result.push({
            id: e.id, title: title, 
            start: attend != 'V' ? e.commutingTm.substr(0,10)+'T00:01' : 
			new Date(new Date(e.commutingTm.substr(0,10)).setDate(new Date(e.commutingTm.substr(0,10)).getDate() -1))
            , color: 'blue'
        });
    });
    return result;
}

const setOvertimes = function (list) {
    let result = [];
    let totalHours = 0;
    let totalMinutes = 0;

    if (list === undefined || list === null) return result;

    Array.prototype.forEach.call(list, function (e) {
            result.push({
                start: e.requestDt,
                end: e.requestDt + "T00:00:01",
                title: '잔업 ' + e.hours + ":" + e.minutes,
                color: '#2DA400'
            });

            totalHours += e.hours;
            totalMinutes += e.minutes;
    });

    totalHours += parseInt(totalMinutes / 60);
   totalMinutes = parseInt(totalMinutes % 60);

    document.getElementById('total-ot').innerHTML = "<p class='todaytotal'>" + totalHours + "시간 " + totalMinutes + "분 </p>";
    return result;
}

const setSchedules = function (list) {
    let result = [];
    if (list === undefined) return result;

    Array.prototype.forEach.call(list, function (e) {
		result.push({
	    	start: e.strDt.split(" ")[0] , end: formatDate(new Date(new Date(e.endDt).getTime() +oneday)),
	    	rendering: 'background', backgroundColor: (e.type =='FV' || e.type =='HV') ? 'red' : 'green',
	    	overlap: true
	    });
		result.push({
			start: e.strDt, end: e.endDt,
			title: e.title, color: (e.type =='FV' || e.type =='HV') ? 'red' : 'green',
		});
    });

    return result;
}

const setCommuteRequests = function (list) {
    let result = [];
    if (list === undefined) return result;

    Array.prototype.forEach.call(list, function (e) {
        let color = e.status === 'Y' ? '#2DA400' : '#452E86';
        result.push({
            start: e.requestDt, end: e.requestDt,
            rendering: 'background', backgroundColor: color,
            overlap: true
        });
    });
    return result;
}

const setHolidays = function (list) {
    let result = [];
    if (list === undefined) return result;

    Array.prototype.forEach.call(list, function (e) {
        let dt = formatDate(new Date(e.holidayDt));

        result.push({
            start: dt, end: formatDate(new Date(new Date(dt).getTime() + oneday)),
            rendering: 'background', backgroundColor: '#FF657E',
            overlap: true
        });

        result.push({
            start: dt, end: dt+"T00:00:01", title: e.title, color: '#FF657E'
        });
    });
    return result;
}

const commutings = function (info, successCallback) {
    $.ajax({
        url: contextPath + '/commuting/search.do?' + ('&sd=' + info.startStr.substr(0, 10)) + ('') + ('&ed=' + info.endStr.substr(0, 10)) + (''),
        method: 'get',
        dataType: 'json',
        contentType: 'application/json; charset=utf-8'
    })
        .done(function (data) {
            console.log(data);
            let events = [];
            const commutings = setCommutings(data.commute);
            const schedules = setSchedules(data.schedules);
            const holidays = setHolidays(data.holidays);
			const commuteRequests = setCommuteRequests(data.commuteRequests); //근태요청
            const overtimes = setOvertimes(data.overtimes);
			
            Array.prototype.push.apply(events, commutings);
            Array.prototype.push.apply(events, schedules);
			Array.prototype.push.apply(events, commuteRequests);
            Array.prototype.push.apply(events, holidays);
            Array.prototype.push.apply(events, overtimes);
            successCallback(events);

            const r = data.nearList;
            document.getElementById('request-title').innerText = r.requestDt;
            document.getElementById('request-content').innerText = koreanType(r.type);
            document.getElementById('request-status').innerText = koreanStatus(r.status);
        })
        .fail(function () {
            alert('일정 정보 조회 중 오류가 발생했습니다.');
            return false;
        });
}

document.addEventListener('DOMContentLoaded', function () {
    let calendarEl = document.getElementById('calendar');
    calendar = new FullCalendar.Calendar(calendarEl, {
        plugins: ['interaction', 'dayGrid', 'resourceTimeline'],
        eventTimeFormat: {
			hour: '2-digit',
			minute: '2-digit',
			hour12: false
		},
        businessHours: true,
      displayEventTime: false,
        header: {
            left: 'title',
            center: '',
            right: 'legend prev,next'
        },
      customButtons: {
         legend : {
            text: 'displaynone'
         }
      },
        events: function (info, successCallback) {
            commutings(info, successCallback);
        },
        eventClick: function (info) {
            //commuting(info);
        },
        selectable: true,
        select: function (info) {
           selectCommuting(info);
        }
    });
    calendar.render();
});


/*
const editCommute = function () {
    $.ajax({
        url: contextPath + 'commuting/'+ editForm.id.value+'.do',
        method: 'put',
		data: JSON.stringify({
            tm: editForm.requestTm.value,
            changeReason: editForm.content.value,
            approveId: editForm.approveId.value
        }),
		contentType: 'application/json; charset=utf-8'
    })
        .done(function (data) {
			alert(data);
	 		window.location.reload();
        })
        .fail(function (data) {
            alert(data.responseText);
        });
};*/

const colenWrite = function (_this) {
	_this.value = _this.value.replace(/[^0-9:]/g,"");
	if(_this.value.length == 2) {
		_this.value = _this.value+":";
	}
};

document.getElementById('edit-close-btn').addEventListener('click', function () {
    closeModal('edit-modal');
}, true);
