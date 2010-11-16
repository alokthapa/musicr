

package com.cdadar.musicr.work;

import java.io.RandomAccessFile;
import java.util.ArrayList;

import android.media.AudioRecord;
import android.util.Log;


// this file is responsible to mix wave files into one!
public class MixWave {

	//assumes perfect world scenario where all files are 16bit wav files and exist.
	
	private Project project;
	
	private String mixtrack = "mix";
	// Number of frames written to file on each output(only in uncompressed mode)
	private int				 framePeriod;
		
	// Buffer for output(only in uncompressed mode)
	private byte[] 			 buffer;
	
	// Number of bytes written to file after header(only in uncompressed mode)
	// after stop() is called, this size is written to the header/data chunk in the wave file
	private int				 payloadSize;
	
	private static final int TIMER_INTERVAL = 120;

	private short 			 nChannels = 2;
	private int				 sRate = 44100;
	private short			 bSamples = 16;
	private int				 bufferSize;
	private int				 aSource;
	private int				 aFormat;

	
	public MixWave(Project p){
		this.project = p;
		framePeriod = sRate * TIMER_INTERVAL / 1000;
		bufferSize = framePeriod * 2 * bSamples * nChannels / 8;
		prepare(mixtrack);
	}
	
	
	public void prepare(String fPath) 
	{
		try{
			RandomAccessFile fWriter  =  new RandomAccessFile( fPath, "rw");
			fWriter.setLength(0); // Set file length to 0, to prevent unexpected behavior in case the file already existed
			fWriter.writeBytes("RIFF");
			fWriter.writeInt(0); // Final file size not known yet, write 0 
			fWriter.writeBytes("WAVE");
			fWriter.writeBytes("fmt ");
			fWriter.writeInt(Integer.reverseBytes(16)); // Sub-chunk size, 16 for PCM
			fWriter.writeShort(Short.reverseBytes((short) 1)); // AudioFormat, 1 for PCM
			fWriter.writeShort(Short.reverseBytes(nChannels));// Number of channels, 1 for mono, 2 for stereo
			fWriter.writeInt(Integer.reverseBytes(sRate)); // Sample rate
			fWriter.writeInt(Integer.reverseBytes(sRate*bSamples*nChannels/8)); // Byte rate, SampleRate*NumberOfChannels*BitsPerSample/8
			fWriter.writeShort(Short.reverseBytes((short)(nChannels*bSamples/8))); // Block align, NumberOfChannels*BitsPerSample/8
			fWriter.writeShort(Short.reverseBytes(bSamples)); // Bits per sample
			fWriter.writeBytes("data");
			fWriter.writeInt(0); // Data chunk size not known yet, write 0
			buffer = new byte[framePeriod*bSamples/8*nChannels];

		}
		catch(Exception e){ Log.e("err","in prepare of MixWave: "+ e.toString());}
	}
	
	
	
	public void mix() throws Exception
	{
		ArrayList<Byte> databytes = new ArrayList<Byte>();
		ArrayList<Track> rtracks = project.getTrackList().getRecordingTracks();

		for(int i = 0; i< rtracks.size(); i++)
		{
			String filename= project.getTrackPath(rtracks.get(i));
			RandomAccessFile fWriter  =  new RandomAccessFile( filename, "r");
			fWriter.skipBytes(44);
			short s = getShort(fWriter.readByte(), fWriter.readByte()); //supposedly should do that!
			
			
			
			
			
			
		}
		
		
	}
	
	/* 
	 * 
	 * Converts a byte[2] to a short, in LITTLE_ENDIAN format
	 * 
	 */
	private short getShort(byte argB1, byte argB2)
	{
		return (short)(argB1 | (argB2 << 8));
	}
	
	

}
