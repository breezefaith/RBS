package uidemo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;


class MyCalendar extends VBox{
	private GridPane gridPane;
	private Label []titleLabel;
	private DayVBox []dayVBox;
	
	private int year=Calendar.getInstance().get(Calendar.YEAR);

	private int month=Calendar.getInstance().get(Calendar.MONTH)+1;
	private Connection connection=null;
	

	public MyCalendar() {
		makePanel();
	}
	public MyCalendar(int year,int month){
		this.month=month;
		this.year=year;
		makePanel();
	}
	private void makePanel(){
				
		gridPane=new GridPane();
		gridPane.getStylesheets().add(MyCalendar.class.getResource("../mystylesheet/MyCalendar.css").toExternalForm());
		gridPane.setAlignment(Pos.CENTER);
		titleLabel=new Label[7];
		String stringTital[]={"日","一","二","三","四","五","六"};
		for(int i=0;i<7;i++){
			titleLabel[i]=new Label(stringTital[i]);
			//设置字体
			titleLabel[i].setFont(new Font("微软雅黑",15));
			titleLabel[i].setTextAlignment(TextAlignment.CENTER);
			titleLabel[i].setContentDisplay(ContentDisplay.CENTER);
			//设置居中
			titleLabel[i].setAlignment(Pos.CENTER);
			//设置高度
			titleLabel[i].setPrefHeight(30);
			//设置样式表
			//titleLabel[i].getStyleClass().add("titlelabel");
			gridPane.add(titleLabel[i], i, 0);
			//设置网格线可见
//			gridPane.setStyle("-fx-grid-lines-visible:true;-fx-grid-lines-color:#343137;-fx-background-insets: 0, 1 ;");
			//设置居中
			GridPane.setValignment(titleLabel[i], VPos.CENTER);
			GridPane.setHalignment(titleLabel[i], HPos.CENTER);
			GridPane.setFillWidth(titleLabel[i], true);
		}
		dayVBox=new DayVBox[42];
		for(int i=0;i<42;i++){
			dayVBox[i]=new DayVBox();
			dayVBox[i].getStyleClass().add("dayvbox");
			dayVBox[i].getDayLabel().getStyleClass().add("daylabel");
			dayVBox[i].getReservationLabel().getStyleClass().add("reservationlabel");
			gridPane.add(dayVBox[i], i%7, i/7+1);
		}
		flushDayButtons(year,month);
		this.getChildren().add(gridPane);
		this.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent arg0) {
				
			}
		});
	}
	
	private void flushDayButtons(int year, int month) {
		CalendarBean cBean=new CalendarBean(year,month);
		String theDate[]=cBean.getCalendar();
		connection=ConnectionInstance.getInstance();
		PreparedStatement preparedStatement=null;
		ResultSet resultSet=null;
		
		for(int i=0;i<42;i++){
			if(theDate[i]==null){
				dayVBox[i].getDayLabel().setText("");
				dayVBox[i].getReservationLabel().setText("");
				dayVBox[i].setYear(0);
				dayVBox[i].setMonth(0);
				dayVBox[i].setDay(0);
				dayVBox[i].getReservationLabel().setStyle("-fx-background-color:#f8f8f8;");
				continue;
			}
			dayVBox[i].setYear(year);
			dayVBox[i].setMonth(month);
			dayVBox[i].setDay(Integer.valueOf(theDate[i]));
			dayVBox[i].getDayLabel().setText(theDate[i]);
			Calendar calendar=Calendar.getInstance();
			int today=calendar.get(Calendar.DATE);
			int todayMonth=calendar.get(Calendar.MONTH)+1;
			int todayYear=calendar.get(Calendar.YEAR);
			if(Integer.valueOf(theDate[i])==today&&month==todayMonth&&year==todayYear){
				dayVBox[i].setStyle("-fx-background-color:#fd9dcf;");
			}
			
			try {
				SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-mm-dd");
				String date=simpleDateFormat.format(simpleDateFormat.parse(year+"-"+month+"-"+theDate[i]));
				preparedStatement=connection.prepareStatement("select count(*) from reservation where date=?");
				preparedStatement.setString(1, date);
				resultSet=preparedStatement.executeQuery();
				resultSet.next();
				dayVBox[i].getReservationLabel().setText(resultSet.getString(1));
				if(resultSet.getInt(1)!=0){
					dayVBox[i].getReservationLabel().setStyle("-fx-background-color:#fbe85e;");
				}
				if(resultSet!=null){
					resultSet.close();
				}
				if(preparedStatement!=null){
					preparedStatement.close();
				}
			} catch (ParseException | SQLException e) {
				e.printStackTrace();
			}
			
		}
	}
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	public int getMonth() {
		return month;
	}
	public void setMonth(int month) {
		this.month = month;
	}
	public Label[] getTitleLabel() {
		return titleLabel;
	}
	public void setTitleLabel(Label[] titleLabel) {
		this.titleLabel = titleLabel;
	}
	public DayVBox[] getDayVBox() {
		return dayVBox;
	}
	public void setDayVBox(DayVBox[] dayVBox) {
		this.dayVBox = dayVBox;
	}
	
}
class CalendarBorderPane extends BorderPane{
	private MyCalendar myCalendar;
	private Button lastMonthButton;
	private Button nextMonthButton;
	private Label currentDateLabel;
	public CalendarBorderPane() {
		myCalendar=new MyCalendar();
		makePane();
	}
	public CalendarBorderPane(int year,int month){
		myCalendar=new MyCalendar(year,month);
		makePane();
	}
	private void makePane(){
		lastMonthButton=new Button("上个月");
		nextMonthButton=new Button("下个月");
		currentDateLabel=new Label(myCalendar.getYear()+"年"+myCalendar.getMonth()+"月");
		currentDateLabel.setPadding(new Insets(0, 0, 0, 100));//设置label左外边距100
		nextMonthButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>(){

			@Override
			public void handle(MouseEvent arg0) {
				int currentYear=myCalendar.getYear();
				int currentMonth=myCalendar.getMonth();
				if(currentMonth==12){
					currentMonth=0;
					currentYear=currentYear+1;
				}
				myCalendar=new MyCalendar(currentYear, currentMonth+1);
				currentDateLabel.setText(currentYear+"年"+(currentMonth+1)+"月");
				setCenter(myCalendar);
			}
			
		});
		lastMonthButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>(){

			@Override
			public void handle(MouseEvent arg0) {
				int currentYear=myCalendar.getYear();
				int currentMonth=myCalendar.getMonth();
				if(currentMonth==1){
					currentMonth=13;
					currentYear=currentYear-1;
				}
				myCalendar=new MyCalendar(currentYear, currentMonth-1);
				currentDateLabel.setText(currentYear+"年"+(currentMonth-1)+"月");
				setCenter(myCalendar);
			}
			
		});
		lastMonthButton.setStyle("-fx-font-size:15px;-fx-margin:15px;");
		nextMonthButton.setStyle("-fx-font-size:15px;-fx-margin:15px;");
		currentDateLabel.setStyle("-fx-font-size:15px;");
		HBox hBox=new HBox();
		hBox.getChildren().add(lastMonthButton);
		hBox.getChildren().add(nextMonthButton);
		hBox.getChildren().add(currentDateLabel);
		hBox.setAlignment(Pos.BASELINE_CENTER);
		
		setTop(hBox);
		setCenter(myCalendar);
	}
	public MyCalendar getMyCalendar() {
		return myCalendar;
	}
	public void setMyCalendar(MyCalendar myCalendar) {
		this.myCalendar = myCalendar;
	}
	public Button getLastMonthButton() {
		return lastMonthButton;
	}
	public void setLastMonthButton(Button lastMonthButton) {
		this.lastMonthButton = lastMonthButton;
	}
	public Button getNextMonthButton() {
		return nextMonthButton;
	}
	public void setNextMonthButton(Button nextMonthButton) {
		this.nextMonthButton = nextMonthButton;
	}
	public Label getCurrentDateLabel() {
		return currentDateLabel;
	}
	public void setCurrentDateLabel(Label currentDateLabel) {
		this.currentDateLabel = currentDateLabel;
	}
}
public class MyCalendarDemo extends Application{

	public static void main(String[] args) {
		MyCalendarDemo.launch(args);
	}
	@Override
	public void start(Stage arg0) throws Exception {
		arg0.setTitle("餐馆系统");
		//borderPane.setPrefSize(400, 320);
		CalendarBorderPane calendarBorderPane=new CalendarBorderPane();
		Scene scene=new Scene(calendarBorderPane);
		arg0.setScene(scene);
		arg0.show();
	}

}
