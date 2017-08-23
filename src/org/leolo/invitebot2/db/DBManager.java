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
	
	private static DBManager instance = null;

	private static DBManager instace;
	
	public static synchronized DBManager createInstance(String className, Properties prop) throws InstantiationException, IllegalAccessException, ClassNotFoundException{
		Object obj = Class.forName(className).newInstance();
		if(obj instanceof DBManager){
			instance = (DBManager) obj;
			instance.properties = prop;
			try{
				instance.init();
			}catch(SQLException sqle){
				throw new RuntimeException("Cannot initizatize database connection", sqle);
			}
			if(!instance.ready){
				throw new RuntimeException("Cannot initizatize database connection");
			}
		}else{
			throw new ClassCastException();
		}
		return instance;
	}
	
	public static DBManager getInstance(){
		return instace;
	}
	
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
	
	public abstract ConfigDao getConfigDao();
}
