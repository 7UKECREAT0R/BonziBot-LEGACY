package com.lukecreator.BonziBot;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;

import java.io.File;
import java.security.SecureRandom;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * This class schedules tracks for the audio player. It contains the queue of tracks.
 */
public class TrackScheduler extends AudioEventAdapter {
	
	private final AudioPlayer player;
	public BlockingQueue<AudioTrack> queue;
	 /**
	 * @param player The audio player this scheduler uses
	 */
	public TrackScheduler(AudioPlayer player) {
	  this.player = player;
	  this.queue = new LinkedBlockingQueue<>();
	}
	 /**
	 * Add the next track to queue or play right away if nothing is in the queue.
	 *
	 * @param track The track to play or add to queue.
	 */
	public void queue(AudioTrack track, boolean sp) {
	  // Calling startTrack with the noInterrupt set to true will start the track only if nothing is currently playing. If
	  // something is playing, it returns false and does nothing. In that case the player was already playing so this
	  // track goes to the queue instead.
		if(sp) {
			BlockingQueue<AudioTrack> clone =
				new LinkedBlockingQueue<AudioTrack>();
			clone.add(track);
			clone.addAll(queue);
			queue = clone;
		} else {
		    if (!player.startTrack(track, true)) {
		  	    queue.offer(track);
		    }
		}

	}
	 /**
	 * Start the next track, stopping the current one if it is playing.
	 * Returns true if the queue is empty.
	 */
	public boolean nextTrack(boolean ignoreShuffle) {
		// Start the next track, regardless of if something is already playing or not. In case queue was empty, we are
		// giving null to startTrack, which is a valid argument and will simply stop the player.
		AudioTrack playing = player.getPlayingTrack();
		if(playing != null && App.queueloop.contains
				((long)playing.getUserData())) {
			AudioTrack clone = playing.makeClone();
			clone.setUserData(playing.getUserData());
			queue.add(clone);
		}
		
		if(playing != null && App.shuffle.contains((long)playing.getUserData()) && !ignoreShuffle) {
			SecureRandom rand = new SecureRandom();

			int pick;
			if(queue.size() == 1) {
				pick = 1;
			} else if(queue.size() <= 0) {
				return true;
			} else {
				pick = rand.nextInt(queue.size());
			}
			
			AudioTrack toClone = ((AudioTrack)queue.toArray()[pick]);
			AudioTrack rClone = toClone.makeClone();
			rClone.setUserData(toClone.getUserData());
			player.startTrack(rClone, false);
			return queue.peek() == null;
		} else {
			boolean isEmpty = queue.peek() == null;
			AudioTrack starting = queue.poll();
			player.startTrack(starting, false);
			return isEmpty;
		}
	}
	@Override
	public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
		
		//System.out.println("Track ended: " + track.getInfo().title + ", len: " + track.getInfo().length + ",\nENDREASON: " + endReason.name());
		
		// Only start the next track if the end reason is suitable for it (FINISHED or LOAD_FAILED)
		long data = (long)track.getUserData();
		if (endReason.mayStartNext) {
			if(App.loop.contains(data) && endReason != AudioTrackEndReason.STOPPED) {
				AudioTrack clone = track.makeClone();
				clone.setUserData(data);
				player.startTrack(clone, false);
			} else if(App.shuffle.contains(data) && queue.size() > 0 && endReason != AudioTrackEndReason.STOPPED) {
				SecureRandom rand = new SecureRandom();

				int pick;
				if(queue.size() == 1) {
					pick = 1;
				} else {
					pick = rand.nextInt(queue.size());
				}
				
				AudioTrack rClone = ((AudioTrack)queue.toArray()[pick]).makeClone();
				rClone.setUserData(data);
				player.startTrack(rClone, false);
			} else if(App.queueloop.contains(data) && queue.size() > 0 && endReason != AudioTrackEndReason.STOPPED) {
				AudioTrack clone = track.makeClone();
				clone.setUserData(data);
				queue.add(clone);
				nextTrack(false);
			} else {
				File f = new File(track.getInfo().uri);
				if(f.exists()) {
					f.delete();
				}
				if(queue.peek() == null) {
					App.noMoreTracks(data);
				}
				nextTrack(false);
			}
		}
	}
}
