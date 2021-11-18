package com.client.soap;

import java.util.HashMap;
import java.util.Map;

public class UserInfo {
	private String isAuthenticated;
	private String userId;
	private String userName;
	private String fullNameEn;
	private String fullNameAr;
	private String mobileNo;
	private String email;
	private String emirateCode;
	private String poBox;

	public String getIsAuthenticated() {
		return isAuthenticated;
	}

	public void setIsAuthenticated(String isAuthenticated) {
		this.isAuthenticated = isAuthenticated;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getFullNameEn() {
		return fullNameEn;
	}

	public void setFullNameEn(String fullNameEn) {
		this.fullNameEn = fullNameEn;
	}

	public String getFullNameAr() {
		return fullNameAr;
	}

	public void setFullNameAr(String fullNameAr) {
		this.fullNameAr = fullNameAr;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEmirateCode() {
		return emirateCode;
	}

	public void setEmirateCode(String emirateCode) {
		this.emirateCode = emirateCode;
	}

	public String getPoBox() {
		return poBox;
	}

	public void setPoBox(String poBox) {
		this.poBox = poBox;
	}

	public Map<String, String> toMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("isAuthenticated", isAuthenticated);
		map.put("userId", userId);
		map.put("userName", userName);
		map.put("fullNameEn", fullNameEn);
		map.put("fullNameAr", fullNameAr);
		map.put("mobileNo", mobileNo);
		map.put("email", email);
		map.put("emirateCode", emirateCode);
		map.put("poBox", poBox);
		return map;
	}
}
