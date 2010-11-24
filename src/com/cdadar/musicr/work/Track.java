package com.cdadar.musicr.work;

import org.json.*;



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
	
	public static Track FromJSON(JSONObject obj)
	{
		try
		{
			Track t = new Track(obj.getString("name"));
			t.setMute(obj.getBoolean("mute"));
			t.setSolo(obj.getBoolean("solo"));
			t.setRecording(obj.getBoolean("recording"));
			return t;

			
		}
		catch(Exception e){}
		return null;
	}
	
	public JSONObject toJSON()
	{
		JSONObject obj = new JSONObject();
		try {
			obj.accumulate("name",name);
			obj.accumulate("desc",desc);
			obj.accumulate("filename",filename);
			obj.accumulate("mute",mute);
			obj.accumulate("solo", solo);
			obj.accumulate("recording", recording);
			return obj;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return null;
		
	}
	
	
}
