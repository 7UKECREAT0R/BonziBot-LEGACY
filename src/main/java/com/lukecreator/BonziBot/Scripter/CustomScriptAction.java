package com.lukecreator.BonziBot.Scripter;

import java.awt.Color;
import java.io.Serializable;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import com.lukecreator.BonziBot.App;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

// Contained inside CustomScriptResult
public class CustomScriptAction implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public enum CustomScriptActionType {
		_unassigned,
		
		PlainText,
		Choose,
		Embed,
		
		SetNickname,
		GiveRole,
		RemoveRole,
		Ban, Kick,
	}
	
	// This function does infact cache the values, I dunno why I did this...
	static CustomScriptActionType[] _displayTypesCache = null;
	public static CustomScriptActionType[] getDisplayTypes() {
		if(_displayTypesCache == null) {
			CustomScriptActionType[] allTypes = CustomScriptActionType.values();
			CustomScriptActionType[] displayTypes = new
					CustomScriptActionType[allTypes.length-1];
			for(int i = 1; i < allTypes.length; i++) {
				displayTypes[i-1] = allTypes[i];
			}
			_displayTypesCache = displayTypes;
		}
		return _displayTypesCache;
		
	}
	
	public CustomScriptActionType type;
	public String data;
	public String name;
	
	public void perform(HashMap<String, String> inputArgs, User performer, TextChannel tc) {
		String data = applyVariables(inputArgs, performer, this.data);
		
		switch(type) {
		
		case Ban:
			break;
		case Choose:
			intern_choose(data, tc, performer);
			break;
		case Embed:
			intern_embed(data, tc, performer);
			break;
		case GiveRole:
			intern_giveRole(data, tc, performer);
			break;
		case Kick:
			break;
		case PlainText:
			intern_plainText(data, tc, performer);
			break;
		case RemoveRole:
			intern_removeRole(data, tc, performer);
			break;
		case SetNickname:
			intern_setNickname(data, tc, performer);
			break;
		case _unassigned:
			return;
		default:
			break;
		
		}
	}
	
	void intern_plainText(String data, TextChannel tc, User performer) {
		tc.sendMessage(data).queue();
	}
	void intern_choose(String data, TextChannel tc, User performer) {
		SecureRandom rand = new SecureRandom();
		String[] options = data.split(",");
		String option = options[rand.nextInt(options.length)];
		option = option.trim();
		tc.sendMessage(option).queue();
	}
	void intern_embed(String data, TextChannel tc, User performer) {
		EmbedBuilder eb = new EmbedBuilder();
		String[] items;
		if(!data.contains(">")) {
			items = data.split(";");
		} else {
			// Use color
			int index = data.indexOf('>');
			String rem = data.substring(index+1);
			items = rem.split(";");
			String col = data.substring(0, index);
			if(col.startsWith("<"))
				col = col.substring(1);
			
			Color c = App.colorFromName(col);
			if(c==null) {
				tc.sendMessage("ERROR: Embed action, the color you picked (" + col + ") isn't in my color list!").queue();
			}
			eb.setColor(c);
		}
		
		// Apply embed titles/descriptions.
		String title = null, desc = null;
		for(int i = 0; i < items.length; i++) {
			String item = items[i];
			if(i == 0) {eb.setTitle(item);continue;}
			if(i == 1) {eb.setDescription(item);continue;}
			int even = i % 2;
			if(even == 0) {
				title = item;
			} else {
				desc = item;
				if(title != null && desc != null) {
					eb.addField(title, desc, false);
					title = null; desc = null;
				}
			}
		}
		if(title != null && desc == null) {
			eb.addField(title, "", false);
		} else if(title != null && desc != null) {
			eb.addField(title, desc, false);
		}
		MessageEmbed msg = eb.build();
		tc.sendMessage(msg).queue();
	}
	void intern_setNickname(String data, TextChannel tc, User performer) {
		Guild g = tc.getGuild();
		int firstSpace = data.indexOf(' ');
		if(firstSpace == -1) {
			tc.sendMessage("ERROR: Not enough arguments in the SetNickname action.").queue();
			return;
		}
		String nick = data.substring(firstSpace).trim();
		String user = data.substring(0, firstSpace).trim();
		
		Member m = getMemberArgument(user, g);
		if(m == null) {
			tc.sendMessage("ERROR: SetNickname action, user \"" + user + "\" is not in the server or doesn't exist!").queue();
			return;
		}
		g.modifyNickname(m, nick).reason("From CustomCommand. Executed by " + performer.toString()).queue();
		return;
	}
	void intern_giveRole(String data, TextChannel tc, User performer) {
		Guild g = tc.getGuild();
		int firstSpace = data.indexOf(' ');
		if(firstSpace == -1) {
			tc.sendMessage("ERROR: Not enough arguments in the GiveRole action.").queue();
			return;
		}
		String role = data.substring(firstSpace).trim();
		String user = data.substring(0, firstSpace).trim();
		
		Member m = getMemberArgument(user, g);
		if(m == null) {
			tc.sendMessage("ERROR: GiveRole action, user \"" + user + "\" is not in the server or doesn't exist!").queue();
			return;
		}
		
		List<Role> roles = g.getRolesByName(role, true);
		if(roles.isEmpty()) {
			tc.sendMessage("ERROR: GiveRole action, role \"" + role + "\" doesn't exist!").queue();
			return;
		}
		
		g.addRoleToMember(m, roles.get(0)).reason("From CustomCommand. Executed by " + performer.toString()).queue();
		return;
	}
	void intern_removeRole(String data, TextChannel tc, User performer) {
		Guild g = tc.getGuild();
		int firstSpace = data.indexOf(' ');
		if(firstSpace == -1) {
			tc.sendMessage("ERROR: Not enough arguments in the RemoveRole action.").queue();
			return;
		}
		String role = data.substring(firstSpace).trim();
		String user = data.substring(0, firstSpace).trim();
		
		Member m = getMemberArgument(user, g);
		if(m == null) {
			tc.sendMessage("ERROR: RemoveRole action, user \"" + user + "\" is not in the server or doesn't exist!").queue();
			return;
		}
		
		List<Role> roles = g.getRolesByName(role, true);
		if(roles.isEmpty()) {
			tc.sendMessage("ERROR: RemoveRole action, role \"" + role + "\" doesn't exist!").queue();
			return;
		}
		
		g.removeRoleFromMember(m, roles.get(0)).reason("From CustomCommand. Executed by " + performer.toString()).queue();
		return;
	}
	void intern_kick(String data, TextChannel tc, User performer) {
		Guild g = tc.getGuild();
		int firstSpace = data.indexOf(' ');
		if(firstSpace == -1) {
			tc.sendMessage("ERROR: Not enough arguments in the Kick action.").queue();
			return;
		}
		
		Member m = getMemberArgument(data, g);
		if(m == null) {
			tc.sendMessage("ERROR: Kick action, user \"" + data + "\" is not in the server or doesn't exist!").queue();
			return;
		}
		g.kick(m).reason("From CustomCommand. Executed by " + performer.toString()).queue();
	}
	void intern_ban(String data, TextChannel tc, User performer) {
		Guild g = tc.getGuild();
		int firstSpace = data.indexOf(' ');
		if(firstSpace == -1) {
			tc.sendMessage("ERROR: Not enough arguments in the Ban action.").queue();
			return;
		}
		
		Member m = getMemberArgument(data, g);
		if(m == null) {
			tc.sendMessage("ERROR: Ban action, user \"" + data + "\" is not in the server or doesn't exist!").queue();
			return;
		}
		g.ban(m, 0).reason("From CustomCommand. Executed by " + performer.toString()).queue();
	}
	
	public String applyVariables(HashMap<String, String> args, User performer, String src) {
		String target = src.replaceAll("\\Q{user}\\E", performer.getId());
		for(Entry<String,String> ent: args.entrySet()) {
			String argName = ent.getKey();
			String argValue = ent.getValue();
			target = target.replaceAll("\\Q{" + argName + "}\\E", argValue);
		}
		return target;
	}
	public Member getMemberArgument(String arg, Guild g) {
		// 7ukecreat0r
		// 214183045278728202
		// <@214183045278728202>
		
		if(arg.startsWith("<@"))
			arg = arg.substring(2);
		if(arg.endsWith(">"))
			arg = arg.substring
			(0, arg.length()-1);
		
		// 7ukecreat0r
		// 214183045278728202
		
		try {
			long id = Long.parseLong(arg);
			return g.getMemberById(id);
		} catch(NumberFormatException nfe) {
			// 7ukecreat0r
			List<Member> mems = g.getMembersByName(arg, true);
			if(!mems.isEmpty())
				return mems.get(0);
			mems = g.getMembersByEffectiveName(arg, true);
			if(!mems.isEmpty())
				return mems.get(0);
			return null;
		}
	}
	@Override
	public String toString() {
		String pick = "";
		switch(type) {
		case Ban:
			pick = "Ban the user \"" + data + "\".";
			break;
		case Choose:
			pick = "Send a random message that can be \"" + data + "\".";
			break;
		case Embed:
			pick = "Send an embed with the data \"" + data + "\".";
			break;
		case GiveRole:
			pick = "Add the b.role to the a.user: \"" + data + "\"";
			break;
		case Kick:
			pick = "Kick the a.user \"" + data + "\".";
			break;
		case PlainText:
			pick = "Send the text \"" + data + "\".";
			break;
		case RemoveRole:
			pick = "Remove the b.role \"" + data + "\" from the a.user.";
			break;
		case SetNickname:
			pick = "Set the a.user's nickname to b: \"" + data + "\".";
			break;
		case _unassigned:
			pick = "Unassigned Action?";
			break;
		default:
			pick = "Bugged case found. Please report this with the report command!";
			break;
		}
		return pick;
	}
}
