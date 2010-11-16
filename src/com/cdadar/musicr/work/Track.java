package com.cdadar.musicr.work;



public class Track {

	String name;
	String desc;
	String filename;
	boolean mute;
	boolean solo;
	boolean recording;
	
	
	Track(String name)
	{
		this.name = name;
		this.desc = "";
		mute = false;
		solo = false;
		recording = false;
	}
	
	public String getName(){return name;}
	
	public Track setName(String val){ name = val; return this;}
	
	public boolean isMuted(){ return mute;}
	
	public boolean isSolo(){return solo;}
	
	public boolean isRecording(){return recording;}
	
	public Track setMute(boolean val){ mute = val; return this;}
	
	public Track setSolo(boolean val){ solo = val; return this;}
	
	public Track setRecording(boolean val){ recording = val; return this;}
	
	
}
