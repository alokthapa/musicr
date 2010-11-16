package com.cdadar.musicr.work;

import java.io.IOException;
import java.io.RandomAccessFile;


import android.media.AudioFormat;
import android.media.AudioRecord;
import android.util.Log;

public class WaveTrack {
	
	private RandomAccessFile fWriter;
	private byte[] 			 buffer;
	private static final int TIMER_INTERVAL = 120;
	
	short nChannels;
	int sRate;
	short bSamples;
	int	framePeriod;
	private int bufferSize;
	String fPath;
	int payloadSize = 0;
	int cAmplitude = 0;
	
	public int getMaxAmplitude()
	{
		return cAmplitude;
	}

	public byte[] getBuffer(){ return buffer;}

	public int getFramePeriod(){ return framePeriod;}

	public int getBufferSize() {return bufferSize;}

	public WaveTrack(String fPath, int sRate, int audioFormat, int channelConfig)
	{
		this.fPath = fPath;
		if (audioFormat == AudioFormat.ENCODING_PCM_16BIT)
		{
			bSamples = 16;
		}
		else
		{
			bSamples = 8;
		}
		
		if (channelConfig == AudioFormat.CHANNEL_CONFIGURATION_MONO)
		{
			nChannels = 1;
		}
		else
		{
			nChannels = 2;
		}
		
		this.sRate = sRate;
		framePeriod = sRate * TIMER_INTERVAL / 1000;
		bufferSize = framePeriod * 2 * bSamples * nChannels / 8;
		if (bufferSize < AudioRecord.getMinBufferSize(sRate, channelConfig, audioFormat))
		{ 
			bufferSize = AudioRecord.getMinBufferSize(sRate, channelConfig, audioFormat);
			framePeriod = bufferSize / ( 2 * bSamples * nChannels / 8 );
		}
		
		prepare();
	}
	
	public void prepare()
	{
		try{
			
			fWriter = new RandomAccessFile(fPath, "rw");
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
		catch (Exception e){
			Log.e("err", "In WaveTrack prepare: " + e.toString());
		}
	}
	
	public void finish()
	{
		try
		{
			fWriter.seek(4); // Write size to RIFF header
			fWriter.writeInt(Integer.reverseBytes(36+payloadSize));
		
			fWriter.seek(40); // Write size to Subchunk2Size field
			fWriter.writeInt(Integer.reverseBytes(payloadSize));
		
			fWriter.close();
		}
		catch(IOException e)
		{
			Log.e("err", "In WaveTrack finish: "+ e.toString());
		}

	}
	
	public void writeBuffers()
	{
		try{
			fWriter.write(buffer); // Write buffer to file
		}
		catch(Exception e){ Log.e("err","In WaveTrack writeBuffers: "+ e.toString());}
		
		payloadSize += buffer.length;
		if (bSamples == 16)
		{
			for (int i=0; i<buffer.length/2; i++)
			{ // 16bit sample size
				short curSample = getShort(buffer[i*2], buffer[i*2+1]);
				if (curSample > cAmplitude)
				{ // Check amplitude
					cAmplitude = curSample;
				}
			}
		}
		else
		{ // 8bit sample size
			for (int i=0; i<buffer.length; i++)
			{
				if (buffer[i] > cAmplitude)
				{ // Check amplitude
					cAmplitude = buffer[i];
				}
			}
		}

	}
	
	//utility fn
	private short getShort(byte argB1, byte argB2)
	{
		return (short)(argB1 | (argB2 << 8));
	}

}
