package org.leolo.invitebot2.db.mysql;

import javax.sql.DataSource;

public class CommandAliasDao implements org.leolo.invitebot2.db.CommandAliasDao {
	
	private DataSource ds;
	
	public CommandAliasDao(DataSource ds){
		this.ds = ds;
	}
	
}
