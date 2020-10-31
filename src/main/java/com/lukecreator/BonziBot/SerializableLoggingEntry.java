package com.lukecreator.BonziBot;

import java.io.Serializable;

import com.lukecreator.BonziBot.LoggingEntry.LogType;

public class SerializableLoggingEntry implements Serializable {
	private static final long serialVersionUID = 1L;

	public long sentID;
	public long targetUser;
	public long invokedUser;
	public LogType type;
	public long time;
	public String reason;
	public String ext;
	
	public SerializableLoggingEntry(LoggingEntry nonSerial) {
		sentID = nonSerial.sentID;
		targetUser = nonSerial.targetUser;
		invokedUser = nonSerial.invokedUser;
		type = nonSerial.type;
		time = nonSerial.time;
		reason = nonSerial.reason;
		ext = nonSerial.ext;
	}
}
