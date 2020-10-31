package com.lukecreator.BonziBot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.dv8tion.jda.api.entities.User;

public class Playlist implements Serializable {
	
	public static final Playlist NONE = new Playlist(new ArrayList<String>(), "Unknown");
	
	private static final long serialVersionUID = -245744481115596341L;
	public final int ID;
	public String name;
	public List<String> urls;
	public String creator;
	public long creatorID;
	
	public int plays;
	
	public void addTrack(String url) {
		urls.add(url);
	}
	public void clearTracks() {
		urls.clear();
	}
	public boolean isOwner(User u) {
		return creatorID == u.getIdLong();
	}
	public boolean removeUrl(String url) {
		return urls.remove(url);
	}
	public Playlist(List<String> urls, String name, User author) {
		this.urls = urls;
		this.name = name;
		creator = author.getName() + "#"
			+ author.getDiscriminator();
		creatorID = author.getIdLong();
		plays = 0;
		ID = App.playlists.size()+1;
	}
	// obsolete
	public Playlist(List<String> urls, String name) {
		this.urls = urls;
		this.name = name;
		plays = 0;
		ID = App.playlists.size()+1;
	}
}
