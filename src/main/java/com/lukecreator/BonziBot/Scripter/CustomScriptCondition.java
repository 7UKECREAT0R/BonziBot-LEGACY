package com.lukecreator.BonziBot.Scripter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.lukecreator.BonziBot.App;

public class CustomScriptCondition  implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	public String commandName; // The first argument.
	public List<CustomScriptArgument> args; // Remaining arguments.
	
	public CustomScriptCondition() {
		this.commandName = null;
		args = new ArrayList<CustomScriptArgument>();
	}
	public CustomScriptCondition addArgument(CustomScriptArgument arg) {
		args.add(arg);
		return this;
	}
	public void removeArgument(String name) {
		int fNum = -1;
		int i = -1;
		for(CustomScriptArgument iter: args) {
			i++;
			if(iter.name.equalsIgnoreCase(name)) {
				fNum = i;
				break;
			}
		}
		if(fNum == -1) {
			return;
		}
		args.remove(fNum);
	}
	public boolean argumentExists(String name) {
		for(CustomScriptArgument arg: args) {
			if(arg.name.equalsIgnoreCase(name)) {
				return true;
			}
		}
		return false;
	}
	public CustomScriptCondition setCommandName(String name) {
		commandName = name;
		return this;
	}
	public int getArgumentCount() {
		return args.size();
	}
	
	// Solve related.
	public boolean checkString(String target, App appInstance, long guildID) {
		CSCTuple commandData = parseCommand(target);
		
		System.out.println("Input command: " + commandData.cmd);
		
		return checkCommandName(commandData, appInstance, guildID)
				&& checkCommandArgs(commandData);
	}
	public boolean isBadArguments(String target, App appInstance, long guildID) {
		CSCTuple commandData = parseCommand(target);
		return !checkCommandArgs(commandData);
	}
	
	public CSCTuple parseCommand(String command) {
		String[] allArgs = command.split("\\s+");
		if(allArgs.length <= 0) {
			return new CSCTuple(null, null);
		}
		// We know it's at least 1 argument.
		String cmd = allArgs[0];
		
		if(allArgs.length <= 1) {
			return new CSCTuple(cmd, new String[0]);
		}
		
		// args >= 1
		String[] populate = new String[allArgs.length-1];
		for(int i = 0; i < populate.length; i++) {
			String temp = allArgs[i+1];
			populate[i] = temp;
		}
		
		return new CSCTuple(cmd, populate);
	}
	private boolean checkCommandName(CSCTuple data, App app, long gid) {
		
		String prefix;
		if(!app.prefixes.containsKey(gid)) {
			// This should never be called under any circumstance.
			app.prefixes.put(gid, App.DEFAULT_PREFIX);
			prefix = App.DEFAULT_PREFIX;
		} else {
			prefix = app.prefixes.get(gid);
		}
		
		if(data.cmd == null)
			return false;
		System.out.println("Comparing to: " + prefix + commandName);
		if(data.cmd.equalsIgnoreCase(prefix + commandName)){
			System.out.println("Good comparison." + prefix + commandName);
			return true;
		} else {
			System.out.println("Bad comparison." + prefix + commandName);
		}
		return false;
	}
	private boolean checkCommandArgs(CSCTuple data) {
		String[] userArgs = data.other;
		
		if(userArgs.length < args.size())
			return false;
		
		for(int i = 0; i < args.size(); i++) {
			CustomScriptArgument current = args.get(i);
			String compare = userArgs[i]; // Should never throw...
			
			if(!current.testType(compare)) {
				return false;
			}
		}
		return true;
	}
	
	@Override
	public String toString() {
		if(commandName == null) {
			return "None.";
		}
		StringBuilder sb = new StringBuilder();
		sb.append(commandName);
		for(CustomScriptArgument arg: args) {
			sb.append(" <" + arg.name + ">");
		}
		return sb.toString();
	}
}

// For "parseCommand(String command)"
class CSCTuple {
	public String cmd;
	public String[] other;
	
	public CSCTuple(String cmd, String[] other) {
		this.cmd = cmd;
		this.other = other;
	}
}