package application.others.login;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import application.global.MySQL;
import application.others.account.Others_Home_Screen_Controller;
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

public class Others_Login_Screen_Controller
{
	@FXML
	private Button othersLoginSubmit,
				   othersLoginBack;
	@FXML
	private TextField othersEmail;
	@FXML
	private PasswordField othersPassword;
	@FXML
	private Text othersLoginError,
				 errorDetails;
	
	/*
	 * Authenticate admin's login credentials and load admin's home page (if credentials are valid)
	 * when "adminLoginSubmit" button is pressed 
	 */
	@FXML
	void othersLoginSubmitAction(ActionEvent event) throws IOException, SQLException
	{
		othersLoginError.setVisible(false);
		errorDetails.setVisible(false);
		errorDetails.setText("");
		
		if (saneInput())
		{
			/*
			 * The following code authenticates the admin's login credentials
			 */
			boolean authenticated = false;
			
			String email = othersEmail.getText().toString().trim();
			String password = othersPassword.getText().toString().trim();
			String query = "SELECT * "
						 + "FROM " + MySQL.TABLE_LOGIN_INFO + " AS A "
						 + "WHERE A.email = \"" + email + "\" "
						 	   + "AND A.password = SHA(\"" + password + "\")";
			
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
				othersLoginError.setVisible(true);
			}
			else if (size >= 1)
			{
				authenticated = true;
			}
			else
			{
				authenticated = false;
				othersLoginError.setVisible(true);
				// DB ERROR HERE
			}
			
			if (authenticated)
			{
				// Open Others Home Screen
				FXMLLoader loader = new FXMLLoader (getClass().getResource("/application/others/account/Others_Home_Screen.fxml"));
				Stage stage = new Stage(StageStyle.DECORATED);
				stage.setScene (new Scene (loader.load()));
				
				// Send current user's credentials to home_screen
				Others_Home_Screen_Controller home_screen = loader.<Others_Home_Screen_Controller>getController();
				home_screen.setDataFromEmail (email);
				
				// Show Home Screen
				stage.show();
				
				// Close current Window
				stage = (Stage) othersLoginSubmit.getScene().getWindow();
				stage.close();
			}
		}
	}
	
	/*
	 * Load home screen when "adminLoginBack" button is pressed 
	 */
	@FXML
	void homeScreenFromOthers(ActionEvent event) throws IOException
	{
		FXMLLoader loader = new FXMLLoader (getClass().getResource("/application/home/Home_Screen.fxml"));
		Stage stage = new Stage(StageStyle.DECORATED);
		stage.setScene (new Scene (loader.load()));
		stage.show();
		
		stage = (Stage) othersLoginBack.getScene().getWindow();
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
		
		TEMP = othersEmail.getText().toString();
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
		
		TEMP = othersPassword.getText().toString();
		TEMP = TEMP.trim();
		if (TEMP == null || TEMP.isEmpty() || TEMP.equals(""))
		{
			sanity = false;
			errorDetails.setText(errorDetails.getText() + "Password cannot be empty.\n");
		}
		
		if (!sanity)
		{
			othersLoginError.setVisible(true);
			errorDetails.setVisible(true);
		}
		
		return sanity;
	}
}