package com.client.soap;

import java.util.HashMap;
import java.util.Map;

public class TransactionInfo {
	private String spCode;
	private String servCode;
	private String sptrn;
	private String amount;
	private String timestamp;
	private String description;
	private String type;
	private String versionCode;
	private String paymentChannel;

	public String getSpCode() {
		return spCode;
	}

	public void setSpCode(String spCode) {
		this.spCode = spCode;
	}

	public String getServCode() {
		return servCode;
	}

	public void setServCode(String servCode) {
		this.servCode = servCode;
	}

	public String getSptrn() {
		return sptrn;
	}

	public void setSptrn(String sptrn) {
		this.sptrn = sptrn;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getVersionCode() {
		return versionCode;
	}

	public void setVersionCode(String versionCode) {
		this.versionCode = versionCode;
	}

	public String getPaymentChannel() {
		return paymentChannel;
	}

	public void setPaymentChannel(String paymentChannel) {
		this.paymentChannel = paymentChannel;
	}

	public Map<String, String> toMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("spCode", spCode);
		map.put("servCode", servCode);
		map.put("sptrn", sptrn);
		map.put("amount", amount);
		map.put("type", type);
		map.put("timestamp", timestamp);
		map.put("description", description);
		map.put("versionCode", versionCode);
		map.put("paymentChannel", paymentChannel);
		return map;
	}
}
