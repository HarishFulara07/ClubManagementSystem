package application.admin.login;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import application.admin.account.Admin_Home_Screen_Controller;
import application.global.MySQL;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Admin_Login_Screen_Controller
{
	@FXML
	private Button adminLoginSubmit,
				   adminLoginBack;
	@FXML
	private TextField adminEmail;
	@FXML
	private PasswordField adminPassword;
	@FXML
	private Text adminLoginError,
				 errorDetails;
	
	/*
	 * Authenticate admin's login credentials and load admin's home page (if credentials are valid)
	 * when "adminLoginSubmit" button is pressed 
	 */
	@FXML
	void adminLoginSubmitAction(ActionEvent event) throws IOException, SQLException
	{
		adminLoginError.setVisible(false);
		errorDetails.setVisible(false);
		errorDetails.setText("");
		
		if (saneInput())
		{
			/*
			 * The following code authenticates the admin's login credentials
			 */
			boolean authenticated = false;
			
			String email = adminEmail.getText().toString().trim();
			String password = adminPassword.getText().toString().trim();
			String query = "SELECT * "
						 + "FROM " + MySQL.TABLE_LOGIN_INFO + " AS A, " + MySQL.TABLE_CLUB_MEMBER_INFO + " AS B "
						 + "WHERE A.email = B.email "
						 		+ "AND A.email = '" + email + "' "
						 		+ "AND A.password = SHA('" + password + "') "
						 		+ "AND B.status = 'ADMIN'";
			
			MySQL.connect();
			
			Statement statement = MySQL.getStatement();
			ResultSet rs = statement.executeQuery (query);
			
			int size;
			rs.last();
			size = rs.getRow();
			rs.beforeFirst();
			
			rs.close();
			statement.close();
			MySQL.disconnect();
			
			if (size == 0)
			{
				authenticated = false;
				adminLoginError.setVisible(true);
			}
			else if (size >= 1)
			{
				authenticated = true;
			}
			else
			{
				authenticated = false;
				adminLoginError.setVisible(true);
				// DB ERROR HERE
			}
			
			if (authenticated)
			{
				// Open Admin Home Screen
				FXMLLoader loader = new FXMLLoader (getClass().getResource("/application/admin/account/Admin_Home_Screen.fxml"));
				Stage stage = new Stage(StageStyle.DECORATED);
				stage.setScene (new Scene (loader.load()));
				
				// Send current user's credentials to home_screen
				Admin_Home_Screen_Controller home_screen = loader.<Admin_Home_Screen_Controller>getController();
				home_screen.setDataFromEmail (email);
				
				// Show Home Screen
				stage.show();
				
				// Close current Window
				stage = (Stage) adminLoginSubmit.getScene().getWindow();
				stage.close();
			}
		}
	}
	
	/*
	 * Load home screen when "adminLoginBack" button is pressed 
	 */
	@FXML
	void homeScreenFromAdmin(ActionEvent event) throws IOException
	{
		FXMLLoader loader = new FXMLLoader (getClass().getResource("/application/home/Home_Screen.fxml"));
		Stage stage = new Stage(StageStyle.DECORATED);
		stage.setScene (new Scene (loader.load()));
		stage.show();
		
		stage = (Stage) adminLoginBack.getScene().getWindow();
		stage.close();
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
			errorDetails.setText(errorDetails.getText() + "EMAIL cannot be empty.\n");
		}
		else if (!TEMP.matches("[^@]+@[^@]+\\.[^@]+"))
		{
			sanity = false;
			errorDetails.setText(errorDetails.getText() + "EMAIL syntax is Invalid.\n");
		}
		
		TEMP = adminPassword.getText().toString();
		TEMP = TEMP.trim();
		if (TEMP == null || TEMP.isEmpty() || TEMP.equals(""))
		{
			sanity = false;
			errorDetails.setText(errorDetails.getText() + "Password cannot be empty.\n");
		}
		
		if (!sanity)
		{
			adminLoginError.setVisible(true);
			errorDetails.setVisible(true);
		}
		
		return sanity;
	}
}