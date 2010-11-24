
package com.cdadar.musicr.work;
import java.io.*;

import java.util.ArrayList;

import android.util.Log;


//this class is ugly since it has to deal with memory stuff...

public class MixWave
{
	Project project ;
	WavFile mix;

  public MixWave(Project project)
  {
	  this.project = project;
  }
  
  private int chunksize = 10000;

  private void actualMix(ArrayList<WavFile> w, int size)
  {
	  int [][] bs = new int[w.size()][(int)size];
	  int [] b = new int [size];  
	  
	  try
	  {
		  for(int i =0;i< w.size();i++)
		  {
			  w.get(i).readFrames(bs[i],size);
		  }

		  for (int s = 0; s< size  ; s++)
		  {
			  for(int i =0;i<w.size();i++)
			  {
				  b[s] += bs[i][s];
			  }
	  			b[s] /= w.size();
		  }
		  mix.writeFrames(b, size);
	  }
	  catch (Exception e)
	  {
		  Log.e("err", "error in actualMix : "+ e.toString() );
	  }
  }
  
  //so that we don't overload the buffer...
  private void chunkedMix(ArrayList<WavFile> w, int frames)
  {
	  if (frames< chunksize)
	  {
		  actualMix(w, frames);
	  }
	  else
	  {
		  actualMix(w, chunksize);
		  chunkedMix(w, frames - chunksize);
	  }
  }
  
  private void chunkedCopy(WavFile w, int frames)
  {
	  if (frames < chunksize)
	  {
		  actualCopy(w, frames);
	  }
	  else{
		  actualCopy(w, chunksize);
		  actualCopy(w, frames- chunksize);
	  }
  }
  
  private void actualCopy(WavFile w, int frames)
  {
	  try
	  {
		  int remaining = (int)w.getFramesRemaining();
		  int [] rest = new int [remaining];
		  w.readFrames(rest,remaining);
		  mix.writeFrames(rest, remaining);
	  }
	  catch(Exception e)
	  {
		  Log.e("err", "error at simpleCopy: " + e.toString());
	  }
  }
  

  private void simpleCopy(WavFile w)
  {
	  chunkedCopy(w, (int)w.getFramesRemaining());
  }
  
  private void  mixTracks(ArrayList<WavFile> w)
  {
	  if (w.size() == 1)
	  {
		  if (w.get(0).getFramesRemaining() > 0)
			  simpleCopy(w.get(0));
	  }
	  else
	  {
		  //assume that all track lengths are smaller than max integer value
		  int minframes = Integer.MAX_VALUE;
		  WavFile least = null;
		  for(int i = 0; i< w.size(); i++)
		  {
			  if (w.get(i).getFramesRemaining() < minframes)
			  {  
				  minframes = (int) w.get(i).getFramesRemaining(); // because we already know it can't be bigger than int
				  least = w.get(i);
			  }
		  }
		  chunkedMix(w, minframes);
		  w.remove(least);
		  mixTracks(w);
	  }
  }
  
  public void mix()
  {
	  try{
		  	ArrayList<Track> tracks = project.getTrackList().getRecordingTracks();
		  	if (tracks.size() == 1)
		  	{
		  		Track t = tracks.get(0);
		  	}
		  	
		  	ArrayList<WavFile> wavs = new ArrayList<WavFile>();

		  	long longest = Long.MIN_VALUE;
		  	
		  	
		  	for(int i =0;i<tracks.size(); i++)
		  	{
		  		String path = project.getTrackPath(tracks.get(i));
		  		WavFile f =WavFile.openWavFile(new File(path));
		  		longest = Math.max(longest, f.getNumFrames());
		  		wavs.add(f);
		  	}
		  	mix = WavFile.newWavFile(new File(project.getTrackPath("mix")), 1, longest, 16, 44100);

		  	
		  	mixTracks(wavs);
			mix.close();
	  }
	  catch(Exception e)
	  {
		  Log.e("err", "in MixWave mix error: " + e.toString());
	  }
  }
}

 