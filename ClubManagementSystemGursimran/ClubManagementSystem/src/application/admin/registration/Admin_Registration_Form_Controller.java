package application.admin.registration;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

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
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Admin_Registration_Form_Controller implements Initializable {
	@FXML
	private Button adminRegSubmit,
				   adminRegBack;
	@FXML
	private TextField adminRegFirstName,
					  adminRegLastName,
					  adminRegRollNum,
					  adminRegEmail,
					  adminRegPassword,
					  adminRegConfirmPassword;
	@FXML
	private ComboBox<String> adminRegClub;
	@FXML
	private RadioButton adminRegMale,
						adminRegFemale,
						adminRegFaculty,
						adminRegStudent,
						adminRegOther;
	@FXML
	private Text adminRegError,
				 errorDetails;
	
	// ********************************************************************************************************** //
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		try
		{
			adminRegClub.setItems (giveAllClubs());
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}
	
	/*
	 * Method returns all the clubs present in clubs_info table
	 */
	private ObservableList<String> giveAllClubs () throws SQLException {
		ObservableList<String> clubNames = FXCollections.observableArrayList ();
		
		MySQL.connect();
		Statement statement = MySQL.getStatement();
		ResultSet rs = statement.executeQuery ("SELECT club_name "
											 + "FROM " + MySQL.TABLE_CLUB_INFO + "");
		while (rs.next())
		{
			clubNames.add (rs.getString("club_name"));
		}
		rs.close();
		statement.close();
		MySQL.disconnect();
		
		return clubNames;
	}
	
	/*
	 * Submit admin's registration form when "adminRegSubmit" button is pressed 
	 */
	@FXML
	void adminRegSubmitAction(ActionEvent event) throws IOException
	{
		adminRegError.setVisible(false);
		errorDetails.setVisible(false);
		errorDetails.setText("");
		
		if (saneInput())
		{	
			adminRegSubmit.setDisable(true);
			adminRegBack.setDisable(true);
			
			/*
			 * The following code inserts the admin form data into the database
			 */
			Statement statement = null;
			
			String first_name = adminRegFirstName.getText().toString().trim();
			String last_name = adminRegLastName.getText().toString().trim();
			String sex = null;
			String profession = null;
			Integer roll_number = Integer.parseInt(adminRegRollNum.getText());
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
			
			String ins1 = null;
			
			ins1 = "INSERT INTO " + MySQL.TABLE_USER_INFO + " ("
														  + "first_name, "
														  + "last_name, "
														  + "sex, "
														  + "profession, "
														  + "roll_number, "
														  + "email"
										  + ") VALUES ("
										  			+ "\"" + first_name + "\", "
										  			+ "\"" + last_name + "\", "
										  			+ "\"" + sex + "\", "
										  			+ "\"" + profession + "\", "
										  			+ "\"" + roll_number + "\", "
										  			+ "\"" + email + "\""
										  + ")";		
			
			String ins2 = "INSERT INTO " + MySQL.TABLE_CLUB_MEMBER_INFO + " ("
																		+ "club_name, "
																		+ "status, "
																		+ "email )"
														+ " VALUES ("
																 + "\"" + club_name + "\", "
																 + "\"ADMIN\", "
																 + "\"" + email + "\")";
			
			String ins3 = "INSERT INTO " + MySQL.TABLE_LOGIN_INFO + " ("
																  + "email, "
																  + "password"
													+ ") VALUES ("  
																  + "\"" + email + "\", "
																  + "SHA(\"" + password + "\")"
													+ ")";
			
			MySQL.connect();
			try {			
				
				statement = MySQL.getStatement();
				
				//execute insert statements and insert values into the database
				statement.executeUpdate(ins1);
				statement.executeUpdate(ins2);
				statement.executeUpdate(ins3);
				
			} catch (SQLException e) {
				adminRegError.setVisible(true);
			}
			MySQL.disconnect();
			
			/*
			 * The following code redirects the Admin to home screen after he submits the form
			 */
			FXMLLoader loader = new FXMLLoader (getClass().getResource("/application/home/Home_Screen.fxml"));
			Stage stage = new Stage(StageStyle.DECORATED);
			stage.setScene (new Scene (loader.load()));
			stage.show();
			stage = (Stage) adminRegSubmit.getScene().getWindow();
			stage.close();
		}
	}
	
	/*
	 * Load home screen when "adminRegBack" button is pressed 
	 */
	@FXML
	void homeScreenFromAdminReg(ActionEvent event) throws IOException
	{
		FXMLLoader loader = new FXMLLoader (getClass().getResource("/application/home/Home_Screen.fxml"));
		Stage stage = new Stage(StageStyle.DECORATED);
		stage.setScene (new Scene (loader.load()));
		stage.show();
		
		stage = (Stage) adminRegBack.getScene().getWindow();
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
		
		TEMP = adminRegFirstName.getText().toString();
		TEMP = TEMP.trim();
		if (TEMP == null || TEMP.isEmpty() || TEMP.equals(""))
		{
			sanity = false;
			errorDetails.setText(errorDetails.getText() + "FIRSTNAME cannot be empty\n");
		}
		else
		{
			for (int i = 0; i < TEMP.length(); i++)
				if (Character.isDigit(TEMP.charAt(i)))
				{
					sanity = false;
					errorDetails.setText(errorDetails.getText() + "FIRSTNAME cannot contain digits\n");
					break;
				}
		}
		
		TEMP = adminRegLastName.getText().toString();
		TEMP = TEMP.trim();
		if (TEMP == null || TEMP.isEmpty() || TEMP.equals(""))
		{
			sanity = false;
			errorDetails.setText(errorDetails.getText() + "LASTNAME cannot be empty\n");
		}
		else
		{
			for (int i = 0; i < TEMP.length(); i++)
				if (Character.isDigit(TEMP.charAt(i)))
				{
					sanity = false;
					errorDetails.setText(errorDetails.getText() + "LASTNAME cannot contain digits\n");
					break;
				}
		}
		
		if (!adminRegMale.isSelected() && !adminRegFemale.isSelected())
		{
			sanity = false;
			errorDetails.setText(errorDetails.getText() + "No Sex is Selected\n");
		}
		
		if (!adminRegFaculty.isSelected() && !adminRegStudent.isSelected() && !adminRegOther.isSelected())
		{
			sanity = false;
			errorDetails.setText(errorDetails.getText() + "No Profession is Selected\n");
		}
		
		TEMP = adminRegRollNum.getText().toString();
		TEMP = TEMP.trim();
		if (TEMP == null || TEMP.isEmpty() || TEMP.equals(""))
		{
			sanity = false;
			errorDetails.setText(errorDetails.getText() + "ROLLNUMBER cannot be empty\n");
		}
		else
		{
			for (int i = 0; i < TEMP.length(); i++)
				if (!Character.isDigit(TEMP.charAt(i)))
				{
					sanity = false;
					errorDetails.setText(errorDetails.getText() + "ROLLNUMBER can only contain digits\n");
					break;
				}
		}
		
		TEMP = adminRegClub.getValue();
		if (TEMP == null || TEMP.isEmpty() || TEMP.equals(""))
		{
			sanity = false;
			errorDetails.setText(errorDetails.getText() + "CLUBNAME cannot be empty\n");
		}
		
		TEMP = adminRegEmail.getText().toString();
		TEMP = TEMP.trim();
		if (TEMP == null || TEMP.isEmpty() || TEMP.equals(""))
		{
			sanity = false;
			errorDetails.setText(errorDetails.getText() + "EMAIL cannot be empty\n");
		}
		else if (!TEMP.matches("[^@]+@[^@]+\\.[^@]+"))
		{
			sanity = false;
			errorDetails.setText(errorDetails.getText() + "EMAIL syntax is Invalid\n");
		}
		
		TEMP = adminRegPassword.getText().toString();
		TEMP = TEMP.trim();
		if (TEMP == null || TEMP.isEmpty() || TEMP.equals(""))
		{
			sanity = false;
			errorDetails.setText(errorDetails.getText() + "PASSWORD cannot be empty\n");
		}
		
		String TEMP1 = adminRegConfirmPassword.getText().toString();
		TEMP1 = TEMP1.trim();
		if (TEMP1 == null || TEMP1.isEmpty() || TEMP1.equals(""))
		{
			sanity = false;
			errorDetails.setText(errorDetails.getText() + "CONFIRM PASSWORD cannot be empty\n");
		}
		
		if (TEMP.compareTo(TEMP1) != 0)
		{
			sanity = false;
			errorDetails.setText(errorDetails.getText() + "PASSWORD and COMFIRM PASSWORD do not match\n");
		}
		
		if (!sanity)
		{
			adminRegError.setVisible(true);
			errorDetails.setVisible(true);
		}
		
		return sanity;
	}
}