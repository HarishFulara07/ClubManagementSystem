package application.others.account;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

import application.database.User_Club_Info;
import application.database.User_Info;
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
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Others_Home_Screen_Controller implements Initializable {
	
	// Home
	@FXML
	private Text othersAccount_Name,
				 othersAccount_Sex,
				 othersAccount_Profession,
				 othersAccount_RollNum,
				 othersAccount_Email,
				 othersAccount_Club,
				 othersAccount_Status;
	
	// Club Information
	@FXML
	private ComboBox<String> othersClubInfo_Name;
	@FXML
	private RadioButton othersClubInfo_Complete,
						othersClubInfo_Admins,
						othersClubInfo_Members;
	@FXML
	private Button othersClubInfo_Submit;
	@FXML
	private Text clubError,
				 clubErrorDetails;
	
	// TODO Member Information
	@FXML
	private TextField othersMemberInfo_Name,
					  othersMemberInfo_RollNum,
					  othersMemberInfo_Email;
	@FXML
	private Button othersMemberInfo_Submit;
	@FXML
	private Text memberError,
				 memberErrorDetails;
	
	// TODO Add Member
	
	// TODO Event information
	@FXML
	private DatePicker othersEventInfo_From,
					   othersEventInfo_To;
	@FXML
	private ComboBox<String> othersEventInfo_Club;
	@FXML
	private Button othersEventInfo_Submit;
	@FXML
	private Text eventError,
				 eventErrorDetails;
	
	// Others
	@FXML
	private Button  othersLogout;
	
	private User_Info currentUser = null;
	
	// **************************************************************************************************************************************************************** //
		
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// Set Club_info Tab
		try
		{
			othersClubInfo_Name.setItems (giveAllClubs());
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}	
		
		// Set Event Tab
		try
		{
			othersEventInfo_Club.setItems (giveAllClubs());
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		
		// Set Home Tab
		othersAccount_Name.setText("");
		othersAccount_Sex.setText("");
		othersAccount_Profession.setText("");
		othersAccount_RollNum.setText("");
		othersAccount_Email.setText("");
		othersAccount_Club.setText("");
		othersAccount_Status.setText("");
	}
	
	/*
	 * Method returns all the clubs present in clubs_info table
	 */
	private ObservableList<String> giveAllClubs () throws SQLException {
		ObservableList<String> clubNames = FXCollections.observableArrayList ();
		
		MySQL.connect();
		Statement statement = MySQL.getStatement();
		ResultSet rs = statement.executeQuery ("SELECT club_name "
											 + "FROM " + MySQL.TABLE_CLUB_INFO);
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
	 * Method to set User Info Before Initialization
	 * Called By AdminLoginScreen
	 */
	public void setDataFromEmail (String email) throws SQLException {
		currentUser = new User_Info();
		
		currentUser.email = email;
		
		MySQL.connect();
		
		Statement statement = MySQL.getStatement();
		ResultSet rs = statement.executeQuery ("SELECT * "
											 + "FROM " + MySQL.TABLE_CLUB_INFO + " AS A, " + MySQL.TABLE_CLUB_MEMBER_INFO + " AS B, " + MySQL.TABLE_USER_INFO + " AS C "
											 + "WHERE A.club_name = B.club_name AND B.email = C.email "
											 		+ "AND B.email = \"" + email + "\"");
		
		rs.last();
		int size = rs.getRow();
		rs.beforeFirst();
		
		if (size == 0)
		{
			currentUser = null;
			System.out.println ("setDataFromLogin(): CURRENT USER IS NOT IN ANY CLUB, PROBLEM IN AUTHENTICATION");
			System.exit (0);
		}
		else
		{
			while (rs.next())
			{
				// Since 'email' is same all following info must be same for all tuples in ResultSet
				currentUser.first_name = rs.getString("C.first_name");
				currentUser.last_name = rs.getString("C.last_name");
				currentUser.sex = rs.getString("C.sex");
				currentUser.profession = rs.getString("C.profession");
				currentUser.roll_number = rs.getInt("C.roll_number");
				
				// All this information may be different for each tuple
				currentUser.club_name.add (rs.getString("A.club_name"));
				currentUser.discipline.add (rs.getString("A.discipline"));
				currentUser.description.add (rs.getString("A.description"));
				currentUser.status.add (rs.getString("B.status"));
			}
		}
		
		rs.close();
		statement.close();
		
		MySQL.disconnect();
		
		// Home Tab Data
		othersAccount_Name.setText (currentUser.first_name + " " + currentUser.last_name);
		othersAccount_Sex.setText (currentUser.sex);
		othersAccount_Profession.setText (currentUser.profession);
		othersAccount_RollNum.setText (Integer.toString (currentUser.roll_number));
		othersAccount_Email.setText (currentUser.email);
		
		othersAccount_Club.setText (currentUser.club_name.get(0));
		int i;
		for (i = 1; i < currentUser.club_name.size(); i++)
		{
			othersAccount_Club.setText (othersAccount_Club.getText() + ", " + currentUser.club_name.get(i));
		}
		
		othersAccount_Status.setText (currentUser.status.get(0));
		for (i = 1; i < currentUser.status.size(); i++)
		{
			othersAccount_Status.setText (othersAccount_Status.getText() + ", " + currentUser.status.get(i));
		}
	}
	
	public void setDataFromUserInfo (User_Info cu)
	{
		currentUser = new User_Info();
		
		currentUser.first_name = cu.first_name;
		currentUser.last_name = cu.last_name;
		currentUser.sex = cu.sex;
		currentUser.discipline = cu.discipline;
		currentUser.description = cu.description;
		currentUser.roll_number = cu.roll_number;
		currentUser.email = cu.email;
		currentUser.club_name = cu.club_name;
		currentUser.status = cu.status;
		currentUser.profession = cu.profession;
		
		// Home Tab Data
		othersAccount_Name.setText (currentUser.first_name + " " + currentUser.last_name);
		othersAccount_Sex.setText (currentUser.sex);
		othersAccount_Profession.setText (currentUser.profession);
		othersAccount_RollNum.setText (Integer.toString (currentUser.roll_number));
		othersAccount_Email.setText (currentUser.email);
		
		othersAccount_Club.setText (currentUser.club_name.get(0));
		int i;
		for (i = 1; i < currentUser.club_name.size(); i++)
		{
			othersAccount_Club.setText (othersAccount_Club.getText() + ", " + currentUser.club_name.get(i));
		}
		
		othersAccount_Status.setText (currentUser.status.get(0));
		for (i = 1; i < currentUser.status.size(); i++)
		{
			othersAccount_Status.setText (othersAccount_Status.getText() + ", " + currentUser.status.get(i));
		}
	}
	
	/*
	 * Logout other's account when "othersLogout" button is pressed
	 */
	@FXML
	void othersLogoutAction(ActionEvent event) throws IOException
	{
		currentUser = null;
		
		FXMLLoader loader = new FXMLLoader (getClass().getResource("/application/home/Home_Screen.fxml"));
		Stage stage = new Stage(StageStyle.DECORATED);
		stage.setScene (new Scene (loader.load()));
		stage.show();
		
		stage = (Stage) othersLogout.getScene().getWindow();
		stage.close();
	}
	
	/*
	 * "Submit" Button in Club Information Tab
	 */
	@FXML
	void clubSubmit (ActionEvent event) throws IOException, SQLException
	{
		clubError.setVisible(false);
		clubErrorDetails.setVisible(false);
		clubErrorDetails.setText("");
		
		if (saneClubInfo())
		{
			String clubName = othersClubInfo_Name.getValue().toString();
			RadioButton button = null;
			
			if (othersClubInfo_Complete.isSelected())
			{
				button = othersClubInfo_Complete;
			}
			else if (othersClubInfo_Admins.isSelected())
			{
				button = othersClubInfo_Admins;
			}
			else
			{
				button = othersClubInfo_Members;
			}
			
			String sql = ("SELECT * "
						+ "FROM " + MySQL.TABLE_CLUB_INFO + " AS A, " + MySQL.TABLE_CLUB_MEMBER_INFO + " AS B, " + MySQL.TABLE_USER_INFO + " AS C "
						+ "WHERE A.club_name = B.club_name AND B.email = C.email "
							  + "AND A.club_name = '" + clubName + "' ");
			
			if (button == othersClubInfo_Complete)
				;
			else if (button == othersClubInfo_Admins)
			{
				sql += "AND B.status = 'ADMIN'";
			}
			else if (button == othersClubInfo_Members)
			{
				sql += "AND B.status != 'ADMIN'";
			}
			
			MySQL.connect();
			
			Statement statement = MySQL.getStatement();
			ResultSet rs = statement.executeQuery (sql);

			ArrayList<User_Club_Info> data = new ArrayList<>();
			
			while (rs.next())
			{
				User_Club_Info uci = new User_Club_Info();
				
				uci.first_name = rs.getString ("C.first_name");
				uci.last_name = rs.getString ("C.last_name");
				uci.sex = rs.getString ("C.sex");
				uci.profession = rs.getString ("C.profession");
				uci.email = rs.getString ("C.email");
				uci.roll_number = rs.getInt ("C.roll_number");
				uci.club_name = rs.getString ("A.club_name");
				uci.discipline = rs.getString ("A.discipline");
				uci.description = rs.getString ("A.description");
				uci.status = rs.getString ("B.status");
				
				data.add (uci);
			}
			
			rs.close();
			statement.close();
						
			MySQL.disconnect();
			
			FXMLLoader loader = new FXMLLoader (getClass().getResource("/application/others/account/Club_Info_Table.fxml"));
			Stage stage = new Stage(StageStyle.DECORATED);
			stage.setScene (new Scene (loader.load()));
			Club_Info_Table_Controller citc = loader.<Club_Info_Table_Controller>getController();
			citc.setData (data, currentUser);
			stage.show();
			
			stage = (Stage) othersClubInfo_Submit.getScene().getWindow();
			stage.close();
		}
	}
	
	/*
	 * "Submit" Button in Member Information Tab
	 */
	@FXML
	void memberSubmit (ActionEvent event) throws IOException
	{
		memberError.setVisible(false);
		memberErrorDetails.setVisible(false);
		memberErrorDetails.setText("");
		
		if (saneMemberInfo())
		{
			//TODO Do Something
		}
	}
	
	/*
	 * "Submit" Button in Event Information Tab
	 */
	@FXML
	void eventSubmit (ActionEvent event) throws IOException
	{
		eventError.setVisible(false);
		eventErrorDetails.setVisible(false);
		eventErrorDetails.setText("");
		
		if (saneEventInfo())
		{
			//TODO Do Something
		}
	}
	
	// AUXILLIARY METHODS
	
	/*
	 * Sanity Checks for Club Information Tab
	 */
	boolean saneClubInfo() {
		boolean sanity = true;
		
		String TEMP;
		
		TEMP = othersClubInfo_Name.getValue();
		TEMP = TEMP.trim();
		if (TEMP == null || TEMP.isEmpty() || TEMP.equals(""))
		{
			sanity = false;
			clubErrorDetails.setText(clubErrorDetails.getText() + "CLUBNAME cannot be empty.\n");
		}
		
		if (!othersClubInfo_Complete.isSelected() && !othersClubInfo_Admins.isSelected() && !othersClubInfo_Members.isSelected())
		{
			sanity = false;
			clubErrorDetails.setText(clubErrorDetails.getText() + "No TYPE Selected.\n");
		}
		
		if (!sanity)
		{
			clubError.setVisible(true);
			clubErrorDetails.setVisible(true);
		}
		
		return sanity;
	}
	
	/*
	 * Sanity Checks for Member Information Tab
	 */
	boolean saneMemberInfo() {
		boolean sanity = true;
		
		String T1, T2;
		
		T1 = othersMemberInfo_Name.getText().toString().trim();
		T2 = othersMemberInfo_RollNum.getText().toString().trim();
		
		if (T1 == null || T1.isEmpty() || T1.equals(""))
			if (T2 == null || T2.isEmpty() || T2.equals(""))
			{
				sanity = false;
				memberErrorDetails.setText(memberErrorDetails.getText() + "Both Fields cannot be empty.\n");
			}
			else
			{
				for (int i = 0; i < T2.length(); i++)
					if (Character.isAlphabetic(T2.charAt(i)))
					{
						sanity = false;
						memberErrorDetails.setText(memberErrorDetails.getText() + "ROLLNUMBER cant contain Alphabets.\n");
						break;
					}
				othersMemberInfo_Name.setText("");
			}
		else
		{
			for (int i = 0; i < T1.length(); i++)
				if (Character.isDigit(T1.charAt(i)))
				{
					sanity = false;
					memberErrorDetails.setText(memberErrorDetails.getText() + "NAME cant contain Digits.\n");
					break;
				}
			othersMemberInfo_RollNum.setText("");
		}
		
		if (!sanity)
		{
			memberError.setVisible(true);
			memberErrorDetails.setVisible(true);
		}
		
		return sanity;
	}
	
	/*
	 * Sanity Checks for Event Information Tab
	 */
	boolean saneEventInfo() {
		boolean sanity = true;
		
		LocalDate FromDate = null, ToDate = null;
		
		FromDate = othersEventInfo_From.getValue();
		ToDate = othersEventInfo_To.getValue();
		
		if (FromDate == null)
		{
			sanity = false;
			eventErrorDetails.setText(eventErrorDetails.getText() + "FROM DATE cannot be Empty.\n");
		}
		if (ToDate == null)
		{
			sanity = false;
			eventErrorDetails.setText(eventErrorDetails.getText() + "TO DATE cannot be Empty.\n");
		}
		
		if (sanity && ToDate != null && FromDate != null && ToDate.isBefore(FromDate))
		{
			sanity = false;
			eventErrorDetails.setText(eventErrorDetails.getText() + "To Date cannot be before From Date.\n");
		}
		
		String ClubName = othersEventInfo_Club.getValue();
		if (ClubName == null || ClubName.isEmpty() || ClubName.equals(""))
		{
			sanity = false;
			eventErrorDetails.setText(eventErrorDetails.getText() + "CLUB NAME cannot be Empty.\n");
		}
		
		if (!sanity)
		{
			eventError.setVisible(true);
			eventErrorDetails.setVisible(true);
		}
		
		return sanity;
	}
}
