package com.lukecreator.BonziBot.Scripter;

import java.io.Serializable;

public class CustomScript implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	public String name;
	public CustomScriptCondition condition;
	public CustomScriptResult result;
	public CustomScriptSettings settings;
	
	public CustomScript() {
		condition = null;
		result = null;
		settings = new CustomScriptSettings();
	}
	
	public CustomScript setCondition(CustomScriptCondition condition) {
		this.condition = condition;
		return this;
	}
	public CustomScript setResult(CustomScriptResult result) {
		this.result = result;
		return this;
	}
	
	public String getConditionString() {
		return condition.toString();
	}
}