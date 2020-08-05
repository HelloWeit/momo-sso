package cn.weit.sso.exception;

import org.springframework.security.authentication.AuthenticationServiceException;

/**
 * @author weitong
 */

public class LogicException extends AuthenticationServiceException {

    private String msg;


    public LogicException(String msg) {
        super(msg);
        this.msg = msg;
    }


    public String getMsg() {
        return msg;
    }

}