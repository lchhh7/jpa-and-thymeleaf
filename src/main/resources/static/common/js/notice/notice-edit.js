const editForm = document.getElementById('edit-form');

const editNotice = function () {
    if (!confirm('공지사항을 수정하시겠습니까?')) return false;

    oEditors.getById["ir1"].exec("UPDATE_CONTENTS_FIELD", []);

    const data = confirmNotice(editForm);
    if (data === false) return false;

    $.ajax({
        url: contextPath + '/notice/edit.do',
        method: 'post',
        data: JSON.stringify(data),
        contentType: 'application/json; charset=utf-8'
    })
        .done(function (data) {
            alert('공지사항을 정상적으로 수정했습니다.');
            location.href = contextPath + "/notice/view.do?id=" + data;
        })
        .fail(function (data) {
            alert(data.responseText);
            return false;
        });
};

document.getElementById('edit-btn').addEventListener('click', editNotice, true);

const cancelEdit = function () {
    if (!confirm('작업 중인 내용이 저장되지 않습니다. 공지사항 수정을 취소하시겠습니까?')) return false;
    location.href = contextPath + '/notice/view.do?id=' + editForm.id.value;
};

document.getElementById('cancel-btn').addEventListener('click', cancelEdit, true);

const deleteAttach = function (el, id) {
    if(!confirm('기존에 업로드 되었던 첨부파일을 삭제하면 복구할 수 없습니다.\n첨부파일을 삭제하시겠습니까?')) return false;

    $.ajax({
        url: contextPath + '/notice/attach.do?id=' + id,
        method: 'delete',
    })
        .done(function () {
            const div = el.parentNode;
            div.parentNode.removeChild(div);
        })
        .fail(function (data) {
            alert(data.responseText);
            return false;
        });
}