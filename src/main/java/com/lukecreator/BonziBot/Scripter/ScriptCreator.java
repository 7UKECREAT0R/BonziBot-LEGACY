package com.lukecreator.BonziBot.Scripter;

import java.awt.Color;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.lukecreator.BonziBot.Scripter.CustomScriptAction.CustomScriptActionType;
import com.lukecreator.BonziBot.Scripter.CustomScriptArgument.ArgType;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;

public class ScriptCreator implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	public enum CustomScriptPaginationState {
		Home,
		EditingScript,
		EditingCondition,
		EditingResult,
		
		HelpMenu,
		ShowMenu,
		DeleteMenu,
		EditMenu,
		NewScript,
	}
	public CustomScriptPaginationState state;
	
	public transient Member lastMember;
	
	public long guildID;
	public long mainMsgID;
	public List<CustomScript> scripts;
	
	// Editor State
	public int editingIndex = -1;
	
	public ScriptCreator(long guildID, long mainMsgID) {
		this.guildID = guildID;
		this.mainMsgID = mainMsgID;
		state = CustomScriptPaginationState.Home;
		scripts = new ArrayList<CustomScript>();
	}
	public EmbedBuilder getBaseEmbed() {
		EmbedBuilder eb = new EmbedBuilder();
		eb.setColor(lastMember.getColor());
		
		String name = lastMember.getEffectiveName();
		String iconUrl = lastMember.getUser()
				.getEffectiveAvatarUrl();
		
		eb.setAuthor(name, null, iconUrl);
		return eb;
	}
	public MessageEmbed getHelpEmbed() {
		EmbedBuilder eb = getBaseEmbed();
		eb.setTitle("Help Menu");
		eb.setDescription("A list of commands you can use in the Script Workspace.");
		
		eb.addField("help", "Shows this menu.", true);
		eb.addField("back", "Go back a menu.", true);
		eb.addField("show", "See all your scripts.", true);
		eb.addField("new", "Begin creating a new script!", true);
		eb.addField("delete", "Delete a script.", true);
		eb.addField("edit", "Edit a script.", true);
		return eb.build();
	}
	public MessageEmbed getHomeEmbed() {
		EmbedBuilder eb = getBaseEmbed();
		eb.setTitle("Home");
		eb.setDescription("Type 'show' to see all your scripts, or 'help' to see other commands.");
		return eb.build();
	}
	public MessageEmbed getShowEmbed() {
		EmbedBuilder eb = getBaseEmbed();
		eb.setTitle("All Scripts");
		eb.setDescription("A list of all the scripts you've made. Type 'back' to go back.");
		
		for(CustomScript script: scripts) {
			eb.addField(script.name,
				"Condition: " + script.condition.toString() +
				"\nResult: " + script.result.toString(), false);
		}
		return eb.build();
	}
	public MessageEmbed getNewScriptEmbed() {
		EmbedBuilder eb = getBaseEmbed();
		eb.setColor(Color.green);
		eb.setTitle("Making new script.");
		eb.setDescription("Type the name of the new script! (or type 'back' to cancel.)");
		return eb.build();
	}
	public MessageEmbed getDeleteScriptEmbed() {
		EmbedBuilder eb = getBaseEmbed();
		eb.setColor(Color.red);
		eb.setTitle("Deleting script...");
		eb.setDescription("Type the name of the script to delete. (or type 'back' to cancel.)");
		return eb.build();
	}
	public MessageEmbed getEditScriptEmbed() {
		EmbedBuilder eb = getBaseEmbed();
		eb.setColor(Color.yellow);
		eb.setTitle("Editing script...");
		eb.setDescription("Type the name of the script you want to edit. (or type 'back' to cancel.)");
		return eb.build();
	}
	public MessageEmbed getEditingScriptEmbed() {
		CustomScript editing = getEditing();
		EmbedBuilder eb = getBaseEmbed();
		eb.setTitle("Editing script.");
		if(editing == null) {
			eb.setDescription("It seems your editor is in a bugged state. No problem, just type 'back'!");
			return eb.build();
		}
		eb.setDescription("Script name: " + editing.name);
		eb.addField("Condition", editing.condition.toString(), false);
		eb.addField("Result", editing.result.toString(), false);
		eb.addBlankField(false);
		eb.addField(getEmojiForBoolean(editing.settings.modOnly) + " Mod Only", "Only let moderators use this command.\nType 'mod only' to toggle.", false);
		eb.addField(getEmojiForBoolean(editing.settings.boosterOnly) + " Upgrader Only", "Only let (bonzibot) upgraders use this command (via the upgrade command).\nType 'upgrader only' to toggle.", false);
		eb.setFooter("Type 'back', 'condition', 'result', 'mod only', or 'upgrader only'...");
		return eb.build();
	}
	public MessageEmbed getConditionEmbed() {
		CustomScript editing = getEditing();
		EmbedBuilder eb = getBaseEmbed();
		eb.setTitle("Editing script condition.");
		eb.setColor(Color.cyan);
		if(editing == null) {
			eb.setDescription("It seems your editor is in a bugged state. No problem, just type 'back'!");
			return eb.build();
		}
		eb.setDescription("Current Condition: " + editing.getConditionString());
		eb.addField("The condition is what people have to type to actually trigger the command!",
				"Type one of the commands below to create your condition.\n\n"
				+ "Valid argument types are: Integer, Number, Text, TrueFalse, Ping", false);
		eb.addBlankField(false);
		eb.addField("setkeyword <name>", "Set the first word of the command. The finished command would be something like \"b:name <arguments>\"", false);
		eb.addBlankField(false);
		eb.addField("add <argument type> <argument name>", "Creates a new argument in your command with a name and type.", false);
		eb.addField("remove <argument name>", "Removes an argument with the specified name.", false);
		eb.setFooter("Type 'back' to return.");
		return eb.build();
	}
	public MessageEmbed getResultEmbed() {
		CustomScript editing = getEditing();
		EmbedBuilder eb = getBaseEmbed();
		eb.setTitle("Editing script result.");
		eb.setColor(Color.pink);
		if(editing == null) {
			eb.setDescription("It seems your editor is in a bugged state. No problem, just type 'back'!");
			return eb.build();
		}
		eb.setDescription("Current Actions:");
		int iter = 0;
		for(CustomScriptAction action: editing.result.actions) {
			iter++;
			eb.appendDescription("\n\"" + action.name + "\", " + iter + ". " + action.toString());
		}
		
		eb.addField("Actions state what happens when someone runs your command!",
				  "delete <name> - Deletes the specified action.\n"
				+ "create <name> <type> <contents>", false);
		eb.addField("Basic Actions:",
				"PlainText - Simply responds with the contents.\n"
				+ "Choose - Respond randomly between a few options. Separated by commas.\n"
				+ "Embed - Respond with an embed. Format: <color>Title1;Desc1;Title2;Desc2;etc..\n", false);
		eb.addField("Complex Actions:",
				"SetNickname - Sets a user's nickname. Format: <username> <nickname>\n"
				+ "GiveRole - Give a user a role. Format: <username> <role name>\n"
				+ "RemoveRole - Remove a user's a role. Format: <username> <role name>\n"
				+ "Ban - Bans a user. Just specify the user's name.\n"
				+ "Kick - Kicks a user. Just specify the user's name.", false);
		eb.addBlankField(false);
		eb.addField("Variables", "Variables will be replaced with something different when the command is run. here are the ones you can use:", false);
		
		StringBuilder desc = new StringBuilder();
		desc.append("{user} - The user that ran the command.\n");
		for(CustomScriptArgument arg: editing.condition.args) {
			desc.append("{" + arg.name + "} - The command argument \"" + arg.name + "\".\n");
		}
		desc = desc.deleteCharAt(desc.length()-1);
		eb.addField("Available Variables:", desc.toString(), false);
		
		eb.setFooter("Enter a command or type 'back'.");
		return eb.build();
	}
	
	public void resetMenu() {
		state = CustomScriptPaginationState.Home;
	}
	public void setTopMessage(MessageEmbed eb, TextChannel tc) {
		tc.retrieveMessageById(mainMsgID).queue(msg -> {
			msg.editMessage(eb).queue();
		}, fail -> {
			String err = fail.getMessage();
			if(err.contains("Unknown Message")) {
				tc.sendMessage(eb).queue(newmsg -> {
					mainMsgID = newmsg.getIdLong();
				});
			}
		});
	}
	public void setTopMessageReactions(TextChannel tc, String...reactions) {
		tc.clearReactionsById(mainMsgID).queue();
		if(reactions.length <= 0) return;
		for(String reaction: reactions) {
			tc.addReactionById(mainMsgID, reaction).queue();
		}
	}
	public CustomScript getEditing() {
		if(editingIndex == -1)
			return null;
		return scripts.get(editingIndex);
	}
	public void replaceEditing(CustomScript newScript) {
		if(newScript == null) return;
		scripts.set(editingIndex, newScript);
	}
	
	public void receiveActionText(String text, TextChannel tc) {
		if(text.isEmpty())
			return;
		String[] args = text.split("\\s+");
		if(args.length <= 0)
			return;
		String cmd = args[0];
		
		if(cmd.equalsIgnoreCase("help")) {
			commandHelp(tc);
			return;
		}
		if(cmd.equalsIgnoreCase("back")) {
			commandBack(tc);
			return;
		}
		if(cmd.equalsIgnoreCase("show")) {
			commandShow(tc);
			return;
		}
		if(cmd.equalsIgnoreCase("new")) {
			commandNew(tc);
			return;
		}
		if(cmd.equalsIgnoreCase("delete")) {
			commandDelete(tc);
			return;
		}
		if(cmd.equalsIgnoreCase("edit")) {
			commandEdit(tc);
			return;
		}
		if(cmd.equalsIgnoreCase("condition")) {
			commandCondition(tc);
			return;
		}
		if(cmd.equalsIgnoreCase("result")) {
			commandResult(tc);
			return;
		}
		if(state == CustomScriptPaginationState.EditingScript) {
			if(text.equalsIgnoreCase("mod only")) {
				CustomScript cs = getEditing();
				cs.settings.modOnly = !cs.settings.modOnly;
				replaceEditing(cs);
				setTopMessage(getEditingScriptEmbed(), tc);
			}
			if(text.equalsIgnoreCase("upgrader only")) {
				CustomScript cs = getEditing();
				cs.settings.boosterOnly = !cs.settings.boosterOnly;
				replaceEditing(cs);
				setTopMessage(getEditingScriptEmbed(), tc);
			}
		}
		if(state == CustomScriptPaginationState.NewScript) {
			paginateNewScriptName(tc, text);
			return;
		}
		if(state == CustomScriptPaginationState.DeleteMenu) {
			paginateDeleteScript(tc, text);
			return;
		}
		if(state == CustomScriptPaginationState.EditMenu) {
			paginateEditScript(tc, text);
			return;
		}
		if(state == CustomScriptPaginationState.EditingCondition) {
			paginateConditionCommand(tc, cmd, args);
			return;
		}
		if(state == CustomScriptPaginationState.EditingResult) {
			paginateResultCommand(tc, cmd, args);
			return;
		}
	}
	public void receiveActionReaction(String reaction, TextChannel tc) {
		
	}
	
	public void commandHelp(TextChannel tc) {
		System.out.println(state);
		if(state != CustomScriptPaginationState.Home)
			return;
		
		setTopMessage(getHelpEmbed(), tc);
		state = CustomScriptPaginationState.HelpMenu;
	}
	public void commandBack(TextChannel tc) {
		if(state == CustomScriptPaginationState.Home)
			return;
		
		if(state == CustomScriptPaginationState.ShowMenu
			|| state == CustomScriptPaginationState.DeleteMenu
			|| state == CustomScriptPaginationState.HelpMenu
			|| state == CustomScriptPaginationState.EditingScript
			|| state == CustomScriptPaginationState.NewScript
			|| state == CustomScriptPaginationState.EditMenu) {
			state = CustomScriptPaginationState.Home;
			setTopMessage(getHomeEmbed(), tc);
			editingIndex = -1;
		}
		if(state == CustomScriptPaginationState.EditingCondition
			|| state == CustomScriptPaginationState.EditingResult) {
			state = CustomScriptPaginationState.EditingScript;
			setTopMessage(getEditingScriptEmbed(), tc);
		}
	}
	public void commandShow(TextChannel tc) {
		if(state != CustomScriptPaginationState.Home)
			return;
		
		setTopMessage(getShowEmbed(), tc);
		state = CustomScriptPaginationState.ShowMenu;
	}
	public void commandNew(TextChannel tc) {
		if(state != CustomScriptPaginationState.Home)
			return;
		
		setTopMessage(getNewScriptEmbed(), tc);
		state = CustomScriptPaginationState.NewScript;
	}
	public void commandDelete(TextChannel tc) {
		if(state != CustomScriptPaginationState.Home)
			return;
		
		if(scripts.size() < 1) {
			tc.sendMessage(failureEmbed("No scripts to delete.")).queue(msg -> {
				msg.delete().queueAfter(3, TimeUnit.SECONDS);
			});
			return;
		}
		
		setTopMessage(getDeleteScriptEmbed(), tc);
		state = CustomScriptPaginationState.DeleteMenu;
	}
	public void commandEdit(TextChannel tc) {
		if(state != CustomScriptPaginationState.Home)
			return;
		
		if(scripts.size() < 1) {
			tc.sendMessage(failureEmbed("No scripts to edit.")).queue(msg -> {
				msg.delete().queueAfter(3, TimeUnit.SECONDS);
			});
			return;
		}
		
		setTopMessage(getEditScriptEmbed(), tc);
		state = CustomScriptPaginationState.EditMenu;
	}
	public void commandCondition(TextChannel tc) {
		if(state != CustomScriptPaginationState
		.EditingScript || editingIndex == -1) {
			return;
		}
		
		setTopMessage(getConditionEmbed(), tc);
		state = CustomScriptPaginationState.EditingCondition;
	}
	public void commandResult(TextChannel tc) {
		if(state != CustomScriptPaginationState
		.EditingScript || editingIndex == -1) {
			return;
		}
		
		setTopMessage(getResultEmbed(), tc);
		state = CustomScriptPaginationState.EditingResult;
	}
	
	public void paginateNewScriptName(TextChannel tc, String text) {
		if(text.contains(" ")) {
			tc.sendMessage(failureEmbed("No spaces can be in the script name.")).queue(msg -> {
				msg.delete().queueAfter(3, TimeUnit.SECONDS);
			});
			return;
		}
		CustomScript cs = createDefaultScript();
		cs.name = text;
		scripts.add(cs);
		editingIndex = scripts.size()-1;
		state = CustomScriptPaginationState.EditingScript;
		setTopMessage(getEditingScriptEmbed(), tc);
	}
	public void paginateDeleteScript(TextChannel tc, String text) {
		for(int i = 0; i < scripts.size(); i++) {
			CustomScript script = scripts.get(i);
			
			if(script.name != null && text
			.equalsIgnoreCase(script.name)) {
				scripts.remove(i);
				tc.sendMessage(successEmbed("Successfully deleted \"" + script.name + "\".")).queue(msg -> {
					msg.delete().queueAfter(5, TimeUnit.SECONDS);
				});
				editingIndex = -1;
				state = CustomScriptPaginationState.Home;
				setTopMessage(getHomeEmbed(), tc);
				return;
			}
		}
		tc.sendMessage(failureEmbed("Can't find a script with that name.")).queue(msg -> {
			msg.delete().queueAfter(3, TimeUnit.SECONDS);
		});
		return;
	}
	public void paginateEditScript(TextChannel tc, String text) {
		for(int i = 0; i < scripts.size(); i++) {
			CustomScript script = scripts.get(i);
			
			if(script.name != null && text
			.equalsIgnoreCase(script.name)) {
				editingIndex = i;
				state = CustomScriptPaginationState.EditingScript;
				setTopMessage(getEditingScriptEmbed(), tc);
				return;
			}
		}
		tc.sendMessage(failureEmbed("Can't find a script with that name.")).queue(msg -> {
			msg.delete().queueAfter(3, TimeUnit.SECONDS);
		});
		return;
	}
	
	public void paginateConditionCommand(TextChannel tc, String cmd, String[] args) {
		if(cmd.equalsIgnoreCase("setkeyword")) {
			if(args.length < 2) {
				tc.sendMessage(failureEmbed("Incorrect syntax.")).queue(msg -> {
					msg.delete().queueAfter(3, TimeUnit.SECONDS);
				});
				return;
			}
			String arg = args[1];
			CustomScript edit = getEditing();
			edit.condition.setCommandName(arg);
			replaceEditing(edit);
			
			setTopMessage(getConditionEmbed(), tc);
			tc.sendMessage(successEmbed("Set the command keyword to \"" + arg + "\".")).queue(msg -> {
				msg.delete().queueAfter(5, TimeUnit.SECONDS);
			});
			return;
		}
		if(cmd.equalsIgnoreCase("add")) {
			if(args.length < 3) {
				tc.sendMessage(failureEmbed("Incorrect syntax.")).queue(msg -> {
					msg.delete().queueAfter(3, TimeUnit.SECONDS);
				});
				return;
			}
			String _type = args[1].toUpperCase();
			String name = args[2];
			ArgType type;
			try {
				type = ArgType.valueOf(ArgType.class, _type);
			} catch(IllegalArgumentException exc) {
				tc.sendMessage(failureEmbed("Not a valid argument type.")).queue(msg -> {
					msg.delete().queueAfter(3, TimeUnit.SECONDS);
				});
				return;
			}
			CustomScript edit = getEditing();
			CustomScriptArgument arg;
			arg = new CustomScriptArgument(type, name);
			edit.condition.addArgument(arg);
			replaceEditing(edit);
			
			setTopMessage(getConditionEmbed(), tc);
			tc.sendMessage(successEmbed("Added the new argument.")).queue(msg -> {
				msg.delete().queueAfter(5, TimeUnit.SECONDS);
			});
			return;
		}
		if(cmd.equalsIgnoreCase("remove")) {
			if(args.length < 2) {
				tc.sendMessage(failureEmbed("Incorrect syntax.")).queue(msg -> {
					msg.delete().queueAfter(3, TimeUnit.SECONDS);
				});
				return;
			}
			String name = args[1];
			CustomScript edit = getEditing();
			if(!edit.condition.argumentExists(name)) {
				tc.sendMessage(failureEmbed("No argument has that name.")).queue(msg -> {
					msg.delete().queueAfter(3, TimeUnit.SECONDS);
				});
				return;
			}
			edit.condition.removeArgument(name);
			replaceEditing(edit);
			
			setTopMessage(getConditionEmbed(), tc);
			tc.sendMessage(successEmbed("Deleted the argument.")).queue(msg -> {
				msg.delete().queueAfter(5, TimeUnit.SECONDS);
			});
			return;
		}
	}
	public void paginateResultCommand(TextChannel tc, String cmd, String[] args) {
		CustomScript editing = getEditing();
		CustomScriptResult res = editing.result;
		if(cmd.equalsIgnoreCase("delete")) {
			String remainder = String.join(" ", args);
			res.removeAction(remainder);
			editing.result = res;
			
			replaceEditing(editing);
			setTopMessage(getResultEmbed(), tc);
			return;
		}
		if(cmd.equalsIgnoreCase("create")) {
			if(args.length < 4) {
				tc.sendMessage(failureEmbed("Not enough arguments specified!")).queue(mm -> {
					mm.delete().queueAfter(3, TimeUnit.SECONDS);
				});
				return;
			}
			String name = args[1];
			
			String _type = args[2];
			CustomScriptAction.CustomScriptActionType type = 
					CustomScriptActionType._unassigned;
			for(CustomScriptAction.CustomScriptActionType
					testType: CustomScriptAction.getDisplayTypes()) {
				System.out.println(_type + " vs. " + testType.name());
				if(_type.equalsIgnoreCase(testType.name())) {
					type = testType;
					break;
				}
			}
			if(type == CustomScriptActionType._unassigned) {
				tc.sendMessage(failureEmbed("Not a valid type!")).queue(mm -> {
					mm.delete().queueAfter(3, TimeUnit.SECONDS);
				});
				return;
			}
			
			List<String> _contents = new ArrayList<String>();
			for(int i = 0; i < args.length; i++) {
				if(i < 3) continue;
				_contents.add(args[i]);
			}
			String contents = String.join(" ", _contents);
			
			CustomScriptAction action = new CustomScriptAction();
			action.type = type;
			action.name = name;
			action.data = contents;
			res.addAction(action);
			editing.result = res;
			
			replaceEditing(editing);
			setTopMessage(getResultEmbed(), tc);
			return;
		}
	}
	
	public MessageEmbed failureEmbed(String text) {
		EmbedBuilder eb = new EmbedBuilder();
		eb.setColor(Color.red);
		eb.setTitle(text);
		return eb.build();
	}
	public MessageEmbed successEmbed(String text) {
		EmbedBuilder eb = new EmbedBuilder();
		eb.setColor(Color.green);
		eb.setTitle(text);
		return eb.build();
	}
	public CustomScript createDefaultScript() {
		CustomScript scr = new CustomScript();
		scr.setCondition(new CustomScriptCondition());
		scr.setResult(new CustomScriptResult());
		return scr;
	}
	public String getEmojiForBoolean(boolean b) {
		if(b)
			return "✅";
		else
			return "🔳";
	}
}

