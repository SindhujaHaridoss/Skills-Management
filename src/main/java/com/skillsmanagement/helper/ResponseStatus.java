package com.skillsmanagement.helper;

public class ResponseStatus<T> {

	private String statusCode;
	private String statusDescription;
	private String statusType;
	public ResponseStatus() {}
	private T data;
	public T getData() {
		return data;
	}
	public void setData(T data) {
		this.data = data;
	}
	public String getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}
	public String getStatusDescription() {
		return statusDescription;
	}
	public void setStatusDescription(String statusDescription) {
		this.statusDescription = statusDescription;
	}
	public String getStatusType() {
		return statusType;
	}
	public void setStatusType(String statusType) {
		this.statusType = statusType;
	}
	public ResponseStatus(String statusCode, String statusType, String statusDescription,T data) {
		this.statusCode = statusCode;
		this.statusType = statusType;
		this.statusDescription = statusDescription;
		this.data = data;
	}
}
