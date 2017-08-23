package org.leolo.invitebot2.db.mysql;

import java.util.List;
import java.util.Vector;
import java.sql.*;

import javax.sql.DataSource;

import org.leolo.invitebot2.model.CommandAlias;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommandAliasDao implements org.leolo.invitebot2.db.CommandAliasDao {
	
	Logger logger = LoggerFactory.getLogger(CommandAliasDao.class);
	private DataSource ds;
	
	public CommandAliasDao(DataSource ds){
		this.ds = ds;
	}
	
	@Override
	public List<CommandAlias> getAll(){
		Vector<CommandAlias> list = new Vector<>();
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		try{
			conn = ds.getConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery("SELECT "
					+ "searchString,replaceString,aliasMode "
					+ "FROM alias "
					+ "ORDER BY processOrder DESC, aliasSequence");
			while(rs.next()){
				list.addElement(CommandAlias.getAlias(rs.getString(1), rs.getString(2), rs.getInt(3)));
			}
		}catch(SQLException sqle){
			logger.error(sqle.getMessage(), sqle);
			return null;
		}finally{
			try{
				if(rs!=null) rs.close();
				if(stmt!=null) stmt.close();
				if(conn!=null) conn.close();
			}catch(SQLException sqle){
				logger.error(sqle.getMessage(), sqle);
			}
		}
		return list;
	}
	
}
