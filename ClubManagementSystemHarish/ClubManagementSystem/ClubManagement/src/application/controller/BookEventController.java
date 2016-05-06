package application.controller;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class BookEventController {
	@FXML
	private Button 	cancelEventBooking, bookEvent;
	@FXML
	private TextField BookEventName, BookEventBudget;
	@FXML
	private ComboBox<Integer> BookEventTeamSize;
	@FXML
	private TextArea BookEventOrganisers, BookEventPrize;
	
	/*
	 * Load admin's account home screen when "cancelEventBooking" button is pressed, i.e, 
	 * admin cancels booking of event 
	 */
	@FXML
	void cancelEventBookingAction(ActionEvent event) throws IOException
	{
		Stage stage = (Stage) cancelEventBooking.getScene().getWindow();
		Parent root = FXMLLoader.load(getClass().getResource("/application/admin/account/AdminHomeScreen.fxml"));
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	/*
	 * Book an event when "bookEvent" button is pressed
	 */
	@FXML
	void bookEventAction(ActionEvent event) throws IOException
	{
		Stage stage = (Stage) bookEvent.getScene().getWindow();
		Parent root = FXMLLoader.load(getClass().getResource("/application/admin/account/AdminHomeScreen.fxml"));
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}	
}