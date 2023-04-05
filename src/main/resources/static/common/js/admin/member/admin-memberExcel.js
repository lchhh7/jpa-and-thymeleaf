const writeExcel = function () {
    if (!confirm('사용자 목록을 출력 하시겠습니까?')) return false;

    $.ajax({
        type: "POST",
        url: contextPath + "admin/memberListPrintByExcel.do",
        contentType: "application/json; charset=utf-8"
    })
        .done(function (resp) {
            getmlFileDownload();
        })
        .fail(function (error) {
            alert(error.responseText);
        });
};

const getmlFileDownload = function() {
		window.open(contextPath+"admin/downLoadmlFile.do");
	}

document.getElementById('memberList').addEventListener('click', writeExcel, true);
