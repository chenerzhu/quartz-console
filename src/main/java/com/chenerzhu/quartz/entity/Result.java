package com.chenerzhu.quartz.entity;

import java.io.Serializable;

public class Result implements Serializable{

	private static final long serialVersionUID = 1L;
	
	public static Result failure(String msg) {
		return new Result(false, msg);
	}
	public static Result failure(Exception e) {
		return new Result(false, e.getMessage());
	}
	public static Result failure() {
		return new Result(false);
	}
	public static Result success() {
		return new Result(true);
	}
	public static Result success(String msg) {
		return new Result(true, msg);
	}
	public Result(boolean valid, String msg) {
		super();
		this.valid = valid;
		this.msg = msg;
	}
	public Result(boolean valid) {
		super();
		this.valid = valid;
	}
	public boolean isValid() {
		return valid;
	}
	public void setValid(boolean valid) {
		this.valid = valid;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	boolean valid;
	String msg;
	Object data;

	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.valid = true;
		this.data = data;
	}
	
}
