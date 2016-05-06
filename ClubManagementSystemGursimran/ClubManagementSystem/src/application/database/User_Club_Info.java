package application.database;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class User_Club_Info {
	public String first_name,
				  last_name,
				  sex,
				  profession,
				  email;
	public int roll_number;
	public String club_name,
				  discipline,
				  description,
				  status;
	
	public StringProperty first_name_property() {
		return new SimpleStringProperty (first_name);
	}
	
	public StringProperty last_name_property() {
		return new SimpleStringProperty (last_name);
	}
	
	public StringProperty sex_property() {
		return new SimpleStringProperty (sex);
	}
	
	public StringProperty profession_property() {
		return new SimpleStringProperty (profession);
	}
	
	public StringProperty email_property() {
		return new SimpleStringProperty (email);
	}
	
	public StringProperty roll_number_property() {
		return new SimpleStringProperty (Integer.toString(roll_number));
	}
	
	public StringProperty club_name_property() {
		return new SimpleStringProperty (club_name);
	}
	
	public StringProperty discipline_property() {
		return new SimpleStringProperty (discipline);
	}
	
	public StringProperty description_property() {
		return new SimpleStringProperty (description);
	}
	
	public StringProperty status_property() {
		return new SimpleStringProperty (status);
	}
}