package com.sztus.unicorn.lib.core.exception;

import com.sztus.unicorn.lib.core.base.BaseError;
import com.sztus.unicorn.lib.core.constant.CodeConst;

/**
 * 用于方法中断的自定异常基类, 此异常不提供堆栈信息的输出
 * 提供自定义的message信息的输入和输出
 *
 * @author Daniel
 * 2020/7/24
 */

public class ProcedureException extends Exception {

	private static final long serialVersionUID = -7349807563076530612L;

	/**
	 * 仅包含message，不记录栈异常，高性能
	 *
	 * @param message 异常描述
	 */
	public ProcedureException(String message) {
		super(message, null, false, false);
		this.code = CodeConst.FAILURE;
		this.message = message;
	}

	/**
	 * 仅包含message，不记录栈异常，高性能
	 *
	 * @param code    错误码定义
	 * @param message 异常描述
	 */
	public ProcedureException(Integer code, String message) {
		this(message);

		this.code = code;
		this.message = message;
	}

	/**
	 * 仅包含message，不记录栈异常，高性能
	 *
	 * @param baseError 参照错误枚举定义
	 */
	public ProcedureException(BaseError baseError) {
		this(baseError.getMessage());

		this.code = baseError.getCode();
		this.message = baseError.getMessage();
	}

	private Integer code;
	private String message;

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	@Override
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
