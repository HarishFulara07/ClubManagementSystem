package application.admin.account;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.ResourceBundle;

import application.database.Event_Info;
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

public class Admin_Home_Screen_Controller implements Initializable {
	
	// Home
	@FXML
	private Text adminAccount_Name,
				 adminAccount_Sex,
				 adminAccount_Profession,
				 adminAccount_RollNum,
				 adminAccount_Email,
				 adminAccount_Club,
				 adminAccount_Status;
	
	// Club Information
	@FXML
	private ComboBox<String> adminClubInfo_Name;
	@FXML
	private RadioButton adminClubInfo_Complete,
						adminClubInfo_Admins,
						adminClubInfo_Members;
	@FXML
	private Button adminClubInfo_Submit;
	@FXML
	private Text clubError,
				 clubErrorDetails;
	
	// TODO Member Information
	@FXML
	private TextField adminMemberInfo_Name,
					  adminMemberInfo_RollNum,
					  adminMemberInfo_Email;
	@FXML
	private Button adminMemberInfo_Submit;
	@FXML
	private Text memberError,
				 memberErrorDetails;
	
	// TODO Add Member
	
	// TODO Event information
	@FXML
	private DatePicker adminEventInfo_From,
					   adminEventInfo_To;
	@FXML
	private ComboBox<String> adminEventInfo_Club;
	@FXML
	private Button adminEventInfo_Submit;
	@FXML
	private Text eventError,
				 eventErrorDetails;
	
	// Book Event Information
	@FXML
	private DatePicker BookEvent_StartDate,
					   BookEvent_EndDate;
	@FXML
	private ComboBox<Integer> BookEvent_StarttimeHH, 
							  BookEvent_StarttimeMM,
							  BookEvent_EndtimeHH,
							  BookEvent_EndtimeMM;
	@FXML
	private ComboBox<String> BookEvent_StarttimeAMPM,
							 BookEvent_EndtimeAMPM,
							 BookEvent_Venue;
	@FXML
	private Button requestTimeSlot;
	@FXML
	private Text bookEventError,
				 bookEventClash,
				 bookEventErrorDetails;
	
	// Others
	@FXML
	private Button  adminLogout;
	
	private User_Info currentUser = null;
	
	// **************************************************************************************************************************************************************** //
	
	private ObservableList<Integer> timeHours = FXCollections.observableArrayList (1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12);
	private ObservableList<Integer> timeMinutes = FXCollections.observableArrayList (0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15,
																			 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30,
																			 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45,
																			 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59);
	private ObservableList<String> time_AM_PM = FXCollections.observableArrayList ("AM", "PM");
	private ObservableList<String> venues = FXCollections.observableArrayList ("CDX",
																			   "Canteen",
																			   "C01", "C02", "C03",
																			   "C11", "C12", "C13",
																			   "C21", "C22", "C23",
																			   "S01", "S02",
																			   "Student's Centre",
																			   "Senate Council Room");
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// Set Club_info Tab
		try
		{
			adminClubInfo_Name.setItems (giveAllClubs());
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}	
		
		// Set Event Tab
		try
		{
			adminEventInfo_Club.setItems (giveAllClubs());
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		
		// Set Book_Event Tab
		BookEvent_StarttimeHH.setItems (timeHours);
		BookEvent_StarttimeMM.setItems (timeMinutes);
		BookEvent_StarttimeAMPM.setItems (time_AM_PM);
		BookEvent_EndtimeHH.setItems (timeHours);
		BookEvent_EndtimeMM.setItems (timeMinutes);
		BookEvent_EndtimeAMPM.setItems (time_AM_PM);
		
		BookEvent_Venue.setItems (venues);
		
		// Set Home Tab
		adminAccount_Name.setText("");
		adminAccount_Sex.setText("");
		adminAccount_Profession.setText("");
		adminAccount_RollNum.setText("");
		adminAccount_Email.setText("");
		adminAccount_Club.setText("");
		adminAccount_Status.setText("");
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
		
		currentUser.email = email;								// Set Email
		
		MySQL.connect();
		
		Statement statement = MySQL.getStatement();
		ResultSet rs = statement.executeQuery ("SELECT * "
											 + "FROM " + MySQL.TABLE_CLUB_INFO + " AS A, " + MySQL.TABLE_CLUB_MEMBER_INFO + " AS B, " + MySQL.TABLE_USER_INFO + " AS C "
											 + "WHERE A.club_name = B.club_name AND B.email = C.email "
											 		+ "AND B.email = \"" + email + "\" "
											 		+ "AND B.status = \"ADMIN\"");
		
		// This gives all the clubs for which current user is ADMIN
		
		rs.last();
		int size = rs.getRow();
		rs.beforeFirst();
		
//		System.out.println ("Running setDataFromEmail.. ResultSet Size == " + size);
		
		if (size == 0)
		{
			currentUser = null;
			System.out.println ("setDataFromLogin(): CURRENT USER IS NOT AN ADMIN FOR ANY CLUB, PROBLEM IN AUTHENTICATION");
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
				currentUser.status.add (rs.getString("B.status"));				// Will always be 'ADMIN'
			}
		}
		
