package application.global;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MySQL {
	// JDBC driver name and database URL
	private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
		
	//  Database credentials
	private static final String USER = "root";
	private static final String PASS = "password";
	private static final String DATABASE_NAME = "club_management_system";
	private static final String DB_URL = "jdbc:mysql://localhost/" + DATABASE_NAME;
	
	private static Connection connection = null;
	
	
	public static void connect ()
	{
		try
		{
			if (connection != null && !connection.isClosed()) {
				// Don't need to do anything
				return;
			}
			
			// Register JDBC driver
			Class.forName (JDBC_DRIVER);
			
			// Open a connection
			connection = DriverManager.getConnection (DB_URL, USER, PASS);
		}
		catch (SQLException se)
		{
			//Handle errors for JDBC
			System.out.println ("JDBC Error !!");
			se.printStackTrace();
		}
		catch (Exception e)
		{
			//Handle errors for Class.forName
			System.out.println ("Class.forName Error !!");
			e.printStackTrace();
		}
	}
	
	public static void disconnect () {
		try
		{
			if (connection == null || connection.isClosed()) {
				// Don't need to do anything
				return;
			}
			
			connection.close();
		}
		catch (SQLException se)
		{
			System.out.println ("SQL Exception: disconnect!!");
			se.printStackTrace();
		}
		catch (Exception e)
		{
			System.out.println ("Exception in disconnect !!");
			e.printStackTrace();
		}
	}
	
	public static Statement getStatement () {
		Statement STM = null;
		
		try
		{
			if (connection == null || connection.isClosed()) {
				System.out.println ("There is no valid Connection available to the Server. Connect First");
				
				// To find out where the problem occurred throw an exception
				new SQLException().printStackTrace();
				
				return null;
			}
			
			STM = connection.createStatement();
		}
		catch (SQLException se)
		{
			System.out.println ("SQL Exception: disconnect!!");
			se.printStackTrace();
		}
		catch (Exception e)
		{
			System.out.println ("Exception in disconnect !!");
			e.printStackTrace();
		}
		return STM;
	}
	
	/* *********************************************************************************************/
	
	// Following functions must be used after a Connection has
	// been made. This is the responsibility of the developer
	
	public static boolean doesTableExist (String tableName) {
		Statement STM = null;
		ResultSet RS = null;
		String sqlQuery = null;
		String q = "\"";
		int NOR;						// Number of Rows
		boolean returnVal = false;
		
		try {
			sqlQuery = "SHOW TABLES LIKE " + q + tableName + q;		
			
			STM = getStatement();
			RS = STM.executeQuery (sqlQuery);
			
			RS.last();
			NOR = RS.getRow();
			RS.first();
			
			if (NOR == 0)
				returnVal = false;		// Table doesn't exist
			else
			{
//				System.out.println (tableName + " exists ...");
				returnVal = true;				
			}
		}
		catch (SQLException se)
		{
			System.out.println ("SQL Exception: disconnect!!");
			se.printStackTrace();
		}
		catch (Exception e)
		{
			System.out.println ("Exception in disconnect !!");
			e.printStackTrace();
		}
		finally
		{
			try
			{
				if (STM != null)
					STM.close();
				if (RS != null)
					RS.close();
			}
			catch (SQLException se) {
			}
		}
		return returnVal;
	}
	
	/* **************************************************************************************************************************************
	 * **************************************************************************************************************************************
	 * ************************************************************************************************************************************** */
	
	// All Tables in the Schema
	public static final String TABLE_CLUB_INFO = "club_info";
	public static final String TABLE_USER_INFO = "user_info";
	public static final String TABLE_MEMBER_STATUS = "member_status";
	public static final String TABLE_CLUB_MEMBER_INFO = "club_member_info";
	public static final String TABLE_LOGIN_INFO = "login_info";
	public static final String TABLE_EVENT_DETAILS = "event_details";
	
	/*
	 * Methods to create individual tables
	 */
	private static void create_club_info () throws SQLException {
		String TABLE_NAME = TABLE_CLUB_INFO;
		String SCHEMA = "("
					  + "club_name VARCHAR(100),"
					  + "discipline VARCHAR(50),"
					  + "description VARCHAR(1000),"
					  + "PRIMARY KEY (club_name)"
					  + ")";
		
		Statement st = getStatement();
		st.executeUpdate("CREATE TABLE " + TABLE_NAME + " " + SCHEMA + ";");
		st.close();
	}
	
	private static void create_user_info () throws SQLException {
		String TABLE_NAME = TABLE_USER_INFO;
		String SCHEMA = "("
					  + "first_name VARCHAR(50), "
					  + "last_name VARCHAR(50), "
					  + "sex VARCHAR(10), "
					  + "profession VARCHAR(50), "
					  + "roll_number INT, "
					  + "email VARCHAR(256), "
					  + "PRIMARY KEY (email) "
					  + ")";
		
		Statement st = getStatement();
		st.executeUpdate("CREATE TABLE " + TABLE_NAME + " " + SCHEMA);
		st.close();
	}
	
	private static void create_member_status () throws SQLException {
		String TABLE_NAME = TABLE_MEMBER_STATUS;
		String SCHEMA = "("
					  + "status VARCHAR(50), "
					  + "PRIMARY KEY (status) "
					  + ")";
		
		Statement st = getStatement();
		st.executeUpdate("CREATE TABLE " + TABLE_NAME + " " + SCHEMA);
		st.executeUpdate("INSERT INTO " + TABLE_NAME + " VALUES ('NON_MEMBER')");
		st.executeUpdate("INSERT INTO " + TABLE_NAME + " VALUES ('MEMBER')");
		st.executeUpdate("INSERT INTO " + TABLE_NAME + " VALUES ('ADMIN')");
		st.close();
	}
	
	private static void create_club_member_info () throws SQLException {
		String TABLE_NAME = TABLE_CLUB_MEMBER_INFO;
		String SCHEMA = "("
					  + "club_name VARCHAR(100), "
					  + "status VARCHAR(50), "
					  + "email VARCHAR(256), "
					  + "PRIMARY KEY (club_name, status, email), "
					  + "FOREIGN KEY (club_name) REFERENCES " + TABLE_CLUB_INFO + "(club_name) "
					  + "ON UPDATE CASCADE "
					  + "ON DELETE CASCADE, "
					  + "FOREIGN KEY (status) REFERENCES " + TABLE_MEMBER_STATUS + "(status) "
					  + "ON UPDATE CASCADE "
					  + "ON DELETE CASCADE "
					  + ")";
		
		Statement st = getStatement();
		st.executeUpdate("CREATE TABLE " + TABLE_NAME + " " + SCHEMA);
		st.close();
	}
	
	private static void create_login_info () throws SQLException {
		String TABLE_NAME = TABLE_LOGIN_INFO;
		String SCHEMA = "("
					  + "email VARCHAR(256), "
					  + "password VARCHAR(50), "
					  + "PRIMARY KEY (email), "
					  + "FOREIGN KEY (email) REFERENCES " + TABLE_USER_INFO + "(email) "
					  + "ON UPDATE CASCADE "
					  + "ON DELETE CASCADE "
					  + ")";
		
		Statement st = getStatement();
		st.executeUpdate("CREATE TABLE " + TABLE_NAME + " " + SCHEMA);
		st.close();
	}
	
	private static void create_event_details () throws SQLException {
		String TABLE_NAME = TABLE_EVENT_DETAILS;
		String SCHEMA = "("
					  + "event_id INT AUTO_INCREMENT, "
					  + "event_name VARCHAR(50), "
					  + "event_start_time TIMESTAMP, "
					  + "event_end_time TIMESTAMP, "
					  + "event_venue VARCHAR(50), "
					  + "description VARCHAR(1000), "
					  + "organising_club VARCHAR(100), "
					  + "organisers VARCHAR(1000), "
					  + "team_size INT, "
					  + "budget INT, "
					  + "prize VARCHAR(1000), "
					  + "PRIMARY KEY (event_id), "
					  + "FOREIGN KEY (organising_club) REFERENCES " + TABLE_CLUB_INFO + "(club_name) "
					  + "ON UPDATE CASCADE "
					  + "ON DELETE CASCADE "
					  + ")";
		
		Statement st = getStatement();
		st.executeUpdate("CREATE TABLE " + TABLE_NAME + " " + SCHEMA);
		st.close();
	}
	
	public static void createAllTables () {
		connect();
		try
		{
			if (!doesTableExist(TABLE_CLUB_INFO))
				create_club_info ();
			
			if (!doesTableExist(TABLE_USER_INFO))
				create_user_info();
			
			if (!doesTableExist(TABLE_MEMBER_STATUS))
				create_member_status();
			
			if (!doesTableExist(TABLE_CLUB_MEMBER_INFO))
				create_club_member_info();
			
			if (!doesTableExist(TABLE_LOGIN_INFO))
				create_login_info();
			
			if (!doesTableExist(TABLE_EVENT_DETAILS))
				create_event_details();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		disconnect();
	}
	
	public static void deleteAllTables() {
		String table_name = null;
		String query = null;
		
		connect();
		try
		{
			Statement statement = getStatement();
			
			table_name = TABLE_EVENT_DETAILS;
			query = "DROP TABLE " + table_name;
			if (doesTableExist(table_name))
				statement.executeUpdate(query);
			
			table_name = TABLE_LOGIN_INFO;
			query = "DROP TABLE " + table_name;
			if (doesTableExist(table_name))
				statement.executeUpdate(query);
			
			table_name = TABLE_CLUB_MEMBER_INFO;
			query = "DROP TABLE " + table_name;
			if (doesTableExist(table_name))
				statement.executeUpdate(query);
			
			table_name = TABLE_MEMBER_STATUS;
			query = "DROP TABLE " + table_name;
			if (doesTableExist(table_name))
				statement.executeUpdate(query);
			
			table_name = TABLE_USER_INFO;
			query = "DROP TABLE " + table_name;
			if (doesTableExist(table_name))
				statement.executeUpdate(query);
			
			table_name = TABLE_CLUB_INFO;
			query = "DROP TABLE " + table_name;
			if (doesTableExist(table_name))
				statement.executeUpdate(query);
			
			statement.close();
		}
		catch (SQLException ex)
		{
			ex.printStackTrace();
		}
		disconnect();
	}
	
	/*public static void main (String[] args) {
		connect();
		deleteAllTables();
		createAllTables ();
		disconnect();
	}*/
}
