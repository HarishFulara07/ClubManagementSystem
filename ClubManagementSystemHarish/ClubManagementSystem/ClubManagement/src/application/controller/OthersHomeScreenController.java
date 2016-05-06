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

public class OthersHomeScreenController implements Initializable{
	@FXML
	private Button 	othersLogout, othersClubInfoSubmit, othersMemberInfoSubmit, othersEventInfoSubmit;
	@FXML
	private TextField othersMemberInfoName, othersMemberInfoRollNum, othersMemberInfoEmail;

	@FXML
	private Text othersAccountName, othersAccountSex, othersAccountProfession, othersAccountRollNum,
					othersAccountEmail, othersAccountClub, othersAccountStatus;
	@FXML
	private RadioButton othersCompleteClubInfo, othersClubAdminsInfo, othersClubMembersInfo;
	@FXML
	private ToggleGroup othersTypeOfInformation;
	@FXML
	private DatePicker othersEventInfoFrom, othersEventInfoTo;
	@FXML
	private ComboBox<String> othersEventInfoClub, othersClubInfoName;
	
	@Override
    public void initialize(URL location, ResourceBundle resources)
    {
		/*
		 * populate "othersClubInfoName" combo-box with value
		 */
		othersClubInfoName.getItems().clear();

		othersClubInfoName.getItems().addAll(
					"All Clubs",
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
		 * display of personal, contact and club information on others account's home screen
		 */
		Connection connection = null;
		Statement statement1 = null;
		Statement statement2 = null;
		ResultSet rs1 = null;
		ResultSet rs2 = null;
		
		String email = OthersLoginScreenController.email;
		String query1 = "SELECT * FROM user_info WHERE email = '" + email + "'";
		String query2 = "SELECT club_name, status FROM club_member_info WHERE email = '" + email + "'";
		
		try {			
			
			connection = JDBCMySQLConnection.getConnection();
			statement1 = connection.createStatement();
			statement2 = connection.createStatement();
			rs1 = statement1.executeQuery(query1);
			rs2 = statement2.executeQuery(query2);
			
			if (rs1.next()) {
				othersAccountName.setText(rs1.getString("first_name") + " " + rs1.getString("last_name"));
				othersAccountSex.setText(rs1.getString("sex"));
				othersAccountProfession.setText(rs1.getString("profession"));
				
				if(rs1.getInt("roll_number") == 0)
				{
					othersAccountRollNum.setText("-----");
				}
				else
				{
					othersAccountRollNum.setText(Integer.toString(rs1.getInt("roll_number")));
				}
				othersAccountEmail.setText(email);
			}
			
			if (rs2.next()) {
				othersAccountClub.setText(rs2.getString("club_name"));
				othersAccountStatus.setText(rs2.getString("status"));
			}
			else
			{
				othersAccountClub.setText("-----");
				othersAccountStatus.setText("-----");
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
	 * Logout others account when "othersLogout" button is pressed
	 */
	@FXML
	void othersLogoutAction(ActionEvent event) throws IOException
	{
		Stage stage = (Stage) othersLogout.getScene().getWindow();
		Parent root = FXMLLoader.load(getClass().getResource("/application/home/HomeScreen.fxml"));
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
}