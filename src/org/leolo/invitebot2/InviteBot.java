package org.leolo.invitebot2;




import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.JoinEvent;
import org.pircbotx.hooks.events.WhoisEvent;
import org.pircbotx.hooks.types.GenericChannelEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InviteBot extends ListenerAdapter {
	Logger logger = LoggerFactory.getLogger(InviteBot.class);

	public void onJoin(JoinEvent event) throws Exception {
		event.getBot().send().whois(event.getUser().getNick());
	}
	public void onGenericChannel(GenericChannelEvent event) throws Exception {
//		System.out.println(event.);
	}
	
	public void onWhois(WhoisEvent event) throws Exception {
		if(event.getRegisteredAs() != null){
			logger.info("User {} is logged in as {}",event.getNick(),event.getRegisteredAs());
		}else{
			logger.info("User {} is not logged in",event.getNick());
		}
	}
}
