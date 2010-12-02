package com.cdadar.musicr;

import com.cdadar.musicr.work.*;


import java.io.IOException;

import com.cdadar.musicr.work.P;

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

public class WelcomeActivity extends Activity {

	private View.OnClickListener lastListener = new View.OnClickListener() {
		public void onClick(View v) {
		
		}
	};
	
	private View.OnClickListener createListener = new View.OnClickListener() {
		public void onClick(View v) {
            Intent myIntent = new Intent(v.getContext(), CreateMusicActivity.class);
            startActivityForResult(myIntent, 0);
		}
	};
	
	private View.OnClickListener selectListener = new View.OnClickListener() {
		public void onClick(View v) {
		    Intent myIntent = new Intent(v.getContext(), OpenActivity.class);
		    startActivityForResult(myIntent, 0);
		}
		    
	};
	
	/** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState) {
    
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);
        Button last = (Button) findViewById(R.id.last);
      
    	findViewById(R.id.last).setOnClickListener(lastListener);
    	findViewById(R.id.create).setOnClickListener(createListener);
    	findViewById(R.id.select ).setOnClickListener(selectListener);
    }
    
}
