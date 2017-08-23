package org.leolo.invitebot2;

import org.leolo.invitebot2.annotation.ConsoleCommand;
import org.leolo.invitebot2.db.CommandAliasDao;
import org.leolo.invitebot2.db.DBManager;
import org.pircbotx.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConsoleCommandProvider {
	Logger logger = LoggerFactory.getLogger(ConsoleCommandProvider.class);
	
	@ConsoleCommand(pmOnly = false, name = "ping")
	public ConsoleCommandResult ping(String line, User source){
		ConsoleCommandResult ccr = new ConsoleCommandResult();
		ccr.println("pong");
		return ccr;
	}
	
	@ConsoleCommand(pmOnly = false, name = "echo")
	public ConsoleCommandResult echo(String line, User source){
		ConsoleCommandResult ccr = new ConsoleCommandResult();
		ccr.println(line.substring(5));
		return ccr;
	}
	
	@ConsoleCommand(pmOnly = false, name = "aliasreload")
	public ConsoleCommandResult reloadAlias(String line, User source){
		ConsoleCommandResult ccr = new ConsoleCommandResult();
		DBManager dbMan = DBManager.getInstance();
		CommandAliasDao cadao = dbMan.getCommandAliasDao();
		Console.commandAlias = cadao.getAll();
		ccr.println("Alias reloaded");
		logger.info(Console.commandAlias.toString());
		return ccr;
	}
	
	
}
