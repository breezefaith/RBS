package uidemo;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

class TableViewItem extends VBox{
	private Label imageLabel;
	private Label textLabel;
	private ImageView imageView;
	private ImageView imageViewOrange,imageViewGreen,imageViewRed;
	public TableViewItem() {
		imageLabel=new Label();
		textLabel=new Label();
		imageView=new ImageView("./img/green_table.jpg");
		
		imageViewGreen=new ImageView("./img/green_table.jpg");
		imageViewRed=new ImageView("./img/red_table.jpg");
		imageViewOrange=new ImageView("./img/orange_table.jpg");
		setFitSize(33, 50);
		
		imageView.setFitHeight(33);
		imageView.setFitWidth(50);
		imageLabel.setGraphic(imageView);
		textLabel.setText("1");
		
		this.getChildren().addAll(imageLabel,textLabel);
		this.setAlignment(Pos.CENTER);
		this.setPadding(new Insets(10, 10, 10, 10));
	}
	public TableViewItem(int state,int tableId){
		imageLabel=new Label();
		textLabel=new Label();
		imageViewGreen=new ImageView("./img/green_table.jpg");
		imageViewRed=new ImageView("./img/red_table.jpg");
		imageViewOrange=new ImageView("./img/orange_table.jpg");
		setFitSize(33, 50);
		
		if(state==1){
			imageLabel.setGraphic(imageViewOrange);
		}else if(state==2){
			imageLabel.setGraphic(imageViewRed);
		}else if(state==3){
			imageLabel.setGraphic(imageViewGreen);
		}
		textLabel.setText(String.valueOf(tableId));
		this.getChildren().addAll(imageLabel,textLabel);
		this.setAlignment(Pos.CENTER);
		this.setPadding(new Insets(10, 10, 10, 10));
	}
	private void setFitSize(double height,double width){
		imageViewGreen.setFitHeight(height);
		imageViewGreen.setFitWidth(width);
		imageViewRed.setFitHeight(height);
		imageViewRed.setFitWidth(width);
		imageViewOrange.setFitHeight(height);
		imageViewOrange.setFitWidth(width);
	}
	public void setImageView(ImageView imageView) {
		this.imageView = imageView;
	}
	public Label getImageLabel() {
		return imageLabel;
	}
	public void setImageLabel(Label imageLabel) {
		this.imageLabel = imageLabel;
	}
	public Label getTextLabel() {
		return textLabel;
	}
	public void setTextLabel(Label textLabel) {
		this.textLabel = textLabel;
	}
	public ImageView getImageView() {
		return imageView;
	}
	public void setGraphic(int state){
		if(state==1){
			imageLabel.setGraphic(imageViewOrange);
		}else if(state==2){
			imageLabel.setGraphic(imageViewRed);
		}else if(state==3){
			imageLabel.setGraphic(imageViewGreen);
		}
	}
	
}
class TableView extends ScrollPane{
	private FlowPane flowPane;
	private Connection connection;
	private TableViewItem[] tableViewItems;
	
	public TableView() {
		flowPane=new FlowPane();
		this.setVbarPolicy(ScrollBarPolicy.ALWAYS);
		for(int i=0;i<30;i++){
			flowPane.getChildren().add(new TableViewItem(i%3+1,i));
		}
		this.setContent(flowPane);
		this.viewportBoundsProperty().addListener(new ChangeListener<Bounds>(){

			@Override
			public void changed(ObservableValue<? extends Bounds> arg0, Bounds arg1, Bounds arg2) {
//				flowPane.setPrefHeight(arg2.getHeight());
//				flowPane.setPrefWidth(arg0.getValue().getWidth());
			}
			
		});
	}
	@SuppressWarnings("unused")
	public TableView(int methond){
		flowPane=new FlowPane();
		this.setVbarPolicy(ScrollBarPolicy.ALWAYS);
		connection=ConnectionInstance.getInstance();
		PreparedStatement preparedStatement=null;
		ResultSet resultSet=null;
		String sql="";
		sql="select count(*) from tables limit 1";
		SimpleDateFormat dateSimpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat timeSimpleDateFormat=new SimpleDateFormat("HH:mm:ss");
		SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date currentDate=new Date();
		try {
			preparedStatement=connection.prepareStatement(sql);
			resultSet=preparedStatement.executeQuery();
			while(resultSet.next()){
				tableViewItems=new TableViewItem[resultSet.getInt(1)];
				break;
			}
			for(int i=0;i<tableViewItems.length;i++){
				tableViewItems[i]=new TableViewItem();
				tableViewItems[i].getTextLabel().setText(String.valueOf(i+1));
			}
			sql="select time,table_id,state,date "
					+ "from reservation,reservationstate "
					+ "where reservation.oid=reservationstate.reservation_id "
					+ "and reservation.date=? "
					+ "";
			preparedStatement=connection.prepareStatement(sql);
			preparedStatement.setString(1, dateSimpleDateFormat.format(currentDate));
			resultSet=preparedStatement.executeQuery();
			while(resultSet.next()){
				String reservationTimeStr=resultSet.getString(1);
				int tableId=resultSet.getInt(2);
				int state=resultSet.getInt(3);
				String reservationDateStr=resultSet.getString(4);
				Date reservationDate=simpleDateFormat.parse(reservationDateStr+" "+reservationTimeStr);
				if(Math.abs(reservationDate.getTime()-currentDate.getTime())<7200000){
					tableViewItems[tableId-1].setGraphic(state);
					
				}
			}
			
			flowPane.getChildren().addAll(tableViewItems);
		} catch (SQLException | ParseException e) {
			e.printStackTrace();
		}
		
		this.setContent(flowPane);
		this.viewportBoundsProperty().addListener(new ChangeListener<Bounds>(){

			@Override
			public void changed(ObservableValue<? extends Bounds> arg0, Bounds arg1, Bounds arg2) {
//				flowPane.setPrefHeight(arg2.getHeight());
//				flowPane.setPrefWidth(arg0.getValue().getWidth());
			}
			
		});
	}
}
public class TableViewDemo extends Application{

	@Override
	public void start(Stage arg0) throws Exception {
		///FlowPane flowPane=new FlowPane();
		Scene scene=new Scene(new TableView(1));
//		for(int i=0;i<30;i++){
//			flowPane.getChildren().add(new TableViewItem());
//		}
		
		arg0.setScene(scene);
		arg0.show();
	}
	public static void main(String[] args) {
		TableViewDemo.launch(args);
	}

}
