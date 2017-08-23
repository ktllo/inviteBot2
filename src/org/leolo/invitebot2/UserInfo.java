package org.leolo.invitebot2;

public class UserInfo{
	String nick;
	private String hostmask;
	String loggedInAs;
	long time;
	
	UserInfo(String nick, String hostmask, String loggedInAs){
		this.nick = nick;
		this.hostmask = hostmask;
		this.loggedInAs = loggedInAs;
		this.time = System.currentTimeMillis();
	}

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public String getHostmask() {
		return hostmask;
	}

	public void setHostmask(String hostmask) {
		this.hostmask = hostmask;
	}

	public String getLoggedInAs() {
		return loggedInAs;
	}

	public void setLoggedInAs(String loggedInAs) {
		this.loggedInAs = loggedInAs;
	}

	public long getTime() {
		return time;
	}

	public void renewTime(){
		time = System.currentTimeMillis();
	}
	
	public void invalidate(){
		time = 0;
	}
}