		rs.close();
		statement.close();
		
		MySQL.disconnect();
		
		// Home Tab Data
		adminAccount_Name.setText (currentUser.first_name + " " + currentUser.last_name);
		adminAccount_Sex.setText (currentUser.sex);
		adminAccount_Profession.setText (currentUser.profession);
		adminAccount_RollNum.setText (Integer.toString (currentUser.roll_number));
		adminAccount_Email.setText (currentUser.email);
		
		adminAccount_Club.setText (currentUser.club_name.get(0));
		int i;
		for (i = 1; i < currentUser.club_name.size(); i++)
		{
			adminAccount_Club.setText (adminAccount_Club.getText() + ", " + currentUser.club_name.get(i));
		}
		
		adminAccount_Status.setText ("ADMIN");
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
		adminAccount_Name.setText (currentUser.first_name + " " + currentUser.last_name);
		adminAccount_Sex.setText (currentUser.sex);
		adminAccount_Profession.setText (currentUser.profession);
		adminAccount_RollNum.setText (Integer.toString (currentUser.roll_number));
		adminAccount_Email.setText (currentUser.email);
		
		adminAccount_Club.setText (currentUser.club_name.get(0));
		int i;
		for (i = 1; i < currentUser.club_name.size(); i++)
		{
			adminAccount_Club.setText (adminAccount_Club.getText() + ", " + currentUser.club_name.get(i));
		}
		
