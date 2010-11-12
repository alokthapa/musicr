package com.cdadar.musicr.work;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import android.util.Log;

public class Project {
	
	ArrayList<String> tracks = null;
	
	String projname;
	String apppath = "/sdcard/commusicr/";
	String workpath = "";
	String currentTrack;
	
	
	public Project(String projname)
	{
		this.projname = projname;
		tracks = new ArrayList<String>();
		
		//create a default track
		tracks.add("track1");
		currentTrack = tracks.get(0);
	}

	public String getCurrentTrack()
	{
		return currentTrack;
	}
	
	public ArrayList<String> getTrackList()
	{
		return tracks;
	}
	
	//tracks
	
	public boolean createNewTrack(String name)
	{
		if(!tracks.contains(name))
		{
			tracks.add(name);
			return true;
		}
		return false;
	}
	
	public boolean deleteTrack(String name){
		if (tracks.contains(name))
		{	
			tracks.remove(name);
			return new File(getTrackPath(name)).delete();		
		}
		return false;
	}
	
	public void selectTrack(String name)
	{
		if (tracks.contains(name))
		{
			this.currentTrack = name;
		}
	}
	
	public boolean renameTrack(String old, String name)
	{
		if (!tracks.contains(name))
		{
			tracks.remove(old);
			tracks.add(name);
			renameFile(old, name);
			return true;
		}
		return false;
	}
	
	
	public void renameFile(String old, String name){
		File f = new File(getTrackPath(old));
		f.renameTo(new File(getTrackPath(name)));	
	}
	
	public String getProjectPath(){return apppath + projname;}
	
	public String getCurrentTrackPath(){ return getTrackPath(currentTrack);}
	
	public String getTrackPath(String track){ return apppath+projname +"/"+track+".wav";}
	
	public String name(){return this.projname;}
	
	public static Project CreateProject(String projname)
	{
		Project p = new Project(projname);
		try
		{
			p.setupProject();
		}
		catch(Exception e){
			Log.e("err","Exception occured while creating project:" + e.toString());
			return null;
		}
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

	    if (!directory.exists() && !directory.mkdirs()) {
	    	Log.e("err","path to file could not be created");
	      throw new IOException("Path to file could not be created.");
	    }

		return false;
		
	}

}
