package sqlitedemo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

public class Test {

	public static void main(String[] args) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet=null;
		String sql;
		@SuppressWarnings("unused")
		SimpleDateFormat timeSimpleDateFormat=new SimpleDateFormat("HH:mm:ss");
		try {
			Class.forName("org.sqlite.JDBC");//加载SQLite的JDBC驱动
			connection=DriverManager.getConnection("jdbc:sqlite:src/sqlite/rbs.db");//建立连接
//			preparedStatement=connection.prepareStatement("delete from tables where oid>4");
//			preparedStatement.executeUpdate();
			
//			sql="select table_id,state from reservation,reservationstate where reservation.oid=reservationstate.reservation_id";
			sql="select name, phoneNumber from customer "
					+ "where oid in (select customer_id "
					+ "from reservation "
					+ "where oid=1)";
			preparedStatement=connection.prepareStatement(sql);
//			preparedStatement.setString(1, dateSimpleDateFormat.format(currentDate));
			System.out.println("查询开始");
			int count=0;
			resultSet=preparedStatement.executeQuery();
			while(resultSet.next()){
				String name=resultSet.getString(1);
				String phone=resultSet.getString(2);
				System.out.println(name + "\t" +phone);
				count++;
			}
			System.out.println("查询结束,共"+count+"条结果");
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}

}
