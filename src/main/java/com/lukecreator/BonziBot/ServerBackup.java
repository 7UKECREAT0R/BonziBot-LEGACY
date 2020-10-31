package com.lukecreator.BonziBot;

import java.awt.Color;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import net.dv8tion.jda.api.Region;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Guild.ExplicitContentLevel;
import net.dv8tion.jda.api.entities.Guild.VerificationLevel;
import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.exceptions.HierarchyException;
import net.dv8tion.jda.api.managers.GuildManager;
import net.dv8tion.jda.api.managers.RoleManager;

public class ServerBackup implements Serializable {

	private static final long serialVersionUID = 3252943049844181808L;
	public LocalDate taken;
	
	List<Object[]> roles;
	List<Object[]> channels;
	Object[] guild;
	
	// Role Serialization Indexing:
	// 0 = Color
	// 1 = Position
	// 2 = Name
	// 3 = Permissions
	
	// TextChannel Serialization Indexing:
	// 0 = Name
	// 1 = Type
	// 2 = Topic
	
	// VoiceChannel Serialization Indexing:
	// 0 = Color
	// 1 = Type
	// 2 = Bitrate
	// 3 = User Limit
	
	// Guild Serialization Indexing:
	// 0 = Name
	// 1 = ECL
	// 2 = Region
	// 3 = VerificationLevel
	
	public ServerBackup(Guild g) {
		taken = LocalDate.now();
		// -----------------------------------------------------------
		roles = new ArrayList<Object[]>();
		channels = new ArrayList<Object[]>();
		
		List<Role> roles;
		List<GuildChannel> channels;
		roles = g.getRoles();
		channels = g.getChannels();
		
		for(Role r : roles) {
			Object[] arr;
			arr = SerializeRole(r);
			this.roles.add(arr);
		}
		for(GuildChannel c : channels) {
			if(c.getType().equals
			(ChannelType.CATEGORY)) {
				continue;
			}
			Object[] arr;
			arr = SerializeChannel(c);
			this.channels.add(arr);
		}
		Object[] gSer = SerializeGuild(g);
		this.guild = gSer;
		System.out.print("Server Backup Created. Guild name: \"" + g.getName() + "\"\n");
		return;
	}
	public void Restore(Guild g) {
		for(Object[] arr : roles) {
			DeserializeRole(g, arr);
		}
		for(Object[] arr : channels) {
			DeserializeChannel(g, arr);
		}
		DeserializeGuild(g, guild);
	}
	
	public static Object[] SerializeRole(Role r) {
		Object[] array = new Object[4]; 
		if(r.getColor() == null) {
			array[0] = Color.gray.getRGB();
		} else {
			array[0] = (r.getColor().getRGB());
		}
		array[1] = (r.getPosition());
		array[2] = (r.getName());
		array[3] = (r.getPermissionsRaw());
		return array;
	}
	public static void DeserializeRole(Guild g, Object[] array) {
		int color = (int) array[0];
		String name = (String) array[2];
		Long permissions = (Long) array[3];
		
		List<Role> rls = g.getRolesByName(name, true);
		if(!rls.isEmpty()) {
			Role r = rls.get(0);
			RoleManager rm;
			try {
				rm = r.getManager();
				rm.setColor(color).complete();
				rm.setPermissions(permissions).complete();
				return;
			} catch(HierarchyException he) {
				return;
			}
		}
		
		g.createRole()
			.setColor(color)
			.setName(name)
			.setPermissions(permissions)
			.complete();
		return;
	}

	public static Object[] SerializeChannel(GuildChannel c) {
		
		if(c.getType().equals(ChannelType.TEXT)) {
			Object[] array = new Object[3];
			TextChannel mc;
			mc = (TextChannel) c;
			array[0] = c.getName();
			array[1] = c.getType().getId();
			array[2] = mc.getTopic();
			return array;
			
		} else if(c.getType().equals(ChannelType.VOICE)) {
			Object[] array = new Object[6];
			VoiceChannel vc;
			vc = (VoiceChannel) c;
			array[0] = c.getName();
			array[1] = c.getType().getId();
			array[2] = vc.getBitrate();
			array[3] = vc.getUserLimit();
			return array;
		} else {
			System.out.print("Error: Attempted to serialize invalid channel.\n");
			Object[] array = {"null"};
			return array;
		}
	}
	public static void DeserializeChannel(Guild g, Object[] array) {
		
		if(array[0].equals("null")) {
			System.out.print("Error: Invalid serialized object! (null)");
			return;
		}
		
		int typeInt = (int) array[1];
		ChannelType type;
		type = ChannelType.fromId(typeInt);
		
		if(type.equals(ChannelType.TEXT)) {
			
			String name = (String) array[0];
			String topic = (String) array[2];
			
			List<TextChannel> channels;
			channels = g.getTextChannelsByName(name, true);
			if(!channels.isEmpty()) {
				for(TextChannel ch : channels) {
					ch.getManager()
						.setName(name)
						.setTopic(topic)
						.complete();
					return;
				}
			}
				
			g.createTextChannel(name)
				.setTopic(topic).complete();
			return;
		}
		if(type.equals(ChannelType.VOICE)) {
			String name = (String) array[0];
			int bitrate = (int) array[2];
			int userlimit = (int) array[3];
			g.createVoiceChannel(name)
				.setBitrate(bitrate)
				.setUserlimit(userlimit)
				.complete();
			return;
		}
	}

	public static Object[] SerializeGuild(Guild g) {
		Object[] array = new Object[4]; 
		array[0] = (g.getName());
		array[1] = (g.getExplicitContentLevel().name());
		array[2] = (g.getRegion().name());
		array[3] = (g.getVerificationLevel().name());
		return array;
	}
	public static void DeserializeGuild(Guild g, Object[] array) {
		String name = (String) array[0];
		String _ecl = (String) array[1];
		String _region = (String) array[2];
		String _vl = (String) array[3];
		
		ExplicitContentLevel ecl;
		ecl = ExplicitContentLevel.valueOf(_ecl);
		Region region;
		region = Region.valueOf(_region);
		VerificationLevel vl;
		vl = VerificationLevel.valueOf(_vl);
		
		GuildManager gm;
		gm = g.getManager();
		
		gm.setName(name).complete();
		gm.setExplicitContentLevel(ecl).complete();
		gm.setRegion(region).complete();
		gm.setVerificationLevel(vl).complete();
		return;
	}
}
