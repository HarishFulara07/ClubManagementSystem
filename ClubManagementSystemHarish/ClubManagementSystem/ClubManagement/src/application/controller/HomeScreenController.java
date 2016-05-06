package application.controller;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class HomeScreenController {
	
	@FXML
	private Button adminLogin, othersLogin, adminRegistration, othersRegistration;
	
	/*
	 * Load admin's login screen when "adminLogin" button is pressed 
	 */
	@FXML
	void adminLoginScreen(ActionEvent event) throws IOException
	{
		Stage stage = (Stage) adminLogin.getScene().getWindow();
		Parent root = FXMLLoader.load(getClass().getResource("/application/admin/login/AdminLoginScreen.fxml"));
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	/*
	 * Load others login screen when "othersLogin" button is pressed 
	 */
	@FXML
	void othersLoginScreen(ActionEvent event) throws IOException
	{
		Stage stage = (Stage) othersLogin.getScene().getWindow();
		Parent root = FXMLLoader.load(getClass().getResource("/application/others/login/OthersLoginScreen.fxml"));
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	/*
	 * Load admin's registration screen when "adminRegistration" button is pressed 
	 */
	@FXML
	void adminRegistrationScreen(ActionEvent event) throws IOException
	{
		Stage stage = (Stage) adminRegistration.getScene().getWindow();
		Parent root = FXMLLoader.load(getClass().getResource("/application/admin/registration/AdminRegistrationForm.fxml"));
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	/*
	 * Load others registration screen when "othersRegistration" button is pressed 
	 */
	@FXML
	void othersRegistrationScreen(ActionEvent event) throws IOException
	{
		Stage stage = (Stage) othersRegistration.getScene().getWindow();
		Parent root = FXMLLoader.load(getClass().getResource("/application/others/registration/OthersRegistrationForm.fxml"));
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
}