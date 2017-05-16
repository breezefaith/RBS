package uidemo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import sqlitedemo.Reservation;

public class ReservationTable extends VBox{
	private final ObservableList<Reservation> data=FXCollections.observableArrayList();
	private TableView<Reservation> tableView=new TableView<>();
	private Label label=new Label("今日预约");
	private final TableColumn<Reservation,SimpleStringProperty> oid=new TableColumn<>("预约号"),
			covers=new TableColumn<>("人数"),
			tableNumber=new TableColumn<>("桌号"),
			date=new TableColumn<>("日期"),
			time=new TableColumn<>("时间"),
			name=new TableColumn<>("姓名"),
			phoneNumber=new TableColumn<>("电话"),
			type=new TableColumn<>("类型"),
			state=new TableColumn<>("状态");
	//数据库连接
	Connection connection=ConnectionInstance.getInstance();
	private int year;
	private int month;
	private int day;
	private void initialData(){
		PreparedStatement preparedStatement=null;
		ResultSet resultSet=null;
		try {
			String sql="select Reservation.oid,Reservation.covers,Tables.number,"
					+ "Reservation.date,Reservation.time,Customer.name,Customer.phoneNumber,"
					+ "ReservationState.type,ReservationState.state "
					+ "from Tables,Reservation,Customer,ReservationState "
					+ "where reservation.date=?"
					+ "and Reservation.table_id=Tables.oid "
					+ "and Reservation.customer_id=Customer.oid "
					+ "and Reservation.oid=ReservationState.reservation_id ";
			SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-mm-dd");
			String date=simpleDateFormat.format(simpleDateFormat.parse(year+"-"+month+"-"+day));
			System.out.println("date="+date);
			preparedStatement=connection.prepareStatement(sql);
			preparedStatement.setString(1, date);
			System.out.println("开始查询");
			resultSet=preparedStatement.executeQuery();
			while(resultSet.next()){
				int oid=resultSet.getInt(1);
				int covers=resultSet.getInt(2);
				int tableNumber=resultSet.getInt(3);
				date=resultSet.getString(4);
				String time=resultSet.getString(5);
				String name=resultSet.getString(6);
				String phoneNumber=resultSet.getString(7);
				int intType=resultSet.getInt(8);
				int intState=resultSet.getInt(9);
				String state=null;
				String type=null;
				switch (intType) {
					case 1:type="预约";break;
					case 2:type="自达";break;
					default:type="未知";break;
				}
				switch (intState) {
					case 1:state="已预约";break;
					case 2:state="已到达";break;
					case 3:state="已结账";break;
					default:state="未知";break;
				}
				data.add(new Reservation(oid, covers, tableNumber, date, time, name, phoneNumber,type,state));
				System.out.println(oid+" "+covers+" "+tableNumber+" "+date+" "+time+" "+name+" "+phoneNumber+" "+type+" "+state);
			}
			System.out.println(data.size());
			System.out.println("查询结束");
		} catch (SQLException | ParseException e) {
			e.printStackTrace();
		} finally {
			try {
				if(resultSet!=null){
					resultSet.close();
				}
				if(preparedStatement!=null){
					preparedStatement.close();
				}
				//需要全局保持连接
//				if(connection!=null){
//					connection.close();
//				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	public ReservationTable() {
		Calendar calendar=Calendar.getInstance();
		year = calendar.get(Calendar.YEAR);
		month = calendar.get(Calendar.MONTH)+1;
		day = calendar.get(Calendar.DATE);
		initialPane();
	}
	public ReservationTable(int y,int m,int d) {
		year = y;
		month = m;
		day = d;
		initialPane();
	}
	@SuppressWarnings("unchecked")
	private void initialPane() {
		System.out.println("initialData()开始执行");
		initialData();
		System.out.println("initialData()执行结束");
		tableView.setItems(data);

		oid.setMinWidth(40);
		oid.setCellValueFactory(new PropertyValueFactory<Reservation,SimpleStringProperty>("oid"));
		
		covers.setMinWidth(60);
		covers.setCellValueFactory(new PropertyValueFactory<Reservation,SimpleStringProperty>("covers"));
		
		tableNumber.setMinWidth(60);
		tableNumber.setCellValueFactory(new PropertyValueFactory<Reservation,SimpleStringProperty>("tableNumber"));
		
		date.setMinWidth(100);
		date.setCellValueFactory(new PropertyValueFactory<Reservation,SimpleStringProperty>("date"));
		
		time.setMinWidth(100);
		time.setCellValueFactory(new PropertyValueFactory<Reservation,SimpleStringProperty>("time"));
		
		name.setMinWidth(80);
		name.setCellValueFactory(new PropertyValueFactory<Reservation,SimpleStringProperty>("name"));
		
		phoneNumber.setMinWidth(150);
		phoneNumber.setCellValueFactory(new PropertyValueFactory<Reservation,SimpleStringProperty>("phoneNumber"));
		
		type.setCellValueFactory(new PropertyValueFactory<Reservation,SimpleStringProperty>("type"));
		
		state.setCellValueFactory(new PropertyValueFactory<Reservation,SimpleStringProperty>("state"));
		
		tableView.getColumns().addAll(oid,covers,tableNumber,date,time,name,phoneNumber,type,state);

		/*tableView.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent arg0) {
				Reservation reservation=tableView.getSelectionModel().getSelectedItem();
				int oid=reservation.getOid();
				int cover=reservation.getCovers();
				int tableNumber=reservation.getTableNumber();
				String name=reservation.getName();
				String phoneNumber=reservation.getPhoneNumber();
				String type=reservation.getType();
				String date=reservation.getDate();
				String time=reservation.getTime();
				String state=reservation.getState();
				System.out.println("hello world");
				System.out.println(oid+" "+cover+" "+tableNumber+" "+date+" "+time+" "+name+" "+phoneNumber+" "+type+" "+state);
				
				Stage stage_pop=new Stage();
//				CreateReservationPane createReservationPane_pop=new CreateReservationPane(1);
				CreateReservationPane createReservationPane_pop=new CreateReservationPane(1,reservation);
				Scene scene_pop=new Scene(createReservationPane_pop);
				stage_pop.setScene(scene_pop);
				stage_pop.show();
				
				createReservationPane_pop.getUpdateButton().addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

					@Override
					public void handle(MouseEvent arg0) {
						createReservationPane_pop.clickUpdate(reservation);
						stage_pop.close();
					}
				});
				createReservationPane_pop.getArrivalButton().addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

					@Override
					public void handle(MouseEvent arg0) {
						createReservationPane_pop.clickArrival(reservation);
						stage_pop.close();
					}
				});
				createReservationPane_pop.getAwayButton().addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

					@Override
					public void handle(MouseEvent arg0) {
						createReservationPane_pop.clickAway(reservation);
						stage_pop.close();
					}
				});
			}
		});*/
		
		label.setStyle("-fx-font-size:18px");
		this.getChildren().addAll(label,tableView);
		setSpacing(5);
		setPadding(new Insets(10, 10, 10, 10));
	}
	public TableView<Reservation> getTableView() {
		return tableView;
	}
}
