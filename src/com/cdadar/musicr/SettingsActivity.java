package com.cdadar.musicr;
import java.util.ArrayList;

import com.cdadar.musicr.work.*;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.media.AudioFormat;
import android.media.MediaRecorder.AudioSource;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SettingsActivity extends Activity{
	
	public void onCreate(Bundle savedInstanceState) 
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        ((TextView) findViewById(R.id.txtsettingsproject)).setText("Project: " + P.currentProject().name());
        
        LinearLayout layout = (LinearLayout) findViewById(R.id.tracklist);
        
        ArrayList<String> tlist = P.currentProject().getTrackList();
        
        for(int i = 0; i< tlist.size();i++)
        {
        	String track = tlist.get(i);
        	CheckBox c = new CheckBox(layout.getContext());
        	//TextView v =  new TextView(layout.getContext());
        	c.setText(track);
        	layout.addView(c);
        }
        
        
        findViewById(R.id.newtrack).setOnClickListener(
        		new View.OnClickListener() {
        			public void onClick(View v) {
        				Project p = P.currentProject();
        				p.createNewTrack("track2");
        				

        			}});
	}


}
