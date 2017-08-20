package org.leolo.invitebot2;

import java.io.IOException;

import org.pircbotx.Configuration;
import org.pircbotx.PircBotX;
import org.pircbotx.cap.EnableCapHandler;
import org.pircbotx.exception.IrcException;

public class BotManager {
	
	public static void main(String [] args) throws IOException, IrcException{
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
