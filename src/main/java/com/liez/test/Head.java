package com.liez.test;

public class Head {
	private String partnerNo;

	private String timestamp;

	private String requestId;

	private String version;

	private String sign;

	private String message;

	private Integer resultCode = Integer.valueOf(1);

	public String getPartnerNo() {
		return this.partnerNo;
	}

	public void setPartnerNo(String partnerNo) {
		this.partnerNo = partnerNo;
	}

	public String getTimestamp() {
		return this.timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getRequestId() {
		return this.requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public String getVersion() {
		return this.version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getSign() {
		return this.sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getMessage() {
		return this.message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Integer getResultCode() {
		return this.resultCode;
	}

	public void setResultCode(Integer resultCode) {
		this.resultCode = resultCode;
	}

	public String toString() {
		return "Head{partnerNo='" + this.partnerNo + '\'' + ", timestamp='" + this.timestamp + '\'' + ", requestId='"
				+ this.requestId + '\'' + ", version='" + this.version + '\'' + ", sign='" + this.sign + '\''
				+ ", message='" + this.message + '\'' + ", resultCode=" + this.resultCode + '}';
	}
}
