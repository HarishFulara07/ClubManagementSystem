package application.others.registration;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

import application.global.MySQL;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Others_Registration_Form_Controller implements Initializable {
	@FXML
	private Button othersRegSubmit,
				   othersRegBack;
	@FXML
	private TextField othersRegFirstName,
					  othersRegLastName,
					  othersRegRollNum,
					  othersRegEmail,
					  othersRegPassword,
					  othersRegConfirmPassword;
	@FXML
	private RadioButton othersRegMale,
						othersRegFemale,
						othersRegFaculty,
						othersRegStudent,
						othersRegOther;
	@FXML
	private Text othersRegError,
				 errorDetails;
	
	// ********************************************************************************************************** //
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
	}
	
	/*
	 * Submit others's registration form when "othersRegSubmit" button is pressed 
	 */
	@FXML
	void othersRegSubmitAction(ActionEvent event) throws IOException
	{
		othersRegError.setVisible(false);
		errorDetails.setVisible(false);
		errorDetails.setText("");
		
		if (saneInput())
		{	
			othersRegSubmit.setDisable(true);
			othersRegBack.setDisable(true);
			
			/*
			 * The following code inserts the others form data into the database
			 */
			Statement statement = null;
			
			String first_name = othersRegFirstName.getText().toString().trim();
			String last_name = othersRegLastName.getText().toString().trim();
			String sex = null;
			String profession = null;
			Integer roll_number = Integer.parseInt(othersRegRollNum.getText());
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
				statement.executeUpdate(ins3);
				
			} catch (SQLException e) {
				othersRegError.setVisible(true);
			}
			MySQL.disconnect();
			
			/*
			 * The following code redirects the others to home screen after he submits the form
			 */
			FXMLLoader loader = new FXMLLoader (getClass().getResource("/application/home/Home_Screen.fxml"));
			Stage stage = new Stage(StageStyle.DECORATED);
			stage.setScene (new Scene (loader.load()));
			stage.show();
			stage = (Stage) othersRegSubmit.getScene().getWindow();
			stage.close();
		}
	}
	
	/*
	 * Load home screen when "othersRegBack" button is pressed 
	 */
	@FXML
	void homeScreenFromOthersReg(ActionEvent event) throws IOException
	{
		FXMLLoader loader = new FXMLLoader (getClass().getResource("/application/home/Home_Screen.fxml"));
		Stage stage = new Stage(StageStyle.DECORATED);
		stage.setScene (new Scene (loader.load()));
		stage.show();
		
		stage = (Stage) othersRegBack.getScene().getWindow();
		stage.close();
	}
	
	// AUXILLIARY METHODS
	
	/*
	 * Performs Sanity Checks
	 * If anything found OUT OF ORDER shows the othersLoginError Text
	 */
	boolean saneInput() {
		boolean sanity = true;
		
		String TEMP;
		
		TEMP = othersRegFirstName.getText().toString();
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
		
		TEMP = othersRegLastName.getText().toString();
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
		
		if (!othersRegMale.isSelected() && !othersRegFemale.isSelected())
		{
			sanity = false;
			errorDetails.setText(errorDetails.getText() + "No Sex is Selected\n");
		}
		
		if (!othersRegFaculty.isSelected() && !othersRegStudent.isSelected() && !othersRegOther.isSelected())
		{
			sanity = false;
			errorDetails.setText(errorDetails.getText() + "No Profession is Selected\n");
		}
		
		TEMP = othersRegRollNum.getText().toString();
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
		
		TEMP = othersRegEmail.getText().toString();
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
		
		TEMP = othersRegPassword.getText().toString();
		TEMP = TEMP.trim();
		if (TEMP == null || TEMP.isEmpty() || TEMP.equals(""))
		{
			sanity = false;
			errorDetails.setText(errorDetails.getText() + "PASSWORD cannot be empty\n");
		}
		
		String TEMP1 = othersRegConfirmPassword.getText().toString();
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
			othersRegError.setVisible(true);
			errorDetails.setVisible(true);
		}
		
		return sanity;
	}
}