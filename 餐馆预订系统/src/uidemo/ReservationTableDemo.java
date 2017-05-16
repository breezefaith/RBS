package uidemo;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ReservationTableDemo extends Application{

	private ReservationTable reservationTable;
	@Override
	public void start(Stage arg0) throws Exception {
		reservationTable=new ReservationTable();
		Scene scene=new Scene(reservationTable);
		arg0.setScene(scene);
		arg0.show();
	}
	public static void main(String[] args) {
		Application.launch(args);
	}
	
}
