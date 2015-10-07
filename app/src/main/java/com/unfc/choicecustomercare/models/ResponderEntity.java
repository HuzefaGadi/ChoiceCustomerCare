package com.unfc.choicecustomercare.models;

/**
 * Created by Rashida on 21/09/15.
 */
public class ResponderEntity extends BaseEntity {

    private String firstName;
    private String lastName;
    private User user;
    private MessageEntity message;
    private boolean deny;
    private String profilePhoto;
    private String isChargedNurse;

    public String getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    public String getIsChargedNurse() {
        return isChargedNurse;
    }

    public void setIsChargedNurse(String isChargedNurse) {
        this.isChargedNurse = isChargedNurse;
    }

    public MessageEntity getMessage() {
        return message;
    }

    public void setMessage(MessageEntity message) {
        this.message = message;
    }

    public boolean isDeny() {
        return deny;
    }

    public void setDeny(boolean deny) {
        this.deny = deny;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
