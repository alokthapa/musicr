
package com.cdadar.musicr.work;
import java.io.*;

import java.util.ArrayList;

import android.util.Log;

public class MixWave
{
	Project project ;
	
  public MixWave(Project project)
  {
	  this.project = project;
  }
  
  public void mix()
  {
	  try{
		  
		  	ArrayList<Track> tracks = project.getTrackList().getRecordingTracks();
		  	ArrayList<WavFile> wavs = new ArrayList<WavFile>();
		  	int [][] buffers = new int[tracks.size()][];
		  	int minFrames = Integer.MAX_VALUE;
		  	
		  	for(int i =0;i<tracks.size(); i++)
		  	{
		  		String path = project.getTrackPath(tracks.get(i));
		  		WavFile f =WavFile.openWavFile(new File(path));
		  		minFrames = Math.min(minFrames, (int)f.getNumFrames());
		  		wavs.add(f);
		  	}
		  	
		  	int []buffer = new int[(int)minFrames];
		  	

	  		for(int i =0;i< tracks.size();i++)
	  		{
	  			buffers[i] = new int[(int)minFrames];
	  		}
	  		
	  		for(int i =0;i< tracks.size();i++)
	  		{
	  			wavs.get(i).readFrames(buffers[i],(int)minFrames);
	  		}
	  		
		    WavFile mix = WavFile.newWavFile(new File(project.getTrackPath("mix")), 1, minFrames, 16, 44100);
		    for (int s = 0; s< minFrames  ; s++)
		    {
		    	
		    	for(int i =0;i<tracks.size();i++)
		    	{
		    		buffer[s] += buffers[i][s];
		    	}
		    		buffer[s] /= tracks.size();
		    }
		    
		    mix.writeFrames(buffer,minFrames);
		    // Close the wavFile
			mix.close();
				
		  
	  }
	  catch(Exception e)
	  {
		  Log.e("err", "in MixWave mix error: " + e.toString());
	  }
	
		
	  
  }
}

 