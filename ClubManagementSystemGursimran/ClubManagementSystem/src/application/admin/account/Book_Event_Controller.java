package application.admin.account;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

import application.database.Event_Info;
import application.database.User_Info;
import application.global.MySQL;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Book_Event_Controller implements Initializable {
	@FXML
	private Button 	cancelEventBooking,
					bookEvent;
	@FXML
	private TextField BookEventName,
					  BookEventBudget;
	@FXML
	private ComboBox<Integer> BookEventTeamSize;
	@FXML
	private ComboBox<String> BookEventOrganisingClub;
	@FXML
	private TextArea BookEventOrganisers,
					 BookEventPrize,
					 BookEventDescription;
	@FXML
	private Text BookEventError,
				 errorDetails;
	
	private Event_Info currentEvent;
	private User_Info currentUser;
	
	// ******************************************************************************************************************************* //
	
	private ObservableList<Integer> teamSizeOptions = FXCollections.observableArrayList (1, 2, 3, 4, 5,
																						 6, 7, 8, 9, 10);
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		BookEventTeamSize.setItems (teamSizeOptions);
	}
	
	public void setData (Event_Info currentEvent, User_Info currentUser)
	{
		this.currentEvent = currentEvent;
		this.currentUser = currentUser;
		BookEventOrganisingClub.setItems (FXCollections.observableArrayList (currentUser.club_name));
	}
	
	/*
	 * Load admin's account home screen when "cancelEventBooking" button is pressed, i.e, 
	 * Admin cancels booking of event 
	 */
	@FXML
	void cancelEventBookingAction(ActionEvent event) throws IOException, SQLException
	{
		currentEvent = null;
		
		FXMLLoader loader = new FXMLLoader (getClass().getResource("/application/admin/account/Admin_Home_Screen.fxml"));
		Stage stage = new Stage(StageStyle.DECORATED);
		stage.setScene (new Scene (loader.load()));
		Admin_Home_Screen_Controller ahsc = loader.<Admin_Home_Screen_Controller>getController();
		ahsc.setDataFromUserInfo(currentUser);
		stage.show();
		
		stage = (Stage) cancelEventBooking.getScene().getWindow();
		stage.close();
	}
	
	/*
	 * Book an event when "bookEvent" button is pressed
	 */
	@FXML
	void bookEventAction(ActionEvent event) throws IOException, SQLException
	{
		BookEventError.setVisible(false);
		errorDetails.setVisible(false);
		errorDetails.setText("");
		
		if (saneInput())
		{
			currentEvent.event_name = BookEventName.getText().toString().trim();
			currentEvent.description = BookEventDescription.getText().toString().trim();
			currentEvent.organising_club = BookEventOrganisingClub.getValue().toString().trim();
			currentEvent.organisers = BookEventOrganisers.getText().toString().trim();
			currentEvent.prize =  BookEventPrize.getText().toString().trim();
			currentEvent.team_size = BookEventTeamSize.getValue().intValue();
			currentEvent.budget = Integer.parseInt (BookEventBudget.getText().toString().trim());
			
			MySQL.connect();
			
			Statement statement = MySQL.getStatement();
			statement.executeUpdate ("INSERT INTO " + MySQL.TABLE_EVENT_DETAILS + " ("
															  + "event_name,"
															  + "event_start_time,"
															  + "event_end_time,"
															  + "event_venue,"
															  + "description,"
															  + "organising_club,"
															  + "organisers,"
															  + "team_size,"
															  + "budget,"
															  + "prize"
															  + ") "
								  + "VALUES ("
								  		   + "\"" + currentEvent.event_name + "\", "
								  		   + "\"" + currentEvent.event_start_time + "\", "
								  		   + "\"" + currentEvent.event_end_time + "\", "
								  		   + "\"" + currentEvent.event_venue + "\", "
								  		   + "\"" + currentEvent.description + "\", "
								  		   + "\"" + currentEvent.organising_club + "\", "
								  		   + "\"" + currentEvent.organisers + "\", "
								  		   		 + currentEvent.team_size + ", "
								  		   		 + currentEvent.budget + ", "
								  		   + "\"" + currentEvent.prize + "\")" );
			statement.close();
			
			MySQL.disconnect();
		}
		
		currentEvent = null;
		
		FXMLLoader loader = new FXMLLoader (getClass().getResource("/application/admin/account/Admin_Home_Screen.fxml"));
		Stage stage = new Stage(StageStyle.DECORATED);
		stage.setScene (new Scene (loader.load()));
		Admin_Home_Screen_Controller ahsc = loader.<Admin_Home_Screen_Controller>getController();
		ahsc.setDataFromEmail (currentUser.email);
		stage.show();
		
		stage = (Stage) bookEvent.getScene().getWindow();
		stage.close();
	}
	
	// AUXILLIARY METHODS
	
	boolean saneInput() {
		boolean sanity = true;
		
		String TEMP;
		
		TEMP = BookEventName.getText().toString().trim();
		if (TEMP == null || TEMP.isEmpty() || TEMP.equals(""))
		{
			sanity = false;
			errorDetails.setText(errorDetails.getText() + "EVENT NAME cannot be Empty.\n");
		}
		
		TEMP = BookEventOrganisers.getText().toString().trim();
		if (TEMP == null || TEMP.isEmpty() || TEMP.equals(""))
		{
			sanity = false;
			errorDetails.setText(errorDetails.getText() + "ORGANISERS cannot be Empty.\n");
		}
		
		TEMP = BookEventTeamSize.getValue().toString();
		if (TEMP == null || TEMP.isEmpty() || TEMP.equals(""))
		{
			sanity = false;
			errorDetails.setText(errorDetails.getText() + "TEAM SIZE cannot be Empty.\n");
		}
		
		TEMP = BookEventBudget.getText().toString().trim();
		if (TEMP == null || TEMP.isEmpty() || TEMP.equals(""))
		{
			sanity = false;
			errorDetails.setText(errorDetails.getText() + "BUDGET cannot be Empty.\n");
		}
		
		TEMP = BookEventPrize.getText().toString().trim();
		if (TEMP == null || TEMP.isEmpty() || TEMP.equals(""))
		{
			sanity = false;
			errorDetails.setText(errorDetails.getText() + "PRIZE cannot be Empty.\n");
		}
		
		if (!sanity)
		{
			BookEventError.setVisible(true);
			errorDetails.setVisible(true);
		}
		
		return sanity;
	}
}