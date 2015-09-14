package com.unfc.choicecustomercare.models;

import java.util.List;

/**
 * Hai Nguyen - 8/27/15.
 */
public class MessageEntity extends BaseEntity {

	private String sent;
	private String clientId;
	private String patientId;
	private String responded;
	private String fullFilled;
	private String responderId;
	private String messageText;
	private String messageTypeId;
	private String messageQueueId;

	List<MessageEntity> incoming;
	List<MessageEntity> inprogress;
	List<MessageEntity> complete;

	public String getSent() {
		return sent;
	}

	public String getClientId() {
		return clientId;
	}

	public String getPatientId() {
		return patientId;
	}

	public String getResponded() {
		return responded;
	}

	public String getFullFilled() {
		return fullFilled;
	}

	public String getResponderId() {
		return responderId;
	}

	public String getMessageText() {
		return messageText;
	}

	public String getMessageTypeId() {
		return messageTypeId;
	}

	public String getMessageQueueId() {
		return messageQueueId;
	}

	public List<MessageEntity> getIncoming() {
		return incoming;
	}

	public List<MessageEntity> getInProgress() {
		return inprogress;
	}

	public List<MessageEntity> getComplete() {
		return complete;
	}
}
