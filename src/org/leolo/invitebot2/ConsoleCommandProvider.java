package org.leolo.invitebot2;

import org.leolo.invitebot2.annotation.ConsoleCommand;
import org.pircbotx.User;

public class ConsoleCommandProvider {

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
	
	
}
