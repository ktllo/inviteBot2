package org.leolo.invitebot2.db.mysql;

import java.sql.*;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.leolo.invitebot2.db.CommandAliasDao;
import org.leolo.invitebot2.db.ConfigDao;

public class DBManager extends org.leolo.invitebot2.db.DBManager{
	
	DataSource ds = null;
	
	@Override
	public void init() throws SQLException{
		logger.info("init() called. DBMan={}", this.properties.getProperty("dbmanager"));
		BasicDataSource bds = new BasicDataSource();
		bds.setDriverClassName("com.mysql.jdbc.Driver");
		bds.setUrl("jdbc:mysql://"+properties.getProperty("dbpath", "localhost")+
				"/"+properties.getProperty("dbname", "invitebot")+"?useSSL=false");
		bds.setUsername(properties.getProperty("dbuser", "root"));
		bds.setPassword(properties.getProperty("dbpass", ""));
		bds.setDefaultAutoCommit(false);
		bds.setMaxTotal(1024);
		bds.setMaxIdle(300);
		ds = bds;
		if(testConnection())
			this.ready();
	}

	@Override
	public boolean testConnection() throws SQLException {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		try{
			conn = ds.getConnection();
			stmt= conn.createStatement();
			rs = stmt.executeQuery("SELECT NOW()");
			return rs.next();
		}catch(SQLException sqle){
			throw sqle;
		}finally{
			if(rs!=null){
				rs.close();
			}
			if(stmt!=null){
				stmt.close();
			}
			if(conn!=null){
				conn.close();
			}
		}
	}

	@Override
	public ConfigDao getConfigDao() {
		return new org.leolo.invitebot2.db.mysql.ConfigDao(ds);
	}

	@Override
	public CommandAliasDao getCommandAliasDao() {
		return new org.leolo.invitebot2.db.mysql.CommandAliasDao(ds);
	}

}
