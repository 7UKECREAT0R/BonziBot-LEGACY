package com.lukecreator.BonziBot.Scripter;

import java.io.Serializable;

public class CustomScriptArgument implements Serializable {
	
	private static final long serialVersionUID = 1L;

	public enum ArgType {
		INTEGER, NUMBER, TEXT, TRUEFFALSE, PING
	}
	
	public ArgType type;
	public String name;
	
	public CustomScriptArgument(ArgType type, String name) {
		this.type = type;
		this.name = name;
	}
	
	public boolean testType(String test) {
		if(type == ArgType.TEXT) {
			return true;
		}
		if(type == ArgType.INTEGER) {
			try {
				Integer.parseInt(test);
			} catch(Exception exc) {
				return false;
			}
			return true;
		}
		if(type == ArgType.NUMBER) {
			try {
				Double.parseDouble(test);
			} catch(Exception exc) {
				return false;
			}
			return true;
		}
		if(type == ArgType.TRUEFFALSE) {
			try {
				Boolean.parseBoolean(test);
			} catch(Exception exc) {
				return false;
			}
			return true;
		}
		if(type == ArgType.PING) {
			return test.matches("/^<@!?(\\d+)>$/");
		}
		return false;
	}
}
