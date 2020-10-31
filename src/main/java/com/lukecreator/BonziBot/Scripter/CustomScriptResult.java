package com.lukecreator.BonziBot.Scripter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

public class CustomScriptResult implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	List<CustomScriptAction> actions = new ArrayList<CustomScriptAction>();
	
	public CustomScriptAction getActionByName(String name) {
		for(CustomScriptAction act: actions) {
			if(act.name.equalsIgnoreCase(name)) {
				return act;
			}
		}
		return null;
	}
	
	public int getActionIndexByName(String name) {
		int i = -1;
		for(CustomScriptAction act: actions) {
			i++;
			if(act.name.equalsIgnoreCase(name)) {
				return i;
			}
		}
		return -1;
	}
	public int getActionCount() {
		return actions.size();
	}
	public CustomScriptAction get(int index) {
		return actions.get(index);
	}
	public CustomScriptAction addAction(CustomScriptAction act) {
		actions.add(act);
		return act;
	}
	public void removeAction(String name) {
		CustomScriptAction target = null;
		for(CustomScriptAction act: actions) {
			if(act.name.equalsIgnoreCase(name)) {
				target = act;
			}
		}
		if(target == null) return;
		actions.remove(target);
	}
	public void replaceAction(CustomScriptAction a, CustomScriptAction b) {
		int i = getActionIndexByName(a.name);
		if(i == -1) return;
		actions.set(i, b);
		return;
	}

	public void perform(CustomScript parent, String text, TextChannel channel, User performer) {
		CSCTuple parsed = parent.condition.parseCommand(text);
		String[] args = parsed.other;
		HashMap<String, String> argsParsed =
				new HashMap<String, String>();
		CustomScriptArgument[] csa = (CustomScriptArgument[])
				parent.condition.args.toArray(new CustomScriptArgument[0]);
		for(int i = 0; i < args.length; i++) {
			String arg = args[i];
			String name = csa[i].name;
			argsParsed.put(name, arg);
		}
		
		for(CustomScriptAction action: actions) {
			action.perform(argsParsed, performer, channel);
		}
	}

	@Override
	public String toString() {
		return "Result with (" + getActionCount() + ") actions.";
	}
}