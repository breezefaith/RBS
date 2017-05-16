package sqlitedemo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UpdateDataDemo {

	public static void main(String[] args) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet=null;
		String url="JDBC:sqlite:src/sqlite/rbs.db";
		String sql="";
		try {
			Class.forName("org.sqlite.JDBC");
			connection=DriverManager.getConnection(url);
			
			sql="update reservation set table_id=1 where oid=9";
			preparedStatement=connection.prepareStatement(sql);
			preparedStatement.executeUpdate();
			
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}finally {
			try {
				if(resultSet!=null){
					resultSet.close();
				}
				if(preparedStatement!=null){
					preparedStatement.close();
				}
				if(connection!=null){
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}
	}

}
