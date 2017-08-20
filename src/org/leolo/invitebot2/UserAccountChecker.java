package org.leolo.invitebot2;

import java.util.HashMap;
import java.util.Random;
import java.util.Set;

import org.pircbotx.Channel;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.JoinEvent;
import org.pircbotx.hooks.events.NickChangeEvent;
import org.pircbotx.hooks.events.PartEvent;
import org.pircbotx.hooks.events.QuitEvent;
import org.pircbotx.hooks.events.WhoisEvent;
import org.pircbotx.hooks.types.GenericMessageEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserAccountChecker extends ListenerAdapter {
	Logger logger = LoggerFactory.getLogger(UserAccountChecker.class);
	
	private HashMap<String, UserInfo> userList = new HashMap<>();
	private static final long MAX_VALID_TIME = 1_800_000;//Unit is ms
	private static final long RETRY_TIME = 1_800_000;//Unit is ms
	
	
	@Override
	public void onJoin(JoinEvent event){
		UserInfo ui = userList.get(event.getUser().getNick());
		if(ui!=null){
			if(ui.loggedInAs !=null){
				//Already have info
				if(System.currentTimeMillis()-ui.time< MAX_VALID_TIME){
					return;
				}
			}else{
				//No information yet
				if(System.currentTimeMillis()-ui.time< RETRY_TIME){
					return;
				}
			}
		}
		event.getBot().send().whois(event.getUser().getNick());
	}
	
	@Override
	public void onWhois(WhoisEvent event) throws Exception {
		logger.debug("Recevied WHOIS information from {} for {}", event.getServer(), event.getNick());
		UserInfo ui = new UserInfo(event.getNick(), event.getNick()+"!"+event.getLogin()+"@"+event.getHostname(), event.getRegisteredAs());
		userList.put(event.getNick(), ui);
	}
	
	public void onNickChange(NickChangeEvent event) throws Exception {
		String oldNick = event.getOldNick();
		String newNick = event.getNewNick();
		UserInfo ui = userList.get(oldNick);
		userList.put(oldNick, null);
		if(ui!=null){
			if(ui.loggedInAs !=null){
				//Already has information
				if(System.currentTimeMillis()-ui.time< MAX_VALID_TIME){
					ui.nick = newNick;
					userList.put(newNick, ui);
					return;
				}
			}
		}
		event.getBot().send().whois(newNick);
	}
	
	@Override
	public void onQuit(QuitEvent event) throws Exception {
		logger.info("User {} quits", event.getUser().getNick());
		userList.put(event.getUser().getNick(), null);
	}
	 
	@Override
	public void onPart(PartEvent event) throws Exception {
		logger.info("onPart Called");
		Set<Channel> chSet = event.getUser().getChannels();
		for(Channel ch:chSet){
			if(ch.getName().equals(event.getChannel().getName())){
				continue;
			}
			if(ch.getUsersNicks().contains(event.getBot().getNick())){
				return;
			}
		}
		logger.info("User {} part from all common channels", event.getUser().getNick());
		userList.put(event.getUser().getNick(), null);
	}
	public void onGenericMessage(GenericMessageEvent event) throws Exception {
		logger.error(event.getClass().getCanonicalName());
	}
	class UserInfo{
		private String nick;
		private String hostmask;
		private String loggedInAs;
		private long time;
		
		private UserInfo(String nick, String hostmask, String loggedInAs){
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
}
