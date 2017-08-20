package org.leolo.invitebot2.db;

import java.sql.SQLException;
import java.util.Properties;

import org.leolo.invitebot2.BotManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
 * This class should provide different version of DAO class
 *
 */
public abstract class DBManager {
	
	protected Logger logger = LoggerFactory.getLogger(DBManager.class);
	
	protected Properties properties;
	private boolean ready =  false;
	
	public void setProperties(Properties properties){
		this.properties = properties;
	}
	
	protected void ready(){
		ready = true;
	}
	
	public final boolean isReady(){
		return ready;
	}
	
	public abstract void init() throws SQLException;
	
	public abstract boolean testConnection() throws SQLException;
}
