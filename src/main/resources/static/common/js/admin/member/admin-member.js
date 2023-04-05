const writeForm = document.getElementById('write-form');
const editForm = document.getElementById('edit-form');
const _auths = function (form) {
    return [
        form.notice, form.member, form.schedule,
        form.supply, form.company
    ];
}

const _checkedAuth = function (form) {
    let list = [];

    _auths(form).forEach(function (e) {
        let data;
        if (e.checked) {
            list.push({
                id: e.value
            });
        }
    });

    return list;
};

const confirmMember = function (form) {
    const requiredFields = [
        form.name, form.memberId, form.position,
        form.mobileNo, form.color
    ];

    if (confirmRequiredField(requiredFields) === false) return false;

    let data = {
        name: form.name.value,
        memberId: form.memberId.value,
        password: form.password.value,
        position: form.position.value,
        department: form.department.value,
        mobileNo: form.mobileNo.value,
        phoneNo: form.phoneNo.value,
        color: form.color.value,
        auths: _checkedAuth(form)
    };

    return isEditForm(data, form);
}

const editMember = function () {
    if (!confirm('사용자 정보를 수정하시겠습니까?')) return false;

    const data = confirmMember(editForm);
    if (data === false) return false;
    
    $.ajax({
        url: contextPath + 'admin/member/' + data.id + '.do',
        method: 'patch',
        data: JSON.stringify(data),
        contentType: 'application/json; charset=utf-8'
    })
        .done(function (data) {
            alert(data);
            closeModal('edit-modal');
        })
        .fail(function (data) {
            alert(data.responseText);
            return false;
        });
};

document.getElementById('edit-btn').addEventListener('click', editMember, true);

