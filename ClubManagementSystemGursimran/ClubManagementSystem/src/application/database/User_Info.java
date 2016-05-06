package application.database;

import java.util.ArrayList;

public class User_Info
{
	public String first_name,
			      last_name,
			      sex,
			      profession,
			      email;
	public int roll_number;
	
	/*
	*  As one person may be MEMBER / ADMIN of many clubs
	*/
	public ArrayList<String> club_name, discipline, description, status;
	
	public User_Info() {
	super();
	
	club_name = new ArrayList<>();
	discipline = new ArrayList<>();
	description = new ArrayList<>();
	status = new ArrayList<>();
	}
}
