package org.leolo.invitebot2;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import org.leolo.invitebot2.annotation.ConsoleCommand;
import org.leolo.invitebot2.db.DBManager;
import org.leolo.invitebot2.model.CommandAlias;
import org.pircbotx.PircBotX;
import org.pircbotx.User;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;
import org.pircbotx.hooks.events.PrivateMessageEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Console extends ListenerAdapter{
	
	private static final int CHANNEL = 1;
	private static final int PRIVATE = 2;
	Logger logger = LoggerFactory.getLogger(Console.class);
	
	List<CommandAlias> commandAlias;
	DBManager dbman;
	ConsoleCommandProvider ccp = new ConsoleCommandProvider();
	
	public Console(DBManager dbman){
		this.dbman  = dbman;
		commandAlias = dbman.getCommandAliasDao().getAll();
	}
	 
	public void onMessage(MessageEvent event) throws Exception {
//		handleLine(event.getMessage(),event.getBot(), event.getUser(), CHANNEL);
	}
	public void onPrivateMessage(PrivateMessageEvent event) throws Exception {
		handleLine(event.getMessage(),event.getBot(), event.getUser(), PRIVATE);
	}
	
	private String preprocessLine(String line){
		for(CommandAlias as:commandAlias){
			if(as.isMatch(line)){
				return as.match(line);
			}
		}
		return line;
	}
	
	private void handleLine(String line, PircBotX bot, User sender, final int source){
		Method [] methods = ccp.getClass().getDeclaredMethods();
		try {
			for(Method method:methods){
//				logger.debug("Dealing with method");
				if(method.isAnnotationPresent(ConsoleCommand.class)){
					ConsoleCommand cc = (ConsoleCommand)method.getAnnotation(ConsoleCommand.class);
					if(line.toLowerCase().startsWith(cc.name().toLowerCase())){
						
							Object obj = method.invoke(ccp, line);
							
							logger.info("Command result {}",obj);
					}
				}
			}
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
