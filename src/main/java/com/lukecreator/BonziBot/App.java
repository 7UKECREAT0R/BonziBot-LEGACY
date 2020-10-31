package com.lukecreator.BonziBot;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.time.Instant;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.security.auth.login.LoginException;
import javax.sound.sampled.AudioFileFormat;

import com.github.jreddit.entity.Submission;
import com.github.jreddit.exception.RetrievalFailedException;
import com.github.jreddit.retrieval.Submissions;
import com.github.jreddit.retrieval.params.SubmissionSort;
import com.github.jreddit.utils.restclient.HttpRestClient;
import com .github.jreddit.utils.restclient.RestClient;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.YouTube.Search;
import com.google.api.services.youtube.YouTube.Videos;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoListResponse;
import com.lukecreator.BonziBot.CustomCommandManager.CCM_ReturnCode;
import com.lukecreator.BonziBot.LoggingEntry.LogType;
import com.lukecreator.BonziBot.RpgClasses.RpgInventory;
import com.lukecreator.BonziBot.RpgClasses.RpgItem;
import com.lukecreator.BonziBot.RpgClasses.RpgItem.RpgItemType;
import com.lukecreator.BonziBot.RpgClasses.RpgPlayer;
import com.lukecreator.BonziBot.Scripter.CustomScript;
import com.lukecreator.BonziBot.Scripter.ScriptCreator;
import com.lukecreator.BonziBot.Web.InternalStat;
import com.lukecreator.BonziBot.Web.Stat;
import com.lukecreator.BonziBot.Web.StatGame;
import com.lukecreator.BonziBot.Web.SteamAPI;
import com.pi4j.system.SystemInfo;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;

import com.sun.speech.freetts.FreeTTS;
import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;
import com.sun.speech.freetts.audio.SingleFileAudioPlayer;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.channel.text.TextChannelCreateEvent;
import net.dv8tion.jda.api.events.channel.text.TextChannelDeleteEvent;
import net.dv8tion.jda.api.events.channel.voice.VoiceChannelCreateEvent;
import net.dv8tion.jda.api.events.channel.voice.VoiceChannelDeleteEvent;
import net.dv8tion.jda.api.events.guild.GuildBanEvent;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildUnbanEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.events.guild.member.update.GuildMemberUpdateNicknameEvent;
import net.dv8tion.jda.api.events.guild.voice.GenericGuildVoiceEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageDeleteEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionRemoveEvent;
import net.dv8tion.jda.api.exceptions.HierarchyException;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.AudioManager;
import net.dv8tion.jda.api.managers.ChannelManager;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

public class App extends ListenerAdapter
{
	public enum BotMode {
		Test,
		Official
	}
	// FEEL FREE TO ADD YOUR OWN ITEMS, BUT TAKE UPMOST CARE TO KEEP PREMIUM AT THE BOTTOM!
	public enum PremiumCommand {
		Nickall, // <Server Owner Only> Nicknames every user on the discord. Only server owners can use this command!
		RainbowRole, // <Server Owner Only> Usage: b:rainbowrole @role. Makes the targeted role rainbow!
		SuperPlay, // Puts your song at the front of the queue!
		Expose, // Exposes the last deleted message!
		Profilepic, // Gets the user's profile picture.
		Troll, // Troll a user with a random troll.
		Comment,
		
		Premium // Gives all special commands in a single package + all bonzibot premium perks. Can be bought with irl money, or coins.
	}
	// FEEL FREE TO ADD YOUR OWN ITEMS, BUT TAKE UPMOST CARE TO KEEP PREMIUM AT THE BOTTOM!
	public String[] premiumDescriptions = {
		"<Server Owner Only> Nicknames every user on the discord.",
		"<Server Owner Only> Makes the targeted role rainbow!",
		"Puts your song at the front of the queue!",
		"Exposes the last deleted message!",
		"Posts the selected user's profile picture!",
		"Troll your friends with a random gag! Tons of possibilities!",
		"Posts the most generic youtube comment possible relating to the video (or not)!",
		
		"Gives all special commands in a single package!"
	};
	// FEEL FREE TO ADD YOUR OWN ITEMS, BUT TAKE UPMOST CARE TO KEEP PREMIUM AT THE BOTTOM!
	public int[] premiumPrices = {
			2000, // Nickall
			5000, // RainbowRole
			3000, // SuperPlay
			500, // Expose
			500, // Profilepic
			10000, // Troll
			1500, // Comment
			
			0, // Premium (20% savings!) [this is set automatically]
	};
	// FEEL FREE TO ADD YOUR OWN ITEMS, BUT TAKE UPMOST CARE TO KEEP PREMIUM AT THE BOTTOM!
	public String[] premiumNames = {
		"🖋️ Nickall",
		"🌈 RainbowRole",
		"⏩ SuperPlay",
		"😮 Expose",
		"🖼️ Profile Pic",
		"🤣 Troll",
		"💬 Comment",
		
		"🏆 Premium"
	};
	public String[] premiumSyntax = {
			"nickall <nickname>",
			"rainbowrole @<role>\nrainbowrole none",
			"superplay <url/song name>",
			"expose",
			"profilepic @<user>",
			"troll @<user>",
			"comment <youtube url>",
			
			"You shouldn't be seeing this.... Report how you did this with the report command plz!"
	};
	public HashMap<Long, List<PremiumCommand>> premiumAccounts = new HashMap<Long, List<PremiumCommand>>();
	
	static BotMode RUNTESTBOT = BotMode.Official; // CHANGE THIS TO SWITCH BETWEEN BOTS
	
	// YOUTUBE API

	public JsonFactory jsFactory = JacksonFactory.getDefaultInstance();
	public YouTube yt;
	YouTube getService() throws GeneralSecurityException, IOException {
		final NetHttpTransport httpTransport = 
			GoogleNetHttpTransport.newTrustedTransport();
		return new YouTube.Builder
			(httpTransport, jsFactory, null)
			.setApplicationName(Private.YTAPI_APP_NAME)
			.build();
	}
		
	// STEAM API
	public SteamAPI steam = new SteamAPI();
	
	public RestClient restClient = new HttpRestClient();
	public CustomCommandManager ccm = new CustomCommandManager();
	
	// PI API
	//public final GpioController gpio = GpioFactory.getInstance();
	
	private AudioPlayerManager playerManager;
	private Map<Long, GuildMusicManager> musicManagers;
	
	// Rpg Stuff
	static HashMap<Long, RpgPlayer> rpgPlayers = new HashMap<Long, RpgPlayer>();
	static List<RpgItem> rpgItems = new ArrayList<RpgItem>();
	
	// Note: These are only public so that we can debug them using the realtime compiler.
	public HashMap<Long, ScriptCreator> scriptCreators = new HashMap<Long, ScriptCreator>();
	public HashMap<Long, String> prefixes = new HashMap<Long, String>();
	public HashMap<Long, Long> modRoles = new HashMap<Long, Long>();
	public HashMap<Long, Long> joinRoles = new HashMap<Long, Long>();
	
	public HashMap<Long, String> joinMessages = new HashMap<Long, String>();
	public HashMap<Long, String> leaveMessages = new HashMap<Long, String>();
	public HashMap<Long, Long> joinLeaveChannels = new HashMap<Long, Long>();
	
	public HashMap<Long, List<String>> filteredWords = new HashMap<Long, List<String>>();
	public HashMap<Long, List<ReactionRole>> reactionRoles = new HashMap<Long, List<ReactionRole>>();
	
	public HashMap<Long, ServerBackup> backups = new HashMap<Long, ServerBackup>();
	public HashMap<Long, HashMap<Long, UserProfile>> profiles = new HashMap<Long, HashMap<Long, UserProfile>>();
	
	public HashMap<Long, String> CCResponseQueue = new HashMap<Long, String>(); // Public Response Queue
	public HashMap<Long, String> _CCResponseQueue = new HashMap<Long, String>(); // Private Response Queue
	
	// All fun Guild-Specific hashmaps.
	public HashMap<Long, Integer> countingGames = new HashMap<Long, Integer>();
	public HashMap<Long, HashMap<Long, Message>> deleteCache = new HashMap<Long, HashMap<Long, Message>>();
	public HashMap<Long, List<Message>> cache = new HashMap<Long, List<Message>>();
	public HashMap<Long, Long> nickallCD = new HashMap<Long, Long>();
	public HashMap<Long, Long> rainbowRoles = new HashMap<Long, Long>();
	public HashMap<Long, Message> exposed = new HashMap<Long, Message>();
	
	public HashMap<Long, Integer> upgrades = new HashMap<Long, Integer>();
	public HashMap<Long, List<Long>> upgraders = new HashMap<Long, List<Long>>();
	public List<Long> acceptedNSFW = new ArrayList<Long>();
	int cacheLimit = 250;
	
	// Note: This is <USER ID, Integer>.
	public HashMap<Long, Integer> xp = new HashMap<Long, Integer>();
	public HashMap<Long, Integer> coins = new HashMap<Long, Integer>();
	public static List<Playlist> playlists = new ArrayList<Playlist>(); // Static so that the Playlist class can use it for ID generation.
	
	// Guild ID, InputStream
	HashMap<Long, InputStream> streams = new HashMap<Long, InputStream>();
	
	public List<Long> ccBans = new ArrayList<Long>();
	public HashMap<Long, Long> ideaCooldownTimers = new HashMap<Long, Long>();
	
	public List<LoggingEntry> logs = new ArrayList<LoggingEntry>();
	public List<Long> dontLog = new ArrayList<Long>();
	
	public static int lottery = 0;
	
	public static List<Long> loop = new ArrayList<Long>();
	public static List<Long> queueloop = new ArrayList<Long>();
	public static List<Long> shuffle = new ArrayList<Long>();
	
	static List<String> jokes = new ArrayList<String>();
	static List<String> defaultswears = new ArrayList<String>();
	static JDA bot = null;
	public static final Looper<String> motds = new Looper<String>(
			"Forgot your prefix? Type \"b:stuck\"!",
			"Type \"b:invite\" to get BonziBot on your server, too!",
			"Check out the \"stats\" command!",
			"Want to add more epicness to your server? Check out the \"modifiers\" command!",
			"Got an epic idea? Tell us by using the \"idea\" command!",
			"Found a bug in BonziBot or have an issue? Let us know with the \"report\" command.",
			"Check out the \"playlist\" command! It lets you make custom playlists to share with the world!");
	public static HashMap<String, String> funMessages = new HashMap<String, String>();
	// ${year}, ${nextyear}, ${yearuploaded}, ${timestamp}, ${title}, ${channel}, ${phrase}, ${object}, ${videomilestone}, ${length}
	public static final String[] YT_USERNAMES = ("debatedrizzly\r\n" + 
		"tutucrank\r\n" + 
		"debateblinks\r\n" + 
		"violatorfactsheet\r\n" + 
		"corkydeliver\r\n" + 
		"skoardsapron\r\n" + 
		"unfittedamuck\r\n" + 
		"densityviolin\r\n" + 
		"bluddingsixties\r\n" + 
		"brokernoe\r\n" + 
		"stifflyherring\r\n" + 
		"adagioincrease\r\n" + 
		"totalscaley\r\n" + 
		"arcticyoghurt\r\n" + 
		"waiteastbound\r\n" + 
		"skaterssecular\r\n" + 
		"backstabbuck\r\n" + 
		"breechescabdriver\r\n" + 
		"smirktraffic\r\n" + 
		"victorialitter\r\n" + 
		"fitelist\r\n" + 
		"slammedwrathful\r\n" + 
		"conetungadal\r\n" + 
		"wakingcorrect\r\n" + 
		"shrapnelscallastle\r\n" + 
		"apricotsnimble\r\n" + 
		"focketsfooter\r\n" + 
		"squeakpicasso\r\n" + 
		"offbeaterrant\r\n" + 
		"wimpingscallion\r\n" + 
		"augustdocument\r\n" + 
		"achievegangway\r\n" + 
		"blueabove\r\n" + 
		"distortedastonished\r\n" + 
		"dreamermatter\r\n" + 
		"cashwear\r\n" + 
		"phasebrushes\r\n" + 
		"catniparc\r\n" + 
		"zygomaticthriving\r\n" + 
		"rallymoyai\r\n" + 
		"doorreroute\r\n" + 
		"tackingfidgety\r\n" + 
		"smarecrimp\r\n" + 
		"sparkstatues\r\n" + 
		"puritymining\r\n" + 
		"radiohope\r\n" + 
		"sproiledgate\r\n" + 
		"frosteddiva\r\n" + 
		"greenbicyclist\r\n" +
		"tatshenshinisierra\r\n" + 
		"libyanpartner\r\n" + 
		"nonstopburgoo\r\n" + 
		"quicknessohm\r\n" + 
		"larrupedgorilla\r\n" + 
		"patchworkthroat\r\n" + 
		"hedgehogcure\r\n" + 
		"roguebadger\r\n" + 
		"swindlerflat\r\n" + 
		"lyingstrut\r\n" + 
		"luggagelouisiana\r\n" + 
		"pawkinstangle\r\n" + 
		"tritesifter\r\n" + 
		"vibratopusher\r\n" + 
		"tessbeach\r\n" + 
		"vimstevens\r\n" + 
		"heartytill\r\n" + 
		"causetopline\r\n" + 
		"creakleludicrous\r\n" + 
		"biddyogle\r\n" + 
		"electrolyteshore\r\n" + 
		"smakebusstop\r\n" + 
		"titaniumtagalong\r\n" + 
		"ciciheached\r\n" + 
		"unbeatenstrobe\r\n" + 
		"soldimage\r\n" + 
		"rifleenchilada\r\n" + 
		"marleyextremum\r\n" + 
		"mastaroshanna\r\n" + 
		"hickgrimy\r\n" + 
		"auditorynape\r\n" + 
		"buoyancycapable\r\n" + 
		"bridejoe\r\n" + 
		"failvertebrae\r\n" + 
		"larvasultan\r\n" + 
		"paymentsbelly\r\n" + 
		"confuciusabyss\r\n" + 
		"grophummus\r\n" + 
		"godlywrinkle\r\n" + 
		"sylvesterlanky\r\n" + 
		"parkourceans\r\n" + 
		"rivergrub\r\n" + 
		"candiddefense\r\n" + 
		"stewywidely\r\n" + 
		"stormtop\r\n" + 
		"closeyoda\r\n" + 
		"pirateolympia\r\n" + 
		"crabsdefault\r\n" + 
		"mercurytoothsome\r\n" + 
		"dessertscontempt" + 
		"giganticjingling").split("\r\n");
	public String getCommentUsername() {
		String base = YT_USERNAMES[random(YT_USERNAMES.length)];
		int nums = random(10000);
		return base + nums;
	}
	public static final String[] YT_COMMENT_PHRASES = {
			"bazinga",
			"zoinks",
			"bruh",
			"whoa",
			"holy cow",
			"uh oh",
			"oh no",
			"i peed my pants",
			"crap",
			"oops",
			"tomato sauce",
			"balls"
	};
	public String getCommentPhrase() {
		return YT_COMMENT_PHRASES[random(YT_COMMENT_PHRASES.length)];
	}
	public static final String[] YT_COMMENT_OBJECTS = {
			"UFO",
			"can of beans",
			"bird",
			"person",
			"poet",
			"signature",
			"hotel",
			"paper",
			"apple",
			"shirt",
			"tennis court",
			"garbage",
			"steak",
			"clothes",
			"airport",
			"technology",
			"insect",
			"potato",
			"phone",
			"highway",
			"art",
			"farmer",
			"cousin",
			"coffee",
			"bathroom",
			"child"
	};
	public String getCommentObject() {
		return YT_COMMENT_OBJECTS[random(YT_COMMENT_OBJECTS.length)];
	}
	public static final String[] YT_COMMENTS = {
			"Whoever is reading this, I hope you become rich and successful (And go chase your dreams)",
			"Don't press read more...\n\n\n**read more**",
			"How to trick an idiot:\n\n\n**read more**",
			"I edited this comment so you'll never know how I got these likes",
			"Like if you're watching in ${yearuploaded}",
			"Who else is watching in ${yearuploaded}?",
			"if your in the year ${nextyear} like this comment!!!",
			"This is how many times they said ${phrase}\n👇",
			"Why is this in my recommended?",
			"I watched this just to get it out of my recommended...",
			"Anybody else randomly get this in their recommended?",
			"Almost forgot to watch this today.",
			"Like this comment if you love ${channel}. Ignore if you hate them.",
			"like = i like ${channel}, comment = i love ${channel}, ignore = death",
			"I think youtube is drunk again, 3 views and 125 likes",
			"I laughed so hard at ${timestamp}",
			"i broke at ${timestamp} 😂😂",
			"Anybody else here before this video hits ${videomilestone} likes?",
			"I was here before this video hit ${videomilestone}",
			"Please check out my channel i make content like this!",
			"ʜᴇʏ ꜰʀɪᴇɴᴅ! ᴄᴏᴍᴇ ꜱᴇᴇ ᴍʏ ᴄʜᴀɴɴᴇʟ ꜰᴏʀ ᴘɪᴄᴛᴜʀᴇꜱ!",
			"Ｄｏｉｎｇ ａ ｇｉｖｅａｗａｙ， from ｏｆｆｉｃｉａｌ ｃｈａｎｎｅｌ： dkghj.3l4jkh2win-now3j.com",
			"If this comment gets 1k likes, I will jump off my roof, no joke.",
			"If this comment gets 1k likes, I will chug a bottle of hot sauce and upload it.",
			"${timestamp} fav part",
			"first",
			"First!!",
			"second!",
			"third comment!",
			"Why am I watching this?",
			"Roses are red, violets are blue, i love ${channel} and you do too. (like if I was right)",
			"99% of you won't see this comment...",
			"This comment section:\n70% like if you agree comments\n28% spam bots\n2% these comments",
			"Wiggle\n"
			+" Wiggle\n"
			+"  Wiggle\n"
			+"   Wiggle\n"
			+"    Wiggle\n"
			+"   Wiggle\n"
			+"  Wiggle\n"
			+  "Wiggle\n"
			+"Wiggle\n"
			+" Wiggle\n"
			+"  Wiggle\n"
			+"   Wiggle\n"
			+"    Wiggle\n"
			+"   Wiggle\n"
			+"  Wiggle\n"
			+  "Wiggle\n"
			+ "Wiggle\n"
			+" Wiggle\n"
			+"  Wiggle\n"
			+"   Wiggle\n"
			+"    Wiggle\n"
			+"   Wiggle\n"
			+"  Wiggle\n"
			+  "Wiggle\n",
			"Can you spot the the mistake?",
			"This video is gay.",
			"Unpopular opinion: this video is actually good.",
			"Unpopular opinion: they are really cringe...",
			"Nobody:\nNot even a single soul:\n${channel}: ${phrase}",
			"Nobody:\nNobody in the universe:\n${channel}: ${phrase}",
			"This is bob.\n😊\n👚\n👖\n👞\nHe is 0 years old. Every like this comment gets he gets 1 year older. How old will he get?",
			"Me: Why do you come here?\n${channel}: ${phrase}\nMe: oh ok",
			"50 views, 200 likes?!",
			"17 views, 100 likes?! mooom youtube is drunk again!",
			"Edit: 100 Likes?!\nEdit again: OMG 300 likes?!?!?!\nEDIT AGAIN: OMGGGG 1K LIKES???????",
			"Nobody:\nYoutube recommendations:",
			"When they said ${phrase}, I felt that.",
			"Notification squad, where you at?",
			"Make this like button turn blue\n👇"
	};
	public static final String[] YT_COMMENTS_MUSIC = {
			"Anybody else think this channel is super underrated?",
			"0:00 replay button!",
			"This is my favorite song ever omg!",
			"Who else came here to check the views?",
			"${timestamp} is the best part! like if you agree.",
			"${timestamp} fav part period.",
			"📁Documents\nL 📁${channel} Music\nL 📁Bad songs\nL ⚠ FOLDER IS EMPTY!",
			"How does this not have a billion views yet?!",
			"This song is so good my neighbors called the police!!!",
			"I blasted this song so loud the cops are at my door but i don't care!",
			"0% Drugs\n0% Girls\n0% Flexing\n100% Talent",
			"You didn't search for this video, did you?",
			"Welcome, person with good taste in music!",
			"tik tok ruined this song...",
			"only people before tik tok can like this comment 😼"
	};
	public static final String[] YT_COMMENTS_LONG = {
			"Why did I watch the whole thing?",
			"Did I really just watch this whole thing...",
			"Mom said 1 more video before bed...",
			"So my mom told me I could watch one more video...",
			"Report this video for nudity so YouTube staff have to look through the whole thing!",
			"See you in 5 years when this gets recommended randomly again...",
			"Doctor: You have ${length} left to live.\nMe:",
	};
	public static final String[] YT_COMMENTS_OLD = {
			"Who else is watching in ${year}?",
			"if your in the year ${nextyear} like this comment!!!",
			"Like if you're watching in ${yearuploaded}",
			"Only true OGs watch this video in ${year}",
			"this video brings me back to ${yearuploaded}.",
			"this video was my childhood!",
	};
	public static final String LOG_MODIFIER = "logging";
	public static final String SCRIPTEDITOR_MODIFIER = "bbscripteditor";
	public static final String DEFAULT_PREFIX = "b:";
	public static final String DEFAULT_JOIN = "**Welcome to (server), (user)**";
	public static final String DEFAULT_LEAVE = "**(user) just left (server)!**";
	
	public static final String ADDRESS_REGEX = "^\\d+\\s[A-z]+\\s[A-z]+"; // Matches 1234 Sesame St.
	public static final String SITE_REGEX = "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]"; // Matches "google.com", "https://www.google.com", etc...
	public static final String IP_REGEX = "(?=.*[^\\.]$)((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.?){4}"; // Matches 255.255.255.255
	public static final String COUNTING_DESCRIPTION = "(counting) A community counting effort! Use the countingleaderboard command to see the leaderboard, or type \"?\" to see the next number.";
	public static long[] admins = {
		415316679610859520l, // Giraffey
		214183045278728202l, // Luke
		206395494648381443l}; // Aaron
	public static final long discord = 529089349762023436l;
	
	public static com.sun.speech.freetts.audio.AudioPlayer ap;
	public static FreeTTS tts;
	public static final String bonziVoice = "";
	public static VoiceManager voiceManager;
	public static Voice kevin16;
	
	//public static Configuration recognitionConfig = new Configuration();
	//public static Context recognitionContext; // Left unassigned because it's initialized on start
	
	// Other
	public static long ignoreNickChange = 0l;
	public static List<StatGame> games = new ArrayList<StatGame>();
	
