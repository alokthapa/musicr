package com.cdadar.musicr.work;

import org.json.*;


import java.io.*;
import java.io.IOException;
import java.util.ArrayList;

import android.util.Log;

public class Project {
	
	TrackList tlist;
	String projname;
	String workpath ;
	String apppath ;
	
	public Project(String projname)
	{
		this.apppath = P.ProjectDir;
		this.projname = projname;
		tlist = new TrackList();
		
	}
	
	public TrackList getTrackList() { return tlist;}
	
	public void setTrackList(TrackList tlist) { this.tlist = tlist;}
	

	public String getProjectPath(){return apppath + projname;}
	
	public String getCurrentTrackPath(){ return getTrackPath(tlist.currentTrack());}
	
	public String getTrackPath(Track track){ return getTrackPath(track.getName());}
	
	public String getTrackPath(String track){ return apppath+projname +"/"+track+".wav";}

	
	public String name(){return this.projname;}
	
	public static Project CreateProject(String projname) throws Exception 
	{
		Project p = new Project(projname);
		p.setupProject();
		p.getTrackList().createNewRecordingTrack("track1");
		
		p.save();
		return p;
	}
	
	public boolean setupProject() throws IOException
	{
		
	    String state = android.os.Environment.getExternalStorageState();
	    Log.i("msg","now creating directories: "+ getProjectPath());
	    if(!state.equals(android.os.Environment.MEDIA_MOUNTED))  {
	    	Log.e("err","SD card is not mounted");
	        throw new IOException("SD Card is not mounted.  It is " + state + ".");
	    }

	    // make sure the directory we plan to store the recording in exists
	    File directory = new File(getProjectPath());
	    if (directory.exists()){
	    	throw new IOException("Project already exists!");
	    }

	    if (!directory.exists() && !directory.mkdirs()) {
	    	Log.e("err","path to file could not be created");
	      throw new IOException("Path to file could not be created.");
	    }

		return false;
	}
	
	
	public String getSavePath()
	{
		return this.apppath + this.projname + ".json";
		
	}
	
	public void save()
	{
		try 
		{
		    FileWriter fstream = new FileWriter(getSavePath());
		    BufferedWriter out = new BufferedWriter(fstream);
		    out.write(this.toJSON().toString());
		    out.close();
		    fstream.close();
		    
		}
		catch (Exception e){
			Log.e("err", "error while saving project "+  this.projname+ ": "+ e.toString());
		}
	}
	
	
	
	public static Project fromJSON(JSONObject obj)
	{
		try 
		{
			Project p = new Project(obj.getString("name"));
			p.setTrackList(TrackList.fromJSON(obj.getJSONArray("tracklist")));
		} 
		catch (JSONException e) 
		{
			e.printStackTrace();
		}

		return null;
	}
	
	public JSONObject toJSON()
	{
		try {
			JSONObject obj = new JSONObject();

			obj.accumulate("name",projname);
			obj.accumulate("tracklist", this.tlist.toJSON());
			return obj;

		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return null;
	}

}
