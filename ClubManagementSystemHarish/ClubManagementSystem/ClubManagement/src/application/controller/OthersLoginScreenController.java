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

public class OthersLoginScreenController {
	@FXML
	private Button othersLoginSubmit, othersLoginBack;
	@FXML
	private TextField othersEmail;
	@FXML
	private PasswordField othersPassword;
	@FXML
	private Text othersLoginError, errorDetails;
	protected static String email = null;
	
	/*
	 * Authenticate other user's login credentials and load other user's home page (if credentials are valid)
	 * when "othersLoginSubmit" button is pressed
	 */
	@FXML
	void othersLoginSubmitAction(ActionEvent event) throws IOException
	{
		othersLoginError.setVisible(false);
		errorDetails.setVisible(false);
		errorDetails.setText("");
		
		if (saneInput())
		{
			/*
			 * The following code authenticates the login credentials of other users
			 */
			Connection connection = null;
			Statement statement = null;
			ResultSet rs = null;
			
			boolean authenticated = false;
			
			email = othersEmail.getText().toString().trim();
			String password = othersPassword.getText().toString().trim();
			
			String query = "SELECT email FROM others_login_info WHERE email = '" + email + "'"
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
					othersLoginError.setVisible(true);
					othersEmail.setText("");
					othersPassword.setText("");
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
				Stage stage = (Stage) othersLoginSubmit.getScene().getWindow();
				Parent root = FXMLLoader.load(getClass().getResource("/application/others/account/OthersHomeScreen.fxml"));
				Scene scene = new Scene(root);
				stage.setScene(scene);
				stage.show();
			}
		}
	}
	
	/*
	 * Load home screen when "othersLoginBack" button is pressed 
	 */
	@FXML
	void homeScreenFromOthers(ActionEvent event) throws IOException
	{
		Stage stage = (Stage) othersLoginBack.getScene().getWindow();
		Parent root = FXMLLoader.load(getClass().getResource("/application/home/HomeScreen.fxml"));
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	// AUXILLIARY METHODS
	
	/*
	 * Performs Sanity Checks
	 * If anything found OUT OF ORDER shows the othersLoginError Text
	 */
	boolean saneInput() {
		boolean sanity = true;
		
		String TEMP;
		
		TEMP = othersEmail.getText().toString();
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
		
		TEMP = othersPassword.getText().toString();
		TEMP = TEMP.trim();
		if (TEMP == null || TEMP.isEmpty() || TEMP.equals(""))
		{
			sanity = false;
			errorDetails.setText(errorDetails.getText() + "Password cannot be empty\n");
		}
		
		if (!sanity)
		{
			othersLoginError.setVisible(true);
			errorDetails.setVisible(true);
		}
		
		return sanity;
	}	
}