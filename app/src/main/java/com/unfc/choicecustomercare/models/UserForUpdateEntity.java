package com.unfc.choicecustomercare.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Rashida on 08/10/15.
 */
public class UserForUpdateEntity {


    @SerializedName("FirstName")
    private String firstName;
    @SerializedName("LastName")
    private String lastName;
    @SerializedName("ResponderId")
    private String responderId;
    @SerializedName("Bio")
    private String bio;
    @SerializedName("ProfilePhoto")
    private String profilePhoto;
    @SerializedName("IsChargedNurse")
    private String isChargedNurse;

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

    public String getResponderId() {
        return responderId;
    }

    public void setResponderId(String responderId) {
        this.responderId = responderId;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

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



}
