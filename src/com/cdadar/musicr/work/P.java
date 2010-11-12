package com.cdadar.musicr.work;

public class P {
	
	private static Project currentProject = null;
	public static Project currentProject(){
		return currentProject;
	}
	
	public static void setCurrentProject(Project p)
	{
		currentProject = p;
	}

}
