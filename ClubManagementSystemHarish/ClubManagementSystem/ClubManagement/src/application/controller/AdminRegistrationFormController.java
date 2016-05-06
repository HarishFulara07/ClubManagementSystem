package application.controller;

import java.sql.*;

import java.io.IOException;

import java.net.URL;
import java.util.ResourceBundle;

import application.jdbc.JDBCMySQLConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

public class AdminRegistrationFormController implements Initializable{
	@FXML
	private Button adminRegSubmit, adminRegBack;
	@FXML
	private TextField adminRegFirstName, adminRegLastName, adminRegRollNum, adminRegEmail,
						adminRegPassword, adminRegConfirmPassword;
	@FXML
	private RadioButton adminRegMale, adminRegFemale, adminRegFaculty, adminRegStudent, adminRegOther;
	@FXML
	private ToggleGroup adminRegSex, adminRegPro;
	@FXML
	private ComboBox<String> adminRegClub;
	
	@Override
    public void initialize(URL location, ResourceBundle resources)
    {
		adminRegClub.getItems().clear();

		adminRegClub.getItems().addAll(
		            "Astronuts",
		            "Audio Bytes",
		            "Byld",
		            "Dramatis Personae",
		            "Eco Club",
		            "Electroholics",
		            "Foobar",
		            "Game Craft",
		            "Ink",
		            "Madtoes",
		            "PhiloSoc",
		            "Prodigious Painters",
		            "Robotics Club",
		            "Tasveer",
		            "The 65th Square",
		            "Trivialis",
		            "Voix de Literati"
		            );
    }
	
	/*
	 * Submit admin's registration form when "adminRegSubmit" button is pressed 
	 */
	@FXML
	void adminRegSubmitAction(ActionEvent event) throws IOException
	{	
		/*
		 * The following code inserts the admin form data into the database
		 */
		Connection connection = null;
		Statement statement = null;
		
		String first_name = adminRegFirstName.getText().toString().trim();
		String last_name = adminRegLastName.getText().toString().trim();
		String sex = null;
		String profession = null;
		Integer roll_number = null;
		String email = adminRegEmail.getText().toString().trim();
		String club_name = adminRegClub.getValue().toString().trim();
		String password = adminRegPassword.getText().toString().trim();
		
		if(adminRegMale.isSelected())
		{
			sex = "Male";
		}
		else
		{
			sex = "Female";
		}
		
		if(adminRegFaculty.isSelected())
		{
			profession = "Faculty";
		}
		else if(adminRegStudent.isSelected())
		{
			profession = "Student";
		}
		else
		{
			profession = "Other";
		}
		
		String ins1;
		
		if(adminRegRollNum.getText().trim().compareTo("") != 0)
		{	
			roll_number = Integer.parseInt(adminRegRollNum.getText());
			ins1 = "INSERT INTO user_info (first_name, last_name, sex, profession, roll_number, email) VALUES"
					+ " ('" + first_name + "', '" + last_name + "', '" + sex + "', '" + profession + "', '"
					+ roll_number + "', '" + email + "')";
		}
		else
		{
			ins1 = "INSERT INTO user_info (first_name, last_name, sex, profession, email) VALUES"
					+ " ('" + first_name + "', '" + last_name + "', '" + sex + "', '" + profession + "', '"
					+ email + "')";
		}
		
		
		String ins2 = "INSERT INTO club_admin_info (club_name, email) VALUES ('" + club_name + "', '" + email + "')";
		String ins3 = "INSERT INTO admin_login_info (email, password) VALUES ('" + email + "', SHA1 ('" 
					+ password + "'))";
		
		try {			
			
			connection = JDBCMySQLConnection.getConnection();
			statement = connection.createStatement();
			
			//execute insert statements and insert values into the database
			statement.executeUpdate(ins1);
			statement.executeUpdate(ins2);
			statement.executeUpdate(ins3);
			
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
		
		/*
		 * The following code redirects the admin to home screen after he submits the form
		 */
		Stage stage = (Stage) adminRegSubmit.getScene().getWindow();
		Parent root = FXMLLoader.load(getClass().getResource("/application/home/HomeScreen.fxml"));
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	/*
	 * Load home screen when "adminRegBack" button is pressed 
	 */
	@FXML
	void homeScreenFromAdminReg(ActionEvent event) throws IOException
	{
		Stage stage = (Stage) adminRegBack.getScene().getWindow();
		Parent root = FXMLLoader.load(getClass().getResource("/application/home/HomeScreen.fxml"));
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
}