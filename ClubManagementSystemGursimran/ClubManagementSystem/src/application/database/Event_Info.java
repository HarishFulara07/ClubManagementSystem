package application.database;

public class Event_Info
{
	public String event_name = null,
				  description = null,
				  organising_club = null,
				  organisers = null,
				  prize = null;
	public int team_size,
			   budget;
	public String event_start_time = null,
			  	  event_end_time = null,
			  	  event_venue = null;
	
	public Event_Info(String event_start_time, String event_end_time, String event_venue)
	{
		super();
		this.event_start_time = event_start_time;
		this.event_end_time = event_end_time;
		this.event_venue = event_venue;
	}
}
