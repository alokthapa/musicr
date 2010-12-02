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
import android.content.*;
import com.cdadar.musicr.work.*;

public class MusicrActivity extends Activity 
{
    public static final String PREFS_NAME = "MusicrPrefsFile";

    private static Project currentProject = null;

    protected Project getCurrentProject()
    {
	if( currentProject == null)
	    {
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		String projectName = settings.getString("proj", "");
		MusicrActivity.currentProject = new Project(projectName);
	    }
	return currentProject;
    }
    
    protected void saveAsCurrentProject(Project p)
    {
	
	SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
	SharedPreferences.Editor editor = settings.edit();
	editor.putString("proj", p.name());
    }
}