		adminAccount_Status.setText ("ADMIN");
	}
	
	/*
	 * When "requestTimeSlot" BUTTON is Clicked
	 * 1. Check for Sanity of Input
	 * 2. Create TimeStamps by joining date and time
	 * 3. Check for schedule clashes
	 * 4. If no clash, Load event booking form screen. 
	 */
	@FXML
	void eventBookingScreen(ActionEvent event) throws IOException
	{
		bookEventError.setVisible(false);
		bookEventClash.setVisible(false);
		bookEventClash.setText("");
		bookEventErrorDetails.setVisible(false);
		bookEventErrorDetails.setText("");
		
		if (saneBookEventInfo())
		{
			DateTimeFormatter dateOutFormat = DateTimeFormatter.ofPattern ("uuuu-MM-dd");
			DateTimeFormatter timeInFormat = DateTimeFormatter.ofPattern ("h:m a");
			DateTimeFormatter timeOutFormat = DateTimeFormatter.ofPattern("HH:mm:ss");
			
			LocalDate SD = BookEvent_StartDate.getValue();								// StartDate
			LocalDate ED = BookEvent_EndDate.getValue();								// EndDate
			LocalTime ST = LocalTime.parse (BookEvent_StarttimeHH.getValue() + ":" 
											+ BookEvent_StarttimeMM.getValue() + " " 
											+ BookEvent_StarttimeAMPM.getValue(),
											timeInFormat);								// StartTime
			LocalTime ET = LocalTime.parse (BookEvent_EndtimeHH.getValue() + ":" 
											+ BookEvent_EndtimeMM.getValue() + " " 
											+ BookEvent_EndtimeAMPM.getValue(),
											timeInFormat);								// EndTime
			
			String StartTimeStamp = SD.format(dateOutFormat) + " " + ST.format(timeOutFormat);
			String EndTimeStamp = ED.format(dateOutFormat) + " " + ET.format(timeOutFormat);
			
			if (isClashing (StartTimeStamp, EndTimeStamp))
			{
				bookEventClash.setText ("Another event's time slot is intersecting with your requested time slot on "
										+ StartTimeStamp + " to " + EndTimeStamp + " . Please Choose another Timeslot.\n");
				bookEventClash.setVisible(true);
			}
			else
			{
				Event_Info currentEvent = new Event_Info(StartTimeStamp, EndTimeStamp, BookEvent_Venue.getValue().toString().trim());
				
				FXMLLoader loader = new FXMLLoader (getClass().getResource("/application/admin/account/Book_Event.fxml"));
				Stage stage = new Stage(StageStyle.DECORATED);
				stage.setScene (new Scene (loader.load()));
				Book_Event_Controller bec = loader.<Book_Event_Controller>getController();
				bec.setData (currentEvent, currentUser);
				stage.show();
				
				stage = (Stage) requestTimeSlot.getScene().getWindow();
				stage.close();
			}
		}
	}
	
	/*
	 * Method to check if the given TimeStamps clash with already present events in DB
	 */
	boolean isClashing (String StartTimeStamp, String EndTimeStamp) {
		MySQL.connect();
		
		boolean clash = false;
		
		try {
			Statement statement = MySQL.getStatement();
			Statement statement2 = MySQL.getStatement();
			ResultSet rs1, rs2;
			
			rs1 = statement.executeQuery ("SELECT * FROM " + MySQL.TABLE_EVENT_DETAILS + " "
										+ "ORDER BY event_id;");
			rs2 = statement2.executeQuery ("SELECT * FROM ("
											+ "(SELECT * FROM " + MySQL.TABLE_EVENT_DETAILS + " "
											+ "WHERE event_start_time > \"" + EndTimeStamp + "\") "
											+ "UNION ALL "
											+ "(SELECT * FROM " + MySQL.TABLE_EVENT_DETAILS + " "
											+ "WHERE event_end_time < \"" + StartTimeStamp + "\") "
										+ " ) AS A "
										+ "ORDER BY A.event_id;");
			
			rs1.last();				rs2.last();
			int size1 = rs1.getRow(), size2 = rs2.getRow();
			rs1.beforeFirst();		rs2.beforeFirst();
			
			if (size1 == size2)
			{
				while (rs1.next() && rs2.next()) {
					int v1 = rs1.getInt("event_id");
					int v2 = rs2.getInt("A.event_id");
					if (v1 != v2)
					{
						clash = true;
						break;
					}
				}
			}
			else
				clash = true;
			
			rs1.close();
			rs2.close();
			statement.close();
			statement2.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		MySQL.disconnect();
		
		return clash;
	}
	
	/*
	 * Logout admin's account when "adminLogout" button is pressed
	 */
	@FXML
	void adminLogoutAction(ActionEvent event) throws IOException
	{
		currentUser = null;
		
		FXMLLoader loader = new FXMLLoader (getClass().getResource("/application/home/Home_Screen.fxml"));
		Stage stage = new Stage(StageStyle.DECORATED);
		stage.setScene (new Scene (loader.load()));
		stage.show();
		
		stage = (Stage) adminLogout.getScene().getWindow();
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
			String clubName = adminClubInfo_Name.getValue().toString();
			RadioButton button = null;
			
			if (adminClubInfo_Complete.isSelected())
			{
				button = adminClubInfo_Complete;
			}
			else if (adminClubInfo_Admins.isSelected())
			{
				button = adminClubInfo_Admins;
			}
			else
			{
				button = adminClubInfo_Members;
			}
			
			String sql = ("SELECT * "
						+ "FROM " + MySQL.TABLE_CLUB_INFO + " AS A, " + MySQL.TABLE_CLUB_MEMBER_INFO + " AS B, " + MySQL.TABLE_USER_INFO + " AS C "
						+ "WHERE A.club_name = B.club_name AND B.email = C.email "
							  + "AND A.club_name = \"" + clubName + "\" ");
			
			if (button == adminClubInfo_Complete)
				;
			else if (button == adminClubInfo_Admins)
			{
				sql += "AND B.status = 'ADMIN'";
			}
			else if (button == adminClubInfo_Members)
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
			
			FXMLLoader loader = new FXMLLoader (getClass().getResource("/application/admin/account/Club_Info_Table.fxml"));
			Stage stage = new Stage(StageStyle.DECORATED);
			stage.setScene (new Scene (loader.load()));
			Club_Info_Table_Controller citc = loader.<Club_Info_Table_Controller>getController();
			citc.setData (data, currentUser);
			stage.show();
			
			stage = (Stage) adminClubInfo_Submit.getScene().getWindow();
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
		
		TEMP = adminClubInfo_Name.getValue();
		TEMP = TEMP.trim();
		if (TEMP == null || TEMP.isEmpty() || TEMP.equals(""))
		{
			sanity = false;
			clubErrorDetails.setText(clubErrorDetails.getText() + "CLUBNAME cannot be empty.\n");
		}
		
		if (!adminClubInfo_Complete.isSelected() && !adminClubInfo_Admins.isSelected() && !adminClubInfo_Members.isSelected())
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
		
		T1 = adminMemberInfo_Name.getText().toString().trim();
		T2 = adminMemberInfo_RollNum.getText().toString().trim();
		
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
				adminMemberInfo_Name.setText("");
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
			adminMemberInfo_RollNum.setText("");
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
		
		FromDate = adminEventInfo_From.getValue();
		ToDate = adminEventInfo_To.getValue();
		
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
		
		String ClubName = adminEventInfo_Club.getValue();
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
	
	/*
	 * Sanity Checks for Book Event Tab
	 */
	boolean saneBookEventInfo () {
		boolean sanity = true;
		String TEMP;
		LocalDate SD, ED;
		
		SD = BookEvent_StartDate.getValue();
		ED = BookEvent_EndDate.getValue();
		
		if (SD == null)
		{
			sanity = false;
			bookEventErrorDetails.setText(bookEventErrorDetails.getText() + "START DATE cannot be Empty.\n");
		}
		if (ED == null)
		{
			sanity = false;
			bookEventErrorDetails.setText(bookEventErrorDetails.getText() + "END DATE cannot be Empty.\n");
		}
		
		if (sanity && SD != null && ED != null && ED.isBefore(SD))
		{
			sanity = false;
			bookEventErrorDetails.setText(bookEventErrorDetails.getText() + "END Date cannot be before START Date.\n");
		}
		
		LocalTime ST = null, ET = null;
		DateTimeFormatter format = DateTimeFormatter.ofPattern("h:m a");
		
		try {
			ST = LocalTime.parse (BookEvent_StarttimeHH.getValue() + ":" 
					+ BookEvent_StarttimeMM.getValue() + " " 
					+ BookEvent_StarttimeAMPM.getValue(),
					format);								// StartTime
			ET = LocalTime.parse (BookEvent_EndtimeHH.getValue() + ":" 
					+ BookEvent_EndtimeMM.getValue() + " " 
					+ BookEvent_EndtimeAMPM.getValue(),
					format);								// EndTime
		}
		catch (DateTimeParseException ex) {
			sanity = false;
			bookEventErrorDetails.setText(bookEventErrorDetails.getText() + "Cannot Parse Time.\n");
		}
		
		if (SD != null && ED != null && SD.isEqual(ED)) {
			if (ST != null && ST != null)
			{
				if (ST.isAfter(ET)) {
					sanity = false;
					bookEventErrorDetails.setText(bookEventErrorDetails.getText() + "END TIME cannot be before START TIME.\n");
				}
			}
			else
			{
				sanity = false;
				bookEventErrorDetails.setText(bookEventErrorDetails.getText() + "Please Choose Time.\n");
			}	
		}
		
		TEMP = BookEvent_Venue.getValue();
		if (TEMP == null || TEMP.isEmpty() || TEMP.equals(""))
		{
			sanity = false;
			bookEventErrorDetails.setText(bookEventErrorDetails.getText() + "Venue Cannot be Empty.\n");
		}
		
		if (!sanity)
		{
			bookEventError.setVisible(true);
			bookEventErrorDetails.setVisible(true);
		}
		
		return sanity;
	}
}
