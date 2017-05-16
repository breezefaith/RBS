package sqlitedemo;

import javafx.beans.property.SimpleIntegerProperty;

public class Table {
	private final SimpleIntegerProperty number;
	private final SimpleIntegerProperty place;
	public Table() {
		number=new SimpleIntegerProperty();
		place=new SimpleIntegerProperty();
	}
	public Table(int num,int pla){
		number=new SimpleIntegerProperty(num);
		place=new SimpleIntegerProperty(pla);
	}
	public void setNumber(int num){
		number.set(num);
	}
	public int getNumber() {
		return number.get();
	}
	public void setPlace(int pla){
		place.set(pla);
	}
	public int getPlace() {
		return place.get();
	}
	
}
