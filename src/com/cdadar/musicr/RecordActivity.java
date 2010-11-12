package com.cdadar.musicr;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.media.AudioFormat;
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
	

	
	public void onCreate(Bundle savedInstanceState) 
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.record);
        TextView tv = (TextView)findViewById(R.id.txtrecordproject);
        
        //p = new Project(getIntent().getStringExtra("com.cdadar.musicr.project"));
        p = P.currentProject();
        tv.setText("Project name "+ p.name());
        
        TextView tv1 = (TextView)findViewById(R.id.txtrecordtrack);
        tv1.setText("Track name:"+ p.getCurrentTrack());

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
    			
        				if (rec != null)
        				{
        					rec.stop();
        					rec.release();
        				}

        			}});
        
        		
        
	}
	
	


}
