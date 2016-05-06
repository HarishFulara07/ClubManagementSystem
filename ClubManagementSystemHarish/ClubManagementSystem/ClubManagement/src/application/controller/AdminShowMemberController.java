package application.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class AdminShowMemberController implements Initializable{
	@FXML
	private Button showMemberBack;
	@FXML
	private Text showMemberName, showMemberSex, showMemberRollNum, showMemberProfession, showMemberEmail,
				showMemberClub, showMemberStatus;
	
	/*
	 * Logout admin's account when "showMemberBack" button is pressed
	 */
	@FXML
	void showMemberBackAction(ActionEvent event) throws IOException
	{
		Stage stage = (Stage) showMemberBack.getScene().getWindow();
		Parent root = FXMLLoader.load(getClass().getResource("/application/admin/account/AdminHomeScreen.fxml"));
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		showMemberName.setText(AdminHomeScreenController.member_first_name + " " + AdminHomeScreenController.member_last_name);
		showMemberSex.setText(AdminHomeScreenController.member_sex);
		
		if(AdminHomeScreenController.member_roll_num.equals("0"))
		{
			showMemberRollNum.setText("-----");
		}
		else
		{
			showMemberRollNum.setText(AdminHomeScreenController.member_roll_num);
		}
		showMemberProfession.setText(AdminHomeScreenController.member_profession);
		showMemberEmail.setText(AdminHomeScreenController.member_mail);
		showMemberClub.setText(AdminHomeScreenController.member_club);
		showMemberStatus.setText(AdminHomeScreenController.member_status);
	}
}
