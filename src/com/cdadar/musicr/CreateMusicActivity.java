package com.cdadar.musicr;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.cdadar.musicr.work.*;

public class CreateMusicActivity extends MusicrActivity {
	
	private View.OnClickListener btnnewListener = new View.OnClickListener() {
		
		public void onClick(View v) {
			String projectname = ((EditText) findViewById(R.id.filename)).getText().toString();

			try{
				Project p = Project.CreateProject(projectname);
				 Intent myIntent = new Intent(v.getContext(), RecordActivity.class);
				 saveAsCurrentProject(p);
				 startActivityForResult(myIntent, 0);
			}
			catch(Exception e)
			{
				new AlertDialog.Builder(v.getContext())
			      .setMessage(e.toString())
			      .show();
			}
		}
	};
	
	private View.OnClickListener btncreatecancelListener = new View.OnClickListener() {
		public void onClick(View v) {
            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            finish();
		}
	};
	
	
	/** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState) {
    
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create);

    	findViewById(R.id.btnnewfile ).setOnClickListener(btnnewListener);
    	findViewById(R.id.btncreatecancel).setOnClickListener(btncreatecancelListener);

    }
}
