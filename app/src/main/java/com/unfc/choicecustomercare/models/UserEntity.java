package com.unfc.choicecustomercare.models;

/**
 * Hai Nguyen - 8/27/15.
 */
public class UserEntity extends BaseEntity {

	private int userType;

	private String MI;
	private String Email;
	private String Password;
	private String userName;
	private String LastName;
	private String FirstName;
	private String token_type;
	private String access_token;
	private String error;
	private String error_description;

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getToken_type() {
		return token_type;
	}

	public void setToken_type(String token_type) {
		this.token_type = token_type;
	}

	public String getAccess_token() {
		return access_token;
	}

	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getError_description() {
		return error_description;
	}

	public void setError_description(String error_description) {
		this.error_description = error_description;
	}

	public String getLastName() {
		return LastName;
	}

	public String getFirstName() {
		return FirstName;
	}

	public String getMI() {
		return MI;
	}

	public void setMI(String MI) {
		this.MI = MI;
	}

	public String getEmail() {
		return Email;
	}

	public void setEmail(String email) {
		this.Email = email;
	}

	public void setLastName(String lastName) {
		LastName = lastName;
	}

	public void setFirstName(String firstName) {
		FirstName = firstName;
	}

	public String getPassword() {
		return Password;
	}

	public void setPassword(String password) {
		this.Password = password;
	}

	public int getUserType() {
		return userType;
	}

	public void setUserType(int userType) {
		this.userType = userType;
	}

	public String getAccessToken() {
		return access_token;
	}

	public String getUserName() {
		return userName;
	}

	public String getTokenType() {
		return token_type;
	}
}
