let attaches = [];      // 업로드 된 파일
let alert_yn = false;   // alert창 표시 여부

/**
 * 업로드 모달 창 닫기
 */
document.getElementById('upload-close-btn').addEventListener('click', function () {
    const wrap = document.getElementById('dropzone');
    const divs = wrap.getElementsByClassName('dz-preview').length;
    const completeDivs = wrap.getElementsByClassName('dz-complete').length;

    if (divs !== completeDivs) {
        if (!confirm("업로드 하지 않은 파일이 있습니다. 창을 닫으시겠습니까?")) return false;

        // TODO: 업로드 하지 않은 파일 목록에서 삭제해야함
    }

    closeModal('upload-modal');
});

/**
 * 파일 추가 시 alert_yn 초기화
 */
document.addEventListener('DOMContentLoaded', function () {
    document.getElementById('dropzone').addEventListener('drop', function () {
        alert_yn = true;
    }, true);

    document.getElementById('dropzone').addEventListener('click', function () {
        alert_yn = true;
    }, true);
});

/**
 * 업로드 성공 시
 */
const uploadSuccess = function (files, response) {
    let div = "";
    for (let i = 0; i < response.length; i++) {
        let e = response[i];
        let uuid = files[i].upload.uuid;
        div += '<div class="filelist mt5 mb10 clearfix">';
        div += '<a class="file floatleft" data-id="' + uuid + '" data-path="' + e.path + e.storedFileName + '">';
        div += e.originalFileName;
        div += '</a>';
        div += '<img class="ml15 eximg pointer" src="' + contextPath + 'common/img/delete.png" alt="삭제" onclick="cancelAttach(this, attaches);">';
        div += '</div>';

        attaches.push(e);
    }
    document.getElementById('files').innerHTML += div;
}

/**
 * 업로드 된 파일을 dropzone 에서 삭제
 */
const cancelAttachByDropZone = function (data) {
    const uuid = data.upload.uuid;
    let els = document.getElementsByClassName('file');
    Array.prototype.forEach.call(els, function (el) {
        if (el.nodeName === "A") {
            if (el.dataset.id === uuid) {
                cancelAttach(el, attaches);
            }
        }
    });
}

const cancelAttachByDropZone2 = function (data) {
    const uuid = data.upload.uuid;
    let els = document.getElementsByClassName('file');
    Array.prototype.forEach.call(els, function (el) {
        if (el.nodeName === "A") {
            if (el.dataset.id === uuid) {
                cancelAttach(el, attaches2);
            }
        }
    });
}

/**
 * 업로드 된 파일 삭제
 */
const cancelAttach = function (el, attaches) {
    const div = el.parentNode;
    div.parentNode.removeChild(div);

    const anchor = div.childNodes[0];
    const target = anchor.dataset.path;

    const idx = attaches.findIndex(function (item) {
        return (item.path + item.storedFileName) === target;
    })

    if (idx > -1) attaches.splice(idx, 1);

    const uuid = anchor.dataset.id;

    const spans = document.getElementsByClassName('dz-uuid-span');
    Array.prototype.forEach.call(spans, function (e) {
        if (uuid === e.innerText) {
            e.parentNode.parentNode.getElementsByClassName('dz-remove')[0].click();
        }
    });
}

const isOverMaxFileCnt = function (i, d, f) {
    if (i > d.options.maxFiles) {
        if (alert_yn) {
            alert('최대 '+ d.options.maxFiles + '개의 파일까지 업로드 할 수 있습니다.');
            alert_yn = false;
        }
        d.removeFile(f);
    }
}

