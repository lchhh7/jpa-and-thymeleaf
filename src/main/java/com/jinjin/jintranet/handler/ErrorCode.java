package com.jinjin.jintranet.handler;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {

    //400 BAD_REQUEST 잘못된 요청
    INVALID_SCHEDULE_PARAMETER(400, "올바른 휴가 종류를 선택해주세요."),

    //409 CONFLICT 중복된 리소스
    ATTACH_DELETE_ERROR(409, "첨부파일 삭제 중 오류가 발생했습니다."),

    ATTACH_FILE_MANAGEMENT_ERROR(409, "파일처리 중 오류가 발생했습니다."),

    DATA_CONVERSION_ERROR(409, "변환 중 오류가 발생했습니다."),

    OPEN_API_ERROR(409, "API 처리중 오류가 발생했습니다."),
    //500 INTERNAL SERVER ERROR
    INTERNAL_SERVER_ERROR(500, "서버 에러입니다.");

    private final int status;
    private final String message;
}