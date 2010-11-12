package com.cdadar.musicr.work;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.util.Log;

public class MusicrAudioRecorder
{
	/**
	 * INITIALIZING : recorder is initializing;
	 * READY : recorder has been initialized, recorder not yet started
	 * RECORDING : recording
	 * ERROR : reconstruction needed
	 * STOPPED: reset needed
	 */
	public enum State {INITIALIZING, READY, RECORDING, ERROR, STOPPED};
	
	public static final boolean RECORDING_UNCOMPRESSED = true;
	public static final boolean RECORDING_COMPRESSED = false;
	
	// The interval in which the recorded samples are output to the file
	// Used only in uncompressed mode
	private static final int TIMER_INTERVAL = 120;

	// Toggles uncompressed recording on/off; RECORDING_UNCOMPRESSED / RECORDING_COMPRESSED
	private boolean 		 rUncompressed;
	
	// Recorder used for uncompressed recording
	private AudioRecord 	 aRecorder = null;
	// Recorder used for compressed recording
	private MediaRecorder	 mRecorder = null;
	
	private byte [] buffer;
	
	
	
	
	// Recorder state; see State
	private State			 state;
	
	WaveTrack track; 		
	
	/**
	 * 
	 * Returns the state of the recorder in a RehearsalAudioRecord.State typed object.
	 * Useful, as no exceptions are thrown.
	 * 
	 * @return recorder state
	 */
	public State getState()
	{
		return state;
	}
	
	/*
	 * 
	 * Method used for recording.
	 * 
	 */
	private AudioRecord.OnRecordPositionUpdateListener updateListener = new AudioRecord.OnRecordPositionUpdateListener()
	{
		public void onPeriodicNotification(AudioRecord recorder)
		{
			aRecorder.read(track.getBuffer(), 0, track.getBuffer().length); // Fill buffer
			track.writeBuffers();
		}
		
		public void onMarkerReached(AudioRecord recorder)
		{
			// NOT USED
		}
	};
	
	/** 
	 * 
	 * 
	 * Default constructor
	 * 
	 * Instantiates a new recorder, in case of compressed recording the parameters can be left as 0.
	 * In case of errors, no exception is thrown, but the state is set to ERROR
	 * 
	 */ 

	public MusicrAudioRecorder(String fPath,  boolean uncompressed, int audioSource, int sampleRate, int channelConfig,
			int audioFormat)
	{
		try
		{
			rUncompressed = uncompressed;
			if (rUncompressed)
			{ // RECORDING_UNCOMPRESSED
				track = new WaveTrack(fPath, sampleRate, audioFormat, channelConfig);
				aRecorder = new AudioRecord(audioSource, sampleRate, channelConfig, audioFormat, track.getBufferSize());
				if (aRecorder.getState() != AudioRecord.STATE_INITIALIZED)
					throw new Exception("AudioRecord initialization failed");
				aRecorder.setRecordPositionUpdateListener(updateListener);
				aRecorder.setPositionNotificationPeriod(track.getFramePeriod());
			} else
			{ // RECORDING_COMPRESSED
				mRecorder = new MediaRecorder();
				mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
				mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
				mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
			}
			
			this.setOutputFile(fPath);

			state = State.INITIALIZING;
		} catch (Exception e)
		{
			if (e.getMessage() != null)
			{
				Log.e(MusicrAudioRecorder.class.getName(), e.getMessage());
			}
			else
			{
				Log.e(MusicrAudioRecorder.class.getName(), "Unknown error occured while initializing recording");
			}
			state = State.ERROR;
		}
	}
	
	/**
	 * Sets output file path, call directly after construction/reset.
	 *  
	 * @param output file path
	 * 
	 */
	
	String fPath;
	public void setOutputFile(String argPath)
	{
		try
		{
			if (state == State.INITIALIZING)
			{
				fPath = argPath;
				if (!rUncompressed)
				{
					mRecorder.setOutputFile(fPath);
				}
			}
		}
		catch (Exception e)
		{
			if (e.getMessage() != null)
			{
				Log.e(MusicrAudioRecorder.class.getName(), e.getMessage());
			}
			else
			{
				Log.e(MusicrAudioRecorder.class.getName(), "Unknown error occured while setting output path");
			}
			state = State.ERROR;
		}
	}
	
