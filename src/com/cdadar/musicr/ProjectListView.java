
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
import android.widget.Toast;
import android.os.PowerManager;
import android.content.Context;
import android.app.ListActivity;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.ListView;

import com.cdadar.musicr.work.*;

public class ProjectListView extends ListActivity
{

    public void onCreate(Bundle savedInstanceState)
    {
	super.onCreate(savedInstanceState);
	
	setListAdapter(new ArrayAdapter<String>(this, R.layout.project_item, 
						Project.getProjects()));
	ListView lv = getListView();
	lv.setTextFilterEnabled(true);
	
	lv.setOnItemClickListener (new AdapterView.OnItemClickListener(){
		public void onItemClick(AdapterView<?> parent, View view,
					int position, long id)
		{
		    Toast.makeText(getApplicationContext(),
				   ((TextView) view).getText(),
				    Toast.LENGTH_SHORT).show();
		}});
    }
}
