package com.unfc.choicecustomercare.utils;

/**
 * Created by Rashida on 17/09/15.
 */
public enum RequestTypeForRooms {


    FOOD(1), DRINK(2), BATHROOM(3), PAIN(4), OTHER(5);

    private final int mValue;

    RequestTypeForRooms(final int value) {
        this.mValue = value;
    }

    /**
     * Get value
     */
    public int getValue() {

        return mValue;
    }

}
