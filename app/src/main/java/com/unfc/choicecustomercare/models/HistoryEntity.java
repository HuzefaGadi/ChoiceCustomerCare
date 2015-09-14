package com.unfc.choicecustomercare.models;

import java.util.List;

/**
 * Hai Nguyen - 8/27/15.
 */
public class HistoryEntity extends BaseEntity {

	boolean deny;
	MessageEntity message;

    public boolean isDeny() {
        return deny;
    }

    public MessageEntity getMessage() {
        return message;
    }
}
