package com.lukecreator.BonziBot;

import java.util.Date;

import net.dv8tion.jda.api.entities.MessageEmbed;

public class LoggingEntry {
	public enum LogType {
		Mute,
		Unmute,
		Ban,
		Unban,
		Warn,
		RemoveWarn,
		ClearWarns,
		ClearMsgs,
		
		DeletedMessage,
		Swear,
		
		NickChange,
		PrefixChange,
		
		TextChannelCreate,
		TextChannelRemove,
		VoiceChannelCreate,
		VoiceChannelRemove
		// etc..
	}
	public LoggingEntry() {
		sentID = 0l;
		targetUser = 0l;
		invokedUser = 0l;
		reason = "";
		ext = "";
		type = null;
		
		Date d = new Date();
		time = d.getTime();
	}
	public LoggingEntry(SerializableLoggingEntry e) {
		sentID = e.sentID;
		targetUser = e.targetUser;
		invokedUser = e.invokedUser;
		type = e.type;
		time = e.time;
		reason = e.reason;
		ext = e.ext;
	}
	
	public long UtcTotalDays() {
		long secs = time/1000;
		long mins = secs/60;
		long hours = mins/60;
		long days = hours/24;
		return days;
	}
	public long sentID; // Message ID.
	public long time; // When log was created.
	public MessageEmbed embed; // EmbedMessage to be sent. Not serialized.
	
	public long targetUser; // The user subject to this entry.
	public long invokedUser; // (may be 0l) The moderator that performed the warn, mute, etc...
	
	public String reason; // Reason for punishment.
	public String ext; // Extra data.
	public LogType type; // The type of entry.
}
