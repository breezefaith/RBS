package uidemo;


import java.util.Calendar;

import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class DayVBox extends VBox{
	private Label dayLabel;
	private Label reservationLabel;
	private Separator separator;
	private int year;
	private int month;
	private int day;
	public Label getDayLabel() {
		return dayLabel;
	}
	public Label getReservationLabel() {
		return reservationLabel;
	}
	public Separator getSeparator() {
		return separator;
	}
	public DayVBox() {
		//年月日初始化
		year=0;
		month=0;
		day=0;
		//创建子控件
		//日期标签
		dayLabel=new Label();
		dayLabel.setAlignment(Pos.CENTER);
		dayLabel.setFont(new Font("黑体",16));
		dayLabel.setPrefWidth(50);
		dayLabel.setPrefHeight(20);
		//预约数标签
		reservationLabel=new Label();
		reservationLabel.setAlignment(Pos.CENTER);
		reservationLabel.setPrefWidth(50);
		//分隔符
		separator=new Separator();
		//设置控件居中
		this.setAlignment(Pos.CENTER);
		//添加控件
		this.getChildren().add(dayLabel);
		this.getChildren().add(separator);
		this.getChildren().add(reservationLabel);
//		this.addEventHandler(MouseEvent.MOUSE_CLICKED, new MyMouseClickedEventHandler());
		this.addEventHandler(MouseEvent.MOUSE_ENTERED, new MyMouseEnteredEventHandler());
		this.addEventHandler(MouseEvent.MOUSE_EXITED, new MyMouseExitedEventHandler());
		
		this.setPrefWidth(100);
	}
	class MyMouseClickedEventHandler implements EventHandler<MouseEvent>{

		@Override
		public void handle(MouseEvent arg0) {
			if(dayLabel==null||dayLabel.getText().equals("")||reservationLabel.getText().equals("0")){
				return;
			}
			//System.out.println(reservationLabel.getText());
//			System.out.println(arg0.getScreenX());
//			Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"hello",new ButtonType("取消", ButtonData.NO),new ButtonType("确定", ButtonData.YES));
//			alert.show();
		}
	}
	private void setVBoxStyle(String style){
		setStyle(style);
	}
	class MyMouseEnteredEventHandler implements EventHandler<MouseEvent>{

		@Override
		public void handle(MouseEvent arg0) {
			if(dayLabel==null||dayLabel.getText().equals("")){
				return;
			}
			//setVBoxStyle("-fx-background-color:#fed0c2;");
			setVBoxStyle("-fx-background-color:#ffff80;");
		}

	}
	class MyMouseExitedEventHandler implements EventHandler<MouseEvent>{

		@Override
		public void handle(MouseEvent arg0) {
			if(dayLabel==null||dayLabel.getText().equals("")){
				return;
			}
			//System.out.println("exit");
			setVBoxStyle("-fx-background-color:#f8f8f8;");
			Calendar calendar=Calendar.getInstance();
			int today=calendar.get(Calendar.DATE);
			int todayMonth=calendar.get(Calendar.MONTH)+1;
			int todayYear=calendar.get(Calendar.YEAR);
			if(day==today&&todayMonth==month&&todayYear==year){
				setVBoxStyle("-fx-background-color:#fd9dcf;");
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
	public int getDay() {
		return day;
	}
	public void setDay(int day) {
		this.day = day;
	}

}
