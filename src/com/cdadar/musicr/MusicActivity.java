package com.cdadar.musicr;

import java.io.IOException;

import android.app.Activity;
import android.graphics.Color;
import android.media.*;
import android.content.*;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.*;
import android.widget.Button;
import android.widget.Toast;

public class MusicActivity extends Activity {

	private MediaRecorder recorder;
	

	MediaPlayer mp;
	AudioRecorder rec;
	public void record()
	{
		rec = new AudioRecorder("/musicr/temp2");
		try
		{
			rec.start();
		}
		catch(Exception e){ 
			e.printStackTrace();
		}
	}
	

	
	private void play()
	{
		try
		{

		    mp = new MediaPlayer();
		    mp.setDataSource("/sdcard/woods.mp3");
		    mp.prepare();
		    mp.start();
		}catch(Exception e){
			e.printStackTrace();
		}

		
	}

	private void play2()
	{
		try
		{

		    mp = new MediaPlayer();
		    mp.setDataSource("/sdcard/chudai.mp3");
		    mp.prepare();
		    mp.start();
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	
	

	public View.OnClickListener startListener = new View.OnClickListener() {
		public void onClick(View v) {
			play();
			
			record();
		}
	};


	private View.OnClickListener stopListener = new View.OnClickListener() {
		public void onClick(View v) {
			try {
				rec.stop();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	};
	
	private View.OnClickListener playListener = new View.OnClickListener() {
		public void onClick(View v) {
				play2();
				play();
		
		}
	};

	
	/** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState) {
   
    	
    	
//    	
//    	Button stopButton = (Button) findViewById(R.id.stop);
//    	stopButton.setOnClickListener(stopListener);
//    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        
        recorder = new MediaRecorder();
        
    	findViewById(R.id.start).setOnClickListener(startListener);
    	findViewById(R.id.stop).setOnClickListener(stopListener);
    	findViewById(R.id.play).setOnClickListener(playListener);

    }
    
    
}