package dev.kkl.bookingsystem.exception;

import dev.kkl.bookingsystem.dto.AppResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(ApplicationErrorException.class)
    @ResponseStatus(value = HttpStatus.OK)
    public AppResponse handleBizException(ApplicationErrorException e) {
        String msg = "app exception";
        if (e != null) {
            msg = e.getMsg();
            log.warn(e.toString());
        }
        return AppResponse.fail(msg);
    }


}
