package com.unfc.choicecustomercare.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Rashida on 07/10/15.
 */
public class Room {

    @SerializedName("Id")
    private int roomId;
    @SerializedName("LocationId")
    private int locationId;
    @SerializedName("Name")
    private String name;

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public int getLocationId() {
        return locationId;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
