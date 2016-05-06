package application.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import application.jdbc.JDBCMySQLConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

public class OthersRegistrationFormController {
	@FXML
	private Button othersRegSubmit, othersRegBack;
	@FXML
	private TextField othersRegFirstName, othersRegLastName, othersRegRollNum, othersRegClub, othersRegEmail,
						othersRegPassword, othersRegConfirmPassword;
	@FXML
	private RadioButton othersRegMale, othersRegFemale, othersRegFaculty, othersRegStudent, othersRegOther;
	@FXML
	private ToggleGroup othersRegSex, othersRegPro;
	
	/*
	 * Submit others registration form when "othersRegSubmit" button is pressed 
	 */
	@FXML
	void othersRegSubmitAction(ActionEvent event) throws IOException
	{
		/*
		 * The following code inserts the others form data into the database
		 */
		Connection connection = null;
		Statement statement = null;
		
		String first_name = othersRegFirstName.getText().toString().trim();
		String last_name = othersRegLastName.getText().toString().trim();
		String sex = null;
		String profession = null;
		Integer roll_number = null;
		String email = othersRegEmail.getText().toString().trim();
		String password = othersRegPassword.getText().toString().trim();
		
		if(othersRegMale.isSelected())
		{
			sex = "Male";
		}
		else
		{
			sex = "Female";
		}
		
		if(othersRegFaculty.isSelected())
		{
			profession = "Faculty";
		}
		else if(othersRegStudent.isSelected())
		{
			profession = "Student";
		}
		else
		{
			profession = "Other";
		}
		
		String ins1;
		
		if(othersRegRollNum.getText().trim().compareTo("") != 0)
		{	
			roll_number = Integer.parseInt(othersRegRollNum.getText());
			ins1 = "INSERT INTO user_info (first_name, last_name, sex, profession, roll_number, email) VALUES"
					+ " ('" + first_name + "', '" + last_name + "', '" + sex + "', '" + profession + "', '"
					+ roll_number + "', '" + email + "')";
		}
		else
		{
			ins1 = "INSERT INTO user_info (first_name, last_name, sex, profession, email) VALUES"
					+ " ('" + first_name + "', '" + last_name + "', '" + sex + "', '" + profession 
					+ "', '" + email + "')";
		}
		
		String ins2 = "INSERT INTO others_login_info (email, password) VALUES ('" + email + "', SHA1 ('" 
					+ password + "'))";
		
		try {			
			
			connection = JDBCMySQLConnection.getConnection();
			statement = connection.createStatement();
			
			//execute insert statements and insert values into the database
			statement.executeUpdate(ins1);
			statement.executeUpdate(ins2);
			
		} catch (SQLException e) {
			
			e.printStackTrace();
			
		} finally {
			
			if (connection != null) {
				
				try {
					/*
					 * Closing the Connection object will also close Statement object as well.
					 * However, we should always explicitly close the Statement object to ensure proper cleanup.
					 */
					statement.close();
					connection.close();
				
				} catch (SQLException e) {
					
					e.printStackTrace();
				
				}
			}
		}
		
		Stage stage = (Stage) othersRegSubmit.getScene().getWindow();
		Parent root = FXMLLoader.load(getClass().getResource("/application/home/HomeScreen.fxml"));
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	/*
	 * Load home screen when "othersRegBack" button is pressed 
	 */
	@FXML
	void homeScreenFromOthersReg(ActionEvent event) throws IOException
	{
		Stage stage = (Stage) othersRegBack.getScene().getWindow();
		Parent root = FXMLLoader.load(getClass().getResource("/application/home/HomeScreen.fxml"));
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
}