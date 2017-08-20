package org.leolo.invitebot2;

import org.pircbotx.PircBotX;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;
import org.pircbotx.hooks.events.PrivateMessageEvent;

public class Console extends ListenerAdapter{
	
	public void onMessage(MessageEvent event) throws Exception {
		
	}
	public void onPrivateMessage(PrivateMessageEvent event) throws Exception {
		handleLine(event.getMessage(),event.getBot());
	}
	
	private void handleLine(String line, PircBotX bot){
		
	}
}