const _acceptMimeTypeArr = [
    "image",                                            // .png, .jpg, .jpeg, .gif, .bmp
    "text",                                             // .txt, .html 등
    "application/vnd.ms-word",                          // .docx 등 워드 관련
    "application/vnd.ms-excel",                         // .xls 등 엑셀 관련
    "application/vnd.ms-powerpoint",                    // .ppt 등 파워포인트 관련
    "application/vnd.openxmlformats-officedocument",    // .docx, .dotx, .xlsx, .xltx, .pptx, .potx, .ppsx
    "applicaion/vnd.hancom",                            // .hwp 관련
    'application/pdf',                                  // .pdf
    'application/octet-stream',                         // .zip, .rar
];
const _acceptExtArr = [
    '.hwp', '.hwt', '.hml', '.hwpx',                    // 한글 확장자
    '.zip', '.tar', '.rar', '.7z'                       // 압축파일 확장자
];

const isValidFileExt = function (d, f) {
    let isValid = false;
    let mimeType = f.type;
    let fileName = f.name;
    let ext = '.' + fileName.slice(fileName.indexOf('.') + 1).toLowerCase();

    for (let i = 0; i < _acceptMimeTypeArr.length; i++) {
        let availableType = _acceptMimeTypeArr[i];
        if (mimeType.startsWith(availableType)) {
            isValid = true;
            break;
        }
    }

    for (let i = 0; i < _acceptExtArr.length; i++) {
        let availableExt = _acceptExtArr[i];
        if (ext === availableExt) {
            isValid = true;
            break;
        }
    }
    
    if (!isValid) {
        if (alert_yn) {
            alert(ext + ' 확장자는 업로드 할 수 없습니다.');
            alert_yn = false;
        }

        d.removeFile(f);
    }
}

const isOverFileSize = function (d, f) {
    const calibrationValue = 1048576;
    const maxFileSize = d.options.maxFilesize * calibrationValue;
    if (f.size > maxFileSize) {
        if (alert_yn) {
            alert('최대 ' + d.options.maxFilesize + 'MB 이하의 파일만 업로드 할 수 있습니다.\n업로드 하신 파일 크기: ' + Math.round(f.size/calibrationValue * 100)/100 + 'MB');
            alert_yn = false;
        }
        d.removeFile(f);
    }
}

const dzInit = function () {
    const dz = this;

    document.getElementById('upload-btn').addEventListener('click', function () {
        dz.processQueue();
    });

    dz.on('addedfile', function (file) {
        let uploadedCnt = document.getElementById('files').childElementCount;
        let wrap = document.getElementById('dropzone');
        let cnt = wrap.querySelectorAll('.dz-preview:not(.dz-success)').length;

        isOverMaxFileCnt((uploadedCnt+cnt), dz, file);
        isValidFileExt(dz, file);
        isOverFileSize(dz, file);

    });
    dz.on('successmultiple', function (files, response) {
        uploadSuccess(files, response);
    });

    dz.on('removedfile', function(data) {
        if (data.status === 'success') {
            cancelAttachByDropZone(data);
        };
    });

    dz.on('maxfilesexceeded', function (files) {
        if (alert_yn){
            alert('최대 '+ dz.options.maxFiles + '개의 파일까지 업로드 할 수 있습니다.');
            alert_yn = false;
        }
        dz.removeFile(files);
    })
}

const dzInit2 = function () {
    const dz2 = this;

    document.getElementById('upload-btn2').addEventListener('click', function () {
        dz2.processQueue();
    });

    dz2.on('addedfile', function (file) {

        let uploadedCnt = document.getElementById('files2').childElementCount;
        let wrap = document.getElementById('dropzone2');
        let cnt = wrap.getElementsByClassName('dz-preview').length;

        isOverMaxFileCnt((uploadedCnt+cnt), dz2, file);
        isValidFileExt(dz2, file);
        isOverFileSize(dz2, file);
    });
    dz2.on('successmultiple', function (files, response) {
        uploadSuccess2(files, response);
    });

    dz2.on('removedfile', function(data) {
        if (data.status === 'success') {
            cancelAttachByDropZone2(data);
        };
    });

    dz2.on('maxfilesexceeded', function (files) {
        if (alert_yn){
            alert('최대 '+ dz2.options.maxFiles + '개의 파일까지 업로드 할 수 있습니다.');
            alert_yn = false;
        }
        dz2.removeFile(files);
    });
}