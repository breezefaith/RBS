package uidemo;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import sqlitedemo.Reservation;

public class MainUI extends Application {

	private CalendarBorderPane calendarBorderPane;
	private Button button_createReservation;
	private ReservationTable reservationTable;
	private VBox vBox;
	private HBox hBox;
	private Scene scene_main;
	private CreateReservationPane createReservationPane;
	private Stage stage_createReservation;
	private Scene scene_createReservation;
	private TableView tableView;
	private Stage stage_main;


	int i=0;
	@Override
	public void start(Stage arg0) throws Exception {
		setStage_main(arg0);
		arg0.setTitle("餐馆系统");
		arg0.setMaximized(true);

		vBox = new VBox();
		hBox = new HBox();
		scene_main = new Scene(hBox);

		reservationTable = new ReservationTable();
		//		reservationTable.setBorder(new Border(new BorderStroke(Color.BLACK,BorderStrokeStyle.SOLID,null,new BorderWidths(0.5))));

		button_createReservation = new Button("新建订单");
		button_createReservation.setStyle("-fx-font-size:15px;");
		createReservationPane=new CreateReservationPane();
		stage_createReservation=new Stage();
		scene_createReservation=new Scene(createReservationPane);
		stage_createReservation.setScene(scene_createReservation);
		stage_createReservation.setResizable(false);
		calendarBorderPane = new CalendarBorderPane();
		//		calendarBorderPane.setBorder(new Border(new BorderStroke(Color.BLACK,BorderStrokeStyle.SOLID,null,new BorderWidths(2))));

		//修改订单、记录到达、结账、 



		vBox.getChildren().add(reservationTable);
		vBox.getChildren().add(button_createReservation);
		vBox.getChildren().add(calendarBorderPane);
		VBox.setVgrow(reservationTable, Priority.ALWAYS);
		VBox.setVgrow(calendarBorderPane, Priority.ALWAYS);

		hBox.getChildren().add(vBox);
		HBox.setHgrow(vBox, Priority.ALWAYS);
		tableView = new TableView(1);
		hBox.getChildren().add(tableView);
		HBox.setHgrow(tableView, Priority.ALWAYS);
		vBox.setAlignment(Pos.CENTER);

		createReservationPane.getConfirmButton().addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>(){
			@Override
			public void handle(MouseEvent e) {
				createReservationPane.clickConfirm();

				refresh();
				stage_createReservation.close();
			}	
		});
		createReservationPane.getCancelButton().addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>(){
			@Override
			public void handle(MouseEvent e) {
				stage_createReservation.close();
			}	
		});
		button_createReservation.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>(){
			@Override
			public void handle(MouseEvent e) {
				stage_createReservation.show();
			}
		});

		reservationTable.getTableView().addEventHandler(MouseEvent.MOUSE_CLICKED, new MyTableViewEventHandler(reservationTable));

		DayVBox[] dayVBoxs=calendarBorderPane.getMyCalendar().getDayVBox();

		for(i=0;i<42;i++){
			if(dayVBoxs[i].getDayLabel()==null||dayVBoxs[i].getDayLabel().getText().equals("")||dayVBoxs[i].getReservationLabel().getText().equals("0")){
				continue;
			}
			int year=dayVBoxs[i].getYear();
			int month=dayVBoxs[i].getMonth();
			int day=dayVBoxs[i].getDay();
			dayVBoxs[i].addEventHandler(MouseEvent.MOUSE_CLICKED, new MyDayVBoxMouseEventHandle(year, month, day));
		}

		arg0.setScene(scene_main);
		arg0.getIcons().add(new Image(MainUI.class.getResourceAsStream("../icons/timg.jpg")));
		arg0.show();
	}
	class MyDayVBoxMouseEventHandle implements EventHandler<MouseEvent>{
		int year,month,day;

		public MyDayVBoxMouseEventHandle(int y,int m,int d) {
			year=y;
			month=m;
			day=d;
		}
		@Override
		public void handle(MouseEvent arg0) {
			Stage stage_pop=new Stage();
			ReservationTable reservationTable_pop=new ReservationTable(year, month, day);
			Scene scene_pop=new Scene(reservationTable_pop);
			stage_pop.setScene(scene_pop);
			stage_pop.show();
			reservationTable_pop.getTableView().addEventHandler(MouseEvent.MOUSE_CLICKED, new MyPopTableViewEventHandler(reservationTable_pop,stage_pop));

		}

	}
	class MyTableViewEventHandler implements EventHandler<MouseEvent>{

		ReservationTable reservationTable;
		public MyTableViewEventHandler(ReservationTable r) {
			reservationTable=r;
		}
		@Override
		public void handle(MouseEvent arg0) {
			Reservation reservation=reservationTable.getTableView().getSelectionModel().getSelectedItem();
			Stage stage_pop=new Stage();
			CreateReservationPane createReservationPane_pop=new CreateReservationPane(1,reservation);
			Scene scene_pop=new Scene(createReservationPane_pop);
			stage_pop.setScene(scene_pop);
			stage_pop.show();

			createReservationPane_pop.getUpdateButton().addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

				@Override
				public void handle(MouseEvent arg0) {
					createReservationPane_pop.clickUpdate(reservation);
					refresh();
					stage_pop.close();
				}
			});
			createReservationPane_pop.getArrivalButton().addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

				@Override
				public void handle(MouseEvent arg0) {
					createReservationPane_pop.clickArrival(reservation);
					refresh();
					stage_pop.close();
				}
			});
			createReservationPane_pop.getAwayButton().addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

				@Override
				public void handle(MouseEvent arg0) {
					createReservationPane_pop.clickAway(reservation);
					refresh();
					stage_pop.close();
				}
			});
			createReservationPane_pop.getDeleteButton().addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

				@Override
				public void handle(MouseEvent arg0) {
					createReservationPane_pop.clickDelete(reservation);
					refresh();
					stage_pop.close();
				}
			});
		}
	}
	class MyPopTableViewEventHandler extends MyTableViewEventHandler {
		Stage stage_origin;
		public MyPopTableViewEventHandler(ReservationTable r,Stage so) {
			super(r);
			stage_origin=so;
		}
		@Override
		public void handle(MouseEvent arg0) {
			Reservation reservation=reservationTable.getTableView().getSelectionModel().getSelectedItem();
			Stage stage_pop=new Stage();
			CreateReservationPane createReservationPane_pop=new CreateReservationPane(1,reservation);
			Scene scene_pop=new Scene(createReservationPane_pop);
			stage_pop.setScene(scene_pop);
			stage_pop.show();

			createReservationPane_pop.getUpdateButton().addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

				@Override
				public void handle(MouseEvent arg0) {
					createReservationPane_pop.clickUpdate(reservation);
					refresh();
					stage_pop.close();
					stage_origin.close();
				}
			});
			createReservationPane_pop.getArrivalButton().addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

				@Override
				public void handle(MouseEvent arg0) {
					createReservationPane_pop.clickArrival(reservation);
					refresh();
					stage_pop.close();
					stage_origin.close();
				}
			});
			createReservationPane_pop.getAwayButton().addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

				@Override
				public void handle(MouseEvent arg0) {
					createReservationPane_pop.clickAway(reservation);
					refresh();
					stage_pop.close();
					stage_origin.close();
				}
			});
			createReservationPane_pop.getDeleteButton().addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

				@Override
				public void handle(MouseEvent arg0) {
					createReservationPane_pop.clickDelete(reservation);
					refresh();
					stage_pop.close();
				}
			});
			
		}
	}
	private void refresh(){
		reservationTable=new ReservationTable();
		calendarBorderPane=new CalendarBorderPane();
		vBox.getChildren().clear();
		vBox.getChildren().addAll(reservationTable,button_createReservation,calendarBorderPane);
		tableView=new TableView(1);
		hBox.getChildren().clear();
		hBox.getChildren().addAll(vBox,tableView);
		reservationTable.getTableView().addEventHandler(MouseEvent.MOUSE_CLICKED, new MyTableViewEventHandler(reservationTable));
		DayVBox[] dayVBoxs=calendarBorderPane.getMyCalendar().getDayVBox();

		for(i=0;i<42;i++){
			if(dayVBoxs[i].getDayLabel()==null||dayVBoxs[i].getDayLabel().getText().equals("")||dayVBoxs[i].getReservationLabel().getText().equals("0")){
				continue;
			}
			int year=dayVBoxs[i].getYear();
			int month=dayVBoxs[i].getMonth();
			int day=dayVBoxs[i].getDay();
			dayVBoxs[i].addEventHandler(MouseEvent.MOUSE_CLICKED, new MyDayVBoxMouseEventHandle(year, month, day));
		}
	}
	public Stage getStage_main() {
		return stage_main;
	}

	public void setStage_main(Stage stage_main) {
		this.stage_main = stage_main;
	}

	public static void main(String[] args) {
		MainUI.launch(args);
	}

}
