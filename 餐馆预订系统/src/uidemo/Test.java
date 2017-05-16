package uidemo;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Test extends Application {

	private BorderPane borderPane;
	private Scene scene;
	private Button button;
	private Stage stage_main;
	@Override
	public void start(Stage arg0) throws Exception {
		stage_main=arg0;
		borderPane = new BorderPane();
		scene = new Scene(borderPane,400,300);

		button = new Button("测试");
		button.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>(){

			@Override
			public void handle(MouseEvent arg0) {
				Stage stage_pop=new Stage();
				Test2 test2=new Test2();
				Scene scene_pop=new Scene(test2);
				
				stage_pop.setScene(scene_pop);
				stage_pop.show();
				test2.getButton().addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

					@Override
					public void handle(MouseEvent e) {
						test2.clickButton();
						stage_main.show();
					}
				});
			}

		});

		borderPane.setCenter(button);

		arg0.setScene(scene);
		arg0.show();
	}
	public static void main(String[] args) {
		Test.launch(args);
	}

}
