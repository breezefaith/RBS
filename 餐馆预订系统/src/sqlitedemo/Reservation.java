package sqlitedemo;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.Button;

public class Reservation {
	private final SimpleIntegerProperty oid;
	private final SimpleIntegerProperty covers;
	private final SimpleIntegerProperty tableNumber;
	private final SimpleStringProperty date;
	private final SimpleStringProperty time;
	private final SimpleStringProperty name;
	private final SimpleStringProperty phoneNumber;
	private final SimpleStringProperty type;
	private final SimpleStringProperty state;
	
	private final Button button_arrival;
	private final Button button_away;
	
	public Reservation() {
		oid=new SimpleIntegerProperty(-1);
		covers=new SimpleIntegerProperty(-1);
		tableNumber=new SimpleIntegerProperty(-1);
		date=new SimpleStringProperty();
		time=new SimpleStringProperty();
		name=new SimpleStringProperty();
		phoneNumber=new SimpleStringProperty();
		type=new SimpleStringProperty();
		state=new SimpleStringProperty();
		
		button_arrival=new Button("到达");
		button_away=new Button("结账");
	}
	public Reservation(int oid,int covers,int tableNumber,String date,String time,String name,String phoneNumber,String type,String state){
		this.oid=new SimpleIntegerProperty(oid);
		this.covers=new SimpleIntegerProperty(covers);
		this.tableNumber=new SimpleIntegerProperty(tableNumber);
		this.date=new SimpleStringProperty(date);
		this.time=new SimpleStringProperty(time);
		this.name=new SimpleStringProperty(name);
		this.phoneNumber=new SimpleStringProperty(phoneNumber);
		
		this.type=new SimpleStringProperty(type);
		this.state=new SimpleStringProperty(state);
		button_arrival=new Button("到达");
		button_away=new Button("结账");
	}
	
	public int getOid() {
		return oid.get();
	}
	public void setOid(int oid) {
		this.oid.set(oid);
	}
	public int getCovers() {
		return covers.get();
	}
	public void setCovers(int covers) {
		this.covers.set(covers);
	}
	public int getTableNumber() {
		return tableNumber.get();
	}
	public void setTableNumber(int tableNumber) {
		this.tableNumber.set(tableNumber);
	}
	public String getDate() {
		return date.get();
	}
	public void setDate(String date) {
		this.date.set(date);
	}
	public String getTime() {
		return time.get();
	}
	public void setTime(String time) {
		this.time.set(time);
	}
	public String getName() {
		return name.get();
	}
	public void setName(String name) {
		this.name.set(name);
	}
	public String getPhoneNumber() {
		return phoneNumber.get();
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber.set(phoneNumber);
	}
	public String getType() {
		return type.get();
	}
	public void setType(String type){
		this.type.set(type);
	}
	public String getState() {
		return state.get();
	}
	public void setState(String state){
		this.state.set(state);
	}
	public Button getButton_arrival() {
		return button_arrival;
	}
	public Button getButton_away() {
		return button_away;
	}
}
