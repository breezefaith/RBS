package uidemo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.StringConverter;
import sqlitedemo.Reservation;
import sqlitedemo.Table;
import javafx.scene.control.TableView;

class CreateReservationPane extends GridPane{
	private TableView<Table> tableView_table;
	private final TableColumn<Table,SimpleStringProperty> number=new TableColumn<>("桌号"),
			place=new TableColumn<>("座位");
	private final ObservableList<Table> data_row=FXCollections.observableArrayList();
	private Label coversLabel;
	private TextField coversTextField;
	private Label dateLabel;
	private DatePicker datePicker;
	private Label timeLabel;
	private Label nameLabel;
	private TextField nameTextField;
	private Label phoneLabel;
	private TextField phoneTextField;
	private Label tableLabel;
	private TextField tableTextField;
	private Button confirmButton;
	private Button cancelButton;
	private HBox hBox;
	private ChoiceBox<String> choiceBox_hour,choiceBox_min,choiceBox_sec;
	private HBox hBox2;
	private Label label_seperator;
	private Label label_seperator2;
	private Label label_type;
	private ChoiceBox<String> choiceBox_type;
	private Connection connection;
	private HBox hBox3;
	private Button updateButton;
	private Button arrivalButton;
	private Button awayButton;
	private Button deleteButton;

	public CreateReservationPane() {
		initialPane();
		addEventHandlers();
		this.setPrefSize(300, 550);
		updateButton.setDisable(true);
		arrivalButton.setDisable(true);
		awayButton.setDisable(true);
		deleteButton.setDisable(true);
	}
	public CreateReservationPane(int flag){
		if(flag==1){
			initialPane();
			addEventHandlers();
			this.setPrefSize(300, 550);
			confirmButton.setDisable(true);
			cancelButton.setDisable(true);
		}
	}
	
