package sqlitedemo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CreateTables {

	@SuppressWarnings("resource")
	public static void main(String[] args) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		try {
			Class.forName("org.sqlite.JDBC");//加载SQLite的JDBC驱动
			//创建Tables表
			connection=DriverManager.getConnection("jdbc:sqlite:src/sqlite/rbs.db");//建立连接
			String sql="create table Tables("
					+ "oid integer,"
					+ "number integer not null,"
					+ "places integer not null,"
					+ "primary key(oid))";
			preparedStatement=connection.prepareStatement(sql);//prepareStatement创建table
			preparedStatement.executeUpdate();
			//创建Customer表
			sql="create table Customer("
					+ "oid integer,"
					+ "name varchar(20) null,"
					+ "phoneNumber varchar(20) not null,"
					+ "primary key(oid))";
			preparedStatement=connection.prepareStatement(sql);
			preparedStatement.executeUpdate();
			
			//创建Reservation表
			sql="create table Reservation("
					+ "oid integer,"
					+ "covers integer not null,"
					+ "date string not null,"
					+ "time string not null,"
					+ "table_id integer not null,"
					+ "customer_id integer,"
					+ "primary key(oid),"
					+ "foreign key(table_id) references Tables(oid) on delete cascade,"
					+ "foreign key(customer_id) references Tables(oid) on delete cascade)";
			preparedStatement=connection.prepareStatement(sql);
			preparedStatement.executeUpdate();
			
			//创建订单状态表
			/*type值为1表示预约，2表示自达
			 * state值为1表示已预约，2表示已到达，3表示空闲或已离开
			 */
			sql="create table ReservationState("
					+ "oid integer primary key,"
					+ "reservation_id integer not null,"
					+ "type integer not null check(type in ('1','2')),"
					+ "state integer not null check(state in ('1','2','3')),"
					+ "foreign key(reservation_id) references Reservation(oid) on delete cascade"
					+ ")";
			preparedStatement=connection.prepareStatement(sql);
			preparedStatement.executeUpdate();
			
			//向Tables插入数据
			preparedStatement=connection.prepareStatement("insert into Tables(number,places) values(?,?),(?,?),(?,?)");
			preparedStatement.setInt(1, 1);
			preparedStatement.setInt(2, 8);
			preparedStatement.setInt(3, 2);
			preparedStatement.setInt(4, 12);
			preparedStatement.setInt(5, 3);
			preparedStatement.setInt(6, 16);
			preparedStatement.executeUpdate();
			//向Customer插入数据
			preparedStatement=connection.prepareStatement("insert into Customer(name,phoneNumber) values(?,?),(?,?),(?,?)");
			preparedStatement.setString(1, "zhang");
			preparedStatement.setString(2, "18251958027");
			preparedStatement.setString(3, "wei");
			preparedStatement.setString(4, "12345678910");
			preparedStatement.setString(5, "wang");
			preparedStatement.setString(6, "10987654321");
			preparedStatement.executeUpdate();
			//向Reservation插入数据
			preparedStatement=connection.prepareStatement("insert into Reservation(covers,date,time,table_id,customer_id) values(?,?,?,?,?),(?,?,?,?,?),(?,?,?,?,?),(?,?,?,?,?)");
			preparedStatement.setInt(1, 8);
			preparedStatement.setString(2, "2017-05-10");
			preparedStatement.setString(3, "12:00:00");
			preparedStatement.setInt(4, 1 );
			preparedStatement.setInt(5, 1);
			preparedStatement.setInt(6, 16);
			preparedStatement.setString(7, "2017-05-10");
			preparedStatement.setString(8, "18:00:00");
			preparedStatement.setInt(9, 3);
			preparedStatement.setInt(10, 1);
			preparedStatement.setInt(11, 12);
			preparedStatement.setString(12, "2017-05-10");
			preparedStatement.setString(13, "12:00:00");
			preparedStatement.setInt(14, 2);
			preparedStatement.setInt(15, 2);
			preparedStatement.setInt(16, 8);
			preparedStatement.setString(17, "2017-05-07");
			preparedStatement.setString(18, "12:00:00");
			preparedStatement.setInt(19, 1);
			preparedStatement.setInt(20, 3);
			preparedStatement.executeUpdate();
			
			//向ReservationState表中插入数据
			preparedStatement=connection.prepareStatement("insert into ReservationState(reservation_id,type,state) values (?,?,?),(?,?,?),(?,?,?),(?,?,?)");
			preparedStatement.setInt(1, 1);
			preparedStatement.setInt(2, 1);
			preparedStatement.setInt(3, 1);
			preparedStatement.setInt(4, 2);
			preparedStatement.setInt(5, 1);
			preparedStatement.setInt(6, 1);
			preparedStatement.setInt(7, 3);
			preparedStatement.setInt(8, 1);
			preparedStatement.setInt(9, 1);
			preparedStatement.setInt(10, 4);
			preparedStatement.setInt(11, 1);
			preparedStatement.setInt(12, 1);
			preparedStatement.executeUpdate();
			
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}finally {
			try {
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
