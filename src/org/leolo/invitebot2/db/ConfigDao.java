package org.leolo.invitebot2.db;

import java.sql.SQLException;

import org.leolo.invitebot2.model.Config;

public interface ConfigDao {
	
	Config getConfig(String key) throws SQLException;
	
	void updateConfig(Config config) throws SQLException;
}
