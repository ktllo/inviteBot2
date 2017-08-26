package org.leolo.invitebot2;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Properties;

import org.leolo.invitebot2.annotation.ConsoleCommand;
import org.leolo.invitebot2.db.DBManager;
import org.leolo.invitebot2.model.CommandAlias;
import org.leolo.invitebot2.util.Helper;
import org.pircbotx.PircBotX;
import org.pircbotx.User;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.ListenerExceptionEvent;
import org.pircbotx.hooks.events.MessageEvent;
import org.pircbotx.hooks.events.PrivateMessageEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Console extends ListenerAdapter{
	
	private static final int CHANNEL = 1;
	private static final int PRIVATE = 2;
	Logger logger = LoggerFactory.getLogger(Console.class);
	private Properties prop;
	
	protected static List<CommandAlias> commandAlias;
	DBManager dbman;
	ConsoleCommandProvider ccp;
	
	public Console(DBManager dbman,Properties prop){
		this.dbman  = dbman;
		this.prop = prop;
		if(commandAlias == null)
			commandAlias = dbman.getCommandAliasDao().getAll();
		ccp = new ConsoleCommandProvider(prop);
	}
	 
	public void onMessage(MessageEvent event) throws Exception {
		String escapeChar = prop.getProperty("escape","!");
		String line = event.getMessage();
		if(line.startsWith(escapeChar)){
			handleLine(line.substring(escapeChar.length()),event.getBot(), event.getUser(), event.getChannel().getName(), CHANNEL);
		}else if(Helper.toLowerCase(line).startsWith(
				Helper.toLowerCase(event.getBot().getNick())
			)){
			line = line.substring(event.getBot().getNick().length());
			while(line.startsWith(" ") || line.startsWith(":") || line.startsWith(",")){
				line = line.substring(1);
			}
			handleLine(line,event.getBot(), event.getUser(), event.getChannel().getName(), CHANNEL);
		}
	}
	public void onPrivateMessage(PrivateMessageEvent event) throws Exception {
		handleLine(event.getMessage(),event.getBot(), event.getUser(), null,  PRIVATE);
	}
	
	private String preprocessLine(String line){
		for(CommandAlias as:commandAlias){
			logger.info("Checking {}, result {}",as, as.isMatch(line));
			if(as.isMatch(line)){
				return as.match(line);
			}
		}
		return line.trim();
	}
	
	private void handleLine(String line, PircBotX bot, User sender, String channel,  final int SOURCE){
		logger.info("Handling {}", line);
		line = preprocessLine(line);
		logger.info("Handling {}", line);
		Method [] methods = ccp.getClass().getDeclaredMethods();
		try {
			for(Method method:methods){
//				logger.debug("Dealing with method "+method.getName());
				if(method.isAnnotationPresent(ConsoleCommand.class)){
					ConsoleCommand cc = (ConsoleCommand)method.getAnnotation(ConsoleCommand.class);
					if(line.toLowerCase().startsWith(cc.name().toLowerCase())){
						ConsoleCommandResult ccr = (ConsoleCommandResult)method.invoke(ccp, line, sender);
						ccr.doneOutput();
						if(SOURCE ==PRIVATE || ccr.isPmOnly()){
							String outputLine = ccr.nextLine();
							while(outputLine != null){
								bot.send().message(sender.getNick(), outputLine);
								outputLine = ccr.nextLine();
							}
						}else{
							String outputLine = ccr.nextLine();
							while(outputLine != null){
								bot.send().message(channel , sender.getNick()+": "+outputLine);
								outputLine = ccr.nextLine();
							}
						}
						return;
					}
				}
			}
		} catch (IllegalAccessException e) {
			logger.error(e.getMessage(), e);
		} catch (IllegalArgumentException e) {
			logger.error(e.getMessage(), e);
		} catch (InvocationTargetException e) {
			logger.error(e.getMessage(), e);
		}
	}
	
	@Override
	public void onListenerException(ListenerExceptionEvent event) throws Exception {
		logger.error(event.getException().getMessage(), event.getException());
	}
}
