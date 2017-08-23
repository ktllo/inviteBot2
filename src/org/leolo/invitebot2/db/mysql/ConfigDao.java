package org.leolo.invitebot2.db.mysql;

import javax.sql.DataSource;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

import org.leolo.invitebot2.model.Config;

public class ConfigDao implements org.leolo.invitebot2.db.ConfigDao{
	DataSource ds;
	
	ConfigDao(DataSource ds){
		this.ds = ds;
	}

	@Override
	public Config getConfig(String key) throws SQLException{
		Config config = null;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "SELECT `key`,`value` FROM `config` WHERE `key` = ?";
		try{
			conn = ds.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, key);
			rs = pstmt.executeQuery();
			if(rs.next()){
				config = new Config(rs.getString(1), rs.getString(2));
			}
		}catch(SQLException sqle){
			throw sqle;
		}finally{
			if(rs!=null){
				rs.close();
			}
			if(pstmt!=null){
				pstmt.close();
			}
			if(conn!=null){
				conn.close();
			}
		}
		return config;
	}

	@Override
	public void updateConfig(Config config) throws SQLException {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = "UPDATE config SET `key`=?,`value`=? WHERE `key`=?";
		try{
			conn = ds.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, config.getKey());
			pstmt.setString(2, config.getValue());
			pstmt.setString(3, config.getSnapshot().getKey());
			pstmt.executeUpdate();
			conn.commit();
		}catch(SQLException sqle){
			conn.rollback();
			throw sqle;
		}finally{
			if(pstmt!=null){
				pstmt.close();
			}
			if(conn!=null){
				conn.close();
			}
		}
	}

	@Override
	public Map<String, Config> getAll() throws SQLException {
		Map<String, Config> map = new HashMap<>();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "SELECT `key`,`value` FROM `config`";
		try{
			conn = ds.getConnection();
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while(rs.next()){
				Config config = new Config(rs.getString(1), rs.getString(2));
				map.put(rs.getString(1), config);
			}
		}catch(SQLException sqle){
			throw sqle;
		}finally{
			if(rs!=null){
				rs.close();
			}
			if(pstmt!=null){
				pstmt.close();
			}
			if(conn!=null){
				conn.close();
			}
		}
		return map;
	}
}
