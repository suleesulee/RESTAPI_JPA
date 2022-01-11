package com.example.jpa.common.model;

import com.example.jpa.board.model.ServiceResult;
import com.example.jpa.notice.model.ResponseMessage;
import org.springframework.http.ResponseEntity;


public class ResponseResult {
    public static ResponseEntity<?> fail(String message) {
        return fail(message, null);
    }

    public static ResponseEntity<?> fail(String message, Object data) {
        return ResponseEntity.badRequest().body(ResponseMessage.fail(message, data));
    }

    public static ResponseEntity<?> success() {

        return ResponseEntity.ok().body(ResponseMessage.success());
    }

    public static ResponseEntity<?> success(Object data) {
        return ResponseEntity.ok().body(ResponseMessage.success(data));
    }

    public static ResponseEntity<?> result(ServiceResult result) {
        if(!result.isResult()){
            return fail(result.getMessage());
        }
        return success();
    }
}
