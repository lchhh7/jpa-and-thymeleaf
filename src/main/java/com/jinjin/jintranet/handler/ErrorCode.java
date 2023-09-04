package com.jinjin.jintranet.handler;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {

    //400 BAD_REQUEST 잘못된 요청
    INVALID_SCHEDULE_PARAMETER(400, "올바른 휴가종류를 선택해주세요."),
    INVALID_APPROVER_PARAMETER(400, "결제자를 선택해주세요."),
    //INVALID_DATE_PARAMETER(400, "일정 종료일과 시작일을 올바르게 입력해주세요."),
    INVALID_DATE_PARAMETER(400, "유효하지 않은 일정입니다."),
    INVALID_CANCEL_PARAMETER(400, "승인 상태인 일정만 취소요청 할 수 있습니다."),
    INVALID_DELETE_PARAMETER(400, "지난 일정은 취소요청 할 수 없습니다."),

    INVALID_CONTENT_PARAMETER(400, "내용을 입력해주세요."),

    //404 NOT_FOUND 잘못된 리소스 접근
    DISPLAY_NOT_FOUND(404, "공지사항을 찾을 수 없습니다."),

    //409 CONFLICT 중복된 리소스
    ATTACH_DELETE_ERROR(409, "첨부파일 삭제 중 오류가 발생했습니다."),

    //500 INTERNAL SERVER ERROR
    INTERNAL_SERVER_ERROR(500, "서버 에러입니다.");

    private final int status;
    private final String message;
}