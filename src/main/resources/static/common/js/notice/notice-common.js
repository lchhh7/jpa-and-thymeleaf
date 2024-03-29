// POST 메서드 구현 예제
async function postData(url = "", data = {}) {
    // 옵션 기본 값은 *로 강조
    const response = await fetch(url, {
        method: "POST", // *GET, POST, PUT, DELETE 등
        mode: "cors", // no-cors, *cors, same-origin
        cache: "no-cache", // *default, no-cache, reload, force-cache, only-if-cached
        credentials: "same-origin", // include, *same-origin, omit
        headers: {
            "Content-Type": "application/json",
            // 'Content-Type': 'application/x-www-form-urlencoded',
        },
        redirect: "follow", // manual, *follow, error
        referrerPolicy: "no-referrer", // no-referrer, *no-referrer-when-downgrade, origin, origin-when-cross-origin, same-origin, strict-origin, strict-origin-when-cross-origin, unsafe-url
        body: JSON.stringify(data), // body의 데이터 유형은 반드시 "Content-Type" 헤더와 일치해야 함
    });
    return response.json(); // JSON 응답을 네이티브 JavaScript 객체로 파싱
}
const confirmNotice = function (form) {
    const requiredFields = [
        form.title, form.ir1
    ];

    //if (confirmRequiredField(requiredFields) === false) return false;

    let data = {
        title: form.title.value,
        content: form.ir1.value
    }
    console.log(data);

    return isEditForm(data, form);
}

const upload = function (form) {
    let formData = new FormData(form);

    $.ajax({
        url: contextPath + 'notice/upload.do',
        method: 'post',
        enctype: 'multipart/form-data',
        processData: false,
        contentType: false,
        data: formData,
    })
        .done(function (data) {
            let div = "";
            Array.prototype.forEach.call(data, function (e) {
                div += `
                    <div class="filelist mt5 mb10 clearfix">
                    <a href="' + e.path + e.storedFileName + '" class="file floatleft" download>\${e.originalFileName}</a>
                    <img class="ml15 eximg" src="' + contextPath + 'common/img/delete.png" alt="삭제" onclick="cancelAttach(this, attaches)">
                    </div>
                `

                attaches.push(e);
            });

            document.getElementById('files').innerHTML += div;
        })
        .fail(function (data) {
            alert(data.responseText);
            return false;
        });
};

nhn.husky.EZCreator.createInIFrame({
    oAppRef: oEditors,
    elPlaceHolder: "ir1",
    sSkinURI: contextPath + "/common/smarteditor/SmartEditor2Skin.html",
    htParams: {
        bUseToolbar: true,
        bUseVerticalResizer: true,
        bUseModeChanger: false,
        SE2M_FontName: {
            htMainFont: {'id': '굴림','name': '굴림','size': '14','url': '','cssUrl': ''} // 기본 글꼴 설정
        },
    },
    fCreator: "createSEditor2",
});

Dropzone.options.dropzone = {
    url: contextPath + '/notice/upload.do',
    autoProcessQueue: false,
    maxFilesize: 10,
    maxFiles: 2,
    parallelUploads: 2,
    addRemoveLinks: true,
    acceptedFiles: ".jpeg,.jpg,.png,.gif,.JPEG,.JPG,.PNG,.GIF,.pdf,.PDF,.zip,.ZIP",
    dictRemoveFile: '<img class="eximg pointer" src="' + contextPath + '/common/img/delete.png" alt="삭제">',
    uploadMultiple: true,
    init: dzInit
};

document.getElementById('upload-modal-btn').addEventListener('click', function () {
    if (document.getElementById('files').childElementCount >= 2) {
        alert('최대 2개의 파일까지 업로드 할 수 있습니다.');
        return false;
    }

    openModal('upload-modal');
});