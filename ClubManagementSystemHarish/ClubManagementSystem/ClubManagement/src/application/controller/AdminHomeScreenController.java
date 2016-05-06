package application.controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class AdminHomeScreenController implements Initializable{
	
	@FXML
	private Button requestTimeSlot, adminLogout, adminClubInfoSubmit, adminMemberInfoSubmit,
					adminEventInfoSubmit, adminAddMemberButton;
	@FXML
	private TextField adminMemberInfoName, adminMemberInfoRollNum, adminMemberInfoEmail,
					adminAddMemberEmail;

	@FXML
	private Text adminAccountName, adminAccountSex, adminAccountProfession, adminAccountRollNum, adminAccountEmail,
					adminAccountClub, adminAccountStatus, adminMemberInfoError, adminAddMemberError;
	@FXML
	private RadioButton adminCompleteClubInfo, adminClubAdminsInfo, adminClubMembersInfo;
	@FXML
	private ToggleGroup adminTypeOfInformation;
	@FXML
	private DatePicker adminEventInfoFrom, adminEventInfoTo, BookEventDate;
	@FXML
	private ComboBox<String> adminEventInfoClub, BookEventAMPM, BookEventVenue, adminAddMemberStatus,
						adminClubInfoName;
	@FXML
	private ComboBox<Integer> BookEventHH, BookEventMM;
	
	public static String member_first_name = null;
	public static String member_last_name = null;
	public static String member_sex = null;
	public static String member_profession = null;
	public static String member_roll_num = null;
	public static String member_mail = null;
	public static String member_club = null;
	public static String member_status = null;
	
	@Override
    public void initialize(URL location, ResourceBundle resources)
    {
		/*
		 * populate "adminClubInfoName" combo-box with value
		 */
		adminClubInfoName.getItems().clear();
		adminMemberInfoError.setText("");
		adminAddMemberError.setText("");
		
		adminClubInfoName.getItems().addAll(
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
		
		/*
		 * populate "adminAddMemberStatus" combo-box with values
		 */
		adminAddMemberStatus.getItems().clear();

		adminAddMemberStatus.getItems().addAll(
					"Member",
					"Admin"
		            );
	
		/*
		 * display of admin's personal, contact and club information on his account's home screen
		 */
		Connection connection = null;
		Statement statement1 = null;
		Statement statement2 = null;
		ResultSet rs1 = null;
		ResultSet rs2 = null;
		
		String email = AdminLoginScreenController.email;
		String query1 = "SELECT * FROM user_info WHERE email = '" + email + "'";
		String query2 = "SELECT club_name FROM club_member_info WHERE email = '" + email + "'";
		
		try {			
			
			connection = JDBCMySQLConnection.getConnection();
			statement1 = connection.createStatement();
			statement2 = connection.createStatement();
			rs1 = statement1.executeQuery(query1);
			rs2 = statement2.executeQuery(query2);
			
			if (rs1.next()) {
				adminAccountName.setText(rs1.getString("first_name") + " " + rs1.getString("last_name"));
				adminAccountSex.setText(rs1.getString("sex"));
				adminAccountProfession.setText(rs1.getString("profession"));
				
				if(rs1.getInt("roll_number") == 0)
				{
					adminAccountRollNum.setText("-----");
				}
				else
				{
					adminAccountRollNum.setText(Integer.toString(rs1.getInt("roll_number")));
				}
				adminAccountEmail.setText(email);
			}
			
			if (rs2.next()) {
				adminAccountClub.setText(rs2.getString("club_name"));
				adminAccountStatus.setText("Admin");
			}
			
		} catch (SQLException e) {
			
			e.printStackTrace();
			
		} finally {
			
			if (connection != null) {
				
				try {
					/*
					 * Closing the Connection object will also close statement1 object as well.
					 * However, we should always explicitly close the statement1 object to ensure proper cleanup.
					 */
					statement1.close();
					statement2.close();
					connection.close();
					
				} catch (SQLException e) {
					
					e.printStackTrace();
				
				}
			}
		}
    }
	
	/*
	 * Load member info when "adminMemberInfoSubmit" button is pressed
	 */
	@FXML
	void adminMemberInfoSubmitAction(ActionEvent event) throws IOException
	{
		String member_name = null;
		Integer member_roll_number = null;
		String member_email = null;
		Boolean flag = false;
		
		Connection connection = null;
		Statement statement = null;
		ResultSet rs = null;
		String query = null;
		
		if(!adminMemberInfoName.getText().equals(""))
		{
			member_name = adminMemberInfoName.getText().toLowerCase();
			
			if(member_name.split(" ").length > 1)
			{
				query = "SELECT * FROM user_info NATURAL JOIN club_member_info WHERE first_name = '" + member_name.split(" ")[0] + "'" 
						+ "AND last_name = '" + member_name.split(" ")[1] + "'";
			}
			else
			{
				adminMemberInfoError.setText("Invalid Search Credentials");
				return;
			}
		}
		else if(!adminMemberInfoRollNum.getText().equals(""))
		{
			member_roll_number = Integer.parseInt(adminMemberInfoRollNum.getText());
			query = "SELECT * FROM user_info NATURAL JOIN club_member_info WHERE roll_number = '" + member_roll_number + "'";
		}
		else if(!adminMemberInfoEmail.getText().equals(""))
		{
			member_email = adminMemberInfoEmail.getText().toLowerCase();
			query = "SELECT * FROM user_info NATURAL JOIN club_member_info WHERE email = '" + member_email + "'";
		}
		else
		{
			adminMemberInfoError.setText("Invalid Search Credentials");
			return;
		}
		
		try {			
			
			connection = JDBCMySQLConnection.getConnection();
			statement = connection.createStatement();
			rs = statement.executeQuery(query);
			
			if(rs.next())
			{
				member_first_name = rs.getString("first_name");
				member_last_name = rs.getString("last_name");
				member_sex = rs.getString("sex");
				member_profession = rs.getString("profession");
				member_roll_num = Integer.toString(rs.getInt("roll_number"));
				member_mail = rs.getString("email");
				member_club = rs.getString("club_name");
				member_status = rs.getString("status");
				flag = true;
			}
			else
			{
				adminMemberInfoError.setText("Invalid Search Credentials");
			}
			
		} catch (SQLException e) {
			
			e.printStackTrace();
			
		} finally {
			
			if (connection != null) {
				
				try {
					/*
					 * Closing the Connection object will also close statement1 object as well.
					 * However, we should always explicitly close the statement1 object to ensure proper cleanup.
					 */
					statement.close();
					connection.close();
					
				} catch (SQLException e) {
					
					e.printStackTrace();
				
				}
			}
		}
		
		if(flag)
		{
			//Change the scene
			Stage stage = (Stage) adminMemberInfoSubmit.getScene().getWindow();
			Parent root = FXMLLoader.load(getClass().getResource("/application/admin/account/ShowMember.fxml"));
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.show();
		}
	}
	
	/*
	 * Give member status to a user
	 * Change status of a user from member to admin or vice versa
	 */
	@FXML
	void adminAddMemberButtonAction(ActionEvent event) throws IOException
	{
		String member_email = null;
		String member_status = null;
		
		Connection connection = null;
		Statement statement1 = null;
		Statement statement2 = null;
		ResultSet rs = null;
		String query1 = null;
		String query2 = null;
		
		if(!adminAddMemberEmail.getText().equals("") && adminAddMemberStatus.getSelectionModel().getSelectedItem() != null)
		{
			member_email = adminAddMemberEmail.getText();
			member_status = adminAddMemberStatus.getSelectionModel().getSelectedItem();
			query1 = "SELECT * FROM club_member_info WHERE email = '" + member_email + "'";
		}
		else
		{
			adminAddMemberError.setText("Invalid Credentials");
			return;
		}
		
		try {			
			
			connection = JDBCMySQLConnection.getConnection();
			statement1 = connection.createStatement();
			statement2 = connection.createStatement();
			rs = statement1.executeQuery(query1);
			
			if(rs.next())
			{
				query2 = "UPDATE club_member_info SET status = '" + member_status + "' WHERE email = '" + member_email + "'";
				statement2.executeUpdate(query2);
				
				Stage stage = (Stage) adminAddMemberButton.getScene().getWindow();
				Parent root = FXMLLoader.load(getClass().getResource("/application/admin/account/AdminHomeScreen.fxml"));
				Scene scene = new Scene(root);
				stage.setScene(scene);
				stage.show();
			}
			else
			{
				adminAddMemberError.setText("Invalid Credentials");
			}
			
		} catch (SQLException e) {
			
			e.printStackTrace();
			
		} finally {
			
			if (connection != null) {
				
				try {
					/*
					 * Closing the Connection object will also close statement1 object as well.
					 * However, we should always explicitly close the statement1 object to ensure proper cleanup.
					 */
					statement1.close();
					statement2.close();
					connection.close();
					
				} catch (SQLException e) {
					
					e.printStackTrace();
				
				}
			}
		}
	}
	
	/*
	 * Load event booking form screen when "requestTimeSlot" button is pressed 
	 */
	@FXML
	void eventBookingScreen(ActionEvent event) throws IOException
	{
		Stage stage = (Stage) requestTimeSlot.getScene().getWindow();
		Parent root = FXMLLoader.load(getClass().getResource("/application/event/BookEvent.fxml"));
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	/*
	 * Logout admin's account when "adminLogout" button is pressed
	 */
	@FXML
	void adminLogoutAction(ActionEvent event) throws IOException
	{
		Stage stage = (Stage) adminLogout.getScene().getWindow();
		Parent root = FXMLLoader.load(getClass().getResource("/application/home/HomeScreen.fxml"));
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
}