package com.cdadar.musicr;
import com.cdadar.musicr.work.*;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.media.AudioFormat;
import android.media.MediaPlayer;
import android.media.MediaRecorder.AudioSource;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.cdadar.musicr.work.*;

public class RecordActivity extends Activity {
	Project p = null;	
	RehearsalAudioRecorder rec = null;
	MediaPlayer mp = null;
	
	public void onCreate(Bundle savedInstanceState) 
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.record);
        TextView tv = (TextView)findViewById(R.id.txtrecordproject);
        
        p = P.currentProject();
        tv.setText("Project name "+ p.name());
        
        TextView tv1 = (TextView)findViewById(R.id.txtrecordtrack);
        tv1.setText("Track name:"+ p.getTrackList().currentTrack().getName());
        
        findViewById(R.id.btnstoprecord).setEnabled(false);
        

        findViewById(R.id.btnplay).setOnClickListener(
        		new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
					    PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
					    PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK);
					    wl.aquire();
					    
						MixWave mix = new MixWave(P.currentProject());
						mix.mix();
					    mp = new MediaPlayer();
					    try{
					    	
						    mp.setDataSource(P.currentProject().getTrackPath("mix"));
						    mp.prepare();
					    
						    
						// and record!
        				rec = 
        					new RehearsalAudioRecorder(
        							true,   //record uncompressed
        							AudioSource.MIC, 			//audio source
        							44100, 						//sample rate
        							AudioFormat.CHANNEL_CONFIGURATION_MONO,  //mono recording
        							AudioFormat.ENCODING_PCM_16BIT); // 16 bit recording
        				rec.setOutputFile(p.getCurrentTrackPath());
        				rec.prepare();

					    mp.start();

        				rec.start();
        				findViewById(R.id.btnrecord).setEnabled(false);
        				findViewById(R.id.btnstoprecord).setEnabled(true);

					    }
					    catch(Exception e)
						{ 
						Log.e("err", "error in btnplay onclick: "+ e.toString());
						if (mp !=null)
						    mp.release();
						if (rec !=null)
						    rec.release();

					    }
					    finally
						{
						    wl.release();
						    

						}

					}
				});
        
        findViewById(R.id.btnrecord).setOnClickListener(
        		new View.OnClickListener() {
        			public void onClick(View v) {
        				rec = 
        					new RehearsalAudioRecorder(
        							true,   //record uncompressed
        							AudioSource.MIC, 			//audio source
        							44100, 						//sample rate
        							AudioFormat.CHANNEL_CONFIGURATION_MONO,  //mono recording
        							AudioFormat.ENCODING_PCM_16BIT); // 16 bit recording
        				rec.setOutputFile(p.getCurrentTrackPath());
        				rec.prepare();
        				rec.start();
        				findViewById(R.id.btnrecord).setEnabled(false);
        				findViewById(R.id.btnstoprecord).setEnabled(true);
   		}});
        
    	findViewById(R.id.btnsettings ).setOnClickListener(    	
    			new View.OnClickListener() {
					public void onClick(View v) {
			            Intent myIntent = new Intent(v.getContext(), SettingsActivity.class);
			            startActivityForResult(myIntent, 0);
					}
				});
    	
        findViewById(R.id.btnstoprecord).setOnClickListener(
        		new View.OnClickListener() {
        			public void onClick(View v) {
    			
        				if (mp != null)
        				{
        					mp.stop();
        					mp.release();
        				}
        				if (rec != null)
        				{
        					rec.stop();
        					rec.release();
            				findViewById(R.id.btnrecord).setEnabled(true);
            		        findViewById(R.id.btnstoprecord).setEnabled(false);
        				}

        			}});
        
        		
        
	}
	
	


}
