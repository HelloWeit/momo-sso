package cn.weit.sso.exception;

import cn.weit.sso.web.RestResult;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * @author weitong
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = LogicException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Object handleException(HttpServletRequest request, LogicException e, HttpServletResponse response) {
        return RestResult.fail(e.getMsg());
    }


}
