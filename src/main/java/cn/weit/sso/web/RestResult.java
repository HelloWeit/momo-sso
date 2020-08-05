package cn.weit.sso.web;


import org.springframework.http.HttpStatus;

/**
 * @author weitong
 */
public class RestResult<T> {

	private Integer code;

	private String msg;

	private T result;

	private RestResult(Integer code) {
		this.code = code;
	}

	public static RestResult success() {
		return new RestResult(HttpStatus.OK.value());
	}

	public static RestResult success(Object object) {
		RestResult restResult = success();
		restResult.result = object;
		return restResult;
	}


	public static RestResult<?> fail(String msg) {
		RestResult<?> restResult = new RestResult(HttpStatus.BAD_REQUEST.value());
		restResult.msg = msg;
		return restResult;
	}


}
