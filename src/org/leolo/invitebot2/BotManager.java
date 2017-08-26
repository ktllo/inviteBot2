package org.leolo.invitebot2;

import java.io.IOException;
import java.nio.charset.Charset;
import java.sql.SQLException;
import java.util.Properties;

import org.leolo.invitebot2.db.DBManager;
import org.pircbotx.Configuration;
import org.pircbotx.PircBotX;
import org.pircbotx.UtilSSLSocketFactory;
import org.pircbotx.cap.EnableCapHandler;
import org.pircbotx.exception.IrcException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BotManager {
	
	Logger logger = LoggerFactory.getLogger(BotManager.class);
	
	public static PircBotX bot;
	
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
		Configuration.Builder b = new Configuration.Builder()
                .setName(prop.getProperty("nick", "invitebot2")) //Set the nick of the bot. CHANGE IN YOUR CODE
                .addAutoJoinChannel("##ktllo")
                .addAutoJoinChannel("##invitebot")
                .addCapHandler(new EnableCapHandler("extended-join",false))
                .addListener(UserAccountChecker.getInstance())
                .addListener(new Console(dbMan, prop))
                .setOnJoinWhoEnabled(true)
                .setEncoding(Charset.forName("utf-8"))
                .setRealName(prop.getProperty("fullname", "invitebot2, new version of invitebot"));
		if(prop.getProperty("ssl","false").equals("true")){
			if(prop.getProperty("ssl.accept_all_cert","false").equals("true")){
				b=b.setSocketFactory(new UtilSSLSocketFactory().disableDiffieHellman().trustAllCertificates());
			}else{
				b=b.setSocketFactory(new UtilSSLSocketFactory().disableDiffieHellman());
			}
			b = b.addServer(prop.getProperty("server"),Integer.parseInt(prop.getProperty("port", "6697")));
		}else{
			b = b.addServer(prop.getProperty("server"),Integer.parseInt(prop.getProperty("port", "6667")));
		}
        String auth = prop.getProperty("auth");
        if("nickserv".equals(auth)){
        	b =  b.setNickservNick(prop.getProperty("user"))
        			.setNickservPassword(prop.getProperty("password"));
        }else if("sasl".equals(auth)){
        	b = b.addCapHandler(new org.pircbotx.cap.SASLCapHandler(prop.getProperty("user"), prop.getProperty("password")));
        }else if("server".equals(auth)){
        	b = b.setServerPassword(prop.getProperty("password"));
        }
        Configuration configuration =  b.buildConfiguration();
        
		//Create our bot with the configuration
		bot = new PircBotX(configuration);
		//Connect to the server
		bot.startBot();
		
//		bot.sendRaw().rawLine("ns :IDENTIFY "+prop.getProperty("user")+" "+prop.getProperty("password"));
	}
	
}