	@SuppressWarnings("unused")
	public CreateReservationPane(int flag,Reservation reservation){
		if(flag==1){
			initialPane();
			addEventHandlers();
			this.setPrefSize(300, 550);
			confirmButton.setDisable(true);
			cancelButton.setDisable(true);
			
			int oid=reservation.getOid();
			int cover=reservation.getCovers();
			int tableNumber=reservation.getTableNumber();
			String name=reservation.getName();
			String phoneNumber=reservation.getPhoneNumber();
			String type=reservation.getType();
			String date=reservation.getDate();
			String time=reservation.getTime();
			String state=reservation.getState();
			//把已有数据加载到指定位置
			coversTextField.setText(String.valueOf(cover));
			choiceBox_type.getSelectionModel().select(type);
			datePicker.setValue(LocalDate.parse(date));
			String[] splitTime=time.split(":");
			choiceBox_hour.getSelectionModel().select(Integer.parseInt(splitTime[0]));
			choiceBox_min.getSelectionModel().select(Integer.parseInt(splitTime[1]));
			choiceBox_sec.getSelectionModel().select(Integer.parseInt(splitTime[2]));
			tableTextField.setText(String.valueOf(tableNumber));
			nameTextField.setText(name);
			phoneTextField.setText(phoneNumber);
			
			//设置类型不可更改
			choiceBox_type.setDisable(true);
		}
	}
	private void initialData(){
		PreparedStatement preparedStatement=null;
		ResultSet resultSet=null;
		data_row.clear();
		try {
			String sql="select number,places from tables";
			preparedStatement=connection.prepareStatement(sql);
			resultSet=preparedStatement.executeQuery();
			while(resultSet.next()){
				data_row.add(new Table(resultSet.getInt(1),resultSet.getInt(2)));
			}
			System.out.println(data_row.size());

			System.out.println("查询结束");
		} catch (SQLException e) {
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
	@SuppressWarnings("unchecked")
	private void initialPane(){
		connection=ConnectionInstance.getInstance();
		label_type=new Label("订单类型：");
		ObservableList<String> data_type=FXCollections.observableArrayList(new String[]{"预约","自达"});
		choiceBox_type=new ChoiceBox<String>(data_type);
		coversLabel=new Label("人数：");
		coversTextField=new TextField();
		dateLabel=new Label("日期：");
		datePicker=new DatePicker();
		String[] option_h=new String[24];
		for(int i=0;i<24;i++){
			option_h[i]=String.valueOf(i);
		}
		ObservableList<String> data_hour=FXCollections.observableArrayList(option_h);
		choiceBox_hour=new ChoiceBox<String>();
		choiceBox_hour.setItems(data_hour);
		String[] option_m=new String[60];
		for(int i=0;i<60;i++){
			option_m[i]=String.valueOf(i); 
		}
		ObservableList<String> data_min=FXCollections.observableArrayList(option_m);
		choiceBox_min=new ChoiceBox<String>();
		choiceBox_min.setItems(data_min);
		String[] option_s=new String[60];
		for(int i=0;i<60;i++){
			option_s[i]=String.valueOf(i);
		}
		ObservableList<String> data_sec=FXCollections.observableArrayList(option_s);
		choiceBox_sec=new ChoiceBox<String>();
		choiceBox_sec.setItems(data_sec);
		hBox2=new HBox();
		label_seperator=new Label(":");
		label_seperator2=new Label(":");
		hBox2.getChildren().addAll(choiceBox_hour,label_seperator,choiceBox_min,label_seperator2,choiceBox_sec);
		HBox.setHgrow(choiceBox_hour, Priority.ALWAYS);
		HBox.setHgrow(choiceBox_min, Priority.ALWAYS);
		HBox.setHgrow(choiceBox_sec, Priority.ALWAYS);
		HBox.setMargin(label_seperator, new Insets(0, 10, 0, 10));
		HBox.setMargin(label_seperator2, new Insets(0, 10, 0, 10));

		timeLabel=new Label("时间：");
		tableLabel=new Label("桌号：");
		tableTextField=new TextField();
		/****************/
		initialData();
		number.setPrefWidth(120);
		number.setCellValueFactory(new PropertyValueFactory<>("number"));
		place.setPrefWidth(120);
		place.setCellValueFactory(new PropertyValueFactory<>("place"));
		tableView_table=new TableView<>();
		tableView_table.getColumns().addAll(number,place);
		tableView_table.setItems(data_row);
		tableView_table.setDisable(true);
		tableView_table.setPrefHeight(150);
		/************************/
		nameLabel=new Label("姓名：");
		nameTextField=new TextField();
		phoneLabel=new Label("电话：");
		phoneTextField=new TextField();
		confirmButton=new Button("确定");
		cancelButton=new Button("取消");
		hBox=new HBox();
		hBox.getChildren().addAll(confirmButton,cancelButton);
		hBox.setAlignment(Pos.CENTER);
		HBox.setMargin(confirmButton, new Insets(0,50,0,0));//insets四参数，上、右、下、左
		
		hBox3=new HBox();
		updateButton=new Button("修改");
		arrivalButton=new Button("到达");
		awayButton=new Button("结账");
		deleteButton=new Button("删除");
		hBox3.setAlignment(Pos.CENTER);
		hBox3.getChildren().addAll(updateButton,arrivalButton,awayButton,deleteButton);
		HBox.setMargin(updateButton, new Insets(0,30,0,0));//insets四参数，上、右、下、左
		HBox.setMargin(arrivalButton, new Insets(0,30,0,0));//insets四参数，上、右、下、左
		HBox.setMargin(awayButton, new Insets(0,30,0,0));//insets四参数，上、右、下、左
		
		this.add(label_type, 0, 0);
		this.add(choiceBox_type, 1, 0);
		this.add(coversLabel, 0, 1);
		this.add(coversTextField, 1,1);
		this.add(dateLabel, 0, 2);
		this.add(datePicker, 1, 2);
		this.add(timeLabel, 0, 3);
		this.add(hBox2, 1, 3);
		this.add(tableLabel, 0, 4);
		this.add(tableTextField, 1, 4);
		this.add(tableView_table, 0, 5);
		this.add(nameLabel, 0, 6);
		this.add(nameTextField, 1, 6);
		this.add(phoneLabel, 0, 7);
		this.add(phoneTextField, 1, 7);
		this.add(hBox, 0, 8);
		this.add(hBox3,0,9);
		
		this.setAlignment(Pos.TOP_CENTER);
		this.setVgap(15);
		this.setHgap(10);
		this.setPadding(new Insets(30,0,20,0));
		GridPane.setColumnIndex(tableView_table, 0);
		GridPane.setColumnSpan(tableView_table, 2);
		GridPane.setColumnIndex(hBox, 0);
		GridPane.setColumnSpan(hBox, 2);
		GridPane.setHgrow(hBox, Priority.ALWAYS);
		GridPane.setHalignment(hBox, HPos.CENTER);
		GridPane.setColumnIndex(hBox3, 0);
		GridPane.setColumnSpan(hBox3, 2);
		
		
	}
	private void addEventHandlers() {
		//设置datePicker内容格式
		datePicker.setConverter(new StringConverter<LocalDate>() {
			DateTimeFormatter dateTimeFormatter=DateTimeFormatter.ofPattern("yyyy-MM-dd");
			@Override
			public String toString(LocalDate arg0) {
				if(arg0!=null){
					return dateTimeFormatter.format(arg0);
				}else{
					return "";
				}
			}

			@Override
			public LocalDate fromString(String arg0) {
				if(arg0!=null&&arg0.isEmpty()!=true){
					return LocalDate.parse(arg0,dateTimeFormatter);
				}else{
					return null;
				}
			}
		});


		//设置若选择类型为“自达”则日期、时间不用填
		choiceBox_type.getSelectionModel().selectFirst();//默认选中“预约”
		choiceBox_type.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> arg0, String arg1, String arg2) {

				if(arg2.equals("自达")){
					choiceBox_hour.setDisable(true);
					choiceBox_min.setDisable(true);
					choiceBox_sec.setDisable(true);
					datePicker.setDisable(true);
				}else if(arg2.equals("预约")){
					choiceBox_hour.setDisable(false);
					choiceBox_min.setDisable(false);
					choiceBox_sec.setDisable(false);
					datePicker.setDisable(false);
				}
			}
		});

