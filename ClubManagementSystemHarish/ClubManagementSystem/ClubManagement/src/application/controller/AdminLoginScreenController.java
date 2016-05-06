package application.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import application.jdbc.JDBCMySQLConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class AdminLoginScreenController {
	
	@FXML
	private Button adminLoginSubmit, adminLoginBack;
	@FXML
	private TextField adminEmail;
	@FXML
	private PasswordField adminPassword;
	@FXML
	private Text adminLoginError, errorDetails;
	protected static String email = null;
	
	/*
	 * Authenticate admin's login credentials and load admin's home page (if credentials are valid)
	 * when "adminLoginSubmit" button is pressed 
	 */
	@FXML
	void adminLoginSubmitAction(ActionEvent event) throws IOException
	{
		adminLoginError.setVisible(false);
		errorDetails.setVisible(false);
		errorDetails.setText("");
		
		if (saneInput())
		{
			/*
			 * The following code authenticates the admin's login credentials
			 */
			Connection connection = null;
			Statement statement = null;
			ResultSet rs = null;
			
			boolean authenticated = false;
			
			email = adminEmail.getText().toString().trim();
			String password = adminPassword.getText().toString().trim();
			
			String query = "SELECT email FROM login_info WHERE email = '" + email + "'"
						+ " AND password = SHA1('" + password + "')";
			
			try {			
				
				connection = JDBCMySQLConnection.getConnection();
				statement = connection.createStatement();
				rs = statement.executeQuery(query);
				
				if (rs.next()) {
					email = rs.getString("email");
					authenticated = true;
				}
				else
				{
					adminLoginError.setVisible(true);
					adminEmail.setText("");
					adminPassword.setText("");
				}
				
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
			 * The following code redirects the admin to his account's home screen after he gets authenticated
			 */
			if(authenticated)
			{
				Stage stage = (Stage) adminLoginSubmit.getScene().getWindow();
				Parent root = FXMLLoader.load(getClass().getResource("/application/admin/account/AdminHomeScreen.fxml"));
				Scene scene = new Scene(root);
				stage.setScene(scene);
				stage.show();
			}
		}
	}
	
	/*
	 * Load home screen when "adminLoginBack" button is pressed 
	 */
	@FXML
	void homeScreenFromAdmin(ActionEvent event) throws IOException
	{
		Stage stage = (Stage) adminLoginBack.getScene().getWindow();
		Parent root = FXMLLoader.load(getClass().getResource("/application/home/HomeScreen.fxml"));
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	// AUXILLIARY METHODS
	
	/*
	 * Performs Sanity Checks
	 * If anything found OUT OF ORDER shows the adminLoginError Text
	 */
	boolean saneInput() {
		boolean sanity = true;
		
		String TEMP;
		
		TEMP = adminEmail.getText().toString();
		TEMP = TEMP.trim();
		if (TEMP == null || TEMP.isEmpty() || TEMP.equals(""))
		{
			sanity = false;
			errorDetails.setText(errorDetails.getText() + "EMAIL cannot be empty\n");
		}
		else if (!TEMP.matches("[^@]+@[^@]+\\.[^@]+"))
		{
			sanity = false;
			errorDetails.setText(errorDetails.getText() + "EMAIL syntax is invalid\n");
		}
		
		TEMP = adminPassword.getText().toString();
		TEMP = TEMP.trim();
		if (TEMP == null || TEMP.isEmpty() || TEMP.equals(""))
		{
			sanity = false;
			errorDetails.setText(errorDetails.getText() + "Password cannot be empty\n");
		}
		
		if (!sanity)
		{
			adminLoginError.setVisible(true);
			errorDetails.setVisible(true);
		}
		
		return sanity;
	}
}