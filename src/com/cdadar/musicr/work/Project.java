package com.cdadar.musicr.work;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import android.util.Log;

public class Project {
	
	TrackList tlist;
	String projname;
	String apppath = "/sdcard/commusicr/";
	String workpath = "";
	
	public Project(String projname)
	{
		this.projname = projname;
		tlist = new TrackList();
		tlist.createNewRecordingTrack("track1");
		
	}
	
	public TrackList getTrackList() { return tlist;}
	
	
	public String getProjectPath(){return apppath + projname;}
	
	public String getCurrentTrackPath(){ return getTrackPath(tlist.currentTrack());}
	
	public String getTrackPath(Track track){ return getTrackPath(track.getName());}
	
	public String getTrackPath(String track){ return apppath+projname +"/"+track+".wav";}

	
	public String name(){return this.projname;}
	
	public static Project CreateProject(String projname) throws Exception 
	{
		Project p = new Project(projname);
		p.setupProject();
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

}
