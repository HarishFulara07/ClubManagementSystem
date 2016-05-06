package application.admin.account;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import application.database.User_Club_Info;
import application.database.User_Info;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Club_Info_Table_Controller implements Initializable {

	@FXML
	private TableView<User_Club_Info> table;
	@FXML
	private TableColumn<User_Club_Info, String> club_name,
										  	  discipline,
										  	  description,
										  	  email,
										  	  first_name,
										  	  last_name,
										  	  sex,
										  	  profession,
										  	  roll_number;
	@FXML
	private Button backButton;
	
	private User_Info ui;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		club_name.setCellValueFactory (cellData -> cellData.getValue().club_name_property());
		discipline.setCellValueFactory (cellData -> cellData.getValue().discipline_property());
		description.setCellValueFactory (cellData -> cellData.getValue().description_property());
		email.setCellValueFactory (cellData -> cellData.getValue().email_property());
		first_name.setCellValueFactory (cellData -> cellData.getValue().first_name_property());
		last_name.setCellValueFactory (cellData -> cellData.getValue().last_name_property());
		sex.setCellValueFactory (cellData -> cellData.getValue().sex_property());
		profession.setCellValueFactory (cellData -> cellData.getValue().profession_property());
		roll_number.setCellValueFactory (cellData -> cellData.getValue().roll_number_property());
	}

	public void setData (ArrayList<User_Club_Info> data, User_Info ui) {
		table.setItems (FXCollections.observableArrayList (data));
		this.ui = ui;
	}
	
	@FXML
	public void onBackButton (ActionEvent e) throws IOException {
		FXMLLoader loader = new FXMLLoader (getClass().getResource("/application/admin/account/Admin_Home_Screen.fxml"));
		Stage stage = new Stage(StageStyle.DECORATED);
		stage.setScene (new Scene (loader.load()));
		Admin_Home_Screen_Controller home_screen = loader.<Admin_Home_Screen_Controller>getController();
		home_screen.setDataFromUserInfo(ui);
		stage.show();
		
		stage = (Stage) backButton.getScene().getWindow();
		stage.close();
	}
}
