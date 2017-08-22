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
	private static final long RETRY_TIME = 300_000;//Unit is ms
	private static final int WHOIS_WAIT_TIME = 2_000;//Unit is ms
	private static UserAccountChecker instance = null;
	
	//Avoid being instanceized
	private UserAccountChecker(){
		
	}
	
	public static synchronized UserAccountChecker getInstance(){
		if(instance == null)
			instance = new UserAccountChecker();
		return instance;
	}
	
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
	
	public String getLoggedInAs(String nickname){
		return getLoggedInAs(nickname, false);
	}
	
	private String getLoggedInAs(String nickname,boolean asked){
		UserInfo ui = userList.get(nickname);
		if(ui == null){
			if(!asked){
				BotManager.bot.send().whois(nickname);
				try {
					Thread.sleep(WHOIS_WAIT_TIME);
				} catch (InterruptedException e) {
					logger.error(e.getMessage(), e);
				}
				return getLoggedInAs(nickname, true);
			}else{
				return null;
			}
		}
		return ui.loggedInAs;
	}
	
	
}
