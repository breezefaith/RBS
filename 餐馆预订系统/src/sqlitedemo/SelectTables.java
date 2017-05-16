package sqlitedemo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SelectTables {

	@SuppressWarnings("resource")
	public static void main(String[] args) {
		Connection connection=null;
		String url="jdbc:sqlite:src/sqlite/rbs.db";
		ResultSet resultSet=null;
		PreparedStatement preparedStatement=null;
		try {
			Class.forName("org.sqlite.JDBC");
			connection=DriverManager.getConnection(url);
			
			String sql="select * from Tables";
			preparedStatement=connection.prepareStatement(sql);
			resultSet=preparedStatement.executeQuery();
			while(resultSet.next()){
				System.out.println(resultSet.getInt("oid")+"\t"
						+resultSet.getInt("number")+"\t"
						+resultSet.getInt("places")
					);
			}
			System.out.println("");
			
			sql="select * from Customer";
			preparedStatement=connection.prepareStatement(sql);
			resultSet=preparedStatement.executeQuery();
			while(resultSet.next()){
				System.out.println(resultSet.getInt("oid")+"\t"
						+resultSet.getString("name")+"\t"
						+resultSet.getString("phoneNumber")
					);
			}
			System.out.println("");
			
			sql="select * from Reservation";
			preparedStatement=connection.prepareStatement(sql);
			resultSet=preparedStatement.executeQuery();
			while(resultSet.next()){
				System.out.println(resultSet.getInt("oid")+"\t"
						+resultSet.getInt("covers")+"\t"
						+resultSet.getString("date")+"\t"
						+resultSet.getString("time")+"\t"
						+resultSet.getInt("table_id")+"\t"
						+resultSet.getInt("customer_id")
					);
			}
			System.out.println("");
			
			sql="select * from reservationstate";
			preparedStatement=connection.prepareStatement(sql);
			resultSet=preparedStatement.executeQuery();
			while(resultSet.next()){
				System.out.println(resultSet.getInt("oid")+"\t"
						+resultSet.getInt("reservation_id")+"\t"
						+resultSet.getInt("type")+"\t"
						+resultSet.getInt("state")
					);
			}
			System.out.println("");
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
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
		}
	}

}