	/**
	 * 
	 * Returns the largest amplitude sampled since the last call to this method.
	 * 
	 * @return returns the largest amplitude since the last call, or 0 when not in recording state. 
	 * 
	 */
	public int getMaxAmplitude()
	{
		if (state == State.RECORDING)
		{
			if (rUncompressed)
			{
				
				return track.getMaxAmplitude();
			}
			else
			{
				try
				{
					return mRecorder.getMaxAmplitude();
				}
				catch (IllegalStateException e)
				{
					return 0;
				}
			}
		}
		else
		{
			return 0;
		}
	}
	

	/**
	 * 
	* Prepares the recorder for recording, in case the recorder is not in the INITIALIZING state and the file path was not set
	* the recorder is set to the ERROR state, which makes a reconstruction necessary.
	* In case uncompressed recording is toggled, the header of the wave file is written.
	* In case of an exception, the state is changed to ERROR
	* 	 
	*/
	public void prepare()
	{
		try
		{
			if (state == State.INITIALIZING)
			{
				if (rUncompressed)
				{
					if ((aRecorder.getState() == AudioRecord.STATE_INITIALIZED) & (fPath != null))
					{
						// write file header
						state = State.READY;
					}
					else
					{
						Log.e(MusicrAudioRecorder.class.getName(), "prepare() method called on uninitialized recorder");
						state = State.ERROR;
					}
				}
				else
				{
					mRecorder.prepare();
					state = State.READY;
				}
			}
			else
			{
				Log.e(MusicrAudioRecorder.class.getName(), "prepare() method called on illegal state");
				release();
				state = State.ERROR;
			}
		}
		catch(Exception e)
		{
			if (e.getMessage() != null)
			{
				Log.e(MusicrAudioRecorder.class.getName(), e.getMessage());
			}
			else
			{
				Log.e(MusicrAudioRecorder.class.getName(), "Unknown error occured in prepare()");
			}
			state = State.ERROR;
		}
	}
	
	/**
	 * 
	 * 
	 *  Releases the resources associated with this class, and removes the unnecessary files, when necessary
	 *  
	 */
	public void release()
	{
		if (state == State.RECORDING)
		{
			stop();
		}
		else
		{
		}
		
		if (rUncompressed)
		{
			if (aRecorder != null)
			{
				aRecorder.release();
			}
		}
		else
		{
			if (mRecorder != null)
			{
				mRecorder.release();
			}
		}
	}
	
	/**
	 * 
	 * 
	 * Resets the recorder to the INITIALIZING state, as if it was just created.
	 * In case the class was in RECORDING state, the recording is stopped.
	 * In case of exceptions the class is set to the ERROR state.
	 * 
	 */
	public void reset()
	{
		try
		{
			if (state != State.ERROR)
			{
				release();
				fPath = null; // Reset file path
				if (rUncompressed)
				{
					//aRecorder = new AudioRecord(aSource, sRate, nChannels+1, aFormat, bufferSize);
				}
				else
				{
					mRecorder = new MediaRecorder();
					mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
					mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
					mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
				}
				state = State.INITIALIZING;
			}
		}
		catch (Exception e)
		{
			Log.e(MusicrAudioRecorder.class.getName(), e.getMessage());
			state = State.ERROR;
		}
	}
	
	/**
	 * 
	 * 
	 * Starts the recording, and sets the state to RECORDING.
	 * Call after prepare().
	 * 
	 */
	public void start()
	{
		if (state == State.READY)
		{
			if (rUncompressed)
			{
				aRecorder.startRecording();
				aRecorder.read(track.getBuffer(), 0, track.getBuffer().length);
			}
			else
			{
				mRecorder.start();
			}
			state = State.RECORDING;
		}
		else
		{
			Log.e(MusicrAudioRecorder.class.getName(), "start() called on illegal state");
			state = State.ERROR;
		}
	}
	
	/**
	 * 
	 * 
	 *  Stops the recording, and sets the state to STOPPED.
	 * In case of further usage, a reset is needed.
	 * Also finalizes the wave file in case of uncompressed recording.
	 * 
	 */
	public void stop()
	{
		if (state == State.RECORDING)
		{
			if (rUncompressed)
			{
				aRecorder.stop();
				track.finish();
				
			}
			else
			{
				mRecorder.stop();
			}
			state = State.STOPPED;
		}
		else
		{
			Log.e(MusicrAudioRecorder.class.getName(), "stop() called on illegal state");
			state = State.ERROR;
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
