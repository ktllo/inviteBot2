package org.leolo.invitebot2.db.mysql;

import java.sql.*;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;

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
		bds.setMaxTotal(1024);
		bds.setMaxIdle(300);
		ds = bds;
		if(testConnection())
			this.ready();
	}

	@Override
	public boolean testConnection() throws SQLException {
		Connection conn = ds.getConnection();
		logger.debug("Connection is type {}", conn.getClass().getCanonicalName());
		PreparedStatement pstmt = conn.prepareStatement("SELECT NOW(), MD5(?) FROM dual;");
		pstmt.setString(1, "TestConnection");
		ResultSet rs = pstmt.executeQuery();
		if(rs.next()){
			logger.info("Time is {}, hash is {}", rs.getString(1), rs.getString(2));
			return true;
		}
		return false;
	}

}
