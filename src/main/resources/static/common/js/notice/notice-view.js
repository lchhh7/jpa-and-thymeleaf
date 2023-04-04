const moveEditPage = function () {
    if (!confirm('공지사항 수정페이지로 이동하시겠습니까?')) return false;
    location.href =  contextPath + '/notice/edit.do?id=' + document.getElementById('id').value;
};

document.getElementById('edit-btn').addEventListener('click', moveEditPage, true);

const deleteNotice = function () {
    if (!confirm('공지사항을 삭제하시겠습니까?')) return false;

    $.ajax({
        url: contextPath + '/notice.do',
        method: 'delete',
        data: JSON.stringify({id: document.getElementById('id').value}),
        contentType: 'application/json; charset:utf-8'
    })
        .done(function (data) {
            alert(data);
            location.href = contextPath + '/notice.do';
        })
        .fail(function (data) {
            alert(data.responseText);
            return false;
        });
};
document.getElementById('delete-btn').addEventListener('click', deleteNotice, true);