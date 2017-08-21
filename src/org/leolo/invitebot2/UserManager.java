package org.leolo.invitebot2;

public class UserManager {
	
	private static UserManager instance = null;
	
	public static synchronized UserManager getInstance(){
		if(instance == null){
			instance = new UserManager();
		}
		return instance;
	}
	
	private UserManager(){
		
	}
}
