package com.lukecreator.BonziBot;

import java.io.Serializable;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.Role;

public class ReactionRole implements Serializable {
	
	private static final long serialVersionUID = -958135939082284887L;
	Long channel;
	Long message;
	Long role;
	
	public ReactionRole(Long Channel, Long Message, Long Role) {
		channel = Channel;
		message = Message;
		role = Role;
	}
	public Long getRoleRaw() {
		return role;
	}
	public Long getMessageRaw() {
		return message;
	}
	public Long getChannelRaw() {
		return channel;
	}
	/**
	 * Gets the role object attached to this ReactionRole object.
	 * @param g The guild the role is in.
	 * @return
	 */
	public Role getRole(Guild g) {
		Role r;
		r = g.getRoleById(role);
		return r;
	}
	
	/**
	 * Gets the message object attached to this ReactionRole object.
	 * If you need a 
	 * This is generally slow, so try to stay away from it.
	 * @param c The channel the message is in.
	 * @return
	 */
	public Message getMessage(MessageChannel c) {
		Message m;
		m = c.retrieveMessageById(message).complete();
		return m;
	}
	public MessageChannel getChannel(Guild g) {
		MessageChannel c;
		c = g.getTextChannelById(channel);
		return c;
	}
}