    public static void main( String[] args ) throws LoginException, InterruptedException, MalformedURLException, IOException
    {
    	JDABuilder jdab = JDABuilder.create(
    			GatewayIntent.GUILD_BANS,
    			GatewayIntent.GUILD_VOICE_STATES,
    			GatewayIntent.GUILD_MEMBERS,
    			GatewayIntent.GUILD_MESSAGES,
    			GatewayIntent.GUILD_MESSAGE_REACTIONS,
    			GatewayIntent.GUILD_EMOJIS
    			).disableCache(CacheFlag.ACTIVITY, CacheFlag.CLIENT_STATUS);
    	
    	String _token = Private.BOT_TOKEN;
    	if(RUNTESTBOT == BotMode.Test) {
    		_token = Private.BOT_TOKEN_TEST;
    	}
    	jdab.setToken(_token);
    	bot = jdab.build();
    	bot.addEventListener(new App());
    	bot.awaitReady();
    	bot.getPresence().setActivity
    		(Activity.playing("*yawn* just booted up!"));
    	
    	// RPG Items
    	rpgItems.add(new RpgItem("Rusting Daggers", "They're pretty worn... Let's use them to stab some people!", RpgItemType.Weapon, 4));
    	rpgItems.add(new RpgItem("Dull Sword", "It may be dulled out, but this thing packs a punch!", RpgItemType.Weapon, 6));
    	rpgItems.add(new RpgItem("Baseball Bat", "Ness called!", RpgItemType.Weapon, 5));
    	rpgItems.add(new RpgItem("Brass Knuckles", "These things are worse than I expected...", RpgItemType.Weapon, 2));
    	rpgItems.add(new RpgItem("Shovel", "I mean, you could use it for digging into your opponent!", RpgItemType.Weapon, 4));
    	rpgItems.add(new RpgItem("Mace", "Going straight back to medieval times!", RpgItemType.Weapon, 6));
    	rpgItems.add(new RpgItem("Caveman Club", "Ooga booga, beat enemy with stick!", RpgItemType.Weapon, 5));
    	rpgItems.add(new RpgItem("Door", "Close the door on their face. Does a surprising amount of damage!", RpgItemType.Weapon, 4));
    	rpgItems.add(new RpgItem("Literal Scythe", "Do I have to explain this one?", RpgItemType.Weapon, 7));
    	rpgItems.add(new RpgItem("Gun", "only problem is that it's an airsoft", RpgItemType.Weapon, 5));
    	
    	rpgItems.add(new RpgItem("Ninja Stars", "You have to know how to be a ninja to use these! (not really!)", RpgItemType.Single_Use_Weapon, 7));
    	rpgItems.add(new RpgItem("Throwing Axe", "I have nothing to say about this, you just throw it.", RpgItemType.Single_Use_Weapon, 9));
    	rpgItems.add(new RpgItem("Grenade", "The epic explosion machine!", RpgItemType.Single_Use_Weapon, 13));
    	rpgItems.add(new RpgItem("Rock", "A literal stone...", RpgItemType.Single_Use_Weapon, 5));
    	rpgItems.add(new RpgItem("Ceramic Bowl", "Shatter this boy on your oppenents head and they'll wish you didn't!", RpgItemType.Single_Use_Weapon, 10));
    	rpgItems.add(new RpgItem("Big Dart", "Play darts, but on your oppenent instad of with them!", RpgItemType.Single_Use_Weapon, 3));
    	rpgItems.add(new RpgItem("Dodgeball", "Nailed him.", RpgItemType.Single_Use_Weapon, 5));
    	rpgItems.add(new RpgItem("Screwdriver", "Throwing knife but better...", RpgItemType.Single_Use_Weapon, 7));
    	rpgItems.add(new RpgItem("Xbox Controller", "Release all your rage at once onto your opponent!", RpgItemType.Single_Use_Weapon, 10));
    	rpgItems.add(new RpgItem("Throwing Dart", "bullseye is the opponent's forehead", RpgItemType.Single_Use_Weapon, 10));
    	
    	rpgItems.add(new RpgItem("Fireball", "This spell lets you yeet fireballs at people!", RpgItemType.Spell, 8, 2));
    	rpgItems.add(new RpgItem("Tornado", "This spell will launch whirlwinds at your opponent.", RpgItemType.Spell, 7, 2));
    	rpgItems.add(new RpgItem("Avalanche", "This spell summons rocks down on your opponent's head.", RpgItemType.Spell, 10, 4));
    	rpgItems.add(new RpgItem("Farting", "This spell rushes your opponent with... fart... gas...", RpgItemType.Spell, 5, 1));
    	rpgItems.add(new RpgItem("Tsumani", "This spell crushes your opponent with a tsunami of water!", RpgItemType.Spell, 15, 5));
    	rpgItems.add(new RpgItem("Psychic", "This spell will throw your opponent into the air using psychic powers!", RpgItemType.Spell, 9, 3));
    	rpgItems.add(new RpgItem("Cloud", "This spell somehow hits your opponent with a cloud...?", RpgItemType.Spell, 6, 2));
    	rpgItems.add(new RpgItem("Distraction", "This spell puts thoughts of fortnite into your opponent's head, inducing self harm!", RpgItemType.Spell, 5, 2));
    	rpgItems.add(new RpgItem("Poison", "Poisons your opponent for multiple turns.", RpgItemType.Spell, 5, 2));
    	rpgItems.add(new RpgItem("Distraction", "This spell puts thoughts of fortnite into your opponent's head, inducing self harm!", RpgItemType.Spell, 5, 2));
    	
    	rpgItems.add(new RpgItem("Pasta", "A huge italian dish that will ensure you're completely full!", RpgItemType.Consumable, 10));
    	rpgItems.add(new RpgItem("Ice Cream", "I scream, you scream, we all scream for- nevermind.", RpgItemType.Consumable, 6));
    	rpgItems.add(new RpgItem("Health Potion", "You probably shouldn't be drinking random potions but whatever!", RpgItemType.Consumable, 12));
    	rpgItems.add(new RpgItem("Cup of Coffee", "Drinking this probably wouldn't be that helpful for combat...", RpgItemType.Consumable, 3));
    	rpgItems.add(new RpgItem("Protein Shake", "Provides you with MAXIMUM nutrition!", RpgItemType.Consumable, 10));
    	rpgItems.add(new RpgItem("Teriyaki Chicken", "absolutely delicious", RpgItemType.Consumable, 7));
    	rpgItems.add(new RpgItem("Mushroom Soup", "It tastes like the forest and maybe a hint of neurotoxin!", RpgItemType.Consumable, 9));
    	rpgItems.add(new RpgItem("Krabby Patty", "sponge boy me bob, take this patty and fight to cure my AIDs!", RpgItemType.Consumable, 10));
    	
    	rpgItems.add(new RpgItem("Iron Bar", "This could be made into something nice.", RpgItemType.Resource, 40));
    	rpgItems.add(new RpgItem("Coin", "You stole this off of mario, he's not happy.", RpgItemType.Resource, 25));
    	rpgItems.add(new RpgItem("Ruby", "This thing is epic, probably sells for a lot of bonzi coins!", RpgItemType.Resource, 75));
    	rpgItems.add(new RpgItem("Checken's Pet Rock", "Checken will buy this off of you for a lot! (how did he even lose it in the first place?)", RpgItemType.Resource, 50));
    	rpgItems.add(new RpgItem("Emerald", "idea ripped from minecraft", RpgItemType.Resource, 35));
    	rpgItems.add(new RpgItem("Bitcoin", "You're wondering how you even found a physical bitcoin!", RpgItemType.Resource, 50));
    	rpgItems.add(new RpgItem("$1000 Check", "Turns out you were the 1,000,000th customer in that store...", RpgItemType.Resource, 100));
    	rpgItems.add(new RpgItem("Stuffed Bear", "A rare collectible figure! (nah ill sell it anyways)", RpgItemType.Resource, 50));
    	
    	// Jokes
    	jokes.add("Today at the bank, an old lady asked me to help check her balance. So I pushed her over.");
    	jokes.add("I bought some shoes from a drug dealer. I don't know what he laced them with, but I've been tripping all day.");
    	jokes.add("I told my girlfriend she drew her eyebrows too high. She seemed surprised.");
    	jokes.add("My dog used to chase people on a bike a lot. It got so bad, finally I had to take his bike away.");
    	jokes.add("I'm so good at sleeping. I can do it with my eyes closed.");
    	jokes.add("My boss told me to have a good day.. so I went home.");
    	jokes.add("Why is Peter Pan always flying? He neverlands.");
    	jokes.add("A woman walks into a library and asked if they had any books about paranoia. The librarian says \"They're right behind you!\"");
    	jokes.add("The other day, my wife asked me to pass her lipstick but I accidentally passed her a glue stick. She still isn't talking to me.");
    	jokes.add("When you look really closely, all mirrors look like eyeballs.");
    	jokes.add("My friend says to me: \"What rhymes with orange\" I said: \"No it doesn't\"");
    	jokes.add("What do you call a guy with a rubber toe? Roberto.");
    	jokes.add("What did the pirate say when he turned 80 years old? Aye matey.");
    	jokes.add("My wife told me I had to stop acting like a flamingo. So I had to put my foot down.");
    	jokes.add("I couldn't figure out why the baseball kept getting larger. Then it hit me.");
    	jokes.add("Why did the old man fall in the well? Because he couldn't see that well.");
    	jokes.add("I ate a clock yesterday, it was very time consuming.");
    	jokes.add("A blind man walks into a bar. And a table. And a chair.");
    	jokes.add("I know a lot of jokes about unemployed people but none of them work.");
    	jokes.add("What's orange and sounds like a parrot? A carrot.");
    	jokes.add("Did you hear about the italian chef that died? He pasta way.");
    	jokes.add("Why couldn't the bicycle stand up? Because it was two tired!");
    	jokes.add("Parallel lines have so much in common. It's a shame they'll never meet.");
    	jokes.add("My wife accused me of being immature. I told her to get out of my fort.");
    	jokes.add("Where do you find a cow with no legs? Right where you left it.");
    	jokes.add("When a deaf person sees someone yawn do they think it's a scream?");
    	jokes.add("As I suspected, someone has been adding soil to my garden. The plot thickens.");
    	jokes.add("How do crazy people go through the forest? They take the physco path.");
    	jokes.add("What did the traffic light say to the car? Don't look! I'm about to change.");
    	jokes.add("I just wrote a book on reverse psychology. Do **not** read it!");
    	jokes.add("What did one hat say to the other? You stay here. I'll go on ahead.");
    	jokes.add("Why wouldn't the shrimp share his treasure? Because he was a little shellfish.");
    	jokes.add("I am on a seafood diet. I see food, and I eat it.");
    	jokes.add("Alcohol! Because no great story started with someone eating a salad.");
    	jokes.add("I don't need a hair stylist, my pillow gives me a new hairstyle every morning.");
    	jokes.add("Don't worry if plan A fails, there are 25 more letters in the alphabet.");
    	jokes.add("If I'm not back in five minutes, just wait longer...");
    	jokes.add("A balanced diet means a cupcake in each hand.");
    	jokes.add("Doing nothing is hard, you never know when you're done.");
    	jokes.add("If you're not supposed to eat at night, why is there a light bulb in the refrigerator?");
    	jokes.add("Don't drink while driving - you might spill the beer.");
    	jokes.add("I think the worst time to have a heart attack is during a game of charades.");
    	jokes.add("My wallet is like an onion. When I open it, it makes me cry...");
    	jokes.add("Doesn't expecting the unexpected make the unexpected expected?");
    	jokes.add("I'm not clumsy, The floor just hates me, the table and chairs are bullies and the walls get in my way.");
    	jokes.add("The only reason I'm fat is because a tiny body couldn't store all this personality.");
    	jokes.add("I'm not lazy, I'm just very relaxed.");
    	jokes.add("Always remember you're unique, just like everyone else.");
    	jokes.add("A cookie a day keeps the sadness away. An entire jar of cookies a day brings it back.");
    	jokes.add("A successful man is one who makes more money than his wife can spend. A successful woman is one who can find such a man.");
    	jokes.add("I asked God for a bike, but I know God doesn't work that way. So I stole a bike and asked for forgiveness.");
    	jokes.add("If you think nobody cares if you're alive, try missing a couple of bank payments.");
    	jokes.add("Money can't buy happiness, but it sure makes misery easier to live with.");
    	jokes.add("If you do a job too well, you'll get stuck with it.");
    	jokes.add("Don't tell me the sky is the limit when there are footprints on the moon.");
    	jokes.add("I don't suffer from insanity, I enjoy every minute of it.");
    	jokes.add("Sometimes I wake up grumpy; other times I let her sleep.");
    	jokes.add("God created the world, everything else is made in China.");
    	jokes.add("When life gives you melons, you might be dyslexic.");
    	jokes.add("Children in the back seat cause accidents, accidents in the back seat cause children!");
    	jokes.add("You never truly understand something until you can explain it to your grandmother.");
    	jokes.add("Experience is a wonderful thing. It enables you to recognise a mistake when you make it again.");
    	jokes.add("You can't have everything, where would you put it?");
    	jokes.add("Don't you wish they made a clap on clap off device for some peoples mouths?");
    	jokes.add("If people are talking behind your back, then just fart.");
    	
    	// Swears (DONT LOOK MOMMY)
    	defaultswears.add("fuck");
    	defaultswears.add("shit");
    	defaultswears.add("nigga");
    	defaultswears.add("nigger");
    	defaultswears.add("bitch");
    	defaultswears.add("whore");
    	defaultswears.add("faggot");
    	defaultswears.add("faggit");
    	defaultswears.add("cunt");
    	
    	// Fun messages
    	funMessages.put("bonzi is bad", "shut up no im not");
    	funMessages.put("bonzibot is bad", "shut up no im not");
    	funMessages.put("bonzi bot is bad", "shut up no im not");
    	funMessages.put("thanks bonzi", "you're welcome!!");
    	funMessages.put("hi bonzi", "hello, {user}!");
    	funMessages.put("hello bonzi", "hello, {user}!");
    	funMessages.put("hi bonzibot", "hello, {user}!");
    	funMessages.put("hello bonzibot", "hello, {user}!");
    	funMessages.put("bonzi shut up", "ok fine ;(");
    	funMessages.put("bonzibot shut up", "ok fine ;(");
    	funMessages.put("bonzi bot shut up", "ok fine ;(");
    	funMessages.put("shut up bonzi", "ok fine ;(");
    	funMessages.put("shut up bonzibot", "ok fine ;(");
    	funMessages.put("shut up bonzi bot", "ok fine ;(");
    	funMessages.put("7uke", "hey wait thats my creator!");
    	funMessages.put("giraffey", "hey wait thats my creator!");
    	funMessages.put("toilet takeoff", "https://www.youtube.com/watch?v=hgvMlGYSCTk");
    	funMessages.put("2+2", "4");
    	funMessages.put("2 + 2", "4");
    	funMessages.put("2 * 2", "4");
    	funMessages.put("2*2", "4");
    	funMessages.put("icup", "i - c - u - p; AHAHA U GOT ME");
    	
    	// Other stuff
    	games.add(new StatGame("Rainbow Six Siege", "r6", "404 Not Found. We are unable to find your profile.", "https://r6.tracker.network/profile/pc/",
    			new InternalStat("Wins", "data-stat=\"PVPMatchesWon\""),
    			new InternalStat("Losses", "data-stat=\"PVPMatchesLost\""),
    			new InternalStat("Win Percentage", "data-stat=\"PVPWLRatio\""),
    			new InternalStat("Kills", "data-stat=\"PVPKills\""),
    			new InternalStat("Deaths", "data-stat=\"PVPDeaths\""),
    			new InternalStat("KDR", "data-stat=\"PVPKDRatio\""),
    			new InternalStat("Total Matches", "data-stat=\"PVPMatchesPlayed\""),
    			new InternalStat("Total Time Played", "data-stat=\"PVPTimePlayed\"")));
    	games.add(new StatGame("Rocket League", "rl", "We could not find your stats, please ensure your platform and name are correct", "https://rocketleague.tracker.network/profile/steam/",
    			new InternalStat("Wins", "data-stat=\"Wins\""),
    			new InternalStat("Goals", "data-stat=\"Goals\""),
    			new InternalStat("Saves", "data-stat=\"Saves\""),
    			new InternalStat("Shots", "data-stat=\"Shots\""),
    			new InternalStat("MVPs", "data-stat=\"MVPs\""),
    			new InternalStat("Assists", "data-stat=\"Assists\"")));
    	
    	// Special case.
    	StatGame csgo = new StatGame("CS:GO", "csgo", "The player either hasn't played CSGO or their profile is private.", "https://tracker.gg/csgo/profile/steam/", 
    			new InternalStat("Kills", "title=\"Kills\""),
    			new InternalStat("Headshots", "title=\"Headshots\""),
    			new InternalStat("Deaths", "title=\"Deaths\""),
    			new InternalStat("K/D Ratio", "title=\"K/D\""),
    			new InternalStat("Win Percentage", "title=\"Win %\""),
    			new InternalStat("Wins", "title=\"Wins\""),
    			new InternalStat("Losses", "title=\"Losses\""),
    			new InternalStat("MVPs", "title=\"MVP\"")
    			);
    	csgo.skipLinesDown = 2;
    	csgo.statsInline = true;
    	csgo.usesSteamURL = true;
    	games.add(csgo);
    }
    public void onReady(ReadyEvent e) {
    	try {
			yt = getService();
		} catch (GeneralSecurityException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
    	System.out.print("YouTube API Object Created.\n");
    	
        this.musicManagers = new HashMap<>();
        this.playerManager = new DefaultAudioPlayerManager();
        AudioSourceManagers.registerRemoteSources(playerManager);
        AudioSourceManagers.registerLocalSource(playerManager);
    	System.out.print("Built Audio Utilities.\n");
        
    	System.out.print("Loading files...\n");
    	load("/home/pi/");
    	System.out.print("Loaded.\n");
    	for(Guild g : e.getJDA().getGuilds()) {
    		verifyModRole(g);
    	}
    	System.out.print("Verified roles.\n");
    	
    	//JDA jda = e.getJDA();
    	
    	ScheduledThreadPoolExecutor exec;
    	exec = new ScheduledThreadPoolExecutor(0);
    	
    	// Autosaving.
    	exec.scheduleAtFixedRate(new Runnable() {
    		public void run() {
    			save("/home/pi/");
    		}
    	}, 20, 300, TimeUnit.SECONDS);
    	
    	// Rainbow Role Manager.
    	exec.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				try {
					JDA jda = e.getJDA();
					Set<Entry<Long, Long>> set;
					set = new HashSet<Entry<Long, Long>>
						(rainbowRoles.entrySet());
					for(Entry<Long, Long> entry : set) {
						try {
							Long gID = entry.getKey();
							Long rID = entry.getValue();
							Guild g = jda.getGuildById(gID);
							Role r = g.getRoleById(rID);
							r.getManager().setColor
								(randomColor()).queue();
						} catch(NullPointerException npe) {
							rainbowRoles.remove(entry.getKey());
							continue;
						} catch(HierarchyException he) {
							rainbowRoles.remove(entry.getKey());
							Guild g = jda.getGuildById(entry.getKey());
							g.getDefaultChannel().sendMessage(failureEmbed("Error: yo i don't have permission to edit that rainbow role... mind moving the bonzibot role above the rainbow role?")).queue();
						}
					}
				} catch(Exception exc) {
					exc.printStackTrace();
				}
			}
    	}, 1, 3, TimeUnit.MINUTES);
    	
    	// MOTD System.
    	exec.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				String next = motds.moveNext();
				bot.getPresence().setActivity
					(Activity.playing(next));
			}
    	}, 15, 15, TimeUnit.MINUTES);
    	
    	System.out.print("Created Autosave Service.\n");
    	int total = 0;
    	for(PremiumCommand cmd : PremiumCommand.values()) {
    		if(!cmd.equals(PremiumCommand.Premium)) {
    			total += premiumPrices[cmd.ordinal()];
    		}	
    	}
    	premiumPrices[premiumPrices.length-1] = (int)Math.round(total*0.8);
    	
    	DirectoryStream<Path> ds = null;
    	try {
			ds = Files.newDirectoryStream(Paths.get(".\\voiceData"));
		} catch (IOException e1) {}
    	if(ds == null) {
    		System.out.print("Detected running in a PI. Did not clear voiceSample cache.");
    	} else {
	    	List<File> names = new ArrayList<File>();
	    	ds.forEach(path -> {
	    		if(path.toFile().getName().contains("voiceSample")) {
	    			names.add(path.toFile());
	    		}
	    	});
	    	for(File f : names) {
	    		f.delete();
	    	}
	    	System.out.print("Cleared voice cache.\n");
    	}
    	
    	// Other stuff
    	System.setProperty("freetts.voices", "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory");
    	voiceManager = VoiceManager.getInstance();
    	kevin16 = voiceManager.getVoice("kevin16");
    	if(kevin16 == null) {
    		System.out.print("[AARON WARNING] The voice kevin16 doesn't exist on the PI...\n");
    	} else {
    		kevin16.allocate();
    		System.out.print("Initialized TTS.\n");
    	}
    }

    @SuppressWarnings("unused")
	@Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent e) {
    	bot = e.getJDA();
    	if(e.getAuthor().isBot()) {
    		return;
    	}
    	if(!e.getGuild().getSelfMember().hasPermission(Permission.ADMINISTRATOR)) {
    		e.getChannel().sendMessage(failureEmbed("Whoa, hold on! I use a lot of permissions for my commands and such, so could you please give my bot role administrator?")).queue();
    		return;
    	}
    	verifyGuild(e.getGuild());
    	
    	Message message = e.getMessage();
    	User user = e.getAuthor();
    	Member member = e.getMember();
    	TextChannel tc = e.getChannel();
    	MessageChannel channel = tc;
    	Guild guild = e.getGuild();
    	String text = message.getContentRaw();
    	String[] args = text.split("\\s+");
    	long guildID = guild.getIdLong();
    	long modLong = modRoles.get(guildID);
    	Role mod = guild.getRoleById(modLong);
    	List<String> filter = getFilter(guild);
    	long id = user.getIdLong();
    	
    	// Loop over the dontLog list
    	while(dontLog.size() > 2500) {
    		dontLog.remove(0);
    	}
    	
    	// Cache Stuff
    	List<Message> ch = cache.get(guildID);
    	if(ch == null) {
    		ch = new ArrayList<Message>();
    	}
    	ch.add(message);
    	if(ch.size() > cacheLimit) {
    		ch.remove(0);
    	}
    	cache.put(guildID, ch);
    	
    	if(args.length == 0) {
    		return;
    	}
    	
    	// HashMaps
    	if(!premiumAccounts.containsKey(id)) {
    		premiumAccounts.put(id, new ArrayList<PremiumCommand>());
    	}
    	List<PremiumCommand> acct = premiumAccounts.get(id);
    	if(acct.contains(PremiumCommand.Premium)) {
    		acct = Arrays.asList(PremiumCommand.values());
    		premiumAccounts.put(id, acct);
    	}
    	if(!rainbowRoles.containsKey(guildID)) {
    		rainbowRoles.put(guildID, 0l);
    	}
    	if(!prefixes.containsKey(guildID)) {
    		prefixes.put(guildID, DEFAULT_PREFIX);
    	}
    	String prefix = prefixes.get(guildID);
    	if(RUNTESTBOT == BotMode.Test && !prefix.equalsIgnoreCase("btest:")) {
    		prefixes.put(guildID, "btest:");
    		prefix = "btest:";
    	}
    	
    	if(guild.getIdLong() == discord) {
    		if(channel.getName().equalsIgnoreCase("latest")) {
    			// It's an update.
    			for(Guild serv : bot.getGuildCache()) {
    				for(TextChannel servTc : serv.getTextChannels()) {
    					String topic = servTc.getTopic();
    					if(channelHasModifier(servTc, "bonziupdates")) {
    						servTc.sendMessage(e.getMessage()).queue();
    						break;
    					}
    				}
    			}
    		}
    	}
    	
    	if(text.toLowerCase().equalsIgnoreCase("b:stuck")) {
    		long _gid = guild.getIdLong();
    		int cgnum;
    		if(countingGames.containsKey(_gid)) {
    			cgnum = countingGames.get(_gid);
    		} else {
    			cgnum = 0;
    		}
    		
    		EmbedBuilder eb = new EmbedBuilder();
    		eb.setColor(Color.green);
    		eb.setTitle("bonzibot instucker-inator");
    		eb.setDescription("Your prefix is: `" + prefix + "`\n"
    				+ "Your counting game number is: `" + cgnum + "`");
    		eb.setFooter("Prefix still not working? Reset it to b: with \"b:forceresetprefix\".");
    		channel.sendMessage(eb.build()).queue();
    		return;
    	}
    	if(text.toLowerCase().equalsIgnoreCase("b:forceresetprefix")) {
            if(!isMod(member, guild)) {
            	channel.sendMessage(failureEmbed("You don't have permission to use this!")).queue(m -> {
            		m.delete().queueAfter(5, TimeUnit.SECONDS);
            	});
            	return;
            }
            prefixes.put(guildID, "b:");
            channel.sendMessage(successEmbed("Successfully reset your prefix. Now \"b:\".")).queue();
    	}
    	if(text.toLowerCase().equalsIgnoreCase("admin:recoverprefix")) {
    		channel.sendMessage("Please wait while I fetch the prefix...").queue();
    		channel.sendMessage("EXACTLY THIS without the quotes: \"" + prefix + "\"").queue();
    		return;
    	}
    	
    	if(channelHasModifier(tc, "counting")) {
    		countingGame(guild, text, message, (TextChannel)channel);
    		return;
    	}
    	
    	if(channelHasModifier(tc, SCRIPTEDITOR_MODIFIER)) {
    		
    		dontLog.add(message.getIdLong());
    		message.delete().queue();
    		
            if(!isMod(member, guild)) {
            	channel.sendMessage(failureEmbed("You don't have permission to use the scriptcreator!")).queue(m -> {
            		m.delete().queueAfter(5, TimeUnit.SECONDS);
            	});
            	return;
            }
            if(getUpgrades(guild) < SCRIPT_EDITOR_COST) {
            	channel.sendMessage(failureEmbed("Your server needs " + SCRIPT_EDITOR_COST + " upgrades to use this feature!")).queue(m -> {
            		m.delete().queueAfter(5, TimeUnit.SECONDS);
            	});
            	return;
            }
    		
    		long gID = guild.getIdLong();
    		if(!scriptCreators.containsKey(gID)) {
    			channel.sendMessage(failureEmbed("Invalid channel! Delete this channel and properly open a scripting workspace by using the \"scripteditor\" command!")).queue();
    			return;
    		}
    		ScriptCreator thisServer = scriptCreators.get(gID);
    		thisServer.lastMember = member;
    		thisServer.receiveActionText(message.getContentRaw(), tc);
    		scriptCreators.put(gID, thisServer);
    		return;
    	}
    	
    	for(String word : args) {
    		if(tc.isNSFW()) {
    			break;
    		}
    		if(channelHasModifier(tc, "nofilter")) { break; }
    		if(text.toLowerCase().startsWith(prefix + "filter")) {
    			break;
    		}
    		for(String swear : filter) {
    			if(word.toLowerCase().contains(swear.toLowerCase())) {
    				message.delete().queue();
    				channel.sendMessage("You can't say that, " + member.getEffectiveName() + "!").queue(mm->mm.delete().queueAfter(5, TimeUnit.SECONDS));
    				return;
    			}
    		}
    	}
    	
    	if(channelHasModifier(tc, "rpg")) {
    		RPG(args, channel, tc, guild, member, message);
    		return;
    	}
    	// ===========================================================================
    	
    	long uid;
    	uid  = user.getIdLong();
    	if(CCResponseQueue.containsKey(uid)) {
    		String cmd = CCResponseQueue.get(uid);
    		CCResponseQueue.remove(uid);
    		CustomCommand cc;
    		String response = text;
    		if(!message.getAttachments().isEmpty()) {
    			response += ("\n" + message.getAttachments().get(0).getUrl());
    		}
    		cc = new CustomCommand(cmd, response, member);
    		ccm.AddCustomCommand(cc, user);
    		channel.sendMessage(successEmbed("Created Tag!")).queue();
    		System.out.print("Tag created.\n"
    			+ "Command: " + cc.command + "\n"
    			+ "Response: " + response + "\n"
    			+ "Creator: " + cc.creator + "\n"
    			+ "Server: " + cc.server + "\n\n");
    		return;
    	}
    	if(_CCResponseQueue.containsKey(uid)) {
    		String cmd = _CCResponseQueue.get(uid);
    		_CCResponseQueue.remove(uid);
    		CustomCommand cc;
    		String response = text;
    		if(!message.getAttachments().isEmpty()) {
    			response += ("\n" + message.getAttachments().get(0).getUrl());
    		}
    		cc = new CustomCommand(cmd, response, member);
    		ccm.AddPrivateCommand(cc, user, guild);
    		channel.sendMessage(successEmbed("Created Private Tag!")).queue();
    		System.out.print("Private tag created.\n"
    			+ "Command: " + cc.command + "\n"
    			+ "Response: " + response + "\n"
    			+ "Creator: " + cc.creator + "\n"
    			+ "Server: " + cc.server + "\n\n");
    		return;
    	}
    	// ===========================================================================
    	// XP Logic
    	if(!xp.containsKey(id)) {
    		xp.put(id, 0);
    	}
    	// ===========================================================================
    	
    	// Modifiers \/
    	if(!channelHasModifier(tc, "noxp")) {
        	int userxp = xp.get(id);
        	int levelBefore = calculateLevel(userxp);
        	userxp++;
        	int level = calculateLevel(userxp);
        	if(levelBefore != level) {
        		int amt = level*10+10;
        		int coins = random(amt) + amt;
        		channel.sendMessage(successEmbed(
        			user.getName() + ", you leveled up to level "
        			+ level + " and gained " + coins + " coins as a reward!")).queue(msg -> {
        				msg.delete().queueAfter(5, TimeUnit.SECONDS);
        			}, f -> {});
        		addCoins(user, coins);
        	}
        	xp.put(id, userxp);
    	}
    	if(channelHasModifier(tc, "anonymous")) {
    		dontLog.add(message.getIdLong());
    		message.delete().queue();
    		
    		String raw = message.getContentRaw();
    		if(!message.getMentionedRoles().isEmpty()) {
    			return;
    		}
    		if(raw.toLowerCase().contains("@everyone") ||
    			raw.toLowerCase().contains("@here")) {
    			raw = "**(Attempted everyone ping from " + getFriendCode(user) + ")**";
    		}
    		
    		channel.sendMessage(raw).queue();
    		return;
    	}
    	if(channelHasModifier(tc, "picturesonly")) {
    		if(message.getAttachments().isEmpty()) {
    			dontLog.add(message.getIdLong());
    			message.delete().queue();
    		}
    		return;
    	}
    	if(channelHasModifier(tc, "premiumonly")) {
    		if(premiumAccounts.containsKey(id) && !isMod(member, guild)) {
    			List<PremiumCommand> cmds;
    			cmds = premiumAccounts.get(id);
    			if(!cmds.contains(PremiumCommand.Premium)) {
        			dontLog.add(message.getIdLong());
        			message.delete().queue();
        			return;
    			}
    		}
    	}
    	// Modifiers /\
    	if(text.toLowerCase().equalsIgnoreCase("admin:test")) {
    		if(!isAdmin(user)) {
    			return;
    		}
    		for(Map.Entry<Long, List<Long>> kv: upgraders.entrySet()) {
    			System.out.println(kv.getKey() + ":");
    			for(long l: kv.getValue()) {
    				System.out.println("	- " + l);
    			}
    		}
    		channel.sendMessage("printed").queue();
    	}
    	if(text.toLowerCase().equalsIgnoreCase("admin:amiupgrader")) {
    		if(!isAdmin(user)) {
    			return;
    		}
    		if(!upgraders.containsKey(guildID)) {
    			channel.sendMessage("no upgrader cache.").queue();
    			return;
    		}
    		System.out.println("fetching from list of guild " + guildID + ".");
    		List<Long> ups = upgraders.get(guildID);
    		if(ups.isEmpty()) {
    			channel.sendMessage("list is empty").queue();
    			return;
    		}
    		for(long l: ups) System.out.println(l);
    		channel.sendMessage("status: " +
    			ups.contains(user.getIdLong())).queue();
    		return;
    	}
    	if(text.toLowerCase().equalsIgnoreCase("admin:disableupgrader")) {
    		if(!isAdmin(user)) {
    			return;
    		}
    		if(!upgraders.containsKey(guildID)) {
    			channel.sendMessage("no upgrader cache.").queue();
    			return;
    		}
    		List<Long> ups = upgraders.get(guildID);
    		upgraders.put(guildID, ups);
    		channel.sendMessage("removed your upgrader status, if any.").queue();
    		return;
    	}
    	if(text.toLowerCase().equalsIgnoreCase("admin:help")) {
    		StringBuilder sb = new StringBuilder();
    		sb.append("admin:pi_stats > Check PI Status.\n");
    		sb.append("admin:tts <text> > Exports and uploads TTS .wav file.\n");
    		sb.append("admin:setlot <number> > Sets the lottery.\n");
    		sb.append("admin:resetxp <id> > Resets the user's XP.\n");
    		sb.append("admin:removecc <name> > Deletes a tag.\n");
    		sb.append("admin:save > Force saves. Done every 5 minutes automatically.\n");
    		sb.append("admin:backup > Force saves and backs up all data.\n");
    		sb.append("admin:restore > Restores data from backup files.\n");
    		sb.append("admin:compile \\`\\`\\`java > Compiles and runs java in the runtime.\n");
    		sb.append("admin:servers > Displays all servers BonziBot is in.\n");
    		sb.append("admin:ccban <id> > Bans somebody from using tags and deletes all of theirs.\n");
    		sb.append("admin:ccunban <id> > Allows user to use tags. Does NOT restore deleted tags.\n");
    		sb.append("admin:setcoins <coins> > Sets your coins.\n");
    		sb.append("admin:addcoins <coins> > Adds 1000 coins to your account.\n");
    		sb.append("admin:subtractcoins <coins> > Subtracts 1000 coins from your account.\n");
    		sb.append("admin:lookupguild <id> > Returns the name and owner of the specified guild.\n");
    		sb.append("admin:amiupgrader > Returns if you're an upgrader of the current guild.\n");
    		sb.append("admin:disableupgrader > Removes you from the guild upgrader cache, if in it.\n");
    		channel.sendMessage(sb.toString()).queue();
    	}
    	if(text.toLowerCase().equalsIgnoreCase("admin:pi_stats")) {
    		EmbedBuilder eb = new EmbedBuilder();
    		eb.setColor(Color.green);
    		eb.setTitle("Raspberry PI Current Status");
    		eb.setDescription("All information displayed is live data.");
    		
    		float ctemp = -1;
    		float cvolt = -1;
    		long maxram = -1;
    		long useram = -1;
    		long cmem = -1;
    		String serial = "";
    		String inf = "";
			try {
				ctemp = SystemInfo.getCpuTemperature();
				cvolt = SystemInfo.getCpuVoltage();
				
				maxram = SystemInfo.getMemoryTotal()/1024/1024;
				useram = SystemInfo.getMemoryUsed()/1024/1024;
				cmem = SystemInfo.getMemoryCached()/1024/1024;
				
				serial = SystemInfo.getSerial();
				inf = SystemInfo.getOsName() + " v."
					+ SystemInfo.getOsVersion();
			} catch (NumberFormatException e1) {
				e1.printStackTrace();
			} catch (UnsupportedOperationException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
    		eb.addField("CPU Temperature", ctemp + "°", true);
    		eb.addField("CPU Voltage", String.valueOf(cvolt) + "V", true);
    		eb.addField("Memory Usage", useram + "/" + maxram + " mb", true);
    		eb.addField("Cached Memory", cmem + " mb", true);
    		eb.addBlankField(false);
    		eb.addField("PI Serial", serial, true);
    		eb.addField("PI OS Info", inf, true);
    		channel.sendMessage(eb.build()).queue();
    	}
    	if(text.toLowerCase().startsWith("admin:tts ")) {
    		String speak = text.substring(10);
    		generateVoiceFile(speak);
    		channel.sendMessage("Generated voice file. Uploading...").queue();
    		channel.sendFile(new File("C:\\temp\\output.wav")).queue();
    		return;
    	}
    	if(text.toLowerCase().startsWith("admin:lookupguild ")) {
    		if(!isAdmin(user)) {
    			return;
    		}
    		Guild found = null;
    		long specified = Long.parseLong(args[1]);
    		for(Guild iteratedGuild: bot.getGuildCache()) {
    			long iGuildID = iteratedGuild.getIdLong();
    			if(specified == iGuildID) {
    				found = iteratedGuild;
    				break;
    			}
    		}
    		if(found == null) {
    			// Didn't find.
    			channel.sendMessage("No guild with that ID.").queue();
    		}
    		channel.sendMessage("Name: " + found.getName()
    		+ "\nOwner: " + getFriendCode(found.retrieveOwner().complete().getUser())
    		+ "\nMember count: " + guild.getMemberCount()).queue();
    		return;
    	}
    	if(text.toLowerCase().startsWith("admin:setlot ")) {
    		if(!isAdmin(user)) {
    			return;
    		}
    		String _coin = text.substring(13);
    		int coin = Integer.parseInt(_coin);
    		int prevLot = lottery;
    		lottery = coin;
    		channel.sendMessage("Set the lottery to " + coin + " coins.\n"
    			+ "Was previously " + prevLot + " coins.").queue();
    		return;
    	}
    	if(text.toLowerCase().startsWith("admin:resetxp ")) {
    		if(!isAdmin(user)) {
    			return;
    		}
    		String _id = text.substring(14);
    		Long l;
    		try {
    			l = Long.parseLong(_id);
    		} catch(Exception exc) {
    			channel.sendMessage("Usage: admin:resetxp <id>").queue();
    			return;
    		}
    		JDA jda = user.getJDA();
    		User target = jda.getUserById(l);
    		if(l == null) {
    			channel.sendMessage("This id is not a valid user!").queue();
    			return;
    		}
    		if(!xp.containsKey(l)) {
    			channel.sendMessage("This user doesn't have any XP!").queue();
    			return;
    		}
    		xp.put(l, 0);
    		privateMessage(target,
    			"Your xp has been reset. This could be due to abuse/spamming.\n"
    			+ "If you think this is a mistake, contact the admin that reset your xp for more info.");
    		channel.sendMessage("Okay, done.").queue();
    		return;
    	}
    	if(text.toLowerCase().startsWith("admin:removecc")) {
    		if(!isAdmin(user)) {
    			return;
    		}
    		removeTag(args, channel, guild, member, message);
    		return;
    	}
    	if(text.toLowerCase().startsWith("admin:save")) {
    		if(!isAdmin(user)) {
    			return;
    		}
        	save("/home/pi/");
        	channel.sendMessage("Ran a force save.").queue();
    	}
    	if(text.toLowerCase().startsWith("admin:backup")) {
    		if(!isAdmin(user)) {
    			System.out.print("Not an admin\n");
    			return;
    		}
    		save("/home/pi/");
        	channel.sendMessage("Initial Save...").queue();
        	save("/home/pi/BACKUP-");
        	channel.sendMessage("Backed up!").queue();
    	}
    	if(text.toLowerCase().startsWith("admin:restore")) {
    		if(!isAdmin(user)) {
    			return;
    		}
        	load("/home/pi/BACKUP-");
        	channel.sendMessage("Loaded backup.").queue();
    	}
    	if(text.toLowerCase().startsWith("admin:checkversion")) {
    		if(!isAdmin(user)) {
    			return;
    		}
    		admin_CheckVersion(args, channel, tc, guild, member, message);
    	}
    	if(text.toLowerCase().startsWith("admin:compile ```java\n")) {
    		if(!isAdmin(user)) {
    			return;
    		}
    		String code = text.substring(22);
    		code = code.replace("\n```", "");
    		channel.sendMessage("Attempting to compile: ```java\n" + code + "\n```").queue();
    		try {
    			Path outpath = new File("C:\\temp\\compiled_out.txt").toPath();
    			OutputStream out = Files.newOutputStream(outpath, new OpenOption[0]);
				Class<?> clazz = DynamicExecutor.compile(code, out);
				Method msg = null;
				Object instance = clazz.newInstance();
				try {
					msg = instance.getClass().getDeclaredMethod("getMsg", GuildMessageReceivedEvent.class, App.class);
				} catch (NoSuchMethodException e1) {
					channel.sendMessage("No such method.").queue();
				} catch (SecurityException e1) {
					e1.printStackTrace();
				}
				out.close();
				File toread = outpath.toFile();
				String read = read(toread);
				if(!read.equals("") && !read.equals(" ")) {
					channel.sendMessage("Failed to compile. Error: ```\n" + read + "\n```").queue();
					return;
				}
				msg.setAccessible(true);
				Object val = msg.invoke(instance, e, this);
				channel.sendMessage(val.toString()).queue();
				return;
			} catch (ClassNotFoundException excc) {
				channel.sendMessage("Failed to compile due to missing class.\n```\n" + buildStackTrace(excc.getStackTrace()) + "\n```").queue();
				return;
			} catch(IllegalAccessException | InstantiationException | IOException exc) {
				exc.printStackTrace();
				return;
			} catch (IllegalArgumentException e1) {
				e1.printStackTrace();
			} catch (InvocationTargetException e1) {
				e1.printStackTrace();
			}
    		
    	}
    	if(text.toLowerCase().startsWith("admin:servers")) {
    		admin_Servers(args, channel, tc, guild, member, message);
    		return;
    	}
    	if(text.toLowerCase().startsWith("admin:ccban ")) {
    		admin_CCBan(args, channel, tc, guild, member, message);
    		return;
    	}
    	if(text.toLowerCase().startsWith("admin:ccunban ")) {
    		admin_CCUnban(args, channel, tc, guild, member, message);
    		return;
    	}
    	if(text.toLowerCase().startsWith("admin:setcoins ")) {
    		if(!isAdmin(user)) {
    			return;
    		}
    		try {
    			int ind = 15;
    			String _input = text.substring(ind);
    			int input = Integer.parseInt(_input);
    			setCoins(user, input);
    			channel.sendMessage("Set your coins to " + input + "!").queue();
    		}catch(Exception exc) {
    			channel.sendMessage("**Exception.**\n"
    				+ exc.getMessage()).queue();
    		}
    		return;
    	}
    	if(text.toLowerCase().startsWith("admin:addcoins")) {
    		if(!isAdmin(user)) {
    			return;
    		}
    		addCoins(user, 1000);
    		channel.sendMessage("Added 1000 coins to your account.").queue();
    		return;
    	}
    	if(text.toLowerCase().startsWith("admin:subtractcoins")) {
    		if(!isAdmin(user)) {
    			return;
    		}
    		subtractCoins(user, 1000);
    		channel.sendMessage("Removed 1000 coins from your account.").queue();
    		return;
    	}
    	// ====================== REGULAR COMMANDS \/ ======================
    	prefix = prefix.toLowerCase();
    	if(text.toLowerCase().startsWith(prefix + "clear ")) {
    		dontLog.add(message.getIdLong());
            clear(args, message, channel, member, guild);
            return;
        }
    	if(text.toLowerCase().startsWith(prefix + "prefix ")) {
    		dontLog.add(message.getIdLong());
    		prefix(user, channel, args, prefix, guildID, member, guild);
    		return;
    	}
    	if(text.toLowerCase().startsWith(prefix + "poll ")) {
    		dontLog.add(message.getIdLong());
            poll(args, user, message, channel, member);
            return;
        }
    	if(text.toLowerCase().startsWith(prefix + "help")) {
    		dontLog.add(message.getIdLong());
    		help(user, channel, args, prefix, guild);
    		return;
    	}
    	if(text.toLowerCase().startsWith(prefix + "say ")) {
    		dontLog.add(message.getIdLong());
            say(args, user, message, channel, guild);
            return;
        }
        if(text.toLowerCase().startsWith(prefix + "yeetocheetobeansburrito")) {
    		dontLog.add(message.getIdLong());
            yeetoCheetoBeansBurrito(channel, message);
            return;
        }
        if(text.toLowerCase().startsWith(prefix + "meme")) {
    		dontLog.add(message.getIdLong());
            meme(channel, args, message);
            return;
        } 
        if(text.toLowerCase().startsWith(prefix + "subreddit ")) {
    		dontLog.add(message.getIdLong());
            subreddit(channel, args, message);
            return;
        }
        if(text.toLowerCase().startsWith(prefix + "modrole")) {
    		dontLog.add(message.getIdLong());
            modRole(args, channel, guild, member, message);
            return;
        }
        if(text.toLowerCase().startsWith(prefix + "joke")) {
    		dontLog.add(message.getIdLong());
            joke(channel);
            return;
        }
        if(text.toLowerCase().startsWith(prefix + "message")) {
    		dontLog.add(message.getIdLong());
            message(args, channel, guild, member, message);
            return;
        }
        if(text.toLowerCase().startsWith(prefix + "joinrole")) {
    		dontLog.add(message.getIdLong());
            joinRole(args, channel, guild, member, message);
            return;
        }
        if(text.toLowerCase().startsWith(prefix + "reactionrole")) {
    		dontLog.add(message.getIdLong());
            reactionRole(args, message, channel, member, user, guild, prefix);
            return;
        }
        if(text.toLowerCase().startsWith(prefix + "filter")) {
    		dontLog.add(message.getIdLong());
            filter(args, channel, guild, member, message);
            return;
        }
        if(text.toLowerCase().startsWith(prefix + "kick")) {
    		dontLog.add(message.getIdLong());
            kick(args, channel, guild, member, message);
            return;
        }
        if(text.toLowerCase().startsWith(prefix + "mute")) {
    		dontLog.add(message.getIdLong());
            mute(args, channel, guild, member, message);
            return;
        }
        if(text.toLowerCase().startsWith(prefix + "unmute")) {
    		dontLog.add(message.getIdLong());
            unmute(args, channel, guild, member, message);
            return;
        }
        if(text.toLowerCase().startsWith(prefix + "ban")) {
    		dontLog.add(message.getIdLong());
            ban(args, channel, guild, member, message);
            return;
        }
        if(text.toLowerCase().startsWith(prefix + "warn ")) {
    		dontLog.add(message.getIdLong());
            warn(args, channel, guild, member, message);
            return;
        }
        if(text.toLowerCase().startsWith(prefix + "warns")) {
    		dontLog.add(message.getIdLong());
            warns(args, channel, guild, member, message);
            return;
        }
        if(text.toLowerCase().startsWith(prefix + "clearwarns")) {
    		dontLog.add(message.getIdLong());
            clearWarns(args, channel, guild, member, message);
            return;
        }
        if(text.toLowerCase().startsWith(prefix + "backup")) {
    		dontLog.add(message.getIdLong());
            backup(args, channel, guild, member, message);
            return;
        }
        if(text.toLowerCase().startsWith(prefix + "restore")) {
    		dontLog.add(message.getIdLong());
            restore(args, channel, guild, member, message);
            return;
        }
        if(text.toLowerCase().startsWith(prefix + "tag ")) {
    		dontLog.add(message.getIdLong());
            tag(args, channel, guild, member, message);
            return;
        }
        if(text.toLowerCase().startsWith(prefix + "privatetag ")) {
    		dontLog.add(message.getIdLong());
            tagPrivate(args, channel, guild, member, message);
            return;
        }
        if(text.toLowerCase().startsWith(prefix + "tagleaderboard")) {
    		dontLog.add(message.getIdLong());
            tagLeaderboard(args, channel, guild, member, message);
            return;
        }
        if(text.toLowerCase().startsWith(prefix + "taginfo")) {
    		dontLog.add(message.getIdLong());
            tagInfo(args, channel, guild, member, message);
            return;
        }
        if(text.toLowerCase().startsWith(prefix + "play ")) {
    		dontLog.add(message.getIdLong());
            play(args, channel, tc, guild, member, message, false);
            return;
        }
        if(text.toLowerCase().equals(prefix + "skipall")) {
    		dontLog.add(message.getIdLong());
            skipAll(args, channel, tc, guild, member, message);
            return;
        }
        if(text.toLowerCase().startsWith(prefix + "skip")) {
    		dontLog.add(message.getIdLong());
            skip(args, channel, tc, guild, member, message);
            return;
        }
        if(text.toLowerCase().equals(prefix + "queue")) {
    		dontLog.add(message.getIdLong());
            queue(args, channel, tc, guild, member, message);
            return;
        }
        if(text.toLowerCase().equals(prefix + "pause")) {
    		dontLog.add(message.getIdLong());
            pause(args, channel, tc, guild, member, message);
            return;
        }
        if(text.toLowerCase().startsWith(prefix + "xpleaderboard")) {
    		dontLog.add(message.getIdLong());
        	xPLeaderboard(args, channel, tc, guild, member, message);
            return;
        }
        if(text.toLowerCase().startsWith(prefix + "xp")) {
    		dontLog.add(message.getIdLong());
            xP(args, channel, tc, guild, member, message);
            return;
        }
        if(text.toLowerCase().startsWith(prefix + "latest")) {
    		dontLog.add(message.getIdLong());
            latest(args, channel, tc, guild, member, message);
            return;
        }
        if(text.toLowerCase().startsWith(prefix + "discord")) {
    		dontLog.add(message.getIdLong());
        	channel.sendMessage(successEmbed("Join the official discord! https://discord.gg/EjKR3hD")).queue();
        	return;
        }
        if(text.toLowerCase().startsWith(prefix + "invite")) {
    		dontLog.add(message.getIdLong());
        	channel.sendMessage(successEmbed("Get BonziBot for your server today! https://discordapp.com/oauth2/authorize?client_id=545806922209558537&permissions=8&scope=bot")).queue();
        	return;
        }
        if(text.toLowerCase().startsWith(prefix + "idea ")) {
    		dontLog.add(message.getIdLong());
            idea(args, channel, tc, guild, member, message);
            return;
        }
        if(text.toLowerCase().startsWith(prefix + "report ")) {
    		dontLog.add(message.getIdLong());
            report(args, channel, tc, guild, member, message);
            return;
        }
        if(text.toLowerCase().startsWith(prefix + "countingleaderboard")) {
    		dontLog.add(message.getIdLong());
        	countingLeaderboard(args, channel, tc, guild, member, message);
            return;
        }
        if(text.toLowerCase().equals(prefix + "loop")) {
    		dontLog.add(message.getIdLong());
        	loop(args, channel, tc, guild, member, message);
            return;
        }
        if(text.toLowerCase().equals(prefix + "loopqueue")) {
    		dontLog.add(message.getIdLong());
        	loopQueue(args, channel, tc, guild, member, message);
            return;
        }
        if(text.toLowerCase().equals(prefix + "shuffle")) {
    		dontLog.add(message.getIdLong());
        	shuffle(args, channel, tc, guild, member, message);
            return;
        }
        if(text.toLowerCase().startsWith(prefix + "playlist")) {
    		dontLog.add(message.getIdLong());
        	playlist(args, channel, tc, guild, member, message);
            return;
        }
        if(text.toLowerCase().startsWith(prefix + "modifiers")) {
    		dontLog.add(message.getIdLong());
        	modifiers(args, channel, tc, guild, member, message);
            return;
        }
        if(text.toLowerCase().startsWith(prefix + "scripteditor")) {
    		dontLog.add(message.getIdLong());
        	scriptEditor(args, channel, tc, guild, member, message);
            return;
        }
        if(text.toLowerCase().equals(prefix + "coinflip")) {
    		dontLog.add(message.getIdLong());
    		coinflip(args, channel, tc, guild, member, message);
    		return;
        }
        if(text.toLowerCase().equals(prefix + "acceptnsfw")) {
    		dontLog.add(message.getIdLong());
    		if(isMod(member, guild)) {
    			acceptedNSFW.add(guildID);
    			tc.sendMessage("Enabled risky commands.").queue();
    		}
    		return;
        }
        if(text.toLowerCase().equals(prefix + "disablensfw")) {
    		dontLog.add(message.getIdLong());
    		if(isMod(member, guild)) {
    			acceptedNSFW.remove(guildID);
    			tc.sendMessage("Disabled risky commands.").queue();
    		}
    		return;
        }
        //-------------------fun stuff lmao---------------------------
        if(text.toLowerCase().startsWith(prefix + "coins")) {
    		dontLog.add(message.getIdLong());
        	coins(args, channel, tc, guild, member, message);
            return;
        }
        if(text.toLowerCase().startsWith(prefix + "slots ")) {
    		dontLog.add(message.getIdLong());
        	slots(args, channel, tc, guild, member, message);
            return;
        }
        if(text.toLowerCase().startsWith(prefix + "chance ")) {
    		dontLog.add(message.getIdLong());
        	chance(args, channel, tc, guild, member, message);
            return;
        }
        if(text.toLowerCase().startsWith(prefix + "lottery")) {
    		dontLog.add(message.getIdLong());
        	lottery(args, channel, tc, guild, member, message);
            return;
        }
        if(text.toLowerCase().startsWith(prefix + "pay ")) {
    		dontLog.add(message.getIdLong());
        	pay(args, channel, tc, guild, member, message);
            return;
        }
        if(text.toLowerCase().startsWith(prefix + "shop")) {
    		dontLog.add(message.getIdLong());
        	shop(args, channel, tc, guild, member, message);
            return;
        }
        if(text.toLowerCase().startsWith(prefix + "buy ")) {
    		dontLog.add(message.getIdLong());
        	buy(args, channel, tc, guild, member, message);
            return;
        }
        if(text.toLowerCase().startsWith(prefix + "purchases")) {
    		dontLog.add(message.getIdLong());
        	purchases(args, channel, tc, guild, member, message);
            return;
        }
        if(text.toLowerCase().startsWith(prefix + "speak ")) {
    		dontLog.add(message.getIdLong());
        	speak(args, channel, tc, guild, member, message);
        	return;
        }
        if(text.toLowerCase().startsWith(prefix + "math ")) {
    		dontLog.add(message.getIdLong());
        	// Math(args, channel, tc, guild, member, message);
        }
        if(text.toLowerCase().startsWith(prefix + "stats")) {
    		dontLog.add(message.getIdLong());
        	stats(args, channel, guild, member, message);
        	return;
        }
        if(text.toLowerCase().equals(prefix + "disconnect")) {
    		dontLog.add(message.getIdLong());
        	disconnect(args, channel, tc, guild, member, message);
        	return;
        }
        if(text.toLowerCase().equals(prefix + "upgrades")) {
    		dontLog.add(message.getIdLong());
    		upgrades(args, channel, guild, member, message);
    		return;
        }
        if(text.toLowerCase().startsWith(prefix + "upgrade")) {
    		dontLog.add(message.getIdLong());
    		upgrade(args, channel, guild, member, message);
    		return;
        }
        if(text.toLowerCase().equals(prefix + "prestige")) {
    		dontLog.add(message.getIdLong());
    		prestige(args, channel, guild, member, message);
    		return;
        }
        
        // Premium
        if(text.toLowerCase().startsWith(prefix + "nickall ")) {
    		dontLog.add(message.getIdLong());
        	nickAll(args, channel, tc, guild, member, message);
            return;
        }
        if(text.toLowerCase().startsWith(prefix + "rainbowrole ")) {
    		dontLog.add(message.getIdLong());
        	rainbowRole(args, channel, tc, guild, member, message);
            return;
        }
        if(text.toLowerCase().startsWith(prefix + "superplay ")) {
    		dontLog.add(message.getIdLong());
        	superPlay(args, channel, tc, guild, member, message);
            return;
        }
        if(text.toLowerCase().equalsIgnoreCase(prefix + "expose")) {
    		dontLog.add(message.getIdLong());
        	expose(args, channel, tc, guild, member, message);
            return;
        }
        if(text.toLowerCase().startsWith(prefix + "profilepic")) {
    		dontLog.add(message.getIdLong());
        	profilepic(args, channel, tc, guild, member, message);
            return;
        }
        if(text.toLowerCase().startsWith(prefix + "troll")) {
    		dontLog.add(message.getIdLong());
        	troll(args, channel, tc, guild, member, message);
            return;
        }
        if(text.toLowerCase().startsWith(prefix + "comment")) {
    		dontLog.add(message.getIdLong());
        	comment(args, channel, tc, guild, member, message);
            return;
        }
        
        if(text.toLowerCase().startsWith(prefix + "bisticeaty")) {
    		dontLog.add(message.getIdLong());
        	EmbedBuilder eb = new EmbedBuilder();
        	eb.setColor(new Color(252, 102, 3));
        	eb.setTitle("BISTICEATY");
        	eb.setDescription("Ultimate destroyer of worlds and BonziBot contributor");
        	eb.addField("This command serves as:", " - Recognition of living 13 years\n"
        			+ " - Massive penis\n"
        			+ " - Huge BonziBot helper", false);
        	eb.addBlankField(false);
        	eb.addField("Royal Bisticeaty quote:",
        			"\"Hi lads it's ya boi bisticeaty I've hacked into the"
        			+ "mainframe to bring you this epic message via bonzibot"
        			+ "and it is that you should give me all your money\"", false);
        	
        	eb.setFooter("plus every time someone uses this command bisticeaty gets 10 coins");
        	channel.sendMessage(eb.build()).queue();
        	long bist = 250381056904003585l;
        	if(user.getIdLong() == bist) return;
        	int bc = coins.get(bist);
        	bc+=10;
        	coins.put(bist, bc);
            return;
        }
        
        if(text.toLowerCase().equals(prefix + "saltyneedsmoney")) {
    		dontLog.add(message.getIdLong());
        	channel.sendMessage("ayo the homie salty got 10 coines").queue();
        	long bist = 292755668623163399l;
        	if(user.getIdLong() == bist) return;
        	int bc = coins.get(bist);
        	bc+=10;
        	coins.put(bist, bc);
    		return;
        }
        
        if(scriptCreators.containsKey(guildID)
        && text.toLowerCase().startsWith(prefix)) {
        	ScriptCreator sc = scriptCreators.get(guildID);
        	for(CustomScript script : sc.scripts) {
        		if(script.condition.checkString(text, this, guildID)) {
        			
        			if(script.settings.modOnly && !isMod(member, guild)) {
            			channel.sendMessage(failureEmbed("This script is for moderators only!")).queue();
            			return;
        			}
        			
        			if(script.settings.boosterOnly) {
        				int ups = getUpgrades(guild);
        				List<Long> check = upgraders.get(guildID);
        				if(ups <= 0 || !check.contains(uid)) {
	            			channel.sendMessage(failureEmbed("This script is exclusive for people who upgraded the server! Try the upgrade command!")).queue();
	            			return;
        				}
        			}
        			
        			// do the script thing
        			if(script.result.getActionCount() <= 0) {
            			channel.sendMessage(failureEmbed("This script doesn't have any result actions yet!")).queue();
            			return;
        			}
        			
        			script.result.perform(script, text, tc, user);
        			
        			return;
        		} else if(script.condition.isBadArguments(text, this, guildID)) {
        			channel.sendMessage(failureEmbed("Incorrect arguments! Usage:\n"
        					+ prefix + script.getConditionString())).queue();
        			return;
        		}
        	}
        }
        
        for(Entry<String, String> kv: funMessages.entrySet()) {
        	String test = kv.getKey();
        	String res = kv.getValue();
        	if(text.toLowerCase().contains(test)) {
        		channel.sendMessage(res.replaceAll("\\Q{user}\\E", user.getAsMention())).queue();
        		break;
        	}
        }
    }    
    public void onGuildJoin(GuildJoinEvent e) {
    	bot = e.getJDA();
    	Guild g = e.getGuild();
    	Long gID = g.getIdLong();
    	TextChannel channel = g.getDefaultChannel();
    	
    	System.out.println("Joined new server named \"" + g.getName() + "\". Currently in " + bot.getGuilds().size());
    	
    	channel.sendMessage("I don't think we've been properly introduced.\n"
    	+ "I am Bonzi Buddy!\n"
    	+ "Type **b:help** to get started!").queueAfter(3, TimeUnit.SECONDS);
    	// --------------------------------------------------------------------
    	joinMessages.put(gID, DEFAULT_JOIN);
    	leaveMessages.put(gID, DEFAULT_LEAVE);
    	joinLeaveChannels.put(gID, g
    			.getDefaultChannel()
    			.getIdLong());
    	verifyModRole(g);
    }
    public void onGuildMemberJoin(GuildMemberJoinEvent e) {
    	bot = e.getJDA();
    	Guild g;
    	User u;
    	String mention;
    	String msg;
    	Role role;
    	g = e.getGuild();
    	
    	if(!messagesDisabled(g)) {
        	u = e.getUser();
        	mention = u.getAsMention();
        	verifyJoinLeaveMessages(g);
        	verifyJLChannel(g);
        	msg = getJoinMessage(g);
        	msg = msg.replace("(user)", mention);
        	msg = msg.replace("(server)", g.getName());
        	
        	MessageChannel channel;
        	channel = getJLChannel(g);
        	if(channel == null) {
        		return;
        	}
        	channel.sendMessage(msg).queue();
    	}
    	
    	verifyJoinRole(g);
    	role = getJoinRole(g);
    	
    	try {
        	if(role != null) {
        		g.addRoleToMember(e.getMember(), role).queue();
        	}
    	} catch(Exception exc) {
    		g.getDefaultChannel().sendMessage(failureEmbed("ERROR: I cannot apply the role " + role.getAsMention() + " to anybody because it's higher than me! Move the bonzibot role higher than " + role.getAsMention() + ", please.")).queue();
    	}
    }
    public void onGuildMemberRemove(GuildMemberRemoveEvent e) {
    	bot = e.getJDA();
    	Guild g;
    	User u;
    	String mention;
    	String msg;
    	g = e.getGuild();
    	
    	if(messagesDisabled(g)) {
    		return;
    	}
    	
    	u = e.getUser();
    	mention = u.getName();
    	verifyJoinLeaveMessages(g);
    	verifyJLChannel(g);
    	msg = getLeaveMessage(g);
    	
    	msg = msg.replace("(user)", mention);
    	msg = msg.replace("(server)", g.getName());
    	
    	
    	MessageChannel channel;
    	channel = getJLChannel(g);
    	if(channel == null) {
    		return;
    	}
    	channel.sendMessage(msg).queue();
    }
    public void onGuildMessageReactionRemove(GuildMessageReactionRemoveEvent e) {
    	TextChannel channel = e.getChannel();
    	try {
    		
        	Member member = e.getMember();
        	if(member.getUser().isBot()) { return; }
        	Guild g = e.getGuild();
        	Long mID = e.getMessageIdLong();
        	
        	if(isReactionRole(mID, g)) {
        		ReactionRole r;
        		r = getReactionRole(g,mID);
        		if(r.equals(null)) {
        			System.out.print("Error: ReactionRole is null?\n");
        			return;
        		}
        		Role role;
        		role = r.getRole(g);
        		if(role == null) {
        			long gID = g.getIdLong();
        			List<ReactionRole> rr = getReactionRoles(g);
        			rr.remove(r);
        			reactionRoles.put(gID, rr);
        		}
    			g.removeRoleFromMember(member, role).queue();
    			privateMessage(member, successEmbed("You have removed the **\"" + role.getName() + "\"** role from yourself."));
    			return;
        	}
    	} catch(HierarchyException exc) {
    		channel.sendMessage(failureEmbed("Error: I can't remove this role from the user? (No Permission)")).queue();
    		return;
    	}
    }
    public void onGuildMessageReactionAdd(GuildMessageReactionAddEvent e) {
    	bot = e.getJDA();
		TextChannel channel = e.getChannel();
    	Member member = e.getMember();
    	String mname = getFriendCode(member.getUser());
    	//User user = e.getUser();
    	if(member.getUser().isBot()) { return; }
    	Guild guild = e.getGuild();
    	
    	/*if(channelHasModifier(channel, SCRIPTEDITOR_MODIFIER)) {
    		
    		e.getReaction().removeReaction(user).queue();
    		
            if(!isMod(member, guild)) {
            	channel.sendMessage(failureEmbed("You don't have permission to use the scriptcreator!")).queue(m -> {
            		m.delete().queueAfter(5, TimeUnit.SECONDS);
            	});
            	return;
            }
    		
    		long gID = guild.getIdLong();
    		if(!scriptCreators.containsKey(gID)) {
    			channel.sendMessage(failureEmbed("Invalid channel! Delete this channel and properly open a scripting workspace by using the \"scripteditor\" command!")).queue();
    			return;
    		}
    		ScriptCreator thisServer = scriptCreators.get(gID);
    		thisServer.lastMember = member;
    		thisServer.receiveActionReaction(e.getReactionEmote().getName(), channel);
    		scriptCreators.put(gID, thisServer);
    		return;
    	}*/
    	try {
        	long mID = e.getMessageIdLong();
        	if(isReactionRole(mID, guild)) {
        		ReactionRole r;
        		r = getReactionRole(guild,mID);
        		if(r.equals(null)) {
        			System.out.print("Error: ReactionRole is null?\n");
        			return;
        		}
        		Role role;
        		role = r.getRole(guild);
        		if(role == null) {
        			long gID = guild.getIdLong();
        			List<ReactionRole> rr = getReactionRoles(guild);
        			rr.remove(r);
        			reactionRoles.put(gID, rr);
        		}
    			guild.addRoleToMember(member, role).queue();
    			privateMessage(member, successEmbed("You have received the **\"" + role.getName() + "\"** role!"));
    			return;
        	} else {
        		LoggingEntry le = getLoggingEntryByID(mID);
        		if(le == null) { return; }
        		if(!isMod(member, guild)) {
        			channel.sendMessage(member.getAsMention() + 
        			", you do not have permission to do this!").queue(mm -> {
        				mm.delete().queueAfter(5, TimeUnit.SECONDS);
        			});
        			return;
        		}
        		channel.retrieveMessageById(mID).queue(msg -> {
        			List<MessageEmbed> embeds = msg.getEmbeds();
        			if(embeds.isEmpty()) { System.out.println("Logging message's embeds are empty??"); return; }
        			MessageEmbed me = embeds.get(0);
        			EmbedBuilder eb = new EmbedBuilder(me);
        			
        			String n = e.getReactionEmote().getName();
        			long tgId = le.targetUser;        			
        			
        			Member tg = guild.getMemberById(tgId);
        			if(tg == null && !n.equalsIgnoreCase("b_undo")) {
        				eb.setTitle("(User not in discord anymore.)\n" + me.getTitle());
        				msg.editMessage(eb.build()).queue();
        				return;
        			}
        			
        			if(n.equalsIgnoreCase("b_warn")) {
        				pagination_Warn(member, tg, le.reason);
        				tg.getUser().openPrivateChannel().queue(ch -> {
        					ch.sendMessage("You've been warned in " + guild.getName() + " for: `" + le.reason + "`.").queue();
        				});
        				String ttl = me.getTitle();
        				String[] spl = ttl.split("\n");
        				ttl = spl[spl.length-1];
        				eb.setTitle("(WARNED BY " + mname + ")\n" + ttl);
        				msg.editMessage(eb.build()).queue();
        				return;
        			} else if(n.equalsIgnoreCase("b_mute")) {
        				if(!pagination_Mute(member, tg, le.reason)) {
            				tg.getUser().openPrivateChannel().queue(ch -> {
            					ch.sendMessage("You've been muted in " + guild.getName() + " for: `" + le.reason + "`.").queue();
            				});
            				String ttl = me.getTitle();
            				String[] spl = ttl.split("\n");
            				ttl = spl[spl.length-1];
            				eb.setTitle("(MUTED BY " + mname + ") Press Again to Unmute.\n" + ttl);
            				msg.editMessage(eb.build()).queue();
            				return;
        				} else {
            				String ttl = me.getTitle();
            				String[] spl = ttl.split("\n");
            				ttl = spl[spl.length-1];
            				eb.setTitle("(UNMUTED BY " + mname + ") Press Again to Mute.\n" + ttl);
            				msg.editMessage(eb.build()).queue();
            				return;
        				}
        			} else if(n.equalsIgnoreCase("b_kick")) {
        				tg.getUser().openPrivateChannel().queue(ch -> {
        					ch.sendMessage("You've been kicked from " + guild.getName() + " for: `" + le.reason + "`.").queue();
        				});
        				pagination_Kick(member, tg, le.reason);
        				String ttl = me.getTitle();
        				String[] spl = ttl.split("\n");
        				ttl = spl[spl.length-1];
        				eb.setTitle("(KICKED BY " + mname + ")\n" + ttl);
        				msg.editMessage(eb.build()).queue();
        				return;
        			} else if(n.equalsIgnoreCase("b_ban")) {
        				tg.getUser().openPrivateChannel().queue(ch -> {
        					ch.sendMessage("You've been banned from " + guild.getName() + " for: `" + le.reason + "`.").queue();
        				});
        				pagination_Ban(member, tg, le.reason);
        				String ttl = me.getTitle();
        				String[] spl = ttl.split("\n");
        				ttl = spl[spl.length-1];
        				eb.setTitle("(BANNED BY " + mname + ")\n" + ttl);
        				msg.editMessage(eb.build()).queue();
        				return;
        			} else if(n.equalsIgnoreCase("b_undo")) {
        				if(le.type == LogType.NickChange) {
        					String old = le.ext;
        					ignoreNickChange = tg.getUser().getIdLong();
        					guild.modifyNickname(tg, old).queue();
        					
            				String ttl = me.getTitle();
            				eb.setTitle("(UNDONE BY " + mname + ")\n" + ttl);
            				msg.editMessage(eb.build()).queue();
            				
            				if(tg != null) {
	            				tg.getUser().openPrivateChannel().queue(ch -> {
	            					ch.sendMessage("Your nickname has been reverted back to *\"" + old + "\"* on the " + guild.getName() + " server.").queue();
	            				});
            				}
            				logs.remove(le);
        				}
        				if(le.type == LogType.PrefixChange) {
        					String old = le.ext;
        					prefixes.put(guild.getIdLong(), old);
        					
            				String ttl = me.getTitle();
            				eb.setTitle("(REVERTED BACK TO \"" + old + "\" BY " + mname + ")\n" + ttl);
            				msg.editMessage(eb.build()).queue();
            				logs.remove(le);
        				}
        				if(le.type == LogType.TextChannelCreate) {
        					long id = le.targetUser; // Actually the channel ID, not a user ID.
        					TextChannel tc = guild.getTextChannelById(id);
        					tc.delete().queue();
        					
            				String ttl = me.getTitle();
            				eb.setTitle("(DELETED BY " + mname + ")\n" + ttl);
            				msg.editMessage(eb.build()).queue();
            				logs.remove(le);
        				}
        				if(le.type == LogType.TextChannelRemove) {
        					String name = le.reason;
        					String topic = le.ext;
        					guild.createTextChannel(name).queue(tc -> {
        						if(topic != null) {
        							tc.getManager()
        								.setTopic(topic)
        								.queue();
        						}
        					});
        					
            				String ttl = me.getTitle();
            				eb.setTitle("(RESTORED BY " + mname + ")\n" + ttl);
            				msg.editMessage(eb.build()).queue();
            				logs.remove(le);
        				}
        				if(le.type == LogType.VoiceChannelCreate) {
        					long id = le.targetUser; // Actually the channel ID, not a user ID.
        					VoiceChannel tc = guild.getVoiceChannelById(id);
        					tc.delete().queue();
        					
            				String ttl = me.getTitle();
            				eb.setTitle("(DELETED BY " + mname + ")\n" + ttl);
            				msg.editMessage(eb.build()).queue();
            				logs.remove(le);
        				}
        				if(le.type == LogType.VoiceChannelRemove) {
        					String name = le.reason;
        					String _limit = le.ext;
        					int limit = Integer.parseInt(_limit);
        					guild.createVoiceChannel(name).queue(vc -> {
        						vc.getManager().setUserLimit(limit).queue();
        					});
        					
            				String ttl = me.getTitle();
            				eb.setTitle("(RESTORED BY " + mname + ")\n" + ttl);
            				msg.editMessage(eb.build()).queue();
            				logs.remove(le);
        				}
        				if(le.type == LogType.Ban) {
        					long id = le.targetUser;
        					guild.unban(String.valueOf(id)).queue();
        					
            				String ttl = me.getTitle();
            				eb.setTitle("(UNBANNED BY " + mname + ")\n" + ttl);
            				msg.editMessage(eb.build()).queue();
            				logs.remove(le);
        				}
        				if(le.type == LogType.Unban) {
        					long id = le.targetUser;
        					guild.ban(String.valueOf(id), 3).queue();
        					
            				String ttl = me.getTitle();
            				eb.setTitle("(RE-BANNED BY " + mname + ")\n" + ttl);
            				msg.editMessage(eb.build()).queue();
            				logs.remove(le);
        				}
        				if(le.type == LogType.Mute) {
        					long id = le.targetUser;
        					long invId = le.invokedUser;
        					performUnmute(guild.getMemberById(id), guild.getMemberById(invId));
        					
            				String ttl = me.getTitle();
            				eb.setTitle("(UNMUTED BY " + mname + ")\n" + ttl);
            				msg.editMessage(eb.build()).queue();
            				logs.remove(le);
        				}
        				if(le.type == LogType.Unmute) {
        					long id = le.targetUser;
        					long invId = le.invokedUser;
        					performMute(guild.getMemberById(id), guild.getMemberById(invId));
        					
            				String ttl = me.getTitle();
            				eb.setTitle("(RE-MUTED BY " + mname + ")\n" + ttl);
            				msg.editMessage(eb.build()).queue();
            				logs.remove(le);
        				}
        				return;
        			}
        		});
        	}
    	} catch(HierarchyException he) {
    		e.getChannel().sendMessage(failureEmbed("Error: I can't add this role to the user! (No Permission)")).queue();
    		return;
    	}
    }
    public void onGenericGuildVoice(GenericGuildVoiceEvent e) {
    	bot = e.getJDA();
    	if(e.getGuild().getSelfMember().getVoiceState().inVoiceChannel()) {
    		Guild g = e.getGuild();
        	GuildVoiceState vs;
        	vs = g.getSelfMember().getVoiceState();
        	VoiceChannel vc = vs.getChannel();
        	
        	if(vc.getMembers().size() == 1) {
            	g.getAudioManager().closeAudioConnection();
            	GuildMusicManager gmm;
            	gmm = getGuildAudioPlayer(g);
            	gmm.scheduler.queue.clear();
            	skipTrack(gmm, true);
        	}
    	}
    }
    public void onGuildMessageDelete(GuildMessageDeleteEvent e) {
    	bot = e.getJDA();
    	Guild g = e.getGuild();
    	long id = e.getMessageIdLong();
    	Message m = fetchMessageFromCache(g, id);
    	
    	if(m == null)
    		return;
    	
    	long gid = g.getIdLong();
    	long aid = m.getAuthor().getIdLong();
    	
    	if(m.getAuthor().isBot())
    		return;
    	
    	if(m.getContentRaw().length() > 3) {
    		exposed.put(g.getIdLong(), m);
    	}
    	
    	TextChannel channel = e.getChannel();
    	if(channelHasModifier(channel, "nologging") &&
    			!channelHasModifier(channel, "logging")) {
    		return;
    	}
    	
    	if(!dontLog.contains(id)) {
    		if(!recentBans.containsKey(gid)) {
    			recentBans.put(gid, new ArrayList<Long>());
    		}
    		List<Long> bans = recentBans.get(gid);
    		if(!bans.contains(aid)) {
            	LoggingEntry entry = getDeleteLog(m);
            	tryLog(g, entry, "b_warn", "b_mute", "b_kick", "b_ban");
    		}
    	}
    }
    public void onGuildMemberUpdateNickname(GuildMemberUpdateNicknameEvent e) {
    	
    	String current = e.getNewNickname();
    	if(current == null) {
    		current = e.getMember().getUser().getName();
    	}
    	String low = current.toLowerCase();
    	String old = e.getOldNickname();
    	if(old == null) {
    		old = e.getMember().getUser().getName();
    	}
    	Guild g = e.getGuild();
    	Member m = e.getMember();
    	User u = m.getUser();
    	
    	if(u.getIdLong() == ignoreNickChange) {
    		ignoreNickChange = 0l;
    		return;
    	}
    	
    	LoggingEntry le = new LoggingEntry();
    	le.targetUser = u.getIdLong();
    	le.invokedUser = u.getIdLong();
    	le.reason = "Changing nickname to \"" + current + "\".";
    	le.ext = old; // Reason here is the old nickname.
    	le.type = LogType.NickChange;
    	
    	EmbedBuilder eb = baseLogEmbed("User changed " +
		 "nickname.", u, Color.green);
    	eb.setDescription("New Nickname: " + current +
		"\n" + "Old Nickname: " + old + "\n\n"
		+ "Press undo to change this user back.");
    	le.embed = eb.build();
    	
    	tryLog(g, le, "b_undo", "b_warn");
    	
    	List<String> filter = getFilter(g);
    	if(filter.isEmpty()) {
    		return;
    	}
    	for(String swear : filter) {
    		String s = swear.toLowerCase();
    		if(low.contains(s)) {
    			try {
        			m.modifyNickname(old).queue(success -> {
            			u.openPrivateChannel().queue(ch -> {
            				ch.sendMessage(failureEmbed("Your new nickname contained words"
            					+ " that are filtered in **" + g.getName() + "**!")).queue();
            			});
        			});
        			return;
    			} catch(HierarchyException exc) {
        			u.openPrivateChannel().queue(ch -> {
        				ch.sendMessage(failureEmbed("Your new nickname contains words"
        					+ " that are filtered in **" + g.getName() + "**, but I don't have permission to change your nickname. Would be appreciated if you could change it to something else!")).queue();
        			});
    			}
    		}
    	}
    }
    public void onTextChannelCreate(TextChannelCreateEvent e) {
    	// Automatically set muted role permissions.
    	bot = e.getJDA();
    	Guild g = e.getGuild();
    	Role muted = getMutedRole(g);
    	
    	TextChannel channel = e.getChannel();
		try {
			ChannelManager cm = channel.getManager();
			cm.putPermissionOverride(muted,
				0l, // allow
				Permission.MESSAGE_WRITE.getRawValue()).queue(); // deny
		} catch(Exception exc) {}
		
		// Log
    	LoggingEntry le = new LoggingEntry();
    	le.targetUser = channel.getIdLong();
    	le.invokedUser = channel.getIdLong();
    	le.reason = "This warn should not exist.";
    	le.ext = null;
    	le.type = LogType.TextChannelCreate;
    	
    	EmbedBuilder eb = baseLogEmbed("New text channel created.", null, Color.orange);
    	eb.setDescription("Name: " + channel.getName() +
    			"\nPress undo to undo this action.");
    	le.embed = eb.build();
    	
    	tryLog(g, le, "b_undo");
    }
    
    HashMap<Long, List<Long>> recentBans = new HashMap<Long, List<Long>>();
    // Logging Events
    public void onGuildBan(GuildBanEvent e) {
    	bot = e.getJDA();
    	User u = e.getUser();
    	Guild g = e.getGuild();
    	long gid = g.getIdLong();
    	
    	if(!recentBans.containsKey(gid)) {
    		recentBans.put(gid, new ArrayList<Long>());
    	}
    	
    	List<Long> rcb = recentBans.get(gid);
    	recentBans.put(gid, rcb);
    	
    	LoggingEntry le = new LoggingEntry();
    	le.targetUser = u.getIdLong();
    	le.invokedUser = u.getIdLong();
    	le.reason = "This warn should not exist.";
    	le.ext = null;
    	le.type = LogType.Ban;
    	
    	EmbedBuilder eb = baseLogEmbed("User was banned.", null, Color.red);
    	eb.setDescription("Name: " + getFriendCode(u) +
    			"\nPress undo to unban this user.");
    	le.embed = eb.build();
    	
    	tryLog(g, le, "b_undo");
    }
    public void onGuildUnban(GuildUnbanEvent e) {
    	bot = e.getJDA();
    	User u = e.getUser();
    	Guild g = e.getGuild();
    	long gid = g.getIdLong();
    	
    	if(!recentBans.containsKey(gid)) {
    		recentBans.put(gid, new ArrayList<Long>());
    	}
    	
    	List<Long> rcb = recentBans.get(gid);
    	recentBans.put(gid, rcb);
    	
    	LoggingEntry le = new LoggingEntry();
    	le.targetUser = u.getIdLong();
    	le.invokedUser = u.getIdLong();
    	le.reason = "This warn should not exist.";
    	le.ext = null;
    	le.type = LogType.Unban;
    	
    	EmbedBuilder eb = baseLogEmbed("User was unbanned.", null, Color.red);
    	eb.setDescription("Name: " + getFriendCode(u) +
    			"\nPress undo to re-ban this user.");
    	le.embed = eb.build();
    	
    	tryLog(g, le, "b_undo");
    }
    public void onTextChannelDelete(TextChannelDeleteEvent e) {
    	bot = e.getJDA();
    	Guild g = e.getGuild();
    	TextChannel tc = e.getChannel();
    	
    	LoggingEntry le = new LoggingEntry();
    	le.targetUser = tc.getIdLong();
    	le.invokedUser = tc.getIdLong();
    	le.reason = tc.getName();
    	le.ext = tc.getTopic();
    	le.type = LogType.TextChannelRemove;
    	
    	EmbedBuilder eb = baseLogEmbed("Text channel deleted.", null, Color.orange);
    	eb.setDescription("Name: " + tc.getName() +
    			"\nTopic: " + tc.getTopic() +
    			"\nPress undo to restore this channel.");
    	le.embed = eb.build();
    	
    	tryLog(g, le, "b_undo");
    }
    public void onVoiceChannelCreate(VoiceChannelCreateEvent e) {
    	bot = e.getJDA();
    	Guild g = e.getGuild();
    	VoiceChannel vc = e.getChannel();
    	
    	LoggingEntry le = new LoggingEntry();
    	le.targetUser = vc.getIdLong();
    	le.invokedUser = vc.getIdLong();
    	le.reason = vc.getName();
    	le.ext = null;
    	le.type = LogType.VoiceChannelCreate;
    	
    	EmbedBuilder eb = baseLogEmbed("Voice channel created.", null, Color.orange);
    	eb.setDescription("Name: " + vc.getName() +
    			"\nPress undo to undo this action.");
    	le.embed = eb.build();
    	
    	tryLog(g, le, "b_undo");
    }
    public void onVoiceChannelDelete(VoiceChannelDeleteEvent e) {
    	bot = e.getJDA();
    	Guild g = e.getGuild();
    	VoiceChannel vc = e.getChannel();
    	
    	LoggingEntry le = new LoggingEntry();
    	le.targetUser = vc.getIdLong();
    	le.invokedUser = vc.getIdLong();
    	le.reason = vc.getName();
    	le.ext = String.valueOf(vc.getUserLimit());
    	le.type = LogType.VoiceChannelRemove;
    	
    	String lmt;
    	if(vc.getUserLimit() == 0)
    		lmt = "None.";
    	else
    		lmt = le.ext;
    	
    	EmbedBuilder eb = baseLogEmbed("Voice channel deleted.", null, Color.orange);
    	eb.setDescription("Name: " + vc.getName() +
    			"\nUser Limit: " + lmt +
    			"\nPress undo to undo this action.");
    	le.embed = eb.build();
    	
    	tryLog(g, le, "b_undo");
    }
    
    // Logging Pagination
    public void pagination_Warn(Member warner, Member target, String reason) {
    	UserProfile pf = getProfile(target);
        pf.addWarn(reason, warner.getUser());
        setProfile(target, pf);
    }
    public boolean pagination_Mute(Member muter, Member target, String reason) { // returns if unmuted
    	Guild g = target.getGuild();
    	List<Role> roles = target.getRoles();
    	Role muted = getMutedRole(g);
    	
    	// Has the muted role?
    	if(roles.stream().anyMatch(r -> r.
    	getIdLong()==muted.getIdLong())) {
    		performUnmute(target, muter);
    		return true;
    	}
    	performMute(target, muter);
    	return false;
    }
    public void pagination_Kick(Member warner, Member target, String reason) {
    	Guild g = target.getGuild();
    	g.kick(target).reason(reason).queue();
    	return;
    }
    public void pagination_Ban(Member warner, Member target, String reason) {
    	Guild g = target.getGuild();
    	g.ban(target, 3).reason(reason).queue();
    	return;
    }
    
    public void admin_Servers(String[] args, MessageChannel channel, TextChannel tc, Guild g, Member member, Message message) {
		if(!isAdmin(member.getUser())) {
			return;
		}
		EmbedBuilder eb = new EmbedBuilder();
		eb.setColor(Color.magenta);
		eb.setTitle("Top servers that contain bonzibot!:");
		List<Guild> _guilds;
		JDA jda = channel.getJDA();
		_guilds = jda.getGuilds();
		eb.setDescription("Showing 10/" + _guilds.size() + " guilds.");
		List<Guild> guilds = new ArrayList<Guild>(_guilds);
		
		Comparator<Guild> c = new Comparator<Guild>() {
			@Override
			public int compare(Guild o1, Guild o2) {
				Integer count1 = o1.getMembers().size();
				Integer count2 = o2.getMembers().size();
				return count1.compareTo(count2);
			}
		};
		Collections.sort(guilds, Collections.reverseOrder(c));
		int i = 0;
		for(Guild _g : guilds) {
			i++; if(i > 10) {break;}
			String name = _g.getName();
			int count = _g.getMembers().size();
			OffsetDateTime join = _g.
				getSelfMember().getTimeJoined();
			LocalDate ld = join.toLocalDate();
			String joined =
				ld.getMonthValue() + "/" +
				ld.getYear() + "/" +
				ld.getDayOfMonth();
			String id = _g.getId();
			String ownername = _g.getOwner().getEffectiveName();
			
			eb.addField(i + ". " + name,
				"Member Count: " + count + "\n"
				+ "Join Date: " + joined + "\n"
				+ "ID: " + id + "\n"
				+ "Owner: " + ownername, false);
		}
		channel.sendMessage(eb.build()).queue();
		return;
    }
    public void admin_CCBan(String[] args, MessageChannel channel, TextChannel tc, Guild g, Member member, Message message) {
    	User user = member.getUser();
		if(!isAdmin(user)) {
			return;
		}
		Long userid = user.getIdLong();
		if(ccBans.contains(userid)) {
			channel.sendMessage("You're banned from using tags/unbanning yourself (lol). Ask somebody else to unban you if you think this is unfair.").queue();
			return;
		}
		if(args.length != 2) {
			channel.sendMessage("Usage: admin:ccban <id>").queue();
			return;
		}
		String idString = args[1];
		Long id;
		try {
			id = Long.parseLong(idString);
		} catch(Exception e) {
			channel.sendMessage("That's not an id.").queue();
			return;
		}
		if(g.getJDA().getUserById(id) == null) {
			channel.sendMessage("Can't find a user with that id.").queue();
			return;
		}
		if(ccBans.contains(id)) {
			channel.sendMessage("This user is already ccbanned.").queue();
			return;
		}
		ccBans.add(id);
		List<CustomCommand> removeQueue = 
			new ArrayList<CustomCommand>();
		for(CustomCommand cmd : ccm.global) {
			if(cmd.creatorID.equals(id)) {
				removeQueue.add(cmd);
				continue;
			}
		}
		for(CustomCommand cc : removeQueue) {
			ccm.global.remove(cc);
		}
		channel.sendMessage("Okay, user with the ID " + id + " is now **banned** from using tags.\n"
			+ "(Deleted " + removeQueue.size() + " of their tags.)").queue();
		return;
    }
    public void admin_CCUnban(String[] args, MessageChannel channel, TextChannel tc, Guild g, Member member, Message message) {
    	User user = member.getUser();
		if(!isAdmin(user)) {
			return;
		}
		Long userid = user.getIdLong();
		if(ccBans.contains(userid)) {
			channel.sendMessage("You're banned from using tags/unbanning yourself (lol). Ask somebody else to unban you if you think this is unfair.").queue();
			return;
		}
		if(args.length != 2) {
			channel.sendMessage("Usage: admin:ccunban <id>").queue();
			return;
		}
		String idString = args[1];
		Long id;
		try {
			id = Long.parseLong(idString);
		} catch(Exception e) {
			channel.sendMessage("That's not an id.").queue();
			return;
		}
		if(g.getJDA().getUserById(id) == null) {
			channel.sendMessage("Can't find a user with that id.").queue();
			return;
		}
		if(!ccBans.contains(id)) {
			channel.sendMessage("This user is not ccbanned.").queue();
			return;
		}
		ccBans.remove(id);
		channel.sendMessage("Okay, user with the ID " + id + " is now **unbanned** from using tags.").queue();
		return;
    }
    public void admin_CheckVersion(String[] args, MessageChannel channel, TextChannel tc, Guild guild, Member member, Message message) {
    	JDA bot = guild.getJDA();
    	for(Guild g : bot.getGuilds()) {
    		HashMap<Long, UserProfile> profile;
    		if(!profiles.containsKey(g.getIdLong())) {continue;}
    		profile = profiles.get(g.getIdLong());
    		for(Member m : g.getMembers()) {
    			UserProfile up;
    			Long id = m.getUser().getIdLong();
    			if(!profile.containsKey(id)) {continue;}
    			up = profile.get(id);
    			up.CheckVersion();
    			profile.put(id, up);
    		}
    		profiles.put(g.getIdLong(), profile);
    	}
    	channel.sendMessage("Okay! All global data is up-to-date.").queue();
    }
    
    // RPG
    public HashMap<Long, List<RpgItem>> shopResponses = new HashMap<Long, List<RpgItem>>();
    public void RPG(String[] args, MessageChannel channel, TextChannel tc, Guild g, Member member, Message message) {
    	long gID = g.getIdLong();
    	String prefix = prefixes.get(gID);
    	String text = message.getContentRaw();
    	if(text.toLowerCase().startsWith(prefix.toLowerCase())) {
    		message.delete().queue();
    		channel.sendMessage(failureEmbed("You don't need to use prefixes in the RPG channel!")).queue(msg -> {
    			msg.delete().queueAfter(3, TimeUnit.SECONDS);
    		});
    		return;
    	}
    	User u = member.getUser();
    	long uID = u.getIdLong();
    	if(!rpgPlayers.containsKey(uID)) {
    		rpgPlayers.put(uID, new RpgPlayer(uID));
    		channel.sendMessage(successEmbed("You've started your RPG adventure! Type \"help\" to get started!")).queue();
    		return;
    	}
    	RpgPlayer player = rpgPlayers.get(uID);
    	
    	if(shopResponses.containsKey(uID)) {
    		RPG_ShopResponse(args, channel, tc, g, member, message, player, shopResponses.get(uID));
    	}
    	
    	String cmd = args[0].toLowerCase();
    	if(cmd.equalsIgnoreCase("help")) {
    		EmbedBuilder eb = new EmbedBuilder();
    		eb.setColor(Color.magenta);
    		eb.setTitle("RPG Commands");
    		eb.setDescription("This is a list of all the commands that can be done in the game!");
    		
    		eb.addField("Item Commands", "Commands that use items!", false);
    		eb.addField("🗺️ Inventory", "Usage: inventory", true);
    		eb.addField("🖐️ Equip", "Usage: equip <item>\nEquips an item.", true);
    		eb.addField("🤺 Attack", "Usage: attack @<user>\nAttacks the selected user with the equipped item.", true);
    		eb.addField("🔨 Use", "Usage: use\nUses the equipped item.", true);
    		
    		eb.addBlankField(false);
    		eb.addField("Action Commands", "Commands that use items!", false);
    		eb.addField("🛡️ Adventure", "Usage: adventure\nGo on an adventure and find items, npcs, and more!", true);
    		eb.addField("🕔 Wait", "Usage: wait\nCounts as taking an action. Restores 1 mana.", true);
    		eb.addField("📊 Stats", "Usage: stats (OR) stats @<user>\nShows yours/other person's current status (hp, mana, etc...)", true);
    		eb.addField("❓ Info", "Usage: info <item>\nGives information about the selected item.", true);
    		
    		channel.sendMessage(eb.build()).queue();
    		return;
    	}
    	if(cmd.equalsIgnoreCase("inventory")) {
    		RPG_Inventory(args, channel, tc, g, member, message, player);
    		return;
    	}
    	if(cmd.equalsIgnoreCase("equip")) {
    		RPG_Equip(args, channel, tc, g, member, message, player);
    		return;
    	}
    	if(cmd.equalsIgnoreCase("attack")) {
    		RPG_Attack(args, channel, tc, g, member, message, player);
    		return;
    	}
    	if(cmd.equalsIgnoreCase("use")) {
    		RPG_Use(args, channel, tc, g, member, message, player);
    		return;
    	}
    	if(cmd.equalsIgnoreCase("wait")) {
    		RPG_Wait(args, channel, tc, g, member, message, player);
    		return;
    	}
    	if(cmd.equalsIgnoreCase("stats")) {
    		RPG_Stats(args, channel, tc, g, member, message, player);
    		return;
    	}
    	if(cmd.equalsIgnoreCase("adventure")) {
    		RPG_Adventure(args, channel, tc, g, member, message, player);
    		return;
    	}
    	if(cmd.equalsIgnoreCase("info")) {
    		RPG_Info(args, channel, tc, g, member, message, player);
    		return;
    	}
    }
    public void RPG_ShopResponse(String[] args, MessageChannel channel, TextChannel tc, Guild g, Member member, Message message, RpgPlayer player, List<RpgItem> items) {
    	RpgInventory virtualInv = new RpgInventory();
    	virtualInv.contents.addAll(items);
    	
    	User u = member.getUser();
    	long uID = u.getIdLong();
    	
    	String request = message.getContentRaw();
    	if(request.toLowerCase().contains("cancel")) {
    		shopResponses.remove(uID);
    		channel.sendMessage(successEmbed("You declined the offer and went home.")).queue();
    		return;
    	}
    	// searchForItem's description is optional.
    	RpgItem picked = virtualInv.searchForItem(request, "");
    	if(picked == null) {
    		channel.sendMessage(failureEmbed("Can't find the item you're looking for! (make shure you speld it rite!)")).queue();
    		return;
    	}
    	int price = (int) Math.round((double)picked.power*2.5);
    	int coins = getCoins(u);
    	if(price > coins) {
    		channel.sendMessage(failureEmbed("You can't afford this item! (balance: " + coins + " coins.)")).queue();
    		return;
    	}
    	subtractCoins(u, price);
		channel.sendMessage(successEmbed("Successfully purchased a(n) " + picked.name + "! Your balance is now " + (coins-price) + ".")).queue();
		player.inv.addItem(picked);
		shopResponses.remove(uID);
		return;
    }
    
    public void RPG_Inventory(String[] args, MessageChannel channel, TextChannel tc, Guild g, Member member, Message message, RpgPlayer player) {
    	RpgInventory inv = player.inv;
    	channel.sendMessage(inv.toEmbed()).queue();
    	player.retaliate();
    }
    public void RPG_Equip(String[] args, MessageChannel channel, TextChannel tc, Guild g, Member member, Message message, RpgPlayer player) {
    	RpgInventory inv = player.inv;
    	String iname = combine(args, 1);
    	if(iname == null || iname == "") {
    		channel.sendMessage(successEmbed("Unequipped current item.")).queue();
    		player.equippedItem = null;
    		return;
    	}
    	RpgItem i = inv.searchForItem(iname, "");
    	if(i == null) {
    		channel.sendMessage(failureEmbed("Can't find the item you're trying to equip (did you speel it rite?)")).queue();
    		return;
    	}
    	player.equippedItem = i;
    	player.retaliate();
    	channel.sendMessage(successEmbed("Successfully equipped \"" + i.name + "\"!")).queue();
    }
    public void RPG_Attack(String[] args, MessageChannel channel, TextChannel tc, Guild g, Member member, Message message, RpgPlayer player) {
    	User u = member.getUser();
    	List<Member> _mentioned = message.getMentionedMembers();
    	if(_mentioned.isEmpty()) {
    		channel.sendMessage(failureEmbed("Make sure you @mention the user you want to attack!")).queue();
    		return;
    	}
    	Member mentioned = _mentioned.get(0);
    	User _target = mentioned.getUser();
    	long targetID = _target.getIdLong();
    	if(!rpgPlayers.containsKey(targetID)) {
    		int r = random(2);
    		if(r == 0) {
    			channel.sendMessage(failureEmbed("This person seems oddly invulnerable... almost as if he hasn't joined RPG yet!")).queue();
    		} else {
    			channel.sendMessage(failureEmbed("You tried to attack him and your weapon went right through him... He replied with \"bruh i havent even started playing yet\".")).queue();
    		}
    		return;
    	}
    	RpgPlayer target = rpgPlayers.get(targetID);
    	
    	RpgItem equipped = player.equippedItem;
    	if(equipped == null) {
    		channel.sendMessage(failureEmbed("You don't have anything equipped! (Type \"equip <item>\" to equip one!")).queue();
    		return;
    	}
    	// Only allow use of attack item types.
    	if(equipped.type != RpgItemType.Single_Use_Weapon
		&& equipped.type != RpgItemType.Spell
		&& equipped.type != RpgItemType.Weapon) {
    		channel.sendMessage(failureEmbed("You must attack with a weapon or spell! (Type \"use\" to use the currently equipped item!)")).queue();
    		return;
    	}
    	if(equipped.type == RpgItemType.Spell) {
    		if(!player.hasEnoughMana(equipped)) {
        		channel.sendMessage(failureEmbed("You don't have enough mana to use that spell! Perform other actions to regain mana! (Example: like typing \"wait\" or \"equip <item>\")")).queue();
        		return;
    		}
    	}
    	if(target.wasAttacked) {
    		channel.sendMessage(failureEmbed("Wait for this player to take an action!")).queue();
    		return;
    	}
    	int _crit = random(7);
    	boolean crit = _crit == 6;
    	
    	int pow = equipped.power;
    	if(crit) { pow*=2; }
    	
    	player.attackWith(equipped);
    	player.wasAttacked = false;
    	player.tryEffectDamage(false);
    	target.tryEffectDamage(false);
    	target.damage(pow);
    	
    	String exclamation = null;
    	int picked = random(5);
    	switch(picked) {
    	case 0:
    		exclamation = "Pow!";
    		break;
    	case 1:
    		exclamation = "Ouch!";
    		break;
    	case 2:
    		exclamation = "Bam!";
    		break;
    	case 3:
    		exclamation = "Boom!";
    		break;
    	case 4:
    		exclamation = "Wham!";
    		break;
    	}
    	if(exclamation == null) {
    		channel.sendMessage(failureEmbed("bruh luke made an oopsie and decided he would not code in the fifth exclamation")).queue();
    		return;
    	}
    	if(crit) {
    		exclamation += " (CRITICAL HIT!)";
    	}
    	String spellText = "";
    	if(equipped.type == RpgItemType.Spell) {
    		spellText = "\n" + member.getAsMention() + " is now at " + player.mana + " mana.";
    	}
    	
    	if(target.isDead()) {
    		String lostText = "";
    		List<RpgItem> con = target.inv.contents;
    		int lostCoins = random(20)+20; // 20-40
    		
    		if(!con.isEmpty()) {
        		RpgItem lost = con.get(random(con.size()));
        		lostText = "\n\n" + mentioned.getAsMention() + " lost their " + lost.name
        				+ " and " + lostCoins + " coins, but respawned anyways!";
        		target.inv.removeItem(lost);
    		}
        	EmbedBuilder eb = new EmbedBuilder();
        	eb.setColor(mentioned.getColor());
        	eb.setTitle(exclamation);
        	
        	// this line is so ugly i swear
        	eb.setDescription(member.getAsMention() + " finished off " + 
        		mentioned.getAsMention() + " with their " + equipped.name + "!\n"
        		
        		+ "The attack dealt " + pow + " damage, leaving " + 
        		mentioned.getAsMention() + " dead!" + spellText + lostText);
        	addCoins(u, lostCoins);
        	subtractCoins(mentioned.getUser(), lostCoins);
        	
        	target.dead();
        	
        	channel.sendMessage(eb.build()).queue();
        	return;
    	}
    	EmbedBuilder eb = new EmbedBuilder();
    	eb.setColor(mentioned.getColor());
    	eb.setTitle(exclamation);
    	
    	// this line is also so ugly i swear
    	eb.setDescription(member.getAsMention() + " attacked " + 
    		mentioned.getAsMention() + " with their " + equipped.name + "!\n"
    		
    		+ "The attack dealt " + pow + " damage, leaving " + mentioned.getAsMention()
    		+ " at " + target.health + "/" + target.maxHealth + " HP." + spellText);
    	
    	channel.sendMessage(eb.build()).queue();
    }
    public void RPG_Use(String[] args, MessageChannel channel, TextChannel tc, Guild g, Member member, Message message, RpgPlayer player) {
    	RpgItem equipped = player.equippedItem;
    	if(equipped == null) {
    		channel.sendMessage(failureEmbed("You don't have anything eqipped!")).queue();
    		return;
    	}
    	if(equipped.type != RpgItemType.Consumable) {
    		channel.sendMessage(failureEmbed("You can't use this item!")).queue();
    		return;
    	}
    	player.consume(equipped);
    	player.retaliate();
		channel.sendMessage(successEmbed("Recovered " + equipped.power + " HP. (now at " + player.health + "/" + player.maxHealth + ")")).queue();
    }
    public void RPG_Wait(String[] args, MessageChannel channel, TextChannel tc, Guild g, Member member, Message message, RpgPlayer player) {
    	player.retaliate();
    	channel.sendMessage(successEmbed("You waited for a moment.\n+1 mana!")).queue();
    }
    public void RPG_Stats(String[] args, MessageChannel channel, TextChannel tc, Guild g, Member member, Message message, RpgPlayer player) {
    	RpgPlayer selected = player;
    	User selectedUser = member.getUser();
    	List<Member> mnd = message.getMentionedMembers();
    	if(!mnd.isEmpty()) {
    		User _pick = mnd.get(0).getUser();
    		selectedUser = _pick;
    		long pickID = _pick.getIdLong();
    		if(!rpgPlayers.containsKey(pickID)) {
    			channel.sendMessage(failureEmbed("That person hasn't started RPG yet!")).queue();
    			return;
    		}
    		RpgPlayer pick = rpgPlayers.get(pickID);
    		selected = pick;
    	}
    	
    	String hp = selected.health + "/" + selected.maxHealth;
    	String mn = selected.mana + "/" + selected.maxMana;
    	RpgItem equipped = selected.equippedItem;
    	String equ = null;
    	if(equipped == null) {
    		equ = "Nothing.";
    	} else {
    		equ = equipped.name;
    	}
    	EmbedBuilder eb = new EmbedBuilder();
    	eb.setAuthor(selectedUser.getName(), null, 
    		selectedUser.getEffectiveAvatarUrl());
    	eb.setColor(Color.magenta);
    	eb.setTitle("Player Stats");
    	eb.setDescription("HP: " + hp + 
			"\nMana: " + mn +
			"\nEquipped Item: " + equ);
    	channel.sendMessage(eb.build()).queue();
    }
    public void RPG_Adventure(String[] args, MessageChannel channel, TextChannel tc, Guild g, Member member, Message message, RpgPlayer player) {
    	boolean npc = random(4) == 3;
    	boolean freebie = random(3) == 1;
    	List<String> names = Arrays.asList("Zipdip100", "7UKECREAT0R", "bisticeaty", "Checken", "xReddish", "Giraffey", "Key", "NebuLa", "Nouhi", "Onyx2D", "Curoa", "Truman", "Ale", "Matthew");
    	List<String> titles = Arrays.asList("the Butcher", "the Shopkeeper", "the Destroyer of Worlds", "the Savage", "the Gay", "the Blacksmith", "the Madlad", "the Accountant", "the Idiot");
    	if(npc) {
    		String name = names.get(random(names.size()));
    		String title = titles.get(random(titles.size()));
    		String s = name + " " + title;
    		
    		List<RpgItem> items = new ArrayList<RpgItem>();
    		List<Integer> prices = new ArrayList<Integer>();
    		int itemCount = (random(4))+1;
    		for(int i = 0; i < itemCount; i++) {
    			RpgItem rpgi = null;
        		do {
        			rpgi = rpgItems.get(random(rpgItems.size()));
        		} while(rpgi.type == RpgItemType.Resource);
    			int cost = (int) Math.round((double)rpgi.power*2.5);
    			items.add(rpgi);
    			prices.add(cost);
    		}
    		EmbedBuilder eb = new EmbedBuilder();
    		eb.setTitle("NPC: " + s);
    		eb.setColor(Color.magenta);
    		eb.setDescription("Respond with the item you'd like to buy, or say \"cancel\" to leave.");
    		for(int i = 0; i < items.size(); i++) {
    			RpgItem item = items.get(i);
    			int price = prices.get(i);
    			
    			eb.addField("Item #" + (i+1), item.
    			toString() + "\nPrice: " + price, false);
    		}
    		channel.sendMessage(eb.build()).queue();
    		shopResponses.put(member.getUser().getIdLong(), items);
    	} else if(freebie) {
    		RpgItem item = null;
    		item = rpgItems.get(random(rpgItems.size()));
    		player.inv.addItem(item);
    		channel.sendMessage(successEmbed("Found a(n) " + item.name + " on the ground!")).queue();
    		return;
    	} else {
    		int damage = random(5);
    		player.damage(damage);
    		if(!player.isDead()) {
    			channel.sendMessage(successEmbed("You didn't find any towns during that adventure... Taken " + damage + " damage because of enemies!")).queue();
    		} else {
    			player.dead();
    			RpgItem i = player.inv.getRandomItem();
    			if(i == null) {
    				channel.sendMessage(successEmbed("You died during that adventure, but you didn't have any items to lose! (take THAT, enemies!)")).queue();
    				return;
    			}
    			boolean lucky = randomBool();
    			if(lucky) {
    				channel.sendMessage(successEmbed("You died during that adventure, but lucked out and didn't lose anything when you respawned!")).queue();
    				return;
    			}
				channel.sendMessage(successEmbed("You died during that adventure. Lost \"" + i.name + "\".")).queue();
				if(player.equippedItem != null) {
					if(player.equippedItem.equals(i)) {
						player.equippedItem = null;
					}
				}
				player.inv.removeItem(i);
				return;
    		}
    		return;
    	}
    }
    public void RPG_Info(String[] args, MessageChannel channel, TextChannel tc, Guild g, Member member, Message message, RpgPlayer player) {
    	RpgInventory inv = player.inv;
    	String text = combine(args, 1);
    	if(text.isEmpty() || text.equalsIgnoreCase("")) {
    		channel.sendMessage(failureEmbed("Usage: info <item>")).queue();
    		return;
    	}
    	RpgItem i = inv.searchForItem(text, "");
    	if(i == null) {
    		channel.sendMessage(failureEmbed("Cannot find the item you're looking for! (did you speel it rite?)")).queue();
    		return;
    	}
    	channel.sendMessage(i.toEmbed()).queue();
    	return;
    }
    
    // Premium Commands
    public void superPlay(String[] args, MessageChannel channel, TextChannel tc, Guild g, Member member, Message message) {
    	if(!hasCommand(member, PremiumCommand.SuperPlay)) {
    		channel.sendMessage(failureEmbed("You need to purchase this command off the shop first! (TIP: Use the `shop` command!)")).queue();
    		return;
    	}
    	if(channelHasModifier(tc, "nomusic")) {
    		channel.sendMessage(failureEmbed("Music commands are disabled in this channel. Please do them in the appropriate channel, thanks!")).queue(msg -> {
    			msg.delete().queueAfter(5, TimeUnit.SECONDS);
    		});
    		return;
    	}
    	play(args, channel, tc, g, member, message, true);
    	return;
    }
    public void rainbowRole(String[] args, MessageChannel channel, TextChannel tc, Guild g, Member member, Message message) {
    	Long gID = g.getIdLong();
    	if(!hasCommand(member, PremiumCommand.RainbowRole)) {
    		channel.sendMessage(failureEmbed("You need to purchase this command off the shop first! (TIP: Use the `shop` command!)")).queue();
    		return;
    	}
    	if(!member.isOwner()) {
    		channel.sendMessage(failureEmbed("You must be the owner of the server to run this!")).queue();
    		return;
    	}
    	if(args.length >= 2 && args[1].equalsIgnoreCase("none")) {
    		rainbowRoles.put(gID, 0l);
    		channel.sendMessage(successEmbed("Disabled RainbowRole on your server.")).queue();
    		return;
    	}
    	List<Role> mentioned = message.getMentionedRoles();
    	if(mentioned.isEmpty()) {
    		channel.sendMessage(failureEmbed("Usage: `rainbowrole @<role>`")).queue();
    		return;
    	}
    	Role r = mentioned.get(0);
    	Long rID = r.getIdLong();
    	rainbowRoles.put(gID, rID);
    	channel.sendMessage(successEmbed("The role " + r.getAsMention() + " is now rainbow!\n"
    			+ "You can disable this by using `rainbowrole none`.")).queue();
    	return;
    }
    public void nickAll(String[] args, MessageChannel channel, TextChannel tc, Guild g, Member member, Message message) {
    	User u = member.getUser();
    	Long id = u.getIdLong();
    	if(nickallCD.containsKey(id)) {
    		Long time = nickallCD.get(id);
    		Long curtime = System.currentTimeMillis();
    		if(time > curtime) {
    			String bd = getDurationBreakdown(time-curtime);
    			channel.sendMessage(failureEmbed("This command is on cooldown! (" + bd + " left)")).queue();
    			return;
    		} else {
    			nickallCD.remove(id);
    		}
    	}
    	if(!hasCommand(member, PremiumCommand.Nickall)) {
    		channel.sendMessage(failureEmbed("You need to purchase this command off the shop first! (TIP: Use the `shop` command!)")).queue();
    		return;
    	}
    	if(!member.isOwner()) {
    		channel.sendMessage(failureEmbed("You must be the owner of the server to run this!")).queue();
    		return;
    	}
    	if(g.getMembers().size() > 100) {
    		channel.sendMessage(failureEmbed("Sorry! The maximum amount of users you can nickall is 100.")).queue();
    		return;
    	}
    	if(args.length < 2) {
    		channel.sendMessage(failureEmbed("Usage: nickall <text>")).queue();
    		return;
    	}
    	String nick = combine(args, 1);
    	if(nick.length() >= 32) {
    		channel.sendMessage(failureEmbed("Input must be less than 32 characters!")).queue();
    		return;
    	}
    	channel.sendMessage(successEmbed("Nicking... The will take about " + g.getMembers().size() + " seconds.")).queue();
    	for(Member m : g.getMembers()) {
    		try {
    			g.modifyNickname(m, nick).completeAfter(1000, TimeUnit.MILLISECONDS);
    		} catch(Exception e) {
    			continue;
    		}
    	}
    	channel.sendMessage(successEmbed("Finished! This command is now on a 10 minute cooldown!")).queue();
    	nickallCD.put(id, System.currentTimeMillis() + (600*1000));
    	return;
    }
    public void expose(String[] args, MessageChannel channel, TextChannel tc, Guild g, Member member, Message message) {
    	if(!hasCommand(member, PremiumCommand.Expose)) {
    		channel.sendMessage(failureEmbed("You need to purchase this command off the shop first! (TIP: Use the `shop` command!)")).queue();
    		return;
    	}
    	Message deleted = fetchLastDeletedMessage(g);
    	if(deleted == null) {
    		channel.sendMessage(failureEmbed("There's nothing to expose! (TIP: This command shows the last deleted message!)")).queue();
    		return;
    	}
    	List<Role> rls = deleted.getMentionedRoles();
    	if(!rls.isEmpty()) {
    		channel.sendMessage(failureEmbed("im not allowed to ping that/those role(s) lmao")).queue();
    		return;
    	}
    	if(deleted.mentionsEveryone()) {
    		channel.sendMessage(failureEmbed("im not allowed to ping everyone lmao")).queue();
    		return;
    	}
    	
    	String text = deleted.getContentRaw();
    	if(!deleted.getAttachments().isEmpty()) {
    		channel.sendMessage(failureEmbed("Unfortunately the solar powered monkey men at Discord HQ don't allow me to expose messages with pictures/files in them!")).queue();
    		return;
    	}
    	List<String> filter = getFilter(g);
    	String a = text.toLowerCase();
    	for(String swear : filter) {
    		if(tc.isNSFW()) { break; }
    		if(channelHasModifier(tc, "nofilter")) { break; }
    		if(a.contains(swear.toLowerCase())) {
        		channel.sendMessage(failureEmbed("That message was deleted because it contained blocked words on this server!")).queue();
        		return;
    		}
    	}
    	// ok now its safe lmao
    	Member poster = deleted.getMember();
    	User uposter = poster.getUser();
    	
    	EmbedBuilder eb = new EmbedBuilder();
    	eb.setColor(poster.getColor());
    	eb.setTitle("Deleted Message:");
    	eb.setDescription(text);
    	if(!deleted.getAttachments().isEmpty())
    		eb.appendDescription("\n\nNOTE: MESSAGE CONTAINED ATTACHMENTS.");
    	eb.setAuthor(getFriendCode(uposter), 
    		null, uposter.getEffectiveAvatarUrl());
    	channel.sendMessage(eb.build()).queue();
    	return;
    }
    public void profilepic(String[] args, MessageChannel channel, TextChannel tc, Guild g, Member member, Message message) {
    	if(!hasCommand(member, PremiumCommand.Profilepic)) {
    		channel.sendMessage(failureEmbed("You need to purchase this command off the shop first! (TIP: Use the `shop` command!)")).queue();
    		return;
    	}
    	List<Member> mentioned = message.getMentionedMembers();
    	if(mentioned.isEmpty()) {
    		channel.sendMessage(failureEmbed("Usage: profilepic @<user>")).queue();
    		return;
    	}
    	Member _pick = mentioned.get(0);
    	User pick = _pick.getUser();
    	String aurl = pick.getEffectiveAvatarUrl();
    	
    	channel.sendMessage("**" + pick.getAsMention() + "'s Profile Picture:**\n" + aurl).queue();
    	return;
    }
    public void troll(String[] args, MessageChannel channel, TextChannel tc, Guild g, Member member, Message message) {
    	if(!hasCommand(member, PremiumCommand.Troll)) {
    		channel.sendMessage(failureEmbed("You need to purchase this command off the shop first! (TIP: Use the `shop` command!)")).queue();
    		return;
    	}
    	List<Member> targets = message.getMentionedMembers();
    	if(targets.isEmpty()) {
    		channel.sendMessage(failureEmbed("Usage: troll @<user>")).queue();
    		return;
    	}
    	channel.sendMessage(failureEmbed("Whoops, the boys are still working on this command!! Have an idea for a troll? Use the idea command, we'd love to hear it because we're out of em!")).queue();
    	return;
    }
    public void comment(String[] args, MessageChannel channel, TextChannel tc, Guild g, Member member, Message message) {
    	if(!hasCommand(member, PremiumCommand.Comment)) {
    		channel.sendMessage(failureEmbed("You need to purchase this command off the shop first! (TIP: Use the `shop` and `buy` commands!)")).queue();
    		return;
    	}
    	if(args.length < 2) {
    		channel.sendMessage(failureEmbed("Usage: comment <youtube url>")).queue();
    		return;
    	}
    	String url = args[1];
    	Pattern pattern = Pattern.compile(SITE_REGEX, Pattern.CASE_INSENSITIVE);
    	Matcher match = pattern.matcher(url);
    	
    	if(!match.find()) {
    		channel.sendMessage(failureEmbed("Not a valid URL!")).queue();
    		return;
    	}
    	
    	boolean shortened = url.contains("youtu.be");
    	String[] identifiers = shortened ? url.split("be/") : url.split("watch\\?v=");
    	if(identifiers.length < 2) {
    		channel.sendMessage(failureEmbed("Not a valid URL!")).queue();
    		return;
    	}
    	
    	try {
        	String videoId = identifiers[1];
        	Videos vapi = yt.videos();
        	
        	YouTube.Videos.List vListReq;
        	vListReq = vapi.list("snippet,statistics,contentDetails");
        	vListReq.setId(videoId);
        	vListReq.setKey(Private.YTAPI_KEY);
        	VideoListResponse vlr = vListReq.execute();
        	List<Video> vids = vlr.getItems();
        	if(vids.isEmpty()) {
        		channel.sendMessage(failureEmbed("Invalid YouTube video? Couldn't get video statistics.")).queue();
        		return;
        	}
        	Video result = vids.iterator().next();
        	String duration = result.getContentDetails().getDuration().substring(2);
        	String[] durationParts = duration.split("[A-Z]");
        	int videoHours = 0, videoMinutes = 0, videoSeconds = 0;
        	int partsLength = durationParts.length;
        	boolean hasHours = duration.contains("H");
        	boolean hasMinutes = duration.contains("M");
        	boolean hasSeconds = duration.contains("S");
        	if(partsLength == 1) {
        		if(hasSeconds)
        			videoSeconds = Integer.parseInt(durationParts[0]);
        		else if(hasMinutes)
        			videoMinutes = Integer.parseInt(durationParts[0]);
        		else if(hasHours)
        			videoHours = Integer.parseInt(durationParts[0]);
        	} else if(partsLength == 2) {
        		if(hasSeconds) {
        			if(hasMinutes) {
        				videoMinutes = Integer.parseInt(durationParts[0]);
        				videoSeconds = Integer.parseInt(durationParts[1]);
        			}
        			if(hasHours) {
        				videoHours = Integer.parseInt(durationParts[0]);
        				videoSeconds = Integer.parseInt(durationParts[1]);
        			}
        		} else if(hasMinutes) {
        			videoHours = Integer.parseInt(durationParts[0]);
        			videoMinutes = Integer.parseInt(durationParts[1]);
        		}
        	} else if(partsLength >= 3) {
           		videoHours = Integer.parseInt(durationParts[0]);
           		videoMinutes = Integer.parseInt(durationParts[1]);
           		videoSeconds = Integer.parseInt(durationParts[2]);
        	}
        	
        	String category = result.getSnippet().getCategoryId();
        	boolean isMusic = category.equalsIgnoreCase("10");
        	String title = result.getSnippet().getTitle();
        	String channelName = result.getSnippet().getChannelTitle();
        	DateTime _uploaded = result.getSnippet().getPublishedAt();
        	LocalDate uploaded = Instant.ofEpochMilli(_uploaded.getValue())
        			.atZone(ZoneId.systemDefault()).toLocalDate();
        	int uploadedYear = uploaded.getYear();
        	int year = LocalDate.now().getYear();
        	int nextYear = year +  1;
        	
        	// Vars:
        	// ${year}, ${nextyear}, ${yearuploaded}, ${timestamp}, ${title}, ${channel}, ${phrase}, ${object}, ${videomilestone}, ${length}
        	
        	// Timestamp
        	int rMin = 0;
        	if(videoMinutes != 0)
        		rMin = random(videoMinutes + 1);
        	int rSec = random(videoSeconds + 1);
        	String rSecString = String.valueOf(rSec);
        	if(rSecString.length() < 2)
        		rSecString = "0" + rSecString;
        	String timestamp = rMin + ":" + rSecString;
        	
        	// Other
        	String phrase = getCommentPhrase();
        	String object = getCommentObject();
        	
        	// Milestone
        	long videoViews = result.getStatistics()
        			.getViewCount().longValue();
        	double dVideoViews = (double)videoViews/10000;
        	long ceil = (long)Math.ceil(dVideoViews);
        	String milestone = String.valueOf(ceil*10000l);
        	
        	// Video length.
        	String vSS = String.valueOf("videoSeconds");
        	String vidLength = videoMinutes + ":" + (vSS.length() < 2 ? "0" + vSS : vSS);
        	
        	boolean isLong = videoMinutes > 30 || videoHours > 0;
        	boolean isOld = (year - uploadedYear) > 3;
        	boolean useNormal = randomBool() && randomBool();
        	String comment;
        	if((isMusic || isLong || isOld) && !useNormal) {
        		if(isOld)
        			comment = YT_COMMENTS_OLD[random(YT_COMMENTS_OLD.length)];
        		else if(isMusic)
        			comment = YT_COMMENTS_MUSIC[random(YT_COMMENTS_MUSIC.length)];
        		else comment = YT_COMMENTS_LONG[random(YT_COMMENTS_LONG.length)];
        	} else {
        		comment = YT_COMMENTS[random(YT_COMMENTS.length)];
        	}
        	
        	comment = comment.replaceAll(Pattern.quote("${year}"), "" + year);
        	comment = comment.replaceAll(Pattern.quote("${nextyear}"), "" + nextYear);
        	comment = comment.replaceAll(Pattern.quote("${yearuploaded}"), "" + uploadedYear);
        	comment = comment.replaceAll(Pattern.quote("${timestamp}"), "" + timestamp);
        	comment = comment.replaceAll(Pattern.quote("${title}"), "" + title);
        	comment = comment.replaceAll(Pattern.quote("${channel}"), "" + channelName);
        	comment = comment.replaceAll(Pattern.quote("${phrase}"), "" + phrase);
        	comment = comment.replaceAll(Pattern.quote("${object}"), "" + object);
        	comment = comment.replaceAll(Pattern.quote("${videomilestone}"), "" + milestone);
        	comment = comment.replaceAll(Pattern.quote("${length}"), "" + vidLength);
        	
        	EmbedBuilder eb = new EmbedBuilder();
        	eb.setColor(Color.white.darker());
        	eb.setAuthor(getCommentUsername());
        	eb.setDescription(comment);
        	channel.sendMessage(eb.build()).queue(msg -> {
        		msg.addReaction("👍").queue();
        		msg.addReaction("👎").queue();
        	});
        	
    	} catch(Exception exc) {
    		exc.printStackTrace();
    		channel.sendMessage(failureEmbed("An unexpected error occurred.")).queue();
    		return;
    	}
    }
    
    /*public void Math(String[] args, MessageChannel channel, TextChannel tc, Guild g, Member member, Message message) {
    	String expr = Combine(args, 1);
    	if(expr.length() < 2) {
    		channel.sendMessage(FailiureEmbed("Usage: math <expression>")).queue();
    		return;
    	}
    	try {
        	Expression e = new Expression(expr);
        	double solution = e.solve();
        	String sol = String.valueOf(solution);
        	channel.sendMessage(SuccessEmbed("Solution: " + sol)).queue();
    	} catch(Exception exc) {
    		channel.sendMessage(FailiureEmbed("Encountered an error while solving: " + exc.getMessage())).queue();
    		exc.printStackTrace();
    		return;
    	}
    	return;
    }*/
    // Regular Commands
    public void coinflip(String[] args, MessageChannel channel, TextChannel tc, Guild g, Member member, Message message) {
    	String send;
    	if(randomBool())
    		send = Private.COINFLIP_H;
    	else
    		send = Private.COINFLIP_T;
    	channel.sendMessage(send).queue();
    }
    public void disconnect(String[] args, MessageChannel channel, TextChannel tc, Guild g, Member member, Message message) {
    	if(channelHasModifier(tc, "nomusic")) {
    		channel.sendMessage(failureEmbed("Music commands are disabled in this channel. Please do them in the appropriate channel, thanks!")).queue(msg -> {
    			msg.delete().queueAfter(5, TimeUnit.SECONDS);
    		});
    		return;
    	}
    	Member self = g.getSelfMember();
    	if(!self.getVoiceState().inVoiceChannel()) {
    		channel.sendMessage(failureEmbed("I'm not playing music right now!")).queue(msg -> {
    			msg.delete().queueAfter(5, TimeUnit.SECONDS);
    		});
    		return;
    	}
    	GuildVoiceState gvs = self.getVoiceState();
    	VoiceChannel vc = gvs.getChannel();
    	if(!member.getVoiceState().inVoiceChannel()) {
    		channel.sendMessage(failureEmbed("Only the people who are actually listening to the music can disconnect me!")).queue(msg -> {
    			msg.delete().queueAfter(5, TimeUnit.SECONDS);
    		});
    		return;
    	}
    	VoiceChannel mvc = member
			.getVoiceState()
			.getChannel();
    	long id1, id2;
    	id1 = vc.getIdLong();
    	id2 = mvc.getIdLong();
    	if(id1 != id2) {
    		channel.sendMessage(failureEmbed("Only the people who are actually listening to the music can disconnect me!")).queue(msg -> {
    			msg.delete().queueAfter(5, TimeUnit.SECONDS);
    		});
    		return;
    	}
    	g.getAudioManager().closeAudioConnection();
    	GuildMusicManager gmm;
    	gmm = getGuildAudioPlayer(g);
    	int queueSize = gmm.scheduler.queue.size();
    	if(queueSize > 0 && !isMod(member, g)) {
    		channel.sendMessage(failureEmbed("Wait, there's still music in the queue!")).queue(msg -> {
    			msg.delete().queueAfter(5, TimeUnit.SECONDS);
    		});
    		return;
    	}
    	gmm.scheduler.queue.clear();
    	skipTrack(gmm, true);
    }
    public void scriptEditor(String[] args, MessageChannel channel, TextChannel tc, Guild g, Member member, Message message) {
        if(!isMod(member, g)) {
        	channel.sendMessage(failureEmbed("You don't have permission to use this!")).queue(m -> {
        		m.delete().queueAfter(5, TimeUnit.SECONDS);
        	});
        	return;
        }
        if(getUpgrades(g) < SCRIPT_EDITOR_COST) {
           	channel.sendMessage(failureEmbed("This command is special for servers with " + SCRIPT_EDITOR_COST + "+ upgrades! Check the upgrades command.")).queue(m -> {
        		m.delete().queueAfter(7, TimeUnit.SECONDS);
        	});
        	return;
        }
        
        TextChannel scriptChannel;
        scriptChannel = checkIfScriptChannelExists(g);
        if(scriptChannel == null) {
        	scriptChannel = createScriptChannel(g);
        	if(scriptChannel == null) {
        		// what the frick??
        		return;
        	}
        }
        
        channel.sendMessage("**" + member.getAsMention() + ", check this channel!: "+ scriptChannel.getAsMention() + "**").queue();
    }
    public void modifiers(String[] args, MessageChannel channel, TextChannel tc, Guild g, Member member, Message message) {
    	EmbedBuilder eb = new EmbedBuilder();
    	eb.setColor(Color.gray);
    	eb.setTitle("Available Modifiers");
    	eb.setDescription("Simply put these words anywhere in a channel topic to enable them!");
    	eb.addField("❌ no xp", "Disables obtaining xp in the channel.", false);
    	eb.addField("🤬 no filter", "Disables the filtering of words in this channel.", false);
    	eb.addField("🎵 no music", "Music commands will not work in this channel. You can put this on channels you don't want to be flooded with music commands!", false);
    	eb.addField("📵 no logging", "This channel will not log deleted messages in your server's logging channel (if any).", false);
    	eb.addBlankField(false);
    	eb.addField("🕵️‍♂️ anonymous", "Every message sent in this channel will be anonymous!", false);
    	eb.addField("🖼️ pictures only", "Only messages with attachments are allowed in channels with this.", false);
    	eb.addField("🏆 premium only", "Only allows BonziBot premium users to chat in this channel.", false);
    	eb.addField("🗒️ " + LOG_MODIFIER, "Puts log messages in this channel.", false);
    	eb.addField("🎰 counting", "Let users collaborate and count as high as they can!", false);
    	eb.addField("⚔️ rpg", "A full role-playing game with pvp, tons of items, and more!", false);
    	eb.addField("⬇️ bonzi updates", "Every time a new BonziBot update comes out it will be posted in this channel!", false);
    	channel.sendMessage(eb.build()).queue();
    }
    public void speak(String[] args, MessageChannel channel, TextChannel tc, Guild g, Member member, Message message) {
    	if(channelHasModifier(tc, "nomusic")) {
    		channel.sendMessage(failureEmbed("Music commands are disabled in this channel. Please do them in the appropriate channel, thanks!")).queue(msg -> {
    			msg.delete().queueAfter(5, TimeUnit.SECONDS);
    		});
    		return;
    	}
    	String s = combine(args, 1);
    	if(s.isEmpty()) {
    		channel.sendMessage(failureEmbed("You have to put something to speak...")).queue();
    		return;
    	}
    	speakInChannel(s, member, tc);
    	return;
    }
    public void videoChat(String[] args, MessageChannel channel, TextChannel tc, Guild g, Member member, Message message) {
    	GuildVoiceState state = member.getVoiceState();
    	if(!state.inVoiceChannel()) {
    		channel.sendMessage(failureEmbed("You must be in a voice channel to use this command!")).queue();
    		return;
    	}
    	VoiceChannel vc = state.getChannel();
    	long vcid = vc.getIdLong();
    	long svid = g.getIdLong();
    	String link = getVideoChannel(vcid, svid);
    	
    	EmbedBuilder eb = new EmbedBuilder();
    	eb.setColor(Color.green);
    	eb.setTitle("Video Channel \"" + vc.getName() + "\"");
    	eb.setDescription("[Open Video Chat](" + link + ")");
    	channel.sendMessage(eb.build()).queue();
    	return;
    }
    public void playlist(String[] args, MessageChannel channel, TextChannel tc, Guild g, Member member, Message message) {
    	if(channelHasModifier(tc, "nomusic")) {
    		channel.sendMessage(failureEmbed("Music commands are disabled in this channel. Please do them in the appropriate channel, thanks!")).queue(msg -> {
    			msg.delete().queueAfter(5, TimeUnit.SECONDS);
    		});
    		return;
    	}
    	User u = member.getUser();
    	if(args.length < 2) {
    		channel.sendMessage(failureEmbed(
				  "playlist play <name>\n"
				+ "playlist top\n"
				+ "playlist top <page #>\n"
				+ "playlist create <name>\n"
				+ "playlist add <url> <name>\n"
				+ "playlist clear <name>\n"
				+ "playlist remove <song #>\n"
				+ "playlist delete <name>\n"
				+ "playlist info <name>")).queue();
    		return;
    	}
    	String q = args[1];
    	if(q.equalsIgnoreCase("play")) {
    		// ------------------ PLAY COMMAND ------------------
    		if(args.length < 3) {
        		channel.sendMessage(failureEmbed
        			("Usage: playlist play <name>")).queue();
          		return;
    		}
    		String name = combine(args, 2);
    		if(!playlistExists(name)) {
        		channel.sendMessage(failureEmbed
            		("No playlist exists with that name!")).queue();
              	return;
    		}
    		Playlist p = getPlaylistByName(name);
    		if(!member.getVoiceState().inVoiceChannel()) {
    			channel.sendMessage("You can only begin songs when you're in a voice channel.").queue();
    			return;
    		}
    		VoiceChannel vc;
    		GuildVoiceState gvs;
    		AudioManager am;
    		gvs = member.getVoiceState();
    		vc = gvs.getChannel();
    		am = g.getAudioManager();
    		am.openAudioConnection(vc);
    		if(u.getIdLong() != p.creatorID) {
    			addPlay(name);
    		}
    		playPlaylist(tc, p);
    		return;
    	} else if(q.equalsIgnoreCase("top")) {
    		// ------------------ TOP COMMAND ------------------
    		// Cloning to prevent saving the sort.
    		
    		List<Playlist> pl = new ArrayList<Playlist>(playlists);
    		Comparator<Playlist> c = new Comparator<Playlist>() {
				@Override
				public int compare(Playlist arg0, Playlist arg1) {
					Integer a, b;
					a = arg0.plays;
					b = arg1.plays;
					return a.compareTo(b);
				}
    		};
    		Collections.sort(pl, Collections.reverseOrder(c));
    		double _pages = pl.size()/10.0;
    		int pages = (int)Math.ceil(_pages);
    		
    		int page = 1;
    		if(args.length <= 2) {
    			page = 1;
    		} else {
    			String _parse = args[2];
    			try {
    				page = Integer.parseInt(_parse);
    			} catch(Exception exc) {
    				channel.sendMessage(failureEmbed("<page> has to be a valid number!")).queue();
    				return;
    			}
    		}
    		EmbedBuilder eb = new EmbedBuilder();
    		eb.setColor(Color.magenta);
    		eb.setTitle("⏯️ Top Playlists by the Community! 🏆\n"
    				+ "(Page " + page + "/" + pages + ")");
    		for(int i = (page*10)-10; i < pl.size(); i++) {
    			Playlist p = pl.get(i);
    			eb.addField(i+1 + ". " + p.name,
    				  "Creator: " + p.creator + "\n"
    				+ "Plays: " + p.plays + "\n", false);
    		}
    		channel.sendMessage(eb.build()).queue();
    		return;
    	} else if(q.equalsIgnoreCase("create")) {
    		// ------------------ CREATE COMMAND ------------------
    		if(args.length < 3) {
        		channel.sendMessage(failureEmbed
        			("Usage: playlist create <name>")).queue();
          		return;
    		}
    		String name = combine(args, 2);
    		if(playlistExists(name)) {
        		channel.sendMessage(failureEmbed
            		("A playlist with this name already exists!")).queue();
              	return;
    		}
    		Playlist p = new Playlist(new 
    			ArrayList<String>(), name, u);
    		addPlaylist(p);
    		channel.sendMessage(successEmbed
        		("Successfully created the playlist!\n"
				+ "You can now begin adding songs to it by doing:\n"
				+ "playlist add <url> " + name)).queue();
            return;
    	} else if(q.equalsIgnoreCase("add")) {
    		// ------------------ ADD COMMAND ------------------
    		if(args.length < 4) {
        		channel.sendMessage(failureEmbed
        			("Usage: playlist add <url> <name>")).queue();
          		return;
    		}
    		String name = combine(args, 3);
    		String url = args[2];
    		if(!playlistExists(name)) {
        		channel.sendMessage(failureEmbed
            		("This playlist doesn't exist!")).queue();
              	return;
    		}
    		Playlist p = getPlaylistByName(name);
    		if(!p.isOwner(u)) {
        		channel.sendMessage(failureEmbed
        			("You can't modify someone else's playlist!")).queue();
                return;
    		}
    		if(!isURL(url)) {
        		channel.sendMessage(failureEmbed
            		("The URL must be a valid link to your song!")).queue();
                return;
    		}
    		if(p.urls.size() >= 25) {
        		channel.sendMessage(failureEmbed
                	("Playlists can't be longer than 25 songs! (Sorry, it's to prevent overload.)")).queue();
                return;
    		}
    		if(!url.toLowerCase().contains("youtube") && 
    		   !url.toLowerCase().contains("soundcloud")) {
        		channel.sendMessage(failureEmbed
            		("Playlists only support youtube and soundcloud at the moment.")).queue();
                return;
    		}
    		addTrackToPlaylist(url, name);
    		channel.sendMessage(successEmbed
            	("Added track to your playlist!")).queue();
            return;
    	} else if(q.equalsIgnoreCase("remove")) {
    		
    	} else if(q.equalsIgnoreCase("clear")) {
    		// ------------------ CLEAR COMMAND ------------------
    		if(args.length < 3) {
        		channel.sendMessage(failureEmbed
        			("Usage: playlist clear <name>")).queue();
          		return;
    		}
    		String name = combine(args, 2);
    		if(!playlistExists(name)) {
        		channel.sendMessage(failureEmbed
            		("This playlist doesn't exist!")).queue();
              	return;
    		}
    		Playlist p = getPlaylistByName(name);
    		if(!p.isOwner(u)) {
        		channel.sendMessage(failureEmbed
        			("You can't modify someone else's playlist!")).queue();
                return;
    		}
    		clearPlaylist(name);
    		channel.sendMessage(successEmbed
                ("Successfully cleared your playlist!")).queue();
            return;
    	} else if(q.equalsIgnoreCase("delete")) {
    		// ------------------ REMOVE COMMAND ------------------
    		if(args.length < 3) {
        		channel.sendMessage(failureEmbed
        			("Usage: playlist delete <name>")).queue();
          		return;
    		}
    		String name = combine(args, 2);
    		if(!playlistExists(name)) {
        		channel.sendMessage(failureEmbed
            		("This playlist doesn't exist!")).queue();
              	return;
    		}
    		Playlist p = getPlaylistByName(name);
    		if(!p.isOwner(u)) {
        		channel.sendMessage(failureEmbed
        			("You can't delete someone else's playlist!")).queue();
                return;
    		}
    		int i = getPlaylistIndexByName(name);
    		playlists.remove(i);
    		channel.sendMessage(successEmbed
                ("Successfully deleted your playlist!")).queue();
            return;
    	} else if(q.equalsIgnoreCase("info")) {
    		// ------------------ REMOVE COMMAND ------------------
    		if(args.length < 3) {
        		channel.sendMessage(failureEmbed
        			("Usage: playlist info <name>")).queue();
          		return;
    		}
    		String name = combine(args, 2);
    		if(!playlistExists(name)) {
        		channel.sendMessage(failureEmbed
            		("This playlist doesn't exist!")).queue();
              	return;
    		}
    		Playlist p = getPlaylistByName(name);
    		EmbedBuilder eb = new EmbedBuilder();
    		eb.setTitle("Playlist by " + p.creator);
    		eb.setColor(Color.magenta);
    		eb.addField(p.name, "Plays: " + p.plays + "\n"
    				+ "ID: " + p.ID, false);
    		StringBuilder sb = new StringBuilder();
    		int x = 0;
    		for(String url : p.urls) {
    			x++;
    			sb.append("[Song " + x + "](" + url + ")\n");
    		}
    		eb.addField("Songs:", sb.toString(), false);
    		channel.sendMessage(eb.build()).queue();
            return;
    	} else {
    		channel.sendMessage(failureEmbed(
				  "playlist play <name>\n"
				+ "playlist top\n"
				+ "playlist top <page #>\n"
				+ "playlist create <name>\n"
				+ "playlist add <url> <name>\n"
				+ "playlist clear <name>\n"
				+ "playlist remove <name>\n"
				+ "playlist info <name>")).queue();
    		return;
    	}
    }
    public void purchases(String[] args, MessageChannel channel, TextChannel tc, Guild g, Member member, Message message) {
    	User u = member.getUser();
    	Long id = u.getIdLong();
    	List<PremiumCommand> acct;
    	acct = premiumAccounts.get(id);
    	
    	boolean guildPremium = false;
    	if(getUpgrades(g) >= ALL_PREMIUM_COST) {
    		acct = Arrays.asList(PremiumCommand.values());
    		guildPremium = true;
    	}
    	
    	EmbedBuilder eb = new EmbedBuilder();
    	String _name = u.getName();
    	String url = u.getEffectiveAvatarUrl();
    	eb.setAuthor(_name, null, url);
    	eb.setColor(Color.magenta);
    	String fc = getFriendCode(u);
    	eb.setTitle("Purchases for user " + fc + ".");
    	if(acct.isEmpty()) {
    		eb.addField("No Purchases!", "It seems like you haven't purchased anything off "
    			+ "the shop yet! Use the `shop` command to take a look!", false);
    	} else if(acct.contains(PremiumCommand.Premium) && !guildPremium) {
    		eb.addField("🏆 BonziBot Premium User 🏆", "You have BonziBot Premium!", false);
    		eb.addBlankField(false);
    	} else if(acct.contains(PremiumCommand.Premium) && guildPremium) {
    		eb.addField("🏆 BonziBot Premium User (This sever) 🏆", "You have BonziBot Premium in this server because of its upgrades!", false);
    		eb.addBlankField(false);
    	}
    	String prefix = prefixes.get(g.getIdLong());
    	for(PremiumCommand cmd : acct) {
    		if(cmd.equals(PremiumCommand.Premium)) {
    			continue;
    		}
    		int i = cmd.ordinal();
    		String name = premiumNames[i];
    		String desc = premiumDescriptions[i];
    		String syntax = premiumSyntax[i];
    		eb.addField(name, prefix + syntax
    			+ "\n(" + desc + ")", false);
    	}
    	channel.sendMessage(eb.build()).queue();
    	return;
    }
    public void buy(String[] args, MessageChannel channel, TextChannel tc, Guild g, Member member, Message message) {
    	User u = member.getUser();
    	Long id = u.getIdLong();
    	int coins = getCoins(u);
    	String input = combine(args, 1);
    	
    	List<PremiumCommand> acct;
    	acct = premiumAccounts.get(id);
    	
    	PremiumCommand target = null;
    	for(PremiumCommand pc : PremiumCommand.values()) {
    		if(input.equalsIgnoreCase(pc.toString())) {
    			target = pc;
    			break;
    		}
    	}
    	if(target == null) {
    		channel.sendMessage(failureEmbed("Couldn't find the item you're looking for! (Check spelling!)")).queue();
    		return;
    	}
    	if(acct.contains(target)) {
    		channel.sendMessage(failureEmbed("You already have this item!")).queue();
    		return;
    	}
    	int i = target.ordinal();
    	int price = premiumPrices[i];
    	String name = premiumNames[i];
    	if(price > coins) {
    		channel.sendMessage(failureEmbed("You can not afford this item!")).queue();
    		return;
    	}
    	if(target.equals(PremiumCommand.Premium)) {
    		acct = Arrays.asList(PremiumCommand.values());
        	subtractCoins(u, price);
        	premiumAccounts.put(id, acct);
        	channel.sendMessage(successEmbed("Success! You've purchased BonziBot premium!\n"
        		+ "**You now have access to every available special command as of now!**\n"
        		+ "*Run the `purchases` command to see the syntax!*")).queue();
        	return;
    	} else {
        	acct.add(target);
        	subtractCoins(u, price);
        	premiumAccounts.put(id, acct);
        	channel.sendMessage(successEmbed("Thanks for purchasing a(n) \"" + name + "\"! *You can see the command"
        		+ " syntax by running the `purchases` command!*\n***(50% has gone to the lottery!)***")).queue();
        	return;
    	}
    }
    public void shop(String[] args, MessageChannel channel, TextChannel tc, Guild g, Member member, Message message) {
    	User u = member.getUser();
    	Long id = u.getIdLong();
    	EmbedBuilder eb = new EmbedBuilder();
    	eb.setColor(Color.magenta);
    	String _name = getFriendCode(u);
    	eb.setAuthor(_name, null, u.getEffectiveAvatarUrl());
    	eb.setTitle("BonziBot Shop!");
    	eb.setDescription("Use the 'buy' command to purchase an item. All purchases are permanent!");
    	
    	List<PremiumCommand> acct;
    	acct = premiumAccounts.get(id);
    	
    	for(PremiumCommand pc : PremiumCommand.values()) {
    		if(acct.contains(pc)) {
    			eb.addField("âœ… Already Purchased!", "You've already purchased this item. Use the `purchases` command to see all that you've bought.", false);
    		} else {
    			int i = pc.ordinal();
    			String name = premiumNames[i];
    			String desc = premiumDescriptions[i];
    			int price = premiumPrices[i];
    			eb.addField(name + " | *" + price 
    				+ " coins.*", desc, false);
    		}
    	}
    	channel.sendMessage(eb.build()).queue();
    	return;
    }
    public void pay(String[] args, MessageChannel channel, TextChannel tc, Guild g, Member member, Message message) {
    	User u = member.getUser();
    	Long gID = g.getIdLong();
    	String p = prefixes.get(gID);
    	int coins = getCoins(u);
    	
    	List<Member> mentioned = message.getMentionedMembers();
    	if(args.length < 3 || mentioned.isEmpty()) {
    		channel.sendMessage("Usage: " + p + "pay @<user> <amount>").queue();
    		return;
    	}
    	User target = mentioned.get(0).getUser();
    	String _input = args[2];
    	int input;
    	try {
    		input = Integer.parseInt(_input);
    	} catch(Exception e) {
    		channel.sendMessage("You need to input a number!").queue();
    		return;
    	}
    	if(input <= 0) {
    		channel.sendMessage("\"boi that would be stealing\" -7UKECREAT0R").queue();
    		return;
    	}
    	if(input > coins) {
    		channel.sendMessage("You don't have that many coins!").queue();
    		return;
    	}
    	subtractCoins(u, input);
    	addCoins(target, input);
    	channel.sendMessage("Transaction completed!").queue();
    	target.openPrivateChannel().queue(ch -> {
    		String uname = u.getName() + "#" + u.getDiscriminator();
    		ch.sendMessage(uname + " paid you " + input + " coins!").queue();
    	});
    	return;
    }
    public void lottery(String[] args, MessageChannel channel, TextChannel tc, Guild g, Member member, Message message) {
    	User u = member.getUser();
    	int coins = getCoins(u);
    	
    	if(coins < 10) {
    		channel.sendMessage("You can't afford a lottery ticket!").queue();
    		return;
    	}
    	int ticket = random(1000);
    	if(ticket == 500) {
    		TextChannel tch = getUpdatesChannel(g.getJDA());
    		String name = "**" + u.getName() + "#" + u.getDiscriminator();
    		channel.sendMessage(u.getAsMention() + " **YOU WON THE LOTTERY! +" + lottery + " COINS!**").queue();
    		tch.sendMessage(name + " JUST WON THE LOTTERY AND GOT " + lottery + " COINS! GG!**").queue();
    		addCoins(u, lottery);
    		lottery = 0;
    	} else {
    		subtractCoins(u, 10);
    		lottery+=10;
    		channel.sendMessage("*You didn't win this time. Try again!*\n"
    			+ "**Current lot: " + lottery + "**").queue();
    		return;
    	}
    }
    public void chance(String[] args, MessageChannel channel, TextChannel tc, Guild g, Member member, Message message) {
    	User u = member.getUser();
    	Long gID = g.getIdLong();
    	String p = prefixes.get(gID);
    	int coins = getCoins(u);
    	
    	if(args.length < 2) {
    		channel.sendMessage("Usage: " + p + "chance <amount to gamble>").queue();
    		return;
    	}
    	String _input = args[1];
    	int input;
    	try {
    		input = Integer.parseInt(_input);
    	} catch(Exception e) {
    		channel.sendMessage("You need to input a number!").queue();
    		return;
    	}
    	if(input == 0) {
    		channel.sendMessage("i mean ok but you didnt gain anything").queue();
    		return;
    	}
    	if(input < 0) {
    		channel.sendMessage("\"Nice try!\" -7UKECREAT0R").queue();
    		return;
    	}
    	if(input > coins) {
    		channel.sendMessage("You don't have that many coins!").queue();
    		return;
    	}
    	boolean rand = randomBool();
    	if(rand) {
    		channel.sendMessage("**You win!** *+" + input + " coins.*").queue();
    		addCoins(u, input);
    		return;
    	} else {
    		channel.sendMessage("**You lost!** *Lost " + input + " coins.*").queue();
    		subtractCoins(u, input);
    		return;
    	}
    }
    public void slots(String[] args, MessageChannel channel, TextChannel tc, Guild g, Member member, Message message) {
    	User u = member.getUser();
    	Long gID = g.getIdLong();
    	String p = prefixes.get(gID);
    	int coins = getCoins(u);
    	
    	if(args.length < 2) {
    		channel.sendMessage(failureEmbed("Usage: " + p + "slots <amount to gamble>")).queue();
    		return;
    	}
    	String _input = args[1];
    	int input;
    	try {
    		input = Integer.parseInt(_input);
    	} catch(Exception e) {
    		channel.sendMessage(failureEmbed("You need to input a number!")).queue();
    		return;
    	}
    	if(input <= 0) {
    		channel.sendMessage(failureEmbed("\"Nice try!\" -7UKECREAT0R")).queue();
    		return;
    	}
    	if(input > coins) {
    		channel.sendMessage(failureEmbed("You don't have that many coins!")).queue();
    		return;
    	}
    	subtractCoins(u, input);
    	StringBuilder slots = new StringBuilder();
    	String[] picked = new String[9];
    	slots.append("**<=======>**\n");
    	for(int i = 0; i < 9; i++) {
    		String chosen = (String)choose(":banana:",
    				":strawberry:", ":apple:");
    		picked[i] = chosen;
    		slots.append(chosen);
    		if((i+1) % 3 == 0) { slots.append("\n"); }
    	}
    	slots.append("**<=======>**");
    	int wins = 0;
    	if(picked[0].equals(picked[1]) && picked[1].equals(picked[2])) {
    		wins++;
    	}
    	if(picked[3].equals(picked[4]) && picked[4].equals(picked[5])) {
    		wins++;
    	}
    	if(picked[6].equals(picked[7]) && picked[7].equals(picked[8])) {
    		wins++;
    	}
    	if(picked[0].equals(picked[3]) && picked[3].equals(picked[6])) {
    		wins++;
    	}
    	if(picked[1].equals(picked[4]) && picked[4].equals(picked[7])) {
    		wins++;
    	}
    	if(picked[2].equals(picked[5]) && picked[5].equals(picked[8])) {
    		wins++;
    	}
    	if(picked[0].equals(picked[4]) && picked[4].equals(picked[8])) {
    		wins++;
    	}
    	if(picked[2].equals(picked[4]) && picked[4].equals(picked[6])) {
    		wins++;
    	}
    	double multiplier = 1;
    	if(wins == 0) {
    		slots.append("\n*Better luck next time! (Lost " + input + " coins.)*");
    		lottery+=input;
    	}
    	if(wins == 1) {
    		int gain = (int)Math.round(input*1.5);
    		slots.append("\n*1.5x Multiplier! (Gained " + (gain-input) + " coins!)*");
    		addCoins(u, gain);
    	}
    	if(wins > 1) {
    		multiplier += (0.5*(wins-1));
    		int gain = (int)Math.round(input*multiplier);
    		slots.append("\n*" + multiplier + "x Multiplier! (Gained " + (gain-input) + " coins!)*");
    		addCoins(u, gain);
    	}
    	String built = slots.toString();
    	channel.sendMessage(built).queue();
    	return;
    }
    public void coins(String[] args, MessageChannel channel, TextChannel tc, Guild g, Member member, Message message) {
    	User u = member.getUser();
    	List<Member> mnt = message.getMentionedMembers();
    	if(!mnt.isEmpty()) {
    		User pick = mnt.get(0).getUser();
    		int coins = getCoins(pick);
    		String men = pick.getAsMention();
    		channel.sendMessage("**The user " + men + " has " + coins + " coins!**").queue();
    		return;
    	}
    	int coins = getCoins(u);
    	String men = u.getAsMention();
    	channel.sendMessage(men + "**, you have " + coins + " coins!**").queue();
    	return;
    }
    public void loop(String[] args, MessageChannel channel, TextChannel tc, Guild g, Member member, Message message) {
    	if(channelHasModifier(tc, "nomusic")) {
    		channel.sendMessage(failureEmbed("Music commands are disabled in this channel. Please do them in the appropriate channel, thanks!")).queue(msg -> {
    			msg.delete().queueAfter(5, TimeUnit.SECONDS);
    		});
    		return;
    	}
    	if(!g.getSelfMember().getVoiceState().inVoiceChannel()) {
    		channel.sendMessage(failureEmbed("I'm not in a voice channel!")).queue();
    		return;
    	}
    	long id = g.getIdLong();
    	if(loop.contains(id)) {
    		loop.remove(id);
    		channel.sendMessage(successEmbed("⬜ Turned OFF loop!")).queue();
    		return;
    	} else {
    		loop.add(id);
    		channel.sendMessage(successEmbed("✅ Turned ON loop!")).queue();
    		return;
    	}
    }
    public void loopQueue(String[] args, MessageChannel channel, TextChannel tc, Guild g, Member member, Message message) {
    	if(channelHasModifier(tc, "nomusic")) {
    		channel.sendMessage(failureEmbed("Music commands are disabled in this channel. Please do them in the appropriate channel, thanks!")).queue(msg -> {
    			msg.delete().queueAfter(5, TimeUnit.SECONDS);
    		});
    		return;
    	}
    	if(!g.getSelfMember().getVoiceState().inVoiceChannel()) {
    		channel.sendMessage(failureEmbed("I'm not in a voice channel!")).queue();
    		return;
    	}
    	long id = g.getIdLong();
    	if(queueloop.contains(id)) {
    		queueloop.remove(id);
    		String txt = "⬜ Turned OFF queue loop!";
    		if(loop.contains(id))
    			txt = txt + " Note that loop is on so the current song will continue to loop.";
    		channel.sendMessage(successEmbed(txt)).queue();
    		return;
    	} else {
    		queueloop.add(id);
    		String txt = "✅ Turned ON queue loop!";
    		if(loop.contains(id))
    			txt = txt + " Note that loop is on so the current song will continue to loop.";
    		channel.sendMessage(successEmbed(txt)).queue();
    		return;
    	}
    }
    public void shuffle(String[] args, MessageChannel channel, TextChannel tc, Guild g, Member member, Message message) {
    	if(channelHasModifier(tc, "nomusic")) {
    		channel.sendMessage(failureEmbed("Music commands are disabled in this channel. Please do them in the appropriate channel, thanks!")).queue(msg -> {
    			msg.delete().queueAfter(5, TimeUnit.SECONDS);
    		});
    		return;
    	}
    	if(!g.getSelfMember().getVoiceState().inVoiceChannel()) {
    		channel.sendMessage(failureEmbed("I'm not in a voice channel!")).queue();
    		return;
    	}
    	long id = g.getIdLong();
    	if(shuffle.contains(id)) {
    		shuffle.remove(id);
    		String txt = "⬜ Turned OFF shuffle!";
    		if(loop.contains(id))
    			txt = txt + " Note that loop is on so the current song will continue to loop.";
    		channel.sendMessage(successEmbed(txt)).queue();
    		return;
    	} else {
    		shuffle.add(id);
    		String txt = "✅ Turned ON shuffle!";
    		if(loop.contains(id))
    			txt = txt + " Note that loop is on so the current song will continue to loop.";
    		channel.sendMessage(successEmbed(txt)).queue();
    		return;
    	}
    }
    public void xPLeaderboard(String[] args, MessageChannel channel, TextChannel tc, Guild g, Member member, Message message) {
    	Collection<Integer> _ints = xp.values();
    	List<Integer> ints = new ArrayList<Integer>(_ints);
    	Collections.sort(ints);
    	Collections.reverse(ints);
    	Set<Map.Entry<Long, Integer>> set;
    	set = xp.entrySet();
    	
    	// Reversing hashmap order because I'm a bit lazy rn
    	HashMap<Integer, Long> newhm = new HashMap<Integer, Long>();
    	for(Map.Entry<Long, Integer> e : set) {
    		newhm.put(e.getValue(), e.getKey());
    	}
    	
    	List<User> users = new ArrayList<User>();
    	JDA jda = g.getJDA();
    	int i = 0;
    	for(int value : ints) {
    		i++; if(i > 10) {break;}
    		User pick = jda.getUserById(newhm.get(value));
    		if(pick == null) {
    			i--;
    			continue;
    		}
    		users.add(pick);
    	}
    	EmbedBuilder eb;
    	eb = new EmbedBuilder();
		eb.setColor(Color.magenta);
		eb.setTitle("XP Leaderboard");
    	i = 0;
    	for(User u : users) {
    		i++;
    		String name = u.getName();
    		name += "#" + u.getDiscriminator();
    		Long uID = u.getIdLong();
    		int xpint = xp.get(uID);
    		
    		String title = i + ". " + name;
    		String desc = xpint + " XP";
    		eb.addField(title, desc, false);
    	}
    	channel.sendMessage(eb.build()).queue();
    	return;
    }
    public void countingLeaderboard(String[] args, MessageChannel channel, TextChannel tc, Guild g, Member member, Message message) {
    	Collection<Integer> _ints = countingGames.values();
    	List<Integer> ints = new ArrayList<Integer>(_ints);
    	Collections.sort(ints);
    	Collections.reverse(ints);
    	Set<Map.Entry<Long, Integer>> set;
    	set = countingGames.entrySet();
    	
    	// Reversing hashmap order because I'm a bit lazy rn
    	HashMap<Integer, Long> newhm = new HashMap<Integer, Long>();
    	for(Map.Entry<Long, Integer> e : set) {
    		newhm.put(e.getValue(), e.getKey());
    	}
    	
    	List<Guild> guilds = new ArrayList<Guild>();
    	JDA jda = g.getJDA();
    	int i = 0;
    	for(int value : ints) {
    		i++; if(i > 10) {break;}
    		Guild pick = jda.getGuildById(newhm.get(value));
    		if(pick == null) {
    			i--;
    			continue;
    		}
    		guilds.add(pick);
    	}
    	EmbedBuilder eb;
    	eb = new EmbedBuilder();
		eb.setColor(Color.magenta);
		eb.setTitle("Counting Game Leaderboard");
    	i = 0;
    	for(Guild ordered : guilds) {
    		i++;
    		Long gID = ordered.getIdLong();
    		String gn = i + ". " + ordered.getName();
    		int points = countingGames.get(gID);
    		eb.addField(gn, "" + points, false);
    	}
    	channel.sendMessage(eb.build()).queue();
    	return;
    }
    public void report(String[] args, MessageChannel channel, TextChannel tc, Guild g, Member member, Message message) {
    	
    	long id = g.getIdLong();
    	long uid = member.getUser().getIdLong();
    	
    	if(ideaCooldownTimers.containsKey(uid)) {
    		long time = ideaCooldownTimers.get(uid);
    		long cur = System.currentTimeMillis();
    		if(time > cur) {
    			channel.sendMessage(failureEmbed("You can only make an idea/report every 12 hours!")).queue();
    			return;
    		} else {
    			ideaCooldownTimers.put(uid, cur + 43200000);
    		}
    	} else {
    		ideaCooldownTimers.put(uid, System.currentTimeMillis() + 43200000);
    	}
    	
		String p = prefixes.get(id);
    	if(args.length < 2) {
    		channel.sendMessage("Usage: " + p + "report <bug/user>").queue();
    		return;
    	}
    	String idea = combine(args, 1);
    	if(idea.length() < 20) {
    		channel.sendMessage("Report is too short! Please provide info on how to reproduce the bug/the user's discord tag and what they did.").queue();
    		return;
    	}
    	channel.sendMessage("**Thank you for submitting this bug/user report."
    		+ " The developers will be on it as soon as possible!**").queue();
    	Guild bb = getBonziBotGuild(tc.getJDA());
    	TextChannel ch = bb.getTextChannelsByName
    		("user-submitted-reports", true).get(0);
    	User u = member.getUser();
    	String name = u.getName() + "#"
    		+ u.getDiscriminator();
    	
    	EmbedBuilder eb = new EmbedBuilder();
    	eb.setColor(Color.magenta);
    	eb.setAuthor(name, null, u.getEffectiveAvatarUrl());
    	eb.addField("Report:", idea, false);
    	ch.sendMessage(eb.build()).queue();
    	return;
    }
    public void idea(String[] args, MessageChannel channel, TextChannel tc, Guild g, Member member, Message message) {
    	
    	long id = g.getIdLong();
    	long uid = member.getUser().getIdLong();
    	
    	if(ideaCooldownTimers.containsKey(uid)) {
    		long time = ideaCooldownTimers.get(uid);
    		long cur = System.currentTimeMillis();
    		if(time > cur) {
    			channel.sendMessage(failureEmbed("You can only make an idea/report every 12 hours!")).queue();
    			return;
    		} else {
    			ideaCooldownTimers.put(uid, cur + 43200000);
    		}
    	} else {
    		ideaCooldownTimers.put(uid, System.currentTimeMillis() + 43200000);
    	}
    	
		String p = prefixes.get(id);
    	if(args.length < 2) {
    		channel.sendMessage("Usage: " + p + "idea <text>").queue();
    		return;
    	}
    	String idea = combine(args, 1);
    	if(idea.length() < 20) {
    		channel.sendMessage(failureEmbed("Idea is too short! (Minimum 20 Characters!)")).queue();
    		return;
    	}
    	channel.sendMessage(successEmbed("Thanks for submitting your idea! The idea has gone into a channel in the official bonzibot discord (b:discord) and if we like it, we'll add it in asap!")).queue();
    	Guild bb = getBonziBotGuild(tc.getJDA());
    	TextChannel ch = bb.getTextChannelsByName
    		("user-submitted-ideas", true).get(0);
    	User u = member.getUser();
    	String name = u.getName() + "#"
    		+ u.getDiscriminator();
    	
    	EmbedBuilder eb = new EmbedBuilder();
    	eb.setColor(Color.magenta);
    	eb.setAuthor(name, null, u.getEffectiveAvatarUrl());
    	eb.addField("Idea:", idea, false);
    	ch.sendMessage(eb.build()).queue();
    	return;
    }
    public void latest(String[] args, MessageChannel channel, TextChannel tc, Guild g, Member member, Message message) {
    	/*l = GetUpdatesFromChannel(
    		GetUpdatesChannel(g.getJDA()));*/
    	int _amount = 3;
    	if(args.length > 1) {
    		String inp = args[1];
    		try {
    			int parse = Integer.parseInt(inp);
    			if(parse > 10) {
    				channel.sendMessage(failureEmbed("Maximum update count is 10!")).queue();
    				return;
    			}
    			_amount = parse;
    		} catch(NumberFormatException nfe) {
    			channel.sendMessage(failureEmbed("You must type a valid number! "
    			+ "(hint: you can also just run the command by itself to get the past 3 updates!)")).queue();
    			return;
    		}
    	}
    	final int amount = _amount;
		Message[] me = new Message[amount];
		TextChannel _tc = getUpdatesChannel(g.getJDA());
		_tc.getHistory().retrievePast(1).queue(past -> {
			long id = past.get(0).getIdLong();
			_tc.retrieveMessageById(id).queue(latest -> {
				int am = amount - 1;
				if(am <= 0) { am = 1; }
				_tc.getHistoryBefore(id, amount-1).queue(his -> {
					List<Message> history = his.getRetrievedHistory();
					for(int i = 0; i < amount; i++) {
						if(i == 0) { me[i] = latest; }
						else {
							me[i] = history.get(i-1);
						}
					}
			    	EmbedBuilder eb = new EmbedBuilder();
			    	eb.setColor(Color.magenta);
			    	eb.setTitle("Showing Latest " + amount + " BonziBot Updates!");
			    	for(Message m : me) {
			    		eb.addField(m.getTimeCreated()
			    		.toLocalDate().toString(),
			    		m.getContentRaw(), false);
			    	}
			    	channel.sendMessage(eb.build()).queue();
				});
			});
		});
    	return;
    }
    public void countingGame(Guild g, String text, Message msg, TextChannel tc) {
    	Long id = g.getIdLong();
    	User author = msg.getAuthor();
    	String mention = author.getAsMention();
    	if(!countingGames.containsKey(id)) {
    		countingGames.put(id, 0);
    	}
    	int cg = countingGames.get(id);
    	if(text.equalsIgnoreCase("?")) {
    		tc.sendMessage(mention + ", the next number is " + (cg+1) + "!").queue(mm -> {
    			mm.delete().queueAfter(3, TimeUnit.SECONDS);
    		});
    	}
    	int input;
    	try {
    		input = Integer.parseInt(text);
    	} catch(Exception exc) {
    		dontLog.add(msg.getIdLong());
    		msg.delete().queue();
    		return;
    	}
    	if(input != (cg+1)) {
    		dontLog.add(msg.getIdLong());
    		msg.delete().queue();
    		return;
    	} else {
    		cg++;
    		countingGames.put(id, cg);
    		
    		// 2/hr ratelimit now in place. Deprecated.
    		//tc.getManager().setTopic("(counting) Count as high as you can! Next number: " + othercg).queue();
    		
    		ChannelManager cm = tc.getManager();
    		String topic = tc.getTopic();
    		if(!topic.equalsIgnoreCase(COUNTING_DESCRIPTION)) {
    			cm.setTopic(COUNTING_DESCRIPTION).queue();
    		}
    		return;
    	}
    }
    public void xP(String[] args, MessageChannel channel, TextChannel tc, Guild g, Member member, Message message) {
    	User u = member.getUser();
    	Long id = u.getIdLong();
    	
    	// recalculated if (!mentioned.isEmpty()).
    	int userxp = xp.get(id);
    	int level = calculateLevel(userxp);
    	
    	List<Member> mentioned = message.getMentionedMembers();
    	if(mentioned.isEmpty()) {
        	EmbedBuilder eb = new EmbedBuilder();
        	eb.setColor(member.getColor());
        	eb.setAuthor(member.getEffectiveName(),
        		null, u.getEffectiveAvatarUrl());
        	eb.setTitle("Your Current XP");
        	eb.addField("XP:", String.valueOf(userxp), false);
        	eb.addField("Current Level: ", level + "", false);
        	channel.sendMessage(eb.build()).queue();
        	return;
    	} else {
    		// at least 1 mention so this is safe.
    		Member target = mentioned.get(0);
    		User ut = target.getUser();
    		long utid = ut.getIdLong();
    		if(!xp.containsKey(utid))
    			xp.put(utid, 0);
    		
    		// recalculate all the values
    		userxp = xp.get(ut.getIdLong());
    		level = calculateLevel(userxp);
    		
        	EmbedBuilder eb = new EmbedBuilder();
        	eb.setColor(target.getColor());
        	eb.setAuthor(target.getEffectiveName(),
        		null, target.getUser().getEffectiveAvatarUrl());
        	eb.setTitle(target.getEffectiveName() + "'s Current XP");
        	eb.addField("XP:", String.valueOf(userxp), false);
        	eb.addField("Current Level: ", level + "", false);
        	channel.sendMessage(eb.build()).queue();
    	}
    }
    public void pause(String[] args, MessageChannel channel, TextChannel tc, Guild g, Member member, Message message) {
    	if(channelHasModifier(tc, "nomusic")) {
    		channel.sendMessage(failureEmbed("Music commands are disabled in this channel. Please do them in the appropriate channel, thanks!")).queue(msg -> {
    			msg.delete().queueAfter(5, TimeUnit.SECONDS);
    		});
    		return;
    	}
    	GuildMusicManager gmm = getGuildAudioPlayer(g);
    	AudioPlayer ap = gmm.player;
    	if(!g.getSelfMember().getVoiceState().inVoiceChannel()) {
    		channel.sendMessage("I'm not playing any tracks right now!").queue();
    		return;
    	}
    	if(gmm.scheduler.queue.isEmpty() && ap.getPlayingTrack() == null) {
    		channel.sendMessage("I'm not playing any tracks right now!").queue();
    		return;
    	}
    	if(ap.isPaused()) {
    		ap.setPaused(false);
    		channel.sendMessage("Okay, unpaused the music!").queue();
    		return;
    	} else {
    		ap.setPaused(true);
    		channel.sendMessage("Okay, paused the music!").queue();
    		return;
    	}
    }
    public void queue(String[] args, MessageChannel channel, TextChannel tc, Guild g, Member member, Message message) {
    	if(channelHasModifier(tc, "nomusic")) {
    		message.delete().queue();
    		channel.sendMessage(failureEmbed("Music commands are disabled in this channel. Please do them in the appropriate channel, thanks!")).queue(msg -> {
    			msg.delete().queueAfter(5, TimeUnit.SECONDS);
    		});
    		return;
    	}
    	long gid = g.getIdLong();
    	EmbedBuilder eb;
    	eb = new EmbedBuilder();
    	eb.setTitle("Tracks in the queue!");
    	eb.setColor(Color.magenta);
    	
    	GuildMusicManager gmm;
    	gmm = getGuildAudioPlayer(g);
    	
    	BlockingQueue<AudioTrack> tracks;
    	tracks = gmm.scheduler.queue;
    	
    	AudioTrack current = gmm.player.getPlayingTrack();
    	if(tracks.isEmpty()) {
    		if(current != null) {
    	    	AudioTrackInfo _info = current.getInfo();
    	    	Long _duration = current.getDuration();
    	    	String _title = _info.title;
    	    	String _author = _info.author;
    	    	String _dur = getDurationBreakdown(_duration);
    	    	eb.addField("⏯️ Current Track:\n" + _title,"Author: " 
    	    			+ _author + "\n" + "Duration: "+ _dur, false);
    	    	eb.setFooter("Looping: " + getEmojiForBoolean(loop.contains(gid)) +
    	    			"\nLooping Queue: " + getEmojiForBoolean(queueloop.contains(gid)) +
    	    			"\nShuffle: " + getEmojiForBoolean(shuffle.contains(gid)));
    	    	channel.sendMessage(eb.build()).queue();
    	    	return;
    		}
    		channel.sendMessage(failureEmbed("There are no audio tracks in the queue/playing!")).queue();
    		return;
    	}
    	
    	if(current != null) {
        	AudioTrackInfo _info = current.getInfo();
        	Long _duration = current.getDuration();
        	String _title = _info.title;
        	String _author = _info.author;
        	String _dur = getDurationBreakdown(_duration);
        	eb.addField("⏯️ Current Track:\n" + _title,"Author: " 
        			+ _author + "\n" + "Duration: "+ _dur, false);
    	}

    	for(AudioTrack tr : tracks) {
    		AudioTrackInfo info = tr.getInfo();
    		Long duration = tr.getDuration();
    		String title = info.title;
    		String author = info.author;
    		String dur = getDurationBreakdown(duration);
    		eb.addField(title, "Author: " + author + "\n"
    				+ "Duration: " + dur, false);
    	}
    	eb.setFooter("Looping: " + getEmojiForBoolean(loop.contains(gid)) +
    			"\nLooping Queue: " + getEmojiForBoolean(queueloop.contains(gid)) +
    			"\nShuffle: " + getEmojiForBoolean(shuffle.contains(gid)));
    	channel.sendMessage(eb.build()).queue();
    	return;
    }
    public void play(String[] args, MessageChannel channel, TextChannel tc, Guild g, Member member, Message message, boolean superplay) {
    	if(channelHasModifier(tc, "nomusic")) {
    		channel.sendMessage(failureEmbed("Music commands are disabled in this channel. Please do them in the appropriate channel, thanks!")).queue(msg -> {
    			msg.delete().queueAfter(5, TimeUnit.SECONDS);
    		});
    		return;
    	}
    	if(args.length < 2) {
    		if(!superplay) {
    			channel.sendMessage(failureEmbed("Usage: play <song name/url>")).queue();
    		} else {
    			channel.sendMessage(failureEmbed("Usage: superplay <song name/url>")).queue();
    		}
    		return;
    	}
    	StringBuilder sb = new StringBuilder();
    	int i = 0;
    	for(String word : args) {
    		i++;
    		if(i==1) {continue;}
    		sb.append(" " + word);
    	}
    	sb = sb.deleteCharAt(0);
    	String url = sb.toString();
		if(!member.getVoiceState().inVoiceChannel()) {
			channel.sendMessage(failureEmbed("You can only begin songs when you're in a voice channel.")).queue();
			return;
		}
		VoiceChannel vc;
		GuildVoiceState gvs;
		AudioManager am;
		gvs = member.getVoiceState();
		vc = gvs.getChannel();
		am = g.getAudioManager();
		am.openAudioConnection(vc);
		// =============================================== end of doo doo borings /\
		loadAndPlay(tc, url, superplay);
		return;
    }
    public void skip(String[] args, MessageChannel channel, TextChannel tc, Guild g, Member member, Message message) {
    	if(channelHasModifier(tc, "nomusic")) {
    		channel.sendMessage(failureEmbed("Music commands are disabled in this channel. Please do them in the appropriate channel, thanks!")).queue(msg -> {
    			msg.delete().queueAfter(5, TimeUnit.SECONDS);
    		});
    		return;
    	}
    	Member self = g.getSelfMember();
    	if(!self.getVoiceState().inVoiceChannel()) {
    		channel.sendMessage(failureEmbed("I'm not playing music right now!")).queue(msg -> {
    			msg.delete().queueAfter(5, TimeUnit.SECONDS);
    		});
    		return;
    	}
    	GuildVoiceState gvs = self.getVoiceState();
    	VoiceChannel vc = gvs.getChannel();
    	if(!member.getVoiceState().inVoiceChannel()) {
    		channel.sendMessage(failureEmbed("Only the people who are actually listening to the music can skip!")).queue(msg -> {
    			msg.delete().queueAfter(5, TimeUnit.SECONDS);
    		});
    		return;
    	}
    	VoiceChannel mvc = member
			.getVoiceState()
			.getChannel();
    	long id1, id2;
    	id1 = vc.getIdLong();
    	id2 = mvc.getIdLong();
    	if(id1 != id2) {
    		channel.sendMessage(failureEmbed("Only the people who are actually listening to the music can skip!")).queue(msg -> {
    			msg.delete().queueAfter(5, TimeUnit.SECONDS);
    		});
    		return;
    	}
    	
    	int amount = 1;
    	if(args.length > 1) {
    		String _amount = args[1];
    		try {
    			amount = Integer.parseInt(_amount);
    		} catch(Exception exc) {
        		channel.sendMessage(failureEmbed("Not a valid amount of tracks to skip!")).queue();
        		return;
    		}
    	}
    	skipTrack(tc, true, amount, false);
    	return;
    }
    public void skipAll(String[] args, MessageChannel channel, TextChannel tc, Guild g, Member member, Message message) {
        if(!isMod(member, g)) {
        	channel.sendMessage(failureEmbed("You don't have permission to use this!")).queue(m -> {
        		m.delete().queueAfter(5, TimeUnit.SECONDS);
        	});
        	return;
        }
    	if(channelHasModifier(tc, "nomusic")) {
    		channel.sendMessage(failureEmbed("Music commands are disabled in this channel. Please do them in the appropriate channel, thanks!")).queue(msg -> {
    			msg.delete().queueAfter(5, TimeUnit.SECONDS);
    		});
    		return;
    	}
    	GuildMusicManager gmm = getGuildAudioPlayer(g);
    	gmm.scheduler.queue.clear();
    	skipTrack(tc, false, 1, true);
    	channel.sendMessage(successEmbed("Cleared all tracks from the queue.")).queue();
    	g.getAudioManager().closeAudioConnection();
    	return;
    }
    public void removeTag(String[] args, MessageChannel channel, Guild g, Member member, Message message) {
    	if(args.length < 2) {
    		channel.sendMessage("Usage: admin:removecc <name>").queue();
    		return;
    	}
    	String s;
    	
    	int i = 0;
    	StringBuilder sb = new StringBuilder();
    	for(String word : args) {
    		i++;
    		if(i == 1) {continue;}
    		sb.append(" " + word);
    	}
    	sb = sb.deleteCharAt(0);
    	s = sb.toString();
    	
    	CCM_ReturnCode rc;
    	rc = ccm.RemoveCustomCommand(s);
    	
    	if(rc.equals(CCM_ReturnCode.DoesntExist)) {
    		channel.sendMessage("Public CustomCommand \"" + s + "\" doesn't exist.").queue();
    		return;
    	} else {
    		channel.sendMessage("Removed!").queue();
    	}
    	return;
    }
    public void tag(String[] args, MessageChannel channel, Guild g, Member member, Message message) {
    	
    	long gid = message.getGuild().getIdLong();
    	if(!acceptedNSFW.contains(gid)) {
    		EmbedBuilder eb = new EmbedBuilder();
    		eb.setColor(Color.red);
    		eb.setTitle("This command is considered \"risky.\"");
    		eb.setDescription("While very unlikely, the tag command could potentially allow inappropriate content in non-nsfw channels. An administrator of the server can enable/disable this using the following commands:");
    		eb.addField("b:acceptnsfw", "Allow risky commands in this server.", false);
    		eb.addField("b:disablensfw", "Do not allow risky commands in this server.", false);
    		channel.sendMessage(eb.build()).queue();
    		return;
    	}
    	
    	if(ccBans.contains(member.getUser().getIdLong())) {
    		channel.sendMessage("You're banned from using tags. Contact a known bonzibot admin if you believe this is unfair.").queue();
    		return;
    	}
    	if(args.length < 2) {
    		channel.sendMessage("Usage: cc <name>").queue();
    		return;
    	}
    	
    	// legacy code
    	User u;
    	Long id;
    	String s;
    	u = member.getUser();
    	id = u.getIdLong();
    	int i = 0;
    	StringBuilder sb = new StringBuilder();
    	for(String word : args) {
    		i++;
    		if(i == 1) {continue;}
    		sb.append(" " + word);
    	}
    	sb = sb.deleteCharAt(0);
    	s = sb.toString();
    	
    	if(s.length() > 256) {
    		channel.sendMessage(failureEmbed("Name can't be longer than 256 characters!")).queue();
    		return;
    	}
    	
    	CustomCommand cc;
    	cc = ccm.public_GetByName(s);
    	if(cc.command.equalsIgnoreCase("NotFound")) {
    		Set<Entry<Long, String>> es;
    		es = CCResponseQueue.entrySet();
    		for(Entry<Long, String> kv : es) {
    			if(kv.getValue().equalsIgnoreCase(s)) {
    				channel.sendMessage(failureEmbed("Somebody is already creating this command!")).queue();
    				return;}}
    		
    		CCResponseQueue.put(id, s);
    		channel.sendMessage(successEmbed("Lucky you! This tags doesn't exist yet! The next message you type will be the response when anyone types it!")).queue();
    		return;
    	}
    	List<String> sw;
    	verifyFilters(g);
    	sw = getFilter(g);
    	TextChannel tc = (TextChannel)channel;
    	for(String swear : sw) {
    		if(tc.isNSFW()) { break; }
    		if(cc.response.toLowerCase()
    		.contains(swear.toLowerCase())) {
				message.delete().queue();
				channel.sendMessage(failureEmbed("This tags contains filtered words, so I've blocked it in this server.")).queue(mm->mm.delete().queueAfter(5, TimeUnit.SECONDS));
				return;
    		}
    	}
    	cc.response = cc.response.replaceAll("@everyone", "**(Attempted everyone ping)**");
    	cc.response = cc.response.replaceAll("@here", "**(Attempted here ping)**");
    	cc.used();
    	String rsp;
    	rsp = cc.response.replace
    		("@everyone", "everyone");
    	channel.sendMessage(rsp).queue();
    	return;
    }
    public void tagLeaderboard(String[] args, MessageChannel channel, Guild g, Member member, Message message) {
    	/*if(getUpgrades(g) >= PRIVATE_TAG_COST) {
    		channel.sendMessage("This only works in discord servers with public tags! (this may be implemented later on!)").queue();
    		return;
    	}*/
    	List<CustomCommand> ccs = ccm.global;
    	Comparator<CustomCommand> c = new Comparator<CustomCommand>() {
    		public int compare(CustomCommand a, CustomCommand b) {
    			Integer _a, _b;
    			_a = a.uses;
    			_b = b.uses;
    			return _a.compareTo(_b);
    		}
    	};
    	Collections.sort(ccs, Collections.reverseOrder(c));
		EmbedBuilder eb;
		eb = new EmbedBuilder();
    	try {
    		eb.setTitle("Top used tags! (10/" + ccs.size() + " shown)");
    		eb.setColor(Color.magenta);
    		
    		for(int i = 0; i < 10; i++) {
    			CustomCommand cc;
    			cc = ccs.get(i);
    			eb.addField(cc.command,
    				"Used **" + cc.uses + "** times.\n"
    				+ "Created By: " + cc.creator + "\n"
    				+ "Discord server: " + cc.server + "\n"
    				+ "Date: " + cc.created.toString(), false);
    		}
    	} catch(Exception exc) {
    		// Do nothing.
    	}
    	channel.sendMessage(eb.build()).queue();
    	return;
    }
    public void tagInfo(String[] args, MessageChannel channel, Guild g, Member member, Message message) {
    	/*if(getUpgrades(g) >= PRIVATE_TAG_COST) {
    		channel.sendMessage("This only works in discord servers with public tags! (this may be added later on!)").queue();
    		return;
    	}*/
    	if(args.length < 2) {
    		channel.sendMessage("Usage: taginfo [name]\n"
    			+ "Or: taginfo @<user>").queue();
    		return;
    	}
    	List<Member> mentioned;
    	mentioned = message.getMentionedMembers();
    	if(!mentioned.isEmpty()) {
    		Member target = mentioned.get(0);
    		Long id = target.getUser().getIdLong();
    		List<CustomCommand> owned = 
    				new ArrayList<CustomCommand>();
			for(CustomCommand cmd : ccm.global) {
				if(cmd.creatorID.equals(id)) {
					owned.add(cmd);
					continue;
				}
			}
	    	Comparator<CustomCommand> c = new Comparator<CustomCommand>() {
	    		public int compare(CustomCommand a, CustomCommand b) {
	    			Integer _a, _b;
	    			_a = a.uses;
	    			_b = b.uses;
	    			return _a.compareTo(_b);
	    		}
	    	};
	    	Collections.sort(owned, Collections.reverseOrder(c));
	    	EmbedBuilder eb = new EmbedBuilder();
	    	eb.setColor(Color.magenta);
	    	eb.setTitle("This user's top 10/" + owned.size() + " tags:");
	    	int count = 0;
	    	for(CustomCommand cc : owned) {
	    		count++;
	    		if(count > 10) {
	    			break;
	    		}
	    		if(cc.command.length() > 256) {
	    			channel.sendMessage("Somehow this person had a tag with a name longer than 256 letters like wtf?\nanyways i removed the tag so it should work now").queue();
	    			ccm.RemoveCustomCommand(cc.command);
	    			return;
	    		}
	    		eb.addField(cc.command,
	    			"Uses: " + cc.uses, false);
	    	}
	    	channel.sendMessage(eb.build()).queue();
    		return;
    	}
    	String cmd;
    	cmd = combine(args, 1);
    	CustomCommand cc;
    	cc = ccm.public_GetByName(cmd);
    	if(cc.response.equalsIgnoreCase("NotFound")) {
    		channel.sendMessage("There is no tag with this name!").queue();
    		return;
    	}
    	EmbedBuilder eb = new EmbedBuilder();
    	eb.setTitle("Tag Info").setColor(Color.magenta);
    	eb.addField(cc.command,
    		"Used **" + cc.uses + "** times.\n"
        	+ "Created By: **" + cc.creator + "**\n"
        	+ "Discord server: **" + cc.server + "**\n"
        	+ "Date: **" + cc.created.toString() + "**", false);
    	channel.sendMessage(eb.build()).queue();
    	return;
    }
    public void tagPrivate(String[] args, MessageChannel channel, Guild g, Member member, Message message) {
    	
    	long gid = g.getIdLong();
    	if(!acceptedNSFW.contains(gid)) {
    		EmbedBuilder eb = new EmbedBuilder();
    		eb.setColor(Color.red);
    		eb.setTitle("This command is considered \"risky.\"");
    		eb.setDescription("While very unlikely, the tag command could potentially allow inappropriate content in non-nsfw channels. An administrator of the server can enable/disable this using the following commands:");
    		eb.addField("b:acceptnsfw", "Allow risky commands in this server.", false);
    		eb.addField("b:disablensfw", "Do not allow risky commands in this server.", false);
    		channel.sendMessage(eb.build()).queue();
    		return;
    	}
    	
    	if(ccBans.contains(member.getUser().getIdLong())) {
    		channel.sendMessage("You're banned from using tags. Contact a known bonzibot admin if you believe this is unfair.").queue();
    		return;
    	}
    	
    	boolean contains = upgrades.containsKey(gid);
    	if(!contains || contains && upgrades.get(gid) < PRIVATE_TAG_COST) {
    		channel.sendMessage(failureEmbed("Your server needs at least " + PRIVATE_TAG_COST + " upgrades to use this! Check the 'upgrade' command!")).queue();
    		return;
    	}
    	
    	String s;
    	Long id;
    	id = member.getUser().getIdLong();
    	int i = 0;
    	StringBuilder sb = new StringBuilder();
    	for(String word : args) {
    		i++;
    		if(i == 1) {continue;}
    		sb.append(" " + word);
    	}
    	sb = sb.deleteCharAt(0);
    	s = sb.toString();
    	
    	CustomCommand cc;
    	cc = ccm.private_GetByName(s, g);
    	if(cc.command.equalsIgnoreCase("NotFound")) {
    		_CCResponseQueue.put(id, s);
    		channel.sendMessage(successEmbed("Lucky you! This private tag doesn't exist yet! The next message you type will be the response for this command!")).queue();
    		return;
    	}
    	channel.sendMessage(cc.response).queue();
    	return;
    }
    public void reactionRole(String[] args, Message message, MessageChannel channel, Member m, User u, Guild g, String prefix) {
        if(!isMod(m, g)) {
        	channel.sendMessage(failureEmbed("You don't have permission to use this!")).queue(mm -> {
        		mm.delete().queueAfter(5, TimeUnit.SECONDS);
        	});
        	return;
        }
        if(args.length < 2) {
        	channel.sendMessage(failureEmbed("Use it like this:\n"+prefix+"reactionrole <@role>\n"+prefix+"reactionrole rolename")).queue();
        	return;
        }
        List<Role> roles;
        roles = message.getMentionedRoles();
        if(roles.isEmpty()) {
        	channel.sendMessage(failureEmbed("You must specify which roles the user will receive!")).queue();
        	return;
        }
        Role role = roles.get(0);
        channel.getHistoryBefore(message, 1).queue(history-> {
        	List<Message> msgs = history.getRetrievedHistory();
        	Message target;
            try {
            	target = msgs.get(0);
            } catch(Exception exc) {
            	// Message is deleted.
            	channel.sendMessage(failureEmbed("Unexpected error occurred. (ReactionRole-0029)")).queue();
            	return;
            }
            ReactionRole rr;
            long me,r,ch;
            me = target.getIdLong();
            r = role.getIdLong();
            ch = channel.getIdLong();
            rr = new ReactionRole(ch,me,r);
            addReactionRole(rr, g);
            message.delete().queue();
            channel.sendMessage(successEmbed("Now when someone reacts to the previous message, they will receive that role!")).queue(mm->mm.delete().queueAfter(5, TimeUnit.SECONDS));
            return;
       });
    }
    public void subreddit(MessageChannel channel, String[] args, Message message) {
    	
    	long gid = message.getGuild().getIdLong();
    	if(!acceptedNSFW.contains(gid)) {
    		EmbedBuilder eb = new EmbedBuilder();
    		eb.setColor(Color.red);
    		eb.setTitle("This command is considered \"risky.\"");
    		eb.setDescription("The subreddit command could potentially allow inappropriate content in non-nsfw channels. An administrator of the server can enable/disable this using the following commands:");
    		eb.addField("b:acceptnsfw", "Allow risky commands in this server.", false);
    		eb.addField("b:disablensfw", "Do not allow risky commands in this server.", false);
    		channel.sendMessage(eb.build()).queue();
    		return;
    	}
    	
        if(args.length == 1) {
            channel.sendMessage("You must put a subreddit!").queue();
            return;
        }
	    Submissions subms = new Submissions(restClient);
	    
	    List<Submission> submiss;
	    try {
	    	submiss = subms.ofSubreddit(args[1], SubmissionSort.NEW, -1 , 100, null, null, true);
	    } catch(RetrievalFailedException rfe) {
	    	channel.sendMessage("Couldn't find anything in that subreddit!").queue(m -> {
	    		m.delete().queueAfter(5, TimeUnit.SECONDS);
	    	});
	    	return;
	    }
	    SecureRandom rand = new SecureRandom();
	    int randomNumber = rand.nextInt(submiss.size());
	    Submission meme = submiss.get(randomNumber);
	    channel.sendMessage(meme.getTitle() + "\n" + meme.getURL()).queue();
    }
    public void meme(MessageChannel channel,String[] args , Message message) {
        
        Submissions subms = new Submissions(restClient);
        List<Submission> submiss = subms.ofSubreddit("dankmemes", SubmissionSort.RISING, -1 , 100, null, null, true);
        SecureRandom rand = new SecureRandom();
        int randomNumber = rand.nextInt(100);
        Submission meme = submiss.get(randomNumber);
        channel.sendMessage(meme.getTitle() + "\n" + meme.getURL()).queue();
        
    }
    public void clear(String[] args, Message message, MessageChannel channel, Member member, Guild g) {
        if(!isMod(member, g)) {
        	channel.sendMessage(failureEmbed("You don't have permission to use this!")).queue(m -> {
        		m.delete().queueAfter(5, TimeUnit.SECONDS);
        	});
        	return;
        }
    	
    	String a = args[1];
        int amount = -1;
        try {
	        amount=Integer.parseInt(a);
        } catch(NumberFormatException nfe) {
	        channel.sendMessage(failureEmbed("Usage: clear <amount>")).queue(m -> {
	        	m.delete().queueAfter(5, TimeUnit.SECONDS);
	        });
	        message.delete().queue();
	        return;
        }
        if(amount > 100) { 
	        channel.sendMessage(failureEmbed("You can't pick a number higher than 100.")).queue(m -> {
	        	m.delete().queueAfter(5, TimeUnit.SECONDS);
	        });
	        message.delete().queue();
	        return;
        }
        if(amount < 1) { 
	        channel.sendMessage(failureEmbed("You can't pick a number below 1.")).queue(m -> {
	        	m.delete().queueAfter(5, TimeUnit.SECONDS);
	        });
	        message.delete().queue();
	        return;
        }
        String mid = message.getId();
        int _amount = amount;
        channel.getHistoryBefore(mid, amount).queue(mh -> {
            List<Message> messages = mh.getRetrievedHistory();
            for(Message _m : messages) {
            	long id = _m.getIdLong();
            	dontLog.add(id);
            }
            channel.purgeMessages(messages);
            channel.sendMessage("You've cleared " + _amount + " messages.").queue(m -> {
            	m.delete().queueAfter(5, TimeUnit.SECONDS);
            });
            LoggingEntry le = new LoggingEntry();
            le.targetUser = member.getIdLong();
            le.invokedUser = member.getIdLong();
            le.type = LogType.ClearMsgs;
            le.reason = "null";
            EmbedBuilder eb = baseLogEmbed("Clear command was run.", member.getUser(), Color.orange);
            eb.setDescription("Cleared a total of " + _amount + " messages.");
            le.embed = eb.build();
            tryLog(g, le);
            
            message.delete().queue();
        });
    }
    public void poll(String[] args, User user, Message message, MessageChannel channel, Member mem) {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        for(String word : args) {
            i++;
            if(i==1){continue;}
            sb.append(" " + word);
        }
        sb = sb.deleteCharAt(0);
        String finished = sb.toString();
        message.delete().queue();
        EmbedBuilder eb = new EmbedBuilder();
        
        eb.setColor(roleColor(mem));
        eb.setAuthor(mem.getEffectiveName(), null,
        	mem.getUser().getEffectiveAvatarUrl());
        eb.setTitle(finished);
        eb.setDescription("Vote up or down!");
        
        channel.sendMessage(eb.build()).queue(m -> {
            m.addReaction("👍").queue();
            m.addReaction("👎").queue();
        });
    }
    public void help(User u, MessageChannel channel, String[] args, String prefix, Guild g) {
    	String eaurl = u.getEffectiveAvatarUrl();
    	
    	long gID = g.getIdLong();
    	boolean hasSC = scriptCreators.containsKey(gID);
    	
		EmbedBuilder eb = new EmbedBuilder();
		eb.setAuthor("Help Menu", null, eaurl);
		eb.setColor(Color.magenta);
		
		if(args.length == 1) {
			eb.addField("📰 Latest News", prefix+"latest\n" + prefix + "latest <amount>\n(See the latest news/updates published to BonziBot!)", false);
			eb.addBlankField(false);
			eb.addField("🎊 Fun", prefix + "help fun", true);
			eb.addField("🟡 Coins", prefix + "help coins", true);
			eb.addField("👮 Moderator", prefix + "help moderator\n"+prefix+"help mod", true);
			eb.addField("🛠️ Utilities", prefix + "help utilities\n"+prefix+"help utils", true);
			eb.addField("🎵 Music", prefix + "help music", true);
			eb.addField("👓 Passive", prefix + "help passive", true);
			eb.addField("🚀 Upgrade Commands", prefix + "help upgrade", true);
			if(hasSC) {
				eb.addField("🖥️ Server Commands", prefix + "help server", true);
			}
		} else if(args.length == 2) {
			String query = args[1];
			if(query.toLowerCase().startsWith("util")) {
				eb.addField("❓ Help", prefix+"help <query>\n(Get help with bonzibot's commands.)", false);
				eb.addField("👋 Join Message", prefix+"message join <text>\nTIP: You can put (user) or (server) in the text to make it say the user's name, or the server's name.", false);
				eb.addField("🚪 Leave Message", prefix+"message leave <text>\nTIP: You can put (user) or (server) in the text to make it say the user's name, or the server's name.", false);
				eb.addField("#️⃣ Message Channel", prefix+"message channel #channel\n(The channel join/leave messages will be sent in.)", false);
				eb.addField("🤐 Enable/Disable Join/Leave Messages", prefix+"message enable/disable\n(Self explanitory.)", false);
				eb.addField("👦 Join Role", prefix+"joinrole @<role>\n" + prefix + "joinrole none\n(The role that a user will receive when they join.)", false);
				eb.addField("😱 Reaction Role", prefix+"reactionrole @<role>\n(Makes it so that the previous message will give a role when you react to it.)", false);
				
				eb.addBlankField(false);
				eb.addField("💾 Backup", prefix+"backup\n(Saves your entire discord server incase of a raid!)", false);
				eb.addField("💾⤴️ Restore", prefix+"restore\n(Restores your server (ONLY IF YOU MADE A BACKUP!))", false);
			} else if(query.toLowerCase().startsWith("mod")) {
				eb.addField("💢 Clear", prefix+"clear <amount>\n(Removes x messages at once.)", false);
				eb.addField("➡️ Prefix", prefix+"prefix <prefix>\n(Sets my prefix on this server to whatever you want.)", false);
				eb.addField("👮 Mod Role", prefix+"modrole\n" + prefix+"modrole @<role>\n(Either shows what the mod role is, or sets the moderator role.)", false);
				eb.addField("🤬 Filter", prefix+"filter\n(Censor certain words that your server doesn't allow.)", false);
				eb.addBlankField(false);
				eb.addField("⚠️ Warn", prefix+"warn @<user> <reason>\n(Warn the user. This goes on permanent record for your mods to use!)", false);
				eb.addField("🚫 Remvove Warn(s)", prefix+"warn remove @<user> <reason>\n(Remove all the warns on the user with a specific reason.\n"
						+ "Example, if you warned someone for \"Being too cool\" than you can remove it by doing \"warn remove @<user> Being too cool)\"", false);
				eb.addField("⛔ Clear Warns", prefix+"clearwarns @<user>\n(Clear all warns off this user.)", false);
				eb.addField("🤐 Mute", prefix+"mute @<user>\n(Prevents the user from talking!)", false);
				eb.addField("😃 Unmute", prefix+"unmute @<user>\n(Allows the user to talk!)", false);
				eb.addField("🦵 Kick", prefix+"kick @<user>\n(Kicks the user.)", false);
				eb.addField("🔨 Ban", prefix+"ban @<user>\n(Bans the user.)", false);
			} else if(query.toLowerCase().startsWith("fun")) {
				eb.addField("🤖 Say", prefix+"say <text>\n(Make me say something!)", false);
				eb.addField("😹 Meme", prefix+"meme\n(I'll post a spicy meme!)", false);
				eb.addField("🇷 Subreddit", prefix+"subreddit <subreddit>\n(Get a random post from that subreddit!)", false);
				eb.addField("🤣 Joke", prefix+"joke\n(I'll make a cheesy dad joke...)", false);
				eb.addField("❔ Poll", prefix+"poll <title>\n(Makes a poll that people can upvote or downvote!)", false);
				eb.addField("🌑 Coinflip", prefix+"coinflip\n(Literally just flips a coin...)", false);
				// OBSOLETE: eb.addField("🎥 Video Chat", prefix+"videochat\n(Creates a button you can press to open a video chat right in the discord server!)", false);
				// OBSOLETE: eb.addField("ðŸ—£ Speak", prefix+"speak <text>\n(Makes me speak in voice chat! Can be skipped with b:skip like any other music track.)", false);
				eb.addBlankField(false);
				eb.addField("📜 Tag", prefix+"tag <name>\n(Access a huge library of \"tags\" made by other bonzibot users! If it doesn't already exist, then you get to make your own!)", false);
				eb.addField("📜🏆 Tag Leaderboard", prefix+"tagleaderboard\n(Shows the most popular tags.)", false);
				eb.addField("📜❓ Tag Info", prefix+"taginfo <name>/<@user>\n(Displays info about said tag/user.)", false);
				eb.addBlankField(false);
				eb.addField("🎓 XP", prefix+"xp\n" + prefix + "xp @<user> (Displays how much xp you/someone has.)", false);
				eb.addField("🎓🏆 XP Leaderboard", prefix+"xpleaderboard\n(See the top xp users in the world!)", false);
			} else if(query.toLowerCase().startsWith("music")) {
				eb.addField("➕ Play", prefix+"play <song name/url>\n(I'll queue up your selected song!)", false);
				eb.addField("💿 Playlist", prefix+"playlist\n(Create, edit, and play compilations of songs by the community!)", false);
				eb.addField("🎼 Queue", prefix+"queue\n(See all songs waiting to be played.)", false);
				eb.addField("▶️ Skip", prefix+"skip\n" + prefix + "skip <amount>(Skip the current song(s).)", false);
				eb.addField("▶️▶️ Skip All", prefix+"skipall\n(Clear the queue of all songs. Moderator only.)", false);
				eb.addField("⏯️ Pause/Resume", prefix+"pause\n(Toggles the music player being paused or not.)", false);
				eb.addField("🔁 Loop", prefix+"loop\n(Will loop the current song. If skip is run, the next song will begin looping.)", false);
				eb.addField("♾️ Loop Queue", prefix+"loopqueue\n(Will loop all the songs in the queue.)", false);
				eb.addField("🔀 Shuffle", prefix+"shuffle\n(Will pick a random song from the queue each time. Doesn't deplete the queue.)", false);
				eb.addField("🚪 Disconnect", prefix+"disconnect\n(This will clear the queue and disconnect me from the voice channel. Note that I automatically do this when everyone leaves, too!)", false);
			} else if(query.toLowerCase().startsWith("passive")) {
				eb.addField("🎰🏆 Counting Leaderboard", prefix+"countingleaderboard\n(See the servers that counted the highest!)", false);
				eb.addField("🧩 Channel Modifiers", prefix+"modifiers\n(List of words you can insert into channel topics to modify how I act in them! Includes lots of minigames and useful features!)", false);
			} else if(query.toLowerCase().startsWith("coins")) {
				eb.addField("🟡 Show Coins", prefix+"coins\n(Shows how many coins you have.)", false);
				eb.addField("🎁 Pay", prefix+"pay @<user> <amount>\n(Pay someone your coins!)", false);
				eb.addBlankField(false);
				eb.addField("🎰 Slots", prefix+"slots <amount>\n(Play slots to either gain/lose coins!)", false);
				eb.addField("🎲 Chance", prefix+"chance <amount>\n(50/50 chance to either double your input, or lose it all!)", false);
				eb.addField("🎟️ Lottery", prefix+"lottery\n(Pay 10 coins, 1/5000 chance to win it all!)", false);
				eb.addBlankField(false);
				eb.addField("🛒 Shop", prefix+"shop\n(See all that you can buy with coins!)", false);
			} else if(query.toLowerCase().startsWith("upgrade")) {
				eb.addField("🚀 upgrade", prefix+"upgrade\n"+prefix+"upgrade <amount>\n(Power up your server and let active users collaborate to unlock new BonziBot features!)", false);
				eb.addField("🎖️ upgrades", prefix+"upgrades\n(See the current server's upgrades and the perks that come with upgrading!)", false);
				eb.addField("👑 prestige", prefix+"prestige\n(See the prestige leaderboard. A prestige is gained every 1000 upgrades.)", false);
				eb.addBlankField(false);
				eb.addField("📜 Private Tag", prefix+"privatetag <name>\n(Same as the tag command, but all the commands are specific for your server!)", false);
				eb.addField("💻 Script Editor", prefix+"scripteditor\n(Opens a full UI with the ability to \"code\" your own commands!)", false);
			} else if(query.toLowerCase().startsWith("server") && hasSC) {
	        	ScriptCreator sc = scriptCreators.get(gID);
	        	for(CustomScript script : sc.scripts) {
	        		eb.addField(script.name, prefix + script.condition.toString(), false);
	        	}
			} else {
				channel.sendMessage("Not a help category!").queue(m -> {
					m.delete().queueAfter(5, TimeUnit.SECONDS);
				});
				return;
			}
		} else {
			channel.sendMessage("Use it like this: " + prefix + "help <query>").queue(m -> {
				m.delete().queueAfter(5, TimeUnit.SECONDS);
			});
			return;
		}
		channel.sendMessage(eb.build()).queue();
    }
    public void prefix(User u, MessageChannel channel, String[] args, String prefix, Long guildID, Member member, Guild g) {
        if(!isMod(member, g)) {
        	channel.sendMessage(failureEmbed("You don't have permission to use this!")).queue(m -> {
        		m.delete().queueAfter(5, TimeUnit.SECONDS);
        	});
        	return;
        }
    	
    	if(args.length != 2) {
    		channel.sendMessage(failureEmbed("Usage: " + prefix + "prefix <prefix>")).queue(message -> {
    			message.delete().queueAfter(5, TimeUnit.SECONDS);
			});
    		return;
    	}
    	String oldPrefix = prefixes.get(guildID);
    	String newPrefix = args[1];
    	
    	if(newPrefix.contains(" ")) {
    		channel.sendMessage(failureEmbed("Your prefix can't have a space in it!")).queue();
    		return;
    	}
    	
    	if(newPrefix.length() > 128) {
    		channel.sendMessage(failureEmbed("Your prefix can't be over 128 characters. (Why would you even want to do this lol??)")).queue();
    		return;
    	}
    	
    	prefixes.put(guildID, newPrefix);
    	channel.sendMessage(successEmbed("Okay, I set your server's prefix to \"" + newPrefix + "\"!")).queue();
    	
        LoggingEntry le = new LoggingEntry();
        le.targetUser = member.getIdLong();
        le.invokedUser = member.getIdLong();
        le.type = LogType.PrefixChange;
        le.reason = "null";
        le.ext = oldPrefix;
        EmbedBuilder eb = baseLogEmbed("Server prefix changed.", member.getUser(), Color.orange);
        eb.setDescription("New Prefix: \"" + newPrefix + "\"\n"
        	+ "Old Prefix: \"" + oldPrefix + "\"\n\nPress undo to revert this modification.");
        le.embed = eb.build();
        tryLog(g, le, "b_undo");
    	return;
    }
    public void say(String[] args, User user, Message message, MessageChannel channel, Guild g) {
    	verifyFilters(g);
    	List<String> filter;
    	filter = getFilter(g);
        try {
        	@SuppressWarnings("unused")
			String dummy = args[1];
        } catch(ArrayIndexOutOfBoundsException oobe) {
            channel.sendMessage("Please specify a message for me to say.").queue(m -> {
            	m.delete().queueAfter(5, TimeUnit.SECONDS);
            });
            return;
        }
        StringBuilder sb = new StringBuilder();
        int i = 0;
        for(String word : args) {
            i++;
            if(i==1){continue;}
            sb.append(" " + word);
        }
        sb = sb.deleteCharAt(0);
        String finished = sb.toString();
        String[] _sa = finished.split(" ");
    	for(String word : _sa) {
    		if(channelHasModifier((TextChannel)channel, "nofilter")) { break; }
    		for(String swear : filter) {
    			if(word.toLowerCase().contains(swear.toLowerCase())) {
    				message.delete().queue();
    				channel.sendMessage(user.getAsMention() + " you can't try to make me bypass the filter!").queue(msg -> {
    					msg.delete().queueAfter(5, TimeUnit.SECONDS);
    				});
    				return;
    			}
    		}
    	}
    	finished = String.join(" ", _sa);
    	finished = finished.replace((CharSequence)"@everyone",
    			(CharSequence)user.getAsMention());
    	finished = finished.replace((CharSequence)"@here",
    			(CharSequence)user.getAsMention());
        message.delete().queue();
        channel.sendMessage(finished).queue();
    } 
    public void yeetoCheetoBeansBurrito(MessageChannel channel, Message message) {
        int rand = random(100);
        channel.sendMessage("Giraffey, Luke, and Checken stinks " + rand + "%").queue();
    }
    public void modRole(String[] args, MessageChannel channel, Guild g, Member member, Message message) {
        if(!isMod(member, g)) {
        	channel.sendMessage(failureEmbed("You don't have permission to use this!")).queue(m -> {
        		m.delete().queueAfter(5, TimeUnit.SECONDS);
        	});
        	return;
        }
    	List<Role> roles = message.getMentionedRoles();
    	if(roles.isEmpty()) {
        	StringBuilder sb = new StringBuilder();
        	Role mod = getModRole(g);
        	
        	if(args.length == 1) {
            	if(!mod.isMentionable()) {
            		sb.append("I can not mention this role directly, but I can give you the name and color!\n");
            		float red, green, blue;
            		red = mod.getColor().getRed();
            		green = mod.getColor().getGreen();
            		blue = mod.getColor().getBlue();
            		sb.append("Color: (R:" + red + ", G:" + green + ", B:" + blue + ")\n");
            		sb.append("Name: " + mod.getName() + "\n");
            		channel.sendMessage(sb.toString()).queue();
            	} else {
            		sb.append("Here's the role:\n" + mod.getAsMention());
            		channel.sendMessage(sb.toString()).queue();
            	}
        	}
        	return;
    	} else {
    		Role newrole = roles.get(0);
    		Long roleid = newrole.getIdLong();
    		Long id = g.getIdLong();
    		
    		modRoles.put(id, roleid);
    		channel.sendMessage
    		("Okay, the new moderator role is "
    			+ newrole.getAsMention() + "!")
    			.queue();
    		return;
    	}
    }
    public void joke(MessageChannel channel) {
    	String joke = getJoke();
    	channel.sendMessage(joke).queue();
    }
    public void coinflip(MessageChannel channel) {
    	if(randomBool()) {
    		channel.sendMessage
	    		("You got **heads**!")
	    		.queue();
    		return;
    	} else {
    		channel.sendMessage
	    		("You got **tails**!")
	    		.queue();
    		return;
    	}
    }
    public void message(String[] args, MessageChannel channel, Guild g, Member member, Message message) {
        if(!isMod(member, g)) {
        	channel.sendMessage(failureEmbed("You don't have permission to use this!")).queue(m -> {
        		m.delete().queueAfter(5, TimeUnit.SECONDS);
        	});
        	return;
        }
        try {
			//System.out.print(args[1]+"\n");
        	args[1].toLowerCase().charAt(0);
        } catch(ArrayIndexOutOfBoundsException oob) {
    		channel.sendMessage("Usage:\n"
    				+ "message join <text>\n"
    				+ "message leave <text>\n"
    				+ "message channel #channel\n"
    				+ "message disable\n"
    				+ "message enable").queue();
    		return;
        }
    	if(args.length < 3 &&
    			!args[1].equalsIgnoreCase("enable") &&
    			!args[1].equalsIgnoreCase("disable")) {
    		channel.sendMessage("Usage:\n"
    				+ "message join <text>\n"
    				+ "message leave <text>\n"
    				+ "message channel #channel\n"
    				+ "message disable\n"
    				+ "message enable").queue();
    		return;
    	}
    	String query;
    	query = args[1];
    	if(query.equalsIgnoreCase("join")) {
    		
    		Long guildID;
    		guildID=g.getIdLong();
    		
    		StringBuilder sb = new StringBuilder();
    		int i = 0;
    		for(String word : args) {
    			i++;
    			if(i==1){continue;}
    			if(i==2){continue;}
    			sb.append(" " + word);
    		}
    		sb = sb.deleteCharAt(0);
    		String finished = sb.toString();
    		String mention = member.getUser().getAsMention();
    		String servername = g.getName();
    		
    		joinMessages.put(guildID, finished);
    		
    		finished = finished.replace("(user)", mention);
    		finished = finished.replace("(server)", servername);
    		channel.sendMessage(
    		"Set the join message to \"" +
    		finished + "\".").queue();
    		return;
    		
    	} else if(query.equalsIgnoreCase("leave")) {
    		
    		Long guildID;
    		guildID=g.getIdLong();
    		
    		StringBuilder sb = new StringBuilder();
    		int i = 0;
    		for(String word : args) {
    			i++;
    			if(i==1){continue;}
    			if(i==2){continue;}
    			sb.append(" " + word);
    		}
    		sb = sb.deleteCharAt(0);
    		String finished = sb.toString();
    		String mention = member.getUser().getName();
    		String servername = g.getName();
    		
    		leaveMessages.put(guildID, finished);
    		
    		finished = finished.replace("(user)", mention);
    		finished = finished.replace("(server)", servername);
    		channel.sendMessage(
    		"Set the leave message to \"" +
    		finished + "\".").queue();
    		return;
    		
    	} else if(query.equalsIgnoreCase("channel")) {
    		
    		List<TextChannel> channels;
    		TextChannel mentioned;
    		Long channelID;
    		Long guildID;
    		channels = message.getMentionedChannels();
    		if(channels.isEmpty()) {
    			channel.sendMessage
    			("You must specify a channel by mentioning it!"
    			+ " (Example: #example-channel)").queue();
    			return; }
    		mentioned=channels.get(0);
    		channelID=mentioned.getIdLong();
    		guildID=g.getIdLong();
    		joinLeaveChannels.put
    		(guildID,channelID);
    		
    		channel.sendMessage("*Set the new join/leave channel to "
    		+ mentioned.getAsMention() + ".*")
    		.queue();
    		return;
    		
    	} else if(query.equalsIgnoreCase("enable")) {
    		Long guildID;
    		Long cID;
    		guildID = g.getIdLong();
    		cID = g.getDefaultChannel().getIdLong();
    		joinLeaveChannels.put(guildID, cID);
    		channel.sendMessage
    		("Join/leave messages are now enabled. (Channel set to the server default)").queue();
    	} else if(query.equalsIgnoreCase("disable")) {
    		Long guildID;
    		guildID = g.getIdLong();
    		joinLeaveChannels.put(guildID, 0l);
    		channel.sendMessage
    		("Okay, join/leave messages are now disabled.").queue();
    	} else {
    		channel.sendMessage
    		("Query \"" + query + 
    		"\" doesn't exist!")
    		.queue();
    		return;
    	}
    }
    public void joinRole(String[] args, MessageChannel channel, Guild g, Member member, Message message) {
        if(!isMod(member, g)) {
        	channel.sendMessage(failureEmbed("You don't have permission to use this!")).queue(m -> {
        		m.delete().queueAfter(5, TimeUnit.SECONDS);
        	});
        	return;
        }
        String prefix=prefixes.get(g.getIdLong());
        if(args.length != 2) {
        	channel.sendMessage(
        	"Usage: " + prefix + "joinrole @<role>\nOR: joinrole none").queue();
        	return;
        }
        if(args[1].toLowerCase().startsWith("none")) {
        	joinRoles.put(
        		g.getIdLong(),0l);
        	channel.sendMessage("Okay, join roles are disabled now!").queue();
        	return;
        }
        List<Role> r = message.getMentionedRoles();
        if(r.isEmpty()) {
        	channel.sendMessage(
        	"Usage: " + prefix + "joinrole @<role>").queue();
        	return;
        }
        Role target = r.get(0);
        joinRoles.put(g.getIdLong(), 
        	target.getIdLong());
        
        channel.sendMessage("Alright, when someone joins the server they will now get the " + target.getAsMention() + " role.").queue();
        return;
    }
    /*public void Stats(String[] args, MessageChannel channel, Guild g, Member member, Message message, boolean ctcb) {
    	
    	if(args.length < 3) {
    		EmbedBuilder eb = new EmbedBuilder();
    		eb.setTitle("Available Game Stats");
    		eb.setColor(Color.magenta);
    		eb.addField("ðŸŒˆ Rainbow Six Siege", "stats rainbowsix <username>", true);
    		eb.addField("ðŸ‘· Fortnite", "stats fortnite <username>", true);
    		eb.addField("ðŸ�€ Rocket League", "stats rocketleague <username>", true);
    		eb.addField("ðŸ”« Overwatch", "stats overwatch <username>", true);
    		eb.addField("ðŸ’£ Battlefield 1", "stats battlefield <username>", true);
    		eb.addField("ðŸ—¡ CS:GO", "stats csgo <username>", true);
    		eb.addBlankField(false);
    		eb.addField("Don't see your favorite game?", "Suggest it by using the **suggest** command!", false);
    		channel.sendMessage(eb.build()).queue();
    		return;
    	}
    	String query = args[1];
    	String username = args[2];
    	
        // Rainbow Six Siege = 0
        // Fortnite = 1
        // Rocket League = 2
        // Overwatch = 3
        // Battlefield = 4
        // Csgo = 5
        
    	if(query.equalsIgnoreCase("rainbowsix")) {
    		MessageEmbed e = getGameStats(0, username, ctcb);
    		channel.sendMessage(e).queue();
    		return;
    	} else if(query.equalsIgnoreCase("fortnite")) {
    		MessageEmbed e = getGameStats(1, username, ctcb);
    		channel.sendMessage(e).queue();
    		return;
    	} else if(query.equalsIgnoreCase("rocketleague")) {
    		MessageEmbed e = getGameStats(2, username, ctcb);
    		channel.sendMessage(e).queue();
    		return;
    	} else if(query.equalsIgnoreCase("overwatch")) {
    		MessageEmbed e = getGameStats(3, username, ctcb);
    		channel.sendMessage(e).queue();
    		return;
    	} else if(query.equalsIgnoreCase("battlefield")) {
    		MessageEmbed e = getGameStats(4, username, ctcb);
    		channel.sendMessage(e).queue();
    		return;
    	} else if(query.equalsIgnoreCase("csgo")) {
    		MessageEmbed e = getGameStats(5, username, ctcb);
    		channel.sendMessage(e).queue();
    		return;
    	}
    }*/
    public void filter(String[] args, MessageChannel channel, Guild g, Member member, Message message) {
        if(!isMod(member, g)) {
        	channel.sendMessage(failureEmbed("You don't have permission to use this!")).queue(m -> {
        		m.delete().queueAfter(5, TimeUnit.SECONDS);
        	});
        	return;
        }
    	if(args.length < 2) {
    		channel.sendMessage("**Usage:**\n"
    				+ "filter add <text>\n"
    				+ "filter remove <text>\n"
    				+ "filter clear\n"
    				+ "filter list\n"
    				+ "filter swears").queue();
    		return;
    	}
    	verifyFilters(g);
    	Long gID = g.getIdLong();
    	
		List<String> filter;
		filter = filteredWords.get(gID);
    	
    	String query = args[1];
    	if(query.equalsIgnoreCase("clear")) {
    		filter.clear();
    		filteredWords.put(gID, filter);
    		
    		channel.sendMessage(successEmbed("Cleared all words from the filter!")).queue();
    		return;
    	}
    	if(query.equalsIgnoreCase("list")) {
    		if(filter.size() == 0) {
    			channel.sendMessage(failureEmbed("**This server doesn't have any filtered words!**")).queue();
    			return;
    		}
    		EmbedBuilder eb = new EmbedBuilder();
    		eb.setTitle("Filtered words on " + g.getName().toUpperCase() + ":");
    		StringBuilder sb = new StringBuilder();
    		for(String word : filter) {
    			sb.append(word + "\n");
    		}
    		String s = sb.toString();
    		User u = member.getUser();
    		u.openPrivateChannel().queue(_ch -> {
    			_ch.sendMessage(s).queue();
    		});
    		return;
    	}
    	if(query.equalsIgnoreCase("swears")) {
    		filter = new ArrayList<String>(defaultswears);
    		filteredWords.put(gID, filter);
    		channel.sendMessage(successEmbed("Set the filter to the basic swear words.")).queue();
    		return;
    	}
    	if(args.length < 3) {
    		channel.sendMessage("**Usage:**\n"
    				+ "filter add <text>\n"
    				+ "filter remove <text>\n"
    				+ "filter clear\n"
    				+ "filter list\n\n"
    				
    				+ "filter swears").queue();
    		return;
    	}
		StringBuilder sb = new StringBuilder();
		int i = 0;
		for(String word : args) {
			i++;
			if(i==1) {continue;}
			if(i==2) {continue;}
			sb.append(" " + word);
		}
		sb = sb.deleteCharAt(0);
		String s = sb.toString();
    	if(query.equalsIgnoreCase("add")) {
    		if(filter.contains(s)) {
    			filteredWords.put(gID, filter);
        		channel.sendMessage(failureEmbed("You've already added this word to the filter!")).queue();
        		return;
    		}
    		filter.add(s);
    		filteredWords.put(gID, filter);
    		channel.sendMessage(successEmbed("Okay, people can no longer say that word!")).queue();
    		return;
    	}
    	if(query.equalsIgnoreCase("remove")) {
    		if(!filter.remove(s)) {
    			channel.sendMessage(failureEmbed("I couldn't find the word you're talking about! Make sure that the **cAsE** is correct!")).queue();
    			return;
    		}
    		filteredWords.put(gID, filter);
    		channel.sendMessage(successEmbed("Okay, people can now say that word!")).queue();
    		return;
    	}
    }
    public void kick(String[] args, MessageChannel channel, Guild g, Member member, Message message) {
        if(!isMod(member, g)) {
        	channel.sendMessage(failureEmbed("You don't have permission to use this!")).queue(m -> {
        		m.delete().queueAfter(5, TimeUnit.SECONDS);
        	});
        	return;
        }
        List<Member> members;
        members = message.getMentionedMembers();
        if(members.isEmpty()) {
        	channel.sendMessage("Make sure to mention the person(s) you want to kick!").queue();
        	return;
        }
        try {
            for(Member mm : members) {
            	g.kick(mm).queue();
            }
        } catch(Exception e) {
        	channel.sendMessage("I cannot kick people that are higher/equal in role to me.").queue();
        	return;
        }
        channel.sendMessage("Kicked!").queue(m -> {
        	m.delete().queueAfter(5, TimeUnit.SECONDS);
        });
    }
    public void ban(String[] args, MessageChannel channel, Guild g, Member member, Message message) {
        if(!isMod(member, g)) {
        	channel.sendMessage(failureEmbed("You don't have permission to use this!")).queue(m -> {
        		m.delete().queueAfter(5, TimeUnit.SECONDS);
        	});
        	return;
        }
        List<Member> members;
        members = message.getMentionedMembers();
        if(members.isEmpty()) {
        	channel.sendMessage("Make sure to mention the person(s) you want to ban!").queue();
        	return;
        }
        try {
        	long myID = member.getUser().getIdLong();
            for(Member mm : members) {
            	long thisID = mm.getUser().getIdLong();
            	if(thisID == myID) {
                    channel.sendMessage(failureEmbed("(idiot stinky!) You can't ban yourself!")).queue(m -> {
                    	m.delete().queueAfter(5, TimeUnit.SECONDS);
                    });
            		return;
            	}
            	g.ban(mm, 7).queue();
            }
        } catch(Exception e) {
        	channel.sendMessage(failureEmbed("I cannot ban people that are higher/equal in role to me.")).queue(m -> {
        		m.delete().queueAfter(5, TimeUnit.SECONDS);
        	});
        	return;
        }
        channel.sendMessage(successEmbed("Banned!")).queue(m -> {
        	m.delete().queueAfter(5, TimeUnit.SECONDS);
        });
    }
    public void mute(String[] args, MessageChannel channel, Guild g, Member member, Message message) {
        if(!isMod(member, g)) {
        	channel.sendMessage(failureEmbed("You don't have permission to use this!")).queue(m -> {
        		m.delete().queueAfter(5, TimeUnit.SECONDS);
        	});
        	return;
        }
        List<Member> members;
        members = message.getMentionedMembers();
        if(members.isEmpty()) {
        	channel.sendMessage("Make sure to mention the person(s) you want to mute!").queue();
        	return;
        }
        for(Member mm : members) {
        	performMute(mm, member);
        }
        channel.sendMessage("Muted!").queue(m -> {
        	m.delete().queueAfter(5, TimeUnit.SECONDS);
        });
    }
    public void unmute(String[] args, MessageChannel channel, Guild g, Member member, Message message) {
        if(!isMod(member, g)) {
        	channel.sendMessage(failureEmbed("You don't have permission to use this!")).queue(m -> {
        		m.delete().queueAfter(5, TimeUnit.SECONDS);
        	});
        	return;
        }
        List<Member> members;
        members = message.getMentionedMembers();
        if(members.isEmpty()) {
        	channel.sendMessage("Make sure to mention the person(s) you want to unmute!").queue();
        	return;
        }
        for(Member mm : members) {
        	performUnmute(mm, member);
        }
        channel.sendMessage("Unmuted!").queue(m -> {
        	m.delete().queueAfter(5, TimeUnit.SECONDS);
        });
    }
    public void warn(String[] args, MessageChannel channel, Guild g, Member member, Message message) {
        if(!isMod(member, g)) {
        	channel.sendMessage(failureEmbed("You don't have permission to use this!")).queue(m -> {
        		m.delete().queueAfter(5, TimeUnit.SECONDS);
        	});
        	return;
        }
        boolean remove = args.length >= 4 && args[1].equalsIgnoreCase("remove");
        if(args.length < 3) {
        	String prefix=prefixes.get(g.getIdLong());
        	channel.sendMessage(failureEmbed("Usage: " + prefix + "warn @<user> <reason>\n"
        			+ "OR " + prefix + "warn remove @<user> <reason>")).queue();
        	return;
        }
        List<Member> members;
        members = message.getMentionedMembers();
        if(members.isEmpty()) {
        	channel.sendMessage("Make sure to mention the person you want to warn!").queue();
        	return;
        }
        StringBuilder sb = new StringBuilder();
        int i = 0;
        for(String word : args) {
        	i++;
        	if(i == 1) continue;
        	if(i == 2) continue;
        	if(i == 3 && remove)
        		continue;
        	sb.append(" " + word);
        }
        sb = sb.deleteCharAt(0);
        String reason = sb.toString();
        Member target = members.get(0);
        UserProfile pf;
        pf = getProfile(target);
        
        if(remove) {
        	int count = pf.removeWarns(reason);
        	setProfile(target, pf);
        	if(count == 0) {
                channel.sendMessage(failureEmbed("No warns with that reason. Are you sure you speeled it rite?"))
	            	.queue(mm->mm.delete().queueAfter
	            	(5, TimeUnit.SECONDS));
                return;
        	}
        	String text = "Removed " + count;
        	if(count == 1)
        		text += " warn.";
        	else
        		text += " warns.";
            channel.sendMessage(successEmbed(text))
            	.queue(mm->mm.delete().queueAfter
            	(5, TimeUnit.SECONDS));
            
            EmbedBuilder eb = baseLogEmbed("Warn(s) were removed. (" + count + ")", member.getUser(), Color.yellow);
            eb.setDescription("Warns removed from user " + getFriendCode(member.getUser()));
            LoggingEntry le = new LoggingEntry();
            le.targetUser = target.getUser().getIdLong();
            le.invokedUser = member.getUser().getIdLong();
            le.type = LogType.RemoveWarn;
            le.reason = "null";
            le.embed = eb.build();
            tryLog(g, le);
            return;
        } else {
            pf.addWarn(reason, member.getUser());
            setProfile(target, pf);
            channel.sendMessage(successEmbed("Warned!"))
            	.queue(mm->mm.delete().queueAfter
            	(5, TimeUnit.SECONDS));
            
            EmbedBuilder eb = baseLogEmbed("User was warned.", member.getUser(), Color.yellow);
            eb.setDescription("Person that warned: " + getFriendCode(member.getUser())
            	  + "\nPerson warned: " + getFriendCode(target.getUser()) +
            		"\nReason: \"" + reason + "\"");
            LoggingEntry le = new LoggingEntry();
            le.targetUser = target.getUser().getIdLong();
            le.invokedUser = member.getUser().getIdLong();
            le.type = LogType.RemoveWarn;
            le.reason = "null";
            le.embed = eb.build();
            tryLog(g, le);
            return;
        }
    }
    public void warns(String[] args, MessageChannel channel, Guild g, Member member, Message message) {
        if(!isMod(member, g)) {
        	channel.sendMessage(failureEmbed("You don't have permission to use this!")).queue(m -> {
        		m.delete().queueAfter(5, TimeUnit.SECONDS);
        	});
        	return;
        }
        if(args.length != 2) {
        	String prefix=prefixes.get(g.getIdLong());
        	channel.sendMessage("Usage: " + prefix + "warns @<user>").queue();
        	return;
        }
        List<Member> members;
        members = message.getMentionedMembers();
        if(members.isEmpty()) {
        	channel.sendMessage("Make sure to mention the person you want to see the warns for!").queue();
        	return;
        }
        Member target;
        UserProfile up;
        target = members.get(0);
        up = getProfile(target);
        
        List<Warn> warns = up.warns;
        if(warns.size() <= 0) {
        	channel.sendMessage("This user doesn't have any warns.").queue();
        	return;
        }
        EmbedBuilder eb = new EmbedBuilder();
        eb.setColor(Color.magenta);
        String en, eaurl;
        en = target.getEffectiveName();
        eaurl = target.getUser().getEffectiveAvatarUrl();
        eb.setAuthor("Warns for user " + en + "", null, eaurl);
        int i = 0;
        for(Warn warn : warns) {
        	i++;
        	LocalDate date = warn.date;
        	String reason = warn.reason;
        	String warnedby = warn.warner_name;
        	
        	String title = "Warn #" + i;
        	title += " | " + date.toString();
        	
        	String desc = "Reason: " + reason;
        	desc += "\nWarned By: " + warnedby;
        	eb.addField(title, desc, false);
        }
        channel.sendMessage(eb.build()).queue();
        return;
    }
    public void clearWarns(String[] args, MessageChannel channel, Guild g, Member member, Message message) {
        if(!isMod(member, g)) {
        	channel.sendMessage(failureEmbed("You don't have permission to use this!")).queue(m -> {
        		m.delete().queueAfter(5, TimeUnit.SECONDS);
        	});
        	return;
        }
        if(args.length != 2) {
        	String prefix=prefixes.get(g.getIdLong());
        	channel.sendMessage("Usage: " + prefix + "clearwarns @<user>").queue();
        	return;
        }
        List<Member> members;
        members = message.getMentionedMembers();
        if(members.isEmpty()) {
        	channel.sendMessage("Make sure to mention the person you want to clear the warns from!").queue();
        	return;
        }
        Member target;
        UserProfile up;
        target = members.get(0);
        up = getProfile(target);
        up.clearWarns();
        setProfile(target, up);
        channel.sendMessage("This person's warns have been cleared!").queue(mm->mm.delete().queueAfter(5, TimeUnit.SECONDS));
        
        User u = member.getUser();
        User tu = target.getUser();
    	LoggingEntry le = new LoggingEntry();
    	le.targetUser = tu.getIdLong();
    	le.invokedUser = u.getIdLong();
    	le.reason = "Invalid warn.";
    	le.ext = null;
    	le.type = LogType.ClearWarns;
    	
    	EmbedBuilder eb = baseLogEmbed("User's warns have been cleared.", null, Color.orange);
    	eb.setDescription("Invoker's Name: " + getFriendCode(u) +
    			"\nTarget's Name: " + getFriendCode(tu));
    	le.embed = eb.build();
    	
    	tryLog(g, le);
        
        return;
    }
    public void backup(String[] args, MessageChannel channel, Guild g, Member member, Message message) {
    	if(!member.isOwner()) {
    		channel.sendMessage("Only the owner can create and restore backups.").queue();
    		return;
    	}
    	channel.sendMessage("Creating backup...").queue();
    	ServerBackup backup = new ServerBackup(g);
    	backups.put(g.getIdLong(), backup);
    	channel.sendMessage("Finished. If a raid were to ever happen, you can now run the restore command!\n"
    			+ "Backed up: `Channel names, colors, bitrate, and userlimits. Role colors, names, and permissions. Server name, location, filter level, and authentication level.`").queue();
    	return;
    }
    public void restore(String[] args, MessageChannel channel, Guild g, Member member, Message message) {
    	if(!member.isOwner()) {
    		channel.sendMessage("Only the owner can create and restore backups.").queue();
    		return;
    	}
    	Long gID;
    	gID = g.getIdLong();
    	if(!backups.containsKey(gID)) {
    		channel.sendMessage("Unfortunately there is no backup saved under your server.").queue();
    		return;
    	}
    	
    	ServerBackup backup = backups.get(gID);
    	channel.sendMessage("Preparing to restore...").queue();
    	channel.sendMessage("Restoring the server... This may take a few minutes depending on the size of your server.").queue();
    	backup.Restore(g);
    	channel.sendMessage("Server restored.").queue();
    	return;
    }
    public void stats(String[] args, MessageChannel channel, Guild g, Member member, Message message) {
		String prefix = prefixes.get(g.getIdLong());
    	if(args.length < 2) {
    		channel.sendMessage(failureEmbed("Incorrect use of command! Try \"" + prefix + "stats help\".")).queue();
    		return;
    	}
		EmbedBuilder eb = new EmbedBuilder();
		eb.setColor(Color.ORANGE);
		String name = member.getEffectiveName();
		String avatar = member.getUser().getEffectiveAvatarUrl();
		eb.setAuthor(name, null, avatar);
		
		if(args.length < 3) {
			eb.addField("Stats Help Menu", "Available ", false);
			for(StatGame sg : games) {
				if(sg.usesSteamURL)
					eb.addField(sg.displayName, prefix + "stats " + sg.keyword + " <steam name>\n"
							+ "(Steam name as in your unique vanity URL.)", false);
				else
					eb.addField(sg.displayName, prefix + "stats " + sg.keyword + " <username>", false);
			}
		}
		if(args.length >= 3) {
			String keyword = args[1];
			String username = args[2];
			String finalUsername = username;
			StatGame game = null;
			for(StatGame sg : games) {
				if(keyword.equalsIgnoreCase(sg.keyword)) {
					game = sg;
				}
			}
			if(game == null) {
				channel.sendMessage(failureEmbed("This game hasn't been added yet or doesn't exist! Try \"" + prefix + "stats help\".")).queue();
				return;
			}
			if(game.usesSteamURL) {
				// Needs steam ID, not vanity URL, so fetch the id.
				channel.sendMessage("Resolving Steam ID...").queue(msg -> {
					msg.delete().queueAfter(5, TimeUnit.SECONDS);
				});
				long id = steam.resolveVanityURL(username);
				username = String.valueOf(id);
			}
			
			channel.sendMessage("Fetching stats, please wait! (This can take a moment.)").queue(msg -> {
				msg.delete().queueAfter(5, TimeUnit.SECONDS);
			});
			eb.setTitle(finalUsername + "'s Stats on " + game.displayName);
			Stat[] results = game.getPlayerStats(username);
			if(results == null) {
				channel.sendMessage(failureEmbed("Player not found.")).queue();
				return;
			}
			for(Stat stat : results) {
				eb.addField(stat.name, stat.value, true);
			}
		}
		channel.sendMessage(eb.build()).queue();
		return;
    }
    public void upgrades(String[] args, MessageChannel channel, Guild g, Member member, Message message) {
    	EmbedBuilder eb = new EmbedBuilder();
    	eb.setColor(Color.magenta);
    	eb.setTitle("SERVER UPGRADES");
    	eb.setDescription("Charge up your server with extra BonziBot perks using the UPGRADE command! Costs " + UPGRADE_COST + " coins per, so get your users/friends to donate!");
    	int upgrades = getUpgrades(g);
    	eb.addField("		" + upgrades + " UPGRADES",
    			PRIVATE_TAG_COST + " Upgrades - Private Tags\n"
    			+ SCRIPT_EDITOR_COST + " Upgrades - Script Editor Access\n"
    			+ ALL_PREMIUM_COST + " Upgrades - All Members Have Premium\n"
    			+ "1000 Upgrades - Server Shows on the Prestige Command! (Prestige 1)\n"
    			+ "2000 Upgrades - Server Shows on the Prestige Command! (Prestige 2)\n"
    			+ "etc...", false);
    	channel.sendMessage(eb.build()).queue();
    }
    public void upgrade(String[] args, MessageChannel channel, Guild g, Member member, Message message) {
    	User u = member.getUser();
    	int coins = getCoins(u);
    	message.delete().queue();
    	int amount = 1;
    	if(args.length >= 2) {
    		String arg = args[1];
    		try {
    			amount = Integer.parseInt(arg);
    		} catch(Exception exc) {}
    	}
    	
    	if(amount < 1) {
    		channel.sendMessage(failureEmbed("You can't put less than 1 upgrade.")).queue(mm -> {
    			mm.delete().queueAfter(5, TimeUnit.SECONDS);
    		});
    		return;
    	}
    	
    	if(coins < (UPGRADE_COST*amount)) {
    		channel.sendMessage(failureEmbed("You don't have enough coins to upgrade the server! (" + (UPGRADE_COST*amount) + " coins)")).queue(mm -> {
    			mm.delete().queueAfter(5, TimeUnit.SECONDS);
    		});
    		return;
    	}
    	
    	for(int i = 0; i < amount; i++) {
    		upgradeGuild(u, g);
    	}
    	
    	EmbedBuilder eb = new EmbedBuilder();
    	eb.setColor(Color.magenta);
    	eb.setAuthor(u.getName(), null, u.getEffectiveAvatarUrl());
    	if(amount > 1)
    		eb.setTitle(getFriendCode(u) + " has upgraded the server " + amount + " times!");
    	else
    		eb.setTitle(getFriendCode(u) + " has upgraded the server!");
    	int ups = getUpgrades(g);
    	if(ups <= 1) {
    		eb.setDescription("The server now has 1 upgrade!");
    	} else
    		eb.setDescription("The server now has " + ups + " upgrades!");
    	
    	String nm = g.getName();
    	if(ups == PRIVATE_TAG_COST)
    		eb.addField("NEW REWARD!", nm + " now has access to private tags! Use the privatetag command!", false);
    	else if(ups == SCRIPT_EDITOR_COST)
    		eb.addField("NEW REWARD!", nm + " now has access to the script editor! Mods and Owners, check the scripteditor command and start making your own commands!", false);
    	else if(ups == ALL_PREMIUM_COST)
    		eb.addField("NEW REWARD!", "All users can now use all of the premium commands! Check the purchases command!\n(Note the free premium commands are only in this server!)", false);
    	else if(ups % 1000 == 0 && ups != 0) {
    		int prestige = ups/1000;
    		eb.addField("PRESTIGE!", nm + " is now prestige " + prestige + "! Your server will now show up on the prestiges command as a level " + prestige + "!", false);
    	}
    	
    	channel.sendMessage(eb.build()).queue();
    	return;
    }
    public void prestige(String[] args, MessageChannel channel, Guild g, Member member, Message message) {
        List<Map.Entry<Long, Integer> > list = 
                new LinkedList<Map.Entry<Long, Integer>>();
        list.addAll(upgrades.entrySet());
   
         Collections.sort(list, new Comparator<Map.Entry<Long, Integer> >() { 
             public int compare(Map.Entry<Long, Integer> o1, Map.Entry<Long, Integer> o2) 
             {
            	 return (o1.getValue()).compareTo(o2.getValue()); 
             }
         });
         
         JDA jda = member.getJDA();
         EmbedBuilder eb = new EmbedBuilder();
         eb.setColor(Color.yellow);
         eb.setTitle("🎖️ Top Prestiged Servers 🎖");
         eb.setDescription("Prestiges are given every 1000 upgrades.");
         
         for(int i = list.size()-1; i > 0; i--) {
        	 Map.Entry<Long, Integer> entry = list.get(i);
        	 String gname = jda.getGuildById(entry.getKey()).getName();
        	 if(entry.getValue() < 1000)
        		 break;
        	 int prest = (int)Math.floor(((double)entry.getValue()/1000.0));
        	 if(prest < 1)
        		 break;
        	 if(prest == 1)
        		 eb.addField(gname, "With [1] Prestige!", false);
        	 else
        		 eb.addField(gname, "With [" + prest + "]" + " Prestiges!", false);
         }
         if(eb.getFields().isEmpty())
        	 eb.addField("Nobody is here yet!", "Use the upgrade command and make a community effort to make your server the first!", false);
         
         channel.sendMessage(eb.build()).queue();
         return;
    }
    // ---------------------------------------------------------------------------------------]
    
    /*
     * Reads the contents of a file. Returns an empty string if an exception occurs. 
     */
    public String read(File f) {
    	if(!f.exists()) {
    		return "";
    	}
    	StringBuilder sb = new StringBuilder();
    	BufferedReader br;
    	try {
			br = new BufferedReader(new FileReader(f));
			String s;
			while((s = br.readLine()) != null) {
				sb.append(s + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
    	return sb.toString();
    }
    public void save(String suffix) {
    	try {
    		FileOutputStream fileout = new FileOutputStream(suffix + "prefixes.ser");
    		ObjectOutputStream objectout = new ObjectOutputStream(fileout);
    		objectout.writeObject(prefixes);
    		objectout.close();
    		fileout.close();
    	} catch(IOException exc) {
    		exc.printStackTrace();
    		return;
    	}
    	try {
    		FileOutputStream fileout = new FileOutputStream(suffix + "modRoles.ser");
    		ObjectOutputStream objectout = new ObjectOutputStream(fileout);
    		objectout.writeObject(modRoles);
    		objectout.close();
    		fileout.close();
    	} catch(IOException exc) {
    		exc.printStackTrace();
    		return;
    	}
    	try {
    		FileOutputStream fileout = new FileOutputStream(suffix + "joinMessages.ser");
    		ObjectOutputStream objectout = new ObjectOutputStream(fileout);
    		objectout.writeObject(joinMessages);
    		objectout.close();
    		fileout.close();
    	} catch(IOException exc) {
    		exc.printStackTrace();
    		return;
    	}
    	try {
    		FileOutputStream fileout = new FileOutputStream(suffix + "leaveMessages.ser");
    		ObjectOutputStream objectout = new ObjectOutputStream(fileout);
    		objectout.writeObject(leaveMessages);
    		objectout.close();
    		fileout.close();
    	} catch(IOException exc) {
    		exc.printStackTrace();
    		return;
    	}
    	try {
    		FileOutputStream fileout = new FileOutputStream(suffix + "joinLeaveChannels.ser");
    		ObjectOutputStream objectout = new ObjectOutputStream(fileout);
    		objectout.writeObject(joinLeaveChannels);
    		objectout.close();
    		fileout.close();
    	} catch(IOException exc) {
    		exc.printStackTrace();
    		return;
    	}
    	try {
    		FileOutputStream fileout = new FileOutputStream(suffix + "joinRoles.ser");
    		ObjectOutputStream objectout = new ObjectOutputStream(fileout);
    		objectout.writeObject(joinRoles);
    		objectout.close();
    		fileout.close();
    	} catch(IOException exc) {
    		exc.printStackTrace();
    		return;
    	}
    	try {
    		FileOutputStream fileout = new FileOutputStream(suffix + "reactionRoles.ser");
    		ObjectOutputStream objectout = new ObjectOutputStream(fileout);
    		objectout.writeObject(reactionRoles);
    		objectout.close();
    		fileout.close();
    	} catch(IOException exc) {
    		exc.printStackTrace();
    		return;
    	}
    	try {
    		FileOutputStream fileout = new FileOutputStream(suffix + "filters.ser");
    		ObjectOutputStream objectout = new ObjectOutputStream(fileout);
    		objectout.writeObject(filteredWords);
    		objectout.close();
    		fileout.close();
    	} catch(IOException exc) {
    		exc.printStackTrace();
    		return;
    	}
    	try {
    		FileOutputStream fileout = new FileOutputStream(suffix + "profiles.ser");
    		ObjectOutputStream objectout = new ObjectOutputStream(fileout);
    		objectout.writeObject(profiles);
    		objectout.close();
    		fileout.close();
    	} catch(IOException exc) {
    		exc.printStackTrace();
    		return;
    	}
    	try {
    		FileOutputStream fileout = new FileOutputStream(suffix + "backups.ser");
    		ObjectOutputStream objectout = new ObjectOutputStream(fileout);
    		objectout.writeObject(backups);
    		objectout.close();
    		fileout.close();
    	} catch(IOException exc) {
    		exc.printStackTrace();
    		return;
    	}
    	try {
    		FileOutputStream fileout = new FileOutputStream(suffix + "cc.ser");
    		ObjectOutputStream objectout = new ObjectOutputStream(fileout);
    		objectout.writeObject(ccm);
    		objectout.close();
    		fileout.close();
    	} catch(IOException exc) {
    		exc.printStackTrace();
    		return;
    	}
    	try {
    		FileOutputStream fileout = new FileOutputStream(suffix + "ccbans.ser");
    		ObjectOutputStream objectout = new ObjectOutputStream(fileout);
    		objectout.writeObject(ccBans);
    		objectout.close();
    		fileout.close();
    	} catch(IOException exc) {
    		exc.printStackTrace();
    		return;
    	}
    	try {
    		FileOutputStream fileout = new FileOutputStream(suffix + "xp.ser");
    		ObjectOutputStream objectout = new ObjectOutputStream(fileout);
    		objectout.writeObject(xp);
    		objectout.close();
    		fileout.close();
    	} catch(IOException exc) {
    		exc.printStackTrace();
    		return;
    	}
    	try {
    		FileOutputStream fileout = new FileOutputStream(suffix + "countingGames.ser");
    		ObjectOutputStream objectout = new ObjectOutputStream(fileout);
    		objectout.writeObject(countingGames);
    		objectout.close();
    		fileout.close();
    	} catch(IOException exc) {
    		exc.printStackTrace();
    		return;
    	}
    	try {
    		FileOutputStream fileout = new FileOutputStream(suffix + "coins.ser");
    		ObjectOutputStream objectout = new ObjectOutputStream(fileout);
    		objectout.writeObject(coins);
    		objectout.close();
    		fileout.close();
    	} catch(IOException exc) {
    		exc.printStackTrace();
    		return;
    	}
    	try {
    		FileOutputStream fileout = new FileOutputStream(suffix + "lottery.ser");
    		ObjectOutputStream objectout = new ObjectOutputStream(fileout);
    		objectout.writeObject(lottery);
    		objectout.close();
    		fileout.close();
    	} catch(IOException exc) {
    		exc.printStackTrace();
    		return;
    	}
    	try {
    		FileOutputStream fileout = new FileOutputStream(suffix + "accounts.ser");
    		ObjectOutputStream objectout = new ObjectOutputStream(fileout);
    		objectout.writeObject(premiumAccounts);
    		objectout.close();
    		fileout.close();
    	} catch(IOException exc) {
    		exc.printStackTrace();
    	}
    	try {
    		FileOutputStream fileout = new FileOutputStream(suffix + "playlists.ser");
    		ObjectOutputStream objectout = new ObjectOutputStream(fileout);
    		objectout.writeObject(playlists);
    		objectout.close();
    		fileout.close();
    	} catch(IOException exc) {
    		exc.printStackTrace();
    		return;
    	}
    	try {
    		List<SerializableLoggingEntry> savable = new ArrayList<SerializableLoggingEntry>();
    		for(LoggingEntry le : logs) { savable.add(new SerializableLoggingEntry(le)); }
    		
    		FileOutputStream fileout = new FileOutputStream(suffix + "logging.ser");
    		ObjectOutputStream objectout = new ObjectOutputStream(fileout);
    		objectout.writeObject(savable);
    		objectout.close();
    		fileout.close();
    	} catch(IOException exc) {
    		exc.printStackTrace();
    		return;
    	}
    	try {
    		FileOutputStream fileout = new FileOutputStream(suffix + "upgrades.ser");
    		ObjectOutputStream objectout = new ObjectOutputStream(fileout);
    		objectout.writeObject(upgrades);
    		objectout.close();
    		fileout.close();
    	} catch(IOException exc) {
    		exc.printStackTrace();
    		return;
    	}
    	try {
    		FileOutputStream fileout = new FileOutputStream(suffix + "upgraders.ser");
    		ObjectOutputStream objectout = new ObjectOutputStream(fileout);
    		objectout.writeObject(upgraders);
    		objectout.close();
    		fileout.close();
    	} catch(IOException exc) {
    		exc.printStackTrace();
    		return;
    	}
    	try {
    		FileOutputStream fileout = new FileOutputStream(suffix + "scriptcreators.ser");
    		ObjectOutputStream objectout = new ObjectOutputStream(fileout);
    		objectout.writeObject(scriptCreators);
    		objectout.close();
    		fileout.close();
    	} catch(IOException exc) {
    		exc.printStackTrace();
    		return;
    	}
    	try {
    		FileOutputStream fileout = new FileOutputStream(suffix + "acceptednsfw.ser");
    		ObjectOutputStream objectout = new ObjectOutputStream(fileout);
    		objectout.writeObject(acceptedNSFW);
    		objectout.close();
    		fileout.close();
    	} catch(IOException exc) {
    		exc.printStackTrace();
    		return;
    	}
    }
    @SuppressWarnings("unchecked")
	public void load(String suffix) {
    	try {
    		FileInputStream filein = new FileInputStream(suffix + "prefixes.ser");
    		ObjectInputStream objectin = new ObjectInputStream(filein);
    		prefixes = (HashMap<Long, String>) objectin.readObject();
    		objectin.close();
    		filein.close();
    	} catch(IOException exc) {
    		exc.printStackTrace();
    		return;
    	} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}
    	try {
    		FileInputStream filein = new FileInputStream(suffix + "modRoles.ser");
    		ObjectInputStream objectin = new ObjectInputStream(filein);
    		modRoles = (HashMap<Long, Long>) objectin.readObject();
    		objectin.close();
    		filein.close();
    	} catch(IOException exc) {
    		exc.printStackTrace();
    		return;
    	} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}
    	try {
    		FileInputStream filein = new FileInputStream(suffix + "joinMessages.ser");
    		ObjectInputStream objectin = new ObjectInputStream(filein);
    		joinMessages = (HashMap<Long, String>) objectin.readObject();
    		objectin.close();
    		filein.close();
    	} catch(IOException exc) {
    		exc.printStackTrace();
    		return;
    	} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}
    	try {
    		FileInputStream filein = new FileInputStream(suffix + "leaveMessages.ser");
    		ObjectInputStream objectin = new ObjectInputStream(filein);
    		leaveMessages = (HashMap<Long, String>) objectin.readObject();
    		objectin.close();
    		filein.close();
    	} catch(IOException exc) {
    		exc.printStackTrace();
    		return;
    	} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}
    	try {
    		FileInputStream filein = new FileInputStream(suffix + "joinLeaveChannels.ser");
    		ObjectInputStream objectin = new ObjectInputStream(filein);
    		joinLeaveChannels = (HashMap<Long, Long>) objectin.readObject();
    		objectin.close();
    		filein.close();
    	} catch(IOException exc) {
    		exc.printStackTrace();
    		return;
    	} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}
    	try {
    		FileInputStream filein = new FileInputStream(suffix + "joinRoles.ser");
    		ObjectInputStream objectin = new ObjectInputStream(filein);
    		joinRoles = (HashMap<Long, Long>) objectin.readObject();
    		objectin.close();
    		filein.close();
    	} catch(IOException exc) {
    		exc.printStackTrace();
    		return;
    	} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}
    	try {
    		FileInputStream filein = new FileInputStream(suffix + "reactionRoles.ser");
    		ObjectInputStream objectin = new ObjectInputStream(filein);
    		reactionRoles = (HashMap<Long, List<ReactionRole>>) objectin.readObject();
    		objectin.close();
    		filein.close();
    	} catch(IOException exc) {
    		exc.printStackTrace();
    		return;
    	} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}
    	try {
    		FileInputStream filein = new FileInputStream(suffix + "filters.ser");
    		ObjectInputStream objectin = new ObjectInputStream(filein);
    		filteredWords = (HashMap<Long, List<String>>) objectin.readObject();
    		objectin.close();
    		filein.close();
    	} catch(IOException exc) {
    		exc.printStackTrace();
    		return;
    	} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}
    	try {
    		FileInputStream filein = new FileInputStream(suffix + "profiles.ser");
    		ObjectInputStream objectin = new ObjectInputStream(filein);
    		profiles = (HashMap<Long, HashMap<Long, UserProfile>>) objectin.readObject();
    		objectin.close();
    		filein.close();
    	} catch(IOException exc) {
    		exc.printStackTrace();
    		return;
    	} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}
    	try {
    		FileInputStream filein = new FileInputStream(suffix + "backups.ser");
    		ObjectInputStream objectin = new ObjectInputStream(filein);
    		backups = (HashMap<Long, ServerBackup>) objectin.readObject();
    		objectin.close();
    		filein.close();
    	} catch(IOException exc) {
    		exc.printStackTrace();
    		return;
    	} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}
    	try {
    		FileInputStream filein = new FileInputStream(suffix + "cc.ser");
    		ObjectInputStream objectin = new ObjectInputStream(filein);
    		ccm = (CustomCommandManager) objectin.readObject();
    		objectin.close();
    		filein.close();
    	} catch(IOException exc) {
    		exc.printStackTrace();
    		return;
    	} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}
    	try {
    		FileInputStream filein = new FileInputStream(suffix + "ccbans.ser");
    		ObjectInputStream objectin = new ObjectInputStream(filein);
    		ccBans = (List<Long>) objectin.readObject();
    		objectin.close();
    		filein.close();
    	} catch(IOException exc) {
    		exc.printStackTrace();
    		return;
    	} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}
    	try {
    		FileInputStream filein = new FileInputStream(suffix + "xp.ser");
    		ObjectInputStream objectin = new ObjectInputStream(filein);
    		xp = (HashMap<Long, Integer>) objectin.readObject();
    		objectin.close();
    		filein.close();
    	} catch(IOException exc) {
    		exc.printStackTrace();
    		return;
    	} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}
    	try {
    		FileInputStream filein = new FileInputStream(suffix + "countingGames.ser");
    		ObjectInputStream objectin = new ObjectInputStream(filein);
    		countingGames = (HashMap<Long, Integer>) objectin.readObject();
    		objectin.close();
    		filein.close();
    	} catch(IOException exc) {
    		exc.printStackTrace();
    		return;
    	} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}
    	try {
    		FileInputStream filein = new FileInputStream(suffix + "coins.ser");
    		ObjectInputStream objectin = new ObjectInputStream(filein);
    		coins = (HashMap<Long, Integer>) objectin.readObject();
    		objectin.close();
    		filein.close();
    	} catch(IOException exc) {
    		exc.printStackTrace();
    		return;
    	} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}
    	try {
    		FileInputStream filein = new FileInputStream(suffix + "lottery.ser");
    		ObjectInputStream objectin = new ObjectInputStream(filein);
    		lottery = (int) objectin.readObject();
    		objectin.close();
    		filein.close();
    	} catch(IOException exc) {
    		exc.printStackTrace();
    		return;
    	} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}
    	try {
    		FileInputStream filein = new FileInputStream(suffix + "accounts.ser");
    		ObjectInputStream objectin = new ObjectInputStream(filein);
    		premiumAccounts = (HashMap<Long, List<PremiumCommand>>) objectin.readObject();
    		objectin.close();
    		filein.close();
    	} catch(IOException exc) {
    		exc.printStackTrace();
    	} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}
    	try {
    		FileInputStream filein = new FileInputStream(suffix + "playlists.ser");
    		ObjectInputStream objectin = new ObjectInputStream(filein);
    		playlists = (List<Playlist>) objectin.readObject();
    		objectin.close();
    		filein.close();
    	} catch(IOException exc) {
    		exc.printStackTrace();
    		return;
    	} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}
    	try {
    		FileInputStream filein = new FileInputStream(suffix + "logging.ser");
    		ObjectInputStream objectin = new ObjectInputStream(filein);
    		List<SerializableLoggingEntry> sle = (List<SerializableLoggingEntry>) objectin.readObject();
    		List<LoggingEntry> le = new ArrayList<LoggingEntry>();
    		for(SerializableLoggingEntry entry : sle) { le.add(new LoggingEntry(entry)); }
    		logs.clear(); logs = le;
    		
    		objectin.close();
    		filein.close();
    	} catch(IOException exc) {
    		exc.printStackTrace();
    		return;
    	} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}
    	try {
    		FileInputStream filein = new FileInputStream(suffix + "upgrades.ser");
    		ObjectInputStream objectin = new ObjectInputStream(filein);
    		upgrades = (HashMap<Long, Integer>) objectin.readObject();
    		objectin.close();
    		filein.close();
    	} catch(IOException exc) {
    		exc.printStackTrace();
    		return;
    	} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}
    	try {
    		FileInputStream filein = new FileInputStream(suffix + "upgraders.ser");
    		ObjectInputStream objectin = new ObjectInputStream(filein);
    		upgraders = (HashMap<Long, List<Long>>) objectin.readObject();
    		objectin.close();
    		filein.close();
    	} catch(IOException exc) {
    		exc.printStackTrace();
    		return;
    	} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}
    	try {
    		FileInputStream filein = new FileInputStream(suffix + "scriptcreators.ser");
    		ObjectInputStream objectin = new ObjectInputStream(filein);
    		scriptCreators = (HashMap<Long, ScriptCreator>) objectin.readObject();
    		objectin.close();
    		filein.close();
    	} catch(IOException exc) {
    		exc.printStackTrace();
    		return;
    	} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}
    	try {
    		FileInputStream filein = new FileInputStream(suffix + "acceptednsfw.ser");
    		ObjectInputStream objectin = new ObjectInputStream(filein);
    		acceptedNSFW = (List<Long>) objectin.readObject();
    		objectin.close();
    		filein.close();
    	} catch(IOException exc) {
    		exc.printStackTrace();
    		return;
    	} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}
    }
    public void verifyGuild(Guild g) {
    	verifyModRole(g);
    	verifyJoinLeaveMessages(g);
    	verifyJLChannel(g);
    	verifyJoinRole(g);
    	verifyReactionRoles(g);
    	verifyFilters(g);
    }
    public void verifyModRole(Guild g) {
		Long guildID;
		guildID = g.getIdLong();
		if(modRoles.containsKey(guildID)) {
			if(g.getRoleById(modRoles
					.get(guildID))
					!= null) {
				return;
			}
		}
		List<Role> potentials;
		potentials = g.getRolesByName("mod", true);
		if(!potentials.isEmpty()) {
			Role r = potentials.get(0);
			modRoles.put(guildID, r.getIdLong());
			return;
		}
		potentials = g.getRolesByName("moderator", true);
		if(!potentials.isEmpty()) {
			Role r = potentials.get(0);
			modRoles.put(guildID, r.getIdLong());
			return;
		}
		
		try {
			g.createRole()
    		.setColor(Color.magenta)
    		.setName("Moderator")
    		.setMentionable(true)
    		.queue(created -> {
    			modRoles.put(guildID, created.getIdLong());
	    	});
		} catch(InsufficientPermissionException ipe) {
			return;
		}
    }
    public void verifyJoinLeaveMessages(Guild g) {
    	Long id;
    	id = g.getIdLong();
    	
    	if(!joinMessages.containsKey(id)) {
    		joinMessages.put(id, DEFAULT_JOIN);
    	}
    	if(!leaveMessages.containsKey(id)) {
    		leaveMessages.put(id, DEFAULT_LEAVE);
    	}
    }
    public void verifyJLChannel(Guild g) {
    	if(!joinLeaveChannels.containsKey(g.getIdLong())) {
    		joinLeaveChannels.put(g.getIdLong(), 
    			g.getDefaultChannel()
    			.getIdLong());
    		return;
    	}
    }
    public void verifyJoinRole(Guild g) {
    	if(!joinRoles.containsKey(g.getIdLong())) {
    		joinRoles.put(g.getIdLong(), 0l);
    	}
    }
    public void verifyReactionRoles(Guild g) {
    	if(!reactionRoles.containsKey(g.getIdLong())) {
    		reactionRoles.put(g.getIdLong(),
    		new ArrayList<ReactionRole>());
    	}
    }
    public void verifyFilters(Guild g) {
    	if(!filteredWords.containsKey(g.getIdLong())) {
    		filteredWords.put(g.getIdLong(), new ArrayList<String>());
    		return;
    	}
    }
    public void verifyMuted(Guild g) {
    	if(g.getRolesByName("Muted", true).isEmpty()) {
    		createMutedRole(g);
    	}
    }
    // verifyProfile is seperate because it deals with a single member.
    public void verifyProfile(Member m) {
    	Guild g = m.getGuild();
    	Long gID, mID;
    	gID = g.getIdLong();
    	mID = m.getUser().getIdLong();
    	if(!profiles.containsKey(gID)) {
    		profiles.put(gID, new HashMap<Long, UserProfile>());
    	}
    	HashMap<Long, UserProfile> up;
    	up = profiles.get(g.getIdLong());
    	
    	if(!up.containsKey(mID)) {
    		up.put(mID, new UserProfile());
    		return;
    	}
    }
    // ---------------------------------------------------------------------------------------]
    // ---------------------------------------------------------------------------------------]
    public static final char CENSOR_CHAR = '#';
    public String censor(String word) {
    	int len = word.length();
    	if(len <= 2) {
    		if(len == 2) return String.valueOf(CENSOR_CHAR) + CENSOR_CHAR;
    		if(len == 1) return String.valueOf(CENSOR_CHAR);
    		if(len <= 0) return "";
    	}
    	//String trimmed = word.substring(1, word.length()-1);
    	StringBuilder sb = new StringBuilder();
    	char[] arr = word.toCharArray();
    	sb.append(arr[0]);
    	for(int i = 0; i < arr.length-2; i++) {
    		sb.append(CENSOR_CHAR);
    	}
    	sb.append(arr[arr.length-1]);
    	return sb.toString();
    }
    public String getJoke() {
    	int rand;
    	rand = random(jokes.size());
    	return jokes.get(rand);
    }
    public static Color colorFromRGB(int r, int g, int b) {
    	Color c;
    	float[] colors = new float[3];
    	colors = Color.RGBtoHSB(r, g, b, null);
    	float Hh, Hs, Hb;
    	Hh = colors[0];
    	Hs = colors[1];
    	Hb = colors[2];
    	c = Color.getHSBColor(Hh, Hs, Hb);
    	return c;
    }
    public static Color colorFromName(String name) {
    	name = name.trim();
    	if(name.equalsIgnoreCase("red"))
    		return Color.red;
    	else if(name.equalsIgnoreCase("orange"))
    		return Color.orange;
    	else if(name.equalsIgnoreCase("yellow"))
    		return Color.yellow;
    	else if(name.equalsIgnoreCase("green"))
    		return Color.green;
    	else if(name.equalsIgnoreCase("blue"))
    		return Color.blue;
    	else if(name.equalsIgnoreCase("purple"))
    		return Color.magenta;
    	else if(name.equalsIgnoreCase("pink"))
    		return Color.magenta;
    	else if(name.equalsIgnoreCase("black"))
    		return Color.black;
    	else if(name.equalsIgnoreCase("white"))
    		return Color.white;
    	else if(name.equalsIgnoreCase("gray"))
    		return Color.gray;
    	else if(name.equalsIgnoreCase("grey"))
    		return Color.gray;
    	else return null;
    }
    public void addReactionRole(ReactionRole r, Guild g) {
    	verifyReactionRoles(g);
    	
    	Long id;
    	id = g.getIdLong();
    	
    	List<ReactionRole> rr;
    	rr = reactionRoles.get(id);
    	rr.add(r);
    	reactionRoles.put(id, rr);
    }
    public void addReactionRole(ReactionRole r, Long id) {
    	verifyReactionRoles
    	(bot.getGuildById(id));
    	
    	List<ReactionRole> rr;
    	rr = reactionRoles.get(id);
    	rr.add(r);
    	reactionRoles.put(id, rr);
    }
    public boolean isReactionRole(Message m, Guild g) {
    	verifyReactionRoles(g);
    	
    	Long id, messageid;
    	messageid = m.getIdLong();
    	id = g.getIdLong();
    	List<ReactionRole> rr;
    	rr = reactionRoles.get(id);
    	for(ReactionRole r : rr) {
    		if(messageid.equals
    		(r.getMessageRaw())) {
    			return true; }
    	} return false;
    }
    public boolean isReactionRole(Long messageid, Guild g) {
    	verifyReactionRoles(g);
    	
    	Long id;
    	id = g.getIdLong();
    	List<ReactionRole> rr;
    	rr = reactionRoles.get(id);
    	for(ReactionRole r : rr) {
    		if(messageid.equals
    		(r.getMessageRaw())) {
    			return true; }
    	} return false;
    }
    public List<ReactionRole> getReactionRoles(Guild g) {
    	verifyReactionRoles(g);
    	return reactionRoles.get(g.getIdLong());
    }
    public ReactionRole getReactionRole(Guild g, Message m) {
    	verifyReactionRoles(g);
    	List<ReactionRole> rr;
    	rr = reactionRoles.get(g.getIdLong());
    	Long mID;
    	mID = m.getIdLong();
    	
    	if(!isReactionRole(m,g)) {
    		return null;
    	}
    	for(ReactionRole r : rr) {
    		Long rID;
    		rID = r.getMessageRaw();
    		if(rID.equals(mID)) {
    			return r;
    		}
    	}
    	return null;
    }
    public ReactionRole getReactionRole(Guild g, Long mID) {
    	verifyReactionRoles(g);
    	List<ReactionRole> rr;
    	rr = reactionRoles.get(g.getIdLong());
    	
    	if(!isReactionRole(mID,g)) {
    		return null;
    	}
    	for(ReactionRole r : rr) {
    		Long rID;
    		rID = r.getMessageRaw();
    		if(rID.equals(mID)) {
    			return r;
    		}
    	}
    	return null;
    }
    public List<String> getFilter(Guild g) {
    	verifyFilters(g);
    	Long id = g.getIdLong();
    	return filteredWords.get(id);
    }
    public boolean isURL(String test) {
    	return test.matches("(https?:\\/\\/(?:www\\.|(?!www))[a-zA-Z0-9][a-zA-Z0-9-]+[a-zA-Z0-9]\\.[^\\s]{2,}|www\\.[a-zA-Z0-9][a-zA-Z0-9-]+[a-zA-Z0-9]\\.[^\\s]{2,}|https?:\\/\\/(?:www\\.|(?!www))[a-zA-Z0-9]+\\.[^\\s]{2,}|www\\.[a-zA-Z0-9]+\\.[^\\s]{2,})");
    }
    public boolean randomBool() {
    	if(random() == 0) {
    		return false;
    	} else {
    		return true;
    	}
    }
    public boolean isAdmin(User u) {
    	long id = u.getIdLong();
    	for(long adminid : admins) {
    		if(id == adminid) {
    			return true;
    		}
    	}
    	return false;
    }
    public void privateMessage(User u, String text) {
    	u.openPrivateChannel().queue((channel) -> {
    		channel.sendMessage(text).queue();
    	});
    }
    public void privateMessage(Member m, String text) {
    	User u = m.getUser();
    	u.openPrivateChannel().queue((channel) -> {
    		channel.sendMessage(text).queue();
    	});
    }
    public void privateMessage(User u, MessageEmbed eb) {
    	u.openPrivateChannel().queue((channel) -> {
    		channel.sendMessage(eb).queue();
    	});
    }
    public void privateMessage(Member m, MessageEmbed eb) {
    	try {
	    	User u = m.getUser();
	    	u.openPrivateChannel().queue((channel) -> {
	    		channel.sendMessage(eb).queue();
	    	});
    	} catch(Exception exc) {} // ignore because user can't be dm'd
    }
    public int random() {
    	return random(2);
    }
    public int random(int max) {
    	SecureRandom rand;
    	rand = new SecureRandom();
    	return rand.nextInt(max);
    }
	public String getEmojiForBoolean(boolean b) {
		if(b)
			return "✅";
		else
			return "🔳";
	}
    public Object choose(Object... objects) {
    	return objects[random(objects.length)];
    }
    public boolean messagesDisabled(Guild g) {
    	verifyJLChannel(g);
    	long id = g.getIdLong();
    	long jlchannel = joinLeaveChannels.get(id);
    	if(jlchannel == 0l) {
    		return true;
    	} else {
    		return false;
    	}
    }
    public Color roleColor(Member m) {
    	Role role;
    	List<Role> roles = m.getRoles();
    	if(roles.isEmpty()) {
    		return Color.gray;
    	}
    	role = roles.get(0);
    	return role.getColor();
    }
    public void createMutedRole(Guild g) {
    	g.createRole()
		.setName("Muted")
		.setColor(Color.gray)
		.queue(muted -> {
	    	List<GuildChannel> chnls;
	    	chnls = g.getChannels();
	    	for(GuildChannel c : chnls) {
	    		try {
	    			ChannelManager cm;
	    			cm = c.getManager();
	    			cm.putPermissionOverride
		    			(muted, 0l, Permission.MESSAGE_WRITE
		    			.getRawValue()).queue(); // deny
	    			continue;
	    		} catch(Exception exc) {
	    			continue;
	    		}
	    	}
		});
    }
    public Role getMutedRole(Guild g) {
    	verifyMuted(g);
    	
    	List<Role> lm;
    	lm = g.getRolesByName
    		("Muted", true);
    	Role r = lm.get(0);
    	return r;
    }
    public String combine(String[] args, int startIndex) {
    	StringBuilder sb = new StringBuilder();
    	int i = -1;
    	for(String word : args) {
    		i++;
    		if(i < startIndex) {
    			continue;
    		}
    		sb.append(" " + word);
    	}
    	try {
    		sb = sb.deleteCharAt(0);
    	} catch(StringIndexOutOfBoundsException exc) {
    		return sb.toString();
    	}
    	return sb.toString();
    }
    public Color randomColor() {
    	SecureRandom rand = new SecureRandom();
    	float r, g, b;
    	r = rand.nextFloat();
    	g = rand.nextFloat();
    	b = rand.nextFloat();
    	return new Color(r, g, b);
    }
    public String getVideoChannel(long chid, long svid) {
    	return "https://discordapp.com/channels/" + svid + "/" + chid;
    }
    public Message fetchMessageFromCache(Guild g, long id) {
    	long gID = g.getIdLong();
    	List<Message> msgs = cache.get(gID);
    	if(msgs == null) {
    		return null;
    	}
    	for(Message m : msgs) {
    		if(m.getIdLong() == id) {
    			return m;
    		}
    	}
    	return null;
    }
    public Message fetchLastDeletedMessage(Guild g) {
    	long gID = g.getIdLong();
    	if(!exposed.containsKey(gID)) {
    		return null;
    	}
    	Message m = exposed.get(gID);
    	return m;
    }
    
    public static boolean channelHasModifier(TextChannel tc, String mod) {
    	if(tc.getTopic() == null) { return false; }
    	String targ = mod.toLowerCase().trim();
    	return tc.getTopic()
    			.toLowerCase()
    			.replaceAll(" ", "")
    			.contains(targ);
    }
    public static String getDurationBreakdown(long millis) {
        if(millis < 0) {
            throw new IllegalArgumentException("Duration must be greater than zero!");
        }
        long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
        millis -= TimeUnit.MINUTES.toMillis(minutes);
        long _seconds = TimeUnit.MILLISECONDS.toSeconds(millis);

        String seconds = _seconds + "";
        if(seconds.length() <= 1) {
        	seconds = "0" + seconds;
        }
        
        StringBuilder sb = new StringBuilder(64);
        sb.append(minutes);
        sb.append(":");
        sb.append(seconds);
        sb.append("");

        return(sb.toString());
    }
    public boolean isMod(Member m, Guild g) {
    	if(isAdmin(m.getUser())) {
    		return true;
    	}
    	verifyModRole(g);
    	Long modid = modRoles.get(g.getIdLong());
    	Role mod = g.getRoleById(modid);
    	if(m.isOwner() || m.hasPermission(Permission.ADMINISTRATOR)) {
    		return true;
    	}
    	if(mod == null) {
    		System.out.print("An unexpected error occurred in the method \"isMod(Member m, Guild g)\" (role is null?)\n");
    		return false;
    	}
    	List<Role> roles;
    	roles = m.getRoles();
    	
    	if(roles.contains(mod)) {
    		return true;
    	} else {
    		return false;
    	}
    }
    public Role getModRole(Guild g) {
    	verifyModRole(g);
    	Long gid = g.getIdLong();
    	Long modid = modRoles.get(gid);
    	Role mod = g.getRoleById(modid);
    	return mod;
    }
    public String getJoinMessage(Guild g) {
    	verifyJoinLeaveMessages(g);
    	return joinMessages.get(g.getIdLong());
    }
    public String getLeaveMessage(Guild g) {
    	verifyJoinLeaveMessages(g);
    	return leaveMessages.get(g.getIdLong());
    }
    public MessageChannel getJLChannel(Guild g) {
    	MessageChannel channel;
    	Long id = g.getIdLong();
    	Long c = joinLeaveChannels
    			.get(id);
    	channel = 
    	g.getTextChannelById(c);
    	
    	return channel;
    }
    public Role getJoinRole(Guild g) {
    	Long id = joinRoles.get(g.getIdLong());
    	if(id == 0l) {
    		return null;
    	}
    	Role r = g.getRoleById(id);
    	return r;
    }
    public UserProfile getProfile(Member m) {
    	verifyProfile(m);
    	
    	Long gID, mID;
    	gID = m.getGuild().getIdLong();
    	mID = m.getUser().getIdLong();
    	
    	HashMap<Long,UserProfile> up;
    	up = profiles.get(gID);
    	return up.get(mID);
    }
    public void setProfile(Member m, UserProfile profile) {
    	verifyProfile(m);
    	
    	Long gID, mID;
    	gID = m.getGuild().getIdLong();
    	mID = m.getUser().getIdLong();
    	
    	HashMap<Long,UserProfile> up;
    	up = profiles.get(gID);
    	up.put(mID, profile);
    	profiles.put(gID, up);
    	return;
    }
    public void loadAndPlay(TextChannel channel, String trackURL, boolean superplay) {
    	System.out.println("Received " + trackURL);
    	GuildMusicManager mm = getGuildAudioPlayer(channel.getGuild());
    	playerManager.loadItemOrdered(mm, trackURL, new AudioLoadResultHandler() {
    		@Override
    		public void trackLoaded(AudioTrack track) {
    			channel.sendMessage(successEmbed("Added to queue: **" + track.getInfo().title + "**")).queue();
    			play(channel.getGuild(), mm, track, superplay);
    		}
			@Override
			public void loadFailed(FriendlyException e) {
				channel.sendMessage(failureEmbed("Audio track " + trackURL + " failed to load. May have been taken down or unavailable in bonzi country!")).queue();
				channel.sendMessage("*If this problem persists send the devs this nerd garbage with the \"report\" command:*\n" + e.toString() + "\n\n" + buildStackTrace(e.getStackTrace())).queue();
			}
			@Override
			public void noMatches(){
				channel.sendMessage(successEmbed("Searching youtube for the track **\"" + trackURL + "\"**...")).queue();
				try {
					Search.List request = yt.search().list("snippet");
					SearchListResponse response = request
							.setKey(Private.YTAPI_KEY)
							.setMaxResults(1l)
							.setQ(trackURL)
							.setVideoCategoryId("10")
							.setType("video")
							.execute();
					List<SearchResult> results;
					results = response.getItems();
					if(results.isEmpty()) {
						channel.sendMessage(failureEmbed("Couldn't find anything on youtube! (maybe put a url or try a different search term!)")).queue();
						return;
					}
					SearchResult sr = results.get(0);
					String id = sr.getId().getVideoId();
					String newurl = "https://www.youtube.com/watch?v=" + id;
					
			    	playerManager.loadItemOrdered(mm, newurl, new AudioLoadResultHandler() {
			    		@Override
			    		public void trackLoaded(AudioTrack track) {
			    			channel.sendMessage(successEmbed("Added to queue: \"" + track.getInfo().title + "\"")).queue();
			    			play(channel.getGuild(), mm, track, superplay);
			    		}
						@Override
						public void loadFailed(FriendlyException e) {
							e.printStackTrace();
							channel.sendMessage("Lol I failed to play this track: " + e.getMessage() + "\n\nInner cause: " + e.getCause().toString() + 
									"\n\nStacktrace: " + buildStackTrace(e.getStackTrace())).queue();
						}
						@Override
						public void noMatches(){
							channel.sendMessage(failureEmbed("Couldn't find anything on youtube! (maybe put a url or try a different search term!)")).queue();
						}
						@Override
						public void playlistLoaded(AudioPlaylist ap) {
							int i = 0;
							for(AudioTrack ft : ap.getTracks()) {
								if(i >= 25) {
									channel.sendMessage(successEmbed("Whoa, that playlist has a lot of songs, so I can't add them all at once! I added the first 25 though!")).queue();
									break;
								}
								i++;
								play(channel.getGuild(), mm, ft, superplay);
							}
							channel.sendMessage(successEmbed("Added songs in the playlist **\"" + ap.getName() + "\"** to the queue.")).queue();
						}
			    	});
			    	return;
				} catch(IOException ioe) {
					channel.sendMessage(failureEmbed("Unexpected error. Please try again.")).queue();
					ioe.printStackTrace();
					return;
				}
			}
			@Override
			public void playlistLoaded(AudioPlaylist ap) {
				int i = 0;
				for(AudioTrack ft : ap.getTracks()) {
					if(i >= 25) {
						channel.sendMessage("(Selected playlist contains more than allowed tracks... Only added first 25.)").queue();
						break;
					}
					i++;
					play(channel.getGuild(), mm, ft, superplay);
				}
				channel.sendMessage("Added songs in the playlist **\"" + ap.getName() + "\"** to the queue.").queue();
			}
    	});
    }
    public void loadAndPlay(TextChannel channel, File f, boolean superplay) {
    	GuildMusicManager mm = getGuildAudioPlayer(channel.getGuild());
    	
    	//AudioInputStream input = AudioSystem.getAudioInputStream(f);
    	//long length = (long) (1000 * input.getFrameLength() / input.getFormat().getFrameRate());
    	
    	//AudioTrackInfo inf = new AudioTrackInfo("Speak Command", "BonziBot", length, "idk", true, f.getAbsolutePath());
    	
    	playerManager.loadItemOrdered(mm, f.getPath(), new AudioLoadResultHandler() {
    		@Override
    		public void trackLoaded(AudioTrack track) {
    			System.out.print(track.getInfo().uri + "\n");
    			play(channel.getGuild(), mm, track, superplay);
    		}
			@Override
			public void loadFailed(FriendlyException e) {
				channel.sendMessage(failureEmbed("Voice clip failed to load.\n\n" + e.getMessage())).queue();
			}
			@Override
			public void noMatches() {
				channel.sendMessage(failureEmbed("The voice clip triggered noMatches which is very odd... Report this bug using b:report and include detail about what you did!")).queue();
				return;
			}
			@Override
			public void playlistLoaded(AudioPlaylist ap) {
				channel.sendMessage(failureEmbed("this is definitely not a playlist, you might want to report this to the devs lol")).queue();
				return;
				// dont handle this because its stupid
			}
    	});
    }
    public void playPlaylist(TextChannel channel, Playlist p) {
    	GuildMusicManager mm = getGuildAudioPlayer(channel.getGuild());
    	for(String url : p.urls) {
        	playerManager.loadItemOrdered(mm, url, new AudioLoadResultHandler() {

    			@Override
    			public void trackLoaded(AudioTrack track) {
        			play(channel.getGuild(), mm, track, false);
    			}

    			@Override
    			public void playlistLoaded(AudioPlaylist playlist) {
					int i = 0;
					for(AudioTrack ft : playlist.getTracks()) {
						if(i >= 25) {
							break;
						}
						i++;
						play(channel.getGuild(), mm, ft, false);
					}
    			}
    			@Override
    			public void noMatches() {
    				channel.sendMessage("Didn't find a valid song in one of these URLS. " + url).queue();
    			}
    			@Override
    			public void loadFailed(FriendlyException exception) {
    				channel.sendMessage(failureEmbed("Audio track failed to load. May have been taken down or unavailable in bonzi country!\n" + url)).queue();
    			}
        	});
    	}
    	channel.sendMessage("Loaded and queued playlist!").queue();
    }
    public GuildMusicManager getGuildAudioPlayer(Guild g) {
    	long gID = g.getIdLong();
    	GuildMusicManager mm = musicManagers.get(gID);
    	
    	if(mm == null) {
    		mm = new GuildMusicManager(playerManager);
    		musicManagers.put(gID, mm);
    	}
    	
    	g.getAudioManager()
    		.setSendingHandler
    		(mm.getSendHandler());
    	return mm;
    }
    public void play(Guild g, GuildMusicManager gmm, AudioTrack track, boolean superplay) {
    	
    	AudioTrackInfo ati = track.getInfo();
    	System.out.println("Play being called: " + ati.title + ", len: " + ati.length);
    	
    	track.setUserData(g.getIdLong());
    	gmm.scheduler.queue(track, superplay);
    }
    public void skipTrack(TextChannel channel, boolean message, int amount, boolean ignoreShuffle) {
    	GuildMusicManager gmm = getGuildAudioPlayer(channel.getGuild());
    	boolean empty = false;
    	for(int i = 0; i < amount; i++) {
    		empty = gmm.scheduler.nextTrack(ignoreShuffle);
    		if(empty)break;
    	}
    	if(message) {
    		if(amount < 1)
    			channel.sendMessage("Skipped no tracks.").queue();
    		if(amount == 1)
    			channel.sendMessage("Skipped the track.").queue();
    		if(amount > 1)
    			channel.sendMessage("Skipped the tracks.").queue();
    	}
    	if(empty) {
    		Guild g = channel.getGuild();
    		g.getAudioManager().closeAudioConnection();
    	}
    }
    public void skipTrack(GuildMusicManager gmm, boolean ignoreShuffle) {
    	gmm.scheduler.nextTrack(ignoreShuffle);
    }
    public void addPlay(String name) {
    	int index = getPlaylistIndexByName(name);
    	if(index == -1) {return;}
    	Playlist p = playlists.get(index);
    	playlists.remove(index);
    	p.plays++;
    	playlists.add(p);
    }
    public boolean playlistExists(String name) {
    	for(Playlist p : playlists) {
    		if(p.name.equalsIgnoreCase(name)) {
    			return true;
    		}
    	}
    	return false;
    }
    public void addTrackToPlaylist(String url, String name) {
    	int index = getPlaylistIndexByName(name);
    	if(index == -1) {return;}
    	Playlist p = playlists.get(index);
    	playlists.remove(index);
    	p.addTrack(url);
    	playlists.add(p);
    }
    public void clearPlaylist(String name) {
    	int index = getPlaylistIndexByName(name);
    	if(index == -1) {return;}
    	Playlist p = playlists.get(index);
    	playlists.remove(index);
    	p.clearTracks();
    	playlists.add(p);
    }
    public void addPlaylist(String name, User author, List<String> urls) {
    	Playlist p = new Playlist(urls, name, author);
    	playlists.add(p);
    	return;
    }
    public void addPlaylist(Playlist p) {
    	playlists.add(p);
    	return;
    }
    public void removePlaylistByName(String name) {
    	Playlist p = getPlaylistByName(name);
    	playlists.remove(p);
    }
    public void removePlaylistsByCreator(User u) {
    	List<Playlist> p = getPlaylistsByCreator(u);
    	playlists.removeAll(p);
    }
    public void removePlaylistsByCreator(Long id) {
    	List<Playlist> p = getPlaylistsByCreator(id);
    	playlists.removeAll(p);
    }
    public Playlist getPlaylistByName(String name) {
    	int index = getPlaylistIndexByName(name);
    	if(index == -1) { return Playlist.NONE; }
    	return playlists.get(index);
    }
    public int getPlaylistIndexByName(String name) {
    	int i = -1;
    	for(Playlist p : playlists) {
    		i++;
    		if(p.name.equalsIgnoreCase(name)) {
    			return i;
    		}
    	}
    	return -1;
    }
    public List<Playlist> getPlaylistsByCreator(User u) {
    	List<Playlist> pl = new ArrayList<Playlist>();
    	long id = u.getIdLong();
    	for(Playlist p : playlists) {
    		if(p.creatorID == id) {
    			pl.add(p);
    		}
    	}
    	return pl;
    }
    public List<Playlist> getPlaylistsByCreator(long id) {
    	List<Playlist> pl = new ArrayList<Playlist>();
    	for(Playlist p : playlists) {
    		if(p.creatorID == id) {
    			pl.add(p);
    		}
    	}
    	return pl;
    }

    public void performMute(Member m, Member as) {
    	Guild g = m.getGuild();
    	User u = m.getUser();
    	verifyMuted(g);
    	Role muted = getMutedRole(g);
    	g.addRoleToMember(m, muted).queue();
    	
    	LoggingEntry le = new LoggingEntry();
    	le.targetUser = u.getIdLong();
    	le.invokedUser = as.getUser().getIdLong();
    	le.reason = "Invalid warn.";
    	le.ext = null;
    	le.type = LogType.Mute;
    	
    	EmbedBuilder eb = baseLogEmbed("User muted.", as.getUser(), Color.orange);
    	eb.setDescription("Invoker's Name: " + getFriendCode(as.getUser()) +
    			"\nTarget's Name: " + getFriendCode(u));
    	le.embed = eb.build();
    	
    	tryLog(g, le, "b_undo");
    }
    public void performUnmute(Member m, Member as) {
    	Guild g = m.getGuild();
    	User u = m.getUser();
    	verifyMuted(g);
    	Role muted = getMutedRole(g);
    	g.removeRoleFromMember(m, muted).queue();
    	
    	LoggingEntry le = new LoggingEntry();
    	le.targetUser = u.getIdLong();
    	le.invokedUser = as.getUser().getIdLong();
    	le.reason = "Invalid warn.";
    	le.ext = null;
    	le.type = LogType.Unmute;
    	
    	EmbedBuilder eb = baseLogEmbed("User unmuted.", as.getUser(), Color.orange);
    	eb.setDescription("Invoker's Name: " + getFriendCode(as.getUser()) +
    			"\nTarget's Name: " + getFriendCode(u));
    	le.embed = eb.build();
    	
    	tryLog(g, le, "b_undo");
    }
    
    public TextChannel checkIfScriptChannelExists(Guild g) {
    	for(TextChannel tc : g.getTextChannels()) {
    		if(channelHasModifier(tc, SCRIPTEDITOR_MODIFIER)) {
    			return tc;
    		}
    	}
    	return null;
    }
    public TextChannel createScriptChannel(Guild g) {
    	TextChannel n = g.createTextChannel("bonzi-script-workspace")
    		.addPermissionOverride(g.getPublicRole(), 0l, Permission.ALL_PERMISSIONS)
    		.setTopic("Channel modifier: " + SCRIPTEDITOR_MODIFIER)
    		.complete();
    	
    	n.sendMessage(successEmbed("Welcome to the scripting workspace. Type \"help\" to get started."))
    		.queue(msg -> {
    			long gID = g.getIdLong();
    			if(!scriptCreators.containsKey(gID)) {
    				scriptCreators.put(gID, new ScriptCreator(gID, msg.getIdLong()));
    			} else {
    				ScriptCreator editor;
    				editor = scriptCreators.get(gID);
    				editor.mainMsgID = msg.getIdLong();
    				editor.resetMenu();
    				scriptCreators.put(gID, editor);
    			}
    			
    	});
    	return n;
    }
    
    public boolean containsSwear(Message m) {
    	Guild guild = m.getGuild();
    	TextChannel sent = m.getTextChannel();
    	if(sent != null && sent.isNSFW()) {
    		return false;
    	}
    	List<String> filter = getFilter(guild);
    	for(String word : m.getContentRaw().split("\\s+")) {
    		if(channelHasModifier(m.getTextChannel(), "nofilter")) { break; }
    		for(String swear : filter) {
    			if(word.toLowerCase().contains(swear.toLowerCase())) {
    				return true;
    			}
    		}
    	}
    	return false;
    }
	public String sendGet(String url) throws Exception {
		// I have no idea how to do that HTTP bullcrap, so thanks random stackoverflow user!
		final String USER_AGENT = "Mozilla/5.0";
		
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setRequestMethod("GET");

		con.setRequestProperty("User-Agent", USER_AGENT);
		
		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		return response.toString();
	}
	public String getFriendCode(User u) {
		return u.getName() + "#" + u.getDiscriminator();
	}
	
	public int calculateLevel(int xp) {
		return (int)Math.floor(Math.sqrt(((double)xp)*0.1));
	}
	public Guild getBonziBotGuild(JDA jda) {
		return jda.getGuildById(discord);
	}
	public TextChannel getUpdatesChannel(JDA jda) {
		Guild bbdiscord = getBonziBotGuild(jda);
		if(bbdiscord == null) {
			return null;
		}
		List<TextChannel> tc_list;
		tc_list = bbdiscord.getTextChannelsByName
			("latest", true);
		if(tc_list.isEmpty()) {
			return null;
		}
		return tc_list.get(0);
	}
	// This method is unused currently.
	public Message[] getUpdatesFromChannel(TextChannel tc, int amount) {
		Message[] me = new Message[amount];
		Long id = tc.getLatestMessageIdLong();
		Message latest = tc.retrieveMessageById(id).complete();
		List<Message> history;
		history = tc.getHistoryBefore(id, amount-1)
			.complete().getRetrievedHistory();
		for(int i = 0; i < amount; i++) {
			if( i==0 ) { me[i]=latest; } else {
				me[i] = history.get(i-1);
			}
		}
		return me;
	}
	
	public void tryLog(Guild g, LoggingEntry ent, String...reactions) {
		MessageEmbed me = ent.embed;
		if(me == null) { return; }
		
		TextChannel firstLogger = null;
		for(TextChannel tc : g.getTextChannels()) {
			if(channelHasModifier(tc, LOG_MODIFIER)) {
				firstLogger = tc;
				break;
			}
		}
		if(firstLogger == null) { return; }
		Guild bguild = getBonziBotGuild(g.getJDA());
		firstLogger.sendMessage(me).queue(msg -> {
			long id = msg.getIdLong();
			ent.sentID = id;
			logs.add(ent);
			
			for(String s : reactions) {
				msg.addReaction(bguild.getEmotesByName(s, true).get(0)).queue();
			}
		});
	}
  	// Not severe = green
	// Somewhat severe = yellow
	// Very severe = red
	public LoggingEntry getDeleteLog(Message deleted) {
		User u = deleted.getAuthor();
		if(u.isBot() && containsSwear(deleted)) {
			String name = getFriendCode(u);
			String aurl = u.getEffectiveAvatarUrl();
			
			String content = deleted.getContentStripped();
			if(content.length() > 1500) {
				content = content.substring(1500) + "...";
			}
			
			EmbedBuilder eb = new EmbedBuilder();
			eb.setColor(Color.yellow);
			eb.setTitle("User triggered swear filter.");
			eb.setAuthor(name, null, aurl);
			eb.setDescription("Channel: " + deleted.getChannel().getName() +
					"\nContents:\n\n" + content);
			
			LoggingEntry ent = new LoggingEntry();
			ent.reason = "Sending the text: \"" + content + "\"";
			ent.embed = eb.build();
			ent.targetUser = u.getIdLong();
			ent.type = LogType.Swear;
			return ent;
		} else {
			String name = getFriendCode(u);
			String aurl = u.getEffectiveAvatarUrl();
			
			String content = deleted.getContentStripped();
			if(content.length() > 1500) {
				content = content.substring(1500) + "...";
			}
			
			EmbedBuilder eb = new EmbedBuilder();
			eb.setColor(Color.green);
			eb.setTitle("Message Deleted.");
			eb.setAuthor(name, null, aurl);
			eb.setDescription("Channel: " + deleted.getChannel().getName() +
					"\nContents:\n\n" + content);
			
			LoggingEntry ent = new LoggingEntry();
			ent.reason = "Sending the text: \"" + content + "\"";
			ent.embed = eb.build();
			ent.targetUser = u.getIdLong();
			ent.type = LogType.DeletedMessage;
			return ent;
		}
	}
	public EmbedBuilder baseLogEmbed(String title, User target, Color intensity) {
		EmbedBuilder eb = new EmbedBuilder();
		eb.setColor(intensity);
		if(target != null) {
			eb.setAuthor(target.getName(), null,
				target.getEffectiveAvatarUrl());
		}
		eb.setTitle(title);
		return eb;
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
	
	public static final int UPGRADE_COST = 750; // coins, not upgrades.
	public static final int PRIVATE_TAG_COST = 10;
	public static final int SCRIPT_EDITOR_COST = 25;
	public static final int ALL_PREMIUM_COST = 100;
	public int getUpgrades(Guild g) {
		long id = g.getIdLong();
		if(!upgrades.containsKey(id))
			upgrades.put(id, 0);
		if(!upgraders.containsKey(id)) {
			upgraders.put(id, new LinkedList<Long>());
			//System.out.println("wrote new list.");
		}
		return upgrades.get(id);
	}
	public void upgradeGuild(User performer, Guild g) {
		long uid = performer.getIdLong();
		long id = g.getIdLong();
		int ups = getUpgrades(g) + 1;
		upgrades.put(id, ups);
		subtractCoins(performer, UPGRADE_COST);
		
		List<Long> upgr = upgraders.get(id);
		
		//System.out.print("before add: ");
		//for(long l: upgr) System.out.println(l);
		if(!upgr.contains(uid)) {
			upgr.add(uid);
		}
		//System.out.print("after add: ");
		//for(long l: upgr) System.out.println(l);
		upgraders.put(id, upgr);
		
		// LIST ENTRIES
		/*for(Map.Entry<Long, List<Long>> kv: upgraders.entrySet()) {
			System.out.println(kv.getKey() + ":");
			for(long l: kv.getValue()) {
				System.out.println("	- " + l);
			}
		}*/
	}
	public int getCoins(User u) {
		verifyCoins(u);
		Long id = u.getIdLong();
		return coins.get(id);
	}
	public void setCoins(User u, int amount) {
		long id = u.getIdLong();
		coins.put(id, amount);
	}
	public void verifyCoins(User u) {
		Long id = u.getIdLong();
		if(!coins.containsKey(id)) {
			coins.put(id, 0);
		}
	}
	public void addCoins(User u, int amount) {
		int current = getCoins(u);
		current+=amount;
		setCoins(u, current);
	}
	public void subtractCoins(User u, int amount) {
		int current = getCoins(u);
		current-=amount;
		if(current < 0) {
			current = 0;
		}
		setCoins(u, current); 
	}
	public boolean hasCommand(Member m, PremiumCommand cmd) {
		User u = m.getUser();
		
		Guild g = m.getGuild();
		if(getUpgrades(g) >= ALL_PREMIUM_COST)
			return true;
		
		long id = u.getIdLong();
		List<PremiumCommand> acct = premiumAccounts.get(id);
		return acct.contains(cmd);
	}
	public String buildStackTrace(StackTraceElement[] ste) {
		StringBuilder sb = new StringBuilder();
		for(StackTraceElement e : ste) {
			sb.append(e.toString() + "\n");
		}
		return sb.toString();
	}
	public void generateVoiceFile(String text) {
		ap = new SingleFileAudioPlayer("..\\voiceData\\ttsoutput", AudioFileFormat.Type.WAVE);
		kevin16.setAudioPlayer(ap);
		kevin16.speak(text);
		ap.close();
		return;
	}
	public String generateVoiceFileWithID(String text) {
		SecureRandom rand = new SecureRandom();
		int picked = rand.nextInt();
		
		String path = "..\\voiceData\\voiceSample_" + picked;
		
		ap = new SingleFileAudioPlayer(path, javax.
			sound.sampled.AudioFileFormat.Type.WAVE);
		kevin16.setAudioPlayer(ap);
		kevin16.speak(text);
		ap.close();
		return path + ".wav";
	}
	public void speakInChannel(String text, Member m, TextChannel channel) {
		GuildVoiceState voice = m.getVoiceState();
		if(!voice.inVoiceChannel()) {
			channel.sendMessage(failureEmbed("You must be in a voice channel to use this!")).queue();
			return;
		}
		VoiceChannel vc = voice.getChannel();
		AudioManager am = vc.getGuild().getAudioManager();
		am.openAudioConnection(vc);
		
		// Actual file stuff
		String path = generateVoiceFileWithID(text);
		File f = new File(path);
		if(!f.exists()) {
			channel.sendMessage(failureEmbed("It seems like the audio file was not generated... Maybe try again?")).queue();
			return;
		}
		loadAndPlay(channel, f, false);
	}
	/*public void getInputStream(Guild g, VoiceChannel vc) {
		long gID = g.getIdLong();
		if(!streams.containsKey(gID)) {
			
		}
	}*/
	
	// Fired by the TrackScheduler class when all tracks are empty.
	public static void noMoreTracks(long gID) {
		System.out.println("noMoreTracks() Fired.");
		// This is a realy bad practice but there's no other simple way to obtain a JDA object otherwise in this scenario.
		// (This is because the bot object may be "outdated" from current events.)
		Guild g = bot.getGuildById(gID);
		if(g == null) {
			System.out.print("noMoreTracks() was supplied an invalid guild ID.\n");
			return;
		}
		Member self = g.getSelfMember();
		GuildVoiceState state = self.getVoiceState();
		AudioManager am = g.getAudioManager();
		if(state.inVoiceChannel()) {
			am.closeAudioConnection();
		}
	}
	public LoggingEntry getLoggingEntryByID(long id) {
		for(LoggingEntry l : logs) {
			if(l.sentID == id) {
				return l;
			}
		}
		return null;
	}
}