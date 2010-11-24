package com.cdadar.musicr.work;

import java.util.ArrayList;
import org.json.*;

public class TrackList {
	
	ArrayList<Track> tracks;
	
	public TrackList()
	{
		tracks = new ArrayList<Track>();
	}

	public ArrayList<Track> getTracks(){ return tracks;}
		
	public void selectTrack(String name)
	{
		currentTrack().setRecording(false);
		findTrack(name).setRecording(true);		
	}
	
	public Track currentTrack()
	{
		for(int i = 0; i<tracks.size();i++)
			if (tracks.get(i).isRecording()) return tracks.get(i);
		return null;
	}
	
	public boolean renameTrack(String old, String name)
	{
		Track t = findTrack(old);
		t.setName(name);
		return true;
	}
	
	public boolean trackExists(String name){
		return !(findTrack(name) == null);
		
	}

	public ArrayList<Track> getRecordingTracks()
	{
		ArrayList<Track> recs = new ArrayList<Track>();
		
		for(int i =0; i< tracks.size();i++ )
		{
			if (tracks.get(i).isSolo())
			{
				recs.clear();
				recs.add(tracks.get(i));
				return recs;
			}
			else if(!tracks.get(i).isRecording() && !tracks.get(i).isMuted())
			{
				recs.add(tracks.get(i));
			}
		}
		return recs;
	}
	
	
	
	public Track findTrack(String name)
	{
		for(int i = 0;i< getTracks().size();i++)
		{
			Track t = getTracks().get(i);
			if (name ==t.name)
				return t;
		}
		return null;
	}
	
	public Track createNewTrack(String name)
	{
		if (!trackExists(name))
		{
			Track t = new Track(name);
			tracks.add(t);
			return t;
		}
		return null;
	}
	
	public Track createNewRecordingTrack(String name){
		Track t = createNewTrack(name);
		t.setRecording(true);
		return t;
	}
	
	
	public boolean deleteTrack(String name){
		if(trackExists(name)){
			tracks.remove(findTrack(name));
			return true;
		}
		return false;
	}
	
	public static TrackList fromJSON(JSONArray arr)
	{
		TrackList tlist = new TrackList();
		ArrayList<Track> ts = tlist.getTracks();
		
		for(int i =0; i< arr.length();i++)
		{
			try {
				ts.add(Track.FromJSON(arr.getJSONObject(i)));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return tlist;
	}
	
	
	public JSONArray toJSON()
	{
		JSONArray arr = new JSONArray();
		
		for(int i = 0; i<tracks.size();i++)
		{
			arr.put(tracks.get(i).toJSON());
		}
		
		return arr;
		
	}

}
