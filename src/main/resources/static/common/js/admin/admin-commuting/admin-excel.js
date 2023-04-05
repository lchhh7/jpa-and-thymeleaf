/*function commuteExcelDownloadByAdmin(month) {
		var data = {
			month: month
		}
		$.ajax({
			type: "POST",
			url: contextPath+"/adminOvertime.do",
			data: JSON.stringify(data),
			contentType: "application/json; charset=utf-8",
		}).done(function (resp) {
			getFileDownload();
		}).fail(function (error) {
			alert(error.responseText);
		});
	}

function getFileDownload() {
		window.open(contextPath+"downLoadCommutingExcel.do");
	}
*/
let month = new Date().getMonth()+1;

const excelWriterByAdmin = function(selectedYear){
		$.ajax({
			type: "POST",
			url: contextPath+"adminOvertimeByExcel.do",
			data: JSON.stringify({
				month : month,
				selectedYear : selectedYear
			}),
			contentType: "application/json; charset=utf-8",
		}).done(function(resp){
				getFileDownload();
				closeModal('admin-excel-yearchoiceModal');
		}).fail(function (error) {
			alert(error.responseText);
		});
	}

const getFileDownload = function() {
	yearOption = document.getElementById('selectYear');
	var selectYear = yearOption.options[yearOption.selectedIndex].value;
	window.open(contextPath+"downLoadAdminFile.do?selectYear="+selectYear);
	}

document.getElementById('excelWriterByAdmin').addEventListener('click',function () {
	openModal('admin-excel-yearchoiceModal');
},true);

document.getElementById('admin-yearchoice-btn').addEventListener('click',function () {
	yearOption = document.getElementById('selectYear');
	var selectedYear = yearOption.options[yearOption.selectedIndex].value;
	excelWriterByAdmin(selectedYear);
},true);


document.getElementById('admin-yearchoice-close-btn').addEventListener('click',function () {
	closeModal('admin-excel-yearchoiceModal');
},true);

