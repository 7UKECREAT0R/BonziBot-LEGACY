package com.lukecreator.BonziBot.Web;

import java.net.MalformedURLException;

public class StatGame {

	public boolean statsInline = false;
	public boolean usesSteamURL = false;
	
	public String customSplitString = "\n";
	
	public int skipLinesDown = 0;
	
	public String displayName; // How it will display in BonziBot.
	public String keyword; // The keyword in the arguments used.
	public String badProfileText;
	
	public String queryURL; // The URL that the scraper will queue.
	public InternalStat[] stats; // The stats to fetch.
	
	public StatGame(String displayName, String keyword, String badProfileText, String queryURL, InternalStat... stats) {
		this.displayName = displayName;
		this.keyword = keyword;
		this.badProfileText = badProfileText;
		this.queryURL = queryURL;
		this.stats = stats;
	}
	public Stat[] getPlayerStats(String username) {
		try {
			
			Scraper scr = new Scraper(queryURL + username);
			scr.downloadSiteData(customSplitString);
			
			if(scr.contentRaw == null) {
				return null;
			}
			if(scr.contentRaw.contains(badProfileText))
				return null;
			
			System.out.println(scr.contentRaw);
			
			Stat[] fetched = new Stat[stats.length];
			for(int i = 0; i < stats.length; i++) {
				InternalStat is = stats[i];
				int ln = scr.lineOfField(is.statQueryName);
				String res;
				if(!statsInline)
					res = scr.getLine(ln + 1 + skipLinesDown).trim();
				else {
					res = scr.getLine(ln + skipLinesDown);
					String[] tmp = res.split(">");
					res = tmp[1];
					tmp = res.split("<");
					res = tmp[0].trim();
				}
				fetched[i] = new Stat(is.statName, res);
			}
			return fetched;
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch(IndexOutOfBoundsException e) {
			return null;
		}
		return null;
	}
}