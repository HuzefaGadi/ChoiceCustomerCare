package com.unfc.choicecustomercare.models;

import java.util.List;

/**
 * Created by Rashida on 21/09/15.
 */
public class User {

    private List<Roles> roles;
    private String email;
    private String userName;
    private String userType;


    public List<Roles> getRoles() {
        return roles;
    }

    public void setRoles(List<Roles> roles) {
        this.roles = roles;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }
}