		//设置coversTextField只能输入数字
		coversTextField.addEventHandler(KeyEvent.KEY_RELEASED,new EventHandler<KeyEvent>(){
			@Override
			public void handle(KeyEvent arg0) {
				//如果输入的是数字字符
				if(!arg0.getText().isEmpty()&&Character.isDigit(arg0.getText().charAt(0))){

				}else{
					coversTextField.deletePreviousChar();
				}
			}
		});
		//设置tableView_table的双击选中行的number填入tableTextField
		tableView_table.setRowFactory(new Callback<TableView<Table>, TableRow<Table>>() {  
			@Override
			public TableRow<Table> call(TableView<Table> arg0) {
				return new TableRowControl();
			}  
		});
		//设置tableTextField输入内容前必须输入人数、日期和时间或者必须输入人数，以及限制输入内容只能为数字
		tableTextField.addEventHandler(MouseEvent.MOUSE_CLICKED,new EventHandler<MouseEvent>(){
			@Override
			public void handle(MouseEvent arg0) {
				initialData();
				String date=null;
				Date currentTime=new Date();
				SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				DecimalFormat decimalFormat=new DecimalFormat("00");
				Calendar.getInstance();
				boolean flag=false;
				if(choiceBox_type.getSelectionModel().getSelectedItem().equals("预约")){
					if(coversTextField.getText()==null||coversTextField.getText().isEmpty()||
							datePicker.getValue()==null||datePicker.getValue().toString().isEmpty()||
							choiceBox_hour.getSelectionModel().getSelectedItem()==null||
							choiceBox_min.getSelectionModel().getSelectedItem()==null||
							choiceBox_sec.getSelectionModel().getSelectedItem()==null){
						tableTextField.clear();
						tableTextField.setText(null);
						Alert alert = new Alert(Alert.AlertType.WARNING,"请先输入人数、日期和时间！",new ButtonType("确定", ButtonData.YES));
						alert.show();
						return;
					}
					date=datePicker.getValue().toString();
					String selectedHour=choiceBox_hour.getSelectionModel().getSelectedItem();
					String selectedMin=choiceBox_min.getSelectionModel().getSelectedItem();
					String selectedSec=choiceBox_sec.getSelectionModel().getSelectedItem();
					try {
						currentTime=simpleDateFormat.parse(date+" "+selectedHour+":"+selectedMin+":"+selectedSec);
					} catch (ParseException e) {
						e.printStackTrace();
					}

				}else if(choiceBox_type.getSelectionModel().getSelectedItem().equals("自达")){
					if(coversTextField.getText()==null||coversTextField.getText().isEmpty()){
						tableTextField.clear();
						tableTextField.setText(null);
						Alert alert = new Alert(Alert.AlertType.WARNING,"请先输入人数！",new ButtonType("确定", ButtonData.YES));
						alert.show();
						return;
					}
					SimpleDateFormat simpleDateFormat1=new SimpleDateFormat("yyyy-MM-dd");
					Calendar calendar1=Calendar.getInstance();
					int year=calendar1.get(Calendar.YEAR);
					int month=calendar1.get(Calendar.MONTH)+1;
					int day=calendar1.get(Calendar.DATE);
					try {
						date=simpleDateFormat1.format(simpleDateFormat1.parse(year+"-"+decimalFormat.format(month)+"-"+decimalFormat.format(day)));
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}
				PreparedStatement preparedStatement=null;
				ResultSet resultSet=null;
				ArrayList<Integer> hasDeletedTableNumber=new ArrayList<>();
				try {
					String sql="select table_id,number,date,time "
							+ "from tables,reservation "
							+ "where reservation.table_id=tables.oid "
							+ "union select oid,number,null,null from tables "
							+ "where oid not in(select table_id from reservation) "
							+ "order by table_id desc";
					preparedStatement=connection.prepareStatement(sql);
					resultSet=preparedStatement.executeQuery();
					while(resultSet.next()){
						flag=true;
						int table_id=resultSet.getInt(1);
						int number=resultSet.getInt(2);
						String reservationDateStr=resultSet.getString(3);
						String reservationTimeStr=resultSet.getString(4);
						if(hasDeletedTableNumber.contains(number)){
							System.out.println("桌号:"+number+"\t已被过滤");
							continue;
						}
						//								System.out.println(table_id+"\t"+number+"\t"+(reservationDateStr==null)+"\t"+reservationTimeStr);
						if((reservationDateStr==null)&&
								Integer.parseInt(coversTextField.getText())>data_row.get(number-1).getPlace()){
							System.out.println("桌号:"+number+"\t时间符合但人数不符合");
							data_row.remove(table_id-1);
							hasDeletedTableNumber.add(number);
						}
						//以下reservationDateStr和reservationTimeStr肯定不是“null”
						if(reservationTimeStr!=null&&reservationDateStr!=null){
							Date reservationTime=simpleDateFormat.parse(date+" "+reservationTimeStr);
							//如果预约的日期与已有订单日期相等且时间相差在+-2小时内或者该桌子的座位数则删除选项
							if((reservationDateStr.equals(date)&&
									Math.abs(reservationTime.getTime()-currentTime.getTime())<(long)2*60*60*1000)){
								System.out.println("桌号:"+number+"\t时间不符合");
								data_row.remove(table_id-1);
								hasDeletedTableNumber.add(number);
							}else if(!(reservationDateStr.equals(date)&&Math.abs(reservationTime.getTime()-currentTime.getTime())<(long)2*60*60*1000)&&
									Integer.parseInt(coversTextField.getText())>data_row.get(number-1).getPlace()){
								System.out.println("桌号:"+number+"\t时间符合但人数不符合");
								data_row.remove(table_id-1);
								hasDeletedTableNumber.add(number);
							}
						}

					}
					System.out.println("line 363:"+flag);
					if(flag!=true){
						for(int i=data_row.size()-1;i>=0;i--){
							if(Integer.parseInt(coversTextField.getText())>data_row.get(i).getPlace()){
								data_row.remove(i);
							}
						}
					}
					tableView_table.setDisable(false);
				} catch (SQLException | ParseException e) {
					e.printStackTrace();
				}
			}
		});
		tableTextField.addEventHandler(KeyEvent.KEY_PRESSED,new EventHandler<KeyEvent>(){
			@Override
			public void handle(KeyEvent arg0) {
				initialData();
				String date=null;
				Date currentTime=new Date();
				SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				DecimalFormat decimalFormat=new DecimalFormat("00");
				Calendar.getInstance();
				if(choiceBox_type.getSelectionModel().getSelectedItem().equals("预约")){
					if(coversTextField.getText()==null||coversTextField.getText().isEmpty()||
							datePicker.getValue()==null||datePicker.getValue().toString().isEmpty()||
							choiceBox_hour.getSelectionModel().getSelectedItem()==null||
							choiceBox_min.getSelectionModel().getSelectedItem()==null||
							choiceBox_sec.getSelectionModel().getSelectedItem()==null){
						tableTextField.clear();
						tableTextField.setText(null);
						Alert alert = new Alert(Alert.AlertType.WARNING,"请先输入人数、日期和时间！",new ButtonType("确定", ButtonData.YES));
						alert.show();
						return;
					}
					date=datePicker.getValue().toString();
					String selectedHour=choiceBox_hour.getSelectionModel().getSelectedItem();
					String selectedMin=choiceBox_min.getSelectionModel().getSelectedItem();
					String selectedSec=choiceBox_sec.getSelectionModel().getSelectedItem();
					try {
						currentTime=simpleDateFormat.parse(date+" "+selectedHour+":"+selectedMin+":"+selectedSec);
					} catch (ParseException e) {
						e.printStackTrace();
					}

				}else if(choiceBox_type.getSelectionModel().getSelectedItem().equals("自达")){
					if(coversTextField.getText()==null||coversTextField.getText().isEmpty()){
						tableTextField.clear();
						tableTextField.setText(null);
						Alert alert = new Alert(Alert.AlertType.WARNING,"请先输入人数！",new ButtonType("确定", ButtonData.YES));
						alert.show();
						return;
					}
					SimpleDateFormat simpleDateFormat1=new SimpleDateFormat("yyyy-MM-dd");
					Calendar calendar1=Calendar.getInstance();
					int year=calendar1.get(Calendar.YEAR);
					int month=calendar1.get(Calendar.MONTH)+1;
					int day=calendar1.get(Calendar.DATE);
					try {
						date=simpleDateFormat1.format(simpleDateFormat1.parse(year+"-"+decimalFormat.format(month)+"-"+decimalFormat.format(day)));
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}
				PreparedStatement preparedStatement=null;
				ResultSet resultSet=null;
				try {
					preparedStatement=connection.prepareStatement("select table_id,number,time "
							+ "from tables,reservation "
							+ "where date=? "
							+ "and reservation.table_id=tables.oid "
							+ "order by table_id desc");
					preparedStatement.setString(1, date);
					resultSet=preparedStatement.executeQuery();


					while(resultSet.next()){
						String reservationTimeStr=resultSet.getString(3);
						Date reservationTime=simpleDateFormat.parse(date+" "+reservationTimeStr);
						int table_id=resultSet.getInt(1);
						int number=resultSet.getInt(2);
						if(Math.abs(reservationTime.getTime()-currentTime.getTime())<(long)2*60*60*1000||
								Integer.parseInt(coversTextField.getText())>number){
							data_row.remove(table_id-1);
						}
					}
					tableView_table.setDisable(false);
				} catch (SQLException | ParseException e) {
					e.printStackTrace();
				}
			}
		});
	}

	//点击确定后的处理
	@SuppressWarnings("resource")
	public void clickConfirm(){
		SimpleDateFormat timeSimpleDateFormat=new SimpleDateFormat("HH:mm:ss");
		SimpleDateFormat dateSimpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
		String dateStr="";
		String timeStr="";
		String name="";
		String phone="";
		int table_number=0;
		int covers=0;
		int type=-1;
		int state=-1;
		if(choiceBox_type.getSelectionModel().getSelectedItem().equals("预约")){
			if(coversTextField.getText()==null||coversTextField.getText().isEmpty()||
					datePicker.getValue()==null||datePicker.getValue().toString().isEmpty()||
					choiceBox_hour.getSelectionModel().getSelectedItem()==null||
					choiceBox_min.getSelectionModel().getSelectedItem()==null||
					choiceBox_sec.getSelectionModel().getSelectedItem()==null){
				tableTextField.clear();
				tableTextField.setText(null);
				Alert alert = new Alert(Alert.AlertType.WARNING,"请先输入人数、日期和时间！",new ButtonType("确定", ButtonData.YES));
				alert.show();
				return;
			}
			if(tableTextField.getText()==null||tableTextField.getText().isEmpty()){
				Alert alert = new Alert(Alert.AlertType.WARNING,"请先输入桌号！",new ButtonType("确定", ButtonData.YES));
				alert.show();
				return;
			}
			if(phoneTextField.getText()==null&&phoneTextField.getText().isEmpty()){
				Alert alert = new Alert(Alert.AlertType.WARNING,"请先输入手机号！",new ButtonType("确定", ButtonData.YES));
				alert.show();
				return;
			}
			covers=Integer.parseInt(coversTextField.getText());
			String date=datePicker.getValue().toString();
			dateStr=date;
			String time_hour=choiceBox_hour.getSelectionModel().getSelectedItem();
			String time_min=choiceBox_min.getSelectionModel().getSelectedItem();
			String time_sec=choiceBox_sec.getSelectionModel().getSelectedItem();
			if(nameTextField.getText()!=null){
				name=nameTextField.getText();
			}
			if(phoneTextField.getText()!=null){
				phone=phoneTextField.getText();
			}
			try {
				timeStr=timeSimpleDateFormat.format(timeSimpleDateFormat.parse(time_hour+":"+time_min+":"+time_sec));
			} catch (ParseException e) {
				e.printStackTrace();
				return;
			}
			table_number=Integer.parseInt(tableTextField.getText());
			type=1;
			state=1;
		}else if(choiceBox_type.getSelectionModel().getSelectedItem().equals("自达")){
			if(coversTextField.getText()==null||coversTextField.getText().isEmpty()){
				tableTextField.clear();
				tableTextField.setText(null);
				Alert alert = new Alert(Alert.AlertType.WARNING,"请先输入人数！",new ButtonType("确定", ButtonData.YES));
				alert.show();
				return;
			}
			if(tableTextField.getText()==null||tableTextField.getText().isEmpty()){
				Alert alert = new Alert(Alert.AlertType.WARNING,"请先输入桌号！",new ButtonType("确定", ButtonData.YES));
				alert.show();
				return;
			}
			covers=Integer.parseInt(coversTextField.getText());
			Date date=new Date();
			dateStr=dateSimpleDateFormat.format(date);
			timeStr=timeSimpleDateFormat.format(date);
			table_number=Integer.parseInt(tableTextField.getText());
			if(nameTextField.getText()!=null){
				name=nameTextField.getText();
			}
			if(phoneTextField.getText()!=null){
				phone=phoneTextField.getText();
			}
			type=2;
			state=2;
		}
		PreparedStatement preparedStatement=null;
		String sql=null;
		ResultSet resultSet=null;
		int flag=0;
		try {
			sql="insert into customer(name,phoneNumber) values(?,?)";
			preparedStatement=connection.prepareStatement(sql);
			preparedStatement.setString(1, name);
			preparedStatement.setString(2, phone);
			preparedStatement.executeUpdate();
			flag=1;
			System.out.println("line 544:查询插入开始执行");
			sql="insert into reservation(covers,date,time,table_id,customer_id) select ?,?,?,?,oid from customer order by oid desc limit 1";
			preparedStatement=connection.prepareStatement(sql);
			preparedStatement.setInt(1, covers);
			preparedStatement.setString(2, dateStr);
			preparedStatement.setString(3, timeStr);
			preparedStatement.setInt(4, table_number);
			preparedStatement.executeUpdate();
			System.out.println("line 544:查询插入执行成功");
			flag=2;
			sql="insert into reservationstate(reservation_id,type,state) select oid,?,? from reservation order by oid desc limit 1";
			preparedStatement=connection.prepareStatement(sql);
			preparedStatement.setInt(1, type);
			preparedStatement.setInt(2, state);
			preparedStatement.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
			if(flag==1){
				String sql1="delete from customer where oid in (select oid from customer order by oid desc limit 1)";
				try {
					preparedStatement=connection.prepareStatement(sql1);
					System.out.println("开始删除customer最新一行记录");
					preparedStatement.executeUpdate();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}else if(flag==2){
				String sql1="delete from customer where oid in (select oid from customer order by oid desc limit 1)";
				try {
					preparedStatement=connection.prepareStatement(sql1);
					System.out.println("开始删除customer最新一行记录");
					preparedStatement.executeUpdate();
					sql1="delete from reservation where oid in (select oid from reservation order by oid desc limit 1)";
					preparedStatement=connection.prepareStatement(sql1);
					System.out.println("开始删除reservation最新一行记录");
					preparedStatement.executeUpdate();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		} finally {
			try {
				if(resultSet!=null){
					resultSet.close();
				}
				if(preparedStatement!=null){
					preparedStatement.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	@SuppressWarnings({ "resource", "unused" })
	public void clickUpdate(Reservation reservation){
		SimpleDateFormat timeSimpleDateFormat=new SimpleDateFormat("HH:mm:ss");
		String dateStr="";
		String timeStr="";
		String name="";
		String phone="";
		int table_number=0;
		int covers=0;
		int type=-1;
		int state=-1;
		if(choiceBox_type.getSelectionModel().getSelectedItem().equals("预约")){
			if(coversTextField.getText()==null||coversTextField.getText().isEmpty()||
					datePicker.getValue()==null||datePicker.getValue().toString().isEmpty()||
					choiceBox_hour.getSelectionModel().getSelectedItem()==null||
					choiceBox_min.getSelectionModel().getSelectedItem()==null||
					choiceBox_sec.getSelectionModel().getSelectedItem()==null){
				tableTextField.clear();
				tableTextField.setText(null);
				Alert alert = new Alert(Alert.AlertType.WARNING,"请先输入人数、日期和时间！",new ButtonType("确定", ButtonData.YES));
				alert.show();
				return;
			}
			if(tableTextField.getText()==null||tableTextField.getText().isEmpty()){
				Alert alert = new Alert(Alert.AlertType.WARNING,"请先输入桌号！",new ButtonType("确定", ButtonData.YES));
				alert.show();
				return;
			}
			if(phoneTextField.getText()==null&&phoneTextField.getText().isEmpty()){
				Alert alert = new Alert(Alert.AlertType.WARNING,"请先输入手机号！",new ButtonType("确定", ButtonData.YES));
				alert.show();
				return;
			}
			covers=Integer.parseInt(coversTextField.getText());
			String date=datePicker.getValue().toString();
			dateStr=date;
			String time_hour=choiceBox_hour.getSelectionModel().getSelectedItem();
			String time_min=choiceBox_min.getSelectionModel().getSelectedItem();
			String time_sec=choiceBox_sec.getSelectionModel().getSelectedItem();
			if(nameTextField.getText()!=null){
				name=nameTextField.getText();
			}
			if(phoneTextField.getText()!=null){
				phone=phoneTextField.getText();
			}
			try {
				timeStr=timeSimpleDateFormat.format(timeSimpleDateFormat.parse(time_hour+":"+time_min+":"+time_sec));
			} catch (ParseException e) {
				e.printStackTrace();
				return;
			}
			table_number=Integer.parseInt(tableTextField.getText());
			type=1;
			state=1;
		}else if(choiceBox_type.getSelectionModel().getSelectedItem().equals("自达")){
			if(coversTextField.getText()==null||coversTextField.getText().isEmpty()){
				tableTextField.clear();
				tableTextField.setText(null);
				Alert alert = new Alert(Alert.AlertType.WARNING,"请先输入人数！",new ButtonType("确定", ButtonData.YES));
				alert.show();
				return;
			}
			if(tableTextField.getText()==null||tableTextField.getText().isEmpty()){
				Alert alert = new Alert(Alert.AlertType.WARNING,"请先输入桌号！",new ButtonType("确定", ButtonData.YES));
				alert.show();
				return;
			}
			covers=Integer.parseInt(coversTextField.getText());
			dateStr=reservation.getDate();
			timeStr=reservation.getTime();
			table_number=Integer.parseInt(tableTextField.getText());
			if(nameTextField.getText()!=null){
				name=nameTextField.getText();
			}
			if(phoneTextField.getText()!=null){
				phone=phoneTextField.getText();
			}
			type=2;
			state=2;
		}
		PreparedStatement preparedStatement=null;
		String sql=null;
		ResultSet resultSet=null;
		int flag=0;
		try {
			sql="update customer set name=?,phoneNumber=? where oid in (select customer_id from reservation where oid=? limit 1)";
			preparedStatement=connection.prepareStatement(sql);
			preparedStatement.setString(1, name);
			preparedStatement.setString(2, phone);
			preparedStatement.setInt(3, reservation.getOid());
			preparedStatement.executeUpdate();
			flag=1;
			System.out.println("line 776:查询更新开始执行");
			sql="update reservation set covers=?,date=?,time=?,table_id=? where oid=?";
			preparedStatement=connection.prepareStatement(sql);
			preparedStatement.setInt(1, covers);
			preparedStatement.setString(2, dateStr);
			preparedStatement.setString(3, timeStr);
			preparedStatement.setInt(4, table_number);
			preparedStatement.setInt(5, reservation.getOid());
			preparedStatement.executeUpdate();
			System.out.println("line 786:更新执行成功");
			flag=2;
			Alert alert = new Alert(Alert.AlertType.INFORMATION,"订单更新成功",new ButtonType("确定", ButtonData.YES));
			alert.show();
		} catch (SQLException e) {
			e.printStackTrace();
			if(flag==1){
//				String sql1="delete from customer where oid in (select oid from customer order by oid desc limit 1)";
//				try {
//					preparedStatement=connection.prepareStatement(sql1);
//					System.out.println("开始删除customer最新一行记录");
//					preparedStatement.executeUpdate();
//				} catch (SQLException e1) {
//					e1.printStackTrace();
//				}
			}else if(flag==2){
//				String sql1="delete from customer where oid in (select oid from customer order by oid desc limit 1)";
//				try {
//					preparedStatement=connection.prepareStatement(sql1);
//					System.out.println("开始删除customer最新一行记录");
//					preparedStatement.executeUpdate();
//					sql1="delete from reservation where oid in (select oid from reservation order by oid desc limit 1)";
//					preparedStatement=connection.prepareStatement(sql1);
//					System.out.println("开始删除reservation最新一行记录");
//					preparedStatement.executeUpdate();
//				} catch (SQLException e1) {
//					e1.printStackTrace();
//				}
			}
		} finally {
			try {
				if(resultSet!=null){
					resultSet.close();
				}
				if(preparedStatement!=null){
					preparedStatement.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void clickArrival(Reservation reservation){
		PreparedStatement preparedStatement=null;
		String sql=null;
		ResultSet resultSet=null;
		try {
			sql="update reservationstate set state=? where reservation_id=?";
			preparedStatement=connection.prepareStatement(sql);
			preparedStatement.setInt(1, 2);
			preparedStatement.setInt(2, reservation.getOid());
			preparedStatement.executeUpdate();
			Alert alert = new Alert(Alert.AlertType.INFORMATION,"记录到达成功",new ButtonType("确定", ButtonData.YES));
			alert.show();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if(resultSet!=null){
					resultSet.close();
				}
				if(preparedStatement!=null){
					preparedStatement.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void clickAway(Reservation reservation){
		PreparedStatement preparedStatement=null;
		String sql=null;
		ResultSet resultSet=null;
		try {
			sql="update reservationstate set state=? where reservation_id=?";
			preparedStatement=connection.prepareStatement(sql);
			preparedStatement.setInt(1, 3);
			preparedStatement.setInt(2, reservation.getOid());
			preparedStatement.executeUpdate();
			Alert alert = new Alert(Alert.AlertType.INFORMATION,"结账成功",new ButtonType("确定", ButtonData.YES));
			alert.show();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if(resultSet!=null){
					resultSet.close();
				}
				if(preparedStatement!=null){
					preparedStatement.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	@SuppressWarnings("resource")
	public void clickDelete(Reservation reservation){
		PreparedStatement preparedStatement=null;
		String sql=null;
		ResultSet resultSet=null;
		try {
			sql="delete from reservationstate where reservation_id=?";
			preparedStatement=connection.prepareStatement(sql);
			preparedStatement.setInt(1, reservation.getOid());
			preparedStatement.executeUpdate();
			sql="delete from reservation where oid=?";
			preparedStatement=connection.prepareStatement(sql);
			preparedStatement.setInt(1, reservation.getOid());
			preparedStatement.executeUpdate();
			Alert alert = new Alert(Alert.AlertType.INFORMATION,"删除成功",new ButtonType("确定", ButtonData.YES));
			alert.show();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if(resultSet!=null){
					resultSet.close();
				}
				if(preparedStatement!=null){
					preparedStatement.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	class TableRowControl extends TableRow<Table> {
		public TableRowControl() {  
			super();  
			this.setOnMouseClicked(new EventHandler<MouseEvent>() {  
				@Override  
				public void handle(MouseEvent event) {
					if (event.getButton().equals(MouseButton.PRIMARY)  
							&& event.getClickCount() == 2  
							&& TableRowControl.this.getIndex() < tableView_table.getItems().size()) {
						tableTextField.setText(String.valueOf(tableView_table.getSelectionModel().getSelectedItem().getNumber()));
					}  
				}  
			});  
		}  
	}
	public Button getConfirmButton() {
		return confirmButton;
	}
	public void setConfirmButton(Button confirmButton) {
		this.confirmButton = confirmButton;
	}
	public Button getCancelButton() {
		return cancelButton;
	}
	public void setCancelButton(Button cancelButton) {
		this.cancelButton = cancelButton;
	}
	public Button getUpdateButton() {
		return updateButton;
	}
	public void setUpdateButton(Button updateButton) {
		this.updateButton = updateButton;
	}
	public Button getArrivalButton() {
		return arrivalButton;
	}
	public void setArrivalButton(Button arrivalButton) {
		this.arrivalButton = arrivalButton;
	}
	public Button getAwayButton() {
		return awayButton;
	}
	public void setAwayButton(Button awayButton) {
		this.awayButton = awayButton;
	}
	public Button getDeleteButton() {
		return deleteButton;
	}
	public void setDeleteButton(Button deleteButton) {
		this.deleteButton = deleteButton;
	}
}
public class CreateReservationDemo extends Application {


	@Override
	public void start(Stage arg0) throws Exception {
		CreateReservationPane createReservationPane=new CreateReservationPane(1);
		Scene scene=new Scene(createReservationPane);

		createReservationPane.getConfirmButton().addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>(){
			@Override
			public void handle(MouseEvent e) {
				createReservationPane.clickConfirm();
				arg0.close();
			}	
		});
		createReservationPane.getCancelButton().addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>(){
			@Override
			public void handle(MouseEvent e) {
				arg0.close();
			}	
		});
		arg0.setScene(scene);
		arg0.show();
	}
	public static void main(String[] args) {
		Application.launch(args);
	}
}