const writeMember = function () {
    if (!confirm('사용자를 등록하시겠습니까?')) return false;

    const data = confirmMember(writeForm);
    if (data === false) return false;
    
    $.ajax({
        url: contextPath + 'admin/member.do',
        method: 'post',
        data: JSON.stringify(data),
        contentType: 'application/json; charset=utf-8'
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

/*const writeEmptyData = function () {
	    $.ajax({
	        url: contextPath + 'emptyDataInsert.do',
	        method: 'post',
	         async:false
	    })
	        .done(function () {
	        })
	        .fail(function () {
	            return false;
	        });
	};*/


/*function writeMember(){	
	if (!confirm('사용자를 등록하시겠습니까?')) return false;
	 return new Promise((resolve, reject) => {
		    const data = confirmMember(writeForm);
		    if (data === false) return false;
		    
		    $.ajax({
		        url: contextPath + 'admin/member.do',
		        method: 'post',
		        data: JSON.stringify(data),
		        contentType: 'application/json; charset=utf-8'
		    })
		        .done(function (data) {
		            alert(data);
		            resolve("성공");
		            location.reload();
		        })
		        .fail(function (data) {
		            alert(data.responseText);
		            reject("실패");
		            return false;
		        });
		});
};

function writeEmptyData(){
	return new Promise((resolve, reject) => {
	    $.ajax({
	        url: contextPath + 'emptyDataInsert.do',
	        method: 'post'
	    })
	        .done(function () {
	            resolve("성공");
	        })
	        .fail(function () {
	            reject("실패");
	            return false;
	        });
	});
};

const promiseFunction = writeMember().then(function () {
	writeEmptyData();
});*/

document.getElementById('write-btn').addEventListener('click',writeMember, true);

const openWriteModal = function () {
    const inputs = writeForm.getElementsByTagName('input');
    const boxes = writeForm.getElementsByTagName('select');
    Array.prototype.forEach.call(inputs, function (input) {
        let type = input.type;
        if (type === "text" || type === "password") {
            input.value = "";
        } else if (type === 'checkbox') {
            input.checked = false;
        }
    });
    Array.prototype.forEach.call(boxes, function (box) {
        box.value = "";
    });

    openModal('write-modal');
};

document.getElementById('write-modal-btn').addEventListener('click', openWriteModal, true);

const member = function (id) {
    const inputs = editForm.getElementsByTagName('input');
    const boxes = editForm.getElementsByTagName('select');
    Array.prototype.forEach.call(inputs, function (input) {
        let type = input.type;
        if (type === "text" || type === "password") {
            input.value = "";
        } else if (type === 'checkbox') {
            input.checked = false;
        }
    });
    Array.prototype.forEach.call(boxes, function (box) {
        box.value = "";
    });


    $.ajax({
        url: contextPath + 'admin/member/' + id + '.do',
        method: 'get',
        dataType: 'json',
        contentType: 'application/json; charset=utf-8'
    })
        .done(function (data) {
            editForm.password.value = '';

            editForm.id.value = data.id;
            editForm.name.value = data.name;
            editForm.memberId.value = data.memberId;
            editForm.position.value = data.position;
            editForm.department.value = data.department;
            editForm.mobileNo.value = data.mobileNo;
            editForm.phoneNo.value = data.phoneNo;
            editForm.color.jscolor.fromString(data.color);

            const auths = _auths(editForm);

            data.auths.forEach(function (auth) {
                for (let i = 0; i < auths.length; i++) {
                    if (auth.id == auths[i].value) {
                        auths[i].checked = true;
                        break;
                    }
                }
            });
        })
        .fail(function () {
            alert('사용자 정보 조회 중 오류가 발생했습니다.');
            return false;
        });

    openModal('edit-modal');
}

const deleteMember = function (id, memberId) {
    if (!confirm(memberId + '의 정보를 삭제하시겠습니까?')) return false;

    $.ajax({
        url: contextPath + 'admin/member/' + id + '.do',
        method: 'delete',
    })
        .done(function (data) {
            alert(data);
            location.reload();
        })
        .fail(function (data) {
            alert(data.responseText);
            return false;
        });
}

const members = function (p) {
    let url = "";
    url += ("&n=" + document.getElementById('searchName').value);
    url += ("&po=" + document.getElementById('searchPosition').value);
    url += ("&d=" + document.getElementById('searchDepartment').value);

    $.ajax({
        url: contextPath + 'admin/member/search.do?p=' + p + url,
        method: 'get',
        dataType: 'json',
        contentType: 'application/json; charset=utf-8'
    })
        .done(function (data) {
            const list = data.list;
            let tr = "";
            if (list.length > 0) {
                list.forEach(function (e) {
                    tr += '<tr class="tbbody">';
                    tr += '<td>' + e.name + '</td>';
                    tr += '<td>' + e.memberId + '</td>';
                    tr += '<td>' + e.positionName + '</td>';
                    tr += '<td>' + nullStr(e.departmentName) + '</td>';
                    tr += '<td>' + nullStr(e.mobileNo) + '</td>';
                    tr += '<td>' + nullStr(e.phoneNo) + '</td>';
                    tr += '<td>';
                    tr += '<a role="button" class="btn jjgray" onclick="member(\'' + e.id + '\')">수정</a>';
                    tr += '<a role="button" class="btn jjred ml5" onclick="deleteMember(\'' + e.id + '\', \'' + e.memberId + '\')">삭제</a>';
                    tr += '</td>';
                    tr += '</tr>';
                });
            } else {
                tr += "<tr><td colspan='7'>등록된 사용자가 없습니다.</td></tr>";
            }
            document.getElementById('members').innerHTML = tr;
            document.getElementById('page').innerHTML = data.page;
            document.getElementById('total-cnt').innerText = "총 사용자 " + data.totalCnt + "명";
        })
        .fail(function (data) {
            alert(data.responseText);
            return false;
        });
};

document.getElementById('search-btn').addEventListener('click', function () {
    members(1);
}, true);

document.addEventListener('DOMContentLoaded', function () {
    members(1);
}, true);

document.getElementById('edit-close-btn').addEventListener('click', function () {
    if (!confirm('작업 중인 내용을 저장하지 않고 창을 닫으시겠습니까?')) return false;
    closeModal('edit-modal');
});

document.getElementById('write-close-btn').addEventListener('click', function () {
    if (!confirm('작업 중인 내용을 저장하지 않고 창을 닫으시겠습니까?')) return false;
    closeModal('write-modal');
});

