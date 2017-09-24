package org.leolo.invitebot2;

import java.util.Properties;

import org.leolo.invitebot2.annotation.ConsoleCommand;
import org.leolo.invitebot2.db.CommandAliasDao;
import org.leolo.invitebot2.db.DBManager;
import org.leolo.invitebot2.util.Glob;
import org.leolo.invitebot2.util.StringUtil;
import org.pircbotx.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConsoleCommandProvider {
	private Properties prop;
	
	Logger logger = LoggerFactory.getLogger(ConsoleCommandProvider.class);
	
	public ConsoleCommandProvider(Properties prop){
		this.prop = prop;
	}
	
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
		if(StringUtil.toEmptyIfNull(prop.getProperty("admin")).equals(UserAccountChecker.getInstance().getLoggedInAs(source.getNick()))){
			DBManager dbMan = DBManager.getInstance();
			CommandAliasDao cadao = dbMan.getCommandAliasDao();
			long startTime = System.currentTimeMillis();
			Console.commandAlias = cadao.getAll();
			ccr.println("Alias reloaded, new size is "+Console.commandAlias.size()+
					", time spent is "+(System.currentTimeMillis()-startTime)+"ms");
		}
		return ccr;
	}
	
	@ConsoleCommand(pmOnly=false, name = "match")
	public ConsoleCommandResult match(String line, User source){
		Glob glob = new Glob();
		ConsoleCommandResult ccr = new ConsoleCommandResult();
		try{
			glob.parse(line.substring(6));
			if(glob.match(source)){
				ccr.println("Matches hostmask "+line.substring(6));
			}else{
				ccr.println("Does not match hostmask "+line.substring(6));
			}
		}catch(RuntimeException re){
			ccr.println("Unable to parse hostmask "+line.substring(6));
		}
		
		return ccr;
	}
	
	
}
