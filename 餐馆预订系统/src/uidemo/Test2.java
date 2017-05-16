package uidemo;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.layout.BorderPane;

public class Test2 extends BorderPane {

	private Button button;
	public Test2() {
		button=new Button("弹窗");
		this.setCenter(button);
	}
	public Button getButton() {
		return button;
	}
	public void setButton(Button button) {
		this.button = button;
	}
	public void clickButton() {
		Alert alert = new Alert(Alert.AlertType.INFORMATION,"结账成功",new ButtonType("确定", ButtonData.YES));
		alert.show();
	}
}
