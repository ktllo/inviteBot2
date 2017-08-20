package org.leolo.invitebot2;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;

import org.leolo.invitebot2.db.DBManager;
import org.pircbotx.Configuration;
import org.pircbotx.PircBotX;
import org.pircbotx.cap.EnableCapHandler;
import org.pircbotx.exception.IrcException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BotManager {
	
	Logger logger = LoggerFactory.getLogger(BotManager.class);
	
	public static void main(String [] args) throws IOException, IrcException{
		Logger logger = LoggerFactory.getLogger(BotManager.class);
		Properties prop = new Properties();
		prop.load(new java.io.FileInputStream("settings.properties"));
		DBManager dbMan = null;
		if(prop.containsKey("dbmanager")){
			try {
				dbMan = DBManager.createInstance(prop.getProperty("dbmanager"), prop);
			} catch (InstantiationException e) {
				logger.error("Cannot load dbmanager class name. STOP");
				System.exit(ErrorCode.CONFIG_ERROR);
			} catch (IllegalAccessException e) {
				logger.error("Cannot load dbmanager class name. STOP");
				System.exit(ErrorCode.CONFIG_ERROR);
			} catch (ClassNotFoundException e) {
				logger.error("Cannot load dbmanager class name. STOP");
				System.exit(ErrorCode.CONFIG_ERROR);
			} catch (RuntimeException e) {
				logger.error(e.getMessage());
				System.exit(ErrorCode.DB_INIT_ERROR);
			}
		}else{
			logger.error("Cannot load dbmanager class name. STOP");
			System.exit(ErrorCode.CONFIG_ERROR);
		}
		Configuration configuration = new Configuration.Builder()
                .setName("lionbot") //Set the nick of the bot. CHANGE IN YOUR CODE
                .addServer("irc.freenode.net") //Join the freenode network
                .addAutoJoinChannel("##ktllo")
                .addAutoJoinChannel("##invitebot")
                .addCapHandler(new EnableCapHandler("extended-join",false))
//                .addListener(new InviteBot())
                .addListener(UserAccountChecker.getInstance())
                .setOnJoinWhoEnabled(true)
                .buildConfiguration();

		//Create our bot with the configuration
		PircBotX bot = new PircBotX(configuration);
		//Connect to the server
		bot.startBot();
	}
	
}
