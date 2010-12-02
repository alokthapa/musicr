package com.cdadar.musicr;
import java.util.ArrayList;
import java.util.UUID;

import com.cdadar.musicr.work.*;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.AudioFormat;
import android.media.MediaRecorder.AudioSource;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.*;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class SettingsActivity extends MusicrActivity{
	
	TableLayout tlayout = null;
	
	public void addNewTrack(Track track)
	{
        TableRow row = new TableRow(tlayout.getContext());
        
        row.setOnLongClickListener(
        		new View.OnLongClickListener() {
					
					@Override
					public boolean onLongClick(View v) {
						// TODO Auto-generated method stub
						Toast.makeText(v.getContext(), "longpressed!!", Toast.LENGTH_SHORT).show();

						return false;
					}
				});
        
        ToggleButton rec = new ToggleButton(row.getContext());
        rec.setText("rec");
        rec.setTextOn("rec");
        rec.setTextOff("rec");
        rec.setTag(track);

        rec.setChecked(track.isRecording());
        rec.setOnCheckedChangeListener(
        		new OnCheckedChangeListener(){
					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						Track t = (Track)buttonView.getTag();
						getCurrentProject().getTrackList().selectTrack(t.getName());
						redraw();
					}
        });
        TextView vv = new TextView(row.getContext());
        vv.setText(track.getName());

        ToggleButton s = new ToggleButton(row.getContext());
        s.setText("s");
        s.setTextOn("s");
        s.setTextOff("s");
        s.setChecked(track.isSolo());
        s.setTag(track);
        s.setOnCheckedChangeListener(
        		new OnCheckedChangeListener(){

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						// TODO Auto-generated method stub
						Track t = (Track) buttonView.getTag();
						t.setSolo(isChecked);
					}
        			
        		});
        
        ToggleButton m = new ToggleButton(row.getContext());
        m.setText("m");
        m.setTextOn("m");
        m.setTextOff("m");
        m.setChecked(track.isMuted());
        m.setTag(track);
        m.setOnCheckedChangeListener(
        		new OnCheckedChangeListener(){

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						Track t = (Track) buttonView.getTag();
						t.setMute(isChecked);
						
						// TODO Auto-generated method stub
						
					}
        			
        		});

        
        row.addView(rec);
        row.addView(vv);
        row.addView(s);
        row.addView(m);
        tlayout.addView(row);
		
	}
	
	public void redraw()
	{
		LinearLayout layout = (LinearLayout) findViewById(R.id.tracklist);
		layout.removeAllViews();
		showTracks();
		
	}

	public void showTracks()
	{
		LinearLayout layout = (LinearLayout) findViewById(R.id.tracklist);

	    ArrayList<Track> tlist = getCurrentProject().getTrackList().getTracks();
	        
        tlayout = new TableLayout(layout.getContext());
	    for(int i = 0; i< tlist.size();i++)
	    	addNewTrack(tlist.get(i));
        layout.addView(tlayout);

	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if ((keyCode == KeyEvent.KEYCODE_BACK)) {
	    Intent myIntent = new Intent(getApplicationContext(), RecordActivity.class);
	    	getCurrentProject().save();
            startActivityForResult(myIntent, 0);
	    }

	    return super.onKeyDown(keyCode, event);

	}
	

	public void onCreate(Bundle savedInstanceState) 
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        ((TextView) findViewById(R.id.txtsettingsproject)).setText("Project: " + getCurrentProject().name());
        
        showTracks();
        
        findViewById(R.id.newtrack).setOnClickListener(
        		new View.OnClickListener() {
        			public void onClick(View v) {
        				LinearLayout layout = (LinearLayout) findViewById(R.id.addtrack);
        				layout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, 150));
        				final EditText et = new EditText(v.getContext());
        				Button btn = new Button(v.getContext());
        				btn.setText("OK");

        				btn.setOnClickListener(new View.OnClickListener() {
							
							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								String str = et.getText().toString();
		        				
Project p = getCurrentProject();
		        				Track t = p.getTrackList().createNewTrack(str);
		        				addNewTrack(t);
								LinearLayout layout = (LinearLayout) findViewById(R.id.addtrack);
								layout.removeAllViews();
		        				layout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, 0));

							}
						});
        				
        				Button cancel = new Button(v.getContext());
        				cancel.setText("Cancel");
        				cancel.setOnClickListener(new View.OnClickListener() {
							
							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								LinearLayout layout = (LinearLayout) findViewById(R.id.addtrack);
								layout.removeAllViews();
		        				layout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, 0));

							}
						});
        				
        			        				
        				layout.addView(et);
        				layout.addView(btn);
        				layout.addView(cancel);
        				
        			}});
	}


}
