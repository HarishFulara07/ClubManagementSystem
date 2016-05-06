package application.main;
	
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Date;

import application.global.MySQL;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			primaryStage.setTitle("IIIT-Delhi Club Management System");
			Parent root = FXMLLoader.load(getClass().getResource("/application/home/Home_Screen.fxml"));
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void sanitizeEvents() throws SQLException
	{
		Date date = new Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		
		String query = "DELETE FROM " + MySQL.TABLE_EVENT_DETAILS + " "
					 + "WHERE event_end_time < \"" + timestamp + "\"";
				
		
		MySQL.connect();
		Statement statement = MySQL.getStatement();
		statement.executeUpdate(query);
		MySQL.disconnect();
	}
	
	public static void main(String[] args) throws SQLException {
//		MySQL.deleteAllTables();
		MySQL.createAllTables();
		sanitizeEvents();
		launch(args);
	}
}
